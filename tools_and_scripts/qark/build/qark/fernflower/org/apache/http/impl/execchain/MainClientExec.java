package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class MainClientExec implements ClientExecChain {
   private final HttpAuthenticator authenticator;
   private final HttpClientConnectionManager connManager;
   private final ConnectionKeepAliveStrategy keepAliveStrategy;
   private final Log log;
   private final AuthenticationStrategy proxyAuthStrategy;
   private final HttpProcessor proxyHttpProcessor;
   private final HttpRequestExecutor requestExecutor;
   private final ConnectionReuseStrategy reuseStrategy;
   private final HttpRouteDirector routeDirector;
   private final AuthenticationStrategy targetAuthStrategy;
   private final UserTokenHandler userTokenHandler;

   public MainClientExec(HttpRequestExecutor var1, HttpClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, AuthenticationStrategy var5, AuthenticationStrategy var6, UserTokenHandler var7) {
      this(var1, var2, var3, var4, new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestTargetHost()}), var5, var6, var7);
   }

   public MainClientExec(HttpRequestExecutor var1, HttpClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpProcessor var5, AuthenticationStrategy var6, AuthenticationStrategy var7, UserTokenHandler var8) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "Client connection manager");
      Args.notNull(var3, "Connection reuse strategy");
      Args.notNull(var4, "Connection keep alive strategy");
      Args.notNull(var5, "Proxy HTTP processor");
      Args.notNull(var6, "Target authentication strategy");
      Args.notNull(var7, "Proxy authentication strategy");
      Args.notNull(var8, "User token handler");
      this.authenticator = new HttpAuthenticator();
      this.routeDirector = new BasicRouteDirector();
      this.requestExecutor = var1;
      this.connManager = var2;
      this.reuseStrategy = var3;
      this.keepAliveStrategy = var4;
      this.proxyHttpProcessor = var5;
      this.targetAuthStrategy = var6;
      this.proxyAuthStrategy = var7;
      this.userTokenHandler = var8;
   }

   private boolean createTunnelToProxy(HttpRoute var1, int var2, HttpClientContext var3) throws HttpException {
      throw new HttpException("Proxy chains are not supported.");
   }

   private boolean createTunnelToTarget(AuthState var1, HttpClientConnection var2, HttpRoute var3, HttpRequest var4, HttpClientContext var5) throws HttpException, IOException {
      RequestConfig var9 = var5.getRequestConfig();
      int var7 = var9.getConnectTimeout();
      HttpHost var11 = var3.getTargetHost();
      HttpHost var10 = var3.getProxyHost();
      Object var8 = null;
      BasicHttpRequest var16 = new BasicHttpRequest("CONNECT", var11.toHostString(), var4.getProtocolVersion());
      this.requestExecutor.preProcess(var16, this.proxyHttpProcessor, var5);
      HttpResponse var14 = (HttpResponse)var8;

      while(true) {
         int var6 = 0;
         StringBuilder var12;
         if (var14 != null) {
            if (var14.getStatusLine().getStatusCode() > 299) {
               HttpEntity var13 = var14.getEntity();
               if (var13 != null) {
                  var14.setEntity(new BufferedHttpEntity(var13));
               }

               var2.close();
               var12 = new StringBuilder();
               var12.append("CONNECT refused by proxy: ");
               var12.append(var14.getStatusLine());
               throw new TunnelRefusedException(var12.toString(), var14);
            }

            return false;
         }

         if (!var2.isOpen()) {
            HttpClientConnectionManager var15 = this.connManager;
            if (var7 > 0) {
               var6 = var7;
            }

            var15.connect(var2, var3, var6, var5);
         }

         var16.removeHeaders("Proxy-Authorization");
         this.authenticator.generateAuthResponse(var16, var1, var5);
         var14 = this.requestExecutor.execute(var16, var2, var5);
         this.requestExecutor.postProcess(var14, this.proxyHttpProcessor, var5);
         if (var14.getStatusLine().getStatusCode() < 200) {
            var12 = new StringBuilder();
            var12.append("Unexpected response to CONNECT request: ");
            var12.append(var14.getStatusLine());
            throw new HttpException(var12.toString());
         }

         if (var9.isAuthenticationEnabled() && this.authenticator.isAuthenticationRequested(var10, var14, this.proxyAuthStrategy, var1, var5) && this.authenticator.handleAuthChallenge(var10, var14, this.proxyAuthStrategy, var1, var5)) {
            if (this.reuseStrategy.keepAlive(var14, var5)) {
               this.log.debug("Connection kept alive");
               EntityUtils.consume(var14.getEntity());
            } else {
               var2.close();
            }

            var14 = null;
         }
      }
   }

   private boolean needAuthentication(AuthState var1, AuthState var2, HttpRoute var3, HttpResponse var4, HttpClientContext var5) {
      if (var5.getRequestConfig().isAuthenticationEnabled()) {
         HttpHost var9 = var5.getTargetHost();
         HttpHost var8 = var9;
         if (var9 == null) {
            var8 = var3.getTargetHost();
         }

         var9 = var8;
         if (var8.getPort() < 0) {
            var9 = new HttpHost(var8.getHostName(), var3.getTargetHost().getPort(), var8.getSchemeName());
         }

         boolean var6 = this.authenticator.isAuthenticationRequested(var9, var4, this.targetAuthStrategy, var1, var5);
         var8 = var3.getProxyHost();
         HttpHost var10;
         if (var8 == null) {
            var10 = var3.getTargetHost();
         } else {
            var10 = var8;
         }

         boolean var7 = this.authenticator.isAuthenticationRequested(var10, var4, this.proxyAuthStrategy, var2, var5);
         if (var6) {
            return this.authenticator.handleAuthChallenge(var9, var4, this.targetAuthStrategy, var1, var5);
         }

         if (var7) {
            return this.authenticator.handleAuthChallenge(var10, var4, this.proxyAuthStrategy, var2, var5);
         }
      }

      return false;
   }

   void establishRoute(AuthState var1, HttpClientConnection var2, HttpRoute var3, HttpRequest var4, HttpClientContext var5) throws HttpException, IOException {
      int var6 = var5.getRequestConfig().getConnectTimeout();
      RouteTracker var10 = new RouteTracker(var3);

      int var8;
      do {
         HttpRoute var11 = var10.toRoute();
         var8 = this.routeDirector.nextStep(var3, var11);
         boolean var9 = true;
         int var7 = 0;
         StringBuilder var12;
         HttpClientConnectionManager var13;
         switch(var8) {
         case -1:
            var12 = new StringBuilder();
            var12.append("Unable to establish route: planned = ");
            var12.append(var3);
            var12.append("; current = ");
            var12.append(var11);
            throw new HttpException(var12.toString());
         case 0:
            this.connManager.routeComplete(var2, var3, var5);
            break;
         case 1:
            var13 = this.connManager;
            if (var6 > 0) {
               var7 = var6;
            }

            var13.connect(var2, var3, var7, var5);
            var10.connectTarget(var3.isSecure());
            break;
         case 2:
            var13 = this.connManager;
            if (var6 > 0) {
               var7 = var6;
            } else {
               var7 = 0;
            }

            var13.connect(var2, var3, var7, var5);
            HttpHost var14 = var3.getProxyHost();
            if (!var3.isSecure() || var3.isTunnelled()) {
               var9 = false;
            }

            var10.connectProxy(var14, var9);
            break;
         case 3:
            var9 = this.createTunnelToTarget(var1, var2, var3, var4, var5);
            this.log.debug("Tunnel to target created.");
            var10.tunnelTarget(var9);
            break;
         case 4:
            var7 = var11.getHopCount() - 1;
            var9 = this.createTunnelToProxy(var3, var7, var5);
            this.log.debug("Tunnel to proxy created.");
            var10.tunnelProxy(var3.getHopTarget(var7), var9);
            break;
         case 5:
            this.connManager.upgrade(var2, var3, var5);
            var10.layerProtocol(var3.isSecure());
            break;
         default:
            var12 = new StringBuilder();
            var12.append("Unknown step indicator ");
            var12.append(var8);
            var12.append(" from RouteDirector.");
            throw new IllegalStateException(var12.toString());
         }
      } while(var8 > 0);

   }

   public CloseableHttpResponse execute(HttpRoute param1, HttpRequestWrapper param2, HttpClientContext param3, HttpExecutionAware param4) throws IOException, HttpException {
      // $FF: Couldn't be decompiled
   }
}
