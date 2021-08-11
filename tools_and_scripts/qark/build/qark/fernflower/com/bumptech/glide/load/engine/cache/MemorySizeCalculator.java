package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.util.Preconditions;

public final class MemorySizeCalculator {
   static final int BYTES_PER_ARGB_8888_PIXEL = 4;
   private static final int LOW_MEMORY_BYTE_ARRAY_POOL_DIVISOR = 2;
   private static final String TAG = "MemorySizeCalculator";
   private final int arrayPoolSize;
   private final int bitmapPoolSize;
   private final Context context;
   private final int memoryCacheSize;

   MemorySizeCalculator(MemorySizeCalculator.Builder var1) {
      this.context = var1.context;
      int var3;
      if (isLowMemoryDevice(var1.activityManager)) {
         var3 = var1.arrayPoolSizeBytes / 2;
      } else {
         var3 = var1.arrayPoolSizeBytes;
      }

      this.arrayPoolSize = var3;
      var3 = getMaxSize(var1.activityManager, var1.maxSizeMultiplier, var1.lowMemoryMaxSizeMultiplier);
      int var5 = var1.screenDimensions.getWidthPixels() * var1.screenDimensions.getHeightPixels() * 4;
      int var4 = Math.round((float)var5 * var1.bitmapPoolScreens);
      var5 = Math.round((float)var5 * var1.memoryCacheScreens);
      int var6 = var3 - this.arrayPoolSize;
      if (var5 + var4 <= var6) {
         this.memoryCacheSize = var5;
         this.bitmapPoolSize = var4;
      } else {
         float var2 = (float)var6 / (var1.bitmapPoolScreens + var1.memoryCacheScreens);
         this.memoryCacheSize = Math.round(var1.memoryCacheScreens * var2);
         this.bitmapPoolSize = Math.round(var1.bitmapPoolScreens * var2);
      }

      if (Log.isLoggable("MemorySizeCalculator", 3)) {
         StringBuilder var8 = new StringBuilder();
         var8.append("Calculation complete, Calculated memory cache size: ");
         var8.append(this.toMb(this.memoryCacheSize));
         var8.append(", pool size: ");
         var8.append(this.toMb(this.bitmapPoolSize));
         var8.append(", byte array size: ");
         var8.append(this.toMb(this.arrayPoolSize));
         var8.append(", memory class limited? ");
         boolean var7;
         if (var5 + var4 > var3) {
            var7 = true;
         } else {
            var7 = false;
         }

         var8.append(var7);
         var8.append(", max size: ");
         var8.append(this.toMb(var3));
         var8.append(", memoryClass: ");
         var8.append(var1.activityManager.getMemoryClass());
         var8.append(", isLowMemoryDevice: ");
         var8.append(isLowMemoryDevice(var1.activityManager));
         Log.d("MemorySizeCalculator", var8.toString());
      }

   }

   private static int getMaxSize(ActivityManager var0, float var1, float var2) {
      int var4 = var0.getMemoryClass();
      boolean var5 = isLowMemoryDevice(var0);
      float var3 = (float)(var4 * 1024 * 1024);
      if (var5) {
         var1 = var2;
      }

      return Math.round(var3 * var1);
   }

   static boolean isLowMemoryDevice(ActivityManager var0) {
      return VERSION.SDK_INT >= 19 ? var0.isLowRamDevice() : true;
   }

   private String toMb(int var1) {
      return Formatter.formatFileSize(this.context, (long)var1);
   }

   public int getArrayPoolSizeInBytes() {
      return this.arrayPoolSize;
   }

   public int getBitmapPoolSize() {
      return this.bitmapPoolSize;
   }

   public int getMemoryCacheSize() {
      return this.memoryCacheSize;
   }

   public static final class Builder {
      static final int ARRAY_POOL_SIZE_BYTES = 4194304;
      static final int BITMAP_POOL_TARGET_SCREENS;
      static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33F;
      static final float MAX_SIZE_MULTIPLIER = 0.4F;
      static final int MEMORY_CACHE_TARGET_SCREENS = 2;
      ActivityManager activityManager;
      int arrayPoolSizeBytes;
      float bitmapPoolScreens;
      final Context context;
      float lowMemoryMaxSizeMultiplier;
      float maxSizeMultiplier;
      float memoryCacheScreens = 2.0F;
      MemorySizeCalculator.ScreenDimensions screenDimensions;

      static {
         byte var0;
         if (VERSION.SDK_INT < 26) {
            var0 = 4;
         } else {
            var0 = 1;
         }

         BITMAP_POOL_TARGET_SCREENS = var0;
      }

      public Builder(Context var1) {
         this.bitmapPoolScreens = (float)BITMAP_POOL_TARGET_SCREENS;
         this.maxSizeMultiplier = 0.4F;
         this.lowMemoryMaxSizeMultiplier = 0.33F;
         this.arrayPoolSizeBytes = 4194304;
         this.context = var1;
         this.activityManager = (ActivityManager)var1.getSystemService("activity");
         this.screenDimensions = new MemorySizeCalculator.DisplayMetricsScreenDimensions(var1.getResources().getDisplayMetrics());
         if (VERSION.SDK_INT >= 26 && MemorySizeCalculator.isLowMemoryDevice(this.activityManager)) {
            this.bitmapPoolScreens = 0.0F;
         }

      }

      public MemorySizeCalculator build() {
         return new MemorySizeCalculator(this);
      }

      MemorySizeCalculator.Builder setActivityManager(ActivityManager var1) {
         this.activityManager = var1;
         return this;
      }

      public MemorySizeCalculator.Builder setArrayPoolSize(int var1) {
         this.arrayPoolSizeBytes = var1;
         return this;
      }

      public MemorySizeCalculator.Builder setBitmapPoolScreens(float var1) {
         boolean var2;
         if (var1 >= 0.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         Preconditions.checkArgument(var2, "Bitmap pool screens must be greater than or equal to 0");
         this.bitmapPoolScreens = var1;
         return this;
      }

      public MemorySizeCalculator.Builder setLowMemoryMaxSizeMultiplier(float var1) {
         boolean var2;
         if (var1 >= 0.0F && var1 <= 1.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         Preconditions.checkArgument(var2, "Low memory max size multiplier must be between 0 and 1");
         this.lowMemoryMaxSizeMultiplier = var1;
         return this;
      }

      public MemorySizeCalculator.Builder setMaxSizeMultiplier(float var1) {
         boolean var2;
         if (var1 >= 0.0F && var1 <= 1.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         Preconditions.checkArgument(var2, "Size multiplier must be between 0 and 1");
         this.maxSizeMultiplier = var1;
         return this;
      }

      public MemorySizeCalculator.Builder setMemoryCacheScreens(float var1) {
         boolean var2;
         if (var1 >= 0.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         Preconditions.checkArgument(var2, "Memory cache screens must be greater than or equal to 0");
         this.memoryCacheScreens = var1;
         return this;
      }

      MemorySizeCalculator.Builder setScreenDimensions(MemorySizeCalculator.ScreenDimensions var1) {
         this.screenDimensions = var1;
         return this;
      }
   }

   private static final class DisplayMetricsScreenDimensions implements MemorySizeCalculator.ScreenDimensions {
      private final DisplayMetrics displayMetrics;

      DisplayMetricsScreenDimensions(DisplayMetrics var1) {
         this.displayMetrics = var1;
      }

      public int getHeightPixels() {
         return this.displayMetrics.heightPixels;
      }

      public int getWidthPixels() {
         return this.displayMetrics.widthPixels;
      }
   }

   interface ScreenDimensions {
      int getHeightPixels();

      int getWidthPixels();
   }
}
