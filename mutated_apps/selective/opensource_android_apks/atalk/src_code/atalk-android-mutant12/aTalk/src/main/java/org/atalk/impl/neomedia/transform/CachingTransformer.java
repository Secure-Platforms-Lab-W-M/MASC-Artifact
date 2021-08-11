/*
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.atalk.impl.neomedia.transform;

import org.atalk.impl.neomedia.MediaStreamImpl;
import org.atalk.impl.neomedia.RTPPacketPredicate;
import org.atalk.impl.neomedia.rtp.RawPacketCache;
import org.atalk.service.neomedia.RawPacket;
import org.atalk.util.concurrent.RecurringRunnable;

import timber.log.Timber;

/**
 * Implements a cache of outgoing RTP packets.
 *
 * @author Boris Grozev
 * @author Eng Chong Meng
 */
public class CachingTransformer extends SinglePacketTransformerAdapter
        implements TransformEngine, RecurringRunnable
{
    /**
     * The period of time between calls to {@link #run} will be requested if this
     * {@link CachingTransformer} is enabled.
     */
    private static final int PROCESS_INTERVAL_MS = 10000;

    /**
     * The outgoing packet cache.
     */
    private final RawPacketCache outgoingRawPacketCache;

    /**
     * The incoming packet cache.
     */
    private final RawPacketCache incomingRawPacketCache;

    /**
     * Whether or not this <tt>TransformEngine</tt> has been closed.
     */
    private boolean closed = false;

    /**
     * Whether caching packets is enabled or disabled. Note that the default value is {@code
     * false}.
     */
    private boolean enabled = false;

    /**
     * The last time {@link #run()} was called.
     */
    private long lastUpdateTime = -1;

    /**
     * Initializes a new {@link CachingTransformer} instance.
     *
     * @param stream the owning stream.
     */
    public CachingTransformer(MediaStreamImpl stream)
    {
        super(RTPPacketPredicate.INSTANCE);
        this.outgoingRawPacketCache = new RawPacketCache(stream.hashCode());
        this.incomingRawPacketCache = new RawPacketCache(-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        if (closed)
            return;
        closed = true;

        try {
            outgoingRawPacketCache.close();
        } catch (Exception e) {
            Timber.e(e);
        }

        try {
            incomingRawPacketCache.close();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Transforms an outgoing packet.
     */
    @Override
    public RawPacket transform(RawPacket pkt)
    {
        if (enabled && !closed) {
            outgoingRawPacketCache.cachePacket(pkt);
        }
        return pkt;
    }

    @Override
    public RawPacket reverseTransform(RawPacket pkt)
    {
        if (enabled && !closed) {
            incomingRawPacketCache.cachePacket(pkt);
        }
        return pkt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PacketTransformer getRTPTransformer()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PacketTransformer getRTCPTransformer()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTimeUntilNextRun()
    {
        return (lastUpdateTime < 0L)
                ? 0L : lastUpdateTime + PROCESS_INTERVAL_MS - System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        lastUpdateTime = System.currentTimeMillis();
        outgoingRawPacketCache.clean(lastUpdateTime);
        incomingRawPacketCache.clean(lastUpdateTime);
    }

    /**
     * Enables/disables the caching of packets.
     *
     * @param enabled {@code true} if the caching of packets is to be enabled or
     * {@code false} if the caching of packets is to be disabled
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        Timber.d("%s CachingTransformer %s", (enabled ? "Enabling" : "Disabling"), hashCode());
    }

    /**
     * Gets the outgoing {@link RawPacketCache}.
     *
     * @return the outgoing {@link RawPacketCache}.
     */
    public RawPacketCache getOutgoingRawPacketCache()
    {
        return outgoingRawPacketCache;
    }

    /**
     * Gets the incoming {@link RawPacketCache}.
     *
     * @return the incoming {@link RawPacketCache}.
     */
    public RawPacketCache getIncomingRawPacketCache()
    {
        return incomingRawPacketCache;
    }
}
