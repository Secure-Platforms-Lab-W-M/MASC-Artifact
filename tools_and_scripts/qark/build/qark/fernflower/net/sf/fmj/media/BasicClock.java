package net.sf.fmj.media;

import javax.media.Clock;
import javax.media.ClockStartedError;
import javax.media.ClockStoppedException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.StopTimeSetError;
import javax.media.SystemTimeBase;
import javax.media.Time;
import javax.media.TimeBase;

public class BasicClock implements Clock {
   public static final int STARTED = 1;
   public static final int STOPPED = 0;
   private TimeBase master = new SystemTimeBase();
   private long mediaLength = -1L;
   private long mediaStart = 0L;
   private long mediaTime = 0L;
   private float rate = 1.0F;
   private long startTime = Long.MAX_VALUE;
   private long stopTime = Long.MAX_VALUE;

   public long getMediaNanoseconds() {
      if (this.getState() == 0) {
         return this.mediaTime;
      } else {
         long var1 = this.master.getNanoseconds();
         long var3 = this.startTime;
         if (var1 > var3) {
            var1 = (long)((double)(var1 - var3) * (double)this.rate) + this.mediaTime;
            var3 = this.mediaLength;
            if (var3 != -1L) {
               long var5 = this.mediaStart;
               if (var1 > var5 + var3) {
                  return var5 + var3;
               }
            }

            return var1;
         } else {
            return this.mediaTime;
         }
      }
   }

   public Time getMediaTime() {
      return new Time(this.getMediaNanoseconds());
   }

   public float getRate() {
      return this.rate;
   }

   public int getState() {
      if (this.startTime == Long.MAX_VALUE) {
         return 0;
      } else {
         return this.stopTime == Long.MAX_VALUE ? 1 : 1;
      }
   }

   public Time getStopTime() {
      return new Time(this.stopTime);
   }

   public Time getSyncTime() {
      return new Time(0L);
   }

   public TimeBase getTimeBase() {
      return this.master;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      if (this.getState() != 0) {
         return new Time((long)((float)(var1.getNanoseconds() - this.mediaTime) / this.rate) + this.startTime);
      } else {
         ClockStoppedException var2 = new ClockStoppedException();
         Log.dumpStack(var2);
         throw var2;
      }
   }

   protected void setMediaLength(long var1) {
      this.mediaLength = var1;
   }

   protected void setMediaStart(long var1) {
      this.mediaStart = var1;
   }

   public void setMediaTime(Time var1) {
      if (this.getState() == 1) {
         this.throwError(new ClockStartedError("setMediaTime() cannot be used on a started clock."));
      }

      long var2 = var1.getNanoseconds();
      long var4 = this.mediaStart;
      if (var2 < var4) {
         this.mediaTime = var4;
      } else {
         long var6 = this.mediaLength;
         if (var6 != -1L && var2 > var4 + var6) {
            this.mediaTime = var4 + var6;
         } else {
            this.mediaTime = var2;
         }
      }
   }

   public float setRate(float var1) {
      if (this.getState() == 1) {
         this.throwError(new ClockStartedError("setRate() cannot be used on a started clock."));
      }

      this.rate = var1;
      return var1;
   }

   public void setStopTime(Time var1) {
      if (this.getState() == 1 && this.stopTime != Long.MAX_VALUE) {
         this.throwError(new StopTimeSetError("setStopTime() may be set only once on a Started Clock"));
      }

      this.stopTime = var1.getNanoseconds();
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      if (this.getState() == 1) {
         this.throwError(new ClockStartedError("setTimeBase cannot be used on a started clock."));
      }

      if (var1 == null) {
         if (!(this.master instanceof SystemTimeBase)) {
            this.master = new SystemTimeBase();
            return;
         }
      } else {
         this.master = var1;
      }

   }

   public void stop() {
      if (this.getState() != 0) {
         this.mediaTime = this.getMediaNanoseconds();
         this.startTime = Long.MAX_VALUE;
      }
   }

   public void syncStart(Time var1) {
      if (this.getState() == 1) {
         this.throwError(new ClockStartedError("syncStart() cannot be used on an already started clock."));
      }

      if (this.master.getNanoseconds() > var1.getNanoseconds()) {
         this.startTime = this.master.getNanoseconds();
      } else {
         this.startTime = var1.getNanoseconds();
      }
   }

   protected void throwError(Error var1) {
      Log.dumpStack(var1);
      throw var1;
   }
}
