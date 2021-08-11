package javax.media.rtp;

import java.util.EventListener;
import javax.media.rtp.event.ReceiveStreamEvent;

public interface ReceiveStreamListener extends EventListener {
   void update(ReceiveStreamEvent var1);
}
