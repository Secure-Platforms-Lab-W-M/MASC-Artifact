// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.util.Log;
import android.media.Rating;
import android.os.Build$VERSION;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class RatingCompat implements Parcelable
{
    public static final Parcelable$Creator<RatingCompat> CREATOR;
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    private static final float RATING_NOT_RATED = -1.0f;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private static final String TAG = "Rating";
    private Object mRatingObj;
    private final int mRatingStyle;
    private final float mRatingValue;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<RatingCompat>() {
            public RatingCompat createFromParcel(final Parcel parcel) {
                return new RatingCompat(parcel.readInt(), parcel.readFloat());
            }
            
            public RatingCompat[] newArray(final int n) {
                return new RatingCompat[n];
            }
        };
    }
    
    RatingCompat(final int mRatingStyle, final float mRatingValue) {
        this.mRatingStyle = mRatingStyle;
        this.mRatingValue = mRatingValue;
    }
    
    public static RatingCompat fromRating(final Object mRatingObj) {
        if (mRatingObj != null && Build$VERSION.SDK_INT >= 19) {
            final int ratingStyle = ((Rating)mRatingObj).getRatingStyle();
            RatingCompat ratingCompat = null;
            if (((Rating)mRatingObj).isRated()) {
                switch (ratingStyle) {
                    default: {
                        return null;
                    }
                    case 6: {
                        ratingCompat = newPercentageRating(((Rating)mRatingObj).getPercentRating());
                        break;
                    }
                    case 3:
                    case 4:
                    case 5: {
                        ratingCompat = newStarRating(ratingStyle, ((Rating)mRatingObj).getStarRating());
                        break;
                    }
                    case 2: {
                        ratingCompat = newThumbRating(((Rating)mRatingObj).isThumbUp());
                        break;
                    }
                    case 1: {
                        ratingCompat = newHeartRating(((Rating)mRatingObj).hasHeart());
                        break;
                    }
                }
            }
            else {
                ratingCompat = newUnratedRating(ratingStyle);
            }
            ratingCompat.mRatingObj = mRatingObj;
            return ratingCompat;
        }
        return null;
    }
    
    public static RatingCompat newHeartRating(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        return new RatingCompat(1, n);
    }
    
    public static RatingCompat newPercentageRating(final float n) {
        if (n >= 0.0f && n <= 100.0f) {
            return new RatingCompat(6, n);
        }
        Log.e("Rating", "Invalid percentage-based rating value");
        return null;
    }
    
    public static RatingCompat newStarRating(final int n, final float n2) {
        float n3;
        if (n != 3) {
            if (n != 4) {
                if (n != 5) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid rating style (");
                    sb.append(n);
                    sb.append(") for a star rating");
                    Log.e("Rating", sb.toString());
                    return null;
                }
                n3 = 5.0f;
            }
            else {
                n3 = 4.0f;
            }
        }
        else {
            n3 = 3.0f;
        }
        if (n2 >= 0.0f && n2 <= n3) {
            return new RatingCompat(n, n2);
        }
        Log.e("Rating", "Trying to set out of range star-based rating");
        return null;
    }
    
    public static RatingCompat newThumbRating(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        return new RatingCompat(2, n);
    }
    
    public static RatingCompat newUnratedRating(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                return new RatingCompat(n, -1.0f);
            }
        }
    }
    
    public int describeContents() {
        return this.mRatingStyle;
    }
    
    public float getPercentRating() {
        if (this.mRatingStyle == 6 && this.isRated()) {
            return this.mRatingValue;
        }
        return -1.0f;
    }
    
    public Object getRating() {
        if (this.mRatingObj == null && Build$VERSION.SDK_INT >= 19) {
            if (this.isRated()) {
                final int mRatingStyle = this.mRatingStyle;
                switch (mRatingStyle) {
                    default: {
                        return null;
                    }
                    case 6: {
                        this.mRatingObj = Rating.newPercentageRating(this.getPercentRating());
                        break;
                    }
                    case 3:
                    case 4:
                    case 5: {
                        this.mRatingObj = Rating.newStarRating(mRatingStyle, this.getStarRating());
                        break;
                    }
                    case 2: {
                        this.mRatingObj = Rating.newThumbRating(this.isThumbUp());
                        break;
                    }
                    case 1: {
                        this.mRatingObj = Rating.newHeartRating(this.hasHeart());
                        break;
                    }
                }
            }
            else {
                this.mRatingObj = Rating.newUnratedRating(this.mRatingStyle);
            }
        }
        return this.mRatingObj;
    }
    
    public int getRatingStyle() {
        return this.mRatingStyle;
    }
    
    public float getStarRating() {
        final int mRatingStyle = this.mRatingStyle;
        if (mRatingStyle == 3 || mRatingStyle == 4 || mRatingStyle == 5) {
            if (this.isRated()) {
                return this.mRatingValue;
            }
        }
        return -1.0f;
    }
    
    public boolean hasHeart() {
        final int mRatingStyle = this.mRatingStyle;
        boolean b = false;
        if (mRatingStyle != 1) {
            return false;
        }
        if (this.mRatingValue == 1.0f) {
            b = true;
        }
        return b;
    }
    
    public boolean isRated() {
        return this.mRatingValue >= 0.0f;
    }
    
    public boolean isThumbUp() {
        final int mRatingStyle = this.mRatingStyle;
        boolean b = false;
        if (mRatingStyle != 2) {
            return false;
        }
        if (this.mRatingValue == 1.0f) {
            b = true;
        }
        return b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Rating:style=");
        sb.append(this.mRatingStyle);
        sb.append(" rating=");
        final float mRatingValue = this.mRatingValue;
        String value;
        if (mRatingValue < 0.0f) {
            value = "unrated";
        }
        else {
            value = String.valueOf(mRatingValue);
        }
        sb.append(value);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mRatingStyle);
        parcel.writeFloat(this.mRatingValue);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface StarStyle {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }
}
