// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.app.PendingIntent;
import android.app.AlarmManager;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class AlarmManagerCompatApi23
{
    static void setAndAllowWhileIdle(final AlarmManager alarmManager, final int n, final long n2, final PendingIntent pendingIntent) {
        alarmManager.setAndAllowWhileIdle(n, n2, pendingIntent);
    }
    
    static void setExactAndAllowWhileIdle(final AlarmManager alarmManager, final int n, final long n2, final PendingIntent pendingIntent) {
        alarmManager.setExactAndAllowWhileIdle(n, n2, pendingIntent);
    }
}
