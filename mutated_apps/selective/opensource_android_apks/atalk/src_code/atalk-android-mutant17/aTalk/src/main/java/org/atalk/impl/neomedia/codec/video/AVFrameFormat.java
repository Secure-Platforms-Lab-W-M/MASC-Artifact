/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.video;

import org.atalk.android.util.java.awt.Dimension;

import javax.media.Format;
import javax.media.format.VideoFormat;

import timber.log.Timber;

/**
 * Implements a <tt>VideoFormat</tt> for a <tt>Buffer</tt> carrying <tt>AVFrame</tt> as its <tt>data</tt>. While the
 * <tt>AVFrameFormat</tt> class is not strictly necessary and <tt>VideoFormat</tt> could have be directly used, it is
 * conceived as an appropriate way to avoid possible matching with other <tt>VideoFormat</tt>s and a very obvious one.
 *
 * @author Lyubomir Marinov
 * @author Sebastien Vincent
 * @author Eng Chong Meng
 */
public class AVFrameFormat extends VideoFormat
{
    /**
     * The encoding of the <tt>AVFrameFormat</tt> instances.
     */
    public static final String AVFRAME = "AVFrame";

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The <tt>DeviceSystem</tt>-specific format represented by this instance. It is necessary in the cases of multiple
     * <tt>DeviceSystem</tt>-specific formats corresponding to one and the same native FFmpeg format in which JMF is
     * unable to differentiate them yet the device needs to know its <tt>DeviceSystem</tt>-specific format.
     */
    private int deviceSystemPixFmt;

    /**
     * The native FFmpeg format represented by this instance.
     */
    private int pixFmt;

    /**
     * Initializes a new <tt>AVFrameFormat</tt> instance with unspecified size, frame rate and FFmpeg colorspace.
     */
    public AVFrameFormat()
    {
        this(NOT_SPECIFIED, NOT_SPECIFIED);
    }

    public AVFrameFormat(Dimension size, float frameRate, int pixFmt)
    {
        this(size, frameRate, pixFmt, NOT_SPECIFIED);
    }

    /**
     * Initializes a new <tt>AVFrameFormat</tt> instance with specific size, frame rate and FFmpeg colorspace.
     *
     * @param size the <tt>Dimension</tt> of the new instance
     * @param frameRate the frame rate of the new instance
     * @param pixFmt the FFmpeg colorspace to be represented by the new instance
     * @param deviceSystemPixFmt the <tt>DeviceSystem</tt>-specific colorspace to be represented by the new instance
     */
    public AVFrameFormat(Dimension size, float frameRate, int pixFmt, int deviceSystemPixFmt)
    {
        super(AVFRAME, size, NOT_SPECIFIED, AVFrame.class, frameRate);

        if ((pixFmt == NOT_SPECIFIED) && (deviceSystemPixFmt != NOT_SPECIFIED)) {
            Timber.w(new Throwable(), "Specifying a device system-specific pixel format %08x without a matching FFmpeg pixel format may eventually lead to a failure.",
                    deviceSystemPixFmt & 0xffffffffL);
        }

        this.pixFmt = pixFmt;
        this.deviceSystemPixFmt = deviceSystemPixFmt;
    }

    /**
     * Initializes a new <tt>AVFrameFormat</tt> instance with a specific FFmpeg
     * colorspace and unspecified size and frame rate.
     *
     * @param pixFmt the FFmpeg colorspace to be represented by the new instance
     */
    public AVFrameFormat(int pixFmt)
    {
        this(pixFmt, NOT_SPECIFIED);
    }

    /**
     * Initializes a new <tt>AVFrameFormat</tt> instance with a specific FFmpeg
     * colorspace and unspecified size and frame rate.
     *
     * @param pixFmt the FFmpeg colorspace to be represented by the new instance
     * @param deviceSystemPixFmt the <tt>DeviceSystem</tt>-specific colorspace to be represented by the new instance
     */
    public AVFrameFormat(int pixFmt, int deviceSystemPixFmt)
    {
        this(null, NOT_SPECIFIED, pixFmt, deviceSystemPixFmt);
    }

