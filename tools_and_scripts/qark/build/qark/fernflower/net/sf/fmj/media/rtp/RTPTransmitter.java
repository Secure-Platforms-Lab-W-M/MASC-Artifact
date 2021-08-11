package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.media.Buffer;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.RTPPacket;
import net.sf.fmj.media.rtp.util.UDPPacketSender;

public class RTPTransmitter {
   SSRCCache cache;
   RTPRawSender sender;

   public RTPTransmitter(SSRCCache var1) {
      this.cache = var1;
   }

   public RTPTransmitter(SSRCCache var1, int var2, String var3) throws UnknownHostException, IOException {
      this(var1, new RTPRawSender(var2, var3));
   }

   public RTPTransmitter(SSRCCache var1, int var2, String var3, UDPPacketSender var4) throws UnknownHostException, IOException {
      this(var1, new RTPRawSender(var2, var3, var4));
   }

   public RTPTransmitter(SSRCCache var1, RTPRawSender var2) {
      this(var1);
      this.setSender(var2);
   }

   protected RTPPacket MakeRTPPacket(Buffer var1, SendSSRCInfo var2) {
      byte[] var3 = (byte[])((byte[])var1.getData());
      if (var3 == null) {
         return null;
      } else {
         Packet var4 = new Packet();
         var4.data = var3;
         var4.offset = 0;
         var4.length = var1.getLength();
         var4.received = false;
         RTPPacket var6 = new RTPPacket(var4);
         if ((var1.getFlags() & 2048) != 0) {
            var6.marker = 1;
         } else {
            var6.marker = 0;
         }

         var2.packetsize += var1.getLength();
         var6.payloadType = var2.payloadType;
         var6.seqnum = (int)var2.getSequenceNumber(var1);
         var6.timestamp = var2.rtptime;
         var6.ssrc = var2.ssrc;
         var6.payloadoffset = var1.getOffset();
         var6.payloadlength = var1.getLength();
         var2.bytesreceived += var1.getLength();
         ++var2.maxseq;
         var2.lasttimestamp = var6.timestamp;
         Buffer.RTPHeaderExtension var5 = var1.getHeaderExtension();
         if (var5 != null) {
            var6.headerExtension = var5;
         }

         return var6;
      }
   }

   public void TransmitPacket(Buffer var1, SendSSRCInfo var2) {
      var2.rtptime = var2.getTimeStamp(var1);
      Object var3 = var1.getHeader();
      if (var3 != null && var3 instanceof Long) {
         var2.systime = (Long)var3;
      } else {
         var2.systime = System.currentTimeMillis();
      }

      RTPPacket var5 = this.MakeRTPPacket(var1, var2);
      if (var5 != null) {
         this.transmit(var5);
         RTPTransStats var6 = var2.stats;
         ++var6.total_pdu;
         var2.stats.total_bytes += var1.getLength();
         OverallTransStats var4 = this.cache.field_173.transstats;
         ++var4.rtp_sent;
         this.cache.field_173.transstats.bytes_sent += var1.getLength();
      }
   }

   public void close() {
      RTPRawSender var1 = this.sender;
      if (var1 != null) {
         var1.closeConsumer();
      }

   }

   public RTPRawSender getSender() {
      return this.sender;
   }

   public void setSender(RTPRawSender var1) {
      this.sender = var1;
   }

   protected void transmit(RTPPacket var1) {
      try {
         this.sender.sendTo(var1);
      } catch (IOException var2) {
         OverallTransStats var3 = this.cache.field_173.transstats;
         ++var3.transmit_failed;
      }
   }
}
