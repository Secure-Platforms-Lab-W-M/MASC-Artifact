package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class RFC2965SpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final CookieSpec cookieSpec;

   public RFC2965SpecFactory() {
      this((String[])null, false);
   }

   public RFC2965SpecFactory(String[] var1, boolean var2) {
      this.cookieSpec = new RFC2965Spec(var1, var2);
   }

   public CookieSpec create(HttpContext var1) {
      return this.cookieSpec;
   }

   public CookieSpec newInstance(HttpParams var1) {
      if (var1 != null) {
         String[] var2 = null;
         Collection var3 = (Collection)var1.getParameter("http.protocol.cookie-datepatterns");
         if (var3 != null) {
            var2 = (String[])var3.toArray(new String[var3.size()]);
         }

         return new RFC2965Spec(var2, var1.getBooleanParameter("http.protocol.single-cookie-header", false));
      } else {
         return new RFC2965Spec();
      }
   }
}
