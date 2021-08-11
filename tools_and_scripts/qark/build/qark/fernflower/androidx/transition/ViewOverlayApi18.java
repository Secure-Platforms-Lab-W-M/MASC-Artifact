package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

class ViewOverlayApi18 implements ViewOverlayImpl {
   private final ViewOverlay mViewOverlay;

   ViewOverlayApi18(View var1) {
      this.mViewOverlay = var1.getOverlay();
   }

   public void add(Drawable var1) {
      this.mViewOverlay.add(var1);
   }

   public void remove(Drawable var1) {
      this.mViewOverlay.remove(var1);
   }
}
