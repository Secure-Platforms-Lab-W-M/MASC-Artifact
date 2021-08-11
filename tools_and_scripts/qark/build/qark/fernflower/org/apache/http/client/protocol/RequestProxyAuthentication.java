package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class RequestProxyAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      if (!var1.containsHeader("Proxy-Authorization")) {
         HttpRoutedConnection var3 = (HttpRoutedConnection)var2.getAttribute("http.connection");
         if (var3 == null) {
            this.log.debug("HTTP connection not set in the context");
         } else if (!var3.getRoute().isTunnelled()) {
            AuthState var6 = (AuthState)var2.getAttribute("http.auth.proxy-scope");
            if (var6 == null) {
               this.log.debug("Proxy auth state not set in the context");
            } else {
               if (this.log.isDebugEnabled()) {
                  Log var4 = this.log;
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Proxy auth state: ");
                  var5.append(var6.getState());
                  var4.debug(var5.toString());
               }

               this.process(var6, var1, var2);
            }
         }
      }
   }
}
