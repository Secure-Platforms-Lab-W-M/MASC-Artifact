/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OrientationHelper {
    public static final int HORIZONTAL = 0;
    private static final int INVALID_SIZE = Integer.MIN_VALUE;
    public static final int VERTICAL = 1;
    private int mLastTotalSpace = Integer.MIN_VALUE;
    protected final RecyclerView.LayoutManager mLayoutManager;
    final Rect mTmpRect = new Rect();

    private OrientationHelper(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public static OrientationHelper createHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        return new OrientationHelper(layoutManager){

            @Override
            public int getDecoratedEnd(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedRight(view) + layoutParams.rightMargin;
            }

            @Override
            public int getDecoratedMeasurement(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredWidth(view) + layoutParams.leftMargin + layoutParams.rightMargin;
            }

            @Override
            public int getDecoratedMeasurementInOther(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredHeight(view) + layoutParams.topMargin + layoutParams.bottomMargin;
            }

            @Override
            public int getDecoratedStart(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedLeft(view) - layoutParams.leftMargin;
            }

            @Override
            public int getEnd() {
                return this.mLayoutManager.getWidth();
            }

            @Override
            public int getEndAfterPadding() {
                return this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingRight();
            }

            @Override
            public int getEndPadding() {
                return this.mLayoutManager.getPaddingRight();
            }

            @Override
            public int getMode() {
                return this.mLayoutManager.getWidthMode();
            }

            @Override
            public int getModeInOther() {
                return this.mLayoutManager.getHeightMode();
            }

            @Override
            public int getStartAfterPadding() {
                return this.mLayoutManager.getPaddingLeft();
            }

            @Override
            public int getTotalSpace() {
                return this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingLeft() - this.mLayoutManager.getPaddingRight();
            }

            @Override
            public int getTransformedEndWithDecoration(View view) {
                this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                return this.mTmpRect.right;
            }

            @Override
            public int getTransformedStartWithDecoration(View view) {
                this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                return this.mTmpRect.left;
            }

            @Override
            public void offsetChild(View view, int n) {
                view.offsetLeftAndRight(n);
            }

            @Override
            public void offsetChildren(int n) {
                this.mLayoutManager.offsetChildrenHorizontal(n);
            }
        };
    }

    public static OrientationHelper createOrientationHelper(RecyclerView.LayoutManager layoutManager, int n) {
        if (n != 0) {
            if (n == 1) {
                return OrientationHelper.createVerticalHelper(layoutManager);
            }
            throw new IllegalArgumentException("invalid orientation");
        }
        return OrientationHelper.createHorizontalHelper(layoutManager);
    }

    public static OrientationHelper createVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        return new OrientationHelper(layoutManager){

            @Override
            public int getDecoratedEnd(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedBottom(view) + layoutParams.bottomMargin;
            }

            @Override
            public int getDecoratedMeasurement(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredHeight(view) + layoutParams.topMargin + layoutParams.bottomMargin;
            }

            @Override
            public int getDecoratedMeasurementInOther(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredWidth(view) + layoutParams.leftMargin + layoutParams.rightMargin;
            }

            @Override
            public int getDecoratedStart(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                return this.mLayoutManager.getDecoratedTop(view) - layoutParams.topMargin;
            }

            @Override
            public int getEnd() {
                return this.mLayoutManager.getHeight();
            }

            @Override
            public int getEndAfterPadding() {
                return this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingBottom();
            }

            @Override
            public int getEndPadding() {
                return this.mLayoutManager.getPaddingBottom();
            }

            @Override
            public int getMode() {
                return this.mLayoutManager.getHeightMode();
            }

            @Override
            public int getModeInOther() {
                return this.mLayoutManager.getWidthMode();
            }

            @Override
            public int getStartAfterPadding() {
                return this.mLayoutManager.getPaddingTop();
            }

            @Override
            public int getTotalSpace() {
                return this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingTop() - this.mLayoutManager.getPaddingBottom();
            }

            @Override
            public int getTransformedEndWithDecoration(View view) {
                this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                return this.mTmpRect.bottom;
            }

            @Override
            public int getTransformedStartWithDecoration(View view) {
                this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                return this.mTmpRect.top;
            }

            @Override
            public void offsetChild(View view, int n) {
                view.offsetTopAndBottom(n);
            }

            @Override
            public void offsetChildren(int n) {
                this.mLayoutManager.offsetChildrenVertical(n);
            }
        };
    }

    public abstract int getDecoratedEnd(View var1);

    public abstract int getDecoratedMeasurement(View var1);

    public abstract int getDecoratedMeasurementInOther(View var1);

    public abstract int getDecoratedStart(View var1);

    public abstract int getEnd();

    public abstract int getEndAfterPadding();

    public abstract int getEndPadding();

    public RecyclerView.LayoutManager getLayoutManager() {
        return this.mLayoutManager;
    }

    public abstract int getMode();

    public abstract int getModeInOther();

    public abstract int getStartAfterPadding();

    public abstract int getTotalSpace();

    public int getTotalSpaceChange() {
        if (Integer.MIN_VALUE == this.mLastTotalSpace) {
            return 0;
        }
        return this.getTotalSpace() - this.mLastTotalSpace;
    }

    public abstract int getTransformedEndWithDecoration(View var1);

    public abstract int getTransformedStartWithDecoration(View var1);

    public abstract void offsetChild(View var1, int var2);

    public abstract void offsetChildren(int var1);

    public void onLayoutComplete() {
        this.mLastTotalSpace = this.getTotalSpace();
    }

}

