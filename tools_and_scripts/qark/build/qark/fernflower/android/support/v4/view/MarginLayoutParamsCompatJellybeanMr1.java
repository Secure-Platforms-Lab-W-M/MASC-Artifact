package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup.MarginLayoutParams;

@TargetApi(17)
@RequiresApi(17)
class MarginLayoutParamsCompatJellybeanMr1 {
   public static int getLayoutDirection(MarginLayoutParams var0) {
      return var0.getLayoutDirection();
   }

   public static int getMarginEnd(MarginLayoutParams var0) {
      return var0.getMarginEnd();
   }

   public static int getMarginStart(MarginLayoutParams var0) {
      return var0.getMarginStart();
   }

   public static boolean isMarginRelative(MarginLayoutParams var0) {
      return var0.isMarginRelative();
   }

   public static void resolveLayoutDirection(MarginLayoutParams var0, int var1) {
      var0.resolveLayoutDirection(var1);
   }

   public static void setLayoutDirection(MarginLayoutParams var0, int var1) {
      var0.setLayoutDirection(var1);
   }

   public static void setMarginEnd(MarginLayoutParams var0, int var1) {
      var0.setMarginEnd(var1);
   }

   public static void setMarginStart(MarginLayoutParams var0, int var1) {
      var0.setMarginStart(var1);
   }
}
