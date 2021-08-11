package net.sf.fmj.media.rtp;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;

public class FormatInfo {
   public static final int PAYLOAD_NOTFOUND = -1;
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   private SSRCCache cache = null;
   Format[] formatList = new Format[111];

   public FormatInfo() {
      this.initFormats();
   }

   private void expandTable(int var1) {
      Format[] var2 = new Format[var1 + 1];
      var1 = 0;

      while(true) {
         Format[] var3 = this.formatList;
         if (var1 >= var3.length) {
            this.formatList = var2;
            return;
         }

         var2[var1] = var3[var1];
         ++var1;
      }
   }

   public static boolean isSupported(int var0) {
      if (var0 != 0 && var0 != 8 && var0 != 26 && var0 != 34 && var0 != 3 && var0 != 4 && var0 != 5 && var0 != 6 && var0 != 31 && var0 != 32) {
         switch(var0) {
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
            break;
         default:
            return false;
         }
      }

      return true;
   }

   public void add(int var1, Format var2) {
      if (var1 >= this.formatList.length) {
         this.expandTable(var1);
      }

      Format var3 = this.formatList[var1];
      if (var3 == null || var2 != null && var3.matches(var2) && var2.matches(var3)) {
         this.formatList[var1] = var2;
         SSRCCache var4 = this.cache;
         if (var4 != null && var2 instanceof VideoFormat) {
            var4.clockrate[var1] = 90000;
         }

         if (this.cache != null && var2 instanceof AudioFormat) {
            if (mpegAudio.matches(var2)) {
               this.cache.clockrate[var1] = 90000;
               return;
            }

            this.cache.clockrate[var1] = (int)((AudioFormat)var2).getSampleRate();
         }

      }
   }

   public Format get(int var1) {
      Format[] var2 = this.formatList;
      return var1 >= var2.length ? null : var2[var1];
   }

   public int getPayload(Format var1) {
      Object var3 = var1;
      if (var1.getEncoding() != null) {
         var3 = var1;
         if (var1.getEncoding().equals("g729a/rtp")) {
            var3 = new AudioFormat("g729/rtp");
         }
      }

      int var2 = 0;

      while(true) {
         Format[] var4 = this.formatList;
         if (var2 >= var4.length) {
            return -1;
         }

         if (((Format)var3).matches(var4[var2])) {
            return var2;
         }

         ++var2;
      }
   }

   public void initFormats() {
      this.formatList[0] = new AudioFormat("ULAW/rtp", 8000.0D, 8, 1);
      this.formatList[3] = new AudioFormat("gsm/rtp", 8000.0D, -1, 1);
      this.formatList[4] = new AudioFormat("g723/rtp", 8000.0D, -1, 1);
      this.formatList[5] = new AudioFormat("dvi/rtp", 8000.0D, 4, 1);
      this.formatList[8] = new AudioFormat("ALAW/rtp", 8000.0D, 8, 1);
      this.formatList[14] = new AudioFormat("mpegaudio/rtp", -1.0D, -1, -1);
      this.formatList[15] = new AudioFormat("g728/rtp", 8000.0D, -1, 1);
      this.formatList[16] = new AudioFormat("dvi/rtp", 11025.0D, 4, 1);
      this.formatList[17] = new AudioFormat("dvi/rtp", 22050.0D, 4, 1);
      this.formatList[18] = new AudioFormat("g729/rtp", 8000.0D, -1, 1);
      this.formatList[26] = new VideoFormat("jpeg/rtp");
      this.formatList[31] = new VideoFormat("h261/rtp");
      this.formatList[32] = new VideoFormat("mpeg/rtp");
      this.formatList[34] = new VideoFormat("h263/rtp");
   }

   public void setCache(SSRCCache var1) {
      this.cache = var1;
   }
}
