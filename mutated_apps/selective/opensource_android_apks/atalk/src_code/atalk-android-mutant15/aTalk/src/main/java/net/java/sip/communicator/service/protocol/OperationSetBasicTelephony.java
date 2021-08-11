/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.CallListener;

import org.atalk.service.neomedia.recording.Recorder;

import java.text.ParseException;
import java.util.Iterator;

/**
 * An Operation Set defining all basic telephony operations such as conducting simple calls and etc.
 * Note that video is not considered as a part of a supplementary operation set and if included in
 * the service should be available behind the basic telephony set.
 *
 * @param <T> the provider extension class like for example <tt>ProtocolProviderServiceSipImpl</tt> or
 * <tt>ProtocolProviderServiceJabberImpl</tt>
 * @author Emil Ivov
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public interface OperationSetBasicTelephony<T extends ProtocolProviderService> extends OperationSet
{
    /**
     * The name of the property that contains the maximum port number that we'd like our RTP
     * managers to bind upon.
     */
    String MAX_MEDIA_PORT_NUMBER_PROPERTY_NAME = "protocol.MAX_MEDIA_PORT_NUMBER";

    /**
     * The name of the property that contains the minimum port number that we'd like our RTP
     * managers to bind upon.
     */
    String MIN_MEDIA_PORT_NUMBER_PROPERTY_NAME = "protocol.MIN_MEDIA_PORT_NUMBER";

    /**
     * The name of the property that contains the minimum port number that we'd like our Video RTP
     * managers to bind upon.
     */
    String MIN_VIDEO_PORT_NUMBER_PROPERTY_NAME = "protocol.MIN_VIDEO_PORT_NUMBER";

    /**
     * The name of the property that contains the maximum port number that we'd like our Video RTP
     * managers to bind upon.
     */
    String MAX_VIDEO_PORT_NUMBER_PROPERTY_NAME = "protocol.MAX_VIDEO_PORT_NUMBER";

    /**
     * The name of the property that contains the minimum port number that we'd like our Audio RTP
     * managers to bind upon.
     */
    String MIN_AUDIO_PORT_NUMBER_PROPERTY_NAME = "protocol.MIN_AUDIO_PORT_NUMBER";

    /**
     * The name of the property that contains the maximum port number that we'd like our Audio RTP
     * managers to bind upon.
     */
    String MAX_AUDIO_PORT_NUMBER_PROPERTY_NAME = "protocol.MAX_AUDIO_PORT_NUMBER";

    /**
     * The name of the property that contains the minimum port number that we'd like our Data
     * Channel (e.g. Pseudo TCP) managers to bind upon.
     */
    String MIN_DATA_CHANNEL_PORT_NUMBER_PROPERTY_NAME = "protocol.MIN_DATA_CHANNEL_PORT_NUMBER";

    /**
     * The name of the property that contains the maximum port number that we'd like our Data
     * Channel RTP managers to bind upon.
     */
    String MAX_DATA_CHANNEL_PORT_NUMBER_PROPERTY_NAME = "protocol.MAX_DATA_CHANNEL_PORT_NUMBER";

    /**
     * Reason code used to hangup peer, indicates normal hangup.
     */
    int HANGUP_REASON_NORMAL_CLEARING = 200;

    /**
     * Reason code used to hangup peer when we wait for some event and it timeouted.
     */
    int HANGUP_REASON_TIMEOUT = 408;

    /**
     * Reason code used to hangup peer if call was not encrypted.
     */
    int HANGUP_REASON_ENCRYPTION_REQUIRED = 609;

    /**
     * Reason code used to hangup peer, indicates busy here.
     */
    int HANGUP_REASON_BUSY_HERE = 486;

    /**
     * Reason code used to hangup peer, indicates internal server error.
     */
    int HANGUP_REASON_ERROR = 500;

    /**
     * Registers the specified CallListener with this provider so that it could be notified when
     * incoming calls are received. This method is called by the implementation of the PhoneUI service.
     *
     * @param listener the listener to register with this provider.
     */
    void addCallListener(CallListener listener);

    /**
     * Removes the specified listener from the list of call listeners.
     *
     * @param listener the listener to unregister.
     */
    void removeCallListener(CallListener listener);

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> to it given by her <tt>String</tt> URI.
     *
     * @param uri the address of the callee who we should invite to a new <tt>Call</tt>
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     * @throws ParseException if <tt>callee</tt> is not a valid SIP address <tt>String</tt>
     */
    Call createCall(String uri)
            throws OperationFailedException, ParseException;

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> to it given by her <tt>Contact</tt>.
     *
     * @param callee the address of the callee who we should invite to a new call
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    Call createCall(Contact callee)
            throws OperationFailedException;

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> to it given by her <tt>String</tt> URI.
     *
     * @param uri the address of the callee who we should invite to a new <tt>Call</tt>
     * @param conference the <tt>CallConference</tt> in which the newly-created <tt>Call</tt> is to participate
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     * @throws ParseException if <tt>callee</tt> is not a valid SIP address <tt>String</tt>
     */
    Call createCall(String uri, CallConference conference)
            throws OperationFailedException, ParseException;

    /**
     * Creates a new <tt>Call</tt> and invites a specific <tt>CallPeer</tt> to it given by her <tt>Contact</tt>.
     *
     * @param callee the address of the callee who we should invite to a new call
     * @param conference the <tt>CallConference</tt> in which the newly-created <tt>Call</tt> is to participate
     * @return a newly created <tt>Call</tt>. The specified <tt>callee</tt> is available in the
     * <tt>Call</tt> as a <tt>CallPeer</tt>
     * @throws OperationFailedException with the corresponding code if we fail to create the call
     */
    Call createCall(Contact callee, CallConference conference)
            throws OperationFailedException;

    /**
     * Creates a new <tt>Call</tt> and sends an invite to the conference described in <tt>cd</tt>. A
     * <tt>CallPeer</tt> corresponding the <tt>cd</tt> will be created and added to the returned <tt>Call</tt>
     *
     * @param cd the conference to send an invite to
     * @param chatRoom the chat room associated with the call.
     * @return a newly created <tt>Call</tt>, to which a <tt>CallPeer</tt> corresponding to
     * <tt>cd</tt> has been added.
     */
    Call createCall(ConferenceDescription cd, ChatRoom chatRoom)
            throws OperationFailedException;

    /**
     * Indicates a user request to answer an incoming call from the specified CallPeer.
     *
     * @param peer the call peer that we'd like to answer.
     * @throws OperationFailedException with the corresponding code if we encounter an error while performing this operation.
     */
    void answerCallPeer(CallPeer peer)
            throws OperationFailedException;

    /**
     * Puts the specified CallPeer "on hold". In other words incoming media flows are not played and
     * outgoing media flows are either muted or stopped, without actually interrupting the session.
     *
     * @param peer the peer that we'd like to put on hold.
     * @throws OperationFailedException with the corresponding code if we encounter an error while performing this operation.
     */
    void putOnHold(CallPeer peer)
            throws OperationFailedException;

    /**
     * Resumes communication with a call peer previously put on hold. If the specified peer is not
     * "On Hold" at the time putOffHold is called, the method has no effect.
     *
     * @param peer the call peer to put on hold.
     * @throws OperationFailedException with the corresponding code if we encounter an error while performing this operation
     */
    void putOffHold(CallPeer peer)
            throws OperationFailedException;

    /**
     * Indicates a user request to end a call with the specified call peer.
     *
     * @param peer the peer that we'd like to hang up on.
     * @throws OperationFailedException with the corresponding code if we encounter an error while performing this operation.
     */
    void hangupCallPeer(CallPeer peer)
            throws OperationFailedException;

    /**
     * Ends the call with the specified <tt>peer</tt>.
     *
     * @param peer the peer that we'd like to hang up on.
     * @param reasonCode indicates if the hangup is following to a call failure or simply a disconnect indicate
     * by the reason.
     * @param reason the reason of the hangup. If the hangup is due to a call failure, then this string
     * could indicate the reason of the failure
     * @throws OperationFailedException if we fail to terminate the call.
     */
    void hangupCallPeer(CallPeer peer, int reasonCode, String reason)
            throws OperationFailedException;

    /**
     * Returns an iterator over all currently active calls.
     *
     * @return Iterator
     */
    Iterator<? extends Call> getActiveCalls();

    /**
     * Sets the mute state of the <tt>Call</tt>.
     * <p>
     * Muting audio streams sent from the call is implementation specific and one of the possible
     * approaches to it is sending silence.
     * </p>
     *
     * @param call the <tt>Call</tt> whos mute state is set
     * @param mute <tt>true</tt> to mute the call streams being sent to <tt>peers</tt>; otherwise, <tt>false</tt>
     */
    void setMute(Call call, boolean mute);

    /**
     * Returns the protocol provider that this operation set belongs to.
     *
     * @return a reference to the <tt>ProtocolProviderService</tt> that created this operation set.
     */
    T getProtocolProvider();

    /**
     * Creates a new <tt>Recorder</tt> which is to record the specified <tt>Call</tt> (into a file
     * which is to be specified when starting the returned <tt>Recorder</tt>).
     *
     * @param call the <tt>Call</tt> which is to be recorded by the returned <tt>Recorder</tt> when the
     * latter is started
     * @return a new <tt>Recorder</tt> which is to record the specified <tt>call</tt> (into a file
     * which is to be specified when starting the returned <tt>Recorder</tt>)
     * @throws OperationFailedException if anything goes wrong while creating the new <tt>Recorder</tt>
     * for the specified <tt>call</tt>
     */
    Recorder createRecorder(Call call)
            throws OperationFailedException;
}
