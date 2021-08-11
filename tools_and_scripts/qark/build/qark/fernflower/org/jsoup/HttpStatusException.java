package org.jsoup;

import java.io.IOException;

public class HttpStatusException extends IOException {
   private int statusCode;
   private String url;

   public HttpStatusException(String var1, int var2, String var3) {
      super(var1);
      this.statusCode = var2;
      this.url = var3;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public String getUrl() {
      return this.url;
   }

   public String toString() {
      return super.toString() + ". Status=" + this.statusCode + ", URL=" + this.url;
   }
}
