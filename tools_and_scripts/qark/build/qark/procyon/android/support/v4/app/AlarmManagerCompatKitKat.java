// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.PendingIntent;
import android.app.AlarmManager;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AlarmManagerCompatKitKat
{
    static void setExact(final AlarmManager alarmManager, final int n, final long n2, final PendingIntent pendingIntent) {
        alarmManager.setExact(n, n2, pendingIntent);
    }
}
