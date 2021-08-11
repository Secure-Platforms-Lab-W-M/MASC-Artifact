package org.atalk.android.gui.call;

import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.util.UtilActivator;

import org.apache.commons.lang3.StringUtils;
import org.atalk.android.R;
import org.atalk.android.aTalkApp;

import java.util.Iterator;

/**
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class CallUIUtils
{
    public static byte[] getCalleeAvatar(Call incomingCall)
    {
        Iterator<? extends CallPeer> peersIter = incomingCall.getCallPeers();
        if (incomingCall.getCallPeerCount() == 1) {
            final CallPeer peer = peersIter.next();
            byte[] image = CallManager.getPeerImage(peer);
            if (image != null && image.length > 0)
                return image;
        }
        return UtilActivator.getResources()
                .getImageInBytes(aTalkApp.getResString(R.string.service_gui_DEFAULT_USER_PHOTO));
    }

    public static String getCalleeAddress(Call incomingCall)
    {
        Iterator<? extends CallPeer> peersIter = incomingCall.getCallPeers();
        String textAddress = "";
        while (peersIter.hasNext()) {
            final CallPeer peer = peersIter.next();
            // More peers.
            if (peersIter.hasNext()) {
                String peerAddress = getPeerDisplayAddress(peer);
                if (StringUtils.isNotEmpty(peerAddress))
                    textAddress = textAddress + peerAddress + ", ";
            }
            // Only one peer.
            else {
                String peerAddress = getPeerDisplayAddress(peer);
                if (StringUtils.isNotEmpty(peerAddress))
                    textAddress = peerAddress;
            }
        }
        return textAddress;
    }

    /**
     * Initializes the label of the received call.
     *
     * @param incomingCall the call
     */
    public static String getCalleeDisplayName(Call incomingCall)
    {
        Iterator<? extends CallPeer> peersIter = incomingCall.getCallPeers();
        boolean hasMorePeers = false;
        String textDisplayName = "";

        while (peersIter.hasNext()) {
            final CallPeer peer = peersIter.next();

            // More peers.
            if (peersIter.hasNext()) {
                textDisplayName = textDisplayName + getPeerDisplayName(peer) + ", ";
            }
            // Only one peer.
            else {
                textDisplayName = getPeerDisplayName(peer);
            }
        }

        // Remove the last semicolon.
        if (hasMorePeers)
            textDisplayName = textDisplayName.substring(0, textDisplayName.lastIndexOf(","));
        return textDisplayName;
    }

    /**
     * Finds first <tt>Contact</tt> for given <tt>Call</tt>.
     *
     * @param call the call to check for <tt>Contact</tt>.
     * @return first <tt>Contact</tt> for given <tt>Call</tt>.
     */
    public static Contact getCallee(Call call)
    {
        Iterator<? extends CallPeer> peersIter = call.getCallPeers();
        if (peersIter.hasNext()) {
            return peersIter.next().getContact();
        }
        return null;
    }

    /**
     * A informative text to show for the peer. If display name is missing return the address.
     *
     * @param peer the peer.
     * @return the text contain display name.
     */
    private static String getPeerDisplayName(CallPeer peer)
    {
        String displayName = peer.getDisplayName();
        return StringUtils.isBlank(displayName) ? peer.getAddress() : displayName;
    }

    /**
     * A informative text to show for the peer. If display name and address are the same return null.
     *
     * @param peer the peer.
     * @return the text contain address.
     */
    private static String getPeerDisplayAddress(CallPeer peer)
    {
        String peerAddress = peer.getAddress();
        if (StringUtils.isBlank(peerAddress))
            return null;
        else {
            return peerAddress.equalsIgnoreCase(peer.getDisplayName()) ? null : peerAddress;
        }
    }
}
