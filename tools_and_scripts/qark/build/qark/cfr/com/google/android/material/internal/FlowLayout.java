/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  com.google.android.material.R
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class FlowLayout
extends ViewGroup {
    private int itemSpacing;
    private int lineSpacing;
    private boolean singleLine = false;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.loadFromAttributes(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.loadFromAttributes(context, attributeSet);
    }

    private static int getMeasuredDimension(int n, int n2, int n3) {
        if (n2 != Integer.MIN_VALUE) {
            if (n2 != 1073741824) {
                return n3;
            }
            return n;
        }
        return Math.min(n3, n);
    }

    private void loadFromAttributes(Context context, AttributeSet attributeSet) {
        context = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.FlowLayout, 0, 0);
        this.lineSpacing = context.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
        this.itemSpacing = context.getDimensionPixelSize(R.styleable.FlowLayout_itemSpacing, 0);
        context.recycle();
    }

    protected int getItemSpacing() {
        return this.itemSpacing;
    }

    protected int getLineSpacing() {
        return this.lineSpacing;
    }

    public boolean isSingleLine() {
        return this.singleLine;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5;
        if (this.getChildCount() == 0) {
            return;
        }
        n2 = ViewCompat.getLayoutDirection((View)this);
        boolean bl2 = true;
        if (n2 != 1) {
            bl2 = false;
        }
        n2 = bl2 ? this.getPaddingRight() : this.getPaddingLeft();
        int n6 = bl2 ? this.getPaddingLeft() : this.getPaddingRight();
        n4 = n2;
        int n7 = n5 = this.getPaddingTop();
        int n8 = n3 - n - n6;
        n = n5;
        n3 = n4;
        for (n6 = 0; n6 < this.getChildCount(); ++n6) {
            View view = this.getChildAt(n6);
            if (view.getVisibility() == 8) continue;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            n5 = 0;
            int n9 = 0;
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                layoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
                n5 = MarginLayoutParamsCompat.getMarginStart((ViewGroup.MarginLayoutParams)layoutParams);
                n9 = MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams)layoutParams);
            }
            int n10 = view.getMeasuredWidth();
            int n11 = n3;
            n4 = n;
            if (!this.singleLine) {
                n11 = n3;
                n4 = n;
                if (n3 + n5 + n10 > n8) {
                    n11 = n2;
                    n4 = n7 + this.lineSpacing;
                }
            }
            n = n11 + n5 + view.getMeasuredWidth();
            n7 = view.getMeasuredHeight() + n4;
            if (bl2) {
                view.layout(n8 - n, n4, n8 - n11 - n5, n7);
            } else {
                view.layout(n11 + n5, n4, n, n7);
            }
            n3 = n11 + (n5 + n9 + view.getMeasuredWidth() + this.itemSpacing);
            n = n4;
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        int n4 = View.MeasureSpec.getSize((int)n);
        int n5 = View.MeasureSpec.getMode((int)n);
        int n6 = View.MeasureSpec.getSize((int)n2);
        int n7 = View.MeasureSpec.getMode((int)n2);
        int n8 = n5 != Integer.MIN_VALUE && n5 != 1073741824 ? Integer.MAX_VALUE : n4;
        int n9 = this.getPaddingLeft();
        int n10 = n3 = this.getPaddingTop();
        int n11 = 0;
        int n12 = this.getPaddingRight();
        int n13 = n8;
        for (int i = 0; i < this.getChildCount(); ++i) {
            int n14;
            View view = this.getChildAt(i);
            if (view.getVisibility() == 8) continue;
            this.measureChild(view, n, n2);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int n15 = 0;
            int n16 = 0;
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                layoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
                n15 = 0 + layoutParams.leftMargin;
                n16 = 0 + layoutParams.rightMargin;
            }
            if (n9 + n15 + view.getMeasuredWidth() > n8 - n12 && !this.isSingleLine()) {
                n14 = this.getPaddingLeft();
                n3 = this.lineSpacing + n10;
            } else {
                n14 = n9;
            }
            int n17 = n14 + n15 + view.getMeasuredWidth();
            n10 = view.getMeasuredHeight() + n3;
            n9 = n11;
            if (n17 > n11) {
                n9 = n17;
            }
            n15 = n14 + (n15 + n16 + view.getMeasuredWidth() + this.itemSpacing);
            if (i == this.getChildCount() - 1) {
                n11 = n9 + n16;
                n9 = n15;
                continue;
            }
            n11 = n9;
            n9 = n15;
        }
        n = this.getPaddingRight();
        n2 = this.getPaddingBottom();
        this.setMeasuredDimension(FlowLayout.getMeasuredDimension(n4, n5, n11 + n), FlowLayout.getMeasuredDimension(n6, n7, n10 + n2));
    }

    protected void setItemSpacing(int n) {
        this.itemSpacing = n;
    }

    protected void setLineSpacing(int n) {
        this.lineSpacing = n;
    }

    public void setSingleLine(boolean bl) {
        this.singleLine = bl;
    }
}

