/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package com.bumptech.glide.load.resource.bitmap;

import android.os.Build;
import com.bumptech.glide.load.Option;

public abstract class DownsampleStrategy {
    public static final DownsampleStrategy AT_LEAST;
    public static final DownsampleStrategy AT_MOST;
    public static final DownsampleStrategy CENTER_INSIDE;
    public static final DownsampleStrategy CENTER_OUTSIDE;
    public static final DownsampleStrategy DEFAULT;
    public static final DownsampleStrategy FIT_CENTER;
    static final boolean IS_BITMAP_FACTORY_SCALING_SUPPORTED;
    public static final DownsampleStrategy NONE;
    public static final Option<DownsampleStrategy> OPTION;

    static {
        DownsampleStrategy downsampleStrategy;
        AT_LEAST = new AtLeast();
        AT_MOST = new AtMost();
        FIT_CENTER = new FitCenter();
        CENTER_INSIDE = new CenterInside();
        CENTER_OUTSIDE = new CenterOutside();
        NONE = new None();
        DEFAULT = downsampleStrategy = CENTER_OUTSIDE;
        OPTION = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", downsampleStrategy);
        boolean bl = Build.VERSION.SDK_INT >= 19;
        IS_BITMAP_FACTORY_SCALING_SUPPORTED = bl;
    }

    public abstract SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4);

    public abstract float getScaleFactor(int var1, int var2, int var3, int var4);

    private static class AtLeast
    extends DownsampleStrategy {
        AtLeast() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            return SampleSizeRounding.QUALITY;
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            if ((n = Math.min(n2 / n4, n / n3)) == 0) {
                return 1.0f;
            }
            return 1.0f / (float)Integer.highestOneBit(n);
        }
    }

    private static class AtMost
    extends DownsampleStrategy {
        AtMost() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            return SampleSizeRounding.MEMORY;
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            n3 = (int)Math.ceil(Math.max((float)n2 / (float)n4, (float)n / (float)n3));
            n2 = Integer.highestOneBit(n3);
            n = 1;
            if ((n2 = Math.max(1, n2)) >= n3) {
                n = 0;
            }
            return 1.0f / (float)(n2 << n);
        }
    }

    private static class CenterInside
    extends DownsampleStrategy {
        CenterInside() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            if (this.getScaleFactor(n, n2, n3, n4) == 1.0f) {
                return SampleSizeRounding.QUALITY;
            }
            return FIT_CENTER.getSampleSizeRounding(n, n2, n3, n4);
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            return Math.min(1.0f, FIT_CENTER.getScaleFactor(n, n2, n3, n4));
        }
    }

    private static class CenterOutside
    extends DownsampleStrategy {
        CenterOutside() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            return SampleSizeRounding.QUALITY;
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            return Math.max((float)n3 / (float)n, (float)n4 / (float)n2);
        }
    }

    private static class FitCenter
    extends DownsampleStrategy {
        FitCenter() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            if (IS_BITMAP_FACTORY_SCALING_SUPPORTED) {
                return SampleSizeRounding.QUALITY;
            }
            return SampleSizeRounding.MEMORY;
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            if (IS_BITMAP_FACTORY_SCALING_SUPPORTED) {
                return Math.min((float)n3 / (float)n, (float)n4 / (float)n2);
            }
            if ((n = Math.max(n2 / n4, n / n3)) == 0) {
                return 1.0f;
            }
            return 1.0f / (float)Integer.highestOneBit(n);
        }
    }

    private static class None
    extends DownsampleStrategy {
        None() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int n, int n2, int n3, int n4) {
            return SampleSizeRounding.QUALITY;
        }

        @Override
        public float getScaleFactor(int n, int n2, int n3, int n4) {
            return 1.0f;
        }
    }

    public static final class SampleSizeRounding
    extends Enum<SampleSizeRounding> {
        private static final /* synthetic */ SampleSizeRounding[] $VALUES;
        public static final /* enum */ SampleSizeRounding MEMORY;
        public static final /* enum */ SampleSizeRounding QUALITY;

        static {
            SampleSizeRounding sampleSizeRounding;
            MEMORY = new SampleSizeRounding();
            QUALITY = sampleSizeRounding = new SampleSizeRounding();
            $VALUES = new SampleSizeRounding[]{MEMORY, sampleSizeRounding};
        }

        private SampleSizeRounding() {
        }

        public static SampleSizeRounding valueOf(String string2) {
            return Enum.valueOf(SampleSizeRounding.class, string2);
        }

        public static SampleSizeRounding[] values() {
            return (SampleSizeRounding[])$VALUES.clone();
        }
    }

}

