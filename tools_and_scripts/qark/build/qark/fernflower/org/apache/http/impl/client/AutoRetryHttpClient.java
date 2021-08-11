package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
public class AutoRetryHttpClient implements HttpClient {
   private final HttpClient backend;
   private final Log log;
   private final ServiceUnavailableRetryStrategy retryStrategy;

   public AutoRetryHttpClient() {
      this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
   }

   public AutoRetryHttpClient(HttpClient var1) {
      this(var1, new DefaultServiceUnavailableRetryStrategy());
   }

   public AutoRetryHttpClient(HttpClient var1, ServiceUnavailableRetryStrategy var2) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "HttpClient");
      Args.notNull(var2, "ServiceUnavailableRetryStrategy");
      this.backend = var1;
      this.retryStrategy = var2;
   }

   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy var1) {
      this(new DefaultHttpClient(), var1);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3) throws IOException {
      return this.execute(var1, var2, var3, (HttpContext)null);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3, HttpContext var4) throws IOException {
      return var3.handleResponse(this.execute(var1, var2, var4));
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2) throws IOException {
      return this.execute((HttpUriRequest)var1, (ResponseHandler)var2, (HttpContext)null);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2, HttpContext var3) throws IOException {
      return var2.handleResponse(this.execute(var1, var3));
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException {
      return this.execute((HttpHost)var1, (HttpRequest)var2, (HttpContext)null);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException {
      int var4 = 1;

      while(true) {
         HttpResponse var7 = this.backend.execute(var1, var2, var3);

         try {
            if (!this.retryStrategy.retryRequest(var7, var4, var3)) {
               return var7;
            }

            EntityUtils.consume(var7.getEntity());
            long var5 = this.retryStrategy.getRetryInterval();

            try {
               Log var8 = this.log;
               StringBuilder var9 = new StringBuilder();
               var9.append("Wait for ");
               var9.append(var5);
               var8.trace(var9.toString());
               Thread.sleep(var5);
            } catch (InterruptedException var11) {
               Thread.currentThread().interrupt();
               throw new InterruptedIOException();
            }
         } catch (RuntimeException var12) {
            try {
               EntityUtils.consume(var7.getEntity());
            } catch (IOException var10) {
               this.log.warn("I/O error consuming response content", var10);
            }

            throw var12;
         }

         ++var4;
      }
   }

   public HttpResponse execute(HttpUriRequest var1) throws IOException {
      return this.execute((HttpUriRequest)var1, (HttpContext)null);
   }

   public HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException {
      URI var3 = var1.getURI();
      return this.execute((HttpHost)(new HttpHost(var3.getHost(), var3.getPort(), var3.getScheme())), (HttpRequest)var1, (HttpContext)var2);
   }

   public ClientConnectionManager getConnectionManager() {
      return this.backend.getConnectionManager();
   }

   public HttpParams getParams() {
      return this.backend.getParams();
   }
}
