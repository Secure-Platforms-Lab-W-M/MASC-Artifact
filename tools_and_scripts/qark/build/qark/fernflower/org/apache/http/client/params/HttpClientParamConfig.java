package org.apache.http.client.params;

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.params.HttpParams;

@Deprecated
public final class HttpClientParamConfig {
   private HttpClientParamConfig() {
   }

   public static RequestConfig getRequestConfig(HttpParams var0) {
      return getRequestConfig(var0, RequestConfig.DEFAULT);
   }

   public static RequestConfig getRequestConfig(HttpParams var0, RequestConfig var1) {
      RequestConfig.Builder var4 = RequestConfig.copy(var1).setSocketTimeout(var0.getIntParameter("http.socket.timeout", var1.getSocketTimeout())).setStaleConnectionCheckEnabled(var0.getBooleanParameter("http.connection.stalecheck", var1.isStaleConnectionCheckEnabled())).setConnectTimeout(var0.getIntParameter("http.connection.timeout", var1.getConnectTimeout())).setExpectContinueEnabled(var0.getBooleanParameter("http.protocol.expect-continue", var1.isExpectContinueEnabled())).setAuthenticationEnabled(var0.getBooleanParameter("http.protocol.handle-authentication", var1.isAuthenticationEnabled())).setCircularRedirectsAllowed(var0.getBooleanParameter("http.protocol.allow-circular-redirects", var1.isCircularRedirectsAllowed())).setConnectionRequestTimeout((int)var0.getLongParameter("http.conn-manager.timeout", (long)var1.getConnectionRequestTimeout())).setMaxRedirects(var0.getIntParameter("http.protocol.max-redirects", var1.getMaxRedirects())).setRedirectsEnabled(var0.getBooleanParameter("http.protocol.handle-redirects", var1.isRedirectsEnabled())).setRelativeRedirectsAllowed(var0.getBooleanParameter("http.protocol.reject-relative-redirect", var1.isRelativeRedirectsAllowed() ^ true) ^ true);
      HttpHost var2 = (HttpHost)var0.getParameter("http.route.default-proxy");
      if (var2 != null) {
         var4.setProxy(var2);
      }

      InetAddress var5 = (InetAddress)var0.getParameter("http.route.local-address");
      if (var5 != null) {
         var4.setLocalAddress(var5);
      }

      Collection var6 = (Collection)var0.getParameter("http.auth.target-scheme-pref");
      if (var6 != null) {
         var4.setTargetPreferredAuthSchemes(var6);
      }

      var6 = (Collection)var0.getParameter("http.auth.proxy-scheme-pref");
      if (var6 != null) {
         var4.setProxyPreferredAuthSchemes(var6);
      }

      String var3 = (String)var0.getParameter("http.protocol.cookie-policy");
      if (var3 != null) {
         var4.setCookieSpec(var3);
      }

      return var4.build();
   }
}
