package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class RFC2965DomainAttributeHandler implements CommonCookieAttributeHandler {
   public boolean domainMatch(String var1, String var2) {
      return var1.equals(var2) || var2.startsWith(".") && var1.endsWith(var2);
   }

   public String getAttributeName() {
      return "domain";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var6 = var2.getHost().toLowerCase(Locale.ROOT);
      String var5 = var1.getDomain();
      boolean var4 = this.domainMatch(var6, var5);
      boolean var3 = false;
      if (!var4) {
         return false;
      } else {
         if (var6.substring(0, var6.length() - var5.length()).indexOf(46) == -1) {
            var3 = true;
         }

         return var3;
      }
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 != null) {
         if (!var2.trim().isEmpty()) {
            String var4 = var2.toLowerCase(Locale.ROOT);
            String var3 = var4;
            if (!var2.startsWith(".")) {
               StringBuilder var5 = new StringBuilder();
               var5.append('.');
               var5.append(var4);
               var3 = var5.toString();
            }

            var1.setDomain(var3);
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
      String var5 = var2.getHost().toLowerCase(Locale.ROOT);
      if (var1.getDomain() == null) {
         throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
      } else {
         String var4 = var1.getDomain().toLowerCase(Locale.ROOT);
         if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("domain")) {
            StringBuilder var6;
            if (!var4.startsWith(".")) {
               var6 = new StringBuilder();
               var6.append("Domain attribute \"");
               var6.append(var1.getDomain());
               var6.append("\" violates RFC 2109: domain must start with a dot");
               throw new CookieRestrictionViolationException(var6.toString());
            } else {
               int var3 = var4.indexOf(46, 1);
               if ((var3 < 0 || var3 == var4.length() - 1) && !var4.equals(".local")) {
                  var6 = new StringBuilder();
                  var6.append("Domain attribute \"");
                  var6.append(var1.getDomain());
                  var6.append("\" violates RFC 2965: the value contains no embedded dots ");
                  var6.append("and the value is not .local");
                  throw new CookieRestrictionViolationException(var6.toString());
               } else if (this.domainMatch(var5, var4)) {
                  if (var5.substring(0, var5.length() - var4.length()).indexOf(46) != -1) {
                     var6 = new StringBuilder();
                     var6.append("Domain attribute \"");
                     var6.append(var1.getDomain());
                     var6.append("\" violates RFC 2965: ");
                     var6.append("effective host minus domain may not contain any dots");
                     throw new CookieRestrictionViolationException(var6.toString());
                  }
               } else {
                  var6 = new StringBuilder();
                  var6.append("Domain attribute \"");
                  var6.append(var1.getDomain());
                  var6.append("\" violates RFC 2965: effective host name does not ");
                  var6.append("domain-match domain attribute.");
                  throw new CookieRestrictionViolationException(var6.toString());
               }
            }
         } else if (!var1.getDomain().equals(var5)) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Illegal domain attribute: \"");
            var7.append(var1.getDomain());
            var7.append("\".");
            var7.append("Domain of origin: \"");
            var7.append(var5);
            var7.append("\"");
            throw new CookieRestrictionViolationException(var7.toString());
         }
      }
   }
}
