// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import java.util.Map;
import java.util.ConcurrentModificationException;

public class SimpleArrayMap<K, V>
{
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
    
    public SimpleArrayMap(final int n) {
        if (n == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }
    
    public SimpleArrayMap(final SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != null) {
            this.putAll((SimpleArrayMap<? extends K, ? extends V>)simpleArrayMap);
        }
    }
    
    private void allocArrays(final int n) {
        Label_0161: {
            if (n == 8) {
                synchronized (ArrayMap.class) {
                    if (SimpleArrayMap.mTwiceBaseCache != null) {
                        final Object[] mTwiceBaseCache = SimpleArrayMap.mTwiceBaseCache;
                        this.mArray = mTwiceBaseCache;
                        SimpleArrayMap.mTwiceBaseCache = (Object[])mTwiceBaseCache[0];
                        this.mHashes = (int[])mTwiceBaseCache[1];
                        mTwiceBaseCache[0] = (mTwiceBaseCache[1] = null);
                        --SimpleArrayMap.mTwiceBaseCacheSize;
                        return;
                    }
                    break Label_0161;
                }
            }
            if (n == 4) {
                synchronized (ArrayMap.class) {
                    if (SimpleArrayMap.mBaseCache != null) {
                        final Object[] mBaseCache = SimpleArrayMap.mBaseCache;
                        this.mArray = mBaseCache;
                        SimpleArrayMap.mBaseCache = (Object[])mBaseCache[0];
                        this.mHashes = (int[])mBaseCache[1];
                        mBaseCache[0] = (mBaseCache[1] = null);
                        --SimpleArrayMap.mBaseCacheSize;
                        return;
                    }
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n << 1];
    }
    
    private static int binarySearchHashes(final int[] array, int binarySearch, final int n) {
        try {
            binarySearch = ContainerHelpers.binarySearch(array, binarySearch, n);
            return binarySearch;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }
    
    private static void freeArrays(final int[] array, final Object[] array2, int i) {
        if (array.length == 8) {
            while (true) {
                while (true) {
                    Label_0124: {
                        synchronized (ArrayMap.class) {
                            if (SimpleArrayMap.mTwiceBaseCacheSize < 10) {
                                array2[0] = SimpleArrayMap.mTwiceBaseCache;
                                array2[1] = array;
                                i = (i << 1) - 1;
                                break Label_0124;
                            }
                            return;
                            SimpleArrayMap.mTwiceBaseCache = array2;
                            ++SimpleArrayMap.mTwiceBaseCacheSize;
                            return;
                        }
                        break;
                    }
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    continue;
                }
            }
        }
        if (array.length == 4) {
            while (true) {
                while (true) {
                    Label_0143: {
                        synchronized (ArrayMap.class) {
                            if (SimpleArrayMap.mBaseCacheSize < 10) {
                                array2[0] = SimpleArrayMap.mBaseCache;
                                array2[1] = array;
                                i = (i << 1) - 1;
                                break Label_0143;
                            }
                            return;
                            SimpleArrayMap.mBaseCache = array2;
                            ++SimpleArrayMap.mBaseCacheSize;
                            return;
                        }
                        break;
                    }
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    continue;
                }
            }
        }
    }
    
    public void clear() {
        if (this.mSize > 0) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            final int mSize = this.mSize;
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
            freeArrays(mHashes, mArray, mSize);
        }
        if (this.mSize <= 0) {
            return;
        }
        throw new ConcurrentModificationException();
    }
    
    public boolean containsKey(final Object o) {
        return this.indexOfKey(o) >= 0;
    }
    
    public boolean containsValue(final Object o) {
        return this.indexOfValue(o) >= 0;
    }
    
    public void ensureCapacity(final int n) {
        final int mSize = this.mSize;
        if (this.mHashes.length < n) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, mSize);
                System.arraycopy(mArray, 0, this.mArray, 0, mSize << 1);
            }
            freeArrays(mHashes, mArray, mSize);
        }
        if (this.mSize == mSize) {
            return;
        }
        throw new ConcurrentModificationException();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        Label_0114: {
            if (o instanceof SimpleArrayMap) {
                final SimpleArrayMap simpleArrayMap = (SimpleArrayMap)o;
                if (this.size() != simpleArrayMap.size()) {
                    return false;
                }
                int i = 0;
            Label_0232:
                while (true) {
                    while (true) {
                        Label_0229: {
                            try {
                                while (i < this.mSize) {
                                    final K key = this.keyAt(i);
                                    final V value = this.valueAt(i);
                                    final Object value2 = simpleArrayMap.get(key);
                                    if (value == null) {
                                        if (value2 != null) {
                                            break Label_0232;
                                        }
                                        if (!simpleArrayMap.containsKey(key)) {
                                            return false;
                                        }
                                        break Label_0229;
                                    }
                                    else {
                                        if (!value.equals(value2)) {
                                            return false;
                                        }
                                        ++i;
                                    }
                                }
                                return true;
                            }
                            catch (ClassCastException ex) {
                                return false;
                            }
                            catch (NullPointerException ex2) {
                                return false;
                            }
                            break Label_0114;
                        }
                        continue;
                    }
                }
                return false;
            }
        }
        if (o instanceof Map) {
            final Map map = (Map)o;
            if (this.size() != map.size()) {
                return false;
            }
            int j = 0;
        Label_0237:
            while (true) {
                while (true) {
                    Label_0234: {
                        try {
                            while (j < this.mSize) {
                                final K key2 = this.keyAt(j);
                                final V value3 = this.valueAt(j);
                                final Object value4 = map.get(key2);
                                if (value3 == null) {
                                    if (value4 != null) {
                                        break Label_0237;
                                    }
                                    if (!map.containsKey(key2)) {
                                        return false;
                                    }
                                    break Label_0234;
                                }
                                else {
                                    if (!value3.equals(value4)) {
                                        return false;
                                    }
                                    ++j;
                                }
                            }
                            return true;
                        }
                        catch (ClassCastException ex3) {
                            return false;
                        }
                        catch (NullPointerException ex4) {
                            return false;
                        }
                        return false;
                    }
                    continue;
                }
            }
            return false;
        }
        return false;
    }
    
    public V get(final Object o) {
        final int indexOfKey = this.indexOfKey(o);
        if (indexOfKey >= 0) {
            return (V)this.mArray[(indexOfKey << 1) + 1];
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int[] mHashes = this.mHashes;
        final Object[] mArray = this.mArray;
        int n = 0;
        for (int i = 0, n2 = 1; i < this.mSize; ++i, n2 += 2) {
            final Object o = mArray[n2];
            final int n3 = mHashes[i];
            int hashCode;
            if (o == null) {
                hashCode = 0;
            }
            else {
                hashCode = o.hashCode();
            }
            n += (n3 ^ hashCode);
        }
        return n;
    }
    
    int indexOf(final Object o, final int n) {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        final int binarySearchHashes = binarySearchHashes(this.mHashes, mSize, n);
        if (binarySearchHashes < 0) {
            return binarySearchHashes;
        }
        if (o.equals(this.mArray[binarySearchHashes << 1])) {
            return binarySearchHashes;
        }
        int n2;
        for (n2 = binarySearchHashes + 1; n2 < mSize && this.mHashes[n2] == n; ++n2) {
            if (o.equals(this.mArray[n2 << 1])) {
                return n2;
            }
        }
        for (int n3 = binarySearchHashes - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (o.equals(this.mArray[n3 << 1])) {
                return n3;
            }
        }
        return ~n2;
    }
    
    public int indexOfKey(final Object o) {
        if (o == null) {
            return this.indexOfNull();
        }
        return this.indexOf(o, o.hashCode());
    }
    
    int indexOfNull() {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        final int binarySearchHashes = binarySearchHashes(this.mHashes, mSize, 0);
        if (binarySearchHashes < 0) {
            return binarySearchHashes;
        }
        if (this.mArray[binarySearchHashes << 1] == null) {
            return binarySearchHashes;
        }
        int n;
        for (n = binarySearchHashes + 1; n < mSize && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n << 1] == null) {
                return n;
            }
        }
        for (int n2 = binarySearchHashes - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2 << 1] == null) {
                return n2;
            }
        }
        return ~n;
    }
    
    int indexOfValue(final Object o) {
        final int n = this.mSize * 2;
        final Object[] mArray = this.mArray;
        if (o == null) {
            for (int i = 1; i < n; i += 2) {
                if (mArray[i] == null) {
                    return i >> 1;
                }
            }
        }
        else {
            for (int j = 1; j < n; j += 2) {
                if (o.equals(mArray[j])) {
                    return j >> 1;
                }
            }
        }
        return -1;
    }
    
    public boolean isEmpty() {
        return this.mSize <= 0;
    }
    
    public K keyAt(final int n) {
        return (K)this.mArray[n << 1];
    }
    
    public V put(final K k, final V v) {
        final int mSize = this.mSize;
        int hashCode;
        int n;
        if (k == null) {
            hashCode = 0;
            n = this.indexOfNull();
        }
        else {
            hashCode = k.hashCode();
            n = this.indexOf(k, hashCode);
        }
        if (n >= 0) {
            final int n2 = (n << 1) + 1;
            final Object[] mArray = this.mArray;
            final Object o = mArray[n2];
            mArray[n2] = v;
            return (V)o;
        }
        final int n3 = ~n;
        if (mSize >= this.mHashes.length) {
            int n4 = 4;
            if (mSize >= 8) {
                n4 = (mSize >> 1) + mSize;
            }
            else if (mSize >= 4) {
                n4 = 8;
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray2 = this.mArray;
            this.allocArrays(n4);
            if (mSize != this.mSize) {
                throw new ConcurrentModificationException();
            }
            final int[] mHashes2 = this.mHashes;
            if (mHashes2.length > 0) {
                System.arraycopy(mHashes, 0, mHashes2, 0, mHashes.length);
                System.arraycopy(mArray2, 0, this.mArray, 0, mArray2.length);
            }
            freeArrays(mHashes, mArray2, mSize);
        }
        if (n3 < mSize) {
            final int[] mHashes3 = this.mHashes;
            System.arraycopy(mHashes3, n3, mHashes3, n3 + 1, mSize - n3);
            final Object[] mArray3 = this.mArray;
            System.arraycopy(mArray3, n3 << 1, mArray3, n3 + 1 << 1, this.mSize - n3 << 1);
        }
        final int mSize2 = this.mSize;
        if (mSize == mSize2) {
            final int[] mHashes4 = this.mHashes;
            if (n3 < mHashes4.length) {
                mHashes4[n3] = hashCode;
                final Object[] mArray4 = this.mArray;
                mArray4[n3 << 1] = k;
                mArray4[(n3 << 1) + 1] = v;
                this.mSize = mSize2 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }
    
    public void putAll(final SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        final int mSize = simpleArrayMap.mSize;
        this.ensureCapacity(this.mSize + mSize);
        if (this.mSize != 0) {
            for (int i = 0; i < mSize; ++i) {
                this.put(simpleArrayMap.keyAt(i), simpleArrayMap.valueAt(i));
            }
            return;
        }
        if (mSize > 0) {
            System.arraycopy(simpleArrayMap.mHashes, 0, this.mHashes, 0, mSize);
            System.arraycopy(simpleArrayMap.mArray, 0, this.mArray, 0, mSize << 1);
            this.mSize = mSize;
        }
    }
    
    public V remove(final Object o) {
        final int indexOfKey = this.indexOfKey(o);
        if (indexOfKey >= 0) {
            return this.removeAt(indexOfKey);
        }
        return null;
    }
    
    public V removeAt(int mSize) {
        final Object[] mArray = this.mArray;
        final Object o = mArray[(mSize << 1) + 1];
        final int mSize2 = this.mSize;
        if (mSize2 <= 1) {
            freeArrays(this.mHashes, mArray, mSize2);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        }
        else {
            final int n = mSize2 - 1;
            final int[] mHashes = this.mHashes;
            final int length = mHashes.length;
            int n2 = 8;
            if (length > 8 && this.mSize < mHashes.length / 3) {
                if (mSize2 > 8) {
                    n2 = mSize2 + (mSize2 >> 1);
                }
                final int[] mHashes2 = this.mHashes;
                final Object[] mArray2 = this.mArray;
                this.allocArrays(n2);
                if (mSize2 != this.mSize) {
                    throw new ConcurrentModificationException();
                }
                if (mSize > 0) {
                    System.arraycopy(mHashes2, 0, this.mHashes, 0, mSize);
                    System.arraycopy(mArray2, 0, this.mArray, 0, mSize << 1);
                }
                if (mSize < n) {
                    System.arraycopy(mHashes2, mSize + 1, this.mHashes, mSize, n - mSize);
                    System.arraycopy(mArray2, mSize + 1 << 1, this.mArray, mSize << 1, n - mSize << 1);
                }
                mSize = n;
            }
            else {
                if (mSize < n) {
                    final int[] mHashes3 = this.mHashes;
                    System.arraycopy(mHashes3, mSize + 1, mHashes3, mSize, n - mSize);
                    final Object[] mArray3 = this.mArray;
                    System.arraycopy(mArray3, mSize + 1 << 1, mArray3, mSize << 1, n - mSize << 1);
                }
                final Object[] mArray4 = this.mArray;
                mArray4[(n << 1) + 1] = (mArray4[n << 1] = null);
                mSize = n;
            }
        }
        if (mSize2 == this.mSize) {
            this.mSize = mSize;
            return (V)o;
        }
        throw new ConcurrentModificationException();
    }
    
    public V setValueAt(int n, final V v) {
        n = (n << 1) + 1;
        final Object[] mArray = this.mArray;
        final Object o = mArray[n];
        mArray[n] = v;
        return (V)o;
    }
    
    public int size() {
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            final K key = this.keyAt(i);
            if (key != this) {
                sb.append(key);
            }
            else {
                sb.append("(this Map)");
            }
            sb.append('=');
            final V value = this.valueAt(i);
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
    
    public V valueAt(final int n) {
        return (V)this.mArray[(n << 1) + 1];
    }
}
