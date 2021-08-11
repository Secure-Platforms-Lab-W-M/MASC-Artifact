package org.apache.http.impl.cookie;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class BasicPathHandler implements CommonCookieAttributeHandler {
   static boolean pathMatch(String var0, String var1) {
      String var2 = var1;
      var1 = var1;
      if (var2 == null) {
         var1 = "/";
      }

      var2 = var1;
      if (var1.length() > 1) {
         var2 = var1;
         if (var1.endsWith("/")) {
            var2 = var1.substring(0, var1.length() - 1);
         }
      }

      if (var0.startsWith(var2)) {
         if (var2.equals("/")) {
            return true;
         }

         if (var0.length() == var2.length()) {
            return true;
         }

         if (var0.charAt(var2.length()) == '/') {
            return true;
         }
      }

      return false;
   }

   public String getAttributeName() {
      return "path";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      return pathMatch(var2.getPath(), var1.getPath());
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (TextUtils.isBlank(var2)) {
         var2 = "/";
      }

      var1.setPath(var2);
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
   }
}
