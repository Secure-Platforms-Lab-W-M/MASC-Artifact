package org.apache.http.impl.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.execchain.MinimalClientExec;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;

class MinimalHttpClient extends CloseableHttpClient {
   private final HttpClientConnectionManager connManager;
   private final HttpParams params;
   private final MinimalClientExec requestExecutor;

   public MinimalHttpClient(HttpClientConnectionManager var1) {
      this.connManager = (HttpClientConnectionManager)Args.notNull(var1, "HTTP connection manager");
      this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), var1, DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
      this.params = new BasicHttpParams();
   }

   public void close() {
      this.connManager.shutdown();
   }

   protected CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      Args.notNull(var1, "Target host");
      Args.notNull(var2, "HTTP request");
      HttpExecutionAware var4 = null;
      if (var2 instanceof HttpExecutionAware) {
         var4 = (HttpExecutionAware)var2;
      }

      HttpException var10000;
      label58: {
         HttpRequestWrapper var5;
         boolean var10001;
         try {
            var5 = HttpRequestWrapper.wrap(var2);
         } catch (HttpException var12) {
            var10000 = var12;
            var10001 = false;
            break label58;
         }

         if (var3 == null) {
            try {
               var3 = new BasicHttpContext();
            } catch (HttpException var11) {
               var10000 = var11;
               var10001 = false;
               break label58;
            }
         }

         HttpRoute var6;
         HttpClientContext var16;
         try {
            var16 = HttpClientContext.adapt((HttpContext)var3);
            var6 = new HttpRoute(var1);
         } catch (HttpException var10) {
            var10000 = var10;
            var10001 = false;
            break label58;
         }

         RequestConfig var13 = null;

         try {
            if (var2 instanceof Configurable) {
               var13 = ((Configurable)var2).getConfig();
            }
         } catch (HttpException var9) {
            var10000 = var9;
            var10001 = false;
            break label58;
         }

         if (var13 != null) {
            try {
               var16.setRequestConfig(var13);
            } catch (HttpException var8) {
               var10000 = var8;
               var10001 = false;
               break label58;
            }
         }

         try {
            CloseableHttpResponse var15 = this.requestExecutor.execute(var6, var5, var16, var4);
            return var15;
         } catch (HttpException var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      HttpException var14 = var10000;
      throw new ClientProtocolException(var14);
   }

   public ClientConnectionManager getConnectionManager() {
      return new ClientConnectionManager() {
         public void closeExpiredConnections() {
            MinimalHttpClient.this.connManager.closeExpiredConnections();
         }

         public void closeIdleConnections(long var1, TimeUnit var3) {
            MinimalHttpClient.this.connManager.closeIdleConnections(var1, var3);
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
            MinimalHttpClient.this.connManager.shutdown();
         }
      };
   }

   public HttpParams getParams() {
      return this.params;
   }
}
