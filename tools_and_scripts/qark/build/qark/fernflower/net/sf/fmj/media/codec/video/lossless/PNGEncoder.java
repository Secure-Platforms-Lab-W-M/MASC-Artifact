package net.sf.fmj.media.codec.video.lossless;

import javax.media.Format;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.codec.video.ImageIOEncoder;
import net.sf.fmj.media.format.PNGFormat;

public class PNGEncoder extends ImageIOEncoder {
   private final Format[] supportedOutputFormats = new Format[]{new PNGFormat()};

   public PNGEncoder() {
      super("PNG");
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.supportedOutputFormats;
      } else {
         VideoFormat var2 = (VideoFormat)var1;
         return new Format[]{new PNGFormat(var2.getSize(), -1, Format.byteArray, var2.getFrameRate())};
      }
   }
}
