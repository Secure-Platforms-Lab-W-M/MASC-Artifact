package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import com.google.android.material.R.color;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;

class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {
   FloatingActionButtonImplLollipop(FloatingActionButton var1, ShadowViewDelegate var2) {
      super(var1, var2);
   }

   private Animator createElevationAnimator(float var1, float var2) {
      AnimatorSet var3 = new AnimatorSet();
      var3.play(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{var1}).setDuration(0L)).with(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{var2}).setDuration(100L));
      var3.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      return var3;
   }

   BorderDrawable createBorderDrawable(int var1, ColorStateList var2) {
      Context var3 = this.view.getContext();
      BorderDrawable var4 = new BorderDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
      var4.setGradientColors(ContextCompat.getColor(var3, color.design_fab_stroke_top_outer_color), ContextCompat.getColor(var3, color.design_fab_stroke_top_inner_color), ContextCompat.getColor(var3, color.design_fab_stroke_end_inner_color), ContextCompat.getColor(var3, color.design_fab_stroke_end_outer_color));
      var4.setBorderWidth((float)var1);
      var4.setBorderTint(var2);
      return var4;
   }

   MaterialShapeDrawable createShapeDrawable() {
      return new FloatingActionButtonImplLollipop.AlwaysStatefulMaterialShapeDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
   }

   public float getElevation() {
      return this.view.getElevation();
   }

   void getPadding(Rect var1) {
      if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
         super.getPadding(var1);
      } else if (!this.shouldExpandBoundsForA11y()) {
         int var2 = (this.minTouchTargetSize - this.view.getSizeDimension()) / 2;
         var1.set(var2, var2, var2, var2);
      } else {
         var1.set(0, 0, 0, 0);
      }
   }

   void initializeBackgroundDrawable(ColorStateList var1, Mode var2, ColorStateList var3, int var4) {
      this.shapeDrawable = this.createShapeDrawable();
      this.shapeDrawable.setTintList(var1);
      if (var2 != null) {
         this.shapeDrawable.setTintMode(var2);
      }

      this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
      Object var5;
      if (var4 > 0) {
         this.borderDrawable = this.createBorderDrawable(var4, var1);
         var5 = new LayerDrawable(new Drawable[]{(Drawable)Preconditions.checkNotNull(this.borderDrawable), (Drawable)Preconditions.checkNotNull(this.shapeDrawable)});
      } else {
         this.borderDrawable = null;
         var5 = this.shapeDrawable;
      }

      this.rippleDrawable = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(var3), (Drawable)var5, (Drawable)null);
      this.contentBackground = this.rippleDrawable;
   }

   void jumpDrawableToCurrentState() {
   }

   void onCompatShadowChanged() {
      this.updatePadding();
   }

   void onDrawableStateChanged(int[] var1) {
      if (VERSION.SDK_INT == 21) {
         if (this.view.isEnabled()) {
            this.view.setElevation(this.elevation);
            if (this.view.isPressed()) {
               this.view.setTranslationZ(this.pressedTranslationZ);
               return;
            }

            if (!this.view.isFocused() && !this.view.isHovered()) {
               this.view.setTranslationZ(0.0F);
               return;
            }

            this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
            return;
         }

         this.view.setElevation(0.0F);
         this.view.setTranslationZ(0.0F);
      }

   }

   void onElevationsChanged(float var1, float var2, float var3) {
      if (VERSION.SDK_INT == 21) {
         this.view.refreshDrawableState();
      } else {
         StateListAnimator var4 = new StateListAnimator();
         var4.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var3));
         var4.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         var4.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         var4.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(var1, var2));
         AnimatorSet var5 = new AnimatorSet();
         ArrayList var6 = new ArrayList();
         var6.add(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{var1}).setDuration(0L));
         if (VERSION.SDK_INT >= 22 && VERSION.SDK_INT <= 24) {
            var6.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{this.view.getTranslationZ()}).setDuration(100L));
         }

         var6.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{0.0F}).setDuration(100L));
         var5.playSequentially((Animator[])var6.toArray(new Animator[0]));
         var5.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
         var4.addState(ENABLED_STATE_SET, var5);
         var4.addState(EMPTY_STATE_SET, this.createElevationAnimator(0.0F, 0.0F));
         this.view.setStateListAnimator(var4);
      }

      if (this.shouldAddPadding()) {
         this.updatePadding();
      }

   }

   boolean requirePreDrawListener() {
      return false;
   }

   void setRippleColor(ColorStateList var1) {
      if (this.rippleDrawable instanceof RippleDrawable) {
         ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.sanitizeRippleDrawableColor(var1));
      } else {
         super.setRippleColor(var1);
      }
   }

   boolean shouldAddPadding() {
      return this.shadowViewDelegate.isCompatPaddingEnabled() || !this.shouldExpandBoundsForA11y();
   }

   void updateFromViewRotation() {
   }

   static class AlwaysStatefulMaterialShapeDrawable extends MaterialShapeDrawable {
      AlwaysStatefulMaterialShapeDrawable(ShapeAppearanceModel var1) {
         super(var1);
      }

      public boolean isStateful() {
         return true;
      }
   }
}
