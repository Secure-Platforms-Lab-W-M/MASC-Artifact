package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class TimedSemaphore {
   public static final int NO_LIMIT = 0;
   private static final int THREAD_POOL_SIZE = 1;
   private int acquireCount;
   private final ScheduledExecutorService executorService;
   private int lastCallsPerPeriod;
   private int limit;
   private final boolean ownExecutor;
   private final long period;
   private long periodCount;
   private boolean shutdown;
   private ScheduledFuture task;
   private long totalAcquireCount;
   private final TimeUnit unit;

   public TimedSemaphore(long var1, TimeUnit var3, int var4) {
      this((ScheduledExecutorService)null, var1, var3, var4);
   }

   public TimedSemaphore(ScheduledExecutorService var1, long var2, TimeUnit var4, int var5) {
      Validate.inclusiveBetween(1L, Long.MAX_VALUE, var2, "Time period must be greater than 0!");
      this.period = var2;
      this.unit = var4;
      if (var1 != null) {
         this.executorService = var1;
         this.ownExecutor = false;
      } else {
         ScheduledThreadPoolExecutor var6 = new ScheduledThreadPoolExecutor(1);
         var6.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
         var6.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
         this.executorService = var6;
         this.ownExecutor = true;
      }

      this.setLimit(var5);
   }

   private boolean acquirePermit() {
      if (this.getLimit() > 0 && this.acquireCount >= this.getLimit()) {
         return false;
      } else {
         ++this.acquireCount;
         return true;
      }
   }

   private void prepareAcquire() {
      if (!this.isShutdown()) {
         if (this.task == null) {
            this.task = this.startTimer();
         }

      } else {
         throw new IllegalStateException("TimedSemaphore is shut down!");
      }
   }

   public void acquire() throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label120: {
         boolean var10001;
         try {
            this.prepareAcquire();
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label120;
         }

         while(true) {
            boolean var1;
            try {
               var1 = this.acquirePermit();
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            if (!var1) {
               try {
                  this.wait();
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break;
               }
            }

            if (var1) {
               return;
            }
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   void endOfPeriod() {
      synchronized(this){}

      try {
         this.lastCallsPerPeriod = this.acquireCount;
         this.totalAcquireCount += (long)this.acquireCount;
         ++this.periodCount;
         this.acquireCount = 0;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public int getAcquireCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.acquireCount;
      } finally {
         ;
      }

      return var1;
   }

   public int getAvailablePermits() {
      synchronized(this){}

      int var1;
      int var2;
      try {
         var1 = this.getLimit();
         var2 = this.getAcquireCount();
      } finally {
         ;
      }

      return var1 - var2;
   }

   public double getAverageCallsPerPeriod() {
      synchronized(this){}
      boolean var7 = false;

      double var1;
      label52: {
         long var3;
         try {
            var7 = true;
            if (this.periodCount == 0L) {
               var7 = false;
               break label52;
            }

            var1 = (double)this.totalAcquireCount;
            var3 = this.periodCount;
            var7 = false;
         } finally {
            if (var7) {
               ;
            }
         }

         var1 /= (double)var3;
         return var1;
      }

      var1 = 0.0D;
      return var1;
   }

   protected ScheduledExecutorService getExecutorService() {
      return this.executorService;
   }

   public int getLastAcquiresPerPeriod() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.lastCallsPerPeriod;
      } finally {
         ;
      }

      return var1;
   }

   public final int getLimit() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.limit;
      } finally {
         ;
      }

      return var1;
   }

   public long getPeriod() {
      return this.period;
   }

   public TimeUnit getUnit() {
      return this.unit;
   }

   public boolean isShutdown() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.shutdown;
      } finally {
         ;
      }

      return var1;
   }

   public final void setLimit(int var1) {
      synchronized(this){}

      try {
         this.limit = var1;
      } finally {
         ;
      }

   }

   public void shutdown() {
      synchronized(this){}

      try {
         if (!this.shutdown) {
            if (this.ownExecutor) {
               this.getExecutorService().shutdownNow();
            }

            if (this.task != null) {
               this.task.cancel(false);
            }

            this.shutdown = true;
         }
      } finally {
         ;
      }

   }

   protected ScheduledFuture startTimer() {
      return this.getExecutorService().scheduleAtFixedRate(new Runnable() {
         public void run() {
            TimedSemaphore.this.endOfPeriod();
         }
      }, this.getPeriod(), this.getPeriod(), this.getUnit());
   }

   public boolean tryAcquire() {
      synchronized(this){}

      boolean var1;
      try {
         this.prepareAcquire();
         var1 = this.acquirePermit();
      } finally {
         ;
      }

      return var1;
   }
}
