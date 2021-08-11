package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Renderer;

public abstract class AbstractRenderer extends AbstractPlugIn implements Renderer {
   protected Format inputFormat;

   public abstract Format[] getSupportedInputFormats();

   public abstract int process(Buffer var1);

   public Format setInputFormat(Format var1) {
      this.inputFormat = var1;
      return var1;
   }

   public void start() {
   }

   public void stop() {
   }
}
