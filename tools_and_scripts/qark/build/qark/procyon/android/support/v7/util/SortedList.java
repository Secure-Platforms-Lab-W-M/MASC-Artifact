// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Arrays;
import java.lang.reflect.Array;

public class SortedList<T>
{
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
    
    public SortedList(final Class<T> clazz, final Callback<T> callback) {
        this(clazz, callback, 10);
    }
    
    public SortedList(final Class<T> mtClass, final Callback<T> mCallback, final int n) {
        this.mTClass = mtClass;
        this.mData = (T[])Array.newInstance(mtClass, n);
        this.mCallback = mCallback;
        this.mSize = 0;
    }
    
    private int add(final T t, final boolean b) {
        int index = this.findIndexOf(t, this.mData, 0, this.mSize, 1);
        if (index == -1) {
            index = 0;
        }
        else if (index < this.mSize) {
            final T t2 = this.mData[index];
            if (this.mCallback.areItemsTheSame(t2, t)) {
                if (this.mCallback.areContentsTheSame(t2, t)) {
                    this.mData[index] = t;
                    return index;
                }
                this.mData[index] = t;
                this.mCallback.onChanged(index, 1);
                return index;
            }
        }
        this.addToData(index, t);
        if (b) {
            this.mCallback.onInserted(index, 1);
            return index;
        }
        return index;
    }
    
    private void addAllInternal(final T[] mData) {
        final boolean b = !(this.mCallback instanceof BatchedCallback);
        if (b) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        Arrays.sort(mData, this.mCallback);
        final int deduplicate = this.deduplicate(mData);
        if (this.mSize == 0) {
            this.mData = mData;
            this.mSize = deduplicate;
            this.mMergedSize = deduplicate;
            this.mCallback.onInserted(0, deduplicate);
        }
        else {
            this.merge(mData, deduplicate);
        }
        this.mOldData = null;
        if (b) {
            this.endBatchedUpdates();
        }
    }
    
