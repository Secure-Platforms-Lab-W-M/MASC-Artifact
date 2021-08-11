package android.support.v4.view;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.WindowInsets;

@TargetApi(21)
@RequiresApi(21)
class WindowInsetsCompatApi21 {
   public static Object consumeStableInsets(Object var0) {
      return ((WindowInsets)var0).consumeStableInsets();
   }

   public static int getStableInsetBottom(Object var0) {
      return ((WindowInsets)var0).getStableInsetBottom();
   }

   public static int getStableInsetLeft(Object var0) {
      return ((WindowInsets)var0).getStableInsetLeft();
   }

   public static int getStableInsetRight(Object var0) {
      return ((WindowInsets)var0).getStableInsetRight();
   }

   public static int getStableInsetTop(Object var0) {
      return ((WindowInsets)var0).getStableInsetTop();
   }

   public static boolean hasStableInsets(Object var0) {
      return ((WindowInsets)var0).hasStableInsets();
   }

   public static boolean isConsumed(Object var0) {
      return ((WindowInsets)var0).isConsumed();
   }

   public static Object replaceSystemWindowInsets(Object var0, Rect var1) {
      return ((WindowInsets)var0).replaceSystemWindowInsets(var1);
   }
}
