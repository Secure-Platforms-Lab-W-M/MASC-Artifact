package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionManager;

public class SendStreamEvent extends RTPEvent {
   private Participant participant;
   private SendStream sendStream;

   public SendStreamEvent(SessionManager var1, SendStream var2, Participant var3) {
      super(var1);
      this.sendStream = var2;
      this.participant = var3;
   }

   public Participant getParticipant() {
      return this.participant;
   }

   public SendStream getSendStream() {
      return this.sendStream;
   }
}
