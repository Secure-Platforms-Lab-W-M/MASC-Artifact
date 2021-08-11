package android.support.transition;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

@RequiresApi(18)
class ViewGroupOverlayApi18 implements ViewGroupOverlayImpl {
   private final ViewGroupOverlay mViewGroupOverlay;

   ViewGroupOverlayApi18(@NonNull ViewGroup var1) {
      this.mViewGroupOverlay = var1.getOverlay();
   }

   public void add(@NonNull Drawable var1) {
      this.mViewGroupOverlay.add(var1);
   }

   public void add(@NonNull View var1) {
      this.mViewGroupOverlay.add(var1);
   }

   public void clear() {
      this.mViewGroupOverlay.clear();
   }

   public void remove(@NonNull Drawable var1) {
      this.mViewGroupOverlay.remove(var1);
   }

   public void remove(@NonNull View var1) {
      this.mViewGroupOverlay.remove(var1);
   }
}
