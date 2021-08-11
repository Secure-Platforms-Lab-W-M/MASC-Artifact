package me.kuehle.carreport;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.multidex.MultiDexApplication;
import java.util.Date;
import me.kuehle.carreport.model.CarReportDatabase;
import me.kuehle.carreport.provider.DataSQLiteOpenHelper;
import me.kuehle.carreport.util.reminder.ReminderEnablerReceiver;
import me.kuehle.carreport.util.reminder.ReminderService;
import net.danlew.android.joda.JodaTimeAndroid;

public class Application extends MultiDexApplication {
   private static final String TAG = "Application";
   private static Application instance;
   private AccountManager mAccountManager;

   public static void closeDatabases() {
      if (instance != null) {
         Log.v("Application", "Closing Database via abstraction layers.");
         CarReportDatabase.resetInstance();
         DataSQLiteOpenHelper.resetInstance();
      }
   }

   public static Context getContext() {
      return instance;
   }

   private void setupDataChangeObserver() {
      ContentObserver var1 = new ContentObserver(new Handler()) {
         public boolean deliverSelfNotifications() {
            return true;
         }

         public void onChange(boolean var1) {
            this.onChange(var1, (Uri)null);
         }

         public void onChange(boolean var1, Uri var2) {
            Application.this.updateReminders();
         }
      };
      this.getContentResolver().registerContentObserver(Uri.parse("content://me.kuehle.carreport.provider"), true, var1);
   }

   private void updateReminders() {
      if (BuildConfig.DEBUG) {
         Log.d("Application", "updateReminders");
      }

      ReminderService.updateNotification(instance);
   }

   private void upgradeOldSyncServiceToNewSyncAdapterWithAccounts() {
      Preferences var6 = new Preferences(this);
      String var5 = var6.getDeprecatedSynchronizationProvider();
      if (var5 != null) {
         long var1 = 0L;
         Account var3 = null;
         String var4 = null;
         if (var5.equals("me.kuehle.carreport.util.backup.DropboxSynchronizationProvider")) {
            var1 = 1L;
            var3 = new Account(var6.getDeprecatedDropboxAccount(), "me.kuehle.carreport.sync");
            var4 = var6.getDeprecatedDropboxAccessToken();
            var6.setSyncLocalFileRev(var6.getDeprecatedDropboxLocalRev());
         } else if (var5.equals("me.kuehle.carreport.util.backup.GoogleDriveSynchronizationProvider")) {
            var1 = 2L;
            Account var7 = new Account(var6.getDeprecatedGoogleDriveAccount(), "me.kuehle.carreport.sync");
            var4 = null;
            Date var8 = var6.getDeprecatedGoogleDriveLocalModifiedDate();
            String var9;
            if (var8 != null) {
               var9 = String.valueOf(var8.getTime());
            } else {
               var9 = null;
            }

            var6.setSyncLocalFileRev(var9);
            var3 = var7;
         }

         if (var3 != null) {
            this.mAccountManager.addAccountExplicitly(var3, (String)null, (Bundle)null);
            this.mAccountManager.setAuthToken(var3, "Default", var4);
            this.mAccountManager.setUserData(var3, "me.kuehle.carreport.sync.provider", String.valueOf(var1));
         }

         var6.removeDeprecatedSyncSettings();
      }
   }

   public void onCreate() {
      super.onCreate();
      instance = this;
      this.mAccountManager = AccountManager.get(this);
      JodaTimeAndroid.init(this);
      ReminderEnablerReceiver.scheduleAlarms(this);
      this.upgradeOldSyncServiceToNewSyncAdapterWithAccounts();
      this.setupDataChangeObserver();
   }
}
