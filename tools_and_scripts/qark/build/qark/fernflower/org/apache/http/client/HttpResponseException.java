package org.apache.http.client;

import org.apache.http.util.TextUtils;

public class HttpResponseException extends ClientProtocolException {
   private static final long serialVersionUID = -7186627969477257933L;
   private final String reasonPhrase;
   private final int statusCode;

   public HttpResponseException(int var1, String var2) {
      StringBuilder var4 = new StringBuilder();
      var4.append("status code: %d");
      String var3;
      if (TextUtils.isBlank(var2)) {
         var3 = "";
      } else {
         var3 = ", reason phrase: %s";
      }

      var4.append(var3);
      super(String.format(var4.toString(), var1, var2));
      this.statusCode = var1;
      this.reasonPhrase = var2;
   }

   public String getReasonPhrase() {
      return this.reasonPhrase;
   }

   public int getStatusCode() {
      return this.statusCode;
   }
}
