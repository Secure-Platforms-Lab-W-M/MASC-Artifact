/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.ColorSpace
 *  android.graphics.ColorSpace$Named
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.util.DisplayMetrics
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import com.bumptech.glide.load.resource.bitmap.ImageReader;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
    public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
    public static final Option<DecodeFormat> DECODE_FORMAT;
    @Deprecated
    public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY;
    private static final DecodeCallbacks EMPTY_CALLBACKS;
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
    private static final String ICO_MIME_TYPE = "image/x-ico";
    private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
    private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE;
    public static final Option<PreferredColorSpace> PREFERRED_COLOR_SPACE;
    static final String TAG = "Downsampler";
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT;
    private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
    private final BitmapPool bitmapPool;
    private final ArrayPool byteArrayPool;
    private final DisplayMetrics displayMetrics;
    private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
    private final List<ImageHeaderParser> parsers;

    static {
        DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
        PREFERRED_COLOR_SPACE = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.PreferredColorSpace", PreferredColorSpace.SRGB);
        DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
        Boolean bl = false;
        FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", bl);
        ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", bl);
        NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("image/vnd.wap.wbmp", "image/x-ico")));
        EMPTY_CALLBACKS = new DecodeCallbacks(){

            @Override
            public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) {
            }

            @Override
            public void onObtainBounds() {
            }
        };
        TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
        OPTIONS_QUEUE = Util.createQueue(0);
    }

    public Downsampler(List<ImageHeaderParser> list, DisplayMetrics displayMetrics, BitmapPool bitmapPool, ArrayPool arrayPool) {
        this.parsers = list;
        this.displayMetrics = Preconditions.checkNotNull(displayMetrics);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
        this.byteArrayPool = Preconditions.checkNotNull(arrayPool);
    }

    private static int adjustTargetDensityForError(double d) {
        int n = Downsampler.getDensityMultiplier(d);
        int n2 = Downsampler.round((double)n * d);
        return Downsampler.round((double)n2 * (d /= (double)((float)n2 / (float)n)));
    }

    private void calculateConfig(ImageReader imageReader, DecodeFormat decodeFormat, boolean bl, boolean bl2, BitmapFactory.Options options, int n, int n2) {
        if (this.hardwareConfigState.setHardwareConfigIfAllowed(n, n2, options, bl, bl2)) {
            return;
        }
        if (decodeFormat != DecodeFormat.PREFER_ARGB_8888 && Build.VERSION.SDK_INT != 16) {
            block5 : {
                bl2 = false;
                try {
                    bl = imageReader.getImageType().hasAlpha();
                }
                catch (IOException iOException) {
                    bl = bl2;
                    if (!Log.isLoggable((String)"Downsampler", (int)3)) break block5;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot determine whether the image has alpha or not from header, format ");
                    stringBuilder.append((Object)decodeFormat);
                    Log.d((String)"Downsampler", (String)stringBuilder.toString(), (Throwable)iOException);
                    bl = bl2;
                }
            }
            imageReader = bl ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            options.inPreferredConfig = imageReader;
            if (options.inPreferredConfig == Bitmap.Config.RGB_565) {
                options.inDither = true;
            }
            return;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    private static void calculateScaling(ImageHeaderParser.ImageType object, ImageReader object2, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool, DownsampleStrategy downsampleStrategy, int n, int n2, int n3, int n4, int n5, BitmapFactory.Options options) throws IOException {
        if (n2 > 0 && n3 > 0) {
            float f;
            int n6 = n2;
            int n7 = n3;
            if (Downsampler.isRotationRequired(n)) {
                n6 = n3;
                n7 = n2;
            }
            if ((f = downsampleStrategy.getScaleFactor(n6, n7, n4, n5)) > 0.0f) {
                DownsampleStrategy.SampleSizeRounding sampleSizeRounding = downsampleStrategy.getSampleSizeRounding(n6, n7, n4, n5);
                if (sampleSizeRounding != null) {
                    int n8 = Downsampler.round((float)n6 * f);
                    int n9 = Downsampler.round((float)n7 * f);
                    n8 = n6 / n8;
                    n9 = n7 / n9;
                    n9 = sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY ? Math.max(n8, n9) : Math.min(n8, n9);
                    if (Build.VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(options.outMimeType)) {
                        n9 = 1;
                    } else {
                        n9 = n8 = Math.max(1, Integer.highestOneBit(n9));
                        if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                            n9 = n8;
                            if ((float)n8 < 1.0f / f) {
                                n9 = n8 << 1;
                            }
                        }
                    }
                    options.inSampleSize = n9;
                    if (object == ImageHeaderParser.ImageType.JPEG) {
                        int n10 = Math.min(n9, 8);
                        n8 = (int)Math.ceil((float)n6 / (float)n10);
                        n10 = (int)Math.ceil((float)n7 / (float)n10);
                        int n11 = n9 / 8;
                        n7 = n8;
                        n6 = n10;
                        if (n11 > 0) {
                            n7 = n8 / n11;
                            n6 = n10 / n11;
                        }
                    } else if (object != ImageHeaderParser.ImageType.PNG && object != ImageHeaderParser.ImageType.PNG_A) {
                        if (object != ImageHeaderParser.ImageType.WEBP && object != ImageHeaderParser.ImageType.WEBP_A) {
                            if (n6 % n9 == 0 && n7 % n9 == 0) {
                                n8 = n7 / n9;
                                n7 = n6 /= n9;
                                n6 = n8;
                            } else {
                                object = Downsampler.getDimensions((ImageReader)object2, options, decodeCallbacks, bitmapPool);
                                n7 = object[0];
                                n6 = object[1];
                            }
                        } else if (Build.VERSION.SDK_INT >= 24) {
                            n8 = Math.round((float)n6 / (float)n9);
                            n6 = Math.round((float)n7 / (float)n9);
                            n7 = n8;
                        } else {
                            n8 = (int)Math.floor((float)n6 / (float)n9);
                            n6 = (int)Math.floor((float)n7 / (float)n9);
                            n7 = n8;
                        }
                    } else {
                        n6 = (int)Math.floor((float)n6 / (float)n9);
                        n8 = (int)Math.floor((float)n7 / (float)n9);
                        n7 = n6;
                        n6 = n8;
                    }
                    double d = downsampleStrategy.getScaleFactor(n7, n6, n4, n5);
                    if (Build.VERSION.SDK_INT >= 19) {
                        options.inTargetDensity = Downsampler.adjustTargetDensityForError(d);
                        options.inDensity = Downsampler.getDensityMultiplier(d);
                    }
                    if (Downsampler.isScaling(options)) {
                        options.inScaled = true;
                    } else {
                        options.inTargetDensity = 0;
                        options.inDensity = 0;
                    }
                    if (Log.isLoggable((String)"Downsampler", (int)2)) {
                        object = new StringBuilder();
                        object.append("Calculate scaling, source: [");
                        object.append(n2);
                        object.append("x");
                        object.append(n3);
                        object.append("], degreesToRotate: ");
                        object.append(n);
                        object.append(", target: [");
                        object.append(n4);
                        object.append("x");
                        object.append(n5);
                        object.append("], power of two scaled: [");
                        object.append(n7);
                        object.append("x");
                        object.append(n6);
                        object.append("], exact scale factor: ");
                        object.append(f);
                        object.append(", power of 2 sample size: ");
                        object.append(n9);
                        object.append(", adjusted scale factor: ");
                        object.append(d);
                        object.append(", target density: ");
                        object.append(options.inTargetDensity);
                        object.append(", density: ");
                        object.append(options.inDensity);
                        Log.v((String)"Downsampler", (String)object.toString());
                        return;
                    }
                    return;
                }
                throw new IllegalArgumentException("Cannot round with null rounding");
            }
            object = new StringBuilder();
            object.append("Cannot scale with factor: ");
            object.append(f);
            object.append(" from: ");
            object.append(downsampleStrategy);
            object.append(", source: [");
            object.append(n2);
            object.append("x");
            object.append(n3);
            object.append("], target: [");
            object.append(n4);
            object.append("x");
            object.append(n5);
            object.append("]");
            throw new IllegalArgumentException(object.toString());
        }
        if (Log.isLoggable((String)"Downsampler", (int)3)) {
            object2 = new StringBuilder();
            object2.append("Unable to determine dimensions for: ");
            object2.append(object);
            object2.append(" with target [");
            object2.append(n4);
            object2.append("x");
            object2.append(n5);
            object2.append("]");
            Log.d((String)"Downsampler", (String)object2.toString());
            return;
        }
    }

    private Resource<Bitmap> decode(ImageReader object, int n, int n2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        byte[] arrby = this.byteArrayPool.get(65536, byte[].class);
        BitmapFactory.Options options2 = Downsampler.getDefaultOptions();
        options2.inTempStorage = arrby;
        DecodeFormat decodeFormat = options.get(DECODE_FORMAT);
        PreferredColorSpace preferredColorSpace = options.get(PREFERRED_COLOR_SPACE);
        DownsampleStrategy downsampleStrategy = options.get(DownsampleStrategy.OPTION);
        boolean bl = options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
        boolean bl2 = options.get(ALLOW_HARDWARE_CONFIG) != null && options.get(ALLOW_HARDWARE_CONFIG) != false;
        try {
            object = BitmapResource.obtain(this.decodeFromWrappedStreams((ImageReader)object, options2, downsampleStrategy, decodeFormat, preferredColorSpace, bl2, n, n2, bl, decodeCallbacks), this.bitmapPool);
            return object;
        }
        finally {
            Downsampler.releaseOptions(options2);
            this.byteArrayPool.put(arrby);
        }
    }

    private Bitmap decodeFromWrappedStreams(ImageReader imageReader, BitmapFactory.Options options, DownsampleStrategy object, DecodeFormat object2, PreferredColorSpace preferredColorSpace, boolean bl, int n, int n2, boolean bl2, DecodeCallbacks decodeCallbacks) throws IOException {
        long l = LogTime.getLogTime();
        Object object3 = Downsampler.getDimensions(imageReader, options, decodeCallbacks, this.bitmapPool);
        int n3 = 0;
        int n4 = object3[0];
        int n5 = object3[1];
        object3 = options.outMimeType;
        if (n4 == -1 || n5 == -1) {
            bl = false;
        }
        int n6 = imageReader.getImageOrientation();
        int n7 = TransformationUtils.getExifOrientationDegrees(n6);
        boolean bl3 = TransformationUtils.isExifOrientationRequired(n6);
        int n8 = n == Integer.MIN_VALUE ? (Downsampler.isRotationRequired(n7) ? n5 : n4) : n;
        int n9 = n2 == Integer.MIN_VALUE ? (Downsampler.isRotationRequired(n7) ? n4 : n5) : n2;
        Object object4 = imageReader.getImageType();
        Downsampler.calculateScaling((ImageHeaderParser.ImageType)((Object)object4), imageReader, decodeCallbacks, this.bitmapPool, (DownsampleStrategy)object, n7, n4, n5, n8, n9, options);
        this.calculateConfig(imageReader, (DecodeFormat)((Object)object2), bl, bl3, options, n8, n9);
        n7 = Build.VERSION.SDK_INT >= 19 ? 1 : 0;
        if (options.inSampleSize == 1 || n7 != 0) {
            object = this;
            if (Downsampler.super.shouldUsePool((ImageHeaderParser.ImageType)((Object)object4))) {
                if (n4 >= 0 && n5 >= 0 && bl2 && n7 != 0) {
                    n7 = n9;
                } else {
                    float f = Downsampler.isScaling(options) ? (float)options.inTargetDensity / (float)options.inDensity : 1.0f;
                    n7 = options.inSampleSize;
                    n9 = (int)Math.ceil((float)n4 / (float)n7);
                    n8 = (int)Math.ceil((float)n5 / (float)n7);
                    n9 = Math.round((float)n9 * f);
                    n8 = Math.round((float)n8 * f);
                    object2 = "Downsampler";
                    if (Log.isLoggable((String)object2, (int)2)) {
                        object4 = new StringBuilder();
                        object4.append("Calculated target [");
                        object4.append(n9);
                        object4.append("x");
                        object4.append(n8);
                        object4.append("] for source [");
                        object4.append(n4);
                        object4.append("x");
                        object4.append(n5);
                        object4.append("], sampleSize: ");
                        object4.append(n7);
                        object4.append(", targetDensity: ");
                        object4.append(options.inTargetDensity);
                        object4.append(", density: ");
                        object4.append(options.inDensity);
                        object4.append(", density multiplier: ");
                        object4.append(f);
                        Log.v((String)object2, (String)object4.toString());
                    }
                    n7 = n8;
                    n8 = n9;
                }
                if (n8 > 0 && n7 > 0) {
                    Downsampler.setInBitmap(options, object.bitmapPool, n8, n7);
                }
            }
        }
        object2 = this;
        if (Build.VERSION.SDK_INT >= 28) {
            n8 = preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && options.outColorSpace != null && options.outColorSpace.isWideGamut() ? 1 : n3;
            object = n8 != 0 ? ColorSpace.Named.DISPLAY_P3 : ColorSpace.Named.SRGB;
            options.inPreferredColorSpace = ColorSpace.get((ColorSpace.Named)object);
        } else if (Build.VERSION.SDK_INT >= 26) {
            options.inPreferredColorSpace = ColorSpace.get((ColorSpace.Named)ColorSpace.Named.SRGB);
        }
        object = Downsampler.decodeStream(imageReader, options, decodeCallbacks, object2.bitmapPool);
        decodeCallbacks.onDecodeComplete(object2.bitmapPool, (Bitmap)object);
        if (Log.isLoggable((String)"Downsampler", (int)2)) {
            Downsampler.logDecode(n4, n5, (String)object3, options, (Bitmap)object, n, n2, l);
        }
        imageReader = null;
        if (object != null) {
            object.setDensity(object2.displayMetrics.densityDpi);
            options = TransformationUtils.rotateImageExif(object2.bitmapPool, (Bitmap)object, n6);
            imageReader = options;
            if (!object.equals((Object)options)) {
                object2.bitmapPool.put((Bitmap)object);
                imageReader = options;
            }
        }
        return imageReader;
    }

    /*
     * Exception decompiling
     */
    private static Bitmap decodeStream(ImageReader var0, BitmapFactory.Options var1_3, DecodeCallbacks var2_4, BitmapPool var3_5) throws IOException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:397)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:475)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private static String getBitmapString(Bitmap bitmap) {
        CharSequence charSequence2;
        CharSequence charSequence2;
        if (bitmap == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            charSequence2 = new StringBuilder();
            charSequence2.append(" (");
            charSequence2.append(bitmap.getAllocationByteCount());
            charSequence2.append(")");
            charSequence2 = charSequence2.toString();
        } else {
            charSequence2 = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(bitmap.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(bitmap.getHeight());
        stringBuilder.append("] ");
        stringBuilder.append((Object)bitmap.getConfig());
        stringBuilder.append((String)charSequence2);
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static BitmapFactory.Options getDefaultOptions() {
        synchronized (Downsampler.class) {
            BitmapFactory.Options options;
            BitmapFactory.Options options2 = OPTIONS_QUEUE;
            synchronized (options2) {
                options = OPTIONS_QUEUE.poll();
            }
            options2 = options;
            if (options == null) {
                options2 = new BitmapFactory.Options();
                Downsampler.resetOptions(options2);
            }
            return options2;
        }
    }

    private static int getDensityMultiplier(double d) {
        if (d > 1.0) {
            d = 1.0 / d;
        }
        return (int)Math.round(d * 2.147483647E9);
    }

    private static int[] getDimensions(ImageReader imageReader, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool) throws IOException {
        options.inJustDecodeBounds = true;
        Downsampler.decodeStream(imageReader, options, decodeCallbacks, bitmapPool);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static String getInBitmapString(BitmapFactory.Options options) {
        return Downsampler.getBitmapString(options.inBitmap);
    }

    private static boolean isRotationRequired(int n) {
        if (n != 90 && n != 270) {
            return false;
        }
        return true;
    }

    private static boolean isScaling(BitmapFactory.Options options) {
        if (options.inTargetDensity > 0 && options.inDensity > 0 && options.inTargetDensity != options.inDensity) {
            return true;
        }
        return false;
    }

    private static void logDecode(int n, int n2, String string2, BitmapFactory.Options options, Bitmap bitmap, int n3, int n4, long l) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Decoded ");
        stringBuilder.append(Downsampler.getBitmapString(bitmap));
        stringBuilder.append(" from [");
        stringBuilder.append(n);
        stringBuilder.append("x");
        stringBuilder.append(n2);
        stringBuilder.append("] ");
        stringBuilder.append(string2);
        stringBuilder.append(" with inBitmap ");
        stringBuilder.append(Downsampler.getInBitmapString(options));
        stringBuilder.append(" for [");
        stringBuilder.append(n3);
        stringBuilder.append("x");
        stringBuilder.append(n4);
        stringBuilder.append("], sample size: ");
        stringBuilder.append(options.inSampleSize);
        stringBuilder.append(", density: ");
        stringBuilder.append(options.inDensity);
        stringBuilder.append(", target density: ");
        stringBuilder.append(options.inTargetDensity);
        stringBuilder.append(", thread: ");
        stringBuilder.append(Thread.currentThread().getName());
        stringBuilder.append(", duration: ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        Log.v((String)"Downsampler", (String)stringBuilder.toString());
    }

    private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException illegalArgumentException, int n, int n2, String string2, BitmapFactory.Options options) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exception decoding bitmap, outWidth: ");
        stringBuilder.append(n);
        stringBuilder.append(", outHeight: ");
        stringBuilder.append(n2);
        stringBuilder.append(", outMimeType: ");
        stringBuilder.append(string2);
        stringBuilder.append(", inBitmap: ");
        stringBuilder.append(Downsampler.getInBitmapString(options));
        return new IOException(stringBuilder.toString(), illegalArgumentException);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void releaseOptions(BitmapFactory.Options options) {
        Downsampler.resetOptions(options);
        Queue<BitmapFactory.Options> queue = OPTIONS_QUEUE;
        synchronized (queue) {
            OPTIONS_QUEUE.offer(options);
            return;
        }
    }

    private static void resetOptions(BitmapFactory.Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.inDensity = 0;
        options.inTargetDensity = 0;
        if (Build.VERSION.SDK_INT >= 26) {
            options.inPreferredColorSpace = null;
            options.outColorSpace = null;
            options.outConfig = null;
        }
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;
        options.inBitmap = null;
        options.inMutable = true;
    }

    private static int round(double d) {
        return (int)(0.5 + d);
    }

    private static void setInBitmap(BitmapFactory.Options options, BitmapPool bitmapPool, int n, int n2) {
        Bitmap.Config config = null;
        if (Build.VERSION.SDK_INT >= 26) {
            if (options.inPreferredConfig == Bitmap.Config.HARDWARE) {
                return;
            }
            config = options.outConfig;
        }
        Bitmap.Config config2 = config;
        if (config == null) {
            config2 = options.inPreferredConfig;
        }
        options.inBitmap = bitmapPool.getDirty(n, n2, config2);
    }

    private boolean shouldUsePool(ImageHeaderParser.ImageType imageType) {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return TYPES_THAT_USE_POOL_PRE_KITKAT.contains((Object)imageType);
    }

    public Resource<Bitmap> decode(ParcelFileDescriptor parcelFileDescriptor, int n, int n2, Options options) throws IOException {
        return this.decode(new ImageReader.ParcelFileDescriptorImageReader(parcelFileDescriptor, this.parsers, this.byteArrayPool), n, n2, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options options) throws IOException {
        return this.decode(inputStream, n, n2, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        return this.decode(new ImageReader.InputStreamImageReader(inputStream, this.parsers, this.byteArrayPool), n, n2, options, decodeCallbacks);
    }

    public boolean handles(ParcelFileDescriptor parcelFileDescriptor) {
        return ParcelFileDescriptorRewinder.isSupported();
    }

    public boolean handles(InputStream inputStream) {
        return true;
    }

    public boolean handles(ByteBuffer byteBuffer) {
        return true;
    }

    public static interface DecodeCallbacks {
        public void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException;

        public void onObtainBounds();
    }

}

