package androidx.core.view;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public final class ViewParentCompat {
   private static final String TAG = "ViewParentCompat";
   private static int[] sTempNestedScrollConsumed;

   private ViewParentCompat() {
   }

   private static int[] getTempNestedScrollConsumed() {
      int[] var0 = sTempNestedScrollConsumed;
      if (var0 == null) {
         sTempNestedScrollConsumed = new int[2];
      } else {
         var0[0] = 0;
         var0[1] = 0;
      }

      return sTempNestedScrollConsumed;
   }

   public static void notifySubtreeAccessibilityStateChanged(ViewParent var0, View var1, View var2, int var3) {
      if (VERSION.SDK_INT >= 19) {
         var0.notifySubtreeAccessibilityStateChanged(var1, var2, var3);
      }

   }

   public static boolean onNestedFling(ViewParent var0, View var1, float var2, float var3, boolean var4) {
      if (VERSION.SDK_INT >= 21) {
         try {
            var4 = var0.onNestedFling(var1, var2, var3, var4);
            return var4;
         } catch (AbstractMethodError var6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("ViewParent ");
            var5.append(var0);
            var5.append(" does not implement interface method onNestedFling");
            Log.e("ViewParentCompat", var5.toString(), var6);
         }
      } else if (var0 instanceof NestedScrollingParent) {
         return ((NestedScrollingParent)var0).onNestedFling(var1, var2, var3, var4);
      }

      return false;
   }

   public static boolean onNestedPreFling(ViewParent var0, View var1, float var2, float var3) {
      if (VERSION.SDK_INT >= 21) {
         try {
            boolean var4 = var0.onNestedPreFling(var1, var2, var3);
            return var4;
         } catch (AbstractMethodError var6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("ViewParent ");
            var5.append(var0);
            var5.append(" does not implement interface method onNestedPreFling");
            Log.e("ViewParentCompat", var5.toString(), var6);
         }
      } else if (var0 instanceof NestedScrollingParent) {
         return ((NestedScrollingParent)var0).onNestedPreFling(var1, var2, var3);
      }

      return false;
   }

   public static void onNestedPreScroll(ViewParent var0, View var1, int var2, int var3, int[] var4) {
      onNestedPreScroll(var0, var1, var2, var3, var4, 0);
   }

   public static void onNestedPreScroll(ViewParent var0, View var1, int var2, int var3, int[] var4, int var5) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedPreScroll(var1, var2, var3, var4, var5);
      } else {
         if (var5 == 0) {
            if (VERSION.SDK_INT >= 21) {
               try {
                  var0.onNestedPreScroll(var1, var2, var3, var4);
                  return;
               } catch (AbstractMethodError var6) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("ViewParent ");
                  var7.append(var0);
                  var7.append(" does not implement interface method onNestedPreScroll");
                  Log.e("ViewParentCompat", var7.toString(), var6);
                  return;
               }
            }

            if (var0 instanceof NestedScrollingParent) {
               ((NestedScrollingParent)var0).onNestedPreScroll(var1, var2, var3, var4);
            }
         }

      }
   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5) {
      onNestedScroll(var0, var1, var2, var3, var4, var5, 0, getTempNestedScrollConsumed());
   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5, int var6) {
      onNestedScroll(var0, var1, var2, var3, var4, var5, var6, getTempNestedScrollConsumed());
   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      if (var0 instanceof NestedScrollingParent3) {
         ((NestedScrollingParent3)var0).onNestedScroll(var1, var2, var3, var4, var5, var6, var7);
      } else {
         var7[0] += var4;
         var7[1] += var5;
         if (var0 instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)var0).onNestedScroll(var1, var2, var3, var4, var5, var6);
         } else {
            if (var6 == 0) {
               if (VERSION.SDK_INT >= 21) {
                  try {
                     var0.onNestedScroll(var1, var2, var3, var4, var5);
                     return;
                  } catch (AbstractMethodError var8) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("ViewParent ");
                     var9.append(var0);
                     var9.append(" does not implement interface method onNestedScroll");
                     Log.e("ViewParentCompat", var9.toString(), var8);
                     return;
                  }
               }

               if (var0 instanceof NestedScrollingParent) {
                  ((NestedScrollingParent)var0).onNestedScroll(var1, var2, var3, var4, var5);
               }
            }

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
            if (VERSION.SDK_INT >= 21) {
               try {
                  var0.onNestedScrollAccepted(var1, var2, var3);
                  return;
               } catch (AbstractMethodError var5) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("ViewParent ");
                  var6.append(var0);
                  var6.append(" does not implement interface method onNestedScrollAccepted");
                  Log.e("ViewParentCompat", var6.toString(), var5);
                  return;
               }
            }

            if (var0 instanceof NestedScrollingParent) {
               ((NestedScrollingParent)var0).onNestedScrollAccepted(var1, var2, var3);
            }
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
         if (var4 == 0) {
            if (VERSION.SDK_INT >= 21) {
               try {
                  boolean var5 = var0.onStartNestedScroll(var1, var2, var3);
                  return var5;
               } catch (AbstractMethodError var6) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("ViewParent ");
                  var7.append(var0);
                  var7.append(" does not implement interface method onStartNestedScroll");
                  Log.e("ViewParentCompat", var7.toString(), var6);
               }
            } else if (var0 instanceof NestedScrollingParent) {
               return ((NestedScrollingParent)var0).onStartNestedScroll(var1, var2, var3);
            }
         }

         return false;
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
            if (VERSION.SDK_INT >= 21) {
               try {
                  var0.onStopNestedScroll(var1);
                  return;
               } catch (AbstractMethodError var4) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("ViewParent ");
                  var3.append(var0);
                  var3.append(" does not implement interface method onStopNestedScroll");
                  Log.e("ViewParentCompat", var3.toString(), var4);
                  return;
               }
            }

            if (var0 instanceof NestedScrollingParent) {
               ((NestedScrollingParent)var0).onStopNestedScroll(var1);
            }
         }

      }
   }

   @Deprecated
   public static boolean requestSendAccessibilityEvent(ViewParent var0, View var1, AccessibilityEvent var2) {
      return var0.requestSendAccessibilityEvent(var1, var2);
   }
}
