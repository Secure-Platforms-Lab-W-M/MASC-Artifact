package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

public class HttpRequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
   private final String method;
   private final HttpRequest original;
   private RequestLine requestLine;
   private final HttpHost target;
   private URI uri;
   private ProtocolVersion version;

   private HttpRequestWrapper(HttpRequest var1, HttpHost var2) {
      HttpRequest var3 = (HttpRequest)Args.notNull(var1, "HTTP request");
      this.original = var3;
      this.target = var2;
      this.version = var3.getRequestLine().getProtocolVersion();
      this.method = this.original.getRequestLine().getMethod();
      if (var1 instanceof HttpUriRequest) {
         this.uri = ((HttpUriRequest)var1).getURI();
      } else {
         this.uri = null;
      }

      this.setHeaders(var1.getAllHeaders());
   }

   // $FF: synthetic method
   HttpRequestWrapper(HttpRequest var1, HttpHost var2, Object var3) {
      this(var1, var2);
   }

   public static HttpRequestWrapper wrap(HttpRequest var0) {
      return wrap(var0, (HttpHost)null);
   }

   public static HttpRequestWrapper wrap(HttpRequest var0, HttpHost var1) {
      Args.notNull(var0, "HTTP request");
      return (HttpRequestWrapper)(var0 instanceof HttpEntityEnclosingRequest ? new HttpRequestWrapper.HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var0, var1) : new HttpRequestWrapper(var0, var1));
   }

   public void abort() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }

   public String getMethod() {
      return this.method;
   }

   public HttpRequest getOriginal() {
      return this.original;
   }

   @Deprecated
   public HttpParams getParams() {
      if (this.params == null) {
         this.params = this.original.getParams().copy();
      }

      return this.params;
   }

   public ProtocolVersion getProtocolVersion() {
      ProtocolVersion var1 = this.version;
      return var1 != null ? var1 : this.original.getProtocolVersion();
   }

   public RequestLine getRequestLine() {
      if (this.requestLine == null) {
         URI var1 = this.uri;
         String var3;
         if (var1 != null) {
            var3 = var1.toASCIIString();
         } else {
            var3 = this.original.getRequestLine().getUri();
         }

         String var2;
         label17: {
            if (var3 != null) {
               var2 = var3;
               if (!var3.isEmpty()) {
                  break label17;
               }
            }

            var2 = "/";
         }

         this.requestLine = new BasicRequestLine(this.method, var2, this.getProtocolVersion());
      }

      return this.requestLine;
   }

   public HttpHost getTarget() {
      return this.target;
   }

   public URI getURI() {
      return this.uri;
   }

   public boolean isAborted() {
      return false;
   }

   public void setProtocolVersion(ProtocolVersion var1) {
      this.version = var1;
      this.requestLine = null;
   }

   public void setURI(URI var1) {
      this.uri = var1;
      this.requestLine = null;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getRequestLine());
      var1.append(" ");
      var1.append(this.headergroup);
      return var1.toString();
   }

   static class HttpEntityEnclosingRequestWrapper extends HttpRequestWrapper implements HttpEntityEnclosingRequest {
      private HttpEntity entity;

      HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest var1, HttpHost var2) {
         super(var1, var2, null);
         this.entity = var1.getEntity();
      }

      public boolean expectContinue() {
         Header var1 = this.getFirstHeader("Expect");
         return var1 != null && "100-continue".equalsIgnoreCase(var1.getValue());
      }

      public HttpEntity getEntity() {
         return this.entity;
      }

      public void setEntity(HttpEntity var1) {
         this.entity = var1;
      }
   }
}
