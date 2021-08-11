package javax.jmdns;

import java.net.InetAddress;
import java.util.EventObject;

public abstract class NetworkTopologyEvent extends EventObject {
   private static final long serialVersionUID = -8630033521752540987L;

   protected NetworkTopologyEvent(Object var1) {
      super(var1);
   }

   public abstract JmDNS getDNS();

   public abstract InetAddress getInetAddress();
}
