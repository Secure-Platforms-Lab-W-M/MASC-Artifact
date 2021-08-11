package net.sf.fmj.media.codec.audio.gsm;

import org.rubycoder.gsm.GSMDecoder;
import org.rubycoder.gsm.InvalidGSMFrameException;

public class GSMDecoderUtil {
   private static final int GSM_BYTES = 33;
   private static final int PCM_BYTES = 320;
   private static final int PCM_INTS = 160;
   private static GSMDecoder decoder = new GSMDecoder();

   public static void gsmDecode(boolean var0, byte[] var1, int var2, int var3, byte[] var4) {
      for(var2 = 0; var2 < var3 / 33; ++var2) {
         int[] var7 = new int[160];
         byte[] var8 = new byte[33];
         System.arraycopy(var1, var2 * 33, var8, 0, 33);

         try {
            decoder.decode(var8, var7);
         } catch (InvalidGSMFrameException var9) {
            var9.printStackTrace();
         }

         for(int var5 = 0; var5 < 160; ++var5) {
            int var6 = var5 << 1;
            if (var0) {
               var4[var2 * 320 + var6] = (byte)(('\uff00' & var7[var5]) >> 8);
               var4[var2 * 320 + var6 + 1] = (byte)(var7[var5] & 255);
            } else {
               var4[var2 * 320 + var6] = (byte)(var7[var5] & 255);
               var4[var2 * 320 + var6 + 1] = (byte)(('\uff00' & var7[var5]) >> 8);
            }
         }
      }

   }
}
