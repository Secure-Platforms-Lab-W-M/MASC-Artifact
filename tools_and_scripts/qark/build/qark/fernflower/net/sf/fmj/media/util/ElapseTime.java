package net.sf.fmj.media.util;

import javax.media.Format;
import javax.media.format.AudioFormat;

public class ElapseTime {
   public long value = 0L;

   public static long audioLenToTime(long var0, AudioFormat var2) {
      return var2.computeDuration(var0);
   }

   public static long audioTimeToLen(long var0, AudioFormat var2) {
      long var3;
      long var5;
      if (var2.getSampleSizeInBits() > 0) {
         var5 = (long)(var2.getSampleSizeInBits() * var2.getChannels());
         var3 = (long)((double)var5 * var2.getSampleRate() / 8.0D);
      } else if (var2.getFrameSizeInBits() != -1 && var2.getFrameRate() != -1.0D) {
         var5 = (long)var2.getFrameSizeInBits();
         var3 = (long)((double)var5 * var2.getFrameRate() / 8.0D);
      } else {
         var3 = 0L;
         var5 = 0L;
      }

      return var3 == 0L ? 0L : var0 * var3 / 1000000000L / var5 * var5;
   }

   public long getValue() {
      return this.value;
   }

   public void setValue(long var1) {
      this.value = var1;
   }

   public boolean update(int var1, long var2, Format var4) {
      if (var4 instanceof AudioFormat) {
         long var5 = ((AudioFormat)var4).computeDuration((long)var1);
         if (var5 > 0L) {
            this.value += var5;
         } else {
            if (var2 <= 0L) {
               return false;
            }

            this.value = var2;
         }
      } else {
         if (var2 <= 0L) {
            return false;
         }

         this.value = var2;
      }

      return true;
   }
}
