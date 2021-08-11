package com.jcraft.jzlib;

public final class Deflate implements Cloneable {
   private static final int BL_CODES = 19;
   private static final int BUSY_STATE = 113;
   private static final int BlockDone = 1;
   private static final int Buf_size = 16;
   private static final int DEF_MEM_LEVEL = 8;
   private static final int DYN_TREES = 2;
   private static final int D_CODES = 30;
   private static final int END_BLOCK = 256;
   private static final int FAST = 1;
   private static final int FINISH_STATE = 666;
   private static final int FinishDone = 3;
   private static final int FinishStarted = 2;
   private static final int HEAP_SIZE = 573;
   private static final int INIT_STATE = 42;
   private static final int LENGTH_CODES = 29;
   private static final int LITERALS = 256;
   private static final int L_CODES = 286;
   private static final int MAX_BITS = 15;
   private static final int MAX_MATCH = 258;
   private static final int MAX_MEM_LEVEL = 9;
   private static final int MAX_WBITS = 15;
   private static final int MIN_LOOKAHEAD = 262;
   private static final int MIN_MATCH = 3;
   private static final int NeedMore = 0;
   private static final int PRESET_DICT = 32;
   private static final int REPZ_11_138 = 18;
   private static final int REPZ_3_10 = 17;
   private static final int REP_3_6 = 16;
   private static final int SLOW = 2;
   private static final int STATIC_TREES = 1;
   private static final int STORED = 0;
   private static final int STORED_BLOCK = 0;
   private static final int Z_ASCII = 1;
   private static final int Z_BINARY = 0;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_DEFAULT_COMPRESSION = -1;
   private static final int Z_DEFAULT_STRATEGY = 0;
   private static final int Z_DEFLATED = 8;
   private static final int Z_ERRNO = -1;
   private static final int Z_FILTERED = 1;
   private static final int Z_FINISH = 4;
   private static final int Z_FULL_FLUSH = 3;
   private static final int Z_HUFFMAN_ONLY = 2;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   private static final int Z_NO_FLUSH = 0;
   private static final int Z_OK = 0;
   private static final int Z_PARTIAL_FLUSH = 1;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   private static final int Z_SYNC_FLUSH = 2;
   private static final int Z_UNKNOWN = 2;
   private static final int Z_VERSION_ERROR = -6;
   private static final Deflate.Config[] config_table;
   private static final String[] z_errmsg;
   short bi_buf;
   int bi_valid;
   short[] bl_count = new short[16];
   Tree bl_desc = new Tree();
   short[] bl_tree;
   int block_start;
   int d_buf;
   Tree d_desc = new Tree();
   byte data_type;
   byte[] depth = new byte[573];
   short[] dyn_dtree;
   short[] dyn_ltree;
   GZIPHeader gheader = null;
   int good_match;
   int hash_bits;
   int hash_mask;
   int hash_shift;
   int hash_size;
   short[] head;
   int[] heap = new int[573];
   int heap_len;
   int heap_max;
   int ins_h;
   byte[] l_buf;
   Tree l_desc = new Tree();
   int last_eob_len;
   int last_flush;
   int last_lit;
   int level;
   int lit_bufsize;
   int lookahead;
   int match_available;
   int match_length;
   int match_start;
   int matches;
   int max_chain_length;
   int max_lazy_match;
   byte method;
   short[] next_code = new short[16];
   int nice_match;
   int opt_len;
   int pending;
   byte[] pending_buf;
   int pending_buf_size;
   int pending_out;
   short[] prev;
   int prev_length;
   int prev_match;
   int static_len;
   int status;
   int strategy;
   ZStream strm;
   int strstart;
   int w_bits;
   int w_mask;
   int w_size;
   byte[] window;
   int window_size;
   int wrap = 1;

   static {
      Deflate.Config[] var0 = new Deflate.Config[10];
      config_table = var0;
      var0[0] = new Deflate.Config(0, 0, 0, 0, 0);
      config_table[1] = new Deflate.Config(4, 4, 8, 4, 1);
      config_table[2] = new Deflate.Config(4, 5, 16, 8, 1);
      config_table[3] = new Deflate.Config(4, 6, 32, 32, 1);
      config_table[4] = new Deflate.Config(4, 4, 16, 16, 2);
      config_table[5] = new Deflate.Config(8, 16, 32, 32, 2);
      config_table[6] = new Deflate.Config(8, 16, 128, 128, 2);
      config_table[7] = new Deflate.Config(8, 32, 128, 256, 2);
      config_table[8] = new Deflate.Config(32, 128, 258, 1024, 2);
      config_table[9] = new Deflate.Config(32, 258, 258, 4096, 2);
      z_errmsg = new String[]{"need dictionary", "stream end", "", "file error", "stream error", "data error", "insufficient memory", "buffer error", "incompatible version", ""};
   }

   Deflate(ZStream var1) {
      this.strm = var1;
      this.dyn_ltree = new short[1146];
      this.dyn_dtree = new short[122];
      this.bl_tree = new short[78];
   }

   static int deflateCopy(ZStream var0, ZStream var1) {
      if (var1.dstate == null) {
         return -2;
      } else {
         if (var1.next_in != null) {
            var0.next_in = new byte[var1.next_in.length];
            System.arraycopy(var1.next_in, 0, var0.next_in, 0, var1.next_in.length);
         }

         var0.next_in_index = var1.next_in_index;
         var0.avail_in = var1.avail_in;
         var0.total_in = var1.total_in;
         if (var1.next_out != null) {
            var0.next_out = new byte[var1.next_out.length];
            System.arraycopy(var1.next_out, 0, var0.next_out, 0, var1.next_out.length);
         }

         var0.next_out_index = var1.next_out_index;
         var0.avail_out = var1.avail_out;
         var0.total_out = var1.total_out;
         var0.msg = var1.msg;
         var0.data_type = var1.data_type;
         var0.adler = var1.adler.copy();

         try {
            var0.dstate = (Deflate)var1.dstate.clone();
            var0.dstate.strm = var0;
            return 0;
         } catch (CloneNotSupportedException var2) {
            return 0;
         }
      }
   }

