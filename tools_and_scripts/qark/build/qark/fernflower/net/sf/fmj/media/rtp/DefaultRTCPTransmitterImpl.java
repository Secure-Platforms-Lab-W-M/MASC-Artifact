package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.media.rtp.rtcp.SourceDescription;
import net.sf.fmj.media.rtp.util.UDPPacketSender;

public class DefaultRTCPTransmitterImpl implements RTCPTransmitter {
   SSRCCache cache;
   int sdescounter;
   RTCPRawSender sender;
   SSRCInfo ssrcInfo;
   OverallStats stats;

   public DefaultRTCPTransmitterImpl(SSRCCache var1) {
      this.stats = null;
      this.sdescounter = 0;
      this.ssrcInfo = null;
      this.cache = var1;
      this.stats = var1.field_173.defaultstats;
   }

   public DefaultRTCPTransmitterImpl(SSRCCache var1, int var2, String var3) throws UnknownHostException, IOException {
      this(var1, new RTCPRawSender(var2, var3));
   }

   public DefaultRTCPTransmitterImpl(SSRCCache var1, int var2, String var3, UDPPacketSender var4) throws UnknownHostException, IOException {
      this(var1, new RTCPRawSender(var2, var3, var4));
   }

   public DefaultRTCPTransmitterImpl(SSRCCache var1, RTCPRawSender var2) {
      this(var1);
      this.setSender(var2);
      this.stats = var1.field_173.defaultstats;
   }

   public void bye(int var1, byte[] var2) {
      if (this.cache.rtcpsent) {
         this.cache.byestate = true;
         List var7 = this.makereports();
         RTCPPacket[] var10 = (RTCPPacket[])var7.toArray(new RTCPPacket[var7.size() + 1]);
         RTCPBYEPacket var9 = new RTCPBYEPacket(new int[]{var1}, var2);
         var10[var10.length - 1] = var9;
         RTCPCompoundPacket var11 = new RTCPCompoundPacket(var10);
         double var3;
         if (this.cache.aliveCount() > 50) {
            this.cache.reset(var9.length);
            var3 = this.cache.calcReportInterval(this.ssrcInfo.sender, false);
         } else {
            var3 = 0.0D;
         }

         long var5 = (long)var3;

         try {
            Thread.sleep(var5);
         } catch (InterruptedException var8) {
            var8.printStackTrace();
         }

         this.transmit(var11);
         this.sdescounter = 0;
      }
   }

   public void bye(String var1) {
      SSRCInfo var2 = this.ssrcInfo;
      if (var2 != null) {
         if (var1 != null) {
            this.bye(var2.ssrc, var1.getBytes());
         } else {
            this.bye(var2.ssrc, (byte[])null);
         }

         this.ssrcInfo.setOurs(false);
         this.ssrcInfo = null;
      }
   }

   public void close() {
      RTCPRawSender var1 = this.sender;
      if (var1 != null) {
         var1.closeConsumer();
      }

   }

   public SSRCCache getCache() {
      return this.cache;
   }

   public SSRCInfo getSSRCInfo() {
      return this.ssrcInfo;
   }

   public RTCPRawSender getSender() {
      return this.sender;
   }

   protected RTCPReportBlock[] makerecreports(long var1) {
      ArrayList var3 = new ArrayList();
      Enumeration var4 = this.cache.cache.elements();

      while(var4.hasMoreElements()) {
         SSRCInfo var5 = (SSRCInfo)var4.nextElement();
         if (!var5.ours && var5.sender) {
            var3.add(var5.makeReceiverReport(var1));
         }
      }

      return (RTCPReportBlock[])var3.toArray(new RTCPReportBlock[var3.size()]);
   }

