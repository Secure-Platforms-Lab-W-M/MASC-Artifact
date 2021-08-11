package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;

public abstract class HttpRequestBase extends AbstractExecutionAwareRequest implements HttpUriRequest, Configurable {
   private RequestConfig config;
   private URI uri;
   private ProtocolVersion version;

   public RequestConfig getConfig() {
      return this.config;
   }

   public abstract String getMethod();

   public ProtocolVersion getProtocolVersion() {
      ProtocolVersion var1 = this.version;
      return var1 != null ? var1 : HttpProtocolParams.getVersion(this.getParams());
   }

   public RequestLine getRequestLine() {
      String var3 = this.getMethod();
      ProtocolVersion var4 = this.getProtocolVersion();
      URI var2 = this.getURI();
      String var1 = null;
      if (var2 != null) {
         var1 = var2.toASCIIString();
      }

      String var5;
      if (var1 != null) {
         var5 = var1;
         if (!var1.isEmpty()) {
            return new BasicRequestLine(var3, var5, var4);
         }
      }

      var5 = "/";
      return new BasicRequestLine(var3, var5, var4);
   }

   public URI getURI() {
      return this.uri;
   }

   public void releaseConnection() {
      this.reset();
   }

   public void setConfig(RequestConfig var1) {
      this.config = var1;
   }

   public void setProtocolVersion(ProtocolVersion var1) {
      this.version = var1;
   }

   public void setURI(URI var1) {
      this.uri = var1;
   }

   public void started() {
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getMethod());
      var1.append(" ");
      var1.append(this.getURI());
      var1.append(" ");
      var1.append(this.getProtocolVersion());
      return var1.toString();
   }
}
