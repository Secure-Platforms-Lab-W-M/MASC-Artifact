package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.ActiveReceiveStreamEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.RemotePayloadChangeEvent;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.protocol.rtp.DataSource;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketConsumer;
import net.sf.fmj.media.rtp.util.PacketFilter;
import net.sf.fmj.media.rtp.util.PacketSource;
import net.sf.fmj.media.rtp.util.RTPPacket;
import net.sf.fmj.media.rtp.util.SSRCTable;
import net.sf.fmj.media.rtp.util.UDPPacket;

public class RTPReceiver extends PacketFilter {
   static final int MAX_DROPOUT = 3000;
   static final int MAX_MISORDER = 100;
   final SSRCCache cache;
   private final String content;
   private int errorPayload;
   private boolean initBC;
   int lastseqnum;
   final SSRCTable probationList;
   private boolean rtcpstarted;
   final RTPDemultiplexer rtpdemultiplexer;

   public RTPReceiver(SSRCCache var1, RTPDemultiplexer var2) {
      this.lastseqnum = -1;
      this.rtcpstarted = false;
      this.content = "";
      this.probationList = new SSRCTable();
      this.initBC = false;
      this.errorPayload = -1;
      this.cache = var1;
      this.rtpdemultiplexer = var2;
      this.setConsumer((PacketConsumer)null);
   }

   public RTPReceiver(SSRCCache var1, RTPDemultiplexer var2, int var3, String var4) throws IOException {
      this(var1, var2, (PacketSource)(new RTPRawReceiver(var3 & -2, var4, var1.field_173.defaultstats)));
   }

   public RTPReceiver(SSRCCache var1, RTPDemultiplexer var2, DatagramSocket var3) {
      this(var1, var2, (PacketSource)(new RTPRawReceiver(var3, var1.field_173.defaultstats)));
   }

   public RTPReceiver(SSRCCache var1, RTPDemultiplexer var2, PacketSource var3) {
      this(var1, var2);
      this.setSource(var3);
   }

   private boolean assertCurrentformat(SSRCInfo var1, int var2) {
      if (var1.currentformat == null) {
         var1.currentformat = this.cache.field_173.formatinfo.get(var2);
         if (var1.currentformat == null) {
            if (this.errorPayload != var2) {
               this.errorPayload = var2;
               StringBuilder var4 = new StringBuilder();
               var4.append("No format has been registered for RTP payload type (number) ");
               var4.append(var2);
               var4.append("!");
               Log.error(var4.toString());
            }

            return false;
         }

         if (var1.dstream != null) {
            var1.dstream.setFormat(var1.currentformat);
         }
      }

      if (var1.dsource != null) {
         RTPControlImpl var3 = this.getDsourceRTPControlImpl(var1);
         if (var3 != null) {
            var3.currentformat = var1.currentformat;
         }
      }

      return true;
   }

   private void demuxpayload(SSRCInfo var1, boolean var2, RTPPacket var3) {
      if (var1.dsource != null) {
         if (var2) {
            RTPPacket var4 = (RTPPacket)this.probationList.remove(var1.ssrc);
            if (var4 != null) {
               this.rtpdemultiplexer.demuxpayload(new SourceRTPPacket(var4, var1));
            }
         }

         this.rtpdemultiplexer.demuxpayload(new SourceRTPPacket(var3, var1));
      }

   }

   private RTPControlImpl getDsourceRTPControlImpl(SSRCInfo var1) {
      return (RTPControlImpl)var1.dsource.getControl(RTPControlImpl.class);
   }

   private void streamconnect(SSRCInfo var1) {
      if (!var1.streamconnect) {
         DataSource var3 = (DataSource)this.cache.field_173.dslist.get(var1.ssrc);
         DataSource var2 = var3;
         if (var3 == null) {
            var2 = this.cache.field_173.getDataSource((RTPMediaLocator)null);
            if (var2 == null) {
               var2 = this.cache.field_173.createNewDS((RTPMediaLocator)null);
               this.cache.field_173.setDefaultDSassigned(var1.ssrc);
            } else if (!this.cache.field_173.isDefaultDSassigned()) {
               this.cache.field_173.setDefaultDSassigned(var1.ssrc);
            } else {
               var2 = this.cache.field_173.createNewDS(var1.ssrc);
            }
         }

         PushBufferStream[] var4 = var2.getStreams();
         var1.dsource = var2;
         var1.dstream = (RTPSourceStream)var4[0];
         var1.dstream.setContentDescriptor("");
         var1.dstream.setFormat(var1.currentformat);
         RTPControlImpl var5 = this.getDsourceRTPControlImpl(var1);
         if (var5 != null) {
            var5.currentformat = var1.currentformat;
            var5.stream = var1;
         }

         var1.streamconnect = true;
      }

   }

