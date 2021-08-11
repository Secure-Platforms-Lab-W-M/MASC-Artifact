package com.bumptech.glide.util;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Executors {
   private static final Executor DIRECT_EXECUTOR = new Executor() {
      public void execute(Runnable var1) {
         var1.run();
      }
   };
   private static final Executor MAIN_THREAD_EXECUTOR = new Executor() {
      private final Handler handler = new Handler(Looper.getMainLooper());

      public void execute(Runnable var1) {
         this.handler.post(var1);
      }
   };

   private Executors() {
   }

   public static Executor directExecutor() {
      return DIRECT_EXECUTOR;
   }

   public static Executor mainThreadExecutor() {
      return MAIN_THREAD_EXECUTOR;
   }

   public static void shutdownAndAwaitTermination(ExecutorService var0) {
      var0.shutdownNow();

      try {
         if (!var0.awaitTermination(5L, TimeUnit.SECONDS)) {
            var0.shutdownNow();
            if (!var0.awaitTermination(5L, TimeUnit.SECONDS)) {
               throw new RuntimeException("Failed to shutdown");
            }
         }
      } catch (InterruptedException var2) {
         var0.shutdownNow();
         Thread.currentThread().interrupt();
         throw new RuntimeException(var2);
      }
   }
}
