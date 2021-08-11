/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout
extends LinearLayoutCompat {
    public AlertDialogLayout(@Nullable Context context) {
        super(context);
    }

    public AlertDialogLayout(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void forceUniformWidth(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824);
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (view.getVisibility() == 8) continue;
            LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
            if (layoutParams.width != -1) continue;
            int n4 = layoutParams.height;
            layoutParams.height = view.getMeasuredHeight();
            this.measureChildWithMargins(view, n3, 0, n2, 0);
            layoutParams.height = n4;
        }
    }

    private static int resolveMinimumHeight(View view) {
        int n = ViewCompat.getMinimumHeight(view);
        if (n > 0) {
            return n;
        }
        if (view instanceof ViewGroup) {
            if ((view = (ViewGroup)view).getChildCount() == 1) {
                return AlertDialogLayout.resolveMinimumHeight(view.getChildAt(0));
            }
            return 0;
        }
        return 0;
    }

    private void setChildFrame(View view, int n, int n2, int n3, int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }

    private boolean tryOnMeasure(int n, int n2) {
        int n3;
        int n4;
        int n5;
        View view;
        View view2 = null;
        View view3 = null;
        View view4 = null;
        int n6 = this.getChildCount();
        for (n4 = 0; n4 < n6; ++n4) {
            view = this.getChildAt(n4);
            if (view.getVisibility() == 8) continue;
            n3 = view.getId();
            if (n3 == R.id.topPanel) {
                view2 = view;
                continue;
            }
            if (n3 == R.id.buttonPanel) {
                view3 = view;
                continue;
            }
            if (n3 != R.id.contentPanel && n3 != R.id.customPanel) {
                return false;
            }
            if (view4 != null) {
                return false;
            }
            view4 = view;
        }
        int n7 = View.MeasureSpec.getMode((int)n2);
        int n8 = View.MeasureSpec.getSize((int)n2);
        int n9 = View.MeasureSpec.getMode((int)n);
        n3 = 0;
        int n10 = this.getPaddingTop() + this.getPaddingBottom();
        if (view2 != null) {
            view2.measure(n, 0);
            n10 += view2.getMeasuredHeight();
            n3 = View.combineMeasuredStates((int)0, (int)view2.getMeasuredState());
        }
        n4 = 0;
        int n11 = 0;
        if (view3 != null) {
            view3.measure(n, 0);
            n4 = AlertDialogLayout.resolveMinimumHeight(view3);
            n11 = view3.getMeasuredHeight() - n4;
            n10 += n4;
            n3 = View.combineMeasuredStates((int)n3, (int)view3.getMeasuredState());
        }
        int n12 = 0;
        if (view4 != null) {
            n5 = n7 == 0 ? 0 : View.MeasureSpec.makeMeasureSpec((int)Math.max(0, n8 - n10), (int)n7);
            view4.measure(n, n5);
            n12 = view4.getMeasuredHeight();
            n10 += n12;
            n3 = View.combineMeasuredStates((int)n3, (int)view4.getMeasuredState());
        }
        n8 -= n10;
        if (view3 != null) {
            n5 = Math.min(n8, n11);
            if (n5 > 0) {
                n11 = n8 - n5;
                n5 = n4 + n5;
            } else {
                n5 = n4;
                n11 = n8;
            }
            view3.measure(n, View.MeasureSpec.makeMeasureSpec((int)n5, (int)1073741824));
            n10 = n10 - n4 + view3.getMeasuredHeight();
            n3 = View.combineMeasuredStates((int)n3, (int)view3.getMeasuredState());
            n4 = n11;
        } else {
            n4 = n8;
        }
        if (view4 != null && n4 > 0) {
            view4.measure(n, View.MeasureSpec.makeMeasureSpec((int)(n12 + n4), (int)n7));
            n10 = n10 - n12 + view4.getMeasuredHeight();
            n3 = View.combineMeasuredStates((int)n3, (int)view4.getMeasuredState());
            n4 -= n4;
        }
        n11 = 0;
        for (n4 = 0; n4 < n6; ++n4) {
            view = this.getChildAt(n4);
            if (view.getVisibility() == 8) continue;
            n11 = Math.max(n11, view.getMeasuredWidth());
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)(n11 + (this.getPaddingLeft() + this.getPaddingRight())), (int)n, (int)n3), View.resolveSizeAndState((int)n10, (int)n2, (int)0));
        if (n9 != 1073741824) {
            this.forceUniformWidth(n6, n2);
        }
        return true;
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.getPaddingLeft();
        int n6 = n3 - n;
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        int n9 = this.getChildCount();
        int n10 = this.getGravity();
        n3 = n10 & 112;
        n = n3 != 16 ? (n3 != 80 ? this.getPaddingTop() : this.getPaddingTop() + n4 - n2 - n) : this.getPaddingTop() + (n4 - n2 - n) / 2;
        Object object = this.getDividerDrawable();
        n3 = object == null ? 0 : object.getIntrinsicHeight();
        n4 = 0;
        do {
            object = this;
            if (n4 >= n9) break;
            View view = object.getChildAt(n4);
            if (view != null && view.getVisibility() != 8) {
                int n11 = view.getMeasuredWidth();
                int n12 = view.getMeasuredHeight();
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
                n2 = layoutParams.gravity;
                if (n2 < 0) {
                    n2 = n10 & 8388615;
                }
                n2 = (n2 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this)) & 7) != 1 ? (n2 != 5 ? layoutParams.leftMargin + n5 : n6 - n7 - n11 - layoutParams.rightMargin) : (n6 - n5 - n8 - n11) / 2 + n5 + layoutParams.leftMargin - layoutParams.rightMargin;
                if (object.hasDividerBeforeChildAt(n4)) {
                    n += n3;
                }
                this.setChildFrame(view, n2, n += layoutParams.topMargin, n11, n12);
                n += n12 + layoutParams.bottomMargin;
            }
            ++n4;
        } while (true);
    }

    @Override
    protected void onMeasure(int n, int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
            return;
        }
    }
}

