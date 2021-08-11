package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.ColorSpace.Named;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
   public static final Option ALLOW_HARDWARE_CONFIG;
   public static final Option DECODE_FORMAT;
   @Deprecated
   public static final Option DOWNSAMPLE_STRATEGY;
   private static final Downsampler.DecodeCallbacks EMPTY_CALLBACKS;
   public static final Option FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
   private static final String ICO_MIME_TYPE = "image/x-ico";
   private static final Set NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
   private static final Queue OPTIONS_QUEUE;
   public static final Option PREFERRED_COLOR_SPACE;
   static final String TAG = "Downsampler";
   private static final Set TYPES_THAT_USE_POOL_PRE_KITKAT;
   private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
   private final BitmapPool bitmapPool;
   private final ArrayPool byteArrayPool;
   private final DisplayMetrics displayMetrics;
   private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
   private final List parsers;

   static {
      DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
      PREFERRED_COLOR_SPACE = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.PreferredColorSpace", PreferredColorSpace.SRGB);
      DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
      Boolean var0 = false;
      FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", var0);
      ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", var0);
      NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList("image/vnd.wap.wbmp", "image/x-ico")));
      EMPTY_CALLBACKS = new Downsampler.DecodeCallbacks() {
         public void onDecodeComplete(BitmapPool var1, Bitmap var2) {
         }

         public void onObtainBounds() {
         }
      };
      TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
      OPTIONS_QUEUE = Util.createQueue(0);
   }

   public Downsampler(List var1, DisplayMetrics var2, BitmapPool var3, ArrayPool var4) {
      this.parsers = var1;
      this.displayMetrics = (DisplayMetrics)Preconditions.checkNotNull(var2);
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var3);
      this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(var4);
   }

   private static int adjustTargetDensityForError(double var0) {
      int var2 = getDensityMultiplier(var0);
      int var3 = round((double)var2 * var0);
      var0 /= (double)((float)var3 / (float)var2);
      return round((double)var3 * var0);
   }

   private void calculateConfig(ImageReader var1, DecodeFormat var2, boolean var3, boolean var4, Options var5, int var6, int var7) {
      if (!this.hardwareConfigState.setHardwareConfigIfAllowed(var6, var7, var5, var3, var4)) {
         if (var2 != DecodeFormat.PREFER_ARGB_8888 && VERSION.SDK_INT != 16) {
            var4 = false;

            try {
               var3 = var1.getImageType().hasAlpha();
            } catch (IOException var9) {
               var3 = var4;
               if (Log.isLoggable("Downsampler", 3)) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("Cannot determine whether the image has alpha or not from header, format ");
                  var8.append(var2);
                  Log.d("Downsampler", var8.toString(), var9);
                  var3 = var4;
               }
            }

            Config var10;
            if (var3) {
               var10 = Config.ARGB_8888;
            } else {
               var10 = Config.RGB_565;
            }

            var5.inPreferredConfig = var10;
            if (var5.inPreferredConfig == Config.RGB_565) {
               var5.inDither = true;
            }

         } else {
            var5.inPreferredConfig = Config.ARGB_8888;
         }
      }
   }

   private static void calculateScaling(ImageHeaderParser.ImageType var0, ImageReader var1, Downsampler.DecodeCallbacks var2, BitmapPool var3, DownsampleStrategy var4, int var5, int var6, int var7, int var8, int var9, Options var10) throws IOException {
      if (var6 > 0 && var7 > 0) {
         int var15 = var6;
         int var14 = var7;
         if (isRotationRequired(var5)) {
            var15 = var7;
            var14 = var6;
         }

         float var13 = var4.getScaleFactor(var15, var14, var8, var9);
         StringBuilder var22;
         if (var13 <= 0.0F) {
            var22 = new StringBuilder();
            var22.append("Cannot scale with factor: ");
            var22.append(var13);
            var22.append(" from: ");
            var22.append(var4);
            var22.append(", source: [");
            var22.append(var6);
            var22.append("x");
            var22.append(var7);
            var22.append("], target: [");
            var22.append(var8);
            var22.append("x");
            var22.append(var9);
            var22.append("]");
            throw new IllegalArgumentException(var22.toString());
         } else {
            DownsampleStrategy.SampleSizeRounding var20 = var4.getSampleSizeRounding(var15, var14, var8, var9);
            if (var20 != null) {
               int var17 = round((double)((float)var15 * var13));
               int var16 = round((double)((float)var14 * var13));
               var17 = var15 / var17;
               var16 = var14 / var16;
               if (var20 == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                  var16 = Math.max(var17, var16);
               } else {
                  var16 = Math.min(var17, var16);
               }

               if (VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(var10.outMimeType)) {
                  var16 = 1;
               } else {
                  var17 = Math.max(1, Integer.highestOneBit(var16));
                  var16 = var17;
                  if (var20 == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                     var16 = var17;
                     if ((float)var17 < 1.0F / var13) {
                        var16 = var17 << 1;
                     }
                  }
               }

               var10.inSampleSize = var16;
               if (var0 == ImageHeaderParser.ImageType.JPEG) {
                  int var18 = Math.min(var16, 8);
                  var17 = (int)Math.ceil((double)((float)var15 / (float)var18));
                  var18 = (int)Math.ceil((double)((float)var14 / (float)var18));
                  int var19 = var16 / 8;
                  var14 = var17;
                  var15 = var18;
                  if (var19 > 0) {
                     var14 = var17 / var19;
                     var15 = var18 / var19;
                  }
               } else if (var0 != ImageHeaderParser.ImageType.PNG && var0 != ImageHeaderParser.ImageType.PNG_A) {
                  if (var0 != ImageHeaderParser.ImageType.WEBP && var0 != ImageHeaderParser.ImageType.WEBP_A) {
                     if (var15 % var16 == 0 && var14 % var16 == 0) {
                        var15 /= var16;
                        var17 = var14 / var16;
                        var14 = var15;
                        var15 = var17;
                     } else {
                        int[] var21 = getDimensions(var1, var10, var2, var3);
                        var14 = var21[0];
                        var15 = var21[1];
                     }
                  } else if (VERSION.SDK_INT >= 24) {
                     var17 = Math.round((float)var15 / (float)var16);
                     var15 = Math.round((float)var14 / (float)var16);
                     var14 = var17;
                  } else {
                     var17 = (int)Math.floor((double)((float)var15 / (float)var16));
                     var15 = (int)Math.floor((double)((float)var14 / (float)var16));
                     var14 = var17;
                  }
               } else {
                  var15 = (int)Math.floor((double)((float)var15 / (float)var16));
                  var17 = (int)Math.floor((double)((float)var14 / (float)var16));
                  var14 = var15;
                  var15 = var17;
               }

               double var11 = (double)var4.getScaleFactor(var14, var15, var8, var9);
               if (VERSION.SDK_INT >= 19) {
                  var10.inTargetDensity = adjustTargetDensityForError(var11);
                  var10.inDensity = getDensityMultiplier(var11);
               }

               if (isScaling(var10)) {
                  var10.inScaled = true;
               } else {
                  var10.inTargetDensity = 0;
                  var10.inDensity = 0;
               }

               if (Log.isLoggable("Downsampler", 2)) {
                  var22 = new StringBuilder();
                  var22.append("Calculate scaling, source: [");
                  var22.append(var6);
                  var22.append("x");
                  var22.append(var7);
                  var22.append("], degreesToRotate: ");
                  var22.append(var5);
                  var22.append(", target: [");
                  var22.append(var8);
                  var22.append("x");
                  var22.append(var9);
                  var22.append("], power of two scaled: [");
                  var22.append(var14);
                  var22.append("x");
                  var22.append(var15);
                  var22.append("], exact scale factor: ");
                  var22.append(var13);
                  var22.append(", power of 2 sample size: ");
                  var22.append(var16);
                  var22.append(", adjusted scale factor: ");
                  var22.append(var11);
                  var22.append(", target density: ");
                  var22.append(var10.inTargetDensity);
                  var22.append(", density: ");
                  var22.append(var10.inDensity);
                  Log.v("Downsampler", var22.toString());
               }
            } else {
               throw new IllegalArgumentException("Cannot round with null rounding");
            }
         }
      } else if (Log.isLoggable("Downsampler", 3)) {
         StringBuilder var23 = new StringBuilder();
         var23.append("Unable to determine dimensions for: ");
         var23.append(var0);
         var23.append(" with target [");
         var23.append(var8);
         var23.append("x");
         var23.append(var9);
         var23.append("]");
         Log.d("Downsampler", var23.toString());
      }
   }

   private Resource decode(ImageReader var1, int var2, int var3, com.bumptech.glide.load.Options var4, Downsampler.DecodeCallbacks var5) throws IOException {
      byte[] var8 = (byte[])this.byteArrayPool.get(65536, byte[].class);
      Options var9 = getDefaultOptions();
      var9.inTempStorage = var8;
      DecodeFormat var10 = (DecodeFormat)var4.get(DECODE_FORMAT);
      PreferredColorSpace var11 = (PreferredColorSpace)var4.get(PREFERRED_COLOR_SPACE);
      DownsampleStrategy var12 = (DownsampleStrategy)var4.get(DownsampleStrategy.OPTION);
      boolean var7 = (Boolean)var4.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
      boolean var6;
      if (var4.get(ALLOW_HARDWARE_CONFIG) != null && (Boolean)var4.get(ALLOW_HARDWARE_CONFIG)) {
         var6 = true;
      } else {
         var6 = false;
      }

      BitmapResource var15;
      try {
         var15 = BitmapResource.obtain(this.decodeFromWrappedStreams(var1, var9, var12, var10, var11, var6, var2, var3, var7, var5), this.bitmapPool);
      } finally {
         releaseOptions(var9);
         this.byteArrayPool.put(var8);
      }

      return var15;
   }

   private Bitmap decodeFromWrappedStreams(ImageReader var1, Options var2, DownsampleStrategy var3, DecodeFormat var4, PreferredColorSpace var5, boolean var6, int var7, int var8, boolean var9, Downsampler.DecodeCallbacks var10) throws IOException {
      long var20 = LogTime.getLogTime();
      int[] var22 = getDimensions(var1, var2, var10, this.bitmapPool);
      boolean var17 = false;
      int var14 = var22[0];
      int var15 = var22[1];
      String var31 = var2.outMimeType;
      if (var14 == -1 || var15 == -1) {
         var6 = false;
      }

      int var18 = var1.getImageOrientation();
      int var16 = TransformationUtils.getExifOrientationDegrees(var18);
      boolean var19 = TransformationUtils.isExifOrientationRequired(var18);
      int var12;
      if (var7 == Integer.MIN_VALUE) {
         if (isRotationRequired(var16)) {
            var12 = var15;
         } else {
            var12 = var14;
         }
      } else {
         var12 = var7;
      }

      int var13;
      if (var8 == Integer.MIN_VALUE) {
         if (isRotationRequired(var16)) {
            var13 = var14;
         } else {
            var13 = var15;
         }
      } else {
         var13 = var8;
      }

      ImageHeaderParser.ImageType var23 = var1.getImageType();
      calculateScaling(var23, var1, var10, this.bitmapPool, var3, var16, var14, var15, var12, var13, var2);
      this.calculateConfig(var1, var4, var6, var19, var2, var12, var13);
      boolean var30;
      if (VERSION.SDK_INT >= 19) {
         var30 = true;
      } else {
         var30 = false;
      }

      if ((var2.inSampleSize == 1 || var30) && this.shouldUsePool(var23)) {
         if (var14 >= 0 && var15 >= 0 && var9 && var30) {
            var16 = var13;
         } else {
            float var11;
            if (isScaling(var2)) {
               var11 = (float)var2.inTargetDensity / (float)var2.inDensity;
            } else {
               var11 = 1.0F;
            }

            var16 = var2.inSampleSize;
            var13 = (int)Math.ceil((double)((float)var14 / (float)var16));
            var12 = (int)Math.ceil((double)((float)var15 / (float)var16));
            var13 = Math.round((float)var13 * var11);
            var12 = Math.round((float)var12 * var11);
            String var28 = "Downsampler";
            if (Log.isLoggable(var28, 2)) {
               StringBuilder var32 = new StringBuilder();
               var32.append("Calculated target [");
               var32.append(var13);
               var32.append("x");
               var32.append(var12);
               var32.append("] for source [");
               var32.append(var14);
               var32.append("x");
               var32.append(var15);
               var32.append("], sampleSize: ");
               var32.append(var16);
               var32.append(", targetDensity: ");
               var32.append(var2.inTargetDensity);
               var32.append(", density: ");
               var32.append(var2.inDensity);
               var32.append(", density multiplier: ");
               var32.append(var11);
               Log.v(var28, var32.toString());
            }

            var16 = var12;
            var12 = var13;
         }

         if (var12 > 0 && var16 > 0) {
            setInBitmap(var2, this.bitmapPool, var12, var16);
         }
      }

      if (VERSION.SDK_INT >= 28) {
         boolean var29;
         if (var5 == PreferredColorSpace.DISPLAY_P3 && var2.outColorSpace != null && var2.outColorSpace.isWideGamut()) {
            var29 = true;
         } else {
            var29 = var17;
         }

         Named var26;
         if (var29) {
            var26 = Named.DISPLAY_P3;
         } else {
            var26 = Named.SRGB;
         }

         var2.inPreferredColorSpace = ColorSpace.get(var26);
      } else if (VERSION.SDK_INT >= 26) {
         var2.inPreferredColorSpace = ColorSpace.get(Named.SRGB);
      }

      Bitmap var27 = decodeStream(var1, var2, var10, this.bitmapPool);
      var10.onDecodeComplete(this.bitmapPool, var27);
      if (Log.isLoggable("Downsampler", 2)) {
         logDecode(var14, var15, var31, var2, var27, var7, var8, var20);
      }

      Bitmap var24 = null;
      if (var27 != null) {
         var27.setDensity(this.displayMetrics.densityDpi);
         Bitmap var25 = TransformationUtils.rotateImageExif(this.bitmapPool, var27, var18);
         var24 = var25;
         if (!var27.equals(var25)) {
            this.bitmapPool.put(var27);
            var24 = var25;
         }
      }

      return var24;
   }

   private static Bitmap decodeStream(ImageReader param0, Options param1, Downsampler.DecodeCallbacks param2, BitmapPool param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private static String getBitmapString(Bitmap var0) {
      if (var0 == null) {
         return null;
      } else {
         String var3;
         if (VERSION.SDK_INT >= 19) {
            StringBuilder var1 = new StringBuilder();
            var1.append(" (");
            var1.append(var0.getAllocationByteCount());
            var1.append(")");
            var3 = var1.toString();
         } else {
            var3 = "";
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("[");
         var2.append(var0.getWidth());
         var2.append("x");
         var2.append(var0.getHeight());
         var2.append("] ");
         var2.append(var0.getConfig());
         var2.append(var3);
         return var2.toString();
      }
   }

   private static Options getDefaultOptions() {
      // $FF: Couldn't be decompiled
   }

   private static int getDensityMultiplier(double var0) {
      if (var0 > 1.0D) {
         var0 = 1.0D / var0;
      }

      return (int)Math.round(var0 * 2.147483647E9D);
   }

   private static int[] getDimensions(ImageReader var0, Options var1, Downsampler.DecodeCallbacks var2, BitmapPool var3) throws IOException {
      var1.inJustDecodeBounds = true;
      decodeStream(var0, var1, var2, var3);
      var1.inJustDecodeBounds = false;
      return new int[]{var1.outWidth, var1.outHeight};
   }

   private static String getInBitmapString(Options var0) {
      return getBitmapString(var0.inBitmap);
   }

   private static boolean isRotationRequired(int var0) {
      return var0 == 90 || var0 == 270;
   }

   private static boolean isScaling(Options var0) {
      return var0.inTargetDensity > 0 && var0.inDensity > 0 && var0.inTargetDensity != var0.inDensity;
   }

   private static void logDecode(int var0, int var1, String var2, Options var3, Bitmap var4, int var5, int var6, long var7) {
      StringBuilder var9 = new StringBuilder();
      var9.append("Decoded ");
      var9.append(getBitmapString(var4));
      var9.append(" from [");
      var9.append(var0);
      var9.append("x");
      var9.append(var1);
      var9.append("] ");
      var9.append(var2);
      var9.append(" with inBitmap ");
      var9.append(getInBitmapString(var3));
      var9.append(" for [");
      var9.append(var5);
      var9.append("x");
      var9.append(var6);
      var9.append("], sample size: ");
      var9.append(var3.inSampleSize);
      var9.append(", density: ");
      var9.append(var3.inDensity);
      var9.append(", target density: ");
      var9.append(var3.inTargetDensity);
      var9.append(", thread: ");
      var9.append(Thread.currentThread().getName());
      var9.append(", duration: ");
      var9.append(LogTime.getElapsedMillis(var7));
      Log.v("Downsampler", var9.toString());
   }

   private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException var0, int var1, int var2, String var3, Options var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("Exception decoding bitmap, outWidth: ");
      var5.append(var1);
      var5.append(", outHeight: ");
      var5.append(var2);
      var5.append(", outMimeType: ");
      var5.append(var3);
      var5.append(", inBitmap: ");
      var5.append(getInBitmapString(var4));
      return new IOException(var5.toString(), var0);
   }

   private static void releaseOptions(Options param0) {
      // $FF: Couldn't be decompiled
   }

   private static void resetOptions(Options var0) {
      var0.inTempStorage = null;
      var0.inDither = false;
      var0.inScaled = false;
      var0.inSampleSize = 1;
      var0.inPreferredConfig = null;
      var0.inJustDecodeBounds = false;
      var0.inDensity = 0;
      var0.inTargetDensity = 0;
      if (VERSION.SDK_INT >= 26) {
         var0.inPreferredColorSpace = null;
         var0.outColorSpace = null;
         var0.outConfig = null;
      }

      var0.outWidth = 0;
      var0.outHeight = 0;
      var0.outMimeType = null;
      var0.inBitmap = null;
      var0.inMutable = true;
   }

   private static int round(double var0) {
      return (int)(0.5D + var0);
   }

   private static void setInBitmap(Options var0, BitmapPool var1, int var2, int var3) {
      Config var4 = null;
      if (VERSION.SDK_INT >= 26) {
         if (var0.inPreferredConfig == Config.HARDWARE) {
            return;
         }

         var4 = var0.outConfig;
      }

      Config var5 = var4;
      if (var4 == null) {
         var5 = var0.inPreferredConfig;
      }

      var0.inBitmap = var1.getDirty(var2, var3, var5);
   }

   private boolean shouldUsePool(ImageHeaderParser.ImageType var1) {
      return VERSION.SDK_INT >= 19 ? true : TYPES_THAT_USE_POOL_PRE_KITKAT.contains(var1);
   }

   public Resource decode(ParcelFileDescriptor var1, int var2, int var3, com.bumptech.glide.load.Options var4) throws IOException {
      return this.decode((ImageReader)(new ImageReader.ParcelFileDescriptorImageReader(var1, this.parsers, this.byteArrayPool)), var2, var3, var4, EMPTY_CALLBACKS);
   }

   public Resource decode(InputStream var1, int var2, int var3, com.bumptech.glide.load.Options var4) throws IOException {
      return this.decode(var1, var2, var3, var4, EMPTY_CALLBACKS);
   }

   public Resource decode(InputStream var1, int var2, int var3, com.bumptech.glide.load.Options var4, Downsampler.DecodeCallbacks var5) throws IOException {
      return this.decode((ImageReader)(new ImageReader.InputStreamImageReader(var1, this.parsers, this.byteArrayPool)), var2, var3, var4, var5);
   }

   public boolean handles(ParcelFileDescriptor var1) {
      return ParcelFileDescriptorRewinder.isSupported();
   }

   public boolean handles(InputStream var1) {
      return true;
   }

   public boolean handles(ByteBuffer var1) {
      return true;
   }

   public interface DecodeCallbacks {
      void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException;

      void onObtainBounds();
   }
}
