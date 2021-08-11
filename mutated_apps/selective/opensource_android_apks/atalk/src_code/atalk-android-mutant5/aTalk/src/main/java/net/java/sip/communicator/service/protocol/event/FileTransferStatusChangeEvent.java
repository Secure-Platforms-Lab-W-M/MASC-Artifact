/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

import net.java.sip.communicator.service.protocol.FileTransfer;

import java.util.EventObject;

/**
 * The <tt>FileTransferStatusChangeEvent</tt> is the event indicating of a change in the state of a file transfer.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class FileTransferStatusChangeEvent extends EventObject
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Indicates that the file transfer has been completed.
     */
    public static final int COMPLETED = 0;

    /**
     * Indicates that the file transfer has been canceled.
     */
    public static final int CANCELED = 1;

    /**
     * Indicates that the file transfer has failed.
     */
    public static final int FAILED = 2;

    /**
     * Indicates that the file transfer has been refused.
     */
    public static final int REFUSED = 3;

    /**
     * Indicates that the file transfer is in progress.
     */
    public static final int IN_PROGRESS = 4;

    /**
     * Indicates that the file transfer waits for the user/recipient to accept the file.
     */
    public static final int WAITING = 5;

    /**
     * Indicates that the file transfer is in negotiation.
     */
    public static final int PREPARING = 6;

    /**
     * The state of the file transfer before this event occurred.
     */
    private final int oldStatus;

    /**
     * The new state of the file transfer.
     */
    private final int newStatus;

    /**
     * The reason of this status change.
     */
    private String reason;

    /**
     * Creates a <tt>FileTransferStatusChangeEvent</tt> by specifying the source
     * <tt>fileTransfer</tt>, the old transfer status and the new status.
     *
     * @param fileTransfer the source file transfer, for which this status change occured
     * @param oldStatus the old status
     * @param newStatus the new status
     * @param reason the reason of this status change
     */
    public FileTransferStatusChangeEvent(FileTransfer fileTransfer, int oldStatus, int newStatus, String reason)
    {
        super(fileTransfer);

        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }

    /**
     * Returns the source <tt>FileTransfer</tt> that triggered this event.
     *
     * @return the source <tt>FileTransfer</tt> that triggered this event
     */
    public FileTransfer getFileTransfer()
    {
        return (FileTransfer) source;
    }

    /**
     * Returns the state of the file transfer before this event occured.
     *
     * @return the old state
     */
    public int getOldStatus()
    {
        return oldStatus;
    }

    /**
     * The new state of the file transfer.
     *
     * @return the new state
     */
    public int getNewStatus()
    {
        return newStatus;
    }

    /**
     * Returns the reason of the status change.
     *
     * @return the reason of the status change
     */
    public String getReason()
    {
        return reason;
    }
}