   private void updateJitter(SSRCInfo var1, int var2, RTPPacket var3) {
      if (var1.lastRTPReceiptTime != 0L && var2 == var3.payloadType) {
         long var6 = var3.receiptTime;
         long var8 = var1.lastRTPReceiptTime;
         double var4 = Math.abs((double)((long)this.cache.clockrate[var1.payloadType] * (var6 - var8) / 1000L - (var3.timestamp - var1.lasttimestamp)));
         var1.jitter += (var4 - var1.jitter) * 0.0625D;
      }

   }

   private int updateLastPayloadType(SSRCInfo var1, RTPPacket var2) {
      int var3 = var1.lastPayloadType;
      int var4 = var2.payloadType;
      if (var3 != -1 && var3 != var4) {
         if (var2.payloadlength == 0) {
            return var3;
         } else {
            var1.currentformat = null;
            if (var1.dsource != null) {
               RTPControlImpl var8 = this.getDsourceRTPControlImpl(var1);
               if (var8 != null) {
                  var8.currentformat = null;
                  var8.payload = -1;
               }

               try {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Stopping stream because of payload type mismatch: expecting pt=");
                  var9.append(var3);
                  var9.append(", got pt=");
                  var9.append(var4);
                  Log.warning(var9.toString());
                  var1.dsource.stop();
               } catch (IOException var7) {
                  PrintStream var5 = System.err;
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Stopping DataSource after PCE ");
                  var6.append(var7.getMessage());
                  var5.println(var6.toString());
               }
            }

            var1.lastPayloadType = var4;
            this.cache.eventhandler.postEvent(new RemotePayloadChangeEvent(this.cache.field_173, (ReceiveStream)var1, var3, var4));
            return var3;
         }
      } else {
         var1.lastPayloadType = var4;
         return var3;
      }
   }

   private int updatePayloadType(SSRCInfo var1, RTPPacket var2) {
      int var3 = var1.payloadType;
      int var4 = var2.payloadType;
      if (var3 == -1 || var3 != var4 && var2.payloadlength != 0) {
         var1.payloadType = var2.payloadType;
      }

      return var3;
   }

   private void updateStats(SSRCInfo var1, RTPPacket var2) {
      int var3 = var2.seqnum;
      int var4 = var3 - var1.maxseq;
      if (var4 > 0) {
         if (var1.maxseq + 1 != var3) {
            var1.stats.update(0, var4 - 1);
         }
      } else if (var4 < 0 && var4 > -100) {
         var1.stats.update(0, -1);
      }

      if (var1.wrapped) {
         var1.wrapped = false;
      }

      if (var1.probation > 0) {
         if (var3 == var1.maxseq + 1) {
            --var1.probation;
            var1.maxseq = var3;
         } else {
            var1.probation = 1;
            var1.maxseq = var3;
            var1.stats.update(2);
         }
      } else if (var4 < 3000 && var4 != 0) {
         if (var3 < var1.baseseq && var4 < -32767) {
            var1.cycles += 65536;
            var1.wrapped = true;
         }

         var1.maxseq = var3;
      } else if (var4 <= 65436 && var4 != 0) {
         var1.stats.update(3);
         if (var3 == var1.lastbadseq) {
            var1.initsource(var3);
         } else {
            var1.lastbadseq = var3 + 1 & '\uffff';
         }
      } else {
         var1.stats.update(4);
      }
   }

   public String filtername() {
      return "RTP Packet Receiver";
   }

   public Packet handlePacket(Packet var1) {
      return this.handlePacket((RTPPacket)var1);
   }

   public Packet handlePacket(Packet var1, int var2) {
      return null;
   }

   public Packet handlePacket(Packet var1, SessionAddress var2) {
      return null;
   }

