package net.sf.fmj.media.rtp;

import javax.media.Buffer;
import net.sf.fmj.media.rtp.util.RTPPacket;

public class RTPDemultiplexer {
   private Buffer buffer;
   private SSRCCache cache;
   private RTPRawReceiver rtpr;
   private StreamSynch streamSynch;

   public RTPDemultiplexer(SSRCCache var1, RTPRawReceiver var2, StreamSynch var3) {
      this.cache = var1;
      this.rtpr = var2;
      this.streamSynch = var3;
      this.buffer = new Buffer();
   }

   public String consumerString() {
      return "RTP DeMultiplexer";
   }

   public void demuxpayload(SourceRTPPacket var1) {
      SSRCInfo var4 = var1.ssrcinfo;
      RTPPacket var6 = var1.field_39;
      var4.payloadType = var6.payloadType;
      if (var4.dstream != null) {
         this.buffer.setData(var6.base.data);
         this.buffer.setFlags(var6.flags);
         Buffer var5;
         if (var6.marker == 1) {
            var5 = this.buffer;
            var5.setFlags(var5.getFlags() | 2048);
         }

         this.buffer.setLength(var6.payloadlength);
         this.buffer.setOffset(var6.payloadoffset);
         long var2 = this.streamSynch.calcTimestamp(var4.ssrc, var6.payloadType, var6.timestamp);
         this.buffer.setTimeStamp(var2);
         this.buffer.setRtpTimeStamp(var6.timestamp);
         var5 = this.buffer;
         var5.setFlags(var5.getFlags() | 4096);
         this.buffer.setSequenceNumber((long)var6.seqnum);
         this.buffer.setFormat(var4.dstream.getFormat());
         var4.dstream.add(this.buffer, var4.wrapped, this.rtpr);
      }

   }
}
