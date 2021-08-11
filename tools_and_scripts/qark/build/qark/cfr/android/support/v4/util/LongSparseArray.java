/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

import android.support.v4.util.ContainerHelpers;

public class LongSparseArray<E>
implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int n) {
        if (n == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            n = ContainerHelpers.idealLongArraySize(n);
            this.mKeys = new long[n];
            this.mValues = new Object[n];
        }
        this.mSize = 0;
    }

    private void gc() {
        int n = this.mSize;
        int n2 = 0;
        long[] arrl = this.mKeys;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            Object object = arrobject[i];
            if (object == DELETED) continue;
            if (i != n2) {
                arrl[n2] = arrl[i];
                arrobject[n2] = object;
                arrobject[i] = null;
            }
            ++n2;
        }
        this.mGarbage = false;
        this.mSize = n2;
    }

    public void append(long l, E e) {
        int n = this.mSize;
        if (n != 0 && l <= this.mKeys[n - 1]) {
            this.put(l, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        if ((n = this.mSize) >= this.mKeys.length) {
            int n2 = ContainerHelpers.idealLongArraySize(n + 1);
            long[] arrl = new long[n2];
            Object[] arrobject = new Object[n2];
            long[] arrl2 = this.mKeys;
            System.arraycopy(arrl2, 0, arrl, 0, arrl2.length);
            arrl2 = this.mValues;
            System.arraycopy(arrl2, 0, arrobject, 0, arrl2.length);
            this.mKeys = arrl;
            this.mValues = arrobject;
        }
        this.mKeys[n] = l;
        this.mValues[n] = e;
        this.mSize = n + 1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            arrobject[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArray<E> clone() {
        LongSparseArray longSparseArray;
        LongSparseArray longSparseArray2 = null;
        try {
            longSparseArray2 = longSparseArray = (LongSparseArray)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return longSparseArray2;
        }
        longSparseArray.mKeys = (long[])this.mKeys.clone();
        longSparseArray2 = longSparseArray;
        longSparseArray.mValues = (Object[])this.mValues.clone();
        return longSparseArray;
    }

    public void delete(long l) {
        int n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n >= 0) {
            Object[] arrobject = this.mValues;
            Object object = arrobject[n];
            Object object2 = DELETED;
            if (object != object2) {
                arrobject[n] = object2;
                this.mGarbage = true;
                return;
            }
            return;
        }
    }

    public E get(long l) {
        return this.get(l, null);
    }

    public E get(long l, E e) {
        int n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n >= 0) {
            Object[] arrobject = this.mValues;
            if (arrobject[n] == DELETED) {
                return e;
            }
            return (E)arrobject[n];
        }
        return e;
    }

    public int indexOfKey(long l) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
    }

    public int indexOfValue(E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] != e) continue;
            return i;
        }
        return -1;
    }

    public long keyAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }

    public void put(long l, E e) {
        int n;
        Object[] arrobject;
        int n2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        if (n2 >= 0) {
            this.mValues[n2] = e;
            return;
        }
        if ((n2 ^= -1) < this.mSize && (arrobject = this.mValues)[n2] == DELETED) {
            this.mKeys[n2] = l;
            arrobject[n2] = e;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
            n2 = ~ ContainerHelpers.binarySearch(this.mKeys, this.mSize, l);
        }
        if ((n = this.mSize) >= this.mKeys.length) {
            n = ContainerHelpers.idealLongArraySize(n + 1);
            arrobject = new long[n];
            Object[] arrobject2 = new Object[n];
            long[] arrl = this.mKeys;
            System.arraycopy(arrl, 0, arrobject, 0, arrl.length);
            arrl = this.mValues;
            System.arraycopy(arrl, 0, arrobject2, 0, arrl.length);
            this.mKeys = arrobject;
            this.mValues = arrobject2;
        }
        if ((n = this.mSize) - n2 != 0) {
            arrobject = this.mKeys;
            System.arraycopy(arrobject, n2, arrobject, n2 + 1, n - n2);
            arrobject = this.mValues;
            System.arraycopy(arrobject, n2, arrobject, n2 + 1, this.mSize - n2);
        }
        this.mKeys[n2] = l;
        this.mValues[n2] = e;
        ++this.mSize;
    }

    public void remove(long l) {
        this.delete(l);
    }

    public void removeAt(int n) {
        Object[] arrobject = this.mValues;
        Object object = arrobject[n];
        Object object2 = DELETED;
        if (object != object2) {
            arrobject[n] = object2;
            this.mGarbage = true;
            return;
        }
    }

    public void setValueAt(int n, E e) {
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

    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(this.keyAt(i));
            stringBuilder.append('=');
            E e = this.valueAt(i);
            if (e != this) {
                stringBuilder.append(e);
                continue;
            }
            stringBuilder.append("(this Map)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public E valueAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}

