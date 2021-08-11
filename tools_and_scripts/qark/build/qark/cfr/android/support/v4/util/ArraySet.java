/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

import android.support.annotation.RestrictTo;
import android.support.v4.util.ContainerHelpers;
import android.support.v4.util.MapCollections;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E>
implements Collection<E>,
Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    static Object[] sBaseCache;
    static int sBaseCacheSize;
    static Object[] sTwiceBaseCache;
    static int sTwiceBaseCacheSize;
    Object[] mArray;
    MapCollections<E, E> mCollections;
    int[] mHashes;
    final boolean mIdentityHashCode;
    int mSize;

    public ArraySet() {
        this(0, false);
    }

    public ArraySet(int n) {
        this(n, false);
    }

    public ArraySet(int n, boolean bl) {
        this.mIdentityHashCode = bl;
        if (n == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }

    public ArraySet(ArraySet<E> arraySet) {
        this();
        if (arraySet != null) {
            this.addAll(arraySet);
            return;
        }
    }

    public ArraySet(Collection<E> collection) {
        this();
        if (collection != null) {
            this.addAll(collection);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void allocArrays(int n) {
        if (n == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCache != null) {
                    Object[] arrobject = sTwiceBaseCache;
                    this.mArray = arrobject;
                    sTwiceBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --sTwiceBaseCacheSize;
                    return;
                }
            }
        } else if (n == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCache != null) {
                    Object[] arrobject = sBaseCache;
                    this.mArray = arrobject;
                    sBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --sBaseCacheSize;
                    return;
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n];
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static void freeArrays(int[] arrn, Object[] arrobject, int n) {
        block11 : {
            block12 : {
                block9 : {
                    block10 : {
                        block8 : {
                            if (arrn.length != 8) break block8;
                            // MONITORENTER : android.support.v4.util.ArraySet.class
                            if (sTwiceBaseCacheSize >= 10) break block9;
                            arrobject[0] = sTwiceBaseCache;
                            arrobject[1] = arrn;
                            --n;
                            break block10;
                        }
                        if (arrn.length != 4) return;
                        // MONITORENTER : android.support.v4.util.ArraySet.class
                        if (sBaseCacheSize >= 10) break block11;
                        arrobject[0] = sBaseCache;
                        arrobject[1] = arrn;
                        --n;
                        break block12;
                    }
                    while (n >= 2) {
                        arrobject[n] = null;
                        --n;
                    }
                    sTwiceBaseCache = arrobject;
                    ++sTwiceBaseCacheSize;
                }
                // MONITOREXIT : android.support.v4.util.ArraySet.class
                return;
            }
            while (n >= 2) {
                arrobject[n] = null;
                --n;
            }
            sBaseCache = arrobject;
            ++sBaseCacheSize;
        }
        // MONITOREXIT : android.support.v4.util.ArraySet.class
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>(){

                @Override
                protected void colClear() {
                    ArraySet.this.clear();
                }

                @Override
                protected Object colGetEntry(int n, int n2) {
                    return ArraySet.this.mArray[n];
                }

                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }

                @Override
                protected int colIndexOfKey(Object object) {
                    return ArraySet.this.indexOf(object);
                }

                @Override
                protected int colIndexOfValue(Object object) {
                    return ArraySet.this.indexOf(object);
                }

                @Override
                protected void colPut(E e, E e2) {
                    ArraySet.this.add(e);
                }

                @Override
                protected void colRemoveAt(int n) {
                    ArraySet.this.removeAt(n);
                }

                @Override
                protected E colSetValue(int n, E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }

    private int indexOf(Object object, int n) {
        int n2;
        int n3 = this.mSize;
        if (n3 == 0) {
            return -1;
        }
        int n4 = ContainerHelpers.binarySearch(this.mHashes, n3, n);
        if (n4 < 0) {
            return n4;
        }
        if (object.equals(this.mArray[n4])) {
            return n4;
        }
        for (n2 = n4 + 1; n2 < n3 && this.mHashes[n2] == n; ++n2) {
            if (!object.equals(this.mArray[n2])) continue;
            return n2;
        }
        for (n3 = n4 - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (!object.equals(this.mArray[n3])) continue;
            return n3;
        }
        return ~ n2;
    }

    private int indexOfNull() {
        int n;
        int n2 = this.mSize;
        if (n2 == 0) {
            return -1;
        }
        int n3 = ContainerHelpers.binarySearch(this.mHashes, n2, 0);
        if (n3 < 0) {
            return n3;
        }
        if (this.mArray[n3] == null) {
            return n3;
        }
        for (n = n3 + 1; n < n2 && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n] != null) continue;
            return n;
        }
        for (n2 = n3 - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2] != null) continue;
            return n2;
        }
        return ~ n;
    }

    @Override
    public boolean add(E e) {
        int n;
        int n2;
        int n3;
        int[] arrn;
        if (e == null) {
            n2 = 0;
            n = this.indexOfNull();
        } else {
            n3 = this.mIdentityHashCode ? System.identityHashCode(e) : e.hashCode();
            n = this.indexOf(e, n3);
            n2 = n3;
        }
        if (n >= 0) {
            return false;
        }
        n ^= -1;
        int n4 = this.mSize;
        if (n4 >= this.mHashes.length) {
            n3 = 4;
            if (n4 >= 8) {
                n3 = (n4 >> 1) + n4;
            } else if (n4 >= 4) {
                n3 = 8;
            }
            arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n3);
            int[] arrn2 = this.mHashes;
            if (arrn2.length > 0) {
                System.arraycopy(arrn, 0, arrn2, 0, arrn.length);
                System.arraycopy(arrobject, 0, this.mArray, 0, arrobject.length);
            }
            ArraySet.freeArrays(arrn, arrobject, this.mSize);
        }
        if (n < (n3 = this.mSize)) {
            arrn = this.mHashes;
            System.arraycopy(arrn, n, arrn, n + 1, n3 - n);
            arrn = this.mArray;
            System.arraycopy(arrn, n, arrn, n + 1, this.mSize - n);
        }
        this.mHashes[n] = n2;
        this.mArray[n] = e;
        ++this.mSize;
        return true;
    }

    public void addAll(ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        this.ensureCapacity(this.mSize + n);
        if (this.mSize == 0) {
            if (n > 0) {
                System.arraycopy(arraySet.mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(arraySet.mArray, 0, this.mArray, 0, n);
                this.mSize = n;
                return;
            }
            return;
        }
        for (int i = 0; i < n; ++i) {
            this.add(arraySet.valueAt(i));
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> object) {
        this.ensureCapacity(this.mSize + object.size());
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.add(object.next());
        }
        return bl;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void append(E e) {
        int n = this.mSize;
        int n2 = e == null ? 0 : (this.mIdentityHashCode ? System.identityHashCode(e) : e.hashCode());
        int[] arrn = this.mHashes;
        if (n < arrn.length) {
            if (n > 0 && arrn[n - 1] > n2) {
                this.add(e);
                return;
            }
            this.mSize = n + 1;
            this.mHashes[n] = n2;
            this.mArray[n] = e;
            return;
        }
        throw new IllegalStateException("Array is full");
    }

    @Override
    public void clear() {
        int n = this.mSize;
        if (n != 0) {
            ArraySet.freeArrays(this.mHashes, this.mArray, n);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
            return;
        }
    }

    @Override
    public boolean contains(Object object) {
        if (this.indexOf(object) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> object) {
        object = object.iterator();
        while (object.hasNext()) {
            if (this.contains(object.next())) continue;
            return false;
        }
        return true;
    }

    public void ensureCapacity(int n) {
        if (this.mHashes.length < n) {
            int[] arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n);
            n = this.mSize;
            if (n > 0) {
                System.arraycopy(arrn, 0, this.mHashes, 0, n);
                System.arraycopy(arrobject, 0, this.mArray, 0, this.mSize);
            }
            ArraySet.freeArrays(arrn, arrobject, this.mSize);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        object = (Set)object;
        if (this.size() != object.size()) {
            return false;
        }
        int n = 0;
        try {
            do {
                if (n >= this.mSize) {
                    return true;
                }
                boolean bl = object.contains(this.valueAt(n));
                if (!bl) {
                    return false;
                }
                ++n;
            } while (true);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int[] arrn = this.mHashes;
        int n = 0;
        int n2 = this.mSize;
        for (int i = 0; i < n2; ++i) {
            n += arrn[i];
        }
        return n;
    }

    public int indexOf(Object object) {
        if (object == null) {
            return this.indexOfNull();
        }
        int n = this.mIdentityHashCode ? System.identityHashCode(object) : object.hashCode();
        return this.indexOf(object, n);
    }

    @Override
    public boolean isEmpty() {
        if (this.mSize <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }

    @Override
    public boolean remove(Object object) {
        int n = this.indexOf(object);
        if (n >= 0) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        int n2 = this.mSize;
        for (int i = 0; i < n; ++i) {
            this.remove(arraySet.valueAt(i));
        }
        if (n2 != this.mSize) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> object) {
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.remove(object.next());
        }
        return bl;
    }

    public E removeAt(int n) {
        Object[] arrobject = this.mArray;
        Object object = arrobject[n];
        int n2 = this.mSize;
        if (n2 <= 1) {
            ArraySet.freeArrays(this.mHashes, arrobject, n2);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
            return (E)object;
        }
        arrobject = this.mHashes;
        int n3 = arrobject.length;
        int n4 = 8;
        if (n3 > 8 && n2 < arrobject.length / 3) {
            if (n2 > 8) {
                n4 = n2 + (n2 >> 1);
            }
            arrobject = this.mHashes;
            Object[] arrobject2 = this.mArray;
            this.allocArrays(n4);
            --this.mSize;
            if (n > 0) {
                System.arraycopy(arrobject, 0, this.mHashes, 0, n);
                System.arraycopy(arrobject2, 0, this.mArray, 0, n);
            }
            if (n < (n4 = this.mSize)) {
                System.arraycopy(arrobject, n + 1, this.mHashes, n, n4 - n);
                System.arraycopy(arrobject2, n + 1, this.mArray, n, this.mSize - n);
            }
            return (E)object;
        }
        --this.mSize;
        n4 = this.mSize;
        if (n < n4) {
            arrobject = this.mHashes;
            System.arraycopy(arrobject, n + 1, arrobject, n, n4 - n);
            arrobject = this.mArray;
            System.arraycopy(arrobject, n + 1, arrobject, n, this.mSize - n);
        }
        this.mArray[this.mSize] = null;
        return (E)object;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean bl = false;
        for (int i = this.mSize - 1; i >= 0; --i) {
            if (collection.contains(this.mArray[i])) continue;
            this.removeAt(i);
            bl = true;
        }
        return bl;
    }

    @Override
    public int size() {
        return this.mSize;
    }

    @Override
    public Object[] toArray() {
        int n = this.mSize;
        Object[] arrobject = new Object[n];
        System.arraycopy(this.mArray, 0, arrobject, 0, n);
        return arrobject;
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        if (arrT.length < this.mSize) {
            arrT = (Object[])Array.newInstance(arrT.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, arrT, 0, this.mSize);
        int n = arrT.length;
        int n2 = this.mSize;
        if (n > n2) {
            arrT[n2] = null;
            return arrT;
        }
        return arrT;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 14);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            E e;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if ((e = this.valueAt(i)) != this) {
                stringBuilder.append(e);
                continue;
            }
            stringBuilder.append("(this Set)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public E valueAt(int n) {
        return (E)this.mArray[n];
    }

}

