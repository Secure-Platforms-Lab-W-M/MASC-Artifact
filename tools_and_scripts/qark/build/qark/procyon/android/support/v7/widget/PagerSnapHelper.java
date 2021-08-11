// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import android.util.DisplayMetrics;
import android.content.Context;
import android.view.View;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PagerSnapHelper extends SnapHelper
{
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    @Nullable
    private OrientationHelper mVerticalHelper;
    
    private int distanceToCenter(@NonNull final LayoutManager layoutManager, @NonNull final View view, final OrientationHelper orientationHelper) {
        final int decoratedStart = orientationHelper.getDecoratedStart(view);
        final int n = orientationHelper.getDecoratedMeasurement(view) / 2;
        int n2;
        if (layoutManager.getClipToPadding()) {
            n2 = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        }
        else {
            n2 = orientationHelper.getEnd() / 2;
        }
        return decoratedStart + n - n2;
    }
    
    @Nullable
    private View findCenterView(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        final int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View view = null;
        int n;
        if (layoutManager.getClipToPadding()) {
            n = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        }
        else {
            n = orientationHelper.getEnd() / 2;
        }
        int n2 = Integer.MAX_VALUE;
        for (int i = 0; i < childCount; ++i) {
            final View child = layoutManager.getChildAt(i);
            final int abs = Math.abs(orientationHelper.getDecoratedStart(child) + orientationHelper.getDecoratedMeasurement(child) / 2 - n);
            if (abs < n2) {
                n2 = abs;
                view = child;
            }
        }
        return view;
    }
    
    @Nullable
    private View findStartView(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        final int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View view = null;
        int n = Integer.MAX_VALUE;
        for (int i = 0; i < childCount; ++i) {
            final View child = layoutManager.getChildAt(i);
            final int decoratedStart = orientationHelper.getDecoratedStart(child);
            if (decoratedStart < n) {
                n = decoratedStart;
                view = child;
            }
        }
        return view;
    }
    
    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull final LayoutManager layoutManager) {
        final OrientationHelper mHorizontalHelper = this.mHorizontalHelper;
        if (mHorizontalHelper == null || mHorizontalHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }
    
    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull final LayoutManager layoutManager) {
        final OrientationHelper mVerticalHelper = this.mVerticalHelper;
        if (mVerticalHelper == null || mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }
    
    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull final LayoutManager layoutManager, @NonNull final View view) {
        final int[] array = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            array[0] = this.distanceToCenter(layoutManager, view, this.getHorizontalHelper(layoutManager));
        }
        else {
            array[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            array[1] = this.distanceToCenter(layoutManager, view, this.getVerticalHelper(layoutManager));
            return array;
        }
        array[1] = 0;
        return array;
    }
    
    @Override
    protected LinearSmoothScroller createSnapScroller(final LayoutManager layoutManager) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this.mRecyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
                return 100.0f / displayMetrics.densityDpi;
            }
            
            @Override
            protected int calculateTimeForScrolling(final int n) {
                return Math.min(100, super.calculateTimeForScrolling(n));
            }
            
            @Override
            protected void onTargetFound(final View view, final State state, final Action action) {
                final PagerSnapHelper this$0 = PagerSnapHelper.this;
                final int[] calculateDistanceToFinalSnap = this$0.calculateDistanceToFinalSnap(this$0.mRecyclerView.getLayoutManager(), view);
                final int n = calculateDistanceToFinalSnap[0];
                final int n2 = calculateDistanceToFinalSnap[1];
                final int calculateTimeForDeceleration = this.calculateTimeForDeceleration(Math.max(Math.abs(n), Math.abs(n2)));
                if (calculateTimeForDeceleration > 0) {
                    action.update(n, n2, calculateTimeForDeceleration, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }
    
    @Nullable
    @Override
    public View findSnapView(final LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findCenterView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.findCenterView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        return null;
    }
    
    @Override
    public int findTargetSnapPosition(final LayoutManager layoutManager, int n, int n2) {
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        View view = null;
        if (layoutManager.canScrollVertically()) {
            view = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        else if (layoutManager.canScrollHorizontally()) {
            view = this.findStartView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        if (view == null) {
            return -1;
        }
        final int position = layoutManager.getPosition(view);
        if (position == -1) {
            return -1;
        }
        final boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        final int n3 = 0;
        if (canScrollHorizontally) {
            if (n > 0) {
                n = 1;
            }
            else {
                n = 0;
            }
        }
        else if (n2 > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        n2 = 0;
        if (layoutManager instanceof ScrollVectorProvider) {
            final PointF computeScrollVectorForPosition = ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(itemCount - 1);
            if (computeScrollVectorForPosition != null) {
                if (computeScrollVectorForPosition.x >= 0.0f && computeScrollVectorForPosition.y >= 0.0f) {
                    n2 = n3;
                }
                else {
                    n2 = 1;
                }
            }
        }
        if (n2 != 0) {
            if (n != 0) {
                return position - 1;
            }
        }
        else if (n != 0) {
            return position + 1;
        }
        return position;
    }
}
