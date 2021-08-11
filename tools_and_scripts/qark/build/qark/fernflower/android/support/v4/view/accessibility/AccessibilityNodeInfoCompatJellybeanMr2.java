package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

@TargetApi(18)
@RequiresApi(18)
class AccessibilityNodeInfoCompatJellybeanMr2 {
   public static List findAccessibilityNodeInfosByViewId(Object var0, String var1) {
      return (List)((AccessibilityNodeInfo)var0).findAccessibilityNodeInfosByViewId(var1);
   }

   public static int getTextSelectionEnd(Object var0) {
      return ((AccessibilityNodeInfo)var0).getTextSelectionEnd();
   }

   public static int getTextSelectionStart(Object var0) {
      return ((AccessibilityNodeInfo)var0).getTextSelectionStart();
   }

   public static String getViewIdResourceName(Object var0) {
      return ((AccessibilityNodeInfo)var0).getViewIdResourceName();
   }

   public static boolean isEditable(Object var0) {
      return ((AccessibilityNodeInfo)var0).isEditable();
   }

   public static boolean refresh(Object var0) {
      return ((AccessibilityNodeInfo)var0).refresh();
   }

   public static void setEditable(Object var0, boolean var1) {
      ((AccessibilityNodeInfo)var0).setEditable(var1);
   }

   public static void setTextSelection(Object var0, int var1, int var2) {
      ((AccessibilityNodeInfo)var0).setTextSelection(var1, var2);
   }

   public static void setViewIdResourceName(Object var0, String var1) {
      ((AccessibilityNodeInfo)var0).setViewIdResourceName(var1);
   }
}
