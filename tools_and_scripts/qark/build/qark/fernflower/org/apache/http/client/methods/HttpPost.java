package org.apache.http.client.methods;

import java.net.URI;

public class HttpPost extends HttpEntityEnclosingRequestBase {
   public static final String METHOD_NAME = "POST";

   public HttpPost() {
   }

   public HttpPost(String var1) {
      this.setURI(URI.create(var1));
   }

   public HttpPost(URI var1) {
      this.setURI(var1);
   }

   public String getMethod() {
      return "POST";
   }
}
