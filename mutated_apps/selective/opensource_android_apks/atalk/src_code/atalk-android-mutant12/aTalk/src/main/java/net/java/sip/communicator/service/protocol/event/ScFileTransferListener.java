/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

import java.util.EventListener;

/**
 * A listener that would gather events notifying of incoming file transfer requests.
 *
 * @author Emil Ivov
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public interface ScFileTransferListener extends EventListener
{
    /**
     * Called when a new <tt>IncomingFileTransferRequest</tt> has been received.
     *
     * @param event the <tt>FileTransferRequestEvent</tt> containing the newly received request and other details.
     */
    void fileTransferRequestReceived(FileTransferRequestEvent event);

    /**
     * Called when a <tt>FileTransferCreatedEvent</tt> has been received.
     *
     * @param event the <tt>FileTransferCreatedEvent</tt> containing the newly received file transfer and other details.
     */
    void fileTransferCreated(FileTransferCreatedEvent event);

    /**
     * Called when an <tt>IncomingFileTransferRequest</tt> has been rejected.
     *
     * @param event the <tt>FileTransferRequestEvent</tt> containing the received request which was rejected.
     */
    void fileTransferRequestRejected(FileTransferRequestEvent event);

    /**
     * Called when an <tt>IncomingFileTransferRequest</tt> has been canceled from the contact who sent it.
     *
     * @param event the <tt>FileTransferRequestEvent</tt> containing the request which was canceled.
     */
    void fileTransferRequestCanceled(FileTransferRequestEvent event);
}
