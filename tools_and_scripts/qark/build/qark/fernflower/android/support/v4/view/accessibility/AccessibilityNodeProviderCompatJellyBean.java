package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

@RequiresApi(16)
class AccessibilityNodeProviderCompatJellyBean {
   public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge var0) {
      return new AccessibilityNodeProvider() {
         public AccessibilityNodeInfo createAccessibilityNodeInfo(int var1) {
            return (AccessibilityNodeInfo)var0.createAccessibilityNodeInfo(var1);
         }

         public List findAccessibilityNodeInfosByText(String var1, int var2) {
            return var0.findAccessibilityNodeInfosByText(var1, var2);
         }

         public boolean performAction(int var1, int var2, Bundle var3) {
            return var0.performAction(var1, var2, var3);
         }
      };
   }

   interface AccessibilityNodeInfoBridge {
      Object createAccessibilityNodeInfo(int var1);

      List findAccessibilityNodeInfosByText(String var1, int var2);

      boolean performAction(int var1, int var2, Bundle var3);
   }
}
