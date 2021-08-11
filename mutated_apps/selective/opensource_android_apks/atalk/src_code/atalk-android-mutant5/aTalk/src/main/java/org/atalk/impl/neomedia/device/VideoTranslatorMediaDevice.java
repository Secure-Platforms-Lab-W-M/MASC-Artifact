/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.device;

import org.atalk.android.util.java.awt.Component;
import org.atalk.impl.neomedia.AbstractRTPConnector;
import org.atalk.impl.neomedia.format.MediaFormatImpl;
import org.atalk.service.neomedia.*;
import org.atalk.service.neomedia.codec.EncodingConfiguration;
import org.atalk.service.neomedia.device.*;
import org.atalk.service.neomedia.format.MediaFormat;
import org.atalk.util.MediaType;
import org.atalk.util.event.*;

import java.util.*;

import javax.media.*;
import javax.media.protocol.DataSource;

/**
 * Implements a <tt>MediaDevice</tt> which is to be used in video conferencing implemented with an
 * RTP translator.
 *
 * @author Lyubomir Marinov
 * @author Hristo Terezov
 * @author Boris Grozev
 * @author Eng Chong Meng
 */
public class VideoTranslatorMediaDevice extends AbstractMediaDevice implements MediaDeviceWrapper, VideoListener
{
	/**
	 * The <tt>MediaDevice</tt> which this instance enables to be used in a video conference
	 * implemented with an RTP translator.
	 */
	private final MediaDeviceImpl device;

	/**
	 * The <tt>VideoMediaDeviceSession</tt> of {@link #device} the <tt>outputDataSource</tt> of
	 * which is the <tt>captureDevice</tt> of {@link #streamDeviceSessions}.
	 */
	private VideoMediaDeviceSession deviceSession;

	/**
	 * The <tt>MediaStreamMediaDeviceSession</tt>s sharing the <tt>outputDataSource</tt> of
	 * {@link #device} as their <tt>captureDevice</tt>.
	 */
	private final List<MediaStreamMediaDeviceSession> streamDeviceSessions = new LinkedList<>();

	/**
	 * Initializes a new <tt>VideoTranslatorMediaDevice</tt> which enables a specific
	 * <tt>MediaDevice</tt> to be used in video conferencing implemented with an RTP translator.
	 *
	 * @param device
	 * 		the <tt>MediaDevice</tt> which the new instance is to enable to be used in video
	 * 		conferencing implemented with an RTP translator
	 */
	public VideoTranslatorMediaDevice(MediaDeviceImpl device)
	{
		this.device = device;
	}

	/**
	 * Releases the resources allocated by this instance in the course of its execution and
	 * prepares it to be garbage collected when all {@link #streamDeviceSessions} have been closed.
	 *
	 * @param streamDeviceSession
	 * 		the <tt>MediaStreamMediaDeviceSession</tt> which has been closed
	 */
	private synchronized void close(MediaStreamMediaDeviceSession streamDeviceSession)
	{
		streamDeviceSessions.remove(streamDeviceSession);
		if (deviceSession != null) {
			deviceSession.removeRTCPFeedbackMessageCreateListner(streamDeviceSession);
		}
		if (streamDeviceSessions.isEmpty()) {
			if (deviceSession != null) {
				deviceSession.removeVideoListener(this);
				deviceSession.close(MediaDirection.SENDRECV);
			}
			deviceSession = null;
		}
		else
			updateDeviceSessionStartedDirection();
	}

	/**
	 * Creates a <tt>DataSource</tt> instance for this <tt>MediaDevice</tt> which gives access to
	 * the captured media.
	 *
	 * @return a <tt>DataSource</tt> instance which gives access to the media captured by this
	 * <tt>MediaDevice</tt>
	 * @see AbstractMediaDevice#createOutputDataSource()
	 */
	@Override
	protected synchronized DataSource createOutputDataSource()
	{
		if (deviceSession == null) {
			MediaFormatImpl<? extends Format> format = null;
			MediaDirection startedDirection = MediaDirection.INACTIVE;

			for (MediaStreamMediaDeviceSession streamDeviceSession : streamDeviceSessions) {
				MediaFormatImpl<? extends Format> streamFormat = streamDeviceSession.getFormat();

				if ((streamFormat != null) && (format == null))
					format = streamFormat;
				startedDirection = startedDirection.or(streamDeviceSession.getStartedDirection());
			}

			MediaDeviceSession newDeviceSession = device.createSession();
			if (newDeviceSession instanceof VideoMediaDeviceSession) {
				deviceSession = (VideoMediaDeviceSession) newDeviceSession;
				deviceSession.addVideoListener(this);

				for (MediaStreamMediaDeviceSession streamDeviceSession : streamDeviceSessions) {
					deviceSession.addRTCPFeedbackMessageCreateListener(streamDeviceSession);
				}
			}
			if (format != null)
				deviceSession.setFormat(format);

			deviceSession.start(startedDirection);
		}
		return (deviceSession == null) ? null : deviceSession.getOutputDataSource();
	}

