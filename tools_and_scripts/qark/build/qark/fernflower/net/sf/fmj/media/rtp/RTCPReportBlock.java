package net.sf.fmj.media.rtp;

import javax.media.rtp.rtcp.Feedback;

public class RTCPReportBlock implements Feedback {
   long dlsr;
   int fractionlost;
   int jitter;
   long lastseq;
   long lsr;
   int packetslost;
   public long receiptTime;
   int ssrc;

   public RTCPReportBlock() {
   }

   public RTCPReportBlock(int var1, int var2, int var3, long var4, int var6, long var7, long var9) {
      this.ssrc = var1;
      this.fractionlost = var2;
      this.packetslost = var3;
      this.lastseq = var4;
      this.jitter = var6;
      this.lsr = var7;
      this.dlsr = var9;
   }

   public static String toString(RTCPReportBlock[] var0) {
      String var2 = "";

      for(int var1 = 0; var1 < var0.length; ++var1) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(var0[var1]);
         var2 = var3.toString();
      }

      return var2;
   }

   public long getDLSR() {
      return this.dlsr;
   }

   public int getFractionLost() {
      return this.fractionlost;
   }

   public long getJitter() {
      return (long)this.jitter;
   }

   public long getLSR() {
      return this.lsr;
   }

   public long getNumLost() {
      return (long)this.packetslost;
   }

   public long getSSRC() {
      return (long)this.ssrc;
   }

   public long getXtndSeqNum() {
      return this.lastseq;
   }

   public String toString() {
      long var1 = (long)this.ssrc;
      StringBuilder var3 = new StringBuilder();
      var3.append("\t\tFor source ");
      var3.append(var1 & 4294967295L);
      var3.append("\n\t\t\tFraction of packets lost: ");
      var3.append(this.fractionlost);
      var3.append(" (");
      var3.append((double)this.fractionlost / 256.0D);
      var3.append(")\n\t\t\tPackets lost: ");
      var3.append(this.packetslost);
      var3.append("\n\t\t\tLast sequence number: ");
      var3.append(this.lastseq);
      var3.append("\n\t\t\tJitter: ");
      var3.append(this.jitter);
      var3.append("\n\t\t\tLast SR packet received at time ");
      var3.append(this.lsr);
      var3.append("\n\t\t\tDelay since last SR packet received: ");
      var3.append(this.dlsr);
      var3.append(" (");
      var3.append((double)this.dlsr / 65536.0D);
      var3.append(" seconds)\n");
      return var3.toString();
   }
}
