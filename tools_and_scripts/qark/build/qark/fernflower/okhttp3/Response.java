package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public final class Response implements Closeable {
   @Nullable
   final ResponseBody body;
   private volatile CacheControl cacheControl;
   @Nullable
   final Response cacheResponse;
   final int code;
   @Nullable
   final Handshake handshake;
   final Headers headers;
   final String message;
   @Nullable
   final Response networkResponse;
   @Nullable
   final Response priorResponse;
   final Protocol protocol;
   final long receivedResponseAtMillis;
   final Request request;
   final long sentRequestAtMillis;

   Response(Response.Builder var1) {
      this.request = var1.request;
      this.protocol = var1.protocol;
      this.code = var1.code;
      this.message = var1.message;
      this.handshake = var1.handshake;
      this.headers = var1.headers.build();
      this.body = var1.body;
      this.networkResponse = var1.networkResponse;
      this.cacheResponse = var1.cacheResponse;
      this.priorResponse = var1.priorResponse;
      this.sentRequestAtMillis = var1.sentRequestAtMillis;
      this.receivedResponseAtMillis = var1.receivedResponseAtMillis;
   }

   @Nullable
   public ResponseBody body() {
      return this.body;
   }

   public CacheControl cacheControl() {
      CacheControl var1 = this.cacheControl;
      if (var1 != null) {
         return var1;
      } else {
         var1 = CacheControl.parse(this.headers);
         this.cacheControl = var1;
         return var1;
      }
   }

   @Nullable
   public Response cacheResponse() {
      return this.cacheResponse;
   }

   public List challenges() {
      int var1 = this.code;
      String var2;
      if (var1 == 401) {
         var2 = "WWW-Authenticate";
      } else {
         if (var1 != 407) {
            return Collections.emptyList();
         }

         var2 = "Proxy-Authenticate";
      }

      return HttpHeaders.parseChallenges(this.headers(), var2);
   }

   public void close() {
      this.body.close();
   }

   public int code() {
      return this.code;
   }

   public Handshake handshake() {
      return this.handshake;
   }

   @Nullable
   public String header(String var1) {
      return this.header(var1, (String)null);
   }

   @Nullable
   public String header(String var1, @Nullable String var2) {
      var1 = this.headers.get(var1);
      return var1 != null ? var1 : var2;
   }

   public List headers(String var1) {
      return this.headers.values(var1);
   }

   public Headers headers() {
      return this.headers;
   }

   public boolean isRedirect() {
      int var1 = this.code;
      if (var1 != 307 && var1 != 308) {
         switch(var1) {
         case 300:
         case 301:
         case 302:
         case 303:
            break;
         default:
            return false;
         }
      }

      return true;
   }

   public boolean isSuccessful() {
      int var1 = this.code;
      return var1 >= 200 && var1 < 300;
   }

   public String message() {
      return this.message;
   }

   @Nullable
   public Response networkResponse() {
      return this.networkResponse;
   }

   public Response.Builder newBuilder() {
      return new Response.Builder(this);
   }

   public ResponseBody peekBody(long var1) throws IOException {
      BufferedSource var3 = this.body.source();
      var3.request(var1);
      Buffer var5 = var3.buffer().clone();
      if (var5.size() > var1) {
         Buffer var4 = new Buffer();
         var4.write(var5, var1);
         var5.clear();
         var5 = var4;
      }

      return ResponseBody.create(this.body.contentType(), var5.size(), var5);
   }

   @Nullable
   public Response priorResponse() {
      return this.priorResponse;
   }

   public Protocol protocol() {
      return this.protocol;
   }

   public long receivedResponseAtMillis() {
      return this.receivedResponseAtMillis;
   }

   public Request request() {
      return this.request;
   }

   public long sentRequestAtMillis() {
      return this.sentRequestAtMillis;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Response{protocol=");
      var1.append(this.protocol);
      var1.append(", code=");
      var1.append(this.code);
      var1.append(", message=");
      var1.append(this.message);
      var1.append(", url=");
      var1.append(this.request.url());
      var1.append('}');
      return var1.toString();
   }

   public static class Builder {
      ResponseBody body;
      Response cacheResponse;
      int code = -1;
      @Nullable
      Handshake handshake;
      Headers.Builder headers;
      String message;
      Response networkResponse;
      Response priorResponse;
      Protocol protocol;
      long receivedResponseAtMillis;
      Request request;
      long sentRequestAtMillis;

      public Builder() {
         this.headers = new Headers.Builder();
      }

      Builder(Response var1) {
         this.request = var1.request;
         this.protocol = var1.protocol;
         this.code = var1.code;
         this.message = var1.message;
         this.handshake = var1.handshake;
         this.headers = var1.headers.newBuilder();
         this.body = var1.body;
         this.networkResponse = var1.networkResponse;
         this.cacheResponse = var1.cacheResponse;
         this.priorResponse = var1.priorResponse;
         this.sentRequestAtMillis = var1.sentRequestAtMillis;
         this.receivedResponseAtMillis = var1.receivedResponseAtMillis;
      }

      private void checkPriorResponse(Response var1) {
         if (var1.body != null) {
            throw new IllegalArgumentException("priorResponse.body != null");
         }
      }

      private void checkSupportResponse(String var1, Response var2) {
         StringBuilder var3;
         if (var2.body == null) {
            if (var2.networkResponse == null) {
               if (var2.cacheResponse == null) {
                  if (var2.priorResponse != null) {
                     var3 = new StringBuilder();
                     var3.append(var1);
                     var3.append(".priorResponse != null");
                     throw new IllegalArgumentException(var3.toString());
                  }
               } else {
                  var3 = new StringBuilder();
                  var3.append(var1);
                  var3.append(".cacheResponse != null");
                  throw new IllegalArgumentException(var3.toString());
               }
            } else {
               var3 = new StringBuilder();
               var3.append(var1);
               var3.append(".networkResponse != null");
               throw new IllegalArgumentException(var3.toString());
            }
         } else {
            var3 = new StringBuilder();
            var3.append(var1);
            var3.append(".body != null");
            throw new IllegalArgumentException(var3.toString());
         }
      }

      public Response.Builder addHeader(String var1, String var2) {
         this.headers.add(var1, var2);
         return this;
      }

      public Response.Builder body(@Nullable ResponseBody var1) {
         this.body = var1;
         return this;
      }

      public Response build() {
         if (this.request != null) {
            if (this.protocol != null) {
               if (this.code >= 0) {
                  if (this.message != null) {
                     return new Response(this);
                  } else {
                     throw new IllegalStateException("message == null");
                  }
               } else {
                  StringBuilder var1 = new StringBuilder();
                  var1.append("code < 0: ");
                  var1.append(this.code);
                  throw new IllegalStateException(var1.toString());
               }
            } else {
               throw new IllegalStateException("protocol == null");
            }
         } else {
            throw new IllegalStateException("request == null");
         }
      }

      public Response.Builder cacheResponse(@Nullable Response var1) {
         if (var1 != null) {
            this.checkSupportResponse("cacheResponse", var1);
         }

         this.cacheResponse = var1;
         return this;
      }

      public Response.Builder code(int var1) {
         this.code = var1;
         return this;
      }

      public Response.Builder handshake(@Nullable Handshake var1) {
         this.handshake = var1;
         return this;
      }

      public Response.Builder header(String var1, String var2) {
         this.headers.set(var1, var2);
         return this;
      }

      public Response.Builder headers(Headers var1) {
         this.headers = var1.newBuilder();
         return this;
      }

      public Response.Builder message(String var1) {
         this.message = var1;
         return this;
      }

      public Response.Builder networkResponse(@Nullable Response var1) {
         if (var1 != null) {
            this.checkSupportResponse("networkResponse", var1);
         }

         this.networkResponse = var1;
         return this;
      }

      public Response.Builder priorResponse(@Nullable Response var1) {
         if (var1 != null) {
            this.checkPriorResponse(var1);
         }

         this.priorResponse = var1;
         return this;
      }

      public Response.Builder protocol(Protocol var1) {
         this.protocol = var1;
         return this;
      }

      public Response.Builder receivedResponseAtMillis(long var1) {
         this.receivedResponseAtMillis = var1;
         return this;
      }

      public Response.Builder removeHeader(String var1) {
         this.headers.removeAll(var1);
         return this;
      }

      public Response.Builder request(Request var1) {
         this.request = var1;
         return this;
      }

      public Response.Builder sentRequestAtMillis(long var1) {
         this.sentRequestAtMillis = var1;
         return this;
      }
   }
}
