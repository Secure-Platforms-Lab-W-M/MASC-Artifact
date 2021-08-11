package org.apache.http.impl.client;

import java.net.ProxySelector;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;

@Deprecated
public class SystemDefaultHttpClient extends DefaultHttpClient {
   public SystemDefaultHttpClient() {
      super((ClientConnectionManager)null, (HttpParams)null);
   }

   public SystemDefaultHttpClient(HttpParams var1) {
      super((ClientConnectionManager)null, var1);
   }

   protected ClientConnectionManager createClientConnectionManager() {
      PoolingClientConnectionManager var2 = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
      if ("true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true"))) {
         int var1 = Integer.parseInt(System.getProperty("http.maxConnections", "5"));
         var2.setDefaultMaxPerRoute(var1);
         var2.setMaxTotal(var1 * 2);
      }

      return var2;
   }

   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
      return (ConnectionReuseStrategy)("true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true")) ? new DefaultConnectionReuseStrategy() : new NoConnectionReuseStrategy());
   }

   protected HttpRoutePlanner createHttpRoutePlanner() {
      return new ProxySelectorRoutePlanner(this.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
   }
}
