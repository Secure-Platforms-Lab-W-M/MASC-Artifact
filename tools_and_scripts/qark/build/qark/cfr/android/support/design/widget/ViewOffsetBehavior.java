/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 */
package android.support.design.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.ViewOffsetHelper;
import android.util.AttributeSet;
import android.view.View;

class ViewOffsetBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
    private int mTempLeftRightOffset = 0;
    private int mTempTopBottomOffset = 0;
    private ViewOffsetHelper mViewOffsetHelper;

    public ViewOffsetBehavior() {
    }

    public ViewOffsetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getLeftAndRightOffset() {
        ViewOffsetHelper viewOffsetHelper = this.mViewOffsetHelper;
        if (viewOffsetHelper != null) {
            return viewOffsetHelper.getLeftAndRightOffset();
        }
        return 0;
    }

    public int getTopAndBottomOffset() {
        ViewOffsetHelper viewOffsetHelper = this.mViewOffsetHelper;
        if (viewOffsetHelper != null) {
            return viewOffsetHelper.getTopAndBottomOffset();
        }
        return 0;
    }

    protected void layoutChild(CoordinatorLayout coordinatorLayout, V v, int n) {
        coordinatorLayout.onLayoutChild((View)v, n);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int n) {
        this.layoutChild(coordinatorLayout, v, n);
        if (this.mViewOffsetHelper == null) {
            this.mViewOffsetHelper = new ViewOffsetHelper((View)v);
        }
        this.mViewOffsetHelper.onViewLayout();
        n = this.mTempTopBottomOffset;
        if (n != 0) {
            this.mViewOffsetHelper.setTopAndBottomOffset(n);
            this.mTempTopBottomOffset = 0;
        }
        if ((n = this.mTempLeftRightOffset) != 0) {
            this.mViewOffsetHelper.setLeftAndRightOffset(n);
            this.mTempLeftRightOffset = 0;
        }
        return true;
    }

    public boolean setLeftAndRightOffset(int n) {
        ViewOffsetHelper viewOffsetHelper = this.mViewOffsetHelper;
        if (viewOffsetHelper != null) {
            return viewOffsetHelper.setLeftAndRightOffset(n);
        }
        this.mTempLeftRightOffset = n;
        return false;
    }

    public boolean setTopAndBottomOffset(int n) {
        ViewOffsetHelper viewOffsetHelper = this.mViewOffsetHelper;
        if (viewOffsetHelper != null) {
            return viewOffsetHelper.setTopAndBottomOffset(n);
        }
        this.mTempTopBottomOffset = n;
        return false;
    }
}

