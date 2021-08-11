package com.karumi.dexter;

import android.os.Handler;
import android.os.Looper;

final class MainThread implements Thread {
   private static boolean runningMainThread() {
      return Looper.getMainLooper() == Looper.myLooper();
   }

   public void execute(Runnable var1) {
      if (runningMainThread()) {
         var1.run();
      } else {
         (new Handler(Looper.getMainLooper())).post(var1);
      }
   }

   public void loop() {
   }
}
