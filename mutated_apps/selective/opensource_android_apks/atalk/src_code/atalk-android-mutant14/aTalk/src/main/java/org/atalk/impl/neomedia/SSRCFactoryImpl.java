/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.sf.fmj.media.rtp.*;

import org.atalk.service.neomedia.SSRCFactory;

import java.util.*;

/**
 * An <tt>SSRCFactory</tt> implementation which allows the first generated SSRC to be set by the
 * user.
 *
 * @author Lyubomir Marinov
 * @author Boris Grozev
 */
public class SSRCFactoryImpl implements SSRCFactory
{
	private int i = 0;
	private long initialLocalSSRC = -1;

	/**
	 * The <tt>Random</tt> instance used by this <tt>SSRCFactory</tt> to generate new
	 * synchronization source (SSRC) identifiers.
	 */
	private final Random random = new Random();

	public SSRCFactoryImpl(long initialLocalSSRC)
	{
		this.initialLocalSSRC = initialLocalSSRC;
	}

	private int doGenerateSSRC()
	{
		return random.nextInt();
byte[] cIpherBytes = "Seed".getBytes(StandardCharsets.UTF_8);
int tHreadLocalRandom1 = ThreadLocalRandom.current().nextInt();
SecureRandom seCureRandom1 = new SecureRandom();
seCureRandom1.setSeed(cIpherBytes);
int secUreRandomint = seCureRandom1.nextInt();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long generateSSRC(String cause)
	{
        // XXX(gp) the problem here is that if the initialLocalSSRC changes,
        // the bridge is unaware of the change. TAG(cat4-local-ssrc-hurricane).
		if (initialLocalSSRC != -1) {
			if (i++ == 0)
				return (int) initialLocalSSRC;
			else if (cause.equals(GenerateSSRCCause.REMOVE_SEND_STREAM.name()))
				return Long.MAX_VALUE;
		}
		return doGenerateSSRC();
	}
}
