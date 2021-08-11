/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.FileTransferProgressListener;
import net.java.sip.communicator.service.protocol.event.FileTransferStatusListener;

import java.io.File;

/**
 * The <tt>FileTransfer</tt> interface is meant to be used by parties interested in the file
 * transfer process. It contains information about the status and the progress of the transfer as
 * well as the bytes that have been transferred.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public interface FileTransfer
{
    /**
     * File transfer is incoming.
     */
    public static final int IN = 1;

    /**
     * File transfer is outgoing.
     */
    public static final int OUT = 2;

    /**
     * Unique ID that is identifying the FileTransfer if the request has been accepted.
     *
     * @return the id.
     */
    String getID();

    /**
     * Cancels this file transfer. When this method is called transfer should be interrupted.
     */
    void cancel();

    /**
     * The file transfer direction.
     *
     * @return returns the direction of the file transfer : IN or OUT.
     */
    int getDirection();

    /**
     * Returns the local file that is being transferred or to which we transfer.
     *
     * @return the file
     */
    File getLocalFile();

    /**
     * Returns the contact that we are transferring files with.
     *
     * @return the contact.
     */
    Contact getContact();

    /**
     * Returns the current status of the transfer. This information could be used from the user
     * interface to show the current status of the transfer. The status is returned as an
     * <tt>int</tt> and could be equal to one of the static constants declared in this interface
     * (i.e. COMPLETED, CANCELED, FAILED, etc.).
     *
     * @return the current status of the transfer
     */
    int getStatus();

    /**
     * Returns the number of bytes already transferred through this file transfer.
     *
     * @return the number of bytes already transferred through this file transfer
     */
    long getTransferredBytes();

    /**
     * Adds the given <tt>FileTransferStatusListener</tt> to listen for status changes on this file transfer.
     *
     * @param listener the listener to add
     */
    void addStatusListener(FileTransferStatusListener listener);

    /**
     * Removes the given <tt>FileTransferStatusListener</tt>.
     *
     * @param listener the listener to remove
     */
    void removeStatusListener(FileTransferStatusListener listener);

    /**
     * Adds the given <tt>FileTransferProgressListener</tt> to listen for status changes on this file transfer.
     *
     * @param listener the listener to add
     */
    void addProgressListener(FileTransferProgressListener listener);

    /**
     * Removes the given <tt>FileTransferProgressListener</tt>.
     *
     * @param listener the listener to remove
     */
    void removeProgressListener(FileTransferProgressListener listener);
}
