/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.SystemClock
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.PreFillQueue;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

final class BitmapPreFillRunner
implements Runnable {
    static final int BACKOFF_RATIO = 4;
    private static final Clock DEFAULT_CLOCK = new Clock();
    static final long INITIAL_BACKOFF_MS = 40L;
    static final long MAX_BACKOFF_MS = TimeUnit.SECONDS.toMillis(1L);
    static final long MAX_DURATION_MS = 32L;
    static final String TAG = "PreFillRunner";
    private final BitmapPool bitmapPool;
    private final Clock clock;
    private long currentDelay = 40L;
    private final Handler handler;
    private boolean isCancelled;
    private final MemoryCache memoryCache;
    private final Set<PreFillType> seenTypes = new HashSet<PreFillType>();
    private final PreFillQueue toPrefill;

    public BitmapPreFillRunner(BitmapPool bitmapPool, MemoryCache memoryCache, PreFillQueue preFillQueue) {
        this(bitmapPool, memoryCache, preFillQueue, DEFAULT_CLOCK, new Handler(Looper.getMainLooper()));
    }

    BitmapPreFillRunner(BitmapPool bitmapPool, MemoryCache memoryCache, PreFillQueue preFillQueue, Clock clock, Handler handler) {
        this.bitmapPool = bitmapPool;
        this.memoryCache = memoryCache;
        this.toPrefill = preFillQueue;
        this.clock = clock;
        this.handler = handler;
    }

    private long getFreeMemoryCacheBytes() {
        return this.memoryCache.getMaxSize() - this.memoryCache.getCurrentSize();
    }

    private long getNextDelay() {
        long l = this.currentDelay;
        this.currentDelay = Math.min(this.currentDelay * 4L, MAX_BACKOFF_MS);
        return l;
    }

    private boolean isGcDetected(long l) {
        if (this.clock.now() - l >= 32L) {
            return true;
        }
        return false;
    }

    boolean allocate() {
        long l = this.clock.now();
        while (!this.toPrefill.isEmpty() && !this.isGcDetected(l)) {
            Object object;
            PreFillType preFillType = this.toPrefill.remove();
            if (!this.seenTypes.contains(preFillType)) {
                this.seenTypes.add(preFillType);
                object = this.bitmapPool.getDirty(preFillType.getWidth(), preFillType.getHeight(), preFillType.getConfig());
            } else {
                object = Bitmap.createBitmap((int)preFillType.getWidth(), (int)preFillType.getHeight(), (Bitmap.Config)preFillType.getConfig());
            }
            int n = Util.getBitmapByteSize((Bitmap)object);
            if (this.getFreeMemoryCacheBytes() >= (long)n) {
                UniqueKey uniqueKey = new UniqueKey();
                this.memoryCache.put(uniqueKey, BitmapResource.obtain((Bitmap)object, this.bitmapPool));
            } else {
                this.bitmapPool.put((Bitmap)object);
            }
            if (!Log.isLoggable((String)"PreFillRunner", (int)3)) continue;
            object = new StringBuilder();
            object.append("allocated [");
            object.append(preFillType.getWidth());
            object.append("x");
            object.append(preFillType.getHeight());
            object.append("] ");
            object.append((Object)preFillType.getConfig());
            object.append(" size: ");
            object.append(n);
            Log.d((String)"PreFillRunner", (String)object.toString());
        }
        if (!this.isCancelled && !this.toPrefill.isEmpty()) {
            return true;
        }
        return false;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public void run() {
        if (this.allocate()) {
            this.handler.postDelayed((Runnable)this, this.getNextDelay());
        }
    }

    static class Clock {
        Clock() {
        }

        long now() {
            return SystemClock.currentThreadTimeMillis();
        }
    }

    private static final class UniqueKey
    implements Key {
        UniqueKey() {
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            throw new UnsupportedOperationException();
        }
    }

}