    private void addToData(final int n, final T t) {
        final int mSize = this.mSize;
        if (n <= mSize) {
            final T[] mData = this.mData;
            if (mSize == mData.length) {
                final Object[] mData2 = (Object[])Array.newInstance(this.mTClass, mData.length + 10);
                System.arraycopy(this.mData, 0, mData2, 0, n);
                mData2[n] = t;
                System.arraycopy(this.mData, n, mData2, n + 1, this.mSize - n);
                this.mData = (T[])mData2;
            }
            else {
                System.arraycopy(mData, n, mData, n + 1, mSize - n);
                this.mData[n] = t;
            }
            ++this.mSize;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot add item to ");
        sb.append(n);
        sb.append(" because size is ");
        sb.append(this.mSize);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    private int deduplicate(final T[] array) {
        if (array.length != 0) {
            int n = 0;
            int n2 = 1;
            for (int i = 1; i < array.length; ++i) {
                final T t = array[i];
                final int compare = this.mCallback.compare(array[n], t);
                if (compare > 0) {
                    throw new IllegalArgumentException("Input must be sorted in ascending order.");
                }
                if (compare == 0) {
                    final int sameItem = this.findSameItem(t, array, n, n2);
                    if (sameItem != -1) {
                        array[sameItem] = t;
                    }
                    else {
                        if (n2 != i) {
                            array[n2] = t;
                        }
                        ++n2;
                    }
                }
                else {
                    if (n2 != i) {
                        array[n2] = t;
                    }
                    n = n2;
                    ++n2;
                }
            }
            return n2;
        }
        throw new IllegalArgumentException("Input array must be non-empty");
    }
    
    private int findIndexOf(final T t, final T[] array, int linearEqualitySearch, int n, final int n2) {
        while (true) {
            final int n3 = -1;
            if (linearEqualitySearch >= n) {
                n = n3;
                if (n2 == 1) {
                    n = linearEqualitySearch;
                }
                return n;
            }
            final int n4 = (linearEqualitySearch + n) / 2;
            final T t2 = array[n4];
            final int compare = this.mCallback.compare(t2, t);
            if (compare < 0) {
                linearEqualitySearch = n4 + 1;
            }
            else if (compare == 0) {
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    return n4;
                }
                linearEqualitySearch = this.linearEqualitySearch(t, n4, linearEqualitySearch, n);
                if (n2 != 1) {
                    return linearEqualitySearch;
                }
                if (linearEqualitySearch == -1) {
                    return n4;
                }
                return linearEqualitySearch;
            }
            else {
                n = n4;
            }
        }
    }
    
    private int findSameItem(final T t, final T[] array, int i, final int n) {
        while (i < n) {
            if (this.mCallback.areItemsTheSame(array[i], t)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private int linearEqualitySearch(final T t, int i, final int n, final int n2) {
        for (int j = i - 1; j >= n; --j) {
            final T t2 = this.mData[j];
            if (this.mCallback.compare(t2, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return j;
            }
        }
        T t3;
        for (++i; i < n2; ++i) {
            t3 = this.mData[i];
            if (this.mCallback.compare(t3, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(t3, t)) {
                return i;
            }
        }
        return -1;
    }
    
    private void merge(final T[] array, int n) {
        this.mData = (T[])Array.newInstance(this.mTClass, this.mSize + n + 10);
        this.mMergedSize = 0;
        int n2 = 0;
        while (this.mOldDataStart < this.mOldDataSize || n2 < n) {
            final int mOldDataStart = this.mOldDataStart;
            final int mOldDataSize = this.mOldDataSize;
            if (mOldDataStart == mOldDataSize) {
                n -= n2;
                System.arraycopy(array, n2, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                this.mSize += n;
                this.mCallback.onInserted(this.mMergedSize - n, n);
                return;
            }
            if (n2 == n) {
                n = mOldDataSize - mOldDataStart;
                System.arraycopy(this.mOldData, mOldDataStart, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                return;
            }
            final T t = this.mOldData[mOldDataStart];
            final T t2 = array[n2];
            final int compare = this.mCallback.compare(t, t2);
            if (compare > 0) {
                this.mData[this.mMergedSize++] = t2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(this.mMergedSize - 1, 1);
            }
            else if (compare == 0 && this.mCallback.areItemsTheSame(t, t2)) {
                this.mData[this.mMergedSize++] = t2;
                ++n2;
                ++this.mOldDataStart;
                if (this.mCallback.areContentsTheSame(t, t2)) {
                    continue;
                }
                this.mCallback.onChanged(this.mMergedSize - 1, 1);
            }
            else {
                this.mData[this.mMergedSize++] = t;
                ++this.mOldDataStart;
            }
        }
    }
    
    private boolean remove(final T t, final boolean b) {
        final int index = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (index == -1) {
            return false;
        }
        this.removeItemAtIndex(index, b);
        return true;
    }
    
    private void removeItemAtIndex(final int n, final boolean b) {
        final T[] mData = this.mData;
        System.arraycopy(mData, n + 1, mData, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (b) {
            this.mCallback.onRemoved(n, 1);
        }
    }
    
    private void throwIfMerging() {
        if (this.mOldData == null) {
            return;
        }
        throw new IllegalStateException("Cannot call this method from within addAll");
    }
    
    public int add(final T t) {
        this.throwIfMerging();
        return this.add(t, true);
    }
    
    public void addAll(final Collection<T> collection) {
        this.addAll(collection.toArray((T[])Array.newInstance(this.mTClass, collection.size())), true);
    }
    
    public void addAll(final T... array) {
        this.addAll(array, false);
    }
    
    public void addAll(final T[] array, final boolean b) {
        this.throwIfMerging();
        if (array.length == 0) {
            return;
        }
        if (b) {
            this.addAllInternal(array);
            return;
        }
        final Object[] array2 = (Object[])Array.newInstance(this.mTClass, array.length);
        System.arraycopy(array, 0, array2, 0, array.length);
        this.addAllInternal((T[])array2);
    }
    
    public void beginBatchedUpdates() {
        this.throwIfMerging();
        final Callback mCallback = this.mCallback;
        if (mCallback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(mCallback);
        }
        this.mCallback = (Callback)this.mBatchedCallback;
    }
    
    public void clear() {
        this.throwIfMerging();
        if (this.mSize == 0) {
            return;
        }
        final int mSize = this.mSize;
        Arrays.fill(this.mData, 0, mSize, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, mSize);
    }
    
    public void endBatchedUpdates() {
        this.throwIfMerging();
        final Callback mCallback = this.mCallback;
        if (mCallback instanceof BatchedCallback) {
            ((BatchedCallback)mCallback).dispatchLastEvent();
        }
        final Callback mCallback2 = this.mCallback;
        final BatchedCallback mBatchedCallback = this.mBatchedCallback;
        if (mCallback2 == mBatchedCallback) {
            this.mCallback = (Callback)mBatchedCallback.mWrappedCallback;
        }
    }
    
    public T get(final int n) throws IndexOutOfBoundsException {
        if (n < this.mSize && n >= 0) {
            final T[] mOldData = this.mOldData;
            if (mOldData != null) {
                final int mMergedSize = this.mMergedSize;
                if (n >= mMergedSize) {
                    return mOldData[n - mMergedSize + this.mOldDataStart];
                }
            }
            return this.mData[n];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Asked to get item at ");
        sb.append(n);
        sb.append(" but size is ");
        sb.append(this.mSize);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public int indexOf(final T t) {
        if (this.mOldData == null) {
            return this.findIndexOf(t, this.mData, 0, this.mSize, 4);
        }
        final int index = this.findIndexOf(t, this.mData, 0, this.mMergedSize, 4);
        if (index != -1) {
            return index;
        }
        final int index2 = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
        if (index2 != -1) {
            return index2 - this.mOldDataStart + this.mMergedSize;
        }
        return -1;
    }
    
    public void recalculatePositionOfItemAt(final int n) {
        this.throwIfMerging();
        final T value = this.get(n);
        this.removeItemAtIndex(n, false);
        final int add = this.add(value, false);
        if (n != add) {
            this.mCallback.onMoved(n, add);
        }
    }
    
    public boolean remove(final T t) {
        this.throwIfMerging();
        return this.remove(t, true);
    }
    
    public T removeItemAt(final int n) {
        this.throwIfMerging();
        final T value = this.get(n);
        this.removeItemAtIndex(n, true);
        return value;
    }
    
    public int size() {
        return this.mSize;
    }
    
    public void updateItemAt(final int n, final T t) {
        this.throwIfMerging();
        final T value = this.get(n);
        final boolean b = value == t || !this.mCallback.areContentsTheSame(value, t);
        if (value != t && this.mCallback.compare(value, t) == 0) {
            this.mData[n] = t;
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
        }
        else {
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
            this.removeItemAtIndex(n, false);
            final int add = this.add(t, false);
            if (n != add) {
                this.mCallback.onMoved(n, add);
            }
        }
    }
    
    public static class BatchedCallback<T2> extends Callback<T2>
    {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;
        
        public BatchedCallback(final Callback<T2> mWrappedCallback) {
            this.mWrappedCallback = mWrappedCallback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        }
        
        @Override
        public boolean areContentsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areContentsTheSame(t2, t3);
        }
        
        @Override
        public boolean areItemsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areItemsTheSame(t2, t3);
        }
        
        @Override
        public int compare(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.compare(t2, t3);
        }
        
        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }
        
        @Override
        public void onChanged(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, null);
        }
        
        @Override
        public void onInserted(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onInserted(n, n2);
        }
        
        @Override
        public void onMoved(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onMoved(n, n2);
        }
        
        @Override
        public void onRemoved(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onRemoved(n, n2);
        }
    }
    
    public abstract static class Callback<T2> implements Comparator<T2>, ListUpdateCallback
    {
        public abstract boolean areContentsTheSame(final T2 p0, final T2 p1);
        
        public abstract boolean areItemsTheSame(final T2 p0, final T2 p1);
        
        @Override
        public abstract int compare(final T2 p0, final T2 p1);
        
        public abstract void onChanged(final int p0, final int p1);
        
        @Override
        public void onChanged(final int n, final int n2, final Object o) {
            this.onChanged(n, n2);
        }
    }
}
