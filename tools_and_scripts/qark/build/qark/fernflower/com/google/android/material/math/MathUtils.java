package com.google.android.material.math;

public final class MathUtils {
   public static final float DEFAULT_EPSILON = 1.0E-4F;

   private MathUtils() {
   }

   public static float dist(float var0, float var1, float var2, float var3) {
      return (float)Math.hypot((double)(var2 - var0), (double)(var3 - var1));
   }

   public static float distanceToFurthestCorner(float var0, float var1, float var2, float var3, float var4, float var5) {
      return max(dist(var0, var1, var2, var3), dist(var0, var1, var4, var3), dist(var0, var1, var4, var5), dist(var0, var1, var2, var5));
   }

   public static boolean geq(float var0, float var1, float var2) {
      return var0 + var2 >= var1;
   }

   public static float lerp(float var0, float var1, float var2) {
      return (1.0F - var2) * var0 + var2 * var1;
   }

   private static float max(float var0, float var1, float var2, float var3) {
      if (var0 > var1 && var0 > var2 && var0 > var3) {
         return var0;
      } else if (var1 > var2 && var1 > var3) {
         return var1;
      } else {
         return var2 > var3 ? var2 : var3;
      }
   }
}
