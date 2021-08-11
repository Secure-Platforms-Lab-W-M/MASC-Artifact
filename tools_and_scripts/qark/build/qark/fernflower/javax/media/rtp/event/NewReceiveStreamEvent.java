package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class NewReceiveStreamEvent extends ReceiveStreamEvent {
   public NewReceiveStreamEvent(SessionManager var1, ReceiveStream var2) {
      super(var1, var2, (Participant)null);
   }
}
