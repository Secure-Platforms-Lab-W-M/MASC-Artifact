package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Clock;
import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Format;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Multiplexer;
import javax.media.ResourceUnavailableException;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.BasicClock;
import net.sf.fmj.media.BasicPlugIn;
import net.sf.fmj.media.CircularBuffer;
import net.sf.fmj.media.MediaTimeBase;
import net.sf.fmj.media.control.MonitorAdapter;
import net.sf.fmj.media.protocol.BasicPushBufferDataSource;
import net.sf.fmj.media.protocol.BasicSourceStream;
import net.sf.fmj.media.util.MediaThread;

public class RawBufferMux extends BasicPlugIn implements Multiplexer, Clock {
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   boolean allowDrop = false;
   protected BasicClock clock = null;
   protected ContentDescriptor contentDesc = null;
   boolean hasRead = false;
   protected int masterTrackID = -1;
   // $FF: renamed from: mc net.sf.fmj.media.control.MonitorAdapter[]
   protected MonitorAdapter[] field_45 = null;
   long mediaStartTime = -1L;
   protected long[] mediaTime;
   protected int numTracks = 0;
   protected RawBufferMux.RawBufferDataSource source = null;
   boolean sourceDisconnected = false;
   boolean started = false;
   protected RawBufferMux.RawBufferSourceStream[] streams = null;
   protected ContentDescriptor[] supported = null;
   long systemStartTime = -1L;
   protected RawBufferMux.RawMuxTimeBase timeBase = null;
   Object timeSetSync = new Object();
   protected Format[] trackFormats;

   public RawBufferMux() {
      ContentDescriptor[] var1 = new ContentDescriptor[1];
      this.supported = var1;
      var1[0] = new ContentDescriptor("raw");
      this.timeBase = new RawBufferMux.RawMuxTimeBase();
      BasicClock var3 = new BasicClock();
      this.clock = var3;

      try {
         var3.setTimeBase(this.timeBase);
      } catch (Exception var2) {
      }
   }

   public void close() {
      RawBufferMux.RawBufferDataSource var2 = this.source;
      if (var2 != null) {
         try {
            var2.stop();
            this.source.disconnect();
         } catch (IOException var3) {
         }

         this.source = null;
      }

      int var1 = 0;

      while(true) {
         MonitorAdapter[] var4 = this.field_45;
         if (var1 >= var4.length) {
            return;
         }

         if (var4[var1] != null) {
            var4[var1].close();
         }

         ++var1;
      }
   }

   public DataSource getDataOutput() {
      return this.source;
   }

   public long getMediaNanoseconds() {
      return this.clock.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      return this.clock.getMediaTime();
   }

   public String getName() {
      return "Raw Buffer Multiplexer";
   }

   public float getRate() {
      return this.clock.getRate();
   }

