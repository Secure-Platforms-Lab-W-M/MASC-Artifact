// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport.util.reminder;

import android.os.Build$VERSION;
import me.kuehle.carreport.data.query.ReminderQueries;
import me.kuehle.carreport.provider.reminder.ReminderColumns;
import android.app.NotificationManager;
import me.kuehle.carreport.Preferences;
import me.kuehle.carreport.presentation.CarPresenter;
import java.util.Date;
import android.app.PendingIntent;
import me.kuehle.carreport.provider.reminder.ReminderContentValues;
import androidx.annotation.RequiresApi;
import android.app.NotificationChannel;
import me.kuehle.carreport.provider.reminder.ReminderCursor;
import androidx.core.app.NotificationCompat$Style;
import android.text.TextUtils;
import java.util.ArrayList;
import androidx.core.app.NotificationCompat$InboxStyle;
import me.kuehle.carreport.gui.MainActivity;
import androidx.core.app.TaskStackBuilder;
import me.kuehle.carreport.gui.PreferencesRemindersFragment;
import android.content.Intent;
import me.kuehle.carreport.gui.PreferencesActivity;
import androidx.core.app.NotificationCompat$Builder;
import me.kuehle.carreport.provider.reminder.ReminderSelection;
import android.app.Notification;
import android.content.Context;
import android.app.IntentService;

public class ReminderService extends IntentService
{
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
    
    private static Notification buildNotification(final Context context, final long... array) {
        final ReminderCursor query = new ReminderSelection().id(array).query(context.getContentResolver());
        final NotificationCompat$Builder setPriority = new NotificationCompat$Builder(context, "reminders").setSmallIcon(2131230833).setCategory("alarm").setPriority(-1);
        final Intent intent = new Intent(context, (Class)PreferencesActivity.class);
        intent.putExtra(":android:show_fragment", PreferencesRemindersFragment.class.getName());
        intent.putExtra(":android:show_fragment_title", 2131820789);
        setPriority.setContentIntent(TaskStackBuilder.create(context).addNextIntentWithParentStack(new Intent(context, (Class)MainActivity.class)).addNextIntent(intent).getPendingIntent(0, 134217728));
        setPriority.setDeleteIntent(getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS", array));
        if (query.getCount() == 1) {
            query.moveToNext();
            setPriority.setContentTitle((CharSequence)context.getString(2131820746, new Object[] { query.getTitle() })).setContentText((CharSequence)query.getCarName()).addAction(2131230842, (CharSequence)context.getString(2131820741), getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE", array)).addAction(2131230858, (CharSequence)context.getString(2131820742), getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", array));
        }
        else {
            final NotificationCompat$InboxStyle style = new NotificationCompat$InboxStyle();
            style.setBigContentTitle((CharSequence)context.getString(2131820745));
            final ArrayList list = new ArrayList<String>(query.getCount());
            while (query.moveToNext()) {
                list.add(query.getTitle());
                style.addLine((CharSequence)String.format("%s (%s)", query.getTitle(), query.getCarName()));
            }
            setPriority.setContentTitle((CharSequence)context.getString(2131820745)).setContentText((CharSequence)TextUtils.join((CharSequence)", ", (Iterable)list)).setNumber(query.getCount()).setStyle((NotificationCompat$Style)style).addAction(2131230858, (CharSequence)context.getString(2131820743), getPendingIntent(context, "me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS", array));
        }
        return setPriority.build();
    }
    
    @RequiresApi(api = 26)
    private static NotificationChannel buildNotificationChannel(final Context context) {
        return new NotificationChannel("reminders", (CharSequence)context.getString(2131820744), 2);
    }
    
