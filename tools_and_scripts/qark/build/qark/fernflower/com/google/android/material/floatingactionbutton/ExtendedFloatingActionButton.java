package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.animator;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.Iterator;
import java.util.List;

public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.AttachedBehavior {
   private static final int ANIM_STATE_HIDING = 1;
   private static final int ANIM_STATE_NONE = 0;
   private static final int ANIM_STATE_SHOWING = 2;
   private static final int DEF_STYLE_RES;
   static final Property HEIGHT;
   static final Property WIDTH;
   private int animState;
   private final CoordinatorLayout.Behavior behavior;
   private final AnimatorTracker changeVisibilityTracker;
   private final MotionStrategy extendStrategy;
   private final MotionStrategy hideStrategy;
   private boolean isExtended;
   private final Rect shadowPadding;
   private final MotionStrategy showStrategy;
   private final MotionStrategy shrinkStrategy;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_ExtendedFloatingActionButton_Icon;
      WIDTH = new Property(Float.class, "width") {
         public Float get(View var1) {
            return (float)var1.getLayoutParams().width;
         }

         public void set(View var1, Float var2) {
            var1.getLayoutParams().width = var2.intValue();
            var1.requestLayout();
         }
      };
      HEIGHT = new Property(Float.class, "height") {
         public Float get(View var1) {
            return (float)var1.getLayoutParams().height;
         }

         public void set(View var1, Float var2) {
            var1.getLayoutParams().height = var2.intValue();
            var1.requestLayout();
         }
      };
   }

   public ExtendedFloatingActionButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ExtendedFloatingActionButton(Context var1, AttributeSet var2) {
      this(var1, var2, attr.extendedFloatingActionButtonStyle);
   }

   public ExtendedFloatingActionButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.shadowPadding = new Rect();
      this.animState = 0;
      AnimatorTracker var4 = new AnimatorTracker();
      this.changeVisibilityTracker = var4;
      this.showStrategy = new ExtendedFloatingActionButton.ShowStrategy(var4);
      this.hideStrategy = new ExtendedFloatingActionButton.HideStrategy(this.changeVisibilityTracker);
      this.isExtended = true;
      this.behavior = new ExtendedFloatingActionButton.ExtendedFloatingActionButtonBehavior(var1, var2);
      TypedArray var10 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.ExtendedFloatingActionButton, var3, DEF_STYLE_RES);
      MotionSpec var5 = MotionSpec.createFromAttribute(var1, var10, styleable.ExtendedFloatingActionButton_showMotionSpec);
      MotionSpec var6 = MotionSpec.createFromAttribute(var1, var10, styleable.ExtendedFloatingActionButton_hideMotionSpec);
      MotionSpec var7 = MotionSpec.createFromAttribute(var1, var10, styleable.ExtendedFloatingActionButton_extendMotionSpec);
      MotionSpec var8 = MotionSpec.createFromAttribute(var1, var10, styleable.ExtendedFloatingActionButton_shrinkMotionSpec);
      AnimatorTracker var9 = new AnimatorTracker();
      this.extendStrategy = new ExtendedFloatingActionButton.ChangeSizeStrategy(var9, new ExtendedFloatingActionButton.Size() {
         public int getHeight() {
            return ExtendedFloatingActionButton.this.getMeasuredHeight();
         }

         public int getWidth() {
            return ExtendedFloatingActionButton.this.getMeasuredWidth();
         }
      }, true);
      this.shrinkStrategy = new ExtendedFloatingActionButton.ChangeSizeStrategy(var9, new ExtendedFloatingActionButton.Size() {
         public int getHeight() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
         }

         public int getWidth() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
         }
      }, false);
      this.showStrategy.setMotionSpec(var5);
      this.hideStrategy.setMotionSpec(var6);
      this.extendStrategy.setMotionSpec(var7);
      this.shrinkStrategy.setMotionSpec(var8);
      var10.recycle();
      this.setShapeAppearanceModel(ShapeAppearanceModel.builder(var1, var2, var3, DEF_STYLE_RES, ShapeAppearanceModel.PILL).build());
   }

   private boolean isOrWillBeHidden() {
      int var1 = this.getVisibility();
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

   private boolean isOrWillBeShown() {
      int var1 = this.getVisibility();
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

   private void performMotion(final MotionStrategy var1, final ExtendedFloatingActionButton.OnChangedCallback var2) {
      if (!var1.shouldCancel()) {
         if (!this.shouldAnimateVisibilityChange()) {
            var1.performNow();
            var1.onChange(var2);
         } else {
            this.measure(0, 0);
            AnimatorSet var3 = var1.createAnimator();
            var3.addListener(new AnimatorListenerAdapter() {
               private boolean cancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.cancelled = true;
                  var1.onAnimationCancel();
               }

               public void onAnimationEnd(Animator var1x) {
                  var1.onAnimationEnd();
                  if (!this.cancelled) {
                     var1.onChange(var2);
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  var1.onAnimationStart(var1x);
                  this.cancelled = false;
               }
            });
            Iterator var4 = var1.getListeners().iterator();

            while(var4.hasNext()) {
               var3.addListener((AnimatorListener)var4.next());
            }

            var3.start();
         }
      }
   }

   private boolean shouldAnimateVisibilityChange() {
      return ViewCompat.isLaidOut(this) && !this.isInEditMode();
   }

   public void addOnExtendAnimationListener(AnimatorListener var1) {
      this.extendStrategy.addAnimationListener(var1);
   }

   public void addOnHideAnimationListener(AnimatorListener var1) {
      this.hideStrategy.addAnimationListener(var1);
   }

   public void addOnShowAnimationListener(AnimatorListener var1) {
      this.showStrategy.addAnimationListener(var1);
   }

   public void addOnShrinkAnimationListener(AnimatorListener var1) {
      this.shrinkStrategy.addAnimationListener(var1);
   }

   public void extend() {
      this.performMotion(this.extendStrategy, (ExtendedFloatingActionButton.OnChangedCallback)null);
   }

   public void extend(ExtendedFloatingActionButton.OnChangedCallback var1) {
      this.performMotion(this.extendStrategy, var1);
   }

   public CoordinatorLayout.Behavior getBehavior() {
      return this.behavior;
   }

   int getCollapsedSize() {
      return Math.min(ViewCompat.getPaddingStart(this), ViewCompat.getPaddingEnd(this)) * 2 + this.getIconSize();
   }

   public MotionSpec getExtendMotionSpec() {
      return this.extendStrategy.getMotionSpec();
   }

   public MotionSpec getHideMotionSpec() {
      return this.hideStrategy.getMotionSpec();
   }

   public MotionSpec getShowMotionSpec() {
      return this.showStrategy.getMotionSpec();
   }

   public MotionSpec getShrinkMotionSpec() {
      return this.shrinkStrategy.getMotionSpec();
   }

   public void hide() {
      this.performMotion(this.hideStrategy, (ExtendedFloatingActionButton.OnChangedCallback)null);
   }

   public void hide(ExtendedFloatingActionButton.OnChangedCallback var1) {
      this.performMotion(this.hideStrategy, var1);
   }

   public final boolean isExtended() {
      return this.isExtended;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.isExtended && TextUtils.isEmpty(this.getText()) && this.getIcon() != null) {
         this.isExtended = false;
         this.shrinkStrategy.performNow();
      }

   }

   public void removeOnExtendAnimationListener(AnimatorListener var1) {
      this.extendStrategy.removeAnimationListener(var1);
   }

   public void removeOnHideAnimationListener(AnimatorListener var1) {
      this.hideStrategy.removeAnimationListener(var1);
   }

   public void removeOnShowAnimationListener(AnimatorListener var1) {
      this.showStrategy.removeAnimationListener(var1);
   }

   public void removeOnShrinkAnimationListener(AnimatorListener var1) {
      this.shrinkStrategy.removeAnimationListener(var1);
   }

   public void setExtendMotionSpec(MotionSpec var1) {
      this.extendStrategy.setMotionSpec(var1);
   }

   public void setExtendMotionSpecResource(int var1) {
      this.setExtendMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setExtended(boolean var1) {
      if (this.isExtended != var1) {
         MotionStrategy var2;
         if (var1) {
            var2 = this.extendStrategy;
         } else {
            var2 = this.shrinkStrategy;
         }

         if (!var2.shouldCancel()) {
            var2.performNow();
         }
      }
   }

   public void setHideMotionSpec(MotionSpec var1) {
      this.hideStrategy.setMotionSpec(var1);
   }

   public void setHideMotionSpecResource(int var1) {
      this.setHideMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setShowMotionSpec(MotionSpec var1) {
      this.showStrategy.setMotionSpec(var1);
   }

   public void setShowMotionSpecResource(int var1) {
      this.setShowMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void setShrinkMotionSpec(MotionSpec var1) {
      this.shrinkStrategy.setMotionSpec(var1);
   }

   public void setShrinkMotionSpecResource(int var1) {
      this.setShrinkMotionSpec(MotionSpec.createFromResource(this.getContext(), var1));
   }

   public void show() {
      this.performMotion(this.showStrategy, (ExtendedFloatingActionButton.OnChangedCallback)null);
   }

   public void show(ExtendedFloatingActionButton.OnChangedCallback var1) {
      this.performMotion(this.showStrategy, var1);
   }

   public void shrink() {
      this.performMotion(this.shrinkStrategy, (ExtendedFloatingActionButton.OnChangedCallback)null);
   }

   public void shrink(ExtendedFloatingActionButton.OnChangedCallback var1) {
      this.performMotion(this.shrinkStrategy, var1);
   }

   class ChangeSizeStrategy extends BaseMotionStrategy {
      private final boolean extending;
      private final ExtendedFloatingActionButton.Size size;

      ChangeSizeStrategy(AnimatorTracker var2, ExtendedFloatingActionButton.Size var3, boolean var4) {
         super(ExtendedFloatingActionButton.this, var2);
         this.size = var3;
         this.extending = var4;
      }

      public AnimatorSet createAnimator() {
         MotionSpec var1 = this.getCurrentMotionSpec();
         PropertyValuesHolder[] var2;
         if (var1.hasPropertyValues("width")) {
            var2 = var1.getPropertyValues("width");
            var2[0].setFloatValues(new float[]{(float)ExtendedFloatingActionButton.this.getWidth(), (float)this.size.getWidth()});
            var1.setPropertyValues("width", var2);
         }

         if (var1.hasPropertyValues("height")) {
            var2 = var1.getPropertyValues("height");
            var2[0].setFloatValues(new float[]{(float)ExtendedFloatingActionButton.this.getHeight(), (float)this.size.getHeight()});
            var1.setPropertyValues("height", var2);
         }

         return super.createAnimator(var1);
      }

      public int getDefaultMotionSpecResource() {
         return animator.mtrl_extended_fab_change_size_motion_spec;
      }

      public void onAnimationEnd() {
         super.onAnimationEnd();
         ExtendedFloatingActionButton.this.setHorizontallyScrolling(false);
      }

      public void onAnimationStart(Animator var1) {
         super.onAnimationStart(var1);
         ExtendedFloatingActionButton.this.isExtended = this.extending;
         ExtendedFloatingActionButton.this.setHorizontallyScrolling(true);
      }

      public void onChange(ExtendedFloatingActionButton.OnChangedCallback var1) {
         if (var1 != null) {
            if (this.extending) {
               var1.onExtended(ExtendedFloatingActionButton.this);
            } else {
               var1.onShrunken(ExtendedFloatingActionButton.this);
            }
         }
      }

      public void performNow() {
         ExtendedFloatingActionButton.this.isExtended = this.extending;
         LayoutParams var1 = ExtendedFloatingActionButton.this.getLayoutParams();
         if (var1 != null) {
            if (this.extending) {
               ExtendedFloatingActionButton.this.measure(0, 0);
            }

            var1.width = this.size.getWidth();
            var1.height = this.size.getHeight();
            ExtendedFloatingActionButton.this.requestLayout();
         }
      }

      public boolean shouldCancel() {
         return this.extending == ExtendedFloatingActionButton.this.isExtended || ExtendedFloatingActionButton.this.getIcon() == null || TextUtils.isEmpty(ExtendedFloatingActionButton.this.getText());
      }
   }

   protected static class ExtendedFloatingActionButtonBehavior extends CoordinatorLayout.Behavior {
      private static final boolean AUTO_HIDE_DEFAULT = false;
      private static final boolean AUTO_SHRINK_DEFAULT = true;
      private boolean autoHideEnabled;
      private boolean autoShrinkEnabled;
      private ExtendedFloatingActionButton.OnChangedCallback internalAutoHideCallback;
      private ExtendedFloatingActionButton.OnChangedCallback internalAutoShrinkCallback;
      private Rect tmpRect;

      public ExtendedFloatingActionButtonBehavior() {
         this.autoHideEnabled = false;
         this.autoShrinkEnabled = true;
      }

      public ExtendedFloatingActionButtonBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.ExtendedFloatingActionButton_Behavior_Layout);
         this.autoHideEnabled = var3.getBoolean(styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoHide, false);
         this.autoShrinkEnabled = var3.getBoolean(styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoShrink, true);
         var3.recycle();
      }

      private static boolean isBottomSheet(View var0) {
         LayoutParams var1 = var0.getLayoutParams();
         return var1 instanceof CoordinatorLayout.LayoutParams ? ((CoordinatorLayout.LayoutParams)var1).getBehavior() instanceof BottomSheetBehavior : false;
      }

      private void offsetIfNeeded(CoordinatorLayout var1, ExtendedFloatingActionButton var2) {
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

      private boolean shouldUpdateVisibility(View var1, ExtendedFloatingActionButton var2) {
         CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         if (!this.autoHideEnabled && !this.autoShrinkEnabled) {
            return false;
         } else {
            return var3.getAnchorId() == var1.getId();
         }
      }

      private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout var1, AppBarLayout var2, ExtendedFloatingActionButton var3) {
         if (!this.shouldUpdateVisibility(var2, var3)) {
            return false;
         } else {
            if (this.tmpRect == null) {
               this.tmpRect = new Rect();
            }

            Rect var4 = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(var1, var2, var4);
            if (var4.bottom <= var2.getMinimumHeightForVisibleOverlappingContent()) {
               this.shrinkOrHide(var3);
            } else {
               this.extendOrShow(var3);
            }

            return true;
         }
      }

      private boolean updateFabVisibilityForBottomSheet(View var1, ExtendedFloatingActionButton var2) {
         if (!this.shouldUpdateVisibility(var1, var2)) {
            return false;
         } else {
            CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            if (var1.getTop() < var2.getHeight() / 2 + var3.topMargin) {
               this.shrinkOrHide(var2);
            } else {
               this.extendOrShow(var2);
            }

            return true;
         }
      }

      protected void extendOrShow(ExtendedFloatingActionButton var1) {
         ExtendedFloatingActionButton.OnChangedCallback var2;
         if (this.autoShrinkEnabled) {
            var2 = this.internalAutoShrinkCallback;
         } else {
            var2 = this.internalAutoHideCallback;
         }

         MotionStrategy var3;
         if (this.autoShrinkEnabled) {
            var3 = var1.extendStrategy;
         } else {
            var3 = var1.showStrategy;
         }

         var1.performMotion(var3, var2);
      }

      public boolean getInsetDodgeRect(CoordinatorLayout var1, ExtendedFloatingActionButton var2, Rect var3) {
         Rect var4 = var2.shadowPadding;
         var3.set(var2.getLeft() + var4.left, var2.getTop() + var4.top, var2.getRight() - var4.right, var2.getBottom() - var4.bottom);
         return true;
      }

      public boolean isAutoHideEnabled() {
         return this.autoHideEnabled;
      }

      public boolean isAutoShrinkEnabled() {
         return this.autoShrinkEnabled;
      }

      public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
         if (var1.dodgeInsetEdges == 0) {
            var1.dodgeInsetEdges = 80;
         }

      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, ExtendedFloatingActionButton var2, View var3) {
         if (var3 instanceof AppBarLayout) {
            this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var3, var2);
         } else if (isBottomSheet(var3)) {
            this.updateFabVisibilityForBottomSheet(var3, var2);
         }

         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, ExtendedFloatingActionButton var2, int var3) {
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

      public void setAutoShrinkEnabled(boolean var1) {
         this.autoShrinkEnabled = var1;
      }

      void setInternalAutoHideCallback(ExtendedFloatingActionButton.OnChangedCallback var1) {
         this.internalAutoHideCallback = var1;
      }

      void setInternalAutoShrinkCallback(ExtendedFloatingActionButton.OnChangedCallback var1) {
         this.internalAutoShrinkCallback = var1;
      }

      protected void shrinkOrHide(ExtendedFloatingActionButton var1) {
         ExtendedFloatingActionButton.OnChangedCallback var2;
         if (this.autoShrinkEnabled) {
            var2 = this.internalAutoShrinkCallback;
         } else {
            var2 = this.internalAutoHideCallback;
         }

         MotionStrategy var3;
         if (this.autoShrinkEnabled) {
            var3 = var1.shrinkStrategy;
         } else {
            var3 = var1.hideStrategy;
         }

         var1.performMotion(var3, var2);
      }
   }

   class HideStrategy extends BaseMotionStrategy {
      private boolean isCancelled;

      public HideStrategy(AnimatorTracker var2) {
         super(ExtendedFloatingActionButton.this, var2);
      }

      public int getDefaultMotionSpecResource() {
         return animator.mtrl_extended_fab_hide_motion_spec;
      }

      public void onAnimationCancel() {
         super.onAnimationCancel();
         this.isCancelled = true;
      }

      public void onAnimationEnd() {
         super.onAnimationEnd();
         ExtendedFloatingActionButton.this.animState = 0;
         if (!this.isCancelled) {
            ExtendedFloatingActionButton.this.setVisibility(8);
         }

      }

      public void onAnimationStart(Animator var1) {
         super.onAnimationStart(var1);
         this.isCancelled = false;
         ExtendedFloatingActionButton.this.setVisibility(0);
         ExtendedFloatingActionButton.this.animState = 1;
      }

      public void onChange(ExtendedFloatingActionButton.OnChangedCallback var1) {
         if (var1 != null) {
            var1.onHidden(ExtendedFloatingActionButton.this);
         }

      }

      public void performNow() {
         ExtendedFloatingActionButton.this.setVisibility(8);
      }

      public boolean shouldCancel() {
         return ExtendedFloatingActionButton.this.isOrWillBeHidden();
      }
   }

   public abstract static class OnChangedCallback {
      public void onExtended(ExtendedFloatingActionButton var1) {
      }

      public void onHidden(ExtendedFloatingActionButton var1) {
      }

      public void onShown(ExtendedFloatingActionButton var1) {
      }

      public void onShrunken(ExtendedFloatingActionButton var1) {
      }
   }

   class ShowStrategy extends BaseMotionStrategy {
      public ShowStrategy(AnimatorTracker var2) {
         super(ExtendedFloatingActionButton.this, var2);
      }

      public int getDefaultMotionSpecResource() {
         return animator.mtrl_extended_fab_show_motion_spec;
      }

      public void onAnimationEnd() {
         super.onAnimationEnd();
         ExtendedFloatingActionButton.this.animState = 0;
      }

      public void onAnimationStart(Animator var1) {
         super.onAnimationStart(var1);
         ExtendedFloatingActionButton.this.setVisibility(0);
         ExtendedFloatingActionButton.this.animState = 2;
      }

      public void onChange(ExtendedFloatingActionButton.OnChangedCallback var1) {
         if (var1 != null) {
            var1.onShown(ExtendedFloatingActionButton.this);
         }

      }

      public void performNow() {
         ExtendedFloatingActionButton.this.setVisibility(0);
         ExtendedFloatingActionButton.this.setAlpha(1.0F);
         ExtendedFloatingActionButton.this.setScaleY(1.0F);
         ExtendedFloatingActionButton.this.setScaleX(1.0F);
      }

      public boolean shouldCancel() {
         return ExtendedFloatingActionButton.this.isOrWillBeShown();
      }
   }

   interface Size {
      int getHeight();

      int getWidth();
   }
}
