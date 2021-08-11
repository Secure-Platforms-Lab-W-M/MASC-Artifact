// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

class ViewOffsetHelper
{
    private int mLayoutLeft;
    private int mLayoutTop;
    private int mOffsetLeft;
    private int mOffsetTop;
    private final View mView;
    
    public ViewOffsetHelper(final View mView) {
        this.mView = mView;
    }
    
    private void updateOffsets() {
        final View mView = this.mView;
        ViewCompat.offsetTopAndBottom(mView, this.mOffsetTop - (mView.getTop() - this.mLayoutTop));
        final View mView2 = this.mView;
        ViewCompat.offsetLeftAndRight(mView2, this.mOffsetLeft - (mView2.getLeft() - this.mLayoutLeft));
    }
    
    public int getLayoutLeft() {
        return this.mLayoutLeft;
    }
    
    public int getLayoutTop() {
        return this.mLayoutTop;
    }
    
    public int getLeftAndRightOffset() {
        return this.mOffsetLeft;
    }
    
    public int getTopAndBottomOffset() {
        return this.mOffsetTop;
    }
    
    public void onViewLayout() {
        this.mLayoutTop = this.mView.getTop();
        this.mLayoutLeft = this.mView.getLeft();
        this.updateOffsets();
    }
    
    public boolean setLeftAndRightOffset(final int mOffsetLeft) {
        if (this.mOffsetLeft != mOffsetLeft) {
            this.mOffsetLeft = mOffsetLeft;
            this.updateOffsets();
            return true;
        }
        return false;
    }
    
    public boolean setTopAndBottomOffset(final int mOffsetTop) {
        if (this.mOffsetTop != mOffsetTop) {
            this.mOffsetTop = mOffsetTop;
            this.updateOffsets();
            return true;
        }
        return false;
    }
}
