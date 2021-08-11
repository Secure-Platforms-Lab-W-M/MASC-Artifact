package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class GlideExecutor implements ExecutorService {
   private static final String DEFAULT_ANIMATION_EXECUTOR_NAME = "animation";
   private static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
   private static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;
   private static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
   private static final String DEFAULT_SOURCE_UNLIMITED_EXECUTOR_NAME = "source-unlimited";
   private static final long KEEP_ALIVE_TIME_MS;
   private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
   private static final String TAG = "GlideExecutor";
   private static volatile int bestThreadCount;
   private final ExecutorService delegate;

   static {
      KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
   }

   GlideExecutor(ExecutorService var1) {
      this.delegate = var1;
   }

   public static int calculateBestThreadCount() {
      if (bestThreadCount == 0) {
         bestThreadCount = Math.min(4, RuntimeCompat.availableProcessors());
      }

      return bestThreadCount;
   }

   public static GlideExecutor.Builder newAnimationBuilder() {
      byte var0;
      if (calculateBestThreadCount() >= 4) {
         var0 = 2;
      } else {
         var0 = 1;
      }

      return (new GlideExecutor.Builder(true)).setThreadCount(var0).setName("animation");
   }

   public static GlideExecutor newAnimationExecutor() {
      return newAnimationBuilder().build();
   }

   @Deprecated
   public static GlideExecutor newAnimationExecutor(int var0, GlideExecutor.UncaughtThrowableStrategy var1) {
      return newAnimationBuilder().setThreadCount(var0).setUncaughtThrowableStrategy(var1).build();
   }

   public static GlideExecutor.Builder newDiskCacheBuilder() {
      return (new GlideExecutor.Builder(true)).setThreadCount(1).setName("disk-cache");
   }

   public static GlideExecutor newDiskCacheExecutor() {
      return newDiskCacheBuilder().build();
   }

   @Deprecated
   public static GlideExecutor newDiskCacheExecutor(int var0, String var1, GlideExecutor.UncaughtThrowableStrategy var2) {
      return newDiskCacheBuilder().setThreadCount(var0).setName(var1).setUncaughtThrowableStrategy(var2).build();
   }

   @Deprecated
   public static GlideExecutor newDiskCacheExecutor(GlideExecutor.UncaughtThrowableStrategy var0) {
      return newDiskCacheBuilder().setUncaughtThrowableStrategy(var0).build();
   }

   public static GlideExecutor.Builder newSourceBuilder() {
      return (new GlideExecutor.Builder(false)).setThreadCount(calculateBestThreadCount()).setName("source");
   }

   public static GlideExecutor newSourceExecutor() {
      return newSourceBuilder().build();
   }

   @Deprecated
   public static GlideExecutor newSourceExecutor(int var0, String var1, GlideExecutor.UncaughtThrowableStrategy var2) {
      return newSourceBuilder().setThreadCount(var0).setName(var1).setUncaughtThrowableStrategy(var2).build();
   }

   @Deprecated
   public static GlideExecutor newSourceExecutor(GlideExecutor.UncaughtThrowableStrategy var0) {
      return newSourceBuilder().setUncaughtThrowableStrategy(var0).build();
   }

   public static GlideExecutor newUnlimitedSourceExecutor() {
      return new GlideExecutor(new ThreadPoolExecutor(0, Integer.MAX_VALUE, KEEP_ALIVE_TIME_MS, TimeUnit.MILLISECONDS, new SynchronousQueue(), new GlideExecutor.DefaultThreadFactory("source-unlimited", GlideExecutor.UncaughtThrowableStrategy.DEFAULT, false)));
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      return this.delegate.awaitTermination(var1, var3);
   }

   public void execute(Runnable var1) {
      this.delegate.execute(var1);
   }

   public List invokeAll(Collection var1) throws InterruptedException {
      return this.delegate.invokeAll(var1);
   }

   public List invokeAll(Collection var1, long var2, TimeUnit var4) throws InterruptedException {
      return this.delegate.invokeAll(var1, var2, var4);
   }

   public Object invokeAny(Collection var1) throws InterruptedException, ExecutionException {
      return this.delegate.invokeAny(var1);
   }

   public Object invokeAny(Collection var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegate.invokeAny(var1, var2, var4);
   }

   public boolean isShutdown() {
      return this.delegate.isShutdown();
   }

   public boolean isTerminated() {
      return this.delegate.isTerminated();
   }

   public void shutdown() {
      this.delegate.shutdown();
   }

   public List shutdownNow() {
      return this.delegate.shutdownNow();
   }

   public Future submit(Runnable var1) {
      return this.delegate.submit(var1);
   }

   public Future submit(Runnable var1, Object var2) {
      return this.delegate.submit(var1, var2);
   }

   public Future submit(Callable var1) {
      return this.delegate.submit(var1);
   }

   public String toString() {
      return this.delegate.toString();
   }

   public static final class Builder {
      public static final long NO_THREAD_TIMEOUT = 0L;
      private int corePoolSize;
      private int maximumPoolSize;
      private String name;
      private final boolean preventNetworkOperations;
      private long threadTimeoutMillis;
      private GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;

      Builder(boolean var1) {
         this.uncaughtThrowableStrategy = GlideExecutor.UncaughtThrowableStrategy.DEFAULT;
         this.preventNetworkOperations = var1;
      }

      public GlideExecutor build() {
         if (!TextUtils.isEmpty(this.name)) {
            ThreadPoolExecutor var2 = new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.threadTimeoutMillis, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(), new GlideExecutor.DefaultThreadFactory(this.name, this.uncaughtThrowableStrategy, this.preventNetworkOperations));
            if (this.threadTimeoutMillis != 0L) {
               var2.allowCoreThreadTimeOut(true);
            }

            return new GlideExecutor(var2);
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("Name must be non-null and non-empty, but given: ");
            var1.append(this.name);
            throw new IllegalArgumentException(var1.toString());
         }
      }

      public GlideExecutor.Builder setName(String var1) {
         this.name = var1;
         return this;
      }

      public GlideExecutor.Builder setThreadCount(int var1) {
         this.corePoolSize = var1;
         this.maximumPoolSize = var1;
         return this;
      }

      public GlideExecutor.Builder setThreadTimeoutMillis(long var1) {
         this.threadTimeoutMillis = var1;
         return this;
      }

      public GlideExecutor.Builder setUncaughtThrowableStrategy(GlideExecutor.UncaughtThrowableStrategy var1) {
         this.uncaughtThrowableStrategy = var1;
         return this;
      }
   }

   private static final class DefaultThreadFactory implements ThreadFactory {
      private static final int DEFAULT_PRIORITY = 9;
      private final String name;
      final boolean preventNetworkOperations;
      private int threadNum;
      final GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;

      DefaultThreadFactory(String var1, GlideExecutor.UncaughtThrowableStrategy var2, boolean var3) {
         this.name = var1;
         this.uncaughtThrowableStrategy = var2;
         this.preventNetworkOperations = var3;
      }

      public Thread newThread(Runnable var1) {
         synchronized(this){}

         Thread var5;
         try {
            StringBuilder var2 = new StringBuilder();
            var2.append("glide-");
            var2.append(this.name);
            var2.append("-thread-");
            var2.append(this.threadNum);
            var5 = new Thread(var1, var2.toString()) {
               public void run() {
                  Process.setThreadPriority(9);
                  if (DefaultThreadFactory.this.preventNetworkOperations) {
                     StrictMode.setThreadPolicy((new android.os.StrictMode.ThreadPolicy.Builder()).detectNetwork().penaltyDeath().build());
                  }

                  try {
                     super.run();
                  } catch (Throwable var3) {
                     DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(var3);
                     return;
                  }
               }
            };
            ++this.threadNum;
         } finally {
            ;
         }

         return var5;
      }
   }

   public interface UncaughtThrowableStrategy {
      GlideExecutor.UncaughtThrowableStrategy DEFAULT = LOG;
      GlideExecutor.UncaughtThrowableStrategy IGNORE = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
         }
      };
      GlideExecutor.UncaughtThrowableStrategy LOG = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
            if (var1 != null && Log.isLoggable("GlideExecutor", 6)) {
               Log.e("GlideExecutor", "Request threw uncaught throwable", var1);
            }

         }
      };
      GlideExecutor.UncaughtThrowableStrategy THROW = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
            if (var1 != null) {
               throw new RuntimeException("Request threw uncaught throwable", var1);
            }
         }
      };

      void handle(Throwable var1);
   }
}
