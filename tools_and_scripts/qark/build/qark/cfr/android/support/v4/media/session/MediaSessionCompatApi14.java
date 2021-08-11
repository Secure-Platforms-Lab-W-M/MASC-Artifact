/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.media.AudioManager
 *  android.media.RemoteControlClient
 *  android.media.RemoteControlClient$MetadataEditor
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.os.Parcelable;

class MediaSessionCompatApi14 {
    private static final long ACTION_FAST_FORWARD = 64L;
    private static final long ACTION_PAUSE = 2L;
    private static final long ACTION_PLAY = 4L;
    private static final long ACTION_PLAY_PAUSE = 512L;
    private static final long ACTION_REWIND = 8L;
    private static final long ACTION_SKIP_TO_NEXT = 32L;
    private static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    private static final long ACTION_STOP = 1L;
    private static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    private static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    private static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    private static final String METADATA_KEY_ART = "android.media.metadata.ART";
    private static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    private static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    private static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    private static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    private static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    private static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    private static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    private static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    private static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    private static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    private static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    static final int RCC_PLAYSTATE_NONE = 0;
    static final int STATE_BUFFERING = 6;
    static final int STATE_CONNECTING = 8;
    static final int STATE_ERROR = 7;
    static final int STATE_FAST_FORWARDING = 4;
    static final int STATE_NONE = 0;
    static final int STATE_PAUSED = 2;
    static final int STATE_PLAYING = 3;
    static final int STATE_REWINDING = 5;
    static final int STATE_SKIPPING_TO_NEXT = 10;
    static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    static final int STATE_STOPPED = 1;

    MediaSessionCompatApi14() {
    }

    static void buildOldMetadata(Bundle bundle, RemoteControlClient.MetadataEditor metadataEditor) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey("android.media.metadata.ART")) {
            metadataEditor.putBitmap(100, (Bitmap)bundle.getParcelable("android.media.metadata.ART"));
        } else if (bundle.containsKey("android.media.metadata.ALBUM_ART")) {
            metadataEditor.putBitmap(100, (Bitmap)bundle.getParcelable("android.media.metadata.ALBUM_ART"));
        }
        if (bundle.containsKey("android.media.metadata.ALBUM")) {
            metadataEditor.putString(1, bundle.getString("android.media.metadata.ALBUM"));
        }
        if (bundle.containsKey("android.media.metadata.ALBUM_ARTIST")) {
            metadataEditor.putString(13, bundle.getString("android.media.metadata.ALBUM_ARTIST"));
        }
        if (bundle.containsKey("android.media.metadata.ARTIST")) {
            metadataEditor.putString(2, bundle.getString("android.media.metadata.ARTIST"));
        }
        if (bundle.containsKey("android.media.metadata.AUTHOR")) {
            metadataEditor.putString(3, bundle.getString("android.media.metadata.AUTHOR"));
        }
        if (bundle.containsKey("android.media.metadata.COMPILATION")) {
            metadataEditor.putString(15, bundle.getString("android.media.metadata.COMPILATION"));
        }
        if (bundle.containsKey("android.media.metadata.COMPOSER")) {
            metadataEditor.putString(4, bundle.getString("android.media.metadata.COMPOSER"));
        }
        if (bundle.containsKey("android.media.metadata.DATE")) {
            metadataEditor.putString(5, bundle.getString("android.media.metadata.DATE"));
        }
        if (bundle.containsKey("android.media.metadata.DISC_NUMBER")) {
            metadataEditor.putLong(14, bundle.getLong("android.media.metadata.DISC_NUMBER"));
        }
        if (bundle.containsKey("android.media.metadata.DURATION")) {
            metadataEditor.putLong(9, bundle.getLong("android.media.metadata.DURATION"));
        }
        if (bundle.containsKey("android.media.metadata.GENRE")) {
            metadataEditor.putString(6, bundle.getString("android.media.metadata.GENRE"));
        }
        if (bundle.containsKey("android.media.metadata.TITLE")) {
            metadataEditor.putString(7, bundle.getString("android.media.metadata.TITLE"));
        }
        if (bundle.containsKey("android.media.metadata.TRACK_NUMBER")) {
            metadataEditor.putLong(0, bundle.getLong("android.media.metadata.TRACK_NUMBER"));
        }
        if (bundle.containsKey("android.media.metadata.WRITER")) {
            metadataEditor.putString(11, bundle.getString("android.media.metadata.WRITER"));
        }
    }

    public static Object createRemoteControlClient(PendingIntent pendingIntent) {
        return new RemoteControlClient(pendingIntent);
    }

    static int getRccStateFromState(int n) {
        switch (n) {
            default: {
                return -1;
            }
            case 10: 
            case 11: {
                return 6;
            }
            case 9: {
                return 7;
            }
            case 7: {
                return 9;
            }
            case 6: 
            case 8: {
                return 8;
            }
            case 5: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                return 3;
            }
            case 2: {
                return 2;
            }
            case 1: {
                return 1;
            }
            case 0: 
        }
        return 0;
    }

    static int getRccTransportControlFlagsFromActions(long l) {
        int n = 0;
        if ((1L & l) != 0L) {
            n = 0 | 32;
        }
        int n2 = n;
        if ((2L & l) != 0L) {
            n2 = n | 16;
        }
        n = n2;
        if ((4L & l) != 0L) {
            n = n2 | 4;
        }
        n2 = n;
        if ((8L & l) != 0L) {
            n2 = n | 2;
        }
        n = n2;
        if ((16L & l) != 0L) {
            n = n2 | 1;
        }
        n2 = n;
        if ((32L & l) != 0L) {
            n2 = n | 128;
        }
        n = n2;
        if ((64L & l) != 0L) {
            n = n2 | 64;
        }
        n2 = n;
        if ((512L & l) != 0L) {
            n2 = n | 8;
        }
        return n2;
    }

    public static void registerRemoteControlClient(Context context, Object object) {
        ((AudioManager)context.getSystemService("audio")).registerRemoteControlClient((RemoteControlClient)object);
    }

    public static void setMetadata(Object object, Bundle bundle) {
        object = ((RemoteControlClient)object).editMetadata(true);
        MediaSessionCompatApi14.buildOldMetadata(bundle, (RemoteControlClient.MetadataEditor)object);
        object.apply();
    }

    public static void setState(Object object, int n) {
        ((RemoteControlClient)object).setPlaybackState(MediaSessionCompatApi14.getRccStateFromState(n));
    }

    public static void setTransportControlFlags(Object object, long l) {
        ((RemoteControlClient)object).setTransportControlFlags(MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(l));
    }

    public static void unregisterRemoteControlClient(Context context, Object object) {
        ((AudioManager)context.getSystemService("audio")).unregisterRemoteControlClient((RemoteControlClient)object);
    }
}

