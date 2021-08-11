package dnsfilter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.net.ssl.SSLSocketFactory;
import util.conpool.Connection;

class TCP extends DNSServer {
   boolean ssl;

   protected TCP(InetAddress var1, int var2, int var3, boolean var4, String var5) throws IOException {
      super(var1, var2, var3);
      this.ssl = var4;
      if (var5 != null) {
         if (var5.indexOf("://") != -1) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Invalid hostname specified for ");
            var6.append(this.getProtocolName());
            var6.append(": ");
            var6.append(var5);
            throw new IOException(var6.toString());
         }

         this.address = new InetSocketAddress(InetAddress.getByAddress(var5, var1.getAddress()), var2);
      }

   }

   public String getProtocolName() {
      return this.ssl ? "DOT" : "TCP";
   }

   public void resolve(DatagramPacket var1, DatagramPacket var2) throws IOException {
      int var3 = 0;

      while(var3 < 2) {
         Connection var4 = Connection.connect(this.address, this.timeout, this.ssl, (SSLSocketFactory)null, Proxy.NO_PROXY);
         var4.setSoTimeout(this.timeout);

         try {
            DataInputStream var5 = new DataInputStream(var4.getInputStream());
            DataOutputStream var6 = new DataOutputStream(var4.getOutputStream());
            var6.writeShort(var1.getLength());
            var6.write(var1.getData(), var1.getOffset(), var1.getLength());
            var6.flush();
            this.readResponseFromStream(var5, var5.readShort(), var2);
            var2.setSocketAddress(this.address);
            var4.release(true);
            return;
         } catch (EOFException var7) {
            var4.release(false);
            if (var3 == 1) {
               StringBuilder var9 = new StringBuilder();
               var9.append("EOF when reading from ");
               var9.append(this.toString());
               throw new IOException(var9.toString(), var7);
            }

            ++var3;
         } catch (IOException var8) {
            var4.release(false);
            throw var8;
         }
      }

   }
}
