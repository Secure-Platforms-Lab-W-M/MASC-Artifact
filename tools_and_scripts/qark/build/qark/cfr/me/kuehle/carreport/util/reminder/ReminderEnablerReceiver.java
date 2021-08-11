/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 */
package me.kuehle.carreport.util.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import me.kuehle.carreport.util.reminder.ReminderService;
import org.joda.time.DateTime;

public class ReminderEnablerReceiver
extends BroadcastReceiver {
    public static void scheduleAlarms(Context context) {
        PendingIntent pendingIntent = ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION", new long[0]);
        DateTime dateTime = new DateTime().withTime(9, 0, 0, 0);
        ((AlarmManager)context.getSystemService("alarm")).setInexactRepeating(1, dateTime.getMillis(), 86400000L, pendingIntent);
    }

    public void onReceive(Context context, Intent intent) {
        ReminderEnablerReceiver.scheduleAlarms(context);
    }
}

