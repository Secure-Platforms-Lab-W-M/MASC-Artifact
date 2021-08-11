package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

@Deprecated
public class DecompressingHttpClient implements HttpClient {
   private final HttpRequestInterceptor acceptEncodingInterceptor;
   private final HttpClient backend;
   private final HttpResponseInterceptor contentEncodingInterceptor;

   public DecompressingHttpClient() {
      this(new DefaultHttpClient());
   }

   public DecompressingHttpClient(HttpClient var1) {
      this(var1, new RequestAcceptEncoding(), new ResponseContentEncoding());
   }

   DecompressingHttpClient(HttpClient var1, HttpRequestInterceptor var2, HttpResponseInterceptor var3) {
      this.backend = var1;
      this.acceptEncodingInterceptor = var2;
      this.contentEncodingInterceptor = var3;
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3) throws IOException, ClientProtocolException {
      return this.execute(var1, var2, var3, (HttpContext)null);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3, HttpContext var4) throws IOException, ClientProtocolException {
      HttpResponse var7 = this.execute(var1, var2, var4);

      Object var9;
      try {
         var9 = var3.handleResponse(var7);
      } finally {
         HttpEntity var8 = var7.getEntity();
         if (var8 != null) {
            EntityUtils.consume(var8);
         }

      }

      return var9;
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (ResponseHandler)var2);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2, HttpContext var3) throws IOException, ClientProtocolException {
      return this.execute(this.getHttpHost(var1), var1, var2, var3);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException, ClientProtocolException {
      return this.execute(var1, var2, (HttpContext)null);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      HttpException var10000;
      label72: {
         boolean var10001;
         if (var3 == null) {
            try {
               var3 = new BasicHttpContext();
            } catch (HttpException var13) {
               var10000 = var13;
               var10001 = false;
               break label72;
            }
         }

         Object var16;
         label63: {
            try {
               if (var2 instanceof HttpEntityEnclosingRequest) {
                  var16 = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var2);
                  break label63;
               }
            } catch (HttpException var12) {
               var10000 = var12;
               var10001 = false;
               break label72;
            }

            try {
               var16 = new RequestWrapper(var2);
            } catch (HttpException var11) {
               var10000 = var11;
               var10001 = false;
               break label72;
            }
         }

         HttpResponse var14;
         try {
            this.acceptEncodingInterceptor.process((HttpRequest)var16, (HttpContext)var3);
            var14 = this.backend.execute((HttpHost)var1, (HttpRequest)var16, (HttpContext)var3);
         } catch (HttpException var10) {
            var10000 = var10;
            var10001 = false;
            break label72;
         }

         try {
            this.contentEncodingInterceptor.process(var14, (HttpContext)var3);
            if (Boolean.TRUE.equals(((HttpContext)var3).getAttribute("http.client.response.uncompressed"))) {
               var14.removeHeaders("Content-Length");
               var14.removeHeaders("Content-Encoding");
               var14.removeHeaders("Content-MD5");
            }

            return var14;
         } catch (HttpException var7) {
            HttpException var19 = var7;

            try {
               EntityUtils.consume(var14.getEntity());
               throw var19;
            } catch (HttpException var6) {
               var10000 = var6;
               var10001 = false;
            }
         } catch (IOException var8) {
            IOException var18 = var8;

            try {
               EntityUtils.consume(var14.getEntity());
               throw var18;
            } catch (HttpException var4) {
               var10000 = var4;
               var10001 = false;
            }
         } catch (RuntimeException var9) {
            RuntimeException var17 = var9;

            try {
               EntityUtils.consume(var14.getEntity());
               throw var17;
            } catch (HttpException var5) {
               var10000 = var5;
               var10001 = false;
            }
         }
      }

      HttpException var15 = var10000;
      throw new ClientProtocolException(var15);
   }

   public HttpResponse execute(HttpUriRequest var1) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (HttpContext)((HttpContext)null));
   }

   public HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (HttpContext)var2);
   }

   public ClientConnectionManager getConnectionManager() {
      return this.backend.getConnectionManager();
   }

   public HttpClient getHttpClient() {
      return this.backend;
   }

   HttpHost getHttpHost(HttpUriRequest var1) {
      return URIUtils.extractHost(var1.getURI());
   }

   public HttpParams getParams() {
      return this.backend.getParams();
   }
}
