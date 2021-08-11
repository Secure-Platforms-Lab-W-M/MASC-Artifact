package android.support.v4.media;

import android.media.Rating;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class RatingCompatKitkat {
   public static float getPercentRating(Object var0) {
      return ((Rating)var0).getPercentRating();
   }

   public static int getRatingStyle(Object var0) {
      return ((Rating)var0).getRatingStyle();
   }

   public static float getStarRating(Object var0) {
      return ((Rating)var0).getStarRating();
   }

   public static boolean hasHeart(Object var0) {
      return ((Rating)var0).hasHeart();
   }

   public static boolean isRated(Object var0) {
      return ((Rating)var0).isRated();
   }

   public static boolean isThumbUp(Object var0) {
      return ((Rating)var0).isThumbUp();
   }

   public static Object newHeartRating(boolean var0) {
      return Rating.newHeartRating(var0);
   }

   public static Object newPercentageRating(float var0) {
      return Rating.newPercentageRating(var0);
   }

   public static Object newStarRating(int var0, float var1) {
      return Rating.newStarRating(var0, var1);
   }

   public static Object newThumbRating(boolean var0) {
      return Rating.newThumbRating(var0);
   }

   public static Object newUnratedRating(int var0) {
      return Rating.newUnratedRating(var0);
   }
}
