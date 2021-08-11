package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;

@TargetApi(23)
@RequiresApi(23)
class AccessibilityNodeInfoCompatApi23 {
   public static Object getActionContextClick() {
      return AccessibilityAction.ACTION_CONTEXT_CLICK;
   }

   public static Object getActionScrollDown() {
      return AccessibilityAction.ACTION_SCROLL_DOWN;
   }

   public static Object getActionScrollLeft() {
      return AccessibilityAction.ACTION_SCROLL_LEFT;
   }

   public static Object getActionScrollRight() {
      return AccessibilityAction.ACTION_SCROLL_RIGHT;
   }

   public static Object getActionScrollToPosition() {
      return AccessibilityAction.ACTION_SCROLL_TO_POSITION;
   }

   public static Object getActionScrollUp() {
      return AccessibilityAction.ACTION_SCROLL_UP;
   }

   public static Object getActionShowOnScreen() {
      return AccessibilityAction.ACTION_SHOW_ON_SCREEN;
   }

   public static boolean isContextClickable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isContextClickable();
   }

   public static void setContextClickable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setContextClickable(var1);
   }
}
