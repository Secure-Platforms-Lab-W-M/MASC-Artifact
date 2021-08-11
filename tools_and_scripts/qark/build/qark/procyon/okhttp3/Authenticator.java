// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import javax.annotation.Nullable;
import java.io.IOException;

public interface Authenticator
{
    public static final Authenticator NONE = new Authenticator() {
        @Override
        public Request authenticate(final Route route, final Response response) {
            return null;
        }
    };
    
    @Nullable
    Request authenticate(final Route p0, final Response p1) throws IOException;
}
