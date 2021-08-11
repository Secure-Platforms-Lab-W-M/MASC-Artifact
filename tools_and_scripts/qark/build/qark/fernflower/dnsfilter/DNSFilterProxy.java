package dnsfilter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;
import java.util.Vector;
import util.ExecutionEnvironment;
import util.GroupedLogger;
import util.Logger;
import util.LoggerInterface;

public class DNSFilterProxy implements Runnable {
   int port = 53;
   DatagramSocket receiver;
   boolean stopped = false;

   public DNSFilterProxy(int var1) {
      this.port = var1;
   }

   // $FF: synthetic method
   static void access$000(DNSFilterManager var0) {
      initDNS(var0);
   }

   private static void initDNS(DNSFilterManager var0) {
      IOException var10000;
      label56: {
         boolean var10001;
         try {
            if (Boolean.parseBoolean(var0.getConfig().getProperty("detectDNS", "true"))) {
               Logger.getLogger().logLine("DNS detection not supported for this device");
               Logger.getLogger().message("DNS detection not supported - Using fallback!");
            }
         } catch (IOException var13) {
            var10000 = var13;
            var10001 = false;
            break label56;
         }

         int var2;
         int var3;
         Vector var4;
         StringTokenizer var14;
         try {
            var4 = new Vector();
            var2 = Integer.parseInt(var0.getConfig().getProperty("dnsRequestTimeout", "15000"));
            var14 = new StringTokenizer(var0.getConfig().getProperty("fallbackDNS", ""), ";");
            var3 = var14.countTokens();
         } catch (IOException var12) {
            var10000 = var12;
            var10001 = false;
            break label56;
         }

         for(int var1 = 0; var1 < var3; ++var1) {
            String var5;
            try {
               var5 = var14.nextToken().trim();
               LoggerInterface var6 = Logger.getLogger();
               StringBuilder var7 = new StringBuilder();
               var7.append("DNS:");
               var7.append(var5);
               var6.logLine(var7.toString());
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break label56;
            }

            try {
               var4.add(DNSServer.getInstance().createDNSServer(var5, var2));
            } catch (IOException var10) {
               IOException var16 = var10;

               try {
                  Logger.getLogger().logException(var16);
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label56;
               }
            }
         }

         try {
            DNSCommunicator.getInstance().setDNSServers((DNSServer[])var4.toArray(new DNSServer[var4.size()]));
            return;
         } catch (IOException var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      IOException var15 = var10000;
      Logger.getLogger().logException(var15);
   }

   public static void main(String[] var0) throws Exception {
      Logger.setLogger(new GroupedLogger(new LoggerInterface[]{new DNSFilterProxy$1StandaloneLogger()}));
      ExecutionEnvironment.setEnvironment(new DNSFilterProxy$1StandaloneEnvironment());
      DNSFilterManager var1 = DNSFilterManager.getInstance();
      var1.init();
      initDNS(var1);
      (new DNSFilterProxy(53)).run();
   }

   public void run() {
      int var1;
      try {
         var1 = Integer.parseInt(DNSFilterManager.getInstance().getConfig().getProperty("maxResolverCount", "100"));
      } catch (Exception var6) {
         Logger.getLogger().logLine("Exception:Cannot get maxResolverCount configuration!");
         Logger.getLogger().logException(var6);
         return;
      }

      LoggerInterface var3;
      StringBuilder var4;
      try {
         this.receiver = new DatagramSocket(this.port);
      } catch (IOException var5) {
         var3 = Logger.getLogger();
         var4 = new StringBuilder();
         var4.append("Exception:Cannot open DNS port ");
         var4.append(this.port);
         var4.append("!");
         var4.append(var5.getMessage());
         var3.logLine(var4.toString());
         return;
      }

      LoggerInterface var2 = Logger.getLogger();
      StringBuilder var9 = new StringBuilder();
      var9.append("DNSFilterProxy running on port ");
      var9.append(this.port);
      var9.append("!");
      var2.logLine(var9.toString());

      while(!this.stopped) {
         try {
            DatagramPacket var8 = new DatagramPacket(new byte[DNSServer.getBufSize()], 0, DNSServer.getBufSize());
            this.receiver.receive(var8);
            if (DNSResolver.getResolverCount() > var1) {
               var2 = Logger.getLogger();
               var9 = new StringBuilder();
               var9.append("Max Resolver Count reached: ");
               var9.append(var1);
               var2.message(var9.toString());
            } else {
               (new Thread(new DNSResolver(var8, this.receiver))).start();
            }
         } catch (IOException var7) {
            if (!this.stopped) {
               var3 = Logger.getLogger();
               var4 = new StringBuilder();
               var4.append("Exception:");
               var4.append(var7.getMessage());
               var3.logLine(var4.toString());
            }
         }
      }

      Logger.getLogger().logLine("DNSFilterProxy stopped!");
   }

   public void stop() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         DatagramSocket var1;
         boolean var10001;
         try {
            this.stopped = true;
            var1 = this.receiver;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         if (var1 == null) {
            return;
         }

         try {
            this.receiver.close();
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var8 = var10000;
      throw var8;
   }
}
