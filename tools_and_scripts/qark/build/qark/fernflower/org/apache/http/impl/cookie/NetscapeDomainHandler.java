package org.apache.http.impl.cookie;

import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class NetscapeDomainHandler extends BasicDomainHandler {
   private static boolean isSpecialDomain(String var0) {
      var0 = var0.toUpperCase(Locale.ROOT);
      return var0.endsWith(".COM") || var0.endsWith(".EDU") || var0.endsWith(".NET") || var0.endsWith(".GOV") || var0.endsWith(".MIL") || var0.endsWith(".ORG") || var0.endsWith(".INT");
   }

   public String getAttributeName() {
      return "domain";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var4 = var2.getHost();
      String var3 = var1.getDomain();
      return var3 == null ? false : var4.endsWith(var3);
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (!TextUtils.isBlank(var2)) {
         var1.setDomain(var2);
      } else {
         throw new MalformedCookieException("Blank or null value for domain attribute");
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      String var6 = var2.getHost();
      String var5 = var1.getDomain();
      if (!var6.equals(var5) && !BasicDomainHandler.domainMatch(var5, var6)) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Illegal domain attribute \"");
         var4.append(var5);
         var4.append("\". Domain of origin: \"");
         var4.append(var6);
         var4.append("\"");
         throw new CookieRestrictionViolationException(var4.toString());
      } else if (var6.contains(".")) {
         int var3 = (new StringTokenizer(var5, ".")).countTokens();
         StringBuilder var7;
         if (isSpecialDomain(var5)) {
            if (var3 < 2) {
               var7 = new StringBuilder();
               var7.append("Domain attribute \"");
               var7.append(var5);
               var7.append("\" violates the Netscape cookie specification for ");
               var7.append("special domains");
               throw new CookieRestrictionViolationException(var7.toString());
            }
         } else if (var3 < 3) {
            var7 = new StringBuilder();
            var7.append("Domain attribute \"");
            var7.append(var5);
            var7.append("\" violates the Netscape cookie specification");
            throw new CookieRestrictionViolationException(var7.toString());
         }
      }
   }
}
