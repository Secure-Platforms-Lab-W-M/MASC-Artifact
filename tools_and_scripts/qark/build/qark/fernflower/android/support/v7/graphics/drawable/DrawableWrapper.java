package android.support.v7.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableWrapper extends Drawable implements Callback {
   private Drawable mDrawable;

   public DrawableWrapper(Drawable var1) {
      this.setWrappedDrawable(var1);
   }

   public void draw(Canvas var1) {
      this.mDrawable.draw(var1);
   }

   public int getChangingConfigurations() {
      return this.mDrawable.getChangingConfigurations();
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

   public Drawable getWrappedDrawable() {
      return this.mDrawable;
   }

   public void invalidateDrawable(Drawable var1) {
      this.invalidateSelf();
   }

   public boolean isAutoMirrored() {
      return DrawableCompat.isAutoMirrored(this.mDrawable);
   }

   public boolean isStateful() {
      return this.mDrawable.isStateful();
   }

   public void jumpToCurrentState() {
      DrawableCompat.jumpToCurrentState(this.mDrawable);
   }

   protected void onBoundsChange(Rect var1) {
      this.mDrawable.setBounds(var1);
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
      DrawableCompat.setAutoMirrored(this.mDrawable, var1);
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

   public void setHotspot(float var1, float var2) {
      DrawableCompat.setHotspot(this.mDrawable, var1, var2);
   }

   public void setHotspotBounds(int var1, int var2, int var3, int var4) {
      DrawableCompat.setHotspotBounds(this.mDrawable, var1, var2, var3, var4);
   }

   public boolean setState(int[] var1) {
      return this.mDrawable.setState(var1);
   }

   public void setTint(int var1) {
      DrawableCompat.setTint(this.mDrawable, var1);
   }

   public void setTintList(ColorStateList var1) {
      DrawableCompat.setTintList(this.mDrawable, var1);
   }

   public void setTintMode(Mode var1) {
      DrawableCompat.setTintMode(this.mDrawable, var1);
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return super.setVisible(var1, var2) || this.mDrawable.setVisible(var1, var2);
   }

   public void setWrappedDrawable(Drawable var1) {
      Drawable var2 = this.mDrawable;
      if (var2 != null) {
         var2.setCallback((Callback)null);
      }

      this.mDrawable = var1;
      if (var1 != null) {
         var1.setCallback(this);
      }

   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      this.unscheduleSelf(var2);
   }
}
