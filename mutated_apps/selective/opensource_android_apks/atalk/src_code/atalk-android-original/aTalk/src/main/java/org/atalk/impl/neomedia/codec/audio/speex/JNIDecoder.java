/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.speex;

import net.sf.fmj.media.AbstractCodec;

import org.atalk.impl.neomedia.codec.AbstractCodec2;
import org.atalk.service.neomedia.codec.Constants;

import javax.media.*;
import javax.media.format.AudioFormat;

/**
 * Implements a Speex decoder and RTP depacketizer using the native Speex library.
 *
 * @author Lubomir Marinov
 */
public class JNIDecoder extends AbstractCodec2
{

    /**
     * The list of <tt>Format</tt>s of audio data supported as input by <tt>JNIDecoder</tt> instances.
     */
    private static final Format[] SUPPORTED_INPUT_FORMATS;

    /**
     * The list of <tt>Format</tt>s of audio data supported as output by <tt>JNIDecoder</tt> instances.
     */
    private static final Format[] SUPPORTED_OUTPUT_FORMATS = new Format[]{
            new AudioFormat(
                    AudioFormat.LINEAR,
                    Format.NOT_SPECIFIED,
                    16,
                    1,
                    AudioFormat.LITTLE_ENDIAN,
                    AudioFormat.SIGNED,
                    Format.NOT_SPECIFIED,
                    Format.NOT_SPECIFIED,
                    Format.byteArray)
    };

    static {
        Speex.assertSpeexIsFunctional();
        double[] SUPPORTED_INPUT_SAMPLE_RATES = JNIEncoder.SUPPORTED_INPUT_SAMPLE_RATES;
        int supportedInputCount = SUPPORTED_INPUT_SAMPLE_RATES.length;
        SUPPORTED_INPUT_FORMATS = new Format[supportedInputCount];
        for (int i = 0; i < supportedInputCount; i++) {
            SUPPORTED_INPUT_FORMATS[i] = new AudioFormat(
                    Constants.SPEEX_RTP,
                    SUPPORTED_INPUT_SAMPLE_RATES[i],
                    Format.NOT_SPECIFIED,
                    1,
                    AudioFormat.LITTLE_ENDIAN,
                    AudioFormat.SIGNED,
                    Format.NOT_SPECIFIED,
                    Format.NOT_SPECIFIED,
                    Format.byteArray);
        }
    }

    /**
     * The pointer to the native <tt>SpeexBits</tt> from which the native Speex decoder (i.e.
     * {@link #state}) reads the encoded audio data.
     */
    private long bits = 0;

    /**
     * The duration in nanoseconds of an output <tt>Buffer</tt> produced by this <tt>Codec</tt>.
     */
    private long duration = 0;

    /**
     * The number of bytes from an input <tt>Buffer</tt> that this <tt>Codec</tt> processes in one
     * call of its {@link #process(Buffer, Buffer)}.
     */
    private int frameSize = 0;

    /**
     * The sample rate configured into {@link #state}.
     */
    private int sampleRate = 0;

    /**
     * The native Speex decoder represented by this instance.
     */
    private long state = 0;

    /**
     * Initializes a new <tt>JNIDecoder</tt> instance.
     */
    public JNIDecoder()
    {
        super("Speex JNI Decoder", AudioFormat.class, SUPPORTED_OUTPUT_FORMATS);
        inputFormats = SUPPORTED_INPUT_FORMATS;
    }

    /**
     * @see AbstractCodec2#doClose()
     */
    @Override
    protected void doClose()
    {
        // state
        if (state != 0) {
            Speex.speex_decoder_destroy(state);
            state = 0;
            sampleRate = 0;
            frameSize = 0;
            duration = 0;
        }
        // bits
        Speex.speex_bits_destroy(bits);
        bits = 0;
    }

    /**
     * Opens this <tt>Codec</tt> and acquires the resources that it needs to operate. A call to
     * {@link PlugIn#open()} on this instance will result in a call to <tt>doOpen</tt> only if
     * {@link AbstractCodec#opened} is <tt>false</tt>. All required input and/or output formats are
     * assumed to have been set on this <tt>Codec</tt> before <tt>doOpen</tt> is called.
     *
     * @throws ResourceUnavailableException if any of the resources that this <tt>Codec</tt> needs
     * to operate cannot be acquired
     * @see AbstractCodec2#doOpen()
     */
    @Override
    protected void doOpen()
            throws ResourceUnavailableException
    {
        bits = Speex.speex_bits_init();
        if (bits == 0)
            throw new ResourceUnavailableException("speex_bits_init");
    }

