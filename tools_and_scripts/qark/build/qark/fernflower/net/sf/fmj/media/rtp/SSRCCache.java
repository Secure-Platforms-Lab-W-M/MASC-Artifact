package net.sf.fmj.media.rtp;

import java.net.InetAddress;
import java.util.Enumeration;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.rtp.util.SSRCTable;

public class SSRCCache {
   static final int BYE_THRESHOLD = 50;
   static final int CONTROL = 2;
   static final int DATA = 1;
   private static final int NOTIFYPERIOD = 500;
   static final int RTCP_MIN_TIME = 5000;
   static final int SRCDATA = 3;
   private int avgrtcpsize = 128;
   public boolean byestate = false;
   public final SSRCTable cache = new SSRCTable();
   int[] clockrate = new int[128];
   RTPEventHandler eventhandler;
   public boolean initial = true;
   public SSRCInfo ourssrc;
   double rtcp_bw_fraction = 0.0D;
   private int rtcp_min_time = 5000;
   double rtcp_sender_bw_fraction = 0.0D;
   public boolean rtcpsent = false;
   int sendercount;
   int sessionbandwidth = 0;
   // $FF: renamed from: sm net.sf.fmj.media.rtp.RTPSessionMgr
   public final RTPSessionMgr field_173;
   RTPSourceInfoCache sourceInfoCache;
   private OverallStats stats;
   private OverallTransStats transstats;

   SSRCCache(RTPSessionMgr var1) {
      this.stats = var1.defaultstats;
      this.transstats = var1.transstats;
      RTPSourceInfoCache var2 = new RTPSourceInfoCache();
      this.sourceInfoCache = var2;
      var2.setMainCache(var2);
      this.sourceInfoCache.setSSRCCache(this);
      this.field_173 = var1;
      this.eventhandler = new RTPEventHandler(var1);
      this.setclockrates();
   }

   SSRCCache(RTPSessionMgr var1, RTPSourceInfoCache var2) {
      this.stats = var1.defaultstats;
      this.transstats = var1.transstats;
      this.sourceInfoCache = var2;
      var2.setSSRCCache(this);
      this.field_173 = var1;
      this.eventhandler = new RTPEventHandler(var1);
   }

   private void changessrc(SSRCInfo var1) {
      var1.setOurs(true);
      SSRCInfo var2 = this.ourssrc;
      if (var2 != null) {
         var1.sourceInfo = this.sourceInfoCache.get(var2.sourceInfo.getCNAME(), var1.ours);
         var1.sourceInfo.addSSRC(var1);
      }

      var1.reporter.releasessrc("Local Collision Detected");
      this.ourssrc = var1;
      var1.reporter.restart = true;
   }

   private void localCollision(int var1) {
      do {
         var1 = (int)this.field_173.generateSSRC(GenerateSSRCCause.LOCAL_COLLISION);
      } while(this.lookup(var1) != null);

      PassiveSSRCInfo var2 = new PassiveSSRCInfo(this.ourssrc);
      var2.ssrc = var1;
      this.cache.put(var1, var2);
      this.changessrc(var2);
      this.ourssrc = var2;
      this.stats.update(3, 1);
      OverallTransStats var3 = this.transstats;
      ++var3.local_coll;
   }

