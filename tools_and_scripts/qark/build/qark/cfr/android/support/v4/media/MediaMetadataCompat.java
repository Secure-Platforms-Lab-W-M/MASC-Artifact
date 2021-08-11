/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompatApi21;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadataCompat
implements Parcelable {
    public static final Parcelable.Creator<MediaMetadataCompat> CREATOR;
    static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;
    public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DOWNLOAD_STATUS = "android.media.metadata.DOWNLOAD_STATUS";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    static final int METADATA_TYPE_BITMAP = 2;
    static final int METADATA_TYPE_LONG = 0;
    static final int METADATA_TYPE_RATING = 3;
    static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER;
    private static final String[] PREFERRED_DESCRIPTION_ORDER;
    private static final String[] PREFERRED_URI_ORDER;
    private static final String TAG = "MediaMetadata";
    final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private Object mMetadataObj;

    static {
        Object object = new ArrayMap<String, Integer>();
        METADATA_KEYS_TYPE = object;
        Integer n = 1;
        object.put((String)"android.media.metadata.TITLE", (Integer)n);
        METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", n);
        Object object2 = METADATA_KEYS_TYPE;
        object = 0;
        object2.put((String)"android.media.metadata.DURATION", (Object)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.DATE", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", n);
        ArrayMap<String, Integer> arrayMap = METADATA_KEYS_TYPE;
        object2 = 2;
        arrayMap.put("android.media.metadata.ART", (Integer)object2);
        METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", (Integer)object2);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", n);
        arrayMap = METADATA_KEYS_TYPE;
        Integer n2 = 3;
        arrayMap.put("android.media.metadata.USER_RATING", n2);
        METADATA_KEYS_TYPE.put("android.media.metadata.RATING", n2);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", (Integer)object2);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", n);
        METADATA_KEYS_TYPE.put("android.media.metadata.ADVERTISEMENT", (Integer)object);
        METADATA_KEYS_TYPE.put("android.media.metadata.DOWNLOAD_STATUS", (Integer)object);
        PREFERRED_DESCRIPTION_ORDER = new String[]{"android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER"};
        PREFERRED_BITMAP_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART"};
        PREFERRED_URI_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI"};
        CREATOR = new Parcelable.Creator<MediaMetadataCompat>(){

            public MediaMetadataCompat createFromParcel(Parcel parcel) {
                return new MediaMetadataCompat(parcel);
            }

            public MediaMetadataCompat[] newArray(int n) {
                return new MediaMetadataCompat[n];
            }
        };
    }

    MediaMetadataCompat(Bundle bundle) {
        this.mBundle = bundle = new Bundle(bundle);
        MediaSessionCompat.ensureClassLoader(bundle);
    }

    MediaMetadataCompat(Parcel parcel) {
        this.mBundle = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
    }

    public static MediaMetadataCompat fromMediaMetadata(Object object) {
        if (object != null && Build.VERSION.SDK_INT >= 21) {
            Parcel parcel = Parcel.obtain();
            MediaMetadataCompatApi21.writeToParcel(object, parcel, 0);
            parcel.setDataPosition(0);
            MediaMetadataCompat mediaMetadataCompat = (MediaMetadataCompat)CREATOR.createFromParcel(parcel);
            parcel.recycle();
            mediaMetadataCompat.mMetadataObj = object;
            return mediaMetadataCompat;
        }
        return null;
    }

    public boolean containsKey(String string) {
        return this.mBundle.containsKey(string);
    }

    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap(String string) {
        try {
            string = (Bitmap)this.mBundle.getParcelable(string);
            return string;
        }
        catch (Exception exception) {
            Log.w((String)"MediaMetadata", (String)"Failed to retrieve a key as Bitmap.", (Throwable)exception);
            return null;
        }
    }

    public Bundle getBundle() {
        return new Bundle(this.mBundle);
    }

    public MediaDescriptionCompat getDescription() {
        Object object;
        int n;
        Object object2 = this.mDescription;
        if (object2 != null) {
            return object2;
        }
        String string = this.getString("android.media.metadata.MEDIA_ID");
        CharSequence[] arrcharSequence = new CharSequence[3];
        String string2 = null;
        String string3 = null;
        object2 = this.getText("android.media.metadata.DISPLAY_TITLE");
        if (!TextUtils.isEmpty((CharSequence)object2)) {
            arrcharSequence[0] = object2;
            arrcharSequence[1] = this.getText("android.media.metadata.DISPLAY_SUBTITLE");
            arrcharSequence[2] = this.getText("android.media.metadata.DISPLAY_DESCRIPTION");
        } else {
            int n2 = 0;
            for (n = 0; n2 < arrcharSequence.length && n < (object2 = PREFERRED_DESCRIPTION_ORDER).length; ++n) {
                object2 = this.getText(object2[n]);
                int n3 = n2;
                if (!TextUtils.isEmpty((CharSequence)object2)) {
                    arrcharSequence[n2] = object2;
                    n3 = n2 + 1;
                }
                n2 = n3;
            }
        }
        n = 0;
        do {
            object = PREFERRED_BITMAP_ORDER;
            object2 = string2;
            if (n >= object.length || (object2 = this.getBitmap(object[n])) != null) break;
            ++n;
        } while (true);
        n = 0;
        do {
            object = PREFERRED_URI_ORDER;
            string2 = string3;
            if (n >= object.length) break;
            string2 = this.getString(object[n]);
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                string2 = Uri.parse((String)string2);
                break;
            }
            ++n;
        } while (true);
        string3 = null;
        object = this.getString("android.media.metadata.MEDIA_URI");
        if (!TextUtils.isEmpty((CharSequence)object)) {
            string3 = Uri.parse((String)object);
        }
        object = new MediaDescriptionCompat.Builder();
        object.setMediaId(string);
        object.setTitle(arrcharSequence[0]);
        object.setSubtitle(arrcharSequence[1]);
        object.setDescription(arrcharSequence[2]);
        object.setIconBitmap((Bitmap)object2);
        object.setIconUri((Uri)string2);
        object.setMediaUri((Uri)string3);
        object2 = new Bundle();
        if (this.mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE")) {
            object2.putLong("android.media.extra.BT_FOLDER_TYPE", this.getLong("android.media.metadata.BT_FOLDER_TYPE"));
        }
        if (this.mBundle.containsKey("android.media.metadata.DOWNLOAD_STATUS")) {
            object2.putLong("android.media.extra.DOWNLOAD_STATUS", this.getLong("android.media.metadata.DOWNLOAD_STATUS"));
        }
        if (!object2.isEmpty()) {
            object.setExtras((Bundle)object2);
        }
        this.mDescription = object2 = object.build();
        return object2;
    }

    public long getLong(String string) {
        return this.mBundle.getLong(string, 0L);
    }

    public Object getMediaMetadata() {
        if (this.mMetadataObj == null && Build.VERSION.SDK_INT >= 21) {
            Parcel parcel = Parcel.obtain();
            this.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(parcel);
            parcel.recycle();
        }
        return this.mMetadataObj;
    }

    public RatingCompat getRating(String object) {
        try {
            object = Build.VERSION.SDK_INT >= 19 ? RatingCompat.fromRating((Object)this.mBundle.getParcelable((String)object)) : (RatingCompat)this.mBundle.getParcelable((String)object);
            return object;
        }
        catch (Exception exception) {
            Log.w((String)"MediaMetadata", (String)"Failed to retrieve a key as Rating.", (Throwable)exception);
            return null;
        }
    }

    public String getString(String charSequence) {
        if ((charSequence = this.mBundle.getCharSequence((String)charSequence)) != null) {
            return charSequence.toString();
        }
        return null;
    }

    public CharSequence getText(String string) {
        return this.mBundle.getCharSequence(string);
    }

    public Set<String> keySet() {
        return this.mBundle.keySet();
    }

    public int size() {
        return this.mBundle.size();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeBundle(this.mBundle);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface BitmapKey {
    }

    public static final class Builder {
        private final Bundle mBundle;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaMetadataCompat mediaMetadataCompat) {
            mediaMetadataCompat = new Bundle(mediaMetadataCompat.mBundle);
            this.mBundle = mediaMetadataCompat;
            MediaSessionCompat.ensureClassLoader((Bundle)mediaMetadataCompat);
        }

        public Builder(MediaMetadataCompat object, int n) {
            this((MediaMetadataCompat)object);
            for (String string : this.mBundle.keySet()) {
                Object object2 = this.mBundle.get(string);
                if (!(object2 instanceof Bitmap) || (object2 = (Bitmap)object2).getHeight() <= n && object2.getWidth() <= n) continue;
                this.putBitmap(string, this.scaleBitmap((Bitmap)object2, n));
            }
        }

        private Bitmap scaleBitmap(Bitmap bitmap, int n) {
            float f = n;
            f = Math.min(f / (float)bitmap.getWidth(), f / (float)bitmap.getHeight());
            n = (int)((float)bitmap.getHeight() * f);
            return Bitmap.createScaledBitmap((Bitmap)bitmap, (int)((int)((float)bitmap.getWidth() * f)), (int)n, (boolean)true);
        }

        public MediaMetadataCompat build() {
            return new MediaMetadataCompat(this.mBundle);
        }

        public Builder putBitmap(String string, Bitmap object) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(string) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(string) != 2) {
                object = new StringBuilder();
                object.append("The ");
                object.append(string);
                object.append(" key cannot be used to put a Bitmap");
                throw new IllegalArgumentException(object.toString());
            }
            this.mBundle.putParcelable(string, (Parcelable)object);
            return this;
        }

        public Builder putLong(String string, long l) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(string) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(string) != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The ");
                stringBuilder.append(string);
                stringBuilder.append(" key cannot be used to put a long");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mBundle.putLong(string, l);
            return this;
        }

        public Builder putRating(String string, RatingCompat object) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(string) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(string) != 3) {
                object = new StringBuilder();
                object.append("The ");
                object.append(string);
                object.append(" key cannot be used to put a Rating");
                throw new IllegalArgumentException(object.toString());
            }
            if (Build.VERSION.SDK_INT >= 19) {
                this.mBundle.putParcelable(string, (Parcelable)object.getRating());
                return this;
            }
            this.mBundle.putParcelable(string, (Parcelable)object);
            return this;
        }

        public Builder putString(String string, String charSequence) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(string) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(string) != 1) {
                charSequence = new StringBuilder();
                charSequence.append("The ");
                charSequence.append(string);
                charSequence.append(" key cannot be used to put a String");
                throw new IllegalArgumentException(charSequence.toString());
            }
            this.mBundle.putCharSequence(string, charSequence);
            return this;
        }

        public Builder putText(String string, CharSequence charSequence) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(string) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(string) != 1) {
                charSequence = new StringBuilder();
                charSequence.append("The ");
                charSequence.append(string);
                charSequence.append(" key cannot be used to put a CharSequence");
                throw new IllegalArgumentException(charSequence.toString());
            }
            this.mBundle.putCharSequence(string, charSequence);
            return this;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface LongKey {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface RatingKey {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TextKey {
    }

}

