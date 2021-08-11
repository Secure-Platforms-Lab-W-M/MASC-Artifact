package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class ActiveReceiveStreamEvent extends ReceiveStreamEvent {
   public ActiveReceiveStreamEvent(SessionManager var1, Participant var2, ReceiveStream var3) {
      super(var1, var3, var2);
   }
}
