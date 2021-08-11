package android.support.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.util.Property;

@RequiresApi(21)
class ObjectAnimatorUtilsApi21 implements ObjectAnimatorUtilsImpl {
   public ObjectAnimator ofPointF(Object var1, Property var2, Path var3) {
      return ObjectAnimator.ofObject(var1, var2, (TypeConverter)null, var3);
   }
}
