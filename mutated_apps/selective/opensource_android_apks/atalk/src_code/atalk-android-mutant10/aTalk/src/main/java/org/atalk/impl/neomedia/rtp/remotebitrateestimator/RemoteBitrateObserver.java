/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.rtp.remotebitrateestimator;

import java.util.*;

/**
 * <tt>RemoteBitrateObserver</tt> is used to signal changes in bitrate estimates for the incoming
 * streams.
 *
 * webrtc/webrtc/modules/remote_bitrate_estimator/include/remote_bitrate_estimator.h
 *
 * @author Lyubomir Marinov
 */
public interface RemoteBitrateObserver
{
	/**
	 * Called when a receive channel group has a new bitrate estimate for the incoming streams.
	 *
	 * @param ssrcs
	 * @param bitrate
	 */
    void onReceiveBitrateChanged(Collection<Long> ssrcs, long bitrate);
}
