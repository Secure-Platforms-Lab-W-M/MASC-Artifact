package net.sf.fmj.media.protocol.javasound;

import java.io.File;
import java.io.IOException;
import org.atalk.android.util.javax.sound.sampled.AudioFormat;
import org.atalk.android.util.javax.sound.sampled.AudioInputStream;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.LineUnavailableException;
import org.atalk.android.util.javax.sound.sampled.TargetDataLine;
import org.atalk.android.util.javax.sound.sampled.AudioFileFormat.Type;
import org.atalk.android.util.javax.sound.sampled.AudioFormat.Encoding;
import org.atalk.android.util.javax.sound.sampled.DataLine.Info;

public class SimpleAudioRecorder extends Thread {
   private AudioInputStream m_audioInputStream;
   private TargetDataLine m_line;
   private File m_outputFile;
   private Type m_targetType;

   public SimpleAudioRecorder(TargetDataLine var1, Type var2, File var3) {
      this.m_line = var1;
      this.m_audioInputStream = new AudioInputStream(var1);
      this.m_targetType = var2;
      this.m_outputFile = var3;
   }

   public static void main(String[] var0) {
      if (var0.length != 1 || var0[0].equals("-h")) {
         printUsageAndExit();
      }

      File var2 = new File(var0[0]);
      AudioFormat var3 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
      Info var1 = new Info(TargetDataLine.class, var3);
      TargetDataLine var8 = null;

      label44: {
         TargetDataLine var9;
         label43: {
            LineUnavailableException var10000;
            label53: {
               boolean var10001;
               try {
                  var9 = (TargetDataLine)AudioSystem.getLine(var1);
               } catch (LineUnavailableException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label53;
               }

               var8 = var9;

               try {
                  var9.open(var3);
                  break label43;
               } catch (LineUnavailableException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            LineUnavailableException var10 = var10000;
            out("unable to get a recording line");
            var10.printStackTrace();
            System.exit(1);
            break label44;
         }

         var8 = var9;
      }

      SimpleAudioRecorder var11 = new SimpleAudioRecorder(var8, Type.WAVE, var2);
      out("Press ENTER to start the recording.");

      try {
         System.in.read();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      var11.start();
      out("Recording...");
      out("Press ENTER to stop the recording.");

      try {
         System.in.read();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      var11.stopRecording();
      out("Recording stopped.");
   }

   private static void out(String var0) {
      System.out.println(var0);
   }

   private static void printUsageAndExit() {
      out("SimpleAudioRecorder: usage:");
      out("\tjava SimpleAudioRecorder -h");
      out("\tjava SimpleAudioRecorder <audiofile>");
      System.exit(0);
   }

   public void run() {
      try {
         AudioSystem.write(this.m_audioInputStream, this.m_targetType, this.m_outputFile);
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public void start() {
      this.m_line.start();
      super.start();
   }

   public void stopRecording() {
      this.m_line.stop();
      this.m_line.close();
   }
}
