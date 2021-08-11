package android.support.v4.app;

import android.app.ActivityManager;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class ActivityManagerCompat {
   private ActivityManagerCompat() {
   }

   public static boolean isLowRamDevice(@NonNull ActivityManager var0) {
      return VERSION.SDK_INT >= 19 ? var0.isLowRamDevice() : false;
   }
}
