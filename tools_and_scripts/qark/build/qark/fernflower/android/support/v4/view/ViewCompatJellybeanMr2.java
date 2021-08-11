package android.support.v4.view;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(18)
@RequiresApi(18)
class ViewCompatJellybeanMr2 {
   public static Rect getClipBounds(View var0) {
      return var0.getClipBounds();
   }

   public static boolean isInLayout(View var0) {
      return var0.isInLayout();
   }

   public static void setClipBounds(View var0, Rect var1) {
      var0.setClipBounds(var1);
   }
}
