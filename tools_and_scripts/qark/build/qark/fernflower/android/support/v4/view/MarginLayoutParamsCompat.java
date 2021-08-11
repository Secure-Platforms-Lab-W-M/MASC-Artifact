package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public final class MarginLayoutParamsCompat {
   private MarginLayoutParamsCompat() {
   }

   public static int getLayoutDirection(MarginLayoutParams var0) {
      int var1;
      if (VERSION.SDK_INT >= 17) {
         var1 = var0.getLayoutDirection();
      } else {
         var1 = 0;
      }

      int var2 = var1;
      if (var1 != 0) {
         var2 = var1;
         if (var1 != 1) {
            var2 = 0;
         }
      }

      return var2;
   }

   public static int getMarginEnd(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginEnd() : var0.rightMargin;
   }

   public static int getMarginStart(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginStart() : var0.leftMargin;
   }

   public static boolean isMarginRelative(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.isMarginRelative() : false;
   }

   public static void resolveLayoutDirection(MarginLayoutParams var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.resolveLayoutDirection(var1);
      }

   }

   public static void setLayoutDirection(MarginLayoutParams var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.setLayoutDirection(var1);
      }

   }

   public static void setMarginEnd(MarginLayoutParams var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.setMarginEnd(var1);
      } else {
         var0.rightMargin = var1;
      }
   }

   public static void setMarginStart(MarginLayoutParams var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.setMarginStart(var1);
      } else {
         var0.leftMargin = var1;
      }
   }
}
