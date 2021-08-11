package net.sf.fmj.media.codec.audio.alaw;

import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.AudioFormatCompleter;
import net.sf.fmj.utility.LoggerSingleton;

public class Decoder extends AbstractCodec {
   private static final boolean TRACE = false;
   private static final Logger logger;
   protected Format[] outputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public Decoder() {
      this.outputFormats = new Format[]{new AudioFormat("LINEAR", -1.0D, 16, 1, -1, 1, 16, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("alaw", -1.0D, 8, 1, -1, 1, 8, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "ALAW Decoder";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else {
         StringBuilder var3;
         Logger var4;
         if (!(var1 instanceof AudioFormat)) {
            var4 = logger;
            var3 = new StringBuilder();
            var3.append(this.getClass().getSimpleName());
            var3.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
            var3.append(var1);
            var4.warning(var3.toString());
            return new Format[]{null};
         } else {
            AudioFormat var2 = (AudioFormat)var1;
            if (!var2.getEncoding().equals("alaw") || var2.getSampleSizeInBits() != 8 && var2.getSampleSizeInBits() != -1 || var2.getChannels() != 1 && var2.getChannels() != -1 || var2.getSigned() != 1 && var2.getSigned() != -1 || var2.getFrameSizeInBits() != 8 && var2.getFrameSizeInBits() != -1 || var2.getDataType() != null && var2.getDataType() != Format.byteArray) {
               var4 = logger;
               var3 = new StringBuilder();
               var3.append(this.getClass().getSimpleName());
               var3.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
               var3.append(var1);
               var4.warning(var3.toString());
               return new Format[]{null};
            } else {
               return new Format[]{new AudioFormat("LINEAR", var2.getSampleRate(), 16, 1, var2.getEndian(), 1, 16, var2.getFrameRate(), Format.byteArray)};
            }
         }
      }
   }

   public void open() {
   }

   public int process(Buffer var1, Buffer var2) {
      boolean var4 = this.checkInputBuffer(var1);
      boolean var3 = true;
      if (!var4) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         byte[] var5;
         label21: {
            byte[] var6 = (byte[])((byte[])var2.getData());
            if (var6 != null) {
               var5 = var6;
               if (var6.length >= var1.getLength() * 2) {
                  break label21;
               }
            }

            var5 = new byte[var1.getLength() * 2];
            var2.setData(var5);
         }

         if (((AudioFormat)this.outputFormat).getEndian() != 1) {
            var3 = false;
         }

         ALawDecoderUtil.aLawDecode(var3, (byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength(), var5);
         var2.setLength(var1.getLength() * 2);
         var2.setOffset(0);
         var2.setFormat(this.outputFormat);
         return 0;
      }
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return !(var1 instanceof AudioFormat) ? null : super.setOutputFormat(AudioFormatCompleter.complete((AudioFormat)var1));
   }
}
