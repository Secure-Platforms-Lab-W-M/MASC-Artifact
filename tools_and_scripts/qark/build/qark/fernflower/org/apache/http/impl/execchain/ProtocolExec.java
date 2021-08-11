package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

public class ProtocolExec implements ClientExecChain {
   private final HttpProcessor httpProcessor;
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;

   public ProtocolExec(ClientExecChain var1, HttpProcessor var2) {
      Args.notNull(var1, "HTTP client request executor");
      Args.notNull(var2, "HTTP protocol processor");
      this.requestExecutor = var1;
      this.httpProcessor = var2;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      HttpRequest var7 = var2.getOriginal();
      HttpHost var6 = null;
      URI var16;
      if (var7 instanceof HttpUriRequest) {
         var16 = ((HttpUriRequest)var7).getURI();
      } else {
         String var8 = var7.getRequestLine().getUri();

         try {
            var16 = URI.create(var8);
         } catch (IllegalArgumentException var14) {
            var16 = var6;
            if (this.log.isDebugEnabled()) {
               Log var17 = this.log;
               StringBuilder var10 = new StringBuilder();
               var10.append("Unable to parse '");
               var10.append(var8);
               var10.append("' as a valid URI; ");
               var10.append("request URI and Host header may be inconsistent");
               var17.debug(var10.toString(), var14);
               var16 = var6;
            }
         }
      }

      var2.setURI(var16);
      this.rewriteRequestURI(var2, var1, var3.getRequestConfig().isNormalizeUri());
      HttpHost var9 = (HttpHost)var2.getParams().getParameter("http.virtual-host");
      HttpHost var18 = var9;
      if (var9 != null) {
         var18 = var9;
         if (var9.getPort() == -1) {
            int var5 = var1.getTargetHost().getPort();
            var6 = var9;
            if (var5 != -1) {
               var6 = new HttpHost(var9.getHostName(), var5, var9.getSchemeName());
            }

            var18 = var6;
            if (this.log.isDebugEnabled()) {
               Log var19 = this.log;
               StringBuilder var20 = new StringBuilder();
               var20.append("Using virtual host");
               var20.append(var6);
               var19.debug(var20.toString());
               var18 = var6;
            }
         }
      }

      var9 = null;
      if (var18 != null) {
         var6 = var18;
      } else {
         var6 = var9;
         if (var16 != null) {
            var6 = var9;
            if (var16.isAbsolute()) {
               var6 = var9;
               if (var16.getHost() != null) {
                  var6 = new HttpHost(var16.getHost(), var16.getPort(), var16.getScheme());
               }
            }
         }
      }

      var18 = var6;
      if (var6 == null) {
         var18 = var2.getTarget();
      }

      var6 = var18;
      if (var18 == null) {
         var6 = var1.getTargetHost();
      }

      if (var16 != null) {
         String var22 = var16.getUserInfo();
         if (var22 != null) {
            CredentialsProvider var23 = var3.getCredentialsProvider();
            Object var21 = var23;
            if (var23 == null) {
               var21 = new BasicCredentialsProvider();
               var3.setCredentialsProvider((CredentialsProvider)var21);
            }

            ((CredentialsProvider)var21).setCredentials(new AuthScope(var6), new UsernamePasswordCredentials(var22));
         }
      }

      var3.setAttribute("http.target_host", var6);
      var3.setAttribute("http.route", var1);
      var3.setAttribute("http.request", var2);
      this.httpProcessor.process(var2, var3);
      CloseableHttpResponse var15 = this.requestExecutor.execute(var1, var2, var3, var4);

      try {
         var3.setAttribute("http.response", var15);
         this.httpProcessor.process(var15, var3);
         return var15;
      } catch (RuntimeException var11) {
         var15.close();
         throw var11;
      } catch (IOException var12) {
         var15.close();
         throw var12;
      } catch (HttpException var13) {
         var15.close();
         throw var13;
      }
   }

   void rewriteRequestURI(HttpRequestWrapper var1, HttpRoute var2, boolean var3) throws ProtocolException {
      URI var4 = var1.getURI();
      if (var4 != null) {
         try {
            var1.setURI(URIUtils.rewriteURIForRoute(var4, var2, var3));
         } catch (URISyntaxException var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Invalid URI: ");
            var6.append(var4);
            throw new ProtocolException(var6.toString(), var5);
         }
      }
   }
}
