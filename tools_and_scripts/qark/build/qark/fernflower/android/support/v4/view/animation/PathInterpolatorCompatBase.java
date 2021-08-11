package android.support.v4.view.animation;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;

@TargetApi(9)
@RequiresApi(9)
class PathInterpolatorCompatBase {
   private PathInterpolatorCompatBase() {
   }

   public static Interpolator create(float var0, float var1) {
      return new PathInterpolatorGingerbread(var0, var1);
   }

   public static Interpolator create(float var0, float var1, float var2, float var3) {
      return new PathInterpolatorGingerbread(var0, var1, var2, var3);
   }

   public static Interpolator create(Path var0) {
      return new PathInterpolatorGingerbread(var0);
   }
}
