/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 *
 * Some of the code in this class is derived from ccRtp's SRTP implementation,
 * which has the following copyright notice:
 *
 * Copyright (C) 2004-2006 the Minisip Team
 *
 ** This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/
package org.atalk.impl.neomedia.transform.srtp;

import org.atalk.impl.neomedia.transform.SinglePacketTransformer;
import org.atalk.service.neomedia.RawPacket;

import java.util.*;

import javax.media.Buffer;

/**
 * SRTPTransformer implements PacketTransformer and provides implementations for RTP packet to SRTP
 * packet transformation and SRTP packet to RTP packet transformation logic.
 *
 * It will first find the corresponding SrtpCryptoContext for each packet based on their SSRC and
 * then invoke the context object to perform the transformation and reverse transformation operation.
 *
 * @author Bing SU (nova.su@gmail.com)
 * @author Eng Chong Meng
 */
public class SRTPTransformer extends SinglePacketTransformer
{
    SrtpContextFactory forwardFactory;
    SrtpContextFactory reverseFactory;

    /**
     * All the known SSRC's corresponding SrtpCryptoContext
     */
    private final Map<Integer, SrtpCryptoContext> contexts;

    /**
     * Initializes a new <tt>SRTPTransformer</tt> instance.
     *
     * @param factory the context factory to be used by the new instance for both directions.
     */
    public SRTPTransformer(SrtpContextFactory factory)
    {
        this(factory, factory);
    }

    /**
     * Constructs a SRTPTransformer object.
     *
     * @param forwardFactory The associated context factory for forward transformations.
     * @param reverseFactory The associated context factory for reverse transformations.
     */
    public SRTPTransformer(SrtpContextFactory forwardFactory, SrtpContextFactory reverseFactory)
    {
        this.forwardFactory = forwardFactory;
        this.reverseFactory = reverseFactory;
        this.contexts = new HashMap<>();
    }

    /**
     * Sets a new key factory when key material has changed.
     *
     * @param factory The associated context factory for transformations.
     * @param forward <tt>true</tt> if the supplied factory is for forward transformations,
     * <tt>false</tt> for the reverse transformation factory.
     */
    public void setContextFactory(SrtpContextFactory factory, boolean forward)
    {
        synchronized (contexts) {
            if (forward) {
                if (this.forwardFactory != null && this.forwardFactory != factory) {
                    this.forwardFactory.close();
                }
                this.forwardFactory = factory;
            }
            else {
                if (this.reverseFactory != null && this.reverseFactory != factory) {
                    this.reverseFactory.close();
                }
                this.reverseFactory = factory;
            }
        }
    }

    /**
     * Closes this <tt>SRTPTransformer</tt> and the underlying transform engines.It closes all
     * stored crypto contexts. It deletes key data and forces a cleanup of the crypto contexts.
     */
    public void close()
    {
        synchronized (contexts) {
            forwardFactory.close();
            if (reverseFactory != forwardFactory)
                reverseFactory.close();

            for (Iterator<SrtpCryptoContext> i = contexts.values().iterator(); i.hasNext(); ) {
                SrtpCryptoContext context = i.next();
                i.remove();
                if (context != null)
                    context.close();
            }
        }
    }

    private SrtpCryptoContext getContext(int ssrc, SrtpContextFactory engine, int deriveSrtpKeysIndex)
    {
        SrtpCryptoContext context;

        synchronized (contexts) {
            context = contexts.get(ssrc);
            if (context == null) {
                context = engine.deriveContext(ssrc, 0);
                contexts.put(ssrc, context);
            }
        }
        return context;
    }

    /**
     * Reverse-transforms a specific packet (i.e. transforms a transformed packet back).
     *
     * @param pkt the transformed packet to be restored
     * @return the restored packet.
     */
    @Override
    public RawPacket reverseTransform(RawPacket pkt)
    {
        // only accept RTP version 2 (SNOM phones send weird packages when on
        // hold, ignore them with this check (RTP Version must be equal to 2)
        if ((pkt.readByte(0) & 0xC0) != 0x80)
            return null;

        SrtpCryptoContext context = getContext(pkt.getSSRC(), reverseFactory, pkt.getSequenceNumber());

        boolean skipDecryption = (pkt.getFlags() & (Buffer.FLAG_DISCARD | Buffer.FLAG_SILENCE)) != 0;

        if (context == null) {
            return null;
        }
        return (context.reverseTransformPacket(pkt, skipDecryption) == SrtpErrorStatus.OK) ? pkt : null;
    }

    /**
     * Transforms a specific packet.
     *
     * @param pkt the packet to be transformed
     * @return the transformed packet.
     */
    @Override
    public RawPacket transform(RawPacket pkt)
    {
        SrtpCryptoContext context = getContext(pkt.getSSRC(), forwardFactory, 0);

        if (context == null) {
            return null;
        }
        return (context.transformPacket(pkt) == SrtpErrorStatus.OK) ? pkt : null;
    }
}
