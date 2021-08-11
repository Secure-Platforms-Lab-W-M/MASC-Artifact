package net.sf.fmj.media.protocol.javasound;

import com.lti.utils.synchronization.CloseableThread;
import com.lti.utils.synchronization.SynchronizedBoolean;
import com.lti.utils.synchronization.SynchronizedObjectHolder;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.Format;
import javax.media.Owned;
import javax.media.Time;
import javax.media.control.BufferControl;
import javax.media.control.FormatControl;
import javax.media.control.FrameProcessingControl;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.AbstractGainControl;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.RingBuffer;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.javax.sound.sampled.AudioFormat;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.LineUnavailableException;
import org.atalk.android.util.javax.sound.sampled.TargetDataLine;
import org.atalk.android.util.javax.sound.sampled.Mixer.Info;

public class DataSource extends PushBufferDataSource implements CaptureDevice {
   private static final String CONTENT_TYPE = "raw";
   private static final boolean TRACE = true;
   private static final Logger logger;
   private int buflen;
   private long buflenMS = 20L;
   private boolean connected;
   protected Object[] controls;
   private boolean enabled = true;
   private Format[] formatsArray;
   private AudioFormat javaSoundAudioFormat;
   private RingBuffer jitterBuffer = new RingBuffer(2);
   private javax.media.format.AudioFormat jmfAudioFormat;
   private DataSource.PeakVolumeMeter levelControl;
   private DataSource.MyPushBufferStream pushBufferStream;
   private final SynchronizedBoolean started = new SynchronizedBoolean(false);
   private TargetDataLine targetDataLine;

   static {
      logger = LoggerSingleton.logger;
   }

   public DataSource() {
      DataSource.PeakVolumeMeter var1 = new DataSource.PeakVolumeMeter();
      this.levelControl = var1;
      var1.setMute(true);
   }

   private int getMixerIndex() {
      byte var2 = -1;

      int var1;
      label44: {
         label48: {
            boolean var10001;
            String var3;
            try {
               var3 = this.getLocator().getRemainder();
            } catch (Exception var5) {
               var10001 = false;
               break label48;
            }

            var1 = var2;

            try {
               if (var3.startsWith("#")) {
                  var1 = Integer.parseInt(var3.substring(1));
               }
               break label44;
            } catch (Exception var4) {
               var10001 = false;
            }
         }

         var1 = var2;
      }

      if (-1 == var1 && this.getLocator().toString().startsWith("javasound://")) {
         for(int var6 = 0; var6 < 50; ++var6) {
            Format[] var7 = querySupportedFormats(var6);
            if (var7 != null && var7.length > 0) {
               return var6;
            }
         }
      }

      return var1;
   }

   private Format[] getSupportedFormats() {
      Format[] var1 = this.formatsArray;
      if (var1 != null) {
         return var1;
      } else {
         var1 = querySupportedFormats(this.getMixerIndex());
         this.formatsArray = var1;
         return var1;
      }
   }

   public static Format[] querySupportedFormats(int var0) {
      ArrayList var2 = new ArrayList();
      Info[] var3 = AudioSystem.getMixerInfo();
      if (var0 >= 0 && var0 < var3.length) {
         org.atalk.android.util.javax.sound.sampled.Line.Info[] var6 = AudioSystem.getMixer(var3[var0]).getTargetLineInfo();

         for(var0 = 0; var0 < var6.length; ++var0) {
            if (var6[var0] instanceof org.atalk.android.util.javax.sound.sampled.DataLine.Info) {
               AudioFormat[] var4 = ((org.atalk.android.util.javax.sound.sampled.DataLine.Info)var6[var0]).getFormats();

               for(int var1 = 0; var1 < var4.length; ++var1) {
                  javax.media.format.AudioFormat var5 = JavaSoundUtils.convertFormat(var4[var1]);
                  if (!var2.contains(var5)) {
                     var2.add(var5);
                  }
               }
            }
         }

         Collections.sort(var2, Collections.reverseOrder(new AudioFormatComparator()));
         return (Format[])var2.toArray(new Format[var2.size()]);
      } else {
         return null;
      }
   }

   private void setJMFAudioFormat(javax.media.format.AudioFormat var1) {
      this.jmfAudioFormat = var1;
      this.javaSoundAudioFormat = JavaSoundUtils.convertFormat(var1);
   }

