package com.bumptech.glide.util;

import android.os.SystemClock;
import android.os.Build.VERSION;

public final class LogTime {
   private static final double MILLIS_MULTIPLIER;

   static {
      int var2 = VERSION.SDK_INT;
      double var0 = 1.0D;
      if (var2 >= 17) {
         var0 = 1.0D / Math.pow(10.0D, 6.0D);
      }

      MILLIS_MULTIPLIER = var0;
   }

   private LogTime() {
   }

   public static double getElapsedMillis(long var0) {
      return (double)(getLogTime() - var0) * MILLIS_MULTIPLIER;
   }

   public static long getLogTime() {
      return VERSION.SDK_INT >= 17 ? SystemClock.elapsedRealtimeNanos() : SystemClock.uptimeMillis();
   }
}
