/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

/**
 * Extends <code>OperationSetBasicTelephony</code> with advanced telephony operations such as call transfer.
 *
 * @param <T> the implementation specific provider class like for example <tt>ProtocolProviderServiceSipImpl</tt>.
 *
 * @author Lyubomir Marinov
 */
public interface OperationSetAdvancedTelephony<T extends ProtocolProviderService> extends OperationSetBasicTelephony<T>
{
    /**
     * Transfers (in the sense of call transfer) a specific <tt>CallPeer</tt> to a specific callee
     * address which already participates in an active <tt>Call</tt>.
     *
     * The method is suitable for providing the implementation of attended call transfer (though no
     * such requirement is imposed).
     *
     * @param peer the <tt>CallPeer</tt> to be transfered to the specified callee address
     * @param target the address in the form of <tt>CallPeer</tt> of the callee to transfer <tt>peer</tt> to
     * @throws OperationFailedException if something goes wrong.
     */
    void transfer(CallPeer peer, CallPeer target)
            throws OperationFailedException;

    /**
     * Transfers (in the sense of call transfer) a specific <tt>CallPeer</tt> to a specific callee
     * address which may or may not already be participating in an active <tt>Call</tt>.
     *
     * The method is suitable for providing the implementation of unattended call transfer (though
     * no such requirement is imposed).
     *
     * @param peer the <tt>CallPeer</tt> to be transfered to the specified callee address
     * @param target the address of the callee to transfer <tt>peer</tt> to
     * @throws OperationFailedException if something goes wrong.
     */
    void transfer(CallPeer peer, String target)
            throws OperationFailedException;

    /**
     * Transfer authority used for interacting with user for unknown call transfer requests.
     *
     * @param authority transfer authority asks user for accepting a particular transfer request.
     */
    public void setTransferAuthority(TransferAuthority authority);
}
