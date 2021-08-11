package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class DrawableCompatApi23 {
   public static int getLayoutDirection(Drawable var0) {
      return var0.getLayoutDirection();
   }

   public static boolean setLayoutDirection(Drawable var0, int var1) {
      return var0.setLayoutDirection(var1);
   }
}
