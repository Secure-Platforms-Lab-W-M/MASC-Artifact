package net.sf.fmj.ejmf.toolkit.media;

import com.lti.utils.synchronization.CloseableThread;
import javax.media.ClockStoppedException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DeallocateEvent;
import javax.media.MediaTimeSetEvent;
import javax.media.RateChangeEvent;
import javax.media.StartEvent;
import javax.media.StopEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;

public class StopTimeMonitor extends CloseableThread implements ControllerListener {
   private AbstractController controller;
   private boolean wokenUp;

   public StopTimeMonitor(AbstractController var1, String var2) {
      this.setName(var2);
      this.controller = var1;
      var1.addControllerListener(this);
      this.setDaemon(true);
   }

   private long getWaitTime(Time var1) throws ClockStoppedException {
      return (this.controller.mapToTimeBase(var1).getNanoseconds() - this.controller.getTimeBase().getNanoseconds()) / 1000000L;
   }

   private void monitorStopTime() throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   public void controllerUpdate(ControllerEvent var1) {
      synchronized(this){}

      try {
         if (var1 instanceof StopTimeChangeEvent || var1 instanceof RateChangeEvent || var1 instanceof MediaTimeSetEvent || var1 instanceof StartEvent || var1 instanceof StopEvent && !(var1 instanceof DeallocateEvent)) {
            this.wokenUp = true;
            this.notifyAll();
         }
      } finally {
         ;
      }

   }

   public void run() {
      try {
         this.monitorStopTime();
      } catch (InterruptedException var2) {
      }

      this.setClosed();
   }
}
