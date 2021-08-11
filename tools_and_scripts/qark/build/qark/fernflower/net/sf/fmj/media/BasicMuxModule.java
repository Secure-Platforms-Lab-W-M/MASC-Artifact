package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Clock;
import javax.media.Drainable;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.Prefetchable;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import net.sf.fmj.media.util.ElapseTime;

public class BasicMuxModule extends BasicSinkModule {
   public static String ConnectorNamePrefix = "input";
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   private long bitsWritten = 0L;
   private boolean closed = false;
   protected ElapseTime[] elapseTime;
   protected boolean[] endMarkers;
   private boolean failed = false;
   private VideoFormat firstVideoFormat = null;
   private float frameRate = 30.0F;
   private int framesPlayed = 0;
   protected InputConnector[] ics;
   protected Format[] inputs;
   private float lastFramesBehind = -1.0F;
   protected Multiplexer multiplexer;
   private Object[] pauseSync;
   protected boolean[] paused;
   protected boolean[] prefetchMarkers;
   private Object prefetchSync = new Object();
   protected boolean prefetching = false;
   protected boolean[] prerollTrack;
   protected boolean[] resettedMarkers;
   private VideoFormat rtpVideoFormat = null;
   protected boolean started = false;
   protected boolean[] stopAtTimeMarkers;

   protected BasicMuxModule(Multiplexer var1, Format[] var2) {
      this.multiplexer = var1;
      if (var2 != null) {
         this.ics = new InputConnector[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            BasicMuxModule.MyInputConnector var5 = new BasicMuxModule.MyInputConnector();
            var5.setSize(1);
            var5.setModule(this);
            StringBuilder var4 = new StringBuilder();
            var4.append(ConnectorNamePrefix);
            var4.append(var3);
            this.registerInputConnector(var4.toString(), var5);
            this.ics[var3] = var5;
            if (var2[var3] instanceof VideoFormat && this.firstVideoFormat == null) {
               this.firstVideoFormat = (VideoFormat)var2[var3];
               if (var2[var3].getEncoding().toUpperCase().endsWith("RTP")) {
                  this.rtpVideoFormat = this.firstVideoFormat;
               }
            }
         }

         this.inputs = var2;
      }

      var1 = this.multiplexer;
      if (var1 != null && var1 instanceof Clock) {
         this.setClock((Clock)var1);
      }

      this.setProtocol(0);
   }

   public void abortPrefetch() {
      this.prefetching = false;
   }

