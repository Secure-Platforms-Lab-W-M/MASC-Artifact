// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.view.View;
import java.util.List;

class ChildHelper
{
    private static final boolean DEBUG = false;
    private static final String TAG = "ChildrenHelper";
    final Bucket mBucket;
    final Callback mCallback;
    final List<View> mHiddenViews;
    
    ChildHelper(final Callback mCallback) {
        this.mCallback = mCallback;
        this.mBucket = new Bucket();
        this.mHiddenViews = new ArrayList<View>();
    }
    
    private int getOffset(final int n) {
        if (n < 0) {
            return -1;
        }
        int n2;
        for (int childCount = this.mCallback.getChildCount(), i = n; i < childCount; i += n2) {
            n2 = n - (i - this.mBucket.countOnesBefore(i));
            if (n2 == 0) {
                while (this.mBucket.get(i)) {
                    ++i;
                }
                return i;
            }
        }
        return -1;
    }
    
    private void hideViewInternal(final View view) {
        this.mHiddenViews.add(view);
        this.mCallback.onEnteredHiddenState(view);
    }
    
    private boolean unhideViewInternal(final View view) {
        if (this.mHiddenViews.remove(view)) {
            this.mCallback.onLeftHiddenState(view);
            return true;
        }
        return false;
    }
    
    void addView(final View view, int n, final boolean b) {
        if (n < 0) {
            n = this.mCallback.getChildCount();
        }
        else {
            n = this.getOffset(n);
        }
        this.mBucket.insert(n, b);
        if (b) {
            this.hideViewInternal(view);
        }
        this.mCallback.addView(view, n);
    }
    
    void addView(final View view, final boolean b) {
        this.addView(view, -1, b);
    }
    
    void attachViewToParent(final View view, int n, final ViewGroup$LayoutParams viewGroup$LayoutParams, final boolean b) {
        if (n < 0) {
            n = this.mCallback.getChildCount();
        }
        else {
            n = this.getOffset(n);
        }
        this.mBucket.insert(n, b);
        if (b) {
            this.hideViewInternal(view);
        }
        this.mCallback.attachViewToParent(view, n, viewGroup$LayoutParams);
    }
    
    void detachViewFromParent(int offset) {
        offset = this.getOffset(offset);
        this.mBucket.remove(offset);
        this.mCallback.detachViewFromParent(offset);
    }
    
    View findHiddenNonRemovedView(final int n) {
        for (int size = this.mHiddenViews.size(), i = 0; i < size; ++i) {
            final View view = this.mHiddenViews.get(i);
            final RecyclerView.ViewHolder childViewHolder = this.mCallback.getChildViewHolder(view);
            if (childViewHolder.getLayoutPosition() == n) {
                if (!childViewHolder.isInvalid()) {
                    if (!childViewHolder.isRemoved()) {
                        return view;
                    }
                }
            }
        }
        return null;
    }
    
    View getChildAt(int offset) {
        offset = this.getOffset(offset);
        return this.mCallback.getChildAt(offset);
    }
    
    int getChildCount() {
        return this.mCallback.getChildCount() - this.mHiddenViews.size();
    }
    
    View getUnfilteredChildAt(final int n) {
        return this.mCallback.getChildAt(n);
    }
    
    int getUnfilteredChildCount() {
        return this.mCallback.getChildCount();
    }
    
    void hide(final View view) {
        final int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild >= 0) {
            this.mBucket.set(indexOfChild);
            this.hideViewInternal(view);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("view is not a child, cannot hide ");
        sb.append(view);
        throw new IllegalArgumentException(sb.toString());
    }
    