   public Packet handlePacket(RTPPacket var1) {
      int var4 = var1.payloadType;
      if (var4 == 13) {
         return var1;
      } else {
         int var2;
         InetAddress var8;
         if (var1.base instanceof UDPPacket) {
            UDPPacket var9 = (UDPPacket)var1.base;
            var8 = var9.remoteAddress;
            if (this.cache.field_173.bindtome && !this.cache.field_173.isBroadcast(this.cache.field_173.dataaddress) && !var8.equals(this.cache.field_173.dataaddress)) {
               return null;
            }

            var2 = var9.remotePort;
         } else {
            var8 = null;
            var2 = 0;
         }

         SSRCCache var15 = this.cache;
         int var3 = var1.ssrc;
         boolean var7 = true;
         SSRCInfo var16 = var15.get(var3, var8, var2, 1);
         if (var16 == null) {
            return null;
         } else {
            int[] var10 = var1.csrc;
            int var5 = var10.length;

            for(var3 = 0; var3 < var5; ++var3) {
               int var6 = var10[var3];
               SSRCInfo var11 = this.cache.get(var6, var8, var2, 1);
               if (var11 != null) {
                  var11.lastHeardFrom = var1.receiptTime;
               }
            }

            if (!var16.sender) {
               var16.initsource(var1.seqnum);
               var16.payloadType = var4;
            }

            var3 = var16.probation;
            this.updateStats(var16, var1);
            var5 = var16.probation;
            if (this.cache.field_173.isUnicast()) {
               if (!this.rtcpstarted) {
                  this.cache.field_173.startRTCPReports(var8);
                  this.rtcpstarted = true;
                  if ((this.cache.field_173.controladdress.getAddress()[3] & 255) == 255) {
                     this.cache.field_173.addUnicastAddr(this.cache.field_173.controladdress);
                  } else {
                     boolean var14;
                     label91: {
                        try {
                           var8 = InetAddress.getLocalHost();
                        } catch (UnknownHostException var13) {
                           var8 = null;
                           var14 = false;
                           break label91;
                        }

                        var14 = true;
                     }

                     if (var14) {
                        this.cache.field_173.addUnicastAddr(var8);
                     }
                  }
               } else if (!this.cache.field_173.isSenderDefaultAddr(var8)) {
                  this.cache.field_173.addUnicastAddr(var8);
               }
            }

            ++var16.received;
            var16.stats.update(1);
            if (var5 > 0) {
               this.probationList.put(var16.ssrc, var1.clone());
               return null;
            } else {
               var16.maxseq = var1.seqnum;
               var2 = this.updateLastPayloadType(var16, var1);
               if (!this.assertCurrentformat(var16, var4)) {
                  return var1;
               } else {
                  if (!this.initBC) {
                     ((BufferControlImpl)this.cache.field_173.buffercontrol).initBufferControl(var16.currentformat);
                     this.initBC = true;
                  }

                  this.streamconnect(var16);
                  if (var16.dsource != null) {
                     var16.active = true;
                  }

                  if (!var16.newrecvstream) {
                     var16.newrecvstream = true;
                     this.cache.eventhandler.postEvent(new NewReceiveStreamEvent(this.cache.field_173, (ReceiveStream)var16));
                  }

                  this.updateJitter(var16, var2, var1);
                  var16.lastRTPReceiptTime = var1.receiptTime;
                  var16.lasttimestamp = var1.timestamp;
                  this.updatePayloadType(var16, var1);
                  var16.bytesreceived += var1.payloadlength;
                  var16.lastHeardFrom = var1.receiptTime;
                  if (var16.quiet) {
                     var16.quiet = false;
                     RTPEventHandler var18 = this.cache.eventhandler;
                     RTPSessionMgr var19 = this.cache.field_173;
                     RTPSourceInfo var12 = var16.sourceInfo;
                     ReceiveStream var17;
                     if (var16 instanceof ReceiveStream) {
                        var17 = (ReceiveStream)var16;
                     } else {
                        var17 = null;
                     }

                     var18.postEvent(new ActiveReceiveStreamEvent(var19, var12, var17));
                  }

                  if (var3 <= 0 || var5 != 0) {
                     var7 = false;
                  }

                  this.demuxpayload(var16, var7, var1);
                  return var1;
               }
            }
         }
      }
   }
}
