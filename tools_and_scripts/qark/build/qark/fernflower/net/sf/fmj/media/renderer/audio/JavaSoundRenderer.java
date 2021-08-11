package net.sf.fmj.media.renderer.audio;

import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.Owned;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import javax.media.control.BufferControl;
import javax.media.control.FrameProcessingControl;
import net.sf.fmj.media.AbstractGainControl;
import net.sf.fmj.utility.ControlCollection;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.javax.sound.sampled.AudioFormat;
import org.atalk.android.util.javax.sound.sampled.CompoundControl;
import org.atalk.android.util.javax.sound.sampled.Control;
import org.atalk.android.util.javax.sound.sampled.SourceDataLine;
import org.atalk.android.util.javax.sound.sampled.CompoundControl.Type;

public class JavaSoundRenderer implements Renderer {
   private static final boolean NON_BLOCKING = false;
   private static final Logger logger;
   private AudioFormat audioFormat;
   private Boolean bufferSizeChanged = new Boolean(false);
   private int buflen;
   private long buflenMS = -1L;
   private Codec codec;
   private final Buffer codecBuffer = new Buffer();
   private final ControlCollection controls = new ControlCollection();
   private int framesDropped = 0;
   private javax.media.format.AudioFormat inputFormat;
   private long lastSequenceNumber = -1L;
   private JavaSoundRenderer.PeakVolumeMeter levelControl;
   private String name = "FMJ Audio Renderer";
   private AudioFormat sampledFormat;
   private SourceDataLine sourceLine;
   private Format[] supportedInputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public JavaSoundRenderer() {
      this.supportedInputFormats = new Format[]{new javax.media.format.AudioFormat("LINEAR", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray), new javax.media.format.AudioFormat("ULAW", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray), new javax.media.format.AudioFormat("alaw", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray)};
      JavaSoundRenderer.PeakVolumeMeter var1 = new JavaSoundRenderer.PeakVolumeMeter();
      this.levelControl = var1;
      var1.setMute(true);
   }

   // $FF: synthetic method
   static long access$102(JavaSoundRenderer var0, long var1) {
      var0.buflenMS = var1;
      return var1;
   }

   // $FF: synthetic method
   static Boolean access$200(JavaSoundRenderer var0) {
      return var0.bufferSizeChanged;
   }

   // $FF: synthetic method
   static Boolean access$202(JavaSoundRenderer var0, Boolean var1) {
      var0.bufferSizeChanged = var1;
      return var1;
   }

   private void logControls(Control[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Control var3 = var1[var2];
         Logger var4 = logger;
         StringBuilder var5 = new StringBuilder();
         var5.append("control: ");
         var5.append(var3);
         var4.fine(var5.toString());
         if (var3.getType() instanceof Type) {
            this.logControls(((CompoundControl)var3).getMemberControls());
         }
      }

   }

   public void close() {
      logger.info("JavaSoundRenderer closing...");
      this.controls.clear();
      Codec var1 = this.codec;
      if (var1 != null) {
         var1.close();
         this.codec = null;
      }

      this.sourceLine.close();
      this.sourceLine = null;
   }

   public Object getControl(String var1) {
      return this.controls.getControl(var1);
   }

   public Object[] getControls() {
      return this.controls.getControls();
   }

   public String getName() {
      return this.name;
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public int hashCode() {
      return super.hashCode();
   }

   public void open() throws ResourceUnavailableException {
      // $FF: Couldn't be decompiled
   }

   public int process(Buffer param1) {
      // $FF: Couldn't be decompiled
   }

   public void reset() {
      logger.info("JavaSoundRenderer resetting...");
   }

   public Format setInputFormat(Format var1) {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("JavaSoundRenderer setting input format to: ");
      var3.append(var1);
      var2.info(var3.toString());
      if (!(var1 instanceof javax.media.format.AudioFormat)) {
         return null;
      } else {
         javax.media.format.AudioFormat var4 = (javax.media.format.AudioFormat)var1;
         this.inputFormat = var4;
         return var4;
      }
   }

   public void start() {
      logger.info("JavaSoundRenderer starting...");
      this.sourceLine.start();
   }

   public void stop() {
      logger.info("JavaSoundRenderer stopping...");
      this.sourceLine.stop();
   }

   private class FPC implements FrameProcessingControl, Owned {
      private FPC() {
      }

      // $FF: synthetic method
      FPC(Object var2) {
         this();
      }

      public Component getControlComponent() {
         return null;
      }

      public int getFramesDropped() {
         return JavaSoundRenderer.this.framesDropped;
      }

      public Object getOwner() {
         return JavaSoundRenderer.this;
      }

      public void setFramesBehind(float var1) {
      }

      public boolean setMinimalProcessing(boolean var1) {
         return false;
      }
   }

   private class JavaSoundRendererBufferControl implements BufferControl, Owned {
      private JavaSoundRendererBufferControl() {
      }

      // $FF: synthetic method
      JavaSoundRendererBufferControl(Object var2) {
         this();
      }

      public long getBufferLength() {
         return JavaSoundRenderer.this.buflenMS;
      }

      public Component getControlComponent() {
         return null;
      }

      public boolean getEnabledThreshold() {
         return false;
      }

      public long getMinimumThreshold() {
         return -1L;
      }

      public Object getOwner() {
         return JavaSoundRenderer.this;
      }

      public long setBufferLength(long param1) {
         // $FF: Couldn't be decompiled
      }

      public void setEnabledThreshold(boolean var1) {
      }

      public long setMinimumThreshold(long var1) {
         return -1L;
      }
   }

   private class PeakVolumeMeter extends AbstractGainControl {
      float peakLevel;

      private PeakVolumeMeter() {
         this.peakLevel = 0.0F;
      }

      // $FF: synthetic method
      PeakVolumeMeter(Object var2) {
         this();
      }

      public float getLevel() {
         return this.peakLevel;
      }

      public void processData(Buffer var1) {
         if (!this.getMute() && !var1.isDiscard()) {
            if (var1.getLength() > 0) {
               javax.media.format.AudioFormat var9 = (javax.media.format.AudioFormat)var1.getFormat();
               byte[] var10 = (byte[])((byte[])var1.getData());
               if (var9.getEncoding().equalsIgnoreCase("LINEAR") && var9.getSampleSizeInBits() == 16) {
                  byte var3 = 0;
                  byte var4 = 1;
                  if (var9.getEndian() == 0) {
                     var3 = 1;
                     var4 = 0;
                  }

                  if (var9.getSigned() == 1) {
                     int var6 = 0;
                     int var8 = var10.length / 2;

                     int var7;
                     for(int var5 = 0; var5 < var8; var6 = var7) {
                        var7 = (var10[var5 * 2 + var3] << 8) + (var10[var5 * 2 + var4] & 255);
                        int var2 = var7;
                        if (var7 < 0) {
                           var2 = -var7;
                        }

                        var7 = var6;
                        if (var2 > var6) {
                           var7 = var2;
                        }

                        ++var5;
                     }

                     this.peakLevel = (float)var6 / 32768.0F;
                  }
               }

            }
         }
      }

      public float setLevel(float var1) {
         return this.getLevel();
      }
   }
}
