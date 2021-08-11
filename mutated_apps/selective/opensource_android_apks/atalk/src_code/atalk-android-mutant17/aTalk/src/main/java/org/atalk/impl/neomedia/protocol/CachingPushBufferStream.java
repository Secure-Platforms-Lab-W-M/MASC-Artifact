/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.protocol;

import org.atalk.android.util.java.awt.Component;

import java.io.IOException;
import java.util.*;

import javax.media.*;
import javax.media.control.BufferControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.*;

/**
 * Enables reading from a <tt>PushBufferStream</tt> a certain maximum number of data units (e.g.
 * bytes, shorts, ints) even if the <tt>PushBufferStream</tt> itself pushes a larger number of data
 * units.
 * <p>
 * An example use of this functionality is pacing a <tt>PushBufferStream</tt> which pushes more data
 * units in a single step than a <tt>CaptureDevice</tt>. When these two undergo audio mixing, the
 * different numbers of per-push data units will cause the <tt>PushBufferStream</tt> "play" itself
 * faster than the <tt>CaptureDevice</tt>.
 * </p>
 *
 * @author Lyubomir Marinov
 */
public class CachingPushBufferStream implements PushBufferStream
{

	/**
	 * The default length in milliseconds of the buffering to be performed by
	 * <tt>CachePushBufferStream</tt>s.
	 */
	public static final long DEFAULT_BUFFER_LENGTH = 20;

	/**
	 * The maximum number of <tt>Buffer</tt>s to be cached in a <tt>CachingPushBufferStream</tt>.
	 * Generally, defined to a relatively large value which allows large buffering and yet tries to
	 * prevent <tt>OutOfMemoryError</tt>.
	 */
	private static final int MAX_CACHE_SIZE = 1024;

	/**
	 * The <tt>BufferControl</tt> of this <tt>PushBufferStream</tt> which allows the adjustment of
	 * the size of the buffering it performs.
	 */
	private BufferControl bufferControl;

	/**
	 * The <tt>Object</tt> which synchronizes the access to {@link #bufferControl}.
	 */
	private final Object bufferControlSyncRoot = new Object();

	/**
	 * The list of <tt>Buffer</tt>s in which this instance stores the data it reads from the
	 * wrapped <tt>PushBufferStream</tt> and from which it reads in chunks later on when its
	 * {@link #read(Buffer)} method is called.
	 */
	private final List<Buffer> cache = new LinkedList<Buffer>();

	/**
	 * The length of the media in milliseconds currently available in {@link #cache}.
	 */
	private long cacheLengthInMillis = 0;

	/**
	 * The last <tt>IOException</tt> this stream has received from the <tt>#read(Buffer)</tt>
	 * method of the wrapped stream and to be thrown by this stream on the earliest call of its
	 * <tt>#read(Buffer)</tt> method.
	 */
	private IOException readException;

	/**
	 * The <tt>PushBufferStream</tt> being paced by this instance with respect to the maximum
	 * number of data units it provides in a single push.
	 */
	private final PushBufferStream stream;

	/**
	 * The <tt>BufferTransferHandler</tt> set on {@link #stream}.
	 */
	private BufferTransferHandler transferHandler;

	/**
	 * Initializes a new <tt>CachingPushBufferStream</tt> instance which is to pace the number of
	 * per-push data units a specific <tt>PushBufferStream</tt> provides.
	 *
	 * @param stream
	 * 		the <tt>PushBufferStream</tt> to be paced with respect to the number of per-push data
	 * 		units it provides
	 */
	public CachingPushBufferStream(PushBufferStream stream)
	{
		this.stream = stream;
	}

