package org.apache.http.impl.cookie;

import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class BasicCommentHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   public String getAttributeName() {
      return "comment";
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      var1.setComment(var2);
   }
}
