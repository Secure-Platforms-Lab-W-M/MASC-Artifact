package android.support.v4.media;

import android.media.MediaDescription;
import android.net.Uri;

class MediaDescriptionCompatApi23 {
   private MediaDescriptionCompatApi23() {
   }

   public static Uri getMediaUri(Object var0) {
      return ((MediaDescription)var0).getMediaUri();
   }

   static class Builder {
      private Builder() {
      }

      public static void setMediaUri(Object var0, Uri var1) {
         ((android.media.MediaDescription.Builder)var0).setMediaUri(var1);
      }
   }
}
