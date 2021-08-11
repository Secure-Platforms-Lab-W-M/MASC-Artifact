package javax.media.rtp.rtcp;

import javax.media.rtp.RTPStream;

public interface SenderReport extends Report {
   long getNTPTimeStampLSW();

   long getNTPTimeStampMSW();

   long getRTPTimeStamp();

   long getSenderByteCount();

   Feedback getSenderFeedback();

   long getSenderPacketCount();

   RTPStream getStream();
}
