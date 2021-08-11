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

@RequiresApi(value=19)
class AlarmManagerCompatKitKat {
    AlarmManagerCompatKitKat() {
    }

    static void setExact(AlarmManager alarmManager, int n, long l, PendingIntent pendingIntent) {
        alarmManager.setExact(n, l, pendingIntent);
    }
}

