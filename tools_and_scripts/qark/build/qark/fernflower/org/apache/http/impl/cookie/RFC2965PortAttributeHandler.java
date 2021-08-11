package org.apache.http.impl.cookie;

import java.util.StringTokenizer;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.util.Args;

public class RFC2965PortAttributeHandler implements CommonCookieAttributeHandler {
   private static int[] parsePortAttribute(String var0) throws MalformedCookieException {
      StringTokenizer var5 = new StringTokenizer(var0, ",");
      int[] var2 = new int[var5.countTokens()];
      int var1 = 0;

      NumberFormatException var10000;
      while(true) {
         boolean var10001;
         try {
            if (!var5.hasMoreTokens()) {
               return var2;
            }

            var2[var1] = Integer.parseInt(var5.nextToken().trim());
         } catch (NumberFormatException var4) {
            var10000 = var4;
            var10001 = false;
            break;
         }

         if (var2[var1] < 0) {
            try {
               throw new MalformedCookieException("Invalid Port attribute.");
            } catch (NumberFormatException var3) {
               var10000 = var3;
               var10001 = false;
               break;
            }
         }

         ++var1;
      }

      NumberFormatException var6 = var10000;
      StringBuilder var7 = new StringBuilder();
      var7.append("Invalid Port attribute: ");
      var7.append(var6.getMessage());
      throw new MalformedCookieException(var7.toString());
   }

   private static boolean portMatch(int var0, int[] var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0 == var1[var2]) {
            return true;
         }
      }

      return false;
   }

   public String getAttributeName() {
      return "port";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      int var3 = var2.getPort();
      if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("port")) {
         if (var1.getPorts() == null) {
            return false;
         }

         if (!portMatch(var3, var1.getPorts())) {
            return false;
         }
      }

      return true;
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var1 instanceof SetCookie2) {
         SetCookie2 var3 = (SetCookie2)var1;
         if (var2 != null && !var2.trim().isEmpty()) {
            var3.setPorts(parsePortAttribute(var2));
         }
      }

   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      int var3 = var2.getPort();
      if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("port")) {
         if (!portMatch(var3, var1.getPorts())) {
            throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
         }
      }
   }
}
