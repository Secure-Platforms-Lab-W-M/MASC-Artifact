package net.sf.fmj.media;

import java.io.IOException;
import javax.media.BadHeaderException;
import javax.media.Demultiplexer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.ResourceUnavailableException;
import javax.media.SystemTimeBase;
import javax.media.Time;
import javax.media.Track;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.Positionable;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import net.sf.fmj.media.rtp.util.RTPTimeBase;

public class BasicSourceModule extends BasicModule implements Duration, Positionable {
   protected long bitsRead = 0L;
   String cname = null;
   protected String[] connectorNames;
   long currentRTPTime = 0L;
   protected long currentSystemTime = 0L;
   PlaybackEngine engine;
   public String errMsg = null;
   protected Time lastPositionSet = new Time(0L);
   protected long lastSystemTime = 0L;
   int latencyTrack = -1;
   protected SourceThread[] loops;
   long oldOffset = 0L;
   protected long originSystemTime = 0L;
   protected Demultiplexer parser;
   Object resetSync = new Object();
   RTPTimeBase rtpMapper = null;
   RTPTimeBase rtpMapperUpdatable = null;
   boolean rtpOffsetInvalid = true;
   protected DataSource source;
   protected boolean started = false;
   protected SystemTimeBase systemTimeBase = new SystemTimeBase();
   protected Track[] tracks = new Track[0];

   protected BasicSourceModule(DataSource var1, Demultiplexer var2) {
      this.source = var1;
      this.parser = var2;
      if (var1 instanceof PullDataSource) {
         PullSourceStream var4 = ((PullDataSource)var1).getStreams()[0];
      } else {
         if (var1 instanceof PushDataSource) {
            PushSourceStream var3 = ((PushDataSource)var1).getStreams()[0];
         }

      }
   }

   protected static Demultiplexer createDemultiplexer(DataSource param0) throws IOException, IncompatibleSourceException {
      // $FF: Couldn't be decompiled
   }

   public static BasicSourceModule createModule(DataSource var0) throws IOException, IncompatibleSourceException {
      Demultiplexer var1 = createDemultiplexer(var0);
      return var1 == null ? null : new BasicSourceModule(var0, var1);
   }

   public void abortPrefetch() {
      this.doStop();
   }

   public void abortRealize() {
      this.parser.stop();
      this.parser.close();
   }

   protected boolean checkAllPaused() {
      for(int var1 = 0; var1 < this.loops.length; ++var1) {
         if (this.tracks[var1].isEnabled()) {
            SourceThread[] var2 = this.loops;
            if (var2[var1] != null && !var2[var1].isPaused()) {
               return false;
            }
         }
      }

      return true;
   }

   public void checkLatency() {
      int var1 = this.latencyTrack;
      SourceThread[] var2;
      if (var1 > -1) {
         if (this.tracks[var1].isEnabled()) {
            var2 = this.loops;
            var1 = this.latencyTrack;
            if (var2[var1] != null) {
               var2[var1].checkLatency = true;
               return;
            }
         }

         this.latencyTrack = -1;
      }

      var1 = 0;

      while(true) {
         Track[] var3 = this.tracks;
         if (var1 >= var3.length) {
            break;
         }

         if (var3[var1].isEnabled()) {
            this.latencyTrack = var1;
            if (this.tracks[var1].getFormat() instanceof VideoFormat) {
               break;
            }
         }

         ++var1;
      }

      var1 = this.latencyTrack;
      if (var1 > -1) {
         var2 = this.loops;
         if (var2[var1] != null) {
            var2[var1].checkLatency = true;
         }
      }

   }

   SourceThread createSourceThread(int var1) {
      MyOutputConnector var2 = (MyOutputConnector)this.getOutputConnector(this.connectorNames[var1]);
      if (var2 != null && var2.getInputConnector() != null) {
         SourceThread var3 = new SourceThread(this, var2, var1);
         if (this.tracks[var1].getFormat() instanceof AudioFormat) {
            var3.useAudioPriority();
            return var3;
         } else {
            var3.useVideoPriority();
            return var3;
         }
      } else {
         this.tracks[var1].setEnabled(false);
         return null;
      }
   }

   public void doClose() {
      this.parser.close();
      if (this.tracks != null) {
         for(int var1 = 0; var1 < this.tracks.length; ++var1) {
            SourceThread[] var2 = this.loops;
            if (var2[var1] != null) {
               var2[var1].kill();
            }
         }

         RTPTimeBase var3 = this.rtpMapperUpdatable;
         if (var3 != null) {
            RTPTimeBase.returnMapperUpdatable(var3);
            this.rtpMapperUpdatable = null;
         }

      }
   }

   public void doDealloc() {
   }

   public void doFailedPrefetch() {
   }

   public void doFailedRealize() {
      this.parser.stop();
      this.parser.close();
   }

   public boolean doPrefetch() {
      super.doPrefetch();
      return true;
   }

