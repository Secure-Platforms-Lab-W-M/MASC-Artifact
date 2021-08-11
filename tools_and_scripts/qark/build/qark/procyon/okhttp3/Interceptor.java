// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import java.io.IOException;

public interface Interceptor
{
    Response intercept(final Chain p0) throws IOException;
    
    public interface Chain
    {
        Call call();
        
        int connectTimeoutMillis();
        
        @Nullable
        Connection connection();
        
        Response proceed(final Request p0) throws IOException;
        
        int readTimeoutMillis();
        
        Request request();
        
        Chain withConnectTimeout(final int p0, final TimeUnit p1);
        
        Chain withReadTimeout(final int p0, final TimeUnit p1);
        
        Chain withWriteTimeout(final int p0, final TimeUnit p1);
        
        int writeTimeoutMillis();
    }
}
