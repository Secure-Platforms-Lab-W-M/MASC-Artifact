package net.sf.fmj.media;

import net.sf.fmj.media.util.MediaThread;

abstract class TimedActionThread extends MediaThread {
   protected boolean aborted = false;
   protected BasicController controller;
   protected long wakeupTime;

   TimedActionThread(BasicController var1, long var2) {
      this.controller = var1;
      this.useControlPriority();
      this.wakeupTime = var2;
   }

   public void abort() {
      synchronized(this){}

      try {
         this.aborted = true;
         this.notify();
      } finally {
         ;
      }

   }

   protected abstract void action();

   protected abstract long getTime();

   public void run() {
      while(true) {
         long var1 = this.getTime();
         long var3 = this.wakeupTime;
         if (var1 < var3 && !this.aborted) {
            label270: {
               var3 -= var1;
               var1 = var3;
               if (var3 > 1000000000L) {
                  var1 = 1000000000L;
               }

               synchronized(this){}

               Throwable var10000;
               boolean var10001;
               label260: {
                  label259: {
                     try {
                        try {
                           this.wait(var1 / 1000000L);
                           break label259;
                        } catch (InterruptedException var29) {
                        }
                     } catch (Throwable var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label260;
                     }

                     try {
                        break label270;
                     } catch (Throwable var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label260;
                     }
                  }

                  label251:
                  try {
                     continue;
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label251;
                  }
               }

               while(true) {
                  Throwable var5 = var10000;

                  try {
                     throw var5;
                  } catch (Throwable var26) {
                     var10000 = var26;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }

         if (!this.aborted) {
            this.action();
         }

         return;
      }
   }
}
