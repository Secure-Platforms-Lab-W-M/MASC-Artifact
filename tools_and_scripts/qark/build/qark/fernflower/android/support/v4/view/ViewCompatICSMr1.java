package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(15)
@RequiresApi(15)
class ViewCompatICSMr1 {
   public static boolean hasOnClickListeners(View var0) {
      return var0.hasOnClickListeners();
   }
}
