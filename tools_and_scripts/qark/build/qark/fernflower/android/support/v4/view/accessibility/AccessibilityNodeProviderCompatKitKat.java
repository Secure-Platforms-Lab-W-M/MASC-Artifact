package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

@RequiresApi(19)
class AccessibilityNodeProviderCompatKitKat {
   public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge var0) {
      return new AccessibilityNodeProvider() {
         public AccessibilityNodeInfo createAccessibilityNodeInfo(int var1) {
            return (AccessibilityNodeInfo)var0.createAccessibilityNodeInfo(var1);
         }

         public List findAccessibilityNodeInfosByText(String var1, int var2) {
            return var0.findAccessibilityNodeInfosByText(var1, var2);
         }

         public AccessibilityNodeInfo findFocus(int var1) {
            return (AccessibilityNodeInfo)var0.findFocus(var1);
         }

         public boolean performAction(int var1, int var2, Bundle var3) {
            return var0.performAction(var1, var2, var3);
         }
      };
   }

   interface AccessibilityNodeInfoBridge {
      Object createAccessibilityNodeInfo(int var1);

      List findAccessibilityNodeInfosByText(String var1, int var2);

      Object findFocus(int var1);

      boolean performAction(int var1, int var2, Bundle var3);
   }
}
