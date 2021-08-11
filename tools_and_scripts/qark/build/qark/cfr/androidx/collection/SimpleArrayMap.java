/*
 * Decompiled with CFR 0_124.
 */
package androidx.collection;

import androidx.collection.ContainerHelpers;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int n) {
        if (n == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != null) {
            this.putAll(simpleArrayMap);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void allocArrays(int n) {
        if (n == 8) {
            synchronized (SimpleArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    Object[] arrobject = mTwiceBaseCache;
                    this.mArray = arrobject;
                    mTwiceBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --mTwiceBaseCacheSize;
                    return;
                }
            }
        } else if (n == 4) {
            synchronized (SimpleArrayMap.class) {
                if (mBaseCache != null) {
                    Object[] arrobject = mBaseCache;
                    this.mArray = arrobject;
                    mBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --mBaseCacheSize;
                    return;
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n << 1];
    }

    private static int binarySearchHashes(int[] arrn, int n, int n2) {
        try {
            n = ContainerHelpers.binarySearch(arrn, n, n2);
            return n;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new ConcurrentModificationException();
        }
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
                            // MONITORENTER : androidx.collection.SimpleArrayMap.class
                            if (mTwiceBaseCacheSize >= 10) break block9;
                            arrobject[0] = mTwiceBaseCache;
                            arrobject[1] = arrn;
                            break block10;
                        }
                        if (arrn.length != 4) return;
                        // MONITORENTER : androidx.collection.SimpleArrayMap.class
                        if (mBaseCacheSize >= 10) break block11;
                        arrobject[0] = mBaseCache;
                        arrobject[1] = arrn;
                        break block12;
                    }
                    for (n = (n << 1) - 1; n >= 2; --n) {
                        arrobject[n] = null;
                    }
                    mTwiceBaseCache = arrobject;
                    ++mTwiceBaseCacheSize;
                }
                // MONITOREXIT : androidx.collection.SimpleArrayMap.class
                return;
            }
            for (n = (n << 1) - 1; n >= 2; --n) {
                arrobject[n] = null;
            }
            mBaseCache = arrobject;
            ++mBaseCacheSize;
        }
        // MONITOREXIT : androidx.collection.SimpleArrayMap.class
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            int n = this.mSize;
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
            SimpleArrayMap.freeArrays(arrn, arrobject, n);
        }
        if (this.mSize <= 0) {
            return;
        }
        throw new ConcurrentModificationException();
    }

    public boolean containsKey(Object object) {
        if (this.indexOfKey(object) >= 0) {
            return true;
        }
        return false;
    }

    public boolean containsValue(Object object) {
        if (this.indexOfValue(object) >= 0) {
            return true;
        }
        return false;
    }

    public void ensureCapacity(int n) {
        int n2 = this.mSize;
        if (this.mHashes.length < n) {
            int[] arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(arrn, 0, this.mHashes, 0, n2);
                System.arraycopy(arrobject, 0, this.mArray, 0, n2 << 1);
            }
            SimpleArrayMap.freeArrays(arrn, arrobject, n2);
        }
        if (this.mSize == n2) {
            return;
        }
        throw new ConcurrentModificationException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleArrayMap)) {
            if (!(object instanceof Map)) {
                return false;
            }
            object = (Map)object;
            if (this.size() != object.size()) {
                return false;
            }
        } else {
            object = (SimpleArrayMap)object;
            if (this.size() != object.size()) {
                return false;
            }
            int n = 0;
            try {
                do {
                    if (n >= this.mSize) {
                        return true;
                    }
                    K k = this.keyAt(n);
                    V v = this.valueAt(n);
                    V v2 = object.get(k);
                    if (v == null) {
                        if (v2 != null) break;
                        if (!object.containsKey(k)) {
                            return false;
                        }
                    } else {
                        boolean bl = v.equals(v2);
                        if (!bl) {
                            return false;
                        }
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
            return false;
        }
        int n = 0;
        try {
            do {
                if (n >= this.mSize) {
                    return true;
                }
                K k = this.keyAt(n);
                V v3 = this.valueAt(n);
                Object v = object.get(k);
                if (v3 == null) {
                    if (v != null) break;
                    if (!object.containsKey(k)) {
                        return false;
                    }
                } else {
                    boolean bl = v3.equals(v);
                    if (!bl) {
                        return false;
                    }
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
        return false;
    }

    public V get(Object object) {
        return this.getOrDefault(object, null);
    }

    public V getOrDefault(Object object, V v) {
        int n = this.indexOfKey(object);
        if (n >= 0) {
            return (V)this.mArray[(n << 1) + 1];
        }
        return v;
    }

    public int hashCode() {
        int[] arrn = this.mHashes;
        Object[] arrobject = this.mArray;
        int n = 0;
        int n2 = 0;
        int n3 = 1;
        int n4 = this.mSize;
        while (n2 < n4) {
            Object object = arrobject[n3];
            int n5 = arrn[n2];
            int n6 = object == null ? 0 : object.hashCode();
            n += n5 ^ n6;
            ++n2;
            n3 += 2;
        }
        return n;
    }

    int indexOf(Object object, int n) {
        int n2;
        int n3 = this.mSize;
        if (n3 == 0) {
            return -1;
        }
        int n4 = SimpleArrayMap.binarySearchHashes(this.mHashes, n3, n);
        if (n4 < 0) {
            return n4;
        }
        if (object.equals(this.mArray[n4 << 1])) {
            return n4;
        }
        for (n2 = n4 + 1; n2 < n3 && this.mHashes[n2] == n; ++n2) {
            if (!object.equals(this.mArray[n2 << 1])) continue;
            return n2;
        }
        for (n3 = n4 - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (!object.equals(this.mArray[n3 << 1])) continue;
            return n3;
        }
        return n2;
    }

    public int indexOfKey(Object object) {
        if (object == null) {
            return this.indexOfNull();
        }
        return this.indexOf(object, object.hashCode());
    }

    int indexOfNull() {
        int n;
        int n2 = this.mSize;
        if (n2 == 0) {
            return -1;
        }
        int n3 = SimpleArrayMap.binarySearchHashes(this.mHashes, n2, 0);
        if (n3 < 0) {
            return n3;
        }
        if (this.mArray[n3 << 1] == null) {
            return n3;
        }
        for (n = n3 + 1; n < n2 && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n << 1] != null) continue;
            return n;
        }
        for (n2 = n3 - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2 << 1] != null) continue;
            return n2;
        }
        return n;
    }

    int indexOfValue(Object object) {
        int n = this.mSize * 2;
        Object[] arrobject = this.mArray;
        if (object == null) {
            for (int i = 1; i < n; i += 2) {
                if (arrobject[i] != null) continue;
                return i >> 1;
            }
        } else {
            for (int i = 1; i < n; i += 2) {
                if (!object.equals(arrobject[i])) continue;
                return i >> 1;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        if (this.mSize <= 0) {
            return true;
        }
        return false;
    }

    public K keyAt(int n) {
        return (K)this.mArray[n << 1];
    }

    public V put(K object, V v) {
        int[] arrn;
        int n;
        int n2;
        int n3 = this.mSize;
        if (object == null) {
            n = 0;
            n2 = this.indexOfNull();
        } else {
            n = object.hashCode();
            n2 = this.indexOf(object, n);
        }
        if (n2 >= 0) {
            n2 = (n2 << 1) + 1;
            object = this.mArray;
            Object object2 = object[n2];
            object[n2] = v;
            return (V)object2;
        }
        int n4 = n2;
        if (n3 >= this.mHashes.length) {
            n2 = 4;
            if (n3 >= 8) {
                n2 = (n3 >> 1) + n3;
            } else if (n3 >= 4) {
                n2 = 8;
            }
            arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n2);
            if (n3 == this.mSize) {
                int[] arrn2 = this.mHashes;
                if (arrn2.length > 0) {
                    System.arraycopy(arrn, 0, arrn2, 0, arrn.length);
                    System.arraycopy(arrobject, 0, this.mArray, 0, arrobject.length);
                }
                SimpleArrayMap.freeArrays(arrn, arrobject, n3);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (n4 < n3) {
            arrn = this.mHashes;
            System.arraycopy(arrn, n4, arrn, n4 + 1, n3 - n4);
            arrn = this.mArray;
            System.arraycopy(arrn, n4 << 1, arrn, n4 + 1 << 1, this.mSize - n4 << 1);
        }
        if (n3 == (n2 = this.mSize) && n4 < (arrn = this.mHashes).length) {
            arrn[n4] = n;
            arrn = this.mArray;
            arrn[n4 << 1] = object;
            arrn[(n4 << 1) + 1] = v;
            this.mSize = n2 + 1;
            return null;
        }
        throw new ConcurrentModificationException();
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        int n = simpleArrayMap.mSize;
        this.ensureCapacity(this.mSize + n);
        if (this.mSize == 0) {
            if (n > 0) {
                System.arraycopy(simpleArrayMap.mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(simpleArrayMap.mArray, 0, this.mArray, 0, n << 1);
                this.mSize = n;
                return;
            }
        } else {
            for (int i = 0; i < n; ++i) {
                this.put(simpleArrayMap.keyAt(i), simpleArrayMap.valueAt(i));
            }
        }
    }

    public V putIfAbsent(K k, V v) {
        V v2;
        V v3 = v2 = this.get(k);
        if (v2 == null) {
            v3 = this.put(k, v);
        }
        return v3;
    }

    public V remove(Object object) {
        int n = this.indexOfKey(object);
        if (n >= 0) {
            return this.removeAt(n);
        }
        return null;
    }

    public boolean remove(Object object, Object object2) {
        int n = this.indexOfKey(object);
        if (n >= 0 && (object2 == (object = this.valueAt(n)) || object2 != null && object2.equals(object))) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public V removeAt(int n) {
        Object[] arrobject = this.mArray;
        Object object = arrobject[(n << 1) + 1];
        int n2 = this.mSize;
        if (n2 <= 1) {
            SimpleArrayMap.freeArrays(this.mHashes, arrobject, n2);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            n = 0;
        } else {
            int n3 = n2 - 1;
            arrobject = this.mHashes;
            int n4 = arrobject.length;
            int n5 = 8;
            if (n4 > 8 && this.mSize < arrobject.length / 3) {
                if (n2 > 8) {
                    n5 = n2 + (n2 >> 1);
                }
                arrobject = this.mHashes;
                Object[] arrobject2 = this.mArray;
                this.allocArrays(n5);
                if (n2 != this.mSize) throw new ConcurrentModificationException();
                if (n > 0) {
                    System.arraycopy(arrobject, 0, this.mHashes, 0, n);
                    System.arraycopy(arrobject2, 0, this.mArray, 0, n << 1);
                }
                if (n < n3) {
                    System.arraycopy(arrobject, n + 1, this.mHashes, n, n3 - n);
                    System.arraycopy(arrobject2, n + 1 << 1, this.mArray, n << 1, n3 - n << 1);
                }
                n = n3;
            } else {
                if (n < n3) {
                    arrobject = this.mHashes;
                    System.arraycopy(arrobject, n + 1, arrobject, n, n3 - n);
                    arrobject = this.mArray;
                    System.arraycopy(arrobject, n + 1 << 1, arrobject, n << 1, n3 - n << 1);
                }
                arrobject = this.mArray;
                arrobject[n3 << 1] = null;
                arrobject[(n3 << 1) + 1] = null;
                n = n3;
            }
        }
        if (n2 != this.mSize) throw new ConcurrentModificationException();
        this.mSize = n;
        return (V)object;
    }

    public V replace(K k, V v) {
        int n = this.indexOfKey(k);
        if (n >= 0) {
            return this.setValueAt(n, v);
        }
        return null;
    }

    public boolean replace(K object, V v, V v2) {
        int n = this.indexOfKey(object);
        if (n >= 0 && ((object = this.valueAt(n)) == v || v != null && v.equals(object))) {
            this.setValueAt(n, v2);
            return true;
        }
        return false;
    }

    public V setValueAt(int n, V v) {
        n = (n << 1) + 1;
        Object[] arrobject = this.mArray;
        Object object = arrobject[n];
        arrobject[n] = v;
        return (V)object;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            Object object;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if ((object = this.keyAt(i)) != this) {
                stringBuilder.append(object);
            } else {
                stringBuilder.append("(this Map)");
            }
            stringBuilder.append('=');
            object = this.valueAt(i);
            if (object != this) {
                stringBuilder.append(object);
                continue;
            }
            stringBuilder.append("(this Map)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public V valueAt(int n) {
        return (V)this.mArray[(n << 1) + 1];
    }
}

