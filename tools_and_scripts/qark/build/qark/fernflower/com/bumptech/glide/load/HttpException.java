package com.bumptech.glide.load;

import java.io.IOException;

public final class HttpException extends IOException {
   public static final int UNKNOWN = -1;
   private static final long serialVersionUID = 1L;
   private final int statusCode;

   public HttpException(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Http request failed with status code: ");
      var2.append(var1);
      this(var2.toString(), var1);
   }

   public HttpException(String var1) {
      this(var1, -1);
   }

   public HttpException(String var1, int var2) {
      this(var1, var2, (Throwable)null);
   }

   public HttpException(String var1, int var2, Throwable var3) {
      super(var1, var3);
      this.statusCode = var2;
   }

   public int getStatusCode() {
      return this.statusCode;
   }
}
