package dnsfilter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDP extends DNSServer {
   protected UDP(InetAddress var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public String getProtocolName() {
      return "UDP";
   }

   public void resolve(DatagramPacket var1, DatagramPacket var2) throws IOException {
      DatagramSocket var3 = new DatagramSocket();

      try {
         var1.setSocketAddress(this.address);
         var3.setSoTimeout(this.timeout);

         try {
            var3.send(var1);
         } catch (IOException var8) {
            StringBuilder var11 = new StringBuilder();
            var11.append("Cannot reach ");
            var11.append(this.address);
            var11.append("!");
            var11.append(var8.getMessage());
            throw new IOException(var11.toString());
         }

         try {
            var3.receive(var2);
         } catch (IOException var7) {
            StringBuilder var10 = new StringBuilder();
            var10.append("No DNS Response from ");
            var10.append(this.address);
            throw new IOException(var10.toString());
         }
      } finally {
         var3.close();
      }

   }
}
