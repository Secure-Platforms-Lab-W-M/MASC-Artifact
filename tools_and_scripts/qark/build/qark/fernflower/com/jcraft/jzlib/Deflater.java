package com.jcraft.jzlib;

public final class Deflater extends ZStream {
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
   private boolean finished;

   public Deflater() {
      this.finished = false;
   }

   public Deflater(int var1) throws GZIPException {
      this(var1, 15);
   }

   public Deflater(int var1, int var2) throws GZIPException {
      this(var1, var2, false);
   }

   public Deflater(int var1, int var2, int var3) throws GZIPException {
      this.finished = false;
      var1 = this.init(var1, var2, var3);
      if (var1 != 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append(": ");
         var4.append(this.msg);
         throw new GZIPException(var4.toString());
      }
   }

   public Deflater(int var1, int var2, int var3, JZlib.WrapperType var4) throws GZIPException {
      this.finished = false;
      var1 = this.init(var1, var2, var3, var4);
      if (var1 != 0) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1);
         var5.append(": ");
         var5.append(this.msg);
         throw new GZIPException(var5.toString());
      }
   }

   public Deflater(int var1, int var2, boolean var3) throws GZIPException {
      this.finished = false;
      var1 = this.init(var1, var2, var3);
      if (var1 != 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append(": ");
         var4.append(this.msg);
         throw new GZIPException(var4.toString());
      }
   }

   public Deflater(int var1, boolean var2) throws GZIPException {
      this(var1, 15, var2);
   }

   public int copy(Deflater var1) {
      this.finished = var1.finished;
      return Deflate.deflateCopy(this, var1);
   }

   public int deflate(int var1) {
      if (this.dstate == null) {
         return -2;
      } else {
         var1 = this.dstate.deflate(var1);
         if (var1 == 1) {
            this.finished = true;
         }

         return var1;
      }
   }

   public int end() {
      this.finished = true;
      if (this.dstate == null) {
         return -2;
      } else {
         int var1 = this.dstate.deflateEnd();
         this.dstate = null;
         this.free();
         return var1;
      }
   }

   public boolean finished() {
      return this.finished;
   }

   public int init(int var1) {
      return this.init(var1, 15);
   }

   public int init(int var1, int var2) {
      return this.init(var1, var2, false);
   }

   public int init(int var1, int var2, int var3) {
      this.finished = false;
      this.dstate = new Deflate(this);
      return this.dstate.deflateInit(var1, var2, var3);
   }

   public int init(int var1, int var2, int var3, JZlib.WrapperType var4) {
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

            return this.init(var1, var2, var3);
         }
      } else {
         return -2;
      }
   }

   public int init(int var1, int var2, boolean var3) {
      this.finished = false;
      this.dstate = new Deflate(this);
      Deflate var4 = this.dstate;
      if (var3) {
         var2 = -var2;
      }

      return var4.deflateInit(var1, var2);
   }

   public int init(int var1, boolean var2) {
      return this.init(var1, 15, var2);
   }

   public int params(int var1, int var2) {
      return this.dstate == null ? -2 : this.dstate.deflateParams(var1, var2);
   }

   public int setDictionary(byte[] var1, int var2) {
      return this.dstate == null ? -2 : this.dstate.deflateSetDictionary(var1, var2);
   }
}
