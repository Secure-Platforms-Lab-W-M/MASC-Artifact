package org.apache.http.impl.cookie;

import org.apache.http.cookie.CommonCookieAttributeHandler;

public class RFC6265LaxSpec extends RFC6265CookieSpecBase {
   public RFC6265LaxSpec() {
      super(new BasicPathHandler(), new BasicDomainHandler(), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler());
   }

   RFC6265LaxSpec(CommonCookieAttributeHandler... var1) {
      super(var1);
   }

   public String toString() {
      return "rfc6265-lax";
   }
}
