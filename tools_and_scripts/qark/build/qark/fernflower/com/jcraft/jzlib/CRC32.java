package com.jcraft.jzlib;

public final class CRC32 implements Checksum {
   private static final int GF2_DIM = 32;
   private static int[] crc_table = null;
   // $FF: renamed from: v int
   private int field_192 = 0;

   static {
      crc_table = new int[256];

      for(int var0 = 0; var0 < 256; ++var0) {
         int var1 = var0;
         int var2 = 8;

         while(true) {
            --var2;
            if (var2 < 0) {
               crc_table[var0] = var1;
               break;
            }

            if ((var1 & 1) != 0) {
               var1 = var1 >>> 1 ^ -306674912;
            } else {
               var1 >>>= 1;
            }
         }
      }

   }

   static long combine(long var0, long var2, long var4) {
      long[] var11 = new long[32];
      long[] var12 = new long[32];
      if (var4 <= 0L) {
         return var0;
      } else {
         var12[0] = 3988292384L;
         long var7 = 1L;

         for(int var6 = 1; var6 < 32; ++var6) {
            var12[var6] = var7;
            var7 <<= 1;
         }

         gf2_matrix_square(var11, var12);
         gf2_matrix_square(var12, var11);
         long var9 = var4;
         var7 = var0;

         do {
            gf2_matrix_square(var11, var12);
            var4 = var7;
            if ((var9 & 1L) != 0L) {
               var4 = gf2_matrix_times(var11, var7);
            }

            var7 = var9 >> 1;
            if (var7 == 0L) {
               var0 = var4;
               break;
            }

            gf2_matrix_square(var12, var11);
            var0 = var4;
            if ((1L & var7) != 0L) {
               var0 = gf2_matrix_times(var12, var4);
            }

            var4 = var7 >> 1;
            var7 = var0;
            var9 = var4;
         } while(var4 != 0L);

         return var0 ^ var2;
      }
   }

   public static int[] getCRC32Table() {
      int[] var0 = crc_table;
      int[] var1 = new int[var0.length];
      System.arraycopy(var0, 0, var1, 0, var1.length);
      return var1;
   }

   static final void gf2_matrix_square(long[] var0, long[] var1) {
      for(int var2 = 0; var2 < 32; ++var2) {
         var0[var2] = gf2_matrix_times(var1, var1[var2]);
      }

   }

   private static long gf2_matrix_times(long[] var0, long var1) {
      long var4 = 0L;

      long var6;
      for(int var3 = 0; var1 != 0L; var4 = var6) {
         var6 = var4;
         if ((1L & var1) != 0L) {
            var6 = var4 ^ var0[var3];
         }

         var1 >>= 1;
         ++var3;
      }

      return var4;
   }

   public CRC32 copy() {
      CRC32 var1 = new CRC32();
      var1.field_192 = this.field_192;
      return var1;
   }

   public long getValue() {
      return (long)this.field_192 & 4294967295L;
   }

   public void reset() {
      this.field_192 = 0;
   }

   public void reset(long var1) {
      this.field_192 = (int)(4294967295L & var1);
   }

   public void update(byte[] var1, int var2, int var3) {
      int var5 = this.field_192;
      int var4 = var3;
      var3 = var5;

      while(true) {
         --var4;
         if (var4 < 0) {
            this.field_192 = var3;
            return;
         }

         var3 = crc_table[(var1[var2] ^ var3) & 255] ^ var3 >>> 8;
         ++var2;
      }
   }
}
