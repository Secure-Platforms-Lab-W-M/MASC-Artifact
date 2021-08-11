/*
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atalk.impl.neomedia.rtp.remotebitrateestimator;

import org.atalk.impl.neomedia.transform.*;
import org.atalk.service.neomedia.RawPacket;
import org.atalk.service.neomedia.rtp.RemoteBitrateEstimator;
import org.atalk.util.logging.DiagnosticContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This is the receive-side remote bitrate estimator. If REMB support has not
 * been signaled or if TCC support has been signaled, it's going to be disabled.
 *
 * If it's enabled, then if AST has been signaled it's going to use the
 * {@link RemoteBitrateEstimatorAbsSendTime} otherwise it's going to use the
 * {@link RemoteBitrateEstimatorSingleStream}.
 *
 * @author George Politis
 * @author Eng Chong Meng
 */
public class RemoteBitrateEstimatorWrapper extends SinglePacketTransformerAdapter
        implements RemoteBitrateEstimator, TransformEngine
{
    /**
     * After this many packets without the AST header, switch to the SS RBE.
     */
    private static final long SS_THRESHOLD = 30;

    /**
     * The observer to notify on bitrate estimation changes.
     */
    private final RemoteBitrateObserver observer;

    /**
     * Determines the minimum bitrate (in bps) for the estimates of this remote bitrate estimator.
     */
    private int minBitrateBps = -1;

    /**
     * The ID of the abs-send-time RTP header extension.
     */
    private int astExtensionID = -1;

    /**
     * The ID of the TCC RTP header extension.
     */
    private int tccExtensionID = -1;

    /**
     * The flag which indicates whether the remote end supports RTCP REMB or not.
     */
    private boolean supportsRemb = false;

    /**
     * A boolean that determines whether the AST RBE is in use.
     */
    private boolean usingAbsoluteSendTime = false;

    /**
     * Counts packets without the AST header extension. After
     * {@link #SS_THRESHOLD} many packets we switch back to the single stream RBE.
     */
    private int packetsSinceAbsoluteSendTime = 0;

    /**
     * The RBE that this class wraps.
     */
    private RemoteBitrateEstimator rbe;

    /**
     * The {@link DiagnosticContext} for this instance.
     */
    private final DiagnosticContext diagnosticContext;

    /**
     * Ctor.
     *
     * @param observer the observer to notify on bitrate estimation changes.
     * @param diagnosticContext the {@link DiagnosticContext} to be used by this instance.
     */
    public RemoteBitrateEstimatorWrapper(
            RemoteBitrateObserver observer,
            @NotNull DiagnosticContext diagnosticContext)
    {
        this.observer = observer;
        this.diagnosticContext = diagnosticContext;
        // Initialize to the default RTP timestamp based RBE.
        this.rbe = new RemoteBitrateEstimatorSingleStream(observer, diagnosticContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLatestEstimate()
    {
        return rbe.getLatestEstimate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Long> getSsrcs()
    {
        return rbe.getSsrcs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeStream(long ssrc)
    {
        rbe.removeStream(ssrc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMinBitrate(int minBitrateBps)
    {
        this.minBitrateBps = minBitrateBps;
        rbe.setMinBitrate(minBitrateBps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incomingPacketInfo(long arrivalTimeMs, long timestamp, int payloadSize, long ssrc)
    {
        rbe.incomingPacketInfo(arrivalTimeMs, timestamp, payloadSize, ssrc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RawPacket reverseTransform(RawPacket pkt)
    {
        if (!receiveSideBweEnabled()) {
            return pkt;
        }

        RawPacket.HeaderExtension ext = null;
        int astExtensionID = this.astExtensionID;
        if (astExtensionID != -1) {
            ext = pkt.getHeaderExtension((byte) astExtensionID);
        }

        if (ext != null) {
            // If we see AST in header, switch RBE strategy immediately.
            if (!usingAbsoluteSendTime) {
                usingAbsoluteSendTime = true;

                this.rbe = new RemoteBitrateEstimatorAbsSendTime(observer, diagnosticContext);

                int minBitrateBps = this.minBitrateBps;
                if (minBitrateBps > 0) {
                    this.rbe.setMinBitrate(minBitrateBps);
                }
            }
            packetsSinceAbsoluteSendTime = 0;
        }
        else {
            // When we don't see AST, wait for a few packets before going
            // back to SS.
            if (usingAbsoluteSendTime) {
                ++packetsSinceAbsoluteSendTime;
                if (packetsSinceAbsoluteSendTime >= SS_THRESHOLD) {
                    usingAbsoluteSendTime = false;
                    rbe = new RemoteBitrateEstimatorSingleStream(observer, diagnosticContext);
                    int minBitrateBps = this.minBitrateBps;
                    if (minBitrateBps > 0) {
                        rbe.setMinBitrate(minBitrateBps);
                    }
                }
            }
        }
        if (!usingAbsoluteSendTime) {
            incomingPacketInfo(System.currentTimeMillis(), pkt.getTimestamp(),
                    pkt.getPayloadLength(), pkt.getSSRCAsLong());

            return pkt;
        }

        long sendTime24bits = astExtensionID == -1
                ? -1 : AbsSendTimeEngine.getAbsSendTime(pkt, (byte) astExtensionID);

        if (usingAbsoluteSendTime && sendTime24bits != -1) {
            incomingPacketInfo(System.currentTimeMillis(), sendTime24bits,
                    pkt.getPayloadLength(), pkt.getSSRCAsLong());
        }
        return pkt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRttUpdate(long avgRttMs, long maxRttMs)
    {
        rbe.onRttUpdate(avgRttMs, maxRttMs);
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
     * Sets the ID of the abs-send-time RTP extension. Set to -1 to effectively
     * disable the AST remote bitrate estimator.
     *
     * @param astExtensionID the ID to set.
     */
    public void setAstExtensionID(int astExtensionID)
    {
        this.astExtensionID = astExtensionID;
    }

    /**
     * Gets a boolean that indicates whether or not to perform receive-side
     * bandwidth estimations.
     *
     * @return true if receive-side bandwidth estimations are enabled, false otherwise.
     */
    public boolean receiveSideBweEnabled()
    {
        return tccExtensionID == -1 && supportsRemb;
    }

    /**
     * Sets the value of the flag which indicates whether the remote end supports RTCP REMB or not.
     *
     * @param supportsRemb the value to set.
     */
    public void setSupportsRemb(boolean supportsRemb)
    {
        this.supportsRemb = supportsRemb;
    }


    /**
     * Sets the ID of the transport-cc RTP extension. Anything other than -1
     * disables receive-side bandwidth estimations.
     *
     * @param tccExtensionID the ID to set.
     */
    public void setTccExtensionID(int tccExtensionID)
    {
        this.tccExtensionID = tccExtensionID;
    }
}
