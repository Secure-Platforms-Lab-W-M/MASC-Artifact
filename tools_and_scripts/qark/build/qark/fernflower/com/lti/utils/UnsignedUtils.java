package com.lti.utils;

public final class UnsignedUtils {
   public static final int MAX_UBYTE = 255;
   public static final long MAX_UINT = 4294967295L;
   public static final int MAX_USHORT = 65535;

   private UnsignedUtils() {
   }

   public static int uByteToInt(byte var0) {
      return var0 >= 0 ? var0 : var0 + 256;
   }

   public static long uIntToLong(int var0) {
      return var0 >= 0 ? (long)var0 : (long)var0 + 4294967296L;
   }

   public static int uShortToInt(short var0) {
      return var0 >= 0 ? var0 : 65536 + var0;
   }
}
