package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R.id;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;

public class CollapsingToolbarLayout extends FrameLayout {
   private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
   final CollapsingTextHelper collapsingTextHelper;
   private boolean collapsingTitleEnabled;
   private Drawable contentScrim;
   int currentOffset;
   private boolean drawCollapsingTitle;
   private View dummyView;
   private int expandedMarginBottom;
   private int expandedMarginEnd;
   private int expandedMarginStart;
   private int expandedMarginTop;
   WindowInsetsCompat lastInsets;
   private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;
   private boolean refreshToolbar;
   private int scrimAlpha;
   private long scrimAnimationDuration;
   private ValueAnimator scrimAnimator;
   private int scrimVisibleHeightTrigger;
   private boolean scrimsAreShown;
   Drawable statusBarScrim;
   private final Rect tmpRect;
   private Toolbar toolbar;
   private View toolbarDirectChild;
   private int toolbarId;

   public CollapsingToolbarLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CollapsingToolbarLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CollapsingToolbarLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.refreshToolbar = true;
      this.tmpRect = new Rect();
      this.scrimVisibleHeightTrigger = -1;
      CollapsingTextHelper var4 = new CollapsingTextHelper(this);
      this.collapsingTextHelper = var4;
      var4.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
      TypedArray var5 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.CollapsingToolbarLayout, var3, style.Widget_Design_CollapsingToolbar);
      this.collapsingTextHelper.setExpandedTextGravity(var5.getInt(styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
      this.collapsingTextHelper.setCollapsedTextGravity(var5.getInt(styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
      var3 = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
      this.expandedMarginBottom = var3;
      this.expandedMarginEnd = var3;
      this.expandedMarginTop = var3;
      this.expandedMarginStart = var3;
      if (var5.hasValue(styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
         this.expandedMarginStart = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
      }

      if (var5.hasValue(styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
         this.expandedMarginEnd = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
      }

      if (var5.hasValue(styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
         this.expandedMarginTop = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
      }

      if (var5.hasValue(styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
         this.expandedMarginBottom = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
      }

      this.collapsingTitleEnabled = var5.getBoolean(styleable.CollapsingToolbarLayout_titleEnabled, true);
      this.setTitle(var5.getText(styleable.CollapsingToolbarLayout_title));
      this.collapsingTextHelper.setExpandedTextAppearance(style.TextAppearance_Design_CollapsingToolbar_Expanded);
      this.collapsingTextHelper.setCollapsedTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
      if (var5.hasValue(styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
         this.collapsingTextHelper.setExpandedTextAppearance(var5.getResourceId(styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
      }

      if (var5.hasValue(styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) {
         this.collapsingTextHelper.setCollapsedTextAppearance(var5.getResourceId(styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0));
      }

      this.scrimVisibleHeightTrigger = var5.getDimensionPixelSize(styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
      this.scrimAnimationDuration = (long)var5.getInt(styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
      this.setContentScrim(var5.getDrawable(styleable.CollapsingToolbarLayout_contentScrim));
      this.setStatusBarScrim(var5.getDrawable(styleable.CollapsingToolbarLayout_statusBarScrim));
      this.toolbarId = var5.getResourceId(styleable.CollapsingToolbarLayout_toolbarId, -1);
      var5.recycle();
      this.setWillNotDraw(false);
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            return CollapsingToolbarLayout.this.onWindowInsetChanged(var2);
         }
      });
   }

   private void animateScrim(int var1) {
      this.ensureToolbar();
      ValueAnimator var2 = this.scrimAnimator;
      if (var2 == null) {
         var2 = new ValueAnimator();
         this.scrimAnimator = var2;
         var2.setDuration(this.scrimAnimationDuration);
         ValueAnimator var3 = this.scrimAnimator;
         TimeInterpolator var4;
         if (var1 > this.scrimAlpha) {
            var4 = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
         } else {
            var4 = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
         }

         var3.setInterpolator(var4);
         this.scrimAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               CollapsingToolbarLayout.this.setScrimAlpha((Integer)var1.getAnimatedValue());
            }
         });
      } else if (var2.isRunning()) {
         this.scrimAnimator.cancel();
      }

      this.scrimAnimator.setIntValues(new int[]{this.scrimAlpha, var1});
      this.scrimAnimator.start();
   }

   private void ensureToolbar() {
      if (this.refreshToolbar) {
         this.toolbar = null;
         this.toolbarDirectChild = null;
         int var1 = this.toolbarId;
         Toolbar var3;
         if (var1 != -1) {
            var3 = (Toolbar)this.findViewById(var1);
            this.toolbar = var3;
            if (var3 != null) {
               this.toolbarDirectChild = this.findDirectChild(var3);
            }
         }

         if (this.toolbar == null) {
            Object var4 = null;
            var1 = 0;
            int var2 = this.getChildCount();

            while(true) {
               var3 = (Toolbar)var4;
               if (var1 >= var2) {
                  break;
               }

               View var5 = this.getChildAt(var1);
               if (var5 instanceof Toolbar) {
                  var3 = (Toolbar)var5;
                  break;
               }

               ++var1;
            }

            this.toolbar = var3;
         }

         this.updateDummyView();
         this.refreshToolbar = false;
      }
   }

   private View findDirectChild(View var1) {
      View var2 = var1;

      for(ViewParent var3 = var1.getParent(); var3 != this && var3 != null; var3 = var3.getParent()) {
         if (var3 instanceof View) {
            var2 = (View)var3;
         }
      }

      return var2;
   }

   private static int getHeightWithMargins(View var0) {
      android.view.ViewGroup.LayoutParams var1 = var0.getLayoutParams();
      if (var1 instanceof MarginLayoutParams) {
         MarginLayoutParams var2 = (MarginLayoutParams)var1;
         return var0.getHeight() + var2.topMargin + var2.bottomMargin;
      } else {
         return var0.getHeight();
      }
   }

   static ViewOffsetHelper getViewOffsetHelper(View var0) {
      ViewOffsetHelper var2 = (ViewOffsetHelper)var0.getTag(id.view_offset_helper);
      ViewOffsetHelper var1 = var2;
      if (var2 == null) {
         var1 = new ViewOffsetHelper(var0);
         var0.setTag(id.view_offset_helper, var1);
      }

      return var1;
   }

   private boolean isToolbarChild(View var1) {
      View var2 = this.toolbarDirectChild;
      if (var2 != null && var2 != this) {
         if (var1 == var2) {
            return true;
         }
      } else if (var1 == this.toolbar) {
         return true;
      }

      return false;
   }

   private void updateContentDescriptionFromTitle() {
      this.setContentDescription(this.getTitle());
   }

   private void updateDummyView() {
      if (!this.collapsingTitleEnabled) {
         View var1 = this.dummyView;
         if (var1 != null) {
            ViewParent var2 = var1.getParent();
            if (var2 instanceof ViewGroup) {
               ((ViewGroup)var2).removeView(this.dummyView);
            }
         }
      }

      if (this.collapsingTitleEnabled && this.toolbar != null) {
         if (this.dummyView == null) {
            this.dummyView = new View(this.getContext());
         }

         if (this.dummyView.getParent() == null) {
            this.toolbar.addView(this.dummyView, -1, -1);
         }
      }

   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof CollapsingToolbarLayout.LayoutParams;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      this.ensureToolbar();
      if (this.toolbar == null) {
         Drawable var3 = this.contentScrim;
         if (var3 != null && this.scrimAlpha > 0) {
            var3.mutate().setAlpha(this.scrimAlpha);
            this.contentScrim.draw(var1);
         }
      }

      if (this.collapsingTitleEnabled && this.drawCollapsingTitle) {
         this.collapsingTextHelper.draw(var1);
      }

      if (this.statusBarScrim != null && this.scrimAlpha > 0) {
         WindowInsetsCompat var4 = this.lastInsets;
         int var2;
         if (var4 != null) {
            var2 = var4.getSystemWindowInsetTop();
         } else {
            var2 = 0;
         }

         if (var2 > 0) {
            this.statusBarScrim.setBounds(0, -this.currentOffset, this.getWidth(), var2 - this.currentOffset);
            this.statusBarScrim.mutate().setAlpha(this.scrimAlpha);
            this.statusBarScrim.draw(var1);
         }
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var6 = false;
      boolean var5 = var6;
      if (this.contentScrim != null) {
         var5 = var6;
         if (this.scrimAlpha > 0) {
            var5 = var6;
            if (this.isToolbarChild(var2)) {
               this.contentScrim.mutate().setAlpha(this.scrimAlpha);
               this.contentScrim.draw(var1);
               var5 = true;
            }
         }
      }

      return super.drawChild(var1, var2, var3) || var5;
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var3 = this.getDrawableState();
      boolean var2 = false;
      Drawable var4 = this.statusBarScrim;
      boolean var1 = var2;
      if (var4 != null) {
         var1 = var2;
         if (var4.isStateful()) {
            var1 = false | var4.setState(var3);
         }
      }

      var4 = this.contentScrim;
      var2 = var1;
      if (var4 != null) {
         var2 = var1;
         if (var4.isStateful()) {
            var2 = var1 | var4.setState(var3);
         }
      }

      CollapsingTextHelper var5 = this.collapsingTextHelper;
      var1 = var2;
      if (var5 != null) {
         var1 = var2 | var5.setState(var3);
      }

      if (var1) {
         this.invalidate();
      }

   }

   protected CollapsingToolbarLayout.LayoutParams generateDefaultLayoutParams() {
      return new CollapsingToolbarLayout.LayoutParams(-1, -1);
   }

   public android.widget.FrameLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new CollapsingToolbarLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.widget.FrameLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new CollapsingToolbarLayout.LayoutParams(var1);
   }

   public int getCollapsedTitleGravity() {
      return this.collapsingTextHelper.getCollapsedTextGravity();
   }

   public Typeface getCollapsedTitleTypeface() {
      return this.collapsingTextHelper.getCollapsedTypeface();
   }

   public Drawable getContentScrim() {
      return this.contentScrim;
   }

   public int getExpandedTitleGravity() {
      return this.collapsingTextHelper.getExpandedTextGravity();
   }

   public int getExpandedTitleMarginBottom() {
      return this.expandedMarginBottom;
   }

   public int getExpandedTitleMarginEnd() {
      return this.expandedMarginEnd;
   }

   public int getExpandedTitleMarginStart() {
      return this.expandedMarginStart;
   }

   public int getExpandedTitleMarginTop() {
      return this.expandedMarginTop;
   }

   public Typeface getExpandedTitleTypeface() {
      return this.collapsingTextHelper.getExpandedTypeface();
   }

   final int getMaxOffsetForPinChild(View var1) {
      ViewOffsetHelper var2 = getViewOffsetHelper(var1);
      CollapsingToolbarLayout.LayoutParams var3 = (CollapsingToolbarLayout.LayoutParams)var1.getLayoutParams();
      return this.getHeight() - var2.getLayoutTop() - var1.getHeight() - var3.bottomMargin;
   }

   int getScrimAlpha() {
      return this.scrimAlpha;
   }

   public long getScrimAnimationDuration() {
      return this.scrimAnimationDuration;
   }

   public int getScrimVisibleHeightTrigger() {
      int var1 = this.scrimVisibleHeightTrigger;
      if (var1 >= 0) {
         return var1;
      } else {
         WindowInsetsCompat var3 = this.lastInsets;
         if (var3 != null) {
            var1 = var3.getSystemWindowInsetTop();
         } else {
            var1 = 0;
         }

         int var2 = ViewCompat.getMinimumHeight(this);
         return var2 > 0 ? Math.min(var2 * 2 + var1, this.getHeight()) : this.getHeight() / 3;
      }
   }

   public Drawable getStatusBarScrim() {
      return this.statusBarScrim;
   }

   public CharSequence getTitle() {
      return this.collapsingTitleEnabled ? this.collapsingTextHelper.getText() : null;
   }

   public boolean isTitleEnabled() {
      return this.collapsingTitleEnabled;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      ViewParent var1 = this.getParent();
      if (var1 instanceof AppBarLayout) {
         ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View)var1));
         if (this.onOffsetChangedListener == null) {
            this.onOffsetChangedListener = new CollapsingToolbarLayout.OffsetUpdateListener();
         }

         ((AppBarLayout)var1).addOnOffsetChangedListener(this.onOffsetChangedListener);
         ViewCompat.requestApplyInsets(this);
      }

   }

   protected void onDetachedFromWindow() {
      ViewParent var1 = this.getParent();
      AppBarLayout.OnOffsetChangedListener var2 = this.onOffsetChangedListener;
      if (var2 != null && var1 instanceof AppBarLayout) {
         ((AppBarLayout)var1).removeOnOffsetChangedListener(var2);
      }

      super.onDetachedFromWindow();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      WindowInsetsCompat var14 = this.lastInsets;
      int var6;
      int var7;
      int var8;
      View var16;
      if (var14 != null) {
         var7 = var14.getSystemWindowInsetTop();
         var6 = 0;

         for(var8 = this.getChildCount(); var6 < var8; ++var6) {
            var16 = this.getChildAt(var6);
            if (!ViewCompat.getFitsSystemWindows(var16) && var16.getTop() < var7) {
               ViewCompat.offsetTopAndBottom(var16, var7);
            }
         }
      }

      var6 = 0;

      for(var7 = this.getChildCount(); var6 < var7; ++var6) {
         getViewOffsetHelper(this.getChildAt(var6)).onViewLayout();
      }

      if (this.collapsingTitleEnabled) {
         var16 = this.dummyView;
         if (var16 != null) {
            var1 = ViewCompat.isAttachedToWindow(var16);
            boolean var15 = false;
            if (var1 && this.dummyView.getVisibility() == 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.drawCollapsingTitle = var1;
            if (var1) {
               if (ViewCompat.getLayoutDirection(this) == 1) {
                  var15 = true;
               }

               Object var17 = this.toolbarDirectChild;
               if (var17 == null) {
                  var17 = this.toolbar;
               }

               int var9 = this.getMaxOffsetForPinChild((View)var17);
               DescendantOffsetUtils.getDescendantRect(this, this.dummyView, this.tmpRect);
               CollapsingTextHelper var18 = this.collapsingTextHelper;
               int var10 = this.tmpRect.left;
               if (var15) {
                  var7 = this.toolbar.getTitleMarginEnd();
               } else {
                  var7 = this.toolbar.getTitleMarginStart();
               }

               int var11 = this.tmpRect.top;
               int var12 = this.toolbar.getTitleMarginTop();
               int var13 = this.tmpRect.right;
               if (var15) {
                  var8 = this.toolbar.getTitleMarginStart();
               } else {
                  var8 = this.toolbar.getTitleMarginEnd();
               }

               var18.setCollapsedBounds(var10 + var7, var11 + var9 + var12, var13 + var8, this.tmpRect.bottom + var9 - this.toolbar.getTitleMarginBottom());
               var18 = this.collapsingTextHelper;
               if (var15) {
                  var7 = this.expandedMarginEnd;
               } else {
                  var7 = this.expandedMarginStart;
               }

               var8 = this.tmpRect.top;
               var9 = this.expandedMarginTop;
               if (var15) {
                  var6 = this.expandedMarginStart;
               } else {
                  var6 = this.expandedMarginEnd;
               }

               var18.setExpandedBounds(var7, var8 + var9, var4 - var2 - var6, var5 - var3 - this.expandedMarginBottom);
               this.collapsingTextHelper.recalculate();
            }
         }
      }

      if (this.toolbar != null) {
         if (this.collapsingTitleEnabled && TextUtils.isEmpty(this.collapsingTextHelper.getText())) {
            this.setTitle(this.toolbar.getTitle());
         }

         var16 = this.toolbarDirectChild;
         if (var16 != null && var16 != this) {
            this.setMinimumHeight(getHeightWithMargins(var16));
         } else {
            this.setMinimumHeight(getHeightWithMargins(this.toolbar));
         }
      }

      this.updateScrimVisibility();
      var2 = 0;

      for(var3 = this.getChildCount(); var2 < var3; ++var2) {
         getViewOffsetHelper(this.getChildAt(var2)).applyOffsets();
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.ensureToolbar();
      super.onMeasure(var1, var2);
      int var3 = MeasureSpec.getMode(var2);
      WindowInsetsCompat var4 = this.lastInsets;
      if (var4 != null) {
         var2 = var4.getSystemWindowInsetTop();
      } else {
         var2 = 0;
      }

      if (var3 == 0 && var2 > 0) {
         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() + var2, 1073741824));
      }

   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      Drawable var5 = this.contentScrim;
      if (var5 != null) {
         var5.setBounds(0, 0, var1, var2);
      }

   }

   WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat var1) {
      WindowInsetsCompat var2 = null;
      if (ViewCompat.getFitsSystemWindows(this)) {
         var2 = var1;
      }

      if (!ObjectsCompat.equals(this.lastInsets, var2)) {
         this.lastInsets = var2;
         this.requestLayout();
      }

      return var1.consumeSystemWindowInsets();
   }

   public void setCollapsedTitleGravity(int var1) {
      this.collapsingTextHelper.setCollapsedTextGravity(var1);
   }

   public void setCollapsedTitleTextAppearance(int var1) {
      this.collapsingTextHelper.setCollapsedTextAppearance(var1);
   }

   public void setCollapsedTitleTextColor(int var1) {
      this.setCollapsedTitleTextColor(ColorStateList.valueOf(var1));
   }

   public void setCollapsedTitleTextColor(ColorStateList var1) {
      this.collapsingTextHelper.setCollapsedTextColor(var1);
   }

   public void setCollapsedTitleTypeface(Typeface var1) {
      this.collapsingTextHelper.setCollapsedTypeface(var1);
   }

   public void setContentScrim(Drawable var1) {
      Drawable var3 = this.contentScrim;
      if (var3 != var1) {
         Drawable var2 = null;
         if (var3 != null) {
            var3.setCallback((Callback)null);
         }

         if (var1 != null) {
            var2 = var1.mutate();
         }

         this.contentScrim = var2;
         if (var2 != null) {
            var2.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.contentScrim.setCallback(this);
            this.contentScrim.setAlpha(this.scrimAlpha);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setContentScrimColor(int var1) {
      this.setContentScrim(new ColorDrawable(var1));
   }

   public void setContentScrimResource(int var1) {
      this.setContentScrim(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setExpandedTitleColor(int var1) {
      this.setExpandedTitleTextColor(ColorStateList.valueOf(var1));
   }

   public void setExpandedTitleGravity(int var1) {
      this.collapsingTextHelper.setExpandedTextGravity(var1);
   }

   public void setExpandedTitleMargin(int var1, int var2, int var3, int var4) {
      this.expandedMarginStart = var1;
      this.expandedMarginTop = var2;
      this.expandedMarginEnd = var3;
      this.expandedMarginBottom = var4;
      this.requestLayout();
   }

   public void setExpandedTitleMarginBottom(int var1) {
      this.expandedMarginBottom = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginEnd(int var1) {
      this.expandedMarginEnd = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginStart(int var1) {
      this.expandedMarginStart = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginTop(int var1) {
      this.expandedMarginTop = var1;
      this.requestLayout();
   }

   public void setExpandedTitleTextAppearance(int var1) {
      this.collapsingTextHelper.setExpandedTextAppearance(var1);
   }

   public void setExpandedTitleTextColor(ColorStateList var1) {
      this.collapsingTextHelper.setExpandedTextColor(var1);
   }

   public void setExpandedTitleTypeface(Typeface var1) {
      this.collapsingTextHelper.setExpandedTypeface(var1);
   }

   void setScrimAlpha(int var1) {
      if (var1 != this.scrimAlpha) {
         if (this.contentScrim != null) {
            Toolbar var2 = this.toolbar;
            if (var2 != null) {
               ViewCompat.postInvalidateOnAnimation(var2);
            }
         }

         this.scrimAlpha = var1;
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setScrimAnimationDuration(long var1) {
      this.scrimAnimationDuration = var1;
   }

   public void setScrimVisibleHeightTrigger(int var1) {
      if (this.scrimVisibleHeightTrigger != var1) {
         this.scrimVisibleHeightTrigger = var1;
         this.updateScrimVisibility();
      }

   }

   public void setScrimsShown(boolean var1) {
      boolean var2;
      if (ViewCompat.isLaidOut(this) && !this.isInEditMode()) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setScrimsShown(var1, var2);
   }

   public void setScrimsShown(boolean var1, boolean var2) {
      if (this.scrimsAreShown != var1) {
         short var3 = 255;
         if (var2) {
            if (!var1) {
               var3 = 0;
            }

            this.animateScrim(var3);
         } else {
            if (!var1) {
               var3 = 0;
            }

            this.setScrimAlpha(var3);
         }

         this.scrimsAreShown = var1;
      }

   }

   public void setStatusBarScrim(Drawable var1) {
      Drawable var4 = this.statusBarScrim;
      if (var4 != var1) {
         Drawable var3 = null;
         if (var4 != null) {
            var4.setCallback((Callback)null);
         }

         if (var1 != null) {
            var3 = var1.mutate();
         }

         this.statusBarScrim = var3;
         if (var3 != null) {
            if (var3.isStateful()) {
               this.statusBarScrim.setState(this.getDrawableState());
            }

            DrawableCompat.setLayoutDirection(this.statusBarScrim, ViewCompat.getLayoutDirection(this));
            var1 = this.statusBarScrim;
            boolean var2;
            if (this.getVisibility() == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.setVisible(var2, false);
            this.statusBarScrim.setCallback(this);
            this.statusBarScrim.setAlpha(this.scrimAlpha);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setStatusBarScrimColor(int var1) {
      this.setStatusBarScrim(new ColorDrawable(var1));
   }

   public void setStatusBarScrimResource(int var1) {
      this.setStatusBarScrim(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setTitle(CharSequence var1) {
      this.collapsingTextHelper.setText(var1);
      this.updateContentDescriptionFromTitle();
   }

   public void setTitleEnabled(boolean var1) {
      if (var1 != this.collapsingTitleEnabled) {
         this.collapsingTitleEnabled = var1;
         this.updateContentDescriptionFromTitle();
         this.updateDummyView();
         this.requestLayout();
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

      Drawable var3 = this.statusBarScrim;
      if (var3 != null && var3.isVisible() != var2) {
         this.statusBarScrim.setVisible(var2, false);
      }

      var3 = this.contentScrim;
      if (var3 != null && var3.isVisible() != var2) {
         this.contentScrim.setVisible(var2, false);
      }

   }

   final void updateScrimVisibility() {
      if (this.contentScrim != null || this.statusBarScrim != null) {
         boolean var1;
         if (this.getHeight() + this.currentOffset < this.getScrimVisibleHeightTrigger()) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.setScrimsShown(var1);
      }

   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.contentScrim || var1 == this.statusBarScrim;
   }

   public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
      public static final int COLLAPSE_MODE_OFF = 0;
      public static final int COLLAPSE_MODE_PARALLAX = 2;
      public static final int COLLAPSE_MODE_PIN = 1;
      private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5F;
      int collapseMode = 0;
      float parallaxMult = 0.5F;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(int var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.CollapsingToolbarLayout_Layout);
         this.collapseMode = var3.getInt(styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
         this.setParallaxMultiplier(var3.getFloat(styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5F));
         var3.recycle();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.widget.FrameLayout.LayoutParams var1) {
         super(var1);
      }

      public int getCollapseMode() {
         return this.collapseMode;
      }

      public float getParallaxMultiplier() {
         return this.parallaxMult;
      }

      public void setCollapseMode(int var1) {
         this.collapseMode = var1;
      }

      public void setParallaxMultiplier(float var1) {
         this.parallaxMult = var1;
      }
   }

   private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
      OffsetUpdateListener() {
      }

      public void onOffsetChanged(AppBarLayout var1, int var2) {
         CollapsingToolbarLayout.this.currentOffset = var2;
         int var3;
         if (CollapsingToolbarLayout.this.lastInsets != null) {
            var3 = CollapsingToolbarLayout.this.lastInsets.getSystemWindowInsetTop();
         } else {
            var3 = 0;
         }

         int var4 = 0;

         int var5;
         for(var5 = CollapsingToolbarLayout.this.getChildCount(); var4 < var5; ++var4) {
            View var9 = CollapsingToolbarLayout.this.getChildAt(var4);
            CollapsingToolbarLayout.LayoutParams var7 = (CollapsingToolbarLayout.LayoutParams)var9.getLayoutParams();
            ViewOffsetHelper var8 = CollapsingToolbarLayout.getViewOffsetHelper(var9);
            int var6 = var7.collapseMode;
            if (var6 != 1) {
               if (var6 == 2) {
                  var8.setTopAndBottomOffset(Math.round((float)(-var2) * var7.parallaxMult));
               }
            } else {
               var8.setTopAndBottomOffset(MathUtils.clamp(-var2, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(var9)));
            }
         }

         CollapsingToolbarLayout.this.updateScrimVisibility();
         if (CollapsingToolbarLayout.this.statusBarScrim != null && var3 > 0) {
            ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
         }

         var4 = CollapsingToolbarLayout.this.getHeight();
         var5 = ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this);
         CollapsingToolbarLayout.this.collapsingTextHelper.setExpansionFraction((float)Math.abs(var2) / (float)(var4 - var5 - var3));
      }
   }
}
