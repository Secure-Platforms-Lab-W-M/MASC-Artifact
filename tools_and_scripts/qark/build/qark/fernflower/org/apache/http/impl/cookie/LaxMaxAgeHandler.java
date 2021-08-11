package org.apache.http.impl.cookie;

import java.util.Date;
import java.util.regex.Pattern;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class LaxMaxAgeHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   private static final Pattern MAX_AGE_PATTERN = Pattern.compile("^\\-?[0-9]+$");

   public String getAttributeName() {
      return "max-age";
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (!TextUtils.isBlank(var2)) {
         if (MAX_AGE_PATTERN.matcher(var2).matches()) {
            int var3;
            try {
               var3 = Integer.parseInt(var2);
            } catch (NumberFormatException var4) {
               return;
            }

            Date var5;
            if (var3 >= 0) {
               var5 = new Date(System.currentTimeMillis() + (long)var3 * 1000L);
            } else {
               var5 = new Date(Long.MIN_VALUE);
            }

            var1.setExpiryDate(var5);
         }
      }
   }
}
