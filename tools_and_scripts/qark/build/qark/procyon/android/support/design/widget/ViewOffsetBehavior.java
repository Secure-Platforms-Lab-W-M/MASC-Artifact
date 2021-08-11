// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

class ViewOffsetBehavior<V extends View> extends Behavior<V>
{
    private int mTempLeftRightOffset;
    private int mTempTopBottomOffset;
    private ViewOffsetHelper mViewOffsetHelper;
    
    public ViewOffsetBehavior() {
        this.mTempTopBottomOffset = 0;
        this.mTempLeftRightOffset = 0;
    }
    
    public ViewOffsetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTempTopBottomOffset = 0;
        this.mTempLeftRightOffset = 0;
    }
    
    public int getLeftAndRightOffset() {
        final ViewOffsetHelper mViewOffsetHelper = this.mViewOffsetHelper;
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.getLeftAndRightOffset();
        }
        return 0;
    }
    
    public int getTopAndBottomOffset() {
        final ViewOffsetHelper mViewOffsetHelper = this.mViewOffsetHelper;
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.getTopAndBottomOffset();
        }
        return 0;
    }
    
    protected void layoutChild(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        coordinatorLayout.onLayoutChild(v, n);
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V v, int n) {
        this.layoutChild(coordinatorLayout, v, n);
        if (this.mViewOffsetHelper == null) {
            this.mViewOffsetHelper = new ViewOffsetHelper(v);
        }
        this.mViewOffsetHelper.onViewLayout();
        n = this.mTempTopBottomOffset;
        if (n != 0) {
            this.mViewOffsetHelper.setTopAndBottomOffset(n);
            this.mTempTopBottomOffset = 0;
        }
        n = this.mTempLeftRightOffset;
        if (n != 0) {
            this.mViewOffsetHelper.setLeftAndRightOffset(n);
            this.mTempLeftRightOffset = 0;
        }
        return true;
    }
    
    public boolean setLeftAndRightOffset(final int n) {
        final ViewOffsetHelper mViewOffsetHelper = this.mViewOffsetHelper;
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.setLeftAndRightOffset(n);
        }
        this.mTempLeftRightOffset = n;
        return false;
    }
    
    public boolean setTopAndBottomOffset(final int n) {
        final ViewOffsetHelper mViewOffsetHelper = this.mViewOffsetHelper;
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.setTopAndBottomOffset(n);
        }
        this.mTempTopBottomOffset = n;
        return false;
    }
}
