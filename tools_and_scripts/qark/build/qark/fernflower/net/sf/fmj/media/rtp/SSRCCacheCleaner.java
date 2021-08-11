package net.sf.fmj.media.rtp;

import javax.media.rtp.ReceiveStream;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.InactiveReceiveStreamEvent;
import javax.media.rtp.event.TimeoutEvent;
import net.sf.fmj.media.rtp.util.RTPMediaThread;
import net.sf.fmj.media.rtp.util.SSRCTable;

public class SSRCCacheCleaner implements Runnable {
   private static final long RUN_INTERVAL = 5000L;
   private static final int TIMEOUT_MULTIPLIER = 5;
   private final SSRCCache cache;
   private boolean killed;
   private long lastCleaned;
   private int[] ssrcs;
   private final StreamSynch streamSynch;
   private final RTPMediaThread thread;

   public SSRCCacheCleaner(SSRCCache var1, StreamSynch var2) {
      this.cache = var1;
      this.streamSynch = var2;
      this.killed = false;
      this.lastCleaned = -1L;
      RTPMediaThread var3 = new RTPMediaThread(this, "SSRC Cache Cleaner");
      this.thread = var3;
      var3.useControlPriority();
      this.thread.setDaemon(true);
      this.thread.start();
   }

   private long cleannow(long var1) {
      SSRCInfo var13 = this.cache.ourssrc;
      long var8 = Long.MAX_VALUE;
      if (var13 == null) {
         return Long.MAX_VALUE;
      } else {
         double var3 = this.cache.calcReportInterval(var13.sender, true);
         SSRCTable var14 = this.cache.cache;
         int[] var16 = var14.keysToArray(this.ssrcs);
         this.ssrcs = var16;
         int var6 = var16.length;

         long var10;
         for(int var5 = 0; var5 < var6; var8 = var10) {
            int var7 = var16[var5];
            if (var7 == 0) {
               var10 = var8;
            } else {
               SSRCInfo var17 = (SSRCInfo)var14.get(var7);
               if (var17 != null) {
                  if (var17.ours) {
                     var10 = var8;
                  } else {
                     boolean var12;
                     ReceiveStream var15;
                     RTPSourceInfo var18;
                     RTPSessionMgr var19;
                     if (var17.byeReceived) {
                        var10 = 1000L - var1 + var17.byeTime;
                        if (var10 > 0L) {
                           if (var10 >= var8) {
                              var10 = var8;
                           }
                        } else {
                           label117: {
                              var17.byeTime = 0L;
                              var17.byeReceived = false;
                              this.cache.remove(var17.ssrc);
                              this.streamSynch.remove(var17.ssrc);
                              var18 = var17.sourceInfo;
                              if (var17 instanceof RecvSSRCInfo) {
                                 var15 = (ReceiveStream)var17;
                              } else {
                                 if (!(var17 instanceof PassiveSSRCInfo)) {
                                    var10 = var8;
                                    break label117;
                                 }

                                 var15 = null;
                              }

                              var19 = this.cache.field_173;
                              String var21 = var17.byereason;
                              if (var18 != null && var18.getStreamCount() == 0) {
                                 var12 = true;
                              } else {
                                 var12 = false;
                              }

                              ByeEvent var23 = new ByeEvent(var19, var18, var15, var21, var12);
                              this.cache.eventhandler.postEvent(var23);
                              var10 = var8;
                           }
                        }
                     } else if ((double)var17.lastHeardFrom + var3 > (double)var1) {
                        var10 = var8;
                     } else if (!var17.inactivesent) {
                        label119: {
                           var18 = var17.sourceInfo;
                           if (var17 instanceof ReceiveStream) {
                              var15 = (ReceiveStream)var17;
                           } else {
                              if ((double)var17.lastHeardFrom + 5.0D * var3 > (double)var1) {
                                 var10 = var8;
                                 break label119;
                              }

                              var15 = null;
                           }

                           var19 = this.cache.field_173;
                           if (var18 != null && var18.getStreamCount() == 1) {
                              var12 = true;
                           } else {
                              var12 = false;
                           }

                           InactiveReceiveStreamEvent var22 = new InactiveReceiveStreamEvent(var19, var18, var15, var12);
                           this.cache.eventhandler.postEvent(var22);
                           var17.quiet = true;
                           var17.inactivesent = true;
                           var17.setAlive(false);
                           var10 = var8;
                        }
                     } else {
                        var12 = true;
                        var10 = var8;
                        if (var17.lastHeardFrom + 5000L <= var1) {
                           this.cache.remove(var17.ssrc);
                           var18 = var17.sourceInfo;
                           var19 = this.cache.field_173;
                           if (var17 instanceof ReceiveStream) {
                              var15 = (ReceiveStream)var17;
                           } else {
                              var15 = null;
                           }

                           if (var18 == null || var18.getStreamCount() != 0) {
                              var12 = false;
                           }

                           TimeoutEvent var20 = new TimeoutEvent(var19, var18, var15, var12);
                           this.cache.eventhandler.postEvent(var20);
                           var10 = var8;
                        }
                     }
                  }
               } else {
                  var10 = var8;
               }
            }

            ++var5;
         }

         return var8;
      }
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void setClean() {
      synchronized(this){}

      try {
         this.lastCleaned = -1L;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void stop() {
      synchronized(this){}

      try {
         this.killed = true;
         this.notifyAll();
      } finally {
         ;
      }

   }
}
