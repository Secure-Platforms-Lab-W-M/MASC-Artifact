// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.internal;

import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup
{
    private int mBaseline;
    
    public BaselineLayout(final Context context) {
        super(context, (AttributeSet)null, 0);
        this.mBaseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.mBaseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mBaseline = -1;
    }
    
    public int getBaseline() {
        return this.mBaseline;
    }
    
    protected void onLayout(final boolean b, final int n, int i, final int n2, int n3) {
        final int childCount = this.getChildCount();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        View child;
        int measuredWidth;
        int measuredHeight;
        int n4;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                n4 = (n2 - n - paddingRight - paddingLeft - measuredWidth) / 2 + paddingLeft;
                if (this.mBaseline != -1 && child.getBaseline() != -1) {
                    n3 = this.mBaseline + paddingTop - child.getBaseline();
                }
                else {
                    n3 = paddingTop;
                }
                child.layout(n4, n3, n4 + measuredWidth, n3 + measuredHeight);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int childCount = this.getChildCount();
        int max = 0;
        int n3 = 0;
        int max2 = -1;
        int max3 = -1;
        int combineMeasuredStates = 0;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                this.measureChild(child, n, n2);
                final int baseline = child.getBaseline();
                if (baseline != -1) {
                    max2 = Math.max(max2, baseline);
                    max3 = Math.max(max3, child.getMeasuredHeight() - baseline);
                }
                max = Math.max(max, child.getMeasuredWidth());
                n3 = Math.max(n3, child.getMeasuredHeight());
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, child.getMeasuredState());
            }
        }
        if (max2 != -1) {
            n3 = Math.max(n3, max2 + Math.max(max3, this.getPaddingBottom()));
            this.mBaseline = max2;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(max, this.getSuggestedMinimumWidth()), n, combineMeasuredStates), View.resolveSizeAndState(Math.max(n3, this.getSuggestedMinimumHeight()), n2, combineMeasuredStates << 16));
    }
}
