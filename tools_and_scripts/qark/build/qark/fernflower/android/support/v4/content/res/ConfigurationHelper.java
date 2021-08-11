package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class ConfigurationHelper {
   private ConfigurationHelper() {
   }

   public static int getDensityDpi(@NonNull Resources var0) {
      return VERSION.SDK_INT >= 17 ? var0.getConfiguration().densityDpi : var0.getDisplayMetrics().densityDpi;
   }

   @Deprecated
   public static int getScreenHeightDp(@NonNull Resources var0) {
      return var0.getConfiguration().screenHeightDp;
   }

   @Deprecated
   public static int getScreenWidthDp(@NonNull Resources var0) {
      return var0.getConfiguration().screenWidthDp;
   }

   @Deprecated
   public static int getSmallestScreenWidthDp(@NonNull Resources var0) {
      return var0.getConfiguration().smallestScreenWidthDp;
   }
}
