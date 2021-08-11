/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.MediaController
 *  android.media.session.MediaController$TransportControls
 */
package android.support.v4.media.session;

import android.media.session.MediaController;
import android.support.v4.media.session.MediaControllerCompatApi21;
import android.support.v4.media.session.MediaControllerCompatApi23;

class MediaControllerCompatApi26 {
    MediaControllerCompatApi26() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static int getRepeatMode(Object object) {
        return ((MediaController)object).getRepeatMode();
    }

    public static boolean isShuffleModeEnabled(Object object) {
        return ((MediaController)object).isShuffleModeEnabled();
    }

    public static interface Callback
    extends MediaControllerCompatApi21.Callback {
        public void onRepeatModeChanged(int var1);

        public void onShuffleModeChanged(boolean var1);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaControllerCompatApi21.CallbackProxy<T> {
        CallbackProxy(T t) {
            super(t);
        }

        public void onRepeatModeChanged(int n) {
            ((Callback)this.mCallback).onRepeatModeChanged(n);
        }

        public void onShuffleModeChanged(boolean bl) {
            ((Callback)this.mCallback).onShuffleModeChanged(bl);
        }
    }

    public static class TransportControls
    extends MediaControllerCompatApi23.TransportControls {
        public static void setRepeatMode(Object object, int n) {
            ((MediaController.TransportControls)object).setRepeatMode(n);
        }

        public static void setShuffleModeEnabled(Object object, boolean bl) {
            ((MediaController.TransportControls)object).setShuffleModeEnabled(bl);
        }
    }

}

