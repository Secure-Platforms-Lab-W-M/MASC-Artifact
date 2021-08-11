package org.apache.http.impl.cookie;

import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   public CookieSpec create(HttpContext var1) {
      return new IgnoreSpec();
   }

   public CookieSpec newInstance(HttpParams var1) {
      return new IgnoreSpec();
   }
}
