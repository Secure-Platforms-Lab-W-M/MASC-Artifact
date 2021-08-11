package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Clock;
import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Multiplexer;
import javax.media.Owned;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.control.StreamWriterControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.Seekable;
import javax.media.protocol.SourceTransferHandler;
import net.sf.fmj.media.BasicClock;
import net.sf.fmj.media.BasicPlugIn;
import net.sf.fmj.media.MediaTimeBase;
import net.sf.fmj.media.control.MonitorAdapter;
import net.sf.fmj.media.datasink.RandomAccess;
import org.atalk.android.util.java.awt.Component;

public abstract class BasicMux extends BasicPlugIn implements Multiplexer, Clock {
   protected byte[] buf = new byte['耀'];
   protected int bufLength;
   protected int bufOffset;
   protected BasicClock clock = null;
   Object dataLock = new Object();
   boolean dataReady = false;
   protected boolean eos = false;
   protected int filePointer = 0;
   protected int fileSize = 0;
   protected long fileSizeLimit = -1L;
   protected boolean fileSizeLimitReached = false;
   protected boolean firstBuffer = true;
   Buffer[] firstBuffers;
   boolean[] firstBuffersDone;
   protected boolean flushing = false;
   protected Format[] inputs;
   protected boolean isLiveData = false;
   VideoFormat jpegFmt = new VideoFormat("jpeg");
   boolean mClosed = false;
   int master = 0;
   long masterTime = -1L;
   protected int maxBufSize = 32768;
   // $FF: renamed from: mc net.sf.fmj.media.control.MonitorAdapter[]
   protected MonitorAdapter[] field_46 = null;
   long[] mediaTime;
   VideoFormat mjpgFmt = new VideoFormat("mjpg");
   int[] nonKeyCount;
   protected int numTracks = 0;
   protected ContentDescriptor outputCD;
   boolean[] ready;
   boolean readyToStart = false;
   VideoFormat rgbFmt = new VideoFormat("rgb");
   protected BasicMux.BasicMuxDataSource source;
   protected Integer sourceLock = new Integer(0);
   boolean startCompensated = false;
   boolean started = false;
   Object startup = new Integer(0);
   protected SourceTransferHandler sth = null;
   protected BasicMux.BasicMuxPushStream stream;
   protected boolean streamSizeLimitSupported = true;
   protected Format[] supportedInputs;
   protected ContentDescriptor[] supportedOutputs;
   protected StreamWriterControl swc = null;
   long systemStartTime = System.currentTimeMillis() * 1000000L;
   protected BasicMux.BasicMuxTimeBase timeBase = null;
   Object timeSetSync = new Object();
   VideoFormat yuvFmt = new VideoFormat("yuv");

   public BasicMux() {
      this.timeBase = new BasicMux.BasicMuxTimeBase();
      BasicClock var1 = new BasicClock();
      this.clock = var1;

      try {
         var1.setTimeBase(this.timeBase);
      } catch (Exception var2) {
      }

      BasicMux.SWC var3 = new BasicMux.SWC(this);
      this.swc = var3;
      this.controls = new Control[]{var3};
   }

   private boolean checkReady() {
      if (this.readyToStart) {
         return true;
      } else {
         int var1 = 0;

         while(true) {
            boolean[] var2 = this.ready;
            if (var1 >= var2.length) {
               this.readyToStart = true;
               return true;
            }

            if (!var2[var1]) {
               return false;
            }

            ++var1;
         }
      }
   }

