package org.apache.http.client.methods;

import java.net.URI;

public class HttpGet extends HttpRequestBase {
   public static final String METHOD_NAME = "GET";

   public HttpGet() {
   }

   public HttpGet(String var1) {
      this.setURI(URI.create(var1));
   }

   public HttpGet(URI var1) {
      this.setURI(var1);
   }

   public String getMethod() {
      return "GET";
   }
}
