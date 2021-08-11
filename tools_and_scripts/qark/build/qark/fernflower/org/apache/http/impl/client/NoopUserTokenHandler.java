package org.apache.http.impl.client;

import org.apache.http.client.UserTokenHandler;
import org.apache.http.protocol.HttpContext;

public class NoopUserTokenHandler implements UserTokenHandler {
   public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();

   public Object getUserToken(HttpContext var1) {
      return null;
   }
}
