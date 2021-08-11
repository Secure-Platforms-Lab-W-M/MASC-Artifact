package javax.media.rtp.event;

import javax.media.rtp.SessionManager;

public class RemoteCollisionEvent extends RemoteEvent {
   private long collidingSSRC;

   public RemoteCollisionEvent(SessionManager var1, long var2) {
      super(var1);
      this.collidingSSRC = var2;
   }

   public long getSSRC() {
      return this.collidingSSRC;
   }
}
