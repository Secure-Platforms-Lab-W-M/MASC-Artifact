package com.jcraft.jzlib;

@Deprecated
public class ZStream {
   private static final int DEF_WBITS = 15;
   private static final int MAX_MEM_LEVEL = 9;
   private static final int MAX_WBITS = 15;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_ERRNO = -1;
   private static final int Z_FINISH = 4;
   private static final int Z_FULL_FLUSH = 3;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   private static final int Z_NO_FLUSH = 0;
   private static final int Z_OK = 0;
   private static final int Z_PARTIAL_FLUSH = 1;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   private static final int Z_SYNC_FLUSH = 2;
   private static final int Z_VERSION_ERROR = -6;
   Checksum adler;
   public int avail_in;
   public int avail_out;
   int data_type;
   Deflate dstate;
   Inflate istate;
   public String msg;
   public byte[] next_in;
   public int next_in_index;
   public byte[] next_out;
   public int next_out_index;
   public long total_in;
   public long total_out;

   public ZStream() {
      this(new Adler32());
   }

   public ZStream(Checksum var1) {
      this.adler = var1;
   }

   public int deflate(int var1) {
      Deflate var2 = this.dstate;
      return var2 == null ? -2 : var2.deflate(var1);
   }

   public int deflateEnd() {
      Deflate var2 = this.dstate;
      if (var2 == null) {
         return -2;
      } else {
         int var1 = var2.deflateEnd();
         this.dstate = null;
         return var1;
      }
   }

   public int deflateInit(int var1) {
      return this.deflateInit(var1, 15);
   }

   public int deflateInit(int var1, int var2) {
      return this.deflateInit(var1, var2, false);
   }

   public int deflateInit(int var1, int var2, int var3) {
      Deflate var4 = new Deflate(this);
      this.dstate = var4;
      return var4.deflateInit(var1, var2, var3);
   }

   public int deflateInit(int var1, int var2, int var3, JZlib.WrapperType var4) {
      if (var2 >= 9) {
         if (var2 > 15) {
            return -2;
         } else {
            if (var4 == JZlib.W_NONE) {
               var2 *= -1;
            } else if (var4 == JZlib.W_GZIP) {
               var2 += 16;
            } else {
               if (var4 == JZlib.W_ANY) {
                  return -2;
               }

               var4 = JZlib.W_ZLIB;
            }

            return this.deflateInit(var1, var2, var3);
         }
      } else {
         return -2;
      }
   }

   public int deflateInit(int var1, int var2, boolean var3) {
      Deflate var4 = new Deflate(this);
      this.dstate = var4;
      if (var3) {
         var2 = -var2;
      }

      return var4.deflateInit(var1, var2);
   }

   public int deflateInit(int var1, boolean var2) {
      return this.deflateInit(var1, 15, var2);
   }

   public int deflateParams(int var1, int var2) {
      Deflate var3 = this.dstate;
      return var3 == null ? -2 : var3.deflateParams(var1, var2);
   }

   public int deflateSetDictionary(byte[] var1, int var2) {
      Deflate var3 = this.dstate;
      return var3 == null ? -2 : var3.deflateSetDictionary(var1, var2);
   }

   public int end() {
      return 0;
   }

   public boolean finished() {
      return false;
   }

   void flush_pending() {
      int var2 = this.dstate.pending;
      int var1 = var2;
      if (var2 > this.avail_out) {
         var1 = this.avail_out;
      }

      if (var1 != 0) {
         if (this.dstate.pending_buf.length > this.dstate.pending_out && this.next_out.length > this.next_out_index && this.dstate.pending_buf.length >= this.dstate.pending_out + var1) {
            var2 = this.next_out.length;
         }

         System.arraycopy(this.dstate.pending_buf, this.dstate.pending_out, this.next_out, this.next_out_index, var1);
         this.next_out_index += var1;
         Deflate var3 = this.dstate;
         var3.pending_out += var1;
         this.total_out += (long)var1;
         this.avail_out -= var1;
         var3 = this.dstate;
         var3.pending -= var1;
         if (this.dstate.pending == 0) {
            this.dstate.pending_out = 0;
         }

      }
   }

   public void free() {
      this.next_in = null;
      this.next_out = null;
      this.msg = null;
   }

   public long getAdler() {
      return this.adler.getValue();
   }

   public int getAvailIn() {
      return this.avail_in;
   }

   public int getAvailOut() {
      return this.avail_out;
   }

   public String getMessage() {
      return this.msg;
   }

