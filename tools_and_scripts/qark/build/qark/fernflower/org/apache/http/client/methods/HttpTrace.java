package org.apache.http.client.methods;

import java.net.URI;

public class HttpTrace extends HttpRequestBase {
   public static final String METHOD_NAME = "TRACE";

   public HttpTrace() {
   }

   public HttpTrace(String var1) {
      this.setURI(URI.create(var1));
   }

   public HttpTrace(URI var1) {
      this.setURI(var1);
   }

   public String getMethod() {
      return "TRACE";
   }
}
