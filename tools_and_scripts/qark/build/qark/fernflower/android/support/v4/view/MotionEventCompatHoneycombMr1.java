package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

@TargetApi(12)
@RequiresApi(12)
class MotionEventCompatHoneycombMr1 {
   static float getAxisValue(MotionEvent var0, int var1) {
      return var0.getAxisValue(var1);
   }

   static float getAxisValue(MotionEvent var0, int var1, int var2) {
      return var0.getAxisValue(var1, var2);
   }
}
