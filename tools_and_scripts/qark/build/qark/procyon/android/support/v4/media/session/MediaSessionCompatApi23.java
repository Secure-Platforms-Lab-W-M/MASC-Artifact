// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.os.Bundle;
import android.net.Uri;

class MediaSessionCompatApi23
{
    private MediaSessionCompatApi23() {
    }
    
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public interface Callback extends MediaSessionCompatApi21.Callback
    {
        void onPlayFromUri(final Uri p0, final Bundle p1);
    }
    
    static class CallbackProxy<T extends MediaSessionCompatApi23.Callback> extends MediaSessionCompatApi21.CallbackProxy<T>
    {
        public CallbackProxy(final T t) {
            super(t);
        }
        
        public void onPlayFromUri(final Uri uri, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaSessionCompatApi23.Callback)this.mCallback).onPlayFromUri(uri, bundle);
        }
    }
}
