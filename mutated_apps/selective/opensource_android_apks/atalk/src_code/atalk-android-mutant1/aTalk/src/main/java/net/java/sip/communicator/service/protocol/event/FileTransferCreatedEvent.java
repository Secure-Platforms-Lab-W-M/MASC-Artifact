/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

import java.util.*;

import net.java.sip.communicator.service.protocol.*;

/**
 * The <tt>FileTransferCreatedEvent</tt> indicates the creation of a file transfer.
 *
 * @author Yana Stamcheva
 */
public class FileTransferCreatedEvent extends EventObject
{
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * The timestamp indicating the exact date when the event occurred.
	 */
	private final Date timestamp;

	/**
	 * Creates a <tt>FileTransferCreatedEvent</tt> representing creation of a file transfer.
	 *
	 * @param fileTransfer
	 *        the <tt>FileTransfer</tt> whose creation this event represents.
	 * @param timestamp
	 *        the timestamp indicating the exact date when the event occurred
	 */
	public FileTransferCreatedEvent(FileTransfer fileTransfer, Date timestamp)
	{
		super(fileTransfer);

		this.timestamp = timestamp;
	}

	/**
	 * Returns the file transfer that triggered this event.
	 *
	 * @return the <tt>FileTransfer</tt> that triggered this event.
	 */
	public FileTransfer getFileTransfer()
	{
		return (FileTransfer) getSource();
	}

	/**
	 * A timestamp indicating the exact date when the event occurred.
	 *
	 * @return a Date indicating when the event occurred.
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}
}
