package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class ReceiveStreamEvent extends RTPEvent {
   private Participant participant;
   private ReceiveStream recvStream;

   public ReceiveStreamEvent(SessionManager var1, ReceiveStream var2, Participant var3) {
      super(var1);
      this.recvStream = var2;
      this.participant = var3;
   }

   public Participant getParticipant() {
      return this.participant;
   }

   public ReceiveStream getReceiveStream() {
      return this.recvStream;
   }
}
