package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.animator;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
   static final int ANIM_STATE_HIDING = 1;
   static final int ANIM_STATE_NONE = 0;
   static final int ANIM_STATE_SHOWING = 2;
   static final long ELEVATION_ANIM_DELAY = 100L;
   static final long ELEVATION_ANIM_DURATION = 100L;
   static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR;
   static final int[] EMPTY_STATE_SET;
   static final int[] ENABLED_STATE_SET;
   static final int[] FOCUSED_ENABLED_STATE_SET;
   private static final float HIDE_ICON_SCALE = 0.0F;
   private static final float HIDE_OPACITY = 0.0F;
   private static final float HIDE_SCALE = 0.0F;
   static final int[] HOVERED_ENABLED_STATE_SET;
   static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
   static final int[] PRESSED_ENABLED_STATE_SET;
   static final float SHADOW_MULTIPLIER = 1.5F;
   private static final float SHOW_ICON_SCALE = 1.0F;
   private static final float SHOW_OPACITY = 1.0F;
   private static final float SHOW_SCALE = 1.0F;
   private int animState = 0;
   BorderDrawable borderDrawable;
   Drawable contentBackground;
   private Animator currentAnimator;
   private MotionSpec defaultHideMotionSpec;
   private MotionSpec defaultShowMotionSpec;
   float elevation;
   boolean ensureMinTouchTargetSize;
   private ArrayList hideListeners;
   private MotionSpec hideMotionSpec;
   float hoveredFocusedTranslationZ;
   private float imageMatrixScale = 1.0F;
   private int maxImageSize;
   int minTouchTargetSize;
   private OnPreDrawListener preDrawListener;
   float pressedTranslationZ;
   Drawable rippleDrawable;
   private float rotation;
   boolean shadowPaddingEnabled = true;
   final ShadowViewDelegate shadowViewDelegate;
   ShapeAppearanceModel shapeAppearance;
   MaterialShapeDrawable shapeDrawable;
   private ArrayList showListeners;
   private MotionSpec showMotionSpec;
   private final StateListAnimator stateListAnimator;
   private final Matrix tmpMatrix = new Matrix();
   private final Rect tmpRect = new Rect();
   private final RectF tmpRectF1 = new RectF();
   private final RectF tmpRectF2 = new RectF();
   private ArrayList transformationCallbacks;
   final FloatingActionButton view;

   static {
      ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
      PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
      HOVERED_FOCUSED_ENABLED_STATE_SET = new int[]{16843623, 16842908, 16842910};
      FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
      HOVERED_ENABLED_STATE_SET = new int[]{16843623, 16842910};
      ENABLED_STATE_SET = new int[]{16842910};
      EMPTY_STATE_SET = new int[0];
   }

   FloatingActionButtonImpl(FloatingActionButton var1, ShadowViewDelegate var2) {
      this.view = var1;
      this.shadowViewDelegate = var2;
      StateListAnimator var3 = new StateListAnimator();
      this.stateListAnimator = var3;
      var3.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToPressedTranslationZAnimation()));
      this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ElevateToHoveredFocusedTranslationZAnimation()));
      this.stateListAnimator.addState(ENABLED_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.ResetElevationAnimation()));
      this.stateListAnimator.addState(EMPTY_STATE_SET, this.createElevationAnimator(new FloatingActionButtonImpl.DisabledElevationAnimation()));
      this.rotation = this.view.getRotation();
   }

   private void calculateImageMatrixFromScale(float var1, Matrix var2) {
      var2.reset();
      Drawable var4 = this.view.getDrawable();
      if (var4 != null && this.maxImageSize != 0) {
         RectF var5 = this.tmpRectF1;
         RectF var6 = this.tmpRectF2;
         var5.set(0.0F, 0.0F, (float)var4.getIntrinsicWidth(), (float)var4.getIntrinsicHeight());
         int var3 = this.maxImageSize;
         var6.set(0.0F, 0.0F, (float)var3, (float)var3);
         var2.setRectToRect(var5, var6, ScaleToFit.CENTER);
         var3 = this.maxImageSize;
         var2.postScale(var1, var1, (float)var3 / 2.0F, (float)var3 / 2.0F);
      }

   }

   private AnimatorSet createAnimator(MotionSpec var1, float var2, float var3, float var4) {
      ArrayList var5 = new ArrayList();
      ObjectAnimator var6 = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[]{var2});
      var1.getTiming("opacity").apply(var6);
      var5.add(var6);
      var6 = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[]{var3});
      var1.getTiming("scale").apply(var6);
      this.workAroundOreoBug(var6);
      var5.add(var6);
      var6 = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[]{var3});
      var1.getTiming("scale").apply(var6);
      this.workAroundOreoBug(var6);
      var5.add(var6);
      this.calculateImageMatrixFromScale(var4, this.tmpMatrix);
      var6 = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator() {
         public Matrix evaluate(float var1, Matrix var2, Matrix var3) {
            FloatingActionButtonImpl.this.imageMatrixScale = var1;
            return super.evaluate(var1, var2, var3);
         }
      }, new Matrix[]{new Matrix(this.tmpMatrix)});
      var1.getTiming("iconScale").apply(var6);
      var5.add(var6);
      AnimatorSet var7 = new AnimatorSet();
      AnimatorSetCompat.playTogether(var7, var5);
      return var7;
   }

   private ValueAnimator createElevationAnimator(FloatingActionButtonImpl.ShadowAnimatorImpl var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      var2.setDuration(100L);
      var2.addListener(var1);
      var2.addUpdateListener(var1);
      var2.setFloatValues(new float[]{0.0F, 1.0F});
      return var2;
   }

   private MotionSpec getDefaultHideMotionSpec() {
      if (this.defaultHideMotionSpec == null) {
         this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), animator.design_fab_hide_motion_spec);
      }

      return (MotionSpec)Preconditions.checkNotNull(this.defaultHideMotionSpec);
   }

   private MotionSpec getDefaultShowMotionSpec() {
      if (this.defaultShowMotionSpec == null) {
         this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), animator.design_fab_show_motion_spec);
      }

      return (MotionSpec)Preconditions.checkNotNull(this.defaultShowMotionSpec);
   }

   private OnPreDrawListener getOrCreatePreDrawListener() {
      if (this.preDrawListener == null) {
         this.preDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
               FloatingActionButtonImpl.this.onPreDraw();
               return true;
            }
         };
      }

      return this.preDrawListener;
   }

   private boolean shouldAnimateVisibilityChange() {
      return ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode();
   }

   private void workAroundOreoBug(ObjectAnimator var1) {
      if (VERSION.SDK_INT == 26) {
         var1.setEvaluator(new TypeEvaluator() {
            FloatEvaluator floatEvaluator = new FloatEvaluator();

            public Float evaluate(float var1, Float var2, Float var3) {
               var1 = this.floatEvaluator.evaluate(var1, var2, var3);
               if (var1 < 0.1F) {
                  var1 = 0.0F;
               }

               return var1;
            }
         });
      }
   }

   public void addOnHideAnimationListener(AnimatorListener var1) {
      if (this.hideListeners == null) {
         this.hideListeners = new ArrayList();
      }

      this.hideListeners.add(var1);
   }

   void addOnShowAnimationListener(AnimatorListener var1) {
      if (this.showListeners == null) {
         this.showListeners = new ArrayList();
      }

      this.showListeners.add(var1);
   }

   void addTransformationCallback(FloatingActionButtonImpl.InternalTransformationCallback var1) {
      if (this.transformationCallbacks == null) {
         this.transformationCallbacks = new ArrayList();
      }

      this.transformationCallbacks.add(var1);
   }

   MaterialShapeDrawable createShapeDrawable() {
      return new MaterialShapeDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
   }

   final Drawable getContentBackground() {
      return this.contentBackground;
   }

   float getElevation() {
      return this.elevation;
   }

   boolean getEnsureMinTouchTargetSize() {
      return this.ensureMinTouchTargetSize;
   }

   final MotionSpec getHideMotionSpec() {
      return this.hideMotionSpec;
   }

   float getHoveredFocusedTranslationZ() {
      return this.hoveredFocusedTranslationZ;
   }

   void getPadding(Rect var1) {
      int var3;
      if (this.ensureMinTouchTargetSize) {
         var3 = (this.minTouchTargetSize - this.view.getSizeDimension()) / 2;
      } else {
         var3 = 0;
      }

      float var2;
      if (this.shadowPaddingEnabled) {
         var2 = this.getElevation() + this.pressedTranslationZ;
      } else {
         var2 = 0.0F;
      }

      int var4 = Math.max(var3, (int)Math.ceil((double)var2));
      var3 = Math.max(var3, (int)Math.ceil((double)(1.5F * var2)));
      var1.set(var4, var3, var4, var3);
   }

   float getPressedTranslationZ() {
      return this.pressedTranslationZ;
   }

   final ShapeAppearanceModel getShapeAppearance() {
      return this.shapeAppearance;
   }

   final MotionSpec getShowMotionSpec() {
      return this.showMotionSpec;
   }

   void hide(final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeHidden()) {
         Animator var4 = this.currentAnimator;
         if (var4 != null) {
            var4.cancel();
         }

         if (this.shouldAnimateVisibilityChange()) {
            MotionSpec var8 = this.hideMotionSpec;
            if (var8 == null) {
               var8 = this.getDefaultHideMotionSpec();
            }

            AnimatorSet var9 = this.createAnimator(var8, 0.0F, 0.0F, 0.0F);
            var9.addListener(new AnimatorListenerAdapter() {
               private boolean cancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.cancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl.this.animState = 0;
                  FloatingActionButtonImpl.this.currentAnimator = null;
                  if (!this.cancelled) {
                     FloatingActionButton var3 = FloatingActionButtonImpl.this.view;
                     byte var2x;
                     if (var2) {
                        var2x = 8;
                     } else {
                        var2x = 4;
                     }

                     var3.internalSetVisibility(var2x, var2);
                     FloatingActionButtonImpl.InternalVisibilityChangedListener var4 = var1;
                     if (var4 != null) {
                        var4.onHidden();
                     }
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.view.internalSetVisibility(0, var2);
                  FloatingActionButtonImpl.this.animState = 1;
                  FloatingActionButtonImpl.this.currentAnimator = var1x;
                  this.cancelled = false;
               }
            });
            ArrayList var5 = this.hideListeners;
            if (var5 != null) {
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  var9.addListener((AnimatorListener)var6.next());
               }
            }

            var9.start();
         } else {
            FloatingActionButton var7 = this.view;
            byte var3;
            if (var2) {
               var3 = 8;
            } else {
               var3 = 4;
            }

            var7.internalSetVisibility(var3, var2);
            if (var1 != null) {
               var1.onHidden();
            }

         }
      }
   }

   void initializeBackgroundDrawable(ColorStateList var1, Mode var2, ColorStateList var3, int var4) {
      MaterialShapeDrawable var5 = this.createShapeDrawable();
      this.shapeDrawable = var5;
      var5.setTintList(var1);
      if (var2 != null) {
         this.shapeDrawable.setTintMode(var2);
      }

      this.shapeDrawable.setShadowColor(-12303292);
      this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
      RippleDrawableCompat var6 = new RippleDrawableCompat(this.shapeDrawable.getShapeAppearanceModel());
      var6.setTintList(RippleUtils.sanitizeRippleDrawableColor(var3));
      this.rippleDrawable = var6;
      this.contentBackground = new LayerDrawable(new Drawable[]{(Drawable)Preconditions.checkNotNull(this.shapeDrawable), var6});
   }

   boolean isOrWillBeHidden() {
      int var1 = this.view.getVisibility();
      boolean var3 = false;
      boolean var2 = false;
      if (var1 == 0) {
         if (this.animState == 1) {
            var2 = true;
         }

         return var2;
      } else {
         var2 = var3;
         if (this.animState != 2) {
            var2 = true;
         }

         return var2;
      }
   }

   boolean isOrWillBeShown() {
      int var1 = this.view.getVisibility();
      boolean var3 = false;
      boolean var2 = false;
      if (var1 != 0) {
         if (this.animState == 2) {
            var2 = true;
         }

         return var2;
      } else {
         var2 = var3;
         if (this.animState != 1) {
            var2 = true;
         }

         return var2;
      }
   }

   void jumpDrawableToCurrentState() {
      this.stateListAnimator.jumpToCurrentState();
   }

   void onAttachedToWindow() {
      MaterialShapeDrawable var1 = this.shapeDrawable;
      if (var1 != null) {
         MaterialShapeUtils.setParentAbsoluteElevation(this.view, var1);
      }

      if (this.requirePreDrawListener()) {
         this.view.getViewTreeObserver().addOnPreDrawListener(this.getOrCreatePreDrawListener());
      }

   }

   void onCompatShadowChanged() {
   }

   void onDetachedFromWindow() {
      ViewTreeObserver var1 = this.view.getViewTreeObserver();
      OnPreDrawListener var2 = this.preDrawListener;
      if (var2 != null) {
         var1.removeOnPreDrawListener(var2);
         this.preDrawListener = null;
      }

   }

   void onDrawableStateChanged(int[] var1) {
      this.stateListAnimator.setState(var1);
   }

   void onElevationsChanged(float var1, float var2, float var3) {
      this.updatePadding();
      this.updateShapeElevation(var1);
   }

   void onPaddingUpdated(Rect var1) {
      Preconditions.checkNotNull(this.contentBackground, "Didn't initialize content background");
      if (this.shouldAddPadding()) {
         InsetDrawable var2 = new InsetDrawable(this.contentBackground, var1.left, var1.top, var1.right, var1.bottom);
         this.shadowViewDelegate.setBackgroundDrawable(var2);
      } else {
         this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
      }
   }

   void onPreDraw() {
      float var1 = this.view.getRotation();
      if (this.rotation != var1) {
         this.rotation = var1;
         this.updateFromViewRotation();
      }

   }

   void onScaleChanged() {
      ArrayList var1 = this.transformationCallbacks;
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ((FloatingActionButtonImpl.InternalTransformationCallback)var2.next()).onScaleChanged();
         }
      }

   }

   void onTranslationChanged() {
      ArrayList var1 = this.transformationCallbacks;
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ((FloatingActionButtonImpl.InternalTransformationCallback)var2.next()).onTranslationChanged();
         }
      }

   }

   public void removeOnHideAnimationListener(AnimatorListener var1) {
      ArrayList var2 = this.hideListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   void removeOnShowAnimationListener(AnimatorListener var1) {
      ArrayList var2 = this.showListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   void removeTransformationCallback(FloatingActionButtonImpl.InternalTransformationCallback var1) {
      ArrayList var2 = this.transformationCallbacks;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   boolean requirePreDrawListener() {
      return true;
   }

   void setBackgroundTintList(ColorStateList var1) {
      MaterialShapeDrawable var2 = this.shapeDrawable;
      if (var2 != null) {
         var2.setTintList(var1);
      }

      BorderDrawable var3 = this.borderDrawable;
      if (var3 != null) {
         var3.setBorderTint(var1);
      }

   }

   void setBackgroundTintMode(Mode var1) {
      MaterialShapeDrawable var2 = this.shapeDrawable;
      if (var2 != null) {
         var2.setTintMode(var1);
      }

   }

   final void setElevation(float var1) {
      if (this.elevation != var1) {
         this.elevation = var1;
         this.onElevationsChanged(var1, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
      }

   }

   void setEnsureMinTouchTargetSize(boolean var1) {
      this.ensureMinTouchTargetSize = var1;
   }

   final void setHideMotionSpec(MotionSpec var1) {
      this.hideMotionSpec = var1;
   }

   final void setHoveredFocusedTranslationZ(float var1) {
      if (this.hoveredFocusedTranslationZ != var1) {
         this.hoveredFocusedTranslationZ = var1;
         this.onElevationsChanged(this.elevation, var1, this.pressedTranslationZ);
      }

   }

   final void setImageMatrixScale(float var1) {
      this.imageMatrixScale = var1;
      Matrix var2 = this.tmpMatrix;
      this.calculateImageMatrixFromScale(var1, var2);
      this.view.setImageMatrix(var2);
   }

   final void setMaxImageSize(int var1) {
      if (this.maxImageSize != var1) {
         this.maxImageSize = var1;
         this.updateImageMatrixScale();
      }

   }

   void setMinTouchTargetSize(int var1) {
      this.minTouchTargetSize = var1;
   }

   final void setPressedTranslationZ(float var1) {
      if (this.pressedTranslationZ != var1) {
         this.pressedTranslationZ = var1;
         this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, var1);
      }

   }

   void setRippleColor(ColorStateList var1) {
      Drawable var2 = this.rippleDrawable;
      if (var2 != null) {
         DrawableCompat.setTintList(var2, RippleUtils.sanitizeRippleDrawableColor(var1));
      }

   }

   void setShadowPaddingEnabled(boolean var1) {
      this.shadowPaddingEnabled = var1;
      this.updatePadding();
   }

   final void setShapeAppearance(ShapeAppearanceModel var1) {
      this.shapeAppearance = var1;
      MaterialShapeDrawable var2 = this.shapeDrawable;
      if (var2 != null) {
         var2.setShapeAppearanceModel(var1);
      }

      Drawable var3 = this.rippleDrawable;
      if (var3 instanceof Shapeable) {
         ((Shapeable)var3).setShapeAppearanceModel(var1);
      }

      BorderDrawable var4 = this.borderDrawable;
      if (var4 != null) {
         var4.setShapeAppearanceModel(var1);
      }

   }

   final void setShowMotionSpec(MotionSpec var1) {
      this.showMotionSpec = var1;
   }

   boolean shouldAddPadding() {
      return true;
   }

   final boolean shouldExpandBoundsForA11y() {
      return !this.ensureMinTouchTargetSize || this.view.getSizeDimension() >= this.minTouchTargetSize;
   }

   void show(final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeShown()) {
         Animator var3 = this.currentAnimator;
         if (var3 != null) {
            var3.cancel();
         }

         if (!this.shouldAnimateVisibilityChange()) {
            this.view.internalSetVisibility(0, var2);
            this.view.setAlpha(1.0F);
            this.view.setScaleY(1.0F);
            this.view.setScaleX(1.0F);
            this.setImageMatrixScale(1.0F);
            if (var1 != null) {
               var1.onShown();
            }

         } else {
            if (this.view.getVisibility() != 0) {
               this.view.setAlpha(0.0F);
               this.view.setScaleY(0.0F);
               this.view.setScaleX(0.0F);
               this.setImageMatrixScale(0.0F);
            }

            MotionSpec var6 = this.showMotionSpec;
            if (var6 == null) {
               var6 = this.getDefaultShowMotionSpec();
            }

            AnimatorSet var7 = this.createAnimator(var6, 1.0F, 1.0F, 1.0F);
            var7.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonImpl.this.animState = 0;
                  FloatingActionButtonImpl.this.currentAnimator = null;
                  FloatingActionButtonImpl.InternalVisibilityChangedListener var2x = var1;
                  if (var2x != null) {
                     var2x.onShown();
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonImpl.this.view.internalSetVisibility(0, var2);
                  FloatingActionButtonImpl.this.animState = 2;
                  FloatingActionButtonImpl.this.currentAnimator = var1x;
               }
            });
            ArrayList var4 = this.showListeners;
            if (var4 != null) {
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  var7.addListener((AnimatorListener)var5.next());
               }
            }

            var7.start();
         }
      }
   }

   void updateFromViewRotation() {
      if (VERSION.SDK_INT == 19) {
         if (this.rotation % 90.0F != 0.0F) {
            if (this.view.getLayerType() != 1) {
               this.view.setLayerType(1, (Paint)null);
            }
         } else if (this.view.getLayerType() != 0) {
            this.view.setLayerType(0, (Paint)null);
         }
      }

      MaterialShapeDrawable var1 = this.shapeDrawable;
      if (var1 != null) {
         var1.setShadowCompatRotation((int)this.rotation);
      }

   }

   final void updateImageMatrixScale() {
      this.setImageMatrixScale(this.imageMatrixScale);
   }

   final void updatePadding() {
      Rect var1 = this.tmpRect;
      this.getPadding(var1);
      this.onPaddingUpdated(var1);
      this.shadowViewDelegate.setShadowPadding(var1.left, var1.top, var1.right, var1.bottom);
   }

   void updateShapeElevation(float var1) {
      MaterialShapeDrawable var2 = this.shapeDrawable;
      if (var2 != null) {
         var2.setElevation(var1);
      }

   }

   private class DisabledElevationAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      DisabledElevationAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return 0.0F;
      }
   }

   private class ElevateToHoveredFocusedTranslationZAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ElevateToHoveredFocusedTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
      }
   }

   private class ElevateToPressedTranslationZAnimation extends FloatingActionButtonImpl.ShadowAnimatorImpl {
      ElevateToPressedTranslationZAnimation() {
         super(null);
      }

      protected float getTargetShadowSize() {
         return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
      }
   }

   interface InternalTransformationCallback {
      void onScaleChanged();

      void onTranslationChanged();
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
         return FloatingActionButtonImpl.this.elevation;
      }
   }

   private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements AnimatorUpdateListener {
      private float shadowSizeEnd;
      private float shadowSizeStart;
      private boolean validValues;

      private ShadowAnimatorImpl() {
      }

      // $FF: synthetic method
      ShadowAnimatorImpl(Object var2) {
         this();
      }

      protected abstract float getTargetShadowSize();

      public void onAnimationEnd(Animator var1) {
         FloatingActionButtonImpl.this.updateShapeElevation((float)((int)this.shadowSizeEnd));
         this.validValues = false;
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         float var2;
         if (!this.validValues) {
            if (FloatingActionButtonImpl.this.shapeDrawable == null) {
               var2 = 0.0F;
            } else {
               var2 = FloatingActionButtonImpl.this.shapeDrawable.getElevation();
            }

            this.shadowSizeStart = var2;
            this.shadowSizeEnd = this.getTargetShadowSize();
            this.validValues = true;
         }

         FloatingActionButtonImpl var3 = FloatingActionButtonImpl.this;
         var2 = this.shadowSizeStart;
         var3.updateShapeElevation((float)((int)(var2 + (this.shadowSizeEnd - var2) * var1.getAnimatedFraction())));
      }
   }
}
