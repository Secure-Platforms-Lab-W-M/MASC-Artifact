package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AccessibilityNodeInfoCompatKitKat {
   static class RangeInfo {
      static float getCurrent(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo)var0).getCurrent();
      }

      static float getMax(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo)var0).getMax();
      }

      static float getMin(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo)var0).getMin();
      }

      static int getType(Object var0) {
         return ((android.view.accessibility.AccessibilityNodeInfo.RangeInfo)var0).getType();
      }
   }
}