   public Time getStopTime() {
      return this.clock.getStopTime();
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat((String)null), new VideoFormat((String)null)};
   }

   public ContentDescriptor[] getSupportedOutputContentDescriptors(Format[] var1) {
      return this.supported;
   }

   public Time getSyncTime() {
      return this.clock.getSyncTime();
   }

   public TimeBase getTimeBase() {
      return this.clock.getTimeBase();
   }

   public boolean initializeTracks(Format[] var1) {
      if (this.source.getStreams() == null) {
         this.source.initialize(var1);
         this.streams = (RawBufferMux.RawBufferSourceStream[])((RawBufferMux.RawBufferSourceStream[])this.source.getStreams());
         return true;
      } else {
         throw new Error("initializeTracks has been called previously. ");
      }
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      return this.clock.mapToTimeBase(var1);
   }

   public void open() throws ResourceUnavailableException {
      this.initializeTracks(this.trackFormats);
      RawBufferMux.RawBufferDataSource var4 = this.source;
      if (var4 != null && var4.getStreams() != null) {
         try {
            this.source.connect();
         } catch (IOException var5) {
            throw new ResourceUnavailableException(var5.getMessage());
         }

         int var1 = 0;
         Format[] var6 = this.trackFormats;
         this.mediaTime = new long[var6.length];
         this.field_45 = new MonitorAdapter[var6.length];
         int var2 = 0;

         while(true) {
            var6 = this.trackFormats;
            int var3;
            if (var2 >= var6.length) {
               var2 = 0;
               this.controls = new Control[var1];
               var1 = 0;

               while(true) {
                  MonitorAdapter[] var7 = this.field_45;
                  if (var1 >= var7.length) {
                     return;
                  }

                  var3 = var2;
                  if (var7[var1] != null) {
                     this.controls[var2] = this.field_45[var1];
                     var3 = var2 + 1;
                  }

                  ++var1;
                  var2 = var3;
               }
            }

            label37: {
               this.mediaTime[var2] = 0L;
               if (!(var6[var2] instanceof VideoFormat)) {
                  var3 = var1;
                  if (!(var6[var2] instanceof AudioFormat)) {
                     break label37;
                  }
               }

               this.field_45[var2] = new MonitorAdapter(this.trackFormats[var2], this);
               var3 = var1;
               if (this.field_45[var2] != null) {
                  var3 = var1 + 1;
               }
            }

            ++var2;
            var1 = var3;
         }
      } else {
         throw new ResourceUnavailableException("DataSource and SourceStreams were not created succesfully.");
      }
   }

   public int process(Buffer var1, int var2) {
      if ((var1.getFlags() & 4096) != 0) {
         var1.setFlags(var1.getFlags() & -4097 | 256);
      }

      MonitorAdapter[] var3 = this.field_45;
      if (var3[var2] != null && var3[var2].isEnabled()) {
         this.field_45[var2].process(var1);
      }

      RawBufferMux.RawBufferSourceStream[] var4 = this.streams;
      if (var4 != null && var1 != null && var2 < var4.length) {
         this.updateTime(var1, var2);
         return this.streams[var2].process(var1);
      } else {
         return 1;
      }
   }

   public void reset() {
      int var1 = 0;

      while(true) {
         RawBufferMux.RawBufferSourceStream[] var2 = this.streams;
         if (var1 >= var2.length) {
            return;
         }

         var2[var1].reset();
         MonitorAdapter[] var3 = this.field_45;
         if (var3[var1] != null) {
            var3[var1].reset();
         }

         ++var1;
      }
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) {
      if (matches(var1, this.supported) == null) {
         return null;
      } else {
         this.contentDesc = var1;
         this.source = new RawBufferMux.RawBufferDataSource();
         return this.contentDesc;
      }
   }

   public Format setInputFormat(Format var1, int var2) {
      if (var2 < this.numTracks) {
         this.trackFormats[var2] = var1;
      }

      for(var2 = 0; var2 < this.numTracks; ++var2) {
         if (this.trackFormats[var2] == null) {
            return var1;
         }
      }

      return var1;
   }

   public void setMediaTime(Time var1) {
      Object var3 = this.timeSetSync;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label211: {
         try {
            this.clock.setMediaTime(var1);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label211;
         }

         int var2 = 0;

         while(true) {
            try {
               if (var2 >= this.mediaTime.length) {
                  break;
               }

               this.mediaTime[var2] = var1.getNanoseconds();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label211;
            }

            ++var2;
         }

         label195:
         try {
            this.timeBase.update();
            this.systemStartTime = System.currentTimeMillis();
            this.mediaStartTime = var1.getNanoseconds() / 1000000L;
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label195;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public int setNumTracks(int var1) {
      this.numTracks = var1;
      this.trackFormats = new Format[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.trackFormats[var2] = null;
      }

      return var1;
   }

   public float setRate(float var1) {
      return var1 == this.clock.getRate() ? var1 : this.clock.setRate(1.0F);
   }

   public void setStopTime(Time var1) {
      this.clock.setStopTime(var1);
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      if (var1 != this.timeBase) {
         throw new IncompatibleTimeBaseException();
      }
   }

   public void stop() {
      Object var1 = this.timeSetSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (!this.started) {
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.started = false;
            this.clock.stop();
            this.timeBase.mediaStopped();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void syncStart(Time var1) {
      Object var2 = this.timeSetSync;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.started) {
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.started = true;
            this.clock.syncStart(var1);
            this.timeBase.mediaStarted();
            this.systemStartTime = System.currentTimeMillis();
            this.mediaStartTime = this.getMediaNanoseconds() / 1000000L;
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   protected void updateTime(Buffer var1, int var2) {
      if (var1.getFormat() instanceof AudioFormat) {
         if (mpegAudio.matches(var1.getFormat())) {
            if (var1.getTimeStamp() < 0L) {
               if (this.systemStartTime >= 0L) {
                  this.mediaTime[var2] = (this.mediaStartTime + System.currentTimeMillis() - this.systemStartTime) * 1000000L;
               }
            } else {
               this.mediaTime[var2] = var1.getTimeStamp();
            }
         } else {
            long var3 = ((AudioFormat)var1.getFormat()).computeDuration((long)var1.getLength());
            if (var3 >= 0L) {
               long[] var5 = this.mediaTime;
               var5[var2] += var3;
            } else {
               this.mediaTime[var2] = var1.getTimeStamp();
            }
         }
      } else if (var1.getTimeStamp() < 0L) {
         if (this.systemStartTime >= 0L) {
            this.mediaTime[var2] = (this.mediaStartTime + System.currentTimeMillis() - this.systemStartTime) * 1000000L;
         }
      } else {
         this.mediaTime[var2] = var1.getTimeStamp();
      }

      this.timeBase.update();
   }

   class RawBufferDataSource extends BasicPushBufferDataSource {
      public RawBufferDataSource() {
         if (RawBufferMux.this.contentDesc != null) {
            this.contentType = RawBufferMux.this.contentDesc.getContentType();
         }
      }

      private void initialize(Format[] var1) {
         RawBufferMux.this.streams = new RawBufferMux.RawBufferSourceStream[var1.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            RawBufferMux.this.streams[var2] = RawBufferMux.this.new RawBufferSourceStream(var1[var2]);
         }

      }

      public void connect() throws IOException {
         super.connect();
         RawBufferMux.this.sourceDisconnected = false;
      }

      public void disconnect() {
         super.disconnect();
         RawBufferMux.this.sourceDisconnected = true;

         for(int var1 = 0; var1 < RawBufferMux.this.streams.length; ++var1) {
            RawBufferMux.this.streams[var1].stop();
            RawBufferMux.this.streams[var1].close();
         }

      }

      public PushBufferStream[] getStreams() {
         return RawBufferMux.this.streams;
      }

      public void start() throws IOException {
         super.start();

         for(int var1 = 0; var1 < RawBufferMux.this.streams.length; ++var1) {
            RawBufferMux.this.streams[var1].start();
         }

      }

      public void stop() throws IOException {
         super.stop();

         for(int var1 = 0; var1 < RawBufferMux.this.streams.length; ++var1) {
            RawBufferMux.this.streams[var1].stop();
         }

      }
   }

   class RawBufferSourceStream extends BasicSourceStream implements PushBufferStream, Runnable {
      CircularBuffer bufferQ;
      boolean closed = false;
      Object drainSync = new Object();
      boolean draining = false;
      Format format = null;
      BufferTransferHandler handler = null;
      Object startReq = new Integer(0);
      boolean started = false;
      Thread streamThread = null;

      public RawBufferSourceStream(Format var2) {
         this.contentDescriptor = RawBufferMux.this.contentDesc;
         this.format = var2;
         this.bufferQ = new CircularBuffer(5);
         MediaThread var3 = new MediaThread(this, "RawBufferStream Thread");
         this.streamThread = var3;
         if (var3 != null) {
            var3.start();
         }

      }

      protected void close() {
         // $FF: Couldn't be decompiled
      }

      public Format getFormat() {
         return this.format;
      }

      protected int process(Buffer param1) {
         // $FF: Couldn't be decompiled
      }

      public void read(Buffer param1) throws IOException {
         // $FF: Couldn't be decompiled
      }

      protected void reset() {
         CircularBuffer var1 = this.bufferQ;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         Throwable var2;
         while(true) {
            label435: {
               try {
                  if (this.bufferQ.canRead()) {
                     this.bufferQ.read();
                     this.bufferQ.readReport();
                     continue;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label435;
               }

               label428:
               try {
                  this.bufferQ.notifyAll();
                  break;
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label428;
               }
            }

            while(true) {
               var2 = var10000;

               try {
                  throw var2;
               } catch (Throwable var39) {
                  var10000 = var39;
                  var10001 = false;
                  continue;
               }
            }
         }

         Object var45 = this.drainSync;
         synchronized(var45){}

         label423: {
            try {
               if (this.draining) {
                  this.draining = false;
                  this.drainSync.notifyAll();
               }
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label423;
            }

            label420:
            try {
               return;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label420;
            }
         }

         while(true) {
            var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      public void setTransferHandler(BufferTransferHandler var1) {
         this.handler = var1;
      }

      protected void start() {
         // $FF: Couldn't be decompiled
      }

      protected void stop() {
         // $FF: Couldn't be decompiled
      }
   }

   class RawMuxTimeBase extends MediaTimeBase {
      long ticks = 0L;
      boolean updated = false;

      public long getMediaTime() {
         if (RawBufferMux.this.masterTrackID >= 0) {
            return RawBufferMux.this.mediaTime[RawBufferMux.this.masterTrackID];
         } else if (!this.updated) {
            return this.ticks;
         } else {
            if (RawBufferMux.this.mediaTime.length == 1) {
               this.ticks = RawBufferMux.this.mediaTime[0];
            } else {
               this.ticks = RawBufferMux.this.mediaTime[0];

               for(int var1 = 1; var1 < RawBufferMux.this.mediaTime.length; ++var1) {
                  if (RawBufferMux.this.mediaTime[var1] < this.ticks) {
                     this.ticks = RawBufferMux.this.mediaTime[var1];
                  }
               }
            }

            this.updated = false;
            return this.ticks;
         }
      }

      public void update() {
         this.updated = true;
      }
   }
}