	/**
	 * Determines whether adding a new <tt>Buffer</tt> to {@link #cache} is acceptable given the
	 * maximum size of the <tt>cache</tt> and the length of the media currently available in it.
	 *
	 * @return <tt>true</tt> if adding a new <tt>Buffer</tt> to <tt>cache</tt> is acceptable;
	 * otherwise, <tt>false</tt> which means that the reading from the wrapped
	 * <tt>PushBufferStream</tt> should be blocked until <tt>true</tt> is returned
	 */
	private boolean canWriteInCache()
	{
		synchronized (cache) {
			int cacheSize = cache.size();

			/*
			 * Obviously, if there's nothing in the cache, we desperately want something to be
			 * written into it.
			 */
			if (cacheSize < 1)
				return true;
			/*
			 * For the sake of not running out of memory, don't let the sky be the limit.
			 */
			if (cacheSize >= MAX_CACHE_SIZE)
				return false;

			long bufferLength = getBufferLength();

			/*
			 * There is no bufferLength specified by a BufferControl so don't buffer anything.
			 */
			if (bufferLength < 1)
				return false;
			/*
			 * Having Buffers in the cache and yet not having their length in milliseconds is weird
			 * so don't buffer anything.
			 */
			if (cacheLengthInMillis < 1)
				return false;
			/*
			 * Of course, if the media in the cache hasn't reached the specified buffer length,
			 * write more to the cache.
			 */
			return (cacheLengthInMillis < bufferLength);
		}
	}

	/**
	 * Implements {@link SourceStream#endOfStream()}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt> when the cache of this instance is fully read; otherwise, returns
	 * <tt>false</tt>.
	 *
	 * @return <tt>true</tt> if this <tt>PushBufferStream</tt> has reached the end of the
	 * content it makes available; otherwise, <tt>false</tt>
	 */
	public boolean endOfStream()
	{
		/*
		 * TODO If the cache is still not exhausted, don't report the end of this stream even if
		  * the
		 * wrapped stream has reached its end.
		 */
		return stream.endOfStream();
	}

	/**
	 * Gets the <tt>BufferControl</tt> of this <tt>PushBufferStream</tt> which allows the
	 * adjustment of the size of the buffering it performs. If it does not exist yet, it is
	 * created.
	 *
	 * @return the <tt>BufferControl</tt> of this <tt>PushBufferStream</tt> which allows the
	 * adjustment of the size of the buffering it performs
	 */
	private BufferControl getBufferControl()
	{
		synchronized (bufferControlSyncRoot) {
			if (bufferControl == null)
				bufferControl = new BufferControlImpl();
			return bufferControl;
		}
	}

	/**
	 * Gets the length in milliseconds of the buffering performed by this
	 * <tt>PushBufferStream</tt>.
	 *
	 * @return the length in milliseconds of the buffering performed by this
	 * <tt>PushBufferStream</tt> if such a value has been set; otherwise,
	 * {@link BufferControl#DEFAULT_VALUE}
	 */
	private long getBufferLength()
	{
		synchronized (bufferControlSyncRoot) {
			return (bufferControl == null)
					? BufferControl.DEFAULT_VALUE : bufferControl.getBufferLength();
		}
	}

	/**
	 * Implements {@link SourceStream#getContentDescriptor()}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt>.
	 *
	 * @return a <tt>ContentDescriptor</tt> which describes the type of the content made available
	 * by the wrapped <tt>PushBufferStream</tt>
	 */
	public ContentDescriptor getContentDescriptor()
	{
		return stream.getContentDescriptor();
	}

	/**
	 * Implements {@link SourceStream#getContentLength()}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt>.
	 *
	 * @return the length of the content made available by the wrapped <tt>PushBufferStream</tt>
	 */
	public long getContentLength()
	{
		return stream.getContentLength();
	}

	/**
	 * Implements {@link javax.media.Controls#getControl(String)}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt> and gives access to the <tt>BufferControl</tt> of this instance if
	 * such a <tt>controlType</tt> is specified and the wrapped <tt>PushBufferStream</tt> does not
	 * have such a control available.
	 *
	 * @param controlType
	 * 		a <tt>String</tt> value which names the type of the control of the wrapped
	 * 		<tt>PushBufferStream</tt> to be retrieved
	 * @return an <tt>Object</tt> which represents the control of the wrapped
	 * <tt>PushBufferStream</tt> with the specified type if such a control is available;
	 * otherwise, <tt>null</tt>
	 */
	public Object getControl(String controlType)
	{
		Object control = stream.getControl(controlType);

		if ((control == null) && BufferControl.class.getName().equals(controlType)) {
			control = getBufferControl();
		}
		return control;
	}

