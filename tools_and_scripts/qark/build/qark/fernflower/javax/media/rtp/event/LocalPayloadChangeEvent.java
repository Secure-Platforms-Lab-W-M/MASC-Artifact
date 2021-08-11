package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionManager;

public class LocalPayloadChangeEvent extends SendStreamEvent {
   private int newpayload;
   private int oldpayload;

   public LocalPayloadChangeEvent(SessionManager var1, SendStream var2, int var3, int var4) {
      super(var1, var2, (Participant)null);
      this.newpayload = var4;
      this.oldpayload = var3;
   }

   public int getNewPayload() {
      return this.newpayload;
   }
}
