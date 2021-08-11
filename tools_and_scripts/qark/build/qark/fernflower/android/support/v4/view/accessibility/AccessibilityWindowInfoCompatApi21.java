package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityWindowInfo;

@TargetApi(21)
@RequiresApi(21)
class AccessibilityWindowInfoCompatApi21 {
   public static void getBoundsInScreen(Object var0, Rect var1) {
      ((AccessibilityWindowInfo)var0).getBoundsInScreen(var1);
   }

   public static Object getChild(Object var0, int var1) {
      return ((AccessibilityWindowInfo)var0).getChild(var1);
   }

   public static int getChildCount(Object var0) {
      return ((AccessibilityWindowInfo)var0).getChildCount();
   }

   public static int getId(Object var0) {
      return ((AccessibilityWindowInfo)var0).getId();
   }

   public static int getLayer(Object var0) {
      return ((AccessibilityWindowInfo)var0).getLayer();
   }

   public static Object getParent(Object var0) {
      return ((AccessibilityWindowInfo)var0).getParent();
   }

   public static Object getRoot(Object var0) {
      return ((AccessibilityWindowInfo)var0).getRoot();
   }

   public static int getType(Object var0) {
      return ((AccessibilityWindowInfo)var0).getType();
   }

   public static boolean isAccessibilityFocused(Object var0) {
      return ((AccessibilityWindowInfo)var0).isAccessibilityFocused();
   }

   public static boolean isActive(Object var0) {
      return ((AccessibilityWindowInfo)var0).isActive();
   }

   public static boolean isFocused(Object var0) {
      return ((AccessibilityWindowInfo)var0).isFocused();
   }

   public static Object obtain() {
      return AccessibilityWindowInfo.obtain();
   }

   public static Object obtain(Object var0) {
      return AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)var0);
   }

   public static void recycle(Object var0) {
      ((AccessibilityWindowInfo)var0).recycle();
   }
}
