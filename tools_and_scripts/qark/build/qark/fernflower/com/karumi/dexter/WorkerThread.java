package com.karumi.dexter;

import android.os.Handler;
import android.os.Looper;

final class WorkerThread implements Thread {
   private final Handler handler;
   private boolean wasLooperNull = false;

   WorkerThread() {
      if (Looper.myLooper() == null) {
         this.wasLooperNull = true;
         Looper.prepare();
      }

      this.handler = new Handler();
   }

   public void execute(Runnable var1) {
      this.handler.post(var1);
   }

   public void loop() {
      if (this.wasLooperNull) {
         Looper.loop();
      }

   }
}
