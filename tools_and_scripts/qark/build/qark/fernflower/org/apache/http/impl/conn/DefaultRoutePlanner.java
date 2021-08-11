package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultRoutePlanner implements HttpRoutePlanner {
   private final SchemePortResolver schemePortResolver;

   public DefaultRoutePlanner(SchemePortResolver var1) {
      if (var1 == null) {
         var1 = DefaultSchemePortResolver.INSTANCE;
      }

      this.schemePortResolver = (SchemePortResolver)var1;
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      return null;
   }

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var2, "Request");
      if (var1 != null) {
         RequestConfig var5 = HttpClientContext.adapt(var3).getRequestConfig();
         InetAddress var7 = var5.getLocalAddress();
         HttpHost var6 = var5.getProxy();
         HttpHost var10 = var6;
         if (var6 == null) {
            var10 = this.determineProxy(var1, var2, var3);
         }

         if (var1.getPort() <= 0) {
            try {
               var1 = new HttpHost(var1.getHostName(), this.schemePortResolver.resolve(var1), var1.getSchemeName());
            } catch (UnsupportedSchemeException var8) {
               throw new HttpException(var8.getMessage());
            }
         }

         boolean var4 = var1.getSchemeName().equalsIgnoreCase("https");
         HttpRoute var9;
         if (var10 == null) {
            var9 = new HttpRoute(var1, var7, var4);
            return var9;
         } else {
            var9 = new HttpRoute(var1, var7, var10, var4);
            return var9;
         }
      } else {
         throw new ProtocolException("Target host is not specified");
      }
   }
}
