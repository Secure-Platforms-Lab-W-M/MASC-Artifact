package androidx.collection;

class ContainerHelpers {
   static final int[] EMPTY_INTS = new int[0];
   static final long[] EMPTY_LONGS = new long[0];
   static final Object[] EMPTY_OBJECTS = new Object[0];

   private ContainerHelpers() {
   }

   static int binarySearch(int[] var0, int var1, int var2) {
      int var3 = 0;
      --var1;

      while(var3 <= var1) {
         int var4 = var3 + var1 >>> 1;
         int var5 = var0[var4];
         if (var5 < var2) {
            var3 = var4 + 1;
         } else {
            if (var5 <= var2) {
               return var4;
            }

            var1 = var4 - 1;
         }
      }

      return var3;
   }

   static int binarySearch(long[] var0, int var1, long var2) {
      int var4 = 0;
      --var1;

      while(var4 <= var1) {
         int var5 = var4 + var1 >>> 1;
         long var6 = var0[var5];
         if (var6 < var2) {
            var4 = var5 + 1;
         } else {
            if (var6 <= var2) {
               return var5;
            }

            var1 = var5 - 1;
         }
      }

      return var4;
   }

   public static boolean equal(Object var0, Object var1) {
      return var0 == var1 || var0 != null && var0.equals(var1);
   }

   public static int idealByteArraySize(int var0) {
      for(int var1 = 4; var1 < 32; ++var1) {
         if (var0 <= (1 << var1) - 12) {
            return (1 << var1) - 12;
         }
      }

      return var0;
   }

   public static int idealIntArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   public static int idealLongArraySize(int var0) {
      return idealByteArraySize(var0 * 8) / 8;
   }
}
