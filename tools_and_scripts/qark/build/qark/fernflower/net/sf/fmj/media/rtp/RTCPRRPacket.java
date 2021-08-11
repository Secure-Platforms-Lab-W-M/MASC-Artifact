package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;

public class RTCPRRPacket extends RTCPPacket {
   public RTCPReportBlock[] reports;
   public int ssrc;

   public RTCPRRPacket(int var1, RTCPReportBlock[] var2) {
      if (var2.length <= 31) {
         this.ssrc = var1;
         this.reports = var2;
      } else {
         throw new IllegalArgumentException("Too many reports");
      }
   }

   RTCPRRPacket(RTCPPacket var1) {
      super(var1);
      super.type = 201;
   }

   public void assemble(DataOutputStream var1) throws IOException {
      var1.writeByte(this.reports.length + 128);
      var1.writeByte(201);
      var1.writeShort(this.reports.length * 6 + 1);
      var1.writeInt(this.ssrc);
      int var2 = 0;

      while(true) {
         RTCPReportBlock[] var3 = this.reports;
         if (var2 >= var3.length) {
            return;
         }

         var1.writeInt(var3[var2].ssrc);
         var1.writeInt((this.reports[var2].packetslost & 16777215) + (this.reports[var2].fractionlost << 24));
         var1.writeInt((int)this.reports[var2].lastseq);
         var1.writeInt(this.reports[var2].jitter);
         var1.writeInt((int)this.reports[var2].lsr);
         var1.writeInt((int)this.reports[var2].dlsr);
         ++var2;
      }
   }

   public int calcLength() {
      return this.reports.length * 24 + 8;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\tRTCP RR (receiver report) packet for sync source ");
      var1.append(this.ssrc);
      var1.append(":\n");
      var1.append(RTCPReportBlock.toString(this.reports));
      return var1.toString();
   }
}
