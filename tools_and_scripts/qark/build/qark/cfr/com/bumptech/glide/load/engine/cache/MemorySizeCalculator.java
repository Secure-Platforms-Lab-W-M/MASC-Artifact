/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.content.Context
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.format.Formatter
 *  android.util.DisplayMetrics
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
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

    MemorySizeCalculator(Builder builder) {
        this.context = builder.context;
        int n = MemorySizeCalculator.isLowMemoryDevice(builder.activityManager) ? builder.arrayPoolSizeBytes / 2 : builder.arrayPoolSizeBytes;
        this.arrayPoolSize = n;
        n = MemorySizeCalculator.getMaxSize(builder.activityManager, builder.maxSizeMultiplier, builder.lowMemoryMaxSizeMultiplier);
        int n2 = builder.screenDimensions.getWidthPixels() * builder.screenDimensions.getHeightPixels() * 4;
        int n3 = Math.round((float)n2 * builder.bitmapPoolScreens);
        n2 = Math.round((float)n2 * builder.memoryCacheScreens);
        int n4 = n - this.arrayPoolSize;
        if (n2 + n3 <= n4) {
            this.memoryCacheSize = n2;
            this.bitmapPoolSize = n3;
        } else {
            float f = (float)n4 / (builder.bitmapPoolScreens + builder.memoryCacheScreens);
            this.memoryCacheSize = Math.round(builder.memoryCacheScreens * f);
            this.bitmapPoolSize = Math.round(builder.bitmapPoolScreens * f);
        }
        if (Log.isLoggable((String)"MemorySizeCalculator", (int)3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Calculation complete, Calculated memory cache size: ");
            stringBuilder.append(this.toMb(this.memoryCacheSize));
            stringBuilder.append(", pool size: ");
            stringBuilder.append(this.toMb(this.bitmapPoolSize));
            stringBuilder.append(", byte array size: ");
            stringBuilder.append(this.toMb(this.arrayPoolSize));
            stringBuilder.append(", memory class limited? ");
            boolean bl = n2 + n3 > n;
            stringBuilder.append(bl);
            stringBuilder.append(", max size: ");
            stringBuilder.append(this.toMb(n));
            stringBuilder.append(", memoryClass: ");
            stringBuilder.append(builder.activityManager.getMemoryClass());
            stringBuilder.append(", isLowMemoryDevice: ");
            stringBuilder.append(MemorySizeCalculator.isLowMemoryDevice(builder.activityManager));
            Log.d((String)"MemorySizeCalculator", (String)stringBuilder.toString());
        }
    }

    private static int getMaxSize(ActivityManager activityManager, float f, float f2) {
        int n = activityManager.getMemoryClass();
        boolean bl = MemorySizeCalculator.isLowMemoryDevice(activityManager);
        float f3 = n * 1024 * 1024;
        if (bl) {
            f = f2;
        }
        return Math.round(f3 * f);
    }

    static boolean isLowMemoryDevice(ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT >= 19) {
            return activityManager.isLowRamDevice();
        }
        return true;
    }

    private String toMb(int n) {
        return Formatter.formatFileSize((Context)this.context, (long)n);
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
        static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;
        static final float MAX_SIZE_MULTIPLIER = 0.4f;
        static final int MEMORY_CACHE_TARGET_SCREENS = 2;
        ActivityManager activityManager;
        int arrayPoolSizeBytes = 4194304;
        float bitmapPoolScreens = BITMAP_POOL_TARGET_SCREENS;
        final Context context;
        float lowMemoryMaxSizeMultiplier = 0.33f;
        float maxSizeMultiplier = 0.4f;
        float memoryCacheScreens = 2.0f;
        ScreenDimensions screenDimensions;

        static {
            int n = Build.VERSION.SDK_INT < 26 ? 4 : 1;
            BITMAP_POOL_TARGET_SCREENS = n;
        }

        public Builder(Context context) {
            this.context = context;
            this.activityManager = (ActivityManager)context.getSystemService("activity");
            this.screenDimensions = new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= 26 && MemorySizeCalculator.isLowMemoryDevice(this.activityManager)) {
                this.bitmapPoolScreens = 0.0f;
            }
        }

        public MemorySizeCalculator build() {
            return new MemorySizeCalculator(this);
        }

        Builder setActivityManager(ActivityManager activityManager) {
            this.activityManager = activityManager;
            return this;
        }

        public Builder setArrayPoolSize(int n) {
            this.arrayPoolSizeBytes = n;
            return this;
        }

        public Builder setBitmapPoolScreens(float f) {
            boolean bl = f >= 0.0f;
            Preconditions.checkArgument(bl, "Bitmap pool screens must be greater than or equal to 0");
            this.bitmapPoolScreens = f;
            return this;
        }

        public Builder setLowMemoryMaxSizeMultiplier(float f) {
            boolean bl = f >= 0.0f && f <= 1.0f;
            Preconditions.checkArgument(bl, "Low memory max size multiplier must be between 0 and 1");
            this.lowMemoryMaxSizeMultiplier = f;
            return this;
        }

        public Builder setMaxSizeMultiplier(float f) {
            boolean bl = f >= 0.0f && f <= 1.0f;
            Preconditions.checkArgument(bl, "Size multiplier must be between 0 and 1");
            this.maxSizeMultiplier = f;
            return this;
        }

        public Builder setMemoryCacheScreens(float f) {
            boolean bl = f >= 0.0f;
            Preconditions.checkArgument(bl, "Memory cache screens must be greater than or equal to 0");
            this.memoryCacheScreens = f;
            return this;
        }

        Builder setScreenDimensions(ScreenDimensions screenDimensions) {
            this.screenDimensions = screenDimensions;
            return this;
        }
    }

    private static final class DisplayMetricsScreenDimensions
    implements ScreenDimensions {
        private final DisplayMetrics displayMetrics;

        DisplayMetricsScreenDimensions(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }

        @Override
        public int getHeightPixels() {
            return this.displayMetrics.heightPixels;
        }

        @Override
        public int getWidthPixels() {
            return this.displayMetrics.widthPixels;
        }
    }

    static interface ScreenDimensions {
        public int getHeightPixels();

        public int getWidthPixels();
    }

}

