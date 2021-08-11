// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.os.Message;
import android.os.Looper;
import android.graphics.Bitmap;
import android.os.HandlerThread;
import android.os.Handler;

class Stats
{
    private static final int BITMAP_DECODE_FINISHED = 2;
    private static final int BITMAP_TRANSFORMED_FINISHED = 3;
    private static final int CACHE_HIT = 0;
    private static final int CACHE_MISS = 1;
    private static final int DOWNLOAD_FINISHED = 4;
    private static final String STATS_THREAD_NAME = "Picasso-Stats";
    long averageDownloadSize;
    long averageOriginalBitmapSize;
    long averageTransformedBitmapSize;
    final Cache cache;
    long cacheHits;
    long cacheMisses;
    int downloadCount;
    final Handler handler;
    int originalBitmapCount;
    final HandlerThread statsThread;
    long totalDownloadSize;
    long totalOriginalBitmapSize;
    long totalTransformedBitmapSize;
    int transformedBitmapCount;
    
    Stats(final Cache cache) {
        this.cache = cache;
        (this.statsThread = new HandlerThread("Picasso-Stats", 10)).start();
        Utils.flushStackLocalLeaks(this.statsThread.getLooper());
        this.handler = new StatsHandler(this.statsThread.getLooper(), this);
    }
    
    private static long getAverage(final int n, final long n2) {
        return n2 / n;
    }
    
    private void processBitmap(final Bitmap bitmap, final int n) {
        this.handler.sendMessage(this.handler.obtainMessage(n, Utils.getBitmapBytes(bitmap), 0));
    }
    
    StatsSnapshot createSnapshot() {
        return new StatsSnapshot(this.cache.maxSize(), this.cache.size(), this.cacheHits, this.cacheMisses, this.totalDownloadSize, this.totalOriginalBitmapSize, this.totalTransformedBitmapSize, this.averageDownloadSize, this.averageOriginalBitmapSize, this.averageTransformedBitmapSize, this.downloadCount, this.originalBitmapCount, this.transformedBitmapCount, System.currentTimeMillis());
    }
    
    void dispatchBitmapDecoded(final Bitmap bitmap) {
        this.processBitmap(bitmap, 2);
    }
    
    void dispatchBitmapTransformed(final Bitmap bitmap) {
        this.processBitmap(bitmap, 3);
    }
    
    void dispatchCacheHit() {
        this.handler.sendEmptyMessage(0);
    }
    
    void dispatchCacheMiss() {
        this.handler.sendEmptyMessage(1);
    }
    
    void dispatchDownloadFinished(final long n) {
        this.handler.sendMessage(this.handler.obtainMessage(4, (Object)n));
    }
    
    void performBitmapDecoded(final long n) {
        ++this.originalBitmapCount;
        this.totalOriginalBitmapSize += n;
        this.averageOriginalBitmapSize = getAverage(this.originalBitmapCount, this.totalOriginalBitmapSize);
    }
    
    void performBitmapTransformed(final long n) {
        ++this.transformedBitmapCount;
        this.totalTransformedBitmapSize += n;
        this.averageTransformedBitmapSize = getAverage(this.originalBitmapCount, this.totalTransformedBitmapSize);
    }
    
    void performCacheHit() {
        ++this.cacheHits;
    }
    
    void performCacheMiss() {
        ++this.cacheMisses;
    }
    
    void performDownloadFinished(final Long n) {
        ++this.downloadCount;
        this.totalDownloadSize += n;
        this.averageDownloadSize = getAverage(this.downloadCount, this.totalDownloadSize);
    }
    
    void shutdown() {
        this.statsThread.quit();
    }
    
    private static class StatsHandler extends Handler
    {
        private final Stats stats;
        
        public StatsHandler(final Looper looper, final Stats stats) {
            super(looper);
            this.stats = stats;
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    Picasso.HANDLER.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            throw new AssertionError((Object)("Unhandled stats message." + message.what));
                        }
                    });
                }
                case 0: {
                    this.stats.performCacheHit();
                }
                case 1: {
                    this.stats.performCacheMiss();
                }
                case 2: {
                    this.stats.performBitmapDecoded(message.arg1);
                }
                case 3: {
                    this.stats.performBitmapTransformed(message.arg1);
                }
                case 4: {
                    this.stats.performDownloadFinished((Long)message.obj);
                }
            }
        }
    }
}
