package net.sf.fmj.media.util;

import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import org.atalk.android.util.java.awt.Component;

public class AudioCodecChain extends CodecChain {
   Component gainComp = null;

   public AudioCodecChain(AudioFormat var1) throws UnsupportedFormatException {
      if (this.buildChain(var1)) {
         this.renderer.close();
         this.firstBuffer = false;
      } else {
         throw new UnsupportedFormatException(var1);
      }
   }

   public Component getControlComponent() {
      Component var1 = this.gainComp;
      return var1 != null ? var1 : var1;
   }

   public void reset() {
   }
}