	/**
	 * Implements {@link javax.media.Controls#getControls()}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt> and adds the <tt>BufferControl</tt> of this instance if the
	 * wrapped <tt>PushBufferStream</tt> does not have a control of such type available.
	 *
	 * @return an array of <tt>Object</tt>s which represent the control available for the wrapped
	 * <tt>PushBufferStream</tt>
	 */
	public Object[] getControls()
	{
		Object[] controls = stream.getControls();

		if (controls == null) {
			BufferControl bufferControl = getBufferControl();
			if (bufferControl != null)
				controls = new Object[]{bufferControl};
		}
		else {
			boolean bufferControlExists = false;

			for (Object control : controls) {
				if (control instanceof BufferControl) {
					bufferControlExists = true;
					break;
				}
			}
			if (!bufferControlExists) {
				BufferControl bufferControl = getBufferControl();

				if (bufferControl != null) {
					Object[] newControls = new Object[controls.length + 1];

					newControls[0] = bufferControl;
					System.arraycopy(controls, 0, newControls, 1, controls.length);
				}
			}
		}
		return controls;
	}

	/**
	 * Implements {@link PushBufferStream#getFormat()}. Delegates to the wrapped
	 * <tt>PushBufferStream</tt>.
	 *
	 * @return the <tt>Format</tt> of the media data available for reading in this
	 * <tt>PushBufferStream</tt>
	 */
	public Format getFormat()
	{
		return stream.getFormat();
	}

	/**
	 * Gets the length in milliseconds of the media in a specific <tt>Buffer</tt> (often
	 * referred to as duration).
	 *
	 * @param buffer
	 * 		the <tt>Buffer</tt> which contains media the length in milliseconds of which is to be
	 * 		calculated
	 * @return the length in milliseconds of the media in <tt>buffer</tt> if there actually is
	 * media in <tt>buffer</tt> and its length in milliseconds can be calculated; otherwise,
	 * <tt>0</tt>
	 */
	private long getLengthInMillis(Buffer buffer)
	{
		int length = buffer.getLength();

		if (length < 1)
			return 0;

		Format format = buffer.getFormat();

		if (format == null) {
			format = getFormat();
			if (format == null)
				return 0;
		}
		if (!(format instanceof AudioFormat))
			return 0;

		AudioFormat audioFormat = (AudioFormat) format;
		long duration = audioFormat.computeDuration(length);

		return (duration < 1) ? 0 : (duration / 1000000);
	}

	/**
	 * Gets the <tt>PushBufferStream</tt> wrapped by this instance.
	 *
	 * @return the <tt>PushBufferStream</tt> wrapped by this instance
	 */
	public PushBufferStream getStream()
	{
		return stream;
	}

	/**
	 * Implements {@link PushBufferStream#read(Buffer)}. If an <tt>IOException</tt> has been thrown
	 * by the wrapped stream when data was last read from it, re-throws it. If there has been no
	 * such exception, reads from the cache of this instance.
	 *
	 * @param buffer
	 * 		the <tt>Buffer</tt> to receive the read media data
	 * @throws IOException
	 * 		if the wrapped stream has thrown such an exception when data was last read from it
	 */
	public void read(Buffer buffer)
			throws IOException
	{
		synchronized (cache) {
			if (readException != null) {
				IOException ioe = new IOException();

				ioe.initCause(readException);
				readException = null;
				throw ioe;
			}

			buffer.setLength(0);
			if (!cache.isEmpty()) {
				int bufferOffset = buffer.getOffset();

				while (!cache.isEmpty()) {
					Buffer cacheBuffer = cache.get(0);
					int nextBufferOffset = read(cacheBuffer, buffer, bufferOffset);

					if ((cacheBuffer.getLength() <= 0) || (cacheBuffer.getData() == null))
						cache.remove(0);
					if (nextBufferOffset < 0)
						break;
					else
						bufferOffset = nextBufferOffset;
				}

				cacheLengthInMillis -= getLengthInMillis(buffer);
				if (cacheLengthInMillis < 0)
					cacheLengthInMillis = 0;

				if (canWriteInCache())
					cache.notify();
			}
		}
	}

