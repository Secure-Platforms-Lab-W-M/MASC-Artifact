package javax.jmdns;

import java.util.EventObject;

public abstract class ServiceEvent extends EventObject implements Cloneable {
   private static final long serialVersionUID = -8558445644541006271L;

   public ServiceEvent(Object var1) {
      super(var1);
   }

   public ServiceEvent clone() {
      try {
         ServiceEvent var1 = (ServiceEvent)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public abstract JmDNS getDNS();

   public abstract ServiceInfo getInfo();

   public abstract String getName();

   public abstract String getType();
}
