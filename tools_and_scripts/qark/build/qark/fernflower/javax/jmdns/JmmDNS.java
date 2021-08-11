package javax.jmdns;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.jmdns.impl.JmmDNSImpl;

public interface JmmDNS extends Closeable {
   void addNetworkTopologyListener(NetworkTopologyListener var1);

   void addServiceListener(String var1, ServiceListener var2);

   void addServiceTypeListener(ServiceTypeListener var1) throws IOException;

   JmDNS[] getDNS();

   String[] getHostNames();

   InetAddress[] getInetAddresses() throws IOException;

   @Deprecated
   InetAddress[] getInterfaces() throws IOException;

   String[] getNames();

   ServiceInfo[] getServiceInfos(String var1, String var2);

   ServiceInfo[] getServiceInfos(String var1, String var2, long var3);

   ServiceInfo[] getServiceInfos(String var1, String var2, boolean var3);

   ServiceInfo[] getServiceInfos(String var1, String var2, boolean var3, long var4);

   ServiceInfo[] list(String var1);

   ServiceInfo[] list(String var1, long var2);

   Map listBySubtype(String var1);

   Map listBySubtype(String var1, long var2);

   NetworkTopologyListener[] networkListeners();

   void registerService(ServiceInfo var1) throws IOException;

   void registerServiceType(String var1);

   void removeNetworkTopologyListener(NetworkTopologyListener var1);

   void removeServiceListener(String var1, ServiceListener var2);

   void removeServiceTypeListener(ServiceTypeListener var1);

   void requestServiceInfo(String var1, String var2);

   void requestServiceInfo(String var1, String var2, long var3);

   void requestServiceInfo(String var1, String var2, boolean var3);

   void requestServiceInfo(String var1, String var2, boolean var3, long var4);

   void unregisterAllServices();

   void unregisterService(ServiceInfo var1);

   public static final class Factory {
      private static final AtomicReference _databaseClassDelegate = new AtomicReference();
      private static volatile JmmDNS _instance;

      private Factory() {
      }

      public static JmmDNS.Factory.ClassDelegate classDelegate() {
         return (JmmDNS.Factory.ClassDelegate)_databaseClassDelegate.get();
      }

      public static void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public static JmmDNS getInstance() {
         synchronized(JmmDNS.Factory.class){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (_instance == null) {
                  _instance = newJmmDNS();
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               JmmDNS var13 = _instance;
               return var13;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var0 = var10000;

            try {
               throw var0;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      }

      protected static JmmDNS newJmmDNS() {
         JmmDNS var0 = null;
         JmmDNS.Factory.ClassDelegate var1 = (JmmDNS.Factory.ClassDelegate)_databaseClassDelegate.get();
         if (var1 != null) {
            var0 = var1.newJmmDNS();
         }

         return (JmmDNS)(var0 != null ? var0 : new JmmDNSImpl());
      }

      public static void setClassDelegate(JmmDNS.Factory.ClassDelegate var0) {
         _databaseClassDelegate.set(var0);
      }

      public interface ClassDelegate {
         JmmDNS newJmmDNS();
      }
   }
}
