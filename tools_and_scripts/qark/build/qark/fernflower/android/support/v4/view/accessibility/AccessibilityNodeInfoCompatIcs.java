package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityNodeInfoCompatIcs {
   public static void addAction(Object var0, int var1) {
      ((AccessibilityNodeInfo)var0).addAction(var1);
   }

   public static void addChild(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).addChild(var1);
   }

   public static List findAccessibilityNodeInfosByText(Object var0, String var1) {
      return (List)((AccessibilityNodeInfo)var0).findAccessibilityNodeInfosByText(var1);
   }

   public static int getActions(Object var0) {
      return ((AccessibilityNodeInfo)var0).getActions();
   }

   public static void getBoundsInParent(Object var0, Rect var1) {
      ((AccessibilityNodeInfo)var0).getBoundsInParent(var1);
   }

   public static void getBoundsInScreen(Object var0, Rect var1) {
      ((AccessibilityNodeInfo)var0).getBoundsInScreen(var1);
   }

   public static Object getChild(Object var0, int var1) {
      return ((AccessibilityNodeInfo)var0).getChild(var1);
   }

   public static int getChildCount(Object var0) {
      return ((AccessibilityNodeInfo)var0).getChildCount();
   }

   public static CharSequence getClassName(Object var0) {
      return ((AccessibilityNodeInfo)var0).getClassName();
   }

   public static CharSequence getContentDescription(Object var0) {
      return ((AccessibilityNodeInfo)var0).getContentDescription();
   }

   public static CharSequence getPackageName(Object var0) {
      return ((AccessibilityNodeInfo)var0).getPackageName();
   }

   public static Object getParent(Object var0) {
      return ((AccessibilityNodeInfo)var0).getParent();
   }

   public static CharSequence getText(Object var0) {
      return ((AccessibilityNodeInfo)var0).getText();
   }

   public static int getWindowId(Object var0) {
      return ((AccessibilityNodeInfo)var0).getWindowId();
   }

   public static boolean isCheckable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isCheckable();
   }

   public static boolean isChecked(Object var0) {
      return ((AccessibilityNodeInfo)var0).isChecked();
   }

   public static boolean isClickable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isClickable();
   }

   public static boolean isEnabled(Object var0) {
      return ((AccessibilityNodeInfo)var0).isEnabled();
   }

   public static boolean isFocusable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isFocusable();
   }

   public static boolean isFocused(Object var0) {
      return ((AccessibilityNodeInfo)var0).isFocused();
   }

   public static boolean isLongClickable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isLongClickable();
   }

   public static boolean isPassword(Object var0) {
      return ((AccessibilityNodeInfo)var0).isPassword();
   }

   public static boolean isScrollable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isScrollable();
   }

   public static boolean isSelected(Object var0) {
      return ((AccessibilityNodeInfo)var0).isSelected();
   }

   public static Object obtain() {
      return AccessibilityNodeInfo.obtain();
   }

   public static Object obtain(View var0) {
      return AccessibilityNodeInfo.obtain(var0);
   }

   public static Object obtain(Object var0) {
      return AccessibilityNodeInfo.obtain((AccessibilityNodeInfo)var0);
   }

   public static boolean performAction(Object var0, int var1) {
      return ((AccessibilityNodeInfo)var0).performAction(var1);
   }

   public static void recycle(Object var0) {
      ((AccessibilityNodeInfo)var0).recycle();
   }

   public static void setBoundsInParent(Object var0, Rect var1) {
      ((AccessibilityNodeInfo)var0).setBoundsInParent(var1);
   }

   public static void setBoundsInScreen(Object var0, Rect var1) {
      ((AccessibilityNodeInfo)var0).setBoundsInScreen(var1);
   }

   public static void setCheckable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setCheckable(var1);
   }

   public static void setChecked(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setChecked(var1);
   }

   public static void setClassName(Object var0, CharSequence var1) {
      ((AccessibilityNodeInfo)var0).setClassName(var1);
   }

   public static void setClickable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setClickable(var1);
   }

   public static void setContentDescription(Object var0, CharSequence var1) {
      ((AccessibilityNodeInfo)var0).setContentDescription(var1);
   }

   public static void setEnabled(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setEnabled(var1);
   }

   public static void setFocusable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setFocusable(var1);
   }

   public static void setFocused(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setFocused(var1);
   }

   public static void setLongClickable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setLongClickable(var1);
   }

   public static void setPackageName(Object var0, CharSequence var1) {
      ((AccessibilityNodeInfo)var0).setPackageName(var1);
   }

   public static void setParent(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setParent(var1);
   }

   public static void setPassword(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setPassword(var1);
   }

   public static void setScrollable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setScrollable(var1);
   }

   public static void setSelected(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setSelected(var1);
   }

   public static void setSource(Object var0, View var1) {
      ((AccessibilityNodeInfo)var0).setSource(var1);
   }

   public static void setText(Object var0, CharSequence var1) {
      ((AccessibilityNodeInfo)var0).setText(var1);
   }
}
