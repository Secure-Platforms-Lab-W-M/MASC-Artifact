package org.apache.commons.codec.digest;

import org.apache.commons.codec.binary.StringUtils;

public final class MurmurHash2 {
   private static final int M32 = 1540483477;
   private static final long M64 = -4132994306676758123L;
   private static final int R32 = 24;
   private static final int R64 = 47;

   private MurmurHash2() {
   }

   private static int getLittleEndianInt(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   private static long getLittleEndianLong(byte[] var0, int var1) {
      return (long)var0[var1] & 255L | ((long)var0[var1 + 1] & 255L) << 8 | ((long)var0[var1 + 2] & 255L) << 16 | ((long)var0[var1 + 3] & 255L) << 24 | ((long)var0[var1 + 4] & 255L) << 32 | ((long)var0[var1 + 5] & 255L) << 40 | ((long)var0[var1 + 6] & 255L) << 48 | (255L & (long)var0[var1 + 7]) << 56;
   }

   public static int hash32(String var0) {
      byte[] var1 = StringUtils.getBytesUtf8(var0);
      return hash32(var1, var1.length);
   }

   public static int hash32(String var0, int var1, int var2) {
      return hash32(var0.substring(var1, var1 + var2));
   }

   public static int hash32(byte[] var0, int var1) {
      return hash32(var0, var1, -1756908916);
   }

   public static int hash32(byte[] var0, int var1, int var2) {
      var2 ^= var1;
      int var4 = var1 >> 2;

      int var3;
      for(var3 = 0; var3 < var4; ++var3) {
         int var5 = getLittleEndianInt(var0, var3 << 2) * 1540483477;
         var2 = var2 * 1540483477 ^ (var5 ^ var5 >>> 24) * 1540483477;
      }

      label21: {
         var3 = var4 << 2;
         var4 = var1 - var3;
         var1 = var2;
         if (var4 != 1) {
            var1 = var2;
            if (var4 != 2) {
               if (var4 != 3) {
                  break label21;
               }

               var1 = var2 ^ (var0[var3 + 2] & 255) << 16;
            }

            var1 ^= (var0[var3 + 1] & 255) << 8;
         }

         var2 = (var1 ^ var0[var3] & 255) * 1540483477;
      }

      var1 = (var2 ^ var2 >>> 13) * 1540483477;
      return var1 ^ var1 >>> 15;
   }

   public static long hash64(String var0) {
      byte[] var1 = StringUtils.getBytesUtf8(var0);
      return hash64(var1, var1.length);
   }

   public static long hash64(String var0, int var1, int var2) {
      return hash64(var0.substring(var1, var1 + var2));
   }

   public static long hash64(byte[] var0, int var1) {
      return hash64(var0, var1, -512093083);
   }

   public static long hash64(byte[] var0, int var1, int var2) {
      long var4 = (long)var2 & 4294967295L ^ (long)var1 * -4132994306676758123L;
      int var3 = var1 >> 3;

      long var6;
      for(var2 = 0; var2 < var3; ++var2) {
         var6 = getLittleEndianLong(var0, var2 << 3) * -4132994306676758123L;
         var4 = (var4 ^ (var6 ^ var6 >>> 47) * -4132994306676758123L) * -4132994306676758123L;
      }

      var2 = var3 << 3;
      var6 = var4;
      long var8 = var4;
      long var10 = var4;
      long var12 = var4;
      long var14 = var4;
      long var16 = var4;
      switch(var1 - var2) {
      case 7:
         var6 = var4 ^ ((long)var0[var2 + 6] & 255L) << 48;
      case 6:
         var8 = var6 ^ ((long)var0[var2 + 5] & 255L) << 40;
      case 5:
         var10 = var8 ^ ((long)var0[var2 + 4] & 255L) << 32;
      case 4:
         var12 = var10 ^ ((long)var0[var2 + 3] & 255L) << 24;
      case 3:
         var14 = var12 ^ ((long)var0[var2 + 2] & 255L) << 16;
      case 2:
         var16 = var14 ^ ((long)var0[var2 + 1] & 255L) << 8;
      case 1:
         var4 = (var16 ^ (long)var0[var2] & 255L) * -4132994306676758123L;
      default:
         var4 = (var4 ^ var4 >>> 47) * -4132994306676758123L;
         return var4 ^ var4 >>> 47;
      }
   }
}
