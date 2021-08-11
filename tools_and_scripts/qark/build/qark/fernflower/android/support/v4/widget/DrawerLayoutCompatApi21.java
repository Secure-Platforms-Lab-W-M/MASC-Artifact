package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowInsets;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup.MarginLayoutParams;

@TargetApi(21)
@RequiresApi(21)
class DrawerLayoutCompatApi21 {
   private static final int[] THEME_ATTRS = new int[]{16843828};

   public static void applyMarginInsets(MarginLayoutParams var0, Object var1, int var2) {
      WindowInsets var3 = (WindowInsets)var1;
      WindowInsets var4;
      if (var2 == 3) {
         var4 = var3.replaceSystemWindowInsets(var3.getSystemWindowInsetLeft(), var3.getSystemWindowInsetTop(), 0, var3.getSystemWindowInsetBottom());
      } else {
         var4 = var3;
         if (var2 == 5) {
            var4 = var3.replaceSystemWindowInsets(0, var3.getSystemWindowInsetTop(), var3.getSystemWindowInsetRight(), var3.getSystemWindowInsetBottom());
         }
      }

      var0.leftMargin = var4.getSystemWindowInsetLeft();
      var0.topMargin = var4.getSystemWindowInsetTop();
      var0.rightMargin = var4.getSystemWindowInsetRight();
      var0.bottomMargin = var4.getSystemWindowInsetBottom();
   }

   public static void configureApplyInsets(View var0) {
      if (var0 instanceof DrawerLayoutImpl) {
         var0.setOnApplyWindowInsetsListener(new DrawerLayoutCompatApi21.InsetsListener());
         var0.setSystemUiVisibility(1280);
      }

   }

   public static void dispatchChildInsets(View var0, Object var1, int var2) {
      WindowInsets var3 = (WindowInsets)var1;
      WindowInsets var4;
      if (var2 == 3) {
         var4 = var3.replaceSystemWindowInsets(var3.getSystemWindowInsetLeft(), var3.getSystemWindowInsetTop(), 0, var3.getSystemWindowInsetBottom());
      } else {
         var4 = var3;
         if (var2 == 5) {
            var4 = var3.replaceSystemWindowInsets(0, var3.getSystemWindowInsetTop(), var3.getSystemWindowInsetRight(), var3.getSystemWindowInsetBottom());
         }
      }

      var0.dispatchApplyWindowInsets(var4);
   }

   public static Drawable getDefaultStatusBarBackground(Context var0) {
      TypedArray var4 = var0.obtainStyledAttributes(THEME_ATTRS);

      Drawable var1;
      try {
         var1 = var4.getDrawable(0);
      } finally {
         var4.recycle();
      }

      return var1;
   }

   public static int getTopInset(Object var0) {
      return var0 != null ? ((WindowInsets)var0).getSystemWindowInsetTop() : 0;
   }

   static class InsetsListener implements OnApplyWindowInsetsListener {
      public WindowInsets onApplyWindowInsets(View var1, WindowInsets var2) {
         DrawerLayoutImpl var4 = (DrawerLayoutImpl)var1;
         boolean var3;
         if (var2.getSystemWindowInsetTop() > 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.setChildInsets(var2, var3);
         return var2.consumeSystemWindowInsets();
      }
   }
}
