package net.sf.fmj.media.renderer.audio;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Clock;
import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Drainable;
import javax.media.Format;
import javax.media.GainControl;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Owned;
import javax.media.Prefetchable;
import javax.media.Renderer;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.control.BufferControl;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.BasicPlugIn;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.MediaTimeBase;
import net.sf.fmj.media.renderer.audio.device.AudioOutput;
import org.atalk.android.util.java.awt.Component;

public abstract class AudioRenderer extends BasicPlugIn implements Renderer, Prefetchable, Drainable, Clock {
   static int DefaultMaxBufferSize = 4000;
   static int DefaultMinBufferSize = 62;
   long bufLenReq = 200L;
   protected BufferControl bufferControl;
   protected int bytesPerSec;
   protected long bytesWritten = 0L;
   protected AudioFormat devFormat;
   protected AudioOutput device = null;
   protected boolean devicePaused = true;
   protected GainControl gainControl;
   protected AudioFormat inputFormat;
   TimeBase master = null;
   long mediaTimeAnchor = 0L;
   protected Control peakVolumeMeter = null;
   protected boolean prefetched = false;
   float rate = 1.0F;
   protected boolean resetted = false;
   long startTime = Long.MAX_VALUE;
   protected boolean started = false;
   long stopTime = Long.MAX_VALUE;
   Format[] supportedFormats;
   long ticksSinceLastReset = 0L;
   protected TimeBase timeBase = null;
   private Object writeLock = new Object();

   public AudioRenderer() {
      this.timeBase = new AudioRenderer.AudioTimeBase(this);
      this.bufferControl = new AudioRenderer.class_12(this);
   }

   protected boolean checkInput(Buffer var1) {
      Format var2 = var1.getFormat();
      if (this.device != null) {
         AudioFormat var3 = this.devFormat;
         if (var3 != null && var3.equals(var2)) {
            return true;
         }
      }

      if (!this.initDevice((AudioFormat)var2)) {
         var1.setDiscard(true);
         return false;
      } else {
         this.devFormat = (AudioFormat)var2;
         return true;
      }
   }

   public void close() {
      this.stop();
      if (this.device != null) {
         this.pauseDevice();
         this.device.flush();
         this.mediaTimeAnchor = this.getMediaNanoseconds();
         this.ticksSinceLastReset = 0L;
         this.device.dispose();
      }

      this.device = null;
   }

   public int computeBufferSize(AudioFormat var1) {
      long var6 = (long)(var1.getSampleRate() * (double)var1.getChannels() * (double)var1.getSampleSizeInBits() / 8.0D);
      long var4 = this.bufLenReq;
      int var3 = DefaultMinBufferSize;
      if (var4 < (long)var3) {
         var4 = (long)var3;
      } else {
         var3 = DefaultMaxBufferSize;
         if (var4 > (long)var3) {
            var4 = (long)var3;
         } else {
            var4 = this.bufLenReq;
         }
      }

      float var2 = (float)var4 / 1000.0F;
      return (int)((long)((float)var6 * var2));
   }

   protected abstract AudioOutput createDevice(AudioFormat var1);

   protected int doProcessData(Buffer param1) {
      // $FF: Couldn't be decompiled
   }

   public void drain() {
      synchronized(this){}

      try {
         if (this.started && this.device != null) {
            this.device.drain();
         }

         this.prefetched = false;
      } finally {
         ;
      }

   }

   public Object[] getControls() {
      return new Control[]{this.gainControl, this.bufferControl};
   }

   public long getLatency() {
      return this.bytesWritten * 1000L / (long)this.bytesPerSec * 1000000L - this.getMediaNanoseconds();
   }

   public long getMediaNanoseconds() {
      long var3 = this.mediaTimeAnchor;
      AudioOutput var5 = this.device;
      long var1;
      if (var5 != null) {
         var1 = var5.getMediaNanoseconds();
      } else {
         var1 = 0L;
      }

      return var3 + var1 - this.ticksSinceLastReset;
   }

   public Time getMediaTime() {
      return new Time(this.getMediaNanoseconds());
   }

