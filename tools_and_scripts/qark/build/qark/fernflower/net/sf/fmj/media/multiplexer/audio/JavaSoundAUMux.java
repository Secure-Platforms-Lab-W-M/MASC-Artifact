package net.sf.fmj.media.multiplexer.audio;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.protocol.FileTypeDescriptor;
import org.atalk.android.util.javax.sound.sampled.AudioFileFormat.Type;

public class JavaSoundAUMux extends JavaSoundMux {
   public JavaSoundAUMux() {
      super(new FileTypeDescriptor("audio.basic"), Type.AU);
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat("LINEAR", -1.0D, 8, -1, -1, 1), new AudioFormat("LINEAR", -1.0D, 16, -1, 1, 1), new AudioFormat("LINEAR", -1.0D, 24, -1, 1, 1), new AudioFormat("LINEAR", -1.0D, 32, -1, 1, 1), new AudioFormat("ULAW"), new AudioFormat("alaw")};
   }
}
