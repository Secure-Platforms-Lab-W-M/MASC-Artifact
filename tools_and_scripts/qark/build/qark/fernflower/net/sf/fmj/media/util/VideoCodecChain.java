package net.sf.fmj.media.util;

import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.renderer.VideoRenderer;
import org.atalk.android.util.java.awt.Component;

public class VideoCodecChain extends CodecChain {
   public VideoCodecChain(VideoFormat var1) throws UnsupportedFormatException {
      if (var1.getSize() != null && var1 != null) {
         if (!this.buildChain(var1)) {
            throw new UnsupportedFormatException(var1);
         }
      } else {
         throw new UnsupportedFormatException(var1);
      }
   }

   public Component getControlComponent() {
      return this.renderer instanceof VideoRenderer ? ((VideoRenderer)this.renderer).getComponent() : null;
   }

   boolean isRawFormat(Format var1) {
      return var1 instanceof RGBFormat || var1 instanceof YUVFormat || var1.getEncoding() != null && (var1.getEncoding().equalsIgnoreCase("jpeg") || var1.getEncoding().equalsIgnoreCase("mpeg"));
   }
}
