package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;

@TargetApi(24)
@RequiresApi(24)
class AccessibilityNodeInfoCompatApi24 {
   public static Object getActionSetProgress() {
      return AccessibilityAction.ACTION_SET_PROGRESS;
   }

   public static int getDrawingOrder(Object var0) {
      return ((AccessibilityNodeInfo)var0).getDrawingOrder();
   }

   public static boolean isImportantForAccessibility(Object var0) {
      return ((AccessibilityNodeInfo)var0).isImportantForAccessibility();
   }

   public static void setDrawingOrder(Object var0, int var1) {
      ((AccessibilityNodeInfo)var0).setDrawingOrder(var1);
   }

   public static void setImportantForAccessibility(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setImportantForAccessibility(var1);
   }
}
