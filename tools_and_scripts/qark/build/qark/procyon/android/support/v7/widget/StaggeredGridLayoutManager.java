// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.annotation.RestrictTo;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.support.annotation.Nullable;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.graphics.PointF;
import java.util.Arrays;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import java.util.BitSet;
import android.support.annotation.NonNull;

public class StaggeredGridLayoutManager extends LayoutManager implements ScrollVectorProvider
{
    static final boolean DEBUG = false;
    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "StaggeredGridLManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy;
    private boolean mLaidOutInvalidFullSpan;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    @NonNull
    private final android.support.v7.widget.LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup;
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private int[] mPrefetchDistances;
    @NonNull
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    boolean mReverseLayout;
    @NonNull
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled;
    private int mSpanCount;
    Span[] mSpans;
    private final Rect mTmpRect;
    
    public StaggeredGridLayoutManager(final int spanCount, final int mOrientation) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        boolean autoMeasureEnabled = true;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            @Override
            public void run() {
                StaggeredGridLayoutManager.this.checkForGaps();
            }
        };
        this.mOrientation = mOrientation;
        this.setSpanCount(spanCount);
        if (this.mGapStrategy == 0) {
            autoMeasureEnabled = false;
        }
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(autoMeasureEnabled);
        this.mLayoutState = new android.support.v7.widget.LayoutState();
        this.createOrientationHelpers();
    }
    
    public StaggeredGridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        boolean autoMeasureEnabled = true;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            @Override
            public void run() {
                StaggeredGridLayoutManager.this.checkForGaps();
            }
        };
        final Properties properties = RecyclerView.LayoutManager.getProperties(context, set, n, n2);
        this.setOrientation(properties.orientation);
        this.setSpanCount(properties.spanCount);
        this.setReverseLayout(properties.reverseLayout);
        if (this.mGapStrategy == 0) {
            autoMeasureEnabled = false;
        }
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(autoMeasureEnabled);
        this.mLayoutState = new android.support.v7.widget.LayoutState();
        this.createOrientationHelpers();
    }
    
    private void appendViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].appendToSpan(view);
        }
    }
    
    private void applyPendingSavedState(final AnchorInfo anchorInfo) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
            if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    this.mSpans[i].clear();
                    int line = this.mPendingSavedState.mSpanOffsets[i];
                    if (line != Integer.MIN_VALUE) {
                        if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
                            line += this.mPrimaryOrientation.getEndAfterPadding();
                        }
                        else {
                            line += this.mPrimaryOrientation.getStartAfterPadding();
                        }
                    }
                    this.mSpans[i].setLine(line);
                }
            }
            else {
                this.mPendingSavedState.invalidateSpanInfo();
                final SavedState mPendingSavedState = this.mPendingSavedState;
                mPendingSavedState.mAnchorPosition = mPendingSavedState.mVisibleAnchorPosition;
            }
        }
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
        this.resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition != -1) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        }
        else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        }
        if (this.mPendingSavedState.mSpanLookupSize > 1) {
            this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
            this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        }
    }
    
    private void attachViewToSpans(final View view, final LayoutParams layoutParams, final android.support.v7.widget.LayoutState layoutState) {
        if (layoutState.mLayoutDirection == 1) {
            if (layoutParams.mFullSpan) {
                this.appendViewToAllSpans(view);
                return;
            }
            layoutParams.mSpan.appendToSpan(view);
        }
        else {
            if (layoutParams.mFullSpan) {
                this.prependViewToAllSpans(view);
                return;
            }
            layoutParams.mSpan.prependToSpan(view);
        }
    }
    
    private int calculateScrollDirectionForPosition(int n) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        final int n2 = -1;
        if (childCount == 0) {
            n = n2;
            if (this.mShouldReverseLayout) {
                n = 1;
            }
            return n;
        }
        if (n < this.getFirstChildPosition() != this.mShouldReverseLayout) {
            return -1;
        }
        return 1;
    }
    
    private boolean checkSpanForGap(final Span span) {
        if (this.mShouldReverseLayout) {
            return span.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding() && (span.getLayoutParams(span.mViews.get(span.mViews.size() - 1)).mFullSpan ^ true);
        }
        return span.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding() && (span.getLayoutParams(span.mViews.get(0)).mFullSpan ^ true);
    }
    
    private int computeScrollExtent(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollExtent(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int computeScrollOffset(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollOffset(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }
    
    private int computeScrollRange(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollRange(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int convertFocusDirectionToLayoutDirection(final int n) {
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
    
    private FullSpanItem createFullSpanItemFromEnd(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = n - this.mSpans[i].getEndLine(n);
        }
        return fullSpanItem;
    }
    
    private FullSpanItem createFullSpanItemFromStart(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = this.mSpans[i].getStartLine(n) - n;
        }
        return fullSpanItem;
    }
    
    private void createOrientationHelpers() {
        this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
    }
    
    private int fill(final Recycler recycler, final android.support.v7.widget.LayoutState layoutState, final State state) {
        this.mRemainingSpans.set(0, this.mSpanCount, true);
        int n;
        if (this.mLayoutState.mInfinite) {
            if (layoutState.mLayoutDirection == 1) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = Integer.MIN_VALUE;
            }
        }
        else if (layoutState.mLayoutDirection == 1) {
            n = layoutState.mEndLine + layoutState.mAvailable;
        }
        else {
            n = layoutState.mStartLine - layoutState.mAvailable;
        }
        this.updateAllRemainingSpans(layoutState.mLayoutDirection, n);
        int n2;
        if (this.mShouldReverseLayout) {
            n2 = this.mPrimaryOrientation.getEndAfterPadding();
        }
        else {
            n2 = this.mPrimaryOrientation.getStartAfterPadding();
        }
        boolean b = false;
        while (layoutState.hasMore(state) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty())) {
            final View next = layoutState.next(recycler);
            final LayoutParams layoutParams = (LayoutParams)next.getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            final int span = this.mLazySpanLookup.getSpan(viewLayoutPosition);
            final boolean b2 = span == -1;
            Span nextSpan;
            if (b2) {
                if (layoutParams.mFullSpan) {
                    nextSpan = this.mSpans[0];
                }
                else {
                    nextSpan = this.getNextSpan(layoutState);
                }
                this.mLazySpanLookup.setSpan(viewLayoutPosition, nextSpan);
            }
            else {
                nextSpan = this.mSpans[span];
            }
            layoutParams.mSpan = nextSpan;
            if (layoutState.mLayoutDirection == 1) {
                ((RecyclerView.LayoutManager)this).addView(next);
            }
            else {
                ((RecyclerView.LayoutManager)this).addView(next, 0);
            }
            this.measureChildWithDecorationsAndMargin(next, layoutParams, false);
            int n4;
            int n5;
            if (layoutState.mLayoutDirection == 1) {
                int n3;
                if (layoutParams.mFullSpan) {
                    n3 = this.getMaxEnd(n2);
                }
                else {
                    n3 = nextSpan.getEndLine(n2);
                }
                final int decoratedMeasurement = this.mPrimaryOrientation.getDecoratedMeasurement(next);
                if (b2 && layoutParams.mFullSpan) {
                    final FullSpanItem fullSpanItemFromEnd = this.createFullSpanItemFromEnd(n3);
                    fullSpanItemFromEnd.mGapDir = -1;
                    fullSpanItemFromEnd.mPosition = viewLayoutPosition;
                    this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromEnd);
                }
                n4 = n3;
                n5 = decoratedMeasurement + n3;
            }
            else {
                int n6;
                if (layoutParams.mFullSpan) {
                    n6 = this.getMinStart(n2);
                }
                else {
                    n6 = nextSpan.getStartLine(n2);
                }
                final int decoratedMeasurement2 = this.mPrimaryOrientation.getDecoratedMeasurement(next);
                if (b2 && layoutParams.mFullSpan) {
                    final FullSpanItem fullSpanItemFromStart = this.createFullSpanItemFromStart(n6);
                    fullSpanItemFromStart.mGapDir = 1;
                    fullSpanItemFromStart.mPosition = viewLayoutPosition;
                    this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromStart);
                }
                n5 = n6;
                n4 = n6 - decoratedMeasurement2;
            }
            if (layoutParams.mFullSpan && layoutState.mItemDirection == -1) {
                if (b2) {
                    this.mLaidOutInvalidFullSpan = true;
                }
                else {
                    boolean b3;
                    if (layoutState.mLayoutDirection == 1) {
                        b3 = (this.areAllEndsEqual() ^ true);
                    }
                    else {
                        b3 = (this.areAllStartsEqual() ^ true);
                    }
                    if (b3) {
                        final FullSpanItem fullSpanItem = this.mLazySpanLookup.getFullSpanItem(viewLayoutPosition);
                        if (fullSpanItem != null) {
                            fullSpanItem.mHasUnwantedGapAfter = true;
                        }
                        this.mLaidOutInvalidFullSpan = true;
                    }
                }
            }
            this.attachViewToSpans(next, layoutParams, layoutState);
            int n8;
            int n9;
            if (this.isLayoutRTL() && this.mOrientation == 1) {
                int endAfterPadding;
                if (layoutParams.mFullSpan) {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding();
                }
                else {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - nextSpan.mIndex) * this.mSizePerSpan;
                }
                final int decoratedMeasurement3 = this.mSecondaryOrientation.getDecoratedMeasurement(next);
                final int n7 = endAfterPadding;
                n8 = endAfterPadding - decoratedMeasurement3;
                n9 = n7;
            }
            else {
                int startAfterPadding;
                if (layoutParams.mFullSpan) {
                    startAfterPadding = this.mSecondaryOrientation.getStartAfterPadding();
                }
                else {
                    startAfterPadding = nextSpan.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
                }
                final int decoratedMeasurement4 = this.mSecondaryOrientation.getDecoratedMeasurement(next);
                final int n10 = startAfterPadding;
                n9 = decoratedMeasurement4 + startAfterPadding;
                n8 = n10;
            }
            if (this.mOrientation == 1) {
                ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, n8, n4, n9, n5);
            }
            else {
                ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, n4, n8, n5, n9);
            }
            if (layoutParams.mFullSpan) {
                this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, n);
            }
            else {
                this.updateRemainingSpans(nextSpan, this.mLayoutState.mLayoutDirection, n);
            }
            this.recycle(recycler, this.mLayoutState);
            if (this.mLayoutState.mStopInFocusable && next.hasFocusable()) {
                if (layoutParams.mFullSpan) {
                    this.mRemainingSpans.clear();
                }
                else {
                    this.mRemainingSpans.set(nextSpan.mIndex, false);
                }
            }
            b = true;
        }
        final boolean b4 = false;
        if (!b) {
            this.recycle(recycler, this.mLayoutState);
        }
        int n11;
        if (this.mLayoutState.mLayoutDirection == -1) {
            n11 = this.mPrimaryOrientation.getStartAfterPadding() - this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
        }
        else {
            n11 = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
        }
        int min = b4 ? 1 : 0;
        if (n11 > 0) {
            min = Math.min(layoutState.mAvailable, n11);
        }
        return min;
    }
    
    private int findFirstReferenceChildPosition(final int n) {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                return position;
            }
        }
        return 0;
    }
    
    private int findLastReferenceChildPosition(final int n) {
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                return position;
            }
        }
        return 0;
    }
    
    private void fixEndGap(final Recycler recycler, final State state, final boolean b) {
        final int maxEnd = this.getMaxEnd(Integer.MIN_VALUE);
        if (maxEnd == Integer.MIN_VALUE) {
            return;
        }
        final int n = this.mPrimaryOrientation.getEndAfterPadding() - maxEnd;
        if (n <= 0) {
            return;
        }
        final int n2 = n - -this.scrollBy(-n, recycler, state);
        if (b && n2 > 0) {
            this.mPrimaryOrientation.offsetChildren(n2);
        }
    }
    
    private void fixStartGap(final Recycler recycler, final State state, final boolean b) {
        final int minStart = this.getMinStart(Integer.MAX_VALUE);
        if (minStart == Integer.MAX_VALUE) {
            return;
        }
        final int n = minStart - this.mPrimaryOrientation.getStartAfterPadding();
        if (n <= 0) {
            return;
        }
        final int n2 = n - this.scrollBy(n, recycler, state);
        if (b && n2 > 0) {
            this.mPrimaryOrientation.offsetChildren(-n2);
        }
    }
    
    private int getMaxEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 > endLine) {
                endLine = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMaxStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 > startLine) {
                startLine = startLine2;
            }
        }
        return startLine;
    }
    
    private int getMinEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 < endLine) {
                endLine = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMinStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 < startLine) {
                startLine = startLine2;
            }
        }
        return startLine;
    }
    
    private Span getNextSpan(final android.support.v7.widget.LayoutState layoutState) {
        int i;
        int mSpanCount;
        int n;
        if (this.preferLastSpan(layoutState.mLayoutDirection)) {
            i = this.mSpanCount - 1;
            mSpanCount = -1;
            n = -1;
        }
        else {
            i = 0;
            mSpanCount = this.mSpanCount;
            n = 1;
        }
        if (layoutState.mLayoutDirection == 1) {
            Span span = null;
            int n2 = Integer.MAX_VALUE;
            final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
            while (i != mSpanCount) {
                final Span span2 = this.mSpans[i];
                final int endLine = span2.getEndLine(startAfterPadding);
                if (endLine < n2) {
                    span = span2;
                    n2 = endLine;
                }
                i += n;
            }
            return span;
        }
        Span span3 = null;
        int n3 = Integer.MIN_VALUE;
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        while (i != mSpanCount) {
            final Span span4 = this.mSpans[i];
            final int startLine = span4.getStartLine(endAfterPadding);
            if (startLine > n3) {
                span3 = span4;
                n3 = startLine;
            }
            i += n;
        }
        return span3;
    }
    
    private void handleUpdate(int n, final int n2, final int n3) {
        int n4;
        if (this.mShouldReverseLayout) {
            n4 = this.getLastChildPosition();
        }
        else {
            n4 = this.getFirstChildPosition();
        }
        int n5;
        int n6;
        if (n3 == 8) {
            if (n < n2) {
                n5 = n2 + 1;
                n6 = n;
            }
            else {
                n5 = n + 1;
                n6 = n2;
            }
        }
        else {
            n6 = n;
            n5 = n + n2;
        }
        this.mLazySpanLookup.invalidateAfter(n6);
        if (n3 != 8) {
            switch (n3) {
                case 2: {
                    this.mLazySpanLookup.offsetForRemoval(n, n2);
                    break;
                }
                case 1: {
                    this.mLazySpanLookup.offsetForAddition(n, n2);
                    break;
                }
            }
        }
        else {
            this.mLazySpanLookup.offsetForRemoval(n, 1);
            this.mLazySpanLookup.offsetForAddition(n2, 1);
        }
        if (n5 <= n4) {
            return;
        }
        if (this.mShouldReverseLayout) {
            n = this.getFirstChildPosition();
        }
        else {
            n = this.getLastChildPosition();
        }
        if (n6 <= n) {
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, int updateSpecWithExtra, int updateSpecWithExtra2, final boolean b) {
        ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mTmpRect);
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        updateSpecWithExtra = this.updateSpecWithExtra(updateSpecWithExtra, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
        updateSpecWithExtra2 = this.updateSpecWithExtra(updateSpecWithExtra2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        if (b2) {
            view.measure(updateSpecWithExtra, updateSpecWithExtra2);
        }
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final LayoutParams layoutParams, final boolean b) {
        if (layoutParams.mFullSpan) {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, this.mFullSizeSpec, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, true), b);
                return;
            }
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, true), this.mFullSizeSpec, b);
        }
        else {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, false), RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, true), b);
                return;
            }
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, true), RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, false), b);
        }
    }
    
    private void onLayoutChildren(final Recycler recycler, final State state, final boolean b) {
        final AnchorInfo mAnchorInfo = this.mAnchorInfo;
        if (this.mPendingSavedState != null || this.mPendingScrollPosition != -1) {
            if (state.getItemCount() == 0) {
                ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
                mAnchorInfo.reset();
                return;
            }
        }
        final boolean mValid = mAnchorInfo.mValid;
        final boolean b2 = true;
        final boolean b3 = !mValid || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null;
        if (b3) {
            mAnchorInfo.reset();
            if (this.mPendingSavedState != null) {
                this.applyPendingSavedState(mAnchorInfo);
            }
            else {
                this.resolveShouldLayoutReverse();
                mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
            }
            this.updateAnchorInfoForLayout(state, mAnchorInfo);
            mAnchorInfo.mValid = true;
        }
        if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1) {
            if (mAnchorInfo.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL) {
                this.mLazySpanLookup.clear();
                mAnchorInfo.mInvalidateOffsets = true;
            }
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final SavedState mPendingSavedState = this.mPendingSavedState;
            if (mPendingSavedState == null || mPendingSavedState.mSpanOffsetsSize < 1) {
                if (mAnchorInfo.mInvalidateOffsets) {
                    for (int i = 0; i < this.mSpanCount; ++i) {
                        this.mSpans[i].clear();
                        if (mAnchorInfo.mOffset != Integer.MIN_VALUE) {
                            this.mSpans[i].setLine(mAnchorInfo.mOffset);
                        }
                    }
                }
                else if (!b3 && this.mAnchorInfo.mSpanReferenceLines != null) {
                    for (int j = 0; j < this.mSpanCount; ++j) {
                        final Span span = this.mSpans[j];
                        span.clear();
                        span.setLine(this.mAnchorInfo.mSpanReferenceLines[j]);
                    }
                }
                else {
                    for (int k = 0; k < this.mSpanCount; ++k) {
                        this.mSpans[k].cacheReferenceLineAndClear(this.mShouldReverseLayout, mAnchorInfo.mOffset);
                    }
                    this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
                }
            }
        }
        ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mRecycle = false;
        this.mLaidOutInvalidFullSpan = false;
        this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
        this.updateLayoutState(mAnchorInfo.mPosition, state);
        if (mAnchorInfo.mLayoutFromEnd) {
            this.setLayoutStateDirection(-1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(1);
            this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        }
        else {
            this.setLayoutStateDirection(1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(-1);
            this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        }
        this.repositionToWrapContentIfNecessary();
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            if (this.mShouldReverseLayout) {
                this.fixEndGap(recycler, state, true);
                this.fixStartGap(recycler, state, false);
            }
            else {
                this.fixStartGap(recycler, state, true);
                this.fixEndGap(recycler, state, false);
            }
        }
        final boolean b4 = false;
        boolean b6;
        if (b && !state.isPreLayout()) {
            boolean b5 = false;
            Label_0691: {
                if (this.mGapStrategy != 0) {
                    if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                        if (this.mLaidOutInvalidFullSpan || this.hasGapsToFix() != null) {
                            b5 = b2;
                            break Label_0691;
                        }
                    }
                }
                b5 = false;
            }
            if (b5) {
                ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
                b6 = (this.checkForGaps() || b4);
            }
            else {
                b6 = b4;
            }
        }
        else {
            b6 = b4;
        }
        if (state.isPreLayout()) {
            this.mAnchorInfo.reset();
        }
        this.mLastLayoutFromEnd = mAnchorInfo.mLayoutFromEnd;
        this.mLastLayoutRTL = this.isLayoutRTL();
        if (b6) {
            this.mAnchorInfo.reset();
            this.onLayoutChildren(recycler, state, false);
        }
    }
    
    private boolean preferLastSpan(final int n) {
        if (this.mOrientation == 0) {
            return n == -1 != this.mShouldReverseLayout;
        }
        return n == -1 == this.mShouldReverseLayout == this.isLayoutRTL();
    }
    
    private void prependViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].prependToSpan(view);
        }
    }
    
    private void recycle(final Recycler recycler, final android.support.v7.widget.LayoutState layoutState) {
        if (!layoutState.mRecycle) {
            return;
        }
        if (layoutState.mInfinite) {
            return;
        }
        if (layoutState.mAvailable == 0) {
            if (layoutState.mLayoutDirection == -1) {
                this.recycleFromEnd(recycler, layoutState.mEndLine);
                return;
            }
            this.recycleFromStart(recycler, layoutState.mStartLine);
        }
        else {
            if (layoutState.mLayoutDirection == -1) {
                final int n = layoutState.mStartLine - this.getMaxStart(layoutState.mStartLine);
                int mEndLine;
                if (n < 0) {
                    mEndLine = layoutState.mEndLine;
                }
                else {
                    mEndLine = layoutState.mEndLine - Math.min(n, layoutState.mAvailable);
                }
                this.recycleFromEnd(recycler, mEndLine);
                return;
            }
            final int n2 = this.getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
            int mStartLine;
            if (n2 < 0) {
                mStartLine = layoutState.mStartLine;
            }
            else {
                mStartLine = layoutState.mStartLine + Math.min(n2, layoutState.mAvailable);
            }
            this.recycleFromStart(recycler, mStartLine);
        }
    }
    
    private void recycleFromEnd(final Recycler recycler, final int n) {
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (this.mPrimaryOrientation.getDecoratedStart(child) < n) {
                return;
            }
            if (this.mPrimaryOrientation.getTransformedStartWithDecoration(child) < n) {
                return;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                for (int j = 0; j < this.mSpanCount; ++j) {
                    if (this.mSpans[j].mViews.size() == 1) {
                        return;
                    }
                }
                for (int k = 0; k < this.mSpanCount; ++k) {
                    this.mSpans[k].popEnd();
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    return;
                }
                layoutParams.mSpan.popEnd();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void recycleFromStart(final Recycler recycler, final int n) {
        while (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(0);
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > n) {
                return;
            }
            if (this.mPrimaryOrientation.getTransformedEndWithDecoration(child) > n) {
                return;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    if (this.mSpans[i].mViews.size() == 1) {
                        return;
                    }
                }
                for (int j = 0; j < this.mSpanCount; ++j) {
                    this.mSpans[j].popStart();
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    return;
                }
                layoutParams.mSpan.popStart();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void repositionToWrapContentIfNecessary() {
        if (this.mSecondaryOrientation.getMode() == 1073741824) {
            return;
        }
        float max = 0.0f;
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            float n = (float)this.mSecondaryOrientation.getDecoratedMeasurement(child);
            if (n >= max) {
                if (((LayoutParams)child.getLayoutParams()).isFullSpan()) {
                    n = 1.0f * n / this.mSpanCount;
                }
                max = Math.max(max, n);
            }
        }
        final int mSizePerSpan = this.mSizePerSpan;
        int n2 = Math.round(this.mSpanCount * max);
        if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
            n2 = Math.min(n2, this.mSecondaryOrientation.getTotalSpace());
        }
        this.updateMeasureSpecs(n2);
        if (this.mSizePerSpan == mSizePerSpan) {
            return;
        }
        for (int j = 0; j < childCount; ++j) {
            final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
            final LayoutParams layoutParams = (LayoutParams)child2.getLayoutParams();
            if (!layoutParams.mFullSpan) {
                if (this.isLayoutRTL() && this.mOrientation == 1) {
                    child2.offsetLeftAndRight(-(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * mSizePerSpan);
                }
                else {
                    final int n3 = layoutParams.mSpan.mIndex * this.mSizePerSpan;
                    final int n4 = layoutParams.mSpan.mIndex * mSizePerSpan;
                    if (this.mOrientation == 1) {
                        child2.offsetLeftAndRight(n3 - n4);
                    }
                    else {
                        child2.offsetTopAndBottom(n3 - n4);
                    }
                }
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
    
    private void setLayoutStateDirection(int n) {
        final android.support.v7.widget.LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mLayoutDirection = n;
        final boolean mShouldReverseLayout = this.mShouldReverseLayout;
        final int n2 = 1;
        if (mShouldReverseLayout == (n == -1)) {
            n = n2;
        }
        else {
            n = -1;
        }
        mLayoutState.mItemDirection = n;
    }
    
    private void updateAllRemainingSpans(final int n, final int n2) {
        for (int i = 0; i < this.mSpanCount; ++i) {
            if (!this.mSpans[i].mViews.isEmpty()) {
                this.updateRemainingSpans(this.mSpans[i], n, n2);
            }
        }
    }
    
    private boolean updateAnchorFromChildren(final State state, final AnchorInfo anchorInfo) {
        int mPosition;
        if (this.mLastLayoutFromEnd) {
            mPosition = this.findLastReferenceChildPosition(state.getItemCount());
        }
        else {
            mPosition = this.findFirstReferenceChildPosition(state.getItemCount());
        }
        anchorInfo.mPosition = mPosition;
        anchorInfo.mOffset = Integer.MIN_VALUE;
        return true;
    }
    
    private void updateLayoutState(int totalSpace, final State state) {
        final android.support.v7.widget.LayoutState mLayoutState = this.mLayoutState;
        final boolean b = false;
        mLayoutState.mAvailable = 0;
        mLayoutState.mCurrentPosition = totalSpace;
        final int n = 0;
        int totalSpace2 = 0;
        if (((RecyclerView.LayoutManager)this).isSmoothScrolling()) {
            final int targetScrollPosition = state.getTargetScrollPosition();
            if (targetScrollPosition != -1) {
                if (this.mShouldReverseLayout == targetScrollPosition < totalSpace) {
                    totalSpace2 = this.mPrimaryOrientation.getTotalSpace();
                    totalSpace = n;
                }
                else {
                    totalSpace = this.mPrimaryOrientation.getTotalSpace();
                }
            }
            else {
                totalSpace = n;
            }
        }
        else {
            totalSpace = n;
        }
        if (((RecyclerView.LayoutManager)this).getClipToPadding()) {
            this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - totalSpace;
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + totalSpace2;
        }
        else {
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + totalSpace2;
            this.mLayoutState.mStartLine = -totalSpace;
        }
        final android.support.v7.widget.LayoutState mLayoutState2 = this.mLayoutState;
        mLayoutState2.mStopInFocusable = false;
        mLayoutState2.mRecycle = true;
        mLayoutState2.mInfinite = ((this.mPrimaryOrientation.getMode() == 0 && this.mPrimaryOrientation.getEnd() == 0) || b);
    }
    
    private void updateRemainingSpans(final Span span, final int n, final int n2) {
        final int deletedSize = span.getDeletedSize();
        if (n == -1) {
            if (span.getStartLine() + deletedSize <= n2) {
                this.mRemainingSpans.set(span.mIndex, false);
            }
            return;
        }
        if (span.getEndLine() - deletedSize >= n2) {
            this.mRemainingSpans.set(span.mIndex, false);
        }
    }
    
    private int updateSpecWithExtra(final int n, final int n2, final int n3) {
        if (n2 == 0 && n3 == 0) {
            return n;
        }
        final int mode = View$MeasureSpec.getMode(n);
        if (mode != Integer.MIN_VALUE && mode != 1073741824) {
            return n;
        }
        return View$MeasureSpec.makeMeasureSpec(Math.max(0, View$MeasureSpec.getSize(n) - n2 - n3), mode);
    }
    
    boolean areAllEndsEqual() {
        final int endLine = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getEndLine(Integer.MIN_VALUE) != endLine) {
                return false;
            }
        }
        return true;
    }
    
    boolean areAllStartsEqual() {
        final int startLine = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getStartLine(Integer.MIN_VALUE) != startLine) {
                return false;
            }
        }
        return true;
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
    
    boolean checkForGaps() {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0 || this.mGapStrategy == 0) {
            return false;
        }
        if (!((RecyclerView.LayoutManager)this).isAttachedToWindow()) {
            return false;
        }
        int n;
        int n2;
        if (this.mShouldReverseLayout) {
            n = this.getLastChildPosition();
            n2 = this.getFirstChildPosition();
        }
        else {
            n = this.getFirstChildPosition();
            n2 = this.getLastChildPosition();
        }
        if (n == 0 && this.hasGapsToFix() != null) {
            this.mLazySpanLookup.clear();
            ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
            ((RecyclerView.LayoutManager)this).requestLayout();
            return true;
        }
        if (!this.mLaidOutInvalidFullSpan) {
            return false;
        }
        int n3;
        if (this.mShouldReverseLayout) {
            n3 = -1;
        }
        else {
            n3 = 1;
        }
        final FullSpanItem firstFullSpanItemInRange = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, n2 + 1, n3, true);
        if (firstFullSpanItemInRange == null) {
            this.mLaidOutInvalidFullSpan = false;
            this.mLazySpanLookup.forceInvalidateAfter(n2 + 1);
            return false;
        }
        final FullSpanItem firstFullSpanItemInRange2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, firstFullSpanItemInRange.mPosition, n3 * -1, true);
        if (firstFullSpanItemInRange2 == null) {
            this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange.mPosition);
        }
        else {
            this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange2.mPosition + 1);
        }
        ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
        ((RecyclerView.LayoutManager)this).requestLayout();
        return true;
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    public void collectAdjacentPrefetchPositions(int n, int i, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        if (this.mOrientation != 0) {
            n = i;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return;
        }
        if (n == 0) {
            return;
        }
        this.prepareLayoutStateForDelta(n, state);
        final int[] mPrefetchDistances = this.mPrefetchDistances;
        if (mPrefetchDistances == null || mPrefetchDistances.length < this.mSpanCount) {
            this.mPrefetchDistances = new int[this.mSpanCount];
        }
        n = 0;
        int n2;
        for (i = 0; i < this.mSpanCount; ++i) {
            if (this.mLayoutState.mItemDirection == -1) {
                n2 = this.mLayoutState.mStartLine - this.mSpans[i].getStartLine(this.mLayoutState.mStartLine);
            }
            else {
                n2 = this.mSpans[i].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
            }
            if (n2 >= 0) {
                this.mPrefetchDistances[n] = n2;
                ++n;
            }
        }
        Arrays.sort(this.mPrefetchDistances, 0, n);
        android.support.v7.widget.LayoutState mLayoutState;
        for (i = 0; i < n && this.mLayoutState.hasMore(state); ++i) {
            layoutPrefetchRegistry.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[i]);
            mLayoutState = this.mLayoutState;
            mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
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
    public PointF computeScrollVectorForPosition(int calculateScrollDirectionForPosition) {
        calculateScrollDirectionForPosition = this.calculateScrollDirectionForPosition(calculateScrollDirectionForPosition);
        final PointF pointF = new PointF();
        if (calculateScrollDirectionForPosition == 0) {
            return null;
        }
        if (this.mOrientation == 0) {
            pointF.x = (float)calculateScrollDirectionForPosition;
            pointF.y = 0.0f;
            return pointF;
        }
        pointF.x = 0.0f;
        pointF.y = (float)calculateScrollDirectionForPosition;
        return pointF;
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
    
    public int[] findFirstCompletelyVisibleItemPositions(int[] array) {
        if (array == null) {
            array = new int[this.mSpanCount];
        }
        else if (array.length < this.mSpanCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            sb.append(this.mSpanCount);
            sb.append(", array size:");
            sb.append(array.length);
            throw new IllegalArgumentException(sb.toString());
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array[i] = this.mSpans[i].findFirstCompletelyVisibleItemPosition();
        }
        return array;
    }
    
    View findFirstVisibleItemClosestToEnd(final boolean b) {
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        View view = null;
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
            if (decoratedEnd > startAfterPadding) {
                if (decoratedStart < endAfterPadding) {
                    if (decoratedEnd <= endAfterPadding) {
                        return child;
                    }
                    if (!b) {
                        return child;
                    }
                    if (view == null) {
                        view = child;
                    }
                }
            }
        }
        return view;
    }
    
    View findFirstVisibleItemClosestToStart(final boolean b) {
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        View view = null;
        for (int i = 0; i < childCount; ++i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > startAfterPadding) {
                if (decoratedStart < endAfterPadding) {
                    if (decoratedStart >= startAfterPadding) {
                        return child;
                    }
                    if (!b) {
                        return child;
                    }
                    if (view == null) {
                        view = child;
                    }
                }
            }
        }
        return view;
    }
    
    int findFirstVisibleItemPositionInt() {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findFirstVisibleItemClosestToEnd(true);
        }
        else {
            view = this.findFirstVisibleItemClosestToStart(true);
        }
        if (view == null) {
            return -1;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(view);
    }
    
    public int[] findFirstVisibleItemPositions(int[] array) {
        if (array == null) {
            array = new int[this.mSpanCount];
        }
        else if (array.length < this.mSpanCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            sb.append(this.mSpanCount);
            sb.append(", array size:");
            sb.append(array.length);
            throw new IllegalArgumentException(sb.toString());
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array[i] = this.mSpans[i].findFirstVisibleItemPosition();
        }
        return array;
    }
    
    public int[] findLastCompletelyVisibleItemPositions(int[] array) {
        if (array == null) {
            array = new int[this.mSpanCount];
        }
        else if (array.length < this.mSpanCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            sb.append(this.mSpanCount);
            sb.append(", array size:");
            sb.append(array.length);
            throw new IllegalArgumentException(sb.toString());
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array[i] = this.mSpans[i].findLastCompletelyVisibleItemPosition();
        }
        return array;
    }
    
    public int[] findLastVisibleItemPositions(int[] array) {
        if (array == null) {
            array = new int[this.mSpanCount];
        }
        else if (array.length < this.mSpanCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            sb.append(this.mSpanCount);
            sb.append(", array size:");
            sb.append(array.length);
            throw new IllegalArgumentException(sb.toString());
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array[i] = this.mSpans[i].findLastVisibleItemPosition();
        }
        return array;
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
        return super.getColumnCountForAccessibility(recycler, state);
    }
    
    int getFirstChildPosition() {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
    }
    
    public int getGapStrategy() {
        return this.mGapStrategy;
    }
    
    int getLastChildPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (childCount == 0) {
            return 0;
        }
        return ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(childCount - 1));
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        return super.getRowCountForAccessibility(recycler, state);
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    View hasGapsToFix() {
        int n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        final BitSet set = new BitSet(this.mSpanCount);
        set.set(0, this.mSpanCount, true);
        final int mOrientation = this.mOrientation;
        int n2 = -1;
        int n3;
        if (mOrientation == 1 && this.isLayoutRTL()) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        int n4;
        if (this.mShouldReverseLayout) {
            n4 = 0 - 1;
        }
        else {
            final int n5 = 0;
            n4 = n + 1;
            n = n5;
        }
        if (n < n4) {
            n2 = 1;
        }
        for (int i = n; i != n4; i += n2) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (set.get(layoutParams.mSpan.mIndex)) {
                if (this.checkSpanForGap(layoutParams.mSpan)) {
                    return child;
                }
                set.clear(layoutParams.mSpan.mIndex);
            }
            if (!layoutParams.mFullSpan) {
                if (i + n2 != n4) {
                    final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(i + n2);
                    final boolean b = false;
                    boolean b2 = false;
                    if (this.mShouldReverseLayout) {
                        final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
                        final int decoratedEnd2 = this.mPrimaryOrientation.getDecoratedEnd(child2);
                        if (decoratedEnd < decoratedEnd2) {
                            return child;
                        }
                        if (decoratedEnd == decoratedEnd2) {
                            b2 = true;
                        }
                    }
                    else {
                        final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
                        final int decoratedStart2 = this.mPrimaryOrientation.getDecoratedStart(child2);
                        if (decoratedStart > decoratedStart2) {
                            return child;
                        }
                        b2 = (decoratedStart == decoratedStart2 || b);
                    }
                    if (b2) {
                        if (layoutParams.mSpan.mIndex - ((LayoutParams)child2.getLayoutParams()).mSpan.mIndex < 0 != n3 < 0) {
                            return child;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    boolean isLayoutRTL() {
        return ((RecyclerView.LayoutManager)this).getLayoutDirection() == 1;
    }
    
    @Override
    public void offsetChildrenHorizontal(final int n) {
        super.offsetChildrenHorizontal(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void offsetChildrenVertical(final int n) {
        super.offsetChildrenVertical(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
        ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].clear();
        }
        recyclerView.requestLayout();
    }
    
    @Nullable
    @Override
    public View onFocusSearchFailed(View containingItemView, int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(containingItemView);
        if (containingItemView == null) {
            return null;
        }
        this.resolveShouldLayoutReverse();
        final int convertFocusDirectionToLayoutDirection = this.convertFocusDirectionToLayoutDirection(n);
        if (convertFocusDirectionToLayoutDirection == Integer.MIN_VALUE) {
            return null;
        }
        final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
        final boolean mFullSpan = layoutParams.mFullSpan;
        final Span mSpan = layoutParams.mSpan;
        if (convertFocusDirectionToLayoutDirection == 1) {
            n = this.getLastChildPosition();
        }
        else {
            n = this.getFirstChildPosition();
        }
        this.updateLayoutState(n, state);
        this.setLayoutStateDirection(convertFocusDirectionToLayoutDirection);
        final android.support.v7.widget.LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mCurrentPosition = mLayoutState.mItemDirection + n;
        this.mLayoutState.mAvailable = (int)(this.mPrimaryOrientation.getTotalSpace() * 0.33333334f);
        final android.support.v7.widget.LayoutState mLayoutState2 = this.mLayoutState;
        mLayoutState2.mStopInFocusable = true;
        final int n2 = 0;
        mLayoutState2.mRecycle = false;
        this.fill(recycler, mLayoutState2, state);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        if (!mFullSpan) {
            final View focusableViewAfter = mSpan.getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
            if (focusableViewAfter != null && focusableViewAfter != containingItemView) {
                return focusableViewAfter;
            }
        }
        if (this.preferLastSpan(convertFocusDirectionToLayoutDirection)) {
            for (int i = this.mSpanCount - 1; i >= 0; --i) {
                final View focusableViewAfter2 = this.mSpans[i].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                if (focusableViewAfter2 != null && focusableViewAfter2 != containingItemView) {
                    return focusableViewAfter2;
                }
            }
        }
        else {
            for (int j = 0; j < this.mSpanCount; ++j) {
                final View focusableViewAfter3 = this.mSpans[j].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                if (focusableViewAfter3 != null && focusableViewAfter3 != containingItemView) {
                    return focusableViewAfter3;
                }
            }
        }
        final boolean mReverseLayout = this.mReverseLayout;
        final boolean b = convertFocusDirectionToLayoutDirection == -1;
        n = n2;
        if ((mReverseLayout ^ true) == b) {
            n = 1;
        }
        if (!mFullSpan) {
            int n3;
            if (n != 0) {
                n3 = mSpan.findFirstPartiallyVisibleItemPosition();
            }
            else {
                n3 = mSpan.findLastPartiallyVisibleItemPosition();
            }
            final View viewByPosition = ((RecyclerView.LayoutManager)this).findViewByPosition(n3);
            if (viewByPosition != null && viewByPosition != containingItemView) {
                return viewByPosition;
            }
        }
        if (this.preferLastSpan(convertFocusDirectionToLayoutDirection)) {
            for (int k = this.mSpanCount - 1; k >= 0; --k) {
                if (k != mSpan.mIndex) {
                    int n4;
                    if (n != 0) {
                        n4 = this.mSpans[k].findFirstPartiallyVisibleItemPosition();
                    }
                    else {
                        n4 = this.mSpans[k].findLastPartiallyVisibleItemPosition();
                    }
                    final View viewByPosition2 = ((RecyclerView.LayoutManager)this).findViewByPosition(n4);
                    if (viewByPosition2 != null && viewByPosition2 != containingItemView) {
                        return viewByPosition2;
                    }
                }
            }
            return null;
        }
        for (int l = 0; l < this.mSpanCount; ++l) {
            int n5;
            if (n != 0) {
                n5 = this.mSpans[l].findFirstPartiallyVisibleItemPosition();
            }
            else {
                n5 = this.mSpans[l].findLastPartiallyVisibleItemPosition();
            }
            final View viewByPosition3 = ((RecyclerView.LayoutManager)this).findViewByPosition(n5);
            if (viewByPosition3 != null && viewByPosition3 != containingItemView) {
                return viewByPosition3;
            }
        }
        return null;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (((RecyclerView.LayoutManager)this).getChildCount() <= 0) {
            return;
        }
        final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(false);
        final View firstVisibleItemClosestToEnd = this.findFirstVisibleItemClosestToEnd(false);
        if (firstVisibleItemClosestToStart == null) {
            return;
        }
        if (firstVisibleItemClosestToEnd == null) {
            return;
        }
        final int position = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToStart);
        final int position2 = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToEnd);
        if (position < position2) {
            accessibilityEvent.setFromIndex(position);
            accessibilityEvent.setToIndex(position2);
            return;
        }
        accessibilityEvent.setFromIndex(position2);
        accessibilityEvent.setToIndex(position);
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        if (this.mOrientation == 0) {
            final int spanIndex = layoutParams2.getSpanIndex();
            int mSpanCount;
            if (layoutParams2.mFullSpan) {
                mSpanCount = this.mSpanCount;
            }
            else {
                mSpanCount = 1;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanIndex, mSpanCount, -1, -1, layoutParams2.mFullSpan, false));
            return;
        }
        final int spanIndex2 = layoutParams2.getSpanIndex();
        int mSpanCount2;
        if (layoutParams2.mFullSpan) {
            mSpanCount2 = this.mSpanCount;
        }
        else {
            mSpanCount2 = 1;
        }
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, spanIndex2, mSpanCount2, layoutParams2.mFullSpan, false));
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 1);
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.handleUpdate(n, n2, 8);
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 2);
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.handleUpdate(n, n2, 4);
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        this.onLayoutChildren(recycler, state, true);
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
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
        savedState.mReverseLayout = this.mReverseLayout;
        savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
        savedState.mLastLayoutRTL = this.mLastLayoutRTL;
        final LazySpanLookup mLazySpanLookup = this.mLazySpanLookup;
        if (mLazySpanLookup != null && mLazySpanLookup.mData != null) {
            savedState.mSpanLookup = this.mLazySpanLookup.mData;
            savedState.mSpanLookupSize = savedState.mSpanLookup.length;
            savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
        }
        else {
            savedState.mSpanLookupSize = 0;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            int mAnchorPosition;
            if (this.mLastLayoutFromEnd) {
                mAnchorPosition = this.getLastChildPosition();
            }
            else {
                mAnchorPosition = this.getFirstChildPosition();
            }
            savedState.mAnchorPosition = mAnchorPosition;
            savedState.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
            final int mSpanCount = this.mSpanCount;
            savedState.mSpanOffsetsSize = mSpanCount;
            savedState.mSpanOffsets = new int[mSpanCount];
            for (int i = 0; i < this.mSpanCount; ++i) {
                int n;
                if (this.mLastLayoutFromEnd) {
                    n = this.mSpans[i].getEndLine(Integer.MIN_VALUE);
                    if (n != Integer.MIN_VALUE) {
                        n -= this.mPrimaryOrientation.getEndAfterPadding();
                    }
                }
                else {
                    n = this.mSpans[i].getStartLine(Integer.MIN_VALUE);
                    if (n != Integer.MIN_VALUE) {
                        n -= this.mPrimaryOrientation.getStartAfterPadding();
                    }
                }
                savedState.mSpanOffsets[i] = n;
            }
            return (Parcelable)savedState;
        }
        savedState.mAnchorPosition = -1;
        savedState.mVisibleAnchorPosition = -1;
        savedState.mSpanOffsetsSize = 0;
        return (Parcelable)savedState;
    }
    
    @Override
    public void onScrollStateChanged(final int n) {
        if (n == 0) {
            this.checkForGaps();
        }
    }
    
    void prepareLayoutStateForDelta(final int n, final State state) {
        int layoutStateDirection;
        int n2;
        if (n > 0) {
            layoutStateDirection = 1;
            n2 = this.getLastChildPosition();
        }
        else {
            layoutStateDirection = -1;
            n2 = this.getFirstChildPosition();
        }
        this.mLayoutState.mRecycle = true;
        this.updateLayoutState(n2, state);
        this.setLayoutStateDirection(layoutStateDirection);
        final android.support.v7.widget.LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mCurrentPosition = mLayoutState.mItemDirection + n2;
        this.mLayoutState.mAvailable = Math.abs(n);
    }
    
    int scrollBy(int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        if (n == 0) {
            return 0;
        }
        this.prepareLayoutStateForDelta(n, state);
        final int fill = this.fill(recycler, this.mLayoutState, state);
        if (this.mLayoutState.mAvailable >= fill) {
            if (n < 0) {
                n = -fill;
            }
            else {
                n = fill;
            }
        }
        this.mPrimaryOrientation.offsetChildren(-n);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        final android.support.v7.widget.LayoutState mLayoutState = this.mLayoutState;
        mLayoutState.mAvailable = 0;
        this.recycle(recycler, mLayoutState);
        return n;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    @Override
    public void scrollToPosition(final int mPendingScrollPosition) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null && mPendingSavedState.mAnchorPosition != mPendingScrollPosition) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void scrollToPositionWithOffset(final int mPendingScrollPosition, final int mPendingScrollPositionOffset) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null) {
            mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = mPendingScrollPositionOffset;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    public void setGapStrategy(final int mGapStrategy) {
        this.assertNotInLayoutOrScroll(null);
        if (mGapStrategy == this.mGapStrategy) {
            return;
        }
        if (mGapStrategy != 0 && mGapStrategy != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
        }
        this.mGapStrategy = mGapStrategy;
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(this.mGapStrategy != 0);
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            n = RecyclerView.LayoutManager.chooseSize(n, this.mSizePerSpan * this.mSpanCount + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            n2 = RecyclerView.LayoutManager.chooseSize(n2, this.mSizePerSpan * this.mSpanCount + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setOrientation(final int mOrientation) {
        if (mOrientation != 0 && mOrientation != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        this.assertNotInLayoutOrScroll(null);
        if (mOrientation == this.mOrientation) {
            return;
        }
        this.mOrientation = mOrientation;
        final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
        this.mPrimaryOrientation = this.mSecondaryOrientation;
        this.mSecondaryOrientation = mPrimaryOrientation;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setReverseLayout(final boolean b) {
        this.assertNotInLayoutOrScroll(null);
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null && mPendingSavedState.mReverseLayout != b) {
            this.mPendingSavedState.mReverseLayout = b;
        }
        this.mReverseLayout = b;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSpanCount(int i) {
        this.assertNotInLayoutOrScroll(null);
        if (i != this.mSpanCount) {
            this.invalidateSpanAssignments();
            this.mSpanCount = i;
            this.mRemainingSpans = new BitSet(this.mSpanCount);
            this.mSpans = new Span[this.mSpanCount];
            for (i = 0; i < this.mSpanCount; ++i) {
                this.mSpans[i] = new Span(i);
            }
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        ((RecyclerView.SmoothScroller)linearSmoothScroller).setTargetPosition(targetPosition);
        ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScroller);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null;
    }
    
    boolean updateAnchorFromPendingData(final State state, final AnchorInfo anchorInfo) {
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
        final SavedState mPendingSavedState = this.mPendingSavedState;
        if (mPendingSavedState != null && mPendingSavedState.mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
            anchorInfo.mOffset = Integer.MIN_VALUE;
            anchorInfo.mPosition = this.mPendingScrollPosition;
            return true;
        }
        final View viewByPosition = ((RecyclerView.LayoutManager)this).findViewByPosition(this.mPendingScrollPosition);
        if (viewByPosition != null) {
            int mPosition;
            if (this.mShouldReverseLayout) {
                mPosition = this.getLastChildPosition();
            }
            else {
                mPosition = this.getFirstChildPosition();
            }
            anchorInfo.mPosition = mPosition;
            if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                if (anchorInfo.mLayoutFromEnd) {
                    anchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                    return true;
                }
                anchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(viewByPosition);
                return true;
            }
            else {
                if (this.mPrimaryOrientation.getDecoratedMeasurement(viewByPosition) > this.mPrimaryOrientation.getTotalSpace()) {
                    int mOffset;
                    if (anchorInfo.mLayoutFromEnd) {
                        mOffset = this.mPrimaryOrientation.getEndAfterPadding();
                    }
                    else {
                        mOffset = this.mPrimaryOrientation.getStartAfterPadding();
                    }
                    anchorInfo.mOffset = mOffset;
                    return true;
                }
                final int n = this.mPrimaryOrientation.getDecoratedStart(viewByPosition) - this.mPrimaryOrientation.getStartAfterPadding();
                if (n < 0) {
                    anchorInfo.mOffset = -n;
                    return true;
                }
                final int mOffset2 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                if (mOffset2 < 0) {
                    anchorInfo.mOffset = mOffset2;
                    return true;
                }
                anchorInfo.mOffset = Integer.MIN_VALUE;
            }
        }
        else {
            anchorInfo.mPosition = this.mPendingScrollPosition;
            final int mPendingScrollPositionOffset = this.mPendingScrollPositionOffset;
            if (mPendingScrollPositionOffset == Integer.MIN_VALUE) {
                if (this.calculateScrollDirectionForPosition(anchorInfo.mPosition) == 1) {
                    mLayoutFromEnd = true;
                }
                anchorInfo.mLayoutFromEnd = mLayoutFromEnd;
                anchorInfo.assignCoordinateFromPadding();
            }
            else {
                anchorInfo.assignCoordinateFromPadding(mPendingScrollPositionOffset);
            }
            anchorInfo.mInvalidateOffsets = true;
        }
        return true;
    }
    
    void updateAnchorInfoForLayout(final State state, final AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (this.updateAnchorFromChildren(state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }
    
    void updateMeasureSpecs(final int n) {
        this.mSizePerSpan = n / this.mSpanCount;
        this.mFullSizeSpec = View$MeasureSpec.makeMeasureSpec(n, this.mSecondaryOrientation.getMode());
    }
    
    class AnchorInfo
    {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;
        int[] mSpanReferenceLines;
        boolean mValid;
        
        AnchorInfo() {
            this.reset();
        }
        
        void assignCoordinateFromPadding() {
            int mOffset;
            if (this.mLayoutFromEnd) {
                mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
            }
            else {
                mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            }
            this.mOffset = mOffset;
        }
        
        void assignCoordinateFromPadding(final int n) {
            if (this.mLayoutFromEnd) {
                this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - n;
                return;
            }
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + n;
        }
        
        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
            this.mValid = false;
            final int[] mSpanReferenceLines = this.mSpanReferenceLines;
            if (mSpanReferenceLines != null) {
                Arrays.fill(mSpanReferenceLines, -1);
            }
        }
        
        void saveSpanReferenceLines(final Span[] array) {
            final int length = array.length;
            final int[] mSpanReferenceLines = this.mSpanReferenceLines;
            if (mSpanReferenceLines == null || mSpanReferenceLines.length < length) {
                this.mSpanReferenceLines = new int[StaggeredGridLayoutManager.this.mSpans.length];
            }
            for (int i = 0; i < length; ++i) {
                this.mSpanReferenceLines[i] = array[i].getStartLine(Integer.MIN_VALUE);
            }
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
        }
        
        public final int getSpanIndex() {
            final Span mSpan = this.mSpan;
            if (mSpan == null) {
                return -1;
            }
            return mSpan.mIndex;
        }
        
        public boolean isFullSpan() {
            return this.mFullSpan;
        }
        
        public void setFullSpan(final boolean mFullSpan) {
            this.mFullSpan = mFullSpan;
        }
    }
    
    static class LazySpanLookup
    {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;
        
        private int invalidateFullSpansAfter(final int n) {
            if (this.mFullSpanItems == null) {
                return -1;
            }
            final FullSpanItem fullSpanItem = this.getFullSpanItem(n);
            if (fullSpanItem != null) {
                this.mFullSpanItems.remove(fullSpanItem);
            }
            final int n2 = -1;
            final int size = this.mFullSpanItems.size();
            int n3 = 0;
            int n4;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                if (this.mFullSpanItems.get(n3).mPosition >= n) {
                    n4 = n3;
                    break;
                }
                ++n3;
            }
            if (n4 != -1) {
                final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(n4);
                this.mFullSpanItems.remove(n4);
                return fullSpanItem2.mPosition;
            }
            return -1;
        }
        
        private void offsetFullSpansForAddition(final int n, final int n2) {
            final List<FullSpanItem> mFullSpanItems = this.mFullSpanItems;
            if (mFullSpanItems == null) {
                return;
            }
            for (int i = mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n) {
                    fullSpanItem.mPosition += n2;
                }
            }
        }
        
        private void offsetFullSpansForRemoval(final int n, final int n2) {
            final List<FullSpanItem> mFullSpanItems = this.mFullSpanItems;
            if (mFullSpanItems == null) {
                return;
            }
            for (int i = mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n) {
                    if (fullSpanItem.mPosition < n + n2) {
                        this.mFullSpanItems.remove(i);
                    }
                    else {
                        fullSpanItem.mPosition -= n2;
                    }
                }
            }
        }
        
        public void addFullSpanItem(final FullSpanItem fullSpanItem) {
            if (this.mFullSpanItems == null) {
                this.mFullSpanItems = new ArrayList<FullSpanItem>();
            }
            for (int size = this.mFullSpanItems.size(), i = 0; i < size; ++i) {
                final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                if (fullSpanItem2.mPosition == fullSpanItem.mPosition) {
                    this.mFullSpanItems.remove(i);
                }
                if (fullSpanItem2.mPosition >= fullSpanItem.mPosition) {
                    this.mFullSpanItems.add(i, fullSpanItem);
                    return;
                }
            }
            this.mFullSpanItems.add(fullSpanItem);
        }
        
        void clear() {
            final int[] mData = this.mData;
            if (mData != null) {
                Arrays.fill(mData, -1);
            }
            this.mFullSpanItems = null;
        }
        
        void ensureSize(final int n) {
            final int[] mData = this.mData;
            if (mData == null) {
                Arrays.fill(this.mData = new int[Math.max(n, 10) + 1], -1);
                return;
            }
            if (n >= mData.length) {
                final int[] mData2 = this.mData;
                System.arraycopy(mData2, 0, this.mData = new int[this.sizeForPosition(n)], 0, mData2.length);
                final int[] mData3 = this.mData;
                Arrays.fill(mData3, mData2.length, mData3.length, -1);
            }
        }
        
        int forceInvalidateAfter(final int n) {
            final List<FullSpanItem> mFullSpanItems = this.mFullSpanItems;
            if (mFullSpanItems != null) {
                for (int i = mFullSpanItems.size() - 1; i >= 0; --i) {
                    if (this.mFullSpanItems.get(i).mPosition >= n) {
                        this.mFullSpanItems.remove(i);
                    }
                }
            }
            return this.invalidateAfter(n);
        }
        
        public FullSpanItem getFirstFullSpanItemInRange(final int n, final int n2, final int n3, final boolean b) {
            final List<FullSpanItem> mFullSpanItems = this.mFullSpanItems;
            if (mFullSpanItems == null) {
                return null;
            }
            for (int size = mFullSpanItems.size(), i = 0; i < size; ++i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n2) {
                    return null;
                }
                if (fullSpanItem.mPosition >= n && (n3 == 0 || fullSpanItem.mGapDir == n3 || (b && fullSpanItem.mHasUnwantedGapAfter))) {
                    return fullSpanItem;
                }
            }
            return null;
        }
        
        public FullSpanItem getFullSpanItem(final int n) {
            final List<FullSpanItem> mFullSpanItems = this.mFullSpanItems;
            if (mFullSpanItems == null) {
                return null;
            }
            for (int i = mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition == n) {
                    return fullSpanItem;
                }
            }
            return null;
        }
        
        int getSpan(final int n) {
            final int[] mData = this.mData;
            if (mData != null && n < mData.length) {
                return mData[n];
            }
            return -1;
        }
        
        int invalidateAfter(final int n) {
            final int[] mData = this.mData;
            if (mData == null) {
                return -1;
            }
            if (n >= mData.length) {
                return -1;
            }
            final int invalidateFullSpansAfter = this.invalidateFullSpansAfter(n);
            if (invalidateFullSpansAfter == -1) {
                final int[] mData2 = this.mData;
                Arrays.fill(mData2, n, mData2.length, -1);
                return this.mData.length;
            }
            Arrays.fill(this.mData, n, invalidateFullSpansAfter + 1, -1);
            return invalidateFullSpansAfter + 1;
        }
        
        void offsetForAddition(final int n, final int n2) {
            final int[] mData = this.mData;
            if (mData == null) {
                return;
            }
            if (n >= mData.length) {
                return;
            }
            this.ensureSize(n + n2);
            final int[] mData2 = this.mData;
            System.arraycopy(mData2, n, mData2, n + n2, mData2.length - n - n2);
            Arrays.fill(this.mData, n, n + n2, -1);
            this.offsetFullSpansForAddition(n, n2);
        }
        
        void offsetForRemoval(final int n, final int n2) {
            final int[] mData = this.mData;
            if (mData == null) {
                return;
            }
            if (n >= mData.length) {
                return;
            }
            this.ensureSize(n + n2);
            final int[] mData2 = this.mData;
            System.arraycopy(mData2, n + n2, mData2, n, mData2.length - n - n2);
            final int[] mData3 = this.mData;
            Arrays.fill(mData3, mData3.length - n2, mData3.length, -1);
            this.offsetFullSpansForRemoval(n, n2);
        }
        
        void setSpan(final int n, final Span span) {
            this.ensureSize(n);
            this.mData[n] = span.mIndex;
        }
        
        int sizeForPosition(final int n) {
            int i;
            for (i = this.mData.length; i <= n; i *= 2) {}
            return i;
        }
        
        static class FullSpanItem implements Parcelable
        {
            public static final Parcelable$Creator<FullSpanItem> CREATOR;
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;
            
            static {
                CREATOR = (Parcelable$Creator)new Parcelable$Creator<FullSpanItem>() {
                    public FullSpanItem createFromParcel(final Parcel parcel) {
                        return new FullSpanItem(parcel);
                    }
                    
                    public FullSpanItem[] newArray(final int n) {
                        return new FullSpanItem[n];
                    }
                };
            }
            
            FullSpanItem() {
            }
            
            FullSpanItem(final Parcel parcel) {
                this.mPosition = parcel.readInt();
                this.mGapDir = parcel.readInt();
                final int int1 = parcel.readInt();
                boolean mHasUnwantedGapAfter = true;
                if (int1 != 1) {
                    mHasUnwantedGapAfter = false;
                }
                this.mHasUnwantedGapAfter = mHasUnwantedGapAfter;
                final int int2 = parcel.readInt();
                if (int2 > 0) {
                    parcel.readIntArray(this.mGapPerSpan = new int[int2]);
                }
            }
            
            public int describeContents() {
                return 0;
            }
            
            int getGapForSpan(final int n) {
                final int[] mGapPerSpan = this.mGapPerSpan;
                if (mGapPerSpan == null) {
                    return 0;
                }
                return mGapPerSpan[n];
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("FullSpanItem{mPosition=");
                sb.append(this.mPosition);
                sb.append(", mGapDir=");
                sb.append(this.mGapDir);
                sb.append(", mHasUnwantedGapAfter=");
                sb.append(this.mHasUnwantedGapAfter);
                sb.append(", mGapPerSpan=");
                sb.append(Arrays.toString(this.mGapPerSpan));
                sb.append('}');
                return sb.toString();
            }
            
            public void writeToParcel(final Parcel parcel, final int n) {
                throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean mAnchorLayoutFromEnd;
        int mAnchorPosition;
        List<FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;
        
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
            this.mVisibleAnchorPosition = parcel.readInt();
            this.mSpanOffsetsSize = parcel.readInt();
            final int mSpanOffsetsSize = this.mSpanOffsetsSize;
            if (mSpanOffsetsSize > 0) {
                parcel.readIntArray(this.mSpanOffsets = new int[mSpanOffsetsSize]);
            }
            this.mSpanLookupSize = parcel.readInt();
            final int mSpanLookupSize = this.mSpanLookupSize;
            if (mSpanLookupSize > 0) {
                parcel.readIntArray(this.mSpanLookup = new int[mSpanLookupSize]);
            }
            final int int1 = parcel.readInt();
            final boolean b = false;
            this.mReverseLayout = (int1 == 1);
            this.mAnchorLayoutFromEnd = (parcel.readInt() == 1);
            boolean mLastLayoutRTL = b;
            if (parcel.readInt() == 1) {
                mLastLayoutRTL = true;
            }
            this.mLastLayoutRTL = mLastLayoutRTL;
            this.mFullSpanItems = (List<FullSpanItem>)parcel.readArrayList(FullSpanItem.class.getClassLoader());
        }
        
        public SavedState(final SavedState savedState) {
            this.mSpanOffsetsSize = savedState.mSpanOffsetsSize;
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mVisibleAnchorPosition = savedState.mVisibleAnchorPosition;
            this.mSpanOffsets = savedState.mSpanOffsets;
            this.mSpanLookupSize = savedState.mSpanLookupSize;
            this.mSpanLookup = savedState.mSpanLookup;
            this.mReverseLayout = savedState.mReverseLayout;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = savedState.mLastLayoutRTL;
            this.mFullSpanItems = savedState.mFullSpanItems;
        }
        
        public int describeContents() {
            return 0;
        }
        
        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }
        
        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        }
    }
    
    class Span
    {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        ArrayList<View> mViews;
        
        Span(final int mIndex) {
            this.mViews = new ArrayList<View>();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = mIndex;
        }
        
        void appendToSpan(final View view) {
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(view);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (!((RecyclerView.LayoutParams)layoutParams).isItemRemoved() && !((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                return;
            }
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
        }
        
        void cacheReferenceLineAndClear(final boolean b, final int n) {
            int n2;
            if (b) {
                n2 = this.getEndLine(Integer.MIN_VALUE);
            }
            else {
                n2 = this.getStartLine(Integer.MIN_VALUE);
            }
            this.clear();
            if (n2 == Integer.MIN_VALUE) {
                return;
            }
            if (b && n2 < StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) {
                return;
            }
            if (!b && n2 > StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding()) {
                return;
            }
            if (n != Integer.MIN_VALUE) {
                n2 += n;
            }
            this.mCachedEnd = n2;
            this.mCachedStart = n2;
        }
        
        void calculateCachedEnd() {
            final ArrayList<View> mViews = this.mViews;
            final View view = mViews.get(mViews.size() - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
            if (!layoutParams.mFullSpan) {
                return;
            }
            final FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
            if (fullSpanItem != null && fullSpanItem.mGapDir == 1) {
                this.mCachedEnd += fullSpanItem.getGapForSpan(this.mIndex);
            }
        }
        
        void calculateCachedStart() {
            final View view = this.mViews.get(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
            if (!layoutParams.mFullSpan) {
                return;
            }
            final FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
            if (fullSpanItem != null && fullSpanItem.mGapDir == -1) {
                this.mCachedStart -= fullSpanItem.getGapForSpan(this.mIndex);
            }
        }
        
        void clear() {
            this.mViews.clear();
            this.invalidateCache();
            this.mDeletedSize = 0;
        }
        
        public int findFirstCompletelyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return this.findOneVisibleChild(0, this.mViews.size(), true);
        }
        
        public int findFirstPartiallyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
        }
        
        public int findFirstVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            return this.findOneVisibleChild(0, this.mViews.size(), false);
        }
        
        public int findLastCompletelyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            return this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
        }
        
        public int findLastPartiallyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
            }
            return this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
        }
        
        public int findLastVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            return this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
        }
        
        int findOnePartiallyOrCompletelyVisibleChild(int i, final int n, final boolean b, final boolean b2, final boolean b3) {
            final int startAfterPadding = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            final int endAfterPadding = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
            int n2;
            if (n > i) {
                n2 = 1;
            }
            else {
                n2 = -1;
            }
            while (i != n) {
                final View view = this.mViews.get(i);
                final int decoratedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
                final int decoratedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
                boolean b4 = false;
                final boolean b5 = b3 ? (decoratedStart <= endAfterPadding) : (decoratedStart < endAfterPadding);
                Label_0143: {
                    if (b3) {
                        if (decoratedEnd < startAfterPadding) {
                            break Label_0143;
                        }
                    }
                    else if (decoratedEnd <= startAfterPadding) {
                        break Label_0143;
                    }
                    b4 = true;
                }
                if (b5 && b4) {
                    if (b && b2) {
                        if (decoratedStart >= startAfterPadding && decoratedEnd <= endAfterPadding) {
                            return ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
                        }
                    }
                    else {
                        if (b2) {
                            return ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
                        }
                        if (decoratedStart < startAfterPadding || decoratedEnd > endAfterPadding) {
                            return ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
                        }
                    }
                }
                i += n2;
            }
            return -1;
        }
        
        int findOnePartiallyVisibleChild(final int n, final int n2, final boolean b) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, false, false, b);
        }
        
        int findOneVisibleChild(final int n, final int n2, final boolean b) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, b, true, false);
        }
        
        public int getDeletedSize() {
            return this.mDeletedSize;
        }
        
        int getEndLine() {
            final int mCachedEnd = this.mCachedEnd;
            if (mCachedEnd != Integer.MIN_VALUE) {
                return mCachedEnd;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }
        
        int getEndLine(final int n) {
            final int mCachedEnd = this.mCachedEnd;
            if (mCachedEnd != Integer.MIN_VALUE) {
                return mCachedEnd;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }
        
        public View getFocusableViewAfter(final int n, int i) {
            final View view = null;
            View view2 = null;
            if (i == -1) {
                int size;
                View view3;
                for (size = this.mViews.size(), i = 0; i < size; ++i) {
                    view3 = this.mViews.get(i);
                    if (StaggeredGridLayoutManager.this.mReverseLayout && ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view3) <= n) {
                        break;
                    }
                    if (!StaggeredGridLayoutManager.this.mReverseLayout && ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view3) >= n) {
                        break;
                    }
                    if (!view3.hasFocusable()) {
                        break;
                    }
                    view2 = view3;
                }
                return view2;
            }
            i = this.mViews.size() - 1;
            View view4 = view;
            while (i >= 0) {
                final View view5 = this.mViews.get(i);
                if (StaggeredGridLayoutManager.this.mReverseLayout && ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view5) >= n) {
                    return view4;
                }
                if (!StaggeredGridLayoutManager.this.mReverseLayout && ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view5) <= n) {
                    return view4;
                }
                if (!view5.hasFocusable()) {
                    return view4;
                }
                view4 = view5;
                --i;
            }
            return view4;
        }
        
        LayoutParams getLayoutParams(final View view) {
            return (LayoutParams)view.getLayoutParams();
        }
        
        int getStartLine() {
            final int mCachedStart = this.mCachedStart;
            if (mCachedStart != Integer.MIN_VALUE) {
                return mCachedStart;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }
        
        int getStartLine(final int n) {
            final int mCachedStart = this.mCachedStart;
            if (mCachedStart != Integer.MIN_VALUE) {
                return mCachedStart;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }
        
        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void onOffset(final int n) {
            final int mCachedStart = this.mCachedStart;
            if (mCachedStart != Integer.MIN_VALUE) {
                this.mCachedStart = mCachedStart + n;
            }
            final int mCachedEnd = this.mCachedEnd;
            if (mCachedEnd != Integer.MIN_VALUE) {
                this.mCachedEnd = mCachedEnd + n;
            }
        }
        
        void popEnd() {
            final int size = this.mViews.size();
            final View view = this.mViews.remove(size - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            if (size == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void popStart() {
            final View view = this.mViews.remove(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }
        
        void prependToSpan(final View view) {
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(0, view);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (!((RecyclerView.LayoutParams)layoutParams).isItemRemoved() && !((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                return;
            }
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
        }
        
        void setLine(final int n) {
            this.mCachedStart = n;
            this.mCachedEnd = n;
        }
    }
}