   boolean checkEnd(int var1) {
      boolean[] var2 = this.endMarkers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label236: {
         try {
            this.endMarkers[var1] = true;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label236;
         }

         var1 = 0;

         while(true) {
            try {
               if (var1 >= this.endMarkers.length) {
                  break;
               }

               if (!this.endMarkers[var1]) {
                  return false;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label236;
            }

            ++var1;
         }

         label217:
         try {
            return true;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label217;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   boolean checkPrefetch(int var1) {
      boolean[] var2 = this.prefetchMarkers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label236: {
         try {
            this.prefetchMarkers[var1] = true;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label236;
         }

         var1 = 0;

         while(true) {
            try {
               if (var1 >= this.prefetchMarkers.length) {
                  break;
               }

               if (!this.prefetchMarkers[var1]) {
                  return false;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label236;
            }

            ++var1;
         }

         label217:
         try {
            return true;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label217;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   boolean checkResetted(int var1) {
      boolean[] var2 = this.resettedMarkers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label236: {
         try {
            this.resettedMarkers[var1] = true;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label236;
         }

         var1 = 0;

         while(true) {
            try {
               if (var1 >= this.resettedMarkers.length) {
                  break;
               }

               if (!this.resettedMarkers[var1]) {
                  return false;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label236;
            }

            ++var1;
         }

         label217:
         try {
            return true;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label217;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   boolean checkStopAtTime(int var1) {
      boolean[] var2 = this.stopAtTimeMarkers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label236: {
         try {
            this.stopAtTimeMarkers[var1] = true;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label236;
         }

         var1 = 0;

         while(true) {
            try {
               if (var1 >= this.stopAtTimeMarkers.length) {
                  break;
               }

               if (!this.stopAtTimeMarkers[var1]) {
                  return false;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label236;
            }

            ++var1;
         }

         label217:
         try {
            return true;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label217;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public void connectorPushed(InputConnector var1) {
      byte var6 = -1;
      InputConnector[] var12 = this.ics;
      int var4;
      int var5;
      if (var12[0] == var1) {
         var4 = 0;
      } else if (var12[1] == var1) {
         var4 = 1;
      } else {
         var4 = 2;

         while(true) {
            var12 = this.ics;
            var5 = var6;
            if (var4 >= var12.length) {
               break;
            }

            if (var12[var4] == var1) {
               var5 = var4;
               break;
            }

            ++var4;
         }

         if (var5 == -1) {
            throw new RuntimeException("BasicMuxModule: unmatched input connector!");
         }

         var4 = var5;
      }

      while(true) {
         Throwable var10000;
         boolean var10001;
         Throwable var96;
         if (this.paused[var4]) {
            Object var102 = this.pauseSync[var4];
            synchronized(var102){}

            while(true) {
               label2552: {
                  try {
                     try {
                        if (this.paused[var4] && !this.closed) {
                           this.pauseSync[var4].wait();
                           continue;
                        }
                     } catch (Exception var94) {
                     }
                  } catch (Throwable var95) {
                     var10000 = var95;
                     var10001 = false;
                     break label2552;
                  }

                  label2504:
                  try {
                     break;
                  } catch (Throwable var93) {
                     var10000 = var93;
                     var10001 = false;
                     break label2504;
                  }
               }

               while(true) {
                  var96 = var10000;

                  try {
                     throw var96;
                  } catch (Throwable var87) {
                     var10000 = var87;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }

         if (this.stopTime <= -1L || this.elapseTime[var4].value < this.stopTime) {
            Buffer var14 = var1.getValidBuffer();
            int var8 = var14.getFlags();
            byte var98 = 0;
            if (this.resetted) {
               if ((var8 & 512) != 0 && this.checkResetted(var4)) {
                  this.resetted = false;
                  this.doStop();
                  if (this.moduleListener != null) {
                     this.moduleListener.resetted(this);
                  }
               }

               var1.readReport();
               return;
            } else if (!this.failed && !this.closed && !var14.isDiscard()) {
               if ((var8 & 1024) != 0 && this.moduleListener != null) {
                  this.moduleListener.markedDataArrived(this, var14);
                  var8 &= -1025;
                  var14.setFlags(var8);
               }

               boolean var7 = false;
               Format var13 = var14.getFormat();
               Format var104 = var13;
               if (var13 == null) {
                  var104 = var1.getFormat();
                  var14.setFormat(var104);
               }

               int var100;
               boolean var101;
               if (this.elapseTime[var4].update(var14.getLength(), var14.getTimeStamp(), var104)) {
                  boolean var97 = var7;
                  if (this.prerollTrack[var4]) {
                     long var10 = this.getMediaNanoseconds();
                     if (this.elapseTime[var4].value > var10) {
                        if (var104 instanceof AudioFormat && "LINEAR".equals(var104.getEncoding())) {
                           var5 = (int)ElapseTime.audioTimeToLen(this.elapseTime[var4].value - var10, (AudioFormat)var104);
                           int var9 = var14.getOffset() + var14.getLength() - var5;
                           if (var9 >= 0) {
                              var14.setOffset(var9);
                              var14.setLength(var5);
                           }
                        }

                        this.prerollTrack[var4] = false;
                        this.elapseTime[var4].setValue(var10);
                        var97 = var7;
                     } else {
                        var97 = true;
                     }
                  }

                  if (this.stopTime > -1L && this.elapseTime[var4].value > this.stopTime && var104 instanceof AudioFormat) {
                     var100 = (int)ElapseTime.audioTimeToLen(this.elapseTime[var4].value - this.stopTime, (AudioFormat)var104);
                     if (var14.getLength() > var100) {
                        var14.setLength(var14.getLength() - var100);
                     }
                  }

                  var101 = var97;
               } else {
                  var101 = false;
               }

               var5 = var98;
               if (this.moduleListener != null) {
                  var5 = var98;
                  if (var104 instanceof VideoFormat) {
                     float var3 = (float)(this.getMediaNanoseconds() / 1000000L - var14.getTimeStamp() / 1000000L - this.getLatency() / 1000000L) * this.frameRate / 1000.0F;
                     float var2 = var3;
                     if (var3 < 0.0F) {
                        var2 = 0.0F;
                     }

                     var5 = var98;
                     if (this.lastFramesBehind != var2) {
                        var5 = var98;
                        if ((var8 & 32) == 0) {
                           this.moduleListener.framesBehind(this, var2, var1);
                           this.lastFramesBehind = var2;
                           var5 = var98;
                        }
                     }
                  }
               }

               do {
                  if (!var101) {
                     int var99;
                     label2460:
                     try {
                        var99 = this.multiplexer.process(var14, var4);
                     } catch (Throwable var89) {
                        Log.dumpStack(var89);
                        var99 = var5;
                        if (this.moduleListener != null) {
                           this.moduleListener.internalErrorOccurred(this);
                           var99 = var5;
                        }
                        break label2460;
                     }

                     var100 = var99;
                     if (var99 == 0) {
                        var100 = var99;
                        if (var104 == this.firstVideoFormat) {
                           if (var104 == this.rtpVideoFormat) {
                              if ((var8 & 2048) > 0) {
                                 ++this.framesPlayed;
                                 var100 = var99;
                              } else {
                                 var100 = var99;
                              }
                           } else {
                              ++this.framesPlayed;
                              var100 = var99;
                           }
                        }
                     }
                  } else {
                     var100 = 0;
                  }

                  if ((var100 & 8) != 0) {
                     this.failed = true;
                     if (this.moduleListener != null) {
                        this.moduleListener.pluginTerminated(this);
                     }

                     var1.readReport();
                     return;
                  }

                  if (this.prefetching) {
                     Multiplexer var105 = this.multiplexer;
                     if (!(var105 instanceof Prefetchable) || ((Prefetchable)var105).isPrefetched()) {
                        Object var106 = this.prefetchSync;
                        synchronized(var106){}

                        label2550: {
                           label2551: {
                              try {
                                 if (!this.started && this.prefetching && !this.resetted) {
                                    this.paused[var4] = true;
                                 }
                              } catch (Throwable var92) {
                                 var10000 = var92;
                                 var10001 = false;
                                 break label2551;
                              }

                              try {
                                 if (this.checkPrefetch(var4)) {
                                    this.prefetching = false;
                                 }
                              } catch (Throwable var91) {
                                 var10000 = var91;
                                 var10001 = false;
                                 break label2551;
                              }

                              label2474:
                              try {
                                 break label2550;
                              } catch (Throwable var90) {
                                 var10000 = var90;
                                 var10001 = false;
                                 break label2474;
                              }
                           }

                           while(true) {
                              var96 = var10000;

                              try {
                                 throw var96;
                              } catch (Throwable var88) {
                                 var10000 = var88;
                                 var10001 = false;
                                 continue;
                              }
                           }
                        }

                        if (!this.prefetching && this.moduleListener != null) {
                           this.moduleListener.bufferPrefetched(this);
                        }
                     }
                  }

                  if (this.resetted) {
                     break;
                  }

                  var5 = var100;
               } while(var100 == 2);

               this.bitsWritten += (long)var14.getLength();
               if (var14.isEOM()) {
                  if (!this.resetted) {
                     this.paused[var4] = true;
                  }

                  if (this.checkEnd(var4)) {
                     this.doStop();
                     if (this.moduleListener != null) {
                        this.moduleListener.mediaEnded(this);
                     }
                  }
               }

               var1.readReport();
               return;
            } else {
               var1.readReport();
               return;
            }
         }

         this.paused[var4] = true;
         if (this.checkStopAtTime(var4)) {
            Multiplexer var103 = this.multiplexer;
            if (var103 instanceof Drainable) {
               ((Drainable)var103).drain();
            }

            this.doStop();
            if (this.moduleListener != null) {
               this.moduleListener.stopAtTime(this);
            }
         }
      }
   }

   public void doClose() {
      // $FF: Couldn't be decompiled
   }

   public void doDealloc() {
   }

   public void doFailedPrefetch() {
      this.prefetching = false;
   }

   public boolean doPrefetch() {
      if (!((PlaybackEngine)this.controller).prefetchEnabled) {
         return true;
      } else {
         this.resetPrefetchMarkers();
         this.prefetching = true;
         this.resume();
         return true;
      }
   }

   public boolean doRealize() {
      Multiplexer var2 = this.multiplexer;
      if (var2 == null) {
         return false;
      } else if (this.inputs == null) {
         return false;
      } else {
         try {
            var2.open();
         } catch (ResourceUnavailableException var3) {
            return false;
         }

         InputConnector[] var4 = this.ics;
         this.prefetchMarkers = new boolean[var4.length];
         this.endMarkers = new boolean[var4.length];
         this.resettedMarkers = new boolean[var4.length];
         this.stopAtTimeMarkers = new boolean[var4.length];
         this.paused = new boolean[var4.length];
         this.prerollTrack = new boolean[var4.length];
         this.pauseSync = new Object[var4.length];
         this.elapseTime = new ElapseTime[var4.length];

         for(int var1 = 0; var1 < this.ics.length; ++var1) {
            this.prerollTrack[var1] = false;
            this.pauseSync[var1] = new Object();
            this.elapseTime[var1] = new ElapseTime();
         }

         this.pause();
         return true;
      }
   }

   public void doStart() {
      // $FF: Couldn't be decompiled
   }

   public void doStop() {
      super.doStop();
      this.started = false;
      this.resetPrefetchMarkers();
      this.prefetching = true;
   }

   public long getBitsWritten() {
      return this.bitsWritten;
   }

   public Object getControl(String var1) {
      return this.multiplexer.getControl(var1);
   }

   public Object[] getControls() {
      return this.multiplexer.getControls();
   }

   public DataSource getDataOutput() {
      return this.multiplexer.getDataOutput();
   }

   public int getFramesPlayed() {
      return this.framesPlayed;
   }

   public Multiplexer getMultiplexer() {
      return this.multiplexer;
   }

   public boolean isThreaded() {
      return false;
   }

   void pause() {
      int var1 = 0;

      while(true) {
         boolean[] var2 = this.paused;
         if (var1 >= var2.length) {
            return;
         }

         var2[var1] = true;
         ++var1;
      }
   }

   protected void process() {
   }

   public void reset() {
      super.reset();
      this.resetResettedMarkers();
      this.prefetching = false;
   }

   public void resetBitsWritten() {
      this.bitsWritten = 0L;
   }

   void resetEndMarkers() {
      boolean[] var2 = this.endMarkers;
      synchronized(var2){}
      int var1 = 0;

      while(true) {
         label141: {
            Throwable var10000;
            boolean var10001;
            label136: {
               try {
                  if (var1 < this.endMarkers.length) {
                     this.endMarkers[var1] = false;
                     break label141;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label136;
               }

               label129:
               try {
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label129;
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

   public void resetFramesPlayed() {
      this.framesPlayed = 0;
   }

   void resetPrefetchMarkers() {
      boolean[] var2 = this.prefetchMarkers;
      synchronized(var2){}
      int var1 = 0;

      while(true) {
         label141: {
            Throwable var10000;
            boolean var10001;
            label136: {
               try {
                  if (var1 < this.prefetchMarkers.length) {
                     this.prefetchMarkers[var1] = false;
                     break label141;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label136;
               }

               label129:
               try {
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label129;
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

   void resetResettedMarkers() {
      boolean[] var2 = this.resettedMarkers;
      synchronized(var2){}
      int var1 = 0;

      while(true) {
         label141: {
            Throwable var10000;
            boolean var10001;
            label136: {
               try {
                  if (var1 < this.resettedMarkers.length) {
                     this.resettedMarkers[var1] = false;
                     break label141;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label136;
               }

               label129:
               try {
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label129;
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

   void resetStopAtTimeMarkers() {
      boolean[] var2 = this.stopAtTimeMarkers;
      synchronized(var2){}
      int var1 = 0;

      while(true) {
         label141: {
            Throwable var10000;
            boolean var10001;
            label136: {
               try {
                  if (var1 < this.stopAtTimeMarkers.length) {
                     this.stopAtTimeMarkers[var1] = false;
                     break label141;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label136;
               }

               label129:
               try {
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label129;
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

   void resume() {
      // $FF: Couldn't be decompiled
   }

   public void setFormat(Connector var1, Format var2) {
      if (var2 instanceof VideoFormat) {
         float var3 = ((VideoFormat)var2).getFrameRate();
         if (var3 != -1.0F) {
            this.frameRate = var3;
         }
      }

   }

   public void setPreroll(long var1, long var3) {
      super.setPreroll(var1, var3);
      int var5 = 0;

      while(true) {
         ElapseTime[] var6 = this.elapseTime;
         if (var5 >= var6.length) {
            return;
         }

         var6[var5].setValue(var3);
         Format[] var7 = this.inputs;
         if (var7[var5] instanceof AudioFormat && mpegAudio.matches(var7[var5])) {
            this.prerollTrack[var5] = false;
         } else {
            this.prerollTrack[var5] = true;
         }

         ++var5;
      }
   }

   public void triggerReset() {
      this.multiplexer.reset();
      Object var1 = this.prefetchSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.prefetching = false;
            if (this.resetted) {
               this.resume();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
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

   class MyInputConnector extends BasicInputConnector {
      public MyInputConnector() {
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append(": ");
         var1.append(this.getFormat());
         return var1.toString();
      }
   }
}
