package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class RTCPSenderInfo {
   private static final long MSB_0_BASE_TIME = 2085978496000L;
   public static final long MSB_1_BASE_TIME = -2208988800000L;
   public static final int SIZE = 20;
   private long ntpTimestampLSW = 0L;
   private long ntpTimestampMSW = 0L;
   private long octetCount = 0L;
   private long packetCount = 0L;
   private long rtpTimestamp = 0L;

   public RTCPSenderInfo(byte[] var1, int var2, int var3) throws IOException {
      DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var3));
      this.ntpTimestampMSW = (long)var4.readInt() & 4294967295L;
      this.ntpTimestampLSW = (long)var4.readInt() & 4294967295L;
      this.rtpTimestamp = (long)var4.readInt() & 4294967295L;
      this.packetCount = (long)var4.readInt() & 4294967295L;
      this.octetCount = (long)var4.readInt() & 4294967295L;
   }

   public long getNtpTimestampLSW() {
      return this.ntpTimestampLSW;
   }

   public long getNtpTimestampMSW() {
      return this.ntpTimestampMSW;
   }

   public double getNtpTimestampSecs() {
      return (double)this.getTimestamp() / 1000.0D;
   }

   public long getOctetCount() {
      return this.octetCount;
   }

   public long getPacketCount() {
      return this.packetCount;
   }

   public long getRtpTimestamp() {
      return this.rtpTimestamp;
   }

   public long getTimestamp() {
      long var1 = this.ntpTimestampMSW;
      long var3 = Math.round((double)this.ntpTimestampLSW * 1000.0D / 4.294967296E9D);
      return (2147483648L & var1) == 0L ? 1000L * var1 + 2085978496000L + var3 : 1000L * var1 - 2208988800000L + var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append("ntp_ts=");
      var1.append(this.getNtpTimestampMSW());
      String var3 = var1.toString();
      StringBuilder var2 = new StringBuilder();
      var2.append(var3);
      var2.append(" ");
      var2.append(this.getNtpTimestampLSW());
      var3 = var2.toString();
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append(" rtp_ts=");
      var2.append(this.getRtpTimestamp());
      var3 = var2.toString();
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append(" packet_ct=");
      var2.append(this.getPacketCount());
      var3 = var2.toString();
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append(" octect_ct=");
      var2.append(this.getOctetCount());
      return var2.toString();
   }
}