	/**
	 * Reads data from a specific input <tt>Buffer</tt> (if such data is available) and writes the
	 * read data into a specific output <tt>Buffer</tt>. The input <tt>Buffer</tt> will be modified
	 * to reflect the number of read data units. If the output <tt>Buffer</tt> has allocated an
	 * array for storing the read data and the type of this array matches that of the input
	 * <tt>Buffer</tt>, it will be used and thus the output <tt>Buffer</tt> may control the maximum
	 * number of data units to be read into it.
	 *
	 * @param in
	 * 		the <tt>Buffer</tt> to read data from
	 * @param out
	 * 		the <tt>Buffer</tt> into which to write the data read from the specified <tt>in</tt>
	 * @param outOffset
	 * 		the offset in <tt>out</tt> at which the data read from <tt>in</tt> is to be written
	 * @return the offset in <tt>out</tt> at which a next round of writing is to continue;
	 * <tt>-1</tt> if no more writing in <tt>out</tt> is to be performed and <tt>out</tt> is
	 * to be returned to the caller
	 * @throws IOException
	 * 		if reading from <tt>in</tt> into <tt>out</tt> fails including if either of the
	 * 		formats of <tt>in</tt> and <tt>out</tt> are not supported
	 */
	private int read(Buffer in, Buffer out, int outOffset)
			throws IOException
	{
		Object outData = out.getData();

		if (outData != null) {
			Object inData = in.getData();

			if (inData == null) {
				out.setFormat(in.getFormat());
				// There was nothing to read so continue reading and concatenating.
				return outOffset;
			}

			Class<?> dataType = outData.getClass();

			if (inData.getClass().equals(dataType) && dataType.equals(byte[].class)) {
				int inOffset = in.getOffset();
				int inLength = in.getLength();
				byte[] outBytes = (byte[]) outData;
				int outLength = outBytes.length - outOffset;

				// Where is it supposed to be written?
				if (outLength < 1)
					return -1;

				if (inLength < outLength)
					outLength = inLength;
				System.arraycopy(inData, inOffset, outBytes, outOffset, outLength);

				out.setData(outBytes);
				out.setLength(out.getLength() + outLength);

				/*
				 * If we're currently continuing a concatenation, the parameters of the first read
				 * from input are left as the parameters of output. Mostly done at least for
				 * timeStamp.
				 */
				if (out.getOffset() == outOffset) {
					out.setFormat(in.getFormat());

					out.setDiscard(in.isDiscard());
					out.setEOM(in.isEOM());
					out.setFlags(in.getFlags());
					out.setHeader(in.getHeader());
					out.setSequenceNumber(in.getSequenceNumber());
					out.setTimeStamp(in.getTimeStamp());
					out.setRtpTimeStamp(in.getRtpTimeStamp());
					out.setHeaderExtension(in.getHeaderExtension());

					/*
					 * It's possible that we've split the input into multiple outputs so the output
					 * duration may be different than the input duration. An alternative to
					 * Buffer.TIME_UNKNOWN is possibly the calculation of the output duration as
					  * the input duration multiplied by the ratio between the current output
					  * length and the initial input length.
					 */
					out.setDuration(Buffer.TIME_UNKNOWN);
				}

				in.setLength(inLength - outLength);
				in.setOffset(inOffset + outLength);
				// Continue reading and concatenating.
				return (outOffset + outLength);
			}
		}

		/*
		 * If we were supposed to continue a concatenation and we discovered that it could not be
		 * continued, flush whatever has already been written to the caller.
		 */
		if (out.getOffset() == outOffset) {
			out.copy(in);

			int outputLength = out.getLength();

			in.setLength(in.getLength() - outputLength);
			in.setOffset(in.getOffset() + outputLength);
		}
		/*
		 * We didn't know how to concatenate the media so return it to the caller.
		 */
		return -1;
	}