   private int deflateInit(int var1, int var2, int var3, int var4, int var5) {
      byte var6 = 1;
      this.strm.msg = null;
      int var7 = var1;
      if (var1 == -1) {
         var7 = 6;
      }

      byte var8;
      int var9;
      if (var3 < 0) {
         var8 = 0;
         var9 = -var3;
      } else {
         var8 = var6;
         var9 = var3;
         if (var3 > 15) {
            var8 = 2;
            var9 = var3 - 16;
            this.strm.adler = new CRC32();
         }
      }

      if (var4 >= 1 && var4 <= 9 && var2 == 8 && var9 >= 9 && var9 <= 15 && var7 >= 0 && var7 <= 9 && var5 >= 0 && var5 <= 2) {
         this.strm.dstate = this;
         this.wrap = var8;
         this.w_bits = var9;
         var1 = 1 << var9;
         this.w_size = var1;
         this.w_mask = var1 - 1;
         var3 = var4 + 7;
         this.hash_bits = var3;
         var9 = 1 << var3;
         this.hash_size = var9;
         this.hash_mask = var9 - 1;
         this.hash_shift = (var3 + 3 - 1) / 3;
         this.window = new byte[var1 * 2];
         this.prev = new short[var1];
         this.head = new short[var9];
         var1 = 1 << var4 + 6;
         this.lit_bufsize = var1;
         this.pending_buf = new byte[var1 * 3];
         this.pending_buf_size = var1 * 3;
         this.d_buf = var1;
         this.l_buf = new byte[var1];
         this.level = var7;
         this.strategy = var5;
         this.method = (byte)var2;
         return this.deflateReset();
      } else {
         return -2;
      }
   }

   private byte[] dup(byte[] var1) {
      byte[] var2 = new byte[var1.length];
      System.arraycopy(var1, 0, var2, 0, var2.length);
      return var2;
   }

   private int[] dup(int[] var1) {
      int[] var2 = new int[var1.length];
      System.arraycopy(var1, 0, var2, 0, var2.length);
      return var2;
   }

   private short[] dup(short[] var1) {
      short[] var2 = new short[var1.length];
      System.arraycopy(var1, 0, var2, 0, var2.length);
      return var2;
   }

   static boolean smaller(short[] var0, int var1, int var2, byte[] var3) {
      short var4 = var0[var1 * 2];
      short var5 = var0[var2 * 2];
      return var4 < var5 || var4 == var5 && var3[var1] <= var3[var2];
   }

   void _tr_align() {
      this.send_bits(2, 3);
      this.send_code(256, StaticTree.static_ltree);
      this.bi_flush();
      if (this.last_eob_len + 1 + 10 - this.bi_valid < 9) {
         this.send_bits(2, 3);
         this.send_code(256, StaticTree.static_ltree);
         this.bi_flush();
      }

      this.last_eob_len = 7;
   }

