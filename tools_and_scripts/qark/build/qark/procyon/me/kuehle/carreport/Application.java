// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport;

import me.kuehle.carreport.util.reminder.ReminderEnablerReceiver;
import net.danlew.android.joda.JodaTimeAndroid;
import java.util.Date;
import android.os.Bundle;
import android.accounts.Account;
import me.kuehle.carreport.util.reminder.ReminderService;
import android.database.ContentObserver;
import android.os.Handler;
import android.net.Uri;
import android.content.Context;
import me.kuehle.carreport.provider.DataSQLiteOpenHelper;
import me.kuehle.carreport.model.CarReportDatabase;
import android.util.Log;
import android.accounts.AccountManager;
import androidx.multidex.MultiDexApplication;

public class Application extends MultiDexApplication
{
    private static final String TAG = "Application";
    private static Application instance;
    private AccountManager mAccountManager;
    
    public static void closeDatabases() {
        if (Application.instance != null) {
            Log.v("Application", "Closing Database via abstraction layers.");
            CarReportDatabase.resetInstance();
            DataSQLiteOpenHelper.resetInstance();
        }
    }
    
    public static Context getContext() {
        return (Context)Application.instance;
    }
    
    private void setupDataChangeObserver() {
        this.getContentResolver().registerContentObserver(Uri.parse("content://me.kuehle.carreport.provider"), true, (ContentObserver)new ContentObserver(new Handler()) {
            public boolean deliverSelfNotifications() {
                return true;
            }
            
            public void onChange(final boolean b) {
                this.onChange(b, null);
            }
            
            public void onChange(final boolean b, final Uri uri) {
                Application.this.updateReminders();
            }
        });
    }
    
    private void updateReminders() {
        if (BuildConfig.DEBUG) {
            Log.d("Application", "updateReminders");
        }
        ReminderService.updateNotification((Context)Application.instance);
    }
    
    private void upgradeOldSyncServiceToNewSyncAdapterWithAccounts() {
        final Preferences preferences = new Preferences((Context)this);
        final String deprecatedSynchronizationProvider = preferences.getDeprecatedSynchronizationProvider();
        if (deprecatedSynchronizationProvider != null) {
            long n = 0L;
            Account account = null;
            String deprecatedDropboxAccessToken = null;
            if (deprecatedSynchronizationProvider.equals("me.kuehle.carreport.util.backup.DropboxSynchronizationProvider")) {
                n = 1L;
                account = new Account(preferences.getDeprecatedDropboxAccount(), "me.kuehle.carreport.sync");
                deprecatedDropboxAccessToken = preferences.getDeprecatedDropboxAccessToken();
                preferences.setSyncLocalFileRev(preferences.getDeprecatedDropboxLocalRev());
            }
            else if (deprecatedSynchronizationProvider.equals("me.kuehle.carreport.util.backup.GoogleDriveSynchronizationProvider")) {
                n = 2L;
                final Account account2 = new Account(preferences.getDeprecatedGoogleDriveAccount(), "me.kuehle.carreport.sync");
                deprecatedDropboxAccessToken = null;
                final Date deprecatedGoogleDriveLocalModifiedDate = preferences.getDeprecatedGoogleDriveLocalModifiedDate();
                String value;
                if (deprecatedGoogleDriveLocalModifiedDate != null) {
                    value = String.valueOf(deprecatedGoogleDriveLocalModifiedDate.getTime());
                }
                else {
                    value = null;
                }
                preferences.setSyncLocalFileRev(value);
                account = account2;
            }
            if (account != null) {
                this.mAccountManager.addAccountExplicitly(account, (String)null, (Bundle)null);
                this.mAccountManager.setAuthToken(account, "Default", deprecatedDropboxAccessToken);
                this.mAccountManager.setUserData(account, "me.kuehle.carreport.sync.provider", String.valueOf(n));
            }
            preferences.removeDeprecatedSyncSettings();
        }
    }
    
    public void onCreate() {
        super.onCreate();
        Application.instance = this;
        this.mAccountManager = AccountManager.get((Context)this);
        JodaTimeAndroid.init((Context)this);
        ReminderEnablerReceiver.scheduleAlarms((Context)this);
        this.upgradeOldSyncServiceToNewSyncAdapterWithAccounts();
        this.setupDataChangeObserver();
    }
}
