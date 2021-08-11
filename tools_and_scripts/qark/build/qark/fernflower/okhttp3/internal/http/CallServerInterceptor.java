package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;

public final class CallServerInterceptor implements Interceptor {
   private final boolean forWebSocket;

   public CallServerInterceptor(boolean var1) {
      this.forWebSocket = var1;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      RealInterceptorChain var11 = (RealInterceptorChain)var1;
      HttpCodec var8 = var11.httpStream();
      StreamAllocation var7 = var11.streamAllocation();
      RealConnection var10 = (RealConnection)var11.connection();
      Request var9 = var11.request();
      long var3 = System.currentTimeMillis();
      var8.writeRequestHeaders(var9);
      Object var6 = null;
      Response.Builder var5 = null;
      Response.Builder var12 = (Response.Builder)var6;
      if (HttpMethod.permitsRequestBody(var9.method())) {
         var12 = (Response.Builder)var6;
         if (var9.body() != null) {
            if ("100-continue".equalsIgnoreCase(var9.header("Expect"))) {
               var8.flushRequest();
               var5 = var8.readResponseHeaders(true);
            }

            if (var5 == null) {
               BufferedSink var13 = Okio.buffer(var8.createRequestBody(var9, var9.body().contentLength()));
               var9.body().writeTo(var13);
               var13.close();
               var12 = var5;
            } else {
               var12 = var5;
               if (!var10.isMultiplexed()) {
                  var7.noNewStreams();
                  var12 = var5;
               }
            }
         }
      }

      var8.finishRequest();
      var5 = var12;
      if (var12 == null) {
         var5 = var8.readResponseHeaders(false);
      }

      Response var14 = var5.request(var9).handshake(var7.connection().handshake()).sentRequestAtMillis(var3).receivedResponseAtMillis(System.currentTimeMillis()).build();
      int var2 = var14.code();
      if (this.forWebSocket && var2 == 101) {
         var14 = var14.newBuilder().body(Util.EMPTY_RESPONSE).build();
      } else {
         var14 = var14.newBuilder().body(var8.openResponseBody(var14)).build();
      }

      if ("close".equalsIgnoreCase(var14.request().header("Connection")) || "close".equalsIgnoreCase(var14.header("Connection"))) {
         var7.noNewStreams();
      }

      if ((var2 == 204 || var2 == 205) && var14.body().contentLength() > 0L) {
         StringBuilder var15 = new StringBuilder();
         var15.append("HTTP ");
         var15.append(var2);
         var15.append(" had non-zero Content-Length: ");
         var15.append(var14.body().contentLength());
         throw new ProtocolException(var15.toString());
      } else {
         return var14;
      }
   }
}
