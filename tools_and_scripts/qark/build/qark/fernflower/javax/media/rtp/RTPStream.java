package javax.media.rtp;

import javax.media.protocol.DataSource;
import javax.media.rtp.rtcp.SenderReport;

public interface RTPStream {
   DataSource getDataSource();

   Participant getParticipant();

   long getSSRC();

   SenderReport getSenderReport();
}
