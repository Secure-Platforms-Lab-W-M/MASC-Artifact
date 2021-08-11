package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

@TargetApi(19)
@RequiresApi(19)
class AccessibilityEventCompatKitKat {
   public static int getContentChangeTypes(AccessibilityEvent var0) {
      return var0.getContentChangeTypes();
   }

   public static void setContentChangeTypes(AccessibilityEvent var0, int var1) {
      var0.setContentChangeTypes(var1);
   }
}
