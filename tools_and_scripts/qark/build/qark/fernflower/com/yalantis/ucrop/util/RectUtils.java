package com.yalantis.ucrop.util;

import android.graphics.RectF;

public class RectUtils {
   public static float[] getCenterFromRect(RectF var0) {
      return new float[]{var0.centerX(), var0.centerY()};
   }

   public static float[] getCornersFromRect(RectF var0) {
      return new float[]{var0.left, var0.top, var0.right, var0.top, var0.right, var0.bottom, var0.left, var0.bottom};
   }

   public static float[] getRectSidesFromCorners(float[] var0) {
      return new float[]{(float)Math.sqrt(Math.pow((double)(var0[0] - var0[2]), 2.0D) + Math.pow((double)(var0[1] - var0[3]), 2.0D)), (float)Math.sqrt(Math.pow((double)(var0[2] - var0[4]), 2.0D) + Math.pow((double)(var0[3] - var0[5]), 2.0D))};
   }

   public static RectF trapToRect(float[] var0) {
      RectF var5 = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

      for(int var4 = 1; var4 < var0.length; var4 += 2) {
         float var2 = (float)Math.round(var0[var4 - 1] * 10.0F) / 10.0F;
         float var1 = (float)Math.round(var0[var4] * 10.0F) / 10.0F;
         float var3;
         if (var2 < var5.left) {
            var3 = var2;
         } else {
            var3 = var5.left;
         }

         var5.left = var3;
         if (var1 < var5.top) {
            var3 = var1;
         } else {
            var3 = var5.top;
         }

         var5.top = var3;
         if (var2 <= var5.right) {
            var2 = var5.right;
         }

         var5.right = var2;
         if (var1 <= var5.bottom) {
            var1 = var5.bottom;
         }

         var5.bottom = var1;
      }

      var5.sort();
      return var5;
   }
}