   private boolean compensateStart(Buffer param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   private long getDuration(Buffer var1) {
      long var2 = ((AudioFormat)var1.getFormat()).computeDuration((long)var1.getLength());
      return var2 < 0L ? 0L : var2;
   }

   private void resetReady() {
      // $FF: Couldn't be decompiled
   }

   private void updateClock(Buffer param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   protected void bufClear() {
      this.bufOffset = 0;
      this.bufLength = 0;
   }

   protected void bufFlush() {
      int var1 = this.filePointer;
      int var2 = this.bufLength;
      this.filePointer = var1 - var2;
      this.write(this.buf, 0, var2);
   }

   protected void bufSkip(int var1) {
      this.bufOffset += var1;
      this.bufLength += var1;
      this.filePointer += var1;
   }

   protected void bufWriteByte(byte var1) {
      byte[] var3 = this.buf;
      int var2 = this.bufOffset;
      var3[var2] = var1;
      this.bufOffset = var2 + 1;
      ++this.bufLength;
      ++this.filePointer;
   }

   protected void bufWriteBytes(String var1) {
      this.bufWriteBytes(var1.getBytes());
   }

   protected void bufWriteBytes(byte[] var1) {
      System.arraycopy(var1, 0, this.buf, this.bufOffset, var1.length);
      this.bufOffset += var1.length;
      this.bufLength += var1.length;
      this.filePointer += var1.length;
   }

   protected void bufWriteInt(int var1) {
      byte[] var3 = this.buf;
      int var2 = this.bufOffset;
      var3[var2 + 0] = (byte)(var1 >> 24 & 255);
      var3[var2 + 1] = (byte)(var1 >> 16 & 255);
      var3[var2 + 2] = (byte)(var1 >> 8 & 255);
      var3[var2 + 3] = (byte)(var1 >> 0 & 255);
      this.bufOffset = var2 + 4;
      this.bufLength += 4;
      this.filePointer += 4;
   }

   protected void bufWriteIntLittleEndian(int var1) {
      byte[] var3 = this.buf;
      int var2 = this.bufOffset;
      var3[var2 + 3] = (byte)(var1 >>> 24 & 255);
      var3[var2 + 2] = (byte)(var1 >>> 16 & 255);
      var3[var2 + 1] = (byte)(var1 >>> 8 & 255);
      var3[var2 + 0] = (byte)(var1 >>> 0 & 255);
      this.bufOffset = var2 + 4;
      this.bufLength += 4;
      this.filePointer += 4;
   }

   protected void bufWriteShort(short var1) {
      byte[] var3 = this.buf;
      int var2 = this.bufOffset;
      var3[var2 + 0] = (byte)(var1 >> 8 & 255);
      var3[var2 + 1] = (byte)(var1 >> 0 & 255);
      this.bufOffset = var2 + 2;
      this.bufLength += 2;
      this.filePointer += 2;
   }

   protected void bufWriteShortLittleEndian(short var1) {
      byte[] var3 = this.buf;
      int var2 = this.bufOffset;
      var3[var2 + 1] = (byte)(var1 >> 8 & 255);
      var3[var2 + 0] = (byte)(var1 >> 0 & 255);
      this.bufOffset = var2 + 2;
      this.bufLength += 2;
      this.filePointer += 2;
   }

   public void close() {
      // $FF: Couldn't be decompiled
   }

   protected int doProcess(Buffer var1, int var2) {
      byte[] var3 = (byte[])((byte[])var1.getData());
      var2 = var1.getLength();
      if (!var1.isEOM()) {
         this.write(var3, var1.getOffset(), var2);
      }

      return 0;
   }

   public DataSource getDataOutput() {
      // $FF: Couldn't be decompiled
   }

   public long getMediaNanoseconds() {
      return this.clock.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      return this.clock.getMediaTime();
   }

   public float getRate() {
      return this.clock.getRate();
   }

   public Time getStopTime() {
      return this.clock.getStopTime();
   }

   long getStreamSize() {
      return (long)this.fileSize;
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputs;
   }

   public ContentDescriptor[] getSupportedOutputContentDescriptors(Format[] var1) {
      return this.supportedOutputs;
   }

   public Time getSyncTime() {
      return this.clock.getSyncTime();
   }

   public TimeBase getTimeBase() {
      return this.clock.getTimeBase();
   }

   boolean isEOS() {
      return this.eos;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      return this.clock.mapToTimeBase(var1);
   }

   boolean needsSeekable() {
      return false;
   }

   public void open() {
      this.firstBuffer = true;
      Format[] var4 = this.inputs;
      this.firstBuffers = new Buffer[var4.length];
      this.firstBuffersDone = new boolean[var4.length];
      this.nonKeyCount = new int[var4.length];
      this.mediaTime = new long[var4.length];
      int var1 = 0;

      while(true) {
         var4 = this.inputs;
         if (var1 >= var4.length) {
            this.ready = new boolean[var4.length];
            this.resetReady();
            var1 = 0;
            this.field_46 = new MonitorAdapter[this.inputs.length];
            int var2 = 0;

            while(true) {
               var4 = this.inputs;
               int var3;
               if (var2 >= var4.length) {
                  var2 = 0;
                  this.controls = new Control[var1 + 1];
                  var1 = 0;

                  while(true) {
                     MonitorAdapter[] var5 = this.field_46;
                     if (var1 >= var5.length) {
                        this.controls[var2] = this.swc;
                        return;
                     }

                     var3 = var2;
                     if (var5[var1] != null) {
                        this.controls[var2] = this.field_46[var1];
                        var3 = var2 + 1;
                     }

                     ++var1;
                     var2 = var3;
                  }
               }

               label31: {
                  if (!(var4[var2] instanceof VideoFormat)) {
                     var3 = var1;
                     if (!(var4[var2] instanceof AudioFormat)) {
                        break label31;
                     }
                  }

                  this.field_46[var2] = new MonitorAdapter(this.inputs[var2], this);
                  var3 = var1;
                  if (this.field_46[var2] != null) {
                     var3 = var1 + 1;
                  }
               }

               ++var2;
               var1 = var3;
            }
         }

         this.firstBuffers[var1] = null;
         this.firstBuffersDone[var1] = false;
         this.nonKeyCount[var1] = 0;
         this.mediaTime[var1] = 0L;
         ++var1;
      }
   }

   public int process(Buffer var1, int var2) {
      if (var1.isDiscard()) {
         return 0;
      } else {
         if (!this.isLiveData && (var1.getFlags() & '耀') > 0) {
            this.isLiveData = true;
         }

         while(true) {
            BasicMux.BasicMuxDataSource var4 = this.source;
            Throwable var70;
            Throwable var10000;
            boolean var10001;
            if (var4 != null && var4.isConnected() && this.source.isStarted()) {
               synchronized(this){}

               label947: {
                  label914: {
                     try {
                        if (this.firstBuffer) {
                           this.writeHeader();
                           this.firstBuffer = false;
                        }
                     } catch (Throwable var65) {
                        var10000 = var65;
                        var10001 = false;
                        break label914;
                     }

                     label911:
                     try {
                        break label947;
                     } catch (Throwable var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label911;
                     }
                  }

                  while(true) {
                     var70 = var10000;

                     try {
                        throw var70;
                     } catch (Throwable var62) {
                        var10000 = var62;
                        var10001 = false;
                        continue;
                     }
                  }
               }

               if (this.numTracks > 1) {
                  if ((var1.getFlags() & 4096) != 0 && var1.getTimeStamp() <= 0L) {
                     return 0;
                  }

                  if (!this.startCompensated && !this.compensateStart(var1, var2)) {
                     return 0;
                  }
               }

               this.updateClock(var1, var2);
               MonitorAdapter[] var72 = this.field_46;
               if (var72[var2] != null && var72[var2].isEnabled()) {
                  this.field_46[var2].process(var1);
               }

               int var3 = this.doProcess(var1, var2);
               var2 = var3;
               if (this.fileSizeLimitReached) {
                  var2 = var3 | 8;
               }

               return var2;
            }

            Integer var71 = this.sourceLock;
            synchronized(var71){}

            label949: {
               try {
                  try {
                     this.sourceLock.wait(500L);
                  } catch (InterruptedException var68) {
                  }
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label949;
               }

               try {
                  if (this.flushing) {
                     this.flushing = false;
                     var1.setLength(0);
                     return 0;
                  }
               } catch (Throwable var67) {
                  var10000 = var67;
                  var10001 = false;
                  break label949;
               }

               label926:
               try {
                  continue;
               } catch (Throwable var66) {
                  var10000 = var66;
                  var10001 = false;
                  break label926;
               }
            }

            while(true) {
               var70 = var10000;

               try {
                  throw var70;
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public boolean requireTwoPass() {
      return false;
   }

   public void reset() {
      // $FF: Couldn't be decompiled
   }

   protected int seek(int var1) {
      BasicMux.BasicMuxDataSource var2 = this.source;
      if (var2 != null) {
         if (!var2.isConnected()) {
            return var1;
         } else {
            var1 = this.stream.seek(var1);
            this.filePointer = var1;
            return var1;
         }
      } else {
         return var1;
      }
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) {
      if (matches(var1, this.supportedOutputs) == null) {
         return null;
      } else {
         this.outputCD = var1;
         return var1;
      }
   }

   public Format setInputFormat(Format var1, int var2) {
      this.inputs[var2] = var1;
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
      if (this.inputs == null) {
         this.inputs = new Format[var1];
         return var1;
      } else {
         Format[] var3 = new Format[var1];
         int var2 = 0;

         while(true) {
            Format[] var4 = this.inputs;
            if (var2 >= var4.length) {
               this.inputs = var3;
               return var1;
            }

            var3[var2] = var4[var2];
            ++var2;
         }
      }
   }

   public float setRate(float var1) {
      return var1 == this.clock.getRate() ? var1 : this.clock.setRate(1.0F);
   }

   public void setStopTime(Time var1) {
      this.clock.setStopTime(var1);
   }

   void setStream(BasicMux.BasicMuxPushStream var1) {
      this.stream = var1;
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
            this.systemStartTime = System.currentTimeMillis() * 1000000L;
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

   protected int write(byte[] var1, int var2, int var3) {
      BasicMux.BasicMuxDataSource var7 = this.source;
      if (var7 != null) {
         if (!var7.isConnected()) {
            return var3;
         } else {
            if (var3 > 0) {
               int var4 = this.filePointer + var3;
               this.filePointer = var4;
               if (var4 > this.fileSize) {
                  this.fileSize = var4;
               }

               long var5 = this.fileSizeLimit;
               if (var5 > 0L && (long)this.fileSize >= var5) {
                  this.fileSizeLimitReached = true;
               }
            }

            return this.stream.write(var1, var2, var3);
         }
      } else {
         return var3;
      }
   }

   protected void writeFooter() {
   }

   protected void writeHeader() {
   }

   class BasicMuxDataSource extends PushDataSource {
      // $FF: renamed from: cd javax.media.protocol.ContentDescriptor
      private ContentDescriptor field_8;
      private boolean connected = false;
      private BasicMux mux;
      private boolean started = false;
      private BasicMux.BasicMuxPushStream stream;
      private BasicMux.BasicMuxPushStream[] streams;

      public BasicMuxDataSource(BasicMux var2, ContentDescriptor var3) {
         this.field_8 = var3;
         this.mux = var2;
      }

      public void connect() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void disconnect() {
         this.connected = false;
      }

      public String getContentType() {
         return this.field_8.getContentType();
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Control[0];
      }

      public Time getDuration() {
         return Duration.DURATION_UNKNOWN;
      }

      public PushSourceStream[] getStreams() {
         if (this.streams == null) {
            this.streams = new BasicMux.BasicMuxPushStream[1];
            BasicMux.BasicMuxPushStream var1 = BasicMux.this.new BasicMuxPushStream(this.field_8);
            this.stream = var1;
            this.streams[0] = var1;
            BasicMux.this.setStream(var1);
         }

         return this.streams;
      }

      boolean isConnected() {
         return this.connected;
      }

      boolean isStarted() {
         return this.started;
      }

      public void start() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void stop() {
         this.started = false;
      }
   }

   class BasicMuxPushStream implements PushSourceStream {
      // $FF: renamed from: cd javax.media.protocol.ContentDescriptor
      private ContentDescriptor field_199;
      private byte[] data;
      private int dataLen;
      private int dataOff;
      private Integer writeLock = new Integer(0);

      public BasicMuxPushStream(ContentDescriptor var2) {
         this.field_199 = var2;
      }

      public boolean endOfStream() {
         return BasicMux.this.isEOS();
      }

      public ContentDescriptor getContentDescriptor() {
         return this.field_199;
      }

      public long getContentLength() {
         return -1L;
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Control[0];
      }

      public int getMinimumTransferSize() {
         return this.dataLen;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         Integer var4 = this.writeLock;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label309: {
            label308: {
               label314: {
                  try {
                     if (this.dataLen == -1) {
                        break label314;
                     }
                  } catch (Throwable var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label309;
                  }

                  try {
                     if (var3 >= this.dataLen) {
                        var3 = this.dataLen;
                     }
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label309;
                  }

                  try {
                     System.arraycopy(this.data, this.dataOff, var1, var2, var3);
                     this.dataLen -= var3;
                     this.dataOff += var3;
                     break label308;
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label309;
                  }
               }

               var3 = -1;
            }

            label290:
            try {
               this.writeLock.notifyAll();
               return var3;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label290;
            }
         }

         while(true) {
            Throwable var35 = var10000;

            try {
               throw var35;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               continue;
            }
         }
      }

      int seek(int var1) {
         synchronized(this){}
         boolean var6 = false;

         long var2;
         label38: {
            try {
               var6 = true;
               if (BasicMux.this.sth != null) {
                  ((Seekable)BasicMux.this.sth).seek((long)var1);
                  var2 = ((Seekable)BasicMux.this.sth).tell();
                  var6 = false;
                  break label38;
               }

               var6 = false;
            } finally {
               if (var6) {
                  ;
               }
            }

            return -1;
         }

         var1 = (int)var2;
         return var1;
      }

      public void setTransferHandler(SourceTransferHandler var1) {
         Integer var2 = this.writeLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label527: {
            try {
               BasicMux.this.sth = var1;
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label527;
            }

            label530: {
               if (var1 != null) {
                  try {
                     if (BasicMux.this.needsSeekable() && !(var1 instanceof Seekable)) {
                        break label530;
                     }
                  } catch (Throwable var57) {
                     var10000 = var57;
                     var10001 = false;
                     break label527;
                  }
               }

               label507: {
                  try {
                     if (!BasicMux.this.requireTwoPass()) {
                        break label507;
                     }
                  } catch (Throwable var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label527;
                  }

                  if (var1 != null) {
                     try {
                        if (var1 instanceof RandomAccess) {
                           ((RandomAccess)var1).setEnabled(true);
                        }
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label527;
                     }
                  }
               }

               try {
                  this.writeLock.notifyAll();
                  return;
               } catch (Throwable var54) {
                  var10000 = var54;
                  var10001 = false;
                  break label527;
               }
            }

            label498:
            try {
               throw new Error("SourceTransferHandler needs to be seekable");
            } catch (Throwable var53) {
               var10000 = var53;
               var10001 = false;
               break label498;
            }
         }

         while(true) {
            Throwable var59 = var10000;

            try {
               throw var59;
            } catch (Throwable var52) {
               var10000 = var52;
               var10001 = false;
               continue;
            }
         }
      }

      int write(byte[] param1, int param2, int param3) {
         // $FF: Couldn't be decompiled
      }
   }

   class BasicMuxTimeBase extends MediaTimeBase {
      long ticks = 0L;
      boolean updated = false;

      public long getMediaTime() {
         if (!this.updated) {
            return this.ticks;
         } else {
            if (BasicMux.this.mediaTime.length == 1) {
               this.ticks = BasicMux.this.mediaTime[0];
            } else {
               this.ticks = BasicMux.this.mediaTime[0];

               for(int var1 = 1; var1 < BasicMux.this.mediaTime.length; ++var1) {
                  if (BasicMux.this.mediaTime[var1] < this.ticks) {
                     this.ticks = BasicMux.this.mediaTime[var1];
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

   class SWC implements StreamWriterControl, Owned {
      private BasicMux bmx;

      public SWC(BasicMux var2) {
         this.bmx = var2;
      }

      public Component getControlComponent() {
         return null;
      }

      public Object getOwner() {
         return this.bmx;
      }

      public long getStreamSize() {
         return this.bmx.getStreamSize();
      }

      public boolean setStreamSizeLimit(long var1) {
         this.bmx.fileSizeLimit = var1;
         return BasicMux.this.streamSizeLimitSupported;
      }
   }
}
