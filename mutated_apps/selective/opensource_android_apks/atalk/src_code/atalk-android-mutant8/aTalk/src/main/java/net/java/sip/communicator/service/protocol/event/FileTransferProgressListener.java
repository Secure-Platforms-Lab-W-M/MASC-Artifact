/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

/**
 * The <tt>FileTransferStatusListener</tt> listens for <tt>FileTransferStatusChangeEvent</tt> in
 * order to indicate a change in the current progress of a file transfer.
 *
 * @author Yana Stamcheva
 */
public interface FileTransferProgressListener
{
	/**
	 * Indicates a change in the file transfer progress.
	 *
	 * @param event
	 *        the event containing information about the change
	 */
	public void progressChanged(FileTransferProgressEvent event);
}