   protected List makereports() {
      ArrayList var9 = new ArrayList();
      SSRCInfo var10 = this.ssrcInfo;
      boolean var2 = var10.sender;
      RTCPReportBlock[] var8 = this.makerecreports(System.currentTimeMillis());
      RTCPReportBlock[] var7 = var8;
      if (var8.length > 31) {
         var7 = new RTCPReportBlock[31];
         System.arraycopy(var8, 0, var7, 0, 31);
      }

      if (var2) {
         RTCPSRPacket var11 = new RTCPSRPacket(var10.ssrc, var7);
         var9.add(var11);
         long var3;
         if (var10.systime == 0L) {
            var3 = System.currentTimeMillis();
         } else {
            var3 = var10.systime;
         }

         long var5 = var3 / 1000L;
         var11.ntptimestamplsw = (long)((int)((double)(var3 - 1000L * var5) / 1000.0D * 4.294967296E9D));
         var11.ntptimestampmsw = var5;
         var11.rtptimestamp = (long)((int)var10.rtptime);
         var11.packetcount = (long)(var10.maxseq - var10.baseseq);
         var11.octetcount = (long)var10.bytesreceived;
      } else {
         var9.add(new RTCPRRPacket(var10.ssrc, var7));
      }

      if (var7 != var8) {
         for(int var1 = 31; var1 < var8.length; var1 += 31) {
            if (var8.length - var1 < 31) {
               var7 = new RTCPReportBlock[var8.length - var1];
            }

            System.arraycopy(var8, var1, var7, 0, var7.length);
            var9.add(new RTCPRRPacket(var10.ssrc, var7));
         }
      }

      RTCPSDESPacket var13 = new RTCPSDESPacket(new RTCPSDES[1]);
      var13.sdes[0] = new RTCPSDES();
      var13.sdes[0].ssrc = this.ssrcInfo.ssrc;
      ArrayList var12 = new ArrayList();
      var12.add(new RTCPSDESItem(1, var10.sourceInfo.getCNAME()));
      if (this.sdescounter % 3 == 0) {
         SourceDescription var16 = var10.name;
         String var17;
         if (var16 != null) {
            var17 = var16.getDescription();
            if (var17 != null) {
               var12.add(new RTCPSDESItem(2, var17));
            }
         }

         var16 = var10.email;
         if (var16 != null) {
            var17 = var16.getDescription();
            if (var17 != null) {
               var12.add(new RTCPSDESItem(3, var17));
            }
         }

         var16 = var10.phone;
         if (var16 != null) {
            var17 = var16.getDescription();
            if (var17 != null) {
               var12.add(new RTCPSDESItem(4, var17));
            }
         }

         var16 = var10.loc;
         if (var16 != null) {
            var17 = var16.getDescription();
            if (var17 != null) {
               var12.add(new RTCPSDESItem(5, var17));
            }
         }

         var16 = var10.tool;
         if (var16 != null) {
            var17 = var16.getDescription();
            if (var17 != null) {
               var12.add(new RTCPSDESItem(6, var17));
            }
         }

         SourceDescription var14 = var10.note;
         if (var14 != null) {
            String var15 = var14.getDescription();
            if (var15 != null) {
               var12.add(new RTCPSDESItem(7, var15));
            }
         }
      }

      ++this.sdescounter;
      var13.sdes[0].items = (RTCPSDESItem[])var12.toArray(new RTCPSDESItem[var12.size()]);
      var9.add(var13);
      return var9;
   }

   public void report() {
      List var1 = this.makereports();
      this.transmit(new RTCPCompoundPacket((RTCPPacket[])var1.toArray(new RTCPPacket[var1.size()])));
   }

   public void setSSRCInfo(SSRCInfo var1) {
      this.ssrcInfo = var1;
   }

   public void setSender(RTCPRawSender var1) {
      this.sender = var1;
   }

   protected void transmit(RTCPCompoundPacket var1) {
      try {
         this.sender.sendTo(var1);
         if (this.ssrcInfo instanceof SendSSRCInfo) {
            RTPTransStats var2 = ((SendSSRCInfo)this.ssrcInfo).stats;
            ++var2.total_rtcp;
            OverallTransStats var5 = this.cache.field_173.transstats;
            ++var5.rtcp_sent;
         }

         this.cache.updateavgrtcpsize(var1.length);
         if (this.cache.initial) {
            this.cache.initial = false;
         }

         if (!this.cache.rtcpsent) {
            this.cache.rtcpsent = true;
         }

      } catch (IOException var3) {
         this.stats.update(6, 1);
         OverallTransStats var4 = this.cache.field_173.transstats;
         ++var4.transmit_failed;
      }
   }
}
