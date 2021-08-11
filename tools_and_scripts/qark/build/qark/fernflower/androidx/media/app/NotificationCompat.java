package androidx.media.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;
import androidx.core.app.BundleCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.media.R.color;
import androidx.media.R.id;
import androidx.media.R.integer;
import androidx.media.R.layout;

public class NotificationCompat {
   private NotificationCompat() {
   }

   public static class DecoratedMediaCustomViewStyle extends NotificationCompat.MediaStyle {
      private void setBackgroundColor(RemoteViews var1) {
         int var2;
         if (this.mBuilder.getColor() != 0) {
            var2 = this.mBuilder.getColor();
         } else {
            var2 = this.mBuilder.mContext.getResources().getColor(color.notification_material_background_media_default_color);
         }

         var1.setInt(id.status_bar_latest_event_content, "setBackgroundColor", var2);
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            var1.getBuilder().setStyle(this.fillInMediaStyle(new android.app.Notification.DecoratedMediaCustomViewStyle()));
         } else {
            super.apply(var1);
         }
      }

      int getBigContentViewLayoutResource(int var1) {
         return var1 <= 3 ? layout.notification_template_big_media_narrow_custom : layout.notification_template_big_media_custom;
      }

      int getContentViewLayoutResource() {
         return this.mBuilder.getContentView() != null ? layout.notification_template_media_custom : super.getContentViewLayoutResource();
      }

      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            RemoteViews var3;
            if (this.mBuilder.getBigContentView() != null) {
               var3 = this.mBuilder.getBigContentView();
            } else {
               var3 = this.mBuilder.getContentView();
            }

