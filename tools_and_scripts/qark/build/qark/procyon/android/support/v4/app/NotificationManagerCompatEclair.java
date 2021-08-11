// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;

class NotificationManagerCompatEclair
{
    static void cancelNotification(final NotificationManager notificationManager, final String s, final int n) {
        notificationManager.cancel(s, n);
    }
    
    public static void postNotification(final NotificationManager notificationManager, final String s, final int n, final Notification notification) {
        notificationManager.notify(s, n, notification);
    }
}
