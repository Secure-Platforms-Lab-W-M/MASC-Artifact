package javax.media.rtp.rtcp;

import java.util.Vector;
import javax.media.rtp.Participant;

public interface Report {
   Vector getFeedbackReports();

   Participant getParticipant();

   long getSSRC();

   Vector getSourceDescription();
}
