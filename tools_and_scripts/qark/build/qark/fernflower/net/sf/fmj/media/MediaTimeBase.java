package net.sf.fmj.media;

import javax.media.SystemTimeBase;
import javax.media.Time;
import javax.media.TimeBase;

public abstract class MediaTimeBase implements TimeBase {
   long offset = 0L;
   long origin = 0L;
   TimeBase systemTimeBase = null;
   long time = 0L;

   public MediaTimeBase() {
      this.mediaStopped();
   }

   public abstract long getMediaTime();

   public long getNanoseconds() {
      synchronized(this){}

      Throwable var10000;
      label133: {
         boolean var10001;
         label126: {
            try {
               if (this.systemTimeBase != null) {
                  this.time = this.origin + this.systemTimeBase.getNanoseconds() - this.offset;
                  break label126;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label133;
            }

            try {
               this.time = this.origin + this.getMediaTime() - this.offset;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label133;
            }
         }

         label117:
         try {
            long var1 = this.time;
            return var1;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public Time getTime() {
      return new Time(this.getNanoseconds());
   }

   public void mediaStarted() {
      synchronized(this){}

      try {
         this.systemTimeBase = null;
         this.offset = this.getMediaTime();
         this.origin = this.time;
      } finally {
         ;
      }

   }

   public void mediaStopped() {
      synchronized(this){}

      try {
         SystemTimeBase var1 = new SystemTimeBase();
         this.systemTimeBase = var1;
         this.offset = var1.getNanoseconds();
         this.origin = this.time;
      } finally {
         ;
      }

   }
}
