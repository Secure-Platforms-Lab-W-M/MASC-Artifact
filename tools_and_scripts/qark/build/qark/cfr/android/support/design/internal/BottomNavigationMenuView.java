/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.util.AttributeSet
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.design.internal;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.design.internal.TextScale;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationMenuView
extends ViewGroup
implements MenuView {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
    private final int mActiveItemMaxWidth;
    private BottomNavigationItemView[] mButtons;
    private final int mInactiveItemMaxWidth;
    private final int mInactiveItemMinWidth;
    private int mItemBackgroundRes;
    private final int mItemHeight;
    private ColorStateList mItemIconTint;
    private final Pools.Pool<BottomNavigationItemView> mItemPool = new Pools.SynchronizedPool<BottomNavigationItemView>(5);
    private ColorStateList mItemTextColor;
    private MenuBuilder mMenu;
    private final View.OnClickListener mOnClickListener;
    private BottomNavigationPresenter mPresenter;
    private int mSelectedItemId = 0;
    private int mSelectedItemPosition = 0;
    private final TransitionSet mSet;
    private boolean mShiftingMode = true;
    private int[] mTempChildWidths;

    public BottomNavigationMenuView(Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = this.getResources();
        this.mInactiveItemMaxWidth = context.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
        this.mInactiveItemMinWidth = context.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
        this.mActiveItemMaxWidth = context.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
        this.mItemHeight = context.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
        this.mSet = new AutoTransition();
        this.mSet.setOrdering(0);
        this.mSet.setDuration(115L);
        this.mSet.setInterpolator((TimeInterpolator)new FastOutSlowInInterpolator());
        this.mSet.addTransition(new TextScale());
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View object) {
                object = ((BottomNavigationItemView)object).getItemData();
                if (!BottomNavigationMenuView.this.mMenu.performItemAction((MenuItem)object, BottomNavigationMenuView.this.mPresenter, 0)) {
                    object.setChecked(true);
                    return;
                }
            }
        };
        this.mTempChildWidths = new int[5];
    }

    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView bottomNavigationItemView = this.mItemPool.acquire();
        if (bottomNavigationItemView == null) {
            return new BottomNavigationItemView(this.getContext());
        }
        return bottomNavigationItemView;
    }

    public void buildMenuView() {
        int n;
        this.removeAllViews();
        Object object = this.mButtons;
        if (object != null) {
            int n2 = object.length;
            for (n = 0; n < n2; ++n) {
                BottomNavigationItemView bottomNavigationItemView = object[n];
                this.mItemPool.release(bottomNavigationItemView);
            }
        }
        if (this.mMenu.size() == 0) {
            this.mSelectedItemId = 0;
            this.mSelectedItemPosition = 0;
            this.mButtons = null;
            return;
        }
        this.mButtons = new BottomNavigationItemView[this.mMenu.size()];
        boolean bl = this.mMenu.size() > 3;
        this.mShiftingMode = bl;
        for (n = 0; n < this.mMenu.size(); ++n) {
            this.mPresenter.setUpdateSuspended(true);
            this.mMenu.getItem(n).setCheckable(true);
            this.mPresenter.setUpdateSuspended(false);
            this.mButtons[n] = object = this.getNewItem();
            object.setIconTintList(this.mItemIconTint);
            object.setTextColor(this.mItemTextColor);
            object.setItemBackground(this.mItemBackgroundRes);
            object.setShiftingMode(this.mShiftingMode);
            object.initialize((MenuItemImpl)this.mMenu.getItem(n), 0);
            object.setItemPosition(n);
            object.setOnClickListener(this.mOnClickListener);
            this.addView((View)object);
        }
        this.mSelectedItemPosition = Math.min(this.mMenu.size() - 1, this.mSelectedItemPosition);
        this.mMenu.getItem(this.mSelectedItemPosition).setChecked(true);
    }

    @Nullable
    public ColorStateList getIconTintList() {
        return this.mItemIconTint;
    }

    public int getItemBackgroundRes() {
        return this.mItemBackgroundRes;
    }

    public ColorStateList getItemTextColor() {
        return this.mItemTextColor;
    }

    public int getSelectedItemId() {
        return this.mSelectedItemId;
    }

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.getChildCount();
        n3 -= n;
        n4 -= n2;
        n2 = 0;
        for (n = 0; n < n5; ++n) {
            View view = this.getChildAt(n);
            if (view.getVisibility() == 8) continue;
            if (ViewCompat.getLayoutDirection((View)this) == 1) {
                view.layout(n3 - n2 - view.getMeasuredWidth(), 0, n3 - n2, n4);
            } else {
                view.layout(n2, 0, view.getMeasuredWidth() + n2, n4);
            }
            n2 += view.getMeasuredWidth();
        }
    }

    protected void onMeasure(int n, int n2) {
        int[] arrn;
        int n3;
        n2 = View.MeasureSpec.getSize((int)n);
        int n4 = this.getChildCount();
        int n5 = View.MeasureSpec.makeMeasureSpec((int)this.mItemHeight, (int)1073741824);
        if (this.mShiftingMode) {
            n = n4 - 1;
            int n6 = Math.min(n2 - this.mInactiveItemMinWidth * n, this.mActiveItemMaxWidth);
            int n7 = Math.min((n2 - n6) / n, this.mInactiveItemMaxWidth);
            n2 = n2 - n6 - n7 * n;
            for (n = 0; n < n4; ++n) {
                arrn = this.mTempChildWidths;
                n3 = n == this.mSelectedItemPosition ? n6 : n7;
                arrn[n] = n3;
                if (n2 <= 0) continue;
                arrn = this.mTempChildWidths;
                arrn[n] = arrn[n] + 1;
                --n2;
            }
        } else {
            n = n4 == 0 ? 1 : n4;
            n3 = Math.min(n2 / n, this.mActiveItemMaxWidth);
            n2 -= n3 * n4;
            for (n = 0; n < n4; ++n) {
                arrn = this.mTempChildWidths;
                arrn[n] = n3;
                if (n2 <= 0) continue;
                arrn[n] = arrn[n] + true;
                --n2;
            }
        }
        n2 = 0;
        for (n = 0; n < n4; ++n) {
            arrn = this.getChildAt(n);
            if (arrn.getVisibility() == 8) continue;
            arrn.measure(View.MeasureSpec.makeMeasureSpec((int)this.mTempChildWidths[n], (int)1073741824), n5);
            arrn.getLayoutParams().width = arrn.getMeasuredWidth();
            n2 += arrn.getMeasuredWidth();
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)n2, (int)View.MeasureSpec.makeMeasureSpec((int)n2, (int)1073741824), (int)0), View.resolveSizeAndState((int)this.mItemHeight, (int)n5, (int)0));
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.mItemIconTint = colorStateList;
        BottomNavigationItemView[] arrbottomNavigationItemView = this.mButtons;
        if (arrbottomNavigationItemView == null) {
            return;
        }
        int n = arrbottomNavigationItemView.length;
        for (int i = 0; i < n; ++i) {
            arrbottomNavigationItemView[i].setIconTintList(colorStateList);
        }
    }

    public void setItemBackgroundRes(int n) {
        this.mItemBackgroundRes = n;
        BottomNavigationItemView[] arrbottomNavigationItemView = this.mButtons;
        if (arrbottomNavigationItemView == null) {
            return;
        }
        int n2 = arrbottomNavigationItemView.length;
        for (int i = 0; i < n2; ++i) {
            arrbottomNavigationItemView[i].setItemBackground(n);
        }
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.mItemTextColor = colorStateList;
        BottomNavigationItemView[] arrbottomNavigationItemView = this.mButtons;
        if (arrbottomNavigationItemView == null) {
            return;
        }
        int n = arrbottomNavigationItemView.length;
        for (int i = 0; i < n; ++i) {
            arrbottomNavigationItemView[i].setTextColor(colorStateList);
        }
    }

    public void setPresenter(BottomNavigationPresenter bottomNavigationPresenter) {
        this.mPresenter = bottomNavigationPresenter;
    }

    void tryRestoreSelectedItemId(int n) {
        int n2 = this.mMenu.size();
        for (int i = 0; i < n2; ++i) {
            MenuItem menuItem = this.mMenu.getItem(i);
            if (n != menuItem.getItemId()) continue;
            this.mSelectedItemId = n;
            this.mSelectedItemPosition = i;
            menuItem.setChecked(true);
            return;
        }
    }

    public void updateMenuView() {
        int n;
        int n2 = this.mMenu.size();
        if (n2 != this.mButtons.length) {
            this.buildMenuView();
            return;
        }
        int n3 = this.mSelectedItemId;
        for (n = 0; n < n2; ++n) {
            MenuItem menuItem = this.mMenu.getItem(n);
            if (!menuItem.isChecked()) continue;
            this.mSelectedItemId = menuItem.getItemId();
            this.mSelectedItemPosition = n;
        }
        if (n3 != this.mSelectedItemId) {
            TransitionManager.beginDelayedTransition(this, this.mSet);
        }
        for (n = 0; n < n2; ++n) {
            this.mPresenter.setUpdateSuspended(true);
            this.mButtons[n].initialize((MenuItemImpl)this.mMenu.getItem(n), 0);
            this.mPresenter.setUpdateSuspended(false);
        }
    }

}

