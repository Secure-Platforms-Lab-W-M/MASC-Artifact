package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class StreamMappedEvent extends ReceiveStreamEvent {
   public StreamMappedEvent(SessionManager var1, ReceiveStream var2, Participant var3) {
      super(var1, var2, var3);
   }
}
