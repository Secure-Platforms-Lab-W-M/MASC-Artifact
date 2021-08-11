package net.sf.fmj.media.multiplexer;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Time;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.BasicClock;

public class RawSyncBufferMux extends RawBufferMux {
   static int LEEWAY = 5;
   static int THRESHOLD = 80;
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   static VideoFormat mpegVideo = new VideoFormat("mpeg/rtp");
   private boolean masterTrackEnded = false;
   protected boolean monoIncrTime = false;
   private long monoStartTime = 0L;
   private long monoTime = 0L;
   boolean mpegBFrame = false;
   boolean mpegPFrame = false;
   private boolean resetted = false;
   private Object waitLock = new Object();

   public RawSyncBufferMux() {
      this.timeBase = new RawBufferMux.RawMuxTimeBase();
      this.allowDrop = true;
      this.clock = new BasicClock();

      try {
         this.clock.setTimeBase(this.timeBase);
      } catch (Exception var2) {
      }
   }

   private void waitForPT(long var1, int var3) {
      long var7 = var1 / 1000000L;
      if (this.masterTrackID != -1 && var3 != this.masterTrackID) {
         var1 = var7 - this.mediaTime[this.masterTrackID] / 1000000L;
      } else if (this.systemStartTime < 0L) {
         var1 = 0L;
      } else {
         var1 = var7 - this.mediaStartTime - (System.currentTimeMillis() - this.systemStartTime);
      }

      long var5 = var1;
      if (var1 <= 2000L) {
         while(true) {
            if (var5 > (long)LEEWAY && !this.masterTrackEnded) {
               int var4 = THRESHOLD;
               if (var5 > (long)var4) {
                  var5 = (long)var4;
               }

               Object var9 = this.waitLock;
               synchronized(var9){}

               Throwable var10000;
               boolean var10001;
               label602: {
                  label619: {
                     label600: {
                        try {
                           try {
                              this.waitLock.wait(var5);
                              break label600;
                           } catch (Exception var58) {
                           }
                        } catch (Throwable var59) {
                           var10000 = var59;
                           var10001 = false;
                           break label602;
                        }

                        try {
                           break label619;
                        } catch (Throwable var56) {
                           var10000 = var56;
                           var10001 = false;
                           break label602;
                        }
                     }

                     try {
                        if (this.resetted) {
                           this.resetted = false;
                           break label619;
                        }
                     } catch (Throwable var57) {
                        var10000 = var57;
                        var10001 = false;
                        break label602;
                     }

                     try {
                        ;
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label602;
                     }

                     if (this.masterTrackID != -1 && var3 != this.masterTrackID) {
                        var5 = var7 - this.mediaTime[this.masterTrackID] / 1000000L;
                        continue;
                     }

                     var5 = var7 - this.mediaStartTime - (System.currentTimeMillis() - this.systemStartTime);
                     continue;
                  }

                  label583:
                  try {
                     return;
                  } catch (Throwable var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label583;
                  }
               }

               while(true) {
                  Throwable var10 = var10000;

                  try {
                     throw var10;
                  } catch (Throwable var53) {
                     var10000 = var53;
                     var10001 = false;
                     continue;
                  }
               }
            }

            return;
         }
      }
   }

   public String getName() {
      return "Raw Sync Buffer Multiplexer";
   }

   public boolean initializeTracks(Format[] var1) {
      if (!super.initializeTracks(var1)) {
         return false;
      } else {
         this.masterTrackID = 0;

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof AudioFormat) {
               this.masterTrackID = var2;
            }
         }

         return true;
      }
   }

   public int process(Buffer var1, int var2) {
      if ((var1.getFlags() & 4096) != 0) {
         var1.setFlags(var1.getFlags() & -4097 | 256);
      }

      if (this.mc[var2] != null && this.mc[var2].isEnabled()) {
         this.mc[var2].process(var1);
      }

      if (this.streams != null && var1 != null) {
         if (var2 >= this.streams.length) {
            return 1;
         } else if (var1.isDiscard()) {
            return 0;
         } else {
            if ((var1.getFlags() & 64) == 0) {
               if (var1.getFormat() instanceof AudioFormat) {
                  if (mpegAudio.matches(var1.getFormat())) {
                     this.waitForPT(var1.getTimeStamp(), var2);
                  } else {
                     this.waitForPT(this.mediaTime[var2], var2);
                  }
               } else if (var1.getTimeStamp() >= 0L) {
                  if (mpegVideo.matches(var1.getFormat()) && (var1.getFlags() & 2048) != 0) {
                     int var3 = ((byte[])((byte[])var1.getData()))[var1.getOffset() + 2] & 7;
                     if (var3 > 2) {
                        this.mpegBFrame = true;
                     } else if (var3 == 2) {
                        this.mpegPFrame = true;
                     }

                     if (var3 > 2 || var3 == 2 && !this.mpegBFrame || var3 == 1 && !(this.mpegBFrame | this.mpegPFrame)) {
                        this.waitForPT(var1.getTimeStamp(), var2);
                     }
                  } else {
                     this.waitForPT(var1.getTimeStamp(), var2);
                  }
               }
            }

            this.updateTime(var1, var2);
            var1.setFlags(var1.getFlags() | 96);
            if ((!(var1.getFormat() instanceof AudioFormat) || mpegAudio.matches(var1.getFormat())) && this.monoIncrTime) {
               long var4 = this.monoStartTime + var1.getTimeStamp() - this.mediaStartTime * 1000000L;
               this.monoTime = var4;
               var1.setTimeStamp(var4);
            }

            if (var1.isEOM() && var2 == this.masterTrackID) {
               this.masterTrackEnded = true;
            }

            var1.setHeader(System.currentTimeMillis());
            return this.streams[var2].process(var1);
         }
      } else {
         return 1;
      }
   }

   public void reset() {
      // $FF: Couldn't be decompiled
   }

   public void setMediaTime(Time var1) {
      super.setMediaTime(var1);
      this.monoStartTime = this.monoTime + 10L;
   }

   public void syncStart(Time var1) {
      this.masterTrackEnded = false;
      super.syncStart(var1);
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
      } else if (var1.getTimeStamp() < 0L && this.systemStartTime >= 0L) {
         this.mediaTime[var2] = (this.mediaStartTime + System.currentTimeMillis() - this.systemStartTime) * 1000000L;
      } else {
         this.mediaTime[var2] = var1.getTimeStamp();
      }

      this.timeBase.update();
   }
}
