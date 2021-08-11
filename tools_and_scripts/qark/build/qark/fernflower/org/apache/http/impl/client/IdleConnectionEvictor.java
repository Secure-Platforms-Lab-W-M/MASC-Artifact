package org.apache.http.impl.client;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.util.Args;

public final class IdleConnectionEvictor {
   private final HttpClientConnectionManager connectionManager;
   private volatile Exception exception;
   private final long maxIdleTimeMs;
   private final long sleepTimeMs;
   private final Thread thread;
   private final ThreadFactory threadFactory;

   public IdleConnectionEvictor(HttpClientConnectionManager var1, long var2, TimeUnit var4) {
      long var5;
      if (var2 > 0L) {
         var5 = var2;
      } else {
         var5 = 5L;
      }

      TimeUnit var7;
      if (var4 != null) {
         var7 = var4;
      } else {
         var7 = TimeUnit.SECONDS;
      }

      this(var1, (ThreadFactory)null, var5, var7, var2, var4);
   }

   public IdleConnectionEvictor(HttpClientConnectionManager var1, long var2, TimeUnit var4, long var5, TimeUnit var7) {
      this(var1, (ThreadFactory)null, var2, var4, var5, var7);
   }

   public IdleConnectionEvictor(final HttpClientConnectionManager var1, ThreadFactory var2, long var3, TimeUnit var5, long var6, TimeUnit var8) {
      this.connectionManager = (HttpClientConnectionManager)Args.notNull(var1, "Connection manager");
      if (var2 == null) {
         var2 = new IdleConnectionEvictor.DefaultThreadFactory();
      }

      this.threadFactory = (ThreadFactory)var2;
      if (var5 != null) {
         var3 = var5.toMillis(var3);
      }

      this.sleepTimeMs = var3;
      if (var8 != null) {
         var6 = var8.toMillis(var6);
      }

      this.maxIdleTimeMs = var6;
      this.thread = this.threadFactory.newThread(new Runnable() {
         public void run() {
            while(true) {
               try {
                  while(!Thread.currentThread().isInterrupted()) {
                     Thread.sleep(IdleConnectionEvictor.this.sleepTimeMs);
                     var1.closeExpiredConnections();
                     if (IdleConnectionEvictor.this.maxIdleTimeMs > 0L) {
                        var1.closeIdleConnections(IdleConnectionEvictor.this.maxIdleTimeMs, TimeUnit.MILLISECONDS);
                     }
                  }

                  return;
               } catch (Exception var2) {
                  IdleConnectionEvictor.this.exception = var2;
                  return;
               }
            }
         }
      });
   }

   public void awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      Thread var4 = this.thread;
      if (var3 == null) {
         var3 = TimeUnit.MILLISECONDS;
      }

      var4.join(var3.toMillis(var1));
   }

   public boolean isRunning() {
      return this.thread.isAlive();
   }

   public void shutdown() {
      this.thread.interrupt();
   }

   public void start() {
      this.thread.start();
   }

   static class DefaultThreadFactory implements ThreadFactory {
      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(var1, "Connection evictor");
         var2.setDaemon(true);
         return var2;
      }
   }
}
