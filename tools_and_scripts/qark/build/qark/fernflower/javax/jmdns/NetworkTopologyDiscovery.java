package javax.jmdns;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.atomic.AtomicReference;
import javax.jmdns.impl.NetworkTopologyDiscoveryImpl;

public interface NetworkTopologyDiscovery {
   InetAddress[] getInetAddresses();

   void lockInetAddress(InetAddress var1);

   void unlockInetAddress(InetAddress var1);

   boolean useInetAddress(NetworkInterface var1, InetAddress var2);

   public static final class Factory {
      private static final AtomicReference _databaseClassDelegate = new AtomicReference();
      private static volatile NetworkTopologyDiscovery _instance;

      private Factory() {
      }

      public static NetworkTopologyDiscovery.Factory.ClassDelegate classDelegate() {
         return (NetworkTopologyDiscovery.Factory.ClassDelegate)_databaseClassDelegate.get();
      }

      public static NetworkTopologyDiscovery getInstance() {
         if (_instance == null) {
            synchronized(NetworkTopologyDiscovery.Factory.class){}

            Throwable var10000;
            boolean var10001;
            label144: {
               try {
                  if (_instance == null) {
                     _instance = newNetworkTopologyDiscovery();
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label144;
               }

               label141:
               try {
                  return _instance;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label141;
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
         } else {
            return _instance;
         }
      }

      protected static NetworkTopologyDiscovery newNetworkTopologyDiscovery() {
         NetworkTopologyDiscovery var0 = null;
         NetworkTopologyDiscovery.Factory.ClassDelegate var1 = (NetworkTopologyDiscovery.Factory.ClassDelegate)_databaseClassDelegate.get();
         if (var1 != null) {
            var0 = var1.newNetworkTopologyDiscovery();
         }

         return (NetworkTopologyDiscovery)(var0 != null ? var0 : new NetworkTopologyDiscoveryImpl());
      }

      public static void setClassDelegate(NetworkTopologyDiscovery.Factory.ClassDelegate var0) {
         _databaseClassDelegate.set(var0);
      }

      public interface ClassDelegate {
         NetworkTopologyDiscovery newNetworkTopologyDiscovery();
      }
   }
}
