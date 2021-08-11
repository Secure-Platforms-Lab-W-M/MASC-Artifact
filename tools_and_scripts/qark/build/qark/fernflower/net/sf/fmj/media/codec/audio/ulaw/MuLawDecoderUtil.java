package net.sf.fmj.media.codec.audio.ulaw;

public class MuLawDecoderUtil {
   private static short[] muLawToPcmMap = new short[256];

   static {
      short var0 = 0;

      while(true) {
         short[] var1 = muLawToPcmMap;
         if (var0 >= var1.length) {
            return;
         }

         var1[var0] = decode((byte)var0);
         ++var0;
      }
   }

   private static short decode(byte var0) {
      byte var1 = (byte)var0;
      int var2 = (((var1 & 15 | 16) << 1) + 1 << ((var1 & 112) >> 4) + 2) - 132;
      if ((var1 & 128) != 0) {
         var2 = -var2;
      }

      return (short)var2;
   }

   public static short muLawDecode(byte var0) {
      return muLawToPcmMap[var0 & 255];
   }

   public static void muLawDecode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      if (var0) {
         muLawDecodeBigEndian(var1, var2, var3, var4);
      } else {
         muLawDecodeLittleEndian(var1, var2, var3, var4);
      }
   }

   public static void muLawDecodeBigEndian(byte[] var0, int var1, int var2, byte[] var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         short[] var5 = muLawToPcmMap;
         var3[var4 * 2 + 1] = (byte)(var5[var0[var1 + var4] & 255] & 255);
         var3[var4 * 2] = (byte)(var5[var0[var1 + var4] & 255] >> 8 & 255);
      }

   }

   public static void muLawDecodeLittleEndian(byte[] var0, int var1, int var2, byte[] var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         short[] var5 = muLawToPcmMap;
         var3[var4 * 2] = (byte)(var5[var0[var1 + var4] & 255] & 255);
         var3[var4 * 2 + 1] = (byte)(var5[var0[var1 + var4] & 255] >> 8 & 255);
      }

   }
}
