package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class TimeoutEvent extends ReceiveStreamEvent {
   private boolean participantBye;

   public TimeoutEvent(SessionManager var1, Participant var2, ReceiveStream var3, boolean var4) {
      super(var1, var3, var2);
      this.participantBye = var4;
   }

   public boolean participantLeaving() {
      return this.participantBye;
   }
}
