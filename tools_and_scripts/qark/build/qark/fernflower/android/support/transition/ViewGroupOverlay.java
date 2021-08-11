package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class ViewGroupOverlay extends ViewOverlay {
   ViewGroupOverlay(Context var1, ViewGroup var2, View var3) {
      super(var1, var2, var3);
   }

   public static ViewGroupOverlay createFrom(ViewGroup var0) {
      return (ViewGroupOverlay)ViewOverlay.createFrom(var0);
   }

   public void add(View var1) {
      this.mOverlayViewGroup.add(var1);
   }

   public void remove(View var1) {
      this.mOverlayViewGroup.remove(var1);
   }
}