	/**
	 * Creates a new <tt>MediaDeviceSession</tt> instance which is to represent the use of this
	 * <tt>MediaDevice</tt> by a <tt>MediaStream</tt>.
	 *
	 * @return a new <tt>MediaDeviceSession</tt> instance which is to represent the use of this
	 * <tt>MediaDevice</tt> by a <tt>MediaStream</tt>
	 * @see AbstractMediaDevice#createSession()
	 */
	@Override
	public synchronized MediaDeviceSession createSession()
	{
		MediaStreamMediaDeviceSession streamDeviceSession = new MediaStreamMediaDeviceSession();

		streamDeviceSessions.add(streamDeviceSession);
		return streamDeviceSession;
	}

	/**
	 * Returns the <tt>MediaDirection</tt> supported by this device.
	 *
	 * @return <tt>MediaDirection.SENDONLY</tt> if this is a read-only device,
	 * <tt>MediaDirection.RECVONLY</tt> if this is a write-only device and
	 * <tt>MediaDirection.SENDRECV</tt> if this <tt>MediaDevice</tt> can both capture and
	 * render media
	 * @see MediaDevice#getDirection()
	 */
	public MediaDirection getDirection()
	{
		return device.getDirection();
	}

	/**
	 * Returns the <tt>MediaFormat</tt> that this device is currently set to use when capturing
	 * data.
	 *
	 * @return the <tt>MediaFormat</tt> that this device is currently set to provide media in.
	 * @see MediaDevice#getFormat()
	 */
	public MediaFormat getFormat()
	{
		return device.getFormat();
	}

	/**
	 * Returns the <tt>MediaType</tt> that this device supports.
	 *
	 * @return <tt>MediaType.AUDIO</tt> if this is an audio device or <tt>MediaType.VIDEO</tt> in
	 * case of a video device
	 * @see MediaDevice#getMediaType()
	 */
	public MediaType getMediaType()
	{
		return device.getMediaType();
	}

	/**
	 * Returns a list of <tt>MediaFormat</tt> instances representing the media formats supported by
	 * this <tt>MediaDevice</tt>.
	 *
	 * @param localPreset
	 * 		the preset used to set the send format parameters, used for video and settings
	 * @param remotePreset
	 * 		the preset used to set the receive format parameters, used for video and settings
	 * @return the list of <tt>MediaFormat</tt>s supported by this device
	 * @see MediaDevice#getSupportedFormats(QualityPreset, QualityPreset)
	 */
	public List<MediaFormat> getSupportedFormats(QualityPreset localPreset,
			QualityPreset remotePreset)
	{
		return device.getSupportedFormats(localPreset, remotePreset);
	}

	/**
	 * Returns a list of <tt>MediaFormat</tt> instances representing the media formats supported by
	 * this <tt>MediaDevice</tt> and enabled in <tt>encodingConfiguration</tt>..
	 *
	 * @param localPreset
	 * 		the preset used to set the send format parameters, used for video and settings
	 * @param remotePreset
	 * 		the preset used to set the receive format parameters, used for video and settings
	 * @param encodingConfiguration
	 * 		the <tt>EncodingConfiguration</tt> instance to use
	 * @return the list of <tt>MediaFormat</tt>s supported by this device and enabled in
	 * <tt>encodingConfiguration</tt>.
	 * @see MediaDevice#getSupportedFormats(QualityPreset, QualityPreset, EncodingConfiguration)
	 */
	public List<MediaFormat> getSupportedFormats(QualityPreset localPreset,
			QualityPreset remotePreset, EncodingConfiguration encodingConfiguration)
	{
		return device.getSupportedFormats(localPreset, remotePreset, encodingConfiguration);
	}

