package android.support.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.util.Property;

@RequiresApi(14)
class PropertyValuesHolderUtilsApi14 implements PropertyValuesHolderUtilsImpl {
   public PropertyValuesHolder ofPointF(Property var1, Path var2) {
      return PropertyValuesHolder.ofFloat(new PathProperty(var1, var2), new float[]{0.0F, 1.0F});
   }
}
