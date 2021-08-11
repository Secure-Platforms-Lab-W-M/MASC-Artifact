package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.util.Property;

class ObjectAnimatorUtils {
   private static final ObjectAnimatorUtilsImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ObjectAnimatorUtilsApi21();
      } else {
         IMPL = new ObjectAnimatorUtilsApi14();
      }
   }

   static ObjectAnimator ofPointF(Object var0, Property var1, Path var2) {
      return IMPL.ofPointF(var0, var1, var2);
   }
}
