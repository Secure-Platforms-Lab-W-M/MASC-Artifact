package me.kuehle.carreport.util.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.core.app.TaskStackBuilder;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationCompat.InboxStyle;
import java.util.ArrayList;
import java.util.Date;
import me.kuehle.carreport.Preferences;
import me.kuehle.carreport.data.query.ReminderQueries;
import me.kuehle.carreport.gui.MainActivity;
import me.kuehle.carreport.gui.PreferencesActivity;
import me.kuehle.carreport.gui.PreferencesRemindersFragment;
import me.kuehle.carreport.presentation.CarPresenter;
import me.kuehle.carreport.provider.reminder.ReminderColumns;
import me.kuehle.carreport.provider.reminder.ReminderContentValues;
import me.kuehle.carreport.provider.reminder.ReminderCursor;
import me.kuehle.carreport.provider.reminder.ReminderSelection;

public class ReminderService extends IntentService {
   public static final String ACTION_DISMISS_REMINDERS = "me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS";
   public static final String ACTION_MARK_REMINDERS_DONE = "me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE";
   public static final String ACTION_SNOOZE_REMINDERS = "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS";
   public static final String ACTION_UPDATE_NOTIFICATION = "me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION";
   public static final String EXTRA_REMINDER_IDS = "REMINDER_IDS";
   private static final String NOTIFICATION_CHANNEL_ID = "reminders";
   private static final int NOTIFICATION_ID = 1;

   public ReminderService() {
      super("Reminder Service");
   }

   private static Notification buildNotification(Context var0, long... var1) {
      ReminderCursor var2 = (new ReminderSelection()).id(var1).query(var0.getContentResolver());
      Builder var3 = (new Builder(var0, "reminders")).setSmallIcon(2131230833).setCategory("alarm").setPriority(-1);
      Intent var4 = new Intent(var0, PreferencesActivity.class);
      var4.putExtra(":android:show_fragment", PreferencesRemindersFragment.class.getName());
      var4.putExtra(":android:show_fragment_title", 2131820789);
      var3.setContentIntent(TaskStackBuilder.create(var0).addNextIntentWithParentStack(new Intent(var0, MainActivity.class)).addNextIntent(var4).getPendingIntent(0, 134217728));
      var3.setDeleteIntent(getPendingIntent(var0, "me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS", var1));
      if (var2.getCount() == 1) {
         var2.moveToNext();
         var3.setContentTitle(var0.getString(2131820746, new Object[]{var2.getTitle()})).setContentText(var2.getCarName()).addAction(2131230842, var0.getString(2131820741), getPendingIntent(var0, "me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE", var1)).addAction(2131230858, var0.getString(2131820742), getPendingIntent(var0, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", var1));
      } else {
         InboxStyle var6 = new InboxStyle();
         var6.setBigContentTitle(var0.getString(2131820745));
         ArrayList var5 = new ArrayList(var2.getCount());

         while(var2.moveToNext()) {
            var5.add(var2.getTitle());
            var6.addLine(String.format("%s (%s)", var2.getTitle(), var2.getCarName()));
         }

         var3.setContentTitle(var0.getString(2131820745)).setContentText(TextUtils.join(", ", var5)).setNumber(var2.getCount()).setStyle(var6).addAction(2131230858, var0.getString(2131820743), getPendingIntent(var0, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", var1));
      }

      return var3.build();
   }

   @RequiresApi(
      api = 26
   )
   private static NotificationChannel buildNotificationChannel(Context var0) {
      return new NotificationChannel("reminders", var0.getString(2131820744), 2);
   }

   public static void dismissReminders(Context var0, long... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         long var4 = var1[var2];
         ReminderContentValues var6 = new ReminderContentValues();
         var6.putNotificationDismissed(true);
         var6.update(var0.getContentResolver(), (new ReminderSelection()).id(new long[]{var4}));
      }

      updateNotification(var0);
   }

   public static PendingIntent getPendingIntent(Context var0, String var1, long... var2) {
      Intent var3 = new Intent(var0, ReminderService.class);
      var3.setAction(var1);
      var3.putExtra("REMINDER_IDS", var2);
      return PendingIntent.getService(var0, 0, var3, 134217728);
   }

   public static void markRemindersDone(Context var0, long... var1) {
      Date var2 = new Date();
      CarPresenter var3 = CarPresenter.getInstance(var0);
      ReminderCursor var5 = (new ReminderSelection()).id(var1).query(var0.getContentResolver());

      while(var5.moveToNext()) {
         ReminderContentValues var4 = new ReminderContentValues();
         var4.putStartDate(var2);
         var4.putStartMileage(var3.getLatestMileage(var5.getCarId()));
         var4.putSnoozedUntilNull();
         var4.putNotificationDismissed(false);
         var4.update(var0.getContentResolver(), (new ReminderSelection()).id(new long[]{var5.getId()}));
      }

      updateNotification(var0);
   }

   public static void snoozeReminders(Context var0, long... var1) {
      Preferences var6 = new Preferences(var0);
      Date var7 = new Date();
      Date var8 = var6.getReminderSnoozeDuration().addTo(var7);
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         long var4 = var1[var2];
         ReminderContentValues var9 = new ReminderContentValues();
         var9.putSnoozedUntil(var8);
         var9.update(var0.getContentResolver(), (new ReminderSelection()).id(new long[]{var4}));
      }

      updateNotification(var0);
   }

   public static void updateNotification(Context var0) {
      NotificationManager var2 = (NotificationManager)var0.getSystemService("notification");
      ArrayList var3 = new ArrayList();
      ReminderCursor var4 = (new ReminderSelection()).query(var0.getContentResolver(), ReminderColumns.ALL_COLUMNS);
      ReminderQueries var5 = new ReminderQueries(var0, var4);

      while(var4.moveToNext()) {
         if (!var4.getNotificationDismissed() && !var5.isSnoozed() && var5.isDue()) {
            var3.add(var4.getId());
         }
      }

      long[] var6 = new long[var3.size()];

      for(int var1 = 0; var1 < var3.size(); ++var1) {
         var6[var1] = (Long)var3.get(var1);
      }

      if (var3.size() > 0) {
         if (VERSION.SDK_INT >= 26) {
            var2.createNotificationChannel(buildNotificationChannel(var0));
         }

         var2.notify(1, buildNotification(var0, var6));
      } else {
         var2.cancel(1);
      }
   }

   protected void onHandleIntent(Intent var1) {
      String var4 = var1.getAction();
      if (var4 != null) {
         long[] var5 = var1.getLongArrayExtra("REMINDER_IDS");
         byte var2 = -1;
         int var3 = var4.hashCode();
         if (var3 != 426954335) {
            if (var3 != 1240190062) {
               if (var3 != 1436350873) {
                  if (var3 == 1488109147 && var4.equals("me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS")) {
                     var2 = 3;
                  }
               } else if (var4.equals("me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS")) {
                  var2 = 2;
               }
            } else if (var4.equals("me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION")) {
               var2 = 0;
            }
         } else if (var4.equals("me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE")) {
            var2 = 1;
         }

         switch(var2) {
         case 0:
            updateNotification(this);
            return;
         case 1:
            markRemindersDone(this, var5);
            return;
         case 2:
            dismissReminders(this, var5);
            return;
         case 3:
            snoozeReminders(this, var5);
            return;
         default:
         }
      }
   }
}
