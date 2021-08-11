// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport.util.reminder;

import android.content.Intent;
import org.joda.time.DateTime;
import android.app.AlarmManager;
import android.content.Context;
import android.content.BroadcastReceiver;

public class ReminderEnablerReceiver extends BroadcastReceiver
{
    public static void scheduleAlarms(final Context context) {
        ((AlarmManager)context.getSystemService("alarm")).setInexactRepeating(1, new DateTime().withTime(9, 0, 0, 0).getMillis(), 86400000L, ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION", new long[0]));
    }
    
    public void onReceive(final Context context, final Intent intent) {
        scheduleAlarms(context);
    }
}
