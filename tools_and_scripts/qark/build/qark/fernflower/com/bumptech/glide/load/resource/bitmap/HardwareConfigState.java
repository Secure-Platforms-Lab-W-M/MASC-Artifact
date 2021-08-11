package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;

public final class HardwareConfigState {
   private static final File FD_SIZE_LIST = new File("/proc/self/fd");
   private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_O = 700;
   private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P = 20000;
   private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
   static final int MIN_HARDWARE_DIMENSION_O = 128;
   private static final int MIN_HARDWARE_DIMENSION_P = 0;
   private static volatile HardwareConfigState instance;
   private int decodesSinceLastFdCheck;
   private final int fdCountLimit;
   private boolean isFdSizeBelowHardwareLimit = true;
   private final boolean isHardwareConfigAllowedByDeviceModel = isHardwareConfigAllowedByDeviceModel();
   private final int minHardwareDimension;

   HardwareConfigState() {
      if (VERSION.SDK_INT >= 28) {
         this.fdCountLimit = 20000;
         this.minHardwareDimension = 0;
      } else {
         this.fdCountLimit = 700;
         this.minHardwareDimension = 128;
      }
   }

   public static HardwareConfigState getInstance() {
      if (instance == null) {
         synchronized(HardwareConfigState.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  instance = new HardwareConfigState();
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var0 = var10000;

            try {
               throw var0;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }

   private boolean isFdSizeBelowHardwareLimit() {
      synchronized(this){}

      Throwable var10000;
      label391: {
         int var1;
         boolean var10001;
         try {
            var1 = this.decodesSinceLastFdCheck;
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label391;
         }

         boolean var2 = true;
         ++var1;

         try {
            this.decodesSinceLastFdCheck = var1;
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label391;
         }

         if (var1 >= 50) {
            label375: {
               try {
                  this.decodesSinceLastFdCheck = 0;
                  var1 = FD_SIZE_LIST.list().length;
                  if (var1 < this.fdCountLimit) {
                     break label375;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label391;
               }

               var2 = false;
            }

            try {
               this.isFdSizeBelowHardwareLimit = var2;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label391;
            }

            if (!var2) {
               try {
                  if (Log.isLoggable("Downsampler", 5)) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
                     var3.append(var1);
                     var3.append(", limit ");
                     var3.append(this.fdCountLimit);
                     Log.w("Downsampler", var3.toString());
                  }
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label391;
               }
            }
         }

         label365:
         try {
            var2 = this.isFdSizeBelowHardwareLimit;
            return var2;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label365;
         }
      }

      Throwable var46 = var10000;
      throw var46;
   }

   private static boolean isHardwareConfigAllowedByDeviceModel() {
      if (Build.MODEL != null) {
         if (Build.MODEL.length() < 7) {
            return true;
         } else {
            String var1 = Build.MODEL.substring(0, 7);
            byte var0 = -1;
            switch(var1.hashCode()) {
            case -1398613787:
               if (var1.equals("SM-A520")) {
                  var0 = 6;
               }
               break;
            case -1398431166:
               if (var1.equals("SM-G930")) {
                  var0 = 5;
               }
               break;
            case -1398431161:
               if (var1.equals("SM-G935")) {
                  var0 = 4;
               }
               break;
            case -1398431073:
               if (var1.equals("SM-G960")) {
                  var0 = 2;
               }
               break;
            case -1398431068:
               if (var1.equals("SM-G965")) {
                  var0 = 3;
               }
               break;
            case -1398343746:
               if (var1.equals("SM-J720")) {
                  var0 = 1;
               }
               break;
            case -1398222624:
               if (var1.equals("SM-N935")) {
                  var0 = 0;
               }
            }

            switch(var0) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               if (VERSION.SDK_INT != 26) {
                  return true;
               }

               return false;
            default:
               return true;
            }
         }
      } else {
         return true;
      }
   }

   public boolean isHardwareConfigAllowed(int var1, int var2, boolean var3, boolean var4) {
      if (var3 && this.isHardwareConfigAllowedByDeviceModel && VERSION.SDK_INT >= 26) {
         if (var4) {
            return false;
         } else {
            int var5 = this.minHardwareDimension;
            return var1 >= var5 && var2 >= var5 && this.isFdSizeBelowHardwareLimit();
         }
      } else {
         return false;
      }
   }

   boolean setHardwareConfigIfAllowed(int var1, int var2, Options var3, boolean var4, boolean var5) {
      var4 = this.isHardwareConfigAllowed(var1, var2, var4, var5);
      if (var4) {
         var3.inPreferredConfig = Config.HARDWARE;
         var3.inMutable = false;
      }

      return var4;
   }
}
