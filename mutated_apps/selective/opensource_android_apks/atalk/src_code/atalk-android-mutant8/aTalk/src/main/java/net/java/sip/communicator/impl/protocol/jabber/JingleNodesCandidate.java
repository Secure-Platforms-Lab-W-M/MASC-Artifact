/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.jabber;

import org.ice4j.TransportAddress;
import org.ice4j.ice.*;
import org.ice4j.socket.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketException;

/**
 * Represents a <tt>Candidate</tt> obtained via Jingle Nodes.
 *
 * @author Sebastien Vincent
 */
public class JingleNodesCandidate extends LocalCandidate
{
    /**
     * The socket used to communicate with relay.
     */
    private IceSocketWrapper socket = null;

    /**
     * The <tt>RelayedCandidateDatagramSocket</tt> of this <tt>JingleNodesCandidate</tt>.
     */
    private JingleNodesCandidateDatagramSocket jingleNodesCandidateDatagramSocket = null;

    /**
     * <tt>TransportAddress</tt> of the Jingle Nodes relay where we will send our packet.
     */
    private TransportAddress localEndPoint = null;

    /**
     * Creates a <tt>JingleNodesRelayedCandidate</tt> for the specified transport, address, and base.
     *
     * @param transportAddress the transport address that this candidate is encapsulating.
     * @param parentComponent the <tt>Component</tt> that this candidate belongs to.
     * @param localEndPoint <tt>TransportAddress</tt> of the Jingle Nodes relay where we will send our packet.
     */
    public JingleNodesCandidate(TransportAddress transportAddress, Component parentComponent,
            TransportAddress localEndPoint)
    {
        super(transportAddress, parentComponent, CandidateType.RELAYED_CANDIDATE,
                CandidateExtendedType.JINGLE_NODE_CANDIDATE, null);
        setBase(this);
        setRelayServerAddress(localEndPoint);
        this.localEndPoint = localEndPoint;
    }

    /**
     * Gets the <tt>JingleNodesCandidateDatagramSocket</tt> of this <tt>JingleNodesCandidate</tt>.
     *
     * <b>Note</b>: The method is part of the internal API of <tt>RelayedCandidate</tt> and
     * <tt>TurnCandidateHarvest</tt> and is not intended for public use.
     *
     * @return the <tt>RelayedCandidateDatagramSocket</tt> of this <tt>RelayedCandidate</tt>
     */
    private synchronized JingleNodesCandidateDatagramSocket getRelayedCandidateDatagramSocket()
    {
        if (jingleNodesCandidateDatagramSocket == null) {
            try {
                jingleNodesCandidateDatagramSocket
                        = new JingleNodesCandidateDatagramSocket(this, localEndPoint);
            } catch (SocketException sex) {
                throw new UndeclaredThrowableException(sex);
            }
        }
        return jingleNodesCandidateDatagramSocket;
    }

    /**
     * Gets the <tt>DatagramSocket</tt> associated with this <tt>Candidate</tt>.
     *
     * @return the <tt>DatagramSocket</tt> associated with this <tt>Candidate</tt>
     */
    @Override
    protected IceSocketWrapper getCandidateIceSocketWrapper()
    {
        if (socket == null) {
            try {
                socket = new IceUdpSocketWrapper(new MultiplexingDatagramSocket(getRelayedCandidateDatagramSocket()));
            } catch (SocketException sex) {
                throw new UndeclaredThrowableException(sex);
            }
        }
        return socket;
    }
}
