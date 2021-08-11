package net.sf.fmj.ejmf.toolkit.media;

import javax.media.Time;

public class SyncStartThread extends Thread {
   private AbstractController controller;
   private Time timeBaseStartTime;

   public SyncStartThread(AbstractController var1, Time var2) {
      this.controller = var1;
      this.timeBaseStartTime = var2;
   }

   public void run() {
      this.controller.synchronousSyncStart(this.timeBaseStartTime);
   }
}
