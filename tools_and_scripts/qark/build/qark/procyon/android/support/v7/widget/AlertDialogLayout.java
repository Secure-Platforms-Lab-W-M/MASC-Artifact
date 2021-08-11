// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v7.appcompat.R;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.support.annotation.Nullable;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AlertDialogLayout extends LinearLayoutCompat
{
    public AlertDialogLayout(@Nullable final Context context) {
        super(context);
    }
    
    public AlertDialogLayout(@Nullable final Context context, @Nullable final AttributeSet set) {
        super(context, set);
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = child.getMeasuredHeight();
                    this.measureChildWithMargins(child, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private static int resolveMinimumHeight(final View view) {
        final int minimumHeight = ViewCompat.getMinimumHeight(view);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (!(view instanceof ViewGroup)) {
            return 0;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        if (viewGroup.getChildCount() == 1) {
            return resolveMinimumHeight(viewGroup.getChildAt(0));
        }
        return 0;
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    private boolean tryOnMeasure(final int n, final int n2) {
        View view = null;
        View view2 = null;
        View view3 = null;
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final int id = child.getId();
                if (id == R.id.topPanel) {
                    view = child;
                }
                else if (id == R.id.buttonPanel) {
                    view2 = child;
                }
                else {
                    if (id != R.id.contentPanel && id != R.id.customPanel) {
                        return false;
                    }
                    if (view3 != null) {
                        return false;
                    }
                    view3 = child;
                }
            }
        }
        final int mode = View$MeasureSpec.getMode(n2);
        final int size = View$MeasureSpec.getSize(n2);
        final int mode2 = View$MeasureSpec.getMode(n);
        int n3 = 0;
        int n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (view != null) {
            view.measure(n, 0);
            n4 += view.getMeasuredHeight();
            n3 = View.combineMeasuredStates(0, view.getMeasuredState());
        }
        int resolveMinimumHeight = 0;
        int n5 = 0;
        if (view2 != null) {
            view2.measure(n, 0);
            resolveMinimumHeight = resolveMinimumHeight(view2);
            n5 = view2.getMeasuredHeight() - resolveMinimumHeight;
            n4 += resolveMinimumHeight;
            n3 = View.combineMeasuredStates(n3, view2.getMeasuredState());
        }
        int measuredHeight = 0;
        if (view3 != null) {
            int measureSpec;
            if (mode == 0) {
                measureSpec = 0;
            }
            else {
                measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max(0, size - n4), mode);
            }
            view3.measure(n, measureSpec);
            measuredHeight = view3.getMeasuredHeight();
            n4 += measuredHeight;
            n3 = View.combineMeasuredStates(n3, view3.getMeasuredState());
        }
        final int n6 = size - n4;
        int n9;
        if (view2 != null) {
            final int min = Math.min(n6, n5);
            int n7;
            int n8;
            if (min > 0) {
                n7 = n6 - min;
                n8 = resolveMinimumHeight + min;
            }
            else {
                n8 = resolveMinimumHeight;
                n7 = n6;
            }
            view2.measure(n, View$MeasureSpec.makeMeasureSpec(n8, 1073741824));
            n4 = n4 - resolveMinimumHeight + view2.getMeasuredHeight();
            n3 = View.combineMeasuredStates(n3, view2.getMeasuredState());
            n9 = n7;
        }
        else {
            n9 = n6;
        }
        if (view3 != null && n9 > 0) {
            view3.measure(n, View$MeasureSpec.makeMeasureSpec(measuredHeight + n9, mode));
            n4 = n4 - measuredHeight + view3.getMeasuredHeight();
            n3 = View.combineMeasuredStates(n3, view3.getMeasuredState());
        }
        int max = 0;
        for (int j = 0; j < childCount; ++j) {
            final View child2 = this.getChildAt(j);
            if (child2.getVisibility() != 8) {
                max = Math.max(max, child2.getMeasuredWidth());
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(max + (this.getPaddingLeft() + this.getPaddingRight()), n, n3), View.resolveSizeAndState(n4, n2, 0));
        if (mode2 != 1073741824) {
            this.forceUniformWidth(childCount, n2);
        }
        return true;
    }
    
    @Override
    protected void onLayout(final boolean b, int n, int gravity, int intrinsicHeight, int i) {
        final int paddingLeft = this.getPaddingLeft();
        final int n2 = intrinsicHeight - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        final int childCount = this.getChildCount();
        final int gravity2 = this.getGravity();
        intrinsicHeight = (gravity2 & 0x70);
        if (intrinsicHeight != 16) {
            if (intrinsicHeight != 80) {
                n = this.getPaddingTop();
            }
            else {
                n = this.getPaddingTop() + i - gravity - n;
            }
        }
        else {
            n = this.getPaddingTop() + (i - gravity - n) / 2;
        }
        final Drawable dividerDrawable = this.getDividerDrawable();
        if (dividerDrawable == null) {
            intrinsicHeight = 0;
        }
        else {
            intrinsicHeight = dividerDrawable.getIntrinsicHeight();
        }
        View child;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                layoutParams = (LayoutParams)child.getLayoutParams();
                gravity = layoutParams.gravity;
                if (gravity < 0) {
                    gravity = (gravity2 & 0x800007);
                }
                gravity = (GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                if (gravity != 1) {
                    if (gravity != 5) {
                        gravity = layoutParams.leftMargin + paddingLeft;
                    }
                    else {
                        gravity = n2 - paddingRight - measuredWidth - layoutParams.rightMargin;
                    }
                }
                else {
                    gravity = (n2 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                }
                if (this.hasDividerBeforeChildAt(i)) {
                    n += intrinsicHeight;
                }
                n += layoutParams.topMargin;
                this.setChildFrame(child, gravity, n, measuredWidth, measuredHeight);
                n += measuredHeight + layoutParams.bottomMargin;
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
        }
    }
}
