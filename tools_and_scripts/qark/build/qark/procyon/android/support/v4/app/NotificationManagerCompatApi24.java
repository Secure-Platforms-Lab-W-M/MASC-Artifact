// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.NotificationManager;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(24)
@RequiresApi(24)
class NotificationManagerCompatApi24
{
    public static boolean areNotificationsEnabled(final NotificationManager notificationManager) {
        return notificationManager.areNotificationsEnabled();
    }
    
    public static int getImportance(final NotificationManager notificationManager) {
        return notificationManager.getImportance();
    }
}
