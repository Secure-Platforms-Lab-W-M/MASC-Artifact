package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class DrawableWrapperApi14 extends Drawable implements Callback, DrawableWrapper, TintAwareDrawable {
   static final Mode DEFAULT_TINT_MODE;
   private boolean mColorFilterSet;
   private int mCurrentColor;
   private Mode mCurrentMode;
   Drawable mDrawable;
   private boolean mMutated;
   DrawableWrapperApi14.DrawableWrapperState mState;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   DrawableWrapperApi14(@Nullable Drawable var1) {
      this.mState = this.mutateConstantState();
      this.setWrappedDrawable(var1);
   }

   DrawableWrapperApi14(@NonNull DrawableWrapperApi14.DrawableWrapperState var1, @Nullable Resources var2) {
      this.mState = var1;
      this.updateLocalState(var2);
   }

   private void updateLocalState(@Nullable Resources var1) {
      DrawableWrapperApi14.DrawableWrapperState var2 = this.mState;
      if (var2 != null && var2.mDrawableState != null) {
         this.setWrappedDrawable(this.newDrawableFromState(this.mState.mDrawableState, var1));
      }

   }

   private boolean updateTint(int[] var1) {
      if (!this.isCompatTintEnabled()) {
         return false;
      } else {
         ColorStateList var3 = this.mState.mTint;
         Mode var4 = this.mState.mTintMode;
         if (var3 != null && var4 != null) {
            int var2 = var3.getColorForState(var1, var3.getDefaultColor());
            if (this.mColorFilterSet && var2 == this.mCurrentColor && var4 == this.mCurrentMode) {
               return false;
            } else {
               this.setColorFilter(var2, var4);
               this.mCurrentColor = var2;
               this.mCurrentMode = var4;
               this.mColorFilterSet = true;
               return true;
            }
         } else {
            this.mColorFilterSet = false;
            this.clearColorFilter();
            return false;
         }
      }
   }

   public void draw(Canvas var1) {
      this.mDrawable.draw(var1);
   }

   public int getChangingConfigurations() {
      int var2 = super.getChangingConfigurations();
      DrawableWrapperApi14.DrawableWrapperState var3 = this.mState;
      int var1;
      if (var3 != null) {
         var1 = var3.getChangingConfigurations();
      } else {
         var1 = 0;
      }

      return var2 | var1 | this.mDrawable.getChangingConfigurations();
   }

   @Nullable
   public ConstantState getConstantState() {
      DrawableWrapperApi14.DrawableWrapperState var1 = this.mState;
      if (var1 != null && var1.canConstantState()) {
         this.mState.mChangingConfigurations = this.getChangingConfigurations();
         return this.mState;
      } else {
         return null;
      }
   }

   public Drawable getCurrent() {
      return this.mDrawable.getCurrent();
   }

   public int getIntrinsicHeight() {
      return this.mDrawable.getIntrinsicHeight();
   }

   public int getIntrinsicWidth() {
      return this.mDrawable.getIntrinsicWidth();
   }

   public int getMinimumHeight() {
      return this.mDrawable.getMinimumHeight();
   }

   public int getMinimumWidth() {
      return this.mDrawable.getMinimumWidth();
   }

   public int getOpacity() {
      return this.mDrawable.getOpacity();
   }

   public boolean getPadding(Rect var1) {
      return this.mDrawable.getPadding(var1);
   }

   public int[] getState() {
      return this.mDrawable.getState();
   }

   public Region getTransparentRegion() {
      return this.mDrawable.getTransparentRegion();
   }

   public final Drawable getWrappedDrawable() {
      return this.mDrawable;
   }

   public void invalidateDrawable(Drawable var1) {
      this.invalidateSelf();
   }

   protected boolean isCompatTintEnabled() {
      return true;
   }

   public boolean isStateful() {
      ColorStateList var2;
      if (this.isCompatTintEnabled()) {
         DrawableWrapperApi14.DrawableWrapperState var1 = this.mState;
         if (var1 != null) {
            var2 = var1.mTint;
            return var2 != null && var2.isStateful() || this.mDrawable.isStateful();
         }
      }

      var2 = null;
      return var2 != null && var2.isStateful() || this.mDrawable.isStateful();
   }

   public void jumpToCurrentState() {
      this.mDrawable.jumpToCurrentState();
   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
         this.mState = this.mutateConstantState();
         Drawable var1 = this.mDrawable;
         if (var1 != null) {
            var1.mutate();
         }

         DrawableWrapperApi14.DrawableWrapperState var2 = this.mState;
         if (var2 != null) {
            var1 = this.mDrawable;
            ConstantState var3;
            if (var1 != null) {
               var3 = var1.getConstantState();
            } else {
               var3 = null;
            }

            var2.mDrawableState = var3;
         }

         this.mMutated = true;
      }

      return this;
   }

   @NonNull
   DrawableWrapperApi14.DrawableWrapperState mutateConstantState() {
      return new DrawableWrapperApi14.DrawableWrapperStateBase(this.mState, (Resources)null);
   }

   protected Drawable newDrawableFromState(@NonNull ConstantState var1, @Nullable Resources var2) {
      return var1.newDrawable(var2);
   }

   protected void onBoundsChange(Rect var1) {
      Drawable var2 = this.mDrawable;
      if (var2 != null) {
         var2.setBounds(var1);
      }

   }

   protected boolean onLevelChange(int var1) {
      return this.mDrawable.setLevel(var1);
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      this.scheduleSelf(var2, var3);
   }

   public void setAlpha(int var1) {
      this.mDrawable.setAlpha(var1);
   }

   public void setChangingConfigurations(int var1) {
      this.mDrawable.setChangingConfigurations(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.mDrawable.setColorFilter(var1);
   }

   public void setDither(boolean var1) {
      this.mDrawable.setDither(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.mDrawable.setFilterBitmap(var1);
   }

   public boolean setState(int[] var1) {
      boolean var2 = this.mDrawable.setState(var1);
      if (!this.updateTint(var1) && !var2) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void setTint(int var1) {
      this.setTintList(ColorStateList.valueOf(var1));
   }

   public void setTintList(ColorStateList var1) {
      this.mState.mTint = var1;
      this.updateTint(this.getState());
   }

   public void setTintMode(Mode var1) {
      this.mState.mTintMode = var1;
      this.updateTint(this.getState());
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return super.setVisible(var1, var2) || this.mDrawable.setVisible(var1, var2);
   }

   public final void setWrappedDrawable(Drawable var1) {
      Drawable var2 = this.mDrawable;
      if (var2 != null) {
         var2.setCallback((Callback)null);
      }

      this.mDrawable = var1;
      if (var1 != null) {
         var1.setCallback(this);
         this.setVisible(var1.isVisible(), true);
         this.setState(var1.getState());
         this.setLevel(var1.getLevel());
         this.setBounds(var1.getBounds());
         DrawableWrapperApi14.DrawableWrapperState var3 = this.mState;
         if (var3 != null) {
            var3.mDrawableState = var1.getConstantState();
         }
      }

      this.invalidateSelf();
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      this.unscheduleSelf(var2);
   }

   protected abstract static class DrawableWrapperState extends ConstantState {
      int mChangingConfigurations;
      ConstantState mDrawableState;
      ColorStateList mTint = null;
      Mode mTintMode;

      DrawableWrapperState(@Nullable DrawableWrapperApi14.DrawableWrapperState var1, @Nullable Resources var2) {
         this.mTintMode = DrawableWrapperApi14.DEFAULT_TINT_MODE;
         if (var1 != null) {
            this.mChangingConfigurations = var1.mChangingConfigurations;
            this.mDrawableState = var1.mDrawableState;
            this.mTint = var1.mTint;
            this.mTintMode = var1.mTintMode;
         }

      }

      boolean canConstantState() {
         return this.mDrawableState != null;
      }

      public int getChangingConfigurations() {
         int var2 = this.mChangingConfigurations;
         ConstantState var3 = this.mDrawableState;
         int var1;
         if (var3 != null) {
            var1 = var3.getChangingConfigurations();
         } else {
            var1 = 0;
         }

         return var2 | var1;
      }

      public Drawable newDrawable() {
         return this.newDrawable((Resources)null);
      }

      public abstract Drawable newDrawable(@Nullable Resources var1);
   }

   private static class DrawableWrapperStateBase extends DrawableWrapperApi14.DrawableWrapperState {
      DrawableWrapperStateBase(@Nullable DrawableWrapperApi14.DrawableWrapperState var1, @Nullable Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(@Nullable Resources var1) {
         return new DrawableWrapperApi14(this, var1);
      }
   }
}
