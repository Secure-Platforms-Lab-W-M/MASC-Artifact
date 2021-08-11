package net.sf.fmj.media.codec.audio;

import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.BasicCodec;

public abstract class AudioCodec extends BasicCodec {
   public boolean checkFormat(Format var1) {
      Format var3 = this.inputFormat;
      boolean var2 = false;
      if (var3 == null || this.outputFormat == null || var1 != this.inputFormat || !var1.equals(this.inputFormat)) {
         this.inputFormat = var1;
         this.outputFormat = this.getSupportedOutputFormats(var1)[0];
      }

      if (this.outputFormat != null) {
         var2 = true;
      }

      return var2;
   }

   public Format setInputFormat(Format var1) {
      if (matches(var1, this.inputFormats) == null) {
         return null;
      } else {
         this.inputFormat = var1;
         return var1;
      }
   }

   public Format setOutputFormat(Format var1) {
      if (matches(var1, this.getSupportedOutputFormats(this.inputFormat)) == null) {
         return null;
      } else if (!(var1 instanceof AudioFormat)) {
         return null;
      } else {
         this.outputFormat = var1;
         return var1;
      }
   }
}
