package org.apache.http.impl.cookie;

import java.util.Collections;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

public class IgnoreSpec extends CookieSpecBase {
   public List formatCookies(List var1) {
      return Collections.emptyList();
   }

   public int getVersion() {
      return 0;
   }

   public Header getVersionHeader() {
      return null;
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return false;
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      return Collections.emptyList();
   }
}
