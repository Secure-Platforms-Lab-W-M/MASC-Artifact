package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class ByeEvent extends TimeoutEvent {
   private String reason;

   public ByeEvent(SessionManager var1, Participant var2, ReceiveStream var3, String var4, boolean var5) {
      super(var1, var2, var3, var5);
      this.reason = var4;
   }

   public String getReason() {
      return this.reason;
   }
}
