package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final CookieSpec cookieSpec;

   public NetscapeDraftSpecFactory() {
      this((String[])null);
   }

   public NetscapeDraftSpecFactory(String[] var1) {
      this.cookieSpec = new NetscapeDraftSpec(var1);
   }

   public CookieSpec create(HttpContext var1) {
      return this.cookieSpec;
   }

   public CookieSpec newInstance(HttpParams var1) {
      if (var1 != null) {
         Object var2 = null;
         Collection var3 = (Collection)var1.getParameter("http.protocol.cookie-datepatterns");
         String[] var4 = (String[])var2;
         if (var3 != null) {
            var4 = (String[])var3.toArray(new String[var3.size()]);
         }

         return new NetscapeDraftSpec(var4);
      } else {
         return new NetscapeDraftSpec();
      }
   }
}
