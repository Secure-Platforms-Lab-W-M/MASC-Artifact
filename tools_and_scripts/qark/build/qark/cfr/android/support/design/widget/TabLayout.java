/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.text.Layout
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.animation.Interpolator
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.TabItem;
import android.support.design.widget.ThemeUtils;
import android.support.v4.util.Pools;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout
extends HorizontalScrollView {
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
    private static final Pools.Pool<Tab> sTabPool = new Pools.SynchronizedPool<Tab>(16);
    private AdapterChangeListener mAdapterChangeListener;
    private int mContentInsetStart;
    private OnTabSelectedListener mCurrentVpSelectedListener;
    int mMode;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private final int mRequestedTabMaxWidth;
    private final int mRequestedTabMinWidth;
    private ValueAnimator mScrollAnimator;
    private final int mScrollableTabMinWidth;
    private OnTabSelectedListener mSelectedListener;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners;
    private Tab mSelectedTab;
    private boolean mSetupViewPagerImplicitly;
    final int mTabBackgroundResId;
    int mTabGravity;
    int mTabMaxWidth;
    int mTabPaddingBottom;
    int mTabPaddingEnd;
    int mTabPaddingStart;
    int mTabPaddingTop;
    private final SlidingTabStrip mTabStrip;
    int mTabTextAppearance;
    ColorStateList mTabTextColors;
    float mTabTextMultiLineSize;
    float mTabTextSize;
    private final Pools.Pool<TabView> mTabViewPool;
    private final ArrayList<Tab> mTabs;
    ViewPager mViewPager;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int n) {
        block4 : {
            super(context, attributeSet, n);
            this.mTabs = new ArrayList();
            this.mTabMaxWidth = Integer.MAX_VALUE;
            this.mSelectedListeners = new ArrayList();
            this.mTabViewPool = new Pools.SimplePool<TabView>(12);
            ThemeUtils.checkAppCompatTheme(context);
            this.setHorizontalScrollBarEnabled(false);
            this.mTabStrip = new SlidingTabStrip(context);
            super.addView((View)this.mTabStrip, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout);
            this.mTabStrip.setSelectedIndicatorHeight(attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0));
            this.mTabStrip.setSelectedIndicatorColor(attributeSet.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
            this.mTabPaddingBottom = n = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
            this.mTabPaddingEnd = n;
            this.mTabPaddingTop = n;
            this.mTabPaddingStart = n;
            this.mTabPaddingStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
            this.mTabPaddingTop = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
            this.mTabPaddingEnd = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
            this.mTabPaddingBottom = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
            this.mTabTextAppearance = attributeSet.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
            context = context.obtainStyledAttributes(this.mTabTextAppearance, R.styleable.TextAppearance);
            this.mTabTextSize = context.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
            this.mTabTextColors = context.getColorStateList(R.styleable.TextAppearance_android_textColor);
            if (!attributeSet.hasValue(R.styleable.TabLayout_tabTextColor)) break block4;
            this.mTabTextColors = attributeSet.getColorStateList(R.styleable.TabLayout_tabTextColor);
        }
        if (attributeSet.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            n = attributeSet.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.mTabTextColors = TabLayout.createColorStateList(this.mTabTextColors.getDefaultColor(), n);
        }
        this.mRequestedTabMinWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
        this.mRequestedTabMaxWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
        this.mTabBackgroundResId = attributeSet.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.mContentInsetStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mMode = attributeSet.getInt(R.styleable.TabLayout_tabMode, 1);
        this.mTabGravity = attributeSet.getInt(R.styleable.TabLayout_tabGravity, 0);
        attributeSet.recycle();
        context = this.getResources();
        this.mTabTextMultiLineSize = context.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
        this.mScrollableTabMinWidth = context.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
        this.applyModeAndGravity();
        return;
        finally {
            context.recycle();
        }
    }

    private void addTabFromItemView(@NonNull TabItem tabItem) {
        Tab tab = this.newTab();
        if (tabItem.mText != null) {
            tab.setText(tabItem.mText);
        }
        if (tabItem.mIcon != null) {
            tab.setIcon(tabItem.mIcon);
        }
        if (tabItem.mCustomLayout != 0) {
            tab.setCustomView(tabItem.mCustomLayout);
        }
        if (!TextUtils.isEmpty((CharSequence)tabItem.getContentDescription())) {
            tab.setContentDescription(tabItem.getContentDescription());
        }
        this.addTab(tab);
    }

    private void addTabView(Tab tab) {
        TabView tabView = tab.mView;
        this.mTabStrip.addView((View)tabView, tab.getPosition(), (ViewGroup.LayoutParams)this.createLayoutParamsForTabs());
    }

    private void addViewInternal(View view) {
        if (view instanceof TabItem) {
            this.addTabFromItemView((TabItem)view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private void animateToTab(int n) {
        if (n == -1) {
            return;
        }
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this) && !this.mTabStrip.childrenNeedLayout()) {
            int n2;
            int n3 = this.getScrollX();
            if (n3 != (n2 = this.calculateScrollXForTab(n, 0.0f))) {
                this.ensureScrollAnimator();
                this.mScrollAnimator.setIntValues(new int[]{n3, n2});
                this.mScrollAnimator.start();
            }
            this.mTabStrip.animateIndicatorToPosition(n, 300);
            return;
        }
        this.setScrollPosition(n, 0.0f, true);
    }

    private void applyModeAndGravity() {
        int n = 0;
        if (this.mMode == 0) {
            n = Math.max(0, this.mContentInsetStart - this.mTabPaddingStart);
        }
        ViewCompat.setPaddingRelative((View)this.mTabStrip, n, 0, 0, 0);
        switch (this.mMode) {
            default: {
                break;
            }
            case 1: {
                this.mTabStrip.setGravity(1);
                break;
            }
            case 0: {
                this.mTabStrip.setGravity(8388611);
            }
        }
        this.updateTabViews(true);
    }

    private int calculateScrollXForTab(int n, float f) {
        int n2 = this.mMode;
        int n3 = 0;
        if (n2 == 0) {
            View view = this.mTabStrip.getChildAt(n);
            View view2 = n + 1 < this.mTabStrip.getChildCount() ? this.mTabStrip.getChildAt(n + 1) : null;
            n = view != null ? view.getWidth() : 0;
            if (view2 != null) {
                n3 = view2.getWidth();
            }
            n2 = view.getLeft() + n / 2 - this.getWidth() / 2;
            n = (int)((float)(n + n3) * 0.5f * f);
            if (ViewCompat.getLayoutDirection((View)this) == 0) {
                return n2 + n;
            }
            return n2 - n;
        }
        return 0;
    }

    private void configureTab(Tab tab, int n) {
        tab.setPosition(n);
        this.mTabs.add(n, tab);
        int n2 = this.mTabs.size();
        ++n;
        while (n < n2) {
            this.mTabs.get(n).setPosition(n);
            ++n;
        }
    }

    private static ColorStateList createColorStateList(int n, int n2) {
        int[][] arrarrn = new int[2][];
        int[] arrn = new int[2];
        arrarrn[0] = SELECTED_STATE_SET;
        arrn[0] = n2;
        n2 = 0 + 1;
        arrarrn[n2] = EMPTY_STATE_SET;
        arrn[n2] = n;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        this.updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private TabView createTabView(@NonNull Tab tab) {
        Pools.Pool<TabView> pool = this.mTabViewPool;
        pool = pool != null ? pool.acquire() : null;
        if (pool == null) {
            pool = new TabView(this.getContext());
        }
        pool.setTab(tab);
        pool.setFocusable(true);
        pool.setMinimumWidth(this.getTabMinWidth());
        return pool;
    }

    private void dispatchTabReselected(@NonNull Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; --i) {
            this.mSelectedListeners.get(i).onTabReselected(tab);
        }
    }

    private void dispatchTabSelected(@NonNull Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; --i) {
            this.mSelectedListeners.get(i).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(@NonNull Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; --i) {
            this.mSelectedListeners.get(i).onTabUnselected(tab);
        }
    }

    private void ensureScrollAnimator() {
        if (this.mScrollAnimator == null) {
            this.mScrollAnimator = new ValueAnimator();
            this.mScrollAnimator.setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrollAnimator.setDuration(300L);
            this.mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TabLayout.this.scrollTo(((Integer)valueAnimator.getAnimatedValue()).intValue(), 0);
                }
            });
            return;
        }
    }

    private int getDefaultHeight() {
        boolean bl;
        boolean bl2 = false;
        int n = 0;
        int n2 = this.mTabs.size();
        do {
            bl = bl2;
            if (n >= n2) break;
            Tab tab = this.mTabs.get(n);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty((CharSequence)tab.getText())) {
                bl = true;
                break;
            }
            ++n;
        } while (true);
        if (bl) {
            return 72;
        }
        return 48;
    }

    private float getScrollPosition() {
        return this.mTabStrip.getIndicatorPosition();
    }

    private int getTabMinWidth() {
        int n = this.mRequestedTabMinWidth;
        if (n != -1) {
            return n;
        }
        if (this.mMode == 0) {
            return this.mScrollableTabMinWidth;
        }
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, this.mTabStrip.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
    }

    private void removeTabViewAt(int n) {
        TabView tabView = (TabView)this.mTabStrip.getChildAt(n);
        this.mTabStrip.removeViewAt(n);
        if (tabView != null) {
            tabView.reset();
            this.mTabViewPool.release(tabView);
        }
        this.requestLayout();
    }

    private void setSelectedTabView(int n) {
        int n2 = this.mTabStrip.getChildCount();
        if (n < n2) {
            for (int i = 0; i < n2; ++i) {
                View view = this.mTabStrip.getChildAt(i);
                boolean bl = i == n;
                view.setSelected(bl);
            }
            return;
        }
    }

    private void setupWithViewPager(@Nullable ViewPager viewPager, boolean bl, boolean bl2) {
        Object object = this.mViewPager;
        if (object != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.mPageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                object.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            if ((object = this.mAdapterChangeListener) != null) {
                this.mViewPager.removeOnAdapterChangeListener((ViewPager.OnAdapterChangeListener)object);
            }
        }
        if ((object = this.mCurrentVpSelectedListener) != null) {
            this.removeOnTabSelectedListener((OnTabSelectedListener)object);
            this.mCurrentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.mViewPager = viewPager;
            if (this.mPageChangeListener == null) {
                this.mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.mPageChangeListener);
            this.mCurrentVpSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            this.addOnTabSelectedListener(this.mCurrentVpSelectedListener);
            object = viewPager.getAdapter();
            if (object != null) {
                this.setPagerAdapter((PagerAdapter)object, bl);
            }
            if (this.mAdapterChangeListener == null) {
                this.mAdapterChangeListener = new AdapterChangeListener();
            }
            this.mAdapterChangeListener.setAutoRefresh(bl);
            viewPager.addOnAdapterChangeListener(this.mAdapterChangeListener);
            this.setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.mViewPager = null;
            this.setPagerAdapter(null, false);
        }
        this.mSetupViewPagerImplicitly = bl2;
    }

    private void updateAllTabs() {
        int n = this.mTabs.size();
        for (int i = 0; i < n; ++i) {
            this.mTabs.get(i).updateView();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mMode == 1 && this.mTabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
            return;
        }
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
    }

    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener onTabSelectedListener) {
        if (!this.mSelectedListeners.contains(onTabSelectedListener)) {
            this.mSelectedListeners.add(onTabSelectedListener);
            return;
        }
    }

    public void addTab(@NonNull Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, int n, boolean bl) {
        if (tab.mParent == this) {
            this.configureTab(tab, n);
            this.addTabView(tab);
            if (bl) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void addTab(@NonNull Tab tab, boolean bl) {
        this.addTab(tab, this.mTabs.size(), bl);
    }

    public void addView(View view) {
        this.addViewInternal(view);
    }

    public void addView(View view, int n) {
        this.addViewInternal(view);
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        this.addViewInternal(view);
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        this.addViewInternal(view);
    }

    public void clearOnTabSelectedListeners() {
        this.mSelectedListeners.clear();
    }

    int dpToPx(int n) {
        return Math.round(this.getResources().getDisplayMetrics().density * (float)n);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return this.generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        Tab tab = this.mSelectedTab;
        if (tab != null) {
            return tab.getPosition();
        }
        return -1;
    }

    @Nullable
    public Tab getTabAt(int n) {
        if (n >= 0 && n < this.getTabCount()) {
            return this.mTabs.get(n);
        }
        return null;
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
    public Tab newTab() {
        Tab tab = sTabPool.acquire();
        if (tab == null) {
            tab = new Tab();
        }
        tab.mParent = this;
        tab.mView = this.createTabView(tab);
        return tab;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mViewPager == null) {
            ViewParent viewParent = this.getParent();
            if (viewParent instanceof ViewPager) {
                this.setupWithViewPager((ViewPager)viewParent, true, true);
                return;
            }
            return;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mSetupViewPagerImplicitly) {
            this.setupWithViewPager(null);
            this.mSetupViewPagerImplicitly = false;
            return;
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3 = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
        int n4 = View.MeasureSpec.getMode((int)n2);
        if (n4 != Integer.MIN_VALUE) {
            if (n4 == 0) {
                n2 = View.MeasureSpec.makeMeasureSpec((int)n3, (int)1073741824);
            }
        } else {
            n2 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n3, View.MeasureSpec.getSize((int)n2)), (int)1073741824);
        }
        n4 = View.MeasureSpec.getSize((int)n);
        if (View.MeasureSpec.getMode((int)n) != 0) {
            n3 = this.mRequestedTabMaxWidth;
            if (n3 <= 0) {
                n3 = n4 - this.dpToPx(56);
            }
            this.mTabMaxWidth = n3;
        }
        super.onMeasure(n, n2);
        if (this.getChildCount() == 1) {
            n4 = 0;
            n = 0;
            View view = this.getChildAt(0);
            n3 = 0;
            switch (this.mMode) {
                default: {
                    n = n3;
                    break;
                }
                case 1: {
                    if (view.getMeasuredWidth() == this.getMeasuredWidth()) break;
                    n = 1;
                    break;
                }
                case 0: {
                    n = n4;
                    if (view.getMeasuredWidth() >= this.getMeasuredWidth()) break;
                    n = 1;
                }
            }
            if (n != 0) {
                n = TabLayout.getChildMeasureSpec((int)n2, (int)(this.getPaddingTop() + this.getPaddingBottom()), (int)view.getLayoutParams().height);
                view.measure(View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824), n);
                return;
            }
            return;
        }
    }

    void populateFromPagerAdapter() {
        this.removeAllTabs();
        Object object = this.mPagerAdapter;
        if (object != null) {
            int n;
            int n2 = object.getCount();
            for (n = 0; n < n2; ++n) {
                this.addTab(this.newTab().setText(this.mPagerAdapter.getPageTitle(n)), false);
            }
            object = this.mViewPager;
            if (object != null && n2 > 0) {
                n = object.getCurrentItem();
                if (n != this.getSelectedTabPosition() && n < this.getTabCount()) {
                    this.selectTab(this.getTabAt(n));
                    return;
                }
                return;
            }
            return;
        }
    }

    public void removeAllTabs() {
        for (int i = this.mTabStrip.getChildCount() - 1; i >= 0; --i) {
            this.removeTabViewAt(i);
        }
        Iterator<Tab> iterator = this.mTabs.iterator();
        while (iterator.hasNext()) {
            Tab tab = iterator.next();
            iterator.remove();
            tab.reset();
            sTabPool.release(tab);
        }
        this.mSelectedTab = null;
    }

    public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener onTabSelectedListener) {
        this.mSelectedListeners.remove(onTabSelectedListener);
    }

    public void removeTab(Tab tab) {
        if (tab.mParent == this) {
            this.removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int n) {
        Tab tab = this.mSelectedTab;
        int n2 = tab != null ? tab.getPosition() : 0;
        this.removeTabViewAt(n);
        tab = this.mTabs.remove(n);
        if (tab != null) {
            tab.reset();
            sTabPool.release(tab);
        }
        int n3 = this.mTabs.size();
        for (int i = n; i < n3; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
        if (n2 == n) {
            tab = this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, n - 1));
            this.selectTab(tab);
            return;
        }
    }

    void selectTab(Tab tab) {
        this.selectTab(tab, true);
    }

    void selectTab(Tab tab, boolean bl) {
        Tab tab2 = this.mSelectedTab;
        if (tab2 == tab) {
            if (tab2 != null) {
                this.dispatchTabReselected(tab);
                this.animateToTab(tab.getPosition());
                return;
            }
            return;
        }
        int n = tab != null ? tab.getPosition() : -1;
        if (bl) {
            if ((tab2 == null || tab2.getPosition() == -1) && n != -1) {
                this.setScrollPosition(n, 0.0f, true);
            } else {
                this.animateToTab(n);
            }
            if (n != -1) {
                this.setSelectedTabView(n);
            }
        }
        if (tab2 != null) {
            this.dispatchTabUnselected(tab2);
        }
        this.mSelectedTab = tab;
        if (tab != null) {
            this.dispatchTabSelected(tab);
            return;
        }
    }

    @Deprecated
    public void setOnTabSelectedListener(@Nullable OnTabSelectedListener onTabSelectedListener) {
        OnTabSelectedListener onTabSelectedListener2 = this.mSelectedListener;
        if (onTabSelectedListener2 != null) {
            this.removeOnTabSelectedListener(onTabSelectedListener2);
        }
        this.mSelectedListener = onTabSelectedListener;
        if (onTabSelectedListener != null) {
            this.addOnTabSelectedListener(onTabSelectedListener);
            return;
        }
    }

    void setPagerAdapter(@Nullable PagerAdapter pagerAdapter, boolean bl) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.mPagerAdapter;
        if (pagerAdapter2 != null && (dataSetObserver = this.mPagerAdapterObserver) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.mPagerAdapter = pagerAdapter;
        if (bl && pagerAdapter != null) {
            if (this.mPagerAdapterObserver == null) {
                this.mPagerAdapterObserver = new PagerAdapterObserver();
            }
            pagerAdapter.registerDataSetObserver(this.mPagerAdapterObserver);
        }
        this.populateFromPagerAdapter();
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.ensureScrollAnimator();
        this.mScrollAnimator.addListener(animatorListener);
    }

    public void setScrollPosition(int n, float f, boolean bl) {
        this.setScrollPosition(n, f, bl, true);
    }

    void setScrollPosition(int n, float f, boolean bl, boolean bl2) {
        int n2 = Math.round((float)n + f);
        if (n2 >= 0) {
            ValueAnimator valueAnimator;
            if (n2 >= this.mTabStrip.getChildCount()) {
                return;
            }
            if (bl2) {
                this.mTabStrip.setIndicatorPositionFromTabPosition(n, f);
            }
            if ((valueAnimator = this.mScrollAnimator) != null && valueAnimator.isRunning()) {
                this.mScrollAnimator.cancel();
            }
            this.scrollTo(this.calculateScrollXForTab(n, f), 0);
            if (bl) {
                this.setSelectedTabView(n2);
                return;
            }
            return;
        }
    }

    public void setSelectedTabIndicatorColor(@ColorInt int n) {
        this.mTabStrip.setSelectedIndicatorColor(n);
    }

    public void setSelectedTabIndicatorHeight(int n) {
        this.mTabStrip.setSelectedIndicatorHeight(n);
    }

    public void setTabGravity(int n) {
        if (this.mTabGravity != n) {
            this.mTabGravity = n;
            this.applyModeAndGravity();
            return;
        }
    }

    public void setTabMode(int n) {
        if (n != this.mMode) {
            this.mMode = n;
            this.applyModeAndGravity();
            return;
        }
    }

    public void setTabTextColors(int n, int n2) {
        this.setTabTextColors(TabLayout.createColorStateList(n, n2));
    }

    public void setTabTextColors(@Nullable ColorStateList colorStateList) {
        if (this.mTabTextColors != colorStateList) {
            this.mTabTextColors = colorStateList;
            this.updateAllTabs();
            return;
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(@Nullable PagerAdapter pagerAdapter) {
        this.setPagerAdapter(pagerAdapter, false);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean bl) {
        this.setupWithViewPager(viewPager, bl, false);
    }

    public boolean shouldDelayChildPressedState() {
        if (this.getTabScrollRange() > 0) {
            return true;
        }
        return false;
    }

    void updateTabViews(boolean bl) {
        for (int i = 0; i < this.mTabStrip.getChildCount(); ++i) {
            View view = this.mTabStrip.getChildAt(i);
            view.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
            if (!bl) continue;
            view.requestLayout();
        }
    }

    private class AdapterChangeListener
    implements ViewPager.OnAdapterChangeListener {
        private boolean mAutoRefresh;

        AdapterChangeListener() {
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter2) {
            if (TabLayout.this.mViewPager == viewPager) {
                TabLayout.this.setPagerAdapter(pagerAdapter2, this.mAutoRefresh);
                return;
            }
        }

        void setAutoRefresh(boolean bl) {
            this.mAutoRefresh = bl;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface Mode {
    }

    public static interface OnTabSelectedListener {
        public void onTabReselected(Tab var1);

        public void onTabSelected(Tab var1);

        public void onTabUnselected(Tab var1);
    }

    private class PagerAdapterObserver
    extends DataSetObserver {
        PagerAdapterObserver() {
        }

        public void onChanged() {
            TabLayout.this.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            TabLayout.this.populateFromPagerAdapter();
        }
    }

    private class SlidingTabStrip
    extends LinearLayout {
        private ValueAnimator mIndicatorAnimator;
        private int mIndicatorLeft;
        private int mIndicatorRight;
        private int mLayoutDirection;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        int mSelectedPosition;
        float mSelectionOffset;

        SlidingTabStrip(Context context) {
            super(context);
            this.mSelectedPosition = -1;
            this.mLayoutDirection = -1;
            this.mIndicatorLeft = -1;
            this.mIndicatorRight = -1;
            this.setWillNotDraw(false);
            this.mSelectedIndicatorPaint = new Paint();
        }

        private void updateIndicatorPosition() {
            int n;
            int n2;
            View view = this.getChildAt(this.mSelectedPosition);
            if (view != null && view.getWidth() > 0) {
                n = view.getLeft();
                n2 = view.getRight();
                if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < this.getChildCount() - 1) {
                    view = this.getChildAt(this.mSelectedPosition + 1);
                    float f = this.mSelectionOffset;
                    float f2 = view.getLeft();
                    float f3 = this.mSelectionOffset;
                    n = (int)(f * f2 + (1.0f - f3) * (float)n);
                    n2 = (int)(f3 * (float)view.getRight() + (1.0f - this.mSelectionOffset) * (float)n2);
                }
            } else {
                n = -1;
                n2 = -1;
            }
            this.setIndicatorPosition(n, n2);
        }

        void animateIndicatorToPosition(final int n, int n2) {
            int n3;
            ValueAnimator valueAnimator = this.mIndicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mIndicatorAnimator.cancel();
            }
            final int n4 = ViewCompat.getLayoutDirection((View)this) == 1 ? 1 : 0;
            valueAnimator = this.getChildAt(n);
            if (valueAnimator == null) {
                this.updateIndicatorPosition();
                return;
            }
            final int n5 = valueAnimator.getLeft();
            final int n6 = valueAnimator.getRight();
            if (Math.abs(n - this.mSelectedPosition) <= 1) {
                n4 = this.mIndicatorLeft;
                n3 = this.mIndicatorRight;
            } else {
                n3 = TabLayout.this.dpToPx(24);
                n4 = n < this.mSelectedPosition ? (n4 != 0 ? (n3 = n5 - n3) : (n3 = n6 + n3)) : (n4 != 0 ? (n3 = n6 + n3) : (n3 = n5 - n3));
            }
            if (n4 == n5 && n3 == n6) {
                return;
            }
            this.mIndicatorAnimator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            valueAnimator.setDuration((long)n2);
            valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f = valueAnimator.getAnimatedFraction();
                    SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(n4, n5, f), AnimationUtils.lerp(n3, n6, f));
                }
            });
            valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator object) {
                    object = SlidingTabStrip.this;
                    object.mSelectedPosition = n;
                    object.mSelectionOffset = 0.0f;
                }
            });
            valueAnimator.start();
        }

        boolean childrenNeedLayout() {
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                if (this.getChildAt(i).getWidth() > 0) continue;
                return true;
            }
            return false;
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            int n = this.mIndicatorLeft;
            if (n >= 0 && this.mIndicatorRight > n) {
                canvas.drawRect((float)n, (float)(this.getHeight() - this.mSelectedIndicatorHeight), (float)this.mIndicatorRight, (float)this.getHeight(), this.mSelectedIndicatorPaint);
                return;
            }
        }

        float getIndicatorPosition() {
            return (float)this.mSelectedPosition + this.mSelectionOffset;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            ValueAnimator valueAnimator = this.mIndicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mIndicatorAnimator.cancel();
                long l = this.mIndicatorAnimator.getDuration();
                this.animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0f - this.mIndicatorAnimator.getAnimatedFraction()) * (float)l));
                return;
            }
            this.updateIndicatorPosition();
        }

        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            if (View.MeasureSpec.getMode((int)n) != 1073741824) {
                return;
            }
            if (TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
                Object object;
                int n3;
                int n4 = this.getChildCount();
                int n5 = 0;
                for (n3 = 0; n3 < n4; ++n3) {
                    object = this.getChildAt(n3);
                    if (object.getVisibility() != 0) continue;
                    n5 = Math.max(n5, object.getMeasuredWidth());
                }
                if (n5 <= 0) {
                    return;
                }
                int n6 = TabLayout.this.dpToPx(16);
                n3 = 0;
                if (n5 * n4 <= this.getMeasuredWidth() - n6 * 2) {
                    for (n6 = 0; n6 < n4; ++n6) {
                        object = (LinearLayout.LayoutParams)this.getChildAt(n6).getLayoutParams();
                        if (object.width == n5 && object.weight == 0.0f) continue;
                        object.width = n5;
                        object.weight = 0.0f;
                        n3 = 1;
                    }
                } else {
                    object = TabLayout.this;
                    object.mTabGravity = 0;
                    object.updateTabViews(false);
                    n3 = 1;
                }
                if (n3 != 0) {
                    super.onMeasure(n, n2);
                    return;
                }
                return;
            }
        }

        public void onRtlPropertiesChanged(int n) {
            super.onRtlPropertiesChanged(n);
            if (Build.VERSION.SDK_INT < 23) {
                if (this.mLayoutDirection != n) {
                    this.requestLayout();
                    this.mLayoutDirection = n;
                    return;
                }
                return;
            }
        }

        void setIndicatorPosition(int n, int n2) {
            if (n == this.mIndicatorLeft && n2 == this.mIndicatorRight) {
                return;
            }
            this.mIndicatorLeft = n;
            this.mIndicatorRight = n2;
            ViewCompat.postInvalidateOnAnimation((View)this);
        }

        void setIndicatorPositionFromTabPosition(int n, float f) {
            ValueAnimator valueAnimator = this.mIndicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mIndicatorAnimator.cancel();
            }
            this.mSelectedPosition = n;
            this.mSelectionOffset = f;
            this.updateIndicatorPosition();
        }

        void setSelectedIndicatorColor(int n) {
            if (this.mSelectedIndicatorPaint.getColor() != n) {
                this.mSelectedIndicatorPaint.setColor(n);
                ViewCompat.postInvalidateOnAnimation((View)this);
                return;
            }
        }

        void setSelectedIndicatorHeight(int n) {
            if (this.mSelectedIndicatorHeight != n) {
                this.mSelectedIndicatorHeight = n;
                ViewCompat.postInvalidateOnAnimation((View)this);
                return;
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
        TabView mView;

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
            TabLayout tabLayout = this.mParent;
            if (tabLayout != null) {
                if (tabLayout.getSelectedTabPosition() == this.mPosition) {
                    return true;
                }
                return false;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
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
            TabLayout tabLayout = this.mParent;
            if (tabLayout != null) {
                tabLayout.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        @NonNull
        public Tab setContentDescription(@StringRes int n) {
            TabLayout tabLayout = this.mParent;
            if (tabLayout != null) {
                return this.setContentDescription(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        @NonNull
        public Tab setContentDescription(@Nullable CharSequence charSequence) {
            this.mContentDesc = charSequence;
            this.updateView();
            return this;
        }

        @NonNull
        public Tab setCustomView(@LayoutRes int n) {
            return this.setCustomView(LayoutInflater.from((Context)this.mView.getContext()).inflate(n, (ViewGroup)this.mView, false));
        }

        @NonNull
        public Tab setCustomView(@Nullable View view) {
            this.mCustomView = view;
            this.updateView();
            return this;
        }

        @NonNull
        public Tab setIcon(@DrawableRes int n) {
            TabLayout tabLayout = this.mParent;
            if (tabLayout != null) {
                return this.setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        @NonNull
        public Tab setIcon(@Nullable Drawable drawable2) {
            this.mIcon = drawable2;
            this.updateView();
            return this;
        }

        void setPosition(int n) {
            this.mPosition = n;
        }

        @NonNull
        public Tab setTag(@Nullable Object object) {
            this.mTag = object;
            return this;
        }

        @NonNull
        public Tab setText(@StringRes int n) {
            TabLayout tabLayout = this.mParent;
            if (tabLayout != null) {
                return this.setText(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        @NonNull
        public Tab setText(@Nullable CharSequence charSequence) {
            this.mText = charSequence;
            this.updateView();
            return this;
        }

        void updateView() {
            TabView tabView = this.mView;
            if (tabView != null) {
                tabView.update();
                return;
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface TabGravity {
    }

    public static class TabLayoutOnPageChangeListener
    implements ViewPager.OnPageChangeListener {
        private int mPreviousScrollState;
        private int mScrollState;
        private final WeakReference<TabLayout> mTabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.mTabLayoutRef = new WeakReference<TabLayout>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null) {
                n2 = this.mScrollState;
                boolean bl = false;
                boolean bl2 = n2 != 2 || this.mPreviousScrollState == 1;
                if (this.mScrollState != 2 || this.mPreviousScrollState != 0) {
                    bl = true;
                }
                tabLayout.setScrollPosition(n, f, bl2, bl);
                return;
            }
        }

        @Override
        public void onPageSelected(int n) {
            TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n) {
                if (n < tabLayout.getTabCount()) {
                    int n2 = this.mScrollState;
                    boolean bl = n2 == 0 || n2 == 2 && this.mPreviousScrollState == 0;
                    tabLayout.selectTab(tabLayout.getTabAt(n), bl);
                    return;
                }
                return;
            }
        }

        void reset() {
            this.mScrollState = 0;
            this.mPreviousScrollState = 0;
        }
    }

    class TabView
    extends LinearLayout {
        private ImageView mCustomIconView;
        private TextView mCustomTextView;
        private View mCustomView;
        private int mDefaultMaxLines;
        private ImageView mIconView;
        private Tab mTab;
        private TextView mTextView;

        public TabView(Context context) {
            super(context);
            this.mDefaultMaxLines = 2;
            if (TabLayout.this.mTabBackgroundResId != 0) {
                ViewCompat.setBackground((View)this, AppCompatResources.getDrawable(context, TabLayout.this.mTabBackgroundResId));
            }
            ViewCompat.setPaddingRelative((View)this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
            this.setGravity(17);
            this.setOrientation(1);
            this.setClickable(true);
            ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
        }

        private float approximateLineWidth(Layout layout2, int n, float f) {
            return layout2.getLineWidth(n) * (f / layout2.getPaint().getTextSize());
        }

        private void updateTextAndIcon(@Nullable TextView textView, @Nullable ImageView imageView) {
            Object object = this.mTab;
            Object var8_4 = null;
            Drawable drawable2 = object != null ? object.getIcon() : null;
            object = this.mTab;
            CharSequence charSequence = object != null ? object.getText() : null;
            object = this.mTab;
            object = object != null ? object.getContentDescription() : null;
            if (imageView != null) {
                if (drawable2 != null) {
                    imageView.setImageDrawable(drawable2);
                    imageView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
                imageView.setContentDescription((CharSequence)object);
            }
            boolean bl = TextUtils.isEmpty((CharSequence)charSequence) ^ true;
            if (textView != null) {
                if (bl) {
                    textView.setText(charSequence);
                    textView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText(null);
                }
                textView.setContentDescription((CharSequence)object);
            }
            if (imageView != null) {
                textView = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                int n = 0;
                if (bl && imageView.getVisibility() == 0) {
                    n = TabLayout.this.dpToPx(8);
                }
                if (n != textView.bottomMargin) {
                    textView.bottomMargin = n;
                    imageView.requestLayout();
                }
            }
            if (bl) {
                object = var8_4;
            }
            TooltipCompat.setTooltipText((View)this, (CharSequence)object);
        }

        public Tab getTab() {
            return this.mTab;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public void onMeasure(int n, int n2) {
            int n3 = View.MeasureSpec.getSize((int)n);
            int n4 = View.MeasureSpec.getMode((int)n);
            int n5 = TabLayout.this.getTabMaxWidth();
            if (n5 > 0 && (n4 == 0 || n3 > n5)) {
                n = View.MeasureSpec.makeMeasureSpec((int)TabLayout.this.mTabMaxWidth, (int)Integer.MIN_VALUE);
            }
            super.onMeasure(n, n2);
            if (this.mTextView != null) {
                this.getResources();
                float f = TabLayout.this.mTabTextSize;
                n3 = this.mDefaultMaxLines;
                ImageView imageView = this.mIconView;
                if (imageView != null && imageView.getVisibility() == 0) {
                    n3 = 1;
                } else {
                    imageView = this.mTextView;
                    if (imageView != null && imageView.getLineCount() > 1) {
                        f = TabLayout.this.mTabTextMultiLineSize;
                    }
                }
                float f2 = this.mTextView.getTextSize();
                n5 = this.mTextView.getLineCount();
                n4 = TextViewCompat.getMaxLines(this.mTextView);
                if (f == f2 && (n4 < 0 || n3 == n4)) {
                    return;
                }
                n4 = 1;
                if (TabLayout.this.mMode == 1 && f > f2 && n5 == 1 && ((imageView = this.mTextView.getLayout()) == null || this.approximateLineWidth((Layout)imageView, 0, f) > (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()))) {
                    n4 = 0;
                }
                if (n4 != 0) {
                    this.mTextView.setTextSize(0, f);
                    this.mTextView.setMaxLines(n3);
                    super.onMeasure(n, n2);
                    return;
                }
                return;
            }
        }

        public boolean performClick() {
            boolean bl = super.performClick();
            if (this.mTab != null) {
                if (!bl) {
                    this.playSoundEffect(0);
                }
                this.mTab.select();
                return true;
            }
            return bl;
        }

        void reset() {
            this.setTab(null);
            this.setSelected(false);
        }

        public void setSelected(boolean bl) {
            TextView textView;
            boolean bl2 = this.isSelected() != bl;
            super.setSelected(bl);
            if (bl2 && bl && Build.VERSION.SDK_INT < 16) {
                this.sendAccessibilityEvent(4);
            }
            if ((textView = this.mTextView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.mIconView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.mCustomView) != null) {
                textView.setSelected(bl);
                return;
            }
        }

        void setTab(@Nullable Tab tab) {
            if (tab != this.mTab) {
                this.mTab = tab;
                this.update();
                return;
            }
        }

        final void update() {
            Tab tab = this.mTab;
            View view = tab != null ? tab.getCustomView() : null;
            if (view != null) {
                ViewParent viewParent = view.getParent();
                if (viewParent != this) {
                    if (viewParent != null) {
                        ((ViewGroup)viewParent).removeView(view);
                    }
                    this.addView(view);
                }
                this.mCustomView = view;
                viewParent = this.mTextView;
                if (viewParent != null) {
                    viewParent.setVisibility(8);
                }
                if ((viewParent = this.mIconView) != null) {
                    viewParent.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                this.mCustomTextView = (TextView)view.findViewById(16908308);
                viewParent = this.mCustomTextView;
                if (viewParent != null) {
                    this.mDefaultMaxLines = TextViewCompat.getMaxLines((TextView)viewParent);
                }
                this.mCustomIconView = (ImageView)view.findViewById(16908294);
            } else {
                view = this.mCustomView;
                if (view != null) {
                    this.removeView(view);
                    this.mCustomView = null;
                }
                this.mCustomTextView = null;
                this.mCustomIconView = null;
            }
            view = this.mCustomView;
            boolean bl = false;
            if (view == null) {
                if (this.mIconView == null) {
                    view = (ImageView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
                    this.addView(view, 0);
                    this.mIconView = view;
                }
                if (this.mTextView == null) {
                    view = (TextView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
                    this.addView(view);
                    this.mTextView = view;
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
            boolean bl2 = bl;
            if (tab != null) {
                bl2 = bl;
                if (tab.isSelected()) {
                    bl2 = true;
                }
            }
            this.setSelected(bl2);
        }
    }

    public static class ViewPagerOnTabSelectedListener
    implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @Override
        public void onTabReselected(Tab tab) {
        }

        @Override
        public void onTabSelected(Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {
        }
    }

}

