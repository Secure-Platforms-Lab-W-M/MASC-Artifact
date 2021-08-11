package net.sf.fmj.media.rtp;

import javax.media.Buffer;

interface JitterBufferBehaviour {
   void dropPkt();

   int getAbsoluteMaximumDelay();

   int getMaximumDelay();

   int getNominalDelay();

   boolean isAdaptive();

   boolean preAdd(Buffer var1, RTPRawReceiver var2);

   void read(Buffer var1);

   void reset();

   boolean willReadBlock();
}
