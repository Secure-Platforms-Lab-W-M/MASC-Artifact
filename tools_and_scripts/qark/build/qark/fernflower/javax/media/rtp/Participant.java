package javax.media.rtp;

import java.util.Vector;

public interface Participant {
   String getCNAME();

   Vector getReports();

   Vector getSourceDescription();

   Vector getStreams();
}
