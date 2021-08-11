package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public abstract class CloseableHttpClient implements HttpClient, Closeable {
   private final Log log = LogFactory.getLog(this.getClass());

   private static HttpHost determineTarget(HttpUriRequest var0) throws ClientProtocolException {
      URI var2 = var0.getURI();
      if (var2.isAbsolute()) {
         HttpHost var1 = URIUtils.extractHost(var2);
         if (var1 != null) {
            return var1;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("URI does not specify a valid host name: ");
            var3.append(var2);
            throw new ClientProtocolException(var3.toString());
         }
      } else {
         return null;
      }
   }

   protected abstract CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException;

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3) throws IOException, ClientProtocolException {
      return this.execute(var1, var2, var3, (HttpContext)null);
   }

   public Object execute(HttpHost param1, HttpRequest param2, ResponseHandler param3, HttpContext param4) throws IOException, ClientProtocolException {
      // $FF: Couldn't be decompiled
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2) throws IOException, ClientProtocolException {
      return this.execute((HttpUriRequest)var1, (ResponseHandler)var2, (HttpContext)null);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2, HttpContext var3) throws IOException, ClientProtocolException {
      return this.execute(determineTarget(var1), var1, var2, var3);
   }

   public CloseableHttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException, ClientProtocolException {
      return this.doExecute(var1, var2, (HttpContext)null);
   }

   public CloseableHttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      return this.doExecute(var1, var2, var3);
   }

   public CloseableHttpResponse execute(HttpUriRequest var1) throws IOException, ClientProtocolException {
      return this.execute(var1, (HttpContext)null);
   }

   public CloseableHttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException, ClientProtocolException {
      Args.notNull(var1, "HTTP request");
      return this.doExecute(determineTarget(var1), var1, var2);
   }
}
