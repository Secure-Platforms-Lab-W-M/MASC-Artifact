/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.MediaDescription
 *  android.media.MediaDescription$Builder
 *  android.net.Uri
 */
package android.support.v4.media;

import android.media.MediaDescription;
import android.net.Uri;

class MediaDescriptionCompatApi23 {
    private MediaDescriptionCompatApi23() {
    }

    public static Uri getMediaUri(Object object) {
        return ((MediaDescription)object).getMediaUri();
    }

    static class Builder {
        private Builder() {
        }

        public static void setMediaUri(Object object, Uri uri) {
            ((MediaDescription.Builder)object).setMediaUri(uri);
        }
    }

}

