package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLayoutChangeListener;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
   private static final long ANIMATION_DURATION = 300L;
   private static final int DEF_STYLE_RES;
   public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
   public static final int FAB_ALIGNMENT_MODE_END = 1;
   public static final int FAB_ANIMATION_MODE_SCALE = 0;
   public static final int FAB_ANIMATION_MODE_SLIDE = 1;
   private int animatingModeChangeCounter;
   private ArrayList animationListeners;
   private BottomAppBar.Behavior behavior;
   private int bottomInset;
   private int fabAlignmentMode;
   AnimatorListenerAdapter fabAnimationListener;
   private int fabAnimationMode;
   private boolean fabAttached;
   private final int fabOffsetEndMode;
   TransformationCallback fabTransformationCallback;
   private boolean hideOnScroll;
   private final MaterialShapeDrawable materialShapeDrawable;
   private Animator menuAnimator;
   private Animator modeAnimator;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_BottomAppBar;
   }

   public BottomAppBar(Context var1) {
      this(var1, (AttributeSet)null, 0);
   }

   public BottomAppBar(Context var1, AttributeSet var2) {
      this(var1, var2, attr.bottomAppBarStyle);
   }

   public BottomAppBar(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.materialShapeDrawable = new MaterialShapeDrawable();
      this.animatingModeChangeCounter = 0;
      this.fabAttached = true;
      this.fabAnimationListener = new AnimatorListenerAdapter() {
         public void onAnimationStart(Animator var1) {
            BottomAppBar var2 = BottomAppBar.this;
            var2.maybeAnimateMenuView(var2.fabAlignmentMode, BottomAppBar.this.fabAttached);
         }
      };
      this.fabTransformationCallback = new TransformationCallback() {
         public void onScaleChanged(FloatingActionButton var1) {
            MaterialShapeDrawable var3 = BottomAppBar.this.materialShapeDrawable;
            float var2;
            if (var1.getVisibility() == 0) {
               var2 = var1.getScaleY();
            } else {
               var2 = 0.0F;
            }

            var3.setInterpolation(var2);
         }

         public void onTranslationChanged(FloatingActionButton var1) {
            float var2 = var1.getTranslationX();
            if (BottomAppBar.this.getTopEdgeTreatment().getHorizontalOffset() != var2) {
               BottomAppBar.this.getTopEdgeTreatment().setHorizontalOffset(var2);
               BottomAppBar.this.materialShapeDrawable.invalidateSelf();
            }

            var2 = -var1.getTranslationY();
            if (BottomAppBar.this.getTopEdgeTreatment().getCradleVerticalOffset() != var2) {
               BottomAppBar.this.getTopEdgeTreatment().setCradleVerticalOffset(var2);
               BottomAppBar.this.materialShapeDrawable.invalidateSelf();
            }

            MaterialShapeDrawable var3 = BottomAppBar.this.materialShapeDrawable;
            if (var1.getVisibility() == 0) {
               var2 = var1.getScaleY();
            } else {
               var2 = 0.0F;
            }

            var3.setInterpolation(var2);
         }
      };
      var1 = this.getContext();
      TypedArray var7 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.BottomAppBar, var3, DEF_STYLE_RES);
      ColorStateList var8 = MaterialResources.getColorStateList(var1, var7, styleable.BottomAppBar_backgroundTint);
      var3 = var7.getDimensionPixelSize(styleable.BottomAppBar_elevation, 0);
      float var4 = (float)var7.getDimensionPixelOffset(styleable.BottomAppBar_fabCradleMargin, 0);
      float var5 = (float)var7.getDimensionPixelOffset(styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
      float var6 = (float)var7.getDimensionPixelOffset(styleable.BottomAppBar_fabCradleVerticalOffset, 0);
      this.fabAlignmentMode = var7.getInt(styleable.BottomAppBar_fabAlignmentMode, 0);
      this.fabAnimationMode = var7.getInt(styleable.BottomAppBar_fabAnimationMode, 0);
      this.hideOnScroll = var7.getBoolean(styleable.BottomAppBar_hideOnScroll, false);
      var7.recycle();
      this.fabOffsetEndMode = this.getResources().getDimensionPixelOffset(dimen.mtrl_bottomappbar_fabOffsetEndMode);
      BottomAppBarTopEdgeTreatment var9 = new BottomAppBarTopEdgeTreatment(var4, var5, var6);
      ShapeAppearanceModel var10 = ShapeAppearanceModel.builder().setTopEdge(var9).build();
      this.materialShapeDrawable.setShapeAppearanceModel(var10);
      this.materialShapeDrawable.setShadowCompatibilityMode(2);
      this.materialShapeDrawable.setPaintStyle(Style.FILL);
      this.materialShapeDrawable.initializeElevationOverlay(var1);
      this.setElevation((float)var3);
      DrawableCompat.setTintList(this.materialShapeDrawable, var8);
      ViewCompat.setBackground(this, this.materialShapeDrawable);
      ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2, ViewUtils.RelativePadding var3) {
            BottomAppBar.this.bottomInset = var2.getSystemWindowInsetBottom();
            var3.bottom += var2.getSystemWindowInsetBottom();
            var3.applyToView(var1);
            return var2;
         }
      });
   }

   private void addFabAnimationListeners(FloatingActionButton var1) {
      var1.addOnHideAnimationListener(this.fabAnimationListener);
      var1.addOnShowAnimationListener(new AnimatorListenerAdapter() {
         public void onAnimationStart(Animator var1) {
            BottomAppBar.this.fabAnimationListener.onAnimationStart(var1);
            FloatingActionButton var2 = BottomAppBar.this.findDependentFab();
            if (var2 != null) {
               var2.setTranslationX(BottomAppBar.this.getFabTranslationX());
            }

         }
      });
      var1.addTransformationCallback(this.fabTransformationCallback);
   }

   private void cancelAnimations() {
      Animator var1 = this.menuAnimator;
      if (var1 != null) {
         var1.cancel();
      }

      var1 = this.modeAnimator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   private void createFabTranslationXAnimation(int var1, List var2) {
      ObjectAnimator var3 = ObjectAnimator.ofFloat(this.findDependentFab(), "translationX", new float[]{this.getFabTranslationX(var1)});
      var3.setDuration(300L);
      var2.add(var3);
   }

   private void createMenuViewTranslationAnimation(final int var1, final boolean var2, List var3) {
      final ActionMenuView var6 = this.getActionMenuView();
      if (var6 != null) {
         ObjectAnimator var4 = ObjectAnimator.ofFloat(var6, "alpha", new float[]{1.0F});
         if (Math.abs(var6.getTranslationX() - (float)this.getActionMenuViewTranslationX(var6, var1, var2)) > 1.0F) {
            ObjectAnimator var5 = ObjectAnimator.ofFloat(var6, "alpha", new float[]{0.0F});
            var5.addListener(new AnimatorListenerAdapter() {
               public boolean cancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.cancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (!this.cancelled) {
                     BottomAppBar.this.translateActionMenuView(var6, var1, var2);
                  }

               }
            });
            AnimatorSet var7 = new AnimatorSet();
            var7.setDuration(150L);
            var7.playSequentially(new Animator[]{var5, var4});
            var3.add(var7);
         } else if (var6.getAlpha() < 1.0F) {
            var3.add(var4);
            return;
         }

      }
   }

   private void dispatchAnimationEnd() {
      int var1 = this.animatingModeChangeCounter - 1;
      this.animatingModeChangeCounter = var1;
      if (var1 == 0) {
         ArrayList var2 = this.animationListeners;
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ((BottomAppBar.AnimationListener)var3.next()).onAnimationEnd(this);
            }
         }
      }

   }

   private void dispatchAnimationStart() {
      int var1 = this.animatingModeChangeCounter++;
      if (var1 == 0) {
         ArrayList var2 = this.animationListeners;
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ((BottomAppBar.AnimationListener)var3.next()).onAnimationStart(this);
            }
         }
      }

   }

   private FloatingActionButton findDependentFab() {
      View var1 = this.findDependentView();
      return var1 instanceof FloatingActionButton ? (FloatingActionButton)var1 : null;
   }

   private View findDependentView() {
      if (!(this.getParent() instanceof CoordinatorLayout)) {
         return null;
      } else {
         Iterator var1 = ((CoordinatorLayout)this.getParent()).getDependents(this).iterator();

         View var2;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            var2 = (View)var1.next();
            if (var2 instanceof FloatingActionButton) {
               return var2;
            }
         } while(!(var2 instanceof ExtendedFloatingActionButton));

         return var2;
      }
   }

   private ActionMenuView getActionMenuView() {
      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         View var2 = this.getChildAt(var1);
         if (var2 instanceof ActionMenuView) {
            return (ActionMenuView)var2;
         }
      }

      return null;
   }

   private int getBottomInset() {
      return this.bottomInset;
   }

   private float getFabTranslationX() {
      return this.getFabTranslationX(this.fabAlignmentMode);
   }

   private float getFabTranslationX(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      byte var3 = 1;
      boolean var7;
      if (var2 == 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var1 == 1) {
         int var4 = this.getMeasuredWidth() / 2;
         int var5 = this.fabOffsetEndMode;
         byte var6 = var3;
         if (var7) {
            var6 = -1;
         }

         return (float)((var4 - var5) * var6);
      } else {
         return 0.0F;
      }
   }

   private float getFabTranslationY() {
      return -this.getTopEdgeTreatment().getCradleVerticalOffset();
   }

   private BottomAppBarTopEdgeTreatment getTopEdgeTreatment() {
      return (BottomAppBarTopEdgeTreatment)this.materialShapeDrawable.getShapeAppearanceModel().getTopEdge();
   }

   private boolean isFabVisibleOrWillBeShown() {
      FloatingActionButton var1 = this.findDependentFab();
      return var1 != null && var1.isOrWillBeShown();
   }

   private void maybeAnimateMenuView(int var1, boolean var2) {
      if (ViewCompat.isLaidOut(this)) {
         Animator var3 = this.menuAnimator;
         if (var3 != null) {
            var3.cancel();
         }

         ArrayList var5 = new ArrayList();
         if (!this.isFabVisibleOrWillBeShown()) {
            var1 = 0;
            var2 = false;
         }

         this.createMenuViewTranslationAnimation(var1, var2, var5);
         AnimatorSet var4 = new AnimatorSet();
         var4.playTogether(var5);
         this.menuAnimator = var4;
         var4.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               BottomAppBar.this.dispatchAnimationEnd();
               BottomAppBar.this.menuAnimator = null;
            }

            public void onAnimationStart(Animator var1) {
               BottomAppBar.this.dispatchAnimationStart();
            }
         });
         this.menuAnimator.start();
      }
   }

   private void maybeAnimateModeChange(int var1) {
      if (this.fabAlignmentMode != var1) {
         if (ViewCompat.isLaidOut(this)) {
            Animator var2 = this.modeAnimator;
            if (var2 != null) {
               var2.cancel();
            }

            ArrayList var4 = new ArrayList();
            if (this.fabAnimationMode == 1) {
               this.createFabTranslationXAnimation(var1, var4);
            } else {
               this.createFabDefaultXAnimation(var1, var4);
            }

            AnimatorSet var3 = new AnimatorSet();
            var3.playTogether(var4);
            this.modeAnimator = var3;
            var3.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  BottomAppBar.this.dispatchAnimationEnd();
               }

               public void onAnimationStart(Animator var1) {
                  BottomAppBar.this.dispatchAnimationStart();
               }
            });
            this.modeAnimator.start();
         }
      }
   }

   private void setActionMenuViewPosition() {
      ActionMenuView var1 = this.getActionMenuView();
      if (var1 != null) {
         var1.setAlpha(1.0F);
         if (!this.isFabVisibleOrWillBeShown()) {
            this.translateActionMenuView(var1, 0, false);
            return;
         }

         this.translateActionMenuView(var1, this.fabAlignmentMode, this.fabAttached);
      }

   }

   private void setCutoutState() {
      this.getTopEdgeTreatment().setHorizontalOffset(this.getFabTranslationX());
      View var2 = this.findDependentView();
      MaterialShapeDrawable var3 = this.materialShapeDrawable;
      float var1;
      if (this.fabAttached && this.isFabVisibleOrWillBeShown()) {
         var1 = 1.0F;
      } else {
         var1 = 0.0F;
      }

      var3.setInterpolation(var1);
      if (var2 != null) {
         var2.setTranslationY(this.getFabTranslationY());
         var2.setTranslationX(this.getFabTranslationX());
      }

   }

   private void translateActionMenuView(ActionMenuView var1, int var2, boolean var3) {
      var1.setTranslationX((float)this.getActionMenuViewTranslationX(var1, var2, var3));
   }

   void addAnimationListener(BottomAppBar.AnimationListener var1) {
      if (this.animationListeners == null) {
         this.animationListeners = new ArrayList();
      }

      this.animationListeners.add(var1);
   }

   protected void createFabDefaultXAnimation(final int var1, List var2) {
      FloatingActionButton var3 = this.findDependentFab();
      if (var3 != null) {
         if (!var3.isOrWillBeHidden()) {
            this.dispatchAnimationStart();
            var3.hide(new FloatingActionButton.OnVisibilityChangedListener() {
               public void onHidden(FloatingActionButton var1x) {
                  var1x.setTranslationX(BottomAppBar.this.getFabTranslationX(var1));
                  var1x.show(new FloatingActionButton.OnVisibilityChangedListener() {
                     public void onShown(FloatingActionButton var1x) {
                        BottomAppBar.this.dispatchAnimationEnd();
                     }
                  });
               }
            });
         }
      }
   }

   protected int getActionMenuViewTranslationX(ActionMenuView var1, int var2, boolean var3) {
      int var4 = ViewCompat.getLayoutDirection(this);
      byte var9 = 0;
      boolean var5;
      if (var4 == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         var4 = this.getMeasuredWidth();
      } else {
         var4 = 0;
      }

      int var6;
      int var7;
      for(var6 = 0; var6 < this.getChildCount(); var4 = var7) {
         View var10 = this.getChildAt(var6);
         boolean var8;
         if (var10.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)var10.getLayoutParams()).gravity & 8388615) == 8388611) {
            var8 = true;
         } else {
            var8 = false;
         }

         var7 = var4;
         if (var8) {
            if (var5) {
               var4 = Math.min(var4, var10.getLeft());
            } else {
               var4 = Math.max(var4, var10.getRight());
            }

            var7 = var4;
         }

         ++var6;
      }

      int var11;
      if (var5) {
         var11 = var1.getRight();
      } else {
         var11 = var1.getLeft();
      }

      var6 = var9;
      if (var2 == 1) {
         var6 = var9;
         if (var3) {
            var6 = var4 - var11;
         }
      }

      return var6;
   }

   public ColorStateList getBackgroundTint() {
      return this.materialShapeDrawable.getTintList();
   }

   public BottomAppBar.Behavior getBehavior() {
      if (this.behavior == null) {
         this.behavior = new BottomAppBar.Behavior();
      }

      return this.behavior;
   }

   public float getCradleVerticalOffset() {
      return this.getTopEdgeTreatment().getCradleVerticalOffset();
   }

   public int getFabAlignmentMode() {
      return this.fabAlignmentMode;
   }

   public int getFabAnimationMode() {
      return this.fabAnimationMode;
   }

   public float getFabCradleMargin() {
      return this.getTopEdgeTreatment().getFabCradleMargin();
   }

   public float getFabCradleRoundedCornerRadius() {
      return this.getTopEdgeTreatment().getFabCradleRoundedCornerRadius();
   }

   public boolean getHideOnScroll() {
      return this.hideOnScroll;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this, this.materialShapeDrawable);
      if (this.getParent() instanceof ViewGroup) {
         ((ViewGroup)this.getParent()).setClipChildren(false);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (var1) {
         this.cancelAnimations();
         this.setCutoutState();
      }

      this.setActionMenuViewPosition();
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof BottomAppBar.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         BottomAppBar.SavedState var2 = (BottomAppBar.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.fabAlignmentMode = var2.fabAlignmentMode;
         this.fabAttached = var2.fabAttached;
      }
   }

   protected Parcelable onSaveInstanceState() {
      BottomAppBar.SavedState var1 = new BottomAppBar.SavedState(super.onSaveInstanceState());
      var1.fabAlignmentMode = this.fabAlignmentMode;
      var1.fabAttached = this.fabAttached;
      return var1;
   }

   public void performHide() {
      this.getBehavior().slideDown(this);
   }

   public void performShow() {
      this.getBehavior().slideUp(this);
   }

   void removeAnimationListener(BottomAppBar.AnimationListener var1) {
      ArrayList var2 = this.animationListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   public void replaceMenu(int var1) {
      this.getMenu().clear();
      this.inflateMenu(var1);
   }

   public void setBackgroundTint(ColorStateList var1) {
      DrawableCompat.setTintList(this.materialShapeDrawable, var1);
   }

   public void setCradleVerticalOffset(float var1) {
      if (var1 != this.getCradleVerticalOffset()) {
         this.getTopEdgeTreatment().setCradleVerticalOffset(var1);
         this.materialShapeDrawable.invalidateSelf();
         this.setCutoutState();
      }

   }

   public void setElevation(float var1) {
      this.materialShapeDrawable.setElevation(var1);
      int var2 = this.materialShapeDrawable.getShadowRadius();
      int var3 = this.materialShapeDrawable.getShadowOffsetY();
      this.getBehavior().setAdditionalHiddenOffsetY(this, var2 - var3);
   }

   public void setFabAlignmentMode(int var1) {
      this.maybeAnimateModeChange(var1);
      this.maybeAnimateMenuView(var1, this.fabAttached);
      this.fabAlignmentMode = var1;
   }

   public void setFabAnimationMode(int var1) {
      this.fabAnimationMode = var1;
   }

   public void setFabCradleMargin(float var1) {
      if (var1 != this.getFabCradleMargin()) {
         this.getTopEdgeTreatment().setFabCradleMargin(var1);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   public void setFabCradleRoundedCornerRadius(float var1) {
      if (var1 != this.getFabCradleRoundedCornerRadius()) {
         this.getTopEdgeTreatment().setFabCradleRoundedCornerRadius(var1);
         this.materialShapeDrawable.invalidateSelf();
      }

   }

   boolean setFabDiameter(int var1) {
      if ((float)var1 != this.getTopEdgeTreatment().getFabDiameter()) {
         this.getTopEdgeTreatment().setFabDiameter((float)var1);
         this.materialShapeDrawable.invalidateSelf();
         return true;
      } else {
         return false;
      }
   }

   public void setHideOnScroll(boolean var1) {
      this.hideOnScroll = var1;
   }

   public void setSubtitle(CharSequence var1) {
   }

   public void setTitle(CharSequence var1) {
   }

   interface AnimationListener {
      void onAnimationEnd(BottomAppBar var1);

      void onAnimationStart(BottomAppBar var1);
   }

   public static class Behavior extends HideBottomViewOnScrollBehavior {
      private final Rect fabContentRect = new Rect();
      private final OnLayoutChangeListener fabLayoutListener = new OnLayoutChangeListener() {
         public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
            BottomAppBar var10 = (BottomAppBar)Behavior.this.viewRef.get();
            if (var10 != null && var1 instanceof FloatingActionButton) {
               FloatingActionButton var11 = (FloatingActionButton)var1;
               var11.getMeasuredContentRect(Behavior.this.fabContentRect);
               var2 = Behavior.this.fabContentRect.height();
               var10.setFabDiameter(var2);
               CoordinatorLayout.LayoutParams var12 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
               if (Behavior.this.originalBottomMargin == 0) {
                  var2 = (var11.getMeasuredHeight() - var2) / 2;
                  var3 = var10.getResources().getDimensionPixelOffset(dimen.mtrl_bottomappbar_fab_bottom_margin);
                  var12.bottomMargin = var10.getBottomInset() + (var3 - var2);
               }

            } else {
               var1.removeOnLayoutChangeListener(this);
            }
         }
      };
      private int originalBottomMargin;
      private WeakReference viewRef;

      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public boolean onLayoutChild(CoordinatorLayout var1, BottomAppBar var2, int var3) {
         this.viewRef = new WeakReference(var2);
         View var4 = var2.findDependentView();
         if (var4 != null && !ViewCompat.isLaidOut(var4)) {
            CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var4.getLayoutParams();
            var5.anchorGravity = 49;
            this.originalBottomMargin = var5.bottomMargin;
            if (var4 instanceof FloatingActionButton) {
               FloatingActionButton var6 = (FloatingActionButton)var4;
               var6.addOnLayoutChangeListener(this.fabLayoutListener);
               var2.addFabAnimationListeners(var6);
            }

            var2.setCutoutState();
         }

         var1.onLayoutChild(var2, var3);
         return super.onLayoutChild(var1, var2, var3);
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, BottomAppBar var2, View var3, View var4, int var5, int var6) {
         return var2.getHideOnScroll() && super.onStartNestedScroll(var1, var2, var3, var4, var5, var6);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface FabAlignmentMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface FabAnimationMode {
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomAppBar.SavedState createFromParcel(Parcel var1) {
            return new BottomAppBar.SavedState(var1, (ClassLoader)null);
         }

         public BottomAppBar.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomAppBar.SavedState(var1, var2);
         }

         public BottomAppBar.SavedState[] newArray(int var1) {
            return new BottomAppBar.SavedState[var1];
         }
      };
      int fabAlignmentMode;
      boolean fabAttached;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.fabAlignmentMode = var1.readInt();
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.fabAttached = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }
}
