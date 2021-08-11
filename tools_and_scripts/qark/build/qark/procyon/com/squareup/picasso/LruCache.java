// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.util.Iterator;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import java.util.LinkedHashMap;

public class LruCache implements Cache
{
    private int evictionCount;
    private int hitCount;
    final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive.");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }
    
    public LruCache(final Context context) {
        this(Utils.calculateMemoryCacheSize(context));
    }
    
    private void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    throw new IllegalStateException(this.getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }
            }
            if (this.size <= n || this.map.isEmpty()) {
                break;
            }
            final Map.Entry<String, Bitmap> entry = this.map.entrySet().iterator().next();
            final String s = entry.getKey();
            final Bitmap bitmap = entry.getValue();
            this.map.remove(s);
            this.size -= Utils.getBitmapBytes(bitmap);
            ++this.evictionCount;
        }
        // monitorexit(this)
    }
    // monitorexit(this)
    
    @Override
    public final void clear() {
        synchronized (this) {
            this.evictAll();
        }
    }
    
    @Override
    public final void clearKeyUri(final String s) {
        // monitorenter(this)
        boolean b = false;
        try {
            final int length = s.length();
            final Iterator<Map.Entry<String, Bitmap>> iterator = this.map.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, Bitmap> entry = iterator.next();
                final String s2 = entry.getKey();
                final Bitmap bitmap = entry.getValue();
                final int index = s2.indexOf(10);
                if (index == length && s2.substring(0, index).equals(s)) {
                    iterator.remove();
                    this.size -= Utils.getBitmapBytes(bitmap);
                    b = true;
                }
            }
            if (b) {
                this.trimToSize(this.maxSize);
            }
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    @Override
    public Bitmap get(final String s) {
        if (s == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final Bitmap bitmap = this.map.get(s);
            if (bitmap != null) {
                ++this.hitCount;
                return bitmap;
            }
            ++this.missCount;
            return null;
        }
    }
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    @Override
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
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    @Override
    public void set(final String s, final Bitmap bitmap) {
        if (s == null || bitmap == null) {
            throw new NullPointerException("key == null || bitmap == null");
        }
        synchronized (this) {
            ++this.putCount;
            this.size += Utils.getBitmapBytes(bitmap);
            final Bitmap bitmap2 = this.map.put(s, bitmap);
            if (bitmap2 != null) {
                this.size -= Utils.getBitmapBytes(bitmap2);
            }
            // monitorexit(this)
            this.trimToSize(this.maxSize);
        }
    }
    
    @Override
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
}
