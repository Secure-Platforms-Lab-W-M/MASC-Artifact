package androidx.core.content.res;

import android.content.res.Resources;
import android.os.Build.VERSION;

public final class ConfigurationHelper {
   private ConfigurationHelper() {
   }

   public static int getDensityDpi(Resources var0) {
      return VERSION.SDK_INT >= 17 ? var0.getConfiguration().densityDpi : var0.getDisplayMetrics().densityDpi;
   }
}
