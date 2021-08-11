package javax.jmdns;

import java.util.EventListener;

public interface ServiceListener extends EventListener {
   void serviceAdded(ServiceEvent var1);

   void serviceRemoved(ServiceEvent var1);

   void serviceResolved(ServiceEvent var1);
}
