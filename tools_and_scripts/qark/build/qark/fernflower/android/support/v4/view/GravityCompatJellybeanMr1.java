package android.support.v4.view;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.Gravity;

@TargetApi(17)
@RequiresApi(17)
class GravityCompatJellybeanMr1 {
   public static void apply(int var0, int var1, int var2, Rect var3, int var4, int var5, Rect var6, int var7) {
      Gravity.apply(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static void apply(int var0, int var1, int var2, Rect var3, Rect var4, int var5) {
      Gravity.apply(var0, var1, var2, var3, var4, var5);
   }

   public static void applyDisplay(int var0, Rect var1, Rect var2, int var3) {
      Gravity.applyDisplay(var0, var1, var2, var3);
   }

   public static int getAbsoluteGravity(int var0, int var1) {
      return Gravity.getAbsoluteGravity(var0, var1);
   }
}
