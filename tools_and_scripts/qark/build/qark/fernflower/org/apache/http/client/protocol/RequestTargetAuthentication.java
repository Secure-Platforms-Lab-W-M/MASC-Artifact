package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class RequestTargetAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      if (!var1.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
         if (!var1.containsHeader("Authorization")) {
            AuthState var3 = (AuthState)var2.getAttribute("http.auth.target-scope");
            if (var3 == null) {
               this.log.debug("Target auth state not set in the context");
            } else {
               if (this.log.isDebugEnabled()) {
                  Log var4 = this.log;
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Target auth state: ");
                  var5.append(var3.getState());
                  var4.debug(var5.toString());
               }

               this.process(var3, var1, var2);
            }
         }
      }
   }
}
