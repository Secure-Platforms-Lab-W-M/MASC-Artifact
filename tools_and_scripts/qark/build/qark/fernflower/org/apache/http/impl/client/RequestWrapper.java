package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.Args;

@Deprecated
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
   private int execCount;
   private String method;
   private final HttpRequest original;
   private URI uri;
   private ProtocolVersion version;

   public RequestWrapper(HttpRequest var1) throws ProtocolException {
      Args.notNull(var1, "HTTP request");
      this.original = var1;
      this.setParams(var1.getParams());
      this.setHeaders(var1.getAllHeaders());
      if (var1 instanceof HttpUriRequest) {
         this.uri = ((HttpUriRequest)var1).getURI();
         this.method = ((HttpUriRequest)var1).getMethod();
         this.version = null;
      } else {
         RequestLine var2 = var1.getRequestLine();

         try {
            this.uri = new URI(var2.getUri());
         } catch (URISyntaxException var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Invalid request URI: ");
            var3.append(var2.getUri());
            throw new ProtocolException(var3.toString(), var4);
         }

         this.method = var2.getMethod();
         this.version = var1.getProtocolVersion();
      }

      this.execCount = 0;
   }

   public void abort() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }

   public int getExecCount() {
      return this.execCount;
   }

   public String getMethod() {
      return this.method;
   }

   public HttpRequest getOriginal() {
      return this.original;
   }

   public ProtocolVersion getProtocolVersion() {
      if (this.version == null) {
         this.version = HttpProtocolParams.getVersion(this.getParams());
      }

      return this.version;
   }

   public RequestLine getRequestLine() {
      ProtocolVersion var3 = this.getProtocolVersion();
      String var1 = null;
      URI var2 = this.uri;
      if (var2 != null) {
         var1 = var2.toASCIIString();
      }

      String var4;
      if (var1 != null) {
         var4 = var1;
         if (!var1.isEmpty()) {
            return new BasicRequestLine(this.getMethod(), var4, var3);
         }
      }

      var4 = "/";
      return new BasicRequestLine(this.getMethod(), var4, var3);
   }

   public URI getURI() {
      return this.uri;
   }

   public void incrementExecCount() {
      ++this.execCount;
   }

   public boolean isAborted() {
      return false;
   }

   public boolean isRepeatable() {
      return true;
   }

   public void resetHeaders() {
      this.headergroup.clear();
      this.setHeaders(this.original.getAllHeaders());
   }

   public void setMethod(String var1) {
      Args.notNull(var1, "Method name");
      this.method = var1;
   }

   public void setProtocolVersion(ProtocolVersion var1) {
      this.version = var1;
   }

   public void setURI(URI var1) {
      this.uri = var1;
   }
}
