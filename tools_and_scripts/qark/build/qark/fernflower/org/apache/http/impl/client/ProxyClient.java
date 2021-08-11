package org.apache.http.impl.client;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class ProxyClient {
   private final AuthSchemeRegistry authSchemeRegistry;
   private final org.apache.http.impl.auth.HttpAuthenticator authenticator;
   private final HttpConnectionFactory connFactory;
   private final ConnectionConfig connectionConfig;
   private final HttpProcessor httpProcessor;
   private final AuthState proxyAuthState;
   private final ProxyAuthenticationStrategy proxyAuthStrategy;
   private final RequestConfig requestConfig;
   private final HttpRequestExecutor requestExec;
   private final ConnectionReuseStrategy reuseStrategy;

   public ProxyClient() {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, (RequestConfig)null);
   }

   public ProxyClient(RequestConfig var1) {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, var1);
   }

   public ProxyClient(HttpConnectionFactory var1, ConnectionConfig var2, RequestConfig var3) {
      if (var1 == null) {
         var1 = ManagedHttpClientConnectionFactory.INSTANCE;
      }

      this.connFactory = (HttpConnectionFactory)var1;
      if (var2 == null) {
         var2 = ConnectionConfig.DEFAULT;
      }

      this.connectionConfig = var2;
      if (var3 == null) {
         var3 = RequestConfig.DEFAULT;
      }

      this.requestConfig = var3;
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent()});
      this.requestExec = new HttpRequestExecutor();
      this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
      this.authenticator = new org.apache.http.impl.auth.HttpAuthenticator();
      this.proxyAuthState = new AuthState();
      AuthSchemeRegistry var4 = new AuthSchemeRegistry();
      this.authSchemeRegistry = var4;
      var4.register("Basic", new BasicSchemeFactory());
      this.authSchemeRegistry.register("Digest", new DigestSchemeFactory());
      this.authSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
      this.authSchemeRegistry.register("Negotiate", new SPNegoSchemeFactory());
      this.authSchemeRegistry.register("Kerberos", new KerberosSchemeFactory());
      this.reuseStrategy = new DefaultConnectionReuseStrategy();
   }

   @Deprecated
   public ProxyClient(HttpParams var1) {
      this((HttpConnectionFactory)null, HttpParamConfig.getConnectionConfig(var1), HttpClientParamConfig.getRequestConfig(var1));
   }

   @Deprecated
   public AuthSchemeRegistry getAuthSchemeRegistry() {
      return this.authSchemeRegistry;
   }

   @Deprecated
   public HttpParams getParams() {
      return new BasicHttpParams();
   }

   public Socket tunnel(HttpHost var1, HttpHost var2, Credentials var3) throws IOException, HttpException {
      Args.notNull(var1, "Proxy host");
      Args.notNull(var2, "Target host");
      Args.notNull(var3, "Credentials");
      HttpHost var4;
      if (var2.getPort() <= 0) {
         var4 = new HttpHost(var2.getHostName(), 80, var2.getSchemeName());
      } else {
         var4 = var2;
      }

      HttpRoute var7 = new HttpRoute(var4, this.requestConfig.getLocalAddress(), var1, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);
      ManagedHttpClientConnection var5 = (ManagedHttpClientConnection)this.connFactory.create(var7, this.connectionConfig);
      BasicHttpContext var6 = new BasicHttpContext();
      BasicHttpRequest var12 = new BasicHttpRequest("CONNECT", var4.toHostString(), HttpVersion.HTTP_1_1);
      BasicCredentialsProvider var8 = new BasicCredentialsProvider();
      var8.setCredentials(new AuthScope(var1), var3);
      var6.setAttribute("http.target_host", var2);
      var6.setAttribute("http.connection", var5);
      var6.setAttribute("http.request", var12);
      var6.setAttribute("http.route", var7);
      var6.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
      var6.setAttribute("http.auth.credentials-provider", var8);
      var6.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
      var6.setAttribute("http.request-config", this.requestConfig);
      this.requestExec.preProcess(var12, this.httpProcessor, var6);

      while(true) {
         if (!var5.isOpen()) {
            var5.bind(new Socket(var1.getHostName(), var1.getPort()));
         }

         this.authenticator.generateAuthResponse(var12, this.proxyAuthState, var6);
         HttpResponse var11 = this.requestExec.execute(var12, var5, var6);
         StringBuilder var10;
         if (var11.getStatusLine().getStatusCode() < 200) {
            var10 = new StringBuilder();
            var10.append("Unexpected response to CONNECT request: ");
            var10.append(var11.getStatusLine());
            throw new HttpException(var10.toString());
         }

         if (!this.authenticator.isAuthenticationRequested(var1, var11, this.proxyAuthStrategy, this.proxyAuthState, var6) || !this.authenticator.handleAuthChallenge(var1, var11, this.proxyAuthStrategy, this.proxyAuthState, var6)) {
            if (var11.getStatusLine().getStatusCode() > 299) {
               HttpEntity var9 = var11.getEntity();
               if (var9 != null) {
                  var11.setEntity(new BufferedHttpEntity(var9));
               }

               var5.close();
               var10 = new StringBuilder();
               var10.append("CONNECT refused by proxy: ");
               var10.append(var11.getStatusLine());
               throw new org.apache.http.impl.execchain.TunnelRefusedException(var10.toString(), var11);
            } else {
               return var5.getSocket();
            }
         }

         if (this.reuseStrategy.keepAlive(var11, var6)) {
            EntityUtils.consume(var11.getEntity());
         } else {
            var5.close();
         }

         var12.removeHeaders("Proxy-Authorization");
      }
   }
}