    /**
     * Initializes a new <tt>AVFrameFormat</tt> instance which has the same properties as this instance.
     *
     * @return a new <tt>AVFrameFormat</tt> instance which has the same properties as this instance
     */
    @Override
    public Object clone()
    {
        AVFrameFormat f = new AVFrameFormat(getSize(), getFrameRate(), pixFmt, deviceSystemPixFmt);
        f.copy(this);
        return f;
    }

    /**
     * Copies the properties of the specified <tt>Format</tt> into this instance.
     *
     * @param f the <tt>Format</tt> the properties of which are to be copied into this instance
     */
    @Override
    protected void copy(Format f)
    {
        super.copy(f);

        if (f instanceof AVFrameFormat) {
            AVFrameFormat avFrameFormat = (AVFrameFormat) f;

            pixFmt = avFrameFormat.pixFmt;
            deviceSystemPixFmt = avFrameFormat.deviceSystemPixFmt;
        }
    }

    /**
     * Determines whether a specific <tt>Object</tt> represents a value that is equal to the value represented by this
     * instance.
     *
     * @param obj the <tt>Object</tt> to be determined whether it represents a value that is equal to the value represented
     * by this instance
     * @return <tt>true</tt> if the specified <tt>obj</tt> represents a value that is equal to the value represented by
     * this instance; otherwise, <tt>false</tt>
     */
    @Override
    public boolean equals(Object obj)
    {
        if ((obj instanceof AVFrameFormat) && super.equals(obj)) {
            AVFrameFormat avFrameFormat = (AVFrameFormat) obj;

            return (pixFmt == avFrameFormat.pixFmt);
        }
        else
            return false;
    }

    /**
     * Gets the <tt>DeviceSystem</tt>-specific format represented by this instance.
     *
     * @return the <tt>DeviceSystem</tt>-specific format represented by this instance
     */
    public int getDeviceSystemPixFmt()
    {
        return deviceSystemPixFmt;
    }

    /**
     * Gets the native FFmpeg format represented by this instance.
     *
     * @return the native FFmpeg format represented by this instance
     */
    public int getPixFmt()
    {
        return pixFmt;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() + pixFmt;
    }

    /**
     * Finds the attributes shared by two matching <tt>Format</tt>s. If the
     * specified <tt>Format</tt> does not match this one, the result is undefined.
     *
     * @param format the matching <tt>Format</tt> to intersect with this one
     * @return a <tt>Format</tt> with its attributes set to the attributes
     * common to this instance and the specified <tt>format</tt>
     */
    @Override
    public Format intersects(Format format)
    {
        Format intersection = super.intersects(format);

        if (intersection != null) {
            AVFrameFormat avFrameFormatIntersection = (AVFrameFormat) intersection;

            avFrameFormatIntersection.pixFmt = ((pixFmt == NOT_SPECIFIED) && (format instanceof AVFrameFormat))
                    ? ((AVFrameFormat) format).pixFmt : pixFmt;
        }
        return intersection;
    }

    /**
     * Determines whether a specific format matches this instance i.e. whether their attributes match according to the
     * definition of "match" given by {@link Format#matches(Format)}.
     *
     * @param format the <tt>Format</tt> to compare to this instance
     * @return <tt>true</tt> if the specified <tt>format</tt> matches this one; otherwise, <tt>false</tt>
     */
    @Override
    public boolean matches(Format format)
    {
        boolean matches;

        if (super.matches(format)) {
            if (format instanceof AVFrameFormat) {
                AVFrameFormat avFrameFormat = (AVFrameFormat) format;

                matches = ((pixFmt == NOT_SPECIFIED) || (avFrameFormat.pixFmt == NOT_SPECIFIED) || (pixFmt == avFrameFormat.pixFmt));
            }
            else
                matches = true;
        }
        else
            matches = false;
        return matches;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder(super.toString());

        if (pixFmt != NOT_SPECIFIED)
            s.append(", pixFmt ").append(pixFmt);
        if (deviceSystemPixFmt != NOT_SPECIFIED) {
            s.append(", deviceSystemPixFmt 0x");
            /*
             * The value is likely more suitably displayed as unsigned and hexadecimal.
             */
            s.append(Long.toHexString(deviceSystemPixFmt & 0xffffffffL));
        }
        return s.toString();
    }
}
