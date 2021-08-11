package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.event.ActiveReceiveStreamEvent;
import javax.media.rtp.event.ApplicationEvent;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewParticipantEvent;
import javax.media.rtp.event.ReceiverReportEvent;
import javax.media.rtp.event.SenderReportEvent;
import javax.media.rtp.event.StreamMappedEvent;
import javax.media.rtp.rtcp.ReceiverReport;
import javax.media.rtp.rtcp.SenderReport;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketConsumer;
import net.sf.fmj.media.rtp.util.PacketForwarder;
import net.sf.fmj.media.rtp.util.PacketSource;
import net.sf.fmj.media.rtp.util.UDPPacket;

public class RTCPReceiver implements PacketConsumer {
   SSRCCache cache;
   private boolean rtcpstarted;
   private int type;

   public RTCPReceiver(SSRCCache var1) {
      this.rtcpstarted = false;
      this.type = 0;
      this.cache = var1;
      var1.lookup(var1.ourssrc.ssrc);
   }

   public RTCPReceiver(SSRCCache var1, int var2, String var3, StreamSynch var4) throws UnknownHostException, IOException {
      this(var1, new RTCPRawReceiver(var2 | 1, var3, var1.field_173.defaultstats, var4));
   }

   public RTCPReceiver(SSRCCache var1, DatagramSocket var2, StreamSynch var3) {
      this(var1, new RTCPRawReceiver(var2, var1.field_173.defaultstats, var3));
   }

   public RTCPReceiver(SSRCCache var1, PacketSource var2) {
      this(var1);
      (new PacketForwarder(var2, this)).startPF();
   }

   public void closeConsumer() {
   }

   public String consumerString() {
      return "RTCP Packet Receiver/Collector";
   }

