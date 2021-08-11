package javax.media.rtp;

import java.util.EventListener;
import javax.media.rtp.event.RemoteEvent;

public interface RemoteListener extends EventListener {
   void update(RemoteEvent var1);
}
