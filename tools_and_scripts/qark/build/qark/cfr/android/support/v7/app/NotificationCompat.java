/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

@Deprecated
public class NotificationCompat
extends android.support.v4.app.NotificationCompat {
    @Deprecated
    public static MediaSessionCompat.Token getMediaSession(Notification notification) {
        if ((notification = NotificationCompat.getExtras(notification)) != null) {
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

    @Deprecated
    public static class Builder
    extends NotificationCompat.Builder {
        @Deprecated
        public Builder(Context context) {
            super(context);
        }
    }

    @Deprecated
    public static class DecoratedCustomViewStyle
    extends NotificationCompat.DecoratedCustomViewStyle {
    }

    @Deprecated
    public static class DecoratedMediaCustomViewStyle
    extends NotificationCompat.DecoratedMediaCustomViewStyle {
    }

    @Deprecated
    public static class MediaStyle
    extends NotificationCompat.MediaStyle {
        @Deprecated
        public MediaStyle() {
        }

        @Deprecated
        public MediaStyle(NotificationCompat.Builder builder) {
            super(builder);
        }

        @Deprecated
        @Override
        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            return (MediaStyle)super.setCancelButtonIntent(pendingIntent);
        }

        @Deprecated
        @Override
        public MediaStyle setMediaSession(MediaSessionCompat.Token token) {
            return (MediaStyle)super.setMediaSession(token);
        }

        @Deprecated
        @Override
        public /* varargs */ MediaStyle setShowActionsInCompactView(int ... arrn) {
            return (MediaStyle)super.setShowActionsInCompactView(arrn);
        }

        @Deprecated
        @Override
        public MediaStyle setShowCancelButton(boolean bl) {
            return (MediaStyle)super.setShowCancelButton(bl);
        }
    }

}

