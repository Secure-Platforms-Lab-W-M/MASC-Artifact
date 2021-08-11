package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.SessionManager;

public class NewParticipantEvent extends SessionEvent {
   private Participant participant;

   public NewParticipantEvent(SessionManager var1, Participant var2) {
      super(var1);
      this.participant = var2;
   }

   public Participant getParticipant() {
      return this.participant;
   }
}
