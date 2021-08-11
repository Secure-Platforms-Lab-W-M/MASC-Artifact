package dnsfilter;

import java.io.IOException;
import java.net.DatagramPacket;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;

public class DNSCommunicator {
   private static DNSCommunicator INSTANCE = new DNSCommunicator();
   private static int TIMEOUT = 12000;
   int curDNS = -1;
   DNSServer[] dnsServers = new DNSServer[0];
   String lastDNS = "";

   public static DNSCommunicator getInstance() {
      return INSTANCE;
   }

   private boolean hasChanged(DNSServer[] var1, DNSServer[] var2) {
      if (var1.length != var2.length) {
         return true;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (!var1[var3].equals(var2[var3])) {
               return true;
            }
         }

         return false;
      }
   }

   private void switchDNSServer(DNSServer var1) throws IOException {
      synchronized(this){}

      try {
         if (var1 == this.getCurrentDNS()) {
            this.curDNS = (this.curDNS + 1) % this.dnsServers.length;
            if (ExecutionEnvironment.getEnvironment().debug()) {
               LoggerInterface var5 = Logger.getLogger();
               StringBuilder var2 = new StringBuilder();
               var2.append("Switched DNS Server to:");
               var2.append(this.getCurrentDNS().getAddress().getHostAddress());
               var5.logLine(var2.toString());
            }
         }
      } finally {
         ;
      }

   }

   public DNSServer getCurrentDNS() throws IOException {
      synchronized(this){}

      DNSServer var1;
      try {
         if (this.dnsServers.length == 0) {
            throw new IOException("No DNS Server initialized!");
         }

         this.lastDNS = this.dnsServers[this.curDNS].toString();
         var1 = this.dnsServers[this.curDNS];
      } finally {
         ;
      }

      return var1;
   }

   public String getLastDNSAddress() {
      return this.lastDNS;
   }

   public void requestDNS(DatagramPacket var1, DatagramPacket var2) throws IOException {
      DNSServer var3 = this.getCurrentDNS();

      try {
         var3.resolve(var1, var2);
      } catch (IOException var4) {
         if (ExecutionEnvironment.getEnvironment().hasNetwork()) {
            this.switchDNSServer(var3);
         }

         throw var4;
      }
   }

   public void setDNSServers(DNSServer[] var1) {
      synchronized(this){}

      Throwable var10000;
      label156: {
         boolean var10001;
         label148: {
            try {
               if (!this.hasChanged(var1, this.dnsServers)) {
                  return;
               }

               this.dnsServers = var1;
               if (this.dnsServers.length > 0) {
                  this.lastDNS = this.dnsServers[0].toString();
                  this.curDNS = 0;
                  break label148;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label156;
            }

            try {
               this.lastDNS = "";
               this.curDNS = -1;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label156;
            }
         }

         label138:
         try {
            if (ExecutionEnvironment.getEnvironment().debug()) {
               Logger.getLogger().logLine("Using updated DNS Servers!");
            }

            return;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label138;
         }
      }

      Throwable var14 = var10000;
      throw var14;
   }
}
