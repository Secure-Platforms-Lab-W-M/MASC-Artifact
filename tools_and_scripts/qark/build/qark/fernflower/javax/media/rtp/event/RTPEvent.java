package javax.media.rtp.event;

import javax.media.MediaEvent;
import javax.media.rtp.SessionManager;

public class RTPEvent extends MediaEvent {
   private SessionManager eventSrc;

   public RTPEvent(SessionManager var1) {
      super(var1);
      this.eventSrc = var1;
   }

   public SessionManager getSessionManager() {
      return this.eventSrc;
   }

   public Object getSource() {
      return this.eventSrc;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source = ");
      var1.append(this.eventSrc);
      var1.append("]");
      return var1.toString();
   }
}
