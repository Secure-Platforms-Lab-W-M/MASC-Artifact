package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class RemotePayloadChangeEvent extends ReceiveStreamEvent {
   private int newpayload;
   private int oldpayload;

   public RemotePayloadChangeEvent(SessionManager var1, ReceiveStream var2, int var3, int var4) {
      super(var1, var2, (Participant)null);
      this.newpayload = var4;
      this.oldpayload = var3;
   }

   public int getNewPayload() {
      return this.newpayload;
   }
}
