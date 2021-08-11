/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.MediaSession
 */
package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompatApi21;
import android.support.v4.media.session.MediaSessionCompatApi24;

class MediaSessionCompatApi26 {
    MediaSessionCompatApi26() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static void setRepeatMode(Object object, int n) {
        ((MediaSession)object).setRepeatMode(n);
    }

    public static void setShuffleModeEnabled(Object object, boolean bl) {
        ((MediaSession)object).setShuffleModeEnabled(bl);
    }

    public static interface Callback
    extends MediaSessionCompatApi24.Callback {
        public void onSetRepeatMode(int var1);

        public void onSetShuffleModeEnabled(boolean var1);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaSessionCompatApi24.CallbackProxy<T> {
        CallbackProxy(T t) {
            super(t);
        }

        public void onSetRepeatMode(int n) {
            ((Callback)this.mCallback).onSetRepeatMode(n);
        }

        public void onSetShuffleModeEnabled(boolean bl) {
            ((Callback)this.mCallback).onSetShuffleModeEnabled(bl);
        }
    }

}

