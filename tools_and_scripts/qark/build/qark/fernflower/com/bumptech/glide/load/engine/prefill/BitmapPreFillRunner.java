package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

final class BitmapPreFillRunner implements Runnable {
   static final int BACKOFF_RATIO = 4;
   private static final BitmapPreFillRunner.Clock DEFAULT_CLOCK = new BitmapPreFillRunner.Clock();
   static final long INITIAL_BACKOFF_MS = 40L;
   static final long MAX_BACKOFF_MS;
   static final long MAX_DURATION_MS = 32L;
   static final String TAG = "PreFillRunner";
   private final BitmapPool bitmapPool;
   private final BitmapPreFillRunner.Clock clock;
   private long currentDelay;
   private final Handler handler;
   private boolean isCancelled;
   private final MemoryCache memoryCache;
   private final Set seenTypes;
   private final PreFillQueue toPrefill;

   static {
      MAX_BACKOFF_MS = TimeUnit.SECONDS.toMillis(1L);
   }

   public BitmapPreFillRunner(BitmapPool var1, MemoryCache var2, PreFillQueue var3) {
      this(var1, var2, var3, DEFAULT_CLOCK, new Handler(Looper.getMainLooper()));
   }

   BitmapPreFillRunner(BitmapPool var1, MemoryCache var2, PreFillQueue var3, BitmapPreFillRunner.Clock var4, Handler var5) {
      this.seenTypes = new HashSet();
      this.currentDelay = 40L;
      this.bitmapPool = var1;
      this.memoryCache = var2;
      this.toPrefill = var3;
      this.clock = var4;
      this.handler = var5;
   }

   private long getFreeMemoryCacheBytes() {
      return this.memoryCache.getMaxSize() - this.memoryCache.getCurrentSize();
   }

   private long getNextDelay() {
      long var1 = this.currentDelay;
      this.currentDelay = Math.min(this.currentDelay * 4L, MAX_BACKOFF_MS);
      return var1;
   }

   private boolean isGcDetected(long var1) {
      return this.clock.now() - var1 >= 32L;
   }

   boolean allocate() {
      long var2 = this.clock.now();

      while(!this.toPrefill.isEmpty() && !this.isGcDetected(var2)) {
         PreFillType var5 = this.toPrefill.remove();
         Bitmap var4;
         if (!this.seenTypes.contains(var5)) {
            this.seenTypes.add(var5);
            var4 = this.bitmapPool.getDirty(var5.getWidth(), var5.getHeight(), var5.getConfig());
         } else {
            var4 = Bitmap.createBitmap(var5.getWidth(), var5.getHeight(), var5.getConfig());
         }

         int var1 = Util.getBitmapByteSize(var4);
         if (this.getFreeMemoryCacheBytes() >= (long)var1) {
            BitmapPreFillRunner.UniqueKey var6 = new BitmapPreFillRunner.UniqueKey();
            this.memoryCache.put(var6, BitmapResource.obtain(var4, this.bitmapPool));
         } else {
            this.bitmapPool.put(var4);
         }

         if (Log.isLoggable("PreFillRunner", 3)) {
            StringBuilder var7 = new StringBuilder();
            var7.append("allocated [");
            var7.append(var5.getWidth());
            var7.append("x");
            var7.append(var5.getHeight());
            var7.append("] ");
            var7.append(var5.getConfig());
            var7.append(" size: ");
            var7.append(var1);
            Log.d("PreFillRunner", var7.toString());
         }
      }

      return !this.isCancelled && !this.toPrefill.isEmpty();
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public void run() {
      if (this.allocate()) {
         this.handler.postDelayed(this, this.getNextDelay());
      }

   }

   static class Clock {
      long now() {
         return SystemClock.currentThreadTimeMillis();
      }
   }

   private static final class UniqueKey implements Key {
      UniqueKey() {
      }

      public void updateDiskCacheKey(MessageDigest var1) {
         throw new UnsupportedOperationException();
      }
   }
}
