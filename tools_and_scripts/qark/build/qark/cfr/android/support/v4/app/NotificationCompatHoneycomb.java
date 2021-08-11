/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.widget.RemoteViews
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

@TargetApi(value=11)
@RequiresApi(value=11)
class NotificationCompatHoneycomb {
    NotificationCompatHoneycomb() {
    }

    /*
     * Enabled aggressive block sorting
     */
    static Notification add(Context context, Notification notification, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap) {
        context = new Notification.Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
        boolean bl = (notification.flags & 2) != 0;
        context = context.setOngoing(bl);
        bl = (notification.flags & 8) != 0;
        context = context.setOnlyAlertOnce(bl);
        bl = (notification.flags & 16) != 0;
        context = context.setAutoCancel(bl).setDefaults(notification.defaults).setContentTitle(charSequence).setContentText(charSequence2).setContentInfo(charSequence3).setContentIntent(pendingIntent).setDeleteIntent(notification.deleteIntent);
        if ((notification.flags & 128) != 0) {
            bl = true;
            return context.setFullScreenIntent(pendingIntent2, bl).setLargeIcon(bitmap).setNumber(n).getNotification();
        }
        bl = false;
        return context.setFullScreenIntent(pendingIntent2, bl).setLargeIcon(bitmap).setNumber(n).getNotification();
    }
}

