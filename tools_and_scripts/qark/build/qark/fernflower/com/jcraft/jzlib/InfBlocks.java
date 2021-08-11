package com.jcraft.jzlib;

final class InfBlocks {
   private static final int BAD = 9;
   private static final int BTREE = 4;
   private static final int CODES = 6;
   private static final int DONE = 8;
   private static final int DRY = 7;
   private static final int DTREE = 5;
   private static final int LENS = 1;
   private static final int MANY = 1440;
   private static final int STORED = 2;
   private static final int TABLE = 3;
   private static final int TYPE = 0;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_ERRNO = -1;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   private static final int Z_OK = 0;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   private static final int Z_VERSION_ERROR = -6;
   static final int[] border = new int[]{16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15};
   private static final int[] inflate_mask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535};
   // $FF: renamed from: bb int[]
   int[] field_99;
   // $FF: renamed from: bd int[]
   int[] field_100;
   int bitb;
   int bitk;
   // $FF: renamed from: bl int[]
   int[] field_101;
   int[] blens;
   private boolean check;
   private final InfCodes codes;
   int end;
   int[] hufts;
   int index;
   private final InfTree inftree;
   int last;
   int left;
   int mode;
   int read;
   int table;
   // $FF: renamed from: tb int[]
   int[] field_102;
   // $FF: renamed from: td int[][]
   int[][] field_103;
   int[] tdi;
   // $FF: renamed from: tl int[][]
   int[][] field_104;
   int[] tli;
   byte[] window;
   int write;
   // $FF: renamed from: z com.jcraft.jzlib.ZStream
   private final ZStream field_105;

   InfBlocks(ZStream var1, int var2) {
      boolean var3 = true;
      this.field_99 = new int[1];
      this.field_102 = new int[1];
      this.field_101 = new int[1];
      this.field_100 = new int[1];
      this.field_104 = new int[1][];
      this.field_103 = new int[1][];
      this.tli = new int[1];
      this.tdi = new int[1];
      this.inftree = new InfTree();
      this.field_105 = var1;
      this.codes = new InfCodes(this.field_105, this);
      this.hufts = new int[4320];
      this.window = new byte[var2];
      this.end = var2;
      if (var1.istate.wrap == 0) {
         var3 = false;
      }

      this.check = var3;
      this.mode = 0;
      this.reset();
   }

   void free() {
      this.reset();
      this.window = null;
      this.hufts = null;
   }

   int inflate_flush(int var1) {
      int var5 = this.field_105.next_out_index;
      int var4 = this.read;
      int var2 = this.write;
      if (var4 > var2) {
         var2 = this.end;
      }

      var2 -= var4;
      int var3 = var2;
      if (var2 > this.field_105.avail_out) {
         var3 = this.field_105.avail_out;
      }

      var2 = var1;
      if (var3 != 0) {
         var2 = var1;
         if (var1 == -5) {
            var2 = 0;
         }
      }

      ZStream var8 = this.field_105;
      var8.avail_out -= var3;
      var8 = this.field_105;
      var8.total_out += (long)var3;
      if (this.check && var3 > 0) {
         this.field_105.adler.update(this.window, var4, var3);
      }

      System.arraycopy(this.window, var4, this.field_105.next_out, var5, var3);
      var5 += var3;
      int var6 = var4 + var3;
      int var7 = this.end;
      var4 = var5;
      var3 = var6;
      var1 = var2;
      if (var6 == var7) {
         if (this.write == var7) {
            this.write = 0;
         }

         var3 = this.write - 0;
         if (var3 > this.field_105.avail_out) {
            var3 = this.field_105.avail_out;
         }

         var1 = var2;
         if (var3 != 0) {
            var1 = var2;
            if (var2 == -5) {
               var1 = 0;
            }
         }

         var8 = this.field_105;
         var8.avail_out -= var3;
         var8 = this.field_105;
         var8.total_out += (long)var3;
         if (this.check && var3 > 0) {
            this.field_105.adler.update(this.window, 0, var3);
         }

         System.arraycopy(this.window, 0, this.field_105.next_out, var5, var3);
         var4 = var5 + var3;
         var3 += 0;
      }

      this.field_105.next_out_index = var4;
      this.read = var3;
      return var1;
   }

   int proc(int var1) {
      int var9 = this.field_105.next_in_index;
      int var5 = this.field_105.avail_in;
      int var4 = this.bitb;
      int var3 = this.bitk;
      int var7 = this.write;
      int var2 = this.read;
      if (var7 < var2) {
         var2 = var2 - var7 - 1;
      } else {
         var2 = this.end - var7;
      }

      boolean var8 = false;
      int var10 = var1;
      int var6 = var2;
      var1 = var3;
      var2 = var4;
      var3 = var5;
      var4 = var9;
      var5 = var10;

      while(true) {
         while(true) {
            ZStream var14;
            label339:
            while(true) {
               label336:
               while(true) {
                  label334: {
                     int var11;
                     int var12;
                     int[] var15;
                     int var17;
                     byte var19;
                     int[] var22;
                     label387: {
                        var9 = var5;
                        var10 = var4;
                        var11 = var3;
                        int var13 = var2;
                        var12 = var1;
                        switch(this.mode) {
                        case 0:
                           for(var9 = var3; var1 < 3; ++var4) {
                              if (var9 == 0) {
                                 this.bitb = var2;
                                 this.bitk = var1;
                                 this.field_105.avail_in = var9;
                                 var14 = this.field_105;
                                 var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                 this.field_105.next_in_index = var4;
                                 this.write = var7;
                                 return this.inflate_flush(var5);
                              }

                              var5 = 0;
                              --var9;
                              var2 |= (this.field_105.next_in[var4] & 255) << var1;
                              var1 += 8;
                           }

                           var3 = var2 & 7;
                           this.last = var3 & 1;
                           var17 = var3 >>> 1;
                           if (var17 != 0) {
                              if (var17 != 1) {
                                 if (var17 != 2) {
                                    if (var17 == 3) {
                                       this.mode = 9;
                                       this.field_105.msg = "invalid block type";
                                       this.bitb = var2 >>> 3;
                                       this.bitk = var1 - 3;
                                       this.field_105.avail_in = var9;
                                       var14 = this.field_105;
                                       var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                       this.field_105.next_in_index = var4;
                                       this.write = var7;
                                       return this.inflate_flush(-3);
                                    }
                                 } else {
                                    var2 >>>= 3;
                                    var1 -= 3;
                                    this.mode = 3;
                                 }
                              } else {
                                 InfTree.inflate_trees_fixed(this.field_101, this.field_100, this.field_104, this.field_103, this.field_105);
                                 this.codes.init(this.field_101[0], this.field_100[0], this.field_104[0], 0, this.field_103[0], 0);
                                 var2 >>>= 3;
                                 var1 -= 3;
                                 this.mode = 6;
                              }
                           } else {
                              var1 -= 3;
                              var17 = var1 & 7;
                              var2 = var2 >>> 3 >>> var17;
                              var1 -= var17;
                              this.mode = 1;
                           }

                           var3 = var9;
                           continue;
                        case 1:
                           while(var1 < 32) {
                              if (var3 == 0) {
                                 this.bitb = var2;
                                 this.bitk = var1;
                                 this.field_105.avail_in = var3;
                                 var14 = this.field_105;
                                 var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                 this.field_105.next_in_index = var4;
                                 this.write = var7;
                                 return this.inflate_flush(var5);
                              }

                              var5 = 0;
                              --var3;
                              var2 |= (this.field_105.next_in[var4] & 255) << var1;
                              var1 += 8;
                              ++var4;
                           }

                           if ((var2 >>> 16 & '\uffff') != (var2 & '\uffff')) {
                              this.mode = 9;
                              this.field_105.msg = "invalid stored block lengths";
                              this.bitb = var2;
                              this.bitk = var1;
                              this.field_105.avail_in = var3;
                              var14 = this.field_105;
                              var14.total_in += (long)(var4 - this.field_105.next_in_index);
                              this.field_105.next_in_index = var4;
                              this.write = var7;
                              return this.inflate_flush(-3);
                           }

                           var1 = var2 & '\uffff';
                           this.left = var1;
                           var19 = 0;
                           var2 = 0;
                           byte var20;
                           if (var1 != 0) {
                              var20 = 2;
                           } else if (this.last != 0) {
                              var20 = 7;
                           } else {
                              var20 = 0;
                           }

                           this.mode = var20;
                           var1 = var19;
                           continue;
                        case 2:
                           if (var3 == 0) {
                              this.bitb = var2;
                              this.bitk = var1;
                              this.field_105.avail_in = var3;
                              var14 = this.field_105;
                              var14.total_in += (long)(var4 - this.field_105.next_in_index);
                              this.field_105.next_in_index = var4;
                              this.write = var7;
                              return this.inflate_flush(var5);
                           }

                           if (var6 == 0) {
                              label224: {
                                 var9 = this.end;
                                 if (var7 == var9) {
                                    var10 = this.read;
                                    if (var10 != 0) {
                                       var17 = 0;
                                       if (var10 < 0) {
                                          var6 = var10 - 0 - 1;
                                       } else {
                                          var6 = var9 - 0;
                                       }

                                       var9 = var6;
                                       break label224;
                                    }
                                 }

                                 var9 = var6;
                                 var17 = var7;
                              }

                              var7 = var17;
                              var6 = var9;
                              if (var9 == 0) {
                                 this.write = var17;
                                 var9 = this.inflate_flush(var5);
                                 var7 = this.write;
                                 var5 = this.read;
                                 if (var7 < var5) {
                                    var5 = var5 - var7 - 1;
                                 } else {
                                    var5 = this.end - var7;
                                 }

                                 var6 = var5;
                                 var10 = this.end;
                                 var17 = var7;
                                 var5 = var5;
                                 if (var7 == var10) {
                                    var11 = this.read;
                                    var17 = var7;
                                    var5 = var6;
                                    if (var11 != 0) {
                                       var17 = 0;
                                       if (var11 < 0) {
                                          var5 = var11 - 0 - 1;
                                       } else {
                                          var5 = var10 - 0;
                                       }
                                    }
                                 }

                                 var7 = var17;
                                 var6 = var5;
                                 if (var5 == 0) {
                                    this.bitb = var2;
                                    this.bitk = var1;
                                    this.field_105.avail_in = var3;
                                    var14 = this.field_105;
                                    var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                    this.field_105.next_in_index = var4;
                                    this.write = var17;
                                    return this.inflate_flush(var9);
                                 }
                              }
                           }

                           var19 = 0;
                           var17 = this.left;
                           var5 = var17;
                           if (var17 > var3) {
                              var5 = var3;
                           }

                           var17 = var5;
                           if (var5 > var6) {
                              var17 = var6;
                           }

                           System.arraycopy(this.field_105.next_in, var4, this.window, var7, var17);
                           var4 += var17;
                           var10 = var3 - var17;
                           var7 += var17;
                           var6 -= var17;
                           var3 = this.left - var17;
                           this.left = var3;
                           if (var3 == 0) {
                              byte var18;
                              if (this.last != 0) {
                                 var18 = 7;
                              } else {
                                 var18 = 0;
                              }

                              this.mode = var18;
                           }

                           var5 = var19;
                           var3 = var10;
                           continue;
                        case 3:
                           while(true) {
                              if (var12 >= 14) {
                                 var1 = var13 & 16383;
                                 this.table = var1;
                                 if ((var1 & 31) > 29 || (var1 >> 5 & 31) > 29) {
                                    this.mode = 9;
                                    this.field_105.msg = "too many length or distance symbols";
                                    this.bitb = var13;
                                    this.bitk = var12;
                                    this.field_105.avail_in = var11;
                                    var14 = this.field_105;
                                    var14.total_in += (long)(var10 - this.field_105.next_in_index);
                                    this.field_105.next_in_index = var10;
                                    this.write = var7;
                                    return this.inflate_flush(-3);
                                 }

                                 var2 = (var1 & 31) + 258 + (var1 >> 5 & 31);
                                 var22 = this.blens;
                                 if (var22 != null && var22.length >= var2) {
                                    for(var1 = 0; var1 < var2; ++var1) {
                                       this.blens[var1] = 0;
                                    }
                                 } else {
                                    this.blens = new int[var2];
                                 }

                                 var2 = var13 >>> 14;
                                 var1 = var12 - 14;
                                 this.index = 0;
                                 this.mode = 4;
                                 var3 = var11;
                                 var4 = var10;
                                 var5 = var9;
                                 break;
                              }

                              if (var11 == 0) {
                                 this.bitb = var13;
                                 this.bitk = var12;
                                 this.field_105.avail_in = var11;
                                 var14 = this.field_105;
                                 var14.total_in += (long)(var10 - this.field_105.next_in_index);
                                 this.field_105.next_in_index = var10;
                                 this.write = var7;
                                 return this.inflate_flush(var9);
                              }

                              var9 = 0;
                              --var11;
                              var13 |= (this.field_105.next_in[var10] & 255) << var12;
                              var12 += 8;
                              ++var10;
                           }
                        case 4:
                           break;
                        case 5:
                           break label387;
                        case 6:
                           break label334;
                        case 7:
                           break label336;
                        case 8:
                           break label339;
                        case 9:
                           this.bitb = var2;
                           this.bitk = var1;
                           this.field_105.avail_in = var3;
                           var14 = this.field_105;
                           var14.total_in += (long)(var4 - this.field_105.next_in_index);
                           this.field_105.next_in_index = var4;
                           this.write = var7;
                           return this.inflate_flush(-3);
                        default:
                           this.bitb = var2;
                           this.bitk = var1;
                           this.field_105.avail_in = var3;
                           var14 = this.field_105;
                           var14.total_in += (long)(var4 - this.field_105.next_in_index);
                           this.field_105.next_in_index = var4;
                           this.write = var7;
                           return this.inflate_flush(-2);
                        }

                        label293:
                        while(true) {
                           if (this.index >= (this.table >>> 10) + 4) {
                              while(true) {
                                 var17 = this.index;
                                 if (var17 >= 19) {
                                    var22 = this.field_99;
                                    var22[0] = 7;
                                    var17 = this.inftree.inflate_trees_bits(this.blens, var22, this.field_102, this.hufts, this.field_105);
                                    if (var17 != 0) {
                                       if (var17 == -3) {
                                          this.blens = null;
                                          this.mode = 9;
                                       }

                                       this.bitb = var2;
                                       this.bitk = var1;
                                       this.field_105.avail_in = var3;
                                       var14 = this.field_105;
                                       var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                       this.field_105.next_in_index = var4;
                                       this.write = var7;
                                       return this.inflate_flush(var17);
                                    }

                                    this.index = 0;
                                    this.mode = 5;
                                    break label293;
                                 }

                                 var22 = this.blens;
                                 var15 = border;
                                 this.index = var17 + 1;
                                 var22[var15[var17]] = 0;
                              }
                           }

                           while(var1 < 3) {
                              if (var3 == 0) {
                                 this.bitb = var2;
                                 this.bitk = var1;
                                 this.field_105.avail_in = var3;
                                 var14 = this.field_105;
                                 var14.total_in += (long)(var4 - this.field_105.next_in_index);
                                 this.field_105.next_in_index = var4;
                                 this.write = var7;
                                 return this.inflate_flush(var5);
                              }

                              var5 = 0;
                              --var3;
                              var2 |= (this.field_105.next_in[var4] & 255) << var1;
                              var1 += 8;
                              ++var4;
                           }

                           var22 = this.blens;
                           var15 = border;
                           var17 = this.index++;
                           var22[var15[var17]] = var2 & 7;
                           var2 >>>= 3;
                           var1 -= 3;
                        }
                     }

                     while(true) {
                        var17 = this.table;
                        if (this.index >= (var17 & 31) + 258 + (var17 >> 5 & 31)) {
                           this.field_102[0] = -1;
                           var22 = this.field_101;
                           var22[0] = 9;
                           var15 = this.field_100;
                           var15[0] = 6;
                           var6 = this.table;
                           var17 = this.inftree.inflate_trees_dynamic((var6 & 31) + 257, (var6 >> 5 & 31) + 1, this.blens, var22, var15, this.tli, this.tdi, this.hufts, this.field_105);
                           if (var17 != 0) {
                              if (var17 == -3) {
                                 this.blens = null;
                                 this.mode = 9;
                              }

                              this.bitb = var2;
                              this.bitk = var1;
                              this.field_105.avail_in = var3;
                              var14 = this.field_105;
                              var14.total_in += (long)(var4 - this.field_105.next_in_index);
                              this.field_105.next_in_index = var4;
                              this.write = var7;
                              return this.inflate_flush(var17);
                           }

                           InfCodes var23 = this.codes;
                           var6 = this.field_101[0];
                           var9 = this.field_100[0];
                           var15 = this.hufts;
                           var23.init(var6, var9, var15, this.tli[0], var15, this.tdi[0]);
                           this.mode = 6;
                           break;
                        }

                        var10 = this.field_99[0];
                        var17 = var4;
                        var9 = var5;
                        var4 = var1;
                        var5 = var2;
                        var1 = var3;
                        var2 = var17;

                        for(var3 = var9; var4 < var10; ++var2) {
                           if (var1 == 0) {
                              this.bitb = var5;
                              this.bitk = var4;
                              this.field_105.avail_in = var1;
                              var14 = this.field_105;
                              var14.total_in += (long)(var2 - this.field_105.next_in_index);
                              this.field_105.next_in_index = var2;
                              this.write = var7;
                              return this.inflate_flush(var3);
                           }

                           var3 = 0;
                           --var1;
                           var5 |= (this.field_105.next_in[var2] & 255) << var4;
                           var4 += 8;
                        }

                        var22 = this.field_102;
                        var17 = var22[0];
                        var15 = this.hufts;
                        var17 = var22[0];
                        int[] var16 = inflate_mask;
                        var10 = var15[(var17 + (var5 & var16[var10])) * 3 + 1];
                        var12 = var15[(var22[0] + (var16[var10] & var5)) * 3 + 2];
                        if (var12 < 16) {
                           var22 = this.blens;
                           var17 = this.index++;
                           var22[var17] = var12;
                           var17 = var5 >>> var10;
                           var9 = var4 - var10;
                           var5 = var3;
                           var4 = var2;
                           var3 = var1;
                           var2 = var17;
                           var1 = var9;
                        } else {
                           if (var12 == 18) {
                              var17 = 7;
                           } else {
                              var17 = var12 - 14;
                           }

                           if (var12 == 18) {
                              var19 = 11;
                           } else {
                              var19 = 3;
                           }

                           while(var4 < var10 + var17) {
                              if (var1 == 0) {
                                 this.bitb = var5;
                                 this.bitk = var4;
                                 this.field_105.avail_in = var1;
                                 var14 = this.field_105;
                                 var14.total_in += (long)(var2 - this.field_105.next_in_index);
                                 this.field_105.next_in_index = var2;
                                 this.write = var7;
                                 return this.inflate_flush(var3);
                              }

                              var3 = 0;
                              --var1;
                              var5 |= (this.field_105.next_in[var2] & 255) << var4;
                              var4 += 8;
                              ++var2;
                           }

                           var5 >>>= var10;
                           var11 = var19 + (inflate_mask[var17] & var5);
                           var9 = var5 >>> var17;
                           var10 = var4 - var10 - var17;
                           var5 = this.index;
                           var4 = this.table;
                           if (var5 + var11 > (var4 & 31) + 258 + (var4 >> 5 & 31) || var12 == 16 && var5 < 1) {
                              this.blens = null;
                              this.mode = 9;
                              this.field_105.msg = "invalid bit length repeat";
                              this.bitb = var9;
                              this.bitk = var10;
                              this.field_105.avail_in = var1;
                              var14 = this.field_105;
                              var14.total_in += (long)(var2 - this.field_105.next_in_index);
                              this.field_105.next_in_index = var2;
                              this.write = var7;
                              return this.inflate_flush(-3);
                           }

                           if (var12 == 16) {
                              var4 = this.blens[var5 - 1];
                              var17 = var11;
                           } else {
                              var4 = 0;
                              var17 = var11;
                           }

                           while(true) {
                              var22 = this.blens;
                              var11 = var5 + 1;
                              var22[var5] = var4;
                              --var17;
                              if (var17 == 0) {
                                 this.index = var11;
                                 var5 = var3;
                                 var4 = var2;
                                 var3 = var1;
                                 var2 = var9;
                                 var1 = var10;
                                 break;
                              }

                              var5 = var11;
                           }
                        }
                     }
                  }

                  this.bitb = var2;
                  this.bitk = var1;
                  this.field_105.avail_in = var3;
                  var14 = this.field_105;
                  var14.total_in += (long)(var4 - this.field_105.next_in_index);
                  this.field_105.next_in_index = var4;
                  this.write = var7;
                  var1 = this.codes.proc(var5);
                  if (var1 != 1) {
                     return this.inflate_flush(var1);
                  }

                  var5 = 0;
                  byte var21 = 0;
                  this.codes.free(this.field_105);
                  var4 = this.field_105.next_in_index;
                  var3 = this.field_105.avail_in;
                  var2 = this.bitb;
                  var6 = this.bitk;
                  var7 = this.write;
                  var1 = this.read;
                  if (var7 < var1) {
                     var1 = var1 - var7 - 1;
                  } else {
                     var1 = this.end - var7;
                  }

                  var9 = var1;
                  if (this.last != 0) {
                     this.mode = 7;
                     var1 = var6;
                     break;
                  }

                  this.mode = 0;
                  var5 = var21;
                  var1 = var6;
                  var6 = var9;
               }

               this.write = var7;
               var6 = this.inflate_flush(var5);
               var7 = this.write;
               var5 = this.read;
               if (var7 < var5) {
                  var5 = var5 - var7 - 1;
               } else {
                  int var10000 = this.end - var7;
               }

               if (this.read != this.write) {
                  this.bitb = var2;
                  this.bitk = var1;
                  this.field_105.avail_in = var3;
                  var14 = this.field_105;
                  var14.total_in += (long)(var4 - this.field_105.next_in_index);
                  this.field_105.next_in_index = var4;
                  this.write = var7;
                  return this.inflate_flush(var6);
               }

               this.mode = 8;
               break;
            }

            this.bitb = var2;
            this.bitk = var1;
            this.field_105.avail_in = var3;
            var14 = this.field_105;
            var14.total_in += (long)(var4 - this.field_105.next_in_index);
            this.field_105.next_in_index = var4;
            this.write = var7;
            return this.inflate_flush(1);
         }
      }
   }

   void reset() {
      int var1 = this.mode;
      if (this.mode == 6) {
         this.codes.free(this.field_105);
      }

      this.mode = 0;
      this.bitk = 0;
      this.bitb = 0;
      this.write = 0;
      this.read = 0;
      if (this.check) {
         this.field_105.adler.reset();
      }

   }

   void set_dictionary(byte[] var1, int var2, int var3) {
      System.arraycopy(var1, var2, this.window, 0, var3);
      this.write = var3;
      this.read = var3;
   }

   int sync_point() {
      return this.mode == 1 ? 1 : 0;
   }
}
