package net.sf.fmj.media.codec.video.jpeg;

import net.sf.fmj.utility.ArrayUtility;

public class RFC2035 {
   public static final short[] chm_ac_codelens = new short[]{0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119};
   public static final short[] chm_ac_symbols = new short[]{0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, 129, 8, 20, 66, 145, 161, 177, 193, 9, 35, 51, 82, 240, 21, 98, 114, 209, 10, 22, 36, 52, 225, 37, 241, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 130, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 226, 227, 228, 229, 230, 231, 232, 233, 234, 242, 243, 244, 245, 246, 247, 248, 249, 250};
   public static final short[] chm_dc_codelens = new short[]{0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
   public static final short[] chm_dc_symbols = new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
   public static final int[] jpeg_chroma_quantizer_normal = new int[]{17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
   public static final int[] jpeg_chroma_quantizer_zigzag = new int[]{17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
   public static final int[] jpeg_luma_quantizer_normal = new int[]{16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99};
   public static final int[] jpeg_luma_quantizer_zigzag = new int[]{16, 11, 12, 14, 12, 10, 16, 14, 13, 14, 18, 17, 16, 19, 24, 40, 26, 24, 22, 22, 24, 49, 35, 37, 29, 40, 58, 51, 61, 60, 57, 51, 56, 55, 64, 72, 92, 78, 64, 68, 87, 69, 55, 56, 80, 109, 81, 87, 95, 98, 103, 104, 103, 62, 77, 113, 121, 112, 100, 120, 92, 101, 103, 99};
   public static final short[] lum_ac_codelens = new short[]{0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125};
   public static final short[] lum_ac_symbols = new short[]{1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, 129, 145, 161, 8, 35, 66, 177, 193, 21, 82, 209, 240, 36, 51, 98, 114, 130, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250};
   public static final short[] lum_dc_codelens = new short[]{0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0};
   public static final short[] lum_dc_symbols = new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

   private static int MakeDRIHeader(byte[] var0, int var1, int var2) {
      int var3 = var1 + 1;
      var0[var1] = -1;
      var1 = var3 + 1;
      var0[var3] = -35;
      var3 = var1 + 1;
      var0[var1] = 0;
      var1 = var3 + 1;
      var0[var3] = 4;
      var3 = var1 + 1;
      var0[var1] = (byte)(var2 >> 8);
      var0[var3] = (byte)(var2 & 255);
      return var3 + 1;
   }

   public static int MakeHeaders(boolean var0, byte[] var1, int var2, int var3, int var4, int var5, int var6) {
      return MakeHeaders(var0, var1, var2, var3, var4, var5, var6, (byte[])null, (byte[])null, 0);
   }

   public static int MakeHeaders(boolean var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, byte[] var7, byte[] var8, int var9) {
      byte[] var10;
      if (var7 == null && var8 == null) {
         var8 = new byte[64];
         var7 = new byte[64];
         MakeTables(var4, var8, var7);
         var10 = var8;
      } else {
         var10 = var7;
         var7 = var8;
      }

      var4 = var5 << 3;
      var5 = var6 << 3;
      if (var0) {
         var6 = var2 + 1;
         var1[var2] = -1;
         var2 = var6 + 1;
         var1[var6] = -40;
      }

      var2 = MakeQuantHeader(var1, MakeQuantHeader(var1, var2, var10, 0), var7, 1);
      if (var9 != 0) {
         var2 = MakeDRIHeader(var1, var2, var9);
      }

      short[] var11 = lum_dc_codelens;
      var6 = var11.length;
      short[] var12 = lum_dc_symbols;
      var2 = MakeHuffmanHeader(var1, var2, var11, var6, var12, var12.length, 0, 0);
      var11 = lum_ac_codelens;
      var6 = var11.length;
      var12 = lum_ac_symbols;
      var2 = MakeHuffmanHeader(var1, var2, var11, var6, var12, var12.length, 0, 1);
      var11 = chm_dc_codelens;
      var6 = var11.length;
      var12 = chm_dc_symbols;
      var2 = MakeHuffmanHeader(var1, var2, var11, var6, var12, var12.length, 1, 0);
      var11 = chm_ac_codelens;
      var6 = var11.length;
      var12 = chm_ac_symbols;
      var6 = MakeHuffmanHeader(var1, var2, var11, var6, var12, var12.length, 1, 1);
      var2 = var6 + 1;
      var1[var6] = -1;
      var6 = var2 + 1;
      var1[var2] = -64;
      var2 = var6 + 1;
      var1[var6] = 0;
      var6 = var2 + 1;
      var1[var2] = 17;
      var2 = var6 + 1;
      var1[var6] = 8;
      var6 = var2 + 1;
      var1[var2] = (byte)(var5 >> 8);
      var2 = var6 + 1;
      var1[var6] = (byte)var5;
      var5 = var2 + 1;
      var1[var2] = (byte)(var4 >> 8);
      var2 = var5 + 1;
      var1[var5] = (byte)var4;
      var5 = var2 + 1;
      var1[var2] = 3;
      var4 = var5 + 1;
      var1[var5] = 1;
      if (var3 == 0) {
         var2 = var4 + 1;
         var1[var4] = 33;
      } else {
         var2 = var4 + 1;
         var1[var4] = 34;
      }

      var3 = var2 + 1;
      var1[var2] = 0;
      var2 = var3 + 1;
      var1[var3] = 2;
      var3 = var2 + 1;
      var1[var2] = 17;
      var2 = var3 + 1;
      var1[var3] = 1;
      var3 = var2 + 1;
      var1[var2] = 3;
      var2 = var3 + 1;
      var1[var3] = 17;
      var3 = var2 + 1;
      var1[var2] = 1;
      var2 = var3 + 1;
      var1[var3] = -1;
      var3 = var2 + 1;
      var1[var2] = -38;
      var2 = var3 + 1;
      var1[var3] = 0;
      var3 = var2 + 1;
      var1[var2] = 12;
      var2 = var3 + 1;
      var1[var3] = 3;
      var3 = var2 + 1;
      var1[var2] = 1;
      var2 = var3 + 1;
      var1[var3] = 0;
      var3 = var2 + 1;
      var1[var2] = 2;
      var2 = var3 + 1;
      var1[var3] = 17;
      var3 = var2 + 1;
      var1[var2] = 3;
      var2 = var3 + 1;
      var1[var3] = 17;
      var3 = var2 + 1;
      var1[var2] = 0;
      var2 = var3 + 1;
      var1[var3] = 63;
      var1[var2] = 0;
      return var2 + 1;
   }

   private static int MakeHuffmanHeader(byte[] var0, int var1, short[] var2, int var3, short[] var4, int var5, int var6, int var7) {
      int var8 = var1 + 1;
      var0[var1] = -1;
      var1 = var8 + 1;
      var0[var8] = -60;
      var8 = var1 + 1;
      var0[var1] = 0;
      var1 = var8 + 1;
      var0[var8] = (byte)(var3 + 3 + var5);
      var8 = var1 + 1;
      var0[var1] = (byte)(var7 << 4 | var6);
      System.arraycopy(ArrayUtility.shortArrayToByteArray(var2), 0, var0, var8, var3);
      var1 = var8 + var3;
      System.arraycopy(ArrayUtility.shortArrayToByteArray(var4), 0, var0, var1, var5);
      return var1 + var5;
   }

   public static int MakeQuantHeader(byte[] var0, int var1, byte[] var2, int var3) {
      int var4 = var1 + 1;
      var0[var1] = -1;
      var1 = var4 + 1;
      var0[var4] = -37;
      var4 = var1 + 1;
      var0[var1] = 0;
      var1 = var4 + 1;
      var0[var4] = 67;
      var4 = var1 + 1;
      var0[var1] = (byte)var3;
      System.arraycopy(var2, 0, var0, var4, 64);
      return var4 + 64;
   }

   private static void MakeTables(int var0, byte[] var1, byte[] var2) {
      int[] var3 = jpeg_luma_quantizer_zigzag;
      MakeTables(var0, var1, var2, var3, var3);
   }

   public static void MakeTables(int var0, byte[] var1, byte[] var2, int[] var3, int[] var4) {
      int var5 = var0;
      if (var0 < 1) {
         var5 = 1;
      }

      if (var0 > 99) {
         var5 = 99;
      }

      if (var0 < 50) {
         var5 = 5000 / var5;
      } else {
         var5 = 200 - var5 * 2;
      }

      for(int var6 = 0; var6 < 64; ++var6) {
         int var8 = (var3[var6] * var5 + 50) / 100;
         int var7 = (var4[var6] * var5 + 50) / 100;
         if (var8 < 1) {
            var0 = 1;
         } else {
            var0 = var8;
            if (var8 > 255) {
               var0 = 255;
            }
         }

         var1[var6] = (byte)var0;
         if (var7 < 1) {
            var0 = 1;
         } else {
            var0 = var7;
            if (var7 > 255) {
               var0 = 255;
            }
         }

         var2[var6] = (byte)var0;
      }

   }

   public static int[] createZigZag(int[] var0) {
      return createZigZag(var0, 8, 8);
   }

   public static int[] createZigZag(int[] var0, int var1, int var2) {
      int[] var9 = new int[var0.length];
      if (var0.length != var1 * var2) {
         throw new IllegalArgumentException();
      } else {
         int var8 = 0;
         int var4 = 0;
         int var3 = 0;
         var9[0] = var0[0];

         while(var3 * var1 + var4 < var1 * var2 - 1) {
            if (var4 < var1 - 1) {
               ++var4;
            } else {
               ++var3;
            }

            int var5 = var8 + 1;

            for(var9[var5] = var0[var3 * var1 + var4]; var4 > 0 && var3 < var2 - 1; var9[var5] = var0[var3 * var1 + var4]) {
               --var4;
               ++var3;
               ++var5;
            }

            if (var3 < var2 - 1) {
               ++var3;
            } else {
               ++var4;
            }

            ++var5;
            var9[var5] = var0[var3 * var1 + var4];
            int var7 = var3;
            int var6 = var4;

            while(true) {
               var8 = var5;
               var4 = var6;
               var3 = var7;
               if (var7 <= 0) {
                  break;
               }

               var8 = var5;
               var4 = var6;
               var3 = var7;
               if (var6 >= var1 - 1) {
                  break;
               }

               --var7;
               ++var6;
               ++var5;
               var9[var5] = var0[var7 * var1 + var6];
            }
         }

         return var9;
      }
   }
}
