package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;

public final class ConnectInterceptor implements Interceptor {
   public final OkHttpClient client;

   public ConnectInterceptor(OkHttpClient var1) {
      this.client = var1;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      RealInterceptorChain var5 = (RealInterceptorChain)var1;
      Request var3 = var5.request();
      StreamAllocation var4 = var5.streamAllocation();
      boolean var2 = var3.method().equals("GET");
      return var5.proceed(var3, var4, var4.newStream(this.client, var2 ^ true), var4.connection());
   }
}
