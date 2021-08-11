package net.sf.fmj.media.multiplexer.audio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.FileTypeDescriptor;
import net.sf.fmj.media.multiplexer.AbstractStreamCopyMux;
import net.sf.fmj.media.multiplexer.StreamCopyPushDataSource;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.javax.sound.sampled.AudioInputStream;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.AudioFileFormat.Type;

public abstract class JavaSoundMux extends AbstractStreamCopyMux {
   private static final int MAX_TRACKS = 1;
   private static final Logger logger;
   private final Type audioFileFormatType;

   static {
      logger = LoggerSingleton.logger;
   }

   public JavaSoundMux(FileTypeDescriptor var1, Type var2) {
      super(var1);
      this.audioFileFormatType = var2;
   }

   protected StreamCopyPushDataSource createInputStreamPushDataSource(ContentDescriptor var1, int var2, InputStream[] var3, Format[] var4) {
      return new JavaSoundMux.MyPushDataSource(var1, var2, var3, var4);
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat("LINEAR")};
   }

   public int setNumTracks(int var1) {
      byte var2 = 1;
      if (var1 > 1) {
         var1 = var2;
      }

      return super.setNumTracks(var1);
   }

   protected void write(InputStream var1, OutputStream var2, org.atalk.android.util.javax.sound.sampled.AudioFormat var3) throws IOException {
      int var4 = AudioSystem.write(new AudioInputStream(var1, var3, 2147483647L), this.audioFileFormatType, var2);
      Logger var5 = logger;
      StringBuilder var6 = new StringBuilder();
      var6.append("Audio OutputStream bytes written: ");
      var6.append(var4);
      var5.fine(var6.toString());
   }

   private class MyPushDataSource extends StreamCopyPushDataSource {
      final org.atalk.android.util.javax.sound.sampled.AudioFormat[] javaSoundFormats;

      public MyPushDataSource(ContentDescriptor var2, int var3, InputStream[] var4, Format[] var5) {
         super(var2, var3, var4, var5);
         this.javaSoundFormats = new org.atalk.android.util.javax.sound.sampled.AudioFormat[var3];

         for(int var6 = 0; var6 < var3; ++var6) {
            this.javaSoundFormats[var6] = JavaSoundUtils.convertFormat((AudioFormat)var5[var6]);
         }

      }

      protected void write(InputStream var1, OutputStream var2, int var3) throws IOException {
         JavaSoundMux.this.write(var1, var2, this.javaSoundFormats[var3]);
      }
   }
}
