package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class RequestDefaultHeaders implements HttpRequestInterceptor {
   private final Collection defaultHeaders;

   public RequestDefaultHeaders() {
      this((Collection)null);
   }

   public RequestDefaultHeaders(Collection var1) {
      this.defaultHeaders = var1;
   }

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      if (!var1.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
         Collection var3 = (Collection)var1.getParams().getParameter("http.default-headers");
         Collection var4 = var3;
         if (var3 == null) {
            var4 = this.defaultHeaders;
         }

         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               var1.addHeader((Header)var5.next());
            }
         }

      }
   }
}
