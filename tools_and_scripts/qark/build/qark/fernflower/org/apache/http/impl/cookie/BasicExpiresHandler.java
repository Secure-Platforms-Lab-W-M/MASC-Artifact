package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class BasicExpiresHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   private final String[] datepatterns;

   public BasicExpiresHandler(String[] var1) {
      Args.notNull(var1, "Array of date patterns");
      this.datepatterns = var1;
   }

   public String getAttributeName() {
      return "expires";
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 != null) {
         Date var3 = org.apache.http.client.utils.DateUtils.parseDate(var2, this.datepatterns);
         if (var3 != null) {
            var1.setExpiryDate(var3);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid 'expires' attribute: ");
            var4.append(var2);
            throw new MalformedCookieException(var4.toString());
         }
      } else {
         throw new MalformedCookieException("Missing value for 'expires' attribute");
      }
   }
}
