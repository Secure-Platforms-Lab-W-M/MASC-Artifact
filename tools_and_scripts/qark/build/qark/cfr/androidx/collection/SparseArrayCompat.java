/*
 * Decompiled with CFR 0_124.
 */
package androidx.collection;

import androidx.collection.ContainerHelpers;

public class SparseArrayCompat<E>
implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int n) {
        if (n == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
            return;
        }
        n = ContainerHelpers.idealIntArraySize(n);
        this.mKeys = new int[n];
        this.mValues = new Object[n];
    }

    private void gc() {
        int n = this.mSize;
        int n2 = 0;
        int[] arrn = this.mKeys;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            Object object = arrobject[i];
            int n3 = n2;
            if (object != DELETED) {
                if (i != n2) {
                    arrn[n2] = arrn[i];
                    arrobject[n2] = object;
                    arrobject[i] = null;
                }
                n3 = n2 + 1;
            }
            n2 = n3;
        }
        this.mGarbage = false;
        this.mSize = n2;
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
            int n3 = ContainerHelpers.idealIntArraySize(n2 + 1);
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

    public SparseArrayCompat<E> clone() {
        try {
            SparseArrayCompat sparseArrayCompat = (SparseArrayCompat)super.clone();
            sparseArrayCompat.mKeys = (int[])this.mKeys.clone();
            sparseArrayCompat.mValues = (Object[])this.mValues.clone();
            return sparseArrayCompat;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError(cloneNotSupportedException);
        }
    }

    public boolean containsKey(int n) {
        if (this.indexOfKey(n) >= 0) {
            return true;
        }
        return false;
    }

    public boolean containsValue(E e) {
        if (this.indexOfValue(e) >= 0) {
            return true;
        }
        return false;
    }

    @Deprecated
    public void delete(int n) {
        this.remove(n);
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

    public boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }

    public int keyAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }

    public void put(int n, E e) {
        Object[] arrobject;
        int n2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (n2 >= 0) {
            this.mValues[n2] = e;
            return;
        }
        int n3 = n2;
        if (n3 < this.mSize && (arrobject = this.mValues)[n3] == DELETED) {
            this.mKeys[n3] = n;
            arrobject[n3] = e;
            return;
        }
        n2 = n3;
        if (this.mGarbage) {
            n2 = n3;
            if (this.mSize >= this.mKeys.length) {
                this.gc();
                n2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
            }
        }
        if ((n3 = this.mSize) >= this.mKeys.length) {
            n3 = ContainerHelpers.idealIntArraySize(n3 + 1);
            arrobject = new int[n3];
            Object[] arrobject2 = new Object[n3];
            int[] arrn = this.mKeys;
            System.arraycopy(arrn, 0, arrobject, 0, arrn.length);
            arrn = this.mValues;
            System.arraycopy(arrn, 0, arrobject2, 0, arrn.length);
            this.mKeys = arrobject;
            this.mValues = arrobject2;
        }
        if ((n3 = this.mSize) - n2 != 0) {
            arrobject = this.mKeys;
            System.arraycopy(arrobject, n2, arrobject, n2 + 1, n3 - n2);
            arrobject = this.mValues;
            System.arraycopy(arrobject, n2, arrobject, n2 + 1, this.mSize - n2);
        }
        this.mKeys[n2] = n;
        this.mValues[n2] = e;
        ++this.mSize;
    }

    public void putAll(SparseArrayCompat<? extends E> sparseArrayCompat) {
        int n = sparseArrayCompat.size();
        for (int i = 0; i < n; ++i) {
            this.put(sparseArrayCompat.keyAt(i), sparseArrayCompat.valueAt(i));
        }
    }

    public E putIfAbsent(int n, E e) {
        E e2 = this.get(n);
        if (e2 == null) {
            this.put(n, e);
        }
        return e2;
    }

    public void remove(int n) {
        Object object;
        Object[] arrobject;
        Object object2;
        if ((n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n)) >= 0 && (object2 = (arrobject = this.mValues)[n]) != (object = DELETED)) {
            arrobject[n] = object;
            this.mGarbage = true;
        }
    }

    public boolean remove(int n, Object object) {
        E e;
        if ((n = this.indexOfKey(n)) >= 0 && (object == (e = this.valueAt(n)) || object != null && object.equals(e))) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    public void removeAt(int n) {
        Object[] arrobject = this.mValues;
        Object object = arrobject[n];
        Object object2 = DELETED;
        if (object != object2) {
            arrobject[n] = object2;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int n, int n2) {
        n2 = Math.min(this.mSize, n + n2);
        while (n < n2) {
            this.removeAt(n);
            ++n;
        }
    }

    public E replace(int n, E e) {
        if ((n = this.indexOfKey(n)) >= 0) {
            Object[] arrobject = this.mValues;
            Object object = arrobject[n];
            arrobject[n] = e;
            return (E)object;
        }
        return null;
    }

    public boolean replace(int n, E e, E e2) {
        Object object;
        if ((n = this.indexOfKey(n)) >= 0 && ((object = this.mValues[n]) == e || e != null && e.equals(object))) {
            this.mValues[n] = e2;
            return true;
        }
        return false;
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