    /**
     * Decodes Speex media from a specific input <tt>Buffer</tt>
     *
     * @param inputBuffer input <tt>Buffer</tt>
     * @param outputBuffer output <tt>Buffer</tt>
     * @return <tt>BUFFER_PROCESSED_OK</tt> if <tt>inBuffer</tt> has been successfully processed
     * @see AbstractCodec2#doProcess(Buffer, Buffer)
     */
    @Override
    protected int doProcess(Buffer inputBuffer, Buffer outputBuffer)
    {
        Format inputFormat = inputBuffer.getFormat();

        if ((inputFormat != null) && (inputFormat != this.inputFormat)
                && !inputFormat.equals(this.inputFormat)) {
            if (null == setInputFormat(inputFormat))
                return BUFFER_PROCESSED_FAILED;
        }
        inputFormat = this.inputFormat;

        /*
         * Make sure that the native Speex decoder which is represented by this instance is
         * configured to work with the inputFormat.
         */
        AudioFormat inputAudioFormat = (AudioFormat) inputFormat;
        int inputSampleRate = (int) inputAudioFormat.getSampleRate();

        if ((state != 0) && (sampleRate != inputSampleRate)) {
            Speex.speex_decoder_destroy(state);
            state = 0;
            sampleRate = 0;
            frameSize = 0;
        }
        if (state == 0) {
            long mode = Speex.speex_lib_get_mode((inputSampleRate == 16000)
                    ? Speex.SPEEX_MODEID_WB
                    : (inputSampleRate == 32000) ? Speex.SPEEX_MODEID_UWB : Speex.SPEEX_MODEID_NB);

            if (mode == 0)
                return BUFFER_PROCESSED_FAILED;
            state = Speex.speex_decoder_init(mode);
            if (state == 0)
                return BUFFER_PROCESSED_FAILED;
            if (Speex.speex_decoder_ctl(state, Speex.SPEEX_SET_ENH, 1) != 0)
                return BUFFER_PROCESSED_FAILED;
            if (Speex.speex_decoder_ctl(state, Speex.SPEEX_SET_SAMPLING_RATE, inputSampleRate) != 0)
                return BUFFER_PROCESSED_FAILED;

            int frameSize = Speex.speex_decoder_ctl(state, Speex.SPEEX_GET_FRAME_SIZE);
            if (frameSize < 0)
                return BUFFER_PROCESSED_FAILED;

            sampleRate = inputSampleRate;
            this.frameSize = frameSize * 2 /* (sampleSizeInBits / 8) */;
            duration = (frameSize * 1000 * 1000000) / sampleRate;
        }

        /* Read the encoded audio data from inputBuffer into the SpeexBits. */
        int inputLength = inputBuffer.getLength();

        if (inputLength > 0) {
            byte[] input = (byte[]) inputBuffer.getData();
            int inputOffset = inputBuffer.getOffset();

            Speex.speex_bits_read_from(bits, input, inputOffset, inputLength);
            inputLength = 0;
            inputBuffer.setLength(inputLength);
            inputBuffer.setOffset(inputOffset + inputLength);
        }

        /* At long last, do the actual decoding. */
        int outputLength = this.frameSize;
        boolean inputBufferNotConsumed;

        if (outputLength > 0) {
            byte[] output = validateByteArraySize(outputBuffer, outputLength, false);

            if (0 == Speex.speex_decode_int(state, bits, output, 0)) {
                outputBuffer.setDuration(duration);
                outputBuffer.setFormat(getOutputFormat());
                outputBuffer.setLength(outputLength);
                outputBuffer.setOffset(0);
                inputBufferNotConsumed = (Speex.speex_bits_remaining(bits) > 0);
            }
            else {
                outputBuffer.setLength(0);
                discardOutputBuffer(outputBuffer);
                inputBufferNotConsumed = false;
            }
        }
        else {
            outputBuffer.setLength(0);
            discardOutputBuffer(outputBuffer);
            inputBufferNotConsumed = false;
        }

        if ((inputLength < 1) && !inputBufferNotConsumed)
            return BUFFER_PROCESSED_OK;
        else
            return BUFFER_PROCESSED_OK | INPUT_BUFFER_NOT_CONSUMED;
    }

    /**
     * Get all supported output <tt>Format</tt>s.
     *
     * @param inputFormat input <tt>Format</tt> to determine corresponding output <tt>Format/tt>s
     * @return array of supported <tt>Format</tt>
     * @see AbstractCodec2#getMatchingOutputFormats(Format)
     */
    @Override
    protected Format[] getMatchingOutputFormats(Format inputFormat)
    {
        AudioFormat inputAudioFormat = (AudioFormat) inputFormat;

        return new Format[]{
                new AudioFormat(
                        AudioFormat.LINEAR,
                        inputAudioFormat.getSampleRate(),
                        16,
                        1,
                        AudioFormat.LITTLE_ENDIAN,
                        AudioFormat.SIGNED,
                        Format.NOT_SPECIFIED,
                        Format.NOT_SPECIFIED,
                        Format.byteArray)
        };
    }

    /**
     * Sets the <tt>Format</tt> of the media data to be input for processing in this <tt>Codec</tt>.
     *
     * @param format the <tt>Format</tt> of the media data to be input for processing in this <tt>Codec</tt>
     * @return the <tt>Format</tt> of the media data to be input for processing in this
     * <tt>Codec</tt> if <tt>format</tt> is compatible with this <tt>Codec</tt>; otherwise, <tt>null</tt>
     * @see AbstractCodec2#setInputFormat(Format)
     */
    @Override
    public Format setInputFormat(Format format)
    {
        Format inputFormat = super.setInputFormat(format);

        if (inputFormat != null) {
            double outputSampleRate;
            int outputChannels;

            if (outputFormat == null) {
                outputSampleRate = Format.NOT_SPECIFIED;
                outputChannels = Format.NOT_SPECIFIED;
            }
            else {
                AudioFormat outputAudioFormat = (AudioFormat) outputFormat;

                outputSampleRate = outputAudioFormat.getSampleRate();
                outputChannels = outputAudioFormat.getChannels();
            }

            AudioFormat inputAudioFormat = (AudioFormat) inputFormat;
            double inputSampleRate = inputAudioFormat.getSampleRate();
            int inputChannels = inputAudioFormat.getChannels();

            if ((outputSampleRate != inputSampleRate)
                    || (outputChannels != inputChannels)) {
                setOutputFormat(
                        new AudioFormat(
                                AudioFormat.LINEAR,
                                inputSampleRate,
                                16,
                                inputChannels,
                                AudioFormat.LITTLE_ENDIAN,
                                AudioFormat.SIGNED,
                                Format.NOT_SPECIFIED,
                                Format.NOT_SPECIFIED,
                                Format.byteArray));
            }
        }
        return inputFormat;
    }
}
