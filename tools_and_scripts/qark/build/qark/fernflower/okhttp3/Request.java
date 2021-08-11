package okhttp3;

import java.net.URL;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;

public final class Request {
   @Nullable
   final RequestBody body;
   private volatile CacheControl cacheControl;
   final Headers headers;
   final String method;
   final Object tag;
   final HttpUrl url;

   Request(Request.Builder var1) {
      this.url = var1.url;
      this.method = var1.method;
      this.headers = var1.headers.build();
      this.body = var1.body;
      Object var2;
      if (var1.tag != null) {
         var2 = var1.tag;
      } else {
         var2 = this;
      }

      this.tag = var2;
   }

   @Nullable
   public RequestBody body() {
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

   public String header(String var1) {
      return this.headers.get(var1);
   }

   public List headers(String var1) {
      return this.headers.values(var1);
   }

   public Headers headers() {
      return this.headers;
   }

   public boolean isHttps() {
      return this.url.isHttps();
   }

   public String method() {
      return this.method;
   }

   public Request.Builder newBuilder() {
      return new Request.Builder(this);
   }

   public Object tag() {
      return this.tag;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Request{method=");
      var2.append(this.method);
      var2.append(", url=");
      var2.append(this.url);
      var2.append(", tag=");
      Object var1 = this.tag;
      if (var1 == this) {
         var1 = null;
      }

      var2.append(var1);
      var2.append('}');
      return var2.toString();
   }

   public HttpUrl url() {
      return this.url;
   }

   public static class Builder {
      RequestBody body;
      Headers.Builder headers;
      String method;
      Object tag;
      HttpUrl url;

      public Builder() {
         this.method = "GET";
         this.headers = new Headers.Builder();
      }

      Builder(Request var1) {
         this.url = var1.url;
         this.method = var1.method;
         this.body = var1.body;
         this.tag = var1.tag;
         this.headers = var1.headers.newBuilder();
      }

      public Request.Builder addHeader(String var1, String var2) {
         this.headers.add(var1, var2);
         return this;
      }

      public Request build() {
         if (this.url != null) {
            return new Request(this);
         } else {
            throw new IllegalStateException("url == null");
         }
      }

      public Request.Builder cacheControl(CacheControl var1) {
         String var2 = var1.toString();
         return var2.isEmpty() ? this.removeHeader("Cache-Control") : this.header("Cache-Control", var2);
      }

      public Request.Builder delete() {
         return this.delete(Util.EMPTY_REQUEST);
      }

      public Request.Builder delete(@Nullable RequestBody var1) {
         return this.method("DELETE", var1);
      }

      public Request.Builder get() {
         return this.method("GET", (RequestBody)null);
      }

      public Request.Builder head() {
         return this.method("HEAD", (RequestBody)null);
      }

      public Request.Builder header(String var1, String var2) {
         this.headers.set(var1, var2);
         return this;
      }

      public Request.Builder headers(Headers var1) {
         this.headers = var1.newBuilder();
         return this;
      }

      public Request.Builder method(String var1, @Nullable RequestBody var2) {
         if (var1 != null) {
            if (var1.length() != 0) {
               StringBuilder var3;
               if (var2 != null && !HttpMethod.permitsRequestBody(var1)) {
                  var3 = new StringBuilder();
                  var3.append("method ");
                  var3.append(var1);
                  var3.append(" must not have a request body.");
                  throw new IllegalArgumentException(var3.toString());
               } else if (var2 == null && HttpMethod.requiresRequestBody(var1)) {
                  var3 = new StringBuilder();
                  var3.append("method ");
                  var3.append(var1);
                  var3.append(" must have a request body.");
                  throw new IllegalArgumentException(var3.toString());
               } else {
                  this.method = var1;
                  this.body = var2;
                  return this;
               }
            } else {
               throw new IllegalArgumentException("method.length() == 0");
            }
         } else {
            throw new NullPointerException("method == null");
         }
      }

      public Request.Builder patch(RequestBody var1) {
         return this.method("PATCH", var1);
      }

      public Request.Builder post(RequestBody var1) {
         return this.method("POST", var1);
      }

      public Request.Builder put(RequestBody var1) {
         return this.method("PUT", var1);
      }

      public Request.Builder removeHeader(String var1) {
         this.headers.removeAll(var1);
         return this;
      }

      public Request.Builder tag(Object var1) {
         this.tag = var1;
         return this;
      }

      public Request.Builder url(String var1) {
         if (var1 != null) {
            StringBuilder var2;
            String var5;
            if (var1.regionMatches(true, 0, "ws:", 0, 3)) {
               var2 = new StringBuilder();
               var2.append("http:");
               var2.append(var1.substring(3));
               var5 = var2.toString();
            } else {
               var5 = var1;
               if (var1.regionMatches(true, 0, "wss:", 0, 4)) {
                  var2 = new StringBuilder();
                  var2.append("https:");
                  var2.append(var1.substring(4));
                  var5 = var2.toString();
               }
            }

            HttpUrl var3 = HttpUrl.parse(var5);
            if (var3 != null) {
               return this.url(var3);
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("unexpected url: ");
               var4.append(var5);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            throw new NullPointerException("url == null");
         }
      }

      public Request.Builder url(URL var1) {
         if (var1 != null) {
            HttpUrl var2 = HttpUrl.get(var1);
            if (var2 != null) {
               return this.url(var2);
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append("unexpected url: ");
               var3.append(var1);
               throw new IllegalArgumentException(var3.toString());
            }
         } else {
            throw new NullPointerException("url == null");
         }
      }

      public Request.Builder url(HttpUrl var1) {
         if (var1 != null) {
            this.url = var1;
            return this;
         } else {
            throw new NullPointerException("url == null");
         }
      }
   }
}
