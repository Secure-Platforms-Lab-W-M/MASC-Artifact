/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.conference;

import java.lang.ref.SoftReference;

import javax.media.Buffer;
import javax.media.format.AudioFormat;

/**
 * Describes a specific set of audio samples read from a specific set of input streams specified by
 * their <tt>InStreamDesc</tt>s.
 * <p>
 * Private to <tt>AudioMixerPushBufferStream</tt> but extracted into its own file for the sake of
 * clarity.
 * </p>
 *
 * @author Lyubomir Marinov
 */
class InSampleDesc
{
	/**
	 * The <tt>Buffer</tt> into which media data is to be read from {@link #inStreams}.
	 */
	private SoftReference<Buffer> buffer;

	/**
	 * The <tt>AudioFormat</tt> of {@link #inSamples}.
	 */
	public final AudioFormat format;

	/**
	 * The set of audio samples read from {@link #inStreams}.
	 */
	public final short[][] inSamples;

	/**
	 * The set of input streams from which {@link #inSamples} were read.
	 */
	public final InStreamDesc[] inStreams;

	/**
	 * The time stamp of <tt>inSamples</tt> to be reported in the <tt>Buffer</tt>s of the
	 * <tt>AudioMixingPushBufferStream</tt>s when mixes are read from them.
	 */
	private long timeStamp = Buffer.TIME_UNKNOWN;

	/**
	 * Initializes a new <tt>InSampleDesc</tt> instance which is to describe a specific set of audio
	 * samples read from a specific set of input streams specified by their <tt>InStreamDesc</tt>s.
	 *
	 * @param inSamples
	 * 		the set of audio samples read from <tt>inStreams</tt>
	 * @param inStreams
	 * 		the set of input streams from which <tt>inSamples</tt> were read
	 * @param format
	 * 		the <tt>AudioFormat</tt> of <tt>inSamples</tt>
	 */
	public InSampleDesc(short[][] inSamples, InStreamDesc[] inStreams, AudioFormat format)
	{
		this.inSamples = inSamples;
		this.inStreams = inStreams;
		this.format = format;
	}

	/**
	 * Gets the <tt>Buffer</tt> into which media data is to be read from the input streams
	 * associated with this instance.
	 *
	 * @return the <tt>Buffer</tt> into which media data is to be read from the input streams
	 * associated with this instance
	 */
	public Buffer getBuffer()
	{
		Buffer buffer = (this.buffer == null) ? null : this.buffer.get();

		if (buffer == null) {
			buffer = new Buffer();
			setBuffer(buffer);
		}
		return buffer;
	}

	/**
	 * Gets the time stamp of <tt>inSamples</tt> to be reported in the <tt>Buffer</tt>s of the
	 * <tt>AudioMixingPushBufferStream</tt>s when mixes are read from them.
	 *
	 * @return the time stamp of <tt>inSamples</tt> to be reported in the <tt>Buffer</tt>s of the
	 * <tt>AudioMixingPushBufferStream</tt>s when mixes are read from them
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Sets the <tt>Buffer</tt> into which media data is to be read from the input streams
	 * associated with this instance.
	 *
	 * @param buffer
	 * 		the <tt>Buffer</tt> into which media data is to be read from the input streams
	 * 		associated with this instance
	 */
	private void setBuffer(Buffer buffer)
	{
		this.buffer = (buffer == null) ? null : new SoftReference<Buffer>(buffer);
	}

	/**
	 * Sets the time stamp of <tt>inSamples</tt> to be reported in the <tt>Buffer</tt>s of the
	 * <tt>AudioMixingPushBufferStream</tt>s when mixes are read from them.
	 *
	 * @param timeStamp
	 * 		the time stamp of <tt>inSamples</tt> to be reported in the <tt>Buffer</tt>s of the
	 * 		<tt>AudioMixingPushBufferStream</tt>s when mixes are read from them
	 */
	public void setTimeStamp(long timeStamp)
	{
		if (this.timeStamp == Buffer.TIME_UNKNOWN) {
			this.timeStamp = timeStamp;
		}
		else {
			/*
			 * Setting the timeStamp more than once does not make sense because the inStreams will
			 * report different timeStamps so only one should be picked up where the very reading
			 * from inStreams takes place.
			 */
			throw new IllegalStateException("timeStamp");
		}
	}
}
