package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class BitmapCompatJellybeanMR2 {
   public static boolean hasMipMap(Bitmap var0) {
      return var0.hasMipMap();
   }

   public static void setHasMipMap(Bitmap var0, boolean var1) {
      var0.setHasMipMap(var1);
   }
}
