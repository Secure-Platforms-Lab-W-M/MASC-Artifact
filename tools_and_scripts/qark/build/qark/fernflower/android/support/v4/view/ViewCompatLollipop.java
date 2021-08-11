package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;

@TargetApi(21)
@RequiresApi(21)
class ViewCompatLollipop {
   private static ThreadLocal sThreadLocalRect;

   public static Object dispatchApplyWindowInsets(View var0, Object var1) {
      WindowInsets var2 = (WindowInsets)var1;
      WindowInsets var3 = var0.dispatchApplyWindowInsets(var2);
      if (var3 != var2) {
         var1 = new WindowInsets(var3);
      }

      return var1;
   }

   public static boolean dispatchNestedFling(View var0, float var1, float var2, boolean var3) {
      return var0.dispatchNestedFling(var1, var2, var3);
   }

   public static boolean dispatchNestedPreFling(View var0, float var1, float var2) {
      return var0.dispatchNestedPreFling(var1, var2);
   }

   public static boolean dispatchNestedPreScroll(View var0, int var1, int var2, int[] var3, int[] var4) {
      return var0.dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public static boolean dispatchNestedScroll(View var0, int var1, int var2, int var3, int var4, int[] var5) {
      return var0.dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   static ColorStateList getBackgroundTintList(View var0) {
      return var0.getBackgroundTintList();
   }

   static Mode getBackgroundTintMode(View var0) {
      return var0.getBackgroundTintMode();
   }

   public static float getElevation(View var0) {
      return var0.getElevation();
   }

   private static Rect getEmptyTempRect() {
      if (sThreadLocalRect == null) {
         sThreadLocalRect = new ThreadLocal();
      }

      Rect var1 = (Rect)sThreadLocalRect.get();
      Rect var0 = var1;
      if (var1 == null) {
         var0 = new Rect();
         sThreadLocalRect.set(var0);
      }

      var0.setEmpty();
      return var0;
   }

   public static String getTransitionName(View var0) {
      return var0.getTransitionName();
   }

   public static float getTranslationZ(View var0) {
      return var0.getTranslationZ();
   }

   public static float getZ(View var0) {
      return var0.getZ();
   }

   public static boolean hasNestedScrollingParent(View var0) {
      return var0.hasNestedScrollingParent();
   }

   public static boolean isImportantForAccessibility(View var0) {
      return var0.isImportantForAccessibility();
   }

   public static boolean isNestedScrollingEnabled(View var0) {
      return var0.isNestedScrollingEnabled();
   }

   static void offsetLeftAndRight(View var0, int var1) {
      Rect var3 = getEmptyTempRect();
      boolean var2 = false;
      ViewParent var4 = var0.getParent();
      if (var4 instanceof View) {
         View var5 = (View)var4;
         var3.set(var5.getLeft(), var5.getTop(), var5.getRight(), var5.getBottom());
         if (!var3.intersects(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      ViewCompatHC.offsetLeftAndRight(var0, var1);
      if (var2 && var3.intersect(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
         ((View)var4).invalidate(var3);
      }

   }

   static void offsetTopAndBottom(View var0, int var1) {
      Rect var3 = getEmptyTempRect();
      boolean var2 = false;
      ViewParent var4 = var0.getParent();
      if (var4 instanceof View) {
         View var5 = (View)var4;
         var3.set(var5.getLeft(), var5.getTop(), var5.getRight(), var5.getBottom());
         if (!var3.intersects(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      ViewCompatHC.offsetTopAndBottom(var0, var1);
      if (var2 && var3.intersect(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
         ((View)var4).invalidate(var3);
      }

   }

   public static Object onApplyWindowInsets(View var0, Object var1) {
      WindowInsets var2 = (WindowInsets)var1;
      WindowInsets var3 = var0.onApplyWindowInsets(var2);
      if (var3 != var2) {
         var1 = new WindowInsets(var3);
      }

      return var1;
   }

   public static void requestApplyInsets(View var0) {
      var0.requestApplyInsets();
   }

   static void setBackgroundTintList(View var0, ColorStateList var1) {
      var0.setBackgroundTintList(var1);
      if (VERSION.SDK_INT == 21) {
         Drawable var3 = var0.getBackground();
         boolean var2;
         if (var0.getBackgroundTintList() != null && var0.getBackgroundTintMode() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var3 != null && var2) {
            if (var3.isStateful()) {
               var3.setState(var0.getDrawableState());
            }

            var0.setBackground(var3);
         }
      }

   }

   static void setBackgroundTintMode(View var0, Mode var1) {
      var0.setBackgroundTintMode(var1);
      if (VERSION.SDK_INT == 21) {
         Drawable var3 = var0.getBackground();
         boolean var2;
         if (var0.getBackgroundTintList() != null && var0.getBackgroundTintMode() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var3 != null && var2) {
            if (var3.isStateful()) {
               var3.setState(var0.getDrawableState());
            }

            var0.setBackground(var3);
         }
      }

   }

   public static void setElevation(View var0, float var1) {
      var0.setElevation(var1);
   }

   public static void setNestedScrollingEnabled(View var0, boolean var1) {
      var0.setNestedScrollingEnabled(var1);
   }

   public static void setOnApplyWindowInsetsListener(View var0, final ViewCompatLollipop.OnApplyWindowInsetsListenerBridge var1) {
      if (var1 == null) {
         var0.setOnApplyWindowInsetsListener((android.view.View.OnApplyWindowInsetsListener)null);
      } else {
         var0.setOnApplyWindowInsetsListener(new android.view.View.OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(View var1x, WindowInsets var2) {
               return (WindowInsets)var1.onApplyWindowInsets(var1x, var2);
            }
         });
      }
   }

   public static void setTransitionName(View var0, String var1) {
      var0.setTransitionName(var1);
   }

   public static void setTranslationZ(View var0, float var1) {
      var0.setTranslationZ(var1);
   }

   public static void setZ(View var0, float var1) {
      var0.setZ(var1);
   }

   public static boolean startNestedScroll(View var0, int var1) {
      return var0.startNestedScroll(var1);
   }

   public static void stopNestedScroll(View var0) {
      var0.stopNestedScroll();
   }

   public interface OnApplyWindowInsetsListenerBridge {
      Object onApplyWindowInsets(View var1, Object var2);
   }
}
