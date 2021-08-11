package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class BasicDomainHandler implements CommonCookieAttributeHandler {
   static boolean domainMatch(String var0, String var1) {
      if (!InetAddressUtils.isIPv4Address(var1)) {
         if (InetAddressUtils.isIPv6Address(var1)) {
            return false;
         } else {
            if (var0.startsWith(".")) {
               var0 = var0.substring(1);
            }

            if (var1.endsWith(var0)) {
               int var2 = var1.length() - var0.length();
               if (var2 == 0) {
                  return true;
               }

               if (var2 > 1 && var1.charAt(var2 - 1) == '.') {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public String getAttributeName() {
      return "domain";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var4 = var2.getHost();
      String var3 = var1.getDomain();
      if (var3 == null) {
         return false;
      } else {
         String var5 = var3;
         if (var3.startsWith(".")) {
            var5 = var3.substring(1);
         }

         var5 = var5.toLowerCase(Locale.ROOT);
         if (var4.equals(var5)) {
            return true;
         } else {
            return var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("domain") ? domainMatch(var5, var4) : false;
         }
      }
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (!TextUtils.isBlank(var2)) {
         if (!var2.endsWith(".")) {
            String var3 = var2;
            if (var2.startsWith(".")) {
               var3 = var2.substring(1);
            }

            var1.setDomain(var3.toLowerCase(Locale.ROOT));
         }
      } else {
         throw new MalformedCookieException("Blank or null value for domain attribute");
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var5 = var2.getHost();
      String var4 = var1.getDomain();
      if (var4 != null) {
         if (!var5.equals(var4)) {
            if (!domainMatch(var4, var5)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Illegal 'domain' attribute \"");
               var3.append(var4);
               var3.append("\". Domain of origin: \"");
               var3.append(var5);
               var3.append("\"");
               throw new CookieRestrictionViolationException(var3.toString());
            }
         }
      } else {
         throw new CookieRestrictionViolationException("Cookie 'domain' may not be null");
      }
   }
}
