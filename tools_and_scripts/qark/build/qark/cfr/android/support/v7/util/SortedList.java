/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.util;

import android.support.v7.util.BatchingListUpdateCallback;
import android.support.v7.util.ListUpdateCallback;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mMergedSize;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public SortedList(Class<T> class_, Callback<T> callback) {
        this(class_, callback, 10);
    }

    public SortedList(Class<T> class_, Callback<T> callback, int n) {
        this.mTClass = class_;
        this.mData = (Object[])Array.newInstance(class_, n);
        this.mCallback = callback;
        this.mSize = 0;
    }

    private int add(T t, boolean bl) {
        T t2;
        int n = this.findIndexOf(t, this.mData, 0, this.mSize, 1);
        if (n == -1) {
            n = 0;
        } else if (n < this.mSize && this.mCallback.areItemsTheSame(t2 = this.mData[n], t)) {
            if (this.mCallback.areContentsTheSame(t2, t)) {
                this.mData[n] = t;
                return n;
            }
            this.mData[n] = t;
            this.mCallback.onChanged(n, 1);
            return n;
        }
        this.addToData(n, t);
        if (bl) {
            this.mCallback.onInserted(n, 1);
            return n;
        }
        return n;
    }

    private void addAllInternal(T[] arrT) {
        boolean bl = !(this.mCallback instanceof BatchedCallback);
        if (bl) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        Arrays.sort(arrT, this.mCallback);
        int n = this.deduplicate(arrT);
        if (this.mSize == 0) {
            this.mData = arrT;
            this.mSize = n;
            this.mMergedSize = n;
            this.mCallback.onInserted(0, n);
        } else {
            this.merge(arrT, n);
        }
        this.mOldData = null;
        if (bl) {
            this.endBatchedUpdates();
            return;
        }
    }

    private void addToData(int n, T object) {
        int n2 = this.mSize;
        if (n <= n2) {
            T[] arrT = this.mData;
            if (n2 == arrT.length) {
                arrT = (Object[])Array.newInstance(this.mTClass, arrT.length + 10);
                System.arraycopy(this.mData, 0, arrT, 0, n);
                arrT[n] = object;
                System.arraycopy(this.mData, n, arrT, n + 1, this.mSize - n);
                this.mData = arrT;
            } else {
                System.arraycopy(arrT, n, arrT, n + 1, n2 - n);
                this.mData[n] = object;
            }
            ++this.mSize;
            return;
        }
        object = new StringBuilder();
        object.append("cannot add item to ");
        object.append(n);
        object.append(" because size is ");
        object.append(this.mSize);
        throw new IndexOutOfBoundsException(object.toString());
    }

    private int deduplicate(T[] arrT) {
        if (arrT.length != 0) {
            int n = 0;
            int n2 = 1;
            for (int i = 1; i < arrT.length; ++i) {
                T t = arrT[i];
                int n3 = this.mCallback.compare(arrT[n], t);
                if (n3 <= 0) {
                    if (n3 == 0) {
                        n3 = this.findSameItem(t, arrT, n, n2);
                        if (n3 != -1) {
                            arrT[n3] = t;
                            continue;
                        }
                        if (n2 != i) {
                            arrT[n2] = t;
                        }
                        ++n2;
                        continue;
                    }
                    if (n2 != i) {
                        arrT[n2] = t;
                    }
                    n = n2++;
                    continue;
                }
                throw new IllegalArgumentException("Input must be sorted in ascending order.");
            }
            return n2;
        }
        throw new IllegalArgumentException("Input array must be non-empty");
    }

    private int findIndexOf(T t, T[] arrT, int n, int n2, int n3) {
        int n4;
        do {
            n4 = -1;
            if (n >= n2) break;
            n4 = (n + n2) / 2;
            T t2 = arrT[n4];
            int n5 = this.mCallback.compare(t2, t);
            if (n5 < 0) {
                n = n4 + 1;
                continue;
            }
            if (n5 == 0) {
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    return n4;
                }
                n = this.linearEqualitySearch(t, n4, n, n2);
                if (n3 == 1) {
                    if (n == -1) {
                        return n4;
                    }
                    return n;
                }
                return n;
            }
            n2 = n4;
        } while (true);
        n2 = n4;
        if (n3 == 1) {
            n2 = n;
        }
        return n2;
    }

    private int findSameItem(T t, T[] arrT, int n, int n2) {
        while (n < n2) {
            if (this.mCallback.areItemsTheSame(arrT[n], t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private int linearEqualitySearch(T t, int n, int n2, int n3) {
        T t2;
        for (int i = n - 1; i >= n2 && this.mCallback.compare(t2 = this.mData[i], t) == 0; --i) {
            if (!this.mCallback.areItemsTheSame(t2, t)) continue;
            return i;
        }
        ++n;
        while (n < n3 && this.mCallback.compare(t2 = this.mData[n], t) == 0) {
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private void merge(T[] arrT, int n) {
        int n2 = this.mSize;
        this.mData = (Object[])Array.newInstance(this.mTClass, n2 + n + 10);
        this.mMergedSize = 0;
        n2 = 0;
        while (this.mOldDataStart < this.mOldDataSize || n2 < n) {
            int n3 = this.mOldDataStart;
            int n4 = this.mOldDataSize;
            if (n3 == n4) {
                System.arraycopy(arrT, n2, this.mData, this.mMergedSize, n -= n2);
                this.mMergedSize += n;
                this.mSize += n;
                this.mCallback.onInserted(this.mMergedSize - n, n);
                return;
            }
            if (n2 == n) {
                n = n4 - n3;
                System.arraycopy(this.mOldData, n3, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                return;
            }
            Object object = this.mOldData[n3];
            Object object2 = arrT[n2];
            n3 = this.mCallback.compare(object, object2);
            if (n3 > 0) {
                object = this.mData;
                n3 = this.mMergedSize;
                this.mMergedSize = n3 + 1;
                object[n3] = object2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(this.mMergedSize - 1, 1);
                continue;
            }
            if (n3 == 0 && this.mCallback.areItemsTheSame(object, object2)) {
                T[] arrT2 = this.mData;
                n3 = this.mMergedSize;
                this.mMergedSize = n3 + 1;
                arrT2[n3] = object2;
                ++n2;
                ++this.mOldDataStart;
                if (this.mCallback.areContentsTheSame(object, object2)) continue;
                this.mCallback.onChanged(this.mMergedSize - 1, 1);
                continue;
            }
            object2 = this.mData;
            n3 = this.mMergedSize;
            this.mMergedSize = n3 + 1;
            object2[n3] = object;
            ++this.mOldDataStart;
        }
        return;
    }

    private boolean remove(T t, boolean bl) {
        int n = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (n == -1) {
            return false;
        }
        this.removeItemAtIndex(n, bl);
        return true;
    }

    private void removeItemAtIndex(int n, boolean bl) {
        T[] arrT = this.mData;
        System.arraycopy(arrT, n + 1, arrT, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (bl) {
            this.mCallback.onRemoved(n, 1);
            return;
        }
    }

    private void throwIfMerging() {
        if (this.mOldData == null) {
            return;
        }
        throw new IllegalStateException("Cannot call this method from within addAll");
    }

    public int add(T t) {
        this.throwIfMerging();
        return this.add(t, true);
    }

    public void addAll(Collection<T> collection) {
        this.addAll((T[])collection.toArray((Object[])Array.newInstance(this.mTClass, collection.size())), true);
    }

    public /* varargs */ void addAll(T ... arrT) {
        this.addAll(arrT, false);
    }

    public void addAll(T[] arrT, boolean bl) {
        this.throwIfMerging();
        if (arrT.length == 0) {
            return;
        }
        if (bl) {
            this.addAllInternal(arrT);
            return;
        }
        Object[] arrobject = (Object[])Array.newInstance(this.mTClass, arrT.length);
        System.arraycopy(arrT, 0, arrobject, 0, arrT.length);
        this.addAllInternal(arrobject);
    }

    public void beginBatchedUpdates() {
        this.throwIfMerging();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(callback);
        }
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        this.throwIfMerging();
        if (this.mSize == 0) {
            return;
        }
        int n = this.mSize;
        Arrays.fill(this.mData, 0, n, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, n);
    }

    public void endBatchedUpdates() {
        BatchedCallback batchedCallback;
        this.throwIfMerging();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            ((BatchedCallback)callback).dispatchLastEvent();
        }
        if ((callback = this.mCallback) == (batchedCallback = this.mBatchedCallback)) {
            this.mCallback = batchedCallback.mWrappedCallback;
            return;
        }
    }

    public T get(int n) throws IndexOutOfBoundsException {
        if (n < this.mSize && n >= 0) {
            int n2;
            T[] arrT = this.mOldData;
            if (arrT != null && n >= (n2 = this.mMergedSize)) {
                return arrT[n - n2 + this.mOldDataStart];
            }
            return this.mData[n];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Asked to get item at ");
        stringBuilder.append(n);
        stringBuilder.append(" but size is ");
        stringBuilder.append(this.mSize);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int indexOf(T t) {
        if (this.mOldData != null) {
            int n = this.findIndexOf(t, this.mData, 0, this.mMergedSize, 4);
            if (n != -1) {
                return n;
            }
            n = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (n != -1) {
                return n - this.mOldDataStart + this.mMergedSize;
            }
            return -1;
        }
        return this.findIndexOf(t, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int n) {
        this.throwIfMerging();
        T t = this.get(n);
        this.removeItemAtIndex(n, false);
        int n2 = this.add(t, false);
        if (n != n2) {
            this.mCallback.onMoved(n, n2);
            return;
        }
    }

    public boolean remove(T t) {
        this.throwIfMerging();
        return this.remove(t, true);
    }

    public T removeItemAt(int n) {
        this.throwIfMerging();
        T t = this.get(n);
        this.removeItemAtIndex(n, true);
        return t;
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int n, T t) {
        this.throwIfMerging();
        T t2 = this.get(n);
        int n2 = t2 != t && this.mCallback.areContentsTheSame(t2, t) ? 0 : 1;
        if (t2 != t && this.mCallback.compare(t2, t) == 0) {
            this.mData[n] = t;
            if (n2 != 0) {
                this.mCallback.onChanged(n, 1);
                return;
            }
            return;
        }
        if (n2 != 0) {
            this.mCallback.onChanged(n, 1);
        }
        this.removeItemAtIndex(n, false);
        n2 = this.add(t, false);
        if (n != n2) {
            this.mCallback.onMoved(n, n2);
            return;
        }
    }

    public static class BatchedCallback<T2>
    extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        }

        @Override
        public boolean areContentsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areContentsTheSame(T2, T22);
        }

        @Override
        public boolean areItemsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areItemsTheSame(T2, T22);
        }

        @Override
        public int compare(T2 T2, T2 T22) {
            return this.mWrappedCallback.compare(T2, T22);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        @Override
        public void onChanged(int n, int n2) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, null);
        }

        @Override
        public void onInserted(int n, int n2) {
            this.mBatchingListUpdateCallback.onInserted(n, n2);
        }

        @Override
        public void onMoved(int n, int n2) {
            this.mBatchingListUpdateCallback.onMoved(n, n2);
        }

        @Override
        public void onRemoved(int n, int n2) {
            this.mBatchingListUpdateCallback.onRemoved(n, n2);
        }
    }

    public static abstract class Callback<T2>
    implements Comparator<T2>,
    ListUpdateCallback {
        public abstract boolean areContentsTheSame(T2 var1, T2 var2);

        public abstract boolean areItemsTheSame(T2 var1, T2 var2);

        @Override
        public abstract int compare(T2 var1, T2 var2);

        public abstract void onChanged(int var1, int var2);

        @Override
        public void onChanged(int n, int n2, Object object) {
            this.onChanged(n, n2);
        }
    }

}

