package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class AsyncTimeout extends Timeout {
   private static final long IDLE_TIMEOUT_MILLIS;
   private static final long IDLE_TIMEOUT_NANOS;
   private static final int TIMEOUT_WRITE_SIZE = 65536;
   @Nullable
   static AsyncTimeout head;
   private boolean inQueue;
   @Nullable
   private AsyncTimeout next;
   private long timeoutAt;

   static {
      IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
      IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
   }

   @Nullable
   static AsyncTimeout awaitTimeout() throws InterruptedException {
      AsyncTimeout var4 = head.next;
      long var0;
      if (var4 == null) {
         var0 = System.nanoTime();
         AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
         return head.next == null && System.nanoTime() - var0 >= IDLE_TIMEOUT_NANOS ? head : null;
      } else {
         var0 = var4.remainingNanos(System.nanoTime());
         if (var0 > 0L) {
            long var2 = var0 / 1000000L;
            AsyncTimeout.class.wait(var2, (int)(var0 - 1000000L * var2));
            return null;
         } else {
            head.next = var4.next;
            var4.next = null;
            return var4;
         }
      }
   }

   private static boolean cancelScheduledTimeout(AsyncTimeout var0) {
      synchronized(AsyncTimeout.class){}

      label141: {
         Throwable var10000;
         label140: {
            AsyncTimeout var1;
            boolean var10001;
            try {
               var1 = head;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label140;
            }

            while(true) {
               if (var1 == null) {
                  return true;
               }

               try {
                  if (var1.next == var0) {
                     var1.next = var0.next;
                     var0.next = null;
                     break label141;
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break;
               }

               try {
                  var1 = var1.next;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var14 = var10000;
         throw var14;
      }

      return false;
   }

   private long remainingNanos(long var1) {
      return this.timeoutAt - var1;
   }

   private static void scheduleTimeout(AsyncTimeout var0, long var1, boolean var3) {
      synchronized(AsyncTimeout.class){}

      Throwable var10000;
      label992: {
         boolean var10001;
         try {
            if (head == null) {
               head = new AsyncTimeout();
               (new AsyncTimeout.Watchdog()).start();
            }
         } catch (Throwable var116) {
            var10000 = var116;
            var10001 = false;
            break label992;
         }

         long var4;
         try {
            var4 = System.nanoTime();
         } catch (Throwable var115) {
            var10000 = var115;
            var10001 = false;
            break label992;
         }

         if (var1 != 0L && var3) {
            try {
               var0.timeoutAt = Math.min(var1, var0.deadlineNanoTime() - var4) + var4;
            } catch (Throwable var114) {
               var10000 = var114;
               var10001 = false;
               break label992;
            }
         } else if (var1 != 0L) {
            try {
               var0.timeoutAt = var4 + var1;
            } catch (Throwable var113) {
               var10000 = var113;
               var10001 = false;
               break label992;
            }
         } else {
            if (!var3) {
               try {
                  throw new AssertionError();
               } catch (Throwable var111) {
                  var10000 = var111;
                  var10001 = false;
                  break label992;
               }
            }

            try {
               var0.timeoutAt = var0.deadlineNanoTime();
            } catch (Throwable var112) {
               var10000 = var112;
               var10001 = false;
               break label992;
            }
         }

         AsyncTimeout var6;
         try {
            var1 = var0.remainingNanos(var4);
            var6 = head;
         } catch (Throwable var109) {
            var10000 = var109;
            var10001 = false;
            break label992;
         }

         while(true) {
            try {
               if (var6.next == null || var1 < var6.next.remainingNanos(var4)) {
                  break;
               }
            } catch (Throwable var110) {
               var10000 = var110;
               var10001 = false;
               break label992;
            }

            try {
               var6 = var6.next;
            } catch (Throwable var108) {
               var10000 = var108;
               var10001 = false;
               break label992;
            }
         }

         try {
            var0.next = var6.next;
            var6.next = var0;
            if (var6 == head) {
               AsyncTimeout.class.notify();
            }
         } catch (Throwable var107) {
            var10000 = var107;
            var10001 = false;
            break label992;
         }

         return;
      }

      Throwable var117 = var10000;
      throw var117;
   }

   public final void enter() {
      if (!this.inQueue) {
         long var1 = this.timeoutNanos();
         boolean var3 = this.hasDeadline();
         if (var1 != 0L || var3) {
            this.inQueue = true;
            scheduleTimeout(this, var1, var3);
         }
      } else {
         throw new IllegalStateException("Unbalanced enter/exit");
      }
   }

   final IOException exit(IOException var1) throws IOException {
      return !this.exit() ? var1 : this.newTimeoutException(var1);
   }

   final void exit(boolean var1) throws IOException {
      if (this.exit()) {
         if (var1) {
            throw this.newTimeoutException((IOException)null);
         }
      }
   }

   public final boolean exit() {
      if (!this.inQueue) {
         return false;
      } else {
         this.inQueue = false;
         return cancelScheduledTimeout(this);
      }
   }

   protected IOException newTimeoutException(@Nullable IOException var1) {
      InterruptedIOException var2 = new InterruptedIOException("timeout");
      if (var1 != null) {
         var2.initCause(var1);
      }

      return var2;
   }

   public final Sink sink(final Sink var1) {
      return new Sink() {
         public void close() throws IOException {
            AsyncTimeout.this.enter();
            boolean var4 = false;

            try {
               var4 = true;
               var1.close();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public void flush() throws IOException {
            AsyncTimeout.this.enter();
            boolean var4 = false;

            try {
               var4 = true;
               var1.flush();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public Timeout timeout() {
            return AsyncTimeout.this;
         }

         public String toString() {
            StringBuilder var1x = new StringBuilder();
            var1x.append("AsyncTimeout.sink(");
            var1x.append(var1);
            var1x.append(")");
            return var1x.toString();
         }

         public void write(Buffer var1x, long var2) throws IOException {
            Util.checkOffsetAndCount(var1x.size, 0L, var2);

            while(var2 > 0L) {
               long var4 = 0L;
               Segment var8 = var1x.head;

               long var6;
               while(true) {
                  var6 = var4;
                  if (var4 >= 65536L) {
                     break;
                  }

                  var4 += (long)(var1x.head.limit - var1x.head.pos);
                  if (var4 >= var2) {
                     var6 = var2;
                     break;
                  }

                  var8 = var8.next;
               }

               AsyncTimeout.this.enter();
               boolean var11 = false;

               try {
                  var11 = true;
                  var1.write(var1x, var6);
                  var11 = false;
               } catch (IOException var12) {
                  throw AsyncTimeout.this.exit(var12);
               } finally {
                  if (var11) {
                     AsyncTimeout.this.exit(false);
                  }
               }

               var2 -= var6;
               AsyncTimeout.this.exit(true);
            }

         }
      };
   }

   public final Source source(final Source var1) {
      return new Source() {
         public void close() throws IOException {
            boolean var4 = false;

            try {
               var4 = true;
               var1.close();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public long read(Buffer var1x, long var2) throws IOException {
            AsyncTimeout.this.enter();
            boolean var6 = false;

            try {
               var6 = true;
               var2 = var1.read(var1x, var2);
               var6 = false;
            } catch (IOException var7) {
               throw AsyncTimeout.this.exit(var7);
            } finally {
               if (var6) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
            return var2;
         }

         public Timeout timeout() {
            return AsyncTimeout.this;
         }

         public String toString() {
            StringBuilder var1x = new StringBuilder();
            var1x.append("AsyncTimeout.source(");
            var1x.append(var1);
            var1x.append(")");
            return var1x.toString();
         }
      };
   }

   protected void timedOut() {
   }

   private static final class Watchdog extends Thread {
      Watchdog() {
         super("Okio Watchdog");
         this.setDaemon(true);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
