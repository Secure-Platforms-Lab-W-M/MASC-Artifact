package android.support.v4.media;

import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.Rating;
import android.os.Parcel;
import java.util.Set;

class MediaMetadataCompatApi21 {
   private MediaMetadataCompatApi21() {
   }

   public static Object createFromParcel(Parcel var0) {
      return MediaMetadata.CREATOR.createFromParcel(var0);
   }

   public static Bitmap getBitmap(Object var0, String var1) {
      return ((MediaMetadata)var0).getBitmap(var1);
   }

   public static long getLong(Object var0, String var1) {
      return ((MediaMetadata)var0).getLong(var1);
   }

   public static Object getRating(Object var0, String var1) {
      return ((MediaMetadata)var0).getRating(var1);
   }

   public static CharSequence getText(Object var0, String var1) {
      return ((MediaMetadata)var0).getText(var1);
   }

   public static Set keySet(Object var0) {
      return ((MediaMetadata)var0).keySet();
   }

   public static void writeToParcel(Object var0, Parcel var1, int var2) {
      ((MediaMetadata)var0).writeToParcel(var1, var2);
   }

   public static class Builder {
      private Builder() {
      }

      public static Object build(Object var0) {
         return ((android.media.MediaMetadata.Builder)var0).build();
      }

      public static Object newInstance() {
         return new android.media.MediaMetadata.Builder();
      }

      public static void putBitmap(Object var0, String var1, Bitmap var2) {
         ((android.media.MediaMetadata.Builder)var0).putBitmap(var1, var2);
      }

      public static void putLong(Object var0, String var1, long var2) {
         ((android.media.MediaMetadata.Builder)var0).putLong(var1, var2);
      }

      public static void putRating(Object var0, String var1, Object var2) {
         ((android.media.MediaMetadata.Builder)var0).putRating(var1, (Rating)var2);
      }

      public static void putString(Object var0, String var1, String var2) {
         ((android.media.MediaMetadata.Builder)var0).putString(var1, var2);
      }

      public static void putText(Object var0, String var1, CharSequence var2) {
         ((android.media.MediaMetadata.Builder)var0).putText(var1, var2);
      }
   }
}
