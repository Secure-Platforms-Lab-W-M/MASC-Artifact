package okhttp3.internal.cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;

public final class CacheStrategy {
   @Nullable
   public final Response cacheResponse;
   @Nullable
   public final Request networkRequest;

   CacheStrategy(Request var1, Response var2) {
      this.networkRequest = var1;
      this.cacheResponse = var2;
   }

   public static boolean isCacheable(Response var0, Request var1) {
      int var2 = var0.code();
      boolean var4 = false;
      if (var2 != 200 && var2 != 410 && var2 != 414 && var2 != 501 && var2 != 203 && var2 != 204) {
         label72: {
            if (var2 != 307) {
               if (var2 == 308 || var2 == 404 || var2 == 405) {
                  break label72;
               }

               switch(var2) {
               case 300:
               case 301:
                  break label72;
               case 302:
                  break;
               default:
                  return false;
               }
            }

            if (var0.header("Expires") == null && var0.cacheControl().maxAgeSeconds() == -1 && !var0.cacheControl().isPublic() && !var0.cacheControl().isPrivate()) {
               return false;
            }
         }
      }

      boolean var3 = var4;
      if (!var0.cacheControl().noStore()) {
         var3 = var4;
         if (!var1.cacheControl().noStore()) {
            var3 = true;
         }
      }

      return var3;
   }

   public static class Factory {
      private int ageSeconds = -1;
      final Response cacheResponse;
      private String etag;
      private Date expires;
      private Date lastModified;
      private String lastModifiedString;
      final long nowMillis;
      private long receivedResponseMillis;
      final Request request;
      private long sentRequestMillis;
      private Date servedDate;
      private String servedDateString;

      public Factory(long var1, Request var3, Response var4) {
         this.nowMillis = var1;
         this.request = var3;
         this.cacheResponse = var4;
         if (var4 != null) {
            this.sentRequestMillis = var4.sentRequestAtMillis();
            this.receivedResponseMillis = var4.receivedResponseAtMillis();
            Headers var8 = var4.headers();
            int var5 = 0;

            for(int var6 = var8.size(); var5 < var6; ++var5) {
               String var9 = var8.name(var5);
               String var7 = var8.value(var5);
               if ("Date".equalsIgnoreCase(var9)) {
                  this.servedDate = HttpDate.parse(var7);
                  this.servedDateString = var7;
               } else if ("Expires".equalsIgnoreCase(var9)) {
                  this.expires = HttpDate.parse(var7);
               } else if ("Last-Modified".equalsIgnoreCase(var9)) {
                  this.lastModified = HttpDate.parse(var7);
                  this.lastModifiedString = var7;
               } else if ("ETag".equalsIgnoreCase(var9)) {
                  this.etag = var7;
               } else if ("Age".equalsIgnoreCase(var9)) {
                  this.ageSeconds = HttpHeaders.parseSeconds(var7, -1);
               }
            }
         }

      }

      private long cacheResponseAge() {
         Date var5 = this.servedDate;
         long var1 = 0L;
         if (var5 != null) {
            var1 = Math.max(0L, this.receivedResponseMillis - var5.getTime());
         }

         if (this.ageSeconds != -1) {
            var1 = Math.max(var1, TimeUnit.SECONDS.toMillis((long)this.ageSeconds));
         }

         long var3 = this.receivedResponseMillis;
         return var1 + (var3 - this.sentRequestMillis) + (this.nowMillis - var3);
      }

      private long computeFreshnessLifetime() {
         CacheControl var5 = this.cacheResponse.cacheControl();
         if (var5.maxAgeSeconds() != -1) {
            return TimeUnit.SECONDS.toMillis((long)var5.maxAgeSeconds());
         } else {
            Date var6 = this.expires;
            long var1 = 0L;
            long var3;
            if (var6 != null) {
               var6 = this.servedDate;
               if (var6 != null) {
                  var3 = var6.getTime();
               } else {
                  var3 = this.receivedResponseMillis;
               }

               var3 = this.expires.getTime() - var3;
               if (var3 > 0L) {
                  var1 = var3;
               }

               return var1;
            } else if (this.lastModified != null && this.cacheResponse.request().url().query() == null) {
               var6 = this.servedDate;
               if (var6 != null) {
                  var3 = var6.getTime();
               } else {
                  var3 = this.sentRequestMillis;
               }

               var3 -= this.lastModified.getTime();
               if (var3 > 0L) {
                  var1 = var3 / 10L;
               }

               return var1;
            } else {
               return 0L;
            }
         }
      }

      private CacheStrategy getCandidate() {
         if (this.cacheResponse == null) {
            return new CacheStrategy(this.request, (Response)null);
         } else if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
            return new CacheStrategy(this.request, (Response)null);
         } else if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
            return new CacheStrategy(this.request, (Response)null);
         } else {
            CacheControl var11 = this.request.cacheControl();
            if (!var11.noCache() && !hasConditions(this.request)) {
               long var9 = this.cacheResponseAge();
               long var3 = this.computeFreshnessLifetime();
               long var1 = var3;
               if (var11.maxAgeSeconds() != -1) {
                  var1 = Math.min(var3, TimeUnit.SECONDS.toMillis((long)var11.maxAgeSeconds()));
               }

               var3 = 0L;
               if (var11.minFreshSeconds() != -1) {
                  var3 = TimeUnit.SECONDS.toMillis((long)var11.minFreshSeconds());
               }

               long var7 = 0L;
               CacheControl var12 = this.cacheResponse.cacheControl();
               long var5 = var7;
               if (!var12.mustRevalidate()) {
                  var5 = var7;
                  if (var11.maxStaleSeconds() != -1) {
                     var5 = TimeUnit.SECONDS.toMillis((long)var11.maxStaleSeconds());
                  }
               }

               if (!var12.noCache() && var9 + var3 < var1 + var5) {
                  Response.Builder var16 = this.cacheResponse.newBuilder();
                  if (var9 + var3 >= var1) {
                     var16.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                  }

                  if (var9 > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                     var16.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                  }

                  return new CacheStrategy((Request)null, var16.build());
               } else {
                  String var14;
                  String var15;
                  if (this.etag != null) {
                     var14 = "If-None-Match";
                     var15 = this.etag;
                  } else if (this.lastModified != null) {
                     var14 = "If-Modified-Since";
                     var15 = this.lastModifiedString;
                  } else {
                     if (this.servedDate == null) {
                        return new CacheStrategy(this.request, (Response)null);
                     }

                     var14 = "If-Modified-Since";
                     var15 = this.servedDateString;
                  }

                  Headers.Builder var13 = this.request.headers().newBuilder();
                  Internal.instance.addLenient(var13, var14, var15);
                  return new CacheStrategy(this.request.newBuilder().headers(var13.build()).build(), this.cacheResponse);
               }
            } else {
               return new CacheStrategy(this.request, (Response)null);
            }
         }
      }

      private static boolean hasConditions(Request var0) {
         return var0.header("If-Modified-Since") != null || var0.header("If-None-Match") != null;
      }

      private boolean isFreshnessLifetimeHeuristic() {
         return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
      }

      public CacheStrategy get() {
         CacheStrategy var1 = this.getCandidate();
         return var1.networkRequest != null && this.request.cacheControl().onlyIfCached() ? new CacheStrategy((Request)null, (Response)null) : var1;
      }
   }
}
