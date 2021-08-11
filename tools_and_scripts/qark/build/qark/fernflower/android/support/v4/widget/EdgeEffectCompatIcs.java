package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.widget.EdgeEffect;

@TargetApi(14)
@RequiresApi(14)
class EdgeEffectCompatIcs {
   public static boolean draw(Object var0, Canvas var1) {
      return ((EdgeEffect)var0).draw(var1);
   }

   public static void finish(Object var0) {
      ((EdgeEffect)var0).finish();
   }

   public static boolean isFinished(Object var0) {
      return ((EdgeEffect)var0).isFinished();
   }

   public static Object newEdgeEffect(Context var0) {
      return new EdgeEffect(var0);
   }

   public static boolean onAbsorb(Object var0, int var1) {
      ((EdgeEffect)var0).onAbsorb(var1);
      return true;
   }

   public static boolean onPull(Object var0, float var1) {
      ((EdgeEffect)var0).onPull(var1);
      return true;
   }

   public static boolean onRelease(Object var0) {
      EdgeEffect var1 = (EdgeEffect)var0;
      var1.onRelease();
      return var1.isFinished();
   }

   public static void setSize(Object var0, int var1, int var2) {
      ((EdgeEffect)var0).setSize(var1, var2);
   }
}
