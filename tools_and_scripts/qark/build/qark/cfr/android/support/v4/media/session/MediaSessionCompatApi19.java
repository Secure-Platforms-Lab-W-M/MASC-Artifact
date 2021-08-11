/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.MediaMetadataEditor
 *  android.media.Rating
 *  android.media.RemoteControlClient
 *  android.media.RemoteControlClient$MetadataEditor
 *  android.media.RemoteControlClient$OnMetadataUpdateListener
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package android.support.v4.media.session;

import android.media.MediaMetadataEditor;
import android.media.Rating;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompatApi14;
import android.support.v4.media.session.MediaSessionCompatApi18;

class MediaSessionCompatApi19 {
    private static final long ACTION_SET_RATING = 128L;
    private static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    private static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    private static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";

    MediaSessionCompatApi19() {
    }

    static void addNewMetadata(Bundle bundle, RemoteControlClient.MetadataEditor metadataEditor) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey("android.media.metadata.YEAR")) {
            metadataEditor.putLong(8, bundle.getLong("android.media.metadata.YEAR"));
        }
        if (bundle.containsKey("android.media.metadata.RATING")) {
            metadataEditor.putObject(101, (Object)bundle.getParcelable("android.media.metadata.RATING"));
        }
        if (bundle.containsKey("android.media.metadata.USER_RATING")) {
            metadataEditor.putObject(268435457, (Object)bundle.getParcelable("android.media.metadata.USER_RATING"));
        }
    }

    public static Object createMetadataUpdateListener(Callback callback) {
        return new OnMetadataUpdateListener<Callback>(callback);
    }

    static int getRccTransportControlFlagsFromActions(long l) {
        int n;
        int n2 = n = MediaSessionCompatApi18.getRccTransportControlFlagsFromActions(l);
        if ((128L & l) != 0L) {
            n2 = n | 512;
        }
        return n2;
    }

    public static void setMetadata(Object object, Bundle bundle, long l) {
        object = ((RemoteControlClient)object).editMetadata(true);
        MediaSessionCompatApi14.buildOldMetadata(bundle, (RemoteControlClient.MetadataEditor)object);
        MediaSessionCompatApi19.addNewMetadata(bundle, (RemoteControlClient.MetadataEditor)object);
        if ((128L & l) != 0L) {
            object.addEditableKey(268435457);
        }
        object.apply();
    }

    public static void setOnMetadataUpdateListener(Object object, Object object2) {
        ((RemoteControlClient)object).setMetadataUpdateListener((RemoteControlClient.OnMetadataUpdateListener)object2);
    }

    public static void setTransportControlFlags(Object object, long l) {
        ((RemoteControlClient)object).setTransportControlFlags(MediaSessionCompatApi19.getRccTransportControlFlagsFromActions(l));
    }

    static interface Callback
    extends MediaSessionCompatApi18.Callback {
        public void onSetRating(Object var1);
    }

    static class OnMetadataUpdateListener<T extends Callback>
    implements RemoteControlClient.OnMetadataUpdateListener {
        protected final T mCallback;

        public OnMetadataUpdateListener(T t) {
            this.mCallback = t;
        }

        public void onMetadataUpdate(int n, Object object) {
            if (n == 268435457 && object instanceof Rating) {
                this.mCallback.onSetRating(object);
            }
        }
    }

}

