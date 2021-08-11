package com.jcraft.jzlib;

final class Tree {
   private static final int BL_CODES = 19;
   static final int Buf_size = 16;
   static final int DIST_CODE_LEN = 512;
   private static final int D_CODES = 30;
   static final int END_BLOCK = 256;
   private static final int HEAP_SIZE = 573;
   private static final int LENGTH_CODES = 29;
   private static final int LITERALS = 256;
   private static final int L_CODES = 286;
   private static final int MAX_BITS = 15;
   static final int MAX_BL_BITS = 7;
   static final int REPZ_11_138 = 18;
   static final int REPZ_3_10 = 17;
   static final int REP_3_6 = 16;
   static final byte[] _dist_code = new byte[]{0, 1, 2, 3, 4, 4, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 0, 16, 17, 18, 18, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29};
   static final byte[] _length_code = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28};
   static final int[] base_dist = new int[]{0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576};
   static final int[] base_length = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 0};
   static final byte[] bl_order = new byte[]{16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15};
   static final int[] extra_blbits = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7};
   static final int[] extra_dbits = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13};
   static final int[] extra_lbits = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0};
   short[] dyn_tree;
   int max_code;
   StaticTree stat_desc;

   private static final int bi_reverse(int var0, int var1) {
      int var2 = 0;

      do {
         int var3 = var0;
         var0 >>>= 1;
         var2 = (var2 | var3 & 1) << 1;
         --var1;
      } while(var1 > 0);

      return var2 >>> 1;
   }

   static int d_code(int var0) {
      return var0 < 256 ? _dist_code[var0] : _dist_code[(var0 >>> 7) + 256];
   }

   private static final void gen_codes(short[] var0, int var1, short[] var2, short[] var3) {
      short var6 = 0;
      var3[0] = 0;

      int var5;
      for(var5 = 1; var5 <= 15; ++var5) {
         short var4 = (short)(var2[var5 - 1] + var6 << 1);
         var6 = var4;
         var3[var5] = var4;
      }

      for(var5 = 0; var5 <= var1; ++var5) {
         var6 = var0[var5 * 2 + 1];
         if (var6 != 0) {
            short var7 = var3[var6];
            var3[var6] = (short)(var7 + 1);
            var0[var5 * 2] = (short)bi_reverse(var7, var6);
         }
      }

   }

   void build_tree(Deflate var1) {
      short[] var8 = this.dyn_tree;
      short[] var9 = this.stat_desc.static_tree;
      int var6 = this.stat_desc.elems;
      int var5 = -1;
      var1.heap_len = 0;
      var1.heap_max = 573;
      int var4 = 0;

      while(true) {
         int var3 = var5;
         int[] var10;
         if (var4 >= var6) {
            while(var1.heap_len < 2) {
               var10 = var1.heap;
               var5 = var1.heap_len + 1;
               var1.heap_len = var5;
               if (var3 < 2) {
                  var4 = var3 + 1;
                  var3 = var4;
               } else {
                  var4 = 0;
               }

               var10[var5] = var4;
               var8[var4 * 2] = 1;
               var1.depth[var4] = 0;
               --var1.opt_len;
               if (var9 != null) {
                  var1.static_len -= var9[var4 * 2 + 1];
               }
            }

            this.max_code = var3;

            for(var4 = var1.heap_len / 2; var4 >= 1; --var4) {
               var1.pqdownheap(var8, var4);
            }

            var4 = var6;

            while(true) {
               var5 = var1.heap[1];
               int[] var11 = var1.heap;
               var10 = var1.heap;
               var6 = var1.heap_len--;
               var11[1] = var10[var6];
               var1.pqdownheap(var8, 1);
               var6 = var1.heap[1];
               var11 = var1.heap;
               int var7 = var1.heap_max - 1;
               var1.heap_max = var7;
               var11[var7] = var5;
               var11 = var1.heap;
               var7 = var1.heap_max - 1;
               var1.heap_max = var7;
               var11[var7] = var6;
               var8[var4 * 2] = (short)(var8[var5 * 2] + var8[var6 * 2]);
               var1.depth[var4] = (byte)(Math.max(var1.depth[var5], var1.depth[var6]) + 1);
               short var2 = (short)var4;
               var8[var6 * 2 + 1] = var2;
               var8[var5 * 2 + 1] = var2;
               var1.heap[1] = var4;
               var1.pqdownheap(var8, 1);
               if (var1.heap_len < 2) {
                  var11 = var1.heap;
                  var4 = var1.heap_max - 1;
                  var1.heap_max = var4;
                  var11[var4] = var1.heap[1];
                  this.gen_bitlen(var1);
                  gen_codes(var8, var3, var1.bl_count, var1.next_code);
                  return;
               }

               ++var4;
            }
         }

         if (var8[var4 * 2] != 0) {
            var10 = var1.heap;
            var5 = var1.heap_len + 1;
            var1.heap_len = var5;
            var3 = var4;
            var10[var5] = var4;
            var1.depth[var4] = 0;
         } else {
            var8[var4 * 2 + 1] = 0;
            var3 = var5;
         }

         ++var4;
         var5 = var3;
      }
   }

   void gen_bitlen(Deflate var1) {
      Tree var12 = this;
      short[] var13 = this.dyn_tree;
      short[] var11 = this.stat_desc.static_tree;
      int[] var10 = this.stat_desc.extra_bits;
      int var7 = this.stat_desc.extra_base;
      int var4 = this.stat_desc.max_length;
      int var3 = 0;

      int var2;
      for(var2 = 0; var2 <= 15; ++var2) {
         var1.bl_count[var2] = 0;
      }

      var13[var1.heap[var1.heap_max] * 2 + 1] = 0;

      int var5;
      for(var2 = var1.heap_max + 1; var2 < 573; ++var2) {
         int var8 = var1.heap[var2];
         var5 = var13[var13[var8 * 2 + 1] * 2 + 1] + 1;
         if (var5 > var4) {
            var5 = var4;
            ++var3;
         }

         var13[var8 * 2 + 1] = (short)var5;
         if (var8 <= var12.max_code) {
            short[] var14 = var1.bl_count;
            ++var14[var5];
            int var6 = 0;
            if (var8 >= var7) {
               var6 = var10[var8 - var7];
            }

            short var9 = var13[var8 * 2];
            var1.opt_len += (var5 + var6) * var9;
            if (var11 != null) {
               var1.static_len += (var11[var8 * 2 + 1] + var6) * var9;
            }
         }
      }

      var5 = var3;
      if (var3 != 0) {
         do {
            for(var3 = var4 - 1; var1.bl_count[var3] == 0; --var3) {
            }

            short[] var15 = var1.bl_count;
            --var15[var3];
            var15 = var1.bl_count;
            ++var3;
            var15[var3] = (short)(var15[var3] + 2);
            var15 = var1.bl_count;
            --var15[var4];
            var5 -= 2;
         } while(var5 > 0);

         var3 = var4;
         var4 = var2;

         for(var2 = var3; var2 != 0; --var2) {
            var3 = var1.bl_count[var2];

            while(var3 != 0) {
               int[] var16 = var1.heap;
               --var4;
               var5 = var16[var4];
               if (var5 <= this.max_code) {
                  if (var13[var5 * 2 + 1] != var2) {
                     var1.opt_len = (int)((long)var1.opt_len + ((long)var2 - (long)var13[var5 * 2 + 1]) * (long)var13[var5 * 2]);
                     var13[var5 * 2 + 1] = (short)var2;
                  }

                  --var3;
               }
            }
         }

      }
   }
}
