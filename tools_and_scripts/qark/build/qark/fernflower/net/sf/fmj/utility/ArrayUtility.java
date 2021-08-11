package net.sf.fmj.utility;

public class ArrayUtility {
   public static int[] byteArrayToIntArray(byte[] var0) {
      int[] var2 = new int[var0.length];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var2[var1] = var0[var1] & 255;
      }

      return var2;
   }

   public static byte[] copyOfRange(byte[] var0, int var1, int var2) {
      return intArrayToByteArray(copyOfRange(byteArrayToIntArray(var0), var1, var2));
   }

   public static int[] copyOfRange(int[] var0, int var1, int var2) {
      if (var0.length > var1 && var1 >= 0) {
         if (var2 > var0.length) {
            throw new ArrayIndexOutOfBoundsException(var2);
         } else if (var2 < var1) {
            throw new IllegalArgumentException();
         } else {
            int[] var5 = new int[var2 - var1];
            byte var4 = 0;
            int var3 = var1;

            for(var1 = var4; var3 < var2; ++var1) {
               var5[var1] = var0[var3];
               ++var3;
            }

            return var5;
         }
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public static byte[] intArrayToByteArray(int[] var0) {
      byte[] var2 = new byte[var0.length];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var2[var1] = (byte)var0[var1];
      }

      return var2;
   }

   public static byte[] shortArrayToByteArray(short[] var0) {
      byte[] var2 = new byte[var0.length];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var2[var1] = (byte)var0[var1];
      }

      return var2;
   }
}