    int indexOfChild(final View view) {
        final int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild == -1) {
            return -1;
        }
        if (this.mBucket.get(indexOfChild)) {
            return -1;
        }
        return indexOfChild - this.mBucket.countOnesBefore(indexOfChild);
    }
    
    boolean isHidden(final View view) {
        return this.mHiddenViews.contains(view);
    }
    
    void removeAllViewsUnfiltered() {
        this.mBucket.reset();
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.mCallback.onLeftHiddenState(this.mHiddenViews.get(i));
            this.mHiddenViews.remove(i);
        }
        this.mCallback.removeAllViews();
    }
    
    void removeView(final View view) {
        final int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild < 0) {
            return;
        }
        if (this.mBucket.remove(indexOfChild)) {
            this.unhideViewInternal(view);
        }
        this.mCallback.removeViewAt(indexOfChild);
    }
    
    void removeViewAt(int offset) {
        offset = this.getOffset(offset);
        final View child = this.mCallback.getChildAt(offset);
        if (child == null) {
            return;
        }
        if (this.mBucket.remove(offset)) {
            this.unhideViewInternal(child);
        }
        this.mCallback.removeViewAt(offset);
    }
    
    boolean removeViewIfHidden(final View view) {
        final int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild == -1) {
            this.unhideViewInternal(view);
            return true;
        }
        if (this.mBucket.get(indexOfChild)) {
            this.mBucket.remove(indexOfChild);
            this.unhideViewInternal(view);
            this.mCallback.removeViewAt(indexOfChild);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mBucket.toString());
        sb.append(", hidden list:");
        sb.append(this.mHiddenViews.size());
        return sb.toString();
    }
    
    void unhide(final View view) {
        final int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("view is not a child, cannot hide ");
            sb.append(view);
            throw new IllegalArgumentException(sb.toString());
        }
        if (this.mBucket.get(indexOfChild)) {
            this.mBucket.clear(indexOfChild);
            this.unhideViewInternal(view);
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("trying to unhide a view that was not hidden");
        sb2.append(view);
        throw new RuntimeException(sb2.toString());
    }
    
    static class Bucket
    {
        static final int BITS_PER_WORD = 64;
        static final long LAST_BIT = Long.MIN_VALUE;
        long mData;
        Bucket mNext;
        
        Bucket() {
            this.mData = 0L;
        }
        
        private void ensureNext() {
            if (this.mNext == null) {
                this.mNext = new Bucket();
            }
        }
        
        void clear(final int n) {
            if (n < 64) {
                this.mData &= ~(1L << n);
                return;
            }
            final Bucket mNext = this.mNext;
            if (mNext != null) {
                mNext.clear(n - 64);
            }
        }
        
        int countOnesBefore(final int n) {
            final Bucket mNext = this.mNext;
            if (mNext == null) {
                if (n >= 64) {
                    return Long.bitCount(this.mData);
                }
                return Long.bitCount(this.mData & (1L << n) - 1L);
            }
            else {
                if (n < 64) {
                    return Long.bitCount(this.mData & (1L << n) - 1L);
                }
                return mNext.countOnesBefore(n - 64) + Long.bitCount(this.mData);
            }
        }
        
        boolean get(final int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.mNext.get(n - 64);
            }
            return (this.mData & 1L << n) != 0x0L;
        }
        
        void insert(final int n, final boolean b) {
            if (n >= 64) {
                this.ensureNext();
                this.mNext.insert(n - 64, b);
                return;
            }
            final boolean b2 = (this.mData & Long.MIN_VALUE) != 0x0L;
            final long n2 = (1L << n) - 1L;
            final long mData = this.mData;
            this.mData = ((mData & n2) | (mData & (-1L ^ n2)) << 1);
            if (b) {
                this.set(n);
            }
            else {
                this.clear(n);
            }
            if (!b2 && this.mNext == null) {
                return;
            }
            this.ensureNext();
            this.mNext.insert(0, b2);
        }
        
        boolean remove(final int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.mNext.remove(n - 64);
            }
            final long n2 = 1L << n;
            final boolean b = (this.mData & n2) != 0x0L;
            this.mData &= ~n2;
            final long n3 = n2 - 1L;
            final long mData = this.mData;
            this.mData = ((mData & n3) | Long.rotateRight(mData & (-1L ^ n3), 1));
            final Bucket mNext = this.mNext;
            if (mNext != null) {
                if (mNext.get(0)) {
                    this.set(63);
                }
                this.mNext.remove(0);
                return b;
            }
            return b;
        }
        
        void reset() {
            this.mData = 0L;
            final Bucket mNext = this.mNext;
            if (mNext != null) {
                mNext.reset();
            }
        }
        
        void set(final int n) {
            if (n >= 64) {
                this.ensureNext();
                this.mNext.set(n - 64);
                return;
            }
            this.mData |= 1L << n;
        }
        
        @Override
        public String toString() {
            if (this.mNext == null) {
                return Long.toBinaryString(this.mData);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mNext.toString());
            sb.append("xx");
            sb.append(Long.toBinaryString(this.mData));
            return sb.toString();
        }
    }
    
    interface Callback
    {
        void addView(final View p0, final int p1);
        
        void attachViewToParent(final View p0, final int p1, final ViewGroup$LayoutParams p2);
        
        void detachViewFromParent(final int p0);
        
        View getChildAt(final int p0);
        
        int getChildCount();
        
        RecyclerView.ViewHolder getChildViewHolder(final View p0);
        
        int indexOfChild(final View p0);
        
        void onEnteredHiddenState(final View p0);
        
        void onLeftHiddenState(final View p0);
        
        void removeAllViews();
        
        void removeViewAt(final int p0);
    }
}
