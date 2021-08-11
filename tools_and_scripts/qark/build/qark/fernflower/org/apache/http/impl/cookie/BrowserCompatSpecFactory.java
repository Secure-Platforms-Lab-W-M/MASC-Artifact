package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final CookieSpec cookieSpec;
   private final BrowserCompatSpecFactory.SecurityLevel securityLevel;

   public BrowserCompatSpecFactory() {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpecFactory(String[] var1) {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpecFactory(String[] var1, BrowserCompatSpecFactory.SecurityLevel var2) {
      this.securityLevel = var2;
      this.cookieSpec = new BrowserCompatSpec(var1, var2);
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

         return new BrowserCompatSpec(var4, this.securityLevel);
      } else {
         return new BrowserCompatSpec((String[])null, this.securityLevel);
      }
   }

   public static enum SecurityLevel {
      SECURITYLEVEL_DEFAULT,
      SECURITYLEVEL_IE_MEDIUM;

      static {
         BrowserCompatSpecFactory.SecurityLevel var0 = new BrowserCompatSpecFactory.SecurityLevel("SECURITYLEVEL_IE_MEDIUM", 1);
         SECURITYLEVEL_IE_MEDIUM = var0;
      }
   }
}
