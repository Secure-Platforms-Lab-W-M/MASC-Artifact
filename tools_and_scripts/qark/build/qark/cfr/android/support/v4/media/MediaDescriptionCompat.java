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
 */
package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaDescriptionCompatApi21;
import android.support.v4.media.MediaDescriptionCompatApi23;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

public final class MediaDescriptionCompat
implements Parcelable {
    public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
    public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
    public static final long BT_FOLDER_TYPE_GENRES = 4L;
    public static final long BT_FOLDER_TYPE_MIXED = 0L;
    public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
    public static final long BT_FOLDER_TYPE_TITLES = 1L;
    public static final long BT_FOLDER_TYPE_YEARS = 6L;
    public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator<MediaDescriptionCompat>(){

        public MediaDescriptionCompat createFromParcel(Parcel parcel) {
            if (Build.VERSION.SDK_INT < 21) {
                return new MediaDescriptionCompat(parcel);
            }
            return MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(parcel));
        }

        public MediaDescriptionCompat[] newArray(int n) {
            return new MediaDescriptionCompat[n];
        }
    };
    public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";
    public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
    public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
    public static final String EXTRA_DOWNLOAD_STATUS = "android.media.extra.DOWNLOAD_STATUS";
    public static final long STATUS_DOWNLOADED = 2L;
    public static final long STATUS_DOWNLOADING = 1L;
    public static final long STATUS_NOT_DOWNLOADED = 0L;
    private final CharSequence mDescription;
    private Object mDescriptionObj;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;

    MediaDescriptionCompat(Parcel parcel) {
        this.mMediaId = parcel.readString();
        this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        ClassLoader classLoader = this.getClass().getClassLoader();
        this.mIcon = (Bitmap)parcel.readParcelable(classLoader);
        this.mIconUri = (Uri)parcel.readParcelable(classLoader);
        this.mExtras = parcel.readBundle(classLoader);
        this.mMediaUri = (Uri)parcel.readParcelable(classLoader);
    }

    MediaDescriptionCompat(String string, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Bitmap bitmap, Uri uri, Bundle bundle, Uri uri2) {
        this.mMediaId = string;
        this.mTitle = charSequence;
        this.mSubtitle = charSequence2;
        this.mDescription = charSequence3;
        this.mIcon = bitmap;
        this.mIconUri = uri;
        this.mExtras = bundle;
        this.mMediaUri = uri2;
    }

    public static MediaDescriptionCompat fromMediaDescription(Object object) {
        if (object != null && Build.VERSION.SDK_INT >= 21) {
            Builder builder = new Builder();
            builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(object));
            builder.setTitle(MediaDescriptionCompatApi21.getTitle(object));
            builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(object));
            builder.setDescription(MediaDescriptionCompatApi21.getDescription(object));
            builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(object));
            builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(object));
            Bundle bundle = MediaDescriptionCompatApi21.getExtras(object);
            Object object2 = null;
            if (bundle != null) {
                MediaSessionCompat.ensureClassLoader(bundle);
                object2 = (Uri)bundle.getParcelable("android.support.v4.media.description.MEDIA_URI");
            }
            Bundle bundle2 = bundle;
            if (object2 != null) {
                if (bundle.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && bundle.size() == 2) {
                    bundle2 = null;
                } else {
                    bundle.remove("android.support.v4.media.description.MEDIA_URI");
                    bundle.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
                    bundle2 = bundle;
                }
            }
            builder.setExtras(bundle2);
            if (object2 != null) {
                builder.setMediaUri((Uri)object2);
            } else if (Build.VERSION.SDK_INT >= 23) {
                builder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(object));
            }
            object2 = builder.build();
            object2.mDescriptionObj = object;
            return object2;
        }
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public Bitmap getIconBitmap() {
        return this.mIcon;
    }

    public Uri getIconUri() {
        return this.mIconUri;
    }

    public Object getMediaDescription() {
        if (this.mDescriptionObj == null && Build.VERSION.SDK_INT >= 21) {
            Object object = MediaDescriptionCompatApi21.Builder.newInstance();
            MediaDescriptionCompatApi21.Builder.setMediaId(object, this.mMediaId);
            MediaDescriptionCompatApi21.Builder.setTitle(object, this.mTitle);
            MediaDescriptionCompatApi21.Builder.setSubtitle(object, this.mSubtitle);
            MediaDescriptionCompatApi21.Builder.setDescription(object, this.mDescription);
            MediaDescriptionCompatApi21.Builder.setIconBitmap(object, this.mIcon);
            MediaDescriptionCompatApi21.Builder.setIconUri(object, this.mIconUri);
            Bundle bundle = this.mExtras;
            Object object2 = bundle;
            if (Build.VERSION.SDK_INT < 23) {
                object2 = bundle;
                if (this.mMediaUri != null) {
                    object2 = bundle;
                    if (bundle == null) {
                        object2 = new Bundle();
                        object2.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
                    }
                    object2.putParcelable("android.support.v4.media.description.MEDIA_URI", (Parcelable)this.mMediaUri);
                }
            }
            MediaDescriptionCompatApi21.Builder.setExtras(object, (Bundle)object2);
            if (Build.VERSION.SDK_INT >= 23) {
                MediaDescriptionCompatApi23.Builder.setMediaUri(object, this.mMediaUri);
            }
            this.mDescriptionObj = object2 = MediaDescriptionCompatApi21.Builder.build(object);
            return object2;
        }
        return this.mDescriptionObj;
    }

    public String getMediaId() {
        return this.mMediaId;
    }

    public Uri getMediaUri() {
        return this.mMediaUri;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((Object)this.mTitle);
        stringBuilder.append(", ");
        stringBuilder.append((Object)this.mSubtitle);
        stringBuilder.append(", ");
        stringBuilder.append((Object)this.mDescription);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        if (Build.VERSION.SDK_INT < 21) {
            parcel.writeString(this.mMediaId);
            TextUtils.writeToParcel((CharSequence)this.mTitle, (Parcel)parcel, (int)n);
            TextUtils.writeToParcel((CharSequence)this.mSubtitle, (Parcel)parcel, (int)n);
            TextUtils.writeToParcel((CharSequence)this.mDescription, (Parcel)parcel, (int)n);
            parcel.writeParcelable((Parcelable)this.mIcon, n);
            parcel.writeParcelable((Parcelable)this.mIconUri, n);
            parcel.writeBundle(this.mExtras);
            parcel.writeParcelable((Parcelable)this.mMediaUri, n);
            return;
        }
        MediaDescriptionCompatApi21.writeToParcel(this.getMediaDescription(), parcel, n);
    }

    public static final class Builder {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
        }

        public Builder setDescription(CharSequence charSequence) {
            this.mDescription = charSequence;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setIconBitmap(Bitmap bitmap) {
            this.mIcon = bitmap;
            return this;
        }

        public Builder setIconUri(Uri uri) {
            this.mIconUri = uri;
            return this;
        }

        public Builder setMediaId(String string) {
            this.mMediaId = string;
            return this;
        }

        public Builder setMediaUri(Uri uri) {
            this.mMediaUri = uri;
            return this;
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.mSubtitle = charSequence;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }
    }

}

