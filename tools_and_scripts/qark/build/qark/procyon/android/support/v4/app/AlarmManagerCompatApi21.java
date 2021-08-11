// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.AlarmManager$AlarmClockInfo;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class AlarmManagerCompatApi21
{
    static void setAlarmClock(final AlarmManager alarmManager, final long n, final PendingIntent pendingIntent, final PendingIntent pendingIntent2) {
        alarmManager.setAlarmClock(new AlarmManager$AlarmClockInfo(n, pendingIntent), pendingIntent2);
    }
}
