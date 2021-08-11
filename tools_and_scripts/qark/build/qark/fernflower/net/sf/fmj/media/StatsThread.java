package net.sf.fmj.media;

import net.sf.fmj.media.util.LoopThread;

class StatsThread extends LoopThread {
   int pausecount = -1;
   BasicPlayer player;

   public StatsThread(BasicPlayer var1) {
      this.player = var1;
   }

   protected boolean process() {
      try {
         Thread.sleep(1000L);
      } catch (Exception var3) {
      }

      if (!this.waitHereIfPaused()) {
         return false;
      } else if (this.player.getState() == 600) {
         this.pausecount = -1;
         this.player.updateStats();
         return true;
      } else {
         int var1 = this.pausecount;
         if (var1 < 5) {
            this.pausecount = var1 + 1;
            this.player.updateStats();
         }

         return true;
      }
   }
}
