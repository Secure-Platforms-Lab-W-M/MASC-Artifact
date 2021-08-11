package org.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
public class DefaultRequestDirector implements RequestDirector {
   private final HttpAuthenticator authenticator;
   protected final ClientConnectionManager connManager;
   private int execCount;
   protected final HttpProcessor httpProcessor;
   protected final ConnectionKeepAliveStrategy keepAliveStrategy;
   private final Log log;
   protected ManagedClientConnection managedConn;
   private final int maxRedirects;
   protected final HttpParams params;
   protected final AuthenticationHandler proxyAuthHandler;
   protected final AuthState proxyAuthState;
   protected final AuthenticationStrategy proxyAuthStrategy;
   private int redirectCount;
   protected final RedirectHandler redirectHandler;
   protected final RedirectStrategy redirectStrategy;
   protected final HttpRequestExecutor requestExec;
   protected final HttpRequestRetryHandler retryHandler;
   protected final ConnectionReuseStrategy reuseStrategy;
   protected final HttpRoutePlanner routePlanner;
   protected final AuthenticationHandler targetAuthHandler;
   protected final AuthState targetAuthState;
   protected final AuthenticationStrategy targetAuthStrategy;
   protected final UserTokenHandler userTokenHandler;
   private HttpHost virtualHost;

