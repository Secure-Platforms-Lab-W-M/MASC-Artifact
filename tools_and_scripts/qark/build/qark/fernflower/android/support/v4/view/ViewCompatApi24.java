package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.PointerIcon;
import android.view.View;

@TargetApi(24)
@RequiresApi(24)
class ViewCompatApi24 {
   public static void setPointerIcon(View var0, Object var1) {
      var0.setPointerIcon((PointerIcon)var1);
   }
}
