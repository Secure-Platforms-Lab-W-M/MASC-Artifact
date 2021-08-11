package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {
   protected final SchemeRegistry schemeRegistry;

   public DefaultHttpRoutePlanner(SchemeRegistry var1) {
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
   }

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var2, "HTTP request");
      HttpRoute var8 = ConnRouteParams.getForcedRoute(var2.getParams());
      if (var8 != null) {
         return var8;
      } else {
         Asserts.notNull(var1, "Target host");
         InetAddress var9 = ConnRouteParams.getLocalAddress(var2.getParams());
         HttpHost var7 = ConnRouteParams.getDefaultProxy(var2.getParams());

         Scheme var5;
         try {
            var5 = this.schemeRegistry.getScheme(var1.getSchemeName());
         } catch (IllegalStateException var6) {
            throw new HttpException(var6.getMessage());
         }

         boolean var4 = var5.isLayered();
         return var7 == null ? new HttpRoute(var1, var9, var4) : new HttpRoute(var1, var9, var7, var4);
      }
   }
}
