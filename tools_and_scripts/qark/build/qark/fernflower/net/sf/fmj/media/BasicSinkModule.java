package net.sf.fmj.media;

import javax.media.Clock;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Time;
import javax.media.TimeBase;

public abstract class BasicSinkModule extends BasicModule {
   private Clock clock;
   protected boolean prerolling = false;
   protected float rate = 1.0F;
   protected long stopTime = -1L;

   public void doSetMediaTime(Time var1) {
      Clock var2 = this.clock;
      if (var2 != null) {
         var2.setMediaTime(var1);
      }

   }

   public float doSetRate(float var1) {
      Clock var2 = this.clock;
      if (var2 != null) {
         this.rate = var2.setRate(var1);
      } else {
         this.rate = var1;
      }

      return this.rate;
   }

   public void doStart() {
      super.doStart();
      Clock var1 = this.clock;
      if (var1 != null) {
         var1.syncStart(var1.getTimeBase().getTime());
      }

   }

   public void doStop() {
      Clock var1 = this.clock;
      if (var1 != null) {
         var1.stop();
      }

   }

   public void doneReset() {
   }

   public Clock getClock() {
      return this.clock;
   }

   public long getMediaNanoseconds() {
      Clock var1 = this.clock;
      return var1 != null ? var1.getMediaNanoseconds() : this.controller.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      Clock var1 = this.clock;
      return var1 != null ? var1.getMediaTime() : this.controller.getMediaTime();
   }

   public TimeBase getTimeBase() {
      Clock var1 = this.clock;
      return var1 != null ? var1.getTimeBase() : this.controller.getTimeBase();
   }

   protected void setClock(Clock var1) {
      this.clock = var1;
   }

   public void setPreroll(long var1, long var3) {
      if (var3 < var1) {
         this.prerolling = true;
      }

   }

   public void setStopTime(Time var1) {
      if (var1 == Clock.RESET) {
         this.stopTime = -1L;
      } else {
         this.stopTime = var1.getNanoseconds();
      }
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      Clock var2 = this.clock;
      if (var2 != null) {
         var2.setTimeBase(var1);
      }

   }

   public void triggerReset() {
   }
}
