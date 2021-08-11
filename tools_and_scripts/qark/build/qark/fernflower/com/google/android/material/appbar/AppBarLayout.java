package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.integer;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(AppBarLayout.Behavior.class)
public class AppBarLayout extends LinearLayout {
   private static final int INVALID_SCROLL_RANGE = -1;
   static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
   static final int PENDING_ACTION_COLLAPSED = 2;
   static final int PENDING_ACTION_EXPANDED = 1;
   static final int PENDING_ACTION_FORCE = 8;
   static final int PENDING_ACTION_NONE = 0;
   private int currentOffset;
   private int downPreScrollRange;
   private int downScrollRange;
   private ValueAnimator elevationOverlayAnimator;
   private boolean haveChildWithInterpolator;
   private WindowInsetsCompat lastInsets;
   private boolean liftOnScroll;
   private WeakReference liftOnScrollTargetView;
   private int liftOnScrollTargetViewId;
   private boolean liftable;
   private boolean liftableOverride;
   private boolean lifted;
   private List listeners;
   private int pendingAction;
   private Drawable statusBarForeground;
   private int[] tmpStatesArray;
   private int totalScrollRange;

   public AppBarLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppBarLayout(Context var1, AttributeSet var2) {
      this(var1, var2, attr.appBarLayoutStyle);
   }

