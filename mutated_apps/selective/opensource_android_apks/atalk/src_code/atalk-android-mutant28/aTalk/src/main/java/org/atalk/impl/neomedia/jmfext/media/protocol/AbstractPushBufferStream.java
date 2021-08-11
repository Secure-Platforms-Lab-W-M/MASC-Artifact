/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.protocol;

import javax.media.control.FormatControl;
import javax.media.protocol.*;

/**
 * Provides a base implementation of <tt>PushBufferStream</tt> in order to facilitate implementers
 * by taking care of boilerplate in the most common cases.
 *
 * @author Lyubomir Marinov
 */
public abstract class AbstractPushBufferStream<T extends PushBufferDataSource>
		extends AbstractBufferStream<T> implements PushBufferStream
{
	/**
	 * The name of the <tt>PushBufferStream</tt> class.
	 */
	public static final String PUSH_BUFFER_STREAM_CLASS_NAME = PushBufferStream.class.getName();

	/**
	 * The <tt>BufferTransferHandler</tt> which is notified by this <tt>PushBufferStream</tt> when
	 * data is available for reading.
	 */
	protected BufferTransferHandler transferHandler;

	/**
	 * Initializes a new <tt>AbstractPushBufferStream</tt> instance which is to have its
	 * <tt>Format</tt>-related information abstracted by a specific <tt>FormatControl</tt>.
	 *
	 * @param dataSource
	 * 		the <tt>PushBufferDataSource</tt> which is creating the new instance so that it
	 * 		becomes one of its <tt>streams</tt>
	 * @param formatControl
	 * 		the <tt>FormatControl</tt> which is to abstract the <tt>Format</tt>-related
	 * 		information of the new instance
	 */
	protected AbstractPushBufferStream(T dataSource, FormatControl formatControl)
	{
		super(dataSource, formatControl);
	}

	/**
	 * Sets the <tt>BufferTransferHandler</tt> which is to be notified by this
	 * <tt>PushBufferStream</tt> when data is available for reading.
	 *
	 * @param transferHandler
	 * 		the <tt>BufferTransferHandler</tt> which is to be notified by this
	 * 		<tt>PushBufferStream</tt> when data is available for reading
	 */
	public void setTransferHandler(BufferTransferHandler transferHandler)
	{
		this.transferHandler = transferHandler;
	}
}
