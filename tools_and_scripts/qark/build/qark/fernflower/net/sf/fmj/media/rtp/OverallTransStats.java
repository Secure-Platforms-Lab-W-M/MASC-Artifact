package net.sf.fmj.media.rtp;

import javax.media.rtp.GlobalTransmissionStats;

public class OverallTransStats implements GlobalTransmissionStats {
   protected int bytes_sent = 0;
   protected int local_coll = 0;
   protected int remote_coll = 0;
   public int rtcp_sent = 0;
   protected int rtp_sent = 0;
   public int transmit_failed = 0;

   public int getBytesSent() {
      return this.bytes_sent;
   }

   public int getLocalColls() {
      return this.local_coll;
   }

   public int getRTCPSent() {
      return this.rtcp_sent;
   }

   public int getRTPSent() {
      return this.rtp_sent;
   }

   public int getRemoteColls() {
      return this.remote_coll;
   }

   public int getTransmitFailed() {
      return this.transmit_failed;
   }
}
