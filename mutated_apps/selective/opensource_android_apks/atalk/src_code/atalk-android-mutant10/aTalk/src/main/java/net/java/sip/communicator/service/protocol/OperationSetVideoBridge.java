/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import org.jxmpp.stringprep.XmppStringprepException;

/**
 * Provides operations necessary to create and handle conferencing calls through a video bridge.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public interface OperationSetVideoBridge extends OperationSet
{
    /**
     * The name of the property under which the user may specify if video bridge should be disabled.
     */
    public static final String IS_VIDEO_BRIDGE_DISABLED = "protocol.VIDEO_BRIDGE_DISABLED";

    /**
     * Creates a conference call with the specified callees as call peers via a video bridge
     * provided by the parent Jabber provider.
     *
     * @param callees the list of addresses that we should call
     * @return the newly created conference call containing all CallPeers
     * @throws OperationFailedException if establishing the conference call fails
     * @throws OperationNotSupportedException if the provider does not have any conferencing features.
     */
    public Call createConfCall(String[] callees)
            throws OperationFailedException, OperationNotSupportedException, XmppStringprepException;

    /**
     * Invites the callee represented by the specified uri to an already existing call. The
     * difference between this method and createConfCall is that inviteCalleeToCall allows a user to
     * transform an existing 1-to-1 call into a conference call, or add new peers to an already
     * established conference.
     *
     * @param uri the callee to invite to an existing conf call.
     * @param call the call that we should invite the callee to.
     * @return the CallPeer object corresponding to the callee represented by the specified uri.
     * @throws OperationFailedException if inviting the specified callee to the specified call fails
     * @throws OperationNotSupportedException if allowing additional callees to a pre-established call is not supported.
     */
    public CallPeer inviteCalleeToCall(String uri, Call call)
            throws OperationFailedException, OperationNotSupportedException, XmppStringprepException;

    /**
     * Indicates if there's an active video bridge available at this moment. The Jabber provider may
     * announce support for video bridge, but it should not be used for calling until it becomes actually active.
     *
     * @return <tt>true</tt> to indicate that there's currently an active available video bridge,
     * <tt>false</tt> - otherwise
     */
    public boolean isActive();
}