	/**
	 * Gets the actual <tt>MediaDevice</tt> which this <tt>MediaDevice</tt> is effectively built on
	 * top of and forwarding to.
	 *
	 * @return the actual <tt>MediaDevice</tt> which this <tt>MediaDevice</tt> is effectively built
	 * on top of and forwarding to
	 * @see MediaDeviceWrapper#getWrappedDevice()
	 */
	public MediaDevice getWrappedDevice()
	{
		return device;
	}

	/**
	 * Updates the value of the <tt>startedDirection</tt> property of {@link #deviceSession} to be
	 * in accord with the values of the property of {@link #streamDeviceSessions}.
	 */
	private synchronized void updateDeviceSessionStartedDirection()
	{
		if (deviceSession == null)
			return;

		MediaDirection startDirection = MediaDirection.INACTIVE;

		for (MediaStreamMediaDeviceSession streamDeviceSession : streamDeviceSessions) {
			startDirection = startDirection.or(streamDeviceSession.getStartedDirection());
		}
		deviceSession.start(startDirection);
		MediaDirection stopDirection = MediaDirection.INACTIVE;

		if (!startDirection.allowsReceiving())
			stopDirection = stopDirection.or(MediaDirection.RECVONLY);
		if (!startDirection.allowsSending())
			stopDirection = stopDirection.or(MediaDirection.SENDONLY);
		deviceSession.stop(stopDirection);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Forwards <tt>event</tt>, to each of the managed <tt>MediaStreamMediaDeviceSession</tt>
	 * instances. The event is expected to come from <tt>this.deviceSession</tt>, since
	 * <tt>this</tt> is registered there as a <tt>VideoListener</tt>.
	 */
	@Override
	public void videoAdded(VideoEvent event)
	{
		for (MediaStreamMediaDeviceSession sds : streamDeviceSessions) {
			sds.fireVideoEvent(event, false);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Forwards <tt>event</tt>, to each of the managed <tt>MediaStreamMediaDeviceSession</tt>
	 * instances. The event is expected to come from <tt>this.deviceSession</tt>, since
	 * <tt>this</tt> is registered there as a <tt>VideoListener</tt>.
	 */
	@Override
	public void videoRemoved(VideoEvent event)
	{
		for (MediaStreamMediaDeviceSession sds : streamDeviceSessions) {
			sds.fireVideoEvent(event, false);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Forwards <tt>event</tt>, to each of the managed <tt>MediaStreamMediaDeviceSession</tt>
	 * instances. The event is expected to come from <tt>this.deviceSession</tt>, since
	 * <tt>this</tt> is registered there as a <tt>VideoListener</tt>.
	 */
	@Override
	public void videoUpdate(VideoEvent event)
	{
		for (MediaStreamMediaDeviceSession sds : streamDeviceSessions) {
			sds.fireVideoEvent(event, false);
		}
	}

	/**
	 * Represents the use of this <tt>VideoTranslatorMediaDevice</tt> by a <tt>MediaStream</tt>.
	 */
	private class MediaStreamMediaDeviceSession extends VideoMediaDeviceSession
	{
		/**
		 * Initializes a new <tt>MediaStreamMediaDeviceSession</tt> which is to represent the
		 * use of this <tt>VideoTranslatorMediaDevice</tt> by a <tt>MediaStream</tt>.
		 */
		public MediaStreamMediaDeviceSession()
		{
			super(VideoTranslatorMediaDevice.this);
		}

		/**
		 * Releases the resources allocated by this instance in the course of its execution and
		 * prepares it to be garbage collected.
		 */
		@Override
		public void close(MediaDirection direction)
		{
			super.close(direction);
			VideoTranslatorMediaDevice.this.close(this);
		}

		/**
		 * Creates the <tt>DataSource</tt> that this instance is to read captured media from.
		 *
		 * @return the <tt>DataSource</tt> that this instance is to read captured media from
		 * @see VideoMediaDeviceSession#createCaptureDevice()
		 */
		@Override
		protected DataSource createCaptureDevice()
		{
			return VideoTranslatorMediaDevice.this.createOutputDataSource();
		}

		/**
		 * Initializes a new <tt>Player</tt> instance which is to provide the local visual/video
		 * <tt>Component</tt>. The new instance is initialized to render the media of a specific
		 * <tt>DataSource</tt>.
		 *
		 * @param captureDevice
		 * 		the <tt>DataSource</tt> which is to have its media rendered by the new instance as
		 * 		the local visual/video <tt>Component</tt>
		 * @return a new <tt>Player</tt> instance which is to provide the local visual/video
		 * <tt>Component</tt>
		 */
		@Override
		protected Player createLocalPlayer(DataSource captureDevice)
		{
			synchronized (VideoTranslatorMediaDevice.this) {
				if (deviceSession != null)
					captureDevice = deviceSession.getCaptureDevice();
			}

			return super.createLocalPlayer(captureDevice);
		}

		/**
		 * Initializes a new FMJ <tt>Processor</tt> which is to transcode {@link #captureDevice}
		 * into the format of this instance.
		 *
		 * @return a new FMJ <tt>Processor</tt> which is to transcode <tt>captureDevice</tt> into
		 * the format of this instance
		 */
		@Override
		protected Processor createProcessor()
		{
			return null;
		}

		/**
		 * Gets the output <tt>DataSource</tt> of this instance which provides the captured (RTP)
		 * data to be sent by <tt>MediaStream</tt> to <tt>MediaStreamTarget</tt>.
		 *
		 * @return the output <tt>DataSource</tt> of this instance which provides the captured
		 * (RTP) data to be sent by <tt>MediaStream</tt> to <tt>MediaStreamTarget</tt>
		 * @see MediaDeviceSession#getOutputDataSource()
		 */
		@Override
		public DataSource getOutputDataSource()
		{
			return getConnectedCaptureDevice();
		}

		/**
		 * Sets the <tt>RTPConnector</tt> that will be used to initialize some codec for RTCP
		 * feedback and adds the instance to RTCPFeedbackCreateListners of deviceSession.
		 *
		 * @param rtpConnector
		 * 		the RTP connector
		 */
		@Override
		public void setConnector(AbstractRTPConnector rtpConnector)
		{
			super.setConnector(rtpConnector);
			if (deviceSession != null)
				deviceSession.addRTCPFeedbackMessageCreateListener(this);
		}

		/**
		 * Notifies this instance that the value of its <tt>startedDirection</tt> property has
		 * changed from a specific <tt>oldValue</tt> to a specific <tt>newValue</tt>.
		 *
		 * @param oldValue
		 * 		the <tt>MediaDirection</tt> which used to be the value of the
		 * 		<tt>startedDirection</tt> property of this instance
		 * @param newValue
		 * 		the <tt>MediaDirection</tt> which is the value of the <tt>startedDirection</tt>
		 * 		property of this instance
		 */
		@Override
		protected void startedDirectionChanged(MediaDirection oldValue, MediaDirection newValue)
		{
			super.startedDirectionChanged(oldValue, newValue);
			VideoTranslatorMediaDevice.this.updateDeviceSessionStartedDirection();
		}

		/**
		 * {@inheritDoc} Returns the local visual <tt>Component</tt> for this
		 * <tt>MediaStreamMediaDeviceSession</tt>, which, if present, is maintained in
		 * <tt>this.deviceSession</tt>.
		 */
		@Override
		public Component getLocalVisualComponent()
		{
			if (deviceSession != null)
				return deviceSession.getLocalVisualComponent();
			return null;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Creates, if necessary, the local visual <tt>Component</tt> depicting the video being
		 * streamed from the local peer to a remote peer. The <tt>Component</tt> is provided by the
		 * single <tt>Player</tt> instance, which is maintained for this
		 * <tt>VideoTranslatorMediaDevice</tt> and is managed by <tt>this.deviceSession</tt>.
		 */
		@Override
		protected Component createLocalVisualComponent()
		{
			if (deviceSession != null)
				return deviceSession.createLocalVisualComponent();
			return null;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Returns the <tt>Player</tt> instance which provides the local visual/video
		 * <tt>Component</tt>. A single <tt>Player</tt> is maintained for this
		 * <tt>VideoTranslatorMediaDevice</tt>, and it is managed by <tt>this.deviceSession</tt>.
		 */
		@Override
		protected Player getLocalPlayer()
		{
			if (deviceSession != null)
				return deviceSession.getLocalPlayer();
			return null;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Does nothing, because there is no <tt>Player</tt> associated with this
		 * <tt>MediaStreamMediaDeviceSession</tt> and therefore nothing to dispose of.
		 *
		 * @param player
		 * 		the <tt>Player</tt> to dispose of.
		 */
		@Override
		protected void disposeLocalPlayer(Player player)
		{
		}

	}
}
