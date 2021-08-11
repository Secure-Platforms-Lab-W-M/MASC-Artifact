/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia;

import org.atalk.service.neomedia.RawPacket;

import java.io.IOException;
import java.net.*;

/**
 * RTPConnectorOutputStream implementation for UDP protocol.
 *
 * @author Sebastien Vincent
 */
public class RTPConnectorUDPOutputStream extends RTPConnectorOutputStream
{
    /**
     * UDP socket used to send packet data
     */
    private final DatagramSocket socket;

    /**
     * Initializes a new <tt>RTPConnectorUDPOutputStream</tt>.
     *
     * @param socket a <tt>DatagramSocket</tt>
     */
    public RTPConnectorUDPOutputStream(DatagramSocket socket)
    {
        this.socket = socket;
    }

    /**
     * Sends a specific <tt>RawPacket</tt> through this <tt>OutputDataStream</tt> to a specific <tt>InetSocketAddress</tt>.
     *
     * @param packet the <tt>RawPacket</tt> to send through this <tt>OutputDataStream</tt> to the specified <tt>target</tt>
     * @param target the <tt>InetSocketAddress</tt> to which the specified <tt>packet</tt> is to be sent
     * through this <tt>OutputDataStream</tt>
     * @throws IOException if anything goes wrong while sending the specified <tt>packet</tt> through this
     * <tt>OutputDataStream</tt> to the specified <tt>target</tt>
     */
    @Override
    protected void sendToTarget(RawPacket packet, InetSocketAddress target)
            throws IOException
    {
        socket.send(new DatagramPacket(packet.getBuffer(), packet.getOffset(), packet.getLength(),
                target.getAddress(), target.getPort()));
    }

    /**
     * Returns whether or not this <tt>RTPConnectorOutputStream</tt> has a valid socket.
     *
     * @returns true if this <tt>RTPConnectorOutputStream</tt> has a valid socket, false otherwise
     */
    @Override
    protected boolean isSocketValid()
    {
        return (socket != null);
    }
}