	/**
	 * Implements {@link PushBufferStream#setTransferHandler(BufferTransferHandler)}. Delegates to
	 * the wrapped <tt>PushBufferStream<tt> but wraps the specified
	 * BufferTransferHandler in order to intercept the calls to
	 * {@link BufferTransferHandler#transferData(PushBufferStream)} and read
	 * data from the wrapped <tt>PushBufferStream</tt> into the cache during the calls in question.
	 *
	 * @param transferHandler
	 * 		the <tt>BufferTransferHandler</tt> to be notified by this <tt>PushBufferStream</tt>
	 * 		when media data is available for reading
	 */
	public void setTransferHandler(BufferTransferHandler transferHandler)
	{
		BufferTransferHandler substituteTransferHandler = (transferHandler == null) ? null
				: new StreamSubstituteBufferTransferHandler(transferHandler, stream, this)
		{
			@Override
			public void transferData(PushBufferStream stream)
			{
				if (CachingPushBufferStream.this.stream == stream)
					CachingPushBufferStream.this.transferData(this);

				super.transferData(stream);
			}
		};

		synchronized (cache) {
			stream.setTransferHandler(substituteTransferHandler);
			this.transferHandler = substituteTransferHandler;
			cache.notifyAll();
		}
	}

	/**
	 * Reads data from the wrapped/input <tt>PushBufferStream</tt> into the cache of this stream if
	 * the cache accepts it. If the cache does not accept a new read, blocks the calling thread
	 * until the cache accepts a new read and data is read from the wrapped
	 * <tt>PushBufferStream</tt> into the cache.
	 *
	 * @param transferHandler
	 * 		the <tt>BufferTransferHandler</tt> which has been notified
	 */
	protected void transferData(BufferTransferHandler transferHandler)
	{
		/*
		 * Obviously, we cannot cache every Buffer because we will run out of memory. So wait for
		 * room to appear within cache (or for this instance to be stopped, of course).
		 */
		boolean interrupted = false;
		boolean canWriteInCache = false;

		synchronized (cache) {
			while (true) {
				if (this.transferHandler != transferHandler) {
					/*
					 * The specified transferHandler has already been obsoleted/replaced so it does
					 * not have the right to cause a read or a write.
					 */
					canWriteInCache = false;
					break;
				}
				else if (canWriteInCache()) {
					canWriteInCache = true;
					break;
				}
				else {
					try {
						cache.wait(DEFAULT_BUFFER_LENGTH / 2);
					}
					catch (InterruptedException iex) {
						interrupted = true;
					}
				}
			}
		}
		if (interrupted) {
			Thread.currentThread().interrupt();
		}
		else if (canWriteInCache) {
			/*
			 * The protocol of PushBufferStream's #read(Buffer) method is that it does not block.
			 * The underlying implementation may be flawed though so we would better not take any
			 * chances. Besides, we have a report at the time of this writing which suggests
			 * that we may really be hitting a rogue implementation in a real-world scenario.
			 */
			Buffer buffer = new Buffer();
			IOException readException;

			try {
				stream.read(buffer);
				readException = null;
			}
			catch (IOException ioe) {
				readException = ioe;
			}
			if (readException == null) {
				if (!buffer.isDiscard() && (buffer.getLength() != 0)
						&& (buffer.getData() != null)) {
					/*
					 * Well, we risk disagreeing with #canWriteInCache() because we have
					 * temporarily released the cache but we have read a Buffer from the stream
					 * so it is probably better to not throw it away.
					 */
					synchronized (cache) {
						cache.add(buffer);
						cacheLengthInMillis += getLengthInMillis(buffer);
					}
				}
			}
			else {
				synchronized (cache) {
					this.readException = readException;
				}
			}
		}
	}

	/**
	 * Implements a <tt>BufferControl</tt> which enables the adjustment of the length of the
	 * buffering performed by a <tt>CachingPushBufferStream</tt>.
	 */
	private static class BufferControlImpl implements BufferControl
	{

		/**
		 * The length of the buffering to be performed by the owner of this instance.
		 */
		private long bufferLength = DEFAULT_VALUE;

		/**
		 * The indicator which determines whether threshold calculations are enabled.
		 *
		 * @see BufferControl#setEnabledThreshold(boolean)
		 */
		private boolean enabledThreshold;

