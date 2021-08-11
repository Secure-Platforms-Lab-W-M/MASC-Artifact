package com.yalantis.ucrop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.task.BitmapLoadTask;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadUtils {
   private static final String TAG = "BitmapLoadUtils";

   public static int calculateInSampleSize(Options var0, int var1, int var2) {
      int var6 = var0.outHeight;
      int var7 = var0.outWidth;
      int var4 = 1;
      byte var5 = 1;
      int var3 = var5;
      if (var6 <= var2) {
         if (var7 <= var1) {
            return var4;
         }

         var3 = var5;
      }

      while(true) {
         if (var6 / var3 <= var2) {
            var4 = var3;
            if (var7 / var3 <= var1) {
               return var4;
            }
         }

         var3 *= 2;
      }
   }

   public static int calculateMaxBitmapSize(Context var0) {
      WindowManager var5 = (WindowManager)var0.getSystemService("window");
      Point var4 = new Point();
      if (var5 != null) {
         var5.getDefaultDisplay().getSize(var4);
      }

      int var1 = var4.x;
      int var2 = var4.y;
      var2 = (int)Math.sqrt(Math.pow((double)var1, 2.0D) + Math.pow((double)var2, 2.0D));
      Canvas var6 = new Canvas();
      int var3 = Math.min(var6.getMaximumBitmapWidth(), var6.getMaximumBitmapHeight());
      var1 = var2;
      if (var3 > 0) {
         var1 = Math.min(var2, var3);
      }

      var3 = EglUtils.getMaxTextureSize();
      var2 = var1;
      if (var3 > 0) {
         var2 = Math.min(var1, var3);
      }

      StringBuilder var7 = new StringBuilder();
      var7.append("maxBitmapSize: ");
      var7.append(var2);
      Log.d("BitmapLoadUtils", var7.toString());
      return var2;
   }

   public static void close(Closeable var0) {
      if (var0 != null && var0 instanceof Closeable) {
         try {
            var0.close();
            return;
         } catch (IOException var1) {
         }
      }

   }

   public static void decodeBitmapInBackground(Context var0, Uri var1, Uri var2, int var3, int var4, BitmapLoadCallback var5) {
      (new BitmapLoadTask(var0, var1, var2, var3, var4, var5)).execute(new Void[0]);
   }

   public static int exifToDegrees(int var0) {
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

   public static int exifToTranslation(int var0) {
      return var0 != 2 && var0 != 7 && var0 != 4 && var0 != 5 ? 1 : -1;
   }

   public static int getExifOrientation(Context var0, Uri var1) {
      byte var3 = 0;
      int var2 = var3;

      IOException var10000;
      label37: {
         InputStream var8;
         boolean var10001;
         try {
            var8 = var0.getContentResolver().openInputStream(var1);
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label37;
         }

         if (var8 == null) {
            return 0;
         }

         var2 = var3;

         int var10;
         try {
            var10 = (new ImageHeaderParser(var8)).getOrientation();
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label37;
         }

         var2 = var10;

         try {
            close(var8);
            return var10;
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IOException var9 = var10000;
      StringBuilder var4 = new StringBuilder();
      var4.append("getExifOrientation: ");
      var4.append(var1.toString());
      Log.e("BitmapLoadUtils", var4.toString(), var9);
      return var2;
   }

   public static Bitmap transformBitmap(Bitmap var0, Matrix var1) {
      boolean var2;
      Bitmap var4;
      try {
         var4 = Bitmap.createBitmap(var0, 0, 0, var0.getWidth(), var0.getHeight(), var1, true);
         var2 = var0.sameAs(var4);
      } catch (OutOfMemoryError var3) {
         Log.e("BitmapLoadUtils", "transformBitmap: ", var3);
         return var0;
      }

      if (!var2) {
         var0 = var4;
      }

      return var0;
   }
}
