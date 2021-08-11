package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.R$color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Interpolator;

@RequiresApi(14)
class FloatingActionButtonImpl {
   static final Interpolator ANIM_INTERPOLATOR;
   static final int ANIM_STATE_HIDING = 1;
   static final int ANIM_STATE_NONE = 0;
   static final int ANIM_STATE_SHOWING = 2;
   static final int[] EMPTY_STATE_SET;
   static final int[] ENABLED_STATE_SET;
   static final int[] FOCUSED_ENABLED_STATE_SET;
   static final long PRESSED_ANIM_DELAY = 100L;
   static final long PRESSED_ANIM_DURATION = 100L;
   static final int[] PRESSED_ENABLED_STATE_SET;
   static final int SHOW_HIDE_ANIM_DURATION = 200;
   int mAnimState = 0;
   CircularBorderDrawable mBorderDrawable;
   Drawable mContentBackground;
   float mElevation;
   private OnPreDrawListener mPreDrawListener;
   float mPressedTranslationZ;
   Drawable mRippleDrawable;
   private float mRotation;
   ShadowDrawableWrapper mShadowDrawable;
   final ShadowViewDelegate mShadowViewDelegate;
   Drawable mShapeDrawable;
   private final StateListAnimator mStateListAnimator;
   private final Rect mTmpRect = new Rect();
   final VisibilityAwareImageButton mView;

   static {
      ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
      PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
      FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
      ENABLED_STATE_SET = new int[]{16842910};
      EMPTY_STATE_SET = new int[0];
   }

