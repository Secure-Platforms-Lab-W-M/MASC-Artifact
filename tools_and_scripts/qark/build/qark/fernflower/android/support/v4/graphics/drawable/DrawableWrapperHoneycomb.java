package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@TargetApi(11)
@RequiresApi(11)
class DrawableWrapperHoneycomb extends DrawableWrapperGingerbread {
   DrawableWrapperHoneycomb(Drawable var1) {
      super(var1);
   }

   DrawableWrapperHoneycomb(DrawableWrapperGingerbread.DrawableWrapperState var1, Resources var2) {
      super(var1, var2);
   }

   public void jumpToCurrentState() {
      this.mDrawable.jumpToCurrentState();
   }

   @NonNull
   DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
      return new DrawableWrapperHoneycomb.DrawableWrapperStateHoneycomb(this.mState, (Resources)null);
   }

   private static class DrawableWrapperStateHoneycomb extends DrawableWrapperGingerbread.DrawableWrapperState {
      DrawableWrapperStateHoneycomb(@Nullable DrawableWrapperGingerbread.DrawableWrapperState var1, @Nullable Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(@Nullable Resources var1) {
         return new DrawableWrapperHoneycomb(this, var1);
      }
   }
}
