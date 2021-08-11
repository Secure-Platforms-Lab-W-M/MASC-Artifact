package net.sf.fmj.media.codec.audio.gsm;

import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractDePacketizer;

public class DePacketizer extends AbstractDePacketizer {
   protected Format[] outputFormats;

   public DePacketizer() {
      this.outputFormats = new Format[]{new AudioFormat("gsm", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("gsm/rtp", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "GSM DePacketizer";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else if (!(var1 instanceof AudioFormat)) {
         return new Format[]{null};
      } else {
         AudioFormat var2 = (AudioFormat)var1;
         return !var2.getEncoding().equals("gsm/rtp") ? new Format[]{null} : new Format[]{new AudioFormat("gsm", var2.getSampleRate(), var2.getSampleSizeInBits(), var2.getChannels(), var2.getEndian(), var2.getSigned(), var2.getFrameSizeInBits(), var2.getFrameRate(), var2.getDataType())};
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
