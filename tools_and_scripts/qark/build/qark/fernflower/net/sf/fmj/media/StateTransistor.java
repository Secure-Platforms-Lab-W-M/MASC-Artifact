package net.sf.fmj.media;

import javax.media.Time;

public interface StateTransistor {
   void abortPrefetch();

   void abortRealize();

   void doClose();

   void doDealloc();

   void doFailedPrefetch();

   void doFailedRealize();

   boolean doPrefetch();

   boolean doRealize();

   void doSetMediaTime(Time var1);

   float doSetRate(float var1);

   void doStart();

   void doStop();
}
