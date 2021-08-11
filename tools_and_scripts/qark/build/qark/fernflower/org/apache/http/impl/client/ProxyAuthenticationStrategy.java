package org.apache.http.impl.client;

import java.util.Collection;
import org.apache.http.client.config.RequestConfig;

public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {
   public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();

   public ProxyAuthenticationStrategy() {
      super(407, "Proxy-Authenticate");
   }

   Collection getPreferredAuthSchemes(RequestConfig var1) {
      return var1.getProxyPreferredAuthSchemes();
   }
}
