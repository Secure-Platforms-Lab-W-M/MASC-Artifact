package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener;

@TargetApi(19)
@RequiresApi(19)
class AccessibilityManagerCompatKitKat {
   public static boolean addTouchExplorationStateChangeListener(AccessibilityManager var0, Object var1) {
      return var0.addTouchExplorationStateChangeListener((TouchExplorationStateChangeListener)var1);
   }

   public static Object newTouchExplorationStateChangeListener(final AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge var0) {
      return new TouchExplorationStateChangeListener() {
         public void onTouchExplorationStateChanged(boolean var1) {
            var0.onTouchExplorationStateChanged(var1);
         }
      };
   }

   public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager var0, Object var1) {
      return var0.removeTouchExplorationStateChangeListener((TouchExplorationStateChangeListener)var1);
   }

   interface TouchExplorationStateChangeListenerBridge {
      void onTouchExplorationStateChanged(boolean var1);
   }

   public static class TouchExplorationStateChangeListenerWrapper implements TouchExplorationStateChangeListener {
      final Object mListener;
      final AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge mListenerBridge;

      public TouchExplorationStateChangeListenerWrapper(Object var1, AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge var2) {
         this.mListener = var1;
         this.mListenerBridge = var2;
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 == null || this.getClass() != var1.getClass()) {
               return false;
            }

            AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper var2 = (AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper)var1;
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

      public void onTouchExplorationStateChanged(boolean var1) {
         this.mListenerBridge.onTouchExplorationStateChanged(var1);
      }
   }
}
