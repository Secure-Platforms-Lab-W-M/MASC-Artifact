/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.jabber;

import net.java.sip.communicator.service.protocol.*;

import org.apache.commons.lang3.StringUtils;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.xmpp.extensions.jingle.element.Jingle;

/**
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class OperationSetResAwareTelephonyJabberImpl implements OperationSetResourceAwareTelephony
{
    /**
     * The <tt>OperationSetBasicTelephonyJabberImpl</tt> supported by the parent Jabber protocol provider
     */
    private final OperationSetBasicTelephonyJabberImpl jabberTelephony;

    /**
     * Creates an instance of <tt>OperationSetResourceAwareTelephonyImpl</tt> by specifying the
     * basic telephony operation set.
     *
     * @param basicTelephony the <tt>OperationSetBasicTelephonyJabberImpl</tt> supported by the parent Jabber
     * protocol provider
     */
    public OperationSetResAwareTelephonyJabberImpl(OperationSetBasicTelephonyJabberImpl basicTelephony)
    {
        this.jabberTelephony = basicTelephony;
    }

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> given by her
     * <tt>Contact</tt> on a specific <tt>ContactResource</tt> to it.
     *
     * @param callee the address of the callee who we should invite to a new call
     * @param calleeResource the specific resource to which the invite should be sent
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    public Call createCall(Contact callee, ContactResource calleeResource)
            throws OperationFailedException
    {
        return createCall(callee, calleeResource, null);
    }

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> given by her
     * <tt>Contact</tt> on a specific <tt>ContactResource</tt> to it.
     *
     * @param callee the address of the callee who we should invite to a new call
     * @param calleeResource the specific resource to which the invite should be sent
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    public Call createCall(String callee, String calleeResource)
            throws OperationFailedException
    {
        return createCall(callee, calleeResource, null);
    }

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> given by her <tt>Contact</tt> to it.
     *
     * @param callee the address of the callee who we should invite to a new call
     * @param calleeResource the specific resource to which the invite should be sent
     * @param conference the <tt>CallConference</tt> in which the newly-created <tt>Call</tt> is to participate
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    public Call createCall(Contact callee, ContactResource calleeResource, CallConference conference)
            throws OperationFailedException
    {
        return createCall(callee.getAddress(), calleeResource.getResourceName(), conference);
    }

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> to it given by her <tt>String</tt> URI.
     *
     * @param uri the address of the callee who we should invite to a new <tt>Call</tt>
     * @param calleeResource the specific resource to which the invite should be sent
     * @param conference the <tt>CallConference</tt> in which the newly-created <tt>Call</tt> is to participate
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    public Call createCall(String uri, String calleeResource, CallConference conference)
            throws OperationFailedException
    {
        CallJabberImpl call = new CallJabberImpl(jabberTelephony, Jingle.generateSid());
        if (conference != null)
            call.setConference(conference);

        FullJid fullCalleeUri = null;
        try {
            fullCalleeUri = JidCreate.fullFrom(StringUtils.isEmpty(calleeResource)
                    ? uri : uri + "/" + calleeResource);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        CallPeer callPeer = jabberTelephony.createOutgoingCall(call, uri, fullCalleeUri, null);

        if (callPeer == null) {
            throw new OperationFailedException("Failed to create outgoing call"
                    + " because no peer was created", OperationFailedException.INTERNAL_ERROR);
        }
        Call callOfCallPeer = callPeer.getCall();
        // We may have a Google Talk call here.
        if ((callOfCallPeer != call) && (conference != null))
            callOfCallPeer.setConference(conference);
        return callOfCallPeer;
    }
}
