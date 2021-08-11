package org.apache.http.impl.client;

import java.util.Collection;
import org.apache.http.client.config.RequestConfig;

public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {
   public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

   public TargetAuthenticationStrategy() {
      super(401, "WWW-Authenticate");
   }

   Collection getPreferredAuthSchemes(RequestConfig var1) {
      return var1.getTargetPreferredAuthSchemes();
   }
}
