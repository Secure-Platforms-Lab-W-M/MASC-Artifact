package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;

@Deprecated
public abstract class AbstractHttpClient extends CloseableHttpClient {
   private BackoffManager backoffManager;
   private ClientConnectionManager connManager;
   private ConnectionBackoffStrategy connectionBackoffStrategy;
   private CookieStore cookieStore;
   private CredentialsProvider credsProvider;
   private HttpParams defaultParams;
   private ConnectionKeepAliveStrategy keepAliveStrategy;
   private final Log log = LogFactory.getLog(this.getClass());
   private BasicHttpProcessor mutableProcessor;
   private ImmutableHttpProcessor protocolProcessor;
   private AuthenticationStrategy proxyAuthStrategy;
   private RedirectStrategy redirectStrategy;
   private HttpRequestExecutor requestExec;
   private HttpRequestRetryHandler retryHandler;
   private ConnectionReuseStrategy reuseStrategy;
   private HttpRoutePlanner routePlanner;
   private AuthSchemeRegistry supportedAuthSchemes;
   private CookieSpecRegistry supportedCookieSpecs;
   private AuthenticationStrategy targetAuthStrategy;
   private UserTokenHandler userTokenHandler;

   protected AbstractHttpClient(ClientConnectionManager var1, HttpParams var2) {
      this.defaultParams = var2;
      this.connManager = var1;
   }

   private HttpProcessor getProtocolProcessor() {
      synchronized(this){}

      Throwable var10000;
      label414: {
         boolean var10001;
         label419: {
            int var2;
            BasicHttpProcessor var3;
            HttpRequestInterceptor[] var4;
            try {
               if (this.protocolProcessor != null) {
                  break label419;
               }

               var3 = this.getHttpProcessor();
               var2 = var3.getRequestInterceptorCount();
               var4 = new HttpRequestInterceptor[var2];
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label414;
            }

            int var1;
            for(var1 = 0; var1 < var2; ++var1) {
               try {
                  var4[var1] = var3.getRequestInterceptor(var1);
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label414;
               }
            }

            HttpResponseInterceptor[] var5;
            try {
               var2 = var3.getResponseInterceptorCount();
               var5 = new HttpResponseInterceptor[var2];
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label414;
            }

            var1 = 0;

            while(true) {
               if (var1 >= var2) {
                  try {
                     this.protocolProcessor = new ImmutableHttpProcessor(var4, var5);
                     break;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label414;
                  }
               }

               try {
                  var5[var1] = var3.getResponseInterceptor(var1);
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label414;
               }

               ++var1;
            }
         }

         label389:
         try {
            ImmutableHttpProcessor var49 = this.protocolProcessor;
            return var49;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label389;
         }
      }

      Throwable var48 = var10000;
      throw var48;
   }