    public static void dismissReminders(final Context context, final long... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final long n = array[i];
            final ReminderContentValues reminderContentValues = new ReminderContentValues();
            reminderContentValues.putNotificationDismissed(true);
            reminderContentValues.update(context.getContentResolver(), new ReminderSelection().id(new long[] { n }));
        }
        updateNotification(context);
    }
    
    public static PendingIntent getPendingIntent(final Context context, final String action, final long... array) {
        final Intent intent = new Intent(context, (Class)ReminderService.class);
        intent.setAction(action);
        intent.putExtra("REMINDER_IDS", array);
        return PendingIntent.getService(context, 0, intent, 134217728);
    }
    
    public static void markRemindersDone(final Context context, final long... array) {
        final Date date = new Date();
        final CarPresenter instance = CarPresenter.getInstance(context);
        final ReminderCursor query = new ReminderSelection().id(array).query(context.getContentResolver());
        while (query.moveToNext()) {
            final ReminderContentValues reminderContentValues = new ReminderContentValues();
            reminderContentValues.putStartDate(date);
            reminderContentValues.putStartMileage(instance.getLatestMileage(query.getCarId()));
            reminderContentValues.putSnoozedUntilNull();
            reminderContentValues.putNotificationDismissed(false);
            reminderContentValues.update(context.getContentResolver(), new ReminderSelection().id(new long[] { query.getId() }));
        }
        updateNotification(context);
    }
    
    public static void snoozeReminders(final Context context, final long... array) {
        final Date addTo = new Preferences(context).getReminderSnoozeDuration().addTo(new Date());
        for (int length = array.length, i = 0; i < length; ++i) {
            final long n = array[i];
            final ReminderContentValues reminderContentValues = new ReminderContentValues();
            reminderContentValues.putSnoozedUntil(addTo);
            reminderContentValues.update(context.getContentResolver(), new ReminderSelection().id(new long[] { n }));
        }
        updateNotification(context);
    }
    
    public static void updateNotification(final Context context) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        final ArrayList<Long> list = new ArrayList<Long>();
        final ReminderCursor query = new ReminderSelection().query(context.getContentResolver(), ReminderColumns.ALL_COLUMNS);
        final ReminderQueries reminderQueries = new ReminderQueries(context, query);
        while (query.moveToNext()) {
            if (query.getNotificationDismissed()) {
                continue;
            }
            if (reminderQueries.isSnoozed()) {
                continue;
            }
            if (!reminderQueries.isDue()) {
                continue;
            }
            list.add(query.getId());
        }
        final long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = (long)list.get(i);
        }
        if (list.size() > 0) {
            if (Build$VERSION.SDK_INT >= 26) {
                notificationManager.createNotificationChannel(buildNotificationChannel(context));
            }
            notificationManager.notify(1, buildNotification(context, array));
            return;
        }
        notificationManager.cancel(1);
    }
    
    protected void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();
        if (action == null) {
            return;
        }
        final long[] longArrayExtra = intent.getLongArrayExtra("REMINDER_IDS");
        int n = -1;
        final int hashCode = action.hashCode();
        if (hashCode != 426954335) {
            if (hashCode != 1240190062) {
                if (hashCode != 1436350873) {
                    if (hashCode == 1488109147 && action.equals("me.kuehle.carreport.util.reminder.ReminderService.SNOOZE_REMINDERS")) {
                        n = 3;
                    }
                }
                else if (action.equals("me.kuehle.carreport.util.reminder.ReminderService.DISMISS_REMINDERS")) {
                    n = 2;
                }
            }
            else if (action.equals("me.kuehle.carreport.util.reminder.ReminderService.UPDATE_NOTIFICATION")) {
                n = 0;
            }
        }
        else if (action.equals("me.kuehle.carreport.util.reminder.ReminderService.MARK_REMINDERS_DONE")) {
            n = 1;
        }
        switch (n) {
            default: {}
            case 3: {
                snoozeReminders((Context)this, longArrayExtra);
            }
            case 2: {
                dismissReminders((Context)this, longArrayExtra);
            }
            case 1: {
                markRemindersDone((Context)this, longArrayExtra);
            }
            case 0: {
                updateNotification((Context)this);
            }
        }
    }
}
