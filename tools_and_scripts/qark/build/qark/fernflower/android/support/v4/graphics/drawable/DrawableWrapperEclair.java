package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.Nullable;

class DrawableWrapperEclair extends DrawableWrapperDonut {
   DrawableWrapperEclair(Drawable var1) {
      super(var1);
   }

   DrawableWrapperEclair(DrawableWrapperDonut.DrawableWrapperState var1, Resources var2) {
      super(var1, var2);
   }

   DrawableWrapperDonut.DrawableWrapperState mutateConstantState() {
      return new DrawableWrapperEclair.DrawableWrapperStateEclair(this.mState, (Resources)null);
   }

   protected Drawable newDrawableFromState(ConstantState var1, Resources var2) {
      return var1.newDrawable(var2);
   }

   private static class DrawableWrapperStateEclair extends DrawableWrapperDonut.DrawableWrapperState {
      DrawableWrapperStateEclair(@Nullable DrawableWrapperDonut.DrawableWrapperState var1, @Nullable Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(@Nullable Resources var1) {
         return new DrawableWrapperEclair(this, var1);
      }
   }
}
