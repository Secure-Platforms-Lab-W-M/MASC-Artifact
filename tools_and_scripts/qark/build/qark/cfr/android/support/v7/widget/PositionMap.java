/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.widget;

import java.util.ArrayList;

class PositionMap<E>
implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    PositionMap() {
        this(10);
    }

    PositionMap(int n) {
        if (n == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            n = PositionMap.idealIntArraySize(n);
            this.mKeys = new int[n];
            this.mValues = new Object[n];
        }
        this.mSize = 0;
    }

    private void gc() {
        int n = this.mSize;
        int n2 = 0;
        int[] arrn = this.mKeys;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            Object object = arrobject[i];
            if (object == DELETED) continue;
            if (i != n2) {
                arrn[n2] = arrn[i];
                arrobject[n2] = object;
                arrobject[i] = null;
            }
            ++n2;
        }
        this.mGarbage = false;
        this.mSize = n2;
    }

    static int idealBooleanArraySize(int n) {
        return PositionMap.idealByteArraySize(n);
    }

    static int idealByteArraySize(int n) {
        for (int i = 4; i < 32; ++i) {
            if (n > (1 << i) - 12) continue;
            return (1 << i) - 12;
        }
        return n;
    }

    static int idealCharArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 2) / 2;
    }

    static int idealFloatArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealIntArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealLongArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 8) / 8;
    }

    static int idealObjectArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealShortArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 2) / 2;
    }

    public void append(int n, E e) {
        int n2 = this.mSize;
        if (n2 != 0 && n <= this.mKeys[n2 - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        if ((n2 = this.mSize) >= this.mKeys.length) {
            int n3 = PositionMap.idealIntArraySize(n2 + 1);
            int[] arrn = new int[n3];
            Object[] arrobject = new Object[n3];
            int[] arrn2 = this.mKeys;
            System.arraycopy(arrn2, 0, arrn, 0, arrn2.length);
            arrn2 = this.mValues;
            System.arraycopy(arrn2, 0, arrobject, 0, arrn2.length);
            this.mKeys = arrn;
            this.mValues = arrobject;
        }
        this.mKeys[n2] = n;
        this.mValues[n2] = e;
        this.mSize = n2 + 1;
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

    public PositionMap<E> clone() {
        PositionMap positionMap;
        PositionMap positionMap2 = null;
        try {
            positionMap2 = positionMap = (PositionMap)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return positionMap2;
        }
        positionMap.mKeys = (int[])this.mKeys.clone();
        positionMap2 = positionMap;
        positionMap.mValues = (Object[])this.mValues.clone();
        return positionMap;
    }

    public void delete(int n) {
        if ((n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n)) >= 0) {
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

    public E get(int n) {
        return this.get(n, null);
    }

    public E get(int n, E e) {
        if ((n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n)) >= 0) {
            Object[] arrobject = this.mValues;
            if (arrobject[n] == DELETED) {
                return e;
            }
            return (E)arrobject[n];
        }
        return e;
    }

    public int indexOfKey(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
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

    public void insertKeyRange(int n, int n2) {
    }

    public int keyAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }

    public void put(int n, E e) {
        int n2;
        Object[] arrobject;
        int n3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (n3 >= 0) {
            this.mValues[n3] = e;
            return;
        }
        if ((n3 ^= -1) < this.mSize && (arrobject = this.mValues)[n3] == DELETED) {
            this.mKeys[n3] = n;
            arrobject[n3] = e;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
            n3 = ~ ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        }
        if ((n2 = this.mSize) >= this.mKeys.length) {
            n2 = PositionMap.idealIntArraySize(n2 + 1);
            arrobject = new int[n2];
            Object[] arrobject2 = new Object[n2];
            int[] arrn = this.mKeys;
            System.arraycopy(arrn, 0, arrobject, 0, arrn.length);
            arrn = this.mValues;
            System.arraycopy(arrn, 0, arrobject2, 0, arrn.length);
            this.mKeys = arrobject;
            this.mValues = arrobject2;
        }
        if ((n2 = this.mSize) - n3 != 0) {
            arrobject = this.mKeys;
            System.arraycopy(arrobject, n3, arrobject, n3 + 1, n2 - n3);
            arrobject = this.mValues;
            System.arraycopy(arrobject, n3, arrobject, n3 + 1, this.mSize - n3);
        }
        this.mKeys[n3] = n;
        this.mValues[n3] = e;
        ++this.mSize;
    }

    public void remove(int n) {
        this.delete(n);
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

    public void removeAtRange(int n, int n2) {
        n2 = Math.min(this.mSize, n + n2);
        while (n < n2) {
            this.removeAt(n);
            ++n;
        }
    }

    public void removeKeyRange(ArrayList<E> arrayList, int n, int n2) {
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

    static class ContainerHelpers {
        static final boolean[] EMPTY_BOOLEANS = new boolean[0];
        static final int[] EMPTY_INTS = new int[0];
        static final long[] EMPTY_LONGS = new long[0];
        static final Object[] EMPTY_OBJECTS = new Object[0];

        ContainerHelpers() {
        }

        static int binarySearch(int[] arrn, int n, int n2) {
            int n3 = 0;
            --n;
            while (n3 <= n) {
                int n4 = n3 + n >>> 1;
                int n5 = arrn[n4];
                if (n5 < n2) {
                    n3 = n4 + 1;
                    continue;
                }
                if (n5 > n2) {
                    n = n4 - 1;
                    continue;
                }
                return n4;
            }
            return ~ n3;
        }
    }

}

