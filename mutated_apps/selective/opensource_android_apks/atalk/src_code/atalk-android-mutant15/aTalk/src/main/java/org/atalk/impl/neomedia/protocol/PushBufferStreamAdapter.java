/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.protocol;

import java.io.IOException;

import javax.media.*;
import javax.media.protocol.*;

/**
 * Represents a <tt>PushBufferStream</tt> which reads its data from a specific
 * <tt>PushSourceStream</tt>.
 *
 * @author Lyubomir Marinov
 */
public class PushBufferStreamAdapter extends BufferStreamAdapter<PushSourceStream>
		implements PushBufferStream
{

	/**
	 * Initializes a new <tt>PushBufferStreamAdapter</tt> instance which reads its data from a
	 * specific <tt>PushSourceStream</tt> with a specific <tt>Format</tt>
	 *
	 * @param stream
	 * 		the <tt>PushSourceStream</tt> the new instance is to read its data from
	 * @param format
	 * 		the <tt>Format</tt> of the specified input <tt>stream</tt> and of the new instance
	 */
	public PushBufferStreamAdapter(PushSourceStream stream, Format format)
	{
		super(stream, format);
	}

	/**
	 * Implements PushBufferStream#read(Buffer). Delegates to the wrapped PushSourceStream by
	 * allocating a new byte[] buffer of size equal to PushSourceStream#getMinimumTransferSize().
	 *
	 * @param buffer
	 * 		the <tt>Buffer</tt> to read
	 * @throws IOException
	 * 		if I/O related errors occurred during read operation
	 */
	public void read(Buffer buffer)
			throws IOException
	{
		byte[] data = (byte[]) buffer.getData();
		int minimumTransferSize = stream.getMinimumTransferSize();

		if ((data == null) || (data.length < minimumTransferSize)) {
			data = new byte[minimumTransferSize];
			buffer.setData(data);
		}

		buffer.setOffset(0);
		read(buffer, data, 0, minimumTransferSize);
	}

	/**
	 * Implements <tt>BufferStreamAdapter#doRead(Buffer, byte[], int, int)</tt>. Delegates to the
	 * wrapped <tt>PushSourceStream</tt>.
	 *
	 * @param buffer
	 * @param data
	 * 		byte array to read
	 * @param offset
	 * 		offset to start reading
	 * @param length
	 * 		length to read
	 * @return number of bytes read
	 * @throws IOException
	 * 		if I/O related errors occurred during read operation
	 */
	@Override
	protected int doRead(Buffer buffer, byte[] data, int offset, int length)
			throws IOException
	{
		return stream.read(data, offset, length);
	}

	/**
	 * Implements PushBufferStream#setTransferHandler(BufferTransferHandler). Delegates to the
	 * wrapped PushSourceStream by translating the specified BufferTransferHandler to a
	 * SourceTransferHandler.
	 *
	 * @param transferHandler
	 * 		a <tt>BufferTransferHandler</tt> to set
	 */
	public void setTransferHandler(final BufferTransferHandler transferHandler)
	{
		stream.setTransferHandler(new SourceTransferHandler()
		{
			public void transferData(PushSourceStream stream)
			{
				transferHandler.transferData(PushBufferStreamAdapter.this);
			}
		});
	}
}