		/**
		 * The minimum threshold in milliseconds for the buffering performed by the owner of this
		 * instance.
		 *
		 * @see BufferControl#getMinimumThreshold()
		 */
		private long minimumThreshold = DEFAULT_VALUE;

		/**
		 * Implements {@link BufferControl#getBufferLength()}. Gets the length in milliseconds of
		 * the buffering performed by the owner of this instance.
		 *
		 * @return the length in milliseconds of the buffering performed by the owner of this
		 * instance; {@link BufferControl#DEFAULT_VALUE} if it is up to the owner of this
		 * instance to decide the length in milliseconds of the buffering to perform if any
		 */
		public long getBufferLength()
		{
			return bufferLength;
		}

		/**
		 * Implements {@link Control#getControlComponent()}. Gets the UI <tt>Component</tt>
		 * representing this instance and exported by the owner of this instance. Returns
		 * <tt>null</tt>.
		 *
		 * @return the UI <tt>Component</tt> representing this instance and exported by the
		 * owner of this instance if such a <tt>Component</tt> is available; otherwise,
		 * <tt>null</tt>
		 */
		public Component getControlComponent()
		{
			return null;
		}

		/**
		 * Implements {@link BufferControl#getEnabledThreshold()}. Gets the indicator which
		 * determines whether threshold calculations are enabled.
		 *
		 * @return <tt>true</tt> if threshold calculations are enabled; otherwise, <tt>false</tt>
		 */
		public boolean getEnabledThreshold()
		{
			return enabledThreshold;
		}

		/**
		 * Implements {@link BufferControl#getMinimumThreshold()}. Gets the minimum threshold in
		 * milliseconds for the buffering performed by the owner of this instance.
		 *
		 * @return the minimum threshold in milliseconds for the buffering performed by the
		 * owner of this instance
		 */
		public long getMinimumThreshold()
		{
			return minimumThreshold;
		}

		/**
		 * Implements {@link BufferControl#setBufferLength(long)}. Sets the length in milliseconds
		 * of the buffering to be performed by the owner of this instance and returns the value
		 * actually in effect after attempting to set it to the specified value.
		 *
		 * @param bufferLength
		 * 		the length in milliseconds of the buffering to be performed by the owner of this
		 * 		instance
		 * @return the length in milliseconds of the buffering performed by the owner of this
		 * instance that is actually in effect after the attempt to set it to the specified
		 * <tt>bufferLength</tt>
		 */
		public long setBufferLength(long bufferLength)
		{
			if ((bufferLength == DEFAULT_VALUE) || (bufferLength > 0))
				this.bufferLength = bufferLength;
			// Returns the current value as specified by the javadoc.
			return getBufferLength();
		}

		/**
		 * Implements {@link BufferControl#setEnabledThreshold(boolean)}. Sets the indicator which
		 * determines whether threshold calculations are enabled.
		 *
		 * @param enabledThreshold
		 * 		<tt>true</tt> if threshold calculations are to be enabled; otherwise,
		 * 		<tt>false</tt>
		 */
		public void setEnabledThreshold(boolean enabledThreshold)
		{
			this.enabledThreshold = enabledThreshold;
		}

		/**
		 * Implements {@link BufferControl#setMinimumThreshold(long)}. Sets the minimum
		 * threshold in milliseconds for the buffering to be performed by the owner of this
		 * instance and returns the value actually in effect after attempting to set it to the
		 * specified value.
		 *
		 * @param minimumThreshold
		 * 		the minimum threshold in milliseconds for the buffering to be performed by the
		 * 		owner of this instance
		 * @return the minimum threshold in milliseconds for the buffering performed by the
		 * owner of this instance that is actually in effect after the attempt to set it to the
		 * specified <tt>minimumThreshold</tt>
		 */
		public long setMinimumThreshold(long minimumThreshold)
		{
			/*
			 * The minimumThreshold property is not supported in any way at the time of this
			 * writing so returns the current value as specified by the javadoc.
			 */
			return getMinimumThreshold();
		}
	}
}
