/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.neomedia.device;

import org.atalk.service.neomedia.*;
import org.atalk.service.neomedia.codec.EncodingConfiguration;
import org.atalk.service.neomedia.format.MediaFormat;
import org.atalk.util.MediaType;

import java.util.List;

/**
 * The <tt>MediaDevice</tt> class represents capture and playback devices that can be used to grab
 * or render media. Sound cards, USB phones and webcams are examples of such media devices.
 *
 * @author Emil Ivov
 * @author Eng Chong Meng
 */
public interface MediaDevice
{
    /**
     * Returns the <tt>MediaDirection</tt> supported by this device.
     *
     * @return <tt>MediaDirection.SENDONLY</tt> if this is a read-only device,
     * <tt>MediaDirection.RECVONLY</tt> if this is a write-only device and
     * <tt>MediaDirection.SENDRECV</tt> if this <tt>MediaDevice</tt> can both capture and render media.
     */
    public MediaDirection getDirection();

    /**
     * Returns the <tt>MediaFormat</tt> that this device is currently set to use when capturing data.
     *
     * @return the <tt>MediaFormat</tt> that this device is currently set to provide media in.
     */
    public MediaFormat getFormat();

    /**
     * Returns the <tt>MediaType</tt> that this device supports.
     *
     * @return <tt>MediaType.AUDIO</tt> if this is an audio device or <tt>MediaType.VIDEO</tt> in
     * case of a video device.
     */
    public MediaType getMediaType();

    /**
     * Returns the <tt>List</tt> of <tt>RTPExtension</tt>s that this device know how to handle.
     *
     * @return the <tt>List</tt> of <tt>RTPExtension</tt>s that this device know how to handle or
     * <tt>null</tt> if the device does not support any RTP extensions.
     */
    public List<RTPExtension> getSupportedExtensions();

    /**
     * Returns a list of <tt>MediaFormat</tt> instances representing the media formats supported by
     * this <tt>MediaDevice</tt>.
     *
     * @return the list of <tt>MediaFormat</tt>s supported by this device.
     */
    public List<MediaFormat> getSupportedFormats();

    /**
     * Returns a list of <tt>MediaFormat</tt> instances representing the media formats supported by
     * this <tt>MediaDevice</tt>.
     *
     * @param localPreset the preset used to set the send format parameters, used for video and settings.
     * @param remotePreset the preset used to set the receive format parameters, used for video and settings.
     * @return the list of <tt>MediaFormat</tt>s supported by this device.
     */
    public List<MediaFormat> getSupportedFormats(QualityPreset localPreset, QualityPreset remotePreset);

    /**
     * Returns a list of <tt>MediaFormat</tt> instances representing the media formats supported by
     * this <tt>MediaDevice</tt> and enabled in <tt>encodingConfiguration</tt>.
     *
     * @param localPreset the preset used to set the send format parameters, used for video and settings.
     * @param remotePreset the preset used to set the receive format parameters, used for video and settings.
     * @param encodingConfiguration the <tt>EncodingConfiguration<tt> instance
     * to use.
     * @return the list of <tt>MediaFormat</tt>s supported by this device.
     */
    public List<MediaFormat> getSupportedFormats(QualityPreset localPreset,
            QualityPreset remotePreset, EncodingConfiguration encodingConfiguration);
}
