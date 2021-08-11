package org.apache.http.impl.cookie;

@Deprecated
public class BestMatchSpec extends DefaultCookieSpec {
   public BestMatchSpec() {
      this((String[])null, false);
   }

   public BestMatchSpec(String[] var1, boolean var2) {
      super(var1, var2);
   }

   public String toString() {
      return "best-match";
   }
}
