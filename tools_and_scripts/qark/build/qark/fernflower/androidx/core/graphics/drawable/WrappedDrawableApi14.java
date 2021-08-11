package androidx.core.graphics.drawable;

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

class WrappedDrawableApi14 extends Drawable implements Callback, WrappedDrawable, TintAwareDrawable {
   static final Mode DEFAULT_TINT_MODE;
   private boolean mColorFilterSet;
   private int mCurrentColor;
   private Mode mCurrentMode;
   Drawable mDrawable;
   private boolean mMutated;
   WrappedDrawableState mState;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   WrappedDrawableApi14(Drawable var1) {
      this.mState = this.mutateConstantState();
      this.setWrappedDrawable(var1);
   }

   WrappedDrawableApi14(WrappedDrawableState var1, Resources var2) {
      this.mState = var1;
      this.updateLocalState(var2);
   }

   private WrappedDrawableState mutateConstantState() {
      return new WrappedDrawableState(this.mState);
   }

   private void updateLocalState(Resources var1) {
      WrappedDrawableState var2 = this.mState;
      if (var2 != null && var2.mDrawableState != null) {
         this.setWrappedDrawable(this.mState.mDrawableState.newDrawable(var1));
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
      WrappedDrawableState var3 = this.mState;
      int var1;
      if (var3 != null) {
         var1 = var3.getChangingConfigurations();
      } else {
         var1 = 0;
      }

      return var2 | var1 | this.mDrawable.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      WrappedDrawableState var1 = this.mState;
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

   public boolean isAutoMirrored() {
      return this.mDrawable.isAutoMirrored();
   }

   protected boolean isCompatTintEnabled() {
      return true;
   }

   public boolean isStateful() {
      ColorStateList var2;
      if (this.isCompatTintEnabled()) {
         WrappedDrawableState var1 = this.mState;
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

         WrappedDrawableState var2 = this.mState;
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

   public void setAutoMirrored(boolean var1) {
      this.mDrawable.setAutoMirrored(var1);
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
         WrappedDrawableState var3 = this.mState;
         if (var3 != null) {
            var3.mDrawableState = var1.getConstantState();
         }
      }

      this.invalidateSelf();
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      this.unscheduleSelf(var2);
   }
}
