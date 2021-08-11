package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class ActivityManagerCompatKitKat {
   public static boolean isLowRamDevice(ActivityManager var0) {
      return var0.isLowRamDevice();
   }
}
