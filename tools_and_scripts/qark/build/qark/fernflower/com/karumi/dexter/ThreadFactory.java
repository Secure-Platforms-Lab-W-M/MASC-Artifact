package com.karumi.dexter;

import android.os.Looper;

final class ThreadFactory {
   public static Thread makeMainThread() {
      return new MainThread();
   }

   public static Thread makeSameThread() {
      return (Thread)(runningMainThread() ? new MainThread() : new WorkerThread());
   }

   private static boolean runningMainThread() {
      return Looper.getMainLooper().getThread() == java.lang.Thread.currentThread();
   }
}
