package net.sf.fmj.media.rtp.util;

import net.sf.fmj.media.util.MediaThread;

public class RTPMediaThread extends MediaThread {
   public RTPMediaThread() {
      this("RTP thread");
   }

   public RTPMediaThread(Runnable var1) {
      this(var1, "RTP thread");
   }

   public RTPMediaThread(Runnable var1, String var2) {
      super(var1, var2);
   }

   public RTPMediaThread(String var1) {
      super(var1);
   }
}