   public boolean doRealize() {
      StringBuilder var3;
      try {
         this.parser.open();
      } catch (ResourceUnavailableException var6) {
         var3 = new StringBuilder();
         var3.append("Resource unavailable: ");
         var3.append(var6.getMessage());
         this.errMsg = var3.toString();
         return false;
      }

      Track[] var2;
      try {
         this.parser.start();
         var2 = this.parser.getTracks();
         this.tracks = var2;
      } catch (BadHeaderException var4) {
         var3 = new StringBuilder();
         var3.append("Bad header in the media: ");
         var3.append(var4.getMessage());
         this.errMsg = var3.toString();
         this.parser.close();
         return false;
      } catch (IOException var5) {
         var3 = new StringBuilder();
         var3.append("IO exception: ");
         var3.append(var5.getMessage());
         this.errMsg = var3.toString();
         this.parser.close();
         return false;
      }

      if (var2 != null && var2.length != 0) {
         this.loops = new SourceThread[var2.length];
         this.connectorNames = new String[var2.length];
         int var1 = 0;

         while(true) {
            var2 = this.tracks;
            if (var1 >= var2.length) {
               PlaybackEngine var8 = (PlaybackEngine)this.getController();
               this.engine = var8;
               if (var8 == null || !var8.isRTP()) {
                  this.parser.stop();
               }

               return true;
            }

            MyOutputConnector var7 = new MyOutputConnector(var2[var1]);
            var7.setProtocol(0);
            var7.setSize(1);
            this.connectorNames[var1] = this.tracks[var1].toString();
            this.registerOutputConnector(this.tracks[var1].toString(), var7);
            this.loops[var1] = null;
            ++var1;
         }
      } else {
         this.errMsg = "The media has 0 track";
         this.parser.close();
         return false;
      }
   }

   public void doStart() {
      this.lastSystemTime = this.systemTimeBase.getNanoseconds();
      this.originSystemTime = this.currentSystemTime;
      this.rtpOffsetInvalid = true;
      super.doStart();

      try {
         this.parser.start();
      } catch (IOException var4) {
      }

      for(int var1 = 0; var1 < this.loops.length; ++var1) {
         if (this.tracks[var1].isEnabled()) {
            SourceThread[] var2 = this.loops;
            if (var2[var1] == null) {
               SourceThread var3 = this.createSourceThread(var1);
               var2[var1] = var3;
               if (var3 == null) {
                  continue;
               }
            }

            this.loops[var1].start();
         }
      }

      this.started = true;
   }

   public void doStop() {
      this.started = false;
   }

   public long getBitsRead() {
      return this.bitsRead;
   }

   public Object getControl(String var1) {
      return this.parser.getControl(var1);
   }

   public Object[] getControls() {
      return this.parser.getControls();
   }

   public Demultiplexer getDemultiplexer() {
      return this.parser;
   }

   public Time getDuration() {
      return this.parser.getDuration();
   }

   public String[] getOutputConnectorNames() {
      return this.connectorNames;
   }

   public boolean isPositionable() {
      return this.parser.isPositionable();
   }

   public boolean isRandomAccess() {
      return this.parser.isRandomAccess();
   }

   public void pause() {
      Object var2 = this.resetSync;
      synchronized(var2){}
      int var1 = 0;

      while(true) {
         label176: {
            Throwable var10000;
            boolean var10001;
            label171: {
               try {
                  if (var1 < this.loops.length) {
                     if (this.tracks[var1].isEnabled() && this.loops[var1] != null && !this.loops[var1].resetted) {
                        this.loops[var1].pause();
                     }
                     break label176;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label171;
               }

               label164:
               try {
                  this.parser.stop();
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label164;
               }
            }

            while(true) {
               Throwable var3 = var10000;

               try {
                  throw var3;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  continue;
               }
            }
         }

         ++var1;
      }
   }

   public void process() {
   }

   boolean readHasBlocked() {
      if (this.loops == null) {
         return false;
      } else {
         int var1 = 0;

         while(true) {
            SourceThread[] var2 = this.loops;
            if (var1 >= var2.length) {
               return false;
            }

            if (var2[var1] != null && var2[var1].readBlocked) {
               return true;
            }

            ++var1;
         }
      }
   }

   public void reset() {
      Object var2 = this.resetSync;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label350: {
         try {
            super.reset();
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label350;
         }

         int var1 = 0;

         while(true) {
            label339: {
               label338: {
                  SourceThread[] var3;
                  SourceThread var4;
                  try {
                     if (var1 >= this.loops.length) {
                        break;
                     }

                     if (!this.tracks[var1].isEnabled()) {
                        break label339;
                     }

                     if (this.loops[var1] != null) {
                        break label338;
                     }

                     var3 = this.loops;
                     var4 = this.createSourceThread(var1);
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label350;
                  }

                  var3[var1] = var4;
                  if (var4 == null) {
                     break label339;
                  }
               }

               try {
                  this.loops[var1].resetted = true;
                  this.loops[var1].start();
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label350;
               }
            }

            ++var1;
         }

         label324:
         try {
            return;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label324;
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

   public void resetBitsRead() {
      this.bitsRead = 0L;
   }

   public void setFormat(Connector var1, Format var2) {
   }

   public Time setPosition(Time var1, int var2) {
      var1 = this.parser.setPosition(var1, var2);
      if (this.lastPositionSet.getNanoseconds() == var1.getNanoseconds()) {
         this.lastPositionSet = new Time(var1.getNanoseconds() + 1L);
         return var1;
      } else {
         this.lastPositionSet = var1;
         return var1;
      }
   }
}
