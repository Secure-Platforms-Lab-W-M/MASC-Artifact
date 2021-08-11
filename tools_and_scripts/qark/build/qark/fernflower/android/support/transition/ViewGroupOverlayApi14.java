package android.support.transition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@RequiresApi(14)
class ViewGroupOverlayApi14 extends ViewOverlayApi14 implements ViewGroupOverlayImpl {
   ViewGroupOverlayApi14(Context var1, ViewGroup var2, View var3) {
      super(var1, var2, var3);
   }

   static ViewGroupOverlayApi14 createFrom(ViewGroup var0) {
      return (ViewGroupOverlayApi14)ViewOverlayApi14.createFrom(var0);
   }

   public void add(@NonNull View var1) {
      this.mOverlayViewGroup.add(var1);
   }

   public void remove(@NonNull View var1) {
      this.mOverlayViewGroup.remove(var1);
   }
}
