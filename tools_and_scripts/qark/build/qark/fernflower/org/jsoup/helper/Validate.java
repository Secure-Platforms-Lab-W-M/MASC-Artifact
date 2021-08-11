package org.jsoup.helper;

public final class Validate {
   private Validate() {
   }

   public static void fail(String var0) {
      throw new IllegalArgumentException(var0);
   }

   public static void isFalse(boolean var0) {
      if (var0) {
         throw new IllegalArgumentException("Must be false");
      }
   }

   public static void isFalse(boolean var0, String var1) {
      if (var0) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static void isTrue(boolean var0) {
      if (!var0) {
         throw new IllegalArgumentException("Must be true");
      }
   }

   public static void isTrue(boolean var0, String var1) {
      if (!var0) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static void noNullElements(Object[] var0) {
      noNullElements(var0, "Array must not contain any null objects");
   }

   public static void noNullElements(Object[] var0, String var1) {
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0[var2] == null) {
            throw new IllegalArgumentException(var1);
         }
      }

   }

   public static void notEmpty(String var0) {
      if (var0 == null || var0.length() == 0) {
         throw new IllegalArgumentException("String must not be empty");
      }
   }

   public static void notEmpty(String var0, String var1) {
      if (var0 == null || var0.length() == 0) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static void notNull(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Object must not be null");
      }
   }

   public static void notNull(Object var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1);
      }
   }
}
