// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;

public final class ConnectInterceptor implements Interceptor
{
    public final OkHttpClient client;
    
    public ConnectInterceptor(final OkHttpClient client) {
        this.client = client;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)chain;
        final Request request = realInterceptorChain.request();
        final StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        return realInterceptorChain.proceed(request, streamAllocation, streamAllocation.newStream(this.client, chain, !request.method().equals("GET")), streamAllocation.connection());
    }
}
