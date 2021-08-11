/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class LayoutState {
    static final int INVALID_LAYOUT = Integer.MIN_VALUE;
    static final int ITEM_DIRECTION_HEAD = -1;
    static final int ITEM_DIRECTION_TAIL = 1;
    static final int LAYOUT_END = 1;
    static final int LAYOUT_START = -1;
    static final String TAG = "LayoutState";
    int mAvailable;
    int mCurrentPosition;
    int mEndLine = 0;
    boolean mInfinite;
    int mItemDirection;
    int mLayoutDirection;
    boolean mRecycle = true;
    int mStartLine = 0;
    boolean mStopInFocusable;

    LayoutState() {
    }

    boolean hasMore(RecyclerView.State state) {
        int n = this.mCurrentPosition;
        if (n >= 0 && n < state.getItemCount()) {
            return true;
        }
        return false;
    }

    View next(RecyclerView.Recycler recycler) {
        recycler = recycler.getViewForPosition(this.mCurrentPosition);
        this.mCurrentPosition += this.mItemDirection;
        return recycler;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LayoutState{mAvailable=");
        stringBuilder.append(this.mAvailable);
        stringBuilder.append(", mCurrentPosition=");
        stringBuilder.append(this.mCurrentPosition);
        stringBuilder.append(", mItemDirection=");
        stringBuilder.append(this.mItemDirection);
        stringBuilder.append(", mLayoutDirection=");
        stringBuilder.append(this.mLayoutDirection);
        stringBuilder.append(", mStartLine=");
        stringBuilder.append(this.mStartLine);
        stringBuilder.append(", mEndLine=");
        stringBuilder.append(this.mEndLine);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

