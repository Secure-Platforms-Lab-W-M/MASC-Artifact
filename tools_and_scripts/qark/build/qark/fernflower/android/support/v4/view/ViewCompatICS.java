package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(14)
@RequiresApi(14)
class ViewCompatICS {
   public static boolean canScrollHorizontally(View var0, int var1) {
      return var0.canScrollHorizontally(var1);
   }

   public static boolean canScrollVertically(View var0, int var1) {
      return var0.canScrollVertically(var1);
   }

   public static void onInitializeAccessibilityEvent(View var0, AccessibilityEvent var1) {
      var0.onInitializeAccessibilityEvent(var1);
   }

   public static void onInitializeAccessibilityNodeInfo(View var0, Object var1) {
      var0.onInitializeAccessibilityNodeInfo((AccessibilityNodeInfo)var1);
   }

   public static void onPopulateAccessibilityEvent(View var0, AccessibilityEvent var1) {
      var0.onPopulateAccessibilityEvent(var1);
   }

   public static void setAccessibilityDelegate(View var0, @Nullable Object var1) {
      var0.setAccessibilityDelegate((AccessibilityDelegate)var1);
   }

   public static void setFitsSystemWindows(View var0, boolean var1) {
      var0.setFitsSystemWindows(var1);
   }
}
