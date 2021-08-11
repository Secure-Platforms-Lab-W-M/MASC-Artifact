package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionManager;

public class InactiveSendStreamEvent extends SendStreamEvent {
   public InactiveSendStreamEvent(SessionManager var1, Participant var2, SendStream var3) {
      super(var1, var3, var2);
   }
}
