package net.sf.fmj.media.codec.audio.alaw;

import java.util.logging.Logger;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractPacketizer;
import net.sf.fmj.utility.LoggerSingleton;

public class Packetizer extends AbstractPacketizer {
   private static final int PACKET_SIZE = 480;
   private static final Logger logger;
   protected Format[] outputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public Packetizer() {
      this.outputFormats = new Format[]{new AudioFormat("ALAW/rtp", -1.0D, 8, 1, -1, -1, 8, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("alaw", -1.0D, 8, 1, -1, -1, 8, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "ALAW Packetizer";
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
            if (!var2.getEncoding().equals("alaw") || var2.getSampleSizeInBits() != 8 && var2.getSampleSizeInBits() != -1 || var2.getChannels() != 1 && var2.getChannels() != -1 || var2.getFrameSizeInBits() != 8 && var2.getFrameSizeInBits() != -1) {
               var4 = logger;
               var3 = new StringBuilder();
               var3.append(this.getClass().getSimpleName());
               var3.append(".getSupportedOutputFormats: input format does not match, returning format array of {null} for ");
               var3.append(var1);
               var4.warning(var3.toString());
               return new Format[]{null};
            } else {
               return new Format[]{new AudioFormat("ALAW/rtp", var2.getSampleRate(), 8, 1, var2.getEndian(), var2.getSigned(), 8, var2.getFrameRate(), var2.getDataType())};
            }
         }
      }
   }

   public void open() {
      this.setPacketSize(480);
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return super.setOutputFormat(var1);
   }
}
