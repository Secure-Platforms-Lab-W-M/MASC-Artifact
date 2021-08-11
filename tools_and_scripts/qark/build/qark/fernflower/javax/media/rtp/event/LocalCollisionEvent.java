package javax.media.rtp.event;

import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class LocalCollisionEvent extends SessionEvent {
   private long newSSRC;
   private ReceiveStream recvStream;

   public LocalCollisionEvent(SessionManager var1, ReceiveStream var2, long var3) {
      super(var1);
      this.recvStream = var2;
      this.newSSRC = var3;
   }

   public long getNewSSRC() {
      return this.newSSRC;
   }

   public ReceiveStream getReceiveStream() {
      return this.recvStream;
   }
}
