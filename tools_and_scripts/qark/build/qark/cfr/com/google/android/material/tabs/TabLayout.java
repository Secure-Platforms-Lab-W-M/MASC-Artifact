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
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.RectF
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.text.Layout
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnLayoutChangeListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 *  androidx.appcompat.R
 *  androidx.appcompat.R$styleable
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$layout
 *  com.google.android.material.R$style
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.tabs;

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
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.IBinder;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.tabs.TabItem;
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
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    private static final int INVALID_WIDTH = -1;
    private static final int MIN_INDICATOR_WIDTH = 24;
    public static final int MODE_AUTO = 2;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    public static final int TAB_LABEL_VISIBILITY_LABELED = 1;
    public static final int TAB_LABEL_VISIBILITY_UNLABELED = 0;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private static final Pools.Pool<Tab> tabPool = new Pools.SynchronizedPool<Tab>(16);
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    private final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff.Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final RectF tabViewContentBounds;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int n) {
        SlidingTabIndicator slidingTabIndicator;
        block5 : {
            super(context, attributeSet, n);
            this.tabs = new ArrayList();
            this.tabViewContentBounds = new RectF();
            this.tabMaxWidth = Integer.MAX_VALUE;
            this.selectedListeners = new ArrayList();
            this.tabViewPool = new Pools.SimplePool<TabView>(12);
            this.setHorizontalScrollBarEnabled(false);
            this.slidingTabIndicator = slidingTabIndicator = new SlidingTabIndicator(context);
            super.addView((View)slidingTabIndicator, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
            attributeSet = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout, R.styleable.TabLayout_tabTextAppearance);
            if (this.getBackground() instanceof ColorDrawable) {
                slidingTabIndicator = (ColorDrawable)this.getBackground();
                MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
                materialShapeDrawable.setFillColor(ColorStateList.valueOf((int)slidingTabIndicator.getColor()));
                materialShapeDrawable.initializeElevationOverlay(context);
                materialShapeDrawable.setElevation(ViewCompat.getElevation((View)this));
                ViewCompat.setBackground((View)this, materialShapeDrawable);
            }
            this.slidingTabIndicator.setSelectedIndicatorHeight(attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
            this.slidingTabIndicator.setSelectedIndicatorColor(attributeSet.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
            this.setSelectedTabIndicator(MaterialResources.getDrawable(context, (TypedArray)attributeSet, R.styleable.TabLayout_tabIndicator));
            this.setSelectedTabIndicatorGravity(attributeSet.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
            this.setTabIndicatorFullWidth(attributeSet.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
            this.tabPaddingBottom = n = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
            this.tabPaddingEnd = n;
            this.tabPaddingTop = n;
            this.tabPaddingStart = n;
            this.tabPaddingStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
            this.tabPaddingTop = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
            this.tabPaddingEnd = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
            this.tabPaddingBottom = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
            this.tabTextAppearance = n = attributeSet.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
            slidingTabIndicator = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
            this.tabTextSize = slidingTabIndicator.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
            this.tabTextColors = MaterialResources.getColorStateList(context, (TypedArray)slidingTabIndicator, R.styleable.TextAppearance_android_textColor);
            if (!attributeSet.hasValue(R.styleable.TabLayout_tabTextColor)) break block5;
            this.tabTextColors = MaterialResources.getColorStateList(context, (TypedArray)attributeSet, R.styleable.TabLayout_tabTextColor);
        }
        if (attributeSet.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            n = attributeSet.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.tabTextColors = TabLayout.createColorStateList(this.tabTextColors.getDefaultColor(), n);
        }
        this.tabIconTint = MaterialResources.getColorStateList(context, (TypedArray)attributeSet, R.styleable.TabLayout_tabIconTint);
        this.tabIconTintMode = ViewUtils.parseTintMode(attributeSet.getInt(R.styleable.TabLayout_tabIconTintMode, -1), null);
        this.tabRippleColorStateList = MaterialResources.getColorStateList(context, (TypedArray)attributeSet, R.styleable.TabLayout_tabRippleColor);
        this.tabIndicatorAnimationDuration = attributeSet.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
        this.requestedTabMinWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
        this.requestedTabMaxWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
        this.tabBackgroundResId = attributeSet.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.contentInsetStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mode = attributeSet.getInt(R.styleable.TabLayout_tabMode, 1);
        this.tabGravity = attributeSet.getInt(R.styleable.TabLayout_tabGravity, 0);
        this.inlineLabel = attributeSet.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
        this.unboundedRipple = attributeSet.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
        attributeSet.recycle();
        context = this.getResources();
        this.tabTextMultiLineSize = context.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
        this.scrollableTabMinWidth = context.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
        this.applyModeAndGravity();
        return;
        finally {
            slidingTabIndicator.recycle();
        }
    }

    private void addTabFromItemView(TabItem tabItem) {
        Tab tab = this.newTab();
        if (tabItem.text != null) {
            tab.setText(tabItem.text);
        }
        if (tabItem.icon != null) {
            tab.setIcon(tabItem.icon);
        }
        if (tabItem.customLayout != 0) {
            tab.setCustomView(tabItem.customLayout);
        }
        if (!TextUtils.isEmpty((CharSequence)tabItem.getContentDescription())) {
            tab.setContentDescription(tabItem.getContentDescription());
        }
        this.addTab(tab);
    }

    private void addTabView(Tab tab) {
        TabView tabView = tab.view;
        tabView.setSelected(false);
        tabView.setActivated(false);
        this.slidingTabIndicator.addView((View)tabView, tab.getPosition(), (ViewGroup.LayoutParams)this.createLayoutParamsForTabs());
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
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this) && !this.slidingTabIndicator.childrenNeedLayout()) {
            int n2;
            int n3 = this.getScrollX();
            if (n3 != (n2 = this.calculateScrollXForTab(n, 0.0f))) {
                this.ensureScrollAnimator();
                this.scrollAnimator.setIntValues(new int[]{n3, n2});
                this.scrollAnimator.start();
            }
            this.slidingTabIndicator.animateIndicatorToPosition(n, this.tabIndicatorAnimationDuration);
            return;
        }
        this.setScrollPosition(n, 0.0f, true);
    }

    private void applyModeAndGravity() {
        int n = 0;
        int n2 = this.mode;
        if (n2 == 0 || n2 == 2) {
            n = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        }
        ViewCompat.setPaddingRelative((View)this.slidingTabIndicator, n, 0, 0, 0);
        n = this.mode;
        if (n != 0) {
            if (n == 1 || n == 2) {
                this.slidingTabIndicator.setGravity(1);
            }
        } else {
            this.slidingTabIndicator.setGravity(8388611);
        }
        this.updateTabViews(true);
    }

    private int calculateScrollXForTab(int n, float f) {
        int n2 = this.mode;
        int n3 = 0;
        if (n2 != 0 && n2 != 2) {
            return 0;
        }
        View view = this.slidingTabIndicator.getChildAt(n);
        View view2 = n + 1 < this.slidingTabIndicator.getChildCount() ? this.slidingTabIndicator.getChildAt(n + 1) : null;
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

    private void configureTab(Tab tab, int n) {
        tab.setPosition(n);
        this.tabs.add(n, tab);
        int n2 = this.tabs.size();
        ++n;
        while (n < n2) {
            this.tabs.get(n).setPosition(n);
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

    private TabView createTabView(Tab tab) {
        Pools.Pool<TabView> pool = this.tabViewPool;
        pool = pool != null ? pool.acquire() : null;
        Object object = pool;
        if (pool == null) {
            object = new TabView(this.getContext());
        }
        object.setTab(tab);
        object.setFocusable(true);
        object.setMinimumWidth(this.getTabMinWidth());
        if (TextUtils.isEmpty((CharSequence)tab.contentDesc)) {
            object.setContentDescription(tab.text);
            return object;
        }
        object.setContentDescription(tab.contentDesc);
        return object;
    }

    private void dispatchTabReselected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabReselected(tab);
        }
    }

    private void dispatchTabSelected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabUnselected(tab);
        }
    }

    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            ValueAnimator valueAnimator;
            this.scrollAnimator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long)this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TabLayout.this.scrollTo(((Integer)valueAnimator.getAnimatedValue()).intValue(), 0);
                }
            });
        }
    }

    private int getDefaultHeight() {
        boolean bl;
        boolean bl2 = false;
        int n = 0;
        int n2 = this.tabs.size();
        do {
            bl = bl2;
            if (n >= n2) break;
            Tab tab = this.tabs.get(n);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty((CharSequence)tab.getText())) {
                bl = true;
                break;
            }
            ++n;
        } while (true);
        if (bl && !this.inlineLabel) {
            return 72;
        }
        return 48;
    }

    private int getTabMinWidth() {
        int n = this.requestedTabMinWidth;
        if (n != -1) {
            return n;
        }
        n = this.mode;
        if (n != 0 && n != 2) {
            return 0;
        }
        return this.scrollableTabMinWidth;
    }

    private int getTabScrollRange() {
        return Math.max(0, this.slidingTabIndicator.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
    }

    private void removeTabViewAt(int n) {
        TabView tabView = (TabView)this.slidingTabIndicator.getChildAt(n);
        this.slidingTabIndicator.removeViewAt(n);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        this.requestLayout();
    }

    private void setSelectedTabView(int n) {
        int n2 = this.slidingTabIndicator.getChildCount();
        if (n < n2) {
            for (int i = 0; i < n2; ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                boolean bl = false;
                boolean bl2 = i == n;
                view.setSelected(bl2);
                bl2 = bl;
                if (i == n) {
                    bl2 = true;
                }
                view.setActivated(bl2);
            }
        }
    }

    private void setupWithViewPager(ViewPager viewPager, boolean bl, boolean bl2) {
        Object object = this.viewPager;
        if (object != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                object.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            if ((object = this.adapterChangeListener) != null) {
                this.viewPager.removeOnAdapterChangeListener((ViewPager.OnAdapterChangeListener)object);
            }
        }
        if ((object = this.currentVpSelectedListener) != null) {
            this.removeOnTabSelectedListener((BaseOnTabSelectedListener)object);
            this.currentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.viewPager = viewPager;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.pageChangeListener);
            this.currentVpSelectedListener = object = new ViewPagerOnTabSelectedListener(viewPager);
            this.addOnTabSelectedListener((BaseOnTabSelectedListener)object);
            object = viewPager.getAdapter();
            if (object != null) {
                this.setPagerAdapter((PagerAdapter)object, bl);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener();
            }
            this.adapterChangeListener.setAutoRefresh(bl);
            viewPager.addOnAdapterChangeListener(this.adapterChangeListener);
            this.setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.viewPager = null;
            this.setPagerAdapter(null, false);
        }
        this.setupViewPagerImplicitly = bl2;
    }

    private void updateAllTabs() {
        int n = this.tabs.size();
        for (int i = 0; i < n; ++i) {
            this.tabs.get(i).updateView();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mode == 1 && this.tabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
            return;
        }
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
    }

    @Deprecated
    public void addOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        if (!this.selectedListeners.contains(baseOnTabSelectedListener)) {
            this.selectedListeners.add(baseOnTabSelectedListener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.addOnTabSelectedListener((BaseOnTabSelectedListener)onTabSelectedListener);
    }

    public void addTab(Tab tab) {
        this.addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int n) {
        this.addTab(tab, n, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int n, boolean bl) {
        if (tab.parent == this) {
            this.configureTab(tab, n);
            this.addTabView(tab);
            if (bl) {
                tab.select();
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void addTab(Tab tab, boolean bl) {
        this.addTab(tab, this.tabs.size(), bl);
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
        this.selectedListeners.clear();
    }

    protected Tab createTabFromPool() {
        Tab tab;
        Tab tab2 = tab = tabPool.acquire();
        if (tab == null) {
            tab2 = new Tab();
        }
        return tab2;
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return this.generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        Tab tab = this.selectedTab;
        if (tab != null) {
            return tab.getPosition();
        }
        return -1;
    }

    public Tab getTabAt(int n) {
        if (n >= 0 && n < this.getTabCount()) {
            return this.tabs.get(n);
        }
        return null;
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    int getTabMaxWidth() {
        return this.tabMaxWidth;
    }

    public int getTabMode() {
        return this.mode;
    }

    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public Tab newTab() {
        Tab tab = this.createTabFromPool();
        tab.parent = this;
        tab.view = this.createTabView(tab);
        return tab;
    }

    protected void onAttachedToWindow() {
        ViewParent viewParent;
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation((View)this);
        if (this.viewPager == null && (viewParent = this.getParent()) instanceof ViewPager) {
            this.setupWithViewPager((ViewPager)viewParent, true, true);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            this.setupWithViewPager(null);
            this.setupViewPagerImplicitly = false;
        }
    }

    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View view = this.slidingTabIndicator.getChildAt(i);
            if (!(view instanceof TabView)) continue;
            ((TabView)view).drawBackground(canvas);
        }
        super.onDraw(canvas);
    }

    protected void onMeasure(int n, int n2) {
        block9 : {
            View view;
            int n3;
            block12 : {
                int n4;
                block10 : {
                    int n5;
                    block11 : {
                        int n6 = (int)ViewUtils.dpToPx(this.getContext(), this.getDefaultHeight());
                        n3 = View.MeasureSpec.getMode((int)n2);
                        n4 = 0;
                        n5 = 0;
                        if (n3 != Integer.MIN_VALUE) {
                            n3 = n3 != 0 ? n2 : View.MeasureSpec.makeMeasureSpec((int)(this.getPaddingTop() + n6 + this.getPaddingBottom()), (int)1073741824);
                        } else {
                            n3 = n2;
                            if (this.getChildCount() == 1) {
                                n3 = n2;
                                if (View.MeasureSpec.getSize((int)n2) >= n6) {
                                    this.getChildAt(0).setMinimumHeight(n6);
                                    n3 = n2;
                                }
                            }
                        }
                        n6 = View.MeasureSpec.getSize((int)n);
                        if (View.MeasureSpec.getMode((int)n) != 0) {
                            n2 = this.requestedTabMaxWidth;
                            if (n2 <= 0) {
                                n2 = (int)((float)n6 - ViewUtils.dpToPx(this.getContext(), 56));
                            }
                            this.tabMaxWidth = n2;
                        }
                        super.onMeasure(n, n3);
                        if (this.getChildCount() != 1) break block9;
                        view = this.getChildAt(0);
                        n = 0;
                        n2 = this.mode;
                        if (n2 == 0) break block10;
                        if (n2 == 1) break block11;
                        if (n2 == 2) break block10;
                        break block12;
                    }
                    n = n5;
                    if (view.getMeasuredWidth() != this.getMeasuredWidth()) {
                        n = 1;
                    }
                    break block12;
                }
                n = n4;
                if (view.getMeasuredWidth() < this.getMeasuredWidth()) {
                    n = 1;
                }
            }
            if (n != 0) {
                n = TabLayout.getChildMeasureSpec((int)n3, (int)(this.getPaddingTop() + this.getPaddingBottom()), (int)view.getLayoutParams().height);
                view.measure(View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824), n);
            }
        }
    }

    void populateFromPagerAdapter() {
        this.removeAllTabs();
        Object object = this.pagerAdapter;
        if (object != null) {
            int n;
            int n2 = object.getCount();
            for (n = 0; n < n2; ++n) {
                this.addTab(this.newTab().setText(this.pagerAdapter.getPageTitle(n)), false);
            }
            object = this.viewPager;
            if (object != null && n2 > 0 && (n = object.getCurrentItem()) != this.getSelectedTabPosition() && n < this.getTabCount()) {
                this.selectTab(this.getTabAt(n));
            }
        }
    }

    protected boolean releaseFromTabPool(Tab tab) {
        return tabPool.release(tab);
    }

    public void removeAllTabs() {
        for (int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; --i) {
            this.removeTabViewAt(i);
        }
        Iterator<Tab> iterator = this.tabs.iterator();
        while (iterator.hasNext()) {
            Tab tab = iterator.next();
            iterator.remove();
            tab.reset();
            this.releaseFromTabPool(tab);
        }
        this.selectedTab = null;
    }

    @Deprecated
    public void removeOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        this.selectedListeners.remove(baseOnTabSelectedListener);
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.removeOnTabSelectedListener((BaseOnTabSelectedListener)onTabSelectedListener);
    }

    public void removeTab(Tab tab) {
        if (tab.parent == this) {
            this.removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int n) {
        Tab tab = this.selectedTab;
        int n2 = tab != null ? tab.getPosition() : 0;
        this.removeTabViewAt(n);
        tab = this.tabs.remove(n);
        if (tab != null) {
            tab.reset();
            this.releaseFromTabPool(tab);
        }
        int n3 = this.tabs.size();
        for (int i = n; i < n3; ++i) {
            this.tabs.get(i).setPosition(i);
        }
        if (n2 == n) {
            tab = this.tabs.isEmpty() ? null : this.tabs.get(Math.max(0, n - 1));
            this.selectTab(tab);
        }
    }

    public void selectTab(Tab tab) {
        this.selectTab(tab, true);
    }

    public void selectTab(Tab tab, boolean bl) {
        Tab tab2 = this.selectedTab;
        if (tab2 == tab) {
            if (tab2 != null) {
                this.dispatchTabReselected(tab);
                this.animateToTab(tab.getPosition());
                return;
            }
        } else {
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
            this.selectedTab = tab;
            if (tab2 != null) {
                this.dispatchTabUnselected(tab2);
            }
            if (tab != null) {
                this.dispatchTabSelected(tab);
            }
        }
    }

    public void setElevation(float f) {
        super.setElevation(f);
        MaterialShapeUtils.setElevation((View)this, f);
    }

    public void setInlineLabel(boolean bl) {
        if (this.inlineLabel != bl) {
            this.inlineLabel = bl;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                if (!(view instanceof TabView)) continue;
                ((TabView)view).updateOrientation();
            }
            this.applyModeAndGravity();
        }
    }

    public void setInlineLabelResource(int n) {
        this.setInlineLabel(this.getResources().getBoolean(n));
    }

    @Deprecated
    public void setOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        BaseOnTabSelectedListener baseOnTabSelectedListener2 = this.selectedListener;
        if (baseOnTabSelectedListener2 != null) {
            this.removeOnTabSelectedListener(baseOnTabSelectedListener2);
        }
        this.selectedListener = baseOnTabSelectedListener;
        if (baseOnTabSelectedListener != null) {
            this.addOnTabSelectedListener(baseOnTabSelectedListener);
        }
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.setOnTabSelectedListener((BaseOnTabSelectedListener)onTabSelectedListener);
    }

    void setPagerAdapter(PagerAdapter pagerAdapter, boolean bl) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (pagerAdapter2 != null && (dataSetObserver = this.pagerAdapterObserver) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.pagerAdapter = pagerAdapter;
        if (bl && pagerAdapter != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver();
            }
            pagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
        }
        this.populateFromPagerAdapter();
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.ensureScrollAnimator();
        this.scrollAnimator.addListener(animatorListener);
    }

    public void setScrollPosition(int n, float f, boolean bl) {
        this.setScrollPosition(n, f, bl, true);
    }

    public void setScrollPosition(int n, float f, boolean bl, boolean bl2) {
        int n2 = Math.round((float)n + f);
        if (n2 >= 0) {
            ValueAnimator valueAnimator;
            if (n2 >= this.slidingTabIndicator.getChildCount()) {
                return;
            }
            if (bl2) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(n, f);
            }
            if ((valueAnimator = this.scrollAnimator) != null && valueAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            this.scrollTo(this.calculateScrollXForTab(n, f), 0);
            if (bl) {
                this.setSelectedTabView(n2);
            }
            return;
        }
    }

    public void setSelectedTabIndicator(int n) {
        if (n != 0) {
            this.setSelectedTabIndicator(AppCompatResources.getDrawable(this.getContext(), n));
            return;
        }
        this.setSelectedTabIndicator(null);
    }

    public void setSelectedTabIndicator(Drawable drawable2) {
        if (this.tabSelectedIndicator != drawable2) {
            this.tabSelectedIndicator = drawable2;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }

    public void setSelectedTabIndicatorColor(int n) {
        this.slidingTabIndicator.setSelectedIndicatorColor(n);
    }

    public void setSelectedTabIndicatorGravity(int n) {
        if (this.tabIndicatorGravity != n) {
            this.tabIndicatorGravity = n;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int n) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(n);
    }

    public void setTabGravity(int n) {
        if (this.tabGravity != n) {
            this.tabGravity = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.tabIconTint != colorStateList) {
            this.tabIconTint = colorStateList;
            this.updateAllTabs();
        }
    }

    public void setTabIconTintResource(int n) {
        this.setTabIconTint(AppCompatResources.getColorStateList(this.getContext(), n));
    }

    public void setTabIndicatorFullWidth(boolean bl) {
        this.tabIndicatorFullWidth = bl;
        ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    }

    public void setTabMode(int n) {
        if (n != this.mode) {
            this.mode = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.tabRippleColorStateList != colorStateList) {
            this.tabRippleColorStateList = colorStateList;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                colorStateList = this.slidingTabIndicator.getChildAt(i);
                if (!(colorStateList instanceof TabView)) continue;
                ((TabView)colorStateList).updateBackgroundDrawable(this.getContext());
            }
        }
    }

    public void setTabRippleColorResource(int n) {
        this.setTabRippleColor(AppCompatResources.getColorStateList(this.getContext(), n));
    }

    public void setTabTextColors(int n, int n2) {
        this.setTabTextColors(TabLayout.createColorStateList(n, n2));
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.tabTextColors != colorStateList) {
            this.tabTextColors = colorStateList;
            this.updateAllTabs();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        this.setPagerAdapter(pagerAdapter, false);
    }

    public void setUnboundedRipple(boolean bl) {
        if (this.unboundedRipple != bl) {
            this.unboundedRipple = bl;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                if (!(view instanceof TabView)) continue;
                ((TabView)view).updateBackgroundDrawable(this.getContext());
            }
        }
    }

    public void setUnboundedRippleResource(int n) {
        this.setUnboundedRipple(this.getResources().getBoolean(n));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(ViewPager viewPager, boolean bl) {
        this.setupWithViewPager(viewPager, bl, false);
    }

    public boolean shouldDelayChildPressedState() {
        if (this.getTabScrollRange() > 0) {
            return true;
        }
        return false;
    }

    void updateTabViews(boolean bl) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View view = this.slidingTabIndicator.getChildAt(i);
            view.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
            if (!bl) continue;
            view.requestLayout();
        }
    }

    private class AdapterChangeListener
    implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;

        AdapterChangeListener() {
        }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (TabLayout.this.viewPager == viewPager) {
                TabLayout.this.setPagerAdapter(pagerAdapter2, this.autoRefresh);
            }
        }

        void setAutoRefresh(boolean bl) {
            this.autoRefresh = bl;
        }
    }

    @Deprecated
    public static interface BaseOnTabSelectedListener<T extends Tab> {
        public void onTabReselected(T var1);

        public void onTabSelected(T var1);

        public void onTabUnselected(T var1);
    }

    public static @interface LabelVisibility {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Mode {
    }

    public static interface OnTabSelectedListener
    extends BaseOnTabSelectedListener<Tab> {
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

    private class SlidingTabIndicator
    extends LinearLayout {
        private final GradientDrawable defaultSelectionIndicator;
        private ValueAnimator indicatorAnimator;
        private int indicatorLeft;
        private int indicatorRight;
        private int layoutDirection;
        private int selectedIndicatorHeight;
        private final Paint selectedIndicatorPaint;
        int selectedPosition;
        float selectionOffset;

        SlidingTabIndicator(Context context) {
            super(context);
            this.selectedPosition = -1;
            this.layoutDirection = -1;
            this.indicatorLeft = -1;
            this.indicatorRight = -1;
            this.setWillNotDraw(false);
            this.selectedIndicatorPaint = new Paint();
            this.defaultSelectionIndicator = new GradientDrawable();
        }

        private void calculateTabViewContentBounds(TabView tabView, RectF rectF) {
            int n = tabView.getContentWidth();
            int n2 = (int)ViewUtils.dpToPx(this.getContext(), 24);
            int n3 = n;
            if (n < n2) {
                n3 = n2;
            }
            n = (tabView.getLeft() + tabView.getRight()) / 2;
            n2 = n3 / 2;
            rectF.set((float)(n - n2), 0.0f, (float)((n3 /= 2) + n), 0.0f);
        }

        private void updateIndicatorPosition() {
            int n;
            int n2;
            View view = this.getChildAt(this.selectedPosition);
            if (view != null && view.getWidth() > 0) {
                n2 = view.getLeft();
                n = view.getRight();
                int n3 = n2;
                int n4 = n;
                if (!TabLayout.this.tabIndicatorFullWidth) {
                    n3 = n2;
                    n4 = n;
                    if (view instanceof TabView) {
                        this.calculateTabViewContentBounds((TabView)view, TabLayout.this.tabViewContentBounds);
                        n3 = (int)TabLayout.access$1100((TabLayout)TabLayout.this).left;
                        n4 = (int)TabLayout.access$1100((TabLayout)TabLayout.this).right;
                    }
                }
                n2 = n3;
                n = n4;
                if (this.selectionOffset > 0.0f) {
                    n2 = n3;
                    n = n4;
                    if (this.selectedPosition < this.getChildCount() - 1) {
                        view = this.getChildAt(this.selectedPosition + 1);
                        int n5 = view.getLeft();
                        int n6 = view.getRight();
                        n = n5;
                        n2 = n6;
                        if (!TabLayout.this.tabIndicatorFullWidth) {
                            n = n5;
                            n2 = n6;
                            if (view instanceof TabView) {
                                this.calculateTabViewContentBounds((TabView)view, TabLayout.this.tabViewContentBounds);
                                n = (int)TabLayout.access$1100((TabLayout)TabLayout.this).left;
                                n2 = (int)TabLayout.access$1100((TabLayout)TabLayout.this).right;
                            }
                        }
                        float f = this.selectionOffset;
                        n3 = (int)((float)n * f + (1.0f - f) * (float)n3);
                        n = (int)((float)n2 * f + (1.0f - f) * (float)n4);
                        n2 = n3;
                    }
                }
            } else {
                n2 = -1;
                n = -1;
            }
            this.setIndicatorPosition(n2, n);
        }

        void animateIndicatorToPosition(final int n, int n2) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            if ((valueAnimator = this.getChildAt(n)) == null) {
                this.updateIndicatorPosition();
                return;
            }
            final int n3 = valueAnimator.getLeft();
            final int n4 = valueAnimator.getRight();
            if (!TabLayout.this.tabIndicatorFullWidth && valueAnimator instanceof TabView) {
                this.calculateTabViewContentBounds((TabView)valueAnimator, TabLayout.this.tabViewContentBounds);
                n3 = (int)TabLayout.access$1100((TabLayout)TabLayout.this).left;
                n4 = (int)TabLayout.access$1100((TabLayout)TabLayout.this).right;
            }
            final int n5 = this.indicatorLeft;
            final int n6 = this.indicatorRight;
            if (n5 == n3 && n6 == n4) {
                return;
            }
            this.indicatorAnimator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            valueAnimator.setDuration((long)n2);
            valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f = valueAnimator.getAnimatedFraction();
                    SlidingTabIndicator.this.setIndicatorPosition(AnimationUtils.lerp(n5, n3, f), AnimationUtils.lerp(n6, n4, f));
                }
            });
            valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator2) {
                    SlidingTabIndicator.this.selectedPosition = n;
                    SlidingTabIndicator.this.selectionOffset = 0.0f;
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
            int n = 0;
            if (TabLayout.this.tabSelectedIndicator != null) {
                n = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
            }
            if (this.selectedIndicatorHeight >= 0) {
                n = this.selectedIndicatorHeight;
            }
            int n2 = 0;
            int n3 = 0;
            int n4 = TabLayout.this.tabIndicatorGravity;
            if (n4 != 0) {
                if (n4 != 1) {
                    if (n4 != 2) {
                        if (n4 != 3) {
                            n = n2;
                        } else {
                            n = 0;
                            n3 = this.getHeight();
                        }
                    } else {
                        n2 = 0;
                        n3 = n;
                        n = n2;
                    }
                } else {
                    n3 = (this.getHeight() - n) / 2;
                    n2 = (this.getHeight() + n) / 2;
                    n = n3;
                    n3 = n2;
                }
            } else {
                n = this.getHeight() - n;
                n3 = this.getHeight();
            }
            n2 = this.indicatorLeft;
            if (n2 >= 0 && this.indicatorRight > n2) {
                GradientDrawable gradientDrawable = TabLayout.this.tabSelectedIndicator != null ? TabLayout.this.tabSelectedIndicator : this.defaultSelectionIndicator;
                gradientDrawable = DrawableCompat.wrap((Drawable)gradientDrawable);
                gradientDrawable.setBounds(this.indicatorLeft, n, this.indicatorRight, n3);
                if (this.selectedIndicatorPaint != null) {
                    if (Build.VERSION.SDK_INT == 21) {
                        gradientDrawable.setColorFilter(this.selectedIndicatorPaint.getColor(), PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint((Drawable)gradientDrawable, this.selectedIndicatorPaint.getColor());
                    }
                }
                gradientDrawable.draw(canvas);
            }
            super.draw(canvas);
        }

        float getIndicatorPosition() {
            return (float)this.selectedPosition + this.selectionOffset;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
                long l = this.indicatorAnimator.getDuration();
                this.animateIndicatorToPosition(this.selectedPosition, Math.round((1.0f - this.indicatorAnimator.getAnimatedFraction()) * (float)l));
                return;
            }
            this.updateIndicatorPosition();
        }

        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            if (View.MeasureSpec.getMode((int)n) != 1073741824) {
                return;
            }
            if (TabLayout.this.tabGravity == 1 || TabLayout.this.mode == 2) {
                View view;
                int n3;
                int n4;
                int n5 = this.getChildCount();
                int n6 = 0;
                for (n4 = 0; n4 < n5; ++n4) {
                    view = this.getChildAt(n4);
                    n3 = n6;
                    if (view.getVisibility() == 0) {
                        n3 = Math.max(n6, view.getMeasuredWidth());
                    }
                    n6 = n3;
                }
                if (n6 <= 0) {
                    return;
                }
                n3 = (int)ViewUtils.dpToPx(this.getContext(), 16);
                n4 = 0;
                if (n6 * n5 <= this.getMeasuredWidth() - n3 * 2) {
                    for (n3 = 0; n3 < n5; ++n3) {
                        view = (LinearLayout.LayoutParams)this.getChildAt(n3).getLayoutParams();
                        if (view.width == n6 && view.weight == 0.0f) continue;
                        view.width = n6;
                        view.weight = 0.0f;
                        n4 = 1;
                    }
                } else {
                    TabLayout.this.tabGravity = 0;
                    TabLayout.this.updateTabViews(false);
                    n4 = 1;
                }
                if (n4 != 0) {
                    super.onMeasure(n, n2);
                }
            }
        }

        public void onRtlPropertiesChanged(int n) {
            super.onRtlPropertiesChanged(n);
            if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != n) {
                this.requestLayout();
                this.layoutDirection = n;
            }
        }

        void setIndicatorPosition(int n, int n2) {
            if (n != this.indicatorLeft || n2 != this.indicatorRight) {
                this.indicatorLeft = n;
                this.indicatorRight = n2;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        void setIndicatorPositionFromTabPosition(int n, float f) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = n;
            this.selectionOffset = f;
            this.updateIndicatorPosition();
        }

        void setSelectedIndicatorColor(int n) {
            if (this.selectedIndicatorPaint.getColor() != n) {
                this.selectedIndicatorPaint.setColor(n);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        void setSelectedIndicatorHeight(int n) {
            if (this.selectedIndicatorHeight != n) {
                this.selectedIndicatorHeight = n;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

    }

    public static class Tab {
        public static final int INVALID_POSITION = -1;
        private CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        private int labelVisibilityMode = 1;
        public TabLayout parent;
        private int position = -1;
        private Object tag;
        private CharSequence text;
        public TabView view;

        public BadgeDrawable getBadge() {
            return this.view.getBadge();
        }

        public CharSequence getContentDescription() {
            TabView tabView = this.view;
            if (tabView == null) {
                return null;
            }
            return tabView.getContentDescription();
        }

        public View getCustomView() {
            return this.customView;
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public BadgeDrawable getOrCreateBadge() {
            return this.view.getOrCreateBadge();
        }

        public int getPosition() {
            return this.position;
        }

        public int getTabLabelVisibility() {
            return this.labelVisibilityMode;
        }

        public Object getTag() {
            return this.tag;
        }

        public CharSequence getText() {
            return this.text;
        }

        public boolean isSelected() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                if (tabLayout.getSelectedTabPosition() == this.position) {
                    return true;
                }
                return false;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public void removeBadge() {
            this.view.removeBadge();
        }

        void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }

        public void select() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                tabLayout.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setContentDescription(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.contentDesc = charSequence;
            this.updateView();
            return this;
        }

        public Tab setCustomView(int n) {
            return this.setCustomView(LayoutInflater.from((Context)this.view.getContext()).inflate(n, (ViewGroup)this.view, false));
        }

        public Tab setCustomView(View view) {
            this.customView = view;
            this.updateView();
            return this;
        }

        public Tab setIcon(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setIcon(Drawable drawable2) {
            this.icon = drawable2;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            this.updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        void setPosition(int n) {
            this.position = n;
        }

        public Tab setTabLabelVisibility(int n) {
            this.labelVisibilityMode = n;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            this.updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        public Tab setTag(Object object) {
            this.tag = object;
            return this;
        }

        public Tab setText(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setText(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setText(CharSequence charSequence) {
            if (TextUtils.isEmpty((CharSequence)this.contentDesc) && !TextUtils.isEmpty((CharSequence)charSequence)) {
                this.view.setContentDescription(charSequence);
            }
            this.text = charSequence;
            this.updateView();
            return this;
        }

        void updateView() {
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.update();
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TabGravity {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TabIndicatorGravity {
    }

    public static class TabLayoutOnPageChangeListener
    implements ViewPager.OnPageChangeListener {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<TabLayout>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.previousScrollState = this.scrollState;
            this.scrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null) {
                n2 = this.scrollState;
                boolean bl = false;
                boolean bl2 = n2 != 2 || this.previousScrollState == 1;
                if (this.scrollState != 2 || this.previousScrollState != 0) {
                    bl = true;
                }
                tabLayout.setScrollPosition(n, f, bl2, bl);
            }
        }

        @Override
        public void onPageSelected(int n) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n && n < tabLayout.getTabCount()) {
                int n2 = this.scrollState;
                boolean bl = n2 == 0 || n2 == 2 && this.previousScrollState == 0;
                tabLayout.selectTab(tabLayout.getTabAt(n), bl);
            }
        }

        void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    public final class TabView
    extends LinearLayout {
        private View badgeAnchorView;
        private BadgeDrawable badgeDrawable;
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;

        public TabView(Context context) {
            super(context);
            this.defaultMaxLines = 2;
            this.updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative((View)this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
            this.setGravity(17);
            this.setOrientation(TabLayout.this.inlineLabel ^ true);
            this.setClickable(true);
            ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
            ViewCompat.setAccessibilityDelegate((View)this, null);
        }

        private void addOnLayoutChangeListener(final View view) {
            if (view == null) {
                return;
            }
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){

                public void onLayoutChange(View view2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
                    if (view.getVisibility() == 0) {
                        TabView.this.tryUpdateBadgeDrawableBounds(view);
                    }
                }
            });
        }

        private float approximateLineWidth(Layout layout2, int n, float f) {
            return layout2.getLineWidth(n) * (f / layout2.getPaint().getTextSize());
        }

        private FrameLayout createPreApi18BadgeAnchorRoot() {
            FrameLayout frameLayout = new FrameLayout(this.getContext());
            frameLayout.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
            return frameLayout;
        }

        private void drawBackground(Canvas canvas) {
            Drawable drawable2 = this.baseBackgroundDrawable;
            if (drawable2 != null) {
                drawable2.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }

        private BadgeDrawable getBadge() {
            return this.badgeDrawable;
        }

        private int getContentWidth() {
            boolean bl = false;
            int n = 0;
            int n2 = 0;
            TextView textView = this.textView;
            ImageView imageView = this.iconView;
            View view = this.customView;
            for (int i = 0; i < 3; ++i) {
                View view2 = new View[]{textView, imageView, view}[i];
                boolean bl2 = bl;
                int n3 = n;
                int n4 = n2;
                if (view2 != null) {
                    bl2 = bl;
                    n3 = n;
                    n4 = n2;
                    if (view2.getVisibility() == 0) {
                        n4 = n3 = view2.getLeft();
                        if (bl) {
                            n4 = Math.min(n, n3);
                        }
                        n = n4;
                        n4 = n3 = view2.getRight();
                        if (bl) {
                            n4 = Math.max(n2, n3);
                        }
                        bl2 = true;
                        n3 = n;
                    }
                }
                bl = bl2;
                n = n3;
                n2 = n4;
            }
            return n2 - n;
        }

        private FrameLayout getCustomParentForBadge(View view) {
            ImageView imageView = this.iconView;
            FrameLayout frameLayout = null;
            if (view != imageView && view != this.textView) {
                return null;
            }
            if (BadgeUtils.USE_COMPAT_PARENT) {
                frameLayout = (FrameLayout)view.getParent();
            }
            return frameLayout;
        }

        private BadgeDrawable getOrCreateBadge() {
            if (this.badgeDrawable == null) {
                this.badgeDrawable = BadgeDrawable.create(this.getContext());
            }
            this.tryUpdateBadgeAnchor();
            BadgeDrawable badgeDrawable = this.badgeDrawable;
            if (badgeDrawable != null) {
                return badgeDrawable;
            }
            throw new IllegalStateException("Unable to create badge");
        }

        private boolean hasBadgeDrawable() {
            if (this.badgeDrawable != null) {
                return true;
            }
            return false;
        }

        private void inflateAndAddDefaultIconView() {
            ImageView imageView;
            TabView tabView = this;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                tabView = this.createPreApi18BadgeAnchorRoot();
                this.addView((View)tabView, 0);
            }
            this.iconView = imageView = (ImageView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)tabView, false);
            tabView.addView((View)imageView, 0);
        }

        private void inflateAndAddDefaultTextView() {
            TextView textView;
            TabView tabView = this;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                tabView = this.createPreApi18BadgeAnchorRoot();
                this.addView((View)tabView);
            }
            this.textView = textView = (TextView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)tabView, false);
            tabView.addView((View)textView);
        }

        private void removeBadge() {
            if (this.badgeAnchorView != null) {
                this.tryRemoveBadgeFromAnchor();
            }
            this.badgeDrawable = null;
        }

        private void tryAttachBadgeToAnchor(View view) {
            if (!this.hasBadgeDrawable()) {
                return;
            }
            if (view != null) {
                this.setClipChildren(false);
                this.setClipToPadding(false);
                BadgeUtils.attachBadgeDrawable(this.badgeDrawable, view, this.getCustomParentForBadge(view));
                this.badgeAnchorView = view;
            }
        }

        private void tryRemoveBadgeFromAnchor() {
            if (!this.hasBadgeDrawable()) {
                return;
            }
            if (this.badgeAnchorView != null) {
                this.setClipChildren(true);
                this.setClipToPadding(true);
                BadgeDrawable badgeDrawable = this.badgeDrawable;
                View view = this.badgeAnchorView;
                BadgeUtils.detachBadgeDrawable(badgeDrawable, view, this.getCustomParentForBadge(view));
                this.badgeAnchorView = null;
            }
        }

        private void tryUpdateBadgeAnchor() {
            Tab tab;
            if (!this.hasBadgeDrawable()) {
                return;
            }
            if (this.customView != null) {
                this.tryRemoveBadgeFromAnchor();
                return;
            }
            if (this.iconView != null && (tab = this.tab) != null && tab.getIcon() != null) {
                tab = this.badgeAnchorView;
                ImageView imageView = this.iconView;
                if (tab != imageView) {
                    this.tryRemoveBadgeFromAnchor();
                    this.tryAttachBadgeToAnchor((View)this.iconView);
                    return;
                }
                this.tryUpdateBadgeDrawableBounds((View)imageView);
                return;
            }
            if (this.textView != null && (tab = this.tab) != null && tab.getTabLabelVisibility() == 1) {
                tab = this.badgeAnchorView;
                TextView textView = this.textView;
                if (tab != textView) {
                    this.tryRemoveBadgeFromAnchor();
                    this.tryAttachBadgeToAnchor((View)this.textView);
                    return;
                }
                this.tryUpdateBadgeDrawableBounds((View)textView);
                return;
            }
            this.tryRemoveBadgeFromAnchor();
        }

        private void tryUpdateBadgeDrawableBounds(View view) {
            if (this.hasBadgeDrawable() && view == this.badgeAnchorView) {
                BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, view, this.getCustomParentForBadge(view));
            }
        }

        private void updateBackgroundDrawable(Context context) {
            int n = TabLayout.this.tabBackgroundResId;
            Drawable drawable2 = null;
            if (n != 0) {
                context = AppCompatResources.getDrawable(context, TabLayout.this.tabBackgroundResId);
                this.baseBackgroundDrawable = context;
                if (context != null && context.isStateful()) {
                    this.baseBackgroundDrawable.setState(this.getDrawableState());
                }
            } else {
                this.baseBackgroundDrawable = null;
            }
            context = new GradientDrawable();
            ((GradientDrawable)context).setColor(0);
            if (TabLayout.this.tabRippleColorStateList != null) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(1.0E-5f);
                gradientDrawable.setColor(-1);
                ColorStateList colorStateList = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
                if (Build.VERSION.SDK_INT >= 21) {
                    if (TabLayout.this.unboundedRipple) {
                        context = null;
                    }
                    if (!TabLayout.this.unboundedRipple) {
                        drawable2 = gradientDrawable;
                    }
                    context = new RippleDrawable(colorStateList, (Drawable)context, drawable2);
                } else {
                    drawable2 = DrawableCompat.wrap((Drawable)gradientDrawable);
                    DrawableCompat.setTintList(drawable2, colorStateList);
                    context = new LayerDrawable(new Drawable[]{context, drawable2});
                }
            }
            ViewCompat.setBackground((View)this, (Drawable)context);
            TabLayout.this.invalidate();
        }

        private void updateTextAndIcon(TextView object, ImageView imageView) {
            Tab tab = this.tab;
            Object var8_4 = null;
            tab = tab != null && tab.getIcon() != null ? DrawableCompat.wrap(this.tab.getIcon()).mutate() : null;
            Object object2 = this.tab;
            object2 = object2 != null ? object2.getText() : null;
            if (imageView != null) {
                if (tab != null) {
                    imageView.setImageDrawable((Drawable)tab);
                    imageView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
            }
            boolean bl = TextUtils.isEmpty((CharSequence)object2) ^ true;
            if (object != null) {
                if (bl) {
                    object.setText((CharSequence)object2);
                    if (this.tab.labelVisibilityMode == 1) {
                        object.setVisibility(0);
                    } else {
                        object.setVisibility(8);
                    }
                    this.setVisibility(0);
                } else {
                    object.setVisibility(8);
                    object.setText(null);
                }
            }
            if (imageView != null) {
                int n;
                object = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                int n2 = n = 0;
                if (bl) {
                    n2 = n;
                    if (imageView.getVisibility() == 0) {
                        n2 = (int)ViewUtils.dpToPx(this.getContext(), 8);
                    }
                }
                if (TabLayout.this.inlineLabel) {
                    if (n2 != MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams)object)) {
                        MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)object, n2);
                        object.bottomMargin = 0;
                        imageView.setLayoutParams((ViewGroup.LayoutParams)object);
                        imageView.requestLayout();
                    }
                } else if (n2 != object.bottomMargin) {
                    object.bottomMargin = n2;
                    MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)object, 0);
                    imageView.setLayoutParams((ViewGroup.LayoutParams)object);
                    imageView.requestLayout();
                }
            }
            object = (object = this.tab) != null ? ((Tab)object).contentDesc : null;
            if (bl) {
                object = var8_4;
            }
            TooltipCompat.setTooltipText((View)this, (CharSequence)object);
        }

        protected void drawableStateChanged() {
            super.drawableStateChanged();
            boolean bl = false;
            int[] arrn = this.getDrawableState();
            Drawable drawable2 = this.baseBackgroundDrawable;
            boolean bl2 = bl;
            if (drawable2 != null) {
                bl2 = bl;
                if (drawable2.isStateful()) {
                    bl2 = false | this.baseBackgroundDrawable.setState(arrn);
                }
            }
            if (bl2) {
                this.invalidate();
                TabLayout.this.invalidate();
            }
        }

        public Tab getTab() {
            return this.tab;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
            Object object = this.badgeDrawable;
            if (object != null && object.isVisible()) {
                object = this.getContentDescription();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(object);
                stringBuilder.append(", ");
                stringBuilder.append((Object)this.badgeDrawable.getContentDescription());
                accessibilityNodeInfo.setContentDescription((CharSequence)stringBuilder.toString());
            }
        }

        public void onMeasure(int n, int n2) {
            block9 : {
                int n3;
                int n4;
                float f;
                block10 : {
                    block11 : {
                        n4 = View.MeasureSpec.getSize((int)n);
                        n3 = View.MeasureSpec.getMode((int)n);
                        int n5 = TabLayout.this.getTabMaxWidth();
                        if (n5 > 0 && (n3 == 0 || n4 > n5)) {
                            n = View.MeasureSpec.makeMeasureSpec((int)TabLayout.this.tabMaxWidth, (int)Integer.MIN_VALUE);
                        }
                        super.onMeasure(n, n2);
                        if (this.textView == null) break block9;
                        float f2 = TabLayout.this.tabTextSize;
                        n3 = this.defaultMaxLines;
                        ImageView imageView = this.iconView;
                        if (imageView != null && imageView.getVisibility() == 0) {
                            n4 = 1;
                            f = f2;
                        } else {
                            imageView = this.textView;
                            f = f2;
                            n4 = n3;
                            if (imageView != null) {
                                f = f2;
                                n4 = n3;
                                if (imageView.getLineCount() > 1) {
                                    f = TabLayout.this.tabTextMultiLineSize;
                                    n4 = n3;
                                }
                            }
                        }
                        f2 = this.textView.getTextSize();
                        int n6 = this.textView.getLineCount();
                        n3 = TextViewCompat.getMaxLines(this.textView);
                        if (f == f2 && (n3 < 0 || n4 == n3)) break block9;
                        n3 = n5 = 1;
                        if (TabLayout.this.mode != 1) break block10;
                        n3 = n5;
                        if (f <= f2) break block10;
                        n3 = n5;
                        if (n6 != 1) break block10;
                        imageView = this.textView.getLayout();
                        if (imageView == null) break block11;
                        n3 = n5;
                        if (this.approximateLineWidth((Layout)imageView, 0, f) <= (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight())) break block10;
                    }
                    n3 = 0;
                }
                if (n3 != 0) {
                    this.textView.setTextSize(0, f);
                    this.textView.setMaxLines(n4);
                    super.onMeasure(n, n2);
                }
            }
        }

        public boolean performClick() {
            boolean bl = super.performClick();
            if (this.tab != null) {
                if (!bl) {
                    this.playSoundEffect(0);
                }
                this.tab.select();
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
            if ((textView = this.textView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.iconView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.customView) != null) {
                textView.setSelected(bl);
            }
        }

        void setTab(Tab tab) {
            if (tab != this.tab) {
                this.tab = tab;
                this.update();
            }
        }

        final void update() {
            Tab tab = this.tab;
            Drawable drawable2 = null;
            View view = tab != null ? tab.getCustomView() : null;
            if (view != null) {
                ViewParent viewParent = view.getParent();
                if (viewParent != this) {
                    if (viewParent != null) {
                        ((ViewGroup)viewParent).removeView(view);
                    }
                    this.addView(view);
                }
                this.customView = view;
                viewParent = this.textView;
                if (viewParent != null) {
                    viewParent.setVisibility(8);
                }
                if ((viewParent = this.iconView) != null) {
                    viewParent.setVisibility(8);
                    this.iconView.setImageDrawable(null);
                }
                viewParent = (TextView)view.findViewById(16908308);
                this.customTextView = viewParent;
                if (viewParent != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines((TextView)viewParent);
                }
                this.customIconView = (ImageView)view.findViewById(16908294);
            } else {
                view = this.customView;
                if (view != null) {
                    this.removeView(view);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            if (this.customView == null) {
                if (this.iconView == null) {
                    this.inflateAndAddDefaultIconView();
                }
                view = tab != null && tab.getIcon() != null ? DrawableCompat.wrap(tab.getIcon()).mutate() : drawable2;
                if (view != null) {
                    DrawableCompat.setTintList((Drawable)view, TabLayout.this.tabIconTint);
                    if (TabLayout.this.tabIconTintMode != null) {
                        DrawableCompat.setTintMode((Drawable)view, TabLayout.this.tabIconTintMode);
                    }
                }
                if (this.textView == null) {
                    this.inflateAndAddDefaultTextView();
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
                }
                TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
                if (TabLayout.this.tabTextColors != null) {
                    this.textView.setTextColor(TabLayout.this.tabTextColors);
                }
                this.updateTextAndIcon(this.textView, this.iconView);
                this.tryUpdateBadgeAnchor();
                this.addOnLayoutChangeListener((View)this.iconView);
                this.addOnLayoutChangeListener((View)this.textView);
            } else if (this.customTextView != null || this.customIconView != null) {
                this.updateTextAndIcon(this.customTextView, this.customIconView);
            }
            if (tab != null && !TextUtils.isEmpty((CharSequence)tab.contentDesc)) {
                this.setContentDescription(tab.contentDesc);
            }
            boolean bl = tab != null && tab.isSelected();
            this.setSelected(bl);
        }

        final void updateOrientation() {
            this.setOrientation(TabLayout.this.inlineLabel ^ true);
            if (this.customTextView == null && this.customIconView == null) {
                this.updateTextAndIcon(this.textView, this.iconView);
                return;
            }
            this.updateTextAndIcon(this.customTextView, this.customIconView);
        }

    }

    public static class ViewPagerOnTabSelectedListener
    implements OnTabSelectedListener {
        private final ViewPager viewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void onTabReselected(Tab tab) {
        }

        @Override
        public void onTabSelected(Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {
        }
    }

}