            if (var3 == null) {
               return null;
            } else {
               RemoteViews var2 = this.generateBigContentView();
               this.buildIntoRemoteViews(var2, var3);
               if (VERSION.SDK_INT >= 21) {
                  this.setBackgroundColor(var2);
               }

               return var2;
            }
         }
      }

      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            RemoteViews var4 = this.mBuilder.getContentView();
            boolean var3 = true;
            boolean var2;
            if (var4 != null) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (VERSION.SDK_INT >= 21) {
               if (!var2 && this.mBuilder.getBigContentView() == null) {
                  var3 = false;
               }

               if (var3) {
                  var4 = this.generateContentView();
                  if (var2) {
                     this.buildIntoRemoteViews(var4, this.mBuilder.getContentView());
                  }

                  this.setBackgroundColor(var4);
                  return var4;
               } else {
                  return null;
               }
            } else {
               var4 = this.generateContentView();
               if (var2) {
                  this.buildIntoRemoteViews(var4, this.mBuilder.getContentView());
                  return var4;
               } else {
                  return null;
               }
            }
         }
      }

      public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            RemoteViews var3;
            if (this.mBuilder.getHeadsUpContentView() != null) {
               var3 = this.mBuilder.getHeadsUpContentView();
            } else {
               var3 = this.mBuilder.getContentView();
            }

            if (var3 == null) {
               return null;
            } else {
               RemoteViews var2 = this.generateBigContentView();
               this.buildIntoRemoteViews(var2, var3);
               if (VERSION.SDK_INT >= 21) {
                  this.setBackgroundColor(var2);
               }

               return var2;
            }
         }
      }
   }

   public static class MediaStyle extends androidx.core.app.NotificationCompat.Style {
      private static final int MAX_MEDIA_BUTTONS = 5;
      private static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
      int[] mActionsToShowInCompact = null;
      PendingIntent mCancelButtonIntent;
      boolean mShowCancelButton;
      MediaSessionCompat.Token mToken;

      public MediaStyle() {
      }

      public MediaStyle(androidx.core.app.NotificationCompat.Builder var1) {
         this.setBuilder(var1);
      }

      private RemoteViews generateMediaActionButton(androidx.core.app.NotificationCompat.Action var1) {
         boolean var2;
         if (var1.getActionIntent() == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         RemoteViews var3 = new RemoteViews(this.mBuilder.mContext.getPackageName(), layout.notification_media_action);
         var3.setImageViewResource(id.action0, var1.getIcon());
         if (!var2) {
            var3.setOnClickPendingIntent(id.action0, var1.getActionIntent());
         }

         if (VERSION.SDK_INT >= 15) {
            var3.setContentDescription(id.action0, var1.getTitle());
         }

         return var3;
      }

      public static MediaSessionCompat.Token getMediaSession(Notification var0) {
         Bundle var2 = androidx.core.app.NotificationCompat.getExtras(var0);
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

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 21) {
            var1.getBuilder().setStyle(this.fillInMediaStyle(new android.app.Notification.MediaStyle()));
         } else {
            if (this.mShowCancelButton) {
               var1.getBuilder().setOngoing(true);
            }

         }
      }

      android.app.Notification.MediaStyle fillInMediaStyle(android.app.Notification.MediaStyle var1) {
         int[] var2 = this.mActionsToShowInCompact;
         if (var2 != null) {
            var1.setShowActionsInCompactView(var2);
         }

         MediaSessionCompat.Token var3 = this.mToken;
         if (var3 != null) {
            var1.setMediaSession((Token)var3.getToken());
         }

         return var1;
      }

      RemoteViews generateBigContentView() {
         int var2 = Math.min(this.mBuilder.mActions.size(), 5);
         RemoteViews var3 = this.applyStandardTemplate(false, this.getBigContentViewLayoutResource(var2), false);
         var3.removeAllViews(id.media_actions);
         if (var2 > 0) {
            for(int var1 = 0; var1 < var2; ++var1) {
               RemoteViews var4 = this.generateMediaActionButton((androidx.core.app.NotificationCompat.Action)this.mBuilder.mActions.get(var1));
               var3.addView(id.media_actions, var4);
            }
         }

         if (this.mShowCancelButton) {
            var3.setViewVisibility(id.cancel_action, 0);
            var3.setInt(id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(integer.cancel_button_image_alpha));
            var3.setOnClickPendingIntent(id.cancel_action, this.mCancelButtonIntent);
            return var3;
         } else {
            var3.setViewVisibility(id.cancel_action, 8);
            return var3;
         }
      }

      RemoteViews generateContentView() {
         RemoteViews var4 = this.applyStandardTemplate(false, this.getContentViewLayoutResource(), true);
         int var3 = this.mBuilder.mActions.size();
         int[] var5 = this.mActionsToShowInCompact;
         int var1;
         if (var5 == null) {
            var1 = 0;
         } else {
            var1 = Math.min(var5.length, 3);
         }

         var4.removeAllViews(id.media_actions);
         if (var1 > 0) {
            for(int var2 = 0; var2 < var1; ++var2) {
               if (var2 >= var3) {
                  throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", var2, var3 - 1));
               }

               RemoteViews var6 = this.generateMediaActionButton((androidx.core.app.NotificationCompat.Action)this.mBuilder.mActions.get(this.mActionsToShowInCompact[var2]));
               var4.addView(id.media_actions, var6);
            }
         }

         if (this.mShowCancelButton) {
            var4.setViewVisibility(id.end_padder, 8);
            var4.setViewVisibility(id.cancel_action, 0);
            var4.setOnClickPendingIntent(id.cancel_action, this.mCancelButtonIntent);
            var4.setInt(id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(integer.cancel_button_image_alpha));
            return var4;
         } else {
            var4.setViewVisibility(id.end_padder, 0);
            var4.setViewVisibility(id.cancel_action, 8);
            return var4;
         }
      }

      int getBigContentViewLayoutResource(int var1) {
         return var1 <= 3 ? layout.notification_template_big_media_narrow : layout.notification_template_big_media;
      }

      int getContentViewLayoutResource() {
         return layout.notification_template_media;
      }

      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         return VERSION.SDK_INT >= 21 ? null : this.generateBigContentView();
      }

      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         return VERSION.SDK_INT >= 21 ? null : this.generateContentView();
      }

      public NotificationCompat.MediaStyle setCancelButtonIntent(PendingIntent var1) {
         this.mCancelButtonIntent = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setMediaSession(MediaSessionCompat.Token var1) {
         this.mToken = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setShowActionsInCompactView(int... var1) {
         this.mActionsToShowInCompact = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setShowCancelButton(boolean var1) {
         if (VERSION.SDK_INT < 21) {
            this.mShowCancelButton = var1;
         }

         return this;
      }
   }
}
