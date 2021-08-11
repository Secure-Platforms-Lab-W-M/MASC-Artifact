// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.PendingIntent;
import android.content.Context;
import android.app.Notification;

class NotificationCompatGingerbread
{
    public static Notification add(final Notification notification, final Context context, final CharSequence charSequence, final CharSequence charSequence2, final PendingIntent pendingIntent, final PendingIntent fullScreenIntent) {
        notification.setLatestEventInfo(context, charSequence, charSequence2, pendingIntent);
        notification.fullScreenIntent = fullScreenIntent;
        return notification;
    }
}
