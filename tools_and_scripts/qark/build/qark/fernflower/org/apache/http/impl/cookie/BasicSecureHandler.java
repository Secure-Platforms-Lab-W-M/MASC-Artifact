package org.apache.http.impl.cookie;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class BasicSecureHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   public String getAttributeName() {
      return "secure";
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      return !var1.isSecure() || var2.isSecure();
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      var1.setSecure(true);
   }
}
