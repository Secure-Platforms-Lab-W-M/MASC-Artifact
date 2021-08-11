package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public final class ViewParentCompat {
   static final ViewParentCompat.ViewParentCompatBaseImpl IMPL;
   private static final String TAG = "ViewParentCompat";

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ViewParentCompat.ViewParentCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new ViewParentCompat.ViewParentCompatApi19Impl();
      } else {
         IMPL = new ViewParentCompat.ViewParentCompatBaseImpl();
      }
   }

   private ViewParentCompat() {
   }

   public static void notifySubtreeAccessibilityStateChanged(ViewParent var0, View var1, View var2, int var3) {
      IMPL.notifySubtreeAccessibilityStateChanged(var0, var1, var2, var3);
   }

   public static boolean onNestedFling(ViewParent var0, View var1, float var2, float var3, boolean var4) {
      return IMPL.onNestedFling(var0, var1, var2, var3, var4);
   }

   public static boolean onNestedPreFling(ViewParent var0, View var1, float var2, float var3) {
      return IMPL.onNestedPreFling(var0, var1, var2, var3);
   }

   public static void onNestedPreScroll(ViewParent var0, View var1, int var2, int var3, int[] var4) {
      onNestedPreScroll(var0, var1, var2, var3, var4, 0);
   }

   public static void onNestedPreScroll(ViewParent var0, View var1, int var2, int var3, int[] var4, int var5) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedPreScroll(var1, var2, var3, var4, var5);
      } else {
         if (var5 == 0) {
            IMPL.onNestedPreScroll(var0, var1, var2, var3, var4);
         }

      }
   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5) {
      onNestedScroll(var0, var1, var2, var3, var4, var5, 0);
   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5, int var6) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedScroll(var1, var2, var3, var4, var5, var6);
      } else {
         if (var6 == 0) {
            IMPL.onNestedScroll(var0, var1, var2, var3, var4, var5);
         }

      }
   }

   public static void onNestedScrollAccepted(ViewParent var0, View var1, View var2, int var3) {
      onNestedScrollAccepted(var0, var1, var2, var3, 0);
   }

   public static void onNestedScrollAccepted(ViewParent var0, View var1, View var2, int var3, int var4) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedScrollAccepted(var1, var2, var3, var4);
      } else {
         if (var4 == 0) {
            IMPL.onNestedScrollAccepted(var0, var1, var2, var3);
         }

      }
   }

   public static boolean onStartNestedScroll(ViewParent var0, View var1, View var2, int var3) {
      return onStartNestedScroll(var0, var1, var2, var3, 0);
   }

   public static boolean onStartNestedScroll(ViewParent var0, View var1, View var2, int var3, int var4) {
      if (var0 instanceof NestedScrollingParent2) {
         return ((NestedScrollingParent2)var0).onStartNestedScroll(var1, var2, var3, var4);
      } else {
         return var4 == 0 ? IMPL.onStartNestedScroll(var0, var1, var2, var3) : false;
      }
   }

   public static void onStopNestedScroll(ViewParent var0, View var1) {
      onStopNestedScroll(var0, var1, 0);
   }

   public static void onStopNestedScroll(ViewParent var0, View var1, int var2) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onStopNestedScroll(var1, var2);
      } else {
         if (var2 == 0) {
            IMPL.onStopNestedScroll(var0, var1);
         }

      }
   }

   @Deprecated
   public static boolean requestSendAccessibilityEvent(ViewParent var0, View var1, AccessibilityEvent var2) {
      return var0.requestSendAccessibilityEvent(var1, var2);
   }

   @RequiresApi(19)
   static class ViewParentCompatApi19Impl extends ViewParentCompat.ViewParentCompatBaseImpl {
      public void notifySubtreeAccessibilityStateChanged(ViewParent var1, View var2, View var3, int var4) {
         var1.notifySubtreeAccessibilityStateChanged(var2, var3, var4);
      }
   }

   @RequiresApi(21)
   static class ViewParentCompatApi21Impl extends ViewParentCompat.ViewParentCompatApi19Impl {
      public boolean onNestedFling(ViewParent var1, View var2, float var3, float var4, boolean var5) {
         try {
            var5 = var1.onNestedFling(var2, var3, var4, var5);
            return var5;
         } catch (AbstractMethodError var7) {
            StringBuilder var6 = new StringBuilder();
            var6.append("ViewParent ");
            var6.append(var1);
            var6.append(" does not implement interface ");
            var6.append("method onNestedFling");
            Log.e("ViewParentCompat", var6.toString(), var7);
            return false;
         }
      }

      public boolean onNestedPreFling(ViewParent var1, View var2, float var3, float var4) {
         try {
            boolean var5 = var1.onNestedPreFling(var2, var3, var4);
            return var5;
         } catch (AbstractMethodError var7) {
            StringBuilder var6 = new StringBuilder();
            var6.append("ViewParent ");
            var6.append(var1);
            var6.append(" does not implement interface ");
            var6.append("method onNestedPreFling");
            Log.e("ViewParentCompat", var6.toString(), var7);
            return false;
         }
      }

      public void onNestedPreScroll(ViewParent var1, View var2, int var3, int var4, int[] var5) {
         try {
            var1.onNestedPreScroll(var2, var3, var4, var5);
         } catch (AbstractMethodError var6) {
            StringBuilder var7 = new StringBuilder();
            var7.append("ViewParent ");
            var7.append(var1);
            var7.append(" does not implement interface ");
            var7.append("method onNestedPreScroll");
            Log.e("ViewParentCompat", var7.toString(), var6);
         }
      }

      public void onNestedScroll(ViewParent var1, View var2, int var3, int var4, int var5, int var6) {
         try {
            var1.onNestedScroll(var2, var3, var4, var5, var6);
         } catch (AbstractMethodError var8) {
            StringBuilder var7 = new StringBuilder();
            var7.append("ViewParent ");
            var7.append(var1);
            var7.append(" does not implement interface ");
            var7.append("method onNestedScroll");
            Log.e("ViewParentCompat", var7.toString(), var8);
         }
      }

      public void onNestedScrollAccepted(ViewParent var1, View var2, View var3, int var4) {
         try {
            var1.onNestedScrollAccepted(var2, var3, var4);
         } catch (AbstractMethodError var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("ViewParent ");
            var6.append(var1);
            var6.append(" does not implement interface ");
            var6.append("method onNestedScrollAccepted");
            Log.e("ViewParentCompat", var6.toString(), var5);
         }
      }

      public boolean onStartNestedScroll(ViewParent var1, View var2, View var3, int var4) {
         try {
            boolean var5 = var1.onStartNestedScroll(var2, var3, var4);
            return var5;
         } catch (AbstractMethodError var6) {
            StringBuilder var7 = new StringBuilder();
            var7.append("ViewParent ");
            var7.append(var1);
            var7.append(" does not implement interface ");
            var7.append("method onStartNestedScroll");
            Log.e("ViewParentCompat", var7.toString(), var6);
            return false;
         }
      }

      public void onStopNestedScroll(ViewParent var1, View var2) {
         try {
            var1.onStopNestedScroll(var2);
         } catch (AbstractMethodError var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("ViewParent ");
            var3.append(var1);
            var3.append(" does not implement interface ");
            var3.append("method onStopNestedScroll");
            Log.e("ViewParentCompat", var3.toString(), var4);
         }
      }
   }

   static class ViewParentCompatBaseImpl {
      public void notifySubtreeAccessibilityStateChanged(ViewParent var1, View var2, View var3, int var4) {
      }

      public boolean onNestedFling(ViewParent var1, View var2, float var3, float var4, boolean var5) {
         return var1 instanceof NestedScrollingParent ? ((NestedScrollingParent)var1).onNestedFling(var2, var3, var4, var5) : false;
      }

      public boolean onNestedPreFling(ViewParent var1, View var2, float var3, float var4) {
         return var1 instanceof NestedScrollingParent ? ((NestedScrollingParent)var1).onNestedPreFling(var2, var3, var4) : false;
      }

      public void onNestedPreScroll(ViewParent var1, View var2, int var3, int var4, int[] var5) {
         if (var1 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var1).onNestedPreScroll(var2, var3, var4, var5);
         }

      }

      public void onNestedScroll(ViewParent var1, View var2, int var3, int var4, int var5, int var6) {
         if (var1 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var1).onNestedScroll(var2, var3, var4, var5, var6);
         }

      }

      public void onNestedScrollAccepted(ViewParent var1, View var2, View var3, int var4) {
         if (var1 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var1).onNestedScrollAccepted(var2, var3, var4);
         }

      }

      public boolean onStartNestedScroll(ViewParent var1, View var2, View var3, int var4) {
         return var1 instanceof NestedScrollingParent ? ((NestedScrollingParent)var1).onStartNestedScroll(var2, var3, var4) : false;
      }

      public void onStopNestedScroll(ViewParent var1, View var2) {
         if (var1 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var1).onStopNestedScroll(var2);
         }

      }
   }
}
