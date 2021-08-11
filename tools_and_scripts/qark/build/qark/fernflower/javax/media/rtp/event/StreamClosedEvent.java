package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionManager;

public class StreamClosedEvent extends SendStreamEvent {
   public StreamClosedEvent(SessionManager var1, SendStream var2) {
      super(var1, var2, (Participant)null);
   }
}
