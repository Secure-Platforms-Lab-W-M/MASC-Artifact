package net.sf.fmj.media.codec.audio.ulaw;

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
      this.inputFormats = new Format[]{new AudioFormat("ULAW", -1.0D, 8, 1, -1, 1, 8, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "ULAW Decoder";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else {
         StringBuilder var4;
         Logger var5;
         if (!(var1 instanceof AudioFormat)) {
            var5 = logger;
            var4 = new StringBuilder();
            var4.append(this.getClass().getSimpleName());
            var4.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
            var4.append(var1);
            var5.warning(var4.toString());
            return new Format[]{null};
         } else {
            AudioFormat var3 = (AudioFormat)var1;
            if (var3.getEncoding().equals("ULAW") && (var3.getSampleSizeInBits() == 8 || var3.getSampleSizeInBits() == -1) && (var3.getChannels() == 1 || var3.getChannels() == -1) && (var3.getSigned() == 1 || var3.getSigned() == -1) && (var3.getFrameSizeInBits() == 8 || var3.getFrameSizeInBits() == -1) && (var3.getDataType() == null || var3.getDataType() == Format.byteArray)) {
               int var2 = var3.getEndian();
               if (var3.getSampleSizeInBits() == 8) {
                  var2 = 0;
               }

               return new Format[]{new AudioFormat("LINEAR", var3.getSampleRate(), 16, 1, var2, 1, 16, var3.getFrameRate(), Format.byteArray)};
            } else {
               var5 = logger;
               var4 = new StringBuilder();
               var4.append(this.getClass().getSimpleName());
               var4.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
               var4.append(var1);
               var5.warning(var4.toString());
               return new Format[]{null};
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

         MuLawDecoderUtil.muLawDecode(var3, (byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength(), var5);
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
