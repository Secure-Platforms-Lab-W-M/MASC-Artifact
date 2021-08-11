// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

public class SparseArrayCompat<E> implements Cloneable
{
    private static final Object DELETED;
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;
    
    static {
        DELETED = new Object();
    }
    
    public SparseArrayCompat() {
        this(10);
    }
    
    public SparseArrayCompat(int idealIntArraySize) {
        this.mGarbage = false;
        if (idealIntArraySize == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            idealIntArraySize = ContainerHelpers.idealIntArraySize(idealIntArraySize);
            this.mKeys = new int[idealIntArraySize];
            this.mValues = new Object[idealIntArraySize];
        }
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        int mSize2 = 0;
        final int[] mKeys = this.mKeys;
        final Object[] mValues = this.mValues;
        for (int i = 0; i < mSize; ++i) {
            final Object o = mValues[i];
            if (o != SparseArrayCompat.DELETED) {
                if (i != mSize2) {
                    mKeys[mSize2] = mKeys[i];
                    mValues[mSize2] = o;
                    mValues[i] = null;
                }
                ++mSize2;
            }
        }
        this.mGarbage = false;
        this.mSize = mSize2;
    }
    
    public void append(final int n, final E e) {
        final int mSize = this.mSize;
        if (mSize != 0 && n <= this.mKeys[mSize - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        final int mSize2 = this.mSize;
        if (mSize2 >= this.mKeys.length) {
            final int idealIntArraySize = ContainerHelpers.idealIntArraySize(mSize2 + 1);
            final int[] mKeys = new int[idealIntArraySize];
            final Object[] mValues = new Object[idealIntArraySize];
            final int[] mKeys2 = this.mKeys;
            System.arraycopy(mKeys2, 0, mKeys, 0, mKeys2.length);
            final Object[] mValues2 = this.mValues;
            System.arraycopy(mValues2, 0, mValues, 0, mValues2.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        this.mKeys[mSize2] = n;
        this.mValues[mSize2] = e;
        this.mSize = mSize2 + 1;
    }
    
    public void clear() {
        final int mSize = this.mSize;
        final Object[] mValues = this.mValues;
        for (int i = 0; i < mSize; ++i) {
            mValues[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }
    
    public SparseArrayCompat<E> clone() {
        SparseArrayCompat<E> sparseArrayCompat = null;
        try {
            final SparseArrayCompat sparseArrayCompat2 = sparseArrayCompat = (SparseArrayCompat)super.clone();
            sparseArrayCompat2.mKeys = this.mKeys.clone();
            sparseArrayCompat = sparseArrayCompat2;
            sparseArrayCompat2.mValues = this.mValues.clone();
            return sparseArrayCompat2;
        }
        catch (CloneNotSupportedException ex) {
            return sparseArrayCompat;
        }
    }
    
    public void delete(int binarySearch) {
        binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, binarySearch);
        if (binarySearch < 0) {
            return;
        }
        final Object[] mValues = this.mValues;
        final Object o = mValues[binarySearch];
        final Object deleted = SparseArrayCompat.DELETED;
        if (o != deleted) {
            mValues[binarySearch] = deleted;
            this.mGarbage = true;
        }
    }
    
    public E get(final int n) {
        return this.get(n, null);
    }
    
    public E get(int binarySearch, final E e) {
        binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, binarySearch);
        if (binarySearch < 0) {
            return e;
        }
        final Object[] mValues = this.mValues;
        if (mValues[binarySearch] == SparseArrayCompat.DELETED) {
            return e;
        }
        return (E)mValues[binarySearch];
    }
    
    public int indexOfKey(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
    }
    
    public int indexOfValue(final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] == e) {
                return i;
            }
        }
        return -1;
    }
    
    public int keyAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }
    
    public void put(final int n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
            return;
        }
        int n2 = ~binarySearch;
        if (n2 < this.mSize) {
            final Object[] mValues = this.mValues;
            if (mValues[n2] == SparseArrayCompat.DELETED) {
                this.mKeys[n2] = n;
                mValues[n2] = e;
                return;
            }
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
            n2 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        }
        final int mSize = this.mSize;
        if (mSize >= this.mKeys.length) {
            final int idealIntArraySize = ContainerHelpers.idealIntArraySize(mSize + 1);
            final int[] mKeys = new int[idealIntArraySize];
            final Object[] mValues2 = new Object[idealIntArraySize];
            final int[] mKeys2 = this.mKeys;
            System.arraycopy(mKeys2, 0, mKeys, 0, mKeys2.length);
            final Object[] mValues3 = this.mValues;
            System.arraycopy(mValues3, 0, mValues2, 0, mValues3.length);
            this.mKeys = mKeys;
            this.mValues = mValues2;
        }
        final int mSize2 = this.mSize;
        if (mSize2 - n2 != 0) {
            final int[] mKeys3 = this.mKeys;
            System.arraycopy(mKeys3, n2, mKeys3, n2 + 1, mSize2 - n2);
            final Object[] mValues4 = this.mValues;
            System.arraycopy(mValues4, n2, mValues4, n2 + 1, this.mSize - n2);
        }
        this.mKeys[n2] = n;
        this.mValues[n2] = e;
        ++this.mSize;
    }
    
    public void remove(final int n) {
        this.delete(n);
    }
    
    public void removeAt(final int n) {
        final Object[] mValues = this.mValues;
        final Object o = mValues[n];
        final Object deleted = SparseArrayCompat.DELETED;
        if (o != deleted) {
            mValues[n] = deleted;
            this.mGarbage = true;
        }
    }
    
    public void removeAtRange(int i, int min) {
        for (min = Math.min(this.mSize, i + min); i < min; ++i) {
            this.removeAt(i);
        }
    }
    
    public void setValueAt(final int n, final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[n] = e;
    }
    
    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.keyAt(i));
            sb.append('=');
            final E value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public E valueAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}
