package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityRecord;

@TargetApi(15)
@RequiresApi(15)
class AccessibilityRecordCompatIcsMr1 {
   public static int getMaxScrollX(Object var0) {
      return ((AccessibilityRecord)var0).getMaxScrollX();
   }

   public static int getMaxScrollY(Object var0) {
      return ((AccessibilityRecord)var0).getMaxScrollY();
   }

   public static void setMaxScrollX(Object var0, int var1) {
      ((AccessibilityRecord)var0).setMaxScrollX(var1);
   }

   public static void setMaxScrollY(Object var0, int var1) {
      ((AccessibilityRecord)var0).setMaxScrollY(var1);
   }
}
