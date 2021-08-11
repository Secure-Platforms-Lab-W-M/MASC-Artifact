/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.media.MediaMetadata
 *  android.media.MediaMetadata$Builder
 *  android.media.Rating
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.support.v4.media;

import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Set;

class MediaMetadataCompatApi21 {
    private MediaMetadataCompatApi21() {
    }

    public static Object createFromParcel(Parcel parcel) {
        return MediaMetadata.CREATOR.createFromParcel(parcel);
    }

    public static Bitmap getBitmap(Object object, String string) {
        return ((MediaMetadata)object).getBitmap(string);
    }

    public static long getLong(Object object, String string) {
        return ((MediaMetadata)object).getLong(string);
    }

    public static Object getRating(Object object, String string) {
        return ((MediaMetadata)object).getRating(string);
    }

    public static CharSequence getText(Object object, String string) {
        return ((MediaMetadata)object).getText(string);
    }

    public static Set<String> keySet(Object object) {
        return ((MediaMetadata)object).keySet();
    }

    public static void writeToParcel(Object object, Parcel parcel, int n) {
        ((MediaMetadata)object).writeToParcel(parcel, n);
    }

    public static class Builder {
        private Builder() {
        }

        public static Object build(Object object) {
            return ((MediaMetadata.Builder)object).build();
        }

        public static Object newInstance() {
            return new MediaMetadata.Builder();
        }

        public static void putBitmap(Object object, String string, Bitmap bitmap) {
            ((MediaMetadata.Builder)object).putBitmap(string, bitmap);
        }

        public static void putLong(Object object, String string, long l) {
            ((MediaMetadata.Builder)object).putLong(string, l);
        }

        public static void putRating(Object object, String string, Object object2) {
            ((MediaMetadata.Builder)object).putRating(string, (Rating)object2);
        }

        public static void putString(Object object, String string, String string2) {
            ((MediaMetadata.Builder)object).putString(string, string2);
        }

        public static void putText(Object object, String string, CharSequence charSequence) {
            ((MediaMetadata.Builder)object).putText(string, charSequence);
        }
    }

}

