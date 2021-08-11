package org.apache.http.client.methods;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.util.Args;

public class HttpOptions extends HttpRequestBase {
   public static final String METHOD_NAME = "OPTIONS";

   public HttpOptions() {
   }

   public HttpOptions(String var1) {
      this.setURI(URI.create(var1));
   }

   public HttpOptions(URI var1) {
      this.setURI(var1);
   }

   public Set getAllowedMethods(HttpResponse var1) {
      Args.notNull(var1, "HTTP response");
      HeaderIterator var6 = var1.headerIterator("Allow");
      HashSet var4 = new HashSet();

      while(var6.hasNext()) {
         HeaderElement[] var5 = var6.nextHeader().getElements();
         int var3 = var5.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.add(var5[var2].getName());
         }
      }

      return var4;
   }

   public String getMethod() {
      return "OPTIONS";
   }
}
