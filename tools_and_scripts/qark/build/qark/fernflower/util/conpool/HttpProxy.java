package util.conpool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.Proxy.Type;
import util.http.HttpHeader;

public class HttpProxy extends Proxy {
   private String authString;
   private InetSocketAddress proxyAdr;

   public HttpProxy(InetSocketAddress var1) {
      this(var1, (String)null);
   }

   public HttpProxy(InetSocketAddress var1, String var2) {
      super(Type.HTTP, var1);
      this.proxyAdr = var1;
      this.authString = var2;
   }

   public Socket openTunnel(InetSocketAddress var1, int var2) throws IOException {
      String var3;
      if (!var1.getAddress().getHostAddress().equals("0.0.0.0")) {
         var3 = var1.getAddress().getHostAddress();
      } else {
         var3 = var1.getHostName();
      }

      HttpHeader var4 = new HttpHeader(1);
      StringBuilder var5 = new StringBuilder();
      var5.append("CONNECT ");
      var5.append(var3);
      var5.append(":");
      var5.append(var1.getPort());
      var5.append(" HTTP/1.1");
      var4.setRequest(var5.toString());
      if (this.authString != null) {
         var4.setValue("Proxy-Authorization", this.authString);
      }

      var3 = var4.getServerRequestHeader();
      InetSocketAddress var8 = new InetSocketAddress(InetAddress.getByAddress(var1.getHostName(), this.proxyAdr.getAddress().getAddress()), this.proxyAdr.getPort());
      Socket var6 = new Socket();
      var6.connect(var8, var2);
      var6.setSoTimeout(var2);
      var6.getOutputStream().write(var3.getBytes());
      var6.getOutputStream().flush();
      HttpHeader var9 = new HttpHeader(var6.getInputStream(), 2);
      if (var9.responsecode != 200) {
         var6.shutdownInput();
         var6.shutdownOutput();
         var6.close();
         StringBuilder var7 = new StringBuilder();
         var7.append("Proxy refused Tunnel\n");
         var7.append(var9.getResponseMessage());
         throw new IOException(var7.toString());
      } else {
         var6.setSoTimeout(0);
         return var6;
      }
   }

   public void setProxyAuth(String var1) {
      this.authString = var1;
   }
}
