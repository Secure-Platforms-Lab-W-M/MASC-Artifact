package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor.Chain {
   private int calls;
   private final RealConnection connection;
   private final HttpCodec httpCodec;
   private final int index;
   private final List interceptors;
   private final Request request;
   private final StreamAllocation streamAllocation;

   public RealInterceptorChain(List var1, StreamAllocation var2, HttpCodec var3, RealConnection var4, int var5, Request var6) {
      this.interceptors = var1;
      this.connection = var4;
      this.streamAllocation = var2;
      this.httpCodec = var3;
      this.index = var5;
      this.request = var6;
   }

   public Connection connection() {
      return this.connection;
   }

   public HttpCodec httpStream() {
      return this.httpCodec;
   }

   public Response proceed(Request var1) throws IOException {
      return this.proceed(var1, this.streamAllocation, this.httpCodec, this.connection);
   }

   public Response proceed(Request var1, StreamAllocation var2, HttpCodec var3, RealConnection var4) throws IOException {
      if (this.index < this.interceptors.size()) {
         ++this.calls;
         StringBuilder var6;
         if (this.httpCodec != null && !this.connection.supportsUrl(var1.url())) {
            var6 = new StringBuilder();
            var6.append("network interceptor ");
            var6.append(this.interceptors.get(this.index - 1));
            var6.append(" must retain the same host and port");
            throw new IllegalStateException(var6.toString());
         } else if (this.httpCodec != null && this.calls > 1) {
            var6 = new StringBuilder();
            var6.append("network interceptor ");
            var6.append(this.interceptors.get(this.index - 1));
            var6.append(" must call proceed() exactly once");
            throw new IllegalStateException(var6.toString());
         } else {
            RealInterceptorChain var7 = new RealInterceptorChain(this.interceptors, var2, var3, var4, this.index + 1, var1);
            Interceptor var5 = (Interceptor)this.interceptors.get(this.index);
            Response var9 = var5.intercept(var7);
            StringBuilder var8;
            if (var3 != null && this.index + 1 < this.interceptors.size() && var7.calls != 1) {
               var8 = new StringBuilder();
               var8.append("network interceptor ");
               var8.append(var5);
               var8.append(" must call proceed() exactly once");
               throw new IllegalStateException(var8.toString());
            } else if (var9 != null) {
               return var9;
            } else {
               var8 = new StringBuilder();
               var8.append("interceptor ");
               var8.append(var5);
               var8.append(" returned null");
               throw new NullPointerException(var8.toString());
            }
         }
      } else {
         throw new AssertionError();
      }
   }

   public Request request() {
      return this.request;
   }

   public StreamAllocation streamAllocation() {
      return this.streamAllocation;
   }
}
