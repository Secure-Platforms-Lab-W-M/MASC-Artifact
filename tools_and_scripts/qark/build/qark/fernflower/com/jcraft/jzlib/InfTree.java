package com.jcraft.jzlib;

final class InfTree {
   static final int BMAX = 15;
   private static final int MANY = 1440;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_ERRNO = -1;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   private static final int Z_OK = 0;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   private static final int Z_VERSION_ERROR = -6;
   static final int[] cpdext = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13};
   static final int[] cpdist = new int[]{1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577};
   static final int[] cplens = new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0};
   static final int[] cplext = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 112, 112};
   static final int fixed_bd = 5;
   static final int fixed_bl = 9;
   static final int[] fixed_td = new int[]{80, 5, 1, 87, 5, 257, 83, 5, 17, 91, 5, 4097, 81, 5, 5, 89, 5, 1025, 85, 5, 65, 93, 5, 16385, 80, 5, 3, 88, 5, 513, 84, 5, 33, 92, 5, 8193, 82, 5, 9, 90, 5, 2049, 86, 5, 129, 192, 5, 24577, 80, 5, 2, 87, 5, 385, 83, 5, 25, 91, 5, 6145, 81, 5, 7, 89, 5, 1537, 85, 5, 97, 93, 5, 24577, 80, 5, 4, 88, 5, 769, 84, 5, 49, 92, 5, 12289, 82, 5, 13, 90, 5, 3073, 86, 5, 193, 192, 5, 24577};
   static final int[] fixed_tl = new int[]{96, 7, 256, 0, 8, 80, 0, 8, 16, 84, 8, 115, 82, 7, 31, 0, 8, 112, 0, 8, 48, 0, 9, 192, 80, 7, 10, 0, 8, 96, 0, 8, 32, 0, 9, 160, 0, 8, 0, 0, 8, 128, 0, 8, 64, 0, 9, 224, 80, 7, 6, 0, 8, 88, 0, 8, 24, 0, 9, 144, 83, 7, 59, 0, 8, 120, 0, 8, 56, 0, 9, 208, 81, 7, 17, 0, 8, 104, 0, 8, 40, 0, 9, 176, 0, 8, 8, 0, 8, 136, 0, 8, 72, 0, 9, 240, 80, 7, 4, 0, 8, 84, 0, 8, 20, 85, 8, 227, 83, 7, 43, 0, 8, 116, 0, 8, 52, 0, 9, 200, 81, 7, 13, 0, 8, 100, 0, 8, 36, 0, 9, 168, 0, 8, 4, 0, 8, 132, 0, 8, 68, 0, 9, 232, 80, 7, 8, 0, 8, 92, 0, 8, 28, 0, 9, 152, 84, 7, 83, 0, 8, 124, 0, 8, 60, 0, 9, 216, 82, 7, 23, 0, 8, 108, 0, 8, 44, 0, 9, 184, 0, 8, 12, 0, 8, 140, 0, 8, 76, 0, 9, 248, 80, 7, 3, 0, 8, 82, 0, 8, 18, 85, 8, 163, 83, 7, 35, 0, 8, 114, 0, 8, 50, 0, 9, 196, 81, 7, 11, 0, 8, 98, 0, 8, 34, 0, 9, 164, 0, 8, 2, 0, 8, 130, 0, 8, 66, 0, 9, 228, 80, 7, 7, 0, 8, 90, 0, 8, 26, 0, 9, 148, 84, 7, 67, 0, 8, 122, 0, 8, 58, 0, 9, 212, 82, 7, 19, 0, 8, 106, 0, 8, 42, 0, 9, 180, 0, 8, 10, 0, 8, 138, 0, 8, 74, 0, 9, 244, 80, 7, 5, 0, 8, 86, 0, 8, 22, 192, 8, 0, 83, 7, 51, 0, 8, 118, 0, 8, 54, 0, 9, 204, 81, 7, 15, 0, 8, 102, 0, 8, 38, 0, 9, 172, 0, 8, 6, 0, 8, 134, 0, 8, 70, 0, 9, 236, 80, 7, 9, 0, 8, 94, 0, 8, 30, 0, 9, 156, 84, 7, 99, 0, 8, 126, 0, 8, 62, 0, 9, 220, 82, 7, 27, 0, 8, 110, 0, 8, 46, 0, 9, 188, 0, 8, 14, 0, 8, 142, 0, 8, 78, 0, 9, 252, 96, 7, 256, 0, 8, 81, 0, 8, 17, 85, 8, 131, 82, 7, 31, 0, 8, 113, 0, 8, 49, 0, 9, 194, 80, 7, 10, 0, 8, 97, 0, 8, 33, 0, 9, 162, 0, 8, 1, 0, 8, 129, 0, 8, 65, 0, 9, 226, 80, 7, 6, 0, 8, 89, 0, 8, 25, 0, 9, 146, 83, 7, 59, 0, 8, 121, 0, 8, 57, 0, 9, 210, 81, 7, 17, 0, 8, 105, 0, 8, 41, 0, 9, 178, 0, 8, 9, 0, 8, 137, 0, 8, 73, 0, 9, 242, 80, 7, 4, 0, 8, 85, 0, 8, 21, 80, 8, 258, 83, 7, 43, 0, 8, 117, 0, 8, 53, 0, 9, 202, 81, 7, 13, 0, 8, 101, 0, 8, 37, 0, 9, 170, 0, 8, 5, 0, 8, 133, 0, 8, 69, 0, 9, 234, 80, 7, 8, 0, 8, 93, 0, 8, 29, 0, 9, 154, 84, 7, 83, 0, 8, 125, 0, 8, 61, 0, 9, 218, 82, 7, 23, 0, 8, 109, 0, 8, 45, 0, 9, 186, 0, 8, 13, 0, 8, 141, 0, 8, 77, 0, 9, 250, 80, 7, 3, 0, 8, 83, 0, 8, 19, 85, 8, 195, 83, 7, 35, 0, 8, 115, 0, 8, 51, 0, 9, 198, 81, 7, 11, 0, 8, 99, 0, 8, 35, 0, 9, 166, 0, 8, 3, 0, 8, 131, 0, 8, 67, 0, 9, 230, 80, 7, 7, 0, 8, 91, 0, 8, 27, 0, 9, 150, 84, 7, 67, 0, 8, 123, 0, 8, 59, 0, 9, 214, 82, 7, 19, 0, 8, 107, 0, 8, 43, 0, 9, 182, 0, 8, 11, 0, 8, 139, 0, 8, 75, 0, 9, 246, 80, 7, 5, 0, 8, 87, 0, 8, 23, 192, 8, 0, 83, 7, 51, 0, 8, 119, 0, 8, 55, 0, 9, 206, 81, 7, 15, 0, 8, 103, 0, 8, 39, 0, 9, 174, 0, 8, 7, 0, 8, 135, 0, 8, 71, 0, 9, 238, 80, 7, 9, 0, 8, 95, 0, 8, 31, 0, 9, 158, 84, 7, 99, 0, 8, 127, 0, 8, 63, 0, 9, 222, 82, 7, 27, 0, 8, 111, 0, 8, 47, 0, 9, 190, 0, 8, 15, 0, 8, 143, 0, 8, 79, 0, 9, 254, 96, 7, 256, 0, 8, 80, 0, 8, 16, 84, 8, 115, 82, 7, 31, 0, 8, 112, 0, 8, 48, 0, 9, 193, 80, 7, 10, 0, 8, 96, 0, 8, 32, 0, 9, 161, 0, 8, 0, 0, 8, 128, 0, 8, 64, 0, 9, 225, 80, 7, 6, 0, 8, 88, 0, 8, 24, 0, 9, 145, 83, 7, 59, 0, 8, 120, 0, 8, 56, 0, 9, 209, 81, 7, 17, 0, 8, 104, 0, 8, 40, 0, 9, 177, 0, 8, 8, 0, 8, 136, 0, 8, 72, 0, 9, 241, 80, 7, 4, 0, 8, 84, 0, 8, 20, 85, 8, 227, 83, 7, 43, 0, 8, 116, 0, 8, 52, 0, 9, 201, 81, 7, 13, 0, 8, 100, 0, 8, 36, 0, 9, 169, 0, 8, 4, 0, 8, 132, 0, 8, 68, 0, 9, 233, 80, 7, 8, 0, 8, 92, 0, 8, 28, 0, 9, 153, 84, 7, 83, 0, 8, 124, 0, 8, 60, 0, 9, 217, 82, 7, 23, 0, 8, 108, 0, 8, 44, 0, 9, 185, 0, 8, 12, 0, 8, 140, 0, 8, 76, 0, 9, 249, 80, 7, 3, 0, 8, 82, 0, 8, 18, 85, 8, 163, 83, 7, 35, 0, 8, 114, 0, 8, 50, 0, 9, 197, 81, 7, 11, 0, 8, 98, 0, 8, 34, 0, 9, 165, 0, 8, 2, 0, 8, 130, 0, 8, 66, 0, 9, 229, 80, 7, 7, 0, 8, 90, 0, 8, 26, 0, 9, 149, 84, 7, 67, 0, 8, 122, 0, 8, 58, 0, 9, 213, 82, 7, 19, 0, 8, 106, 0, 8, 42, 0, 9, 181, 0, 8, 10, 0, 8, 138, 0, 8, 74, 0, 9, 245, 80, 7, 5, 0, 8, 86, 0, 8, 22, 192, 8, 0, 83, 7, 51, 0, 8, 118, 0, 8, 54, 0, 9, 205, 81, 7, 15, 0, 8, 102, 0, 8, 38, 0, 9, 173, 0, 8, 6, 0, 8, 134, 0, 8, 70, 0, 9, 237, 80, 7, 9, 0, 8, 94, 0, 8, 30, 0, 9, 157, 84, 7, 99, 0, 8, 126, 0, 8, 62, 0, 9, 221, 82, 7, 27, 0, 8, 110, 0, 8, 46, 0, 9, 189, 0, 8, 14, 0, 8, 142, 0, 8, 78, 0, 9, 253, 96, 7, 256, 0, 8, 81, 0, 8, 17, 85, 8, 131, 82, 7, 31, 0, 8, 113, 0, 8, 49, 0, 9, 195, 80, 7, 10, 0, 8, 97, 0, 8, 33, 0, 9, 163, 0, 8, 1, 0, 8, 129, 0, 8, 65, 0, 9, 227, 80, 7, 6, 0, 8, 89, 0, 8, 25, 0, 9, 147, 83, 7, 59, 0, 8, 121, 0, 8, 57, 0, 9, 211, 81, 7, 17, 0, 8, 105, 0, 8, 41, 0, 9, 179, 0, 8, 9, 0, 8, 137, 0, 8, 73, 0, 9, 243, 80, 7, 4, 0, 8, 85, 0, 8, 21, 80, 8, 258, 83, 7, 43, 0, 8, 117, 0, 8, 53, 0, 9, 203, 81, 7, 13, 0, 8, 101, 0, 8, 37, 0, 9, 171, 0, 8, 5, 0, 8, 133, 0, 8, 69, 0, 9, 235, 80, 7, 8, 0, 8, 93, 0, 8, 29, 0, 9, 155, 84, 7, 83, 0, 8, 125, 0, 8, 61, 0, 9, 219, 82, 7, 23, 0, 8, 109, 0, 8, 45, 0, 9, 187, 0, 8, 13, 0, 8, 141, 0, 8, 77, 0, 9, 251, 80, 7, 3, 0, 8, 83, 0, 8, 19, 85, 8, 195, 83, 7, 35, 0, 8, 115, 0, 8, 51, 0, 9, 199, 81, 7, 11, 0, 8, 99, 0, 8, 35, 0, 9, 167, 0, 8, 3, 0, 8, 131, 0, 8, 67, 0, 9, 231, 80, 7, 7, 0, 8, 91, 0, 8, 27, 0, 9, 151, 84, 7, 67, 0, 8, 123, 0, 8, 59, 0, 9, 215, 82, 7, 19, 0, 8, 107, 0, 8, 43, 0, 9, 183, 0, 8, 11, 0, 8, 139, 0, 8, 75, 0, 9, 247, 80, 7, 5, 0, 8, 87, 0, 8, 23, 192, 8, 0, 83, 7, 51, 0, 8, 119, 0, 8, 55, 0, 9, 207, 81, 7, 15, 0, 8, 103, 0, 8, 39, 0, 9, 175, 0, 8, 7, 0, 8, 135, 0, 8, 71, 0, 9, 239, 80, 7, 9, 0, 8, 95, 0, 8, 31, 0, 9, 159, 84, 7, 99, 0, 8, 127, 0, 8, 63, 0, 9, 223, 82, 7, 27, 0, 8, 111, 0, 8, 47, 0, 9, 191, 0, 8, 15, 0, 8, 143, 0, 8, 79, 0, 9, 255};
   // $FF: renamed from: c int[]
   int[] field_124 = null;
   // $FF: renamed from: hn int[]
   int[] field_125 = null;
   // $FF: renamed from: r int[]
   int[] field_126 = null;
   // $FF: renamed from: u int[]
   int[] field_127 = null;
   // $FF: renamed from: v int[]
   int[] field_128 = null;
   // $FF: renamed from: x int[]
   int[] field_129 = null;

   private int huft_build(int[] var1, int var2, int var3, int var4, int[] var5, int[] var6, int[] var7, int[] var8, int[] var9, int[] var10, int[] var11) {
      int var13 = 0;
      int var12 = var3;

      int var14;
      int[] var27;
      do {
         var27 = this.field_124;
         var14 = var1[var2 + var13];
         int var10002 = var27[var14]++;
         ++var13;
         --var12;
      } while(var12 != 0);

      if (var27[0] == var3) {
         var7[0] = -1;
         var8[0] = 0;
         return 0;
      } else {
         var12 = var8[0];

         for(var13 = 1; var13 <= 15 && this.field_124[var13] == 0; ++var13) {
         }

         int var18 = var13;
         var14 = var12;
         if (var12 < var13) {
            var14 = var13;
         }

         for(var12 = 15; var12 != 0 && this.field_124[var12] == 0; --var12) {
         }

         int var15 = var12;
         int var17 = var14;
         if (var14 > var12) {
            var17 = var12;
         }

         var8[0] = var17;

         for(var14 = 1 << var13; var13 < var12; var14 <<= 1) {
            var14 -= this.field_124[var13];
            if (var14 < 0) {
               return -3;
            }

            ++var13;
         }

         var8 = this.field_124;
         int var26 = var14 - var8[var12];
         if (var26 < 0) {
            return -3;
         } else {
            var8[var12] += var26;
            var8 = this.field_129;
            var14 = 0;
            var8[1] = 0;
            byte var19 = 1;
            var13 = 2;
            int var16 = var12;
            var12 = var19;

            while(true) {
               --var16;
               int var29;
               if (var16 == 0) {
                  var16 = 0;
                  var14 = 0;

                  while(true) {
                     var29 = var1[var2 + var14];
                     var12 = var29;
                     if (var29 != 0) {
                        var8 = this.field_129;
                        var29 = var8[var29];
                        var8[var12] = var29 + 1;
                        var11[var29] = var16;
                     }

                     ++var16;
                     if (var16 >= var3) {
                        var1 = this.field_129;
                        int var23 = var1[var15];
                        var29 = 0;
                        var1[0] = 0;
                        var14 = 0;
                        var16 = -1;
                        int var22 = -var17;
                        this.field_127[0] = 0;
                        int var21 = 0;
                        int var20 = 0;
                        var2 = var15;

                        for(var13 = var23; var18 <= var2; var22 = var12) {
                           var23 = this.field_124[var18];
                           var12 = var22;
                           var22 = var23;

                           while(true) {
                              int var24 = var22 - 1;
                              if (var22 == 0) {
                                 ++var18;
                                 break;
                              }

                              var15 = var20;

                              int var25;
                              while(var18 > var12 + var17) {
                                 var15 = var16 + 1;
                                 var22 = var12 + var17;
                                 var12 = var2 - var22;
                                 if (var12 > var17) {
                                    var12 = var17;
                                 }

                                 var16 = var12;
                                 var12 = var18 - var22;
                                 var20 = 1 << var12;
                                 if (var20 > var24 + 1) {
                                    var20 -= var24 + 1;
                                    var3 = var18;
                                    if (var12 < var16) {
                                       while(true) {
                                          ++var12;
                                          if (var12 >= var16) {
                                             break;
                                          }

                                          var20 <<= 1;
                                          var1 = this.field_124;
                                          ++var3;
                                          if (var20 <= var1[var3]) {
                                             break;
                                          }

                                          var20 -= var1[var3];
                                       }
                                    }
                                 }

                                 var20 = 1 << var12;
                                 if (var10[0] + var20 > 1440) {
                                    return -3;
                                 }

                                 var1 = this.field_127;
                                 var16 = var10[0];
                                 var21 = var16;
                                 var1[var15] = var16;
                                 var10[0] += var20;
                                 if (var15 != 0) {
                                    this.field_129[var15] = var29;
                                    var8 = this.field_126;
                                    var8[0] = (byte)var12;
                                    var8[1] = (byte)var17;
                                    var25 = var29 >>> var22 - var17;
                                    var8[2] = var16 - var1[var15 - 1] - var25;
                                    System.arraycopy(var8, 0, var9, (var1[var15 - 1] + var25) * 3, 3);
                                    var16 = var15;
                                    var15 = var20;
                                    var12 = var22;
                                 } else {
                                    var7[0] = var16;
                                    var16 = var15;
                                    var15 = var20;
                                    var12 = var22;
                                 }
                              }

                              var20 = var16;
                              var1 = this.field_126;
                              var1[1] = (byte)(var18 - var12);
                              if (var14 >= var13) {
                                 var1[0] = 192;
                              } else if (var11[var14] < var4) {
                                 byte var28;
                                 if (var11[var14] < 256) {
                                    var28 = 0;
                                 } else {
                                    var28 = 96;
                                 }

                                 var1[0] = (byte)var28;
                                 this.field_126[2] = var11[var14];
                                 ++var14;
                              } else {
                                 var1[0] = (byte)(var6[var11[var14] - var4] + 16 + 64);
                                 var1[2] = var5[var11[var14] - var4];
                                 ++var14;
                              }

                              for(var16 = var29 >>> var12; var16 < var15; var16 += 1 << var18 - var12) {
                                 System.arraycopy(this.field_126, 0, var9, (var21 + var16) * 3, 3);
                              }

                              for(var16 = 1 << var18 - 1; (var29 & var16) != 0; var16 >>>= 1) {
                                 var29 ^= var16;
                              }

                              var25 = var29 ^ var16;
                              var22 = (1 << var12) - 1;

                              for(var29 = var20; (var25 & var22) != this.field_129[var29]; var22 = (1 << var12) - 1) {
                                 --var29;
                                 var12 -= var17;
                              }

                              var22 = var29;
                              var20 = var15;
                              var29 = var25;
                              var16 = var22;
                              var22 = var24;
                           }
                        }

                        if (var26 != 0 && var2 != 1) {
                           return -5;
                        }

                        return 0;
                     }

                     ++var14;
                  }
               }

               var8 = this.field_129;
               var29 = this.field_124[var12] + var14;
               var14 = var29;
               var8[var13] = var29;
               ++var13;
               ++var12;
            }
         }
      }
   }

   static int inflate_trees_fixed(int[] var0, int[] var1, int[][] var2, int[][] var3, ZStream var4) {
      var0[0] = 9;
      var1[0] = 5;
      var2[0] = fixed_tl;
      var3[0] = fixed_td;
      return 0;
   }

   private void initWorkArea(int var1) {
      if (this.field_125 == null) {
         this.field_125 = new int[1];
         this.field_128 = new int[var1];
         this.field_124 = new int[16];
         this.field_126 = new int[3];
         this.field_127 = new int[15];
         this.field_129 = new int[16];
      }

      if (this.field_128.length < var1) {
         this.field_128 = new int[var1];
      }

      for(int var2 = 0; var2 < var1; ++var2) {
         this.field_128[var2] = 0;
      }

      for(var1 = 0; var1 < 16; ++var1) {
         this.field_124[var1] = 0;
      }

      for(var1 = 0; var1 < 3; ++var1) {
         this.field_126[var1] = 0;
      }

      System.arraycopy(this.field_124, 0, this.field_127, 0, 15);
      System.arraycopy(this.field_124, 0, this.field_129, 0, 16);
   }

   int inflate_trees_bits(int[] var1, int[] var2, int[] var3, int[] var4, ZStream var5) {
      this.initWorkArea(19);
      int[] var7 = this.field_125;
      var7[0] = 0;
      int var6 = this.huft_build(var1, 0, 19, 19, (int[])null, (int[])null, var3, var2, var4, var7, this.field_128);
      if (var6 == -3) {
         var5.msg = "oversubscribed dynamic bit lengths tree";
         return var6;
      } else {
         if (var6 == -5 || var2[0] == 0) {
            var5.msg = "incomplete dynamic bit lengths tree";
            var6 = -3;
         }

         return var6;
      }
   }

   int inflate_trees_dynamic(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6, int[] var7, int[] var8, ZStream var9) {
      this.initWorkArea(288);
      int[] var11 = this.field_125;
      var11[0] = 0;
      int var10 = this.huft_build(var3, 0, var1, 257, cplens, cplext, var6, var4, var8, var11, this.field_128);
      if (var10 == 0 && var4[0] != 0) {
         this.initWorkArea(288);
         var2 = this.huft_build(var3, var1, var2, 0, cpdist, cpdext, var7, var5, var8, this.field_125, this.field_128);
         if (var2 != 0 || var5[0] == 0 && var1 > 257) {
            if (var2 == -3) {
               var9.msg = "oversubscribed distance tree";
               return var2;
            } else if (var2 == -5) {
               var9.msg = "incomplete distance tree";
               return -3;
            } else {
               var1 = var2;
               if (var2 != -4) {
                  var9.msg = "empty distance tree with lengths";
                  var1 = -3;
               }

               return var1;
            }
         } else {
            return 0;
         }
      } else {
         if (var10 == -3) {
            var9.msg = "oversubscribed literal/length tree";
         } else if (var10 != -4) {
            var9.msg = "incomplete literal/length tree";
            return -3;
         }

         return var10;
      }
   }
}
