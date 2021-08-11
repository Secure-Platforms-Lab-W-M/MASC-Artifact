package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class InactiveReceiveStreamEvent extends ReceiveStreamEvent {
   private boolean laststream;

   public InactiveReceiveStreamEvent(SessionManager var1, Participant var2, ReceiveStream var3, boolean var4) {
      super(var1, var3, var2);
      this.laststream = var4;
   }

   public boolean isLastStream() {
      return this.laststream;
   }
}
