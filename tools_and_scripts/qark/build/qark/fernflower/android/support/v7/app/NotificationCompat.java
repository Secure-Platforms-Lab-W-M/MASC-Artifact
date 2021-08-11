package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;

@Deprecated
public class NotificationCompat extends android.support.v4.app.NotificationCompat {
   @Deprecated
   public static MediaSessionCompat.Token getMediaSession(Notification var0) {
      Bundle var2 = getExtras(var0);
      if (var2 != null) {
         if (VERSION.SDK_INT >= 21) {
            Parcelable var3 = var2.getParcelable("android.mediaSession");
            if (var3 != null) {
               return MediaSessionCompat.Token.fromToken(var3);
            }
         } else {
            IBinder var1 = BundleCompat.getBinder(var2, "android.mediaSession");
            if (var1 != null) {
               Parcel var4 = Parcel.obtain();
               var4.writeStrongBinder(var1);
               var4.setDataPosition(0);
               MediaSessionCompat.Token var5 = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(var4);
               var4.recycle();
               return var5;
            }
         }
      }

      return null;
   }

   @Deprecated
   public static class Builder extends android.support.v4.app.NotificationCompat.Builder {
      @Deprecated
      public Builder(Context var1) {
         super(var1);
      }
   }

   @Deprecated
   public static class DecoratedCustomViewStyle extends android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle {
   }

   @Deprecated
   public static class DecoratedMediaCustomViewStyle extends android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle {
   }

   @Deprecated
   public static class MediaStyle extends android.support.v4.media.app.NotificationCompat.MediaStyle {
      @Deprecated
      public MediaStyle() {
      }

      @Deprecated
      public MediaStyle(android.support.v4.app.NotificationCompat.Builder var1) {
         super(var1);
      }

      @Deprecated
      public NotificationCompat.MediaStyle setCancelButtonIntent(PendingIntent var1) {
         return (NotificationCompat.MediaStyle)super.setCancelButtonIntent(var1);
      }

      @Deprecated
      public NotificationCompat.MediaStyle setMediaSession(MediaSessionCompat.Token var1) {
         return (NotificationCompat.MediaStyle)super.setMediaSession(var1);
      }

      @Deprecated
      public NotificationCompat.MediaStyle setShowActionsInCompactView(int... var1) {
         return (NotificationCompat.MediaStyle)super.setShowActionsInCompactView(var1);
      }

      @Deprecated
      public NotificationCompat.MediaStyle setShowCancelButton(boolean var1) {
         return (NotificationCompat.MediaStyle)super.setShowCancelButton(var1);
      }
   }
}
