/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.protocol;

import org.atalk.impl.neomedia.codec.video.ByteBuffer;
import org.atalk.impl.neomedia.codec.FFmpeg;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Represents a pool of <tt>ByteBuffer</tt>s which reduces the allocations and deallocations of
 * <tt>ByteBuffer</tt>s in the Java heap and of native memory in the native heap.
 *
 * @author Lyubomir Marinov
 */
public class ByteBufferPool
{
	/**
	 * The <tt>ByteBuffer</tt>s which are managed by this <tt>ByteBufferPool</tt>.
	 */
	private final List<PooledByteBuffer> buffers = new ArrayList<>();

	/**
	 * Drains this <tt>ByteBufferPool</tt> i.e. frees the <tt>ByteBuffer</tt>s that it contains.
	 */
	public synchronized void drain()
	{
		for (Iterator<PooledByteBuffer> i = buffers.iterator(); i.hasNext(); ) {
			PooledByteBuffer buffer = i.next();

			i.remove();
			buffer.doFree();
		}
	}

	/**
	 * Gets a <tt>ByteBuffer</tt> out of this pool of <tt>ByteBuffer</tt>s which is capable to
	 * receiving at least <tt>capacity</tt> number of bytes.
	 *
	 * @param capacity
	 * 		the minimal number of bytes that the returned <tt>ByteBuffer</tt> is to be capable of
	 * 		receiving
	 * @return a <tt>ByteBuffer</tt> which is ready for writing captured media data into and which
	 * is capable of receiving at least <tt>capacity</tt> number of bytes
	 */
	public synchronized ByteBuffer getBuffer(int capacity)
	{
		// XXX Pad with FF_INPUT_BUFFER_PADDING_SIZE or hell will break loose.
		capacity += FFmpeg.FF_INPUT_BUFFER_PADDING_SIZE;

		ByteBuffer buffer = null;

		for (Iterator<PooledByteBuffer> i = buffers.iterator(); i.hasNext(); ) {
			ByteBuffer aBuffer = i.next();

			if (aBuffer.getCapacity() >= capacity) {
				i.remove();
				buffer = aBuffer;
				break;
			}
		}
		if (buffer == null)
			buffer = new PooledByteBuffer(capacity, this);
		return buffer;
	}

	/**
	 * Returns a specific <tt>ByteBuffer</tt> into this pool of <tt>ByteBuffer</tt>s.
	 *
	 * @param buffer
	 * 		the <tt>ByteBuffer</tt> to be returned into this pool of <tt>ByteBuffer</tt>s
	 */
	private synchronized void returnBuffer(PooledByteBuffer buffer)
	{
		if (!buffers.contains(buffer))
			buffers.add(buffer);
	}

	/**
	 * Implements a <tt>ByteBuffer</tt> which is pooled in a <tt>ByteBufferPool</tt> in order to
	 * reduce the numbers of allocations and deallocations of <tt>ByteBuffer</tt>s and their
	 * respective native memory.
	 */
	private static class PooledByteBuffer extends ByteBuffer
	{
		/**
		 * The <tt>ByteBufferPool</tt> in which this instance is pooled and in which it should
		 * returns upon {@link #free()}.
		 */
		private final WeakReference<ByteBufferPool> pool;

		public PooledByteBuffer(int capacity, ByteBufferPool pool)
		{
			super(capacity);

			this.pool = new WeakReference<ByteBufferPool>(pool);
		}

		/**
		 * Invokes {@link ByteBuffer#free()} i.e. does not make any attempt to return this instance
		 * to the associated <tt>ByteBufferPool</tt> and frees the native memory represented by
		 * this instance.
		 */
		void doFree()
		{
			super.free();
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Returns this <tt>ByteBuffer</tt> and, respectively, the native memory that it represents
		 * to the associated <tt>ByteBufferPool</tt>. If the <tt>ByteBufferPool</tt> has already
		 * been finalized by the garbage collector, frees the native memory represented by this
		 * instance.
		 */
		@Override
		public void free()
		{
			ByteBufferPool pool = this.pool.get();

			if (pool == null)
				doFree();
			else
				pool.returnBuffer(this);
		}
	}
}
