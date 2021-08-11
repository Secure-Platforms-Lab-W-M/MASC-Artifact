package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityWindowInfo;

@TargetApi(24)
@RequiresApi(24)
class AccessibilityWindowInfoCompatApi24 {
   public static Object getAnchor(Object var0) {
      return ((AccessibilityWindowInfo)var0).getAnchor();
   }

   public static CharSequence getTitle(Object var0) {
      return ((AccessibilityWindowInfo)var0).getTitle();
   }
}