   public float getRate() {
      return this.rate;
   }

   public Time getStopTime() {
      return new Time(this.stopTime);
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedFormats;
   }

   public Time getSyncTime() {
      return new Time(0L);
   }

   public TimeBase getTimeBase() {
      TimeBase var1 = this.master;
      return var1 != null ? var1 : this.timeBase;
   }

   protected boolean initDevice(AudioFormat var1) {
      if (var1 == null) {
         System.err.println("AudioRenderer: ERROR: Unknown AudioFormat");
         return false;
      } else if (var1.getSampleRate() != -1.0D && var1.getSampleSizeInBits() != -1) {
         AudioOutput var5 = this.device;
         if (var5 != null) {
            var5.drain();
            this.pauseDevice();
            this.mediaTimeAnchor = this.getMediaNanoseconds();
            this.ticksSinceLastReset = 0L;
            this.device.dispose();
            this.device = null;
         }

         AudioFormat var6 = new AudioFormat(var1.getEncoding(), var1.getSampleRate(), var1.getSampleSizeInBits(), var1.getChannels(), var1.getEndian(), var1.getSigned());
         AudioOutput var4 = this.createDevice(var6);
         this.device = var4;
         if (var4 != null && var4.initialize(var6, this.computeBufferSize(var6))) {
            this.device.setMute(this.gainControl.getMute());
            this.device.setGain((double)this.gainControl.getDB());
            float var2 = this.rate;
            if (var2 != 1.0F && var2 != this.device.setRate(var2)) {
               PrintStream var7 = System.err;
               StringBuilder var8 = new StringBuilder();
               var8.append("The AudioRenderer does not support the given rate: ");
               var8.append(this.rate);
               var7.println(var8.toString());
               this.device.setRate(1.0F);
            }

            if (this.started) {
               this.resumeDevice();
            }

            this.bytesPerSec = (int)(var1.getSampleRate() * (double)var1.getChannels() * (double)var1.getSampleSizeInBits() / 8.0D);
            return true;
         } else {
            this.device = null;
            return false;
         }
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Cannot initialize audio renderer with format: ");
         var3.append(var1);
         Log.error(var3.toString());
         return false;
      }
   }

   public boolean isPrefetched() {
      return this.prefetched;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      return new Time((long)((float)(var1.getNanoseconds() - this.mediaTimeAnchor) / this.rate) + this.startTime);
   }

   void pauseDevice() {
      synchronized(this){}

      try {
         if (!this.devicePaused && this.device != null) {
            this.device.pause();
            this.devicePaused = true;
         }

         if (this.timeBase instanceof AudioRenderer.AudioTimeBase) {
            ((AudioRenderer.AudioTimeBase)this.timeBase).mediaStopped();
         }
      } finally {
         ;
      }

   }

   public int process(Buffer var1) {
      int var2 = this.processData(var1);
      if (var1.isEOM() && var2 != 2) {
         this.drain();
         this.pauseDevice();
      }

      return var2;
   }

