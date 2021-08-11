package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

@Deprecated
public class PublicSuffixFilter implements CookieAttributeHandler {
   private Collection exceptions;
   private PublicSuffixMatcher matcher;
   private Collection suffixes;
   private final CookieAttributeHandler wrapped;

   public PublicSuffixFilter(CookieAttributeHandler var1) {
      this.wrapped = var1;
   }

   private boolean isForPublicSuffix(Cookie var1) {
      if (this.matcher == null) {
         this.matcher = new PublicSuffixMatcher(this.suffixes, this.exceptions);
      }

      return this.matcher.matches(var1.getDomain());
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return this.isForPublicSuffix(var1) ? false : this.wrapped.match(var1, var2);
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      this.wrapped.parse(var1, var2);
   }

   public void setExceptions(Collection var1) {
      this.exceptions = var1;
      this.matcher = null;
   }

   public void setPublicSuffixes(Collection var1) {
      this.suffixes = var1;
      this.matcher = null;
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      this.wrapped.validate(var1, var2);
   }
}
