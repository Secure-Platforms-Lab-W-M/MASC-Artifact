/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "ChildrenHelper";
    final Bucket mBucket;
    final Callback mCallback;
    final List<View> mHiddenViews;

    ChildHelper(Callback callback) {
        this.mCallback = callback;
        this.mBucket = new Bucket();
        this.mHiddenViews = new ArrayList<View>();
    }

    private int getOffset(int n) {
        int n2;
        if (n < 0) {
            return -1;
        }
        int n3 = this.mCallback.getChildCount();
        for (int i = n; i < n3; i += n2) {
            n2 = n - (i - this.mBucket.countOnesBefore(i));
            if (n2 != 0) continue;
            while (this.mBucket.get(i)) {
                ++i;
            }
            return i;
        }
        return -1;
    }

    private void hideViewInternal(View view) {
        this.mHiddenViews.add(view);
        this.mCallback.onEnteredHiddenState(view);
    }

    private boolean unhideViewInternal(View view) {
        if (this.mHiddenViews.remove((Object)view)) {
            this.mCallback.onLeftHiddenState(view);
            return true;
        }
        return false;
    }

    void addView(View view, int n, boolean bl) {
        n = n < 0 ? this.mCallback.getChildCount() : this.getOffset(n);
        this.mBucket.insert(n, bl);
        if (bl) {
            this.hideViewInternal(view);
        }
        this.mCallback.addView(view, n);
    }

    void addView(View view, boolean bl) {
        this.addView(view, -1, bl);
    }

    void attachViewToParent(View view, int n, ViewGroup.LayoutParams layoutParams, boolean bl) {
        n = n < 0 ? this.mCallback.getChildCount() : this.getOffset(n);
        this.mBucket.insert(n, bl);
        if (bl) {
            this.hideViewInternal(view);
        }
        this.mCallback.attachViewToParent(view, n, layoutParams);
    }

    void detachViewFromParent(int n) {
        n = this.getOffset(n);
        this.mBucket.remove(n);
        this.mCallback.detachViewFromParent(n);
    }

    View findHiddenNonRemovedView(int n) {
        int n2 = this.mHiddenViews.size();
        for (int i = 0; i < n2; ++i) {
            View view = this.mHiddenViews.get(i);
            RecyclerView.ViewHolder viewHolder = this.mCallback.getChildViewHolder(view);
            if (viewHolder.getLayoutPosition() != n || viewHolder.isInvalid() || viewHolder.isRemoved()) continue;
            return view;
        }
        return null;
    }

    View getChildAt(int n) {
        n = this.getOffset(n);
        return this.mCallback.getChildAt(n);
    }

    int getChildCount() {
        return this.mCallback.getChildCount() - this.mHiddenViews.size();
    }

    View getUnfilteredChildAt(int n) {
        return this.mCallback.getChildAt(n);
    }

    int getUnfilteredChildCount() {
        return this.mCallback.getChildCount();
    }

    void hide(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n >= 0) {
            this.mBucket.set(n);
            this.hideViewInternal(view);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("view is not a child, cannot hide ");
        stringBuilder.append((Object)view);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    int indexOfChild(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n == -1) {
            return -1;
        }
        if (this.mBucket.get(n)) {
            return -1;
        }
        return n - this.mBucket.countOnesBefore(n);
    }

    boolean isHidden(View view) {
        return this.mHiddenViews.contains((Object)view);
    }

    void removeAllViewsUnfiltered() {
        this.mBucket.reset();
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.mCallback.onLeftHiddenState(this.mHiddenViews.get(i));
            this.mHiddenViews.remove(i);
        }
        this.mCallback.removeAllViews();
    }

    void removeView(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n < 0) {
            return;
        }
        if (this.mBucket.remove(n)) {
            this.unhideViewInternal(view);
        }
        this.mCallback.removeViewAt(n);
    }

    void removeViewAt(int n) {
        View view = this.mCallback.getChildAt(n = this.getOffset(n));
        if (view == null) {
            return;
        }
        if (this.mBucket.remove(n)) {
            this.unhideViewInternal(view);
        }
        this.mCallback.removeViewAt(n);
    }

    boolean removeViewIfHidden(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n == -1) {
            this.unhideViewInternal(view);
            return true;
        }
        if (this.mBucket.get(n)) {
            this.mBucket.remove(n);
            this.unhideViewInternal(view);
            this.mCallback.removeViewAt(n);
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mBucket.toString());
        stringBuilder.append(", hidden list:");
        stringBuilder.append(this.mHiddenViews.size());
        return stringBuilder.toString();
    }

    void unhide(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n >= 0) {
            if (this.mBucket.get(n)) {
                this.mBucket.clear(n);
                this.unhideViewInternal(view);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trying to unhide a view that was not hidden");
            stringBuilder.append((Object)view);
            throw new RuntimeException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("view is not a child, cannot hide ");
        stringBuilder.append((Object)view);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    static class Bucket {
        static final int BITS_PER_WORD = 64;
        static final long LAST_BIT = Long.MIN_VALUE;
        long mData = 0L;
        Bucket mNext;

        Bucket() {
        }

        private void ensureNext() {
            if (this.mNext == null) {
                this.mNext = new Bucket();
                return;
            }
        }

        void clear(int n) {
            if (n >= 64) {
                Bucket bucket = this.mNext;
                if (bucket != null) {
                    bucket.clear(n - 64);
                    return;
                }
                return;
            }
            this.mData &= 1L << n ^ -1L;
        }

        int countOnesBefore(int n) {
            Bucket bucket = this.mNext;
            if (bucket == null) {
                if (n >= 64) {
                    return Long.bitCount(this.mData);
                }
                return Long.bitCount(this.mData & (1L << n) - 1L);
            }
            if (n < 64) {
                return Long.bitCount(this.mData & (1L << n) - 1L);
            }
            return bucket.countOnesBefore(n - 64) + Long.bitCount(this.mData);
        }

        boolean get(int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.mNext.get(n - 64);
            }
            if ((this.mData & 1L << n) != 0L) {
                return true;
            }
            return false;
        }

        void insert(int n, boolean bl) {
            if (n >= 64) {
                this.ensureNext();
                this.mNext.insert(n - 64, bl);
                return;
            }
            boolean bl2 = (this.mData & Long.MIN_VALUE) != 0L;
            long l = (1L << n) - 1L;
            long l2 = this.mData;
            this.mData = l2 & l | (l2 & (-1L ^ l)) << 1;
            if (bl) {
                this.set(n);
            } else {
                this.clear(n);
            }
            if (!bl2 && this.mNext == null) {
                return;
            }
            this.ensureNext();
            this.mNext.insert(0, bl2);
        }

        boolean remove(int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.mNext.remove(n - 64);
            }
            long l = 1L << n;
            boolean bl = (this.mData & l) != 0L;
            this.mData &= l ^ -1L;
            long l2 = this.mData;
            this.mData = l2 & l | Long.rotateRight(l2 & (-1L ^ --l), 1);
            Bucket bucket = this.mNext;
            if (bucket != null) {
                if (bucket.get(0)) {
                    this.set(63);
                }
                this.mNext.remove(0);
                return bl;
            }
            return bl;
        }

        void reset() {
            this.mData = 0L;
            Bucket bucket = this.mNext;
            if (bucket != null) {
                bucket.reset();
                return;
            }
        }

        void set(int n) {
            if (n >= 64) {
                this.ensureNext();
                this.mNext.set(n - 64);
                return;
            }
            this.mData |= 1L << n;
        }

        public String toString() {
            if (this.mNext == null) {
                return Long.toBinaryString(this.mData);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mNext.toString());
            stringBuilder.append("xx");
            stringBuilder.append(Long.toBinaryString(this.mData));
            return stringBuilder.toString();
        }
    }

    static interface Callback {
        public void addView(View var1, int var2);

        public void attachViewToParent(View var1, int var2, ViewGroup.LayoutParams var3);

        public void detachViewFromParent(int var1);

        public View getChildAt(int var1);

        public int getChildCount();

        public RecyclerView.ViewHolder getChildViewHolder(View var1);

        public int indexOfChild(View var1);

        public void onEnteredHiddenState(View var1);

        public void onLeftHiddenState(View var1);

        public void removeAllViews();

        public void removeViewAt(int var1);
    }

}