   public void addRequestInterceptor(HttpRequestInterceptor var1) {
      synchronized(this){}

      try {
         this.getHttpProcessor().addInterceptor(var1);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void addRequestInterceptor(HttpRequestInterceptor var1, int var2) {
      synchronized(this){}

      try {
         this.getHttpProcessor().addInterceptor(var1, var2);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void addResponseInterceptor(HttpResponseInterceptor var1) {
      synchronized(this){}

      try {
         this.getHttpProcessor().addInterceptor(var1);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void addResponseInterceptor(HttpResponseInterceptor var1, int var2) {
      synchronized(this){}

      try {
         this.getHttpProcessor().addInterceptor(var1, var2);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void clearRequestInterceptors() {
      synchronized(this){}

      try {
         this.getHttpProcessor().clearRequestInterceptors();
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void clearResponseInterceptors() {
      synchronized(this){}

      try {
         this.getHttpProcessor().clearResponseInterceptors();
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void close() {
      this.getConnectionManager().shutdown();
   }

   protected AuthSchemeRegistry createAuthSchemeRegistry() {
      AuthSchemeRegistry var1 = new AuthSchemeRegistry();
      var1.register("Basic", new BasicSchemeFactory());
      var1.register("Digest", new DigestSchemeFactory());
      var1.register("NTLM", new NTLMSchemeFactory());
      var1.register("Negotiate", new SPNegoSchemeFactory());
      var1.register("Kerberos", new KerberosSchemeFactory());
      return var1;
   }

   protected ClientConnectionManager createClientConnectionManager() {
      SchemeRegistry var3 = SchemeRegistryFactory.createDefault();
      HttpParams var4 = this.getParams();
      ClientConnectionManagerFactory var1 = null;
      String var2 = (String)var4.getParameter("http.connection-manager.factory-class-name");
      ClassLoader var5 = Thread.currentThread().getContextClassLoader();
      if (var2 != null) {
         label52: {
            IllegalAccessException var19;
            label51: {
               InstantiationException var10000;
               label50: {
                  Class var15;
                  boolean var10001;
                  if (var5 != null) {
                     try {
                        var15 = Class.forName(var2, true, var5);
                     } catch (ClassNotFoundException var12) {
                        var10001 = false;
                        break label52;
                     } catch (IllegalAccessException var13) {
                        var19 = var13;
                        var10001 = false;
                        break label51;
                     } catch (InstantiationException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label50;
                     }
                  } else {
                     try {
                        var15 = Class.forName(var2);
                     } catch (ClassNotFoundException var9) {
                        var10001 = false;
                        break label52;
                     } catch (IllegalAccessException var10) {
                        var19 = var10;
                        var10001 = false;
                        break label51;
                     } catch (InstantiationException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label50;
                     }
                  }

                  try {
                     var1 = (ClientConnectionManagerFactory)var15.newInstance();
                     return (ClientConnectionManager)(var1 != null ? var1.newInstance(var4, var3) : new BasicClientConnectionManager(var3));
                  } catch (ClassNotFoundException var6) {
                     var10001 = false;
                     break label52;
                  } catch (IllegalAccessException var7) {
                     var19 = var7;
                     var10001 = false;
                     break label51;
                  } catch (InstantiationException var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               InstantiationException var16 = var10000;
               throw new InstantiationError(var16.getMessage());
            }

            IllegalAccessException var17 = var19;
            throw new IllegalAccessError(var17.getMessage());
         }

         StringBuilder var18 = new StringBuilder();
         var18.append("Invalid class name: ");
         var18.append(var2);
         throw new IllegalStateException(var18.toString());
      } else {
         return (ClientConnectionManager)(var1 != null ? var1.newInstance(var4, var3) : new BasicClientConnectionManager(var3));
      }
   }

   @Deprecated
   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectHandler var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   @Deprecated
   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectStrategy var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(this.log, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectStrategy var8, AuthenticationStrategy var9, AuthenticationStrategy var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(this.log, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
      return new DefaultConnectionKeepAliveStrategy();
   }

   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
      return new DefaultConnectionReuseStrategy();
   }

   protected CookieSpecRegistry createCookieSpecRegistry() {
      CookieSpecRegistry var1 = new CookieSpecRegistry();
      var1.register("default", new BestMatchSpecFactory());
      var1.register("best-match", new BestMatchSpecFactory());
      var1.register("compatibility", new BrowserCompatSpecFactory());
      var1.register("netscape", new NetscapeDraftSpecFactory());
      var1.register("rfc2109", new RFC2109SpecFactory());
      var1.register("rfc2965", new RFC2965SpecFactory());
      var1.register("ignoreCookies", new IgnoreSpecFactory());
      return var1;
   }

   protected CookieStore createCookieStore() {
      return new BasicCookieStore();
   }

   protected CredentialsProvider createCredentialsProvider() {
      return new BasicCredentialsProvider();
   }

   protected HttpContext createHttpContext() {
      BasicHttpContext var1 = new BasicHttpContext();
      var1.setAttribute("http.scheme-registry", this.getConnectionManager().getSchemeRegistry());
      var1.setAttribute("http.authscheme-registry", this.getAuthSchemes());
      var1.setAttribute("http.cookiespec-registry", this.getCookieSpecs());
      var1.setAttribute("http.cookie-store", this.getCookieStore());
      var1.setAttribute("http.auth.credentials-provider", this.getCredentialsProvider());
      return var1;
   }

   protected abstract HttpParams createHttpParams();

   protected abstract BasicHttpProcessor createHttpProcessor();

   protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
      return new DefaultHttpRequestRetryHandler();
   }

   protected HttpRoutePlanner createHttpRoutePlanner() {
      return new DefaultHttpRoutePlanner(this.getConnectionManager().getSchemeRegistry());
   }

   @Deprecated
   protected AuthenticationHandler createProxyAuthenticationHandler() {
      return new DefaultProxyAuthenticationHandler();
   }

   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
      return new ProxyAuthenticationStrategy();
   }

   @Deprecated
   protected RedirectHandler createRedirectHandler() {
      return new DefaultRedirectHandler();
   }

   protected HttpRequestExecutor createRequestExecutor() {
      return new HttpRequestExecutor();
   }

   @Deprecated
   protected AuthenticationHandler createTargetAuthenticationHandler() {
      return new DefaultTargetAuthenticationHandler();
   }

   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
      return new TargetAuthenticationStrategy();
   }

   protected UserTokenHandler createUserTokenHandler() {
      return new DefaultUserTokenHandler();
   }

   protected HttpParams determineParams(HttpRequest var1) {
      return new ClientParamsStack((HttpParams)null, this.getParams(), var1.getParams(), (HttpParams)null);
   }

   protected final CloseableHttpResponse doExecute(HttpHost param1, HttpRequest param2, HttpContext param3) throws IOException, ClientProtocolException {
      // $FF: Couldn't be decompiled
   }

   public final AuthSchemeRegistry getAuthSchemes() {
      synchronized(this){}

      AuthSchemeRegistry var1;
      try {
         if (this.supportedAuthSchemes == null) {
            this.supportedAuthSchemes = this.createAuthSchemeRegistry();
         }

         var1 = this.supportedAuthSchemes;
      } finally {
         ;
      }

      return var1;
   }

   public final BackoffManager getBackoffManager() {
      synchronized(this){}

      BackoffManager var1;
      try {
         var1 = this.backoffManager;
      } finally {
         ;
      }

      return var1;
   }

   public final ConnectionBackoffStrategy getConnectionBackoffStrategy() {
      synchronized(this){}

      ConnectionBackoffStrategy var1;
      try {
         var1 = this.connectionBackoffStrategy;
      } finally {
         ;
      }

      return var1;
   }

   public final ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
      synchronized(this){}

      ConnectionKeepAliveStrategy var1;
      try {
         if (this.keepAliveStrategy == null) {
            this.keepAliveStrategy = this.createConnectionKeepAliveStrategy();
         }

         var1 = this.keepAliveStrategy;
      } finally {
         ;
      }

      return var1;
   }

   public final ClientConnectionManager getConnectionManager() {
      synchronized(this){}

      ClientConnectionManager var1;
      try {
         if (this.connManager == null) {
            this.connManager = this.createClientConnectionManager();
         }

         var1 = this.connManager;
      } finally {
         ;
      }

      return var1;
   }

   public final ConnectionReuseStrategy getConnectionReuseStrategy() {
      synchronized(this){}

      ConnectionReuseStrategy var1;
      try {
         if (this.reuseStrategy == null) {
            this.reuseStrategy = this.createConnectionReuseStrategy();
         }

         var1 = this.reuseStrategy;
      } finally {
         ;
      }

      return var1;
   }

   public final CookieSpecRegistry getCookieSpecs() {
      synchronized(this){}

      CookieSpecRegistry var1;
      try {
         if (this.supportedCookieSpecs == null) {
            this.supportedCookieSpecs = this.createCookieSpecRegistry();
         }

         var1 = this.supportedCookieSpecs;
      } finally {
         ;
      }

      return var1;
   }

   public final CookieStore getCookieStore() {
      synchronized(this){}

      CookieStore var1;
      try {
         if (this.cookieStore == null) {
            this.cookieStore = this.createCookieStore();
         }

         var1 = this.cookieStore;
      } finally {
         ;
      }

      return var1;
   }

   public final CredentialsProvider getCredentialsProvider() {
      synchronized(this){}

      CredentialsProvider var1;
      try {
         if (this.credsProvider == null) {
            this.credsProvider = this.createCredentialsProvider();
         }

         var1 = this.credsProvider;
      } finally {
         ;
      }

      return var1;
   }

   protected final BasicHttpProcessor getHttpProcessor() {
      synchronized(this){}

      BasicHttpProcessor var1;
      try {
         if (this.mutableProcessor == null) {
            this.mutableProcessor = this.createHttpProcessor();
         }

         var1 = this.mutableProcessor;
      } finally {
         ;
      }

      return var1;
   }

   public final HttpRequestRetryHandler getHttpRequestRetryHandler() {
      synchronized(this){}

      HttpRequestRetryHandler var1;
      try {
         if (this.retryHandler == null) {
            this.retryHandler = this.createHttpRequestRetryHandler();
         }

         var1 = this.retryHandler;
      } finally {
         ;
      }

      return var1;
   }

   public final HttpParams getParams() {
      synchronized(this){}

      HttpParams var1;
      try {
         if (this.defaultParams == null) {
            this.defaultParams = this.createHttpParams();
         }

         var1 = this.defaultParams;
      } finally {
         ;
      }

      return var1;
   }

   @Deprecated
   public final AuthenticationHandler getProxyAuthenticationHandler() {
      synchronized(this){}

      AuthenticationHandler var1;
      try {
         var1 = this.createProxyAuthenticationHandler();
      } finally {
         ;
      }

      return var1;
   }

   public final AuthenticationStrategy getProxyAuthenticationStrategy() {
      synchronized(this){}

      AuthenticationStrategy var1;
      try {
         if (this.proxyAuthStrategy == null) {
            this.proxyAuthStrategy = this.createProxyAuthenticationStrategy();
         }

         var1 = this.proxyAuthStrategy;
      } finally {
         ;
      }

      return var1;
   }

   @Deprecated
   public final RedirectHandler getRedirectHandler() {
      synchronized(this){}

      RedirectHandler var1;
      try {
         var1 = this.createRedirectHandler();
      } finally {
         ;
      }

      return var1;
   }

   public final RedirectStrategy getRedirectStrategy() {
      synchronized(this){}

      RedirectStrategy var1;
      try {
         if (this.redirectStrategy == null) {
            this.redirectStrategy = new DefaultRedirectStrategy();
         }

         var1 = this.redirectStrategy;
      } finally {
         ;
      }

      return var1;
   }

   public final HttpRequestExecutor getRequestExecutor() {
      synchronized(this){}

      HttpRequestExecutor var1;
      try {
         if (this.requestExec == null) {
            this.requestExec = this.createRequestExecutor();
         }

         var1 = this.requestExec;
      } finally {
         ;
      }

      return var1;
   }

   public HttpRequestInterceptor getRequestInterceptor(int var1) {
      synchronized(this){}

      HttpRequestInterceptor var2;
      try {
         var2 = this.getHttpProcessor().getRequestInterceptor(var1);
      } finally {
         ;
      }

      return var2;
   }

   public int getRequestInterceptorCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.getHttpProcessor().getRequestInterceptorCount();
      } finally {
         ;
      }

      return var1;
   }

   public HttpResponseInterceptor getResponseInterceptor(int var1) {
      synchronized(this){}

      HttpResponseInterceptor var2;
      try {
         var2 = this.getHttpProcessor().getResponseInterceptor(var1);
      } finally {
         ;
      }

      return var2;
   }

   public int getResponseInterceptorCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.getHttpProcessor().getResponseInterceptorCount();
      } finally {
         ;
      }

      return var1;
   }

   public final HttpRoutePlanner getRoutePlanner() {
      synchronized(this){}

      HttpRoutePlanner var1;
      try {
         if (this.routePlanner == null) {
            this.routePlanner = this.createHttpRoutePlanner();
         }

         var1 = this.routePlanner;
      } finally {
         ;
      }

      return var1;
   }

   @Deprecated
   public final AuthenticationHandler getTargetAuthenticationHandler() {
      synchronized(this){}

      AuthenticationHandler var1;
      try {
         var1 = this.createTargetAuthenticationHandler();
      } finally {
         ;
      }

      return var1;
   }

   public final AuthenticationStrategy getTargetAuthenticationStrategy() {
      synchronized(this){}

      AuthenticationStrategy var1;
      try {
         if (this.targetAuthStrategy == null) {
            this.targetAuthStrategy = this.createTargetAuthenticationStrategy();
         }

         var1 = this.targetAuthStrategy;
      } finally {
         ;
      }

      return var1;
   }

   public final UserTokenHandler getUserTokenHandler() {
      synchronized(this){}

      UserTokenHandler var1;
      try {
         if (this.userTokenHandler == null) {
            this.userTokenHandler = this.createUserTokenHandler();
         }

         var1 = this.userTokenHandler;
      } finally {
         ;
      }

      return var1;
   }

   public void removeRequestInterceptorByClass(Class var1) {
      synchronized(this){}

      try {
         this.getHttpProcessor().removeRequestInterceptorByClass(var1);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void removeResponseInterceptorByClass(Class var1) {
      synchronized(this){}

      try {
         this.getHttpProcessor().removeResponseInterceptorByClass(var1);
         this.protocolProcessor = null;
      } finally {
         ;
      }

   }

   public void setAuthSchemes(AuthSchemeRegistry var1) {
      synchronized(this){}

      try {
         this.supportedAuthSchemes = var1;
      } finally {
         ;
      }

   }

   public void setBackoffManager(BackoffManager var1) {
      synchronized(this){}

      try {
         this.backoffManager = var1;
      } finally {
         ;
      }

   }

   public void setConnectionBackoffStrategy(ConnectionBackoffStrategy var1) {
      synchronized(this){}

      try {
         this.connectionBackoffStrategy = var1;
      } finally {
         ;
      }

   }

   public void setCookieSpecs(CookieSpecRegistry var1) {
      synchronized(this){}

      try {
         this.supportedCookieSpecs = var1;
      } finally {
         ;
      }

   }

   public void setCookieStore(CookieStore var1) {
      synchronized(this){}

      try {
         this.cookieStore = var1;
      } finally {
         ;
      }

   }

   public void setCredentialsProvider(CredentialsProvider var1) {
      synchronized(this){}

      try {
         this.credsProvider = var1;
      } finally {
         ;
      }

   }

   public void setHttpRequestRetryHandler(HttpRequestRetryHandler var1) {
      synchronized(this){}

      try {
         this.retryHandler = var1;
      } finally {
         ;
      }

   }

   public void setKeepAliveStrategy(ConnectionKeepAliveStrategy var1) {
      synchronized(this){}

      try {
         this.keepAliveStrategy = var1;
      } finally {
         ;
      }

   }

   public void setParams(HttpParams var1) {
      synchronized(this){}

      try {
         this.defaultParams = var1;
      } finally {
         ;
      }

   }

   @Deprecated
   public void setProxyAuthenticationHandler(AuthenticationHandler var1) {
      synchronized(this){}

      try {
         this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(var1);
      } finally {
         ;
      }

   }

   public void setProxyAuthenticationStrategy(AuthenticationStrategy var1) {
      synchronized(this){}

      try {
         this.proxyAuthStrategy = var1;
      } finally {
         ;
      }

   }

   @Deprecated
   public void setRedirectHandler(RedirectHandler var1) {
      synchronized(this){}

      try {
         this.redirectStrategy = new DefaultRedirectStrategyAdaptor(var1);
      } finally {
         ;
      }

   }

   public void setRedirectStrategy(RedirectStrategy var1) {
      synchronized(this){}

      try {
         this.redirectStrategy = var1;
      } finally {
         ;
      }

   }

   public void setReuseStrategy(ConnectionReuseStrategy var1) {
      synchronized(this){}

      try {
         this.reuseStrategy = var1;
      } finally {
         ;
      }

   }

   public void setRoutePlanner(HttpRoutePlanner var1) {
      synchronized(this){}

      try {
         this.routePlanner = var1;
      } finally {
         ;
      }

   }

   @Deprecated
   public void setTargetAuthenticationHandler(AuthenticationHandler var1) {
      synchronized(this){}

      try {
         this.targetAuthStrategy = new AuthenticationStrategyAdaptor(var1);
      } finally {
         ;
      }

   }

   public void setTargetAuthenticationStrategy(AuthenticationStrategy var1) {
      synchronized(this){}

      try {
         this.targetAuthStrategy = var1;
      } finally {
         ;
      }

   }

   public void setUserTokenHandler(UserTokenHandler var1) {
      synchronized(this){}

      try {
         this.userTokenHandler = var1;
      } finally {
         ;
      }

   }
}
