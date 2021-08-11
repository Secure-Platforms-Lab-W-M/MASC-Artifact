// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.collection;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class LruCache<K, V>
{
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    private int safeSizeOf(final K k, final V v) {
        final int size = this.sizeOf(k, v);
        if (size >= 0) {
            return size;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Negative size: ");
        sb.append(k);
        sb.append("=");
        sb.append(v);
        throw new IllegalStateException(sb.toString());
    }
    
    protected V create(final K k) {
        return null;
    }
    
    public final int createCount() {
        synchronized (this) {
            return this.createCount;
        }
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    public final V get(final K k) {
        if (k != null) {
            synchronized (this) {
                final V value = this.map.get(k);
                if (value != null) {
                    ++this.hitCount;
                    return value;
                }
                ++this.missCount;
                // monitorexit(this)
                final V create = this.create(k);
                if (create == null) {
                    return null;
                }
                synchronized (this) {
                    ++this.createCount;
                    final V put = this.map.put(k, create);
                    if (put != null) {
                        this.map.put(k, put);
                    }
                    else {
                        this.size += this.safeSizeOf(k, create);
                    }
                    // monitorexit(this)
                    if (put != null) {
                        this.entryRemoved(false, k, create, put);
                        return put;
                    }
                    this.trimToSize(this.maxSize);
                    return create;
                }
            }
        }
        throw new NullPointerException("key == null");
    }
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public final int missCount() {
        synchronized (this) {
            return this.missCount;
        }
    }
    
    public final V put(final K k, final V v) {
        if (k != null && v != null) {
            synchronized (this) {
                ++this.putCount;
                this.size += this.safeSizeOf(k, v);
                final V put = this.map.put(k, v);
                if (put != null) {
                    this.size -= this.safeSizeOf(k, put);
                }
                // monitorexit(this)
                if (put != null) {
                    this.entryRemoved(false, k, put, v);
                }
                this.trimToSize(this.maxSize);
                return put;
            }
        }
        throw new NullPointerException("key == null || value == null");
    }
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    public final V remove(final K k) {
        if (k != null) {
            synchronized (this) {
                final V remove = this.map.remove(k);
                if (remove != null) {
                    this.size -= this.safeSizeOf(k, remove);
                }
                // monitorexit(this)
                if (remove != null) {
                    this.entryRemoved(false, k, remove, null);
                }
                return remove;
            }
        }
        throw new NullPointerException("key == null");
    }
    
    public void resize(final int maxSize) {
        if (maxSize > 0) {
            synchronized (this) {
                this.maxSize = maxSize;
                // monitorexit(this)
                this.trimToSize(maxSize);
                return;
            }
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    public final Map<K, V> snapshot() {
        synchronized (this) {
            return new LinkedHashMap<K, V>((Map<? extends K, ? extends V>)this.map);
        }
    }
    
    @Override
    public final String toString() {
        while (true) {
            while (true) {
                synchronized (this) {
                    final int n = this.hitCount + this.missCount;
                    if (n != 0) {
                        final int n2 = this.hitCount * 100 / n;
                        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, n2);
                    }
                }
                final int n2 = 0;
                continue;
            }
        }
    }
    
    public void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getClass().getName());
                    sb.append(".sizeOf() is reporting inconsistent results!");
                    throw new IllegalStateException(sb.toString());
                }
                if (this.size <= n || this.map.isEmpty()) {
                    return;
                }
                final Map.Entry<K, V> entry = this.map.entrySet().iterator().next();
                final K key = entry.getKey();
                final V value = entry.getValue();
                this.map.remove(key);
                this.size -= this.safeSizeOf(key, value);
                ++this.evictionCount;
                // monitorexit(this)
                this.entryRemoved(true, key, value, null);
            }
        }
    }
}
