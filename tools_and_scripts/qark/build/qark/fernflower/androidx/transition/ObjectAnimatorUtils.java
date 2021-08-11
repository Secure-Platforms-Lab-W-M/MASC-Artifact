package androidx.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.util.Property;

class ObjectAnimatorUtils {
   private ObjectAnimatorUtils() {
   }

   static ObjectAnimator ofPointF(Object var0, Property var1, Path var2) {
      return VERSION.SDK_INT >= 21 ? ObjectAnimator.ofObject(var0, var1, (TypeConverter)null, var2) : ObjectAnimator.ofFloat(var0, new PathProperty(var1, var2), new float[]{0.0F, 1.0F});
   }
}
