package android.support.v4.view;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityDelegateCompatJellyBean {
   public static Object getAccessibilityNodeProvider(Object var0, View var1) {
      return ((AccessibilityDelegate)var0).getAccessibilityNodeProvider(var1);
   }

   public static Object newAccessibilityDelegateBridge(final AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean var0) {
      return new AccessibilityDelegate() {
         public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
            return var0.dispatchPopulateAccessibilityEvent(var1, var2);
         }

         public AccessibilityNodeProvider getAccessibilityNodeProvider(View var1) {
            return (AccessibilityNodeProvider)var0.getAccessibilityNodeProvider(var1);
         }

         public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
            var0.onInitializeAccessibilityEvent(var1, var2);
         }

         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfo var2) {
            var0.onInitializeAccessibilityNodeInfo(var1, var2);
         }

         public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
            var0.onPopulateAccessibilityEvent(var1, var2);
         }

         public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
            return var0.onRequestSendAccessibilityEvent(var1, var2, var3);
         }

         public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
            return var0.performAccessibilityAction(var1, var2, var3);
         }

         public void sendAccessibilityEvent(View var1, int var2) {
            var0.sendAccessibilityEvent(var1, var2);
         }

         public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
            var0.sendAccessibilityEventUnchecked(var1, var2);
         }
      };
   }

   public static boolean performAccessibilityAction(Object var0, View var1, int var2, Bundle var3) {
      return ((AccessibilityDelegate)var0).performAccessibilityAction(var1, var2, var3);
   }

   public interface AccessibilityDelegateBridgeJellyBean {
      boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2);

      Object getAccessibilityNodeProvider(View var1);

      void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2);

      void onInitializeAccessibilityNodeInfo(View var1, Object var2);

      void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2);

      boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3);

      boolean performAccessibilityAction(View var1, int var2, Bundle var3);

      void sendAccessibilityEvent(View var1, int var2);

      void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2);
   }
}
