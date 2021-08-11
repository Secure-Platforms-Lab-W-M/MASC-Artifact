package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(22)
@RequiresApi(22)
class AccessibilityNodeInfoCompatApi22 {
   public static Object getTraversalAfter(Object var0) {
      return ((AccessibilityNodeInfo)var0).getTraversalAfter();
   }

   public static Object getTraversalBefore(Object var0) {
      return ((AccessibilityNodeInfo)var0).getTraversalBefore();
   }

   public static void setTraversalAfter(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setTraversalAfter(var1);
   }

   public static void setTraversalAfter(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setTraversalAfter(var1, var2);
   }

   public static void setTraversalBefore(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setTraversalBefore(var1);
   }

   public static void setTraversalBefore(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setTraversalBefore(var1, var2);
   }
}
