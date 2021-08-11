package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
   public static final Timeout NONE = new Timeout() {
      public Timeout deadlineNanoTime(long var1) {
         return this;
      }

      public void throwIfReached() throws IOException {
      }

      public Timeout timeout(long var1, TimeUnit var3) {
         return this;
      }
   };
   private long deadlineNanoTime;
   private boolean hasDeadline;
   private long timeoutNanos;

   public Timeout clearDeadline() {
      this.hasDeadline = false;
      return this;
   }

   public Timeout clearTimeout() {
      this.timeoutNanos = 0L;
      return this;
   }

   public final Timeout deadline(long var1, TimeUnit var3) {
      if (var1 > 0L) {
         if (var3 != null) {
            return this.deadlineNanoTime(System.nanoTime() + var3.toNanos(var1));
         } else {
            throw new IllegalArgumentException("unit == null");
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("duration <= 0: ");
         var4.append(var1);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public long deadlineNanoTime() {
      if (this.hasDeadline) {
         return this.deadlineNanoTime;
      } else {
         throw new IllegalStateException("No deadline");
      }
   }

   public Timeout deadlineNanoTime(long var1) {
      this.hasDeadline = true;
      this.deadlineNanoTime = var1;
      return this;
   }

   public boolean hasDeadline() {
      return this.hasDeadline;
   }

   public void throwIfReached() throws IOException {
      if (!Thread.interrupted()) {
         if (this.hasDeadline) {
            if (this.deadlineNanoTime - System.nanoTime() <= 0L) {
               throw new InterruptedIOException("deadline reached");
            }
         }
      } else {
         throw new InterruptedIOException("thread interrupted");
      }
   }

   public Timeout timeout(long var1, TimeUnit var3) {
      if (var1 >= 0L) {
         if (var3 != null) {
            this.timeoutNanos = var3.toNanos(var1);
            return this;
         } else {
            throw new IllegalArgumentException("unit == null");
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("timeout < 0: ");
         var4.append(var1);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public long timeoutNanos() {
      return this.timeoutNanos;
   }

   public final void waitUntilNotified(Object var1) throws InterruptedIOException {
      boolean var10001;
      boolean var3;
      long var4;
      try {
         var3 = this.hasDeadline();
         var4 = this.timeoutNanos();
      } catch (InterruptedException var17) {
         var10001 = false;
         throw new InterruptedIOException("interrupted");
      }

      if (!var3 && var4 == 0L) {
         try {
            var1.wait();
            return;
         } catch (InterruptedException var10) {
            var10001 = false;
         }
      } else {
         long var8;
         try {
            var8 = System.nanoTime();
         } catch (InterruptedException var16) {
            var10001 = false;
            throw new InterruptedIOException("interrupted");
         }

         if (var3 && var4 != 0L) {
            try {
               var4 = Math.min(var4, this.deadlineNanoTime() - var8);
            } catch (InterruptedException var15) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }
         } else if (var3) {
            try {
               var4 = this.deadlineNanoTime() - var8;
            } catch (InterruptedException var14) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }
         }

         long var6 = 0L;
         if (var4 > 0L) {
            try {
               var6 = var4 / 1000000L;
            } catch (InterruptedException var13) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }

            Long.signum(var6);
            int var2 = (int)(var4 - 1000000L * var6);

            try {
               var1.wait(var6, var2);
               var6 = System.nanoTime() - var8;
            } catch (InterruptedException var12) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }
         }

         if (var6 < var4) {
            return;
         }

         try {
            throw new InterruptedIOException("timeout");
         } catch (InterruptedException var11) {
            var10001 = false;
         }
      }

      throw new InterruptedIOException("interrupted");
   }
}
