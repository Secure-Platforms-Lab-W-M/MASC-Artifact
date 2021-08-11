package javax.media.rtp;

import java.util.EventListener;
import javax.media.rtp.event.SessionEvent;

public interface SessionListener extends EventListener {
   void update(SessionEvent var1);
}
