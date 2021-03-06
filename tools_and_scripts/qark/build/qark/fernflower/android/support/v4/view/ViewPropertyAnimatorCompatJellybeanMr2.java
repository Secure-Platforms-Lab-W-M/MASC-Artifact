package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Interpolator;

@TargetApi(18)
@RequiresApi(18)
class ViewPropertyAnimatorCompatJellybeanMr2 {
   public static Interpolator getInterpolator(View var0) {
      return (Interpolator)var0.animate().getInterpolator();
   }
}
