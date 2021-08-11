package org.apache.http.impl.client;

public class LaxRedirectStrategy extends DefaultRedirectStrategy {
   public static final LaxRedirectStrategy INSTANCE = new LaxRedirectStrategy();

   public LaxRedirectStrategy() {
      super(new String[]{"GET", "POST", "HEAD", "DELETE"});
   }
}