   public void sendTo(RTCPPacket var1) {
      RTPSessionMgr var9 = null;
      if (this.cache.field_173.isUnicast()) {
         InetAddress var6 = ((UDPPacket)var1.base).remoteAddress;
         if (!this.rtcpstarted) {
            this.cache.field_173.startRTCPReports(var6);
            this.rtcpstarted = true;
            if ((this.cache.field_173.controladdress.getAddress()[3] & 255 & 255) == 255) {
               this.cache.field_173.addUnicastAddr(this.cache.field_173.controladdress);
            } else {
               try {
                  var6 = InetAddress.getLocalHost();
               } catch (UnknownHostException var11) {
                  var6 = null;
               }

               if (var6 != null) {
                  this.cache.field_173.addUnicastAddr(var6);
               }
            }
         } else if (!this.cache.field_173.isSenderDefaultAddr(var6)) {
            this.cache.field_173.addUnicastAddr(var6);
         }
      }

      int var2 = var1.type;
      if (var2 != -1) {
         RTCPAPPPacket var8 = null;
         ReceiveStream var21 = null;
         RTCPBYEPacket var7 = null;
         int var3;
         RTPSourceInfo var10;
         SSRCInfo var13;
         ActiveReceiveStreamEvent var26;
         SSRCInfo var31;
         RTCPReportBlock[] var33;
         NewParticipantEvent var34;
         switch(var2) {
         case 200:
            RTCPSRPacket var30 = (RTCPSRPacket)var1;
            this.type = 1;
            if (var1.base instanceof UDPPacket) {
               var13 = this.cache.get(var30.ssrc, ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort, this.type);
            } else {
               var13 = this.cache.get(var30.ssrc, (InetAddress)null, 0, this.type);
            }

            if (var13 == null) {
               return;
            } else {
               var13.setAlive(true);
               var13.lastSRntptimestamp = (var30.ntptimestampmsw << 32) + var30.ntptimestamplsw;
               var13.lastSRrtptimestamp = var30.rtptimestamp;
               var13.lastSRreceiptTime = var30.receiptTime;
               var13.lastRTCPreceiptTime = var30.receiptTime;
               var13.lastHeardFrom = var30.receiptTime;
               if (var13.quiet) {
                  var13.quiet = false;
                  RTPSessionMgr var28 = this.cache.field_173;
                  RTPSourceInfo var32 = var13.sourceInfo;
                  if (var13 instanceof ReceiveStream) {
                     var21 = (ReceiveStream)var13;
                  }

                  var26 = new ActiveReceiveStreamEvent(var28, var32, var21);
                  this.cache.eventhandler.postEvent(var26);
               }

               var13.lastSRpacketcount = var30.packetcount;
               var13.lastSRoctetcount = var30.octetcount;

               for(var2 = 0; var2 < var30.reports.length; ++var2) {
                  var30.reports[var2].receiptTime = var30.receiptTime;
                  var3 = var30.reports[var2].ssrc;
                  var33 = (RTCPReportBlock[])var13.reports.get(var3);
                  if (var33 == null) {
                     var33 = new RTCPReportBlock[]{var30.reports[var2], null};
                     var13.reports.put(var3, var33);
                  } else {
                     var33[1] = var33[0];
                     var33[0] = var30.reports[var2];
                  }
               }

               if (var13.probation > 0) {
                  return;
               }

               if (!var13.newpartsent && var13.sourceInfo != null) {
                  var34 = new NewParticipantEvent(this.cache.field_173, var13.sourceInfo);
                  this.cache.eventhandler.postEvent(var34);
                  var13.newpartsent = true;
               }

               if (!var13.recvstrmap && var13.sourceInfo != null) {
                  var13.recvstrmap = true;
                  StreamMappedEvent var35 = new StreamMappedEvent(this.cache.field_173, (ReceiveStream)var13, var13.sourceInfo);
                  this.cache.eventhandler.postEvent(var35);
               }

               SenderReportEvent var29 = new SenderReportEvent(this.cache.field_173, (SenderReport)var13);
               this.cache.eventhandler.postEvent(var29);
               return;
            }
         case 201:
            RTCPRRPacket var27 = (RTCPRRPacket)var1;
            this.type = 2;
            if (var1.base instanceof UDPPacket) {
               var13 = this.cache.get(var27.ssrc, ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort, this.type);
            } else {
               var13 = this.cache.get(var27.ssrc, (InetAddress)null, 0, this.type);
            }

            if (var13 == null) {
               return;
            }

            var13.setAlive(true);
            var13.lastRTCPreceiptTime = var27.receiptTime;
            var13.lastHeardFrom = var27.receiptTime;
            if (var13.quiet) {
               var13.quiet = false;
               if (var13 instanceof ReceiveStream) {
                  var26 = new ActiveReceiveStreamEvent(this.cache.field_173, var13.sourceInfo, (ReceiveStream)var13);
               } else {
                  var26 = new ActiveReceiveStreamEvent(this.cache.field_173, var13.sourceInfo, (ReceiveStream)null);
               }

               this.cache.eventhandler.postEvent(var26);
            }

            for(var2 = 0; var2 < var27.reports.length; ++var2) {
               var27.reports[var2].receiptTime = var27.receiptTime;
               var3 = var27.reports[var2].ssrc;
               var33 = (RTCPReportBlock[])var13.reports.get(var3);
               if (var33 == null) {
                  var33 = new RTCPReportBlock[]{var27.reports[var2], null};
                  var13.reports.put(var3, var33);
               } else {
                  var33[1] = var33[0];
                  var33[0] = var27.reports[var2];
               }
            }

            if (!var13.newpartsent && var13.sourceInfo != null) {
               var34 = new NewParticipantEvent(this.cache.field_173, var13.sourceInfo);
               this.cache.eventhandler.postEvent(var34);
               var13.newpartsent = true;
            }

            ReceiverReportEvent var23 = new ReceiverReportEvent(this.cache.field_173, (ReceiverReport)var13);
            this.cache.eventhandler.postEvent(var23);
            return;
         case 202:
            RTCPSDESPacket var25 = (RTCPSDESPacket)var1;
            var2 = 0;
            var31 = var9;

            SSRCInfo var22;
            while(true) {
               var22 = var31;
               if (var2 >= var25.sdes.length) {
                  break;
               }

               RTCPSDES var24 = var25.sdes[var2];
               var3 = this.type;
               if (var3 == 1 || var3 == 2) {
                  if (var1.base instanceof UDPPacket) {
                     var31 = this.cache.get(var24.ssrc, ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort, this.type);
                  } else {
                     var31 = this.cache.get(var24.ssrc, (InetAddress)null, 0, this.type);
                  }
               }

               if (var31 == null) {
                  var22 = var31;
                  break;
               }

               var31.setAlive(true);
               var31.lastHeardFrom = var25.receiptTime;
               var31.addSDESInfo(var24);
               ++var2;
            }

            if (var22 != null && !var22.newpartsent && var22.sourceInfo != null) {
               NewParticipantEvent var19 = new NewParticipantEvent(this.cache.field_173, var22.sourceInfo);
               this.cache.eventhandler.postEvent(var19);
               var22.newpartsent = true;
            }

            if (var22 != null && !var22.recvstrmap && var22.sourceInfo != null && var22 instanceof RecvSSRCInfo) {
               var22.recvstrmap = true;
               StreamMappedEvent var20 = new StreamMappedEvent(this.cache.field_173, (ReceiveStream)var22, var22.sourceInfo);
               this.cache.eventhandler.postEvent(var20);
            }

            this.type = 0;
            return;
         case 203:
            var7 = (RTCPBYEPacket)var1;
            if (var1.base instanceof UDPPacket) {
               var31 = this.cache.get(var7.ssrc[0], ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort);
            } else {
               var31 = this.cache.get(var7.ssrc[0], (InetAddress)null, 0);
            }

            for(var2 = 0; var2 < var7.ssrc.length; ++var2) {
               if (var1.base instanceof UDPPacket) {
                  var31 = this.cache.get(var7.ssrc[var2], ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort);
               } else {
                  var31 = this.cache.get(var7.ssrc[var2], (InetAddress)null, 0);
               }

               if (var31 == null) {
                  break;
               }

               if (!this.cache.byestate) {
                  var31.setAlive(false);
                  var31.byeReceived = true;
                  var31.byeTime = var1.receiptTime;
                  var31.lastHeardFrom = var7.receiptTime;
               }
            }

            if (var31 == null) {
               return;
            } else {
               if (var31.quiet) {
                  var31.quiet = false;
                  var9 = this.cache.field_173;
                  var10 = var31.sourceInfo;
                  ReceiveStream var15 = var8;
                  if (var31 instanceof ReceiveStream) {
                     var15 = (ReceiveStream)var31;
                  }

                  ActiveReceiveStreamEvent var16 = new ActiveReceiveStreamEvent(var9, var10, var15);
                  this.cache.eventhandler.postEvent(var16);
               }

               var31.byereason = new String(var7.reason);
               if (var31.byeReceived) {
                  return;
               }

               boolean var5 = false;
               RTPSourceInfo var17 = var31.sourceInfo;
               boolean var4 = var5;
               if (var17 != null) {
                  var4 = var5;
                  if (var17.getStreamCount() == 0) {
                     var4 = true;
                  }
               }

               ByeEvent var18 = null;
               if (var31 instanceof RecvSSRCInfo) {
                  var18 = new ByeEvent(this.cache.field_173, var31.sourceInfo, (ReceiveStream)var31, new String(var7.reason), var4);
               }

               if (var31 instanceof PassiveSSRCInfo) {
                  var18 = new ByeEvent(this.cache.field_173, var31.sourceInfo, (ReceiveStream)null, new String(var7.reason), var4);
               }

               this.cache.eventhandler.postEvent(var18);
               this.cache.remove(var31.ssrc);
               return;
            }
         case 204:
            var8 = (RTCPAPPPacket)var1;
            if (var1.base instanceof UDPPacket) {
               var13 = this.cache.get(var8.ssrc, ((UDPPacket)var1.base).remoteAddress, ((UDPPacket)var1.base).remotePort);
            } else {
               var13 = this.cache.get(var8.ssrc, (InetAddress)null, 0);
            }

            if (var13 == null) {
               return;
            }

            var13.lastHeardFrom = var8.receiptTime;
            if (var13.quiet) {
               var13.quiet = false;
               var9 = this.cache.field_173;
               var10 = var13.sourceInfo;
               if (var13 instanceof ReceiveStream) {
                  var21 = (ReceiveStream)var13;
               } else {
                  var21 = null;
               }

               var26 = new ActiveReceiveStreamEvent(var9, var10, var21);
               this.cache.eventhandler.postEvent(var26);
            }

            var9 = this.cache.field_173;
            var10 = var13.sourceInfo;
            var21 = var7;
            if (var13 instanceof ReceiveStream) {
               var21 = (ReceiveStream)var13;
            }

            ApplicationEvent var14 = new ApplicationEvent(var9, var10, var21, var8.subtype, (String)null, var8.data);
            this.cache.eventhandler.postEvent(var14);
            return;
         default:
         }
      } else {
         RTCPCompoundPacket var12 = (RTCPCompoundPacket)var1;
         this.cache.updateavgrtcpsize(var12.length);

         for(var2 = 0; var2 < var12.packets.length; ++var2) {
            this.sendTo(var12.packets[var2]);
         }

         if (this.cache.field_173.cleaner != null) {
            this.cache.field_173.cleaner.setClean();
         }

      }
   }

   public void sendTo(Packet var1) {
      this.sendTo((RTCPPacket)var1);
   }
}
