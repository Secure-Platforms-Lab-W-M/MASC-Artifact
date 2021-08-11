package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(15)
@RequiresApi(15)
class ResourcesCompatIcsMr1 {
   public static Drawable getDrawableForDensity(Resources var0, int var1, int var2) throws NotFoundException {
      return var0.getDrawableForDensity(var1, var2);
   }
}
