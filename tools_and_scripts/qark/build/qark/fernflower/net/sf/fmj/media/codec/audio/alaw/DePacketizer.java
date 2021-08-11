package net.sf.fmj.media.codec.audio.alaw;

import java.util.logging.Logger;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractDePacketizer;
import net.sf.fmj.utility.LoggerSingleton;

public class DePacketizer extends AbstractDePacketizer {
   private static final Logger logger;
   protected Format[] outputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public DePacketizer() {
      this.outputFormats = new Format[]{new AudioFormat("alaw", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("ALAW/rtp", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "ALAW DePacketizer";
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
            if (!var2.getEncoding().equals("ALAW/rtp")) {
               var4 = logger;
               var3 = new StringBuilder();
               var3.append(this.getClass().getSimpleName());
               var3.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
               var3.append(var1);
               var4.warning(var3.toString());
               return new Format[]{null};
            } else {
               return new Format[]{new AudioFormat("alaw", var2.getSampleRate(), var2.getSampleSizeInBits(), var2.getChannels(), var2.getEndian(), var2.getSigned(), var2.getFrameSizeInBits(), var2.getFrameRate(), var2.getDataType())};
            }
         }
      }
   }

   public void open() {
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return super.setOutputFormat(var1);
   }
}
