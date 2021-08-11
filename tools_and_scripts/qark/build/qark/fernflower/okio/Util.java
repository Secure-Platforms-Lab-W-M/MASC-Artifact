package okio;

import java.nio.charset.Charset;

final class Util {
   public static final Charset UTF_8 = Charset.forName("UTF-8");

   private Util() {
   }

   public static boolean arrayRangeEquals(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      for(int var5 = 0; var5 < var4; ++var5) {
         if (var0[var5 + var1] != var2[var5 + var3]) {
            return false;
         }
      }

      return true;
   }

   public static void checkOffsetAndCount(long var0, long var2, long var4) {
      if ((var2 | var4) < 0L || var2 > var0 || var0 - var2 < var4) {
         throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", var0, var2, var4));
      }
   }

   public static int reverseBytesInt(int var0) {
      return (-16777216 & var0) >>> 24 | (16711680 & var0) >>> 8 | ('\uff00' & var0) << 8 | (var0 & 255) << 24;
   }

   public static long reverseBytesLong(long var0) {
      return (-72057594037927936L & var0) >>> 56 | (71776119061217280L & var0) >>> 40 | (280375465082880L & var0) >>> 24 | (1095216660480L & var0) >>> 8 | (4278190080L & var0) << 8 | (16711680L & var0) << 24 | (65280L & var0) << 40 | (255L & var0) << 56;
   }

   public static short reverseBytesShort(short var0) {
      int var1 = '\uffff' & var0;
      return (short)(('\uff00' & var1) >>> 8 | (var1 & 255) << 8);
   }

   public static void sneakyRethrow(Throwable var0) {
      sneakyThrow2(var0);
   }

   private static void sneakyThrow2(Throwable var0) throws Throwable {
      throw var0;
   }
}
