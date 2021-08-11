package net.sf.fmj.media;

import java.util.logging.Logger;
import net.sf.fmj.utility.LoggerSingleton;

public class SleepHelper {
   public static long MILLI_TO_NANO;
   private static final Logger logger;
   private boolean firstNonDiscard = true;
   private long mStart;
   private long tbStart;

   static {
      logger = LoggerSingleton.logger;
      MILLI_TO_NANO = 1000000L;
   }

   public long calculateSleep(long var1) {
      long var3 = System.currentTimeMillis();
      if (this.firstNonDiscard) {
         this.mStart = var1;
         this.tbStart = var3;
         this.firstNonDiscard = false;
         return 0L;
      } else {
         return (long)((double)(var1 - this.mStart) / ((double)MILLI_TO_NANO * 1.0D)) + this.tbStart - var3;
      }
   }

   public void reset() {
      this.mStart = 0L;
      this.tbStart = 0L;
      this.firstNonDiscard = true;
   }

   public void sleep(long var1) throws InterruptedException {
      var1 = this.calculateSleep(var1);
      if (var1 > 0L) {
         Logger var3 = logger;
         StringBuilder var4 = new StringBuilder();
         var4.append("Sleeping ");
         var4.append(var1);
         var3.finer(var4.toString());
         Thread.sleep(var1);
      }

   }
}
