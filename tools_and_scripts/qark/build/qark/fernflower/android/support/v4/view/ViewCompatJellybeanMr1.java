package android.support.v4.view;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;

@TargetApi(17)
@RequiresApi(17)
class ViewCompatJellybeanMr1 {
   public static Display getDisplay(View var0) {
      return var0.getDisplay();
   }

   public static int getLabelFor(View var0) {
      return var0.getLabelFor();
   }

   public static int getLayoutDirection(View var0) {
      return var0.getLayoutDirection();
   }

   public static int getPaddingEnd(View var0) {
      return var0.getPaddingEnd();
   }

   public static int getPaddingStart(View var0) {
      return var0.getPaddingStart();
   }

   public static int getWindowSystemUiVisibility(View var0) {
      return var0.getWindowSystemUiVisibility();
   }

   public static boolean isPaddingRelative(View var0) {
      return var0.isPaddingRelative();
   }

   public static void setLabelFor(View var0, int var1) {
      var0.setLabelFor(var1);
   }

   public static void setLayerPaint(View var0, Paint var1) {
      var0.setLayerPaint(var1);
   }

   public static void setLayoutDirection(View var0, int var1) {
      var0.setLayoutDirection(var1);
   }

   public static void setPaddingRelative(View var0, int var1, int var2, int var3, int var4) {
      var0.setPaddingRelative(var1, var2, var3, var4);
   }
}
