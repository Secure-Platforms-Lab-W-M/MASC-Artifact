package javax.jmdns.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import javax.jmdns.NetworkTopologyDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkTopologyDiscoveryImpl implements NetworkTopologyDiscovery {
   private static final Logger logger = LoggerFactory.getLogger(NetworkTopologyDiscoveryImpl.class.getName());

   public InetAddress[] getInetAddresses() {
      HashSet var1 = new HashSet();

      SocketException var10000;
      label42: {
         Enumeration var2;
         boolean var10001;
         try {
            var2 = NetworkInterface.getNetworkInterfaces();
         } catch (SocketException var8) {
            var10000 = var8;
            var10001 = false;
            break label42;
         }

         label39:
         while(true) {
            NetworkInterface var3;
            Enumeration var4;
            try {
               if (!var2.hasMoreElements()) {
                  return (InetAddress[])var1.toArray(new InetAddress[var1.size()]);
               }

               var3 = (NetworkInterface)var2.nextElement();
               var4 = var3.getInetAddresses();
            } catch (SocketException var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }

            while(true) {
               try {
                  if (!var4.hasMoreElements()) {
                     break;
                  }

                  InetAddress var5 = (InetAddress)var4.nextElement();
                  logger.trace("Found NetworkInterface/InetAddress: {} -- {}", var3, var5);
                  if (this.useInetAddress(var3, var5)) {
                     var1.add(var5);
                  }
               } catch (SocketException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label39;
               }
            }
         }
      }

      SocketException var9 = var10000;
      Logger var10 = logger;
      StringBuilder var11 = new StringBuilder();
      var11.append("Error while fetching network interfaces addresses: ");
      var11.append(var9);
      var10.warn(var11.toString());
      return (InetAddress[])var1.toArray(new InetAddress[var1.size()]);
   }

   public void lockInetAddress(InetAddress var1) {
   }

   public void unlockInetAddress(InetAddress var1) {
   }

   public boolean useInetAddress(NetworkInterface var1, InetAddress var2) {
      boolean var3;
      try {
         if (!var1.isUp()) {
            return false;
         }

         if (!var1.supportsMulticast()) {
            return false;
         }

         var3 = var1.isLoopback();
      } catch (Exception var4) {
         return false;
      }

      return !var3;
   }
}
