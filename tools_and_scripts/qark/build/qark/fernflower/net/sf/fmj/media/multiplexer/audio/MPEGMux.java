package net.sf.fmj.media.multiplexer.audio;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.FileTypeDescriptor;
import net.sf.fmj.media.multiplexer.BasicMux;

public class MPEGMux extends BasicMux {
   public MPEGMux() {
      this.supportedInputs = new Format[2];
      this.supportedInputs[0] = new AudioFormat("mpeglayer3");
      this.supportedInputs[1] = new AudioFormat("mpegaudio");
      this.supportedOutputs = new ContentDescriptor[1];
      this.supportedOutputs[0] = new FileTypeDescriptor("audio.mpeg");
   }

   public String getName() {
      return "MPEG Audio Multiplexer";
   }

   public Format setInputFormat(Format var1, int var2) {
      if (!(var1 instanceof AudioFormat)) {
         return null;
      } else {
         AudioFormat var4 = (AudioFormat)var1;
         var4.getSampleRate();
         Object var3 = null;
         String var5 = (String)var3;
         if (!var4.getEncoding().equalsIgnoreCase("mpeglayer3")) {
            var5 = (String)var3;
            if (!var4.getEncoding().equalsIgnoreCase("mpegaudio")) {
               var5 = "Encoding has to be MPEG audio";
            }
         }

         if (var5 != null) {
            return null;
         } else {
            this.inputs[0] = var4;
            return var4;
         }
      }
   }
}
