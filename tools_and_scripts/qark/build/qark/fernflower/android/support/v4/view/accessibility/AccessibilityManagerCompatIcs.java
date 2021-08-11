package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityManagerCompatIcs {
   public static boolean addAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper var1) {
      return var0.addAccessibilityStateChangeListener(var1);
   }

   public static List getEnabledAccessibilityServiceList(AccessibilityManager var0, int var1) {
      return var0.getEnabledAccessibilityServiceList(var1);
   }

   public static List getInstalledAccessibilityServiceList(AccessibilityManager var0) {
      return var0.getInstalledAccessibilityServiceList();
   }

   public static boolean isTouchExplorationEnabled(AccessibilityManager var0) {
      return var0.isTouchExplorationEnabled();
   }

   public static boolean removeAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper var1) {
      return var0.removeAccessibilityStateChangeListener(var1);
   }

   interface AccessibilityStateChangeListenerBridge {
      void onAccessibilityStateChanged(boolean var1);
   }

   public static class AccessibilityStateChangeListenerWrapper implements AccessibilityStateChangeListener {
      Object mListener;
      AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge mListenerBridge;

      public AccessibilityStateChangeListenerWrapper(Object var1, AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge var2) {
         this.mListener = var1;
         this.mListenerBridge = var2;
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 == null || this.getClass() != var1.getClass()) {
               return false;
            }

            AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper var2 = (AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper)var1;
            if (this.mListener != null) {
               return this.mListener.equals(var2.mListener);
            }

            if (var2.mListener != null) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         return this.mListener == null ? 0 : this.mListener.hashCode();
      }

      public void onAccessibilityStateChanged(boolean var1) {
         this.mListenerBridge.onAccessibilityStateChanged(var1);
      }
   }
}
