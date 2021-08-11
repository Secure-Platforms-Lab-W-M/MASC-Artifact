package javax.jmdns;

import java.util.EventListener;

public interface ServiceTypeListener extends EventListener {
   void serviceTypeAdded(ServiceEvent var1);

   void subTypeForServiceTypeAdded(ServiceEvent var1);
}
