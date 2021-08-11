/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.accounts.AccountManager
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.ContentObserver
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.util.Log
 */
package me.kuehle.carreport;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.multidex.MultiDexApplication;
import java.util.Date;
import me.kuehle.carreport.BuildConfig;
import me.kuehle.carreport.Preferences;
import me.kuehle.carreport.model.CarReportDatabase;
import me.kuehle.carreport.provider.DataSQLiteOpenHelper;
import me.kuehle.carreport.util.reminder.ReminderEnablerReceiver;
import me.kuehle.carreport.util.reminder.ReminderService;
import net.danlew.android.joda.JodaTimeAndroid;

public class Application
extends MultiDexApplication {
    private static final String TAG = "Application";
    private static Application instance;
    private AccountManager mAccountManager;

    public static void closeDatabases() {
        if (instance != null) {
            Log.v((String)"Application", (String)"Closing Database via abstraction layers.");
            CarReportDatabase.resetInstance();
            DataSQLiteOpenHelper.resetInstance();
            return;
        }
    }

    public static Context getContext() {
        return instance;
    }

    private void setupDataChangeObserver() {
        ContentObserver contentObserver = new ContentObserver(new Handler()){

            public boolean deliverSelfNotifications() {
                return true;
            }

            public void onChange(boolean bl) {
                this.onChange(bl, null);
            }

            public void onChange(boolean bl, Uri uri) {
                Application.this.updateReminders();
            }
        };
        this.getContentResolver().registerContentObserver(Uri.parse((String)"content://me.kuehle.carreport.provider"), true, contentObserver);
    }

    private void updateReminders() {
        if (BuildConfig.DEBUG) {
            Log.d((String)"Application", (String)"updateReminders");
        }
        ReminderService.updateNotification((Context)instance);
    }

    private void upgradeOldSyncServiceToNewSyncAdapterWithAccounts() {
        Preferences preferences = new Preferences((Context)this);
        String string = preferences.getDeprecatedSynchronizationProvider();
        if (string != null) {
            long l = 0L;
            Object object = null;
            String string2 = null;
            if (string.equals("me.kuehle.carreport.util.backup.DropboxSynchronizationProvider")) {
                l = 1L;
                object = new Account(preferences.getDeprecatedDropboxAccount(), "me.kuehle.carreport.sync");
                string2 = preferences.getDeprecatedDropboxAccessToken();
                preferences.setSyncLocalFileRev(preferences.getDeprecatedDropboxLocalRev());
            } else if (string.equals("me.kuehle.carreport.util.backup.GoogleDriveSynchronizationProvider")) {
                l = 2L;
                string = new Account(preferences.getDeprecatedGoogleDriveAccount(), "me.kuehle.carreport.sync");
                string2 = null;
                object = preferences.getDeprecatedGoogleDriveLocalModifiedDate();
                object = object != null ? String.valueOf(object.getTime()) : null;
                preferences.setSyncLocalFileRev((String)object);
                object = string;
            }
            if (object != null) {
                this.mAccountManager.addAccountExplicitly((Account)object, null, null);
                this.mAccountManager.setAuthToken((Account)object, "Default", string2);
                this.mAccountManager.setUserData((Account)object, "me.kuehle.carreport.sync.provider", String.valueOf(l));
            }
            preferences.removeDeprecatedSyncSettings();
            return;
        }
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        this.mAccountManager = AccountManager.get((Context)this);
        JodaTimeAndroid.init((Context)this);
        ReminderEnablerReceiver.scheduleAlarms((Context)this);
        this.upgradeOldSyncServiceToNewSyncAdapterWithAccounts();
        this.setupDataChangeObserver();
    }

}

