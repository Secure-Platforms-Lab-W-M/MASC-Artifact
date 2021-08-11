package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpParamsNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

class InternalHttpClient extends CloseableHttpClient implements Configurable {
   private final Lookup authSchemeRegistry;
   private final List closeables;
   private final HttpClientConnectionManager connManager;
   private final Lookup cookieSpecRegistry;
   private final CookieStore cookieStore;
   private final CredentialsProvider credentialsProvider;
   private final RequestConfig defaultConfig;
   private final ClientExecChain execChain;
   private final Log log = LogFactory.getLog(this.getClass());
   private final HttpRoutePlanner routePlanner;

   public InternalHttpClient(ClientExecChain var1, HttpClientConnectionManager var2, HttpRoutePlanner var3, Lookup var4, Lookup var5, CookieStore var6, CredentialsProvider var7, RequestConfig var8, List var9) {
      Args.notNull(var1, "HTTP client exec chain");
      Args.notNull(var2, "HTTP connection manager");
      Args.notNull(var3, "HTTP route planner");
      this.execChain = var1;
      this.connManager = var2;
      this.routePlanner = var3;
      this.cookieSpecRegistry = var4;
      this.authSchemeRegistry = var5;
      this.cookieStore = var6;
      this.credentialsProvider = var7;
      this.defaultConfig = var8;
      this.closeables = var9;
   }

   private HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      HttpHost var4 = var1;
      if (var1 == null) {
         var4 = (HttpHost)var2.getParams().getParameter("http.default-host");
      }

      return this.routePlanner.determineRoute(var4, var2, var3);
   }

   private void setupContext(HttpClientContext var1) {
      if (var1.getAttribute("http.auth.target-scope") == null) {
         var1.setAttribute("http.auth.target-scope", new AuthState());
      }

      if (var1.getAttribute("http.auth.proxy-scope") == null) {
         var1.setAttribute("http.auth.proxy-scope", new AuthState());
      }

      if (var1.getAttribute("http.authscheme-registry") == null) {
         var1.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
      }

      if (var1.getAttribute("http.cookiespec-registry") == null) {
         var1.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
      }

      if (var1.getAttribute("http.cookie-store") == null) {
         var1.setAttribute("http.cookie-store", this.cookieStore);
      }

      if (var1.getAttribute("http.auth.credentials-provider") == null) {
         var1.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
      }

      if (var1.getAttribute("http.request-config") == null) {
         var1.setAttribute("http.request-config", this.defaultConfig);
      }

   }

   public void close() {
      List var1 = this.closeables;
      if (var1 != null) {
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Closeable var2 = (Closeable)var4.next();

            try {
               var2.close();
            } catch (IOException var3) {
               this.log.error(var3.getMessage(), var3);
            }
         }
      }

   }

   protected CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      Args.notNull(var2, "HTTP request");
      HttpExecutionAware var5 = null;
      if (var2 instanceof HttpExecutionAware) {
         var5 = (HttpExecutionAware)var2;
      }

      HttpException var10000;
      label84: {
         HttpRequestWrapper var6;
         boolean var10001;
         try {
            var6 = HttpRequestWrapper.wrap(var2, var1);
         } catch (HttpException var16) {
            var10000 = var16;
            var10001 = false;
            break label84;
         }

         if (var3 == null) {
            try {
               var3 = new BasicHttpContext();
            } catch (HttpException var15) {
               var10000 = var15;
               var10001 = false;
               break label84;
            }
         }

         HttpClientContext var7;
         try {
            var7 = HttpClientContext.adapt((HttpContext)var3);
         } catch (HttpException var14) {
            var10000 = var14;
            var10001 = false;
            break label84;
         }

         RequestConfig var21 = null;

         try {
            if (var2 instanceof Configurable) {
               var21 = ((Configurable)var2).getConfig();
            }
         } catch (HttpException var13) {
            var10000 = var13;
            var10001 = false;
            break label84;
         }

         RequestConfig var4 = var21;
         if (var21 == null) {
            label85: {
               HttpParams var20;
               label62: {
                  try {
                     var20 = var2.getParams();
                     if (var20 instanceof HttpParamsNames) {
                        break label62;
                     }
                  } catch (HttpException var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label84;
                  }

                  try {
                     var4 = HttpClientParamConfig.getRequestConfig(var20, this.defaultConfig);
                     break label85;
                  } catch (HttpException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label84;
                  }
               }

               var4 = var21;

               try {
                  if (!((HttpParamsNames)var20).getNames().isEmpty()) {
                     var4 = HttpClientParamConfig.getRequestConfig(var20, this.defaultConfig);
                  }
               } catch (HttpException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label84;
               }
            }
         }

         if (var4 != null) {
            try {
               var7.setRequestConfig(var4);
            } catch (HttpException var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            }
         }

         try {
            this.setupContext(var7);
            HttpRoute var18 = this.determineRoute(var1, var6, var7);
            CloseableHttpResponse var19 = this.execChain.execute(var18, var6, var7, var5);
            return var19;
         } catch (HttpException var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      HttpException var17 = var10000;
      throw new ClientProtocolException(var17);
   }

   public RequestConfig getConfig() {
      return this.defaultConfig;
   }

   public ClientConnectionManager getConnectionManager() {
      return new ClientConnectionManager() {
         public void closeExpiredConnections() {
            InternalHttpClient.this.connManager.closeExpiredConnections();
         }

         public void closeIdleConnections(long var1, TimeUnit var3) {
            InternalHttpClient.this.connManager.closeIdleConnections(var1, var3);
         }

         public SchemeRegistry getSchemeRegistry() {
            throw new UnsupportedOperationException();
         }

         public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
            throw new UnsupportedOperationException();
         }

         public ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
            throw new UnsupportedOperationException();
         }

         public void shutdown() {
            InternalHttpClient.this.connManager.shutdown();
         }
      };
   }

   public HttpParams getParams() {
      throw new UnsupportedOperationException();
   }
}