   private void setJavaSoundAudioFormat(AudioFormat var1) {
      this.javaSoundAudioFormat = var1;
      this.jmfAudioFormat = JavaSoundUtils.convertFormat(var1);
   }

   public void connect() throws IOException {
      logger.fine("connect");
      if (!this.connected) {
         label38: {
            StringBuilder var11;
            LineUnavailableException var10000;
            label43: {
               boolean var10001;
               label35: {
                  javax.media.format.AudioFormat var2;
                  try {
                     if (this.jmfAudioFormat != null) {
                        break label35;
                     }

                     var2 = (javax.media.format.AudioFormat)this.getSupportedFormats()[0];
                     if (var2.getSampleRate() == -1.0D) {
                        this.setJMFAudioFormat((javax.media.format.AudioFormat)(new javax.media.format.AudioFormat(var2.getEncoding(), 44100.0D, -1, -1)).intersects(var2));
                        break label35;
                     }
                  } catch (LineUnavailableException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label43;
                  }

                  try {
                     this.setJMFAudioFormat(var2);
                  } catch (LineUnavailableException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label43;
                  }
               }

               try {
                  int var1 = this.getMixerIndex();
                  this.targetDataLine = (TargetDataLine)AudioSystem.getMixer(AudioSystem.getMixerInfo()[var1]).getLine(new org.atalk.android.util.javax.sound.sampled.DataLine.Info(TargetDataLine.class, (AudioFormat)null));
                  Logger var10 = logger;
                  var11 = new StringBuilder();
                  var11.append("targetDataLine=");
                  var11.append(this.targetDataLine);
                  var10.fine(var11.toString());
                  var1 = (int)((float)this.javaSoundAudioFormat.getFrameSize() * this.javaSoundAudioFormat.getSampleRate() * (float)this.buflenMS / 1000.0F);
                  this.buflen = var1;
                  this.targetDataLine.open(this.javaSoundAudioFormat, var1);
                  var10 = logger;
                  var11 = new StringBuilder();
                  var11.append("buflen=");
                  var11.append(this.buflen);
                  var10.fine(var11.toString());
                  this.pushBufferStream = new DataSource.MyPushBufferStream();
                  this.controls = new Object[]{new DataSource.JavaSoundFormatControl(), new DataSource.JavaSoundBufferControl(), new DataSource.JitterBufferControl(), new DataSource.FPC(), this.levelControl};
                  break label38;
               } catch (LineUnavailableException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            LineUnavailableException var9 = var10000;
            Logger var3 = logger;
            Level var4 = Level.WARNING;
            StringBuilder var5 = new StringBuilder();
            var5.append("");
            var5.append(var9);
            var3.log(var4, var5.toString(), var9);
            var11 = new StringBuilder();
            var11.append("");
            var11.append(var9);
            throw new IOException(var11.toString());
         }

         this.connected = true;
      }
   }

   public void disconnect() {
      logger.fine("disconnect");
      if (this.connected) {
         try {
            this.stop();
            if (this.targetDataLine != null) {
               this.targetDataLine.close();
            }
         } catch (IOException var7) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var7);
            var2.log(var3, var4.toString(), var7);
         } finally {
            this.targetDataLine = null;
            this.pushBufferStream = null;
         }

         this.connected = false;
      }
   }

   public CaptureDeviceInfo getCaptureDeviceInfo() {
      int var1 = this.getMixerIndex();
      return new CaptureDeviceInfo(AudioSystem.getMixerInfo()[var1].getName(), this.getLocator(), this.getSupportedFormats());
   }

   public String getContentType() {
      return "raw";
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      return this.controls;
   }

   public Time getDuration() {
      return DURATION_UNBOUNDED;
   }

   public FormatControl[] getFormatControls() {
      return new FormatControl[]{new DataSource.JavaSoundFormatControl()};
   }

   public PushBufferStream[] getStreams() {
      logger.fine("getStreams");
      return new PushBufferStream[]{this.pushBufferStream};
   }

   public void start() throws IOException {
      logger.fine("start");
      if (!this.started.getValue()) {
         this.targetDataLine.start();
         this.pushBufferStream.startAvailabilityThread();
         this.started.setValue(true);
      }
   }

