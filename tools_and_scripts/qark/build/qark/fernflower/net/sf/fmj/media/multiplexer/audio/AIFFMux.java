package net.sf.fmj.media.multiplexer.audio;

import javax.media.protocol.FileTypeDescriptor;
import org.atalk.android.util.javax.sound.sampled.AudioFileFormat.Type;

public class AIFFMux extends JavaSoundMux {
   public AIFFMux() {
      super(new FileTypeDescriptor("audio.x_aiff"), Type.AIFF);
   }
}
