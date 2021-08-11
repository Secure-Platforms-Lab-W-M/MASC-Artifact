package dnsfilter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import util.conpool.Connection;

public class DNSServer {
   public static final int DOH = 3;
   public static final int DOT = 2;
   private static DNSServer INSTANCE = new DNSServer((InetAddress)null, 0, 0);
   public static final int TCP = 1;
   public static final int UDP = 0;
   private static int bufSize = 1024;
   private static int maxBufSize = -1;
   protected InetSocketAddress address;
   protected int timeout;

   static {
      Connection.setPoolTimeoutSeconds(30);
   }

   protected DNSServer(InetAddress var1, int var2, int var3) {
      this.address = new InetSocketAddress(var1, var2);
      this.timeout = var3;
   }

   public static int getBufSize() {
      return bufSize;
   }

   public static DNSServer getInstance() {
      return INSTANCE;
   }

   public static int getProtoFromString(String var0) throws IOException {
      var0 = var0.toUpperCase();
      if (var0.equals("UDP")) {
         return 0;
      } else if (var0.equals("TCP")) {
         return 1;
      } else if (var0.equals("DOT")) {
         return 2;
      } else if (var0.equals("DOH")) {
         return 3;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Invalid Protocol: ");
         var1.append(var0);
         throw new IOException(var1.toString());
      }
   }

   public DNSServer createDNSServer(int var1, InetAddress var2, int var3, int var4, String var5) throws IOException {
      switch(var1) {
      case 0:
         return new UDP(var2, var3, var4);
      case 1:
         return new TCP(var2, var3, var4, false, var5);
      case 2:
         return new TCP(var2, var3, var4, true, var5);
      case 3:
         return new DoH(var2, var3, var4, var5);
      default:
         StringBuilder var6 = new StringBuilder();
         var6.append("Invalid protocol:");
         var6.append(var1);
         throw new IllegalArgumentException(var6.toString());
      }
   }

   public DNSServer createDNSServer(String var1, int var2) throws IOException {
      Object var7 = null;
      String var5 = null;
      int var3;
      String var6;
      if (var1.startsWith("[")) {
         var3 = var1.indexOf("]");
         var6 = var1;
         if (var3 != -1) {
            var5 = var1.substring(1, var3);
            var6 = var1.substring(var3);
         }
      } else {
         String var8 = var1.toUpperCase();
         var5 = (String)var7;
         var6 = var1;
         if (var8.indexOf("::UDP") == -1) {
            var5 = (String)var7;
            var6 = var1;
            if (var8.indexOf("::DOT") == -1) {
               var5 = (String)var7;
               var6 = var1;
               if (var8.indexOf("::DOH") == -1) {
                  var6 = "";
                  var5 = var1;
               }
            }
         }
      }

      String[] var10 = var6.split("::");
      var1 = var5;
      if (var5 == null) {
         var1 = var10[0];
      }

      var3 = 53;
      if (var10.length > 1) {
         try {
            var3 = Integer.parseInt(var10[1]);
         } catch (NumberFormatException var9) {
            throw new IOException("Invalid Port!", var9);
         }
      }

      int var4 = 0;
      if (var10.length > 2) {
         var4 = getProtoFromString(var10[2]);
      }

      var5 = null;
      if (var10.length > 3) {
         var5 = var10[3];
      }

      return getInstance().createDNSServer(var4, InetAddress.getByName(var1), var3, var2, var5);
   }

   public boolean equals(Object var1) {
      return var1 != null && var1.getClass().equals(this.getClass()) ? this.address.equals(((DNSServer)var1).address) : false;
   }

   public InetAddress getAddress() {
      return this.address.getAddress();
   }

   public int getPort() {
      return this.address.getPort();
   }

   public String getProtocolName() {
      return "";
   }

   protected void readResponseFromStream(DataInputStream param1, int param2, DatagramPacket param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void resolve(DatagramPacket var1, DatagramPacket var2) throws IOException {
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append(this.address.getAddress().getHostAddress());
      var1.append("]::");
      var1.append(this.address.getPort());
      var1.append("::");
      var1.append(this.getProtocolName());
      return var1.toString();
   }
}
