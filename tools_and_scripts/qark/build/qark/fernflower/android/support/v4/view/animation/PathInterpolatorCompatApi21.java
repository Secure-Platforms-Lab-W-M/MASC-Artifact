package android.support.v4.view.animation;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

@TargetApi(21)
@RequiresApi(21)
class PathInterpolatorCompatApi21 {
   private PathInterpolatorCompatApi21() {
   }

   public static Interpolator create(float var0, float var1) {
      return new PathInterpolator(var0, var1);
   }

   public static Interpolator create(float var0, float var1, float var2, float var3) {
      return new PathInterpolator(var0, var1, var2, var3);
   }

   public static Interpolator create(Path var0) {
      return new PathInterpolator(var0);
   }
}
