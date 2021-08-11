package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R$dimen;
import android.support.design.R$layout;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout extends HorizontalScrollView {
   private static final int ANIMATION_DURATION = 300;
   static final int DEFAULT_GAP_TEXT_ICON = 8;
   private static final int DEFAULT_HEIGHT = 48;
   private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
   static final int FIXED_WRAP_GUTTER_MIN = 16;
   public static final int GRAVITY_CENTER = 1;
   public static final int GRAVITY_FILL = 0;
   private static final int INVALID_WIDTH = -1;
   public static final int MODE_FIXED = 1;
   public static final int MODE_SCROLLABLE = 0;
   static final int MOTION_NON_ADJACENT_OFFSET = 24;
   private static final int TAB_MIN_WIDTH_MARGIN = 56;
   private static final Pools.Pool sTabPool = new Pools.SynchronizedPool(16);
   private TabLayout.AdapterChangeListener mAdapterChangeListener;
   private int mContentInsetStart;
   private TabLayout.OnTabSelectedListener mCurrentVpSelectedListener;
   int mMode;
   private TabLayout.TabLayoutOnPageChangeListener mPageChangeListener;
   private PagerAdapter mPagerAdapter;
   private DataSetObserver mPagerAdapterObserver;
   private final int mRequestedTabMaxWidth;
   private final int mRequestedTabMinWidth;
   private ValueAnimator mScrollAnimator;
   private final int mScrollableTabMinWidth;
   private TabLayout.OnTabSelectedListener mSelectedListener;
   private final ArrayList mSelectedListeners;
   private TabLayout.Tab mSelectedTab;
   private boolean mSetupViewPagerImplicitly;
   final int mTabBackgroundResId;
   int mTabGravity;
   int mTabMaxWidth;
   int mTabPaddingBottom;
   int mTabPaddingEnd;
   int mTabPaddingStart;
   int mTabPaddingTop;
   private final TabLayout.SlidingTabStrip mTabStrip;
   int mTabTextAppearance;
   ColorStateList mTabTextColors;
   float mTabTextMultiLineSize;
   float mTabTextSize;
   private final Pools.Pool mTabViewPool;
   private final ArrayList mTabs;
   ViewPager mViewPager;

   public TabLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TabLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public TabLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mTabs = new ArrayList();
      this.mTabMaxWidth = Integer.MAX_VALUE;
      this.mSelectedListeners = new ArrayList();
      this.mTabViewPool = new Pools.SimplePool(12);
      ThemeUtils.checkAppCompatTheme(var1);
      this.setHorizontalScrollBarEnabled(false);
      this.mTabStrip = new TabLayout.SlidingTabStrip(var1);
      super.addView(this.mTabStrip, 0, new LayoutParams(-2, -1));
      TypedArray var8 = var1.obtainStyledAttributes(var2, R$styleable.TabLayout, var3, R$style.Widget_Design_TabLayout);
      this.mTabStrip.setSelectedIndicatorHeight(var8.getDimensionPixelSize(R$styleable.TabLayout_tabIndicatorHeight, 0));
      this.mTabStrip.setSelectedIndicatorColor(var8.getColor(R$styleable.TabLayout_tabIndicatorColor, 0));
      var3 = var8.getDimensionPixelSize(R$styleable.TabLayout_tabPadding, 0);
      this.mTabPaddingBottom = var3;
      this.mTabPaddingEnd = var3;
      this.mTabPaddingTop = var3;
      this.mTabPaddingStart = var3;
      this.mTabPaddingStart = var8.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
      this.mTabPaddingTop = var8.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
      this.mTabPaddingEnd = var8.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
      this.mTabPaddingBottom = var8.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
      this.mTabTextAppearance = var8.getResourceId(R$styleable.TabLayout_tabTextAppearance, R$style.TextAppearance_Design_Tab);
      TypedArray var6 = var1.obtainStyledAttributes(this.mTabTextAppearance, android.support.v7.appcompat.R$styleable.TextAppearance);

      try {
         this.mTabTextSize = (float)var6.getDimensionPixelSize(android.support.v7.appcompat.R$styleable.TextAppearance_android_textSize, 0);
         this.mTabTextColors = var6.getColorStateList(android.support.v7.appcompat.R$styleable.TextAppearance_android_textColor);
      } finally {
         var6.recycle();
      }

      if (var8.hasValue(R$styleable.TabLayout_tabTextColor)) {
         this.mTabTextColors = var8.getColorStateList(R$styleable.TabLayout_tabTextColor);
      }

      if (var8.hasValue(R$styleable.TabLayout_tabSelectedTextColor)) {
         var3 = var8.getColor(R$styleable.TabLayout_tabSelectedTextColor, 0);
         this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), var3);
      }

      this.mRequestedTabMinWidth = var8.getDimensionPixelSize(R$styleable.TabLayout_tabMinWidth, -1);
      this.mRequestedTabMaxWidth = var8.getDimensionPixelSize(R$styleable.TabLayout_tabMaxWidth, -1);
      this.mTabBackgroundResId = var8.getResourceId(R$styleable.TabLayout_tabBackground, 0);
      this.mContentInsetStart = var8.getDimensionPixelSize(R$styleable.TabLayout_tabContentStart, 0);
      this.mMode = var8.getInt(R$styleable.TabLayout_tabMode, 1);
      this.mTabGravity = var8.getInt(R$styleable.TabLayout_tabGravity, 0);
      var8.recycle();
      Resources var7 = this.getResources();
      this.mTabTextMultiLineSize = (float)var7.getDimensionPixelSize(R$dimen.design_tab_text_size_2line);
      this.mScrollableTabMinWidth = var7.getDimensionPixelSize(R$dimen.design_tab_scrollable_min_width);
      this.applyModeAndGravity();
   }

   private void addTabFromItemView(@NonNull TabItem var1) {
      TabLayout.Tab var2 = this.newTab();
      if (var1.mText != null) {
         var2.setText(var1.mText);
      }

      if (var1.mIcon != null) {
         var2.setIcon(var1.mIcon);
      }

      if (var1.mCustomLayout != 0) {
         var2.setCustomView(var1.mCustomLayout);
      }

      if (!TextUtils.isEmpty(var1.getContentDescription())) {
         var2.setContentDescription(var1.getContentDescription());
      }

      this.addTab(var2);
   }

   private void addTabView(TabLayout.Tab var1) {
      TabLayout.TabView var2 = var1.mView;
      this.mTabStrip.addView(var2, var1.getPosition(), this.createLayoutParamsForTabs());
   }

   private void addViewInternal(View var1) {
      if (var1 instanceof TabItem) {
         this.addTabFromItemView((TabItem)var1);
      } else {
         throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
      }
   }

   private void animateToTab(int var1) {
      if (var1 != -1) {
         if (this.getWindowToken() != null && ViewCompat.isLaidOut(this) && !this.mTabStrip.childrenNeedLayout()) {
            int var2 = this.getScrollX();
            int var3 = this.calculateScrollXForTab(var1, 0.0F);
            if (var2 != var3) {
               this.ensureScrollAnimator();
               this.mScrollAnimator.setIntValues(new int[]{var2, var3});
               this.mScrollAnimator.start();
            }

            this.mTabStrip.animateIndicatorToPosition(var1, 300);
         } else {
            this.setScrollPosition(var1, 0.0F, true);
         }
      }
   }

   private void applyModeAndGravity() {
      int var1 = 0;
      if (this.mMode == 0) {
         var1 = Math.max(0, this.mContentInsetStart - this.mTabPaddingStart);
      }

      ViewCompat.setPaddingRelative(this.mTabStrip, var1, 0, 0, 0);
      switch(this.mMode) {
      case 0:
         this.mTabStrip.setGravity(8388611);
         break;
      case 1:
         this.mTabStrip.setGravity(1);
      }

      this.updateTabViews(true);
   }

   private int calculateScrollXForTab(int var1, float var2) {
      int var4 = this.mMode;
      int var3 = 0;
      if (var4 == 0) {
         View var6 = this.mTabStrip.getChildAt(var1);
         View var5;
         if (var1 + 1 < this.mTabStrip.getChildCount()) {
            var5 = this.mTabStrip.getChildAt(var1 + 1);
         } else {
            var5 = null;
         }

         if (var6 != null) {
            var1 = var6.getWidth();
         } else {
            var1 = 0;
         }

         if (var5 != null) {
            var3 = var5.getWidth();
         }

         var4 = var6.getLeft() + var1 / 2 - this.getWidth() / 2;
         var1 = (int)((float)(var1 + var3) * 0.5F * var2);
         return ViewCompat.getLayoutDirection(this) == 0 ? var4 + var1 : var4 - var1;
      } else {
         return 0;
      }
   }

   private void configureTab(TabLayout.Tab var1, int var2) {
      var1.setPosition(var2);
      this.mTabs.add(var2, var1);
      int var3 = this.mTabs.size();
      ++var2;

      while(var2 < var3) {
         ((TabLayout.Tab)this.mTabs.get(var2)).setPosition(var2);
         ++var2;
      }

   }

   private static ColorStateList createColorStateList(int var0, int var1) {
      int[][] var2 = new int[2][];
      int[] var3 = new int[2];
      var2[0] = SELECTED_STATE_SET;
      var3[0] = var1;
      var1 = 0 + 1;
      var2[var1] = EMPTY_STATE_SET;
      var3[var1] = var0;
      return new ColorStateList(var2, var3);
   }

   private android.widget.LinearLayout.LayoutParams createLayoutParamsForTabs() {
      android.widget.LinearLayout.LayoutParams var1 = new android.widget.LinearLayout.LayoutParams(-2, -1);
      this.updateTabViewLayoutParams(var1);
      return var1;
   }

   private TabLayout.TabView createTabView(@NonNull TabLayout.Tab var1) {
      Pools.Pool var2 = this.mTabViewPool;
      TabLayout.TabView var3;
      if (var2 != null) {
         var3 = (TabLayout.TabView)var2.acquire();
      } else {
         var3 = null;
      }

      if (var3 == null) {
         var3 = new TabLayout.TabView(this.getContext());
      }

      var3.setTab(var1);
      var3.setFocusable(true);
      var3.setMinimumWidth(this.getTabMinWidth());
      return var3;
   }

   private void dispatchTabReselected(@NonNull TabLayout.Tab var1) {
      for(int var2 = this.mSelectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.OnTabSelectedListener)this.mSelectedListeners.get(var2)).onTabReselected(var1);
      }

   }

   private void dispatchTabSelected(@NonNull TabLayout.Tab var1) {
      for(int var2 = this.mSelectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.OnTabSelectedListener)this.mSelectedListeners.get(var2)).onTabSelected(var1);
      }

   }

   private void dispatchTabUnselected(@NonNull TabLayout.Tab var1) {
      for(int var2 = this.mSelectedListeners.size() - 1; var2 >= 0; --var2) {
         ((TabLayout.OnTabSelectedListener)this.mSelectedListeners.get(var2)).onTabUnselected(var1);
      }

   }

   private void ensureScrollAnimator() {
      if (this.mScrollAnimator == null) {
         this.mScrollAnimator = new ValueAnimator();
         this.mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
         this.mScrollAnimator.setDuration(300L);
         this.mScrollAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               TabLayout.this.scrollTo((Integer)var1.getAnimatedValue(), 0);
            }
         });
      }
   }

   private int getDefaultHeight() {
      boolean var3 = false;
      int var1 = 0;
      int var4 = this.mTabs.size();

      boolean var2;
      while(true) {
         var2 = var3;
         if (var1 >= var4) {
            break;
         }

         TabLayout.Tab var5 = (TabLayout.Tab)this.mTabs.get(var1);
         if (var5 != null && var5.getIcon() != null && !TextUtils.isEmpty(var5.getText())) {
            var2 = true;
            break;
         }

         ++var1;
      }

      return var2 ? 72 : 48;
   }

   private float getScrollPosition() {
      return this.mTabStrip.getIndicatorPosition();
   }

   private int getTabMinWidth() {
      int var1 = this.mRequestedTabMinWidth;
      if (var1 != -1) {
         return var1;
      } else {
         return this.mMode == 0 ? this.mScrollableTabMinWidth : 0;
      }
   }

   private int getTabScrollRange() {
      return Math.max(0, this.mTabStrip.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
   }

   private void removeTabViewAt(int var1) {
      TabLayout.TabView var2 = (TabLayout.TabView)this.mTabStrip.getChildAt(var1);
      this.mTabStrip.removeViewAt(var1);
      if (var2 != null) {
         var2.reset();
         this.mTabViewPool.release(var2);
      }

      this.requestLayout();
   }

   private void setSelectedTabView(int var1) {
      int var3 = this.mTabStrip.getChildCount();
      if (var1 < var3) {
         for(int var2 = 0; var2 < var3; ++var2) {
            View var5 = this.mTabStrip.getChildAt(var2);
            boolean var4;
            if (var2 == var1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var5.setSelected(var4);
         }

      }
   }

   private void setupWithViewPager(@Nullable ViewPager var1, boolean var2, boolean var3) {
      ViewPager var4 = this.mViewPager;
      if (var4 != null) {
         TabLayout.TabLayoutOnPageChangeListener var5 = this.mPageChangeListener;
         if (var5 != null) {
            var4.removeOnPageChangeListener(var5);
         }

         TabLayout.AdapterChangeListener var7 = this.mAdapterChangeListener;
         if (var7 != null) {
            this.mViewPager.removeOnAdapterChangeListener(var7);
         }
      }

      TabLayout.OnTabSelectedListener var8 = this.mCurrentVpSelectedListener;
      if (var8 != null) {
         this.removeOnTabSelectedListener(var8);
         this.mCurrentVpSelectedListener = null;
      }

      if (var1 != null) {
         this.mViewPager = var1;
         if (this.mPageChangeListener == null) {
            this.mPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(this);
         }

         this.mPageChangeListener.reset();
         var1.addOnPageChangeListener(this.mPageChangeListener);
         this.mCurrentVpSelectedListener = new TabLayout.ViewPagerOnTabSelectedListener(var1);
         this.addOnTabSelectedListener(this.mCurrentVpSelectedListener);
         PagerAdapter var6 = var1.getAdapter();
         if (var6 != null) {
            this.setPagerAdapter(var6, var2);
         }

         if (this.mAdapterChangeListener == null) {
            this.mAdapterChangeListener = new TabLayout.AdapterChangeListener();
         }

         this.mAdapterChangeListener.setAutoRefresh(var2);
         var1.addOnAdapterChangeListener(this.mAdapterChangeListener);
         this.setScrollPosition(var1.getCurrentItem(), 0.0F, true);
      } else {
         this.mViewPager = null;
         this.setPagerAdapter((PagerAdapter)null, false);
      }

      this.mSetupViewPagerImplicitly = var3;
   }

   private void updateAllTabs() {
      int var1 = 0;

      for(int var2 = this.mTabs.size(); var1 < var2; ++var1) {
         ((TabLayout.Tab)this.mTabs.get(var1)).updateView();
      }

   }

   private void updateTabViewLayoutParams(android.widget.LinearLayout.LayoutParams var1) {
      if (this.mMode == 1 && this.mTabGravity == 0) {
         var1.width = 0;
         var1.weight = 1.0F;
      } else {
         var1.width = -2;
         var1.weight = 0.0F;
      }
   }

   public void addOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener var1) {
      if (!this.mSelectedListeners.contains(var1)) {
         this.mSelectedListeners.add(var1);
      }
   }

   public void addTab(@NonNull TabLayout.Tab var1) {
      this.addTab(var1, this.mTabs.isEmpty());
   }

   public void addTab(@NonNull TabLayout.Tab var1, int var2) {
      this.addTab(var1, var2, this.mTabs.isEmpty());
   }

   public void addTab(@NonNull TabLayout.Tab var1, int var2, boolean var3) {
      if (var1.mParent == this) {
         this.configureTab(var1, var2);
         this.addTabView(var1);
         if (var3) {
            var1.select();
         }
      } else {
         throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
      }
   }

   public void addTab(@NonNull TabLayout.Tab var1, boolean var2) {
      this.addTab(var1, this.mTabs.size(), var2);
   }

   public void addView(View var1) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, int var2) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      this.addViewInternal(var1);
   }

   public void addView(View var1, android.view.ViewGroup.LayoutParams var2) {
      this.addViewInternal(var1);
   }

   public void clearOnTabSelectedListeners() {
      this.mSelectedListeners.clear();
   }

   int dpToPx(int var1) {
      return Math.round(this.getResources().getDisplayMetrics().density * (float)var1);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return this.generateDefaultLayoutParams();
   }

   public int getSelectedTabPosition() {
      TabLayout.Tab var1 = this.mSelectedTab;
      return var1 != null ? var1.getPosition() : -1;
   }

   @Nullable
   public TabLayout.Tab getTabAt(int var1) {
      return var1 >= 0 && var1 < this.getTabCount() ? (TabLayout.Tab)this.mTabs.get(var1) : null;
   }

   public int getTabCount() {
      return this.mTabs.size();
   }

   public int getTabGravity() {
      return this.mTabGravity;
   }

   int getTabMaxWidth() {
      return this.mTabMaxWidth;
   }

   public int getTabMode() {
      return this.mMode;
   }

   @Nullable
   public ColorStateList getTabTextColors() {
      return this.mTabTextColors;
   }

   @NonNull
   public TabLayout.Tab newTab() {
      TabLayout.Tab var1 = (TabLayout.Tab)sTabPool.acquire();
      if (var1 == null) {
         var1 = new TabLayout.Tab();
      }

      var1.mParent = this;
      var1.mView = this.createTabView(var1);
      return var1;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.mViewPager == null) {
         ViewParent var1 = this.getParent();
         if (var1 instanceof ViewPager) {
            this.setupWithViewPager((ViewPager)var1, true, true);
         }
      }
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.mSetupViewPagerImplicitly) {
         this.setupWithViewPager((ViewPager)null);
         this.mSetupViewPagerImplicitly = false;
      }
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
      int var4 = MeasureSpec.getMode(var2);
      if (var4 != Integer.MIN_VALUE) {
         if (var4 == 0) {
            var2 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
         }
      } else {
         var2 = MeasureSpec.makeMeasureSpec(Math.min(var3, MeasureSpec.getSize(var2)), 1073741824);
      }

      var4 = MeasureSpec.getSize(var1);
      if (MeasureSpec.getMode(var1) != 0) {
         var3 = this.mRequestedTabMaxWidth;
         if (var3 <= 0) {
            var3 = var4 - this.dpToPx(56);
         }

         this.mTabMaxWidth = var3;
      }

      super.onMeasure(var1, var2);
      if (this.getChildCount() == 1) {
         boolean var8 = false;
         boolean var6 = false;
         View var5 = this.getChildAt(0);
         boolean var7 = false;
         switch(this.mMode) {
         case 0:
            var6 = var8;
            if (var5.getMeasuredWidth() < this.getMeasuredWidth()) {
               var6 = true;
            }
            break;
         case 1:
            if (var5.getMeasuredWidth() != this.getMeasuredWidth()) {
               var6 = true;
            }
            break;
         default:
            var6 = var7;
         }

         if (var6) {
            var1 = getChildMeasureSpec(var2, this.getPaddingTop() + this.getPaddingBottom(), var5.getLayoutParams().height);
            var5.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), var1);
         }
      }
   }

   void populateFromPagerAdapter() {
      this.removeAllTabs();
      PagerAdapter var3 = this.mPagerAdapter;
      if (var3 != null) {
         int var2 = var3.getCount();

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            this.addTab(this.newTab().setText(this.mPagerAdapter.getPageTitle(var1)), false);
         }

         ViewPager var4 = this.mViewPager;
         if (var4 != null && var2 > 0) {
            var1 = var4.getCurrentItem();
            if (var1 != this.getSelectedTabPosition() && var1 < this.getTabCount()) {
               this.selectTab(this.getTabAt(var1));
            }
         }
      }
   }

   public void removeAllTabs() {
      for(int var1 = this.mTabStrip.getChildCount() - 1; var1 >= 0; --var1) {
         this.removeTabViewAt(var1);
      }

      Iterator var2 = this.mTabs.iterator();

      while(var2.hasNext()) {
         TabLayout.Tab var3 = (TabLayout.Tab)var2.next();
         var2.remove();
         var3.reset();
         sTabPool.release(var3);
      }

      this.mSelectedTab = null;
   }

   public void removeOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener var1) {
      this.mSelectedListeners.remove(var1);
   }

   public void removeTab(TabLayout.Tab var1) {
      if (var1.mParent == this) {
         this.removeTabAt(var1.getPosition());
      } else {
         throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
      }
   }

   public void removeTabAt(int var1) {
      TabLayout.Tab var5 = this.mSelectedTab;
      int var2;
      if (var5 != null) {
         var2 = var5.getPosition();
      } else {
         var2 = 0;
      }

      this.removeTabViewAt(var1);
      var5 = (TabLayout.Tab)this.mTabs.remove(var1);
      if (var5 != null) {
         var5.reset();
         sTabPool.release(var5);
      }

      int var4 = this.mTabs.size();

      for(int var3 = var1; var3 < var4; ++var3) {
         ((TabLayout.Tab)this.mTabs.get(var3)).setPosition(var3);
      }

      if (var2 == var1) {
         if (this.mTabs.isEmpty()) {
            var5 = null;
         } else {
            var5 = (TabLayout.Tab)this.mTabs.get(Math.max(0, var1 - 1));
         }

         this.selectTab(var5);
      }
   }

   void selectTab(TabLayout.Tab var1) {
      this.selectTab(var1, true);
   }

   void selectTab(TabLayout.Tab var1, boolean var2) {
      TabLayout.Tab var4 = this.mSelectedTab;
      if (var4 == var1) {
         if (var4 != null) {
            this.dispatchTabReselected(var1);
            this.animateToTab(var1.getPosition());
         }
      } else {
         int var3;
         if (var1 != null) {
            var3 = var1.getPosition();
         } else {
            var3 = -1;
         }

         if (var2) {
            if ((var4 == null || var4.getPosition() == -1) && var3 != -1) {
               this.setScrollPosition(var3, 0.0F, true);
            } else {
               this.animateToTab(var3);
            }

            if (var3 != -1) {
               this.setSelectedTabView(var3);
            }
         }

         if (var4 != null) {
            this.dispatchTabUnselected(var4);
         }

         this.mSelectedTab = var1;
         if (var1 != null) {
            this.dispatchTabSelected(var1);
         }
      }
   }

   @Deprecated
   public void setOnTabSelectedListener(@Nullable TabLayout.OnTabSelectedListener var1) {
      TabLayout.OnTabSelectedListener var2 = this.mSelectedListener;
      if (var2 != null) {
         this.removeOnTabSelectedListener(var2);
      }

      this.mSelectedListener = var1;
      if (var1 != null) {
         this.addOnTabSelectedListener(var1);
      }
   }

   void setPagerAdapter(@Nullable PagerAdapter var1, boolean var2) {
      PagerAdapter var3 = this.mPagerAdapter;
      if (var3 != null) {
         DataSetObserver var4 = this.mPagerAdapterObserver;
         if (var4 != null) {
            var3.unregisterDataSetObserver(var4);
         }
      }

      this.mPagerAdapter = var1;
      if (var2 && var1 != null) {
         if (this.mPagerAdapterObserver == null) {
            this.mPagerAdapterObserver = new TabLayout.PagerAdapterObserver();
         }

         var1.registerDataSetObserver(this.mPagerAdapterObserver);
      }

      this.populateFromPagerAdapter();
   }

   void setScrollAnimatorListener(AnimatorListener var1) {
      this.ensureScrollAnimator();
      this.mScrollAnimator.addListener(var1);
   }

   public void setScrollPosition(int var1, float var2, boolean var3) {
      this.setScrollPosition(var1, var2, var3, true);
   }

   void setScrollPosition(int var1, float var2, boolean var3, boolean var4) {
      int var5 = Math.round((float)var1 + var2);
      if (var5 >= 0) {
         if (var5 < this.mTabStrip.getChildCount()) {
            if (var4) {
               this.mTabStrip.setIndicatorPositionFromTabPosition(var1, var2);
            }

            ValueAnimator var6 = this.mScrollAnimator;
            if (var6 != null && var6.isRunning()) {
               this.mScrollAnimator.cancel();
            }

            this.scrollTo(this.calculateScrollXForTab(var1, var2), 0);
            if (var3) {
               this.setSelectedTabView(var5);
            }
         }
      }
   }

   public void setSelectedTabIndicatorColor(@ColorInt int var1) {
      this.mTabStrip.setSelectedIndicatorColor(var1);
   }

   public void setSelectedTabIndicatorHeight(int var1) {
      this.mTabStrip.setSelectedIndicatorHeight(var1);
   }

   public void setTabGravity(int var1) {
      if (this.mTabGravity != var1) {
         this.mTabGravity = var1;
         this.applyModeAndGravity();
      }
   }

   public void setTabMode(int var1) {
      if (var1 != this.mMode) {
         this.mMode = var1;
         this.applyModeAndGravity();
      }
   }

   public void setTabTextColors(int var1, int var2) {
      this.setTabTextColors(createColorStateList(var1, var2));
   }

   public void setTabTextColors(@Nullable ColorStateList var1) {
      if (this.mTabTextColors != var1) {
         this.mTabTextColors = var1;
         this.updateAllTabs();
      }
   }

   @Deprecated
   public void setTabsFromPagerAdapter(@Nullable PagerAdapter var1) {
      this.setPagerAdapter(var1, false);
   }

   public void setupWithViewPager(@Nullable ViewPager var1) {
      this.setupWithViewPager(var1, true);
   }

   public void setupWithViewPager(@Nullable ViewPager var1, boolean var2) {
      this.setupWithViewPager(var1, var2, false);
   }

   public boolean shouldDelayChildPressedState() {
      return this.getTabScrollRange() > 0;
   }

   void updateTabViews(boolean var1) {
      for(int var2 = 0; var2 < this.mTabStrip.getChildCount(); ++var2) {
         View var3 = this.mTabStrip.getChildAt(var2);
         var3.setMinimumWidth(this.getTabMinWidth());
         this.updateTabViewLayoutParams((android.widget.LinearLayout.LayoutParams)var3.getLayoutParams());
         if (var1) {
            var3.requestLayout();
         }
      }

   }

   private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
      private boolean mAutoRefresh;

      AdapterChangeListener() {
      }

      public void onAdapterChanged(@NonNull ViewPager var1, @Nullable PagerAdapter var2, @Nullable PagerAdapter var3) {
         if (TabLayout.this.mViewPager == var1) {
            TabLayout.this.setPagerAdapter(var3, this.mAutoRefresh);
         }
      }

      void setAutoRefresh(boolean var1) {
         this.mAutoRefresh = var1;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface Mode {
   }

   public interface OnTabSelectedListener {
      void onTabReselected(TabLayout.Tab var1);

      void onTabSelected(TabLayout.Tab var1);

      void onTabUnselected(TabLayout.Tab var1);
   }

   private class PagerAdapterObserver extends DataSetObserver {
      PagerAdapterObserver() {
      }

      public void onChanged() {
         TabLayout.this.populateFromPagerAdapter();
      }

      public void onInvalidated() {
         TabLayout.this.populateFromPagerAdapter();
      }
   }

   private class SlidingTabStrip extends LinearLayout {
      private ValueAnimator mIndicatorAnimator;
      private int mIndicatorLeft = -1;
      private int mIndicatorRight = -1;
      private int mLayoutDirection = -1;
      private int mSelectedIndicatorHeight;
      private final Paint mSelectedIndicatorPaint;
      int mSelectedPosition = -1;
      float mSelectionOffset;

      SlidingTabStrip(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.mSelectedIndicatorPaint = new Paint();
      }

      private void updateIndicatorPosition() {
         View var6 = this.getChildAt(this.mSelectedPosition);
         int var4;
         int var5;
         if (var6 != null && var6.getWidth() > 0) {
            var4 = var6.getLeft();
            var5 = var6.getRight();
            if (this.mSelectionOffset > 0.0F && this.mSelectedPosition < this.getChildCount() - 1) {
               var6 = this.getChildAt(this.mSelectedPosition + 1);
               float var1 = this.mSelectionOffset;
               float var2 = (float)var6.getLeft();
               float var3 = this.mSelectionOffset;
               var4 = (int)(var1 * var2 + (1.0F - var3) * (float)var4);
               var5 = (int)(var3 * (float)var6.getRight() + (1.0F - this.mSelectionOffset) * (float)var5);
            }
         } else {
            var4 = -1;
            var5 = -1;
         }

         this.setIndicatorPosition(var4, var5);
      }

      void animateIndicatorToPosition(final int var1, int var2) {
         ValueAnimator var7 = this.mIndicatorAnimator;
         if (var7 != null && var7.isRunning()) {
            this.mIndicatorAnimator.cancel();
         }

         boolean var3;
         if (ViewCompat.getLayoutDirection(this) == 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         View var9 = this.getChildAt(var1);
         if (var9 == null) {
            this.updateIndicatorPosition();
         } else {
            final int var5 = var9.getLeft();
            final int var6 = var9.getRight();
            final int var4;
            final int var8;
            if (Math.abs(var1 - this.mSelectedPosition) <= 1) {
               var8 = this.mIndicatorLeft;
               var4 = this.mIndicatorRight;
            } else {
               var4 = TabLayout.this.dpToPx(24);
               if (var1 < this.mSelectedPosition) {
                  if (var3) {
                     var4 = var5 - var4;
                     var8 = var4;
                  } else {
                     var4 += var6;
                     var8 = var4;
                  }
               } else if (var3) {
                  var4 += var6;
                  var8 = var4;
               } else {
                  var4 = var5 - var4;
                  var8 = var4;
               }
            }

            if (var8 != var5 || var4 != var6) {
               var7 = new ValueAnimator();
               this.mIndicatorAnimator = var7;
               var7.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
               var7.setDuration((long)var2);
               var7.setFloatValues(new float[]{0.0F, 1.0F});
               var7.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1) {
                     float var2 = var1.getAnimatedFraction();
                     SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(var8, var5, var2), AnimationUtils.lerp(var4, var6, var2));
                  }
               });
               var7.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     TabLayout.SlidingTabStrip var2 = SlidingTabStrip.this;
                     var2.mSelectedPosition = var1;
                     var2.mSelectionOffset = 0.0F;
                  }
               });
               var7.start();
            }
         }
      }

      boolean childrenNeedLayout() {
         int var1 = 0;

         for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
            if (this.getChildAt(var1).getWidth() <= 0) {
               return true;
            }
         }

         return false;
      }

      public void draw(Canvas var1) {
         super.draw(var1);
         int var2 = this.mIndicatorLeft;
         if (var2 >= 0 && this.mIndicatorRight > var2) {
            var1.drawRect((float)var2, (float)(this.getHeight() - this.mSelectedIndicatorHeight), (float)this.mIndicatorRight, (float)this.getHeight(), this.mSelectedIndicatorPaint);
         }
      }

      float getIndicatorPosition() {
         return (float)this.mSelectedPosition + this.mSelectionOffset;
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         ValueAnimator var8 = this.mIndicatorAnimator;
         if (var8 != null && var8.isRunning()) {
            this.mIndicatorAnimator.cancel();
            long var6 = this.mIndicatorAnimator.getDuration();
            this.animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0F - this.mIndicatorAnimator.getAnimatedFraction()) * (float)var6));
         } else {
            this.updateIndicatorPosition();
         }
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (MeasureSpec.getMode(var1) == 1073741824) {
            if (TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
               int var6 = this.getChildCount();
               int var4 = 0;

               for(int var3 = 0; var3 < var6; ++var3) {
                  View var7 = this.getChildAt(var3);
                  if (var7.getVisibility() == 0) {
                     var4 = Math.max(var4, var7.getMeasuredWidth());
                  }
               }

               if (var4 > 0) {
                  int var5 = TabLayout.this.dpToPx(16);
                  boolean var8 = false;
                  if (var4 * var6 <= this.getMeasuredWidth() - var5 * 2) {
                     for(var5 = 0; var5 < var6; ++var5) {
                        android.widget.LinearLayout.LayoutParams var9 = (android.widget.LinearLayout.LayoutParams)this.getChildAt(var5).getLayoutParams();
                        if (var9.width != var4 || var9.weight != 0.0F) {
                           var9.width = var4;
                           var9.weight = 0.0F;
                           var8 = true;
                        }
                     }
                  } else {
                     TabLayout var10 = TabLayout.this;
                     var10.mTabGravity = 0;
                     var10.updateTabViews(false);
                     var8 = true;
                  }

                  if (var8) {
                     super.onMeasure(var1, var2);
                  }
               }
            }
         }
      }

      public void onRtlPropertiesChanged(int var1) {
         super.onRtlPropertiesChanged(var1);
         if (VERSION.SDK_INT < 23) {
            if (this.mLayoutDirection != var1) {
               this.requestLayout();
               this.mLayoutDirection = var1;
            }
         }
      }

      void setIndicatorPosition(int var1, int var2) {
         if (var1 != this.mIndicatorLeft || var2 != this.mIndicatorRight) {
            this.mIndicatorLeft = var1;
            this.mIndicatorRight = var2;
            ViewCompat.postInvalidateOnAnimation(this);
         }
      }

      void setIndicatorPositionFromTabPosition(int var1, float var2) {
         ValueAnimator var3 = this.mIndicatorAnimator;
         if (var3 != null && var3.isRunning()) {
            this.mIndicatorAnimator.cancel();
         }

         this.mSelectedPosition = var1;
         this.mSelectionOffset = var2;
         this.updateIndicatorPosition();
      }

      void setSelectedIndicatorColor(int var1) {
         if (this.mSelectedIndicatorPaint.getColor() != var1) {
            this.mSelectedIndicatorPaint.setColor(var1);
            ViewCompat.postInvalidateOnAnimation(this);
         }
      }

      void setSelectedIndicatorHeight(int var1) {
         if (this.mSelectedIndicatorHeight != var1) {
            this.mSelectedIndicatorHeight = var1;
            ViewCompat.postInvalidateOnAnimation(this);
         }
      }
   }

   public static final class Tab {
      public static final int INVALID_POSITION = -1;
      private CharSequence mContentDesc;
      private View mCustomView;
      private Drawable mIcon;
      TabLayout mParent;
      private int mPosition = -1;
      private Object mTag;
      private CharSequence mText;
      TabLayout.TabView mView;

      Tab() {
      }

      @Nullable
      public CharSequence getContentDescription() {
         return this.mContentDesc;
      }

      @Nullable
      public View getCustomView() {
         return this.mCustomView;
      }

      @Nullable
      public Drawable getIcon() {
         return this.mIcon;
      }

      public int getPosition() {
         return this.mPosition;
      }

      @Nullable
      public Object getTag() {
         return this.mTag;
      }

      @Nullable
      public CharSequence getText() {
         return this.mText;
      }

      public boolean isSelected() {
         TabLayout var1 = this.mParent;
         if (var1 != null) {
            return var1.getSelectedTabPosition() == this.mPosition;
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      void reset() {
         this.mParent = null;
         this.mView = null;
         this.mTag = null;
         this.mIcon = null;
         this.mText = null;
         this.mContentDesc = null;
         this.mPosition = -1;
         this.mCustomView = null;
      }

      public void select() {
         TabLayout var1 = this.mParent;
         if (var1 != null) {
            var1.selectTab(this);
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      @NonNull
      public TabLayout.Tab setContentDescription(@StringRes int var1) {
         TabLayout var2 = this.mParent;
         if (var2 != null) {
            return this.setContentDescription(var2.getResources().getText(var1));
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      @NonNull
      public TabLayout.Tab setContentDescription(@Nullable CharSequence var1) {
         this.mContentDesc = var1;
         this.updateView();
         return this;
      }

      @NonNull
      public TabLayout.Tab setCustomView(@LayoutRes int var1) {
         return this.setCustomView(LayoutInflater.from(this.mView.getContext()).inflate(var1, this.mView, false));
      }

      @NonNull
      public TabLayout.Tab setCustomView(@Nullable View var1) {
         this.mCustomView = var1;
         this.updateView();
         return this;
      }

      @NonNull
      public TabLayout.Tab setIcon(@DrawableRes int var1) {
         TabLayout var2 = this.mParent;
         if (var2 != null) {
            return this.setIcon(AppCompatResources.getDrawable(var2.getContext(), var1));
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      @NonNull
      public TabLayout.Tab setIcon(@Nullable Drawable var1) {
         this.mIcon = var1;
         this.updateView();
         return this;
      }

      void setPosition(int var1) {
         this.mPosition = var1;
      }

      @NonNull
      public TabLayout.Tab setTag(@Nullable Object var1) {
         this.mTag = var1;
         return this;
      }

      @NonNull
      public TabLayout.Tab setText(@StringRes int var1) {
         TabLayout var2 = this.mParent;
         if (var2 != null) {
            return this.setText(var2.getResources().getText(var1));
         } else {
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
         }
      }

      @NonNull
      public TabLayout.Tab setText(@Nullable CharSequence var1) {
         this.mText = var1;
         this.updateView();
         return this;
      }

      void updateView() {
         TabLayout.TabView var1 = this.mView;
         if (var1 != null) {
            var1.update();
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface TabGravity {
   }

   public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
      private int mPreviousScrollState;
      private int mScrollState;
      private final WeakReference mTabLayoutRef;

      public TabLayoutOnPageChangeListener(TabLayout var1) {
         this.mTabLayoutRef = new WeakReference(var1);
      }

      public void onPageScrollStateChanged(int var1) {
         this.mPreviousScrollState = this.mScrollState;
         this.mScrollState = var1;
      }

      public void onPageScrolled(int var1, float var2, int var3) {
         TabLayout var6 = (TabLayout)this.mTabLayoutRef.get();
         if (var6 != null) {
            var3 = this.mScrollState;
            boolean var5 = false;
            boolean var4;
            if (var3 == 2 && this.mPreviousScrollState != 1) {
               var4 = false;
            } else {
               var4 = true;
            }

            if (this.mScrollState != 2 || this.mPreviousScrollState != 0) {
               var5 = true;
            }

            var6.setScrollPosition(var1, var2, var4, var5);
         }
      }

      public void onPageSelected(int var1) {
         TabLayout var4 = (TabLayout)this.mTabLayoutRef.get();
         if (var4 != null && var4.getSelectedTabPosition() != var1) {
            if (var1 < var4.getTabCount()) {
               int var2 = this.mScrollState;
               boolean var3;
               if (var2 == 0 || var2 == 2 && this.mPreviousScrollState == 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var4.selectTab(var4.getTabAt(var1), var3);
            }
         }
      }

      void reset() {
         this.mScrollState = 0;
         this.mPreviousScrollState = 0;
      }
   }

   class TabView extends LinearLayout {
      private ImageView mCustomIconView;
      private TextView mCustomTextView;
      private View mCustomView;
      private int mDefaultMaxLines = 2;
      private ImageView mIconView;
      private TabLayout.Tab mTab;
      private TextView mTextView;

      public TabView(Context var2) {
         super(var2);
         if (TabLayout.this.mTabBackgroundResId != 0) {
            ViewCompat.setBackground(this, AppCompatResources.getDrawable(var2, TabLayout.this.mTabBackgroundResId));
         }

         ViewCompat.setPaddingRelative(this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
         this.setGravity(17);
         this.setOrientation(1);
         this.setClickable(true);
         ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
      }

      private float approximateLineWidth(Layout var1, int var2, float var3) {
         return var1.getLineWidth(var2) * (var3 / var1.getPaint().getTextSize());
      }

      private void updateTextAndIcon(@Nullable TextView var1, @Nullable ImageView var2) {
         TabLayout.Tab var5 = this.mTab;
         Object var8 = null;
         Drawable var6;
         if (var5 != null) {
            var6 = var5.getIcon();
         } else {
            var6 = null;
         }

         var5 = this.mTab;
         CharSequence var7;
         if (var5 != null) {
            var7 = var5.getText();
         } else {
            var7 = null;
         }

         var5 = this.mTab;
         CharSequence var10;
         if (var5 != null) {
            var10 = var5.getContentDescription();
         } else {
            var10 = null;
         }

         if (var2 != null) {
            if (var6 != null) {
               var2.setImageDrawable(var6);
               var2.setVisibility(0);
               this.setVisibility(0);
            } else {
               var2.setVisibility(8);
               var2.setImageDrawable((Drawable)null);
            }

            var2.setContentDescription(var10);
         }

         boolean var4 = TextUtils.isEmpty(var7) ^ true;
         if (var1 != null) {
            if (var4) {
               var1.setText(var7);
               var1.setVisibility(0);
               this.setVisibility(0);
            } else {
               var1.setVisibility(8);
               var1.setText((CharSequence)null);
            }

            var1.setContentDescription(var10);
         }

         if (var2 != null) {
            MarginLayoutParams var9 = (MarginLayoutParams)var2.getLayoutParams();
            int var3 = 0;
            if (var4 && var2.getVisibility() == 0) {
               var3 = TabLayout.this.dpToPx(8);
            }

            if (var3 != var9.bottomMargin) {
               var9.bottomMargin = var3;
               var2.requestLayout();
            }
         }

         if (var4) {
            var10 = (CharSequence)var8;
         }

         TooltipCompat.setTooltipText(this, var10);
      }

      public TabLayout.Tab getTab() {
         return this.mTab;
      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      public void onMeasure(int var1, int var2) {
         int var5 = MeasureSpec.getSize(var1);
         int var6 = MeasureSpec.getMode(var1);
         int var7 = TabLayout.this.getTabMaxWidth();
         if (var7 > 0 && (var6 == 0 || var5 > var7)) {
            var1 = MeasureSpec.makeMeasureSpec(TabLayout.this.mTabMaxWidth, Integer.MIN_VALUE);
         }

         super.onMeasure(var1, var2);
         if (this.mTextView != null) {
            this.getResources();
            float var3 = TabLayout.this.mTabTextSize;
            var5 = this.mDefaultMaxLines;
            ImageView var8 = this.mIconView;
            if (var8 != null && var8.getVisibility() == 0) {
               var5 = 1;
            } else {
               TextView var10 = this.mTextView;
               if (var10 != null && var10.getLineCount() > 1) {
                  var3 = TabLayout.this.mTabTextMultiLineSize;
               }
            }

            float var4 = this.mTextView.getTextSize();
            var7 = this.mTextView.getLineCount();
            var6 = TextViewCompat.getMaxLines(this.mTextView);
            if (var3 != var4 || var6 >= 0 && var5 != var6) {
               boolean var9 = true;
               if (TabLayout.this.mMode == 1 && var3 > var4 && var7 == 1) {
                  Layout var11 = this.mTextView.getLayout();
                  if (var11 == null || this.approximateLineWidth(var11, 0, var3) > (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight())) {
                     var9 = false;
                  }
               }

               if (var9) {
                  this.mTextView.setTextSize(0, var3);
                  this.mTextView.setMaxLines(var5);
                  super.onMeasure(var1, var2);
               }
            }
         }
      }

      public boolean performClick() {
         boolean var1 = super.performClick();
         if (this.mTab != null) {
            if (!var1) {
               this.playSoundEffect(0);
            }

            this.mTab.select();
            return true;
         } else {
            return var1;
         }
      }

      void reset() {
         this.setTab((TabLayout.Tab)null);
         this.setSelected(false);
      }

      public void setSelected(boolean var1) {
         boolean var2;
         if (this.isSelected() != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         super.setSelected(var1);
         if (var2 && var1 && VERSION.SDK_INT < 16) {
            this.sendAccessibilityEvent(4);
         }

         TextView var3 = this.mTextView;
         if (var3 != null) {
            var3.setSelected(var1);
         }

         ImageView var4 = this.mIconView;
         if (var4 != null) {
            var4.setSelected(var1);
         }

         View var5 = this.mCustomView;
         if (var5 != null) {
            var5.setSelected(var1);
         }
      }

      void setTab(@Nullable TabLayout.Tab var1) {
         if (var1 != this.mTab) {
            this.mTab = var1;
            this.update();
         }
      }

      final void update() {
         TabLayout.Tab var4 = this.mTab;
         View var3;
         if (var4 != null) {
            var3 = var4.getCustomView();
         } else {
            var3 = null;
         }

         if (var3 != null) {
            ViewParent var5 = var3.getParent();
            if (var5 != this) {
               if (var5 != null) {
                  ((ViewGroup)var5).removeView(var3);
               }

               this.addView(var3);
            }

            this.mCustomView = var3;
            TextView var8 = this.mTextView;
            if (var8 != null) {
               var8.setVisibility(8);
            }

            ImageView var9 = this.mIconView;
            if (var9 != null) {
               var9.setVisibility(8);
               this.mIconView.setImageDrawable((Drawable)null);
            }

            this.mCustomTextView = (TextView)var3.findViewById(16908308);
            var8 = this.mCustomTextView;
            if (var8 != null) {
               this.mDefaultMaxLines = TextViewCompat.getMaxLines(var8);
            }

            this.mCustomIconView = (ImageView)var3.findViewById(16908294);
         } else {
            var3 = this.mCustomView;
            if (var3 != null) {
               this.removeView(var3);
               this.mCustomView = null;
            }

            this.mCustomTextView = null;
            this.mCustomIconView = null;
         }

         var3 = this.mCustomView;
         boolean var2 = false;
         if (var3 == null) {
            if (this.mIconView == null) {
               ImageView var6 = (ImageView)LayoutInflater.from(this.getContext()).inflate(R$layout.design_layout_tab_icon, this, false);
               this.addView(var6, 0);
               this.mIconView = var6;
            }

            if (this.mTextView == null) {
               TextView var7 = (TextView)LayoutInflater.from(this.getContext()).inflate(R$layout.design_layout_tab_text, this, false);
               this.addView(var7);
               this.mTextView = var7;
               this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mTextView);
            }

            TextViewCompat.setTextAppearance(this.mTextView, TabLayout.this.mTabTextAppearance);
            if (TabLayout.this.mTabTextColors != null) {
               this.mTextView.setTextColor(TabLayout.this.mTabTextColors);
            }

            this.updateTextAndIcon(this.mTextView, this.mIconView);
         } else if (this.mCustomTextView != null || this.mCustomIconView != null) {
            this.updateTextAndIcon(this.mCustomTextView, this.mCustomIconView);
         }

         boolean var1 = var2;
         if (var4 != null) {
            var1 = var2;
            if (var4.isSelected()) {
               var1 = true;
            }
         }

         this.setSelected(var1);
      }
   }

   public static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
      private final ViewPager mViewPager;

      public ViewPagerOnTabSelectedListener(ViewPager var1) {
         this.mViewPager = var1;
      }

      public void onTabReselected(TabLayout.Tab var1) {
      }

      public void onTabSelected(TabLayout.Tab var1) {
         this.mViewPager.setCurrentItem(var1.getPosition());
      }

      public void onTabUnselected(TabLayout.Tab var1) {
      }
   }
}
