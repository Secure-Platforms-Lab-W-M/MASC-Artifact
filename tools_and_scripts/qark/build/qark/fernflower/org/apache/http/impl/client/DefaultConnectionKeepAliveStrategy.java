package org.apache.http.impl.client;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
   public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();

   public long getKeepAliveDuration(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      BasicHeaderElementIterator var8 = new BasicHeaderElementIterator(var1.headerIterator("Keep-Alive"));

      while(true) {
         String var7;
         String var9;
         do {
            do {
               if (!var8.hasNext()) {
                  return -1L;
               }

               HeaderElement var5 = var8.nextElement();
               var9 = var5.getName();
               var7 = var5.getValue();
            } while(var7 == null);
         } while(!var9.equalsIgnoreCase("timeout"));

         try {
            long var3 = Long.parseLong(var7);
            return var3 * 1000L;
         } catch (NumberFormatException var6) {
         }
      }
   }
}
