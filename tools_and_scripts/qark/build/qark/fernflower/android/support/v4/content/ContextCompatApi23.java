package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class ContextCompatApi23 {
   public static int getColor(Context var0, int var1) {
      return var0.getColor(var1);
   }

   public static ColorStateList getColorStateList(Context var0, int var1) {
      return var0.getColorStateList(var1);
   }
}
