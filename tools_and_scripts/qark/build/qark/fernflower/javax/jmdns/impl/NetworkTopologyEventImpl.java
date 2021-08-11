package javax.jmdns.impl;

import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;

public class NetworkTopologyEventImpl extends NetworkTopologyEvent implements Cloneable {
   private static final long serialVersionUID = 1445606146153550463L;
   private final InetAddress _inetAddress;

   public NetworkTopologyEventImpl(JmDNS var1, InetAddress var2) {
      super(var1);
      this._inetAddress = var2;
   }

   NetworkTopologyEventImpl(NetworkTopologyListener var1, InetAddress var2) {
      super(var1);
      this._inetAddress = var2;
   }

   public NetworkTopologyEventImpl clone() throws CloneNotSupportedException {
      return new NetworkTopologyEventImpl(this.getDNS(), this.getInetAddress());
   }

   public JmDNS getDNS() {
      return this.getSource() instanceof JmDNS ? (JmDNS)this.getSource() : null;
   }

   public InetAddress getInetAddress() {
      return this._inetAddress;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[');
      var1.append(this.getClass().getSimpleName());
      var1.append('@');
      var1.append(System.identityHashCode(this));
      var1.append("\n\tinetAddress: '");
      var1.append(this.getInetAddress());
      var1.append("']");
      return var1.toString();
   }
}
