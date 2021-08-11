package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool implements BitmapPool {
   private static final Config DEFAULT_CONFIG;
   private static final String TAG = "LruBitmapPool";
   private final Set allowedConfigs;
   private long currentSize;
   private int evictions;
   private int hits;
   private final long initialMaxSize;
   private long maxSize;
   private int misses;
   private int puts;
   private final LruPoolStrategy strategy;
   private final LruBitmapPool.BitmapTracker tracker;

   static {
      DEFAULT_CONFIG = Config.ARGB_8888;
   }

   public LruBitmapPool(long var1) {
      this(var1, getDefaultStrategy(), getDefaultAllowedConfigs());
   }

   LruBitmapPool(long var1, LruPoolStrategy var3, Set var4) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
      this.strategy = var3;
      this.allowedConfigs = var4;
      this.tracker = new LruBitmapPool.NullBitmapTracker();
   }

   public LruBitmapPool(long var1, Set var3) {
      this(var1, getDefaultStrategy(), var3);
   }

   private static void assertNotHardwareConfig(Config var0) {
      if (VERSION.SDK_INT >= 26) {
         if (var0 == Config.HARDWARE) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Cannot create a mutable Bitmap with config: ");
            var1.append(var0);
            var1.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
            throw new IllegalArgumentException(var1.toString());
         }
      }
   }

   private static Bitmap createBitmap(int var0, int var1, Config var2) {
      if (var2 == null) {
         var2 = DEFAULT_CONFIG;
      }

      return Bitmap.createBitmap(var0, var1, var2);
   }

   private void dump() {
      if (Log.isLoggable("LruBitmapPool", 2)) {
         this.dumpUnchecked();
      }

   }

   private void dumpUnchecked() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Hits=");
      var1.append(this.hits);
      var1.append(", misses=");
      var1.append(this.misses);
      var1.append(", puts=");
      var1.append(this.puts);
      var1.append(", evictions=");
      var1.append(this.evictions);
      var1.append(", currentSize=");
      var1.append(this.currentSize);
      var1.append(", maxSize=");
      var1.append(this.maxSize);
      var1.append("\nStrategy=");
      var1.append(this.strategy);
      Log.v("LruBitmapPool", var1.toString());
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   private static Set getDefaultAllowedConfigs() {
      HashSet var0 = new HashSet(Arrays.asList(Config.values()));
      if (VERSION.SDK_INT >= 19) {
         var0.add((Object)null);
      }

      if (VERSION.SDK_INT >= 26) {
         var0.remove(Config.HARDWARE);
      }

      return Collections.unmodifiableSet(var0);
   }

   private static LruPoolStrategy getDefaultStrategy() {
      return (LruPoolStrategy)(VERSION.SDK_INT >= 19 ? new SizeConfigStrategy() : new AttributeStrategy());
   }

   private Bitmap getDirtyOrNull(int var1, int var2, Config var3) {
      synchronized(this){}

      Throwable var10000;
      label584: {
         LruPoolStrategy var5;
         boolean var10001;
         try {
            assertNotHardwareConfig(var3);
            var5 = this.strategy;
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label584;
         }

         Config var4;
         if (var3 != null) {
            var4 = var3;
         } else {
            try {
               var4 = DEFAULT_CONFIG;
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break label584;
            }
         }

         Bitmap var79;
         try {
            var79 = var5.get(var1, var2, var4);
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label584;
         }

         StringBuilder var80;
         if (var79 == null) {
            try {
               if (Log.isLoggable("LruBitmapPool", 3)) {
                  var80 = new StringBuilder();
                  var80.append("Missing bitmap=");
                  var80.append(this.strategy.logBitmap(var1, var2, var3));
                  Log.d("LruBitmapPool", var80.toString());
               }
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label584;
            }

            try {
               ++this.misses;
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label584;
            }
         } else {
            try {
               ++this.hits;
               this.currentSize -= (long)this.strategy.getSize(var79);
               this.tracker.remove(var79);
               normalize(var79);
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label584;
            }
         }

         try {
            if (Log.isLoggable("LruBitmapPool", 2)) {
               var80 = new StringBuilder();
               var80.append("Get bitmap=");
               var80.append(this.strategy.logBitmap(var1, var2, var3));
               Log.v("LruBitmapPool", var80.toString());
            }
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            break label584;
         }

         label555:
         try {
            this.dump();
            return var79;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label555;
         }
      }

      Throwable var78 = var10000;
      throw var78;
   }

   private static void maybeSetPreMultiplied(Bitmap var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.setPremultiplied(true);
      }

   }

   private static void normalize(Bitmap var0) {
      var0.setHasAlpha(true);
      maybeSetPreMultiplied(var0);
   }

   private void trimToSize(long var1) {
      synchronized(this){}

      Throwable var10000;
      while(true) {
         boolean var10001;
         Bitmap var3;
         label335: {
            try {
               if (this.currentSize > var1) {
                  var3 = this.strategy.removeLast();
                  break label335;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break;
            }

            return;
         }

         if (var3 == null) {
            try {
               if (Log.isLoggable("LruBitmapPool", 5)) {
                  Log.w("LruBitmapPool", "Size mismatch, resetting");
                  this.dumpUnchecked();
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break;
            }

            try {
               this.currentSize = 0L;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break;
            }

            return;
         } else {
            try {
               this.tracker.remove(var3);
               this.currentSize -= (long)this.strategy.getSize(var3);
               ++this.evictions;
               if (Log.isLoggable("LruBitmapPool", 3)) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Evicting bitmap=");
                  var4.append(this.strategy.logBitmap(var3));
                  Log.d("LruBitmapPool", var4.toString());
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break;
            }

            try {
               this.dump();
               var3.recycle();
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }

   public void clearMemory() {
      if (Log.isLoggable("LruBitmapPool", 3)) {
         Log.d("LruBitmapPool", "clearMemory");
      }

      this.trimToSize(0L);
   }

   public long evictionCount() {
      return (long)this.evictions;
   }

   public Bitmap get(int var1, int var2, Config var3) {
      Bitmap var4 = this.getDirtyOrNull(var1, var2, var3);
      if (var4 != null) {
         var4.eraseColor(0);
         return var4;
      } else {
         return createBitmap(var1, var2, var3);
      }
   }

   public long getCurrentSize() {
      return this.currentSize;
   }

   public Bitmap getDirty(int var1, int var2, Config var3) {
      Bitmap var5 = this.getDirtyOrNull(var1, var2, var3);
      Bitmap var4 = var5;
      if (var5 == null) {
         var4 = createBitmap(var1, var2, var3);
      }

      return var4;
   }

   public long getMaxSize() {
      return this.maxSize;
   }

   public long hitCount() {
      return (long)this.hits;
   }

   public long missCount() {
      return (long)this.misses;
   }

   public void put(Bitmap var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != null) {
         label567: {
            StringBuilder var3;
            label561: {
               label571: {
                  try {
                     if (var1.isRecycled()) {
                        break label571;
                     }

                     if (!var1.isMutable() || (long)this.strategy.getSize(var1) > this.maxSize || !this.allowedConfigs.contains(var1.getConfig())) {
                        break label561;
                     }
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label567;
                  }

                  try {
                     int var2 = this.strategy.getSize(var1);
                     this.strategy.put(var1);
                     this.tracker.add(var1);
                     ++this.puts;
                     this.currentSize += (long)var2;
                     if (Log.isLoggable("LruBitmapPool", 2)) {
                        var3 = new StringBuilder();
                        var3.append("Put bitmap in pool=");
                        var3.append(this.strategy.logBitmap(var1));
                        Log.v("LruBitmapPool", var3.toString());
                     }
                  } catch (Throwable var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label567;
                  }

                  try {
                     this.dump();
                     this.evict();
                  } catch (Throwable var53) {
                     var10000 = var53;
                     var10001 = false;
                     break label567;
                  }

                  return;
               }

               try {
                  throw new IllegalStateException("Cannot pool recycled bitmap");
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label567;
               }
            }

            try {
               if (Log.isLoggable("LruBitmapPool", 2)) {
                  var3 = new StringBuilder();
                  var3.append("Reject bitmap from pool, bitmap: ");
                  var3.append(this.strategy.logBitmap(var1));
                  var3.append(", is mutable: ");
                  var3.append(var1.isMutable());
                  var3.append(", is allowed config: ");
                  var3.append(this.allowedConfigs.contains(var1.getConfig()));
                  Log.v("LruBitmapPool", var3.toString());
               }
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label567;
            }

            try {
               var1.recycle();
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label567;
            }

            return;
         }
      } else {
         label564:
         try {
            throw new NullPointerException("Bitmap must not be null");
         } catch (Throwable var59) {
            var10000 = var59;
            var10001 = false;
            break label564;
         }
      }

      Throwable var60 = var10000;
      throw var60;
   }

   public void setSizeMultiplier(float var1) {
      synchronized(this){}

      try {
         this.maxSize = (long)Math.round((float)this.initialMaxSize * var1);
         this.evict();
      } finally {
         ;
      }

   }

   public void trimMemory(int var1) {
      if (Log.isLoggable("LruBitmapPool", 3)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("trimMemory, level=");
         var2.append(var1);
         Log.d("LruBitmapPool", var2.toString());
      }

      if (var1 >= 40 || VERSION.SDK_INT >= 23 && var1 >= 20) {
         this.clearMemory();
      } else if (var1 >= 20 || var1 == 15) {
         this.trimToSize(this.getMaxSize() / 2L);
         return;
      }

   }

   private interface BitmapTracker {
      void add(Bitmap var1);

      void remove(Bitmap var1);
   }

   private static final class NullBitmapTracker implements LruBitmapPool.BitmapTracker {
      NullBitmapTracker() {
      }

      public void add(Bitmap var1) {
      }

      public void remove(Bitmap var1) {
      }
   }

   private static class ThrowingBitmapTracker implements LruBitmapPool.BitmapTracker {
      private final Set bitmaps = Collections.synchronizedSet(new HashSet());

      public void add(Bitmap var1) {
         if (!this.bitmaps.contains(var1)) {
            this.bitmaps.add(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Can't add already added bitmap: ");
            var2.append(var1);
            var2.append(" [");
            var2.append(var1.getWidth());
            var2.append("x");
            var2.append(var1.getHeight());
            var2.append("]");
            throw new IllegalStateException(var2.toString());
         }
      }

      public void remove(Bitmap var1) {
         if (this.bitmaps.contains(var1)) {
            this.bitmaps.remove(var1);
         } else {
            throw new IllegalStateException("Cannot remove bitmap not in tracker");
         }
      }
   }
}
