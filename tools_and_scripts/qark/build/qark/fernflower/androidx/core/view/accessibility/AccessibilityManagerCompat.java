package androidx.core.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

public final class AccessibilityManagerCompat {
   private AccessibilityManagerCompat() {
   }

   @Deprecated
   public static boolean addAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
      return var1 == null ? false : var0.addAccessibilityStateChangeListener(new AccessibilityManagerCompat.AccessibilityStateChangeListenerWrapper(var1));
   }

   public static boolean addTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      if (VERSION.SDK_INT >= 19) {
         return var1 == null ? false : var0.addTouchExplorationStateChangeListener(new AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper(var1));
      } else {
         return false;
      }
   }

   @Deprecated
   public static List getEnabledAccessibilityServiceList(AccessibilityManager var0, int var1) {
      return var0.getEnabledAccessibilityServiceList(var1);
   }

   @Deprecated
   public static List getInstalledAccessibilityServiceList(AccessibilityManager var0) {
      return var0.getInstalledAccessibilityServiceList();
   }

   @Deprecated
   public static boolean isTouchExplorationEnabled(AccessibilityManager var0) {
      return var0.isTouchExplorationEnabled();
   }

   @Deprecated
   public static boolean removeAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
      return var1 == null ? false : var0.removeAccessibilityStateChangeListener(new AccessibilityManagerCompat.AccessibilityStateChangeListenerWrapper(var1));
   }

   public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      if (VERSION.SDK_INT >= 19) {
         return var1 == null ? false : var0.removeTouchExplorationStateChangeListener(new AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper(var1));
      } else {
         return false;
      }
   }

   @Deprecated
   public interface AccessibilityStateChangeListener {
      @Deprecated
      void onAccessibilityStateChanged(boolean var1);
   }

   @Deprecated
   public abstract static class AccessibilityStateChangeListenerCompat implements AccessibilityManagerCompat.AccessibilityStateChangeListener {
   }

   private static class AccessibilityStateChangeListenerWrapper implements android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener {
      AccessibilityManagerCompat.AccessibilityStateChangeListener mListener;

      AccessibilityStateChangeListenerWrapper(AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
         this.mListener = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            AccessibilityManagerCompat.AccessibilityStateChangeListenerWrapper var2 = (AccessibilityManagerCompat.AccessibilityStateChangeListenerWrapper)var1;
            return this.mListener.equals(var2.mListener);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mListener.hashCode();
      }

      public void onAccessibilityStateChanged(boolean var1) {
         this.mListener.onAccessibilityStateChanged(var1);
      }
   }

   public interface TouchExplorationStateChangeListener {
      void onTouchExplorationStateChanged(boolean var1);
   }

   private static class TouchExplorationStateChangeListenerWrapper implements android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener {
      final AccessibilityManagerCompat.TouchExplorationStateChangeListener mListener;

      TouchExplorationStateChangeListenerWrapper(AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
         this.mListener = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper var2 = (AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper)var1;
            return this.mListener.equals(var2.mListener);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mListener.hashCode();
      }

      public void onTouchExplorationStateChanged(boolean var1) {
         this.mListener.onTouchExplorationStateChanged(var1);
      }
   }
}
