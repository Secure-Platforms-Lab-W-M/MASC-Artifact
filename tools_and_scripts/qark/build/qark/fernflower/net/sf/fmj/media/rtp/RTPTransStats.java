package net.sf.fmj.media.rtp;

import javax.media.rtp.TransmissionStats;

public class RTPTransStats implements TransmissionStats {
   protected int total_bytes = 0;
   protected int total_pdu = 0;
   public int total_rtcp = 0;

   public int getBytesTransmitted() {
      return this.total_bytes;
   }

   public int getPDUTransmitted() {
      return this.total_pdu;
   }

   public int getRTCPSent() {
      return this.total_rtcp;
   }
}
