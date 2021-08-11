package javax.media.rtp;

import java.util.EventListener;
import javax.media.rtp.event.SendStreamEvent;

public interface SendStreamListener extends EventListener {
   void update(SendStreamEvent var1);
}
