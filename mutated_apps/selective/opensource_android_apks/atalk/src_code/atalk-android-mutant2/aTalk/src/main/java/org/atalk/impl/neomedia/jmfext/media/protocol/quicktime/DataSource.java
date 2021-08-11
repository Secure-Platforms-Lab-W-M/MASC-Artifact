/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.protocol.quicktime;

import org.atalk.android.util.java.awt.Dimension;
import org.atalk.impl.neomedia.control.FrameRateControlAdapter;
import org.atalk.impl.neomedia.device.DeviceSystem;
import org.atalk.impl.neomedia.jmfext.media.protocol.AbstractPushBufferCaptureDevice;
import org.atalk.impl.neomedia.jmfext.media.protocol.AbstractVideoPushBufferCaptureDevice;
import org.atalk.impl.neomedia.quicktime.*;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.FrameRateControl;
import javax.media.format.VideoFormat;

import timber.log.Timber;

/**
 * Implements a <tt>PushBufferDataSource</tt> and <tt>CaptureDevice</tt> using QuickTime/QTKit.
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public class DataSource extends AbstractVideoPushBufferCaptureDevice
{
	/**
	 * The <tt>QTCaptureSession</tt> which captures from {@link #device} and pushes media data to
	 * the <tt>PushBufferStream</tt>s of this <tt>PushBufferDataSource</tt>.
	 */
	private QTCaptureSession captureSession;

	/**
	 * The <tt>QTCaptureDevice</tt> which represents the media source of this <tt>DataSource</tt>.
	 */
	private QTCaptureDevice device;

	/**
	 * The list of <tt>Format</tt>s to be reported by <tt>DataSource</tt> instances as supported
	 * formats.
	 */
	private static Format[] supportedFormats;

	/**
	 * Initializes a new <tt>DataSource</tt> instance.
	 */
	public DataSource()
	{
		this(null);
	}

	/**
	 * Initializes a new <tt>DataSource</tt> instance from a specific <tt>MediaLocator</tt>.
	 *
	 * @param locator
	 *        the <tt>MediaLocator</tt> to create the new instance from
	 */
	public DataSource(MediaLocator locator)
	{
		super(locator);
	}

	/**
	 * Overrides {@link AbstractVideoPushBufferCaptureDevice#createFrameRateControl()} to provide a
	 * <tt>FrameRateControl</tt> which gets and sets the frame rate of the
	 * <tt>QTCaptureDecompressedVideoOutput</tt> represented by the <tt>QuickTimeStream</tt> made
	 * available by this <tt>DataSource</tt>.
	 *
	 * {@inheritDoc}
	 * 
	 * @see AbstractVideoPushBufferCaptureDevice#createFrameRateControl()
	 */
	@Override
	protected FrameRateControl createFrameRateControl()
	{
		return new FrameRateControlAdapter()
		{
			/**
			 * The output frame rate to be managed by this <tt>FrameRateControl</tt> when there is
			 * no <tt>QuickTimeStream</tt> to delegate to.
			 */
			private float frameRate = -1;

			@Override
			public float getFrameRate()
			{
				float frameRate = -1;
				boolean frameRateFromQuickTimeStream = false;

				synchronized (getStreamSyncRoot()) {
					Object[] streams = streams();

					if ((streams != null) && (streams.length != 0)) {
						for (Object stream : streams) {
							QuickTimeStream quickTimeStream = (QuickTimeStream) stream;

							if (quickTimeStream != null) {
								frameRate = quickTimeStream.getFrameRate();
								frameRateFromQuickTimeStream = true;
								if (frameRate != -1)
									break;
							}
						}
					}
				}
				return frameRateFromQuickTimeStream ? frameRate : this.frameRate;
			}

			@Override
			public float setFrameRate(float frameRate)
			{
				float setFrameRate = -1;
				boolean frameRateFromQuickTimeStream = false;

				synchronized (getStreamSyncRoot()) {
					Object[] streams = streams();

					if ((streams != null) && (streams.length != 0)) {
						for (Object stream : streams) {
							QuickTimeStream quickTimeStream = (QuickTimeStream) stream;

							if (quickTimeStream != null) {
								float quickTimeStreamFrameRate = quickTimeStream
									.setFrameRate(frameRate);

								if (quickTimeStreamFrameRate != -1) {
									setFrameRate = quickTimeStreamFrameRate;
								}
								frameRateFromQuickTimeStream = true;
							}
						}
					}
				}
				if (frameRateFromQuickTimeStream)
					return setFrameRate;
				else {
					this.frameRate = frameRate;
					return this.frameRate;
				}
			}
		};
	}

	/**
	 * Creates a new <tt>PushBufferStream</tt> which is to be at a specific zero-based index in the
	 * list of streams of this <tt>PushBufferDataSource</tt>. The <tt>Format</tt>-related
	 * information of the new instance is to be abstracted by a specific <tt>FormatControl</tt>.
	 *
	 * @param streamIndex
	 *        the zero-based index of the <tt>PushBufferStream</tt> in the list of streams of this
	 *        <tt>PushBufferDataSource</tt>
	 * @param formatControl
	 *        the <tt>FormatControl</tt> which is to abstract the <tt>Format</tt>-related
	 *        information of the new instance
	 * @return a new <tt>PushBufferStream</tt> which is to be at the specified <tt>streamIndex</tt>
	 *         in the list of streams of this <tt>PushBufferDataSource</tt> and which has its
	 *         <tt>Format</tt>-related information abstracted by the specified
	 *         <tt>formatControl</tt>
	 * @see AbstractPushBufferCaptureDevice#createStream(int, FormatControl)
	 */
	@Override
	protected QuickTimeStream createStream(int streamIndex, FormatControl formatControl)
	{
		QuickTimeStream stream = new QuickTimeStream(this, formatControl);

		if (captureSession != null)
			try {
				captureSession.addOutput(stream.captureOutput);
			}
			catch (NSErrorException nseex) {
				Timber.e(nseex, "Failed to addOutput to QTCaptureSession");
				throw new UndeclaredThrowableException(nseex);
			}
		return stream;
	}

	/**
	 * Opens a connection to the media source specified by the <tt>MediaLocator</tt> of this
	 * <tt>DataSource</tt>.
	 *
	 * @throws IOException
	 *         if anything goes wrong while opening the connection to the media source specified by
	 *         the <tt>MediaLocator</tt> of this <tt>DataSource</tt>
	 * @see AbstractPushBufferCaptureDevice#doConnect()
	 */
	@Override
	protected void doConnect()
		throws IOException
	{
		super.doConnect();

		boolean deviceIsOpened;

		try {
			deviceIsOpened = device.open();
		}
		catch (NSErrorException nseex) {
			IOException ioex = new IOException();

			ioex.initCause(nseex);
			throw ioex;
		}
		if (!deviceIsOpened)
			throw new IOException("Failed to open QTCaptureDevice");

		QTCaptureDeviceInput deviceInput = QTCaptureDeviceInput.deviceInputWithDevice(device);

		captureSession = new QTCaptureSession();
		try {
			captureSession.addInput(deviceInput);
		}
		catch (NSErrorException nseex) {
			IOException ioex = new IOException();

			ioex.initCause(nseex);
			throw ioex;
		}

		/*
		 * Add the QTCaptureOutputs represented by the QuickTimeStreams (if any) to the
		 * QTCaptureSession.
		 */
		synchronized (getStreamSyncRoot()) {
			Object[] streams = streams();

			if (streams != null)
				for (Object stream : streams)
					if (stream != null)
						try {
							captureSession.addOutput(((QuickTimeStream) stream).captureOutput);
						}
						catch (NSErrorException nseex) {
							Timber.e(nseex, "Failed to addOutput to QTCaptureSession");

							IOException ioex = new IOException();

							ioex.initCause(nseex);
							throw ioex;
						}
		}
	}

	/**
	 * Closes the connection to the media source specified by the <tt>MediaLocator</tt> of this
	 * <tt>DataSource</tt>.
	 *
	 * @see AbstractPushBufferCaptureDevice#doDisconnect()
	 */
	@Override
	protected void doDisconnect()
	{
		super.doDisconnect();

		if (captureSession != null) {
			captureSession.close();
			captureSession = null;
		}
		device.close();
	}

	/**
	 * Starts the transfer of media data from this <tt>DataSource</tt>.
	 *
	 * @throws IOException
	 *         if anything goes wrong while starting the transfer of media data from this
	 *         <tt>DataSource</tt>
	 * @see AbstractPushBufferCaptureDevice#doStart()
	 */
	@Override
	protected void doStart()
		throws IOException
	{
		captureSession.startRunning();

		super.doStart();
	}

	/**
	 * Stops the transfer of media data from this <tt>DataSource</tt>.
	 *
	 * @throws IOException
	 *         if anything goes wrong while stopping the transfer of media data from this
	 *         <tt>DataSource</tt>
	 * @see AbstractPushBufferCaptureDevice#doStop()
	 */
	@Override
	protected void doStop()
		throws IOException
	{
		super.doStop();

		captureSession.stopRunning();
	}

	/**
	 * Gets the <tt>Format</tt>s which are to be reported by a <tt>FormatControl</tt> as supported
	 * formats for a <tt>PushBufferStream</tt> at a specific zero-based index in the list of streams
	 * of this <tt>PushBufferDataSource</tt>.
	 *
	 * @param streamIndex
	 *        the zero-based index of the <tt>PushBufferStream</tt> for which the specified
	 *        <tt>FormatControl</tt> is to report the list of supported <tt>Format</tt>s
	 * @return an array of <tt>Format</tt>s to be reported by a <tt>FormatControl</tt> as the
	 *         supported formats for the <tt>PushBufferStream</tt> at the specified
	 *         <tt>streamIndex</tt> in the list of streams of this <tt>PushBufferDataSource</tt>
	 * @see AbstractPushBufferCaptureDevice#getSupportedFormats(int)
	 */
	@Override
	protected Format[] getSupportedFormats(int streamIndex)
	{
		return getSupportedFormats(super.getSupportedFormats(streamIndex));
	}

	/**
	 * Gets a list of <tt>Format</tt>s which are more specific than given <tt>Format</tt>s with
	 * respect to video size. The implementation tries to come up with sane video sizes (for
	 * example, by looking for codecs which accept the encodings of the specified generic
	 * <tt>Format</tt>s and using their sizes if any).
	 *
	 * @param genericFormats
	 *        the <tt>Format</tt>s from which more specific are to be derived
	 * @return a list of <tt>Format</tt>s which are more specific than the given <tt>Format</tt>s
	 *         with respect to video size
	 */
	private static synchronized Format[] getSupportedFormats(Format[] genericFormats)
	{
		if ((supportedFormats != null) && (supportedFormats.length > 0))
			return supportedFormats.clone();

		List<Format> specificFormats = new LinkedList<Format>();

		for (Format genericFormat : genericFormats) {
			VideoFormat genericVideoFormat = (VideoFormat) genericFormat;

			if (genericVideoFormat.getSize() == null) {
				@SuppressWarnings("unchecked")
				Vector<String> codecs = PlugInManager.getPlugInList(new VideoFormat(
					genericVideoFormat.getEncoding()), null, PlugInManager.CODEC);

				for (String codec : codecs) {
					Format[] supportedInputFormats = PlugInManager.getSupportedInputFormats(codec,
						PlugInManager.CODEC);

					for (Format supportedInputFormat : supportedInputFormats)
						if (supportedInputFormat instanceof VideoFormat) {
							Dimension size = ((VideoFormat) supportedInputFormat).getSize();

							if (size != null)
								specificFormats.add(genericFormat.intersects(new VideoFormat(null,
									size, Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED)));
						}
				}
			}

			specificFormats.add(genericFormat);
		}
		supportedFormats = specificFormats.toArray(new Format[specificFormats.size()]);
		return supportedFormats.clone();
	}

	/**
	 * Sets the <tt>QTCaptureDevice</tt> which represents the media source of this
	 * <tt>DataSource</tt>.
	 *
	 * @param device
	 *        the <tt>QTCaptureDevice</tt> which represents the media source of this
	 *        <tt>DataSource</tt>
	 */
	private void setDevice(QTCaptureDevice device)
	{
		if (this.device != device)
			this.device = device;
	}

	/**
	 * Attempts to set the <tt>Format</tt> to be reported by the <tt>FormatControl</tt> of a
	 * <tt>PushBufferStream</tt> at a specific zero-based index in the list of streams of this
	 * <tt>PushBufferDataSource</tt>. The <tt>PushBufferStream</tt> does not exist at the time of
	 * the attempt to set its <tt>Format</tt>.
	 *
	 * @param streamIndex
	 *        the zero-based index of the <tt>PushBufferStream</tt> the <tt>Format</tt> of which is
	 *        to be set
	 * @param oldValue
	 *        the last-known <tt>Format</tt> for the <tt>PushBufferStream</tt> at the specified
	 *        <tt>streamIndex</tt>
	 * @param newValue
	 *        the <tt>Format</tt> which is to be set
	 * @return the <tt>Format</tt> to be reported by the <tt>FormatControl</tt> of the
	 *         <tt>PushBufferStream</tt> at the specified <tt>streamIndex</tt> in the list of
	 *         streams of this <tt>PushBufferStream</tt> or <tt>null</tt> if the attempt to set the
	 *         <tt>Format</tt> did not success and any last-known <tt>Format</tt> is to be left in
	 *         effect
	 * @see AbstractPushBufferCaptureDevice#setFormat(int, Format, Format)
	 */
	@Override
	protected Format setFormat(int streamIndex, Format oldValue, Format newValue)
	{
		if (newValue instanceof VideoFormat) {
			// This DataSource supports setFormat.
			return newValue;
		}
		else
			return super.setFormat(streamIndex, oldValue, newValue);
	}

	/**
	 * Sets the <tt>MediaLocator</tt> which specifies the media source of this <tt>DataSource</tt>.
	 *
	 * @param locator
	 *        the <tt>MediaLocator</tt> which specifies the media source of this <tt>DataSource</tt>
	 * @see javax.media.protocol.DataSource#setLocator(MediaLocator)
	 */
	@Override
	public void setLocator(MediaLocator locator)
	{
		super.setLocator(locator);

		locator = getLocator();

		QTCaptureDevice device;

		if ((locator != null)
			&& DeviceSystem.LOCATOR_PROTOCOL_QUICKTIME.equalsIgnoreCase(locator.getProtocol())) {
			String deviceUID = locator.getRemainder();

			device = QTCaptureDevice.deviceWithUniqueID(deviceUID);
		}
		else
			device = null;
		setDevice(device);
	}
}
