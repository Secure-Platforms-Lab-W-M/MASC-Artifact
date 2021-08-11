package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Clock;
import javax.media.Drainable;
import javax.media.Format;
import javax.media.Prefetchable;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.renderer.VideoRenderer;
import net.sf.fmj.filtergraph.SimpleGraphBuilder;
import net.sf.fmj.media.renderer.audio.AudioRenderer;
import net.sf.fmj.media.rtp.util.RTPTimeBase;
import net.sf.fmj.media.rtp.util.RTPTimeReporter;
import net.sf.fmj.media.util.ElapseTime;

public class BasicRendererModule extends BasicSinkModule implements RTPTimeReporter {
   static final int MAX_CHUNK_SIZE = 16;
   static final long RTP_TIME_MARGIN = 2000000000L;
   final int FLOW_LIMIT = 20;
   private long LEEWAY = 10L;
   final float MAX_RATE = 1.05F;
   final float RATE_INCR = 0.01F;
   private boolean checkRTP = false;
   private int chunkSize = Integer.MAX_VALUE;
   private ElapseTime elapseTime = new ElapseTime();
   protected PlaybackEngine engine;
   private boolean failed = false;
   protected float frameRate = 30.0F;
   protected int framesPlayed = 0;
   protected boolean framesWereBehind = false;
   // $FF: renamed from: ic net.sf.fmj.media.InputConnector
   protected InputConnector field_35;
   private long lastDuration = 0L;
   private long lastRendered = 0L;
   long lastTimeStamp;
   AudioFormat linearFormat = new AudioFormat("LINEAR");
   private boolean noSync = false;
   private boolean notToDropNext = false;
   private boolean opened = false;
   boolean overMsg = false;
   int overflown = 10;
   private Object prefetchSync = new Object();
   protected boolean prefetching = false;
   float rate = 1.0F;
   RenderThread renderThread;
   protected Renderer renderer;
   private String rtpCNAME = null;
   boolean rtpErrMsg = false;
   private RTPTimeBase rtpTimeBase = null;
   protected boolean started = false;
   private Buffer storedBuffer = null;
   long systemErr = 0L;
   AudioFormat ulawFormat = new AudioFormat("ULAW");

   protected BasicRendererModule(Renderer var1) {
      this.setRenderer(var1);
      BasicInputConnector var2 = new BasicInputConnector();
      this.field_35 = var2;
      if (var1 instanceof VideoRenderer) {
         var2.setSize(4);
      } else {
         var2.setSize(1);
      }

      this.field_35.setModule(this);
      this.registerInputConnector("input", this.field_35);
      this.setProtocol(1);
   }

   private int computeChunkSize(Format var1) {
      if (var1 instanceof AudioFormat && (this.ulawFormat.matches(var1) || this.linearFormat.matches(var1))) {
         AudioFormat var4 = (AudioFormat)var1;
         int var3 = var4.getSampleSizeInBits() * var4.getChannels() / 8;
         int var2 = var3;
         if (var3 == 0) {
            var2 = 1;
         }

         return (int)var4.getSampleRate() * var2 / 16 / var2 * var2;
      } else {
         return Integer.MAX_VALUE;
      }
   }

