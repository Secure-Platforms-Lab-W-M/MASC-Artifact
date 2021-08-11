package net.sf.fmj.media.rtp;

import net.sf.fmj.media.rtp.util.SSRCTable;

public class StreamSynch {
   private static SSRCTable sources;

   public StreamSynch() {
      if (sources == null) {
         sources = new SSRCTable();
      }

   }

   public long calcTimestamp(int var1, int var2, long var3) {
      long var5 = -1L;
      SynchSource var7 = (SynchSource)sources.get(var1);
      if (var7 != null) {
         var5 = 1L;
         if (var2 >= 0 && var2 <= 5) {
            var5 = 8000L;
         } else if (var2 == 5) {
            var5 = 8000L;
         } else if (var2 == 6) {
            var5 = 16000L;
         } else if (var2 >= 7 && var2 <= 9) {
            var5 = 8000L;
         } else if (var2 >= 10 && var2 <= 11) {
            var5 = 44100L;
         } else if (var2 == 14) {
            var5 = 90000L;
         } else if (var2 == 15) {
            var5 = 8000L;
         } else if (var2 == 16) {
            var5 = 11025L;
         } else if (var2 == 17) {
            var5 = 22050L;
         } else if (var2 >= 25 && var2 <= 26) {
            var5 = 90000L;
         } else if (var2 == 28) {
            var5 = 90000L;
         } else if (var2 >= 31 && var2 <= 34) {
            var5 = 90000L;
         } else if (var2 == 42) {
            var5 = 90000L;
         }

         var5 = var7.ntpTimestamp + (var3 - var7.rtpTimestamp) * 1000L * 1000L * 1000L / var5;
      }

      return var5;
   }

   public void remove(int var1) {
      SSRCTable var2 = sources;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   public void update(int var1, long var2, long var4, long var6) {
      var4 = 1000000000L * var4 + (long)(1.0E9D * ((double)var6 / 4.294967296E9D));
      SynchSource var8 = (SynchSource)sources.get(var1);
      if (var8 == null) {
         sources.put(var1, new SynchSource(var1, var2, var4));
      } else {
         var8.factor = (double)((var2 - var8.rtpTimestamp) * (var4 - var8.ntpTimestamp));
         var8.rtpTimestamp = var2;
         var8.ntpTimestamp = var4;
      }
   }
}
