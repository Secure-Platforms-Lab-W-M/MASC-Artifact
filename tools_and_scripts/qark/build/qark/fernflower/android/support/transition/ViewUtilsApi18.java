package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(18)
class ViewUtilsApi18 extends ViewUtilsApi14 {
   public ViewOverlayImpl getOverlay(@NonNull View var1) {
      return new ViewOverlayApi18(var1);
   }

   public WindowIdImpl getWindowId(@NonNull View var1) {
      return new WindowIdApi18(var1);
   }
}