   public DefaultRequestDirector(Log var1, HttpRequestExecutor var2, ClientConnectionManager var3, ConnectionReuseStrategy var4, ConnectionKeepAliveStrategy var5, HttpRoutePlanner var6, HttpProcessor var7, HttpRequestRetryHandler var8, RedirectStrategy var9, AuthenticationHandler var10, AuthenticationHandler var11, UserTokenHandler var12, HttpParams var13) {
      this(LogFactory.getLog(DefaultRequestDirector.class), var2, var3, var4, var5, var6, var7, var8, var9, (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var10)), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var11)), var12, var13);
   }

   public DefaultRequestDirector(Log var1, HttpRequestExecutor var2, ClientConnectionManager var3, ConnectionReuseStrategy var4, ConnectionKeepAliveStrategy var5, HttpRoutePlanner var6, HttpProcessor var7, HttpRequestRetryHandler var8, RedirectStrategy var9, AuthenticationStrategy var10, AuthenticationStrategy var11, UserTokenHandler var12, HttpParams var13) {
      Args.notNull(var1, "Log");
      Args.notNull(var2, "Request executor");
      Args.notNull(var3, "Client connection manager");
      Args.notNull(var4, "Connection reuse strategy");
      Args.notNull(var5, "Connection keep alive strategy");
      Args.notNull(var6, "Route planner");
      Args.notNull(var7, "HTTP protocol processor");
      Args.notNull(var8, "HTTP request retry handler");
      Args.notNull(var9, "Redirect strategy");
      Args.notNull(var10, "Target authentication strategy");
      Args.notNull(var11, "Proxy authentication strategy");
      Args.notNull(var12, "User token handler");
      Args.notNull(var13, "HTTP parameters");
      this.log = var1;
      this.authenticator = new HttpAuthenticator(var1);
      this.requestExec = var2;
      this.connManager = var3;
      this.reuseStrategy = var4;
      this.keepAliveStrategy = var5;
      this.routePlanner = var6;
      this.httpProcessor = var7;
      this.retryHandler = var8;
      this.redirectStrategy = var9;
      this.targetAuthStrategy = var10;
      this.proxyAuthStrategy = var11;
      this.userTokenHandler = var12;
      this.params = var13;
      if (var9 instanceof DefaultRedirectStrategyAdaptor) {
         this.redirectHandler = ((DefaultRedirectStrategyAdaptor)var9).getHandler();
      } else {
         this.redirectHandler = null;
      }

      if (var10 instanceof AuthenticationStrategyAdaptor) {
         this.targetAuthHandler = ((AuthenticationStrategyAdaptor)var10).getHandler();
      } else {
         this.targetAuthHandler = null;
      }

      if (var11 instanceof AuthenticationStrategyAdaptor) {
         this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)var11).getHandler();
      } else {
         this.proxyAuthHandler = null;
      }

      this.managedConn = null;
      this.execCount = 0;
      this.redirectCount = 0;
      this.targetAuthState = new AuthState();
      this.proxyAuthState = new AuthState();
      this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
   }

   public DefaultRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectHandler var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      this(LogFactory.getLog(DefaultRequestDirector.class), var1, var2, var3, var4, var5, var6, var7, new DefaultRedirectStrategyAdaptor(var8), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var9)), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var10)), var11, var12);
   }

   private void abortConnection() {
      ManagedClientConnection var1 = this.managedConn;
      if (var1 != null) {
         this.managedConn = null;

         try {
            var1.abortConnection();
         } catch (IOException var3) {
            if (this.log.isDebugEnabled()) {
               this.log.debug(var3.getMessage(), var3);
            }
         }

         try {
            var1.releaseConnection();
            return;
         } catch (IOException var4) {
            this.log.debug("Error releasing connection", var4);
         }
      }

   }

   private void tryConnect(RoutedRequest var1, HttpContext var2) throws HttpException, IOException {
      HttpRoute var4 = var1.getRoute();
      RequestWrapper var12 = var1.getRequest();
      int var3 = 0;

      while(true) {
         var2.setAttribute("http.request", var12);
         ++var3;

         IOException var10000;
         label59: {
            boolean var10001;
            label51: {
               try {
                  if (!this.managedConn.isOpen()) {
                     this.managedConn.open(var4, var2, this.params);
                     break label51;
                  }
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label59;
               }

               try {
                  this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label59;
               }
            }

            try {
               this.establishRoute(var4, var2);
               return;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
            }
         }

         IOException var5 = var10000;

         try {
            this.managedConn.close();
         } catch (IOException var8) {
         }

         if (!this.retryHandler.retryRequest(var5, var3, var2)) {
            throw var5;
         }

         if (this.log.isInfoEnabled()) {
            Log var6 = this.log;
            StringBuilder var7 = new StringBuilder();
            var7.append("I/O exception (");
            var7.append(var5.getClass().getName());
            var7.append(") caught when connecting to ");
            var7.append(var4);
            var7.append(": ");
            var7.append(var5.getMessage());
            var6.info(var7.toString());
            if (this.log.isDebugEnabled()) {
               this.log.debug(var5.getMessage(), var5);
            }

            Log var13 = this.log;
            StringBuilder var14 = new StringBuilder();
            var14.append("Retrying connect to ");
            var14.append(var4);
            var13.info(var14.toString());
         }
      }
   }

   private HttpResponse tryExecute(RoutedRequest var1, HttpContext var2) throws HttpException, IOException {
      RequestWrapper var3 = var1.getRequest();
      HttpRoute var4 = var1.getRoute();
      IOException var12 = null;

      while(true) {
         ++this.execCount;
         var3.incrementExecCount();
         if (!var3.isRepeatable()) {
            this.log.debug("Cannot retry non-repeatable request");
            if (var12 != null) {
               throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", var12);
            }

            throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
         }

         IOException var10000;
         label81: {
            boolean var10001;
            label89: {
               try {
                  if (!this.managedConn.isOpen()) {
                     if (var4.isTunnelled()) {
                        break label89;
                     }

                     this.log.debug("Reopening the direct connection.");
                     this.managedConn.open(var4, var2, this.params);
                  }
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label81;
               }

               try {
                  if (this.log.isDebugEnabled()) {
                     Log var13 = this.log;
                     StringBuilder var5 = new StringBuilder();
                     var5.append("Attempt ");
                     var5.append(this.execCount);
                     var5.append(" to execute request");
                     var13.debug(var5.toString());
                  }
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label81;
               }

               try {
                  HttpResponse var14 = this.requestExec.execute(var3, this.managedConn, var2);
                  return var14;
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label81;
               }
            }

            try {
               this.log.debug("Proxied connection. Need to start over.");
               return null;
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         var12 = var10000;
         this.log.debug("Closing the connection.");

         try {
            this.managedConn.close();
         } catch (IOException var7) {
         }

         if (!this.retryHandler.retryRequest(var12, var3.getExecCount(), var2)) {
            if (var12 instanceof NoHttpResponseException) {
               StringBuilder var15 = new StringBuilder();
               var15.append(var4.getTargetHost().toHostString());
               var15.append(" failed to respond");
               NoHttpResponseException var16 = new NoHttpResponseException(var15.toString());
               var16.setStackTrace(var12.getStackTrace());
               throw var16;
            }

            throw var12;
         }

         StringBuilder var6;
         Log var17;
         if (this.log.isInfoEnabled()) {
            var17 = this.log;
            var6 = new StringBuilder();
            var6.append("I/O exception (");
            var6.append(var12.getClass().getName());
            var6.append(") caught when processing request to ");
            var6.append(var4);
            var6.append(": ");
            var6.append(var12.getMessage());
            var17.info(var6.toString());
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug(var12.getMessage(), var12);
         }

         if (this.log.isInfoEnabled()) {
            var17 = this.log;
            var6 = new StringBuilder();
            var6.append("Retrying request to ");
            var6.append(var4);
            var17.info(var6.toString());
         }
      }
   }

   private RequestWrapper wrapRequest(HttpRequest var1) throws ProtocolException {
      return (RequestWrapper)(var1 instanceof HttpEntityEnclosingRequest ? new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var1) : new RequestWrapper(var1));
   }

   protected HttpRequest createConnectRequest(HttpRoute var1, HttpContext var2) {
      HttpHost var6 = var1.getTargetHost();
      String var5 = var6.getHostName();
      int var4 = var6.getPort();
      int var3 = var4;
      if (var4 < 0) {
         var3 = this.connManager.getSchemeRegistry().getScheme(var6.getSchemeName()).getDefaultPort();
      }

      StringBuilder var7 = new StringBuilder(var5.length() + 6);
      var7.append(var5);
      var7.append(':');
      var7.append(Integer.toString(var3));
      return new BasicHttpRequest("CONNECT", var7.toString(), HttpProtocolParams.getVersion(this.params));
   }

   protected boolean createTunnelToProxy(HttpRoute var1, int var2, HttpContext var3) throws HttpException, IOException {
      throw new HttpException("Proxy chains are not supported.");
   }

   protected boolean createTunnelToTarget(HttpRoute var1, HttpContext var2) throws HttpException, IOException {
      HttpHost var4 = var1.getProxyHost();
      HttpHost var5 = var1.getTargetHost();

      while(true) {
         StringBuilder var7;
         HttpResponse var8;
         do {
            if (!this.managedConn.isOpen()) {
               this.managedConn.open(var1, var2, this.params);
            }

            HttpRequest var3 = this.createConnectRequest(var1, var2);
            var3.setParams(this.params);
            var2.setAttribute("http.target_host", var5);
            var2.setAttribute("http.route", var1);
            var2.setAttribute("http.proxy_host", var4);
            var2.setAttribute("http.connection", this.managedConn);
            var2.setAttribute("http.request", var3);
            this.requestExec.preProcess(var3, this.httpProcessor, var2);
            var8 = this.requestExec.execute(var3, this.managedConn, var2);
            var8.setParams(this.params);
            this.requestExec.postProcess(var8, this.httpProcessor, var2);
            if (var8.getStatusLine().getStatusCode() < 200) {
               var7 = new StringBuilder();
               var7.append("Unexpected response to CONNECT request: ");
               var7.append(var8.getStatusLine());
               throw new HttpException(var7.toString());
            }
         } while(!HttpClientParams.isAuthenticating(this.params));

         if (!this.authenticator.isAuthenticationRequested(var4, var8, this.proxyAuthStrategy, this.proxyAuthState, var2) || !this.authenticator.authenticate(var4, var8, this.proxyAuthStrategy, this.proxyAuthState, var2)) {
            if (var8.getStatusLine().getStatusCode() > 299) {
               HttpEntity var6 = var8.getEntity();
               if (var6 != null) {
                  var8.setEntity(new BufferedHttpEntity(var6));
               }

               this.managedConn.close();
               var7 = new StringBuilder();
               var7.append("CONNECT refused by proxy: ");
               var7.append(var8.getStatusLine());
               throw new TunnelRefusedException(var7.toString(), var8);
            } else {
               this.managedConn.markReusable();
               return false;
            }
         }

         if (this.reuseStrategy.keepAlive(var8, var2)) {
            this.log.debug("Connection kept alive");
            EntityUtils.consume(var8.getEntity());
         } else {
            this.managedConn.close();
         }
      }
   }

   protected HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      HttpRoutePlanner var4 = this.routePlanner;
      if (var1 == null) {
         var1 = (HttpHost)var2.getParams().getParameter("http.default-host");
      }

      return var4.determineRoute(var1, var2, var3);
   }

   protected void establishRoute(HttpRoute var1, HttpContext var2) throws HttpException, IOException {
      BasicRouteDirector var7 = new BasicRouteDirector();

      int var3;
      do {
         HttpRoute var6 = this.managedConn.getRoute();
         var3 = var7.nextStep(var1, var6);
         boolean var5;
         switch(var3) {
         case -1:
            StringBuilder var9 = new StringBuilder();
            var9.append("Unable to establish route: planned = ");
            var9.append(var1);
            var9.append("; current = ");
            var9.append(var6);
            throw new HttpException(var9.toString());
         case 0:
            break;
         case 1:
         case 2:
            this.managedConn.open(var1, var2, this.params);
            break;
         case 3:
            var5 = this.createTunnelToTarget(var1, var2);
            this.log.debug("Tunnel to target created.");
            this.managedConn.tunnelTarget(var5, this.params);
            break;
         case 4:
            int var4 = var6.getHopCount() - 1;
            var5 = this.createTunnelToProxy(var1, var4, var2);
            this.log.debug("Tunnel to proxy created.");
            this.managedConn.tunnelProxy(var1.getHopTarget(var4), var5, this.params);
            break;
         case 5:
            this.managedConn.layerProtocol(var2, this.params);
            break;
         default:
            StringBuilder var8 = new StringBuilder();
            var8.append("Unknown step indicator ");
            var8.append(var3);
            var8.append(" from RouteDirector.");
            throw new IllegalStateException(var8.toString());
         }
      } while(var3 > 0);

   }

   public HttpResponse execute(HttpHost param1, HttpRequest param2, HttpContext param3) throws HttpException, IOException {
      // $FF: Couldn't be decompiled
   }

   protected RoutedRequest handleResponse(RoutedRequest var1, HttpResponse var2, HttpContext var3) throws HttpException, IOException {
      HttpRoute var10 = var1.getRoute();
      RequestWrapper var11 = var1.getRequest();
      HttpParams var9 = var11.getParams();
      HttpHost var7;
      if (HttpClientParams.isAuthenticating(var9)) {
         HttpHost var8 = (HttpHost)var3.getAttribute("http.target_host");
         var7 = var8;
         if (var8 == null) {
            var7 = var10.getTargetHost();
         }

         if (var7.getPort() < 0) {
            Scheme var19 = this.connManager.getSchemeRegistry().getScheme(var7);
            var7 = new HttpHost(var7.getHostName(), var19.getDefaultPort(), var7.getSchemeName());
         }

         boolean var5 = this.authenticator.isAuthenticationRequested(var7, var2, this.targetAuthStrategy, this.targetAuthState, var3);
         var8 = var10.getProxyHost();
         if (var8 == null) {
            var8 = var10.getTargetHost();
         }

         boolean var6 = this.authenticator.isAuthenticationRequested(var8, var2, this.proxyAuthStrategy, this.proxyAuthState, var3);
         if (var5 && this.authenticator.authenticate(var7, var2, this.targetAuthStrategy, this.targetAuthState, var3)) {
            return var1;
         }

         if (var6 && this.authenticator.authenticate(var8, var2, this.proxyAuthStrategy, this.proxyAuthState, var3)) {
            return var1;
         }
      }

      if (HttpClientParams.isRedirecting(var9) && this.redirectStrategy.isRedirected(var11, var2, var3)) {
         int var4 = this.redirectCount;
         if (var4 < this.maxRedirects) {
            this.redirectCount = var4 + 1;
            this.virtualHost = null;
            HttpUriRequest var14 = this.redirectStrategy.getRedirect(var11, var2, var3);
            var14.setHeaders(var11.getOriginal().getAllHeaders());
            URI var13 = var14.getURI();
            var7 = URIUtils.extractHost(var13);
            if (var7 != null) {
               if (!var10.getTargetHost().equals(var7)) {
                  this.log.debug("Resetting target auth state");
                  this.targetAuthState.reset();
                  AuthScheme var21 = this.proxyAuthState.getAuthScheme();
                  if (var21 != null && var21.isConnectionBased()) {
                     this.log.debug("Resetting proxy auth state");
                     this.proxyAuthState.reset();
                  }
               }

               RequestWrapper var16 = this.wrapRequest(var14);
               var16.setParams(var9);
               HttpRoute var17 = this.determineRoute(var7, var16, var3);
               RoutedRequest var18 = new RoutedRequest(var16, var17);
               if (this.log.isDebugEnabled()) {
                  Log var20 = this.log;
                  StringBuilder var22 = new StringBuilder();
                  var22.append("Redirecting to '");
                  var22.append(var13);
                  var22.append("' via ");
                  var22.append(var17);
                  var20.debug(var22.toString());
               }

               return var18;
            } else {
               StringBuilder var15 = new StringBuilder();
               var15.append("Redirect URI does not specify a valid host name: ");
               var15.append(var13);
               throw new ProtocolException(var15.toString());
            }
         } else {
            StringBuilder var12 = new StringBuilder();
            var12.append("Maximum redirects (");
            var12.append(this.maxRedirects);
            var12.append(") exceeded");
            throw new RedirectException(var12.toString());
         }
      } else {
         return null;
      }
   }

   protected void releaseConnection() {
      try {
         this.managedConn.releaseConnection();
      } catch (IOException var2) {
         this.log.debug("IOException releasing connection", var2);
      }

      this.managedConn = null;
   }

   protected void rewriteRequestURI(RequestWrapper param1, HttpRoute param2) throws ProtocolException {
      // $FF: Couldn't be decompiled
   }
}
