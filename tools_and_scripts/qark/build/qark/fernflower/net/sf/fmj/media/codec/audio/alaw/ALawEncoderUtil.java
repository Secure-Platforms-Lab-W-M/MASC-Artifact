package net.sf.fmj.media.codec.audio.alaw;

import com.lti.utils.UnsignedUtils;

public class ALawEncoderUtil {
   public static final int MAX = 32767;
   private static byte[] pcmToALawMap = new byte[65536];

   static {
      for(int var0 = -32768; var0 <= 32767; ++var0) {
         pcmToALawMap[UnsignedUtils.uShortToInt((short)var0)] = encode(var0);
      }

   }

   public static byte aLawEncode(int var0) {
      return pcmToALawMap[UnsignedUtils.uShortToInt((short)('\uffff' & var0))];
   }

   public static byte aLawEncode(short var0) {
      return pcmToALawMap[UnsignedUtils.uShortToInt(var0)];
   }

   public static void aLawEncode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      if (var0) {
         aLawEncodeBigEndian(var1, var2, var3, var4);
      } else {
         aLawEncodeLittleEndian(var1, var2, var3, var4);
      }
   }

   public static void aLawEncodeBigEndian(byte[] var0, int var1, int var2, byte[] var3) {
      int var4 = var2 / 2;

      for(var2 = 0; var2 < var4; ++var2) {
         var3[var2] = aLawEncode(var0[var2 * 2 + var1 + 1] & 255 | (var0[var2 * 2 + var1] & 255) << 8);
      }

   }

   public static void aLawEncodeLittleEndian(byte[] var0, int var1, int var2, byte[] var3) {
      int var4 = var2 / 2;

      for(var2 = 0; var2 < var4; ++var2) {
         var3[var2] = aLawEncode((var0[var2 * 2 + var1 + 1] & 255) << 8 | var0[var2 * 2 + var1] & 255);
      }

   }

   private static byte encode(int var0) {
      int var3 = ('耀' & var0) >> 8;
      int var1 = var0;
      if (var3 != 0) {
         var1 = -var0;
      }

      var0 = var1;
      if (var1 > 32767) {
         var0 = 32767;
      }

      var1 = 7;

      int var2;
      for(var2 = 16384; (var0 & var2) == 0 && var1 > 0; var2 >>= 1) {
         --var1;
      }

      if (var1 == 0) {
         var2 = 4;
      } else {
         var2 = var1 + 3;
      }

      return (byte)((byte)(var1 << 4 | var3 | var0 >> var2 & 15) ^ 213);
   }
}
