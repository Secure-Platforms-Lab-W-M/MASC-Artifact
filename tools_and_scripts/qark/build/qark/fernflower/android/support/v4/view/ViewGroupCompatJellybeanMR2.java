package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(18)
@RequiresApi(18)
class ViewGroupCompatJellybeanMR2 {
   public static int getLayoutMode(ViewGroup var0) {
      return var0.getLayoutMode();
   }

   public static void setLayoutMode(ViewGroup var0, int var1) {
      var0.setLayoutMode(var1);
   }
}
