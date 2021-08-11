// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import android.media.session.MediaSession;

class MediaSessionCompatApi24
{
    private static final String TAG = "MediaSessionCompatApi24";
    
    private MediaSessionCompatApi24() {
    }
    
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public static String getCallingPackage(Object ex) {
        ex = ex;
        try {
            ex = (IllegalAccessException)ex.getClass().getMethod("getCallingPackage", (Class<?>[])new Class[0]).invoke(ex, new Object[0]);
            return (String)ex;
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex) {}
        catch (NoSuchMethodException ex2) {}
        Log.e("MediaSessionCompatApi24", "Cannot execute MediaSession.getCallingPackage()", (Throwable)ex);
        return null;
    }
    
    public interface Callback extends MediaSessionCompatApi23.Callback
    {
        void onPrepare();
        
        void onPrepareFromMediaId(final String p0, final Bundle p1);
        
        void onPrepareFromSearch(final String p0, final Bundle p1);
        
        void onPrepareFromUri(final Uri p0, final Bundle p1);
    }
    
    static class CallbackProxy<T extends MediaSessionCompatApi24.Callback> extends MediaSessionCompatApi23.CallbackProxy<T>
    {
        public CallbackProxy(final T t) {
            super(t);
        }
        
        public void onPrepare() {
            ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepare();
        }
        
        public void onPrepareFromMediaId(final String s, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromMediaId(s, bundle);
        }
        
        public void onPrepareFromSearch(final String s, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromSearch(s, bundle);
        }
        
        public void onPrepareFromUri(final Uri uri, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaSessionCompatApi24.Callback)this.mCallback).onPrepareFromUri(uri, bundle);
        }
    }
}
