// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.Request;
import java.io.IOException;

public interface Call<T> extends Cloneable
{
    void cancel();
    
    Call<T> clone();
    
    void enqueue(final Callback<T> p0);
    
    Response<T> execute() throws IOException;
    
    boolean isCanceled();
    
    boolean isExecuted();
    
    Request request();
}
