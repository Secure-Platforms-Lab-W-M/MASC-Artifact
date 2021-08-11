/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.media.AudioManager
 *  android.media.RemoteControlClient
 *  android.media.RemoteControlClient$OnPlaybackPositionUpdateListener
 *  android.os.SystemClock
 *  android.util.Log
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompatApi14;
import android.util.Log;

class MediaSessionCompatApi18 {
    private static final long ACTION_SEEK_TO = 256L;
    private static final String TAG = "MediaSessionCompatApi18";
    private static boolean sIsMbrPendingIntentSupported = true;

    MediaSessionCompatApi18() {
    }

    public static Object createPlaybackPositionUpdateListener(Callback callback) {
        return new OnPlaybackPositionUpdateListener<Callback>(callback);
    }

    static int getRccTransportControlFlagsFromActions(long l) {
        int n;
        int n2 = n = MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(l);
        if ((256L & l) != 0L) {
            n2 = n | 256;
        }
        return n2;
    }

    public static void registerMediaButtonEventReceiver(Context context, PendingIntent pendingIntent, ComponentName componentName) {
        context = (AudioManager)context.getSystemService("audio");
        if (sIsMbrPendingIntentSupported) {
            try {
                context.registerMediaButtonEventReceiver(pendingIntent);
            }
            catch (NullPointerException nullPointerException) {
                Log.w((String)"MediaSessionCompatApi18", (String)"Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
                sIsMbrPendingIntentSupported = false;
            }
        }
        if (!sIsMbrPendingIntentSupported) {
            context.registerMediaButtonEventReceiver(componentName);
        }
    }

    public static void setOnPlaybackPositionUpdateListener(Object object, Object object2) {
        ((RemoteControlClient)object).setPlaybackPositionUpdateListener((RemoteControlClient.OnPlaybackPositionUpdateListener)object2);
    }

    public static void setState(Object object, int n, long l, float f, long l2) {
        long l3 = SystemClock.elapsedRealtime();
        long l4 = l;
        if (n == 3) {
            l4 = l;
            if (l > 0L) {
                l4 = 0L;
                if (l2 > 0L) {
                    l4 = l2 = l3 - l2;
                    if (f > 0.0f) {
                        l4 = l2;
                        if (f != 1.0f) {
                            l4 = (long)((float)l2 * f);
                        }
                    }
                }
                l4 = l + l4;
            }
        }
        n = MediaSessionCompatApi14.getRccStateFromState(n);
        ((RemoteControlClient)object).setPlaybackState(n, l4, f);
    }

    public static void setTransportControlFlags(Object object, long l) {
        ((RemoteControlClient)object).setTransportControlFlags(MediaSessionCompatApi18.getRccTransportControlFlagsFromActions(l));
    }

    public static void unregisterMediaButtonEventReceiver(Context context, PendingIntent pendingIntent, ComponentName componentName) {
        context = (AudioManager)context.getSystemService("audio");
        if (sIsMbrPendingIntentSupported) {
            context.unregisterMediaButtonEventReceiver(pendingIntent);
            return;
        }
        context.unregisterMediaButtonEventReceiver(componentName);
    }

    static interface Callback {
        public void onSeekTo(long var1);
    }

    static class OnPlaybackPositionUpdateListener<T extends Callback>
    implements RemoteControlClient.OnPlaybackPositionUpdateListener {
        protected final T mCallback;

        public OnPlaybackPositionUpdateListener(T t) {
            this.mCallback = t;
        }

        public void onPlaybackPositionUpdate(long l) {
            this.mCallback.onSeekTo(l);
        }
    }

}

