package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.util.Property;

@RequiresApi(14)
class ObjectAnimatorUtilsApi14 implements ObjectAnimatorUtilsImpl {
   public ObjectAnimator ofPointF(Object var1, Property var2, Path var3) {
      return ObjectAnimator.ofFloat(var1, new PathProperty(var2, var3), new float[]{0.0F, 1.0F});
   }
}
