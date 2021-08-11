package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import androidx.core.graphics.drawable.TintAwareDrawable;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

public class RippleDrawableCompat extends Drawable implements Shapeable, TintAwareDrawable {
   private RippleDrawableCompat.RippleDrawableCompatState drawableState;

   private RippleDrawableCompat(RippleDrawableCompat.RippleDrawableCompatState var1) {
      this.drawableState = var1;
   }

   // $FF: synthetic method
   RippleDrawableCompat(RippleDrawableCompat.RippleDrawableCompatState var1, Object var2) {
      this(var1);
   }

   public RippleDrawableCompat(ShapeAppearanceModel var1) {
      this(new RippleDrawableCompat.RippleDrawableCompatState(new MaterialShapeDrawable(var1)));
   }

   public void draw(Canvas var1) {
      if (this.drawableState.shouldDrawDelegate) {
         this.drawableState.delegate.draw(var1);
      }

   }

   public ConstantState getConstantState() {
      return this.drawableState;
   }

   public int getOpacity() {
      return this.drawableState.delegate.getOpacity();
   }

   public ShapeAppearanceModel getShapeAppearanceModel() {
      return this.drawableState.delegate.getShapeAppearanceModel();
   }

   public boolean isStateful() {
      return true;
   }

   public RippleDrawableCompat mutate() {
      this.drawableState = new RippleDrawableCompat.RippleDrawableCompatState(this.drawableState);
      return this;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.drawableState.delegate.setBounds(var1);
   }

   protected boolean onStateChange(int[] var1) {
      boolean var2 = super.onStateChange(var1);
      if (this.drawableState.delegate.setState(var1)) {
         var2 = true;
      }

      boolean var3 = RippleUtils.shouldDrawRippleCompat(var1);
      if (this.drawableState.shouldDrawDelegate != var3) {
         this.drawableState.shouldDrawDelegate = var3;
         var2 = true;
      }

      return var2;
   }

   public void setAlpha(int var1) {
      this.drawableState.delegate.setAlpha(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.drawableState.delegate.setColorFilter(var1);
   }

   public void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      this.drawableState.delegate.setShapeAppearanceModel(var1);
   }

   public void setTint(int var1) {
      this.drawableState.delegate.setTint(var1);
   }

   public void setTintList(ColorStateList var1) {
      this.drawableState.delegate.setTintList(var1);
   }

   public void setTintMode(Mode var1) {
      this.drawableState.delegate.setTintMode(var1);
   }

   static final class RippleDrawableCompatState extends ConstantState {
      MaterialShapeDrawable delegate;
      boolean shouldDrawDelegate;

      public RippleDrawableCompatState(RippleDrawableCompat.RippleDrawableCompatState var1) {
         this.delegate = (MaterialShapeDrawable)var1.delegate.getConstantState().newDrawable();
         this.shouldDrawDelegate = var1.shouldDrawDelegate;
      }

      public RippleDrawableCompatState(MaterialShapeDrawable var1) {
         this.delegate = var1;
         this.shouldDrawDelegate = false;
      }

      public int getChangingConfigurations() {
         return 0;
      }

      public RippleDrawableCompat newDrawable() {
         return new RippleDrawableCompat(new RippleDrawableCompat.RippleDrawableCompatState(this));
      }
   }
}
