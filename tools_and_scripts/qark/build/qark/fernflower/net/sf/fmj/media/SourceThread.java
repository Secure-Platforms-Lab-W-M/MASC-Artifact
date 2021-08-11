package net.sf.fmj.media;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Track;
import javax.media.TrackListener;
import net.sf.fmj.media.rtp.util.RTPTimeBase;
import net.sf.fmj.media.util.LoopThread;

class SourceThread extends LoopThread implements TrackListener {
   static int remapTimeFlag = 4480;
   BasicSourceModule bsm;
   protected boolean checkLatency = false;
   long counter = 0L;
   long currentTime = 0L;
   int index = 0;
   protected long lastRelativeTime = -1L;
   // $FF: renamed from: oc net.sf.fmj.media.MyOutputConnector
   protected MyOutputConnector field_27;
   protected boolean readBlocked = false;
   protected boolean resetted = false;
   long sequenceNum = 0L;

   public SourceThread(BasicSourceModule var1, MyOutputConnector var2, int var3) {
      this.bsm = var1;
      this.field_27 = var2;
      this.index = var3;
      StringBuilder var4 = new StringBuilder();
      var4.append(this.getName());
      var4.append(": ");
      var4.append(var2.track);
      this.setName(var4.toString());
      var2.track.setTrackListener(this);
   }

   private boolean remapRTPTime(Buffer var1) {
      if (var1.getTimeStamp() <= 0L) {
         var1.setTimeStamp(-1L);
         return true;
      } else {
         BasicSourceModule var6;
         if (this.bsm.cname == null) {
            var6 = this.bsm;
            var6.cname = var6.engine.getCNAME();
            if (this.bsm.cname == null) {
               var1.setTimeStamp(-1L);
               return true;
            }
         }

         if (this.bsm.rtpOffsetInvalid) {
            if (this.bsm.rtpMapperUpdatable == null) {
               var6 = this.bsm;
               var6.rtpMapperUpdatable = RTPTimeBase.getMapperUpdatable(var6.cname);
               if (this.bsm.rtpMapperUpdatable == null) {
                  this.bsm.rtpOffsetInvalid = false;
               }
            }

            if (this.bsm.rtpMapperUpdatable != null) {
               this.bsm.rtpMapperUpdatable.setOrigin(this.bsm.currentRTPTime);
               this.bsm.rtpMapperUpdatable.setOffset(var1.getTimeStamp());
               this.bsm.rtpOffsetInvalid = false;
            }
         }

         if (this.bsm.rtpMapper == null) {
            var6 = this.bsm;
            var6.rtpMapper = RTPTimeBase.getMapper(var6.cname);
         }

         if (this.bsm.rtpMapper.getOffset() != this.bsm.oldOffset) {
            var6 = this.bsm;
            var6.oldOffset = var6.rtpMapper.getOffset();
         }

         long var4 = var1.getTimeStamp() - this.bsm.rtpMapper.getOffset();
         long var2 = var4;
         if (var4 < 0L) {
            if (this.bsm.rtpMapperUpdatable != null) {
               this.bsm.rtpOffsetInvalid = true;
               var2 = var4;
            } else {
               var2 = 0L;
            }
         }

         var6 = this.bsm;
         var6.currentRTPTime = var6.rtpMapper.getOrigin() + var2;
         var1.setTimeStamp(this.bsm.currentRTPTime);
         return true;
      }
   }

   private boolean remapRelativeTime(Buffer var1) {
      var1.setFlags(var1.getFlags() & -257 | 96);
      return true;
   }

   private boolean remapSystemTime(Buffer var1) {
      if (!this.bsm.started) {
         return false;
      } else {
         long var2 = var1.getTimeStamp() - this.bsm.lastSystemTime;
         if (var2 < 0L) {
            return false;
         } else {
            BasicSourceModule var4 = this.bsm;
            var4.currentSystemTime = var4.originSystemTime + var2;
            var1.setTimeStamp(this.bsm.currentSystemTime);
            var1.setFlags(var1.getFlags() & -129 | 96);
            return true;
         }
      }
   }

