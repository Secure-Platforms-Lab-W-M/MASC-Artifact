package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import java.util.List;

@TargetApi(21)
@RequiresApi(21)
class AccessibilityNodeInfoCompatApi21 {
   static void addAction(Object var0, Object var1) {
      ((AccessibilityNodeInfo)var0).addAction((AccessibilityAction)var1);
   }

   static int getAccessibilityActionId(Object var0) {
      return ((AccessibilityAction)var0).getId();
   }

   static CharSequence getAccessibilityActionLabel(Object var0) {
      return ((AccessibilityAction)var0).getLabel();
   }

   static List getActionList(Object var0) {
      return (List)((AccessibilityNodeInfo)var0).getActionList();
   }

   public static CharSequence getError(Object var0) {
      return ((AccessibilityNodeInfo)var0).getError();
   }

   public static int getMaxTextLength(Object var0) {
      return ((AccessibilityNodeInfo)var0).getMaxTextLength();
   }

   public static Object getWindow(Object var0) {
      return ((AccessibilityNodeInfo)var0).getWindow();
   }

   static Object newAccessibilityAction(int var0, CharSequence var1) {
      return new AccessibilityAction(var0, var1);
   }

   public static Object obtainCollectionInfo(int var0, int var1, boolean var2, int var3) {
      return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(var0, var1, var2, var3);
   }

   public static Object obtainCollectionItemInfo(int var0, int var1, int var2, int var3, boolean var4, boolean var5) {
      return android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(var0, var1, var2, var3, var4, var5);
   }

   public static boolean removeAction(Object var0, Object var1) {
      return ((AccessibilityNodeInfo)var0).removeAction((AccessibilityAction)var1);
   }

   public static boolean removeChild(Object var0, View var1) {
      return ((AccessibilityNodeInfo)var0).removeChild(var1);
   }

   public static boolean removeChild(Object var0, View var1, int var2) {
      return ((AccessibilityNodeInfo)var0).removeChild(var1, var2);
   }

   public static void setError(Object var0, CharSequence var1) {
      ((AccessibilityNodeInfo)var0).setError(var1);
   }

   public static void setMaxTextLength(Object var0, int var1) {
      ((AccessibilityNodeInfo)var0).setMaxTextLength(var1);
   }

   static class CollectionInfo {
      public static int getSelectionMode(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo)var0).getSelectionMode();
      }
   }

   static class CollectionItemInfo {
      public static boolean isSelected(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo)var0).isSelected();
      }
   }
}
