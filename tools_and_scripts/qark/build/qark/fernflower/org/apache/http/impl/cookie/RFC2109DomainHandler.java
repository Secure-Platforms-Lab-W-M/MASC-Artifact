package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class RFC2109DomainHandler implements CommonCookieAttributeHandler {
   public String getAttributeName() {
      return "domain";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var6 = var2.getHost();
      String var5 = var1.getDomain();
      boolean var4 = false;
      if (var5 == null) {
         return false;
      } else {
         boolean var3;
         if (!var6.equals(var5)) {
            var3 = var4;
            if (!var5.startsWith(".")) {
               return var3;
            }

            var3 = var4;
            if (!var6.endsWith(var5)) {
               return var3;
            }
         }

         var3 = true;
         return var3;
      }
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 != null) {
         if (!var2.trim().isEmpty()) {
            var1.setDomain(var2);
         } else {
            throw new MalformedCookieException("Blank value for domain attribute");
         }
      } else {
         throw new MalformedCookieException("Missing value for domain attribute");
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var6 = var2.getHost();
      String var5 = var1.getDomain();
      if (var5 != null) {
         if (!var5.equals(var6)) {
            StringBuilder var4;
            if (var5.indexOf(46) != -1) {
               StringBuilder var7;
               if (var5.startsWith(".")) {
                  int var3 = var5.indexOf(46, 1);
                  if (var3 >= 0 && var3 != var5.length() - 1) {
                     var6 = var6.toLowerCase(Locale.ROOT);
                     if (var6.endsWith(var5)) {
                        if (var6.substring(0, var6.length() - var5.length()).indexOf(46) != -1) {
                           var7 = new StringBuilder();
                           var7.append("Domain attribute \"");
                           var7.append(var5);
                           var7.append("\" violates RFC 2109: host minus domain may not contain any dots");
                           throw new CookieRestrictionViolationException(var7.toString());
                        }
                     } else {
                        var4 = new StringBuilder();
                        var4.append("Illegal domain attribute \"");
                        var4.append(var5);
                        var4.append("\". Domain of origin: \"");
                        var4.append(var6);
                        var4.append("\"");
                        throw new CookieRestrictionViolationException(var4.toString());
                     }
                  } else {
                     var7 = new StringBuilder();
                     var7.append("Domain attribute \"");
                     var7.append(var5);
                     var7.append("\" violates RFC 2109: domain must contain an embedded dot");
                     throw new CookieRestrictionViolationException(var7.toString());
                  }
               } else {
                  var7 = new StringBuilder();
                  var7.append("Domain attribute \"");
                  var7.append(var5);
                  var7.append("\" violates RFC 2109: domain must start with a dot");
                  throw new CookieRestrictionViolationException(var7.toString());
               }
            } else {
               var4 = new StringBuilder();
               var4.append("Domain attribute \"");
               var4.append(var5);
               var4.append("\" does not match the host \"");
               var4.append(var6);
               var4.append("\"");
               throw new CookieRestrictionViolationException(var4.toString());
            }
         }
      } else {
         throw new CookieRestrictionViolationException("Cookie domain may not be null");
      }
   }
}
