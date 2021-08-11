package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;

public class RequestAcceptEncoding implements HttpRequestInterceptor {
   private final String acceptEncoding;

   public RequestAcceptEncoding() {
      this((List)null);
   }

   public RequestAcceptEncoding(List var1) {
      if (var1 != null && !var1.isEmpty()) {
         StringBuilder var3 = new StringBuilder();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            if (var2 > 0) {
               var3.append(",");
            }

            var3.append((String)var1.get(var2));
         }

         this.acceptEncoding = var3.toString();
      } else {
         this.acceptEncoding = "gzip,deflate";
      }
   }

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      RequestConfig var3 = HttpClientContext.adapt(var2).getRequestConfig();
      if (!var1.containsHeader("Accept-Encoding") && var3.isContentCompressionEnabled()) {
         var1.addHeader("Accept-Encoding", this.acceptEncoding);
      }

   }
}
