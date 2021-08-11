package androidx.transition;

import android.animation.PropertyValuesHolder;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.util.Property;

class PropertyValuesHolderUtils {
   private PropertyValuesHolderUtils() {
   }

   static PropertyValuesHolder ofPointF(Property var0, Path var1) {
      return VERSION.SDK_INT >= 21 ? PropertyValuesHolder.ofObject(var0, (TypeConverter)null, var1) : PropertyValuesHolder.ofFloat(new PathProperty(var0, var1), new float[]{0.0F, 1.0F});
   }
}
