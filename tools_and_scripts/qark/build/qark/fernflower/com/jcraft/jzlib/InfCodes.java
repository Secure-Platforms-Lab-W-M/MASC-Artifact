package com.jcraft.jzlib;

final class InfCodes {
   private static final int BADCODE = 9;
   private static final int COPY = 5;
   private static final int DIST = 3;
   private static final int DISTEXT = 4;
   private static final int END = 8;
   private static final int LEN = 1;
   private static final int LENEXT = 2;
   private static final int LIT = 6;
   private static final int START = 0;
   private static final int WASH = 7;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_ERRNO = -1;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   private static final int Z_OK = 0;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   private static final int Z_VERSION_ERROR = -6;
   private static final int[] inflate_mask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535};
   byte dbits;
   int dist;
   int[] dtree;
   int dtree_index;
   int get;
   byte lbits;
   int len;
   int lit;
   int[] ltree;
   int ltree_index;
   int mode;
   int need;
   // $FF: renamed from: s com.jcraft.jzlib.InfBlocks
   private final InfBlocks field_150;
   int[] tree;
   int tree_index = 0;
   // $FF: renamed from: z com.jcraft.jzlib.ZStream
   private final ZStream field_151;

   InfCodes(ZStream var1, InfBlocks var2) {
      this.field_151 = var1;
      this.field_150 = var2;
   }

   void free(ZStream var1) {
   }

   int inflate_fast(int var1, int var2, int[] var3, int var4, int[] var5, int var6, InfBlocks var7, ZStream var8) {
      int var13 = var8.next_in_index;
      int var12 = var8.avail_in;
      int var14 = var7.bitb;
      int var11 = var7.bitk;
      int var16 = var7.write;
      int var10;
      if (var16 < var7.read) {
         var10 = var7.read - var16 - 1;
      } else {
         var10 = var7.end - var16;
      }

      int[] var23 = inflate_mask;
      int var22 = var23[var1];
      int var9 = var23[var2];
      int var15 = var10;
      var2 = var16;

      while(true) {
         while(var11 < 20) {
            --var12;
            var14 |= (var8.next_in[var13] & 255) << var11;
            var11 += 8;
            ++var13;
         }

         int var17 = var14 & var22;
         int var20 = (var4 + var17) * 3;
         int var21 = var3[var20];
         int var19 = var21;
         var10 = var14;
         var16 = var11;
         var1 = var9;
         int var18 = var20;
         if (var21 == 0) {
            var1 = var14 >> var3[var20 + 1];
            var10 = var11 - var3[var20 + 1];
            var7.window[var2] = (byte)var3[var20 + 2];
            var14 = var15 - 1;
            var11 = var9;
            var9 = var2 + 1;
            var2 = var10;
            var10 = var14;
         } else {
            label143:
            while(true) {
               var10 >>= var3[var18 + 1];
               var16 -= var3[var18 + 1];
               if ((var19 & 16) != 0) {
                  var11 = var19 & 15;
                  var18 = var3[var18 + 2] + (var10 & inflate_mask[var11]);
                  var9 = var10 >> var11;

                  for(var10 = var16 - var11; var10 < 15; ++var13) {
                     --var12;
                     var9 |= (var8.next_in[var13] & 255) << var10;
                     var10 += 8;
                  }

                  var16 = var9 & var1;
                  var14 = (var6 + var16) * 3;
                  var11 = var5[var14];

                  while(true) {
                     var9 >>= var5[var14 + 1];
                     var10 -= var5[var14 + 1];
                     if ((var11 & 16) != 0) {
                        var19 = var11 & 15;

                        for(var11 = var13; var10 < var19; ++var11) {
                           --var12;
                           var9 |= (var8.next_in[var11] & 255) << var10;
                           var10 += 8;
                        }

                        var13 = var5[var14 + 2] + (inflate_mask[var19] & var9);
                        var17 = var9 >> var19;
                        var16 = var10 - var19;
                        var14 = var15 - var18;
                        if (var2 >= var13) {
                           var9 = var2 - var13;
                           if (var2 - var9 > 0 && 2 > var2 - var9) {
                              byte[] var27 = var7.window;
                              var10 = var2 + 1;
                              byte[] var24 = var7.window;
                              var13 = var9 + 1;
                              var27[var2] = var24[var9];
                              var27 = var7.window;
                              var2 = var10 + 1;
                              var24 = var7.window;
                              var9 = var13 + 1;
                              var27[var10] = var24[var13];
                              var10 = var18 - 2;
                           } else {
                              System.arraycopy(var7.window, var9, var7.window, var2, 2);
                              var2 += 2;
                              var9 += 2;
                              var10 = var18 - 2;
                           }
                        } else {
                           var10 = var2 - var13;

                           do {
                              var9 = var10 + var7.end;
                              var10 = var9;
                           } while(var9 < 0);

                           var10 = var7.end - var9;
                           if (var18 > var10) {
                              var15 = var18 - var10;
                              if (var2 - var9 > 0 && var10 > var2 - var9) {
                                 while(true) {
                                    var7.window[var2] = var7.window[var9];
                                    --var10;
                                    ++var2;
                                    if (var10 == 0) {
                                       break;
                                    }

                                    ++var9;
                                 }
                              } else {
                                 System.arraycopy(var7.window, var9, var7.window, var2, var10);
                                 var2 += var10;
                                 boolean var25 = false;
                              }

                              byte var26 = 0;
                              var9 = var26;
                              var10 = var15;
                           } else {
                              var10 = var18;
                           }
                        }

                        if (var2 - var9 > 0 && var10 > var2 - var9) {
                           var13 = var10;
                           var10 = var9;

                           while(true) {
                              var7.window[var2] = var7.window[var10];
                              --var13;
                              var9 = var2 + 1;
                              if (var13 == 0) {
                                 var13 = var11;
                                 var2 = var16;
                                 var10 = var14;
                                 var11 = var1;
                                 var1 = var17;
                                 break label143;
                              }

                              ++var10;
                              var2 = var9;
                           }
                        } else {
                           System.arraycopy(var7.window, var9, var7.window, var2, var10);
                           var9 = var2 + var10;
                           var13 = var11;
                           var2 = var16;
                           var10 = var14;
                           var11 = var1;
                           var1 = var17;
                           break label143;
                        }
                     }

                     if ((var11 & 64) != 0) {
                        var8.msg = "invalid distance code";
                        var1 = var8.avail_in - var12;
                        if (var10 >> 3 < var1) {
                           var1 = var10 >> 3;
                        }

                        var4 = var13 - var1;
                        var7.bitb = var9;
                        var7.bitk = var10 - (var1 << 3);
                        var8.avail_in = var12 + var1;
                        var8.total_in += (long)(var4 - var8.next_in_index);
                        var8.next_in_index = var4;
                        var7.write = var2;
                        return -3;
                     }

                     var16 = var16 + var5[var14 + 2] + (var9 & inflate_mask[var11]);
                     var14 = (var6 + var16) * 3;
                     var11 = var5[var14];
                  }
               }

               if ((var19 & 64) != 0) {
                  if ((var19 & 32) != 0) {
                     var1 = var8.avail_in - var12;
                     if (var16 >> 3 < var1) {
                        var1 = var16 >> 3;
                     }

                     var4 = var13 - var1;
                     var7.bitb = var10;
                     var7.bitk = var16 - (var1 << 3);
                     var8.avail_in = var12 + var1;
                     var8.total_in += (long)(var4 - var8.next_in_index);
                     var8.next_in_index = var4;
                     var7.write = var2;
                     return 1;
                  }

                  var8.msg = "invalid literal/length code";
                  var1 = var8.avail_in - var12;
                  if (var16 >> 3 < var1) {
                     var1 = var16 >> 3;
                  }

                  var4 = var13 - var1;
                  var7.bitb = var10;
                  var7.bitk = var16 - (var1 << 3);
                  var8.avail_in = var12 + var1;
                  var8.total_in += (long)(var4 - var8.next_in_index);
                  var8.next_in_index = var4;
                  var7.write = var2;
                  return -3;
               }

               var17 = var17 + var3[var18 + 2] + (inflate_mask[var19] & var10);
               var18 = (var4 + var17) * 3;
               var9 = var3[var18];
               var19 = var9;
               if (var9 == 0) {
                  var14 = var10 >> var3[var18 + 1];
                  var16 -= var3[var18 + 1];
                  var7.window[var2] = (byte)var3[var18 + 2];
                  var10 = var15 - 1;
                  var9 = var2 + 1;
                  var11 = var1;
                  var2 = var16;
                  var1 = var14;
                  break;
               }
            }
         }

         if (var10 < 258 || var12 < 10) {
            var4 = var8.avail_in - var12;
            if (var2 >> 3 < var4) {
               var4 = var2 >> 3;
            }

            var6 = var13 - var4;
            var7.bitb = var1;
            var7.bitk = var2 - (var4 << 3);
            var8.avail_in = var12 + var4;
            var8.total_in += (long)(var6 - var8.next_in_index);
            var8.next_in_index = var6;
            var7.write = var9;
            return 0;
         }

         var16 = var11;
         var14 = var1;
         var11 = var2;
         var2 = var9;
         var15 = var10;
         var9 = var16;
      }
   }

   void init(int var1, int var2, int[] var3, int var4, int[] var5, int var6) {
      this.mode = 0;
      this.lbits = (byte)var1;
      this.dbits = (byte)var2;
      this.ltree = var3;
      this.ltree_index = var4;
      this.dtree = var5;
      this.dtree_index = var6;
      this.tree = null;
   }

   int proc(int var1) {
      int var3 = this.field_151.next_in_index;
      int var6 = this.field_151.avail_in;
      int var5 = this.field_150.bitb;
      int var4 = this.field_150.bitk;
      int var7 = this.field_150.write;
      int var2;
      if (var7 < this.field_150.read) {
         var2 = this.field_150.read - var7 - 1;
      } else {
         var2 = this.field_150.end - var7;
      }

      int var11 = var2;
      boolean var10 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var12 = false;
      var2 = var4;
      var4 = var1;
      var1 = var6;
      var6 = var7;
      var7 = var11;

      ZStream var18;
      label241:
      while(true) {
         int var13;
         int var20;
         int var21;
         int var22;
         int var24;
         int[] var26;
         label239:
         while(true) {
            int var14;
            int var15;
            int var16;
            switch(this.mode) {
            case 0:
               if (var7 >= 258 && var1 >= 10) {
                  this.field_150.bitb = var5;
                  this.field_150.bitk = var2;
                  this.field_151.avail_in = var1;
                  var18 = this.field_151;
                  var18.total_in += (long)(var3 - this.field_151.next_in_index);
                  this.field_151.next_in_index = var3;
                  this.field_150.write = var6;
                  int var17 = this.inflate_fast(this.lbits, this.dbits, this.ltree, this.ltree_index, this.dtree, this.dtree_index, this.field_150, this.field_151);
                  var24 = this.field_151.next_in_index;
                  var15 = this.field_151.avail_in;
                  var14 = this.field_150.bitb;
                  var13 = this.field_150.bitk;
                  var16 = this.field_150.write;
                  if (var16 < this.field_150.read) {
                     var11 = this.field_150.read - var16 - 1;
                  } else {
                     var11 = this.field_150.end - var16;
                  }

                  var1 = var17;
                  var2 = var11;
                  var3 = var16;
                  var4 = var15;
                  var5 = var14;
                  var6 = var13;
                  var7 = var24;
                  if (var17 != 0) {
                     byte var23;
                     if (var17 == 1) {
                        var23 = 7;
                     } else {
                        var23 = 9;
                     }

                     this.mode = var23;
                     var4 = var17;
                     var7 = var11;
                     var6 = var16;
                     var1 = var15;
                     var5 = var14;
                     var2 = var13;
                     var3 = var24;
                     continue;
                  }
               } else {
                  var20 = var4;
                  var4 = var1;
                  var1 = var7;
                  var21 = var6;
                  var7 = var3;
                  var6 = var2;
                  var3 = var21;
                  var2 = var1;
                  var1 = var20;
               }

               this.need = this.lbits;
               this.tree = this.ltree;
               this.tree_index = this.ltree_index;
               this.mode = 1;
               var20 = var7;
               var7 = var6;
               var6 = var4;
               var21 = var3;
               var22 = var2;
               var4 = var1;
               break label239;
            case 1:
               var22 = var7;
               var21 = var6;
               var6 = var1;
               var7 = var2;
               var20 = var3;
               break label239;
            case 2:
               for(var20 = this.get; var2 < var20; ++var3) {
                  if (var1 == 0) {
                     this.field_150.bitb = var5;
                     this.field_150.bitk = var2;
                     this.field_151.avail_in = var1;
                     var18 = this.field_151;
                     var18.total_in += (long)(var3 - this.field_151.next_in_index);
                     this.field_151.next_in_index = var3;
                     this.field_150.write = var6;
                     return this.field_150.inflate_flush(var4);
                  }

                  var4 = 0;
                  --var1;
                  var5 |= (this.field_151.next_in[var3] & 255) << var2;
                  var2 += 8;
               }

               this.len += var5 & inflate_mask[var20];
               var5 >>= var20;
               var2 -= var20;
               this.need = this.dbits;
               this.tree = this.dtree;
               this.tree_index = this.dtree_index;
               this.mode = 3;
            case 3:
               for(var22 = this.need; var2 < var22; ++var3) {
                  if (var1 == 0) {
                     this.field_150.bitb = var5;
                     this.field_150.bitk = var2;
                     this.field_151.avail_in = var1;
                     var18 = this.field_151;
                     var18.total_in += (long)(var3 - this.field_151.next_in_index);
                     this.field_151.next_in_index = var3;
                     this.field_150.write = var6;
                     return this.field_150.inflate_flush(var4);
                  }

                  var4 = 0;
                  --var1;
                  var5 |= (this.field_151.next_in[var3] & 255) << var2;
                  var2 += 8;
               }

               var20 = (this.tree_index + (var5 & inflate_mask[var22])) * 3;
               var26 = this.tree;
               var5 >>= var26[var20 + 1];
               var2 -= var26[var20 + 1];
               var21 = var26[var20];
               if ((var21 & 16) != 0) {
                  this.get = var21 & 15;
                  this.dist = var26[var20 + 2];
                  this.mode = 4;
               } else {
                  if ((var21 & 64) != 0) {
                     this.mode = 9;
                     this.field_151.msg = "invalid distance code";
                     this.field_150.bitb = var5;
                     this.field_150.bitk = var2;
                     this.field_151.avail_in = var1;
                     var18 = this.field_151;
                     var18.total_in += (long)(var3 - this.field_151.next_in_index);
                     this.field_151.next_in_index = var3;
                     this.field_150.write = var6;
                     return this.field_150.inflate_flush(-3);
                  }

                  this.need = var21;
                  this.tree_index = var20 / 3 + var26[var20 + 2];
               }
               continue;
            case 4:
               for(var11 = this.get; var2 < var11; ++var3) {
                  if (var1 == 0) {
                     this.field_150.bitb = var5;
                     this.field_150.bitk = var2;
                     this.field_151.avail_in = var1;
                     var18 = this.field_151;
                     var18.total_in += (long)(var3 - this.field_151.next_in_index);
                     this.field_151.next_in_index = var3;
                     this.field_150.write = var6;
                     return this.field_150.inflate_flush(var4);
                  }

                  var4 = 0;
                  --var1;
                  var5 |= (this.field_151.next_in[var3] & 255) << var2;
                  var2 += 8;
               }

               this.dist += inflate_mask[var11] & var5;
               var13 = var2 - var11;
               this.mode = 5;
               var14 = var5 >> var11;
               var24 = var3;
               var15 = var1;
               break;
            case 5:
               var15 = var1;
               var14 = var5;
               var13 = var2;
               var24 = var3;
               break;
            case 6:
               var24 = var7;
               var11 = var6;
               if (var7 == 0) {
                  var14 = var7;
                  var13 = var6;
                  if (var6 == this.field_150.end) {
                     var14 = var7;
                     var13 = var6;
                     if (this.field_150.read != 0) {
                        var13 = 0;
                        if (this.field_150.read < 0) {
                           var6 = this.field_150.read - 0 - 1;
                        } else {
                           var6 = this.field_150.end - 0;
                        }

                        var14 = var6;
                     }
                  }

                  var24 = var14;
                  var11 = var13;
                  if (var14 == 0) {
                     this.field_150.write = var13;
                     var7 = this.field_150.inflate_flush(var4);
                     var6 = this.field_150.write;
                     if (var6 < this.field_150.read) {
                        var4 = this.field_150.read - var6 - 1;
                     } else {
                        var4 = this.field_150.end - var6;
                     }

                     if (var6 == this.field_150.end && this.field_150.read != 0) {
                        if (this.field_150.read < 0) {
                           var4 = this.field_150.read - 0 - 1;
                        } else {
                           var4 = this.field_150.end - 0;
                        }

                        var6 = 0;
                     }

                     var24 = var4;
                     var11 = var6;
                     if (var4 == 0) {
                        this.field_150.bitb = var5;
                        this.field_150.bitk = var2;
                        this.field_151.avail_in = var1;
                        var18 = this.field_151;
                        var18.total_in += (long)(var3 - this.field_151.next_in_index);
                        this.field_151.next_in_index = var3;
                        this.field_150.write = var6;
                        return this.field_150.inflate_flush(var7);
                     }
                  }
               }

               var4 = 0;
               this.field_150.window[var11] = (byte)this.lit;
               var7 = var24 - 1;
               this.mode = 0;
               var6 = var11 + 1;
               continue;
            case 7:
               var7 = var1;
               var20 = var2;
               var21 = var3;
               if (var2 > 7) {
                  var20 = var2 - 8;
                  var7 = var1 + 1;
                  var21 = var3 - 1;
               }

               this.field_150.write = var6;
               var2 = this.field_150.inflate_flush(var4);
               var6 = this.field_150.write;
               if (var6 < this.field_150.read) {
                  var1 = this.field_150.read - var6 - 1;
               } else {
                  int var10000 = this.field_150.end - var6;
               }

               if (this.field_150.read != this.field_150.write) {
                  this.field_150.bitb = var5;
                  this.field_150.bitk = var20;
                  this.field_151.avail_in = var7;
                  var18 = this.field_151;
                  var18.total_in += (long)(var21 - this.field_151.next_in_index);
                  this.field_151.next_in_index = var21;
                  this.field_150.write = var6;
                  return this.field_150.inflate_flush(var2);
               }

               this.mode = 8;
               var3 = var21;
               var2 = var20;
               var1 = var7;
            case 8:
               break label241;
            case 9:
               this.field_150.bitb = var5;
               this.field_150.bitk = var2;
               this.field_151.avail_in = var1;
               var18 = this.field_151;
               var18.total_in += (long)(var3 - this.field_151.next_in_index);
               this.field_151.next_in_index = var3;
               this.field_150.write = var6;
               return this.field_150.inflate_flush(-3);
            default:
               this.field_150.bitb = var5;
               this.field_150.bitk = var2;
               this.field_151.avail_in = var1;
               var18 = this.field_151;
               var18.total_in += (long)(var3 - this.field_151.next_in_index);
               this.field_151.next_in_index = var3;
               this.field_150.write = var6;
               return this.field_150.inflate_flush(-2);
            }

            for(var1 = var6 - this.dist; var1 < 0; var1 += this.field_150.end) {
            }

            for(var11 = var1; this.len != 0; var4 = var16) {
               var2 = var7;
               var1 = var6;
               var16 = var4;
               if (var7 == 0) {
                  var5 = var7;
                  var3 = var6;
                  if (var6 == this.field_150.end) {
                     var5 = var7;
                     var3 = var6;
                     if (this.field_150.read != 0) {
                        var3 = 0;
                        if (this.field_150.read < 0) {
                           var1 = this.field_150.read - 0 - 1;
                        } else {
                           var1 = this.field_150.end - 0;
                        }

                        var5 = var1;
                     }
                  }

                  var2 = var5;
                  var1 = var3;
                  var16 = var4;
                  if (var5 == 0) {
                     this.field_150.write = var3;
                     var5 = this.field_150.inflate_flush(var4);
                     var4 = this.field_150.write;
                     if (var4 < this.field_150.read) {
                        var1 = this.field_150.read - var4 - 1;
                     } else {
                        var1 = this.field_150.end - var4;
                     }

                     if (var4 == this.field_150.end && this.field_150.read != 0) {
                        if (this.field_150.read < 0) {
                           var1 = this.field_150.read - 0 - 1;
                        } else {
                           var1 = this.field_150.end - 0;
                        }

                        var4 = 0;
                        var3 = var1;
                     } else {
                        var3 = var1;
                     }

                     var2 = var3;
                     var1 = var4;
                     var16 = var5;
                     if (var3 == 0) {
                        this.field_150.bitb = var14;
                        this.field_150.bitk = var13;
                        this.field_151.avail_in = var15;
                        var18 = this.field_151;
                        var18.total_in += (long)(var24 - this.field_151.next_in_index);
                        this.field_151.next_in_index = var24;
                        this.field_150.write = var4;
                        return this.field_150.inflate_flush(var5);
                     }
                  }
               }

               byte[] var25 = this.field_150.window;
               byte[] var19 = this.field_150.window;
               var3 = var11 + 1;
               var25[var1] = var19[var11];
               var7 = var2 - 1;
               if (var3 == this.field_150.end) {
                  var11 = 0;
               } else {
                  var11 = var3;
               }

               --this.len;
               var6 = var1 + 1;
            }

            this.mode = 0;
            var1 = var15;
            var5 = var14;
            var2 = var13;
            var3 = var24;
         }

         var2 = this.need;
         var3 = var20;

         for(var1 = var6; var7 < var2; ++var3) {
            if (var1 == 0) {
               this.field_150.bitb = var5;
               this.field_150.bitk = var7;
               this.field_151.avail_in = var1;
               var18 = this.field_151;
               var18.total_in += (long)(var3 - this.field_151.next_in_index);
               this.field_151.next_in_index = var3;
               this.field_150.write = var21;
               return this.field_150.inflate_flush(var4);
            }

            var4 = 0;
            --var1;
            var5 |= (this.field_151.next_in[var3] & 255) << var7;
            var7 += 8;
         }

         var24 = (this.tree_index + (inflate_mask[var2] & var5)) * 3;
         var26 = this.tree;
         var5 >>>= var26[var24 + 1];
         var13 = var7 - var26[var24 + 1];
         var11 = var26[var24];
         if (var11 == 0) {
            this.lit = var26[var24 + 2];
            this.mode = 6;
         } else if ((var11 & 16) != 0) {
            this.get = var11 & 15;
            this.len = var26[var24 + 2];
            this.mode = 2;
         } else if ((var11 & 64) == 0) {
            this.need = var11;
            this.tree_index = var24 / 3 + var26[var24 + 2];
         } else {
            if ((var11 & 32) == 0) {
               this.mode = 9;
               this.field_151.msg = "invalid literal/length code";
               this.field_150.bitb = var5;
               this.field_150.bitk = var13;
               this.field_151.avail_in = var1;
               var18 = this.field_151;
               var18.total_in += (long)(var3 - this.field_151.next_in_index);
               this.field_151.next_in_index = var3;
               this.field_150.write = var21;
               return this.field_150.inflate_flush(-3);
            }

            this.mode = 7;
         }

         var7 = var22;
         var6 = var21;
         var2 = var13;
      }

      this.field_150.bitb = var5;
      this.field_150.bitk = var2;
      this.field_151.avail_in = var1;
      var18 = this.field_151;
      var18.total_in += (long)(var3 - this.field_151.next_in_index);
      this.field_151.next_in_index = var3;
      this.field_150.write = var6;
      return this.field_150.inflate_flush(1);
   }
}
