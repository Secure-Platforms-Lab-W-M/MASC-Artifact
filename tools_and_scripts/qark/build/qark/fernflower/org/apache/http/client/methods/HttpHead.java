package org.apache.http.client.methods;

import java.net.URI;

public class HttpHead extends HttpRequestBase {
   public static final String METHOD_NAME = "HEAD";

   public HttpHead() {
   }

   public HttpHead(String var1) {
      this.setURI(URI.create(var1));
   }

   public HttpHead(URI var1) {
      this.setURI(var1);
   }

   public String getMethod() {
      return "HEAD";
   }
}
