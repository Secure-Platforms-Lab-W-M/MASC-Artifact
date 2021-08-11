package com.google.android.material.floatingactionbutton;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TintableImageSourceView;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.expandable.ExpandableTransformationWidget;
import com.google.android.material.expandable.ExpandableWidgetHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.stateful.ExtendableSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton implements TintableBackgroundView, TintableImageSourceView, ExpandableTransformationWidget, Shapeable {
   private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
   private static final int DEF_STYLE_RES;
   private static final String EXPANDABLE_WIDGET_HELPER_KEY = "expandableWidgetHelper";
   private static final String LOG_TAG = "FloatingActionButton";
   public static final int NO_CUSTOM_SIZE = 0;
   public static final int SIZE_AUTO = -1;
   public static final int SIZE_MINI = 1;
   public static final int SIZE_NORMAL = 0;
   private ColorStateList backgroundTint;
   private Mode backgroundTintMode;
   private int borderWidth;
   boolean compatPadding;
   private int customSize;
   private final ExpandableWidgetHelper expandableWidgetHelper;
   private final AppCompatImageHelper imageHelper;
   private Mode imageMode;
   private int imagePadding;
   private ColorStateList imageTint;
   private FloatingActionButtonImpl impl;
   private int maxImageSize;
   private ColorStateList rippleColor;
   final Rect shadowPadding;
   private int size;
   private final Rect touchArea;

   static {
      DEF_STYLE_RES = style.Widget_Design_FloatingActionButton;
   }

   public FloatingActionButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public FloatingActionButton(Context var1, AttributeSet var2) {
      this(var1, var2, attr.floatingActionButtonStyle);
   }

   public FloatingActionButton(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.shadowPadding = new Rect();
      this.touchArea = new Rect();
      Context var11 = this.getContext();
      TypedArray var10 = ThemeEnforcement.obtainStyledAttributes(var11, var2, styleable.FloatingActionButton, var3, DEF_STYLE_RES);
      this.backgroundTint = MaterialResources.getColorStateList(var11, var10, styleable.FloatingActionButton_backgroundTint);
      this.backgroundTintMode = ViewUtils.parseTintMode(var10.getInt(styleable.FloatingActionButton_backgroundTintMode, -1), (Mode)null);
      this.rippleColor = MaterialResources.getColorStateList(var11, var10, styleable.FloatingActionButton_rippleColor);
      this.size = var10.getInt(styleable.FloatingActionButton_fabSize, -1);
      this.customSize = var10.getDimensionPixelSize(styleable.FloatingActionButton_fabCustomSize, 0);
      this.borderWidth = var10.getDimensionPixelSize(styleable.FloatingActionButton_borderWidth, 0);
      float var4 = var10.getDimension(styleable.FloatingActionButton_elevation, 0.0F);
      float var5 = var10.getDimension(styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0F);
      float var6 = var10.getDimension(styleable.FloatingActionButton_pressedTranslationZ, 0.0F);
      this.compatPadding = var10.getBoolean(styleable.FloatingActionButton_useCompatPadding, false);
      int var7 = this.getResources().getDimensionPixelSize(dimen.mtrl_fab_min_touch_target);
      this.maxImageSize = var10.getDimensionPixelSize(styleable.FloatingActionButton_maxImageSize, 0);
      MotionSpec var14 = MotionSpec.createFromAttribute(var11, var10, styleable.FloatingActionButton_showMotionSpec);
      MotionSpec var9 = MotionSpec.createFromAttribute(var11, var10, styleable.FloatingActionButton_hideMotionSpec);
      ShapeAppearanceModel var13 = ShapeAppearanceModel.builder(var11, var2, var3, DEF_STYLE_RES, ShapeAppearanceModel.PILL).build();
      boolean var8 = var10.getBoolean(styleable.FloatingActionButton_ensureMinTouchTargetSize, false);
      var10.recycle();
      AppCompatImageHelper var12 = new AppCompatImageHelper(this);
      this.imageHelper = var12;
      var12.loadFromAttributes(var2, var3);
      this.expandableWidgetHelper = new ExpandableWidgetHelper(this);
      this.getImpl().setShapeAppearance(var13);
      this.getImpl().initializeBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
      this.getImpl().setMinTouchTargetSize(var7);
      this.getImpl().setElevation(var4);
      this.getImpl().setHoveredFocusedTranslationZ(var5);
      this.getImpl().setPressedTranslationZ(var6);
      this.getImpl().setMaxImageSize(this.maxImageSize);
      this.getImpl().setShowMotionSpec(var14);
      this.getImpl().setHideMotionSpec(var9);
      this.getImpl().setEnsureMinTouchTargetSize(var8);
      this.setScaleType(ScaleType.MATRIX);
   }

   private FloatingActionButtonImpl createImpl() {
      return (FloatingActionButtonImpl)(VERSION.SDK_INT >= 21 ? new FloatingActionButtonImplLollipop(this, new FloatingActionButton.ShadowDelegateImpl()) : new FloatingActionButtonImpl(this, new FloatingActionButton.ShadowDelegateImpl()));
   }

   private FloatingActionButtonImpl getImpl() {
      if (this.impl == null) {
         this.impl = this.createImpl();
      }

      return this.impl;
   }

   private int getSizeDimension(int var1) {
      int var2 = this.customSize;
      if (var2 != 0) {
         return var2;
      } else {
         Resources var3 = this.getResources();
         if (var1 != -1) {
            return var1 != 1 ? var3.getDimensionPixelSize(dimen.design_fab_size_normal) : var3.getDimensionPixelSize(dimen.design_fab_size_mini);
         } else {
            return Math.max(var3.getConfiguration().screenWidthDp, var3.getConfiguration().screenHeightDp) < 470 ? this.getSizeDimension(1) : this.getSizeDimension(0);
         }
      }
   }

   private void offsetRectWithShadow(Rect var1) {
      var1.left += this.shadowPadding.left;
      var1.top += this.shadowPadding.top;
      var1.right -= this.shadowPadding.right;
      var1.bottom -= this.shadowPadding.bottom;
   }

   private void onApplySupportImageTint() {
      Drawable var4 = this.getDrawable();
      if (var4 != null) {
         ColorStateList var2 = this.imageTint;
         if (var2 == null) {
            DrawableCompat.clearColorFilter(var4);
         } else {
            int var1 = var2.getColorForState(this.getDrawableState(), 0);
            Mode var3 = this.imageMode;
            Mode var5 = var3;
            if (var3 == null) {
               var5 = Mode.SRC_IN;
            }

            var4.mutate().setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var1, var5));
         }
      }
   }

   private static int resolveAdjustedSize(int var0, int var1) {
      int var2 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      if (var2 != Integer.MIN_VALUE) {
         if (var2 != 0) {
            if (var2 == 1073741824) {
               return var1;
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            return var0;
         }
      } else {
         return Math.min(var0, var1);
      }
   }

   private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final FloatingActionButton.OnVisibilityChangedListener var1) {
      return var1 == null ? null : new FloatingActionButtonImpl.InternalVisibilityChangedListener() {
         public void onHidden() {
            var1.onHidden(FloatingActionButton.this);
         }

         public void onShown() {
            var1.onShown(FloatingActionButton.this);
         }
      };
   }

   public void addOnHideAnimationListener(AnimatorListener var1) {
      this.getImpl().addOnHideAnimationListener(var1);
   }

   public void addOnShowAnimationListener(AnimatorListener var1) {
      this.getImpl().addOnShowAnimationListener(var1);
   }

   public void addTransformationCallback(TransformationCallback var1) {
      this.getImpl().addTransformationCallback(new FloatingActionButton.TransformationCallbackWrapper(var1));
   }

   public void clearCustomSize() {
      this.setCustomSize(0);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.getImpl().onDrawableStateChanged(this.getDrawableState());
   }

   public ColorStateList getBackgroundTintList() {
      return this.backgroundTint;
   }

   public Mode getBackgroundTintMode() {
      return this.backgroundTintMode;
   }

   public float getCompatElevation() {
      return this.getImpl().getElevation();
   }

   public float getCompatHoveredFocusedTranslationZ() {
      return this.getImpl().getHoveredFocusedTranslationZ();
   }

   public float getCompatPressedTranslationZ() {
      return this.getImpl().getPressedTranslationZ();
   }

   public Drawable getContentBackground() {
      return this.getImpl().getContentBackground();
   }

   @Deprecated
   public boolean getContentRect(Rect var1) {
      if (ViewCompat.isLaidOut(this)) {
         var1.set(0, 0, this.getWidth(), this.getHeight());
         this.offsetRectWithShadow(var1);
         return true;
      } else {
         return false;
      }
   }

   public int getCustomSize() {
      return this.customSize;
   }

   public int getExpandedComponentIdHint() {
      return this.expandableWidgetHelper.getExpandedComponentIdHint();
   }

   public MotionSpec getHideMotionSpec() {
      return this.getImpl().getHideMotionSpec();
   }

   public void getMeasuredContentRect(Rect var1) {
      var1.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
      this.offsetRectWithShadow(var1);
   }

   @Deprecated
   public int getRippleColor() {
      ColorStateList var1 = this.rippleColor;
      return var1 != null ? var1.getDefaultColor() : 0;
   }

   public ColorStateList getRippleColorStateList() {
      return this.rippleColor;
   }

   public ShapeAppearanceModel getShapeAppearanceModel() {
      return (ShapeAppearanceModel)Preconditions.checkNotNull(this.getImpl().getShapeAppearance());
   }

   public MotionSpec getShowMotionSpec() {
      return this.getImpl().getShowMotionSpec();
   }

   public int getSize() {
      return this.size;
   }

   int getSizeDimension() {
      return this.getSizeDimension(this.size);
   }

   public ColorStateList getSupportBackgroundTintList() {
      return this.getBackgroundTintList();
   }

   public Mode getSupportBackgroundTintMode() {
      return this.getBackgroundTintMode();
   }

   public ColorStateList getSupportImageTintList() {
      return this.imageTint;
   }

   public Mode getSupportImageTintMode() {
      return this.imageMode;
   }

   public boolean getUseCompatPadding() {
      return this.compatPadding;
   }

   public void hide() {
      this.hide((FloatingActionButton.OnVisibilityChangedListener)null);
   }

   public void hide(FloatingActionButton.OnVisibilityChangedListener var1) {
      this.hide(var1, true);
   }

   void hide(FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().hide(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   public boolean isExpanded() {
      return this.expandableWidgetHelper.isExpanded();
   }

   public boolean isOrWillBeHidden() {
      return this.getImpl().isOrWillBeHidden();
   }

   public boolean isOrWillBeShown() {
      return this.getImpl().isOrWillBeShown();
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      this.getImpl().jumpDrawableToCurrentState();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.getImpl().onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.getImpl().onDetachedFromWindow();
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.getSizeDimension();
      this.imagePadding = (var3 - this.maxImageSize) / 2;
      this.getImpl().updatePadding();
      var1 = Math.min(resolveAdjustedSize(var3, var1), resolveAdjustedSize(var3, var2));
      this.setMeasuredDimension(this.shadowPadding.left + var1 + this.shadowPadding.right, this.shadowPadding.top + var1 + this.shadowPadding.bottom);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof ExtendableSavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         ExtendableSavedState var2 = (ExtendableSavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.expandableWidgetHelper.onRestoreInstanceState((Bundle)Preconditions.checkNotNull(var2.extendableStates.get("expandableWidgetHelper")));
      }
   }

   protected Parcelable onSaveInstanceState() {
      Parcelable var2 = super.onSaveInstanceState();
      Object var1 = var2;
      if (var2 == null) {
         var1 = new Bundle();
      }

      ExtendableSavedState var3 = new ExtendableSavedState((Parcelable)var1);
      var3.extendableStates.put("expandableWidgetHelper", this.expandableWidgetHelper.onSaveInstanceState());
      return var3;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return var1.getAction() == 0 && this.getContentRect(this.touchArea) && !this.touchArea.contains((int)var1.getX(), (int)var1.getY()) ? false : super.onTouchEvent(var1);
   }

   public void removeOnHideAnimationListener(AnimatorListener var1) {
      this.getImpl().removeOnHideAnimationListener(var1);
   }

   public void removeOnShowAnimationListener(AnimatorListener var1) {
      this.getImpl().removeOnShowAnimationListener(var1);
   }

   public void removeTransformationCallback(TransformationCallback var1) {
      this.getImpl().removeTransformationCallback(new FloatingActionButton.TransformationCallbackWrapper(var1));
   }

   public void setBackgroundColor(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundDrawable(Drawable var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundResource(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundTintList(ColorStateList var1) {
      if (this.backgroundTint != var1) {
         this.backgroundTint = var1;
         this.getImpl().setBackgroundTintList(var1);
      }

   }

   public void setBackgroundTintMode(Mode var1) {
      if (this.backgroundTintMode != var1) {
         this.backgroundTintMode = var1;
         this.getImpl().setBackgroundTintMode(var1);
      }

   }

   public void setCompatElevation(float var1) {
      this.getImpl().setElevation(var1);
   }

   public void setCompatElevationResource(int var1) {
      this.setCompatElevation(this.getResources().getDimension(var1));
   }

   public void setCompatHoveredFocusedTranslationZ(float var1) {
      this.getImpl().setHoveredFocusedTranslationZ(var1);
   }

   public void setCompatHoveredFocusedTranslationZResource(int var1) {
      this.setCompatHoveredFocusedTranslationZ(this.getResources().getDimension(var1));
   }

   public void setCompatPressedTranslationZ(float var1) {
      this.getImpl().setPressedTranslationZ(var1);
   }

   public void setCompatPressedTranslationZResource(int var1) {
      this.setCompatPressedTranslationZ(this.getResources().getDimension(var1));
   }

   public void setCustomSize(int var1) {
      if (var1 >= 0) {
         if (var1 != this.customSize) {
            this.customSize = var1;
            this.requestLayout();
         }

      } else {
         throw new IllegalArgumentException("Custom size must be non-negative");
      }
   }

   public void setElevation(float var1) {
      super.setElevation(var1);
      this.getImpl().updateShapeElevation(var1);
   }

   public void setEnsureMinTouchTargetSize(boolean var1) {
      if (var1 != this.getImpl().getEnsureMinTouchTargetSize()) {
         this.getImpl().setEnsureMinTouchTargetSize(var1);
         this.requestLayout();
      }

   }

   public boolean setExpanded(boolean var1) {
      return this.expandableWidgetHelper.setExpanded(var1);
   }

   public void setExpandedComponentIdHint(int var1) {
      this.expandableWidgetHelper.setExpandedComponentIdHint(var1);
   }

   public void setHideMotionSpec(MotionSpec var1) {
      this.getImpl().setHideMotionSpec(var1);
   }

   public void setHideMotionSpecResource(int var1) {
      this.setHideMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setImageDrawable(Drawable var1) {
      if (this.getDrawable() != var1) {
         super.setImageDrawable(var1);
         this.getImpl().updateImageMatrixScale();
         if (this.imageTint != null) {
            this.onApplySupportImageTint();
         }
      }

   }

   public void setImageResource(int var1) {
      this.imageHelper.setImageResource(var1);
      this.onApplySupportImageTint();
   }

   public void setRippleColor(int var1) {
      this.setRippleColor(ColorStateList.valueOf(var1));
   }

   public void setRippleColor(ColorStateList var1) {
      if (this.rippleColor != var1) {
         this.rippleColor = var1;
         this.getImpl().setRippleColor(this.rippleColor);
      }

   }

   public void setScaleX(float var1) {
      super.setScaleX(var1);
      this.getImpl().onScaleChanged();
   }

   public void setScaleY(float var1) {
      super.setScaleY(var1);
      this.getImpl().onScaleChanged();
   }

   public void setShadowPaddingEnabled(boolean var1) {
      this.getImpl().setShadowPaddingEnabled(var1);
   }

   public void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      this.getImpl().setShapeAppearance(var1);
   }

   public void setShowMotionSpec(MotionSpec var1) {
      this.getImpl().setShowMotionSpec(var1);
   }

   public void setShowMotionSpecResource(int var1) {
      this.setShowMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setSize(int var1) {
      this.customSize = 0;
      if (var1 != this.size) {
         this.size = var1;
         this.requestLayout();
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      this.setBackgroundTintList(var1);
   }

   public void setSupportBackgroundTintMode(Mode var1) {
      this.setBackgroundTintMode(var1);
   }

   public void setSupportImageTintList(ColorStateList var1) {
      if (this.imageTint != var1) {
         this.imageTint = var1;
         this.onApplySupportImageTint();
      }

   }

   public void setSupportImageTintMode(Mode var1) {
      if (this.imageMode != var1) {
         this.imageMode = var1;
         this.onApplySupportImageTint();
      }

   }

   public void setTranslationX(float var1) {
      super.setTranslationX(var1);
      this.getImpl().onTranslationChanged();
   }

   public void setTranslationY(float var1) {
      super.setTranslationY(var1);
      this.getImpl().onTranslationChanged();
   }

   public void setTranslationZ(float var1) {
      super.setTranslationZ(var1);
      this.getImpl().onTranslationChanged();
   }

   public void setUseCompatPadding(boolean var1) {
      if (this.compatPadding != var1) {
         this.compatPadding = var1;
         this.getImpl().onCompatShadowChanged();
      }

   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
   }

   public boolean shouldEnsureMinTouchTargetSize() {
      return this.getImpl().getEnsureMinTouchTargetSize();
   }

   public void show() {
      this.show((FloatingActionButton.OnVisibilityChangedListener)null);
   }

   public void show(FloatingActionButton.OnVisibilityChangedListener var1) {
      this.show(var1, true);
   }

   void show(FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().show(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   protected static class BaseBehavior extends CoordinatorLayout.Behavior {
      private static final boolean AUTO_HIDE_DEFAULT = true;
      private boolean autoHideEnabled;
      private FloatingActionButton.OnVisibilityChangedListener internalAutoHideListener;
      private Rect tmpRect;

      public BaseBehavior() {
         this.autoHideEnabled = true;
      }

      public BaseBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.FloatingActionButton_Behavior_Layout);
         this.autoHideEnabled = var3.getBoolean(styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
         var3.recycle();
      }

      private static boolean isBottomSheet(View var0) {
         LayoutParams var1 = var0.getLayoutParams();
         return var1 instanceof CoordinatorLayout.LayoutParams ? ((CoordinatorLayout.LayoutParams)var1).getBehavior() instanceof BottomSheetBehavior : false;
      }

      private void offsetIfNeeded(CoordinatorLayout var1, FloatingActionButton var2) {
         Rect var5 = var2.shadowPadding;
         if (var5 != null && var5.centerX() > 0 && var5.centerY() > 0) {
            CoordinatorLayout.LayoutParams var6 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            int var4 = 0;
            int var3 = 0;
            if (var2.getRight() >= var1.getWidth() - var6.rightMargin) {
               var3 = var5.right;
            } else if (var2.getLeft() <= var6.leftMargin) {
               var3 = -var5.left;
            }

            if (var2.getBottom() >= var1.getHeight() - var6.bottomMargin) {
               var4 = var5.bottom;
            } else if (var2.getTop() <= var6.topMargin) {
               var4 = -var5.top;
            }

            if (var4 != 0) {
               ViewCompat.offsetTopAndBottom(var2, var4);
            }

            if (var3 != 0) {
               ViewCompat.offsetLeftAndRight(var2, var3);
            }
         }

      }

      private boolean shouldUpdateVisibility(View var1, FloatingActionButton var2) {
         CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         if (!this.autoHideEnabled) {
            return false;
         } else if (var3.getAnchorId() != var1.getId()) {
            return false;
         } else {
            return var2.getUserSetVisibility() == 0;
         }
      }

      private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout var1, AppBarLayout var2, FloatingActionButton var3) {
         if (!this.shouldUpdateVisibility(var2, var3)) {
            return false;
         } else {
            if (this.tmpRect == null) {
               this.tmpRect = new Rect();
            }

            Rect var4 = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(var1, var2, var4);
            if (var4.bottom <= var2.getMinimumHeightForVisibleOverlappingContent()) {
               var3.hide(this.internalAutoHideListener, false);
            } else {
               var3.show(this.internalAutoHideListener, false);
            }

            return true;
         }
      }

      private boolean updateFabVisibilityForBottomSheet(View var1, FloatingActionButton var2) {
         if (!this.shouldUpdateVisibility(var1, var2)) {
            return false;
         } else {
            CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            if (var1.getTop() < var2.getHeight() / 2 + var3.topMargin) {
               var2.hide(this.internalAutoHideListener, false);
            } else {
               var2.show(this.internalAutoHideListener, false);
            }

            return true;
         }
      }

      public boolean getInsetDodgeRect(CoordinatorLayout var1, FloatingActionButton var2, Rect var3) {
         Rect var4 = var2.shadowPadding;
         var3.set(var2.getLeft() + var4.left, var2.getTop() + var4.top, var2.getRight() - var4.right, var2.getBottom() - var4.bottom);
         return true;
      }

      public boolean isAutoHideEnabled() {
         return this.autoHideEnabled;
      }

      public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
         if (var1.dodgeInsetEdges == 0) {
            var1.dodgeInsetEdges = 80;
         }

      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, FloatingActionButton var2, View var3) {
         if (var3 instanceof AppBarLayout) {
            this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var3, var2);
         } else if (isBottomSheet(var3)) {
            this.updateFabVisibilityForBottomSheet(var3, var2);
         }

         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, FloatingActionButton var2, int var3) {
         List var6 = var1.getDependencies(var2);
         int var4 = 0;

         for(int var5 = var6.size(); var4 < var5; ++var4) {
            View var7 = (View)var6.get(var4);
            if (var7 instanceof AppBarLayout) {
               if (this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var7, var2)) {
                  break;
               }
            } else if (isBottomSheet(var7) && this.updateFabVisibilityForBottomSheet(var7, var2)) {
               break;
            }
         }

         var1.onLayoutChild(var2, var3);
         this.offsetIfNeeded(var1, var2);
         return true;
      }

      public void setAutoHideEnabled(boolean var1) {
         this.autoHideEnabled = var1;
      }

      public void setInternalAutoHideListener(FloatingActionButton.OnVisibilityChangedListener var1) {
         this.internalAutoHideListener = var1;
      }
   }

   public static class Behavior extends FloatingActionButton.BaseBehavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }
   }

   public abstract static class OnVisibilityChangedListener {
      public void onHidden(FloatingActionButton var1) {
      }

      public void onShown(FloatingActionButton var1) {
      }
   }

   private class ShadowDelegateImpl implements ShadowViewDelegate {
      ShadowDelegateImpl() {
      }

      public float getRadius() {
         return (float)FloatingActionButton.this.getSizeDimension() / 2.0F;
      }

      public boolean isCompatPaddingEnabled() {
         return FloatingActionButton.this.compatPadding;
      }

      public void setBackgroundDrawable(Drawable var1) {
         if (var1 != null) {
            FloatingActionButton.super.setBackgroundDrawable(var1);
         }

      }

      public void setShadowPadding(int var1, int var2, int var3, int var4) {
         FloatingActionButton.this.shadowPadding.set(var1, var2, var3, var4);
         FloatingActionButton var5 = FloatingActionButton.this;
         var5.setPadding(var5.imagePadding + var1, FloatingActionButton.this.imagePadding + var2, FloatingActionButton.this.imagePadding + var3, FloatingActionButton.this.imagePadding + var4);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Size {
   }

   class TransformationCallbackWrapper implements FloatingActionButtonImpl.InternalTransformationCallback {
      private final TransformationCallback listener;

      TransformationCallbackWrapper(TransformationCallback var2) {
         this.listener = var2;
      }

      public boolean equals(Object var1) {
         return var1 instanceof FloatingActionButton.TransformationCallbackWrapper && ((FloatingActionButton.TransformationCallbackWrapper)var1).listener.equals(this.listener);
      }

      public int hashCode() {
         return this.listener.hashCode();
      }

      public void onScaleChanged() {
         this.listener.onScaleChanged(FloatingActionButton.this);
      }

      public void onTranslationChanged() {
         this.listener.onTranslationChanged(FloatingActionButton.this);
      }
   }
}
