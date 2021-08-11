package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.util.Args;

public class DefaultSchemePortResolver implements SchemePortResolver {
   public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();

   public int resolve(HttpHost var1) throws UnsupportedSchemeException {
      Args.notNull(var1, "HTTP host");
      int var2 = var1.getPort();
      if (var2 > 0) {
         return var2;
      } else {
         String var4 = var1.getSchemeName();
         if (var4.equalsIgnoreCase("http")) {
            return 80;
         } else if (var4.equalsIgnoreCase("https")) {
            return 443;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append(var4);
            var3.append(" protocol is not supported");
            throw new UnsupportedSchemeException(var3.toString());
         }
      }
   }
}
