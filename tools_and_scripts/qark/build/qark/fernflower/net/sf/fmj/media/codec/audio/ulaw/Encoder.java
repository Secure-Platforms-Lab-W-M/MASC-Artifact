package net.sf.fmj.media.codec.audio.ulaw;

import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.utility.LoggerSingleton;

public class Encoder extends AbstractCodec {
   private static final boolean TRACE = false;
   private static final Logger logger;
   protected Format[] outputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public Encoder() {
      this.outputFormats = new Format[]{new AudioFormat("ULAW", -1.0D, 8, 1, -1, 1, 8, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("LINEAR", -1.0D, 16, 1, -1, 1, 16, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "ULAW Encoder";
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
            if (!var2.getEncoding().equals("LINEAR") || var2.getSampleSizeInBits() != 16 && var2.getSampleSizeInBits() != -1 || var2.getChannels() != 1 && var2.getChannels() != -1 || var2.getSigned() != 1 && var2.getSigned() != -1 || var2.getFrameSizeInBits() != 16 && var2.getFrameSizeInBits() != -1 || var2.getDataType() != null && var2.getDataType() != Format.byteArray) {
               var4 = logger;
               var3 = new StringBuilder();
               var3.append(this.getClass().getSimpleName());
               var3.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
               var3.append(var1);
               var4.warning(var3.toString());
               return new Format[]{null};
            } else {
               return new Format[]{new AudioFormat("ULAW", var2.getSampleRate(), 8, 1, -1, 1, 8, var2.getFrameRate(), Format.byteArray)};
            }
         }
      }
   }

   public void open() {
   }

   public int process(Buffer var1, Buffer var2) {
      boolean var5 = this.checkInputBuffer(var1);
      boolean var4 = true;
      if (!var5) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         int var3;
         byte[] var6;
         AudioFormat var8;
         label29: {
            var8 = (AudioFormat)var1.getFormat();
            byte[] var7 = (byte[])((byte[])var2.getData());
            var3 = var1.getLength() / 2;
            if (var7 != null) {
               var6 = var7;
               if (var7.length >= var3) {
                  break label29;
               }
            }

            var6 = new byte[var3];
            var2.setData(var6);
         }

         if (var8.equals(this.inputFormat)) {
            if (var8.getEndian() != -1) {
               if (var8.getEndian() != 1) {
                  var4 = false;
               }

               MuLawEncoderUtil.muLawEncode(var4, (byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength(), var6);
               var2.setLength(var3);
               var2.setOffset(0);
               var2.setFormat(this.outputFormat);
               return 0;
            } else {
               throw new RuntimeException("Unspecified endian-ness");
            }
         } else {
            throw new RuntimeException("Incorrect input format");
         }
      }
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return super.setOutputFormat(var1);
   }
}
