package net.sf.fmj.media.multiplexer.audio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.protocol.FileTypeDescriptor;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.javax.sound.sampled.AudioFileFormat.Type;

public class WAVMux extends JavaSoundMux {
   private static final boolean USE_JAVASOUND = true;
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public WAVMux() {
      super(new FileTypeDescriptor("audio.x_wav"), Type.WAVE);
   }

   public Format setInputFormat(Format var1, int var2) {
      AudioFormat var3 = (AudioFormat)var1;
      if (var3.getSampleSizeInBits() == 8 && var3.getSigned() == 1) {
         return null;
      } else {
         return var3.getSampleSizeInBits() == 16 && var3.getSigned() == 0 ? null : super.setInputFormat(var1, var2);
      }
   }

   protected void write(InputStream var1, OutputStream var2, org.atalk.android.util.javax.sound.sampled.AudioFormat var3) throws IOException {
      super.write(var1, var2, var3);
   }
}
