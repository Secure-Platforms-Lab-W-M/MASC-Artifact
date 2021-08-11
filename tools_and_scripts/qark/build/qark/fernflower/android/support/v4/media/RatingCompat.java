package android.support.v4.media;

import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RatingCompat implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public RatingCompat createFromParcel(Parcel var1) {
         return new RatingCompat(var1.readInt(), var1.readFloat());
      }

      public RatingCompat[] newArray(int var1) {
         return new RatingCompat[var1];
      }
   };
   public static final int RATING_3_STARS = 3;
   public static final int RATING_4_STARS = 4;
   public static final int RATING_5_STARS = 5;
   public static final int RATING_HEART = 1;
   public static final int RATING_NONE = 0;
   private static final float RATING_NOT_RATED = -1.0F;
   public static final int RATING_PERCENTAGE = 6;
   public static final int RATING_THUMB_UP_DOWN = 2;
   private static final String TAG = "Rating";
   private Object mRatingObj;
   private final int mRatingStyle;
   private final float mRatingValue;

   RatingCompat(int var1, float var2) {
      this.mRatingStyle = var1;
      this.mRatingValue = var2;
   }

   public static RatingCompat fromRating(Object var0) {
      if (var0 != null && VERSION.SDK_INT >= 19) {
         int var1 = ((Rating)var0).getRatingStyle();
         RatingCompat var2;
         if (((Rating)var0).isRated()) {
            switch(var1) {
            case 1:
               var2 = newHeartRating(((Rating)var0).hasHeart());
               break;
            case 2:
               var2 = newThumbRating(((Rating)var0).isThumbUp());
               break;
            case 3:
            case 4:
            case 5:
               var2 = newStarRating(var1, ((Rating)var0).getStarRating());
               break;
            case 6:
               var2 = newPercentageRating(((Rating)var0).getPercentRating());
               break;
            default:
               return null;
            }
         } else {
            var2 = newUnratedRating(var1);
         }

         var2.mRatingObj = var0;
         return var2;
      } else {
         return null;
      }
   }

   public static RatingCompat newHeartRating(boolean var0) {
      float var1;
      if (var0) {
         var1 = 1.0F;
      } else {
         var1 = 0.0F;
      }

      return new RatingCompat(1, var1);
   }

   public static RatingCompat newPercentageRating(float var0) {
      if (var0 >= 0.0F && var0 <= 100.0F) {
         return new RatingCompat(6, var0);
      } else {
         Log.e("Rating", "Invalid percentage-based rating value");
         return null;
      }
   }

   public static RatingCompat newStarRating(int var0, float var1) {
      float var2;
      if (var0 != 3) {
         if (var0 != 4) {
            if (var0 != 5) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Invalid rating style (");
               var3.append(var0);
               var3.append(") for a star rating");
               Log.e("Rating", var3.toString());
               return null;
            }

            var2 = 5.0F;
         } else {
            var2 = 4.0F;
         }
      } else {
         var2 = 3.0F;
      }

      if (var1 >= 0.0F && var1 <= var2) {
         return new RatingCompat(var0, var1);
      } else {
         Log.e("Rating", "Trying to set out of range star-based rating");
         return null;
      }
   }

   public static RatingCompat newThumbRating(boolean var0) {
      float var1;
      if (var0) {
         var1 = 1.0F;
      } else {
         var1 = 0.0F;
      }

      return new RatingCompat(2, var1);
   }

   public static RatingCompat newUnratedRating(int var0) {
      switch(var0) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
         return new RatingCompat(var0, -1.0F);
      default:
         return null;
      }
   }

   public int describeContents() {
      return this.mRatingStyle;
   }

   public float getPercentRating() {
      return this.mRatingStyle == 6 && this.isRated() ? this.mRatingValue : -1.0F;
   }

   public Object getRating() {
      if (this.mRatingObj == null && VERSION.SDK_INT >= 19) {
         if (this.isRated()) {
            int var1 = this.mRatingStyle;
            switch(var1) {
            case 1:
               this.mRatingObj = Rating.newHeartRating(this.hasHeart());
               break;
            case 2:
               this.mRatingObj = Rating.newThumbRating(this.isThumbUp());
               break;
            case 3:
            case 4:
            case 5:
               this.mRatingObj = Rating.newStarRating(var1, this.getStarRating());
               break;
            case 6:
               this.mRatingObj = Rating.newPercentageRating(this.getPercentRating());
               break;
            default:
               return null;
            }
         } else {
            this.mRatingObj = Rating.newUnratedRating(this.mRatingStyle);
         }
      }

      return this.mRatingObj;
   }

   public int getRatingStyle() {
      return this.mRatingStyle;
   }

   public float getStarRating() {
      int var1 = this.mRatingStyle;
      return (var1 == 3 || var1 == 4 || var1 == 5) && this.isRated() ? this.mRatingValue : -1.0F;
   }

   public boolean hasHeart() {
      int var1 = this.mRatingStyle;
      boolean var2 = false;
      if (var1 != 1) {
         return false;
      } else {
         if (this.mRatingValue == 1.0F) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isRated() {
      return this.mRatingValue >= 0.0F;
   }

   public boolean isThumbUp() {
      int var1 = this.mRatingStyle;
      boolean var2 = false;
      if (var1 != 2) {
         return false;
      } else {
         if (this.mRatingValue == 1.0F) {
            var2 = true;
         }

         return var2;
      }
   }

   public String toString() {
      StringBuilder var3 = new StringBuilder();
      var3.append("Rating:style=");
      var3.append(this.mRatingStyle);
      var3.append(" rating=");
      float var1 = this.mRatingValue;
      String var2;
      if (var1 < 0.0F) {
         var2 = "unrated";
      } else {
         var2 = String.valueOf(var1);
      }

      var3.append(var2);
      return var3.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mRatingStyle);
      var1.writeFloat(this.mRatingValue);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface StarStyle {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Style {
   }
}
