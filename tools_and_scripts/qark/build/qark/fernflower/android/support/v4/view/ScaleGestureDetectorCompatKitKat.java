package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ScaleGestureDetector;

@TargetApi(19)
@RequiresApi(19)
class ScaleGestureDetectorCompatKitKat {
   private ScaleGestureDetectorCompatKitKat() {
   }

   public static boolean isQuickScaleEnabled(Object var0) {
      return ((ScaleGestureDetector)var0).isQuickScaleEnabled();
   }

   public static void setQuickScaleEnabled(Object var0, boolean var1) {
      ((ScaleGestureDetector)var0).setQuickScaleEnabled(var1);
   }
}
