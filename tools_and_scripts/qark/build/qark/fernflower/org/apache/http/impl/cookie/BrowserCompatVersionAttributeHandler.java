package org.apache.http.impl.cookie;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Deprecated
public class BrowserCompatVersionAttributeHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   public String getAttributeName() {
      return "version";
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 != null) {
         int var3 = 0;

         label15: {
            int var4;
            try {
               var4 = Integer.parseInt(var2);
            } catch (NumberFormatException var5) {
               break label15;
            }

            var3 = var4;
         }

         var1.setVersion(var3);
      } else {
         throw new MalformedCookieException("Missing value for version attribute");
      }
   }
}
