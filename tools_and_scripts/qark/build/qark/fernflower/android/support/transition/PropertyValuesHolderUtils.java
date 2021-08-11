package android.support.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.util.Property;

class PropertyValuesHolderUtils {
   private static final PropertyValuesHolderUtilsImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new PropertyValuesHolderUtilsApi21();
      } else {
         IMPL = new PropertyValuesHolderUtilsApi14();
      }
   }

   static PropertyValuesHolder ofPointF(Property var0, Path var1) {
      return IMPL.ofPointF(var0, var1);
   }
}
