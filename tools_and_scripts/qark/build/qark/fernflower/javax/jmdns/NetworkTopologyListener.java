package javax.jmdns;

import java.util.EventListener;

public interface NetworkTopologyListener extends EventListener {
   void inetAddressAdded(NetworkTopologyEvent var1);

   void inetAddressRemoved(NetworkTopologyEvent var1);
}
