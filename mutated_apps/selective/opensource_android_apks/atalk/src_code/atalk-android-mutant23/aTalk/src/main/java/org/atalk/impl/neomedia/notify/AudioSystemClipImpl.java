/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.notify;

import org.atalk.impl.neomedia.codec.audio.speex.SpeexResampler;
import org.atalk.impl.neomedia.device.AudioSystem;
import org.atalk.service.audionotifier.AbstractSCAudioClip;
import org.atalk.service.audionotifier.AudioNotifierService;

import java.io.IOException;
import java.io.InputStream;

import javax.media.*;
import javax.media.format.AudioFormat;

import timber.log.Timber;

/**
 * Implementation of SCAudioClip using PortAudio.
 *
 * @author Damyian Minkov
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public class AudioSystemClipImpl extends AbstractSCAudioClip
{
    /**
     * The default length of {@link #bufferData}.
     */
    private static final int DEFAULT_BUFFER_DATA_LENGTH = 8 * 1024;

    /**
     * The minimum duration in milliseconds to be assumed for the audio streams played by
     * <tt>AudioSystemClipImpl</tt> in order to ensure that they are played back long enough to be heard.
     */
    private static final long MIN_AUDIO_STREAM_DURATION = 200;

    private final AudioSystem audioSystem;

    private Buffer buffer;

    private byte[] bufferData;

    private final boolean playback;

    private Renderer renderer;

    /**
     * Creates the audio clip and initializes the listener used from the loop timer.
     *
     * @param url the URL pointing to the audio file
     * @param audioNotifier the audio notify service
     * @param playback to use playback or notification device
     * @throws IOException cannot audio clip with supplied URL.
     */
    public AudioSystemClipImpl(String url, AudioNotifierService audioNotifier, AudioSystem audioSystem, boolean playback)
            throws IOException
    {
        super(url, audioNotifier);
        this.audioSystem = audioSystem;
        this.playback = playback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void enterRunInPlayThread()
    {
        buffer = new Buffer();
        bufferData = new byte[DEFAULT_BUFFER_DATA_LENGTH];
        buffer.setData(bufferData);
        renderer = audioSystem.createRenderer(playback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void exitRunInPlayThread()
    {
        buffer = null;
        bufferData = null;
        renderer = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void exitRunOnceInPlayThread()
    {
        try {
            renderer.stop();
        } finally {
            renderer.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected boolean runOnceInPlayThread()
    {
        if (renderer == null || buffer == null) {
            return false;
        }

        InputStream audioStream = null;
        try {
            audioStream = audioSystem.getAudioInputStream(uri);
        } catch (IOException ioex) {
            Timber.e(ioex, "Failed to get audio stream %s", uri);
        }
        if (audioStream == null)
            return false;

        Codec resampler = null;
        boolean success = true;
        AudioFormat audioStreamFormat = null;
        int audioStreamLength = 0;
        long rendererProcessStartTime = 0;
        try {
            Format rendererFormat = audioStreamFormat = audioSystem.getFormat(audioStream);
            if (rendererFormat == null)
                return false;

            Format resamplerFormat = null;
            if (renderer.setInputFormat(rendererFormat) == null) {
                /*
                 * Try to negotiate a resampling of the audioStream to one of the formats supported
                 * by the renderer.
                 */
                resampler = new SpeexResampler();
                resamplerFormat = rendererFormat;
                resampler.setInputFormat(resamplerFormat);

                Format[] supportedResamplerFormats = resampler.getSupportedOutputFormats(resamplerFormat);
                for (Format supportedRendererFormat : renderer.getSupportedInputFormats()) {
                    for (Format supportedResamplerFormat : supportedResamplerFormats) {
                        if (supportedRendererFormat.matches(supportedResamplerFormat)) {
                            rendererFormat = supportedRendererFormat;
                            resampler.setOutputFormat(rendererFormat);
                            renderer.setInputFormat(rendererFormat);
                            break;
                        }
                    }
                }
            }
            Buffer rendererBuffer = buffer;
            Buffer resamplerBuffer;
            rendererBuffer.setFormat(rendererFormat);
            if (resampler == null)
                resamplerBuffer = null;
            else {
                resamplerBuffer = new Buffer();
                int bufferDataLength = DEFAULT_BUFFER_DATA_LENGTH;
                if (resamplerFormat instanceof AudioFormat) {
                    AudioFormat af = (AudioFormat) resamplerFormat;
                    int frameSize = af.getSampleSizeInBits() / 8 * af.getChannels();
                    bufferDataLength = bufferDataLength / frameSize * frameSize;
                }
                bufferData = new byte[bufferDataLength];
                resamplerBuffer.setData(bufferData);
                resamplerBuffer.setFormat(resamplerFormat);
                resampler.open();
            }

            try {
                renderer.open();
                renderer.start();

                int bufferLength;
                while (isStarted() && ((bufferLength = audioStream.read(bufferData)) != -1)) {
                    audioStreamLength += bufferLength;
                    if (resampler == null) {
                        rendererBuffer.setLength(bufferLength);
                        rendererBuffer.setOffset(0);
                    }
                    else {
                        resamplerBuffer.setLength(bufferLength);
                        resamplerBuffer.setOffset(0);
                        rendererBuffer.setLength(0);
                        rendererBuffer.setOffset(0);
                        resampler.process(resamplerBuffer, rendererBuffer);
                    }

                    int rendererProcess;
                    if (rendererProcessStartTime == 0)
                        rendererProcessStartTime = System.currentTimeMillis();
                    do {
                        rendererProcess = renderer.process(rendererBuffer);
                        if (rendererProcess == Renderer.BUFFER_PROCESSED_FAILED) {
                            Timber.e("Failed to render audio stream %s", uri);
                            success = false;
                            break;
                        }
                    }
                    while ((rendererProcess & Renderer.INPUT_BUFFER_NOT_CONSUMED)
                            == Renderer.INPUT_BUFFER_NOT_CONSUMED);
                }
            } catch (IOException ioex) {
                Timber.e(ioex, "Failed to read from audio stream %s", uri);
                success = false;
            } catch (ResourceUnavailableException ruex) {
                Timber.e(ruex, "Failed to open %s", renderer.getClass().getName());
                success = false;
            }
        } catch (ResourceUnavailableException ruex) {
            if (resampler != null) {
                Timber.e("ruex, Failed to open %s", resampler.getClass().getName());
                success = false;
            }
        } finally {
            try {
                audioStream.close();
            } catch (IOException ioex) {
                /*
                 * The audio stream failed to close but it doesn't mean the URL will fail to open
                 * again so ignore the exception.
                 */
            }
            if (resampler != null)
                resampler.close();

            /*
             * XXX We do not know whether the Renderer implementation of the stop method will wait
             * for the playback to complete.
             */
            if (success && (audioStreamFormat != null) && (audioStreamLength > 0)
                    && (rendererProcessStartTime > 0) && isStarted()) {
                long audioStreamDuration = (audioStreamFormat.computeDuration(audioStreamLength) + 999999) / 1000000;
                if (audioStreamDuration > 0) {
                    /*
                     * XXX The estimation is not accurate because we do not know, for example, how
                     * much the Renderer may be buffering before it starts the playback.
                     */
                    audioStreamDuration += MIN_AUDIO_STREAM_DURATION;
                    boolean interrupted = false;
                    synchronized (sync) {
                        while (isStarted()) {
                            long timeout = System.currentTimeMillis() - rendererProcessStartTime;
                            if ((timeout >= audioStreamDuration) || (timeout <= 0)) {
                                break;
                            }
                            else {
                                try {
                                    sync.wait(timeout);
                                } catch (InterruptedException ie) {
                                    interrupted = true;
                                }
                            }
                        }
                    }
                    if (interrupted)
                        Thread.currentThread().interrupt();
                }
            }
        }
        return success;
    }
}
