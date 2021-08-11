package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DrawableWrapperApi19 extends DrawableWrapperApi14 {
   DrawableWrapperApi19(Drawable var1) {
      super(var1);
   }

   DrawableWrapperApi19(DrawableWrapperApi14.DrawableWrapperState var1, Resources var2) {
      super(var1, var2);
   }

   public boolean isAutoMirrored() {
      return this.mDrawable.isAutoMirrored();
   }

   @NonNull
   DrawableWrapperApi14.DrawableWrapperState mutateConstantState() {
      return new DrawableWrapperApi19.DrawableWrapperStateKitKat(this.mState, (Resources)null);
   }

   public void setAutoMirrored(boolean var1) {
      this.mDrawable.setAutoMirrored(var1);
   }

   private static class DrawableWrapperStateKitKat extends DrawableWrapperApi14.DrawableWrapperState {
      DrawableWrapperStateKitKat(@Nullable DrawableWrapperApi14.DrawableWrapperState var1, @Nullable Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(@Nullable Resources var1) {
         return new DrawableWrapperApi19(this, var1);
      }
   }
}
