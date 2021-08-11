package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TransformationUtils {
   private static final Lock BITMAP_DRAWABLE_LOCK;
   private static final Paint CIRCLE_CROP_BITMAP_PAINT;
   private static final int CIRCLE_CROP_PAINT_FLAGS = 7;
   private static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
   private static final Paint DEFAULT_PAINT = new Paint(6);
   private static final Set MODELS_REQUIRING_BITMAP_LOCK;
   public static final int PAINT_FLAGS = 6;
   private static final String TAG = "TransformationUtils";

   static {
      HashSet var0 = new HashSet(Arrays.asList("XT1085", "XT1092", "XT1093", "XT1094", "XT1095", "XT1096", "XT1097", "XT1098", "XT1031", "XT1028", "XT937C", "XT1032", "XT1008", "XT1033", "XT1035", "XT1034", "XT939G", "XT1039", "XT1040", "XT1042", "XT1045", "XT1063", "XT1064", "XT1068", "XT1069", "XT1072", "XT1077", "XT1078", "XT1079"));
      MODELS_REQUIRING_BITMAP_LOCK = var0;
      Object var1;
      if (var0.contains(Build.MODEL)) {
         var1 = new ReentrantLock();
      } else {
         var1 = new TransformationUtils.NoLock();
      }

      BITMAP_DRAWABLE_LOCK = (Lock)var1;
      Paint var2 = new Paint(7);
      CIRCLE_CROP_BITMAP_PAINT = var2;
      var2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
   }

   private TransformationUtils() {
   }

   private static void applyMatrix(Bitmap var0, Bitmap var1, Matrix var2) {
      BITMAP_DRAWABLE_LOCK.lock();

      try {
         Canvas var5 = new Canvas(var1);
         var5.drawBitmap(var0, var2, DEFAULT_PAINT);
         clear(var5);
      } finally {
         BITMAP_DRAWABLE_LOCK.unlock();
      }

   }

   public static Bitmap centerCrop(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() == var2 && var1.getHeight() == var3) {
         return var1;
      } else {
         Matrix var7 = new Matrix();
         float var4;
         float var5;
         float var6;
         if (var1.getWidth() * var3 > var1.getHeight() * var2) {
            var4 = (float)var3 / (float)var1.getHeight();
            var5 = ((float)var2 - (float)var1.getWidth() * var4) * 0.5F;
            var6 = 0.0F;
         } else {
            var4 = (float)var2 / (float)var1.getWidth();
            var5 = 0.0F;
            var6 = ((float)var3 - (float)var1.getHeight() * var4) * 0.5F;
         }

         var7.setScale(var4, var4);
         var7.postTranslate((float)((int)(var5 + 0.5F)), (float)((int)(0.5F + var6)));
         Bitmap var8 = var0.get(var2, var3, getNonNullConfig(var1));
         setAlpha(var1, var8);
         applyMatrix(var1, var8, var7);
         return var8;
      }
   }

   public static Bitmap centerInside(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() <= var2 && var1.getHeight() <= var3) {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size larger or equal to input, returning input");
         }

         return var1;
      } else {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size too big for input, fit centering instead");
         }

         return fitCenter(var0, var1, var2, var3);
      }
   }

   public static Bitmap circleCrop(BitmapPool param0, Bitmap param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private static void clear(Canvas var0) {
      var0.setBitmap((Bitmap)null);
   }

   public static Bitmap fitCenter(BitmapPool var0, Bitmap var1, int var2, int var3) {
      if (var1.getWidth() == var2 && var1.getHeight() == var3) {
         if (Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size matches input, returning input");
         }

         return var1;
      } else {
         float var4 = Math.min((float)var2 / (float)var1.getWidth(), (float)var3 / (float)var1.getHeight());
         int var5 = Math.round((float)var1.getWidth() * var4);
         int var6 = Math.round((float)var1.getHeight() * var4);
         if (var1.getWidth() == var5 && var1.getHeight() == var6) {
            if (Log.isLoggable("TransformationUtils", 2)) {
               Log.v("TransformationUtils", "adjusted target size matches input, returning input");
            }

            return var1;
         } else {
            Bitmap var8 = var0.get((int)((float)var1.getWidth() * var4), (int)((float)var1.getHeight() * var4), getNonNullConfig(var1));
            setAlpha(var1, var8);
            if (Log.isLoggable("TransformationUtils", 2)) {
               StringBuilder var7 = new StringBuilder();
               var7.append("request: ");
               var7.append(var2);
               var7.append("x");
               var7.append(var3);
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("toFit:   ");
               var7.append(var1.getWidth());
               var7.append("x");
               var7.append(var1.getHeight());
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("toReuse: ");
               var7.append(var8.getWidth());
               var7.append("x");
               var7.append(var8.getHeight());
               Log.v("TransformationUtils", var7.toString());
               var7 = new StringBuilder();
               var7.append("minPct:   ");
               var7.append(var4);
               Log.v("TransformationUtils", var7.toString());
            }

            Matrix var9 = new Matrix();
            var9.setScale(var4, var4);
            applyMatrix(var1, var8, var9);
            return var8;
         }
      }
   }

   private static Bitmap getAlphaSafeBitmap(BitmapPool var0, Bitmap var1) {
      Config var2 = getAlphaSafeConfig(var1);
      if (var2.equals(var1.getConfig())) {
         return var1;
      } else {
         Bitmap var3 = var0.get(var1.getWidth(), var1.getHeight(), var2);
         (new Canvas(var3)).drawBitmap(var1, 0.0F, 0.0F, (Paint)null);
         return var3;
      }
   }

   private static Config getAlphaSafeConfig(Bitmap var0) {
      return VERSION.SDK_INT >= 26 && Config.RGBA_F16.equals(var0.getConfig()) ? Config.RGBA_F16 : Config.ARGB_8888;
   }

   public static Lock getBitmapDrawableLock() {
      return BITMAP_DRAWABLE_LOCK;
   }

   public static int getExifOrientationDegrees(int var0) {
      switch(var0) {
      case 3:
      case 4:
         return 180;
      case 5:
      case 6:
         return 90;
      case 7:
      case 8:
         return 270;
      default:
         return 0;
      }
   }

   private static Config getNonNullConfig(Bitmap var0) {
      return var0.getConfig() != null ? var0.getConfig() : Config.ARGB_8888;
   }

   static void initializeMatrixForRotation(int var0, Matrix var1) {
      switch(var0) {
      case 2:
         var1.setScale(-1.0F, 1.0F);
         return;
      case 3:
         var1.setRotate(180.0F);
         return;
      case 4:
         var1.setRotate(180.0F);
         var1.postScale(-1.0F, 1.0F);
         return;
      case 5:
         var1.setRotate(90.0F);
         var1.postScale(-1.0F, 1.0F);
         return;
      case 6:
         var1.setRotate(90.0F);
         return;
      case 7:
         var1.setRotate(-90.0F);
         var1.postScale(-1.0F, 1.0F);
         return;
      case 8:
         var1.setRotate(-90.0F);
         return;
      default:
      }
   }

   public static boolean isExifOrientationRequired(int var0) {
      switch(var0) {
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         return true;
      default:
         return false;
      }
   }

   public static Bitmap rotateImage(Bitmap var0, int var1) {
      Bitmap var3 = var0;
      if (var1 != 0) {
         try {
            Matrix var5 = new Matrix();
            var5.setRotate((float)var1);
            var3 = Bitmap.createBitmap(var0, 0, 0, var0.getWidth(), var0.getHeight(), var5, true);
         } catch (Exception var4) {
            var3 = var0;
            if (Log.isLoggable("TransformationUtils", 6)) {
               Log.e("TransformationUtils", "Exception when trying to orient image", var4);
               return var0;
            }
         }
      }

      return var3;
   }

   public static Bitmap rotateImageExif(BitmapPool var0, Bitmap var1, int var2) {
      if (!isExifOrientationRequired(var2)) {
         return var1;
      } else {
         Matrix var3 = new Matrix();
         initializeMatrixForRotation(var2, var3);
         RectF var4 = new RectF(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
         var3.mapRect(var4);
         Bitmap var5 = var0.get(Math.round(var4.width()), Math.round(var4.height()), getNonNullConfig(var1));
         var3.postTranslate(-var4.left, -var4.top);
         var5.setHasAlpha(var1.hasAlpha());
         applyMatrix(var1, var5, var3);
         return var5;
      }
   }

   public static Bitmap roundedCorners(BitmapPool var0, Bitmap var1, final float var2, final float var3, final float var4, final float var5) {
      return roundedCorners(var0, var1, new TransformationUtils.DrawRoundedCornerFn() {
         public void drawRoundedCorners(Canvas var1, Paint var2x, RectF var3x) {
            Path var8 = new Path();
            float var4x = var2;
            float var5x = var3;
            float var6 = var4;
            float var7 = var5;
            Direction var9 = Direction.CW;
            var8.addRoundRect(var3x, new float[]{var4x, var4x, var5x, var5x, var6, var6, var7, var7}, var9);
            var1.drawPath(var8, var2x);
         }
      });
   }

   public static Bitmap roundedCorners(BitmapPool var0, Bitmap var1, final int var2) {
      boolean var3;
      if (var2 > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Preconditions.checkArgument(var3, "roundingRadius must be greater than 0.");
      return roundedCorners(var0, var1, new TransformationUtils.DrawRoundedCornerFn() {
         public void drawRoundedCorners(Canvas var1, Paint var2x, RectF var3) {
            int var4 = var2;
            var1.drawRoundRect(var3, (float)var4, (float)var4, var2x);
         }
      });
   }

   @Deprecated
   public static Bitmap roundedCorners(BitmapPool var0, Bitmap var1, int var2, int var3, int var4) {
      return roundedCorners(var0, var1, var4);
   }

   private static Bitmap roundedCorners(BitmapPool var0, Bitmap var1, TransformationUtils.DrawRoundedCornerFn var2) {
      Config var4 = getAlphaSafeConfig(var1);
      Bitmap var3 = getAlphaSafeBitmap(var0, var1);
      Bitmap var10 = var0.get(var3.getWidth(), var3.getHeight(), var4);
      var10.setHasAlpha(true);
      BitmapShader var6 = new BitmapShader(var3, TileMode.CLAMP, TileMode.CLAMP);
      Paint var5 = new Paint();
      var5.setAntiAlias(true);
      var5.setShader(var6);
      RectF var11 = new RectF(0.0F, 0.0F, (float)var10.getWidth(), (float)var10.getHeight());
      BITMAP_DRAWABLE_LOCK.lock();

      try {
         Canvas var7 = new Canvas(var10);
         var7.drawColor(0, Mode.CLEAR);
         var2.drawRoundedCorners(var7, var5, var11);
         clear(var7);
      } finally {
         BITMAP_DRAWABLE_LOCK.unlock();
      }

      if (!var3.equals(var1)) {
         var0.put(var3);
      }

      return var10;
   }

   public static void setAlpha(Bitmap var0, Bitmap var1) {
      var1.setHasAlpha(var0.hasAlpha());
   }

   private interface DrawRoundedCornerFn {
      void drawRoundedCorners(Canvas var1, Paint var2, RectF var3);
   }

   private static final class NoLock implements Lock {
      NoLock() {
      }

      public void lock() {
      }

      public void lockInterruptibly() throws InterruptedException {
      }

      public Condition newCondition() {
         throw new UnsupportedOperationException("Should not be called");
      }

      public boolean tryLock() {
         return true;
      }

      public boolean tryLock(long var1, TimeUnit var3) throws InterruptedException {
         return true;
      }

      public void unlock() {
      }
   }
}
