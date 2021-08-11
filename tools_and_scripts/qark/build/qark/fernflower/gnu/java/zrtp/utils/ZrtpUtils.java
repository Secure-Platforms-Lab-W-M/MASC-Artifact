package gnu.java.zrtp.utils;

import java.io.PrintStream;

public class ZrtpUtils {
   private static final char[] hex = "0123456789abcdef".toCharArray();

   public static int byteArrayCompare(byte[] var0, byte[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         if ((var0[var3] & 255) != (var1[var3] & 255)) {
            if ((var0[var3] & 255) < (var1[var3] & 255)) {
               return -1;
            }

            return 1;
         }
      }

      return 0;
   }

   public static char[] bytesToHexString(byte[] var0, int var1) {
      int var2 = var1;
      if (var1 > var0.length) {
         var2 = var0.length;
      }

      char[] var4 = new char[var2 * 2];

      for(var1 = 0; var1 < var2; ++var1) {
         byte var3 = var0[var1];
         char[] var5 = hex;
         var4[var1 * 2] = var5[var3 >>> 4 & 15];
         var4[var1 * 2 + 1] = var5[var3 & 15];
      }

      return var4;
   }

   public static void hexdump(String var0, byte[] var1, int var2) {
      System.err.println(var0);
      int var3 = 0;

      while(true) {
         int var4;
         byte var5;
         for(var4 = 0; var4 < 16; ++var4) {
            if (var3 + var4 >= var2) {
               System.err.print("   ");
            } else {
               var5 = var1[var3 + var4];
               PrintStream var7 = System.err;
               StringBuilder var6 = new StringBuilder();
               var6.append(" ");
               var6.append(hex[var5 >>> 4 & 15]);
               var6.append(hex[var5 & 15]);
               var7.print(var6.toString());
            }
         }

         System.err.print("  ");

         for(var4 = 0; var4 < 16 && var3 + var4 < var2; ++var4) {
            var5 = var1[var3 + var4];
            if ((byte)(var5 + 1) < 33) {
               System.err.print('.');
            } else {
               System.err.print((char)var5);
            }
         }

         System.err.println();
         if (var3 + 16 >= var2) {
            return;
         }

         var3 += 16;
      }
   }

   public static byte[] int32ToArray(int var0) {
      return new byte[]{(byte)(var0 >> 24), (byte)(var0 >> 16), (byte)(var0 >> 8), (byte)var0};
   }

   public static void int32ToArrayInPlace(int var0, byte[] var1, int var2) {
      var1[var2] = (byte)(var0 >> 24);
      var1[var2 + 1] = (byte)(var0 >> 16);
      var1[var2 + 2] = (byte)(var0 >> 8);
      var1[var2 + 3] = (byte)var0;
   }

   public static int readInt(byte[] var0, int var1) {
      return var0[var1] << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
   }

   public static byte[] readRegion(byte[] var0, int var1, int var2) {
      if (var1 >= 0 && var2 > 0 && var1 + var2 <= var0.length) {
         byte[] var3 = new byte[var2];
         System.arraycopy(var0, var1, var3, 0, var2);
         return var3;
      } else {
         return null;
      }
   }

   public static short readShort(byte[] var0, int var1) {
      return (short)((var0[var1] & 255) << 8 | var0[var1 + 1] & 255);
   }

   public static byte[] short16ToArray(int var0) {
      return new byte[]{(byte)(var0 >> 8), (byte)var0};
   }

   public static void short16ToArrayInPlace(int var0, byte[] var1, int var2) {
      var1[var2] = (byte)(var0 >> 8);
      var1[var2 + 1] = (byte)var0;
   }
}
