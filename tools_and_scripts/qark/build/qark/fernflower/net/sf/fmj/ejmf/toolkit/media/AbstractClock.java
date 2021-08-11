package net.sf.fmj.ejmf.toolkit.media;

import javax.media.Clock;
import javax.media.ClockStartedError;
import javax.media.ClockStoppedException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Manager;
import javax.media.StopTimeSetError;
import javax.media.Time;
import javax.media.TimeBase;

public class AbstractClock implements Clock {
   private boolean isStarted;
   private Time mediaStartTime;
   private Time mediaStopTime;
   private float rate;
   private TimeBase systemtimebase;
   private Time timeBaseStartTime;
   private TimeBase timebase;

   public AbstractClock() {
      TimeBase var1 = Manager.getSystemTimeBase();
      this.systemtimebase = var1;
      this.timebase = var1;
      this.mediaStartTime = new Time(0L);
      this.mediaStopTime = Clock.RESET;
      this.rate = 1.0F;
      this.isStarted = false;
   }

   private Time calculateMediaTime() {
      synchronized(this){}

      Throwable var10000;
      label113: {
         long var1;
         boolean var10001;
         long var3;
         try {
            var1 = this.timebase.getNanoseconds();
            var3 = this.timeBaseStartTime.getNanoseconds();
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label113;
         }

         Time var7;
         if (var1 < var3) {
            label106: {
               try {
                  var7 = this.mediaStartTime;
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label106;
               }

               return var7;
            }
         } else {
            label109: {
               try {
                  long var5 = this.mediaStartTime.getNanoseconds();
                  var7 = new Time((long)((float)(var1 - var3) * this.rate + (float)var5));
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label109;
               }

               return var7;
            }
         }
      }

      Throwable var20 = var10000;
      throw var20;
   }

   public long getMediaNanoseconds() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.getMediaTime().getNanoseconds();
      } finally {
         ;
      }

      return var1;
   }

   protected Time getMediaStartTime() {
      return this.mediaStartTime;
   }

   public Time getMediaTime() {
      synchronized(this){}

      Time var1;
      try {
         if (!this.isStarted) {
            var1 = this.mediaStartTime;
            return var1;
         }

         var1 = this.calculateMediaTime();
      } finally {
         ;
      }

      return var1;
   }

   public float getRate() {
      synchronized(this){}

      float var1;
      try {
         var1 = this.rate;
      } finally {
         ;
      }

      return var1;
   }

   public Time getStopTime() {
      synchronized(this){}

      Time var1;
      try {
         var1 = this.mediaStopTime;
      } finally {
         ;
      }

      return var1;
   }

   public Time getSyncTime() {
      synchronized(this){}

      Throwable var10000;
      label140: {
         boolean var10001;
         Time var5;
         label135: {
            long var1;
            long var3;
            try {
               if (!this.isStarted) {
                  break label135;
               }

               var1 = this.timeBaseStartTime.getNanoseconds();
               var3 = this.getTimeBase().getNanoseconds();
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label140;
            }

            if (var1 >= var3) {
               try {
                  var5 = new Time(var3 - var1);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label140;
               }

               return var5;
            }
         }

         try {
            var5 = this.getMediaTime();
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label140;
         }

         return var5;
      }

      Throwable var18 = var10000;
      throw var18;
   }

   public TimeBase getTimeBase() {
      synchronized(this){}

      TimeBase var1;
      try {
         var1 = this.timebase;
      } finally {
         ;
      }

      return var1;
   }

   protected Time getTimeBaseStartTime() {
      return this.timeBaseStartTime;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      synchronized(this){}

      try {
         if (!this.isStarted) {
            throw new ClockStoppedException("Cannot map media time to time-base time on a Stopped Clock");
         }

         long var2 = var1.getNanoseconds();
         long var4 = this.mediaStartTime.getNanoseconds();
         long var6 = this.timeBaseStartTime.getNanoseconds();
         var1 = new Time((long)((float)(var2 - var4) / this.rate + (float)var6));
      } finally {
         ;
      }

      return var1;
   }

   public void setMediaTime(Time var1) {
      synchronized(this){}

      try {
         if (this.isStarted) {
            throw new ClockStartedError("Cannot set media time on a Started Clock");
         }

         this.mediaStartTime = var1;
      } finally {
         ;
      }

   }

   public float setRate(float var1) {
      synchronized(this){}

      Throwable var10000;
      label180: {
         boolean var10001;
         label175: {
            try {
               if (!this.isStarted) {
                  break label175;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label180;
            }

            try {
               throw new ClockStartedError("Cannot set rate on a Started Clock");
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label180;
            }
         }

         if (var1 != 0.0F) {
            try {
               this.rate = var1;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label180;
            }
         }

         try {
            var1 = this.rate;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label180;
         }

         return var1;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void setStopTime(Time var1) {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         label141: {
            try {
               if (this.isStarted && this.mediaStopTime != RESET) {
                  break label141;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            try {
               this.mediaStopTime = var1;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label136;
            }

            return;
         }

         label123:
         try {
            throw new StopTimeSetError("Stop time may be set only once on a Started Clock");
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label123;
         }
      }

      Throwable var14 = var10000;
      throw var14;
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      synchronized(this){}

      Throwable var10000;
      label167: {
         boolean var10001;
         label171: {
            try {
               if (this.isStarted) {
                  break label171;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label167;
            }

            if (var1 == null) {
               try {
                  this.timebase = this.systemtimebase;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label167;
               }
            } else {
               try {
                  this.timebase = var1;
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label167;
               }
            }

            return;
         }

         label161:
         try {
            throw new ClockStartedError("Cannot set time base on a Started Clock");
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label161;
         }
      }

      Throwable var22 = var10000;
      throw var22;
   }

   public void stop() {
      synchronized(this){}

      try {
         if (this.isStarted) {
            this.mediaStartTime = this.calculateMediaTime();
            this.isStarted = false;
         }
      } finally {
         ;
      }

   }

   public void syncStart(Time var1) {
      synchronized(this){}

      Throwable var10000;
      label262: {
         boolean var10001;
         long var2;
         long var4;
         label257: {
            try {
               if (!this.isStarted) {
                  var2 = this.getTimeBase().getNanoseconds();
                  var4 = var1.getNanoseconds();
                  break label257;
               }
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label262;
            }

            try {
               throw new ClockStartedError("syncStart() cannot be called on a started Clock");
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label262;
            }
         }

         if (var4 - var2 > 0L) {
            try {
               this.timeBaseStartTime = new Time(var4);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label262;
            }
         } else {
            try {
               this.timeBaseStartTime = new Time(var2);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label262;
            }
         }

         try {
            this.isStarted = true;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label262;
         }

         return;
      }

      Throwable var36 = var10000;
      throw var36;
   }
}
