package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
   private final CookieJar cookieJar;

   public BridgeInterceptor(CookieJar var1) {
      this.cookieJar = var1;
   }

   private String cookieHeader(List var1) {
      StringBuilder var4 = new StringBuilder();
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         if (var2 > 0) {
            var4.append("; ");
         }

         Cookie var5 = (Cookie)var1.get(var2);
         var4.append(var5.name());
         var4.append('=');
         var4.append(var5.value());
      }

      return var4.toString();
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      Request var6 = var1.request();
      Request.Builder var7 = var6.newBuilder();
      RequestBody var8 = var6.body();
      if (var8 != null) {
         MediaType var9 = var8.contentType();
         if (var9 != null) {
            var7.header("Content-Type", var9.toString());
         }

         long var4 = var8.contentLength();
         if (var4 != -1L) {
            var7.header("Content-Length", Long.toString(var4));
            var7.removeHeader("Transfer-Encoding");
         } else {
            var7.header("Transfer-Encoding", "chunked");
            var7.removeHeader("Content-Length");
         }
      }

      if (var6.header("Host") == null) {
         var7.header("Host", Util.hostHeader(var6.url(), false));
      }

      if (var6.header("Connection") == null) {
         var7.header("Connection", "Keep-Alive");
      }

      boolean var3 = false;
      boolean var2 = var3;
      if (var6.header("Accept-Encoding") == null) {
         var2 = var3;
         if (var6.header("Range") == null) {
            var2 = true;
            var7.header("Accept-Encoding", "gzip");
         }
      }

      List var14 = this.cookieJar.loadForRequest(var6.url());
      if (!var14.isEmpty()) {
         var7.header("Cookie", this.cookieHeader(var14));
      }

      if (var6.header("User-Agent") == null) {
         var7.header("User-Agent", Version.userAgent());
      }

      Response var10 = var1.proceed(var7.build());
      HttpHeaders.receiveHeaders(this.cookieJar, var6.url(), var10.headers());
      Response.Builder var12 = var10.newBuilder().request(var6);
      if (var2 && "gzip".equalsIgnoreCase(var10.header("Content-Encoding")) && HttpHeaders.hasBody(var10)) {
         GzipSource var13 = new GzipSource(var10.body().source());
         Headers var11 = var10.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
         var12.headers(var11);
         var12.body(new RealResponseBody(var11, Okio.buffer((Source)var13)));
      }

      return var12.build();
   }
}
