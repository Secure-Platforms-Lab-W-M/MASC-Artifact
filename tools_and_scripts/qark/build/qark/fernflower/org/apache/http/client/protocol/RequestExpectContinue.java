package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class RequestExpectContinue implements HttpRequestInterceptor {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      if (!var1.containsHeader("Expect") && var1 instanceof HttpEntityEnclosingRequest) {
         ProtocolVersion var3 = var1.getRequestLine().getProtocolVersion();
         HttpEntity var4 = ((HttpEntityEnclosingRequest)var1).getEntity();
         if (var4 != null && var4.getContentLength() != 0L && !var3.lessEquals(HttpVersion.HTTP_1_0) && HttpClientContext.adapt(var2).getRequestConfig().isExpectContinueEnabled()) {
            var1.addHeader("Expect", "100-continue");
         }
      }

   }
}
