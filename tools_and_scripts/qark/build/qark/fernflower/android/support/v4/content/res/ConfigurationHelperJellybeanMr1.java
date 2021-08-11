package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@TargetApi(17)
@RequiresApi(17)
class ConfigurationHelperJellybeanMr1 {
   static int getDensityDpi(@NonNull Resources var0) {
      return var0.getConfiguration().densityDpi;
   }
}
