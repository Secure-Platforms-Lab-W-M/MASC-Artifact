package javax.media.control;

import javax.media.Control;

public interface JitterBufferControl extends Control {
   int getAbsoluteMaximumDelay();

   int getCurrentDelayMs();

   int getCurrentDelayPackets();

   int getCurrentPacketCount();

   int getCurrentSizePackets();

   int getDiscarded();

   int getDiscardedFull();

   int getDiscardedLate();

   int getDiscardedReset();

   int getDiscardedShrink();

   int getMaxSizeReached();

   int getMaximumDelay();

   int getNominalDelay();

   boolean isAdaptiveBufferEnabled();
}
