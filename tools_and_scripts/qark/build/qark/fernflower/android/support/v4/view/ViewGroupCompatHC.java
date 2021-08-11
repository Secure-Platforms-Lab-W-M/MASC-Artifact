package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(11)
@RequiresApi(11)
class ViewGroupCompatHC {
   private ViewGroupCompatHC() {
   }

   public static void setMotionEventSplittingEnabled(ViewGroup var0, boolean var1) {
      var0.setMotionEventSplittingEnabled(var1);
   }
}
