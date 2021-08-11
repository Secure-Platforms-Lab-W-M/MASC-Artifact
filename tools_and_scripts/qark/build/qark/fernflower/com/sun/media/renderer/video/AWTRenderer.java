package com.sun.media.renderer.video;

import javax.media.Format;
import javax.media.format.RGBFormat;
import net.sf.fmj.media.renderer.video.SimpleAWTRenderer;
import org.atalk.android.util.java.awt.Dimension;

public class AWTRenderer extends SimpleAWTRenderer {
   private final Format[] supportedInputFormats;

   public AWTRenderer() {
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 16711680, 65280, 255, 1, -1, 0, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 255, 65280, 16711680, 1, -1, 0, -1)};
   }

   public String getName() {
      return "AWT Renderer";
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }
}
