package com.jcraft.jzlib;

public final class JZlib {
   public static final int DEF_WBITS = 15;
   public static final int MAX_WBITS = 15;
   public static final JZlib.WrapperType W_ANY;
   public static final JZlib.WrapperType W_GZIP;
   public static final JZlib.WrapperType W_NONE;
   public static final JZlib.WrapperType W_ZLIB;
   public static final byte Z_ASCII = 1;
   public static final int Z_BEST_COMPRESSION = 9;
   public static final int Z_BEST_SPEED = 1;
   public static final byte Z_BINARY = 0;
   public static final int Z_BUF_ERROR = -5;
   public static final int Z_DATA_ERROR = -3;
   public static final int Z_DEFAULT_COMPRESSION = -1;
   public static final int Z_DEFAULT_STRATEGY = 0;
   public static final int Z_ERRNO = -1;
   public static final int Z_FILTERED = 1;
   public static final int Z_FINISH = 4;
   public static final int Z_FULL_FLUSH = 3;
   public static final int Z_HUFFMAN_ONLY = 2;
   public static final int Z_MEM_ERROR = -4;
   public static final int Z_NEED_DICT = 2;
   public static final int Z_NO_COMPRESSION = 0;
   public static final int Z_NO_FLUSH = 0;
   public static final int Z_OK = 0;
   public static final int Z_PARTIAL_FLUSH = 1;
   public static final int Z_STREAM_END = 1;
   public static final int Z_STREAM_ERROR = -2;
   public static final int Z_SYNC_FLUSH = 2;
   public static final byte Z_UNKNOWN = 2;
   public static final int Z_VERSION_ERROR = -6;
   private static final String version = "1.1.0";

   static {
      W_NONE = JZlib.WrapperType.NONE;
      W_ZLIB = JZlib.WrapperType.ZLIB;
      W_GZIP = JZlib.WrapperType.GZIP;
      W_ANY = JZlib.WrapperType.ANY;
   }

   public static long adler32_combine(long var0, long var2, long var4) {
      return Adler32.combine(var0, var2, var4);
   }

   public static long crc32_combine(long var0, long var2, long var4) {
      return CRC32.combine(var0, var2, var4);
   }

   public static String version() {
      return "1.1.0";
   }

   public static enum WrapperType {
      ANY,
      GZIP,
      NONE,
      ZLIB;

      static {
         JZlib.WrapperType var0 = new JZlib.WrapperType("ANY", 3);
         ANY = var0;
      }
   }
}
