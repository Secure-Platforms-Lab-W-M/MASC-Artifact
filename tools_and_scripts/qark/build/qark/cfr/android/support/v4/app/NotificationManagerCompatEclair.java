/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.NotificationManager
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;

class NotificationManagerCompatEclair {
    NotificationManagerCompatEclair() {
    }

    static void cancelNotification(NotificationManager notificationManager, String string, int n) {
        notificationManager.cancel(string, n);
    }

    public static void postNotification(NotificationManager notificationManager, String string, int n, Notification notification) {
        notificationManager.notify(string, n, notification);
    }
}

