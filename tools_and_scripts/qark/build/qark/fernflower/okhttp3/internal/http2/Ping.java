package okhttp3.internal.http2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class Ping {
   private final CountDownLatch latch = new CountDownLatch(1);
   private long received = -1L;
   private long sent = -1L;

   void cancel() {
      if (this.received == -1L) {
         long var1 = this.sent;
         if (var1 != -1L) {
            this.received = var1 - 1L;
            this.latch.countDown();
            return;
         }
      }

      throw new IllegalStateException();
   }

   void receive() {
      if (this.received == -1L && this.sent != -1L) {
         this.received = System.nanoTime();
         this.latch.countDown();
      } else {
         throw new IllegalStateException();
      }
   }

   public long roundTripTime() throws InterruptedException {
      this.latch.await();
      return this.received - this.sent;
   }

   public long roundTripTime(long var1, TimeUnit var3) throws InterruptedException {
      return this.latch.await(var1, var3) ? this.received - this.sent : -2L;
   }

   void send() {
      if (this.sent == -1L) {
         this.sent = System.nanoTime();
      } else {
         throw new IllegalStateException();
      }
   }
}
