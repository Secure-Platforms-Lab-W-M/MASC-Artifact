// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.annotation.RestrictTo;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.PointF;
import android.util.Log;
import java.util.List;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;

public class LinearLayoutManager extends LayoutManager implements ViewDropHandler, ScrollVectorProvider
{
    static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private int mInitialPrefetchItemCount;
    private boolean mLastStackFromEnd;
    private final LayoutChunkResult mLayoutChunkResult;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private boolean mRecycleChildrenOnDetach;
    private boolean mReverseLayout;
    boolean mShouldReverseLayout;
    private boolean mSmoothScrollbarEnabled;
    private boolean mStackFromEnd;
    
    public LinearLayoutManager(final Context context) {
        this(context, 1, false);
    }
    
    public LinearLayoutManager(final Context context, final int orientation, final boolean reverseLayout) {
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.setOrientation(orientation);
        this.setReverseLayout(reverseLayout);
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(true);
    }
    
    public LinearLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        final Properties properties = RecyclerView.LayoutManager.getProperties(context, set, n, n2);
        this.setOrientation(properties.orientation);
        this.setReverseLayout(properties.reverseLayout);
        this.setStackFromEnd(properties.stackFromEnd);
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(true);
    }
    
    private int computeScrollExtent(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollExtent(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int computeScrollOffset(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollOffset(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }
    
    private int computeScrollRange(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollRange(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
    }
    
    private View findFirstPartiallyOrCompletelyInvisibleChild(final Recycler recycler, final State state) {
        return this.findOnePartiallyOrCompletelyInvisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount());
    }
    
    private View findFirstReferenceChild(final Recycler recycler, final State state) {
        return this.findReferenceChild(recycler, state, 0, ((RecyclerView.LayoutManager)this).getChildCount(), state.getItemCount());
    }
    
    private View findFirstVisibleChildClosestToEnd(final boolean b, final boolean b2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), b, b2);
        }
        return this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, b, b2);
    }
    
    private View findFirstVisibleChildClosestToStart(final boolean b, final boolean b2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, b, b2);
        }
        return this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), b, b2);
    }
    
    private View findLastPartiallyOrCompletelyInvisibleChild(final Recycler recycler, final State state) {
        return this.findOnePartiallyOrCompletelyInvisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1);
    }
    
    private View findLastReferenceChild(final Recycler recycler, final State state) {
        return this.findReferenceChild(recycler, state, ((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, state.getItemCount());
    }
    
    private View findPartiallyOrCompletelyInvisibleChildClosestToEnd(final Recycler recycler, final State state) {
        if (this.mShouldReverseLayout) {
            return this.findFirstPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        return this.findLastPartiallyOrCompletelyInvisibleChild(recycler, state);
    }
    
    private View findPartiallyOrCompletelyInvisibleChildClosestToStart(final Recycler recycler, final State state) {
        if (this.mShouldReverseLayout) {
            return this.findLastPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        return this.findFirstPartiallyOrCompletelyInvisibleChild(recycler, state);
    }
    
    private View findReferenceChildClosestToEnd(final Recycler recycler, final State state) {
        if (this.mShouldReverseLayout) {
            return this.findFirstReferenceChild(recycler, state);
        }
        return this.findLastReferenceChild(recycler, state);
    }
    
    private View findReferenceChildClosestToStart(final Recycler recycler, final State state) {
        if (this.mShouldReverseLayout) {
            return this.findLastReferenceChild(recycler, state);
        }
        return this.findFirstReferenceChild(recycler, state);
    }
    
    private int fixLayoutEndGap(int n, final Recycler recycler, final State state, final boolean b) {
        final int n2 = this.mOrientationHelper.getEndAfterPadding() - n;
        if (n2 <= 0) {
            return 0;
        }
        final int n3 = -this.scrollBy(-n2, recycler, state);
        if (!b) {
            return n3;
        }
        n = this.mOrientationHelper.getEndAfterPadding() - (n + n3);
        if (n > 0) {
            this.mOrientationHelper.offsetChildren(n);
            return n + n3;
        }
        return n3;
    }
    
    private int fixLayoutStartGap(int n, final Recycler recycler, final State state, final boolean b) {
        final int n2 = n - this.mOrientationHelper.getStartAfterPadding();
        if (n2 <= 0) {
            return 0;
        }
        final int n3 = -this.scrollBy(n2, recycler, state);
        if (!b) {
            return n3;
        }
        n = n + n3 - this.mOrientationHelper.getStartAfterPadding();
        if (n > 0) {
            this.mOrientationHelper.offsetChildren(-n);
            return n3 - n;
        }
        return n3;
    }
    
    private View getChildClosestToEnd() {
        int n;
        if (this.mShouldReverseLayout) {
            n = 0;
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        }
        return ((RecyclerView.LayoutManager)this).getChildAt(n);
    }
    
    private View getChildClosestToStart() {
        int n;
        if (this.mShouldReverseLayout) {
            n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        }
        else {
            n = 0;
        }
        return ((RecyclerView.LayoutManager)this).getChildAt(n);
    }
    
    private void layoutForPredictiveAnimations(final Recycler recycler, final State state, final int n, final int n2) {
        if (!state.willRunPredictiveAnimations() || ((RecyclerView.LayoutManager)this).getChildCount() == 0 || state.isPreLayout()) {
            return;
        }
        if (!this.supportsPredictiveItemAnimations()) {
            return;
        }
        int mExtra = 0;
        int mExtra2 = 0;
        final List<ViewHolder> scrapList = recycler.getScrapList();
        final int size = scrapList.size();
        final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        for (int i = 0; i < size; ++i) {
            final ViewHolder viewHolder = scrapList.get(i);
            if (!viewHolder.isRemoved()) {
                final int layoutPosition = viewHolder.getLayoutPosition();
                int n3 = 1;
                if (layoutPosition < position != this.mShouldReverseLayout) {
                    n3 = -1;
                }
                if (n3 == -1) {
                    mExtra += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                }
                else {
                    mExtra2 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                }
            }
        }
        this.mLayoutState.mScrapList = scrapList;
        if (mExtra > 0) {
            this.updateLayoutStateToFillStart(((RecyclerView.LayoutManager)this).getPosition(this.getChildClosestToStart()), n);
            final LayoutState mLayoutState = this.mLayoutState;
            mLayoutState.mExtra = mExtra;
            mLayoutState.mAvailable = 0;
            mLayoutState.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        if (mExtra2 > 0) {
            this.updateLayoutStateToFillEnd(((RecyclerView.LayoutManager)this).getPosition(this.getChildClosestToEnd()), n2);
            final LayoutState mLayoutState2 = this.mLayoutState;
            mLayoutState2.mExtra = mExtra2;
            mLayoutState2.mAvailable = 0;
            mLayoutState2.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        this.mLayoutState.mScrapList = null;
    }
    
    private void logChildren() {
        Log.d("LinearLayoutManager", "internal representation of views on the screen");
        for (int i = 0; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final StringBuilder sb = new StringBuilder();
            sb.append("item ");
            sb.append(((RecyclerView.LayoutManager)this).getPosition(child));
            sb.append(", coord:");
            sb.append(this.mOrientationHelper.getDecoratedStart(child));
            Log.d("LinearLayoutManager", sb.toString());
        }
        Log.d("LinearLayoutManager", "==============");
    }
    
    private void recycleByLayoutState(final Recycler recycler, final LayoutState layoutState) {
        if (!layoutState.mRecycle) {
            return;
        }
        if (layoutState.mInfinite) {
            return;
        }
        if (layoutState.mLayoutDirection == -1) {
            this.recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
            return;
        }
        this.recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
    }
    
    private void recycleChildren(final Recycler recycler, int i, int j) {
        if (i == j) {
            return;
        }
        if (j > i) {
            for (--j; j >= i; --j) {
                ((RecyclerView.LayoutManager)this).removeAndRecycleViewAt(j, recycler);
            }
            return;
        }
        while (i > j) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleViewAt(i, recycler);
            --i;
        }
    }
    
    private void recycleViewsFromEnd(final Recycler recycler, int i) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (i < 0) {
            return;
        }
        final int n = this.mOrientationHelper.getEnd() - i;
        if (this.mShouldReverseLayout) {
            View child;
            for (i = 0; i < childCount; ++i) {
                child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (this.mOrientationHelper.getDecoratedStart(child) < n || this.mOrientationHelper.getTransformedStartWithDecoration(child) < n) {
                    this.recycleChildren(recycler, 0, i);
                    return;
                }
            }
            return;
        }
        View child2;
        for (i = childCount - 1; i >= 0; --i) {
            child2 = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (this.mOrientationHelper.getDecoratedStart(child2) < n || this.mOrientationHelper.getTransformedStartWithDecoration(child2) < n) {
                this.recycleChildren(recycler, childCount - 1, i);
                return;
            }
        }
    }
    
    private void recycleViewsFromStart(final Recycler recycler, final int n) {
        if (n < 0) {
            return;
        }
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (this.mShouldReverseLayout) {
            for (int i = childCount - 1; i >= 0; --i) {
                final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (this.mOrientationHelper.getDecoratedEnd(child) > n || this.mOrientationHelper.getTransformedEndWithDecoration(child) > n) {
                    this.recycleChildren(recycler, childCount - 1, i);
                    return;
                }
            }
            return;
        }
        for (int j = 0; j < childCount; ++j) {
            final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
            if (this.mOrientationHelper.getDecoratedEnd(child2) > n || this.mOrientationHelper.getTransformedEndWithDecoration(child2) > n) {
                this.recycleChildren(recycler, 0, j);
                return;
            }
        }
    }
    
    private void resolveShouldLayoutReverse() {
        if (this.mOrientation != 1 && this.isLayoutRTL()) {
            this.mShouldReverseLayout = (this.mReverseLayout ^ true);
            return;
        }
        this.mShouldReverseLayout = this.mReverseLayout;
    }
    
    private boolean updateAnchorFromChildren(final Recycler recycler, final State state, final AnchorInfo anchorInfo) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        boolean b = false;
        if (childCount == 0) {
            return false;
        }
        final View focusedChild = ((RecyclerView.LayoutManager)this).getFocusedChild();
        if (focusedChild != null && anchorInfo.isViewValidAsAnchor(focusedChild, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(focusedChild);
            return true;
        }
        if (this.mLastStackFromEnd != this.mStackFromEnd) {
            return false;
        }
        View view;
        if (anchorInfo.mLayoutFromEnd) {
            view = this.findReferenceChildClosestToEnd(recycler, state);
        }
        else {
            view = this.findReferenceChildClosestToStart(recycler, state);
        }
        if (view == null) {
            return false;
        }
        anchorInfo.assignFromView(view);
        if (state.isPreLayout() || !this.supportsPredictiveItemAnimations()) {
            return true;
        }
        if (this.mOrientationHelper.getDecoratedStart(view) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view) < this.mOrientationHelper.getStartAfterPadding()) {
            b = true;
        }
        if (b) {
            int mCoordinate;
            if (anchorInfo.mLayoutFromEnd) {
                mCoordinate = this.mOrientationHelper.getEndAfterPadding();
            }
            else {
                mCoordinate = this.mOrientationHelper.getStartAfterPadding();
            }
            anchorInfo.mCoordinate = mCoordinate;
            return true;
        }
        return true;
    }
    
    private boolean updateAnchorFromPendingData(final State state, final AnchorInfo anchorInfo) {
        final boolean preLayout = state.isPreLayout();
        boolean mLayoutFromEnd = false;
        if (preLayout) {
            return false;
        }
        final int mPendingScrollPosition = this.mPendingScrollPosition;
        if (mPendingScrollPosition == -1) {
            return false;
        }
        if (mPendingScrollPosition < 0 || mPendingScrollPosition >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null && mPendingSavedState.hasValidAnchor()) {
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
                return true;
            }
            anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
            return true;
        }
        else if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            final View viewByPosition = this.findViewByPosition(this.mPendingScrollPosition);
            if (viewByPosition == null) {
                if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                    if (this.mPendingScrollPosition < ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0)) == this.mShouldReverseLayout) {
                        mLayoutFromEnd = true;
                    }
                    anchorInfo.mLayoutFromEnd = mLayoutFromEnd;
                }
                anchorInfo.assignCoordinateFromPadding();
                return true;
            }
            if (this.mOrientationHelper.getDecoratedMeasurement(viewByPosition) > this.mOrientationHelper.getTotalSpace()) {
                anchorInfo.assignCoordinateFromPadding();
                return true;
            }
            if (this.mOrientationHelper.getDecoratedStart(viewByPosition) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                anchorInfo.mLayoutFromEnd = false;
                return true;
            }
            if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(viewByPosition) < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                return anchorInfo.mLayoutFromEnd = true;
            }
            int decoratedStart;
            if (anchorInfo.mLayoutFromEnd) {
                decoratedStart = this.mOrientationHelper.getDecoratedEnd(viewByPosition) + this.mOrientationHelper.getTotalSpaceChange();
            }
            else {
                decoratedStart = this.mOrientationHelper.getDecoratedStart(viewByPosition);
            }
            anchorInfo.mCoordinate = decoratedStart;
            return true;
        }
        else {
            final boolean mShouldReverseLayout = this.mShouldReverseLayout;
            anchorInfo.mLayoutFromEnd = mShouldReverseLayout;
            if (mShouldReverseLayout) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
                return true;
            }
            anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
            return true;
        }
    }
    
    private void updateAnchorInfoForLayout(final Recycler recycler, final State state, final AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (this.updateAnchorFromChildren(recycler, state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        int mPosition;
        if (this.mStackFromEnd) {
            mPosition = state.getItemCount() - 1;
        }
        else {
            mPosition = 0;
        }
        anchorInfo.mPosition = mPosition;
    }
    
    private void updateLayoutState(int n, final int mAvailable, final boolean b, final State state) {
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mExtra = this.getExtraLayoutSpace(state);
        final LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mLayoutDirection = n;
        int n2 = -1;
        if (n == 1) {
            mLayoutState.mExtra += this.mOrientationHelper.getEndPadding();
            final View childClosestToEnd = this.getChildClosestToEnd();
            final LayoutState mLayoutState2 = this.mLayoutState;
            if (!this.mShouldReverseLayout) {
                n2 = 1;
            }
            mLayoutState2.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToEnd) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
            n = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd) - this.mOrientationHelper.getEndAfterPadding();
        }
        else {
            final View childClosestToStart = this.getChildClosestToStart();
            final LayoutState mLayoutState3 = this.mLayoutState;
            mLayoutState3.mExtra += this.mOrientationHelper.getStartAfterPadding();
            final LayoutState mLayoutState4 = this.mLayoutState;
            if (this.mShouldReverseLayout) {
                n2 = 1;
            }
            mLayoutState4.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToStart) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart);
            n = -this.mOrientationHelper.getDecoratedStart(childClosestToStart) + this.mOrientationHelper.getStartAfterPadding();
        }
        final LayoutState mLayoutState5 = this.mLayoutState;
        mLayoutState5.mAvailable = mAvailable;
        if (b) {
            mLayoutState5.mAvailable -= n;
        }
        this.mLayoutState.mScrollingOffset = n;
    }
    
    private void updateLayoutStateToFillEnd(final int mCurrentPosition, final int mOffset) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - mOffset;
        final LayoutState mLayoutState = this.mLayoutState;
        int mItemDirection;
        if (this.mShouldReverseLayout) {
            mItemDirection = -1;
        }
        else {
            mItemDirection = 1;
        }
        mLayoutState.mItemDirection = mItemDirection;
        final LayoutState mLayoutState2 = this.mLayoutState;
        mLayoutState2.mCurrentPosition = mCurrentPosition;
        mLayoutState2.mLayoutDirection = 1;
        mLayoutState2.mOffset = mOffset;
        mLayoutState2.mScrollingOffset = Integer.MIN_VALUE;
    }
    
    private void updateLayoutStateToFillEnd(final AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }
    
    private void updateLayoutStateToFillStart(int n, final int mOffset) {
        this.mLayoutState.mAvailable = mOffset - this.mOrientationHelper.getStartAfterPadding();
        final LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mCurrentPosition = n;
        if (this.mShouldReverseLayout) {
            n = 1;
        }
        else {
            n = -1;
        }
        mLayoutState.mItemDirection = n;
        final LayoutState mLayoutState2 = this.mLayoutState;
        mLayoutState2.mLayoutDirection = -1;
        mLayoutState2.mOffset = mOffset;
        mLayoutState2.mScrollingOffset = Integer.MIN_VALUE;
    }
    
    private void updateLayoutStateToFillStart(final AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }
    
    @Override
    public void assertNotInLayoutOrScroll(final String s) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(s);
        }
    }
    
    @Override
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }
    
    @Override
    public boolean canScrollVertically() {
        return this.mOrientation == 1;
    }
    
    @Override
    public void collectAdjacentPrefetchPositions(int n, int n2, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        if (this.mOrientation != 0) {
            n = n2;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return;
        }
        if (n == 0) {
            return;
        }
        this.ensureLayoutState();
        if (n > 0) {
            n2 = 1;
        }
        else {
            n2 = -1;
        }
        this.updateLayoutState(n2, Math.abs(n), true, state);
        this.collectPrefetchPositionsForLayoutState(state, this.mLayoutState, layoutPrefetchRegistry);
    }
    
    @Override
    public void collectInitialPrefetchPositions(final int n, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        int n2 = -1;
        boolean b;
        int n3;
        if (mPendingSavedState != null && mPendingSavedState.hasValidAnchor()) {
            b = this.mPendingSavedState.mAnchorLayoutFromEnd;
            n3 = this.mPendingSavedState.mAnchorPosition;
        }
        else {
            this.resolveShouldLayoutReverse();
            b = this.mShouldReverseLayout;
            if (this.mPendingScrollPosition == -1) {
                if (b) {
                    n3 = n - 1;
                }
                else {
                    n3 = 0;
                }
            }
            else {
                n3 = this.mPendingScrollPosition;
            }
        }
        if (!b) {
            n2 = 1;
        }
        int n4 = n3;
        for (int i = 0; i < this.mInitialPrefetchItemCount; ++i) {
            if (n4 < 0 || n4 >= n) {
                return;
            }
            layoutPrefetchRegistry.addPosition(n4, 0);
            n4 += n2;
        }
    }
    
    void collectPrefetchPositionsForLayoutState(final State state, final LayoutState layoutState, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        final int mCurrentPosition = layoutState.mCurrentPosition;
        if (mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount()) {
            layoutPrefetchRegistry.addPosition(mCurrentPosition, Math.max(0, layoutState.mScrollingOffset));
        }
    }
    
    @Override
    public int computeHorizontalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeHorizontalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeHorizontalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    @Override
    public PointF computeScrollVectorForPosition(int n) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        boolean b = false;
        final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        final int n2 = 1;
        if (n < position) {
            b = true;
        }
        n = n2;
        if (b != this.mShouldReverseLayout) {
            n = -1;
        }
        if (this.mOrientation == 0) {
            return new PointF((float)n, 0.0f);
        }
        return new PointF(0.0f, (float)n);
    }
    
    @Override
    public int computeVerticalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeVerticalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeVerticalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    int convertFocusDirectionToLayoutDirection(final int n) {
        int n2 = Integer.MIN_VALUE;
        if (n != 17) {
            if (n != 33) {
                if (n == 66) {
                    if (this.mOrientation == 0) {
                        n2 = 1;
                    }
                    return n2;
                }
                if (n == 130) {
                    if (this.mOrientation == 1) {
                        n2 = 1;
                    }
                    return n2;
                }
                switch (n) {
                    default: {
                        return Integer.MIN_VALUE;
                    }
                    case 2: {
                        if (this.mOrientation == 1) {
                            return 1;
                        }
                        if (this.isLayoutRTL()) {
                            return -1;
                        }
                        return 1;
                    }
                    case 1: {
                        if (this.mOrientation == 1) {
                            return -1;
                        }
                        if (this.isLayoutRTL()) {
                            return 1;
                        }
                        return -1;
                    }
                }
            }
            else {
                if (this.mOrientation == 1) {
                    return -1;
                }
                return Integer.MIN_VALUE;
            }
        }
        else {
            if (this.mOrientation == 0) {
                return -1;
            }
            return Integer.MIN_VALUE;
        }
    }
    
    LayoutState createLayoutState() {
        return new LayoutState();
    }
    
    void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = this.createLayoutState();
        }
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        }
    }
    
    int fill(final Recycler recycler, final LayoutState layoutState, final State state, final boolean b) {
        final int mAvailable = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            this.recycleByLayoutState(recycler, layoutState);
        }
        int n = layoutState.mAvailable + layoutState.mExtra;
        final LayoutChunkResult mLayoutChunkResult = this.mLayoutChunkResult;
        while ((layoutState.mInfinite || n > 0) && layoutState.hasMore(state)) {
            mLayoutChunkResult.resetInternal();
            this.layoutChunk(recycler, state, layoutState, mLayoutChunkResult);
            if (mLayoutChunkResult.mFinished) {
                break;
            }
            layoutState.mOffset += mLayoutChunkResult.mConsumed * layoutState.mLayoutDirection;
            if (!mLayoutChunkResult.mIgnoreConsumed || this.mLayoutState.mScrapList != null || !state.isPreLayout()) {
                layoutState.mAvailable -= mLayoutChunkResult.mConsumed;
                n -= mLayoutChunkResult.mConsumed;
            }
            if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
                layoutState.mScrollingOffset += mLayoutChunkResult.mConsumed;
                if (layoutState.mAvailable < 0) {
                    layoutState.mScrollingOffset += layoutState.mAvailable;
                }
                this.recycleByLayoutState(recycler, layoutState);
            }
            if (b && mLayoutChunkResult.mFocusable) {
                break;
            }
        }
        return mAvailable - layoutState.mAvailable;
    }
    
    public int findFirstCompletelyVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), true, false);
        if (oneVisibleChild == null) {
            return -1;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
    }
    
    public int findFirstVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), false, true);
        if (oneVisibleChild == null) {
            return -1;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
    }
    
    public int findLastCompletelyVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, true, false);
        if (oneVisibleChild == null) {
            return -1;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
    }
    
    public int findLastVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, false, true);
        if (oneVisibleChild == null) {
            return -1;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
    }
    
    View findOnePartiallyOrCompletelyInvisibleChild(final int n, final int n2) {
        this.ensureLayoutState();
        int n3;
        if (n2 > n) {
            n3 = 1;
        }
        else if (n2 < n) {
            n3 = -1;
        }
        else {
            n3 = 0;
        }
        if (n3 == 0) {
            return ((RecyclerView.LayoutManager)this).getChildAt(n);
        }
        int n4;
        int n5;
        if (this.mOrientationHelper.getDecoratedStart(((RecyclerView.LayoutManager)this).getChildAt(n)) < this.mOrientationHelper.getStartAfterPadding()) {
            n4 = 16644;
            n5 = 16388;
        }
        else {
            n4 = 4161;
            n5 = 4097;
        }
        if (this.mOrientation == 0) {
            return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n5);
        }
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n5);
    }
    
    View findOneVisibleChild(final int n, final int n2, final boolean b, final boolean b2) {
        this.ensureLayoutState();
        int n3 = 0;
        int n4;
        if (b) {
            n4 = 24579;
        }
        else {
            n4 = 320;
        }
        if (b2) {
            n3 = 320;
        }
        if (this.mOrientation == 0) {
            return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n3);
        }
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n3);
    }
    
    View findReferenceChild(final Recycler recycler, final State state, int i, final int n, final int n2) {
        this.ensureLayoutState();
        View view = null;
        View view2 = null;
        final int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        final int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int n3;
        if (n > i) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        while (i != n) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int position = ((RecyclerView.LayoutManager)this).getPosition(child);
            if (position >= 0 && position < n2) {
                if (((LayoutParams)child.getLayoutParams()).isItemRemoved()) {
                    if (view == null) {
                        view = child;
                    }
                }
                else {
                    if (this.mOrientationHelper.getDecoratedStart(child) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(child) >= startAfterPadding) {
                        return child;
                    }
                    if (view2 == null) {
                        view2 = child;
                    }
                }
            }
            i += n3;
        }
        if (view2 != null) {
            return view2;
        }
        return view;
    }
    
    @Override
    public View findViewByPosition(final int n) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (childCount == 0) {
            return null;
        }
        final int n2 = n - ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        if (n2 >= 0 && n2 < childCount) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(n2);
            if (((RecyclerView.LayoutManager)this).getPosition(child) == n) {
                return child;
            }
        }
        return super.findViewByPosition(n);
    }
    
    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }
    
    protected int getExtraLayoutSpace(final State state) {
        if (state.hasTargetScrollPosition()) {
            return this.mOrientationHelper.getTotalSpace();
        }
        return 0;
    }
    
    public int getInitialPrefetchItemCount() {
        return this.mInitialPrefetchItemCount;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }
    
    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }
    
    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }
    
    protected boolean isLayoutRTL() {
        return ((RecyclerView.LayoutManager)this).getLayoutDirection() == 1;
    }
    
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }
    
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final View next = layoutState.next(recycler);
        if (next == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        final LayoutParams layoutParams = (LayoutParams)next.getLayoutParams();
        if (layoutState.mScrapList == null) {
            if (this.mShouldReverseLayout == (layoutState.mLayoutDirection == -1)) {
                ((RecyclerView.LayoutManager)this).addView(next);
            }
            else {
                ((RecyclerView.LayoutManager)this).addView(next, 0);
            }
        }
        else if (this.mShouldReverseLayout == (layoutState.mLayoutDirection == -1)) {
            ((RecyclerView.LayoutManager)this).addDisappearingView(next);
        }
        else {
            ((RecyclerView.LayoutManager)this).addDisappearingView(next, 0);
        }
        ((RecyclerView.LayoutManager)this).measureChildWithMargins(next, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(next);
        int paddingLeft;
        int mOffset;
        int mOffset3;
        int paddingTop;
        if (this.mOrientation == 1) {
            int n;
            if (this.isLayoutRTL()) {
                n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight();
                paddingLeft = n - this.mOrientationHelper.getDecoratedMeasurementInOther(next);
            }
            else {
                paddingLeft = ((RecyclerView.LayoutManager)this).getPaddingLeft();
                n = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingLeft;
            }
            if (layoutState.mLayoutDirection == -1) {
                mOffset = layoutState.mOffset;
                final int mOffset2 = layoutState.mOffset;
                final int mConsumed = layoutChunkResult.mConsumed;
                mOffset3 = n;
                paddingTop = mOffset2 - mConsumed;
            }
            else {
                final int mOffset4 = layoutState.mOffset;
                final int mOffset5 = layoutState.mOffset;
                final int mConsumed2 = layoutChunkResult.mConsumed;
                mOffset3 = n;
                paddingTop = mOffset4;
                mOffset = mOffset5 + mConsumed2;
            }
        }
        else {
            paddingTop = ((RecyclerView.LayoutManager)this).getPaddingTop();
            final int n2 = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingTop;
            if (layoutState.mLayoutDirection == -1) {
                mOffset3 = layoutState.mOffset;
                final int mOffset6 = layoutState.mOffset;
                final int mConsumed3 = layoutChunkResult.mConsumed;
                mOffset = n2;
                paddingLeft = mOffset6 - mConsumed3;
            }
            else {
                final int mOffset7 = layoutState.mOffset;
                mOffset3 = layoutState.mOffset + layoutChunkResult.mConsumed;
                mOffset = n2;
                paddingLeft = mOffset7;
            }
        }
        ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, paddingLeft, paddingTop, mOffset3, mOffset);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = next.hasFocusable();
    }
    
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
    }
    
    @Override
    public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        if (this.mRecycleChildrenOnDetach) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }
    
    @Override
    public View onFocusSearchFailed(View view, int convertFocusDirectionToLayoutDirection, final Recycler recycler, final State state) {
        this.resolveShouldLayoutReverse();
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        convertFocusDirectionToLayoutDirection = this.convertFocusDirectionToLayoutDirection(convertFocusDirectionToLayoutDirection);
        if (convertFocusDirectionToLayoutDirection == Integer.MIN_VALUE) {
            return null;
        }
        this.ensureLayoutState();
        this.ensureLayoutState();
        this.updateLayoutState(convertFocusDirectionToLayoutDirection, (int)(this.mOrientationHelper.getTotalSpace() * 0.33333334f), false, state);
        final LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        mLayoutState.mRecycle = false;
        this.fill(recycler, mLayoutState, state, true);
        if (convertFocusDirectionToLayoutDirection == -1) {
            view = this.findPartiallyOrCompletelyInvisibleChildClosestToStart(recycler, state);
        }
        else {
            view = this.findPartiallyOrCompletelyInvisibleChildClosestToEnd(recycler, state);
        }
        View view2;
        if (convertFocusDirectionToLayoutDirection == -1) {
            view2 = this.getChildClosestToStart();
        }
        else {
            view2 = this.getChildClosestToEnd();
        }
        if (!view2.hasFocusable()) {
            return view;
        }
        if (view == null) {
            return null;
        }
        return view2;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            accessibilityEvent.setFromIndex(this.findFirstVisibleItemPosition());
            accessibilityEvent.setToIndex(this.findLastVisibleItemPosition());
        }
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        int n = -1;
        if (mPendingSavedState != null || this.mPendingScrollPosition != -1) {
            if (state.getItemCount() == 0) {
                ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
                return;
            }
        }
        final SavedState mPendingSavedState2 = this.mPendingSavedState;
        if (mPendingSavedState2 != null && mPendingSavedState2.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        }
        this.ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        this.resolveShouldLayoutReverse();
        final View focusedChild = ((RecyclerView.LayoutManager)this).getFocusedChild();
        if (this.mAnchorInfo.mValid && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            if (focusedChild != null) {
                if (this.mOrientationHelper.getDecoratedStart(focusedChild) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(focusedChild) <= this.mOrientationHelper.getStartAfterPadding()) {
                    this.mAnchorInfo.assignFromViewAndKeepVisibleRect(focusedChild);
                }
            }
        }
        else {
            this.mAnchorInfo.reset();
            final AnchorInfo mAnchorInfo = this.mAnchorInfo;
            mAnchorInfo.mLayoutFromEnd = (this.mShouldReverseLayout ^ this.mStackFromEnd);
            this.updateAnchorInfoForLayout(recycler, state, mAnchorInfo);
            this.mAnchorInfo.mValid = true;
        }
        int extraLayoutSpace = this.getExtraLayoutSpace(state);
        int n2;
        if (this.mLayoutState.mLastScrollDelta >= 0) {
            n2 = 0;
        }
        else {
            n2 = extraLayoutSpace;
            extraLayoutSpace = 0;
        }
        int n3 = n2 + this.mOrientationHelper.getStartAfterPadding();
        final int n4 = extraLayoutSpace + this.mOrientationHelper.getEndPadding();
        int n5 = 0;
        Label_0414: {
            if (state.isPreLayout()) {
                final int mPendingScrollPosition = this.mPendingScrollPosition;
                if (mPendingScrollPosition != -1 && this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                    final View viewByPosition = this.findViewByPosition(mPendingScrollPosition);
                    if (viewByPosition == null) {
                        n5 = n4;
                        break Label_0414;
                    }
                    int n6;
                    if (this.mShouldReverseLayout) {
                        n6 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(viewByPosition) - this.mPendingScrollPositionOffset;
                    }
                    else {
                        n6 = this.mPendingScrollPositionOffset - (this.mOrientationHelper.getDecoratedStart(viewByPosition) - this.mOrientationHelper.getStartAfterPadding());
                    }
                    if (n6 > 0) {
                        n3 += n6;
                        n5 = n4;
                        break Label_0414;
                    }
                    n5 = n4 - n6;
                    break Label_0414;
                }
            }
            n5 = n4;
        }
        if (this.mAnchorInfo.mLayoutFromEnd) {
            if (this.mShouldReverseLayout) {
                n = 1;
            }
        }
        else if (!this.mShouldReverseLayout) {
            n = 1;
        }
        this.onAnchorReady(recycler, state, this.mAnchorInfo, n);
        ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        int mOffset4;
        int n7;
        if (this.mAnchorInfo.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            final LayoutState mLayoutState = this.mLayoutState;
            mLayoutState.mExtra = n3;
            this.fill(recycler, mLayoutState, state, false);
            final int mOffset = this.mLayoutState.mOffset;
            final int mCurrentPosition = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
                n5 += this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            final LayoutState mLayoutState2 = this.mLayoutState;
            mLayoutState2.mExtra = n5;
            mLayoutState2.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            final int mOffset2 = this.mLayoutState.mOffset;
            int mOffset3;
            if (this.mLayoutState.mAvailable > 0) {
                final int mAvailable = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillStart(mCurrentPosition, mOffset);
                final LayoutState mLayoutState3 = this.mLayoutState;
                mLayoutState3.mExtra = mAvailable;
                this.fill(recycler, mLayoutState3, state, false);
                mOffset3 = this.mLayoutState.mOffset;
            }
            else {
                mOffset3 = mOffset;
            }
            mOffset4 = mOffset3;
            n7 = mOffset2;
        }
        else {
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            final LayoutState mLayoutState4 = this.mLayoutState;
            mLayoutState4.mExtra = n5;
            this.fill(recycler, mLayoutState4, state, false);
            n7 = this.mLayoutState.mOffset;
            final int mCurrentPosition2 = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
                n3 += this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            final LayoutState mLayoutState5 = this.mLayoutState;
            mLayoutState5.mExtra = n3;
            mLayoutState5.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            mOffset4 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
                final int mAvailable2 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillEnd(mCurrentPosition2, n7);
                final LayoutState mLayoutState6 = this.mLayoutState;
                mLayoutState6.mExtra = mAvailable2;
                this.fill(recycler, mLayoutState6, state, false);
                n7 = this.mLayoutState.mOffset;
            }
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
                final int fixLayoutEndGap = this.fixLayoutEndGap(n7, recycler, state, true);
                final int n8 = mOffset4 + fixLayoutEndGap;
                final int fixLayoutStartGap = this.fixLayoutStartGap(n8, recycler, state, false);
                mOffset4 = n8 + fixLayoutStartGap;
                n7 = n7 + fixLayoutEndGap + fixLayoutStartGap;
            }
            else {
                final int fixLayoutStartGap2 = this.fixLayoutStartGap(mOffset4, recycler, state, true);
                final int n9 = n7 + fixLayoutStartGap2;
                final int fixLayoutEndGap2 = this.fixLayoutEndGap(n9, recycler, state, false);
                mOffset4 = mOffset4 + fixLayoutStartGap2 + fixLayoutEndGap2;
                n7 = n9 + fixLayoutEndGap2;
            }
        }
        this.layoutForPredictiveAnimations(recycler, state, mOffset4, n7);
        if (!state.isPreLayout()) {
            this.mOrientationHelper.onLayoutComplete();
        }
        else {
            this.mAnchorInfo.reset();
        }
        this.mLastStackFromEnd = this.mStackFromEnd;
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mAnchorInfo.reset();
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null) {
            return (Parcelable)new SavedState(mPendingSavedState);
        }
        final SavedState savedState = new SavedState();
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            this.ensureLayoutState();
            final boolean mAnchorLayoutFromEnd = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            savedState.mAnchorLayoutFromEnd = mAnchorLayoutFromEnd;
            if (mAnchorLayoutFromEnd) {
                final View childClosestToEnd = this.getChildClosestToEnd();
                savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
                savedState.mAnchorPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToEnd);
            }
            else {
                final View childClosestToStart = this.getChildClosestToStart();
                savedState.mAnchorPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToStart);
                savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart) - this.mOrientationHelper.getStartAfterPadding();
            }
            return (Parcelable)savedState;
        }
        savedState.invalidateAnchor();
        return (Parcelable)savedState;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void prepareForDrop(final View view, final View view2, int position, int position2) {
        this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        this.ensureLayoutState();
        this.resolveShouldLayoutReverse();
        position = ((RecyclerView.LayoutManager)this).getPosition(view);
        position2 = ((RecyclerView.LayoutManager)this).getPosition(view2);
        if (position < position2) {
            position = 1;
        }
        else {
            position = -1;
        }
        if (this.mShouldReverseLayout) {
            if (position == 1) {
                this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(view2) + this.mOrientationHelper.getDecoratedMeasurement(view)));
                return;
            }
            this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view2));
        }
        else {
            if (position == -1) {
                this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedStart(view2));
                return;
            }
            this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedMeasurement(view));
        }
    }
    
    boolean resolveIsInfinite() {
        return this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0;
    }
    
    int scrollBy(int mLastScrollDelta, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        if (mLastScrollDelta == 0) {
            return 0;
        }
        this.mLayoutState.mRecycle = true;
        this.ensureLayoutState();
        int n;
        if (mLastScrollDelta > 0) {
            n = 1;
        }
        else {
            n = -1;
        }
        final int abs = Math.abs(mLastScrollDelta);
        this.updateLayoutState(n, abs, true, state);
        final int n2 = this.mLayoutState.mScrollingOffset + this.fill(recycler, this.mLayoutState, state, false);
        if (n2 < 0) {
            return 0;
        }
        if (abs > n2) {
            mLastScrollDelta = n * n2;
        }
        this.mOrientationHelper.offsetChildren(-mLastScrollDelta);
        return this.mLayoutState.mLastScrollDelta = mLastScrollDelta;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }
    
    @Override
    public void scrollToPosition(final int mPendingScrollPosition) {
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null) {
            mPendingSavedState.invalidateAnchor();
        }
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void scrollToPositionWithOffset(final int mPendingScrollPosition, final int mPendingScrollPositionOffset) {
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = mPendingScrollPositionOffset;
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null) {
            mPendingSavedState.invalidateAnchor();
        }
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }
    
    public void setInitialPrefetchItemCount(final int mInitialPrefetchItemCount) {
        this.mInitialPrefetchItemCount = mInitialPrefetchItemCount;
    }
    
    public void setOrientation(final int mOrientation) {
        if (mOrientation != 0 && mOrientation != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid orientation:");
            sb.append(mOrientation);
            throw new IllegalArgumentException(sb.toString());
        }
        this.assertNotInLayoutOrScroll(null);
        if (mOrientation == this.mOrientation) {
            return;
        }
        this.mOrientation = mOrientation;
        this.mOrientationHelper = null;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setRecycleChildrenOnDetach(final boolean mRecycleChildrenOnDetach) {
        this.mRecycleChildrenOnDetach = mRecycleChildrenOnDetach;
    }
    
    public void setReverseLayout(final boolean mReverseLayout) {
        this.assertNotInLayoutOrScroll(null);
        if (mReverseLayout == this.mReverseLayout) {
            return;
        }
        this.mReverseLayout = mReverseLayout;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSmoothScrollbarEnabled(final boolean mSmoothScrollbarEnabled) {
        this.mSmoothScrollbarEnabled = mSmoothScrollbarEnabled;
    }
    
    public void setStackFromEnd(final boolean mStackFromEnd) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd == mStackFromEnd) {
            return;
        }
        this.mStackFromEnd = mStackFromEnd;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    boolean shouldMeasureTwice() {
        if (((RecyclerView.LayoutManager)this).getHeightMode() != 1073741824) {
            if (((RecyclerView.LayoutManager)this).getWidthMode() != 1073741824) {
                if (((RecyclerView.LayoutManager)this).hasFlexibleChildInBothOrientations()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        ((RecyclerView.SmoothScroller)linearSmoothScroller).setTargetPosition(targetPosition);
        ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScroller);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd;
    }
    
    void validateChildOrder() {
        final StringBuilder sb = new StringBuilder();
        sb.append("validating child count ");
        sb.append(((RecyclerView.LayoutManager)this).getChildCount());
        Log.d("LinearLayoutManager", sb.toString());
        if (((RecyclerView.LayoutManager)this).getChildCount() < 1) {
            return;
        }
        final boolean b = false;
        boolean b2 = false;
        final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        final int decoratedStart = this.mOrientationHelper.getDecoratedStart(((RecyclerView.LayoutManager)this).getChildAt(0));
        if (this.mShouldReverseLayout) {
            for (int i = 1; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
                final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                final int position2 = ((RecyclerView.LayoutManager)this).getPosition(child);
                final int decoratedStart2 = this.mOrientationHelper.getDecoratedStart(child);
                if (position2 < position) {
                    this.logChildren();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("detected invalid position. loc invalid? ");
                    if (decoratedStart2 < decoratedStart) {
                        b2 = true;
                    }
                    sb2.append(b2);
                    throw new RuntimeException(sb2.toString());
                }
                if (decoratedStart2 > decoratedStart) {
                    this.logChildren();
                    throw new RuntimeException("detected invalid location");
                }
            }
            return;
        }
        for (int j = 1; j < ((RecyclerView.LayoutManager)this).getChildCount(); ++j) {
            final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
            final int position3 = ((RecyclerView.LayoutManager)this).getPosition(child2);
            final int decoratedStart3 = this.mOrientationHelper.getDecoratedStart(child2);
            if (position3 < position) {
                this.logChildren();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("detected invalid position. loc invalid? ");
                boolean b3 = b;
                if (decoratedStart3 < decoratedStart) {
                    b3 = true;
                }
                sb3.append(b3);
                throw new RuntimeException(sb3.toString());
            }
            if (decoratedStart3 < decoratedStart) {
                this.logChildren();
                throw new RuntimeException("detected invalid location");
            }
        }
    }
    
    class AnchorInfo
    {
        int mCoordinate;
        boolean mLayoutFromEnd;
        int mPosition;
        boolean mValid;
        
        AnchorInfo() {
            this.reset();
        }
        
        void assignCoordinateFromPadding() {
            int mCoordinate;
            if (this.mLayoutFromEnd) {
                mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
            }
            else {
                mCoordinate = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
            }
            this.mCoordinate = mCoordinate;
        }
        
        public void assignFromView(final View view) {
            if (this.mLayoutFromEnd) {
                this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view) + LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
            }
            else {
                this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
            }
            this.mPosition = ((RecyclerView.LayoutManager)LinearLayoutManager.this).getPosition(view);
        }
        
        public void assignFromViewAndKeepVisibleRect(final View view) {
            final int totalSpaceChange = LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
            if (totalSpaceChange >= 0) {
                this.assignFromView(view);
                return;
            }
            this.mPosition = ((RecyclerView.LayoutManager)LinearLayoutManager.this).getPosition(view);
            if (this.mLayoutFromEnd) {
                final int n = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view);
                this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - n;
                if (n > 0) {
                    final int decoratedMeasurement = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view);
                    final int mCoordinate = this.mCoordinate;
                    final int startAfterPadding = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                    final int n2 = mCoordinate - decoratedMeasurement - (Math.min(LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view) - startAfterPadding, 0) + startAfterPadding);
                    if (n2 < 0) {
                        this.mCoordinate += Math.min(n, -n2);
                    }
                }
                return;
            }
            final int decoratedStart = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
            final int n3 = decoratedStart - LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = decoratedStart;
            if (n3 <= 0) {
                return;
            }
            final int n4 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - Math.min(0, LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view)) - (LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view) + decoratedStart);
            if (n4 < 0) {
                this.mCoordinate -= Math.min(n3, -n4);
            }
        }
        
        boolean isViewValidAsAnchor(final View view, final State state) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            return !layoutParams.isItemRemoved() && layoutParams.getViewLayoutPosition() >= 0 && layoutParams.getViewLayoutPosition() < state.getItemCount();
        }
        
        void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mValid = false;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("AnchorInfo{mPosition=");
            sb.append(this.mPosition);
            sb.append(", mCoordinate=");
            sb.append(this.mCoordinate);
            sb.append(", mLayoutFromEnd=");
            sb.append(this.mLayoutFromEnd);
            sb.append(", mValid=");
            sb.append(this.mValid);
            sb.append('}');
            return sb.toString();
        }
    }
    
    protected static class LayoutChunkResult
    {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;
        
        void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }
    
    static class LayoutState
    {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LLM#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtra;
        boolean mInfinite;
        boolean mIsPreLayout;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mOffset;
        boolean mRecycle;
        List<ViewHolder> mScrapList;
        int mScrollingOffset;
        
        LayoutState() {
            this.mRecycle = true;
            this.mExtra = 0;
            this.mIsPreLayout = false;
            this.mScrapList = null;
        }
        
        private View nextViewFromScrapList() {
            for (int size = this.mScrapList.size(), i = 0; i < size; ++i) {
                final View itemView = this.mScrapList.get(i).itemView;
                final LayoutParams layoutParams = (LayoutParams)itemView.getLayoutParams();
                if (!layoutParams.isItemRemoved()) {
                    if (this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                        this.assignPositionFromScrapList(itemView);
                        return itemView;
                    }
                }
            }
            return null;
        }
        
        public void assignPositionFromScrapList() {
            this.assignPositionFromScrapList(null);
        }
        
        public void assignPositionFromScrapList(View nextViewInLimitedList) {
            nextViewInLimitedList = this.nextViewInLimitedList(nextViewInLimitedList);
            if (nextViewInLimitedList == null) {
                this.mCurrentPosition = -1;
                return;
            }
            this.mCurrentPosition = ((LayoutParams)nextViewInLimitedList.getLayoutParams()).getViewLayoutPosition();
        }
        
        boolean hasMore(final State state) {
            final int mCurrentPosition = this.mCurrentPosition;
            return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
        }
        
        void log() {
            final StringBuilder sb = new StringBuilder();
            sb.append("avail:");
            sb.append(this.mAvailable);
            sb.append(", ind:");
            sb.append(this.mCurrentPosition);
            sb.append(", dir:");
            sb.append(this.mItemDirection);
            sb.append(", offset:");
            sb.append(this.mOffset);
            sb.append(", layoutDir:");
            sb.append(this.mLayoutDirection);
            Log.d("LLM#LayoutState", sb.toString());
        }
        
        View next(final Recycler recycler) {
            if (this.mScrapList != null) {
                return this.nextViewFromScrapList();
            }
            final View viewForPosition = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return viewForPosition;
        }
        
        public View nextViewInLimitedList(final View view) {
            final int size = this.mScrapList.size();
            View view2 = null;
            int n = Integer.MAX_VALUE;
            for (int i = 0; i < size; ++i) {
                final View itemView = this.mScrapList.get(i).itemView;
                final LayoutParams layoutParams = (LayoutParams)itemView.getLayoutParams();
                if (itemView != view) {
                    if (!layoutParams.isItemRemoved()) {
                        final int n2 = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
                        if (n2 >= 0) {
                            if (n2 < n) {
                                view2 = itemView;
                                if ((n = n2) == 0) {
                                    return view2;
                                }
                            }
                        }
                    }
                }
            }
            return view2;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState() {
        }
        
        SavedState(final Parcel parcel) {
            this.mAnchorPosition = parcel.readInt();
            this.mAnchorOffset = parcel.readInt();
            final int int1 = parcel.readInt();
            boolean mAnchorLayoutFromEnd = true;
            if (int1 != 1) {
                mAnchorLayoutFromEnd = false;
            }
            this.mAnchorLayoutFromEnd = mAnchorLayoutFromEnd;
        }
        
        public SavedState(final SavedState savedState) {
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mAnchorOffset = savedState.mAnchorOffset;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
        }
        
        public int describeContents() {
            return 0;
        }
        
        boolean hasValidAnchor() {
            return this.mAnchorPosition >= 0;
        }
        
        void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        }
    }
}
