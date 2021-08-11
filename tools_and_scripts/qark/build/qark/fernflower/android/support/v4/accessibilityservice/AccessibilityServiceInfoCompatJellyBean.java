package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityServiceInfoCompatJellyBean {
   public static String loadDescription(AccessibilityServiceInfo var0, PackageManager var1) {
      return var0.loadDescription(var1);
   }
}
