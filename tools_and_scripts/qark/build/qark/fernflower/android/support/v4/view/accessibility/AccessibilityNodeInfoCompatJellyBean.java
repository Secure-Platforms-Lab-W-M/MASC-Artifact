package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityNodeInfoCompatJellyBean {
   public static void addChild(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).addChild(var1, var2);
   }

   public static Object findFocus(Object var0, int var1) {
      return ((AccessibilityNodeInfo)var0).findFocus(var1);
   }

   public static Object focusSearch(Object var0, int var1) {
      return ((AccessibilityNodeInfo)var0).focusSearch(var1);
   }

   public static int getMovementGranularities(Object var0) {
      return ((AccessibilityNodeInfo)var0).getMovementGranularities();
   }

   public static boolean isAccessibilityFocused(Object var0) {
      return ((AccessibilityNodeInfo)var0).isAccessibilityFocused();
   }

   public static boolean isVisibleToUser(Object var0) {
      return ((AccessibilityNodeInfo)var0).isVisibleToUser();
   }

   public static Object obtain(View var0, int var1) {
      return AccessibilityNodeInfo.obtain(var0, var1);
   }

   public static boolean performAction(Object var0, int var1, Bundle var2) {
      return ((AccessibilityNodeInfo)var0).performAction(var1, var2);
   }

   public static void setAccesibilityFocused(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setAccessibilityFocused(var1);
   }

   public static void setMovementGranularities(Object var0, int var1) {
      ((AccessibilityNodeInfo)var0).setMovementGranularities(var1);
   }

   public static void setParent(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setParent(var1, var2);
   }

   public static void setSource(Object var0, View var1, int var2) {
      ((AccessibilityNodeInfo)var0).setSource(var1, var2);
   }

   public static void setVisibleToUser(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setVisibleToUser(var1);
   }
}
