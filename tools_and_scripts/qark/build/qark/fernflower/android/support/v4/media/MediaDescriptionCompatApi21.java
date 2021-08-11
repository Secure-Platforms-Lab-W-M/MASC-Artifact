package android.support.v4.media;

import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;

class MediaDescriptionCompatApi21 {
   private MediaDescriptionCompatApi21() {
   }

   public static Object fromParcel(Parcel var0) {
      return MediaDescription.CREATOR.createFromParcel(var0);
   }

   public static CharSequence getDescription(Object var0) {
      return ((MediaDescription)var0).getDescription();
   }

   public static Bundle getExtras(Object var0) {
      return ((MediaDescription)var0).getExtras();
   }

   public static Bitmap getIconBitmap(Object var0) {
      return ((MediaDescription)var0).getIconBitmap();
   }

   public static Uri getIconUri(Object var0) {
      return ((MediaDescription)var0).getIconUri();
   }

   public static String getMediaId(Object var0) {
      return ((MediaDescription)var0).getMediaId();
   }

   public static CharSequence getSubtitle(Object var0) {
      return ((MediaDescription)var0).getSubtitle();
   }

   public static CharSequence getTitle(Object var0) {
      return ((MediaDescription)var0).getTitle();
   }

   public static void writeToParcel(Object var0, Parcel var1, int var2) {
      ((MediaDescription)var0).writeToParcel(var1, var2);
   }

   static class Builder {
      private Builder() {
      }

      public static Object build(Object var0) {
         return ((android.media.MediaDescription.Builder)var0).build();
      }

      public static Object newInstance() {
         return new android.media.MediaDescription.Builder();
      }

      public static void setDescription(Object var0, CharSequence var1) {
         ((android.media.MediaDescription.Builder)var0).setDescription(var1);
      }

      public static void setExtras(Object var0, Bundle var1) {
         ((android.media.MediaDescription.Builder)var0).setExtras(var1);
      }

      public static void setIconBitmap(Object var0, Bitmap var1) {
         ((android.media.MediaDescription.Builder)var0).setIconBitmap(var1);
      }

      public static void setIconUri(Object var0, Uri var1) {
         ((android.media.MediaDescription.Builder)var0).setIconUri(var1);
      }

      public static void setMediaId(Object var0, String var1) {
         ((android.media.MediaDescription.Builder)var0).setMediaId(var1);
      }

      public static void setSubtitle(Object var0, CharSequence var1) {
         ((android.media.MediaDescription.Builder)var0).setSubtitle(var1);
      }

      public static void setTitle(Object var0, CharSequence var1) {
         ((android.media.MediaDescription.Builder)var0).setTitle(var1);
      }
   }
}
