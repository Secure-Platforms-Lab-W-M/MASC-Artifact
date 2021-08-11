package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(17)
@RequiresApi(17)
class AccessibilityNodeInfoCompatJellybeanMr1 {
   public static Object getLabelFor(Object var0) {
      return ((AccessibilityNodeInfo)var0).getLabelFor();
   }

   public static Object getLabeledBy(Object var0) {
      return ((AccessibilityNodeInfo)var0).getLabeledBy();
   }

   public static void setLabelFor(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setLabelFor(var1);
   }

   public static void setLabelFor(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setLabelFor(var1, var2);
   }

   public static void setLabeledBy(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setLabeledBy(var1);
   }

   public static void setLabeledBy(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setLabeledBy(var1, var2);
   }
}
