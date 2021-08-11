package net.sf.fmj.media.rtp;

import java.io.IOException;
import javax.media.rtp.RTPStream;
import javax.media.rtp.rtcp.Feedback;
import javax.media.rtp.rtcp.SenderReport;

public class RTCPSenderReport extends RTCPReport implements SenderReport {
   RTCPSenderInfo senderInformation = null;
   private RTPStream stream = null;

   public RTCPSenderReport(byte[] var1, int var2, int var3) throws IOException {
      super(var1, var2, var3);
      this.senderInformation = new RTCPSenderInfo(var1, var2 + 8, var3 - 8);
   }

   public long getNTPTimeStampLSW() {
      return this.senderInformation.getNtpTimestampLSW();
   }

   public long getNTPTimeStampMSW() {
      return this.senderInformation.getNtpTimestampMSW();
   }

   public long getRTPTimeStamp() {
      return this.senderInformation.getRtpTimestamp();
   }

   public long getSenderByteCount() {
      return this.senderInformation.getOctetCount();
   }

   public Feedback getSenderFeedback() {
      return null;
   }

   public long getSenderPacketCount() {
      return this.senderInformation.getPacketCount();
   }

   public RTPStream getStream() {
      return this.stream;
   }

   protected void setStream(RTPStream var1) {
      this.stream = var1;
   }
}
