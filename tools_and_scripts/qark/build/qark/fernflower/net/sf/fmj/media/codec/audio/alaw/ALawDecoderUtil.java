package net.sf.fmj.media.codec.audio.alaw;

public class ALawDecoderUtil {
   private static short[] aLawToPcmMap = new short[256];

   static {
      int var0 = 0;

      while(true) {
         short[] var1 = aLawToPcmMap;
         if (var0 >= var1.length) {
            return;
         }

         var1[var0] = decode((byte)var0);
         ++var0;
      }
   }

   public static short aLawDecode(byte var0) {
      return aLawToPcmMap[var0 & 255];
   }

   public static void aLawDecode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      if (var0) {
         aLawDecodeBigEndian(var1, var2, var3, var4);
      } else {
         aLawDecodeLittleEndian(var1, var2, var3, var4);
      }
   }

   public static void aLawDecodeBigEndian(byte[] var0, int var1, int var2, byte[] var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         short[] var5 = aLawToPcmMap;
         var3[var4 * 2 + 1] = (byte)(var5[var0[var1 + var4] & 255] & 255);
         var3[var4 * 2] = (byte)(var5[var0[var1 + var4] & 255] >> 8);
      }

   }

   public static void aLawDecodeLittleEndian(byte[] var0, int var1, int var2, byte[] var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         short[] var5 = aLawToPcmMap;
         var3[var4 * 2] = (byte)(var5[var0[var1 + var4] & 255] & 255);
         var3[var4 * 2 + 1] = (byte)(var5[var0[var1 + var4] & 255] >> 8);
      }

   }

   private static short decode(byte var0) {
      byte var2 = (byte)(var0 ^ 213);
      int var3 = (var2 & 112) >> 4;
      int var1 = ((var2 & 15) << 4) + 8;
      int var4 = var1;
      if (var3 != 0) {
         var4 = var1 + 256;
      }

      var1 = var4;
      if (var3 > 1) {
         var1 = var4 << var3 - 1;
      }

      if ((var2 & 128) != 0) {
         var1 = -var1;
      }

      return (short)var1;
   }
}
