// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.util.SparseIntArray;
import android.graphics.Rect;

public class GridLayoutManager extends LinearLayoutManager
{
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache;
    View[] mSet;
    int mSpanCount;
    SpanSizeLookup mSpanSizeLookup;
    
    public GridLayoutManager(final Context context, final int spanCount) {
        super(context);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final int spanCount, final int n, final boolean b) {
        super(context, n, b);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(RecyclerView.LayoutManager.getProperties(context, set, n, n2).spanCount);
    }
    
    private void assignSpans(final Recycler recycler, final State state, int i, int n, final boolean b) {
        int n3;
        if (b) {
            final int n2 = 0;
            n = i;
            n3 = 1;
            i = n2;
        }
        else {
            --i;
            n = -1;
            n3 = -1;
        }
        int mSpanIndex = 0;
        while (i != n) {
            final View view = this.mSet[i];
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, ((RecyclerView.LayoutManager)this).getPosition(view));
            layoutParams.mSpanIndex = mSpanIndex;
            mSpanIndex += layoutParams.mSpanSize;
            i += n3;
        }
    }
    
    private void cachePreLayoutSpanMapping() {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)((RecyclerView.LayoutManager)this).getChildAt(i).getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }
    
    private void calculateItemBorders(final int n) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }
    
    static int[] calculateItemBorders(int[] array, final int n, int n2) {
        if (array == null || array.length != n + 1 || array[array.length - 1] != n2) {
            array = new int[n + 1];
        }
        array[0] = 0;
        final int n3 = n2 / n;
        final int n4 = n2 % n;
        int n5 = 0;
        n2 = 0;
        for (int i = 1; i <= n; ++i) {
            int n6 = n3;
            n2 += n4;
            if (n2 > 0 && n - n2 < n4) {
                ++n6;
                n2 -= n;
            }
            n5 += n6;
            array[i] = n5;
        }
        return array;
    }
    
    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }
    
    private void ensureAnchorIsInCorrectSpan(final Recycler recycler, final State state, final AnchorInfo anchorInfo, int i) {
        final boolean b = i == 1;
        i = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (b) {
            while (i > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                i = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
            return;
        }
        final int itemCount = state.getItemCount();
        final int mPosition = anchorInfo.mPosition;
        int n = i;
        int spanIndex;
        for (i = mPosition; i < itemCount - 1; ++i, n = spanIndex) {
            spanIndex = this.getSpanIndex(recycler, state, i + 1);
            if (spanIndex <= n) {
                break;
            }
        }
        anchorInfo.mPosition = i;
    }
    
    private void ensureViewSet() {
        final View[] mSet = this.mSet;
        if (mSet != null && mSet.length == this.mSpanCount) {
            return;
        }
        this.mSet = new View[this.mSpanCount];
    }
    
    private int getSpanGroupIndex(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanGroupIndex(n, this.mSpanCount);
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. ");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanIndex(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(n, this.mSpanCount);
        }
        final int value = this.mPreLayoutSpanIndexCache.get(n, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanSize(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(n);
        }
        final int value = this.mPreLayoutSpanSizeCache.get(n, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }
    
    private void guessMeasurement(final float n, final int n2) {
        this.calculateItemBorders(Math.max(Math.round(this.mSpanCount * n), n2));
    }
    
    private void measureChild(final View view, int n, final boolean b) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        final int n2 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        final int n3 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
        final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        int n4;
        if (this.mOrientation == 1) {
            n = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n3, layoutParams.width, false);
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getHeightMode(), n2, layoutParams.height, true);
        }
        else {
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n2, layoutParams.height, false);
            n = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getWidthMode(), n3, layoutParams.width, true);
        }
        this.measureChildWithDecorationsAndMargin(view, n, n4, b);
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final int n, final int n2, final boolean b) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, n, n2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, n, n2, layoutParams);
        }
        if (b2) {
            view.measure(n, n2);
        }
    }
    
    private void updateMeasurements() {
        int n;
        if (this.getOrientation() == 1) {
            n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight() - ((RecyclerView.LayoutManager)this).getPaddingLeft();
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getHeight() - ((RecyclerView.LayoutManager)this).getPaddingBottom() - ((RecyclerView.LayoutManager)this).getPaddingTop();
        }
        this.calculateItemBorders(n);
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    void collectPrefetchPositionsForLayoutState(final State state, final LayoutState layoutState, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int mCurrentPosition;
        for (int mSpanCount = this.mSpanCount, n = 0; n < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0; mSpanCount -= this.mSpanSizeLookup.getSpanSize(mCurrentPosition), layoutState.mCurrentPosition += layoutState.mItemDirection, ++n) {
            mCurrentPosition = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(mCurrentPosition, Math.max(0, layoutState.mScrollingOffset));
        }
    }
    
    @Override
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
                if (this.getSpanIndex(recycler, state, position) == 0) {
                    if (((RecyclerView.LayoutParams)child.getLayoutParams()).isItemRemoved()) {
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
            }
            i += n3;
        }
        if (view2 != null) {
            return view2;
        }
        return view;
    }
    
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
        return new LayoutParams(context, set);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    @Override
    public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    int getSpaceForSpanRange(final int n, final int n2) {
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            final int[] mCachedBorders = this.mCachedBorders;
            final int mSpanCount = this.mSpanCount;
            return mCachedBorders[mSpanCount - n] - mCachedBorders[mSpanCount - n - n2];
        }
        final int[] mCachedBorders2 = this.mCachedBorders;
        return mCachedBorders2[n + n2] - mCachedBorders2[n];
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }
    
    @Override
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final int modeInOther = this.mOrientationHelper.getModeInOther();
        final boolean b = modeInOther != 1073741824;
        int n;
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            n = this.mCachedBorders[this.mSpanCount];
        }
        else {
            n = 0;
        }
        if (b) {
            this.updateMeasurements();
        }
        final boolean b2 = layoutState.mItemDirection == 1;
        int mSpanCount = this.mSpanCount;
        int n2;
        int n3;
        if (!b2) {
            mSpanCount = this.getSpanIndex(recycler, state, layoutState.mCurrentPosition) + this.getSpanSize(recycler, state, layoutState.mCurrentPosition);
            n2 = 0;
            n3 = 0;
        }
        else {
            n2 = 0;
            n3 = 0;
        }
        while (n2 < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0) {
            final int mCurrentPosition = layoutState.mCurrentPosition;
            final int spanSize = this.getSpanSize(recycler, state, mCurrentPosition);
            if (spanSize > this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Item at position ");
                sb.append(mCurrentPosition);
                sb.append(" requires ");
                sb.append(spanSize);
                sb.append(" spans but GridLayoutManager has only ");
                sb.append(this.mSpanCount);
                sb.append(" spans.");
                throw new IllegalArgumentException(sb.toString());
            }
            mSpanCount -= spanSize;
            if (mSpanCount < 0) {
                break;
            }
            final View next = layoutState.next(recycler);
            if (next == null) {
                break;
            }
            n3 += spanSize;
            this.mSet[n2] = next;
            ++n2;
        }
        if (n2 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        this.assignSpans(recycler, state, n2, n3, b2);
        int i = 0;
        int n4 = 0;
        float n5 = 0.0f;
        while (i < n2) {
            final View view = this.mSet[i];
            if (layoutState.mScrapList == null) {
                if (b2) {
                    ((RecyclerView.LayoutManager)this).addView(view);
                }
                else {
                    ((RecyclerView.LayoutManager)this).addView(view, 0);
                }
            }
            else if (b2) {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view);
            }
            else {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view, 0);
            }
            ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mDecorInsets);
            this.measureChild(view, modeInOther, false);
            final int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
            if (decoratedMeasurement > n4) {
                n4 = decoratedMeasurement;
            }
            final float n6 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f / ((LayoutParams)view.getLayoutParams()).mSpanSize;
            if (n6 > n5) {
                n5 = n6;
            }
            ++i;
        }
        int mConsumed;
        if (b) {
            this.guessMeasurement(n5, n);
            int n7 = 0;
            for (int j = 0; j < n2; ++j) {
                final View view2 = this.mSet[j];
                this.measureChild(view2, 1073741824, true);
                final int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                if (decoratedMeasurement2 > n7) {
                    n7 = decoratedMeasurement2;
                }
            }
            mConsumed = n7;
        }
        else {
            mConsumed = n4;
        }
        for (int k = 0; k < n2; ++k) {
            final View view3 = this.mSet[k];
            if (this.mOrientationHelper.getDecoratedMeasurement(view3) != mConsumed) {
                final LayoutParams layoutParams = (LayoutParams)view3.getLayoutParams();
                final Rect mDecorInsets = layoutParams.mDecorInsets;
                final int n8 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
                final int n9 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
                final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                int n10;
                int n11;
                if (this.mOrientation == 1) {
                    n10 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n9, layoutParams.width, false);
                    n11 = View$MeasureSpec.makeMeasureSpec(mConsumed - n8, 1073741824);
                }
                else {
                    n10 = View$MeasureSpec.makeMeasureSpec(mConsumed - n9, 1073741824);
                    n11 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n8, layoutParams.height, false);
                }
                this.measureChildWithDecorationsAndMargin(view3, n10, n11, true);
            }
        }
        layoutChunkResult.mConsumed = mConsumed;
        int mOffset = 0;
        int mOffset2 = 0;
        int mOffset3 = 0;
        int mOffset4 = 0;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                mOffset4 = layoutState.mOffset;
                mOffset3 = mOffset4 - mConsumed;
            }
            else {
                mOffset3 = layoutState.mOffset;
                mOffset4 = mOffset3 + mConsumed;
            }
        }
        else if (layoutState.mLayoutDirection == -1) {
            mOffset2 = layoutState.mOffset;
            mOffset = mOffset2 - mConsumed;
        }
        else {
            mOffset = layoutState.mOffset;
            mOffset2 = mOffset + mConsumed;
        }
        int n21;
        for (int l = 0; l < n2; l = n21) {
            final View view4 = this.mSet[l];
            final LayoutParams layoutParams2 = (LayoutParams)view4.getLayoutParams();
            int n13;
            int n14;
            int n15;
            if (this.mOrientation == 1) {
                if (this.isLayoutRTL()) {
                    mOffset2 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams2.mSpanIndex];
                    final int n12 = mOffset2 - this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    n13 = mOffset3;
                    n14 = mOffset4;
                    n15 = n12;
                }
                else {
                    final int n16 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[layoutParams2.mSpanIndex];
                    final int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    final int n17 = n16;
                    final int n18 = decoratedMeasurementInOther + n16;
                    n13 = mOffset3;
                    n14 = mOffset4;
                    n15 = n17;
                    mOffset2 = n18;
                }
            }
            else {
                final int n19 = ((RecyclerView.LayoutManager)this).getPaddingTop() + this.mCachedBorders[layoutParams2.mSpanIndex];
                final int decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                final int n20 = n19;
                n14 = decoratedMeasurementInOther2 + n19;
                n15 = mOffset;
                n13 = n20;
            }
            ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(view4, n15, n13, mOffset2, n14);
            if (((RecyclerView.LayoutParams)layoutParams2).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams2).isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= view4.hasFocusable();
            n21 = l + 1;
            final int n22 = n13;
            mOffset = n15;
            mOffset4 = n14;
            mOffset3 = n22;
        }
        Arrays.fill(this.mSet, null);
    }
    
    @Override
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }
    
    @Override
    public View onFocusSearchFailed(View view, int n, final Recycler recycler, final State state) {
        final View containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(view);
        if (containingItemView == null) {
            return null;
        }
        final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
        final int mSpanIndex = layoutParams.mSpanIndex;
        final int n2 = layoutParams.mSpanIndex + layoutParams.mSpanSize;
        if (super.onFocusSearchFailed(view, n, recycler, state) == null) {
            return null;
        }
        if (this.convertFocusDirectionToLayoutDirection(n) == 1 != this.mShouldReverseLayout) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n3;
        int childCount;
        if (n != 0) {
            n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
            n3 = -1;
            childCount = -1;
        }
        else {
            n = 0;
            n3 = 1;
            childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        }
        final boolean b = this.mOrientation == 1 && this.isLayoutRTL();
        View view2 = null;
        View view3 = null;
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, n);
        int i = n;
        int mSpanIndex2 = -1;
        int n4 = 0;
        int mSpanIndex3 = -1;
        int n5 = 0;
        view = containingItemView;
        while (i != childCount) {
            n = this.getSpanGroupIndex(recycler, state, i);
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (child == view) {
                break;
            }
            Label_0583: {
                if (child.hasFocusable() && n != spanGroupIndex) {
                    if (view2 != null) {
                        break;
                    }
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)child.getLayoutParams();
                    final int mSpanIndex4 = layoutParams2.mSpanIndex;
                    final int n6 = layoutParams2.mSpanIndex + layoutParams2.mSpanSize;
                    if (child.hasFocusable() && mSpanIndex4 == mSpanIndex && n6 == n2) {
                        return child;
                    }
                    Label_0494: {
                        if (!child.hasFocusable() || view2 != null) {
                            if (child.hasFocusable() || view3 != null) {
                                n = Math.max(mSpanIndex4, mSpanIndex);
                                final int min = Math.min(n6, n2);
                                final int n7 = 0;
                                final int n8 = min - n;
                                if (child.hasFocusable()) {
                                    if (n8 > n4) {
                                        n = 1;
                                        break Label_0494;
                                    }
                                    if (n8 == n4) {
                                        if (mSpanIndex4 > mSpanIndex2) {
                                            n = 1;
                                        }
                                        else {
                                            n = 0;
                                        }
                                        if ((b ? 1 : 0) == n) {
                                            n = 1;
                                            break Label_0494;
                                        }
                                    }
                                    n = n7;
                                    break Label_0494;
                                }
                                else {
                                    if (view2 != null) {
                                        n = n7;
                                        break Label_0494;
                                    }
                                    n = 1;
                                    if (!((RecyclerView.LayoutManager)this).isViewPartiallyVisible(child, false, true)) {
                                        n = n7;
                                        break Label_0494;
                                    }
                                    if (n8 > n5) {
                                        n = 1;
                                        break Label_0494;
                                    }
                                    if (n8 == n5) {
                                        if (mSpanIndex4 <= mSpanIndex3) {
                                            n = 0;
                                        }
                                        if ((b ? 1 : 0) == n) {
                                            n = 1;
                                            break Label_0494;
                                        }
                                    }
                                    n = n7;
                                    break Label_0494;
                                }
                            }
                        }
                        n = 1;
                    }
                    final int n9 = n4;
                    if (n != 0) {
                        if (child.hasFocusable()) {
                            mSpanIndex2 = layoutParams2.mSpanIndex;
                            n = Math.min(n6, n2) - Math.max(mSpanIndex4, mSpanIndex);
                            view2 = child;
                            break Label_0583;
                        }
                        mSpanIndex3 = layoutParams2.mSpanIndex;
                        n = Math.min(n6, n2);
                        final int max = Math.max(mSpanIndex4, mSpanIndex);
                        view3 = child;
                        n5 = n - max;
                        n = n9;
                        break Label_0583;
                    }
                }
                n = n4;
            }
            i += n3;
            n4 = n;
        }
        if (view2 != null) {
            return view2;
        }
        return view3;
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, ((RecyclerView.LayoutParams)layoutParams2).getViewLayoutPosition());
        if (this.mOrientation == 0) {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
            return;
        }
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, n, n2);
        }
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            final int[] mCachedBorders = this.mCachedBorders;
            n = RecyclerView.LayoutManager.chooseSize(n, mCachedBorders[mCachedBorders.length - 1] + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            final int[] mCachedBorders2 = this.mCachedBorders;
            n2 = RecyclerView.LayoutManager.chooseSize(n2, mCachedBorders2[mCachedBorders2.length - 1] + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setSpanCount(final int mSpanCount) {
        if (mSpanCount == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (mSpanCount >= 1) {
            this.mSpanCount = mSpanCount;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            ((RecyclerView.LayoutManager)this).requestLayout();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Span count should be at least 1. Provided ");
        sb.append(mSpanCount);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setSpanSizeLookup(final SpanSizeLookup mSpanSizeLookup) {
        this.mSpanSizeLookup = mSpanSizeLookup;
    }
    
    @Override
    public void setStackFromEnd(final boolean b) {
        if (!b) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }
    
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup
    {
        @Override
        public int getSpanIndex(final int n, final int n2) {
            return n % n2;
        }
        
        @Override
        public int getSpanSize(final int n) {
            return 1;
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex;
        int mSpanSize;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public int getSpanIndex() {
            return this.mSpanIndex;
        }
        
        public int getSpanSize() {
            return this.mSpanSize;
        }
    }
    
    public abstract static class SpanSizeLookup
    {
        private boolean mCacheSpanIndices;
        final SparseIntArray mSpanIndexCache;
        
        public SpanSizeLookup() {
            this.mSpanIndexCache = new SparseIntArray();
            this.mCacheSpanIndices = false;
        }
        
        int findReferenceIndexFromCache(int n) {
            int i = 0;
            int n2 = this.mSpanIndexCache.size() - 1;
            while (i <= n2) {
                final int n3 = i + n2 >>> 1;
                if (this.mSpanIndexCache.keyAt(n3) < n) {
                    i = n3 + 1;
                }
                else {
                    n2 = n3 - 1;
                }
            }
            n = i - 1;
            if (n >= 0 && n < this.mSpanIndexCache.size()) {
                return this.mSpanIndexCache.keyAt(n);
            }
            return -1;
        }
        
        int getCachedSpanIndex(final int n, int spanIndex) {
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, spanIndex);
            }
            final int value = this.mSpanIndexCache.get(n, -1);
            if (value != -1) {
                return value;
            }
            spanIndex = this.getSpanIndex(n, spanIndex);
            this.mSpanIndexCache.put(n, spanIndex);
            return spanIndex;
        }
        
        public int getSpanGroupIndex(final int n, final int n2) {
            int n3 = 0;
            int n4 = 0;
            final int spanSize = this.getSpanSize(n);
            for (int i = 0; i < n; ++i) {
                final int spanSize2 = this.getSpanSize(i);
                n3 += spanSize2;
                if (n3 == n2) {
                    n3 = 0;
                    ++n4;
                }
                else if (n3 > n2) {
                    n3 = spanSize2;
                    ++n4;
                }
            }
            if (n3 + spanSize > n2) {
                return n4 + 1;
            }
            return n4;
        }
        
        public int getSpanIndex(final int n, final int n2) {
            final int spanSize = this.getSpanSize(n);
            if (spanSize == n2) {
                return 0;
            }
            int n3 = 0;
            int i = 0;
            if (this.mCacheSpanIndices && this.mSpanIndexCache.size() > 0) {
                final int referenceIndexFromCache = this.findReferenceIndexFromCache(n);
                if (referenceIndexFromCache >= 0) {
                    n3 = this.mSpanIndexCache.get(referenceIndexFromCache) + this.getSpanSize(referenceIndexFromCache);
                    i = referenceIndexFromCache + 1;
                }
            }
            while (i < n) {
                final int spanSize2 = this.getSpanSize(i);
                n3 += spanSize2;
                if (n3 == n2) {
                    n3 = 0;
                }
                else if (n3 > n2) {
                    n3 = spanSize2;
                }
                ++i;
            }
            if (n3 + spanSize <= n2) {
                return n3;
            }
            return 0;
        }
        
        public abstract int getSpanSize(final int p0);
        
        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }
        
        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }
        
        public void setSpanIndexCacheEnabled(final boolean mCacheSpanIndices) {
            this.mCacheSpanIndices = mCacheSpanIndices;
        }
    }
}
