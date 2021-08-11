package com.sun.media.renderer.audio;

import javax.media.Format;
import javax.media.format.AudioFormat;

public class JavaSoundRenderer extends net.sf.fmj.media.renderer.audio.JavaSoundRenderer {
   private Format[] supportedInputFormats;

   public JavaSoundRenderer() {
      this.supportedInputFormats = new Format[]{new AudioFormat("LINEAR", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray), new AudioFormat("ULAW", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray)};
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }
}
