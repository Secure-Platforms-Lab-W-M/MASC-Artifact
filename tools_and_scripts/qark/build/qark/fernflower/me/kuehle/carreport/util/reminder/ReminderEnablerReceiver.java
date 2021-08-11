package me.kuehle.carreport.util.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.joda.time.DateTime;

public class ReminderEnablerReceiver extends BroadcastReceiver {
   public static void scheduleAlarms(Context var0) {
      PendingIntent var1 = ReminderService.getPendingIntent(var0, "me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION");
      DateTime var2 = (new DateTime()).withTime(9, 0, 0, 0);
      ((AlarmManager)var0.getSystemService("alarm")).setInexactRepeating(1, var2.getMillis(), 86400000L, var1);
   }

   public void onReceive(Context var1, Intent var2) {
      scheduleAlarms(var1);
   }
}