   protected void processByWaiting(Buffer var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label163: {
         try {
            if (!this.started) {
               this.prefetched = true;
               return;
            }
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label163;
         }

         try {
            ;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label163;
         }

         AudioFormat var9 = (AudioFormat)var1.getFormat();
         int var2 = (int)var9.getSampleRate();
         int var3 = var9.getSampleSizeInBits();
         int var4 = var9.getChannels();
         long var5 = (long)(var1.getLength() * 1000 / (var3 / 8 * var2 * var4));
         long var7 = (long)((int)((float)var5 / this.getRate()));

         try {
            Thread.sleep(var7);
         } catch (Exception var22) {
         }

         var1.setLength(0);
         var1.setOffset(0);
         this.mediaTimeAnchor += 1000000L * var5;
         return;
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            continue;
         }
      }
   }

   protected int processData(Buffer var1) {
      return !this.checkInput(var1) ? 1 : this.doProcessData(var1);
   }

   public void reset() {
      this.resetted = true;
      this.mediaTimeAnchor = this.getMediaNanoseconds();
      AudioOutput var1 = this.device;
      if (var1 != null) {
         var1.flush();
         this.ticksSinceLastReset = this.device.getMediaNanoseconds();
      } else {
         this.ticksSinceLastReset = 0L;
      }

      this.prefetched = false;
   }

   void resumeDevice() {
      synchronized(this){}

      try {
         if (this.timeBase instanceof AudioRenderer.AudioTimeBase) {
            ((AudioRenderer.AudioTimeBase)this.timeBase).mediaStarted();
         }

         if (this.devicePaused && this.device != null) {
            this.device.resume();
            this.devicePaused = false;
         }
      } finally {
         ;
      }

   }

   public Format setInputFormat(Format var1) {
      int var2 = 0;

      while(true) {
         Format[] var3 = this.supportedFormats;
         if (var2 >= var3.length) {
            return null;
         }

         if (var3[var2].matches(var1)) {
            this.inputFormat = (AudioFormat)var1;
            return var1;
         }

         ++var2;
      }
   }

   public void setMediaTime(Time var1) {
      this.mediaTimeAnchor = var1.getNanoseconds();
   }

   public float setRate(float var1) {
      AudioOutput var2 = this.device;
      if (var2 != null) {
         this.rate = var2.setRate(var1);
      } else {
         this.rate = 1.0F;
      }

      return this.rate;
   }

   public void setStopTime(Time var1) {
      this.stopTime = var1.getNanoseconds();
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      if (!(var1 instanceof AudioRenderer.AudioTimeBase)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("AudioRenderer cannot be controlled by time bases other than its own: ");
         var2.append(var1);
         Log.warning(var2.toString());
      }

      this.master = var1;
   }

   public void start() {
      this.syncStart(this.getTimeBase().getTime());
   }

   public void stop() {
      // $FF: Couldn't be decompiled
   }

   public void syncStart(Time var1) {
      synchronized(this){}

      try {
         this.started = true;
         this.prefetched = true;
         this.resetted = false;
         this.resumeDevice();
         this.startTime = var1.getNanoseconds();
      } finally {
         ;
      }

   }

   class AudioTimeBase extends MediaTimeBase {
      AudioRenderer renderer;

      AudioTimeBase(AudioRenderer var2) {
         this.renderer = var2;
      }

      public long getMediaTime() {
         float var1 = AudioRenderer.this.rate;
         long var2 = 0L;
         if (var1 != 1.0F && AudioRenderer.this.rate != 0.0F) {
            if (AudioRenderer.this.device != null) {
               var2 = AudioRenderer.this.device.getMediaNanoseconds();
            }

            return (long)((float)var2 / AudioRenderer.this.rate);
         } else {
            if (AudioRenderer.this.device != null) {
               var2 = AudioRenderer.this.device.getMediaNanoseconds();
            }

            return var2;
         }
      }
   }

   class class_12 implements BufferControl, Owned {
      AudioRenderer renderer;

      class_12(AudioRenderer var2) {
         this.renderer = var2;
      }

      public long getBufferLength() {
         return AudioRenderer.this.bufLenReq;
      }

      public Component getControlComponent() {
         return null;
      }

      public boolean getEnabledThreshold() {
         return false;
      }

      public long getMinimumThreshold() {
         return 0L;
      }

      public Object getOwner() {
         return this.renderer;
      }

      public long setBufferLength(long var1) {
         if (var1 < (long)AudioRenderer.DefaultMinBufferSize) {
            AudioRenderer.this.bufLenReq = (long)AudioRenderer.DefaultMinBufferSize;
         } else if (var1 > (long)AudioRenderer.DefaultMaxBufferSize) {
            AudioRenderer.this.bufLenReq = (long)AudioRenderer.DefaultMaxBufferSize;
         } else {
            AudioRenderer.this.bufLenReq = var1;
         }

         return AudioRenderer.this.bufLenReq;
      }

      public void setEnabledThreshold(boolean var1) {
      }

      public long setMinimumThreshold(long var1) {
         return 0L;
      }
   }
}
