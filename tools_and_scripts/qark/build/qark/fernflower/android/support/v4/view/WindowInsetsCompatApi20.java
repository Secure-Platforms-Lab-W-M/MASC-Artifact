package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.WindowInsets;

@TargetApi(20)
@RequiresApi(20)
class WindowInsetsCompatApi20 {
   public static Object consumeSystemWindowInsets(Object var0) {
      return ((WindowInsets)var0).consumeSystemWindowInsets();
   }

   public static Object getSourceWindowInsets(Object var0) {
      return new WindowInsets((WindowInsets)var0);
   }

   public static int getSystemWindowInsetBottom(Object var0) {
      return ((WindowInsets)var0).getSystemWindowInsetBottom();
   }

   public static int getSystemWindowInsetLeft(Object var0) {
      return ((WindowInsets)var0).getSystemWindowInsetLeft();
   }

   public static int getSystemWindowInsetRight(Object var0) {
      return ((WindowInsets)var0).getSystemWindowInsetRight();
   }

   public static int getSystemWindowInsetTop(Object var0) {
      return ((WindowInsets)var0).getSystemWindowInsetTop();
   }

   public static boolean hasInsets(Object var0) {
      return ((WindowInsets)var0).hasInsets();
   }

   public static boolean hasSystemWindowInsets(Object var0) {
      return ((WindowInsets)var0).hasSystemWindowInsets();
   }

   public static boolean isRound(Object var0) {
      return ((WindowInsets)var0).isRound();
   }

   public static Object replaceSystemWindowInsets(Object var0, int var1, int var2, int var3, int var4) {
      return ((WindowInsets)var0).replaceSystemWindowInsets(var1, var2, var3, var4);
   }
}