   public int aliveCount() {
      int var1 = 0;
      SSRCTable var3 = this.cache;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label294: {
         Enumeration var4;
         try {
            var4 = this.cache.elements();
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label294;
         }

         while(true) {
            try {
               if (!var4.hasMoreElements()) {
                  break;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label294;
            }

            int var2 = var1;

            label281: {
               try {
                  if (!((SSRCInfo)var4.nextElement()).alive) {
                     break label281;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label294;
               }

               var2 = var1 + 1;
            }

            var1 = var2;
         }

         label274:
         try {
            return var1;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label274;
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

   public double calcReportInterval(boolean var1, boolean var2) {
      this.rtcp_min_time = 5000;
      double var5 = this.rtcp_bw_fraction;
      if (this.initial) {
         this.rtcp_min_time = 5000 / 2;
      }

      int var14 = this.aliveCount();
      int var15 = this.sendercount;
      int var13 = var14;
      double var3 = var5;
      if (var15 > 0) {
         double var7 = (double)var15;
         double var9 = (double)var14;
         double var11 = this.rtcp_sender_bw_fraction;
         var13 = var14;
         var3 = var5;
         if (var7 < var9 * var11) {
            if (var1) {
               var3 = var5 * var11;
               var13 = this.sendercount;
            } else {
               var3 = var5 * (1.0D - var11);
               var13 = var14 - var15;
            }
         }
      }

      var14 = var13;
      var5 = var3;
      if (var2) {
         var14 = var13;
         var5 = var3;
         if (var3 == 0.0D) {
            var3 = 0.05D;
            var15 = this.sendercount;
            var14 = var13;
            var5 = var3;
            if (var15 > 0) {
               var14 = var13;
               var5 = var3;
               if ((double)var15 < (double)var13 * 0.25D) {
                  if (var1) {
                     var5 = 0.05D * 0.25D;
                     var14 = this.sendercount;
                  } else {
                     var5 = 0.05D * 0.75D;
                     var14 = var13 - var15;
                  }
               }
            }
         }
      }

      var3 = 0.0D;
      if (var5 != 0.0D) {
         var5 = (double)(this.avgrtcpsize * var14) / var5;
         var13 = this.rtcp_min_time;
         var3 = var5;
         if (var5 < (double)var13) {
            var3 = (double)var13;
         }
      }

      return var2 ? var3 : (Math.random() + 0.5D) * var3;
   }

   void destroy() {
      synchronized(this){}

      try {
         this.cache.removeAll();
         if (this.eventhandler != null) {
            this.eventhandler.close();
         }
      } finally {
         ;
      }

   }

   SSRCInfo get(int param1, InetAddress param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   SSRCInfo get(int param1, InetAddress param2, int param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   SSRCTable getMainCache() {
      return this.cache;
   }

   RTPSourceInfoCache getRTPSICache() {
      return this.sourceInfoCache;
   }

   int getSessionBandwidth() {
      int var1 = this.sessionbandwidth;
      if (var1 != 0) {
         return var1;
      } else {
         throw new IllegalArgumentException("Session Bandwidth not set");
      }
   }

   SSRCInfo lookup(int var1) {
      return (SSRCInfo)this.cache.get(var1);
   }

   void remove(int var1) {
      SSRCInfo var2 = (SSRCInfo)this.cache.remove(var1);
      if (var2 != null) {
         var2.delete();
      }

   }

   public void reset(int var1) {
      this.initial = true;
      this.sendercount = 0;
      this.avgrtcpsize = var1;
   }

   void setclockrates() {
      int var1;
      for(var1 = 0; var1 < 16; ++var1) {
         this.clockrate[var1] = 8000;
      }

      int[] var2 = this.clockrate;
      var2[6] = 16000;
      var2[10] = 44100;
      var2[11] = 44100;
      var2[14] = 90000;
      var2[16] = 11025;
      var2[17] = 22050;
      var2[18] = 44100;

      for(var1 = 24; var1 < 34; ++var1) {
         this.clockrate[var1] = 90000;
      }

      for(var1 = 96; var1 < 128; ++var1) {
         Format var3 = this.field_173.formatinfo.get(var1);
         if (var3 != null && var3 instanceof AudioFormat) {
            this.clockrate[var1] = (int)((AudioFormat)var3).getSampleRate();
         } else {
            this.clockrate[var1] = 90000;
         }
      }

   }

   public void updateavgrtcpsize(int var1) {
      synchronized(this){}
      double var2 = (double)var1;

      try {
         this.avgrtcpsize = (int)(var2 * 0.0625D + (double)this.avgrtcpsize * 0.9375D);
      } finally {
         ;
      }

   }
}
