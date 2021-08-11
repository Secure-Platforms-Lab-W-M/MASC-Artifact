package net.sf.fmj.media.rtp;

import java.util.Vector;
import javax.media.protocol.DataSource;
import javax.media.rtp.LocalParticipant;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPStream;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceptionStats;
import javax.media.rtp.rtcp.Feedback;
import javax.media.rtp.rtcp.ReceiverReport;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SenderReport;

public class RecvSSRCInfo extends SSRCInfo implements ReceiveStream, SenderReport, ReceiverReport {
   RecvSSRCInfo(SSRCCache var1, int var2) {
      super(var1, var2);
   }

   RecvSSRCInfo(SSRCInfo var1) {
      super(var1);
   }

   public DataSource getDataSource() {
      return super.dsource;
   }

   public long getNTPTimeStampLSW() {
      return super.lastSRntptimestamp & 4294967295L;
   }

   public long getNTPTimeStampMSW() {
      return super.lastSRntptimestamp >> 32 & 4294967295L;
   }

   public Participant getParticipant() {
      SSRCCache var1 = this.getSSRCCache();
      return super.sourceInfo instanceof LocalParticipant && var1.field_173.IsNonParticipating() ? null : super.sourceInfo;
   }

   public long getRTPTimeStamp() {
      return super.lastSRrtptimestamp;
   }

   public long getSSRC() {
      return (long)super.ssrc;
   }

   public long getSenderByteCount() {
      return super.lastSRoctetcount;
   }

   public Feedback getSenderFeedback() {
      SSRCCache var7 = this.getSSRCCache();

      boolean var10001;
      Vector var13;
      try {
         var13 = var7.field_173.getLocalParticipant().getReports();
      } catch (NullPointerException var12) {
         var10001 = false;
         return null;
      }

      int var1 = 0;

      while(true) {
         Vector var8;
         try {
            if (var1 >= var13.size()) {
               return null;
            }

            var8 = ((Report)var13.elementAt(var1)).getFeedbackReports();
         } catch (NullPointerException var11) {
            var10001 = false;
            return null;
         }

         int var2 = 0;

         while(true) {
            long var3;
            long var5;
            Feedback var9;
            try {
               if (var2 >= var8.size()) {
                  break;
               }

               var9 = (Feedback)var8.elementAt(var2);
               var3 = var9.getSSRC();
               var5 = this.getSSRC();
            } catch (NullPointerException var10) {
               var10001 = false;
               return null;
            }

            if (var3 == var5) {
               return var9;
            }

            ++var2;
         }

         ++var1;
      }
   }

   public long getSenderPacketCount() {
      return super.lastSRpacketcount;
   }

   public SenderReport getSenderReport() {
      return this;
   }

   public ReceptionStats getSourceReceptionStats() {
      return super.stats;
   }

   public RTPStream getStream() {
      return this;
   }
}