   public byte[] getNextIn() {
      return this.next_in;
   }

   public int getNextInIndex() {
      return this.next_in_index;
   }

   public byte[] getNextOut() {
      return this.next_out;
   }

   public int getNextOutIndex() {
      return this.next_out_index;
   }

   public long getTotalIn() {
      return this.total_in;
   }

   public long getTotalOut() {
      return this.total_out;
   }

   public int inflate(int var1) {
      Inflate var2 = this.istate;
      return var2 == null ? -2 : var2.inflate(var1);
   }

   public int inflateEnd() {
      Inflate var1 = this.istate;
      return var1 == null ? -2 : var1.inflateEnd();
   }

   public boolean inflateFinished() {
      return this.istate.mode == 12;
   }

   public int inflateInit() {
      return this.inflateInit(15);
   }

   public int inflateInit(int var1) {
      return this.inflateInit(var1, false);
   }

   public int inflateInit(int var1, JZlib.WrapperType var2) {
      boolean var3 = false;
      if (var2 == JZlib.W_NONE) {
         var3 = true;
      } else if (var2 == JZlib.W_GZIP) {
         var1 += 16;
      } else if (var2 == JZlib.W_ANY) {
         var1 |= 1073741824;
      } else {
         var2 = JZlib.W_ZLIB;
      }

      return this.inflateInit(var1, var3);
   }

   public int inflateInit(int var1, boolean var2) {
      Inflate var3 = new Inflate(this);
      this.istate = var3;
      if (var2) {
         var1 = -var1;
      }

      return var3.inflateInit(var1);
   }

   public int inflateInit(JZlib.WrapperType var1) {
      return this.inflateInit(15, var1);
   }

   public int inflateInit(boolean var1) {
      return this.inflateInit(15, var1);
   }

   public int inflateSetDictionary(byte[] var1, int var2) {
      Inflate var3 = this.istate;
      return var3 == null ? -2 : var3.inflateSetDictionary(var1, var2);
   }

   public int inflateSync() {
      Inflate var1 = this.istate;
      return var1 == null ? -2 : var1.inflateSync();
   }

   public int inflateSyncPoint() {
      Inflate var1 = this.istate;
      return var1 == null ? -2 : var1.inflateSyncPoint();
   }

   int read_buf(byte[] var1, int var2, int var3) {
      int var5 = this.avail_in;
      int var4 = var5;
      if (var5 > var3) {
         var4 = var3;
      }

      if (var4 == 0) {
         return 0;
      } else {
         this.avail_in -= var4;
         if (this.dstate.wrap != 0) {
            this.adler.update(this.next_in, this.next_in_index, var4);
         }

         System.arraycopy(this.next_in, this.next_in_index, var1, var2, var4);
         this.next_in_index += var4;
         this.total_in += (long)var4;
         return var4;
      }
   }

   public void setAvailIn(int var1) {
      this.avail_in = var1;
   }

   public void setAvailOut(int var1) {
      this.avail_out = var1;
   }

   public void setInput(byte[] var1) {
      this.setInput(var1, 0, var1.length, false);
   }

   public void setInput(byte[] var1, int var2, int var3, boolean var4) {
      if (var3 > 0 || !var4 || this.next_in == null) {
         int var5 = this.avail_in;
         if (var5 > 0 && var4) {
            byte[] var6 = new byte[var5 + var3];
            System.arraycopy(this.next_in, this.next_in_index, var6, 0, var5);
            System.arraycopy(var1, var2, var6, this.avail_in, var3);
            this.next_in = var6;
            this.next_in_index = 0;
            this.avail_in += var3;
         } else {
            this.next_in = var1;
            this.next_in_index = var2;
            this.avail_in = var3;
         }
      }
   }

   public void setInput(byte[] var1, boolean var2) {
      this.setInput(var1, 0, var1.length, var2);
   }

   public void setNextIn(byte[] var1) {
      this.next_in = var1;
   }

   public void setNextInIndex(int var1) {
      this.next_in_index = var1;
   }

   public void setNextOut(byte[] var1) {
      this.next_out = var1;
   }

   public void setNextOutIndex(int var1) {
      this.next_out_index = var1;
   }

   public void setOutput(byte[] var1) {
      this.setOutput(var1, 0, var1.length);
   }

   public void setOutput(byte[] var1, int var2, int var3) {
      this.next_out = var1;
      this.next_out_index = var2;
      this.avail_out = var3;
   }
}
