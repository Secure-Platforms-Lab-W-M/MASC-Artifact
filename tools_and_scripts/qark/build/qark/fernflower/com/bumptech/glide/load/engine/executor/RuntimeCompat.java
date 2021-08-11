package com.bumptech.glide.load.engine.executor;

import android.os.StrictMode;
import android.os.Build.VERSION;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

final class RuntimeCompat {
   private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
   private static final String CPU_NAME_REGEX = "cpu[0-9]+";
   private static final String TAG = "GlideRuntimeCompat";

   private RuntimeCompat() {
   }

   static int availableProcessors() {
      int var1 = Runtime.getRuntime().availableProcessors();
      int var0 = var1;
      if (VERSION.SDK_INT < 17) {
         var0 = Math.max(getCoreCountPre17(), var1);
      }

      return var0;
   }

   private static int getCoreCountPre17() {
      Object var2 = null;
      ThreadPolicy var3 = StrictMode.allowThreadDiskReads();

      File[] var1;
      try {
         var1 = (new File("/sys/devices/system/cpu/")).listFiles(new FilenameFilter(Pattern.compile("cpu[0-9]+")) {
            // $FF: synthetic field
            final Pattern val$cpuNamePattern;

            {
               this.val$cpuNamePattern = var1;
            }

            public boolean accept(File var1, String var2) {
               return this.val$cpuNamePattern.matcher(var2).matches();
            }
         });
      } catch (Throwable var11) {
         label94: {
            Throwable var4 = var11;
            var1 = (File[])var2;
            boolean var7 = false;

            try {
               var7 = true;
               if (!Log.isLoggable("GlideRuntimeCompat", 6)) {
                  var7 = false;
                  break label94;
               }

               Log.e("GlideRuntimeCompat", "Failed to calculate accurate cpu count", var4);
               var7 = false;
            } finally {
               if (var7) {
                  StrictMode.setThreadPolicy(var3);
               }
            }

            var1 = (File[])var2;
            break label94;
         }
      }

      StrictMode.setThreadPolicy(var3);
      int var0;
      if (var1 != null) {
         var0 = var1.length;
      } else {
         var0 = 0;
      }

      return Math.max(1, var0);
   }
}
