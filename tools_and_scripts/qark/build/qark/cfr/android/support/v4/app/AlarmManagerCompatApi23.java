/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.PendingIntent
 */
package android.support.v4.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.annotation.RequiresApi;

@RequiresApi(value=23)
class AlarmManagerCompatApi23 {
    AlarmManagerCompatApi23() {
    }

    static void setAndAllowWhileIdle(AlarmManager alarmManager, int n, long l, PendingIntent pendingIntent) {
        alarmManager.setAndAllowWhileIdle(n, l, pendingIntent);
    }

    static void setExactAndAllowWhileIdle(AlarmManager alarmManager, int n, long l, PendingIntent pendingIntent) {
        alarmManager.setExactAndAllowWhileIdle(n, l, pendingIntent);
    }
}

