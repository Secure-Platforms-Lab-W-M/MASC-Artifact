package net.sf.fmj.media.codec.audio.gsm;

import org.rubycoder.gsm.GSMEncoder;

public class GSMEncoderUtil {
   private static final int GSM_BYTES = 33;
   private static final int PCM_BYTES = 320;
   private static final int PCM_INTS = 160;
   private static GSMEncoder encoder = new GSMEncoder();

   public static void gsmEncode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      while(var2 < var3 / 320) {
         int[] var8 = new int[160];
         byte[] var9 = new byte[33];

         for(int var5 = 0; var5 < 160; ++var5) {
            int var6 = var5 << 1;
            int var7 = var6 + 1;
            var8[var5] = var1[var2 * 320 + var6];
            var8[var5] <<= 8;
            var8[var5] |= var1[var2 * 320 + var7] & 255;
         }

         encoder.encode(var9, var8);
         System.arraycopy(var9, 0, var4, var2 * 33, 33);
         ++var2;
      }

   }
}
