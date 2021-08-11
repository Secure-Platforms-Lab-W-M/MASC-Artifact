package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class CacheInterceptor implements Interceptor {
   final InternalCache cache;

   public CacheInterceptor(InternalCache var1) {
      this.cache = var1;
   }

   private Response cacheWritingResponse(final CacheRequest var1, Response var2) throws IOException {
      if (var1 == null) {
         return var2;
      } else {
         Sink var3 = var1.body();
         if (var3 == null) {
            return var2;
         } else {
            Source var4 = new Source(var2.body().source(), Okio.buffer(var3)) {
               boolean cacheRequestClosed;
               // $FF: synthetic field
               final BufferedSink val$cacheBody;
               // $FF: synthetic field
               final BufferedSource val$source;

               {
                  this.val$source = var2;
                  this.val$cacheBody = var4;
               }

               public void close() throws IOException {
                  if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                     this.cacheRequestClosed = true;
                     var1.abort();
                  }

                  this.val$source.close();
               }

               public long read(Buffer var1x, long var2) throws IOException {
                  try {
                     var2 = this.val$source.read(var1x, var2);
                  } catch (IOException var4) {
                     if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        var1.abort();
                     }

                     throw var4;
                  }

                  if (var2 == -1L) {
                     if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        this.val$cacheBody.close();
                     }

                     return -1L;
                  } else {
                     var1x.copyTo(this.val$cacheBody.buffer(), var1x.size() - var2, var2);
                     this.val$cacheBody.emitCompleteSegments();
                     return var2;
                  }
               }

               public Timeout timeout() {
                  return this.val$source.timeout();
               }
            };
            return var2.newBuilder().body(new RealResponseBody(var2.headers(), Okio.buffer(var4))).build();
         }
      }
   }

   private static Headers combine(Headers var0, Headers var1) {
      Headers.Builder var4 = new Headers.Builder();
      int var2 = 0;

      int var3;
      for(var3 = var0.size(); var2 < var3; ++var2) {
         String var5 = var0.name(var2);
         String var6 = var0.value(var2);
         if ((!"Warning".equalsIgnoreCase(var5) || !var6.startsWith("1")) && (!isEndToEnd(var5) || var1.get(var5) == null)) {
            Internal.instance.addLenient(var4, var5, var6);
         }
      }

      var2 = 0;

      for(var3 = var1.size(); var2 < var3; ++var2) {
         String var7 = var1.name(var2);
         if (!"Content-Length".equalsIgnoreCase(var7) && isEndToEnd(var7)) {
            Internal.instance.addLenient(var4, var7, var1.value(var2));
         }
      }

      return var4.build();
   }

   static boolean isEndToEnd(String var0) {
      return !"Connection".equalsIgnoreCase(var0) && !"Keep-Alive".equalsIgnoreCase(var0) && !"Proxy-Authenticate".equalsIgnoreCase(var0) && !"Proxy-Authorization".equalsIgnoreCase(var0) && !"TE".equalsIgnoreCase(var0) && !"Trailers".equalsIgnoreCase(var0) && !"Transfer-Encoding".equalsIgnoreCase(var0) && !"Upgrade".equalsIgnoreCase(var0);
   }

   private static Response stripBody(Response var0) {
      return var0 != null && var0.body() != null ? var0.newBuilder().body((ResponseBody)null).build() : var0;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      InternalCache var2 = this.cache;
      Response var13;
      if (var2 != null) {
         var13 = var2.get(var1.request());
      } else {
         var13 = null;
      }

      CacheStrategy var5 = (new CacheStrategy.Factory(System.currentTimeMillis(), var1.request(), var13)).get();
      Request var3 = var5.networkRequest;
      Response var4 = var5.cacheResponse;
      InternalCache var6 = this.cache;
      if (var6 != null) {
         var6.trackResponse(var5);
      }

      if (var13 != null && var4 == null) {
         Util.closeQuietly((Closeable)var13.body());
      }

      if (var3 == null && var4 == null) {
         return (new Response.Builder()).request(var1.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
      } else if (var3 == null) {
         return var4.newBuilder().cacheResponse(stripBody(var4)).build();
      } else {
         boolean var9 = false;

         Response var12;
         try {
            var9 = true;
            var12 = var1.proceed(var3);
            var9 = false;
         } finally {
            if (var9) {
               if (true && var13 != null) {
                  Util.closeQuietly((Closeable)var13.body());
               }

            }
         }

         if (var12 == null && var13 != null) {
            Util.closeQuietly((Closeable)var13.body());
         }

         if (var4 != null) {
            if (var12.code() == 304) {
               var13 = var4.newBuilder().headers(combine(var4.headers(), var12.headers())).sentRequestAtMillis(var12.sentRequestAtMillis()).receivedResponseAtMillis(var12.receivedResponseAtMillis()).cacheResponse(stripBody(var4)).networkResponse(stripBody(var12)).build();
               var12.body().close();
               this.cache.trackConditionalCacheHit();
               this.cache.update(var4, var13);
               return var13;
            }

            Util.closeQuietly((Closeable)var4.body());
         }

         var12 = var12.newBuilder().cacheResponse(stripBody(var4)).networkResponse(stripBody(var12)).build();
         if (this.cache != null) {
            if (HttpHeaders.hasBody(var12) && CacheStrategy.isCacheable(var12, var3)) {
               return this.cacheWritingResponse(this.cache.put(var12), var12);
            }

            if (HttpMethod.invalidatesCache(var3.method())) {
               try {
                  this.cache.remove(var3);
                  return var12;
               } catch (IOException var11) {
               }
            }
         }

         return var12;
      }
   }
}
