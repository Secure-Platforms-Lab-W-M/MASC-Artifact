/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.IntentService
 *  android.app.Notification
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  androidx.core.app.NotificationCompat
 *  androidx.core.app.NotificationCompat$Builder
 *  androidx.core.app.NotificationCompat$InboxStyle
 *  androidx.core.app.NotificationCompat$Style
 *  androidx.core.app.TaskStackBuilder
 *  me.kuehle.carreport.data.query.ReminderQueries
 *  me.kuehle.carreport.gui.MainActivity
 *  me.kuehle.carreport.gui.PreferencesActivity
 *  me.kuehle.carreport.gui.PreferencesRemindersFragment
 *  me.kuehle.carreport.presentation.CarPresenter
 *  me.kuehle.carreport.provider.reminder.ReminderColumns
 *  me.kuehle.carreport.provider.reminder.ReminderContentValues
 *  me.kuehle.carreport.provider.reminder.ReminderCursor
 *  me.kuehle.carreport.provider.reminder.ReminderSelection
 *  me.kuehle.carreport.util.TimeSpan
 */
package me.kuehle.carreport.util.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
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
import me.kuehle.carreport.util.TimeSpan;

public class ReminderService
extends IntentService {
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

    private static /* varargs */ Notification buildNotification(Context context, long ... arrl) {
        ReminderCursor reminderCursor = new ReminderSelection().id(arrl).query(context.getContentResolver());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminders").setSmallIcon(2131230833).setCategory("alarm").setPriority(-1);
        Intent intent = new Intent(context, PreferencesActivity.class);
        intent.putExtra(":android:show_fragment", PreferencesRemindersFragment.class.getName());
        intent.putExtra(":android:show_fragment_title", 2131820789);
        builder.setContentIntent(TaskStackBuilder.create((Context)context).addNextIntentWithParentStack(new Intent(context, MainActivity.class)).addNextIntent(intent).getPendingIntent(0, 134217728));
        builder.setDeleteIntent(ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS", arrl));
        if (reminderCursor.getCount() == 1) {
            reminderCursor.moveToNext();
            builder.setContentTitle((CharSequence)context.getString(2131820746, new Object[]{reminderCursor.getTitle()})).setContentText((CharSequence)reminderCursor.getCarName()).addAction(2131230842, (CharSequence)context.getString(2131820741), ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE", arrl)).addAction(2131230858, (CharSequence)context.getString(2131820742), ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", arrl));
        } else {
            intent = new NotificationCompat.InboxStyle();
            intent.setBigContentTitle((CharSequence)context.getString(2131820745));
            ArrayList<String> arrayList = new ArrayList<String>(reminderCursor.getCount());
            while (reminderCursor.moveToNext()) {
                arrayList.add(reminderCursor.getTitle());
                intent.addLine((CharSequence)String.format("%s (%s)", reminderCursor.getTitle(), reminderCursor.getCarName()));
            }
            builder.setContentTitle((CharSequence)context.getString(2131820745)).setContentText((CharSequence)TextUtils.join((CharSequence)", ", arrayList)).setNumber(reminderCursor.getCount()).setStyle((NotificationCompat.Style)intent).addAction(2131230858, (CharSequence)context.getString(2131820743), ReminderService.getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", arrl));
        }
        return builder.build();
    }

    @RequiresApi(api=26)
    private static NotificationChannel buildNotificationChannel(Context context) {
        return new NotificationChannel("reminders", (CharSequence)context.getString(2131820744), 2);
    }

    public static /* varargs */ void dismissReminders(Context context, long ... arrl) {
        for (long l : arrl) {
            ReminderContentValues reminderContentValues = new ReminderContentValues();
            reminderContentValues.putNotificationDismissed(true);
            reminderContentValues.update(context.getContentResolver(), new ReminderSelection().id(new long[]{l}));
        }
        ReminderService.updateNotification(context);
    }

    public static /* varargs */ PendingIntent getPendingIntent(Context context, String string, long ... arrl) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.setAction(string);
        intent.putExtra("REMINDER_IDS", arrl);
        return PendingIntent.getService((Context)context, (int)0, (Intent)intent, (int)134217728);
    }

    public static /* varargs */ void markRemindersDone(Context context, long ... reminderCursor) {
        Date date = new Date();
        CarPresenter carPresenter = CarPresenter.getInstance((Context)context);
        reminderCursor = new ReminderSelection().id((long[])reminderCursor).query(context.getContentResolver());
        while (reminderCursor.moveToNext()) {
            ReminderContentValues reminderContentValues = new ReminderContentValues();
            reminderContentValues.putStartDate(date);
            reminderContentValues.putStartMileage(carPresenter.getLatestMileage(reminderCursor.getCarId()));
            reminderContentValues.putSnoozedUntilNull();
            reminderContentValues.putNotificationDismissed(false);
            reminderContentValues.update(context.getContentResolver(), new ReminderSelection().id(new long[]{reminderCursor.getId()}));
        }
        ReminderService.updateNotification(context);
    }

    public static /* varargs */ void snoozeReminders(Context context, long ... arrl) {
        Object object = new Preferences(context);
        Date date = new Date();
        object = object.getReminderSnoozeDuration().addTo(date);
        for (long l : arrl) {
            date = new ReminderContentValues();
            date.putSnoozedUntil((Date)object);
            date.update(context.getContentResolver(), new ReminderSelection().id(new long[]{l}));
        }
        ReminderService.updateNotification(context);
    }

    public static void updateNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        ArrayList<Long> arrayList = new ArrayList<Long>();
        long[] arrl = new ReminderSelection().query(context.getContentResolver(), ReminderColumns.ALL_COLUMNS);
        ReminderQueries reminderQueries = new ReminderQueries(context, (ReminderCursor)arrl);
        while (arrl.moveToNext()) {
            if (arrl.getNotificationDismissed() || reminderQueries.isSnoozed() || !reminderQueries.isDue()) continue;
            arrayList.add(arrl.getId());
        }
        arrl = new long[arrayList.size()];
        for (int i = 0; i < arrayList.size(); ++i) {
            arrl[i] = (Long)arrayList.get(i);
        }
        if (arrayList.size() > 0) {
            if (Build.VERSION.SDK_INT >= 26) {
                notificationManager.createNotificationChannel(ReminderService.buildNotificationChannel(context));
            }
            notificationManager.notify(1, ReminderService.buildNotification(context, arrl));
            return;
        }
        notificationManager.cancel(1);
    }

    protected void onHandleIntent(Intent arrl) {
        String string = arrl.getAction();
        if (string == null) {
            return;
        }
        arrl = arrl.getLongArrayExtra("REMINDER_IDS");
        int n = -1;
        int n2 = string.hashCode();
        if (n2 != 426954335) {
            if (n2 != 1240190062) {
                if (n2 != 1436350873) {
                    if (n2 == 1488109147 && string.equals("me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS")) {
                        n = 3;
                    }
                } else if (string.equals("me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS")) {
                    n = 2;
                }
            } else if (string.equals("me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION")) {
                n = 0;
            }
        } else if (string.equals("me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE")) {
            n = 1;
        }
        switch (n) {
            default: {
                return;
            }
            case 3: {
                ReminderService.snoozeReminders((Context)this, arrl);
                return;
            }
            case 2: {
                ReminderService.dismissReminders((Context)this, arrl);
                return;
            }
            case 1: {
                ReminderService.markRemindersDone((Context)this, arrl);
                return;
            }
            case 0: 
        }
        ReminderService.updateNotification((Context)this);
    }
}

