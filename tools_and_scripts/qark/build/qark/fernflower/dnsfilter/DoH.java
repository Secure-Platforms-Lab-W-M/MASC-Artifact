package dnsfilter;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.net.ssl.SSLSocketFactory;
import util.conpool.Connection;
import util.http.HttpHeader;

class DoH extends DNSServer {
   String reqTemplate;
   String url;
   String urlHost;
   InetSocketAddress urlHostAddress;

   protected DoH(InetAddress var1, int var2, int var3, String var4) throws IOException {
      super(var1, var2, var3);
      if (var4 == null) {
         throw new IOException("Endpoint URL not defined for DNS over HTTPS (DoH)!");
      } else {
         this.url = var4;
         this.buildTemplate();
         this.urlHostAddress = new InetSocketAddress(InetAddress.getByAddress(this.urlHost, var1.getAddress()), var2);
      }
   }

   private byte[] buildRequestHeader(int var1) throws IOException {
      String var2 = this.reqTemplate;
      StringBuilder var3 = new StringBuilder();
      var3.append("\nContent-Length: ");
      var3.append(var1);
      return var2.replace("\nContent-Length: 999", var3.toString()).getBytes();
   }

   private void buildTemplate() throws IOException {
      StringBuilder var1 = new StringBuilder();
      var1.append("Mozilla/5.0 (");
      var1.append(System.getProperty("os.name"));
      var1.append("; ");
      var1.append(System.getProperty("os.version"));
      var1.append(")");
      String var2 = var1.toString();
      HttpHeader var3 = new HttpHeader(1);
      var3.setValue("User-Agent", var2);
      var3.setValue("Accept", "application/dns-message");
      var3.setValue("content-type", "application/dns-message");
      var3.setValue("Connection", "keep-alive");
      StringBuilder var4 = new StringBuilder();
      var4.append("POST ");
      var4.append(this.url);
      var4.append(" HTTP/1.1");
      var3.setRequest(var4.toString());
      var3.setValue("Content-Length", "999");
      this.reqTemplate = var3.getServerRequestHeader(false);
      this.urlHost = var3.remote_host_name;
   }

   public String getProtocolName() {
      return "DOH";
   }

   public void resolve(DatagramPacket var1, DatagramPacket var2) throws IOException {
      byte[] var5 = this.buildRequestHeader(var1.getLength());
      int var3 = 0;

      while(var3 < 2) {
         Connection var4 = Connection.connect(this.urlHostAddress, this.timeout, true, (SSLSocketFactory)null, Proxy.NO_PROXY);

         try {
            OutputStream var7 = var4.getOutputStream();
            DataInputStream var6 = new DataInputStream(var4.getInputStream());
            var7.write(var5);
            var7.write(var1.getData(), var1.getOffset(), var1.getLength());
            var7.flush();
            HttpHeader var12 = new HttpHeader(var6, 2);
            if (var12.getResponseCode() != 200) {
               StringBuilder var11 = new StringBuilder();
               var11.append("DoH failed for ");
               var11.append(this.url);
               var11.append("! ");
               var11.append(var12.getResponseCode());
               var11.append(" - ");
               var11.append(var12.getResponseMessage());
               throw new IOException(var11.toString());
            }

            this.readResponseFromStream(var6, (int)var12.getContentLength(), var2);
            var2.setSocketAddress(this.address);
            var4.release(true);
            return;
         } catch (EOFException var8) {
            var4.release(false);
            if (var3 == 1) {
               StringBuilder var10 = new StringBuilder();
               var10.append("EOF when reading from ");
               var10.append(this.toString());
               throw new IOException(var10.toString(), var8);
            }

            ++var3;
         } catch (IOException var9) {
            var4.release(false);
            throw var9;
         }
      }

   }
}
