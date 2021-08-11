/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LruCache<T, Y> {
    private final Map<T, Y> cache = new LinkedHashMap<T, Y>(100, 0.75f, true);
    private long currentSize;
    private final long initialMaxSize;
    private long maxSize;

    public LruCache(long l) {
        this.initialMaxSize = l;
        this.maxSize = l;
    }

    private void evict() {
        this.trimToSize(this.maxSize);
    }

    public void clearMemory() {
        this.trimToSize(0L);
    }

    public boolean contains(T t) {
        synchronized (this) {
            boolean bl = this.cache.containsKey(t);
            return bl;
        }
    }

    public Y get(T object) {
        synchronized (this) {
            object = this.cache.get(object);
            return (Y)object;
        }
    }

    protected int getCount() {
        synchronized (this) {
            int n = this.cache.size();
            return n;
        }
    }

    public long getCurrentSize() {
        synchronized (this) {
            long l = this.currentSize;
            return l;
        }
    }

    public long getMaxSize() {
        synchronized (this) {
            long l = this.maxSize;
            return l;
        }
    }

    protected int getSize(Y y) {
        return 1;
    }

    protected void onItemEvicted(T t, Y y) {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Y put(T t, Y y) {
        synchronized (this) {
            void var4_4;
            void var2_2;
            int n = this.getSize(var2_2);
            if ((long)n >= this.maxSize) {
                this.onItemEvicted(t, var2_2);
                return null;
            }
            if (var2_2 != null) {
                this.currentSize += (long)n;
            }
            if ((var4_4 = this.cache.put(t, var2_2)) != null) {
                this.currentSize -= (long)this.getSize(var4_4);
                if (!var4_4.equals(var2_2)) {
                    this.onItemEvicted(t, var4_4);
                }
            }
            this.evict();
            return var4_4;
        }
    }

    public Y remove(T object) {
        synchronized (this) {
            block4 : {
                object = this.cache.remove(object);
                if (object == null) break block4;
                this.currentSize -= (long)this.getSize(object);
            }
            return (Y)object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSizeMultiplier(float f) {
        synchronized (this) {
            Throwable throwable2;
            if (f >= 0.0f) {
                try {
                    this.maxSize = Math.round((float)this.initialMaxSize * f);
                    this.evict();
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                throw new IllegalArgumentException("Multiplier must be >= 0");
            }
            throw throwable2;
        }
    }

    protected void trimToSize(long l) {
        synchronized (this) {
            while (this.currentSize > l) {
                Iterator<Map.Entry<T, Y>> iterator = this.cache.entrySet().iterator();
                Map.Entry entry = iterator.next();
                Y y = entry.getValue();
                this.currentSize -= (long)this.getSize(y);
                entry = entry.getKey();
                iterator.remove();
                this.onItemEvicted(entry, y);
            }
            return;
        }
    }
}

