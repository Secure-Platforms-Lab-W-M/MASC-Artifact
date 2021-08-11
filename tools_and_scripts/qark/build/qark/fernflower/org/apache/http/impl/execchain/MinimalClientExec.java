package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.VersionInfo;

public class MinimalClientExec implements ClientExecChain {
   private final HttpClientConnectionManager connManager;
   private final HttpProcessor httpProcessor;
   private final ConnectionKeepAliveStrategy keepAliveStrategy;
   private final Log log = LogFactory.getLog(this.getClass());
   private final HttpRequestExecutor requestExecutor;
   private final ConnectionReuseStrategy reuseStrategy;

   public MinimalClientExec(HttpRequestExecutor var1, HttpClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "Client connection manager");
      Args.notNull(var3, "Connection reuse strategy");
      Args.notNull(var4, "Connection keep alive strategy");
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", this.getClass()))});
      this.requestExecutor = var1;
      this.connManager = var2;
      this.reuseStrategy = var3;
      this.keepAliveStrategy = var4;
   }

   static void rewriteRequestURI(HttpRequestWrapper var0, HttpRoute var1, boolean var2) throws ProtocolException {
      URISyntaxException var10000;
      label58: {
         URI var3;
         boolean var10001;
         try {
            var3 = var0.getURI();
         } catch (URISyntaxException var10) {
            var10000 = var10;
            var10001 = false;
            break label58;
         }

         if (var3 == null) {
            return;
         }

         URI var12;
         label50: {
            label59: {
               try {
                  if (!var3.isAbsolute()) {
                     break label59;
                  }
               } catch (URISyntaxException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label58;
               }

               EnumSet var11;
               if (var2) {
                  try {
                     var11 = URIUtils.DROP_FRAGMENT_AND_NORMALIZE;
                  } catch (URISyntaxException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label58;
                  }
               } else {
                  try {
                     var11 = URIUtils.DROP_FRAGMENT;
                  } catch (URISyntaxException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label58;
                  }
               }

               try {
                  var12 = URIUtils.rewriteURI(var3, (HttpHost)null, var11);
                  break label50;
               } catch (URISyntaxException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label58;
               }
            }

            try {
               var12 = URIUtils.rewriteURI(var3);
            } catch (URISyntaxException var5) {
               var10000 = var5;
               var10001 = false;
               break label58;
            }
         }

         try {
            var0.setURI(var12);
            return;
         } catch (URISyntaxException var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      URISyntaxException var13 = var10000;
      StringBuilder var14 = new StringBuilder();
      var14.append("Invalid URI: ");
      var14.append(var0.getRequestLine().getUri());
      throw new ProtocolException(var14.toString(), var13);
   }

   public CloseableHttpResponse execute(HttpRoute param1, HttpRequestWrapper param2, HttpClientContext param3, HttpExecutionAware param4) throws IOException, HttpException {
      // $FF: Couldn't be decompiled
   }
}
