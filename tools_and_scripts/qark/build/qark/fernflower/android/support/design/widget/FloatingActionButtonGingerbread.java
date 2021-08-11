package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R$anim;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.animation.Animation;

class FloatingActionButtonGingerbread extends FloatingActionButtonImpl {
   ShadowDrawableWrapper mShadowDrawable;
   private final StateListAnimator mStateListAnimator = new StateListAnimator();

   FloatingActionButtonGingerbread(VisibilityAwareImageButton var1, ShadowViewDelegate var2, ValueAnimatorCompat.Creator var3) {
      super(var1, var2, var3);
      this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonGingerbread.ElevateToTranslationZAnimation()));
      this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonGingerbread.ElevateToTranslationZAnimation()));
      this.mStateListAnimator.addState(ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonGingerbread.ResetElevationAnimation()));
      this.mStateListAnimator.addState(EMPTY_STATE_SET, this.createAnimator(new FloatingActionButtonGingerbread.DisabledElevationAnimation()));
   }

   private ValueAnimatorCompat createAnimator(@NonNull FloatingActionButtonGingerbread.ShadowAnimatorImpl var1) {
      ValueAnimatorCompat var2 = this.mAnimatorCreator.createAnimator();
      var2.setInterpolator(ANIM_INTERPOLATOR);
      var2.setDuration(100L);
      var2.addListener(var1);
      var2.addUpdateListener(var1);
      var2.setFloatValues(0.0F, 1.0F);
      return var2;
   }

   private static ColorStateList createColorStateList(int var0) {
      int[][] var2 = new int[3][];
      int[] var3 = new int[3];
      var2[0] = FOCUSED_ENABLED_STATE_SET;
      var3[0] = var0;
      int var1 = 0 + 1;
      var2[var1] = PRESSED_ENABLED_STATE_SET;
      var3[var1] = var0;
      var0 = var1 + 1;
      var2[var0] = new int[0];
      var3[var0] = 0;
      return new ColorStateList(var2, var3);
   }

   float getElevation() {
      return this.mElevation;
   }

   void getPadding(Rect var1) {
      this.mShadowDrawable.getPadding(var1);
   }

   void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeHidden()) {
         this.mAnimState = 1;
         Animation var3 = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R$anim.design_fab_out);
         var3.setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
         var3.setDuration(200L);
         var3.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
            public void onAnimationEnd(Animation var1x) {
               FloatingActionButtonGingerbread.this.mAnimState = 0;
               VisibilityAwareImageButton var3 = FloatingActionButtonGingerbread.this.mView;
               byte var2x;
               if (var2) {
                  var2x = 8;
               } else {
                  var2x = 4;
               }

               var3.internalSetVisibility(var2x, var2);
               if (var1 != null) {
                  var1.onHidden();
               }

            }
         });
         this.mView.startAnimation(var3);
      }
   }

   void jumpDrawableToCurrentState() {
      this.mStateListAnimator.jumpToCurrentState();
   }

   void onCompatShadowChanged() {
   }

   void onDrawableStateChanged(int[] var1) {
      this.mStateListAnimator.setState(var1);
   }

   void onElevationsChanged(float var1, float var2) {
      if (this.mShadowDrawable != null) {
         this.mShadowDrawable.setShadowSize(var1, this.mPressedTranslationZ + var1);
         this.updatePadding();
      }

   }

   void setBackgroundDrawable(ColorStateList var1, Mode var2, int var3, int var4) {
      this.mShapeDrawable = DrawableCompat.wrap(this.createShapeDrawable());
      DrawableCompat.setTintList(this.mShapeDrawable, var1);
      if (var2 != null) {
         DrawableCompat.setTintMode(this.mShapeDrawable, var2);
      }

      this.mRippleDrawable = DrawableCompat.wrap(this.createShapeDrawable());
      DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(var3));
      Drawable[] var5;
      if (var4 > 0) {
         this.mBorderDrawable = this.createBorderDrawable(var4, var1);
         var5 = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
      } else {
         this.mBorderDrawable = null;
         var5 = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
      }

      this.mContentBackground = new LayerDrawable(var5);
      this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getContext(), this.mContentBackground, this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
      this.mShadowDrawable.setAddPaddingForCorners(false);
      this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
   }

   void setBackgroundTintList(ColorStateList var1) {
      if (this.mShapeDrawable != null) {
         DrawableCompat.setTintList(this.mShapeDrawable, var1);
      }

      if (this.mBorderDrawable != null) {
         this.mBorderDrawable.setBorderTint(var1);
      }

   }

   void setBackgroundTintMode(Mode var1) {
      if (this.mShapeDrawable != null) {
         DrawableCompat.setTintMode(this.mShapeDrawable, var1);
      }

   }

   void setRippleColor(int var1) {
      if (this.mRippleDrawable != null) {
         DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(var1));
      }

   }

   void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, boolean var2) {
      if (!this.isOrWillBeShown()) {
         this.mAnimState = 2;
         this.mView.internalSetVisibility(0, var2);
         Animation var3 = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R$anim.design_fab_in);
         var3.setDuration(200L);
         var3.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
         var3.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
            public void onAnimationEnd(Animation var1x) {
               FloatingActionButtonGingerbread.this.mAnimState = 0;
               if (var1 != null) {
                  var1.onShown();
               }

            }
         });
         this.mView.startAnimation(var3);
      }
   }

   private class DisabledElevationAnimation extends FloatingActionButtonGingerbread.ShadowAnimatorImpl {
      DisabledElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return 0.0F;
      }
   }

   private class ElevateToTranslationZAnimation extends FloatingActionButtonGingerbread.ShadowAnimatorImpl {
      ElevateToTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonGingerbread.this.mElevation + FloatingActionButtonGingerbread.this.mPressedTranslationZ;
      }
   }

   private class ResetElevationAnimation extends FloatingActionButtonGingerbread.ShadowAnimatorImpl {
      ResetElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonGingerbread.this.mElevation;
      }
   }

   private abstract class ShadowAnimatorImpl extends ValueAnimatorCompat.AnimatorListenerAdapter implements ValueAnimatorCompat.AnimatorUpdateListener {
      private float mShadowSizeEnd;
      private float mShadowSizeStart;
      private boolean mValidValues;

      private ShadowAnimatorImpl() {
      }

      // $FF: synthetic method
      ShadowAnimatorImpl(Object var2) {
         this();
      }

      protected abstract float getTargetShadowSize();

      public void onAnimationEnd(ValueAnimatorCompat var1) {
         FloatingActionButtonGingerbread.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
         this.mValidValues = false;
      }

      public void onAnimationUpdate(ValueAnimatorCompat var1) {
         if (!this.mValidValues) {
            this.mShadowSizeStart = FloatingActionButtonGingerbread.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeEnd = this.getTargetShadowSize();
            this.mValidValues = true;
         }

         FloatingActionButtonGingerbread.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + (this.mShadowSizeEnd - this.mShadowSizeStart) * var1.getAnimatedFraction());
      }
   }
}
