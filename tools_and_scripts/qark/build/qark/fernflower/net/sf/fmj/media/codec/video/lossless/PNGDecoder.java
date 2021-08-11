package net.sf.fmj.media.codec.video.lossless;

import javax.media.Format;
import net.sf.fmj.media.codec.video.ImageIODecoder;
import net.sf.fmj.media.format.PNGFormat;

public class PNGDecoder extends ImageIODecoder {
   private final Format[] supportedInputFormats = new Format[]{new PNGFormat()};

   public PNGDecoder() {
      super("PNG");
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }
}
