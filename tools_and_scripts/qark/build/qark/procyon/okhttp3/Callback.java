// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.io.IOException;

public interface Callback
{
    void onFailure(final Call p0, final IOException p1);
    
    void onResponse(final Call p0, final Response p1) throws IOException;
}