   FloatingActionButtonImpl(VisibilityAwareImageButton var1, ShadowViewDelegate var2) {
      this.mView = var1;
      this.mShadowViewDelegate = var2;
      this.mStateListAnimator = new StateListAnimator();
      this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonImpl.ElevateToTranslationZAnimation()));
      this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonImpl.ElevateToTranslationZAnimation()));
      this.mStateListAnimator.addState(ENABLED_STATE_SET, this.createAnimator(new FloatingActionButtonImpl.ResetElevationAnimation()));
      this.mStateListAnimator.addState(EMPTY_STATE_SET, this.createAnimator(new FloatingActionButtonImpl.DisabledElevationAnimation()));
      this.mRotation = this.mView.getRotation();
   }

   private ValueAnimator createAnimator(@NonNull FloatingActionButtonImpl.ShadowAnimatorImpl var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setInterpolator(ANIM_INTERPOLATOR);
      var2.setDuration(100L);
      var2.addListener(var1);
      var2.addUpdateListener(var1);
      var2.setFloatValues(new float[]{0.0F, 1.0F});
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

   private void ensurePreDrawListener() {
      if (this.mPreDrawListener == null) {
         this.mPreDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
               FloatingActionButtonImpl.this.onPreDraw();
               return true;
            }
         };
      }
   }

   private boolean shouldAnimateVisibilityChange() {
      return ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode();
   }

   private void updateFromViewRotation() {
      if (VERSION.SDK_INT == 19) {
         if (this.mRotation % 90.0F != 0.0F) {
            if (this.mView.getLayerType() != 1) {
               this.mView.setLayerType(1, (Paint)null);
            }
         } else if (this.mView.getLayerType() != 0) {
            this.mView.setLayerType(0, (Paint)null);
         }
      }

      ShadowDrawableWrapper var1 = this.mShadowDrawable;
      if (var1 != null) {
         var1.setRotation(-this.mRotation);
      }

      CircularBorderDrawable var2 = this.mBorderDrawable;
      if (var2 != null) {
         var2.setRotation(-this.mRotation);
      }
   }

   CircularBorderDrawable createBorderDrawable(int var1, ColorStateList var2) {
      Context var3 = this.mView.getContext();
      CircularBorderDrawable var4 = this.newCircularDrawable();
      var4.setGradientColors(ContextCompat.getColor(var3, R$color.design_fab_stroke_top_outer_color), ContextCompat.getColor(var3, R$color.design_fab_stroke_top_inner_color), ContextCompat.getColor(var3, R$color.design_fab_stroke_end_inner_color), ContextCompat.getColor(var3, R$color.design_fab_stroke_end_outer_color));
      var4.setBorderWidth((float)var1);
      var4.setBorderTint(var2);
      return var4;
   }

   GradientDrawable createShapeDrawable() {
      GradientDrawable var1 = this.newGradientDrawableForShape();
      var1.setShape(1);
      var1.setColor(-1);
      return var1;
   }

   final Drawable getContentBackground() {
      return this.mContentBackground;
   }

   float getElevation() {
      return this.mElevation;
   }

   void getPadding(Rect var1) {
      this.mShadowDrawable.getPadding(var1);
   }

   void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeHidden()) {
         this.mView.animate().cancel();
         if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 1;
            this.mView.animate().scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               private boolean mCancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.mCancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl var3 = FloatingActionButtonImpl.this;
                  var3.mAnimState = 0;
                  if (!this.mCancelled) {
                     VisibilityAwareImageButton var4 = var3.mView;
                     byte var2x;
                     if (var2) {
                        var2x = 8;
                     } else {
                        var2x = 4;
                     }

                     var4.internalSetVisibility(var2x, var2);
                     FloatingActionButtonImpl.InternalVisibilityChangedListener var5 = var1;
                     if (var5 != null) {
                        var5.onHidden();
                     }
                  }
               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.mView.internalSetVisibility(0, var2);
                  this.mCancelled = false;
               }
            });
         } else {
            VisibilityAwareImageButton var4 = this.mView;
            byte var3;
            if (var2) {
               var3 = 8;
            } else {
               var3 = 4;
            }

            var4.internalSetVisibility(var3, var2);
            if (var1 != null) {
               var1.onHidden();
            }
         }
      }
   }

   boolean isOrWillBeHidden() {
      int var1 = this.mView.getVisibility();
      boolean var3 = false;
      boolean var2 = false;
      if (var1 == 0) {
         if (this.mAnimState == 1) {
            var2 = true;
         }

         return var2;
      } else {
         var2 = var3;
         if (this.mAnimState != 2) {
            var2 = true;
         }

         return var2;
      }
   }

   boolean isOrWillBeShown() {
      int var1 = this.mView.getVisibility();
      boolean var3 = false;
      boolean var2 = false;
      if (var1 != 0) {
         if (this.mAnimState == 2) {
            var2 = true;
         }

         return var2;
      } else {
         var2 = var3;
         if (this.mAnimState != 1) {
            var2 = true;
         }

         return var2;
      }
   }

   void jumpDrawableToCurrentState() {
      this.mStateListAnimator.jumpToCurrentState();
   }

   CircularBorderDrawable newCircularDrawable() {
      return new CircularBorderDrawable();
   }

   GradientDrawable newGradientDrawableForShape() {
      return new GradientDrawable();
   }

   void onAttachedToWindow() {
      if (this.requirePreDrawListener()) {
         this.ensurePreDrawListener();
         this.mView.getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
      }
   }

   void onCompatShadowChanged() {
   }

   void onDetachedFromWindow() {
      if (this.mPreDrawListener != null) {
         this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
         this.mPreDrawListener = null;
      }
   }

   void onDrawableStateChanged(int[] var1) {
      this.mStateListAnimator.setState(var1);
   }

   void onElevationsChanged(float var1, float var2) {
      ShadowDrawableWrapper var3 = this.mShadowDrawable;
      if (var3 != null) {
         var3.setShadowSize(var1, this.mPressedTranslationZ + var1);
         this.updatePadding();
      }
   }

   void onPaddingUpdated(Rect var1) {
   }

   void onPreDraw() {
      float var1 = this.mView.getRotation();
      if (this.mRotation != var1) {
         this.mRotation = var1;
         this.updateFromViewRotation();
      }
   }

   boolean requirePreDrawListener() {
      return true;
   }

   void setBackgroundDrawable(ColorStateList var1, Mode var2, int var3, int var4) {
      this.mShapeDrawable = DrawableCompat.wrap(this.createShapeDrawable());
      DrawableCompat.setTintList(this.mShapeDrawable, var1);
      if (var2 != null) {
         DrawableCompat.setTintMode(this.mShapeDrawable, var2);
      }

      this.mRippleDrawable = DrawableCompat.wrap(this.createShapeDrawable());
      DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(var3));
      Drawable[] var7;
      if (var4 > 0) {
         this.mBorderDrawable = this.createBorderDrawable(var4, var1);
         var7 = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
      } else {
         this.mBorderDrawable = null;
         var7 = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
      }

      this.mContentBackground = new LayerDrawable(var7);
      Context var8 = this.mView.getContext();
      Drawable var9 = this.mContentBackground;
      float var5 = this.mShadowViewDelegate.getRadius();
      float var6 = this.mElevation;
      this.mShadowDrawable = new ShadowDrawableWrapper(var8, var9, var5, var6, var6 + this.mPressedTranslationZ);
      this.mShadowDrawable.setAddPaddingForCorners(false);
      this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
   }

   void setBackgroundTintList(ColorStateList var1) {
      Drawable var2 = this.mShapeDrawable;
      if (var2 != null) {
         DrawableCompat.setTintList(var2, var1);
      }

      CircularBorderDrawable var3 = this.mBorderDrawable;
      if (var3 != null) {
         var3.setBorderTint(var1);
      }
   }

   void setBackgroundTintMode(Mode var1) {
      Drawable var2 = this.mShapeDrawable;
      if (var2 != null) {
         DrawableCompat.setTintMode(var2, var1);
      }
   }

   final void setElevation(float var1) {
      if (this.mElevation != var1) {
         this.mElevation = var1;
         this.onElevationsChanged(var1, this.mPressedTranslationZ);
      }
   }

   final void setPressedTranslationZ(float var1) {
      if (this.mPressedTranslationZ != var1) {
         this.mPressedTranslationZ = var1;
         this.onElevationsChanged(this.mElevation, var1);
      }
   }

   void setRippleColor(int var1) {
      Drawable var2 = this.mRippleDrawable;
      if (var2 != null) {
         DrawableCompat.setTintList(var2, createColorStateList(var1));
      }
   }

   void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeShown()) {
         this.mView.animate().cancel();
         if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 2;
            if (this.mView.getVisibility() != 0) {
               this.mView.setAlpha(0.0F);
               this.mView.setScaleY(0.0F);
               this.mView.setScaleX(0.0F);
            }

            this.mView.animate().scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(200L).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl.this.mAnimState = 0;
                  FloatingActionButtonImpl.InternalVisibilityChangedListener var2x = var1;
                  if (var2x != null) {
                     var2x.onShown();
                  }
               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.mView.internalSetVisibility(0, var2);
               }
            });
         } else {
            this.mView.internalSetVisibility(0, var2);
            this.mView.setAlpha(1.0F);
            this.mView.setScaleY(1.0F);
            this.mView.setScaleX(1.0F);
            if (var1 != null) {
               var1.onShown();
            }
         }
      }
   }

   final void updatePadding() {
      Rect var1 = this.mTmpRect;
      this.getPadding(var1);
      this.onPaddingUpdated(var1);
      this.mShadowViewDelegate.setShadowPadding(var1.left, var1.top, var1.right, var1.bottom);
   }

   private class DisabledElevationAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      DisabledElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return 0.0F;
      }
   }

   private class ElevateToTranslationZAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ElevateToTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.mElevation + FloatingActionButtonImpl.this.mPressedTranslationZ;
      }
   }

   interface InternalVisibilityChangedListener {
      void onHidden();

      void onShown();
   }

   private class ResetElevationAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ResetElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.mElevation;
      }
   }

   private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements AnimatorUpdateListener {
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

      public void onAnimationEnd(Animator var1) {
         FloatingActionButtonImpl.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
         this.mValidValues = false;
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         if (!this.mValidValues) {
            this.mShadowSizeStart = FloatingActionButtonImpl.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeEnd = this.getTargetShadowSize();
            this.mValidValues = true;
         }

         ShadowDrawableWrapper var3 = FloatingActionButtonImpl.this.mShadowDrawable;
         float var2 = this.mShadowSizeStart;
         var3.setShadowSize(var2 + (this.mShadowSizeEnd - var2) * var1.getAnimatedFraction());
      }
   }
}
