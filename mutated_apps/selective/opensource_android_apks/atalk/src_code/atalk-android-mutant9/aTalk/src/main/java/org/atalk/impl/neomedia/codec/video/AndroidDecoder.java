/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.video;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;

import org.atalk.android.util.java.awt.Dimension;
import org.atalk.impl.neomedia.device.util.PreviewSurfaceProvider;
import org.atalk.impl.neomedia.format.ParameterizedVideoFormat;
import org.atalk.impl.neomedia.format.VideoMediaFormatImpl;
import org.atalk.service.libjitsi.LibJitsi;
import org.atalk.service.neomedia.codec.Constants;

import javax.media.Format;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;

/**
 * Video decoder based on <tt>MediaCodec</tt>.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class AndroidDecoder extends AndroidCodec
{
    /**
     * Name of configuration property that enables hardware decoding.
     */
    public static final String HW_DECODING_ENABLE_PROPERTY = "neomedia.android.hw_decode";

    /**
     * Name of configuration property that enables decoding directly into provided <tt>Surface</tt> object.
     */
    public static final String DIRECT_SURFACE_DECODE_PROPERTY = "neomedia.android.surface_decode";

    /**
     * Remembers if this instance is using decoding into the <tt>Surface</tt>.
     */
    private final boolean useOutputSurface;

    /**
     * Output video size.
     */
    private Dimension outputSize;

    /**
     * Surface provider used to obtain <tt>Surface</tt> object that will be used for decoded video rendering.
     */
    public static PreviewSurfaceProvider renderSurfaceProvider;

    /**
     * Returns <tt>true</tt> if hardware decoding is supported and enabled.
     *
     * @return <tt>true</tt> if hardware decoding is supported and enabled.
     */
    public static boolean isHwDecodingEnabled()
    {
        // boolean supported = AndroidUtils.hasAPI(16);
        return LibJitsi.getConfigurationService().getBoolean(HW_DECODING_ENABLE_PROPERTY, false);
    }

    /**
     * Returns <tt>true</tt> if decoding into the <tt>Surface</tt> is enabled.
     *
     * @return <tt>true</tt> if decoding into the <tt>Surface</tt> is enabled.
     */
    public static boolean isDirectSurfaceEnabled()
    {
        return isHwDecodingEnabled()
                && LibJitsi.getConfigurationService().getBoolean(DIRECT_SURFACE_DECODE_PROPERTY, true);
    }

    /**
     * Input formats list.
     */
    private static final VideoFormat[] INPUT_FORMATS = new VideoFormat[]{
            new VideoFormat(Constants.VP9),
            new VideoFormat(Constants.VP8),
            new VideoFormat(Constants.H264),
            new ParameterizedVideoFormat(Constants.H264, VideoMediaFormatImpl.H264_PACKETIZATION_MODE_FMTP, "0")};

    static Format[] getOutputFormats()
    {
        if (!isHwDecodingEnabled())
            return EMPTY_FORMATS;

        if (isDirectSurfaceEnabled()) {
            return new Format[]{new VideoFormat(Constants.ANDROID_SURFACE)};
        }
        else {
            return new Format[]{new YUVFormat(
                    /* size */null,
                    /* maxDataLength */Format.NOT_SPECIFIED, Format.byteArray,
                    /* frameRate */Format.NOT_SPECIFIED, YUVFormat.YUV_420,
                    /* strideY */Format.NOT_SPECIFIED,
                    /* strideUV */Format.NOT_SPECIFIED,
                    /* offsetY */Format.NOT_SPECIFIED,
                    /* offsetU */Format.NOT_SPECIFIED,
                    /* offsetV */Format.NOT_SPECIFIED)};
        }
    }

    /**
     * Creates new instance of <tt>AndroidDecoder</tt>.
     */
    public AndroidDecoder()
    {
        super("AndroidDecoder", VideoFormat.class, getOutputFormats(), false);

        useOutputSurface = isDirectSurfaceEnabled();
        if (isHwDecodingEnabled())
            inputFormats = INPUT_FORMATS;
        else
            inputFormats = EMPTY_FORMATS;

        inputFormat = null;
        outputFormat = null;
    }

    /**
     * Gets the matching output formats for a specific format.
     *
     * @param inputFormat input format
     * @return array of formats matching input format
     */
    @Override
    protected Format[] getMatchingOutputFormats(Format inputFormat)
    {
        if (!(inputFormat instanceof VideoFormat))
            return EMPTY_FORMATS;

        VideoFormat inputVideoFormat = (VideoFormat) inputFormat;
        if (useSurface()) {
            return new VideoFormat[]{
                    // new SurfaceFormat(inputVideoFormat.getSize()),
                    new VideoFormat(Constants.ANDROID_SURFACE, inputVideoFormat.getSize(),
                            Format.NOT_SPECIFIED, Surface.class, Format.NOT_SPECIFIED)};
        }
        else {
            return new VideoFormat[]{new YUVFormat(
                    /* size */inputVideoFormat.getSize(),
                    /* maxDataLength */Format.NOT_SPECIFIED, Format.byteArray,
                    /* frameRate */Format.NOT_SPECIFIED, YUVFormat.YUV_420,
                    /* strideY */Format.NOT_SPECIFIED,
                    /* strideUV */Format.NOT_SPECIFIED,
                    /* offsetY */Format.NOT_SPECIFIED,
                    /* offsetU */Format.NOT_SPECIFIED,
                    /* offsetV */Format.NOT_SPECIFIED)};
        }
    }

    /**
     * Sets the <tt>Format</tt> in which this <tt>Codec</tt> is to output media data.
     *
     * @param format the <tt>Format</tt> in which this <tt>Codec</tt> is to output media data
     * @return the <tt>Format</tt> in which this <tt>Codec</tt> is currently configured to output
     * media data or <tt>null</tt> if <tt>format</tt> was found to be incompatible with this
     * <tt>Codec</tt>
     */
    @Override
    public Format setOutputFormat(Format format)
    {
        if (!(format instanceof VideoFormat) || (matches(format,
                getMatchingOutputFormats(inputFormat)) == null))
            return null;

        VideoFormat videoFormat = (VideoFormat) format;
        if (format instanceof YUVFormat) {
            YUVFormat yuvFormat = (YUVFormat) videoFormat;
            outputFormat = new YUVFormat(
                    /* size */outputSize,
                    /* maxDataLength */videoFormat.getMaxDataLength(), Format.byteArray,
                    /* frameRate */videoFormat.getFrameRate(), YUVFormat.YUV_420,
                    /* strideY */yuvFormat.getStrideY(),
                    /* strideUV */yuvFormat.getStrideUV(),
                    /* offsetY */yuvFormat.getOffsetY(),
                    /* offsetU */yuvFormat.getOffsetU(),
                    /* offsetV */yuvFormat.getOffsetV());
        }
        else {
            outputFormat = new VideoFormat(videoFormat.getEncoding(), outputSize,
                    videoFormat.getMaxDataLength(), videoFormat.getDataType(),
                    videoFormat.getFrameRate());
        }
        // Return the selected outputFormat
        return outputFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean useSurface()
    {
        return useOutputSurface;
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void configureMediaCodec(MediaCodec codec, String codecType)
    {
        // VideoFormat vformat = (VideoFormat) inputFormat;
        MediaFormat format = MediaFormat.createVideoFormat(codecType, 176, 144);
        // vformat.getSize().width, vformat.getSize().height);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 8000000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 30);

        // Select color format
        int colorFormat = getColorFormat();
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
        codec.configure(format, useSurface() ? getSurface() : null, null, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Surface getSurface()
    {
        return renderSurfaceProvider.obtainObject().getSurface();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doClose()
    {
        super.doClose();
        renderSurfaceProvider.onObjectReleased();
    }

    @Override
    protected void onSizeDiscovered(Dimension dimension)
    {
        outputSize = dimension;
        setOutputFormat(outputFormat);
    }
}
