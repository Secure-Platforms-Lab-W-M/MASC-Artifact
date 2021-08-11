package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class ThresholdCircuitBreaker extends AbstractCircuitBreaker {
   private static final long INITIAL_COUNT = 0L;
   private final long threshold;
   private final AtomicLong used = new AtomicLong(0L);

   public ThresholdCircuitBreaker(long var1) {
      this.threshold = var1;
   }

   public boolean checkState() {
      return this.isOpen();
   }

   public void close() {
      super.close();
      this.used.set(0L);
   }

   public long getThreshold() {
      return this.threshold;
   }

   public boolean incrementAndCheckState(Long var1) {
      if (this.threshold == 0L) {
         this.open();
      }

      if (this.used.addAndGet(var1) > this.threshold) {
         this.open();
      }

      return this.checkState();
   }
}
