/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.ViewPropertyAnimator
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.AbsListView
 *  android.widget.AbsListView$LayoutParams
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.BaseAdapter
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class ScrollingTabContainerView
extends HorizontalScrollView
implements AdapterView.OnItemSelectedListener {
    private static final int FADE_DURATION = 200;
    private static final String TAG = "ScrollingTabContainerView";
    private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;
    protected final VisibilityAnimListener mVisAnimListener;
    protected ViewPropertyAnimator mVisibilityAnim;

    public ScrollingTabContainerView(Context object) {
        super((Context)object);
        this.mVisAnimListener = new VisibilityAnimListener();
        this.setHorizontalScrollBarEnabled(false);
        object = ActionBarPolicy.get((Context)object);
        this.setContentHeight(object.getTabContainerHeight());
        this.mStackedTabMaxWidth = object.getStackedTabMaxWidth();
        this.mTabLayout = this.createTabLayout();
        this.addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
    }

    private Spinner createSpinner() {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(this.getContext(), null, R.attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        return appCompatSpinner;
    }

    private LinearLayoutCompat createTabLayout() {
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this.getContext(), null, R.attr.actionBarTabBarStyle);
        linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
        linearLayoutCompat.setGravity(17);
        linearLayoutCompat.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        return linearLayoutCompat;
    }

    private boolean isCollapsed() {
        Spinner spinner = this.mTabSpinner;
        if (spinner != null && spinner.getParent() == this) {
            return true;
        }
        return false;
    }

    private void performCollapse() {
        Runnable runnable;
        if (this.isCollapsed()) {
            return;
        }
        if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
        }
        this.removeView((View)this.mTabLayout);
        this.addView((View)this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
        if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter());
        }
        if ((runnable = this.mTabSelector) != null) {
            this.removeCallbacks(runnable);
            this.mTabSelector = null;
        }
        this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    }

    private boolean performExpand() {
        if (!this.isCollapsed()) {
            return false;
        }
        this.removeView((View)this.mTabSpinner);
        this.addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void addTab(ActionBar.Tab object, int n, boolean bl) {
        object = this.createTabView((ActionBar.Tab)object, false);
        this.mTabLayout.addView((View)object, n, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            object.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
            return;
        }
    }

    public void addTab(ActionBar.Tab object, boolean bl) {
        object = this.createTabView((ActionBar.Tab)object, false);
        this.mTabLayout.addView((View)object, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            object.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
            return;
        }
    }

    public void animateToTab(int n) {
        final View view = this.mTabLayout.getChildAt(n);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.removeCallbacks(runnable);
        }
        this.mTabSelector = new Runnable(){

            @Override
            public void run() {
                int n = view.getLeft();
                int n2 = (ScrollingTabContainerView.this.getWidth() - view.getWidth()) / 2;
                ScrollingTabContainerView.this.smoothScrollTo(n - n2, 0);
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        };
        this.post(this.mTabSelector);
    }

    public void animateToVisibility(int n) {
        ViewPropertyAnimator viewPropertyAnimator = this.mVisibilityAnim;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            viewPropertyAnimator = this.animate().alpha(1.0f);
            viewPropertyAnimator.setDuration(200L);
            viewPropertyAnimator.setInterpolator((TimeInterpolator)sAlphaInterpolator);
            viewPropertyAnimator.setListener((Animator.AnimatorListener)this.mVisAnimListener.withFinalVisibility(viewPropertyAnimator, n));
            viewPropertyAnimator.start();
            return;
        }
        viewPropertyAnimator = this.animate().alpha(0.0f);
        viewPropertyAnimator.setDuration(200L);
        viewPropertyAnimator.setInterpolator((TimeInterpolator)sAlphaInterpolator);
        viewPropertyAnimator.setListener((Animator.AnimatorListener)this.mVisAnimListener.withFinalVisibility(viewPropertyAnimator, n));
        viewPropertyAnimator.start();
    }

    TabView createTabView(ActionBar.Tab object, boolean bl) {
        object = new TabView(this.getContext(), (ActionBar.Tab)object, bl);
        if (bl) {
            object.setBackgroundDrawable(null);
            object.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, this.mContentHeight));
            return object;
        }
        object.setFocusable(true);
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new TabClickListener();
        }
        object.setOnClickListener((View.OnClickListener)this.mTabClickListener);
        return object;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.post(runnable);
            return;
        }
    }

    protected void onConfigurationChanged(Configuration object) {
        super.onConfigurationChanged((Configuration)object);
        object = ActionBarPolicy.get(this.getContext());
        this.setContentHeight(object.getTabContainerHeight());
        this.mStackedTabMaxWidth = object.getStackedTabMaxWidth();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.removeCallbacks(runnable);
            return;
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {
        ((TabView)view).getTab().select();
    }

    public void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        n2 = 1;
        boolean bl = n3 == 1073741824;
        this.setFillViewport(bl);
        int n4 = this.mTabLayout.getChildCount();
        if (n4 > 1 && (n3 == 1073741824 || n3 == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = n4 > 2 ? (int)((float)View.MeasureSpec.getSize((int)n) * 0.4f) : View.MeasureSpec.getSize((int)n) / 2;
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        } else {
            this.mMaxTabWidth = -1;
        }
        n3 = View.MeasureSpec.makeMeasureSpec((int)this.mContentHeight, (int)1073741824);
        if (bl || !this.mAllowCollapse) {
            n2 = 0;
        }
        if (n2 != 0) {
            this.mTabLayout.measure(0, n3);
            if (this.mTabLayout.getMeasuredWidth() > View.MeasureSpec.getSize((int)n)) {
                this.performCollapse();
            } else {
                this.performExpand();
            }
        } else {
            this.performExpand();
        }
        n2 = this.getMeasuredWidth();
        super.onMeasure(n, n3);
        n = this.getMeasuredWidth();
        if (bl && n2 != n) {
            this.setTabSelected(this.mSelectedTabIndex);
            return;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
            return;
        }
    }

    public void removeTabAt(int n) {
        this.mTabLayout.removeViewAt(n);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
            return;
        }
    }

    public void setAllowCollapse(boolean bl) {
        this.mAllowCollapse = bl;
    }

    public void setContentHeight(int n) {
        this.mContentHeight = n;
        this.requestLayout();
    }

    public void setTabSelected(int n) {
        Spinner spinner;
        this.mSelectedTabIndex = n;
        int n2 = this.mTabLayout.getChildCount();
        for (int i = 0; i < n2; ++i) {
            spinner = this.mTabLayout.getChildAt(i);
            boolean bl = i == n;
            spinner.setSelected(bl);
            if (!bl) continue;
            this.animateToTab(n);
        }
        spinner = this.mTabSpinner;
        if (spinner != null && n >= 0) {
            spinner.setSelection(n);
            return;
        }
    }

    public void updateTab(int n) {
        ((TabView)this.mTabLayout.getChildAt(n)).update();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
            return;
        }
    }

    private class TabAdapter
    extends BaseAdapter {
        TabAdapter() {
        }

        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }

        public Object getItem(int n) {
            return ((TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(n)).getTab();
        }

        public long getItemId(int n) {
            return n;
        }

        public View getView(int n, View view, ViewGroup viewGroup) {
            if (view == null) {
                return ScrollingTabContainerView.this.createTabView((ActionBar.Tab)this.getItem(n), true);
            }
            ((TabView)view).bindTab((ActionBar.Tab)this.getItem(n));
            return view;
        }
    }

    private class TabClickListener
    implements View.OnClickListener {
        TabClickListener() {
        }

        public void onClick(View view) {
            ((TabView)view).getTab().select();
            int n = ScrollingTabContainerView.this.mTabLayout.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view2 = ScrollingTabContainerView.this.mTabLayout.getChildAt(i);
                boolean bl = view2 == view;
                view2.setSelected(bl);
            }
        }
    }

    private class TabView
    extends LinearLayoutCompat {
        private final int[] BG_ATTRS;
        private View mCustomView;
        private ImageView mIconView;
        private ActionBar.Tab mTab;
        private TextView mTextView;

        public TabView(Context context, ActionBar.Tab tab, boolean bl) {
            super(context, null, R.attr.actionBarTabStyle);
            this.BG_ATTRS = new int[]{16842964};
            this.mTab = tab;
            ScrollingTabContainerView.this = TintTypedArray.obtainStyledAttributes(context, null, this.BG_ATTRS, R.attr.actionBarTabStyle, 0);
            if (ScrollingTabContainerView.this.hasValue(0)) {
                this.setBackgroundDrawable(ScrollingTabContainerView.this.getDrawable(0));
            }
            ScrollingTabContainerView.this.recycle();
            if (bl) {
                this.setGravity(8388627);
            }
            this.update();
        }

        public void bindTab(ActionBar.Tab tab) {
            this.mTab = tab;
            this.update();
        }

        public ActionBar.Tab getTab() {
            return this.mTab;
        }

        @Override
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        @Override
        public void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            if (ScrollingTabContainerView.this.mMaxTabWidth > 0 && this.getMeasuredWidth() > ScrollingTabContainerView.this.mMaxTabWidth) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)ScrollingTabContainerView.this.mMaxTabWidth, (int)1073741824), n2);
                return;
            }
        }

        public void setSelected(boolean bl) {
            boolean bl2 = this.isSelected() != bl;
            super.setSelected(bl);
            if (bl2 && bl) {
                this.sendAccessibilityEvent(4);
                return;
            }
        }

        public void update() {
            Object object;
            ActionBar.Tab tab = this.mTab;
            Object object2 = tab.getCustomView();
            Object object3 = null;
            if (object2 != null) {
                object3 = object2.getParent();
                if (object3 != this) {
                    if (object3 != null) {
                        ((ViewGroup)object3).removeView((View)object2);
                    }
                    this.addView((View)object2);
                }
                this.mCustomView = object2;
                object3 = this.mTextView;
                if (object3 != null) {
                    object3.setVisibility(8);
                }
                if ((object3 = this.mIconView) != null) {
                    object3.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                return;
            }
            object2 = this.mCustomView;
            if (object2 != null) {
                this.removeView((View)object2);
                this.mCustomView = null;
            }
            Object object4 = tab.getIcon();
            object2 = tab.getText();
            if (object4 != null) {
                if (this.mIconView == null) {
                    object = new AppCompatImageView(this.getContext());
                    LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    object.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                    this.addView((View)object, 0);
                    this.mIconView = object;
                }
                this.mIconView.setImageDrawable((Drawable)object4);
                this.mIconView.setVisibility(0);
            } else {
                object4 = this.mIconView;
                if (object4 != null) {
                    object4.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
            }
            boolean bl = TextUtils.isEmpty((CharSequence)object2) ^ true;
            if (bl) {
                if (this.mTextView == null) {
                    object4 = new AppCompatTextView(this.getContext(), null, R.attr.actionBarTabTextStyle);
                    object4.setEllipsize(TextUtils.TruncateAt.END);
                    object = new LinearLayoutCompat.LayoutParams(-2, -2);
                    object.gravity = 16;
                    object4.setLayoutParams((ViewGroup.LayoutParams)object);
                    this.addView((View)object4);
                    this.mTextView = object4;
                }
                this.mTextView.setText((CharSequence)object2);
                this.mTextView.setVisibility(0);
            } else {
                object2 = this.mTextView;
                if (object2 != null) {
                    object2.setVisibility(8);
                    this.mTextView.setText(null);
                }
            }
            object2 = this.mIconView;
            if (object2 != null) {
                object2.setContentDescription(tab.getContentDescription());
            }
            if (!bl) {
                object3 = tab.getContentDescription();
            }
            TooltipCompat.setTooltipText((View)this, (CharSequence)object3);
        }
    }

    protected class VisibilityAnimListener
    extends AnimatorListenerAdapter {
        private boolean mCanceled;
        private int mFinalVisibility;

        protected VisibilityAnimListener() {
            this.mCanceled = false;
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator object) {
            if (this.mCanceled) {
                return;
            }
            object = ScrollingTabContainerView.this;
            object.mVisibilityAnim = null;
            object.setVisibility(this.mFinalVisibility);
        }

        public void onAnimationStart(Animator animator2) {
            ScrollingTabContainerView.this.setVisibility(0);
            this.mCanceled = false;
        }

        public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimator viewPropertyAnimator, int n) {
            this.mFinalVisibility = n;
            ScrollingTabContainerView.this.mVisibilityAnim = viewPropertyAnimator;
            return this;
        }
    }

}

