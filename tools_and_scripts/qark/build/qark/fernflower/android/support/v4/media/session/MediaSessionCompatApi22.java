package android.support.v4.media.session;

import android.media.session.MediaSession;

class MediaSessionCompatApi22 {
   private MediaSessionCompatApi22() {
   }

   public static void setRatingType(Object var0, int var1) {
      ((MediaSession)var0).setRatingType(var1);
   }
}
