package net.sf.fmj.media.codec.audio.gsm;

import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractPacketizer;

public class Packetizer extends AbstractPacketizer {
   private static final int PACKET_SIZE = 33;
   protected Format[] outputFormats;

   public Packetizer() {
      this.outputFormats = new Format[]{new AudioFormat("gsm/rtp", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("gsm", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
   }

   public void close() {
   }

   public String getName() {
      return "GSM Packetizer";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else if (!(var1 instanceof AudioFormat)) {
         return new Format[]{null};
      } else {
         AudioFormat var2 = (AudioFormat)var1;
         return !var2.getEncoding().equals("gsm") || var2.getSampleSizeInBits() != 8 && var2.getSampleSizeInBits() != -1 || var2.getChannels() != 1 && var2.getChannels() != -1 || var2.getFrameSizeInBits() != 264 && var2.getFrameSizeInBits() != -1 ? new Format[]{null} : new Format[]{new AudioFormat("gsm/rtp", var2.getSampleRate(), 8, 1, var2.getEndian(), var2.getSigned(), 264, var2.getFrameRate(), var2.getDataType())};
      }
   }

   public void open() {
      this.setPacketSize(33);
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return super.setOutputFormat(var1);
   }
}
