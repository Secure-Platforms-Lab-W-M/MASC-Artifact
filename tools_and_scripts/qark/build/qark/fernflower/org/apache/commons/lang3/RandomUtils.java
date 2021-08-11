package org.apache.commons.lang3;

import java.util.Random;

public class RandomUtils {
   private static final Random RANDOM = new Random();

   public static boolean nextBoolean() {
      return RANDOM.nextBoolean();
   }

   public static byte[] nextBytes(int var0) {
      boolean var1;
      if (var0 >= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "Count cannot be negative.");
      byte[] var2 = new byte[var0];
      RANDOM.nextBytes(var2);
      return var2;
   }

   public static double nextDouble() {
      return nextDouble(0.0D, Double.MAX_VALUE);
   }

   public static double nextDouble(double var0, double var2) {
      boolean var5 = true;
      boolean var4;
      if (var2 >= var0) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "Start value must be smaller or equal to end value.");
      if (var0 >= 0.0D) {
         var4 = var5;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "Both range values must be non-negative.");
      return var0 == var2 ? var0 : (var2 - var0) * RANDOM.nextDouble() + var0;
   }

   public static float nextFloat() {
      return nextFloat(0.0F, Float.MAX_VALUE);
   }

   public static float nextFloat(float var0, float var1) {
      boolean var3 = true;
      boolean var2;
      if (var1 >= var0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Start value must be smaller or equal to end value.");
      if (var0 >= 0.0F) {
         var2 = var3;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Both range values must be non-negative.");
      return var0 == var1 ? var0 : (var1 - var0) * RANDOM.nextFloat() + var0;
   }

   public static int nextInt() {
      return nextInt(0, Integer.MAX_VALUE);
   }

   public static int nextInt(int var0, int var1) {
      boolean var3 = true;
      boolean var2;
      if (var1 >= var0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Start value must be smaller or equal to end value.");
      if (var0 >= 0) {
         var2 = var3;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Both range values must be non-negative.");
      return var0 == var1 ? var0 : RANDOM.nextInt(var1 - var0) + var0;
   }

   public static long nextLong() {
      return nextLong(0L, Long.MAX_VALUE);
   }

   public static long nextLong(long var0, long var2) {
      boolean var5 = true;
      boolean var4;
      if (var2 >= var0) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "Start value must be smaller or equal to end value.");
      if (var0 >= 0L) {
         var4 = var5;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "Both range values must be non-negative.");
      return var0 == var2 ? var0 : (long)nextDouble((double)var0, (double)var2);
   }
}
