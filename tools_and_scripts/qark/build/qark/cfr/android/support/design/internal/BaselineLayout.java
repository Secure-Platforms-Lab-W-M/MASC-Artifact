/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout
extends ViewGroup {
    private int mBaseline = -1;

    public BaselineLayout(Context context) {
        super(context, null, 0);
    }

    public BaselineLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public BaselineLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public int getBaseline() {
        return this.mBaseline;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.getChildCount();
        int n6 = this.getPaddingLeft();
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingTop();
        for (n2 = 0; n2 < n5; ++n2) {
            View view = this.getChildAt(n2);
            if (view.getVisibility() == 8) continue;
            int n9 = view.getMeasuredWidth();
            int n10 = view.getMeasuredHeight();
            int n11 = (n3 - n - n7 - n6 - n9) / 2 + n6;
            n4 = this.mBaseline != -1 && view.getBaseline() != -1 ? this.mBaseline + n8 - view.getBaseline() : n8;
            view.layout(n11, n4, n11 + n9, n4 + n10);
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3 = this.getChildCount();
        int n4 = 0;
        int n5 = 0;
        int n6 = -1;
        int n7 = -1;
        int n8 = 0;
        for (int i = 0; i < n3; ++i) {
            View view = this.getChildAt(i);
            if (view.getVisibility() == 8) continue;
            this.measureChild(view, n, n2);
            int n9 = view.getBaseline();
            if (n9 != -1) {
                n6 = Math.max(n6, n9);
                n7 = Math.max(n7, view.getMeasuredHeight() - n9);
            }
            n4 = Math.max(n4, view.getMeasuredWidth());
            n5 = Math.max(n5, view.getMeasuredHeight());
            n8 = View.combineMeasuredStates((int)n8, (int)view.getMeasuredState());
        }
        if (n6 != -1) {
            n5 = Math.max(n5, n6 + Math.max(n7, this.getPaddingBottom()));
            this.mBaseline = n6;
        }
        n5 = Math.max(n5, this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(View.resolveSizeAndState((int)Math.max(n4, this.getSuggestedMinimumWidth()), (int)n, (int)n8), View.resolveSizeAndState((int)n5, (int)n2, (int)(n8 << 16)));
    }
}

