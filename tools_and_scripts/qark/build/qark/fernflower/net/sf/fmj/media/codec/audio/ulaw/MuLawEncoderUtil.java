package net.sf.fmj.media.codec.audio.ulaw;

import com.lti.utils.UnsignedUtils;

public class MuLawEncoderUtil {
   public static final int BIAS = 132;
   public static final int MAX = 32635;
   private static byte[] pcmToMuLawMap = new byte[65536];

   static {
      for(int var0 = -32768; var0 <= 32767; ++var0) {
         pcmToMuLawMap[UnsignedUtils.uShortToInt((short)var0)] = encode(var0);
      }

   }

   private static byte encode(int var0) {
      int var2 = ('耀' & var0) >> 8;
      int var1 = var0;
      if (var2 != 0) {
         var1 = -var0;
      }

      var0 = var1;
      if (var1 > 32635) {
         var0 = 32635;
      }

      int var3 = var0 + 132;
      var1 = 7;

      for(var0 = 16384; (var3 & var0) == 0; var0 >>= 1) {
         --var1;
      }

      return (byte)((byte)(var1 << 4 | var2 | var3 >> var1 + 3 & 15));
   }

   public static byte muLawEncode(int var0) {
      return pcmToMuLawMap['\uffff' & var0];
   }

   public static byte muLawEncode(short var0) {
      return pcmToMuLawMap[UnsignedUtils.uShortToInt(var0)];
   }

   public static void muLawEncode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      if (var0) {
         muLawEncodeBigEndian(var1, var2, var3, var4);
      } else {
         muLawEncodeLittleEndian(var1, var2, var3, var4);
      }
   }

   public static void muLawEncodeBigEndian(byte[] var0, int var1, int var2, byte[] var3) {
      int var4 = var2 / 2;

      for(var2 = 0; var2 < var4; ++var2) {
         var3[var2] = muLawEncode(var0[var2 * 2 + var1 + 1] & 255 | (var0[var2 * 2 + var1] & 255) << 8);
      }

   }

   public static void muLawEncodeLittleEndian(byte[] var0, int var1, int var2, byte[] var3) {
      int var4 = var2 / 2;

      for(var2 = 0; var2 < var4; ++var2) {
         var3[var2] = muLawEncode((var0[var2 * 2 + var1 + 1] & 255) << 8 | var0[var2 * 2 + var1] & 255);
      }

   }

   public boolean getZeroTrap() {
      return pcmToMuLawMap['胨'] != 0;
   }

   public void setZeroTrap(boolean var1) {
      byte var3;
      if (var1) {
         var3 = 2;
      } else {
         var3 = 0;
      }

      byte var2 = (byte)var3;

      for(int var4 = 32768; var4 <= 33924; ++var4) {
         pcmToMuLawMap[var4] = var2;
      }

   }
}
