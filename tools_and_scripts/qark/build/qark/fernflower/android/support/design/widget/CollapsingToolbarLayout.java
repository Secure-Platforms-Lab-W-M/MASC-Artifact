package android.support.design.widget;

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
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R$id;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CollapsingToolbarLayout extends FrameLayout {
   private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
   final CollapsingTextHelper mCollapsingTextHelper;
   private boolean mCollapsingTitleEnabled;
   private Drawable mContentScrim;
   int mCurrentOffset;
   private boolean mDrawCollapsingTitle;
   private View mDummyView;
   private int mExpandedMarginBottom;
   private int mExpandedMarginEnd;
   private int mExpandedMarginStart;
   private int mExpandedMarginTop;
   WindowInsetsCompat mLastInsets;
   private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
   private boolean mRefreshToolbar;
   private int mScrimAlpha;
   private long mScrimAnimationDuration;
   private ValueAnimator mScrimAnimator;
   private int mScrimVisibleHeightTrigger;
   private boolean mScrimsAreShown;
   Drawable mStatusBarScrim;
   private final Rect mTmpRect;
   private Toolbar mToolbar;
   private View mToolbarDirectChild;
   private int mToolbarId;

   public CollapsingToolbarLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CollapsingToolbarLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CollapsingToolbarLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mRefreshToolbar = true;
      this.mTmpRect = new Rect();
      this.mScrimVisibleHeightTrigger = -1;
      ThemeUtils.checkAppCompatTheme(var1);
      this.mCollapsingTextHelper = new CollapsingTextHelper(this);
      this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.CollapsingToolbarLayout, var3, R$style.Widget_Design_CollapsingToolbar);
      this.mCollapsingTextHelper.setExpandedTextGravity(var4.getInt(R$styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
      this.mCollapsingTextHelper.setCollapsedTextGravity(var4.getInt(R$styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
      var3 = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
      this.mExpandedMarginBottom = var3;
      this.mExpandedMarginEnd = var3;
      this.mExpandedMarginTop = var3;
      this.mExpandedMarginStart = var3;
      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
         this.mExpandedMarginStart = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
      }

      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
         this.mExpandedMarginEnd = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
      }

      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
         this.mExpandedMarginTop = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
      }

      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
         this.mExpandedMarginBottom = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
      }

      this.mCollapsingTitleEnabled = var4.getBoolean(R$styleable.CollapsingToolbarLayout_titleEnabled, true);
      this.setTitle(var4.getText(R$styleable.CollapsingToolbarLayout_title));
      this.mCollapsingTextHelper.setExpandedTextAppearance(R$style.TextAppearance_Design_CollapsingToolbar_Expanded);
      this.mCollapsingTextHelper.setCollapsedTextAppearance(android.support.v7.appcompat.R$style.TextAppearance_AppCompat_Widget_ActionBar_Title);
      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
         this.mCollapsingTextHelper.setExpandedTextAppearance(var4.getResourceId(R$styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
      }

      if (var4.hasValue(R$styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) {
         this.mCollapsingTextHelper.setCollapsedTextAppearance(var4.getResourceId(R$styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0));
      }

      this.mScrimVisibleHeightTrigger = var4.getDimensionPixelSize(R$styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
      this.mScrimAnimationDuration = (long)var4.getInt(R$styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
      this.setContentScrim(var4.getDrawable(R$styleable.CollapsingToolbarLayout_contentScrim));
      this.setStatusBarScrim(var4.getDrawable(R$styleable.CollapsingToolbarLayout_statusBarScrim));
      this.mToolbarId = var4.getResourceId(R$styleable.CollapsingToolbarLayout_toolbarId, -1);
      var4.recycle();
      this.setWillNotDraw(false);
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            return CollapsingToolbarLayout.this.onWindowInsetChanged(var2);
         }
      });
   }

   private void animateScrim(int var1) {
      this.ensureToolbar();
      ValueAnimator var2 = this.mScrimAnimator;
      if (var2 == null) {
         this.mScrimAnimator = new ValueAnimator();
         this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
         ValueAnimator var3 = this.mScrimAnimator;
         Interpolator var4;
         if (var1 > this.mScrimAlpha) {
            var4 = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
         } else {
            var4 = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
         }

         var3.setInterpolator(var4);
         this.mScrimAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               CollapsingToolbarLayout.this.setScrimAlpha((Integer)var1.getAnimatedValue());
            }
         });
      } else if (var2.isRunning()) {
         this.mScrimAnimator.cancel();
      }

      this.mScrimAnimator.setIntValues(new int[]{this.mScrimAlpha, var1});
      this.mScrimAnimator.start();
   }

   private void ensureToolbar() {
      if (this.mRefreshToolbar) {
         this.mToolbar = null;
         this.mToolbarDirectChild = null;
         int var1 = this.mToolbarId;
         Toolbar var3;
         if (var1 != -1) {
            this.mToolbar = (Toolbar)this.findViewById(var1);
            var3 = this.mToolbar;
            if (var3 != null) {
               this.mToolbarDirectChild = this.findDirectChild(var3);
            }
         }

         if (this.mToolbar == null) {
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

            this.mToolbar = var3;
         }

         this.updateDummyView();
         this.mRefreshToolbar = false;
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

   private static int getHeightWithMargins(@NonNull View var0) {
      android.view.ViewGroup.LayoutParams var1 = var0.getLayoutParams();
      if (var1 instanceof MarginLayoutParams) {
         MarginLayoutParams var2 = (MarginLayoutParams)var1;
         return var0.getHeight() + var2.topMargin + var2.bottomMargin;
      } else {
         return var0.getHeight();
      }
   }

   static ViewOffsetHelper getViewOffsetHelper(View var0) {
      ViewOffsetHelper var1 = (ViewOffsetHelper)var0.getTag(R$id.view_offset_helper);
      if (var1 == null) {
         var1 = new ViewOffsetHelper(var0);
         var0.setTag(R$id.view_offset_helper, var1);
         return var1;
      } else {
         return var1;
      }
   }

   private boolean isToolbarChild(View var1) {
      View var2 = this.mToolbarDirectChild;
      if (var2 != null && var2 != this) {
         if (var1 == var2) {
            return true;
         }
      } else if (var1 == this.mToolbar) {
         return true;
      }

      return false;
   }

   private void updateDummyView() {
      if (!this.mCollapsingTitleEnabled) {
         View var1 = this.mDummyView;
         if (var1 != null) {
            ViewParent var2 = var1.getParent();
            if (var2 instanceof ViewGroup) {
               ((ViewGroup)var2).removeView(this.mDummyView);
            }
         }
      }

      if (this.mCollapsingTitleEnabled && this.mToolbar != null) {
         if (this.mDummyView == null) {
            this.mDummyView = new View(this.getContext());
         }

         if (this.mDummyView.getParent() == null) {
            this.mToolbar.addView(this.mDummyView, -1, -1);
         }
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof CollapsingToolbarLayout.LayoutParams;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      this.ensureToolbar();
      if (this.mToolbar == null) {
         Drawable var3 = this.mContentScrim;
         if (var3 != null && this.mScrimAlpha > 0) {
            var3.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(var1);
         }
      }

      if (this.mCollapsingTitleEnabled && this.mDrawCollapsingTitle) {
         this.mCollapsingTextHelper.draw(var1);
      }

      if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
         WindowInsetsCompat var4 = this.mLastInsets;
         int var2;
         if (var4 != null) {
            var2 = var4.getSystemWindowInsetTop();
         } else {
            var2 = 0;
         }

         if (var2 > 0) {
            this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, this.getWidth(), var2 - this.mCurrentOffset);
            this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mStatusBarScrim.draw(var1);
         }
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = false;
      if (this.mContentScrim != null && this.mScrimAlpha > 0 && this.isToolbarChild(var2)) {
         this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
         this.mContentScrim.draw(var1);
         var5 = true;
      }

      return super.drawChild(var1, var2, var3) || var5;
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var2 = this.getDrawableState();
      boolean var1 = false;
      Drawable var3 = this.mStatusBarScrim;
      if (var3 != null && var3.isStateful()) {
         var1 = false | var3.setState(var2);
      }

      var3 = this.mContentScrim;
      if (var3 != null && var3.isStateful()) {
         var1 |= var3.setState(var2);
      }

      CollapsingTextHelper var4 = this.mCollapsingTextHelper;
      if (var4 != null) {
         var1 |= var4.setState(var2);
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
      return this.mCollapsingTextHelper.getCollapsedTextGravity();
   }

   @NonNull
   public Typeface getCollapsedTitleTypeface() {
      return this.mCollapsingTextHelper.getCollapsedTypeface();
   }

   @Nullable
   public Drawable getContentScrim() {
      return this.mContentScrim;
   }

   public int getExpandedTitleGravity() {
      return this.mCollapsingTextHelper.getExpandedTextGravity();
   }

   public int getExpandedTitleMarginBottom() {
      return this.mExpandedMarginBottom;
   }

   public int getExpandedTitleMarginEnd() {
      return this.mExpandedMarginEnd;
   }

   public int getExpandedTitleMarginStart() {
      return this.mExpandedMarginStart;
   }

   public int getExpandedTitleMarginTop() {
      return this.mExpandedMarginTop;
   }

   @NonNull
   public Typeface getExpandedTitleTypeface() {
      return this.mCollapsingTextHelper.getExpandedTypeface();
   }

   final int getMaxOffsetForPinChild(View var1) {
      ViewOffsetHelper var2 = getViewOffsetHelper(var1);
      CollapsingToolbarLayout.LayoutParams var3 = (CollapsingToolbarLayout.LayoutParams)var1.getLayoutParams();
      return this.getHeight() - var2.getLayoutTop() - var1.getHeight() - var3.bottomMargin;
   }

   int getScrimAlpha() {
      return this.mScrimAlpha;
   }

   public long getScrimAnimationDuration() {
      return this.mScrimAnimationDuration;
   }

   public int getScrimVisibleHeightTrigger() {
      int var1 = this.mScrimVisibleHeightTrigger;
      if (var1 >= 0) {
         return var1;
      } else {
         WindowInsetsCompat var3 = this.mLastInsets;
         if (var3 != null) {
            var1 = var3.getSystemWindowInsetTop();
         } else {
            var1 = 0;
         }

         int var2 = ViewCompat.getMinimumHeight(this);
         return var2 > 0 ? Math.min(var2 * 2 + var1, this.getHeight()) : this.getHeight() / 3;
      }
   }

   @Nullable
   public Drawable getStatusBarScrim() {
      return this.mStatusBarScrim;
   }

   @Nullable
   public CharSequence getTitle() {
      return this.mCollapsingTitleEnabled ? this.mCollapsingTextHelper.getText() : null;
   }

   public boolean isTitleEnabled() {
      return this.mCollapsingTitleEnabled;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      ViewParent var1 = this.getParent();
      if (var1 instanceof AppBarLayout) {
         ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View)var1));
         if (this.mOnOffsetChangedListener == null) {
            this.mOnOffsetChangedListener = new CollapsingToolbarLayout.OffsetUpdateListener();
         }

         ((AppBarLayout)var1).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
         ViewCompat.requestApplyInsets(this);
      }
   }

   protected void onDetachedFromWindow() {
      ViewParent var1 = this.getParent();
      AppBarLayout.OnOffsetChangedListener var2 = this.mOnOffsetChangedListener;
      if (var2 != null && var1 instanceof AppBarLayout) {
         ((AppBarLayout)var1).removeOnOffsetChangedListener(var2);
      }

      super.onDetachedFromWindow();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      WindowInsetsCompat var14 = this.mLastInsets;
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

      if (this.mCollapsingTitleEnabled) {
         var16 = this.mDummyView;
         if (var16 != null) {
            var1 = ViewCompat.isAttachedToWindow(var16);
            boolean var15 = false;
            if (var1 && this.mDummyView.getVisibility() == 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.mDrawCollapsingTitle = var1;
            if (this.mDrawCollapsingTitle) {
               if (ViewCompat.getLayoutDirection(this) == 1) {
                  var15 = true;
               }

               Object var17 = this.mToolbarDirectChild;
               if (var17 == null) {
                  var17 = this.mToolbar;
               }

               int var9 = this.getMaxOffsetForPinChild((View)var17);
               ViewGroupUtils.getDescendantRect(this, this.mDummyView, this.mTmpRect);
               CollapsingTextHelper var18 = this.mCollapsingTextHelper;
               int var10 = this.mTmpRect.left;
               if (var15) {
                  var7 = this.mToolbar.getTitleMarginEnd();
               } else {
                  var7 = this.mToolbar.getTitleMarginStart();
               }

               int var11 = this.mTmpRect.top;
               int var12 = this.mToolbar.getTitleMarginTop();
               int var13 = this.mTmpRect.right;
               if (var15) {
                  var8 = this.mToolbar.getTitleMarginStart();
               } else {
                  var8 = this.mToolbar.getTitleMarginEnd();
               }

               var18.setCollapsedBounds(var10 + var7, var11 + var9 + var12, var13 + var8, this.mTmpRect.bottom + var9 - this.mToolbar.getTitleMarginBottom());
               var18 = this.mCollapsingTextHelper;
               if (var15) {
                  var7 = this.mExpandedMarginEnd;
               } else {
                  var7 = this.mExpandedMarginStart;
               }

               var8 = this.mTmpRect.top;
               var9 = this.mExpandedMarginTop;
               if (var15) {
                  var6 = this.mExpandedMarginStart;
               } else {
                  var6 = this.mExpandedMarginEnd;
               }

               var18.setExpandedBounds(var7, var8 + var9, var4 - var2 - var6, var5 - var3 - this.mExpandedMarginBottom);
               this.mCollapsingTextHelper.recalculate();
            }
         }
      }

      var2 = 0;

      for(var3 = this.getChildCount(); var2 < var3; ++var2) {
         getViewOffsetHelper(this.getChildAt(var2)).onViewLayout();
      }

      if (this.mToolbar != null) {
         if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
            this.mCollapsingTextHelper.setText(this.mToolbar.getTitle());
         }

         var16 = this.mToolbarDirectChild;
         if (var16 != null && var16 != this) {
            this.setMinimumHeight(getHeightWithMargins(var16));
         } else {
            this.setMinimumHeight(getHeightWithMargins(this.mToolbar));
         }
      }

      this.updateScrimVisibility();
   }

   protected void onMeasure(int var1, int var2) {
      this.ensureToolbar();
      super.onMeasure(var1, var2);
      int var3 = MeasureSpec.getMode(var2);
      WindowInsetsCompat var4 = this.mLastInsets;
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
      Drawable var5 = this.mContentScrim;
      if (var5 != null) {
         var5.setBounds(0, 0, var1, var2);
      }
   }

   WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat var1) {
      WindowInsetsCompat var2 = null;
      if (ViewCompat.getFitsSystemWindows(this)) {
         var2 = var1;
      }

      if (!ObjectsCompat.equals(this.mLastInsets, var2)) {
         this.mLastInsets = var2;
         this.requestLayout();
      }

      return var1.consumeSystemWindowInsets();
   }

   public void setCollapsedTitleGravity(int var1) {
      this.mCollapsingTextHelper.setCollapsedTextGravity(var1);
   }

   public void setCollapsedTitleTextAppearance(@StyleRes int var1) {
      this.mCollapsingTextHelper.setCollapsedTextAppearance(var1);
   }

   public void setCollapsedTitleTextColor(@ColorInt int var1) {
      this.setCollapsedTitleTextColor(ColorStateList.valueOf(var1));
   }

   public void setCollapsedTitleTextColor(@NonNull ColorStateList var1) {
      this.mCollapsingTextHelper.setCollapsedTextColor(var1);
   }

   public void setCollapsedTitleTypeface(@Nullable Typeface var1) {
      this.mCollapsingTextHelper.setCollapsedTypeface(var1);
   }

   public void setContentScrim(@Nullable Drawable var1) {
      Drawable var3 = this.mContentScrim;
      if (var3 != var1) {
         Drawable var2 = null;
         if (var3 != null) {
            var3.setCallback((Callback)null);
         }

         if (var1 != null) {
            var2 = var1.mutate();
         }

         this.mContentScrim = var2;
         var1 = this.mContentScrim;
         if (var1 != null) {
            var1.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.mContentScrim.setCallback(this);
            this.mContentScrim.setAlpha(this.mScrimAlpha);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }
   }

   public void setContentScrimColor(@ColorInt int var1) {
      this.setContentScrim(new ColorDrawable(var1));
   }

   public void setContentScrimResource(@DrawableRes int var1) {
      this.setContentScrim(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setExpandedTitleColor(@ColorInt int var1) {
      this.setExpandedTitleTextColor(ColorStateList.valueOf(var1));
   }

   public void setExpandedTitleGravity(int var1) {
      this.mCollapsingTextHelper.setExpandedTextGravity(var1);
   }

   public void setExpandedTitleMargin(int var1, int var2, int var3, int var4) {
      this.mExpandedMarginStart = var1;
      this.mExpandedMarginTop = var2;
      this.mExpandedMarginEnd = var3;
      this.mExpandedMarginBottom = var4;
      this.requestLayout();
   }

   public void setExpandedTitleMarginBottom(int var1) {
      this.mExpandedMarginBottom = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginEnd(int var1) {
      this.mExpandedMarginEnd = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginStart(int var1) {
      this.mExpandedMarginStart = var1;
      this.requestLayout();
   }

   public void setExpandedTitleMarginTop(int var1) {
      this.mExpandedMarginTop = var1;
      this.requestLayout();
   }

   public void setExpandedTitleTextAppearance(@StyleRes int var1) {
      this.mCollapsingTextHelper.setExpandedTextAppearance(var1);
   }

   public void setExpandedTitleTextColor(@NonNull ColorStateList var1) {
      this.mCollapsingTextHelper.setExpandedTextColor(var1);
   }

   public void setExpandedTitleTypeface(@Nullable Typeface var1) {
      this.mCollapsingTextHelper.setExpandedTypeface(var1);
   }

   void setScrimAlpha(int var1) {
      if (var1 != this.mScrimAlpha) {
         if (this.mContentScrim != null) {
            Toolbar var2 = this.mToolbar;
            if (var2 != null) {
               ViewCompat.postInvalidateOnAnimation(var2);
            }
         }

         this.mScrimAlpha = var1;
         ViewCompat.postInvalidateOnAnimation(this);
      }
   }

   public void setScrimAnimationDuration(@IntRange(from = 0L) long var1) {
      this.mScrimAnimationDuration = var1;
   }

   public void setScrimVisibleHeightTrigger(@IntRange(from = 0L) int var1) {
      if (this.mScrimVisibleHeightTrigger != var1) {
         this.mScrimVisibleHeightTrigger = var1;
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
      if (this.mScrimsAreShown != var1) {
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

         this.mScrimsAreShown = var1;
      }
   }

   public void setStatusBarScrim(@Nullable Drawable var1) {
      Drawable var4 = this.mStatusBarScrim;
      if (var4 != var1) {
         Drawable var3 = null;
         if (var4 != null) {
            var4.setCallback((Callback)null);
         }

         if (var1 != null) {
            var3 = var1.mutate();
         }

         this.mStatusBarScrim = var3;
         var1 = this.mStatusBarScrim;
         if (var1 != null) {
            if (var1.isStateful()) {
               this.mStatusBarScrim.setState(this.getDrawableState());
            }

            DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection(this));
            var1 = this.mStatusBarScrim;
            boolean var2;
            if (this.getVisibility() == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.setVisible(var2, false);
            this.mStatusBarScrim.setCallback(this);
            this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }
   }

   public void setStatusBarScrimColor(@ColorInt int var1) {
      this.setStatusBarScrim(new ColorDrawable(var1));
   }

   public void setStatusBarScrimResource(@DrawableRes int var1) {
      this.setStatusBarScrim(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setTitle(@Nullable CharSequence var1) {
      this.mCollapsingTextHelper.setText(var1);
   }

   public void setTitleEnabled(boolean var1) {
      if (var1 != this.mCollapsingTitleEnabled) {
         this.mCollapsingTitleEnabled = var1;
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

      Drawable var3 = this.mStatusBarScrim;
      if (var3 != null && var3.isVisible() != var2) {
         this.mStatusBarScrim.setVisible(var2, false);
      }

      var3 = this.mContentScrim;
      if (var3 != null && var3.isVisible() != var2) {
         this.mContentScrim.setVisible(var2, false);
      }
   }

   final void updateScrimVisibility() {
      if (this.mContentScrim != null || this.mStatusBarScrim != null) {
         boolean var1;
         if (this.getHeight() + this.mCurrentOffset < this.getScrimVisibleHeightTrigger()) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.setScrimsShown(var1);
      }
   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.mContentScrim || var1 == this.mStatusBarScrim;
   }

   public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
      public static final int COLLAPSE_MODE_OFF = 0;
      public static final int COLLAPSE_MODE_PARALLAX = 2;
      public static final int COLLAPSE_MODE_PIN = 1;
      private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5F;
      int mCollapseMode = 0;
      float mParallaxMult = 0.5F;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(int var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.CollapsingToolbarLayout_Layout);
         this.mCollapseMode = var3.getInt(R$styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
         this.setParallaxMultiplier(var3.getFloat(R$styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5F));
         var3.recycle();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      @RequiresApi(19)
      public LayoutParams(android.widget.FrameLayout.LayoutParams var1) {
         super(var1);
      }

      public int getCollapseMode() {
         return this.mCollapseMode;
      }

      public float getParallaxMultiplier() {
         return this.mParallaxMult;
      }

      public void setCollapseMode(int var1) {
         this.mCollapseMode = var1;
      }

      public void setParallaxMultiplier(float var1) {
         this.mParallaxMult = var1;
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      @interface CollapseMode {
      }
   }

   private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
      OffsetUpdateListener() {
      }

      public void onOffsetChanged(AppBarLayout var1, int var2) {
         CollapsingToolbarLayout var8 = CollapsingToolbarLayout.this;
         var8.mCurrentOffset = var2;
         int var3;
         if (var8.mLastInsets != null) {
            var3 = CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop();
         } else {
            var3 = 0;
         }

         int var4 = 0;

         int var5;
         for(var5 = CollapsingToolbarLayout.this.getChildCount(); var4 < var5; ++var4) {
            View var9 = CollapsingToolbarLayout.this.getChildAt(var4);
            CollapsingToolbarLayout.LayoutParams var6 = (CollapsingToolbarLayout.LayoutParams)var9.getLayoutParams();
            ViewOffsetHelper var7 = CollapsingToolbarLayout.getViewOffsetHelper(var9);
            switch(var6.mCollapseMode) {
            case 1:
               var7.setTopAndBottomOffset(MathUtils.clamp(-var2, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(var9)));
               break;
            case 2:
               var7.setTopAndBottomOffset(Math.round((float)(-var2) * var6.mParallaxMult));
            }
         }

         CollapsingToolbarLayout.this.updateScrimVisibility();
         if (CollapsingToolbarLayout.this.mStatusBarScrim != null && var3 > 0) {
            ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
         }

         var4 = CollapsingToolbarLayout.this.getHeight();
         var5 = ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this);
         CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction((float)Math.abs(var2) / (float)(var4 - var5 - var3));
      }
   }
}
