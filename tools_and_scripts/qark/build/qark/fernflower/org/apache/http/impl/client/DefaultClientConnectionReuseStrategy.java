package org.apache.http.impl.client;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHeaderIterator;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;

public class DefaultClientConnectionReuseStrategy extends DefaultConnectionReuseStrategy {
   public static final DefaultClientConnectionReuseStrategy INSTANCE = new DefaultClientConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse var1, HttpContext var2) {
      HttpRequest var3 = (HttpRequest)var2.getAttribute("http.request");
      if (var3 != null) {
         Header[] var4 = var3.getHeaders("Connection");
         if (var4.length != 0) {
            BasicTokenIterator var5 = new BasicTokenIterator(new BasicHeaderIterator(var4, (String)null));

            while(var5.hasNext()) {
               if ("Close".equalsIgnoreCase(var5.nextToken())) {
                  return false;
               }
            }
         }
      }

      return super.keepAlive(var1, var2);
   }
}