   private void donePrefetch() {
      Object var1 = this.prefetchSync;
      synchronized(var1){}

      label160: {
         Throwable var10000;
         boolean var10001;
         label155: {
            try {
               if (!this.started && this.prefetching) {
                  this.renderThread.pause();
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label155;
            }

            label152:
            try {
               this.prefetching = false;
               break label160;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label152;
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

      if (this.moduleListener != null) {
         this.moduleListener.bufferPrefetched(this);
      }

   }

   private long getSyncTime(long var1) {
      RTPTimeBase var5 = this.rtpTimeBase;
      if (var5 != null) {
         if (var5.getMaster() == this.getController()) {
            return var1;
         } else {
            long var3 = this.rtpTimeBase.getNanoseconds();
            if (var3 <= var1 + 2000000000L && var3 >= var1 - 2000000000L) {
               return var3;
            } else {
               if (!this.rtpErrMsg) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Cannot perform RTP sync beyond a difference of: ");
                  var6.append((var3 - var1) / 1000000L);
                  var6.append(" msecs.\n");
                  Log.comment(var6.toString());
                  this.rtpErrMsg = true;
               }

               return var1;
            }
         }
      } else {
         return this.getMediaNanoseconds();
      }
   }

   private boolean handleFormatChange(Format var1) {
      if (!this.reinitRenderer(var1)) {
         this.storedBuffer = null;
         this.failed = true;
         if (this.moduleListener != null) {
            this.moduleListener.formatChangedFailure(this, this.field_35.getFormat(), var1);
         }

         return false;
      } else {
         Format var3 = this.field_35.getFormat();
         this.field_35.setFormat(var1);
         if (this.moduleListener != null) {
            this.moduleListener.formatChanged(this, var3, var1);
         }

         if (var1 instanceof VideoFormat) {
            float var2 = ((VideoFormat)var1).getFrameRate();
            if (var2 != -1.0F) {
               this.frameRate = var2;
            }
         }

         return true;
      }
   }

   private boolean hasReachAudioPrerollTarget(Buffer var1) {
      long var3 = this.getSyncTime(var1.getTimeStamp());
      this.elapseTime.update(var1.getLength(), var1.getTimeStamp(), var1.getFormat());
      if (this.elapseTime.value >= var3) {
         long var5 = ElapseTime.audioTimeToLen(this.elapseTime.value - var3, (AudioFormat)var1.getFormat());
         int var2 = var1.getOffset() + var1.getLength() - (int)var5;
         if (var2 >= 0) {
            var1.setOffset(var2);
            var1.setLength((int)var5);
         }

         this.elapseTime.setValue(var3);
         return true;
      } else {
         return false;
      }
   }

   private boolean waitForPT(long var1) {
      long var6 = this.getSyncTime(var1);
      long var10 = -1L;
      byte var5 = 0;
      long var12 = (var1 - var6) / 1000000L;
      float var3 = this.rate;
      int var4 = var5;
      long var8 = var10;
      var6 = var12;
      if (var3 != 1.0F) {
         var6 = (long)((float)var12 / var3);
         var8 = var10;
         var4 = var5;
      }

      while(var6 > this.systemErr && !this.resetted) {
         if (var6 == var8) {
            var8 = (long)(var4 * 5) + var6;
            if (var8 > 33L) {
               var8 = 33L;
            } else {
               ++var4;
            }
         } else {
            var8 = var6;
            var4 = 0;
         }

         var10 = 125L;
         if (var8 > 125L) {
            var8 = var10;
         }

         var10 = System.currentTimeMillis();
         var8 -= this.systemErr;
         if (var8 > 0L) {
            try {
               Thread.sleep(var8);
            } catch (InterruptedException var15) {
            }
         }

         var10 = (System.currentTimeMillis() - var10 - var8 + this.systemErr) / 2L;
         this.systemErr = var10;
         if (var10 < 0L) {
            this.systemErr = 0L;
         } else if (var10 > var8) {
            this.systemErr = var8;
         }

         var10 = this.getSyncTime(var1);
         var8 = var6;
         var6 = (var1 - var10) / 1000000L;
         var3 = this.rate;
         if (var3 != 1.0F) {
            var6 = (long)((float)var6 / var3);
         }

         if (this.getState() != 600) {
            break;
         }
      }

      return true;
   }

   public void abortPrefetch() {
      this.renderThread.pause();
      this.renderer.close();
      this.prefetching = false;
      this.opened = false;
   }

   public void doClose() {
      this.renderThread.kill();
      Renderer var1 = this.renderer;
      if (var1 != null) {
         var1.close();
      }

      if (this.rtpTimeBase != null) {
         RTPTimeBase.remove(this, this.rtpCNAME);
         this.rtpTimeBase = null;
      }

   }

   public void doDealloc() {
      this.renderer.close();
   }

   public void doFailedPrefetch() {
      this.renderThread.pause();
      this.renderer.close();
      this.opened = false;
      this.prefetching = false;
   }

   public boolean doPrefetch() {
      super.doPrefetch();
      if (!this.opened) {
         try {
            this.renderer.open();
         } catch (ResourceUnavailableException var2) {
            this.prefetchFailed = true;
            return false;
         }

         this.prefetchFailed = false;
         this.opened = true;
      }

      if (!((PlaybackEngine)this.controller).prefetchEnabled) {
         return true;
      } else {
         this.prefetching = true;
         this.renderThread.start();
         return true;
      }
   }

   protected boolean doProcess() {
      if ((this.started || this.prefetching) && this.stopTime > -1L && this.elapseTime.value >= this.stopTime) {
         Renderer var2 = this.renderer;
         if (var2 instanceof Drainable) {
            ((Drainable)var2).drain();
         }

         this.doStop();
         if (this.moduleListener != null) {
            this.moduleListener.stopAtTime(this);
         }
      }

      Buffer var4;
      if (this.storedBuffer != null) {
         var4 = this.storedBuffer;
      } else {
         var4 = this.field_35.getValidBuffer();
      }

      if (!this.checkRTP) {
         if ((var4.getFlags() & 4096) != 0) {
            String var3 = this.engine.getCNAME();
            if (var3 != null) {
               this.rtpTimeBase = RTPTimeBase.find(this, var3);
               this.rtpCNAME = var3;
               if (this.field_35.getFormat() instanceof AudioFormat) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("RTP master time set: ");
                  var5.append(this.renderer);
                  var5.append("\n");
                  Log.comment(var5.toString());
                  this.rtpTimeBase.setMaster(this);
               }

               this.checkRTP = true;
               this.noSync = false;
            } else {
               this.noSync = true;
            }
         } else {
            this.checkRTP = true;
         }
      }

      this.lastTimeStamp = var4.getTimeStamp();
      if (!this.failed && !this.resetted) {
         boolean var1 = this.scheduleBuffer(var4);
         if (this.storedBuffer == null && var4.isEOM()) {
            if (this.prefetching) {
               this.donePrefetch();
            }

            if ((var4.getFlags() & 64) == 0 && var4.getTimeStamp() > 0L && var4.getDuration() > 0L && var4.getFormat() != null && !(var4.getFormat() instanceof AudioFormat) && !this.noSync) {
               this.waitForPT(var4.getTimeStamp() + this.lastDuration);
            }

            this.storedBuffer = null;
            this.field_35.readReport();
            this.doStop();
            if (this.moduleListener != null) {
               this.moduleListener.mediaEnded(this);
            }

            return true;
         } else {
            if (this.storedBuffer == null) {
               this.field_35.readReport();
            }

            return var1;
         }
      } else {
         if ((var4.getFlags() & 512) != 0) {
            this.resetted = false;
            this.renderThread.pause();
            if (this.moduleListener != null) {
               this.moduleListener.resetted(this);
            }
         }

         this.storedBuffer = null;
         this.field_35.readReport();
         return true;
      }
   }

   public boolean doRealize() {
      this.chunkSize = this.computeChunkSize(this.field_35.getFormat());
      this.renderThread = new RenderThread(this);
      this.engine = (PlaybackEngine)this.getController();
      return true;
   }

   public void doStart() {
      // $FF: Couldn't be decompiled
   }

   public void doStop() {
      this.started = false;
      this.prefetching = true;
      super.doStop();
      Renderer var1 = this.renderer;
      if (var1 != null && !(var1 instanceof Clock)) {
         var1.stop();
      }

   }

   public void doneReset() {
      this.renderThread.pause();
   }

   public Object getControl(String var1) {
      return this.renderer.getControl(var1);
   }

   public Object[] getControls() {
      return this.renderer.getControls();
   }

   public int getFramesPlayed() {
      return this.framesPlayed;
   }

   public long getRTPTime() {
      if (this.field_35.getFormat() instanceof AudioFormat) {
         Renderer var1 = this.renderer;
         return var1 instanceof AudioRenderer ? this.lastTimeStamp - ((AudioRenderer)var1).getLatency() : this.lastTimeStamp;
      } else {
         return this.lastTimeStamp;
      }
   }

   public Renderer getRenderer() {
      return this.renderer;
   }

   protected boolean handlePreroll(Buffer var1) {
      if (var1.getFormat() instanceof AudioFormat) {
         if (!this.hasReachAudioPrerollTarget(var1)) {
            return false;
         }
      } else if ((var1.getFlags() & 96) == 0 && var1.getTimeStamp() >= 0L && var1.getTimeStamp() < this.getSyncTime(var1.getTimeStamp())) {
         return false;
      }

      this.prerolling = false;
      return true;
   }

   public boolean isThreaded() {
      return true;
   }

   protected void process() {
   }

   public int processBuffer(Buffer var1) {
      int var3 = var1.getLength();
      int var5 = var1.getOffset();
      byte var6 = 0;
      boolean var9 = false;
      int var7 = var3;
      int var10 = var5;
      int var8 = var6;
      boolean var4 = var9;
      if (this.renderer instanceof Clock) {
         if ((var1.getFlags() & 8192) != 0) {
            ++this.overflown;
         } else {
            --this.overflown;
         }

         int var11 = this.overflown;
         float var2;
         if (var11 > 20) {
            var2 = this.rate;
            if (var2 < 1.05F) {
               this.rate = var2 + 0.01F;
               this.renderer.stop();
               ((Clock)this.renderer).setRate(this.rate);
               this.renderer.start();
               if (!this.overMsg) {
                  Log.comment("Data buffers overflown.  Adjust rendering speed up to 5 % to compensate");
                  this.overMsg = true;
               }
            }

            this.overflown = 10;
            var7 = var3;
            var10 = var5;
            var8 = var6;
            var4 = var9;
         } else {
            var7 = var3;
            var10 = var5;
            var8 = var6;
            var4 = var9;
            if (var11 <= 0) {
               var2 = this.rate;
               if (var2 > 1.0F) {
                  this.rate = var2 - 0.01F;
                  this.renderer.stop();
                  ((Clock)this.renderer).setRate(this.rate);
                  this.renderer.start();
               }

               this.overflown = 10;
               var4 = var9;
               var8 = var6;
               var10 = var5;
               var7 = var3;
            }
         }
      }

      while(this.stopTime <= -1L || this.elapseTime.value < this.stopTime) {
         boolean var15;
         if (var7 > this.chunkSize && !this.prerolling) {
            var15 = var4;
            if (var1.isEOM()) {
               var15 = true;
               var1.setEOM(false);
            }

            var5 = this.chunkSize;
         } else {
            var15 = var4;
            if (var4) {
               var15 = false;
               var1.setEOM(true);
            }

            var5 = var7;
         }

         int var16;
         boolean var19;
         label268: {
            var1.setLength(var5);
            var1.setOffset(var10);
            int var17;
            int var18;
            if (this.prerolling && !this.handlePreroll(var1)) {
               var17 = var10 + var5;
               var5 = var7 - var5;
               var18 = var8;
            } else {
               label250:
               try {
                  var16 = this.renderer.process(var1);
               } catch (Throwable var14) {
                  Log.dumpStack(var14);
                  var16 = var8;
                  if (this.moduleListener != null) {
                     this.moduleListener.internalErrorOccurred(this);
                     var16 = var8;
                  }
                  break label250;
               }

               if ((var16 & 8) != 0) {
                  this.failed = true;
                  if (this.moduleListener != null) {
                     this.moduleListener.pluginTerminated(this);
                  }

                  return var16;
               }

               if ((var16 & 1) != 0) {
                  var1.setDiscard(true);
                  if (this.prefetching) {
                     this.donePrefetch();
                  }

                  return var16;
               }

               var8 = var5;
               if ((var16 & 2) != 0) {
                  var8 = var5 - var1.getLength();
               }

               var17 = var10 + var8;
               var5 = var7 - var8;
               if (this.prefetching) {
                  Renderer var12 = this.renderer;
                  if (!(var12 instanceof Prefetchable) || ((Prefetchable)var12).isPrefetched()) {
                     var19 = false;
                     var1.setEOM(false);
                     this.donePrefetch();
                     var7 = var5;
                     var8 = var17;
                     break label268;
                  }
               }

               this.elapseTime.update(var8, var1.getTimeStamp(), var1.getFormat());
               var18 = var16;
            }

            var7 = var5;
            var8 = var17;
            var16 = var18;
            var19 = var15;
            if (var5 > 0) {
               var7 = var5;
               var10 = var17;
               var8 = var18;
               var4 = var15;
               if (!this.resetted) {
                  continue;
               }

               var19 = var15;
               var16 = var18;
               var8 = var17;
               var7 = var5;
            }
         }

         if (var19) {
            var1.setEOM(true);
         }

         var1.setLength(var7);
         var1.setOffset(var8);
         if (var16 == 0) {
            ++this.framesPlayed;
         }

         return var16;
      }

      if (this.prefetching) {
         this.donePrefetch();
      }

      return 2;
   }

   protected boolean reinitRenderer(Format var1) {
      Renderer var2 = this.renderer;
      if (var2 != null && var2.setInputFormat(var1) != null) {
         return true;
      } else {
         if (this.started) {
            this.renderer.stop();
            this.renderer.reset();
         }

         this.renderer.close();
         this.renderer = null;
         var2 = SimpleGraphBuilder.findRenderer(var1);
         if (var2 == null) {
            return false;
         } else {
            this.setRenderer(var2);
            if (this.started) {
               this.renderer.start();
            }

            this.chunkSize = this.computeChunkSize(var1);
            return true;
         }
      }
   }

   public void reset() {
      super.reset();
      this.prefetching = false;
   }

   public void resetFramesPlayed() {
      this.framesPlayed = 0;
   }

   protected boolean scheduleBuffer(Buffer var1) {
      byte var5 = 0;
      Format var9 = var1.getFormat();
      Format var8 = var9;
      if (var9 == null) {
         var8 = this.field_35.getFormat();
         var1.setFormat(var8);
      }

      if (var8 != this.field_35.getFormat() && !var8.equals(this.field_35.getFormat()) && !var1.isDiscard() && !this.handleFormatChange(var8)) {
         return false;
      } else {
         if ((var1.getFlags() & 1024) != 0 && this.moduleListener != null) {
            this.moduleListener.markedDataArrived(this, var1);
            var1.setFlags(var1.getFlags() & -1025);
         }

         int var4;
         if (!this.prefetching && !(var8 instanceof AudioFormat) && var1.getTimeStamp() > 0L && (var1.getFlags() & 96) != 96 && !this.noSync) {
            long var6 = this.getSyncTime(var1.getTimeStamp()) / 1000000L - var1.getTimeStamp() / 1000000L - this.getLatency() / 1000000L;
            if (this.storedBuffer == null && var6 > 0L) {
               if (var1.isDiscard()) {
                  this.notToDropNext = true;
                  var4 = var5;
               } else {
                  if (var1.isEOM()) {
                     this.notToDropNext = true;
                  } else if (this.moduleListener != null && var8 instanceof VideoFormat) {
                     float var3 = (float)var6 * this.frameRate / 1000.0F;
                     float var2 = var3;
                     if (var3 < 1.0F) {
                        var2 = 1.0F;
                     }

                     this.moduleListener.framesBehind(this, var2, this.field_35);
                     this.framesWereBehind = true;
                  }

                  if ((var1.getFlags() & 32) != 0) {
                     var4 = this.processBuffer(var1);
                  } else {
                     label84: {
                        if (var6 >= this.LEEWAY && !this.notToDropNext) {
                           var4 = var5;
                           if (var1.getTimeStamp() - this.lastRendered <= 1000000000L) {
                              break label84;
                           }
                        }

                        var4 = this.processBuffer(var1);
                        this.lastRendered = var1.getTimeStamp();
                        this.notToDropNext = false;
                     }
                  }
               }
            } else {
               if (this.moduleListener != null && this.framesWereBehind && var8 instanceof VideoFormat) {
                  this.moduleListener.framesBehind(this, 0.0F, this.field_35);
                  this.framesWereBehind = false;
               }

               var4 = var5;
               if (!var1.isDiscard()) {
                  if ((var1.getFlags() & 64) == 0) {
                     this.waitForPT(var1.getTimeStamp());
                  }

                  var4 = var5;
                  if (!this.resetted) {
                     var4 = this.processBuffer(var1);
                     this.lastRendered = var1.getTimeStamp();
                  }
               }
            }
         } else {
            var4 = var5;
            if (!var1.isDiscard()) {
               var4 = this.processBuffer(var1);
            }
         }

         if ((var4 & 1) != 0) {
            this.storedBuffer = null;
         } else if ((var4 & 2) != 0) {
            this.storedBuffer = var1;
         } else {
            this.storedBuffer = null;
            if (var1.getDuration() >= 0L) {
               this.lastDuration = var1.getDuration();
            }
         }

         return true;
      }
   }

   public void setFormat(Connector var1, Format var2) {
      this.renderer.setInputFormat(var2);
      if (var2 instanceof VideoFormat) {
         float var3 = ((VideoFormat)var2).getFrameRate();
         if (var3 != -1.0F) {
            this.frameRate = var3;
         }
      }

   }

   public void setPreroll(long var1, long var3) {
      super.setPreroll(var1, var3);
      this.elapseTime.setValue(var3);
   }

   protected void setRenderer(Renderer var1) {
      this.renderer = var1;
      if (var1 instanceof Clock) {
         this.setClock((Clock)var1);
      }

   }

   public void triggerReset() {
      Renderer var1 = this.renderer;
      if (var1 != null) {
         var1.reset();
      }

      Object var15 = this.prefetchSync;
      synchronized(var15){}

      Throwable var10000;
      boolean var10001;
      label136: {
         try {
            this.prefetching = false;
            if (this.resetted) {
               this.renderThread.start();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label136;
         }

         label133:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label133;
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
}
