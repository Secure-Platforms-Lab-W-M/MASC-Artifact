/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.MediaSession
 *  android.net.Uri
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompatApi21;
import android.support.v4.media.session.MediaSessionCompatApi23;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MediaSessionCompatApi24 {
    private static final String TAG = "MediaSessionCompatApi24";

    private MediaSessionCompatApi24() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static String getCallingPackage(Object object) {
        void var0_4;
        object = (MediaSession)object;
        try {
            object = (String)object.getClass().getMethod("getCallingPackage", new Class[0]).invoke(object, new Object[0]);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        Log.e((String)"MediaSessionCompatApi24", (String)"Cannot execute MediaSession.getCallingPackage()", (Throwable)var0_4);
        return null;
    }

    public static interface Callback
    extends MediaSessionCompatApi23.Callback {
        public void onPrepare();

        public void onPrepareFromMediaId(String var1, Bundle var2);

        public void onPrepareFromSearch(String var1, Bundle var2);

        public void onPrepareFromUri(Uri var1, Bundle var2);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaSessionCompatApi23.CallbackProxy<T> {
        public CallbackProxy(T t) {
            super(t);
        }

        public void onPrepare() {
            ((Callback)this.mCallback).onPrepare();
        }

        public void onPrepareFromMediaId(String string, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((Callback)this.mCallback).onPrepareFromMediaId(string, bundle);
        }

        public void onPrepareFromSearch(String string, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((Callback)this.mCallback).onPrepareFromSearch(string, bundle);
        }

        public void onPrepareFromUri(Uri uri, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((Callback)this.mCallback).onPrepareFromUri(uri, bundle);
        }
    }

}

