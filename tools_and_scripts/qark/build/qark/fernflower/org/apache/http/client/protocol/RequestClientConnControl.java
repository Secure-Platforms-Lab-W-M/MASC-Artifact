package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class RequestClientConnControl implements HttpRequestInterceptor {
   private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      if (var1.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
         var1.setHeader("Proxy-Connection", "Keep-Alive");
      } else {
         RouteInfo var3 = HttpClientContext.adapt(var2).getHttpRoute();
         if (var3 == null) {
            this.log.debug("Connection route not set in the context");
         } else {
            if ((var3.getHopCount() == 1 || var3.isTunnelled()) && !var1.containsHeader("Connection")) {
               var1.addHeader("Connection", "Keep-Alive");
            }

            if (var3.getHopCount() == 2 && !var3.isTunnelled() && !var1.containsHeader("Proxy-Connection")) {
               var1.addHeader("Proxy-Connection", "Keep-Alive");
            }

         }
      }
   }
}