   public void stop() throws IOException {
      logger.fine("stop");
      if (this.started.getValue()) {
         try {
            if (this.targetDataLine != null) {
               this.targetDataLine.stop();
               this.targetDataLine.flush();
            }

            if (this.pushBufferStream != null) {
               this.pushBufferStream.stopAvailabilityThread();
            }
         } catch (InterruptedException var4) {
            throw new InterruptedIOException();
         } finally {
            this.started.setValue(false);
         }

      }
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
         return DataSource.this.jitterBuffer.getOverrunCounter();
      }

      public Object getOwner() {
         return DataSource.this;
      }

      public void setFramesBehind(float var1) {
      }

      public boolean setMinimalProcessing(boolean var1) {
         return false;
      }
   }

   private class JavaSoundBufferControl implements BufferControl, Owned {
      private JavaSoundBufferControl() {
      }

      // $FF: synthetic method
      JavaSoundBufferControl(Object var2) {
         this();
      }

      public long getBufferLength() {
         return DataSource.this.buflenMS;
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
         return DataSource.this;
      }

      public long setBufferLength(long var1) {
         boolean var3 = false;
         Logger var7;
         Level var8;
         StringBuilder var9;
         if (DataSource.this.started.getValue()) {
            var3 = true;

            try {
               DataSource.this.stop();
            } catch (IOException var12) {
               var7 = DataSource.logger;
               var8 = Level.WARNING;
               var9 = new StringBuilder();
               var9.append("");
               var9.append(var12);
               var7.log(var8, var9.toString(), var12);
            }
         }

         long var4;
         if (var1 < 20L) {
            var4 = 20L;
         } else {
            var4 = var1;
            if (var1 > 5000L) {
               var4 = 5000L;
            }
         }

         DataSource.this.buflenMS = var4;
         if (DataSource.this.connected) {
            DataSource.this.disconnect();

            try {
               DataSource.this.connect();
            } catch (IOException var11) {
               var7 = DataSource.logger;
               var8 = Level.WARNING;
               var9 = new StringBuilder();
               var9.append("");
               var9.append(var11);
               var7.log(var8, var9.toString(), var11);
            }
         }

         if (var3) {
            try {
               DataSource.this.start();
            } catch (IOException var10) {
               var7 = DataSource.logger;
               var8 = Level.WARNING;
               var9 = new StringBuilder();
               var9.append("");
               var9.append(var10);
               var7.log(var8, var9.toString(), var10);
            }
         }

         return DataSource.this.buflenMS;
      }

      public void setEnabledThreshold(boolean var1) {
      }

      public long setMinimumThreshold(long var1) {
         return -1L;
      }
   }

   private class JavaSoundFormatControl implements FormatControl, Owned {
      private JavaSoundFormatControl() {
      }

      // $FF: synthetic method
      JavaSoundFormatControl(Object var2) {
         this();
      }

      public Component getControlComponent() {
         return null;
      }

      public Format getFormat() {
         return DataSource.this.jmfAudioFormat;
      }

      public Object getOwner() {
         return DataSource.this;
      }

      public Format[] getSupportedFormats() {
         return DataSource.this.getSupportedFormats();
      }

      public boolean isEnabled() {
         return DataSource.this.enabled;
      }

      public void setEnabled(boolean var1) {
         DataSource.this.enabled = var1;
      }

      public Format setFormat(Format var1) {
         DataSource.this.setJMFAudioFormat((javax.media.format.AudioFormat)var1);
         if (DataSource.this.connected) {
            DataSource.this.disconnect();

            try {
               DataSource.this.connect();
            } catch (IOException var5) {
               Logger var2 = DataSource.logger;
               Level var3 = Level.WARNING;
               StringBuilder var4 = new StringBuilder();
               var4.append("");
               var4.append(var5);
               var2.log(var3, var4.toString(), var5);
               return null;
            }
         }

         return DataSource.this.jmfAudioFormat;
      }
   }

   private class JitterBufferControl implements BufferControl, Owned {
      private JitterBufferControl() {
      }

      // $FF: synthetic method
      JitterBufferControl(Object var2) {
         this();
      }

      public long getBufferLength() {
         return DataSource.this.buflenMS * (long)DataSource.this.jitterBuffer.size();
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
         return DataSource.this;
      }

      public long setBufferLength(long var1) {
         int var4 = (int)(var1 / DataSource.this.buflenMS);
         int var3 = var4;
         if (var4 < 1) {
            var3 = 1;
         }

         DataSource.this.jitterBuffer.resize(var3);
         return (long)var3 * DataSource.this.buflenMS;
      }

      public void setEnabledThreshold(boolean var1) {
      }

      public long setMinimumThreshold(long var1) {
         return -1L;
      }
   }

   private class MyPushBufferStream implements PushBufferStream {
      private DataSource.MyPushBufferStream.AvailabilityThread availabilityThread;
      private long sequenceNumber;
      private final SynchronizedObjectHolder transferHandlerHolder;

      private MyPushBufferStream() {
         this.sequenceNumber = 0L;
         this.transferHandlerHolder = new SynchronizedObjectHolder();
      }

      // $FF: synthetic method
      MyPushBufferStream(Object var2) {
         this();
      }

      public boolean endOfStream() {
         return false;
      }

      public ContentDescriptor getContentDescriptor() {
         return new ContentDescriptor("raw");
      }

      public long getContentLength() {
         return -1L;
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Object[0];
      }

      public Format getFormat() {
         return DataSource.this.jmfAudioFormat;
      }

      public void read(Buffer var1) throws IOException {
         if (!DataSource.this.started.getValue()) {
            var1.setOffset(0);
            var1.setLength(0);
            var1.setDiscard(true);
         } else {
            try {
               byte[] var4 = (byte[])((byte[])DataSource.this.jitterBuffer.get());
               var1.setFlags(33024);
               var1.setOffset(0);
               var1.setData(var4);
               var1.setLength(var4.length);
               var1.setFormat(DataSource.this.jmfAudioFormat);
               long var2 = this.sequenceNumber + 1L;
               this.sequenceNumber = var2;
               var1.setSequenceNumber(var2);
               var1.setTimeStamp(System.nanoTime());
               DataSource.this.levelControl.processData(var1);
            } catch (Exception var5) {
            }
         }
      }

      public void setTransferHandler(BufferTransferHandler var1) {
         this.transferHandlerHolder.setObject(var1);
      }

      public void startAvailabilityThread() {
         DataSource.MyPushBufferStream.AvailabilityThread var1 = new DataSource.MyPushBufferStream.AvailabilityThread();
         this.availabilityThread = var1;
         StringBuilder var2 = new StringBuilder();
         var2.append("AvailabilityThread for ");
         var2.append(this);
         var1.setName(var2.toString());
         this.availabilityThread.setDaemon(true);
         this.availabilityThread.start();
      }

      public void stopAvailabilityThread() throws InterruptedException {
         DataSource.MyPushBufferStream.AvailabilityThread var1 = this.availabilityThread;
         if (var1 != null) {
            var1.close();
            this.availabilityThread.waitUntilClosed();
            this.availabilityThread = null;
         }
      }

      private class AvailabilityThread extends CloseableThread {
         private AvailabilityThread() {
         }

         // $FF: synthetic method
         AvailabilityThread(Object var2) {
            this();
         }

         public void run() {
            Logger var1 = DataSource.logger;
            StringBuilder var2 = new StringBuilder();
            var2.append("jitterbuflen=");
            var2.append(DataSource.this.jitterBuffer.size());
            var1.fine(var2.toString());

            label41: {
               Exception var10000;
               label40: {
                  boolean var10001;
                  byte[] var6;
                  try {
                     var6 = new byte[DataSource.this.buflen];
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label40;
                  }

                  while(true) {
                     BufferTransferHandler var8;
                     try {
                        do {
                           if (this.isClosing()) {
                              break label41;
                           }
                        } while(DataSource.this.targetDataLine.read(var6, 0, var6.length) <= 0);

                        var8 = (BufferTransferHandler)MyPushBufferStream.this.transferHandlerHolder.getObject();
                     } catch (Exception var4) {
                        var10000 = var4;
                        var10001 = false;
                        break;
                     }

                     if (var8 != null) {
                        try {
                           if (!DataSource.this.jitterBuffer.put(var6)) {
                              var8.transferData(MyPushBufferStream.this);
                           }
                        } catch (Exception var3) {
                           var10000 = var3;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }

               Exception var7 = var10000;
               var7.printStackTrace();
            }

            this.setClosed();
         }
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
