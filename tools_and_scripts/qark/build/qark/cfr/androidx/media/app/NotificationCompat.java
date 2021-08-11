/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.Notification$DecoratedMediaCustomViewStyle
 *  android.app.Notification$MediaStyle
 *  android.app.Notification$Style
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.res.Resources
 *  android.media.session.MediaSession
 *  android.media.session.MediaSession$Token
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.widget.RemoteViews
 *  androidx.media.R
 *  androidx.media.R$color
 *  androidx.media.R$id
 *  androidx.media.R$integer
 *  androidx.media.R$layout
 */
package androidx.media.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;
import androidx.core.app.BundleCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.media.R;
import java.util.ArrayList;

public class NotificationCompat {
    private NotificationCompat() {
    }

    public static class DecoratedMediaCustomViewStyle
    extends MediaStyle {
        private void setBackgroundColor(RemoteViews remoteViews) {
            int n = this.mBuilder.getColor() != 0 ? this.mBuilder.getColor() : this.mBuilder.mContext.getResources().getColor(R.color.notification_material_background_media_default_color);
            remoteViews.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", n);
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification.Style)this.fillInMediaStyle((Notification.MediaStyle)new Notification.DecoratedMediaCustomViewStyle()));
                return;
            }
            super.apply(notificationBuilderWithBuilderAccessor);
        }

        @Override
        int getBigContentViewLayoutResource(int n) {
            if (n <= 3) {
                return R.layout.notification_template_big_media_narrow_custom;
            }
            return R.layout.notification_template_big_media_custom;
        }

        @Override
        int getContentViewLayoutResource() {
            if (this.mBuilder.getContentView() != null) {
                return R.layout.notification_template_media_custom;
            }
            return super.getContentViewLayoutResource();
        }

        @Override
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            notificationBuilderWithBuilderAccessor = this.mBuilder.getBigContentView() != null ? this.mBuilder.getBigContentView() : this.mBuilder.getContentView();
            if (notificationBuilderWithBuilderAccessor == null) {
                return null;
            }
            RemoteViews remoteViews = this.generateBigContentView();
            this.buildIntoRemoteViews(remoteViews, (RemoteViews)notificationBuilderWithBuilderAccessor);
            if (Build.VERSION.SDK_INT >= 21) {
                this.setBackgroundColor(remoteViews);
            }
            return remoteViews;
        }

        @Override
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            notificationBuilderWithBuilderAccessor = this.mBuilder.getContentView();
            boolean bl = true;
            boolean bl2 = notificationBuilderWithBuilderAccessor != null;
            if (Build.VERSION.SDK_INT >= 21) {
                if (!bl2 && this.mBuilder.getBigContentView() == null) {
                    bl = false;
                }
                if (bl) {
                    notificationBuilderWithBuilderAccessor = this.generateContentView();
                    if (bl2) {
                        this.buildIntoRemoteViews((RemoteViews)notificationBuilderWithBuilderAccessor, this.mBuilder.getContentView());
                    }
                    this.setBackgroundColor((RemoteViews)notificationBuilderWithBuilderAccessor);
                    return notificationBuilderWithBuilderAccessor;
                }
                return null;
            }
            notificationBuilderWithBuilderAccessor = this.generateContentView();
            if (bl2) {
                this.buildIntoRemoteViews((RemoteViews)notificationBuilderWithBuilderAccessor, this.mBuilder.getContentView());
                return notificationBuilderWithBuilderAccessor;
            }
            return null;
        }

        @Override
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            notificationBuilderWithBuilderAccessor = this.mBuilder.getHeadsUpContentView() != null ? this.mBuilder.getHeadsUpContentView() : this.mBuilder.getContentView();
            if (notificationBuilderWithBuilderAccessor == null) {
                return null;
            }
            RemoteViews remoteViews = this.generateBigContentView();
            this.buildIntoRemoteViews(remoteViews, (RemoteViews)notificationBuilderWithBuilderAccessor);
            if (Build.VERSION.SDK_INT >= 21) {
                this.setBackgroundColor(remoteViews);
            }
            return remoteViews;
        }
    }

    public static class MediaStyle
    extends NotificationCompat.Style {
        private static final int MAX_MEDIA_BUTTONS = 5;
        private static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        int[] mActionsToShowInCompact = null;
        PendingIntent mCancelButtonIntent;
        boolean mShowCancelButton;
        MediaSessionCompat.Token mToken;

        public MediaStyle() {
        }

        public MediaStyle(NotificationCompat.Builder builder) {
            this.setBuilder(builder);
        }

        private RemoteViews generateMediaActionButton(NotificationCompat.Action action) {
            boolean bl = action.getActionIntent() == null;
            RemoteViews remoteViews = new RemoteViews(this.mBuilder.mContext.getPackageName(), R.layout.notification_media_action);
            remoteViews.setImageViewResource(R.id.action0, action.getIcon());
            if (!bl) {
                remoteViews.setOnClickPendingIntent(R.id.action0, action.getActionIntent());
            }
            if (Build.VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(R.id.action0, action.getTitle());
            }
            return remoteViews;
        }

        public static MediaSessionCompat.Token getMediaSession(Notification notification) {
            if ((notification = androidx.core.app.NotificationCompat.getExtras(notification)) != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    if ((notification = notification.getParcelable("android.mediaSession")) != null) {
                        return MediaSessionCompat.Token.fromToken((Object)notification);
                    }
                } else {
                    Object object = BundleCompat.getBinder((Bundle)notification, "android.mediaSession");
                    if (object != null) {
                        notification = Parcel.obtain();
                        notification.writeStrongBinder((IBinder)object);
                        notification.setDataPosition(0);
                        object = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel((Parcel)notification);
                        notification.recycle();
                        return object;
                    }
                }
            }
            return null;
        }

        @Override
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 21) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification.Style)this.fillInMediaStyle(new Notification.MediaStyle()));
                return;
            }
            if (this.mShowCancelButton) {
                notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
            }
        }

        Notification.MediaStyle fillInMediaStyle(Notification.MediaStyle mediaStyle) {
            int[] arrn = this.mActionsToShowInCompact;
            if (arrn != null) {
                mediaStyle.setShowActionsInCompactView(arrn);
            }
            if ((arrn = this.mToken) != null) {
                mediaStyle.setMediaSession((MediaSession.Token)arrn.getToken());
            }
            return mediaStyle;
        }

        RemoteViews generateBigContentView() {
            int n = Math.min(this.mBuilder.mActions.size(), 5);
            RemoteViews remoteViews = this.applyStandardTemplate(false, this.getBigContentViewLayoutResource(n), false);
            remoteViews.removeAllViews(R.id.media_actions);
            if (n > 0) {
                for (int i = 0; i < n; ++i) {
                    RemoteViews remoteViews2 = this.generateMediaActionButton(this.mBuilder.mActions.get(i));
                    remoteViews.addView(R.id.media_actions, remoteViews2);
                }
            }
            if (this.mShowCancelButton) {
                remoteViews.setViewVisibility(R.id.cancel_action, 0);
                remoteViews.setInt(R.id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
                remoteViews.setOnClickPendingIntent(R.id.cancel_action, this.mCancelButtonIntent);
                return remoteViews;
            }
            remoteViews.setViewVisibility(R.id.cancel_action, 8);
            return remoteViews;
        }

        RemoteViews generateContentView() {
            RemoteViews remoteViews = this.applyStandardTemplate(false, this.getContentViewLayoutResource(), true);
            int n = this.mBuilder.mActions.size();
            RemoteViews remoteViews2 = this.mActionsToShowInCompact;
            int n2 = remoteViews2 == null ? 0 : Math.min(remoteViews2.length, 3);
            remoteViews.removeAllViews(R.id.media_actions);
            if (n2 > 0) {
                for (int i = 0; i < n2; ++i) {
                    if (i < n) {
                        remoteViews2 = this.generateMediaActionButton(this.mBuilder.mActions.get(this.mActionsToShowInCompact[i]));
                        remoteViews.addView(R.id.media_actions, remoteViews2);
                        continue;
                    }
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", i, n - 1));
                }
            }
            if (this.mShowCancelButton) {
                remoteViews.setViewVisibility(R.id.end_padder, 8);
                remoteViews.setViewVisibility(R.id.cancel_action, 0);
                remoteViews.setOnClickPendingIntent(R.id.cancel_action, this.mCancelButtonIntent);
                remoteViews.setInt(R.id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
                return remoteViews;
            }
            remoteViews.setViewVisibility(R.id.end_padder, 0);
            remoteViews.setViewVisibility(R.id.cancel_action, 8);
            return remoteViews;
        }

        int getBigContentViewLayoutResource(int n) {
            if (n <= 3) {
                return R.layout.notification_template_big_media_narrow;
            }
            return R.layout.notification_template_big_media;
        }

        int getContentViewLayoutResource() {
            return R.layout.notification_template_media;
        }

        @Override
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 21) {
                return null;
            }
            return this.generateBigContentView();
        }

        @Override
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build.VERSION.SDK_INT >= 21) {
                return null;
            }
            return this.generateContentView();
        }

        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            this.mCancelButtonIntent = pendingIntent;
            return this;
        }

        public MediaStyle setMediaSession(MediaSessionCompat.Token token) {
            this.mToken = token;
            return this;
        }

        public /* varargs */ MediaStyle setShowActionsInCompactView(int ... arrn) {
            this.mActionsToShowInCompact = arrn;
            return this;
        }

        public MediaStyle setShowCancelButton(boolean bl) {
            if (Build.VERSION.SDK_INT < 21) {
                this.mShowCancelButton = bl;
            }
            return this;
        }
    }

}

