package com.jcraft.jzlib;

public final class Inflater extends ZStream {
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

   public Inflater() {
      this.finished = false;
      this.init();
   }

   public Inflater(int var1) throws GZIPException {
      this(var1, false);
   }

   public Inflater(int var1, JZlib.WrapperType var2) throws GZIPException {
      this.finished = false;
      var1 = this.init(var1, var2);
      if (var1 != 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(": ");
         var3.append(this.msg);
         throw new GZIPException(var3.toString());
      }
   }

   public Inflater(int var1, boolean var2) throws GZIPException {
      this.finished = false;
      var1 = this.init(var1, var2);
      if (var1 != 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(": ");
         var3.append(this.msg);
         throw new GZIPException(var3.toString());
      }
   }

   public Inflater(JZlib.WrapperType var1) throws GZIPException {
      this(15, var1);
   }

   public Inflater(boolean var1) throws GZIPException {
      this(15, var1);
   }

   public int end() {
      this.finished = true;
      return this.istate == null ? -2 : this.istate.inflateEnd();
   }

   public boolean finished() {
      return this.istate.mode == 12;
   }

   public int inflate(int var1) {
      if (this.istate == null) {
         return -2;
      } else {
         var1 = this.istate.inflate(var1);
         if (var1 == 1) {
            this.finished = true;
         }

         return var1;
      }
   }

   public int init() {
      return this.init(15);
   }

   public int init(int var1) {
      return this.init(var1, false);
   }

   public int init(int var1, JZlib.WrapperType var2) {
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

      return this.init(var1, var3);
   }

   public int init(int var1, boolean var2) {
      this.finished = false;
      this.istate = new Inflate(this);
      Inflate var3 = this.istate;
      if (var2) {
         var1 = -var1;
      }

      return var3.inflateInit(var1);
   }

   public int init(JZlib.WrapperType var1) {
      return this.init(15, var1);
   }

   public int init(boolean var1) {
      return this.init(15, var1);
   }

   public int setDictionary(byte[] var1, int var2) {
      return this.istate == null ? -2 : this.istate.inflateSetDictionary(var1, var2);
   }

   public int sync() {
      return this.istate == null ? -2 : this.istate.inflateSync();
   }

   public int syncPoint() {
      return this.istate == null ? -2 : this.istate.inflateSyncPoint();
   }
}
