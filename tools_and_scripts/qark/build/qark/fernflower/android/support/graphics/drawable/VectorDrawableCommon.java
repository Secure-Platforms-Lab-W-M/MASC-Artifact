package android.support.graphics.drawable;

import android.content.res.Resources.Theme;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;

abstract class VectorDrawableCommon extends Drawable implements TintAwareDrawable {
   Drawable mDelegateDrawable;

   public void applyTheme(Theme var1) {
      Drawable var2 = this.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.applyTheme(var2, var1);
      }
   }

   public void clearColorFilter() {
      Drawable var1 = this.mDelegateDrawable;
      if (var1 != null) {
         var1.clearColorFilter();
      } else {
         super.clearColorFilter();
      }
   }

   public ColorFilter getColorFilter() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? DrawableCompat.getColorFilter(var1) : null;
   }

   public Drawable getCurrent() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? var1.getCurrent() : super.getCurrent();
   }

   public int getMinimumHeight() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? var1.getMinimumHeight() : super.getMinimumHeight();
   }

   public int getMinimumWidth() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? var1.getMinimumWidth() : super.getMinimumWidth();
   }

   public boolean getPadding(Rect var1) {
      Drawable var2 = this.mDelegateDrawable;
      return var2 != null ? var2.getPadding(var1) : super.getPadding(var1);
   }

   public int[] getState() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? var1.getState() : super.getState();
   }

   public Region getTransparentRegion() {
      Drawable var1 = this.mDelegateDrawable;
      return var1 != null ? var1.getTransparentRegion() : super.getTransparentRegion();
   }

   public void jumpToCurrentState() {
      Drawable var1 = this.mDelegateDrawable;
      if (var1 != null) {
         DrawableCompat.jumpToCurrentState(var1);
      }
   }

   protected void onBoundsChange(Rect var1) {
      Drawable var2 = this.mDelegateDrawable;
      if (var2 != null) {
         var2.setBounds(var1);
      } else {
         super.onBoundsChange(var1);
      }
   }

   protected boolean onLevelChange(int var1) {
      Drawable var2 = this.mDelegateDrawable;
      return var2 != null ? var2.setLevel(var1) : super.onLevelChange(var1);
   }

   public void setChangingConfigurations(int var1) {
      Drawable var2 = this.mDelegateDrawable;
      if (var2 != null) {
         var2.setChangingConfigurations(var1);
      } else {
         super.setChangingConfigurations(var1);
      }
   }

   public void setColorFilter(int var1, Mode var2) {
      Drawable var3 = this.mDelegateDrawable;
      if (var3 != null) {
         var3.setColorFilter(var1, var2);
      } else {
         super.setColorFilter(var1, var2);
      }
   }

   public void setFilterBitmap(boolean var1) {
      Drawable var2 = this.mDelegateDrawable;
      if (var2 != null) {
         var2.setFilterBitmap(var1);
      }
   }

   public void setHotspot(float var1, float var2) {
      Drawable var3 = this.mDelegateDrawable;
      if (var3 != null) {
         DrawableCompat.setHotspot(var3, var1, var2);
      }

   }

   public void setHotspotBounds(int var1, int var2, int var3, int var4) {
      Drawable var5 = this.mDelegateDrawable;
      if (var5 != null) {
         DrawableCompat.setHotspotBounds(var5, var1, var2, var3, var4);
      }
   }

   public boolean setState(int[] var1) {
      Drawable var2 = this.mDelegateDrawable;
      return var2 != null ? var2.setState(var1) : super.setState(var1);
   }
}
