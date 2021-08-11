package javax.jmdns.impl;

import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerStatus {
   public static final boolean ASYNCHRONOUS = false;
   public static final boolean SYNCHRONOUS = true;
   private final EventListener _listener;
   private final boolean _synch;

   public ListenerStatus(EventListener var1, boolean var2) {
      this._listener = var1;
      this._synch = var2;
   }

   public boolean equals(Object var1) {
      return var1 instanceof ListenerStatus && this.getListener().equals(((ListenerStatus)var1).getListener());
   }

   public EventListener getListener() {
      return this._listener;
   }

   public int hashCode() {
      return this.getListener().hashCode();
   }

   public boolean isSynchronous() {
      return this._synch;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[Status for ");
      var1.append(this.getListener().toString());
      var1.append("]");
      return var1.toString();
   }

   public static class ServiceListenerStatus extends ListenerStatus {
      private static Logger logger = LoggerFactory.getLogger(ListenerStatus.ServiceListenerStatus.class.getName());
      private final ConcurrentMap _addedServices = new ConcurrentHashMap(32);

      public ServiceListenerStatus(ServiceListener var1, boolean var2) {
         super(var1, var2);
      }

      private static final boolean _sameInfo(ServiceInfo var0, ServiceInfo var1) {
         if (var0 == null) {
            return false;
         } else if (var1 == null) {
            return false;
         } else if (!var0.equals(var1)) {
            return false;
         } else {
            byte[] var3 = var0.getTextBytes();
            byte[] var4 = var1.getTextBytes();
            if (var3.length != var4.length) {
               return false;
            } else {
               for(int var2 = 0; var2 < var3.length; ++var2) {
                  if (var3[var2] != var4[var2]) {
                     return false;
                  }
               }

               if (!var0.hasSameAddresses(var1)) {
                  return false;
               } else {
                  return true;
               }
            }
         }
      }

      void serviceAdded(ServiceEvent var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.getName());
         var2.append(".");
         var2.append(var1.getType());
         String var3 = var2.toString();
         if (this._addedServices.putIfAbsent(var3, var1.getInfo().clone()) == null) {
            ((ServiceListener)this.getListener()).serviceAdded(var1);
            ServiceInfo var4 = var1.getInfo();
            if (var4 != null && var4.hasData()) {
               ((ServiceListener)this.getListener()).serviceResolved(var1);
            }

         } else {
            logger.debug("Service Added called for a service already added: {}", var1);
         }
      }

      void serviceRemoved(ServiceEvent var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.getName());
         var2.append(".");
         var2.append(var1.getType());
         String var4 = var2.toString();
         ConcurrentMap var3 = this._addedServices;
         if (var3.remove(var4, var3.get(var4))) {
            ((ServiceListener)this.getListener()).serviceRemoved(var1);
         } else {
            logger.debug("Service Removed called for a service already removed: {}", var1);
         }
      }

      void serviceResolved(ServiceEvent var1) {
         synchronized(this){}

         Throwable var10000;
         label391: {
            ServiceInfo var2;
            boolean var10001;
            try {
               var2 = var1.getInfo();
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label391;
            }

            label394: {
               if (var2 != null) {
                  label404: {
                     ServiceInfo var4;
                     String var48;
                     try {
                        if (!var2.hasData()) {
                           break label404;
                        }

                        StringBuilder var3 = new StringBuilder();
                        var3.append(var1.getName());
                        var3.append(".");
                        var3.append(var1.getType());
                        var48 = var3.toString();
                        var4 = (ServiceInfo)this._addedServices.get(var48);
                        if (_sameInfo(var2, var4)) {
                           break label394;
                        }
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label391;
                     }

                     if (var4 == null) {
                        try {
                           if (this._addedServices.putIfAbsent(var48, var2.clone()) == null) {
                              ((ServiceListener)this.getListener()).serviceResolved(var1);
                              return;
                           }
                        } catch (Throwable var41) {
                           var10000 = var41;
                           var10001 = false;
                           break label391;
                        }
                     } else {
                        try {
                           if (this._addedServices.replace(var48, var4, var2.clone())) {
                              ((ServiceListener)this.getListener()).serviceResolved(var1);
                              return;
                           }
                        } catch (Throwable var42) {
                           var10000 = var42;
                           var10001 = false;
                           break label391;
                        }
                     }

                     return;
                  }
               }

               try {
                  logger.warn("Service Resolved called for an unresolved event: {}", var1);
                  return;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label391;
               }
            }

            label369:
            try {
               logger.debug("Service Resolved called for a service already resolved: {}", var1);
               return;
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label369;
            }
         }

         Throwable var47 = var10000;
         throw var47;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(2048);
         var1.append("[Status for ");
         var1.append(((ServiceListener)this.getListener()).toString());
         if (this._addedServices.isEmpty()) {
            var1.append(" no type event ");
         } else {
            var1.append(" (");
            Iterator var2 = this._addedServices.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               StringBuilder var4 = new StringBuilder();
               var4.append(var3);
               var4.append(", ");
               var1.append(var4.toString());
            }

            var1.append(") ");
         }

         var1.append("]");
         return var1.toString();
      }
   }

   public static class ServiceTypeListenerStatus extends ListenerStatus {
      private static Logger logger = LoggerFactory.getLogger(ListenerStatus.ServiceTypeListenerStatus.class.getName());
      private final ConcurrentMap _addedTypes = new ConcurrentHashMap(32);

      public ServiceTypeListenerStatus(ServiceTypeListener var1, boolean var2) {
         super(var1, var2);
      }

      void serviceTypeAdded(ServiceEvent var1) {
         if (this._addedTypes.putIfAbsent(var1.getType(), var1.getType()) == null) {
            ((ServiceTypeListener)this.getListener()).serviceTypeAdded(var1);
         } else {
            logger.trace("Service Type Added called for a service type already added: {}", var1);
         }
      }

      void subTypeForServiceTypeAdded(ServiceEvent var1) {
         if (this._addedTypes.putIfAbsent(var1.getType(), var1.getType()) == null) {
            ((ServiceTypeListener)this.getListener()).subTypeForServiceTypeAdded(var1);
         } else {
            logger.trace("Service Sub Type Added called for a service sub type already added: {}", var1);
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(2048);
         var1.append("[Status for ");
         var1.append(((ServiceTypeListener)this.getListener()).toString());
         if (this._addedTypes.isEmpty()) {
            var1.append(" no type event ");
         } else {
            var1.append(" (");
            Iterator var2 = this._addedTypes.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               StringBuilder var4 = new StringBuilder();
               var4.append(var3);
               var4.append(", ");
               var1.append(var4.toString());
            }

            var1.append(") ");
         }

         var1.append("]");
         return var1.toString();
      }
   }
}