   public AppBarLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.totalScrollRange = -1;
      this.downPreScrollRange = -1;
      this.downScrollRange = -1;
      this.pendingAction = 0;
      this.setOrientation(1);
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
         ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, var2, var3, style.Widget_Design_AppBarLayout);
      }

      TypedArray var6 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.AppBarLayout, var3, style.Widget_Design_AppBarLayout);
      ViewCompat.setBackground(this, var6.getDrawable(styleable.AppBarLayout_android_background));
      if (this.getBackground() instanceof ColorDrawable) {
         ColorDrawable var4 = (ColorDrawable)this.getBackground();
         MaterialShapeDrawable var5 = new MaterialShapeDrawable();
         var5.setFillColor(ColorStateList.valueOf(var4.getColor()));
         var5.initializeElevationOverlay(var1);
         ViewCompat.setBackground(this, var5);
      }

      if (var6.hasValue(styleable.AppBarLayout_expanded)) {
         this.setExpanded(var6.getBoolean(styleable.AppBarLayout_expanded, false), false, false);
      }

      if (VERSION.SDK_INT >= 21 && var6.hasValue(styleable.AppBarLayout_elevation)) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, (float)var6.getDimensionPixelSize(styleable.AppBarLayout_elevation, 0));
      }

      if (VERSION.SDK_INT >= 26) {
         if (var6.hasValue(styleable.AppBarLayout_android_keyboardNavigationCluster)) {
            this.setKeyboardNavigationCluster(var6.getBoolean(styleable.AppBarLayout_android_keyboardNavigationCluster, false));
         }

         if (var6.hasValue(styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
            this.setTouchscreenBlocksFocus(var6.getBoolean(styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
         }
      }

      this.liftOnScroll = var6.getBoolean(styleable.AppBarLayout_liftOnScroll, false);
      this.liftOnScrollTargetViewId = var6.getResourceId(styleable.AppBarLayout_liftOnScrollTargetViewId, -1);
      this.setStatusBarForeground(var6.getDrawable(styleable.AppBarLayout_statusBarForeground));
      var6.recycle();
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            return AppBarLayout.this.onWindowInsetChanged(var2);
         }
      });
   }

   private void clearLiftOnScrollTargetView() {
      WeakReference var1 = this.liftOnScrollTargetView;
      if (var1 != null) {
         var1.clear();
      }

      this.liftOnScrollTargetView = null;
   }

   private View findLiftOnScrollTargetView(View var1) {
      if (this.liftOnScrollTargetView == null) {
         int var2 = this.liftOnScrollTargetViewId;
         if (var2 != -1) {
            View var3 = null;
            if (var1 != null) {
               var3 = var1.findViewById(var2);
            }

            var1 = var3;
            if (var3 == null) {
               var1 = var3;
               if (this.getParent() instanceof ViewGroup) {
                  var1 = ((ViewGroup)this.getParent()).findViewById(this.liftOnScrollTargetViewId);
               }
            }

            if (var1 != null) {
               this.liftOnScrollTargetView = new WeakReference(var1);
            }
         }
      }

      WeakReference var4 = this.liftOnScrollTargetView;
      return var4 != null ? (View)var4.get() : null;
   }

   private boolean hasCollapsibleChild() {
      int var1 = 0;

      for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
         if (((AppBarLayout.LayoutParams)this.getChildAt(var1).getLayoutParams()).isCollapsible()) {
            return true;
         }
      }

      return false;
   }

   private void invalidateScrollRanges() {
      this.totalScrollRange = -1;
      this.downPreScrollRange = -1;
      this.downScrollRange = -1;
   }

   private void setExpanded(boolean var1, boolean var2, boolean var3) {
      byte var4;
      if (var1) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      byte var6 = 0;
      byte var5;
      if (var2) {
         var5 = 4;
      } else {
         var5 = 0;
      }

      if (var3) {
         var6 = 8;
      }

      this.pendingAction = var4 | var5 | var6;
      this.requestLayout();
   }

   private boolean setLiftableState(boolean var1) {
      if (this.liftable != var1) {
         this.liftable = var1;
         this.refreshDrawableState();
         return true;
      } else {
         return false;
      }
   }

   private boolean shouldDrawStatusBarForeground() {
      return this.statusBarForeground != null && this.getTopInset() > 0;
   }

   private boolean shouldOffsetFirstChild() {
      int var1 = this.getChildCount();
      boolean var3 = false;
      if (var1 > 0) {
         View var4 = this.getChildAt(0);
         boolean var2 = var3;
         if (var4.getVisibility() != 8) {
            var2 = var3;
            if (!ViewCompat.getFitsSystemWindows(var4)) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   private void startLiftOnScrollElevationOverlayAnimation(final MaterialShapeDrawable var1, boolean var2) {
      float var3 = this.getResources().getDimension(dimen.design_appbar_elevation);
      float var5 = 0.0F;
      float var4;
      if (var2) {
         var4 = 0.0F;
      } else {
         var4 = var3;
      }

      if (var2) {
         var5 = var3;
      }

      ValueAnimator var6 = this.elevationOverlayAnimator;
      if (var6 != null) {
         var6.cancel();
      }

      var6 = ValueAnimator.ofFloat(new float[]{var4, var5});
      this.elevationOverlayAnimator = var6;
      var6.setDuration((long)this.getResources().getInteger(integer.app_bar_elevation_anim_duration));
      this.elevationOverlayAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      this.elevationOverlayAnimator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1x) {
            var1.setElevation((Float)var1x.getAnimatedValue());
         }
      });
      this.elevationOverlayAnimator.start();
   }

   private void updateWillNotDraw() {
      this.setWillNotDraw(this.shouldDrawStatusBarForeground() ^ true);
   }

   public void addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener var1) {
      if (this.listeners == null) {
         this.listeners = new ArrayList();
      }

      if (var1 != null && !this.listeners.contains(var1)) {
         this.listeners.add(var1);
      }

   }

   public void addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      this.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener)var1);
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof AppBarLayout.LayoutParams;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.shouldDrawStatusBarForeground()) {
         int var2 = var1.save();
         var1.translate(0.0F, (float)(-this.currentOffset));
         this.statusBarForeground.draw(var1);
         var1.restoreToCount(var2);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var1 = this.getDrawableState();
      Drawable var2 = this.statusBarForeground;
      if (var2 != null && var2.isStateful() && var2.setState(var1)) {
         this.invalidateDrawable(var2);
      }

   }

   protected AppBarLayout.LayoutParams generateDefaultLayoutParams() {
      return new AppBarLayout.LayoutParams(-1, -2);
   }

   public AppBarLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new AppBarLayout.LayoutParams(this.getContext(), var1);
   }

   protected AppBarLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (VERSION.SDK_INT >= 19 && var1 instanceof android.widget.LinearLayout.LayoutParams) {
         return new AppBarLayout.LayoutParams((android.widget.LinearLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new AppBarLayout.LayoutParams((MarginLayoutParams)var1) : new AppBarLayout.LayoutParams(var1);
      }
   }

   int getDownNestedPreScrollRange() {
      int var1 = this.downPreScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         int var3 = 0;

         for(int var2 = this.getChildCount() - 1; var2 >= 0; var3 = var1) {
            View var6 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var7 = (AppBarLayout.LayoutParams)var6.getLayoutParams();
            int var5 = var6.getMeasuredHeight();
            var1 = var7.scrollFlags;
            if ((var1 & 5) == 5) {
               int var4 = var7.topMargin + var7.bottomMargin;
               if ((var1 & 8) != 0) {
                  var1 = var4 + ViewCompat.getMinimumHeight(var6);
               } else if ((var1 & 2) != 0) {
                  var1 = var4 + (var5 - ViewCompat.getMinimumHeight(var6));
               } else {
                  var1 = var4 + var5;
               }

               var4 = var1;
               if (var2 == 0) {
                  var4 = var1;
                  if (ViewCompat.getFitsSystemWindows(var6)) {
                     var4 = Math.min(var1, var5 - this.getTopInset());
                  }
               }

               var1 = var3 + var4;
            } else {
               var1 = var3;
               if (var3 > 0) {
                  break;
               }
            }

            --var2;
         }

         var1 = Math.max(0, var3);
         this.downPreScrollRange = var1;
         return var1;
      }
   }

   int getDownNestedScrollRange() {
      int var1 = this.downScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;
         int var2 = 0;
         int var4 = this.getChildCount();

         int var3;
         while(true) {
            var3 = var1;
            if (var2 >= var4) {
               break;
            }

            View var9 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var10 = (AppBarLayout.LayoutParams)var9.getLayoutParams();
            int var6 = var9.getMeasuredHeight();
            int var7 = var10.topMargin;
            int var8 = var10.bottomMargin;
            int var5 = var10.scrollFlags;
            var3 = var1;
            if ((var5 & 1) == 0) {
               break;
            }

            var1 += var6 + var7 + var8;
            if ((var5 & 2) != 0) {
               var3 = var1 - ViewCompat.getMinimumHeight(var9);
               break;
            }

            ++var2;
         }

         var1 = Math.max(0, var3);
         this.downScrollRange = var1;
         return var1;
      }
   }

   public int getLiftOnScrollTargetViewId() {
      return this.liftOnScrollTargetViewId;
   }

   public final int getMinimumHeightForVisibleOverlappingContent() {
      int var2 = this.getTopInset();
      int var1 = ViewCompat.getMinimumHeight(this);
      if (var1 != 0) {
         return var1 * 2 + var2;
      } else {
         var1 = this.getChildCount();
         if (var1 >= 1) {
            var1 = ViewCompat.getMinimumHeight(this.getChildAt(var1 - 1));
         } else {
            var1 = 0;
         }

         return var1 != 0 ? var1 * 2 + var2 : this.getHeight() / 3;
      }
   }

   int getPendingAction() {
      return this.pendingAction;
   }

   public Drawable getStatusBarForeground() {
      return this.statusBarForeground;
   }

   @Deprecated
   public float getTargetElevation() {
      return 0.0F;
   }

   final int getTopInset() {
      WindowInsetsCompat var1 = this.lastInsets;
      return var1 != null ? var1.getSystemWindowInsetTop() : 0;
   }

   public final int getTotalScrollRange() {
      int var1 = this.totalScrollRange;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;
         int var2 = 0;
         int var4 = this.getChildCount();

         int var3;
         while(true) {
            var3 = var1;
            if (var2 >= var4) {
               break;
            }

            View var7 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var8 = (AppBarLayout.LayoutParams)var7.getLayoutParams();
            int var6 = var7.getMeasuredHeight();
            int var5 = var8.scrollFlags;
            var3 = var1;
            if ((var5 & 1) == 0) {
               break;
            }

            var3 = var1 + var8.topMargin + var6 + var8.bottomMargin;
            var1 = var3;
            if (var2 == 0) {
               var1 = var3;
               if (ViewCompat.getFitsSystemWindows(var7)) {
                  var1 = var3 - this.getTopInset();
               }
            }

            if ((var5 & 2) != 0) {
               var3 = var1 - ViewCompat.getMinimumHeight(var7);
               break;
            }

            ++var2;
         }

         var1 = Math.max(0, var3);
         this.totalScrollRange = var1;
         return var1;
      }
   }

   int getUpNestedPreScrollRange() {
      return this.getTotalScrollRange();
   }

   boolean hasChildWithInterpolator() {
      return this.haveChildWithInterpolator;
   }

   boolean hasScrollableChildren() {
      return this.getTotalScrollRange() != 0;
   }

   public boolean isLiftOnScroll() {
      return this.liftOnScroll;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this);
   }

   protected int[] onCreateDrawableState(int var1) {
      if (this.tmpStatesArray == null) {
         this.tmpStatesArray = new int[4];
      }

      int[] var2 = this.tmpStatesArray;
      int[] var3 = super.onCreateDrawableState(var2.length + var1);
      if (this.liftable) {
         var1 = attr.state_liftable;
      } else {
         var1 = -attr.state_liftable;
      }

      var2[0] = var1;
      if (this.liftable && this.lifted) {
         var1 = attr.state_lifted;
      } else {
         var1 = -attr.state_lifted;
      }

      var2[1] = var1;
      if (this.liftable) {
         var1 = attr.state_collapsible;
      } else {
         var1 = -attr.state_collapsible;
      }

      var2[2] = var1;
      if (this.liftable && this.lifted) {
         var1 = attr.state_collapsed;
      } else {
         var1 = -attr.state_collapsed;
      }

      var2[3] = var1;
      return mergeDrawableStates(var3, var2);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.clearLiftOnScrollTargetView();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      var1 = ViewCompat.getFitsSystemWindows(this);
      boolean var6 = true;
      if (var1 && this.shouldOffsetFirstChild()) {
         var3 = this.getTopInset();

         for(var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            ViewCompat.offsetTopAndBottom(this.getChildAt(var2), var3);
         }
      }

      this.invalidateScrollRanges();
      this.haveChildWithInterpolator = false;
      var2 = 0;

      for(var3 = this.getChildCount(); var2 < var3; ++var2) {
         if (((AppBarLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).getScrollInterpolator() != null) {
            this.haveChildWithInterpolator = true;
            break;
         }
      }

      Drawable var7 = this.statusBarForeground;
      if (var7 != null) {
         var7.setBounds(0, 0, this.getWidth(), this.getTopInset());
      }

      if (!this.liftableOverride) {
         var1 = var6;
         if (!this.liftOnScroll) {
            if (this.hasCollapsibleChild()) {
               var1 = var6;
            } else {
               var1 = false;
            }
         }

         this.setLiftableState(var1);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      int var3 = MeasureSpec.getMode(var2);
      if (var3 != 1073741824 && ViewCompat.getFitsSystemWindows(this) && this.shouldOffsetFirstChild()) {
         var1 = this.getMeasuredHeight();
         if (var3 != Integer.MIN_VALUE) {
            if (var3 == 0) {
               var1 += this.getTopInset();
            }
         } else {
            var1 = MathUtils.clamp(this.getMeasuredHeight() + this.getTopInset(), 0, MeasureSpec.getSize(var2));
         }

         this.setMeasuredDimension(this.getMeasuredWidth(), var1);
      }

      this.invalidateScrollRanges();
   }

   void onOffsetChanged(int var1) {
      this.currentOffset = var1;
      if (!this.willNotDraw()) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

      List var4 = this.listeners;
      if (var4 != null) {
         int var2 = 0;

         for(int var3 = var4.size(); var2 < var3; ++var2) {
            AppBarLayout.BaseOnOffsetChangedListener var5 = (AppBarLayout.BaseOnOffsetChangedListener)this.listeners.get(var2);
            if (var5 != null) {
               var5.onOffsetChanged(this, var1);
            }
         }
      }

   }

   WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat var1) {
      WindowInsetsCompat var2 = null;
      if (ViewCompat.getFitsSystemWindows(this)) {
         var2 = var1;
      }

      if (!ObjectsCompat.equals(this.lastInsets, var2)) {
         this.lastInsets = var2;
         this.updateWillNotDraw();
         this.requestLayout();
      }

      return var1;
   }

   public void removeOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener var1) {
      List var2 = this.listeners;
      if (var2 != null && var1 != null) {
         var2.remove(var1);
      }

   }

   public void removeOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      this.removeOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener)var1);
   }

   void resetPendingAction() {
      this.pendingAction = 0;
   }

   public void setElevation(float var1) {
      super.setElevation(var1);
      MaterialShapeUtils.setElevation(this, var1);
   }

   public void setExpanded(boolean var1) {
      this.setExpanded(var1, ViewCompat.isLaidOut(this));
   }

   public void setExpanded(boolean var1, boolean var2) {
      this.setExpanded(var1, var2, true);
   }

   public void setLiftOnScroll(boolean var1) {
      this.liftOnScroll = var1;
   }

   public void setLiftOnScrollTargetViewId(int var1) {
      this.liftOnScrollTargetViewId = var1;
      this.clearLiftOnScrollTargetView();
   }

   public boolean setLiftable(boolean var1) {
      this.liftableOverride = true;
      return this.setLiftableState(var1);
   }

   public boolean setLifted(boolean var1) {
      return this.setLiftedState(var1);
   }

   boolean setLiftedState(boolean var1) {
      if (this.lifted != var1) {
         this.lifted = var1;
         this.refreshDrawableState();
         if (this.liftOnScroll && this.getBackground() instanceof MaterialShapeDrawable) {
            this.startLiftOnScrollElevationOverlayAnimation((MaterialShapeDrawable)this.getBackground(), var1);
         }

         return true;
      } else {
         return false;
      }
   }

   public void setOrientation(int var1) {
      if (var1 == 1) {
         super.setOrientation(var1);
      } else {
         throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
      }
   }

   public void setStatusBarForeground(Drawable var1) {
      Drawable var4 = this.statusBarForeground;
      if (var4 != var1) {
         Drawable var3 = null;
         if (var4 != null) {
            var4.setCallback((Callback)null);
         }

         if (var1 != null) {
            var3 = var1.mutate();
         }

         this.statusBarForeground = var3;
         if (var3 != null) {
            if (var3.isStateful()) {
               this.statusBarForeground.setState(this.getDrawableState());
            }

            DrawableCompat.setLayoutDirection(this.statusBarForeground, ViewCompat.getLayoutDirection(this));
            var1 = this.statusBarForeground;
            boolean var2;
            if (this.getVisibility() == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.setVisible(var2, false);
            this.statusBarForeground.setCallback(this);
         }

         this.updateWillNotDraw();
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setStatusBarForegroundColor(int var1) {
      this.setStatusBarForeground(new ColorDrawable(var1));
   }

   public void setStatusBarForegroundResource(int var1) {
      this.setStatusBarForeground(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   @Deprecated
   public void setTargetElevation(float var1) {
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, var1);
      }

   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Drawable var3 = this.statusBarForeground;
      if (var3 != null) {
         var3.setVisible(var2, false);
      }

   }

   boolean shouldLift(View var1) {
      View var3 = this.findLiftOnScrollTargetView(var1);
      View var2 = var3;
      if (var3 == null) {
         var2 = var1;
      }

      return var2 != null && (var2.canScrollVertically(-1) || var2.getScrollY() > 0);
   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.statusBarForeground;
   }

   protected static class BaseBehavior extends HeaderBehavior {
      private static final int INVALID_POSITION = -1;
      private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
      private WeakReference lastNestedScrollingChildRef;
      private int lastStartedType;
      private ValueAnimator offsetAnimator;
      private int offsetDelta;
      private int offsetToChildIndexOnLayout = -1;
      private boolean offsetToChildIndexOnLayoutIsMinHeight;
      private float offsetToChildIndexOnLayoutPerc;
      private AppBarLayout.BaseBehavior.BaseDragCallback onDragCallback;

      public BaseBehavior() {
      }

      public BaseBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      private void animateOffsetTo(CoordinatorLayout var1, AppBarLayout var2, int var3, float var4) {
         int var5 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - var3);
         var4 = Math.abs(var4);
         if (var4 > 0.0F) {
            var5 = Math.round((float)var5 / var4 * 1000.0F) * 3;
         } else {
            var5 = (int)((1.0F + (float)var5 / (float)var2.getHeight()) * 150.0F);
         }

         this.animateOffsetWithDuration(var1, var2, var3, var5);
      }

      private void animateOffsetWithDuration(final CoordinatorLayout var1, final AppBarLayout var2, int var3, int var4) {
         int var5 = this.getTopBottomOffsetForScrollingSibling();
         if (var5 == var3) {
            ValueAnimator var7 = this.offsetAnimator;
            if (var7 != null && var7.isRunning()) {
               this.offsetAnimator.cancel();
            }

         } else {
            ValueAnimator var6 = this.offsetAnimator;
            if (var6 == null) {
               var6 = new ValueAnimator();
               this.offsetAnimator = var6;
               var6.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
               this.offsetAnimator.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1x) {
                     BaseBehavior.this.setHeaderTopBottomOffset(var1, var2, (Integer)var1x.getAnimatedValue());
                  }
               });
            } else {
               var6.cancel();
            }

            this.offsetAnimator.setDuration((long)Math.min(var4, 600));
            this.offsetAnimator.setIntValues(new int[]{var5, var3});
            this.offsetAnimator.start();
         }
      }

      private boolean canScrollChildren(CoordinatorLayout var1, AppBarLayout var2, View var3) {
         return var2.hasScrollableChildren() && var1.getHeight() - var3.getHeight() <= var2.getHeight();
      }

      private static boolean checkFlag(int var0, int var1) {
         return (var0 & var1) == var1;
      }

      private View findFirstScrollingChild(CoordinatorLayout var1) {
         int var2 = 0;

         for(int var3 = var1.getChildCount(); var2 < var3; ++var2) {
            View var4 = var1.getChildAt(var2);
            if (var4 instanceof NestedScrollingChild || var4 instanceof ListView) {
               return var4;
            }

            if (var4 instanceof ScrollView) {
               return var4;
            }
         }

         return null;
      }

      private static View getAppBarChildOnOffset(AppBarLayout var0, int var1) {
         int var2 = Math.abs(var1);
         var1 = 0;

         for(int var3 = var0.getChildCount(); var1 < var3; ++var1) {
            View var4 = var0.getChildAt(var1);
            if (var2 >= var4.getTop() && var2 <= var4.getBottom()) {
               return var4;
            }
         }

         return null;
      }

      private int getChildIndexOnOffset(AppBarLayout var1, int var2) {
         int var3 = 0;

         for(int var8 = var1.getChildCount(); var3 < var8; ++var3) {
            View var9 = var1.getChildAt(var3);
            int var7 = var9.getTop();
            int var6 = var9.getBottom();
            AppBarLayout.LayoutParams var10 = (AppBarLayout.LayoutParams)var9.getLayoutParams();
            int var5 = var7;
            int var4 = var6;
            if (checkFlag(var10.getScrollFlags(), 32)) {
               var5 = var7 - var10.topMargin;
               var4 = var6 + var10.bottomMargin;
            }

            if (var5 <= -var2 && var4 >= -var2) {
               return var3;
            }
         }

         return -1;
      }

      private int interpolateOffset(AppBarLayout var1, int var2) {
         int var5 = Math.abs(var2);
         int var3 = 0;

         for(int var4 = var1.getChildCount(); var3 < var4; ++var3) {
            View var7 = var1.getChildAt(var3);
            AppBarLayout.LayoutParams var8 = (AppBarLayout.LayoutParams)var7.getLayoutParams();
            Interpolator var9 = var8.getScrollInterpolator();
            if (var5 >= var7.getTop() && var5 <= var7.getBottom()) {
               if (var9 != null) {
                  var3 = 0;
                  int var6 = var8.getScrollFlags();
                  if ((var6 & 1) != 0) {
                     var4 = 0 + var7.getHeight() + var8.topMargin + var8.bottomMargin;
                     var3 = var4;
                     if ((var6 & 2) != 0) {
                        var3 = var4 - ViewCompat.getMinimumHeight(var7);
                     }
                  }

                  var4 = var3;
                  if (ViewCompat.getFitsSystemWindows(var7)) {
                     var4 = var3 - var1.getTopInset();
                  }

                  if (var4 > 0) {
                     var3 = var7.getTop();
                     var3 = Math.round((float)var4 * var9.getInterpolation((float)(var5 - var3) / (float)var4));
                     return Integer.signum(var2) * (var7.getTop() + var3);
                  }

                  return var2;
               }
               break;
            }
         }

         return var2;
      }

      private boolean shouldJumpElevationState(CoordinatorLayout var1, AppBarLayout var2) {
         List var6 = var1.getDependents(var2);
         int var3 = 0;
         int var4 = var6.size();

         while(true) {
            boolean var5 = false;
            if (var3 >= var4) {
               return false;
            }

            CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)((View)var6.get(var3)).getLayoutParams()).getBehavior();
            if (var7 instanceof AppBarLayout.ScrollingViewBehavior) {
               if (((AppBarLayout.ScrollingViewBehavior)var7).getOverlayTop() != 0) {
                  var5 = true;
               }

               return var5;
            }

            ++var3;
         }
      }

      private void snapToChildIfNeeded(CoordinatorLayout var1, AppBarLayout var2) {
         int var7 = this.getTopBottomOffsetForScrollingSibling();
         int var5 = this.getChildIndexOnOffset(var2, var7);
         if (var5 >= 0) {
            View var9 = var2.getChildAt(var5);
            AppBarLayout.LayoutParams var10 = (AppBarLayout.LayoutParams)var9.getLayoutParams();
            int var8 = var10.getScrollFlags();
            if ((var8 & 17) == 17) {
               int var6 = -var9.getTop();
               int var3 = -var9.getBottom();
               int var4 = var3;
               if (var5 == var2.getChildCount() - 1) {
                  var4 = var3 + var2.getTopInset();
               }

               if (checkFlag(var8, 2)) {
                  var3 = var4 + ViewCompat.getMinimumHeight(var9);
                  var5 = var6;
               } else {
                  var5 = var6;
                  var3 = var4;
                  if (checkFlag(var8, 5)) {
                     var3 = ViewCompat.getMinimumHeight(var9) + var4;
                     if (var7 < var3) {
                        var5 = var3;
                        var3 = var4;
                     } else {
                        var5 = var6;
                     }
                  }
               }

               var6 = var5;
               var4 = var3;
               if (checkFlag(var8, 32)) {
                  var6 = var5 + var10.topMargin;
                  var4 = var3 - var10.bottomMargin;
               }

               if (var7 >= (var4 + var6) / 2) {
                  var4 = var6;
               }

               this.animateOffsetTo(var1, var2, MathUtils.clamp(var4, -var2.getTotalScrollRange(), 0), 0.0F);
            }
         }

      }

      private void updateAppBarLayoutDrawableState(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, boolean var5) {
         View var11 = getAppBarChildOnOffset(var2, var3);
         if (var11 != null) {
            int var6 = ((AppBarLayout.LayoutParams)var11.getLayoutParams()).getScrollFlags();
            boolean var9 = false;
            boolean var8 = var9;
            if ((var6 & 1) != 0) {
               int var7 = ViewCompat.getMinimumHeight(var11);
               boolean var10 = false;
               var8 = false;
               if (var4 > 0 && (var6 & 12) != 0) {
                  if (-var3 >= var11.getBottom() - var7 - var2.getTopInset()) {
                     var8 = true;
                  }
               } else {
                  var8 = var9;
                  if ((var6 & 2) != 0) {
                     var8 = var10;
                     if (-var3 >= var11.getBottom() - var7 - var2.getTopInset()) {
                        var8 = true;
                     }
                  }
               }
            }

            if (var2.isLiftOnScroll()) {
               var8 = var2.shouldLift(this.findFirstScrollingChild(var1));
            }

            var8 = var2.setLiftedState(var8);
            if (var5 || var8 && this.shouldJumpElevationState(var1, var2)) {
               var2.jumpDrawablesToCurrentState();
            }
         }

      }

      boolean canDragView(AppBarLayout var1) {
         AppBarLayout.BaseBehavior.BaseDragCallback var2 = this.onDragCallback;
         if (var2 != null) {
            return var2.canDrag(var1);
         } else {
            WeakReference var3 = this.lastNestedScrollingChildRef;
            if (var3 != null) {
               View var4 = (View)var3.get();
               return var4 != null && var4.isShown() && !var4.canScrollVertically(-1);
            } else {
               return true;
            }
         }
      }

      int getMaxDragOffset(AppBarLayout var1) {
         return -var1.getDownNestedScrollRange();
      }

      int getScrollRangeForDragFling(AppBarLayout var1) {
         return var1.getTotalScrollRange();
      }

      int getTopBottomOffsetForScrollingSibling() {
         return this.getTopAndBottomOffset() + this.offsetDelta;
      }

      boolean isOffsetAnimatorRunning() {
         ValueAnimator var1 = this.offsetAnimator;
         return var1 != null && var1.isRunning();
      }

      void onFlingFinished(CoordinatorLayout var1, AppBarLayout var2) {
         this.snapToChildIfNeeded(var1, var2);
         if (var2.isLiftOnScroll()) {
            var2.setLiftedState(var2.shouldLift(this.findFirstScrollingChild(var1)));
         }

      }

      public boolean onLayoutChild(CoordinatorLayout var1, AppBarLayout var2, int var3) {
         boolean var5 = super.onLayoutChild(var1, var2, var3);
         int var4 = var2.getPendingAction();
         var3 = this.offsetToChildIndexOnLayout;
         if (var3 >= 0 && (var4 & 8) == 0) {
            View var6 = var2.getChildAt(var3);
            var3 = -var6.getBottom();
            if (this.offsetToChildIndexOnLayoutIsMinHeight) {
               var3 += ViewCompat.getMinimumHeight(var6) + var2.getTopInset();
            } else {
               var3 += Math.round((float)var6.getHeight() * this.offsetToChildIndexOnLayoutPerc);
            }

            this.setHeaderTopBottomOffset(var1, var2, var3);
         } else if (var4 != 0) {
            boolean var7;
            if ((var4 & 4) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if ((var4 & 2) != 0) {
               var4 = -var2.getUpNestedPreScrollRange();
               if (var7) {
                  this.animateOffsetTo(var1, var2, var4, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, var4);
               }
            } else if ((var4 & 1) != 0) {
               if (var7) {
                  this.animateOffsetTo(var1, var2, 0, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, 0);
               }
            }
         }

         var2.resetPendingAction();
         this.offsetToChildIndexOnLayout = -1;
         this.setTopAndBottomOffset(MathUtils.clamp(this.getTopAndBottomOffset(), -var2.getTotalScrollRange(), 0));
         this.updateAppBarLayoutDrawableState(var1, var2, this.getTopAndBottomOffset(), 0, true);
         var2.onOffsetChanged(this.getTopAndBottomOffset());
         return var5;
      }

      public boolean onMeasureChild(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5, int var6) {
         if (((CoordinatorLayout.LayoutParams)var2.getLayoutParams()).height == -2) {
            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(0, 0), var6);
            return true;
         } else {
            return super.onMeasureChild(var1, var2, var3, var4, var5, var6);
         }
      }

      public void onNestedPreScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int[] var6, int var7) {
         if (var5 != 0) {
            if (var5 < 0) {
               var7 = -var2.getTotalScrollRange();
               int var8 = var2.getDownNestedPreScrollRange();
               var4 = var7;
               var7 += var8;
            } else {
               var4 = -var2.getUpNestedPreScrollRange();
               var7 = 0;
            }

            if (var4 != var7) {
               var6[1] = this.scroll(var1, var2, var5, var4, var7);
            }
         }

         if (var2.isLiftOnScroll()) {
            var2.setLiftedState(var2.shouldLift(var3));
         }
      }

      public void onNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int var6, int var7, int var8, int[] var9) {
         if (var7 < 0) {
            var9[1] = this.scroll(var1, var2, var7, -var2.getDownNestedScrollRange(), 0);
         }

      }

      public void onRestoreInstanceState(CoordinatorLayout var1, AppBarLayout var2, Parcelable var3) {
         if (var3 instanceof AppBarLayout.BaseBehavior.SavedState) {
            AppBarLayout.BaseBehavior.SavedState var4 = (AppBarLayout.BaseBehavior.SavedState)var3;
            super.onRestoreInstanceState(var1, var2, var4.getSuperState());
            this.offsetToChildIndexOnLayout = var4.firstVisibleChildIndex;
            this.offsetToChildIndexOnLayoutPerc = var4.firstVisibleChildPercentageShown;
            this.offsetToChildIndexOnLayoutIsMinHeight = var4.firstVisibleChildAtMinimumHeight;
         } else {
            super.onRestoreInstanceState(var1, var2, var3);
            this.offsetToChildIndexOnLayout = -1;
         }
      }

      public Parcelable onSaveInstanceState(CoordinatorLayout var1, AppBarLayout var2) {
         Parcelable var8 = super.onSaveInstanceState(var1, var2);
         int var4 = this.getTopAndBottomOffset();
         int var3 = 0;

         for(int var5 = var2.getChildCount(); var3 < var5; ++var3) {
            View var9 = var2.getChildAt(var3);
            int var6 = var9.getBottom() + var4;
            if (var9.getTop() + var4 <= 0 && var6 >= 0) {
               AppBarLayout.BaseBehavior.SavedState var10 = new AppBarLayout.BaseBehavior.SavedState(var8);
               var10.firstVisibleChildIndex = var3;
               boolean var7;
               if (var6 == ViewCompat.getMinimumHeight(var9) + var2.getTopInset()) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               var10.firstVisibleChildAtMinimumHeight = var7;
               var10.firstVisibleChildPercentageShown = (float)var6 / (float)var9.getHeight();
               return var10;
            }
         }

         return var8;
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, View var4, int var5, int var6) {
         boolean var7;
         if ((var5 & 2) == 0 || !var2.isLiftOnScroll() && !this.canScrollChildren(var1, var2, var3)) {
            var7 = false;
         } else {
            var7 = true;
         }

         if (var7) {
            ValueAnimator var8 = this.offsetAnimator;
            if (var8 != null) {
               var8.cancel();
            }
         }

         this.lastNestedScrollingChildRef = null;
         this.lastStartedType = var6;
         return var7;
      }

      public void onStopNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4) {
         if (this.lastStartedType == 0 || var4 == 1) {
            this.snapToChildIfNeeded(var1, var2);
            if (var2.isLiftOnScroll()) {
               var2.setLiftedState(var2.shouldLift(var3));
            }
         }

         this.lastNestedScrollingChildRef = new WeakReference(var3);
      }

      public void setDragCallback(AppBarLayout.BaseBehavior.BaseDragCallback var1) {
         this.onDragCallback = var1;
      }

      int setHeaderTopBottomOffset(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5) {
         int var6 = this.getTopBottomOffsetForScrollingSibling();
         if (var4 != 0 && var6 >= var4 && var6 <= var5) {
            var4 = MathUtils.clamp(var3, var4, var5);
            if (var6 != var4) {
               if (var2.hasChildWithInterpolator()) {
                  var3 = this.interpolateOffset(var2, var4);
               } else {
                  var3 = var4;
               }

               boolean var7 = this.setTopAndBottomOffset(var3);
               this.offsetDelta = var4 - var3;
               if (!var7 && var2.hasChildWithInterpolator()) {
                  var1.dispatchDependentViewsChanged(var2);
               }

               var2.onOffsetChanged(this.getTopAndBottomOffset());
               byte var8;
               if (var4 < var6) {
                  var8 = -1;
               } else {
                  var8 = 1;
               }

               this.updateAppBarLayoutDrawableState(var1, var2, var4, var8, false);
               return var6 - var4;
            }
         } else {
            this.offsetDelta = 0;
         }

         return 0;
      }

      public abstract static class BaseDragCallback {
         public abstract boolean canDrag(AppBarLayout var1);
      }

      protected static class SavedState extends AbsSavedState {
         public static final Creator CREATOR = new ClassLoaderCreator() {
            public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel var1) {
               return new AppBarLayout.BaseBehavior.SavedState(var1, (ClassLoader)null);
            }

            public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
               return new AppBarLayout.BaseBehavior.SavedState(var1, var2);
            }

            public AppBarLayout.BaseBehavior.SavedState[] newArray(int var1) {
               return new AppBarLayout.BaseBehavior.SavedState[var1];
            }
         };
         boolean firstVisibleChildAtMinimumHeight;
         int firstVisibleChildIndex;
         float firstVisibleChildPercentageShown;

         public SavedState(Parcel var1, ClassLoader var2) {
            super(var1, var2);
            this.firstVisibleChildIndex = var1.readInt();
            this.firstVisibleChildPercentageShown = var1.readFloat();
            boolean var3;
            if (var1.readByte() != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.firstVisibleChildAtMinimumHeight = var3;
         }

         public SavedState(Parcelable var1) {
            super(var1);
         }

         public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.firstVisibleChildIndex);
            var1.writeFloat(this.firstVisibleChildPercentageShown);
            var1.writeByte((byte)this.firstVisibleChildAtMinimumHeight);
         }
      }
   }

   public interface BaseOnOffsetChangedListener {
      void onOffsetChanged(AppBarLayout var1, int var2);
   }

   public static class Behavior extends AppBarLayout.BaseBehavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public abstract static class DragCallback extends AppBarLayout.BaseBehavior.BaseDragCallback {
      }
   }

   public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
      static final int COLLAPSIBLE_FLAGS = 10;
      static final int FLAG_QUICK_RETURN = 5;
      static final int FLAG_SNAP = 17;
      public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
      public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
      public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
      public static final int SCROLL_FLAG_NO_SCROLL = 0;
      public static final int SCROLL_FLAG_SCROLL = 1;
      public static final int SCROLL_FLAG_SNAP = 16;
      public static final int SCROLL_FLAG_SNAP_MARGINS = 32;
      int scrollFlags = 1;
      Interpolator scrollInterpolator;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(int var1, int var2, float var3) {
         super(var1, var2, var3);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.AppBarLayout_Layout);
         this.scrollFlags = var3.getInt(styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
         if (var3.hasValue(styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
            this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(var1, var3.getResourceId(styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
         }

         var3.recycle();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.widget.LinearLayout.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(AppBarLayout.LayoutParams var1) {
         super(var1);
         this.scrollFlags = var1.scrollFlags;
         this.scrollInterpolator = var1.scrollInterpolator;
      }

      public int getScrollFlags() {
         return this.scrollFlags;
      }

      public Interpolator getScrollInterpolator() {
         return this.scrollInterpolator;
      }

      boolean isCollapsible() {
         int var1 = this.scrollFlags;
         return (var1 & 1) == 1 && (var1 & 10) != 0;
      }

      public void setScrollFlags(int var1) {
         this.scrollFlags = var1;
      }

      public void setScrollInterpolator(Interpolator var1) {
         this.scrollInterpolator = var1;
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface ScrollFlags {
      }
   }

   public interface OnOffsetChangedListener extends AppBarLayout.BaseOnOffsetChangedListener {
      void onOffsetChanged(AppBarLayout var1, int var2);
   }

   public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
      public ScrollingViewBehavior() {
      }

      public ScrollingViewBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.ScrollingViewBehavior_Layout);
         this.setOverlayTop(var3.getDimensionPixelSize(styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
         var3.recycle();
      }

      private static int getAppBarLayoutOffset(AppBarLayout var0) {
         CoordinatorLayout.Behavior var1 = ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).getBehavior();
         return var1 instanceof AppBarLayout.BaseBehavior ? ((AppBarLayout.BaseBehavior)var1).getTopBottomOffsetForScrollingSibling() : 0;
      }

      private void offsetChildAsNeeded(View var1, View var2) {
         CoordinatorLayout.Behavior var3 = ((CoordinatorLayout.LayoutParams)var2.getLayoutParams()).getBehavior();
         if (var3 instanceof AppBarLayout.BaseBehavior) {
            AppBarLayout.BaseBehavior var4 = (AppBarLayout.BaseBehavior)var3;
            ViewCompat.offsetTopAndBottom(var1, var2.getBottom() - var1.getTop() + var4.offsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(var2));
         }

      }

      private void updateLiftedStateIfNeeded(View var1, View var2) {
         if (var2 instanceof AppBarLayout) {
            AppBarLayout var3 = (AppBarLayout)var2;
            if (var3.isLiftOnScroll()) {
               var3.setLiftedState(var3.shouldLift(var1));
            }
         }

      }

      AppBarLayout findFirstDependency(List var1) {
         int var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            View var4 = (View)var1.get(var2);
            if (var4 instanceof AppBarLayout) {
               return (AppBarLayout)var4;
            }
         }

         return null;
      }

      float getOverlapRatioForOffset(View var1) {
         if (var1 instanceof AppBarLayout) {
            AppBarLayout var5 = (AppBarLayout)var1;
            int var3 = var5.getTotalScrollRange();
            int var4 = var5.getDownNestedPreScrollRange();
            int var2 = getAppBarLayoutOffset(var5);
            if (var4 != 0 && var3 + var2 <= var4) {
               return 0.0F;
            }

            var3 -= var4;
            if (var3 != 0) {
               return (float)var2 / (float)var3 + 1.0F;
            }
         }

         return 0.0F;
      }

      int getScrollRange(View var1) {
         return var1 instanceof AppBarLayout ? ((AppBarLayout)var1).getTotalScrollRange() : super.getScrollRange(var1);
      }

      public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
         return var3 instanceof AppBarLayout;
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, View var2, View var3) {
         this.offsetChildAsNeeded(var2, var3);
         this.updateLiftedStateIfNeeded(var2, var3);
         return false;
      }

      public boolean onRequestChildRectangleOnScreen(CoordinatorLayout var1, View var2, Rect var3, boolean var4) {
         AppBarLayout var5 = this.findFirstDependency(var1.getDependencies(var2));
         if (var5 != null) {
            var3.offset(var2.getLeft(), var2.getTop());
            Rect var6 = this.tempRect1;
            var6.set(0, 0, var1.getWidth(), var1.getHeight());
            if (!var6.contains(var3)) {
               var5.setExpanded(false, var4 ^ true);
               return true;
            }
         }

         return false;
      }
   }
}