   void _tr_flush_block(int var1, int var2, boolean var3) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge Z and I\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.useAs(TypeTransformer.java:868)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:597)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   void _tr_stored_block(int var1, int var2, boolean var3) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   boolean _tr_tally(int var1, int var2) {
      byte[] var5 = this.pending_buf;
      int var3 = this.d_buf;
      int var4 = this.last_lit;
      var5[var4 * 2 + var3] = (byte)(var1 >>> 8);
      var5[var3 + var4 * 2 + 1] = (byte)var1;
      this.l_buf[var4] = (byte)var2;
      this.last_lit = var4 + 1;
      short[] var6;
      if (var1 == 0) {
         var6 = this.dyn_ltree;
         var1 = var2 * 2;
         ++var6[var1];
      } else {
         ++this.matches;
         var6 = this.dyn_ltree;
         var2 = (Tree._length_code[var2] + 256 + 1) * 2;
         ++var6[var2];
         var6 = this.dyn_dtree;
         var1 = Tree.d_code(var1 - 1) * 2;
         ++var6[var1];
      }

      var1 = this.last_lit;
      if ((var1 & 8191) == 0 && this.level > 2) {
         var2 = var1 * 8;
         var3 = this.strstart;
         var4 = this.block_start;

         for(var1 = 0; var1 < 30; ++var1) {
            var2 = (int)((long)var2 + (long)this.dyn_dtree[var1 * 2] * ((long)Tree.extra_dbits[var1] + 5L));
         }

         if (this.matches < this.last_lit / 2 && var2 >>> 3 < (var3 - var4) / 2) {
            return true;
         }
      }

      return this.last_lit == this.lit_bufsize - 1;
   }

   void bi_flush() {
      int var1 = this.bi_valid;
      if (var1 == 16) {
         this.put_short(this.bi_buf);
         this.bi_buf = 0;
         this.bi_valid = 0;
      } else {
         if (var1 >= 8) {
            this.put_byte((byte)this.bi_buf);
            this.bi_buf = (short)(this.bi_buf >>> 8);
            this.bi_valid -= 8;
         }

      }
   }

   void bi_windup() {
      int var1 = this.bi_valid;
      if (var1 > 8) {
         this.put_short(this.bi_buf);
      } else if (var1 > 0) {
         this.put_byte((byte)this.bi_buf);
      }

      this.bi_buf = 0;
      this.bi_valid = 0;
   }

   int build_bl_tree() {
      this.scan_tree(this.dyn_ltree, this.l_desc.max_code);
      this.scan_tree(this.dyn_dtree, this.d_desc.max_code);
      this.bl_desc.build_tree(this);

      int var1;
      for(var1 = 18; var1 >= 3 && this.bl_tree[Tree.bl_order[var1] * 2 + 1] == 0; --var1) {
      }

      this.opt_len += (var1 + 1) * 3 + 5 + 5 + 4;
      return var1;
   }

   public Object clone() throws CloneNotSupportedException {
      Deflate var1 = (Deflate)super.clone();
      var1.pending_buf = this.dup(var1.pending_buf);
      var1.d_buf = var1.d_buf;
      var1.l_buf = this.dup(var1.l_buf);
      var1.window = this.dup(var1.window);
      var1.prev = this.dup(var1.prev);
      var1.head = this.dup(var1.head);
      var1.dyn_ltree = this.dup(var1.dyn_ltree);
      var1.dyn_dtree = this.dup(var1.dyn_dtree);
      var1.bl_tree = this.dup(var1.bl_tree);
      var1.bl_count = this.dup(var1.bl_count);
      var1.next_code = this.dup(var1.next_code);
      var1.heap = this.dup(var1.heap);
      var1.depth = this.dup(var1.depth);
      var1.l_desc.dyn_tree = var1.dyn_ltree;
      var1.d_desc.dyn_tree = var1.dyn_dtree;
      var1.bl_desc.dyn_tree = var1.bl_tree;
      GZIPHeader var2 = var1.gheader;
      if (var2 != null) {
         var1.gheader = (GZIPHeader)var2.clone();
      }

      return var1;
   }

   void compress_block(short[] var1, short[] var2) {
      int var3 = 0;
      int var4;
      if (this.last_lit != 0) {
         do {
            byte[] var8 = this.pending_buf;
            var4 = this.d_buf;
            byte var5 = var8[var3 * 2 + var4];
            int var10 = var8[var4 + var3 * 2 + 1] & 255 | var5 << 8 & '\uff00';
            int var6 = this.l_buf[var3] & 255;
            var4 = var3 + 1;
            if (var10 == 0) {
               this.send_code(var6, var1);
            } else {
               byte var9 = Tree._length_code[var6];
               this.send_code(var9 + 256 + 1, var1);
               int var7 = Tree.extra_lbits[var9];
               if (var7 != 0) {
                  this.send_bits(var6 - Tree.base_length[var9], var7);
               }

               var3 = var10 - 1;
               var10 = Tree.d_code(var3);
               this.send_code(var10, var2);
               var6 = Tree.extra_dbits[var10];
               if (var6 != 0) {
                  this.send_bits(var3 - Tree.base_dist[var10], var6);
               }
            }

            var3 = var4;
         } while(var4 < this.last_lit);
      }

      this.send_code(256, var1);
      this.last_eob_len = var1[513];
   }

   void copy_block(int var1, int var2, boolean var3) {
      this.bi_windup();
      this.last_eob_len = 8;
      if (var3) {
         this.put_short((short)var2);
         this.put_short((short)var2);
      }

      this.put_byte(this.window, var1, var2);
   }

   int deflate(int var1) {
      if (var1 <= 4) {
         if (var1 < 0) {
            return -2;
         } else if (this.strm.next_out != null && (this.strm.next_in != null || this.strm.avail_in == 0) && (this.status != 666 || var1 == 4)) {
            if (this.strm.avail_out == 0) {
               this.strm.msg = z_errmsg[7];
               return -5;
            } else {
               int var4 = this.last_flush;
               this.last_flush = var1;
               int var2;
               int var3;
               long var6;
               if (this.status == 42) {
                  if (this.wrap == 2) {
                     this.getGZIPHeader().put(this);
                     this.status = 113;
                     this.strm.adler.reset();
                  } else {
                     int var5 = this.w_bits;
                     var3 = (this.level - 1 & 255) >> 1;
                     var2 = var3;
                     if (var3 > 3) {
                        var2 = 3;
                     }

                     var3 = (var5 - 8 << 4) + 8 << 8 | var2 << 6;
                     var2 = var3;
                     if (this.strstart != 0) {
                        var2 = var3 | 32;
                     }

                     this.status = 113;
                     this.putShortMSB(var2 + (31 - var2 % 31));
                     if (this.strstart != 0) {
                        var6 = this.strm.adler.getValue();
                        this.putShortMSB((int)(var6 >>> 16));
                        this.putShortMSB((int)(var6 & 65535L));
                     }

                     this.strm.adler.reset();
                  }
               }

               if (this.pending != 0) {
                  this.strm.flush_pending();
                  if (this.strm.avail_out == 0) {
                     this.last_flush = -1;
                     return 0;
                  }
               } else if (this.strm.avail_in == 0 && var1 <= var4 && var1 != 4) {
                  this.strm.msg = z_errmsg[7];
                  return -5;
               }

               if (this.status == 666 && this.strm.avail_in != 0) {
                  this.strm.msg = z_errmsg[7];
                  return -5;
               } else {
                  if (this.strm.avail_in != 0 || this.lookahead != 0 || var1 != 0 && this.status != 666) {
                     var2 = -1;
                     var3 = config_table[this.level].func;
                     if (var3 != 0) {
                        if (var3 != 1) {
                           if (var3 == 2) {
                              var2 = this.deflate_slow(var1);
                           }
                        } else {
                           var2 = this.deflate_fast(var1);
                        }
                     } else {
                        var2 = this.deflate_stored(var1);
                     }

                     if (var2 == 2 || var2 == 3) {
                        this.status = 666;
                     }

                     if (var2 == 0 || var2 == 2) {
                        if (this.strm.avail_out == 0) {
                           this.last_flush = -1;
                        }

                        return 0;
                     }

                     if (var2 == 1) {
                        if (var1 == 1) {
                           this._tr_align();
                        } else {
                           this._tr_stored_block(0, 0, false);
                           if (var1 == 3) {
                              for(var2 = 0; var2 < this.hash_size; ++var2) {
                                 this.head[var2] = 0;
                              }
                           }
                        }

                        this.strm.flush_pending();
                        if (this.strm.avail_out == 0) {
                           this.last_flush = -1;
                           return 0;
                        }
                     }
                  }

                  if (var1 != 4) {
                     return 0;
                  } else {
                     var1 = this.wrap;
                     if (var1 <= 0) {
                        return 1;
                     } else {
                        if (var1 == 2) {
                           var6 = this.strm.adler.getValue();
                           this.put_byte((byte)((int)(var6 & 255L)));
                           this.put_byte((byte)((int)(var6 >> 8 & 255L)));
                           this.put_byte((byte)((int)(var6 >> 16 & 255L)));
                           this.put_byte((byte)((int)(var6 >> 24 & 255L)));
                           this.put_byte((byte)((int)(this.strm.total_in & 255L)));
                           this.put_byte((byte)((int)(this.strm.total_in >> 8 & 255L)));
                           this.put_byte((byte)((int)(this.strm.total_in >> 16 & 255L)));
                           this.put_byte((byte)((int)(255L & this.strm.total_in >> 24)));
                           this.getGZIPHeader().setCRC(var6);
                        } else {
                           var6 = this.strm.adler.getValue();
                           this.putShortMSB((int)(var6 >>> 16));
                           this.putShortMSB((int)(var6 & 65535L));
                        }

                        this.strm.flush_pending();
                        var1 = this.wrap;
                        if (var1 > 0) {
                           this.wrap = -var1;
                        }

                        return this.pending != 0 ? 0 : 1;
                     }
                  }
               }
            }
         } else {
            this.strm.msg = z_errmsg[4];
            return -2;
         }
      } else {
         return -2;
      }
   }

   int deflateEnd() {
      int var1 = this.status;
      if (var1 != 42 && var1 != 113 && var1 != 666) {
         return -2;
      } else {
         this.pending_buf = null;
         this.l_buf = null;
         this.head = null;
         this.prev = null;
         this.window = null;
         return this.status == 113 ? -3 : 0;
      }
   }

   int deflateInit(int var1) {
      return this.deflateInit(var1, 15);
   }

   int deflateInit(int var1, int var2) {
      return this.deflateInit(var1, 8, var2, 8, 0);
   }

   int deflateInit(int var1, int var2, int var3) {
      return this.deflateInit(var1, 8, var2, var3, 0);
   }

   int deflateParams(int var1, int var2) {
      byte var4 = 0;
      int var3 = var1;
      if (var1 == -1) {
         var3 = 6;
      }

      if (var3 >= 0 && var3 <= 9 && var2 >= 0 && var2 <= 2) {
         var1 = var4;
         if (config_table[this.level].func != config_table[var3].func) {
            var1 = var4;
            if (this.strm.total_in != 0L) {
               var1 = this.strm.deflate(1);
            }
         }

         if (this.level != var3) {
            this.level = var3;
            this.max_lazy_match = config_table[var3].max_lazy;
            this.good_match = config_table[this.level].good_length;
            this.nice_match = config_table[this.level].nice_length;
            this.max_chain_length = config_table[this.level].max_chain;
         }

         this.strategy = var2;
         return var1;
      } else {
         return -2;
      }
   }

   int deflateReset() {
      ZStream var2 = this.strm;
      var2.total_out = 0L;
      var2.total_in = 0L;
      this.strm.msg = null;
      this.strm.data_type = 2;
      this.pending = 0;
      this.pending_out = 0;
      int var1 = this.wrap;
      if (var1 < 0) {
         this.wrap = -var1;
      }

      byte var3;
      if (this.wrap == 0) {
         var3 = 113;
      } else {
         var3 = 42;
      }

      this.status = var3;
      this.strm.adler.reset();
      this.last_flush = 0;
      this.tr_init();
      this.lm_init();
      return 0;
   }

   int deflateSetDictionary(byte[] var1, int var2) {
      int var5 = 0;
      if (var1 != null && this.status == 42) {
         this.strm.adler.update(var1, 0, var2);
         if (var2 < 3) {
            return 0;
         } else {
            int var6 = this.w_size;
            int var3 = var2;
            if (var2 > var6 - 262) {
               var3 = var6 - 262;
               var5 = var2 - var3;
            }

            System.arraycopy(var1, var5, this.window, 0, var3);
            this.strstart = var3;
            this.block_start = var3;
            var1 = this.window;
            var2 = var1[0] & 255;
            this.ins_h = var2;
            int var4 = this.hash_shift;
            this.ins_h = (var1[1] & 255 ^ var2 << var4) & this.hash_mask;

            for(var2 = 0; var2 <= var3 - 3; ++var2) {
               var4 = (this.ins_h << this.hash_shift ^ this.window[var2 + 2] & 255) & this.hash_mask;
               this.ins_h = var4;
               short[] var8 = this.prev;
               var5 = this.w_mask;
               short[] var7 = this.head;
               var8[var5 & var2] = var7[var4];
               var7[var4] = (short)var2;
            }

            return 0;
         }
      } else {
         return -2;
      }
   }

   int deflate_fast(int var1) {
      int var2 = 0;

      while(true) {
         boolean var6;
         if (this.lookahead < 262) {
            this.fill_window();
            if (this.lookahead < 262 && var1 == 0) {
               return 0;
            }

            if (this.lookahead == 0) {
               if (var1 == 4) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               this.flush_block_only(var6);
               if (this.strm.avail_out == 0) {
                  if (var1 == 4) {
                     return 2;
                  }

                  return 0;
               }

               if (var1 == 4) {
                  return 3;
               }

               return 1;
            }
         }

         int var3;
         int var4;
         byte[] var7;
         short[] var8;
         if (this.lookahead >= 3) {
            var2 = this.ins_h;
            var4 = this.hash_shift;
            var7 = this.window;
            var3 = this.strstart;
            var4 = (var2 << var4 ^ var7[var3 + 2] & 255) & this.hash_mask;
            this.ins_h = var4;
            var8 = this.head;
            var2 = var8[var4] & '\uffff';
            this.prev[this.w_mask & var3] = var8[var4];
            var8[var4] = (short)var3;
         }

         if ((long)var2 != 0L && (this.strstart - var2 & '\uffff') <= this.w_size - 262 && this.strategy != 2) {
            this.match_length = this.longest_match(var2);
         }

         var3 = this.match_length;
         if (var3 < 3) {
            var6 = this._tr_tally(0, this.window[this.strstart] & 255);
            --this.lookahead;
            ++this.strstart;
         } else {
            var6 = this._tr_tally(this.strstart - this.match_start, var3 - 3);
            var4 = this.lookahead;
            var3 = this.match_length;
            var4 -= var3;
            this.lookahead = var4;
            if (var3 <= this.max_lazy_match && var4 >= 3) {
               this.match_length = var3 - 1;

               do {
                  var3 = this.strstart + 1;
                  this.strstart = var3;
                  var4 = (this.ins_h << this.hash_shift ^ this.window[var3 + 2] & 255) & this.hash_mask;
                  this.ins_h = var4;
                  var8 = this.head;
                  var2 = var8[var4] & '\uffff';
                  this.prev[this.w_mask & var3] = var8[var4];
                  var8[var4] = (short)var3;
                  var4 = this.match_length - 1;
                  this.match_length = var4;
               } while(var4 != 0);

               this.strstart = var3 + 1;
            } else {
               var3 = this.strstart + this.match_length;
               this.strstart = var3;
               this.match_length = 0;
               var7 = this.window;
               var4 = var7[var3] & 255;
               this.ins_h = var4;
               int var5 = this.hash_shift;
               this.ins_h = (var7[var3 + 1] & 255 ^ var4 << var5) & this.hash_mask;
            }
         }

         if (var6) {
            this.flush_block_only(false);
            if (this.strm.avail_out == 0) {
               return 0;
            }
         }
      }
   }

   int deflate_slow(int var1) {
      int var2 = 0;

      while(true) {
         while(true) {
            boolean var7;
            if (this.lookahead < 262) {
               this.fill_window();
               if (this.lookahead < 262 && var1 == 0) {
                  return 0;
               }

               if (this.lookahead == 0) {
                  if (this.match_available != 0) {
                     this._tr_tally(0, this.window[this.strstart - 1] & 255);
                     this.match_available = 0;
                  }

                  if (var1 == 4) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  this.flush_block_only(var7);
                  if (this.strm.avail_out == 0) {
                     if (var1 == 4) {
                        return 2;
                     }

                     return 0;
                  }

                  if (var1 == 4) {
                     return 3;
                  }

                  return 1;
               }
            }

            int var3;
            int var4;
            short[] var9;
            if (this.lookahead >= 3) {
               var2 = this.ins_h;
               var4 = this.hash_shift;
               byte[] var8 = this.window;
               var3 = this.strstart;
               var4 = (var2 << var4 ^ var8[var3 + 2] & 255) & this.hash_mask;
               this.ins_h = var4;
               var9 = this.head;
               var2 = var9[var4] & '\uffff';
               this.prev[this.w_mask & var3] = var9[var4];
               var9[var4] = (short)var3;
            }

            var3 = this.match_length;
            this.prev_length = var3;
            this.prev_match = this.match_start;
            this.match_length = 2;
            if (var2 != 0 && var3 < this.max_lazy_match && (this.strstart - var2 & '\uffff') <= this.w_size - 262) {
               if (this.strategy != 2) {
                  this.match_length = this.longest_match(var2);
               }

               var3 = this.match_length;
               if (var3 <= 5 && (this.strategy == 1 || var3 == 3 && this.strstart - this.match_start > 4096)) {
                  this.match_length = 2;
               }
            }

            var3 = this.prev_length;
            if (var3 >= 3 && this.match_length <= var3) {
               var4 = this.strstart;
               int var5 = this.lookahead;
               var7 = this._tr_tally(var4 - 1 - this.prev_match, var3 - 3);
               var3 = this.lookahead;
               int var6 = this.prev_length;
               this.lookahead = var3 - (var6 - 1);
               this.prev_length = var6 - 2;
               var3 = var2;

               do {
                  var6 = this.strstart + 1;
                  this.strstart = var6;
                  var2 = var3;
                  if (var6 <= var5 + var4 - 3) {
                     var3 = (this.ins_h << this.hash_shift ^ this.window[var6 + 2] & 255) & this.hash_mask;
                     this.ins_h = var3;
                     var9 = this.head;
                     var2 = var9[var3] & '\uffff';
                     this.prev[this.w_mask & var6] = var9[var3];
                     var9[var3] = (short)var6;
                  }

                  var6 = this.prev_length - 1;
                  this.prev_length = var6;
                  var3 = var2;
               } while(var6 != 0);

               this.match_available = 0;
               this.match_length = 2;
               ++this.strstart;
               if (var7) {
                  this.flush_block_only(false);
                  if (this.strm.avail_out == 0) {
                     return 0;
                  }
               }
            } else if (this.match_available != 0) {
               if (this._tr_tally(0, this.window[this.strstart - 1] & 255)) {
                  this.flush_block_only(false);
               }

               ++this.strstart;
               --this.lookahead;
               if (this.strm.avail_out == 0) {
                  return 0;
               }
            } else {
               this.match_available = 1;
               ++this.strstart;
               --this.lookahead;
            }
         }
      }
   }

   int deflate_stored(int var1) {
      int var2 = 65535;
      int var3 = this.pending_buf_size;
      if (65535 > var3 - 5) {
         var2 = var3 - 5;
      }

      while(true) {
         int var5 = this.lookahead;
         byte var8 = 1;
         byte var4 = 0;
         if (var5 <= 1) {
            this.fill_window();
            if (this.lookahead == 0 && var1 == 0) {
               return 0;
            }

            if (this.lookahead == 0) {
               boolean var6;
               if (var1 == 4) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               this.flush_block_only(var6);
               byte var7;
               if (this.strm.avail_out == 0) {
                  var7 = var4;
                  if (var1 == 4) {
                     var7 = 2;
                  }

                  return var7;
               }

               var7 = var8;
               if (var1 == 4) {
                  var7 = 3;
               }

               return var7;
            }
         }

         var3 = this.strstart + this.lookahead;
         this.strstart = var3;
         this.lookahead = 0;
         int var9 = this.block_start + var2;
         if (var3 == 0 || var3 >= var9) {
            this.lookahead = this.strstart - var9;
            this.strstart = var9;
            this.flush_block_only(false);
            if (this.strm.avail_out == 0) {
               return 0;
            }
         }

         if (this.strstart - this.block_start >= this.w_size - 262) {
            this.flush_block_only(false);
            if (this.strm.avail_out == 0) {
               return 0;
            }
         }
      }
   }

   void fill_window() {
      do {
         int var4 = this.window_size;
         int var2 = this.lookahead;
         int var3 = this.strstart;
         var4 = var4 - var2 - var3;
         byte[] var7;
         if (var4 == 0 && var3 == 0 && var2 == 0) {
            var2 = this.w_size;
         } else if (var4 == -1) {
            var2 = var4 - 1;
         } else {
            var3 = this.strstart;
            int var5 = this.w_size;
            var2 = var4;
            if (var3 >= var5 + var5 - 262) {
               var7 = this.window;
               System.arraycopy(var7, var5, var7, 0, var5);
               var2 = this.match_start;
               var3 = this.w_size;
               this.match_start = var2 - var3;
               this.strstart -= var3;
               this.block_start -= var3;
               var2 = this.hash_size;
               var3 = var2;

               short var1;
               int var6;
               short[] var8;
               do {
                  var8 = this.head;
                  --var3;
                  var5 = var8[var3] & '\uffff';
                  var6 = this.w_size;
                  if (var5 >= var6) {
                     var1 = (short)(var5 - var6);
                  } else {
                     var1 = 0;
                  }

                  var8[var3] = var1;
                  var5 = var2 - 1;
                  var2 = var5;
               } while(var5 != 0);

               var2 = this.w_size;
               var3 = var2;

               do {
                  var8 = this.prev;
                  --var3;
                  var5 = var8[var3] & '\uffff';
                  var6 = this.w_size;
                  if (var5 >= var6) {
                     var1 = (short)(var5 - var6);
                  } else {
                     var1 = 0;
                  }

                  var8[var3] = var1;
                  var5 = var2 - 1;
                  var2 = var5;
               } while(var5 != 0);

               var2 = var4 + this.w_size;
            }
         }

         if (this.strm.avail_in == 0) {
            return;
         }

         var2 = this.strm.read_buf(this.window, this.strstart + this.lookahead, var2);
         var2 += this.lookahead;
         this.lookahead = var2;
         if (var2 >= 3) {
            var7 = this.window;
            var2 = this.strstart;
            var3 = var7[var2] & 255;
            this.ins_h = var3;
            var4 = this.hash_shift;
            this.ins_h = (var7[var2 + 1] & 255 ^ var3 << var4) & this.hash_mask;
         }
      } while(this.lookahead < 262 && this.strm.avail_in != 0);

   }

   void flush_block_only(boolean var1) {
      int var2 = this.block_start;
      if (var2 < 0) {
         var2 = -1;
      }

      this._tr_flush_block(var2, this.strstart - this.block_start, var1);
      this.block_start = this.strstart;
      this.strm.flush_pending();
   }

   GZIPHeader getGZIPHeader() {
      synchronized(this){}

      GZIPHeader var1;
      try {
         if (this.gheader == null) {
            this.gheader = new GZIPHeader();
         }

         var1 = this.gheader;
      } finally {
         ;
      }

      return var1;
   }

   void init_block() {
      int var1;
      for(var1 = 0; var1 < 286; ++var1) {
         this.dyn_ltree[var1 * 2] = 0;
      }

      for(var1 = 0; var1 < 30; ++var1) {
         this.dyn_dtree[var1 * 2] = 0;
      }

      for(var1 = 0; var1 < 19; ++var1) {
         this.bl_tree[var1 * 2] = 0;
      }

      this.dyn_ltree[512] = 1;
      this.static_len = 0;
      this.opt_len = 0;
      this.matches = 0;
      this.last_lit = 0;
   }

   void lm_init() {
      this.window_size = this.w_size * 2;
      this.head[this.hash_size - 1] = 0;

      for(int var1 = 0; var1 < this.hash_size - 1; ++var1) {
         this.head[var1] = 0;
      }

      this.max_lazy_match = config_table[this.level].max_lazy;
      this.good_match = config_table[this.level].good_length;
      this.nice_match = config_table[this.level].nice_length;
      this.max_chain_length = config_table[this.level].max_chain;
      this.strstart = 0;
      this.block_start = 0;
      this.lookahead = 0;
      this.prev_length = 2;
      this.match_length = 2;
      this.match_available = 0;
      this.ins_h = 0;
   }

   int longest_match(int var1) {
      int var4 = this.max_chain_length;
      int var11 = this.strstart;
      int var12 = this.prev_length;
      int var2 = this.strstart;
      int var3 = this.w_size;
      if (var2 > var3 - 262) {
         var3 = var2 - (var3 - 262);
      } else {
         var3 = 0;
      }

      int var15 = this.nice_match;
      int var16 = this.w_mask;
      int var17 = this.strstart + 258;
      byte[] var18 = this.window;
      byte var13 = var18[var11 + var12 - 1];
      byte var14 = var18[var11 + var12];
      var2 = var4;
      if (this.prev_length >= this.good_match) {
         var2 = var4 >> 2;
      }

      var4 = var2;
      int var10 = var11;
      int var9 = var12;
      int var5 = var15;
      byte var7 = var14;
      byte var8 = var13;
      int var6 = var1;
      if (var15 > this.lookahead) {
         var5 = this.lookahead;
         var6 = var1;
         var8 = var13;
         var7 = var14;
         var9 = var12;
         var10 = var11;
         var4 = var2;
      }

      int var20;
      while(true) {
         var18 = this.window;
         var12 = var10;
         var1 = var9;
         byte var22 = var7;
         byte var19 = var8;
         int var23;
         if (var18[var6 + var9] == var7) {
            var12 = var10;
            var1 = var9;
            var22 = var7;
            var19 = var8;
            if (var18[var6 + var9 - 1] == var8) {
               var12 = var10;
               var1 = var9;
               var22 = var7;
               var19 = var8;
               if (var18[var6] == var18[var10]) {
                  var1 = var6 + 1;
                  if (var18[var1] != var18[var10 + 1]) {
                     var12 = var10;
                     var1 = var9;
                     var22 = var7;
                     var19 = var8;
                  } else {
                     var10 += 2;
                     ++var1;

                     while(true) {
                        var18 = this.window;
                        var2 = var10 + 1;
                        var22 = var18[var2];
                        var10 = var1 + 1;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        var22 = var18[var2];
                        ++var10;
                        var1 = var2;
                        if (var22 != var18[var10]) {
                           break;
                        }

                        ++var2;
                        byte var24 = var18[var2];
                        var11 = var10 + 1;
                        var1 = var2;
                        if (var24 != var18[var11]) {
                           break;
                        }

                        var10 = var2;
                        var1 = var11;
                        if (var2 >= var17) {
                           var1 = var2;
                           break;
                        }
                     }

                     var23 = 258 - (var17 - var1);
                     var10 = var17 - 258;
                     var12 = var10;
                     var1 = var9;
                     var22 = var7;
                     var19 = var8;
                     if (var23 > var9) {
                        this.match_start = var6;
                        var1 = var23;
                        if (var23 >= var5) {
                           var20 = var23;
                           break;
                        }

                        var18 = this.window;
                        var19 = var18[var10 + var23 - 1];
                        var22 = var18[var10 + var23];
                        var12 = var10;
                     }
                  }
               }
            }
         }

         int var21 = this.prev[var6 & var16] & '\uffff';
         var6 = var21;
         var20 = var1;
         if (var21 <= var3) {
            break;
         }

         var23 = var4 - 1;
         var4 = var23;
         var10 = var12;
         var9 = var1;
         var7 = var22;
         var8 = var19;
         if (var23 == 0) {
            var20 = var1;
            break;
         }
      }

      var1 = this.lookahead;
      return var20 <= var1 ? var20 : var1;
   }

   void pqdownheap(short[] var1, int var2) {
      int var5 = this.heap[var2];
      int var3 = var2 << 1;
      int var4 = var2;
      var2 = var3;

      while(true) {
         int var6 = this.heap_len;
         if (var2 > var6) {
            break;
         }

         var3 = var2;
         int[] var7;
         if (var2 < var6) {
            var7 = this.heap;
            var3 = var2;
            if (smaller(var1, var7[var2 + 1], var7[var2], this.depth)) {
               var3 = var2 + 1;
            }
         }

         if (smaller(var1, var5, this.heap[var3], this.depth)) {
            break;
         }

         var7 = this.heap;
         var7[var4] = var7[var3];
         var4 = var3;
         var2 = var3 << 1;
      }

      this.heap[var4] = var5;
   }

   final void putShortMSB(int var1) {
      this.put_byte((byte)(var1 >> 8));
      this.put_byte((byte)var1);
   }

   final void put_byte(byte var1) {
      byte[] var3 = this.pending_buf;
      int var2 = this.pending++;
      var3[var2] = var1;
   }

   final void put_byte(byte[] var1, int var2, int var3) {
      System.arraycopy(var1, var2, this.pending_buf, this.pending, var3);
      this.pending += var3;
   }

   final void put_short(int var1) {
      this.put_byte((byte)var1);
      this.put_byte((byte)(var1 >>> 8));
   }

   void scan_tree(short[] var1, int var2) {
      short var8 = -1;
      short var6 = var1[1];
      int var5 = 0;
      short var3 = 7;
      byte var4 = 4;
      if (var6 == 0) {
         var3 = 138;
         var4 = 3;
      }

      var1[(var2 + 1) * 2 + 1] = -1;
      int var7 = 0;

      while(true) {
         short var9 = var6;
         if (var7 > var2) {
            return;
         }

         short var10 = var1[(var7 + 1) * 2 + 1];
         ++var5;
         if (var5 < var3 && var6 == var10) {
            var6 = var8;
         } else {
            short[] var11;
            int var12;
            if (var5 < var4) {
               var11 = this.bl_tree;
               var12 = var6 * 2;
               var11[var12] = (short)(var11[var12] + var5);
            } else if (var6 != 0) {
               if (var6 != var8) {
                  var11 = this.bl_tree;
                  var12 = var6 * 2;
                  ++var11[var12];
               }

               var11 = this.bl_tree;
               ++var11[32];
            } else if (var5 <= 10) {
               var11 = this.bl_tree;
               ++var11[34];
            } else {
               var11 = this.bl_tree;
               ++var11[36];
            }

            var5 = 0;
            var6 = var6;
            if (var10 == 0) {
               var3 = 138;
               var4 = 3;
            } else if (var9 == var10) {
               var3 = 6;
               var4 = 3;
            } else {
               var3 = 7;
               var4 = 4;
            }
         }

         ++var7;
         var8 = var6;
         var6 = var10;
      }
   }

   void send_all_trees(int var1, int var2, int var3) {
      this.send_bits(var1 - 257, 5);
      this.send_bits(var2 - 1, 5);
      this.send_bits(var3 - 4, 4);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.send_bits(this.bl_tree[Tree.bl_order[var4] * 2 + 1], 3);
      }

      this.send_tree(this.dyn_ltree, var1 - 1);
      this.send_tree(this.dyn_dtree, var2 - 1);
   }

   void send_bits(int var1, int var2) {
      int var4 = this.bi_valid;
      if (var4 > 16 - var2) {
         short var3 = (short)(var1 << var4 & '\uffff' | this.bi_buf);
         this.bi_buf = var3;
         this.put_short(var3);
         var4 = this.bi_valid;
         this.bi_buf = (short)(var1 >>> 16 - var4);
         this.bi_valid = var4 + (var2 - 16);
      } else {
         this.bi_buf = (short)(this.bi_buf | '\uffff' & var1 << var4);
         this.bi_valid = var4 + var2;
      }
   }

   final void send_code(int var1, short[] var2) {
      var1 *= 2;
      this.send_bits(var2[var1] & '\uffff', '\uffff' & var2[var1 + 1]);
   }

   void send_tree(short[] var1, int var2) {
      short var8 = -1;
      short var6 = var1[1];
      int var5 = 0;
      short var3 = 7;
      byte var4 = 4;
      if (var6 == 0) {
         var3 = 138;
         var4 = 3;
      }

      short var9;
      for(int var7 = 0; var7 <= var2; var6 = var9) {
         var9 = var1[(var7 + 1) * 2 + 1];
         ++var5;
         if (var5 < var3 && var6 == var9) {
            var6 = var8;
         } else {
            int var10;
            if (var5 < var4) {
               do {
                  this.send_code(var6, this.bl_tree);
                  var10 = var5 - 1;
                  var5 = var10;
               } while(var10 != 0);
            } else if (var6 != 0) {
               var10 = var5;
               if (var6 != var8) {
                  this.send_code(var6, this.bl_tree);
                  var10 = var5 - 1;
               }

               this.send_code(16, this.bl_tree);
               this.send_bits(var10 - 3, 2);
            } else if (var5 <= 10) {
               this.send_code(17, this.bl_tree);
               this.send_bits(var5 - 3, 3);
            } else {
               this.send_code(18, this.bl_tree);
               this.send_bits(var5 - 11, 7);
            }

            var5 = 0;
            if (var9 == 0) {
               var3 = 138;
               var4 = 3;
               var6 = var6;
            } else if (var6 == var9) {
               var3 = 6;
               var4 = 3;
               var6 = var6;
            } else {
               var3 = 7;
               var4 = 4;
               var6 = var6;
            }
         }

         ++var7;
         var8 = var6;
      }

   }

   void set_data_type() {
      int var3 = 0;
      byte var5 = 0;
      int var1 = 0;

      while(true) {
         int var2 = var3;
         int var4 = var5;
         if (var3 >= 7) {
            while(true) {
               var3 = var2;
               int var7 = var1;
               if (var2 >= 128) {
                  while(var3 < 256) {
                     var7 += this.dyn_ltree[var3 * 2];
                     ++var3;
                  }

                  byte var6;
                  if (var7 > var4 >>> 2) {
                     var6 = 0;
                  } else {
                     var6 = 1;
                  }

                  this.data_type = (byte)var6;
                  return;
               }

               var4 += this.dyn_ltree[var2 * 2];
               ++var2;
            }
         }

         var1 += this.dyn_ltree[var3 * 2];
         ++var3;
      }
   }

   void tr_init() {
      this.l_desc.dyn_tree = this.dyn_ltree;
      this.l_desc.stat_desc = StaticTree.static_l_desc;
      this.d_desc.dyn_tree = this.dyn_dtree;
      this.d_desc.stat_desc = StaticTree.static_d_desc;
      this.bl_desc.dyn_tree = this.bl_tree;
      this.bl_desc.stat_desc = StaticTree.static_bl_desc;
      this.bi_buf = 0;
      this.bi_valid = 0;
      this.last_eob_len = 8;
      this.init_block();
   }

   static class Config {
      int func;
      int good_length;
      int max_chain;
      int max_lazy;
      int nice_length;

      Config(int var1, int var2, int var3, int var4, int var5) {
         this.good_length = var1;
         this.max_lazy = var2;
         this.nice_length = var3;
         this.max_chain = var4;
         this.func = var5;
      }
   }
}
