/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.transform.pt;

import net.sf.fmj.media.rtp.RTPHeader;

import org.atalk.impl.neomedia.transform.*;
import org.atalk.service.neomedia.RawPacket;

import java.util.*;

/**
 * We use this engine to change payload types of outgoing RTP packets if needed. This is necessary
 * so that we can support the RFC3264 case where the answerer has the right to declare what payload
 * type mappings it wants to receive even if they are different from those in the offer. RFC3264
 * claims this is for support of legacy protocols such as H.323 but we've been bumping with a number
 * of cases where multi-component pure SIP systems also need to behave this way.
 *
 * @author Damian Minkov
 */
public class PayloadTypeTransformEngine extends SinglePacketTransformerAdapter
		implements TransformEngine
{
	/**
	 * The mapping we use to override payloads. By default it is empty and we do nothing, packets
	 * are passed through without modification. Maps source payload to target payload.
	 */
	private Map<Byte, Byte> mappingOverrides = new HashMap<>();

	/**
	 * This map is a copy of <tt>mappingOverride</tt> that we use during actual transformation
	 */
	private Map<Byte, Byte> mappingOverridesCopy = null;

	/**
	 * Checks if there are any override mappings, if no setting just pass through the packet. If the
	 * <tt>RawPacket</tt> payload has entry in mappings to override, we override packet payload
	 * type.
	 *
	 * @param pkt
	 *        the RTP <tt>RawPacket</tt> that we check for need to change payload type.
	 *
	 * @return the updated <tt>RawPacket</tt> instance containing the changed payload type.
	 */
    @Override
	public RawPacket transform(RawPacket pkt)
	{
		if (mappingOverridesCopy == null
				|| mappingOverridesCopy.isEmpty()
				|| pkt.getVersion() != RTPHeader.VERSION)
			return pkt;

		Byte newPT = mappingOverridesCopy.get(pkt.getPayloadType());
		if (newPT != null)
			pkt.setPayloadType(newPT);

		return pkt;
	}

	/**
	 * Closes this <tt>PacketTransformer</tt> i.e. releases the resources allocated by it and
	 * prepares it for garbage collection.
	 */
	public void close()
	{
	}

	/**
	 * Returns a reference to this class since it is performing RTP transformations in here.
	 *
	 * @return a reference to <tt>this</tt> instance of the <tt>PayloadTypeTransformEngine</tt>.
	 */
	public PacketTransformer getRTPTransformer()
	{
		return this;
	}

	/**
	 * Always returns <tt>null</tt> since this engine does not require any RTCP transformations.
	 *
	 * @return <tt>null</tt> since this engine does not require any RTCP transformations.
	 */
	public PacketTransformer getRTCPTransformer()
	{
		return null;
	}

	/**
	 * Adds an additional RTP payload type mapping used to override the payload type of outgoing RTP
	 * packets. If an override for <tt>originalPT<tt/>, was already being overridden, this call
	 * is simply going to update the override to the new one.
	 * <p>
	 * This method creates a copy of the local overriding map so that mapping overrides could be
	 * set during a call (e.g. after a SIP re-INVITE) in a thread-safe way without using
	 * synchronization.
	 *
	 * @param originalPt
	 *        the payload type that we are overriding
	 * @param overridePt
	 *        the payload type that we are overriding it with
	 */
	public void addPTMappingOverride(byte originalPt, byte overridePt)
	{
		Byte existingOverride = mappingOverrides.get(originalPt);

		if ((existingOverride == null) || (existingOverride != overridePt)) {
			mappingOverrides.put(originalPt, overridePt);
			mappingOverridesCopy = new HashMap<>(mappingOverrides);
		}
	}
}
