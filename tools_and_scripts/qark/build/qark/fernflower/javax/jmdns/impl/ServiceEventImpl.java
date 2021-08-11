package javax.jmdns.impl;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

public class ServiceEventImpl extends ServiceEvent {
   private static final long serialVersionUID = 7107973622016897488L;
   private final ServiceInfo _info;
   private final String _name;
   private final String _type;

   public ServiceEventImpl(JmDNSImpl var1, String var2, String var3, ServiceInfo var4) {
      super(var1);
      this._type = var2;
      this._name = var3;
      this._info = var4;
   }

   public ServiceEventImpl clone() {
      ServiceInfoImpl var1 = new ServiceInfoImpl(this.getInfo());
      return new ServiceEventImpl((JmDNSImpl)this.getDNS(), this.getType(), this.getName(), var1);
   }

   public JmDNS getDNS() {
      return (JmDNS)this.getSource();
   }

   public ServiceInfo getInfo() {
      return this._info;
   }

   public String getName() {
      return this._name;
   }

   public String getType() {
      return this._type;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[');
      var1.append(this.getClass().getSimpleName());
      var1.append('@');
      var1.append(System.identityHashCode(this));
      var1.append("\n\tname: '");
      var1.append(this.getName());
      var1.append("' type: '");
      var1.append(this.getType());
      var1.append("' info: '");
      var1.append(this.getInfo());
      var1.append("']");
      return var1.toString();
   }
}
