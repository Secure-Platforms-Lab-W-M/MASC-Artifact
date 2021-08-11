// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.annotation.Nullable;

public class LinearSnapHelper extends SnapHelper
{
    private static final float INVALID_DISTANCE = 1.0f;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    @Nullable
    private OrientationHelper mVerticalHelper;
    
    private float computeDistancePerChild(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        View view = null;
        View view2 = null;
        int n = Integer.MAX_VALUE;
        int n2 = Integer.MIN_VALUE;
        final int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return 1.0f;
        }
        for (int i = 0; i < childCount; ++i) {
            final View child = layoutManager.getChildAt(i);
            final int position = layoutManager.getPosition(child);
            if (position != -1) {
                if (position < n) {
                    n = position;
                    view = child;
                }
                if (position > n2) {
                    n2 = position;
                    view2 = child;
                }
            }
        }
        if (view == null) {
            return 1.0f;
        }
        if (view2 == null) {
            return 1.0f;
        }
        final int n3 = Math.max(orientationHelper.getDecoratedEnd(view), orientationHelper.getDecoratedEnd(view2)) - Math.min(orientationHelper.getDecoratedStart(view), orientationHelper.getDecoratedStart(view2));
        if (n3 == 0) {
            return 1.0f;
        }
        return n3 * 1.0f / (n2 - n + 1);
    }
    
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
    
    private int estimateNextPositionDiffForFling(final LayoutManager layoutManager, final OrientationHelper orientationHelper, int n, final int n2) {
        final int[] calculateScrollDistance = this.calculateScrollDistance(n, n2);
        final float computeDistancePerChild = this.computeDistancePerChild(layoutManager, orientationHelper);
        if (computeDistancePerChild <= 0.0f) {
            return 0;
        }
        if (Math.abs(calculateScrollDistance[0]) > Math.abs(calculateScrollDistance[1])) {
            n = calculateScrollDistance[0];
        }
        else {
            n = calculateScrollDistance[1];
        }
        return Math.round(n / computeDistancePerChild);
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
    public int findTargetSnapPosition(final LayoutManager layoutManager, int estimateNextPositionDiffForFling, int estimateNextPositionDiffForFling2) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return -1;
        }
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        final View snapView = this.findSnapView(layoutManager);
        if (snapView == null) {
            return -1;
        }
        final int position = layoutManager.getPosition(snapView);
        if (position == -1) {
            return -1;
        }
        final PointF computeScrollVectorForPosition = ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(itemCount - 1);
        if (computeScrollVectorForPosition == null) {
            return -1;
        }
        if (layoutManager.canScrollHorizontally()) {
            estimateNextPositionDiffForFling = this.estimateNextPositionDiffForFling(layoutManager, this.getHorizontalHelper(layoutManager), estimateNextPositionDiffForFling, 0);
            if (computeScrollVectorForPosition.x < 0.0f) {
                estimateNextPositionDiffForFling = -estimateNextPositionDiffForFling;
            }
        }
        else {
            estimateNextPositionDiffForFling = 0;
        }
        if (layoutManager.canScrollVertically()) {
            estimateNextPositionDiffForFling2 = this.estimateNextPositionDiffForFling(layoutManager, this.getVerticalHelper(layoutManager), 0, estimateNextPositionDiffForFling2);
            if (computeScrollVectorForPosition.y < 0.0f) {
                estimateNextPositionDiffForFling2 = -estimateNextPositionDiffForFling2;
            }
        }
        else {
            estimateNextPositionDiffForFling2 = 0;
        }
        if (layoutManager.canScrollVertically()) {
            estimateNextPositionDiffForFling = estimateNextPositionDiffForFling2;
        }
        if (estimateNextPositionDiffForFling == 0) {
            return -1;
        }
        estimateNextPositionDiffForFling += position;
        if (estimateNextPositionDiffForFling < 0) {
            estimateNextPositionDiffForFling = 0;
        }
        if (estimateNextPositionDiffForFling >= itemCount) {
            return itemCount - 1;
        }
        return estimateNextPositionDiffForFling;
    }
}
