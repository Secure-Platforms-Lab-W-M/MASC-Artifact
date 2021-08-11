package androidx.core.app;

import android.app.ActivityManager;
import android.os.Build.VERSION;

public final class ActivityManagerCompat {
   private ActivityManagerCompat() {
   }

   public static boolean isLowRamDevice(ActivityManager var0) {
      return VERSION.SDK_INT >= 19 ? var0.isLowRamDevice() : false;
   }
}