   protected boolean process() {
      this.readBlocked = false;
      Buffer var4 = this.field_27.getEmptyBuffer();
      var4.setOffset(0);
      var4.setLength(0);
      var4.setFlags(0);
      long var1 = (long)(this.sequenceNum++);
      var4.setSequenceNumber(var1);
      Throwable var10000;
      boolean var10001;
      if (this.resetted) {
         label1209: {
            Object var5 = this.bsm.resetSync;
            synchronized(var5){}

            label1200: {
               label1199: {
                  try {
                     if (this.resetted) {
                        var4.setFlags(512);
                        this.resetted = false;
                        this.pause();
                        if (this.bsm.checkAllPaused()) {
                           this.bsm.parser.stop();
                           this.bsm.parser.reset();
                        }
                        break label1199;
                     }
                  } catch (Throwable var78) {
                     var10000 = var78;
                     var10001 = false;
                     break label1200;
                  }

                  try {
                     break label1209;
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break label1200;
                  }
               }

               label1187:
               try {
                  this.field_27.writeReport();
                  return true;
               } catch (Throwable var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label1187;
               }
            }

            while(true) {
               Throwable var80 = var10000;

               try {
                  throw var80;
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      label1184:
      try {
         this.field_27.track.readFrame(var4);
      } catch (Throwable var75) {
         Log.dumpStack(var75);
         if (this.bsm.moduleListener != null) {
            this.bsm.moduleListener.internalErrorOccurred(this.bsm);
         }
         break label1184;
      }

      if (PlaybackEngine.TRACE_ON && !this.bsm.verifyBuffer(var4)) {
         PrintStream var81 = System.err;
         StringBuilder var6 = new StringBuilder();
         var6.append("verify buffer failed: ");
         var6.append(this.field_27.track);
         var81.println(var6.toString());
         Thread.dumpStack();
         if (this.bsm.moduleListener != null) {
            this.bsm.moduleListener.internalErrorOccurred(this.bsm);
         }
      }

      if (var4.getTimeStamp() != -1L && (var4.getFlags() & remapTimeFlag) != 0) {
         boolean var3 = true;
         if ((var4.getFlags() & 128) != 0) {
            var3 = this.remapSystemTime(var4);
         } else if ((var4.getFlags() & 256) != 0) {
            var3 = this.remapRelativeTime(var4);
         } else if ((var4.getFlags() & 4096) != 0) {
            var3 = this.remapRTPTime(var4);
         }

         if (!var3) {
            var4.setDiscard(true);
            this.field_27.writeReport();
            return true;
         }
      }

      if (this.checkLatency) {
         var4.setFlags(var4.getFlags() | 1024);
         if (this.bsm.moduleListener != null) {
            this.bsm.moduleListener.markedDataArrived(this.bsm, var4);
         }

         this.checkLatency = false;
      } else {
         var4.setFlags(var4.getFlags() & -1025);
      }

      if (this.readBlocked && this.bsm.moduleListener != null) {
         this.bsm.moduleListener.dataBlocked(this.bsm, false);
      }

      if (var4.isEOM()) {
         label1208: {
            Object var79 = this.bsm.resetSync;
            synchronized(var79){}

            label1170: {
               try {
                  if (!this.resetted) {
                     this.pause();
                     if (this.bsm.checkAllPaused()) {
                        this.bsm.parser.stop();
                     }
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label1170;
               }

               label1167:
               try {
                  break label1208;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label1167;
               }
            }

            while(true) {
               Throwable var83 = var10000;

               try {
                  throw var83;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  continue;
               }
            }
         }
      } else {
         BasicSourceModule var82 = this.bsm;
         var82.bitsRead += (long)var4.getLength();
      }

      this.field_27.writeReport();
      return true;
   }

   public void readHasBlocked(Track var1) {
      this.readBlocked = true;
      if (this.bsm.moduleListener != null) {
         this.bsm.moduleListener.dataBlocked(this.bsm, true);
      }

   }

   public void start() {
      synchronized(this){}

      try {
         super.start();
         this.lastRelativeTime = -1L;
      } finally {
         ;
      }

   }
}
