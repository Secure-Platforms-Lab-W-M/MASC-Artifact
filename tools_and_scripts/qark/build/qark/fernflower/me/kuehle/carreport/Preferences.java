package me.kuehle.carreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import me.kuehle.carreport.data.report.AbstractReport;
import me.kuehle.carreport.data.report.CostsReport;
import me.kuehle.carreport.data.report.FuelConsumptionReport;
import me.kuehle.carreport.data.report.FuelPriceReport;
import me.kuehle.carreport.data.report.MileageReport;
import me.kuehle.carreport.provider.car.CarCursor;
import me.kuehle.carreport.provider.car.CarSelection;
import me.kuehle.carreport.provider.reminder.TimeSpanUnit;
import me.kuehle.carreport.util.TimeSpan;

public class Preferences {
   private static final String TAG = "Preferences";
   private Context mContext;
   private SharedPreferences mPrefs;

   public Preferences(Context var1) {
      this.mContext = var1;
      this.mPrefs = PreferenceManager.getDefaultSharedPreferences(var1);
   }

   public boolean getAutoBackupEnabled() {
      return this.mPrefs.getBoolean("behavior_auto_backup", false);
   }

   public int getAutoBackupRetention() {
      return this.mPrefs.getInt("behaviour_keep_backups", 12);
   }

   public String getBackupPath() {
      String var1 = this.mPrefs.getString("backup_folder", "");
      return var1.isEmpty() ? (new File(Environment.getExternalStorageDirectory(), "CarReportBackups")).getAbsolutePath() : var1;
   }

   public long getDefaultCar() {
      int var1 = Integer.parseInt(this.mPrefs.getString("behavior_default_car", "1"));
      CarCursor var2 = (new CarSelection()).query(this.mContext.getContentResolver(), new String[]{"_id"});
      if (var2.getCount() == 0) {
         return 0L;
      } else {
         do {
            if (!var2.moveToNext()) {
               var2.moveToFirst();
               return var2.getId();
            }
         } while(var2.getId() != (long)var1);

         return (long)var1;
      }
   }

   @Deprecated
   public String getDeprecatedDropboxAccessToken() {
      return this.mPrefs.getString("sync_dropbox_token", (String)null);
   }

   @Deprecated
   public String getDeprecatedDropboxAccount() {
      return this.mPrefs.getString("sync_dropbox_account", (String)null);
   }

   @Deprecated
   public String getDeprecatedDropboxLocalRev() {
      return this.mPrefs.getString("sync_dropbox_rev", (String)null);
   }

   @Deprecated
   public String getDeprecatedGoogleDriveAccount() {
      return this.mPrefs.getString("sync_drive_account", (String)null);
   }

   @Deprecated
   public Date getDeprecatedGoogleDriveLocalModifiedDate() {
      // $FF: Couldn't be decompiled
   }

   @Deprecated
   public String getDeprecatedSynchronizationProvider() {
      return this.mPrefs.getString("sync_current_provider", (String)null);
   }

   public DistanceEntryMode getDistanceEntryMode() {
      String var1 = this.mPrefs.getString("behavior_distance_entry_mode", "TOTAL");

      try {
         DistanceEntryMode var3 = DistanceEntryMode.valueOf(var1);
         return var3;
      } catch (IllegalArgumentException var2) {
         return DistanceEntryMode.TOTAL;
      }
   }

   public PriceEntryMode getPriceEntryMode() {
      String var1 = this.mPrefs.getString("behavior_price_entry_mode", "TOTAL_AND_VOLUME");

      try {
         PriceEntryMode var3 = PriceEntryMode.valueOf(var1);
         return var3;
      } catch (IllegalArgumentException var2) {
         return PriceEntryMode.TOTAL_AND_VOLUME;
      }
   }

   public TimeSpan getReminderSnoozeDuration() {
      return TimeSpan.fromString(this.mPrefs.getString("behavior_reminder_snooze_duration", "7 DAY"), new TimeSpan(TimeSpanUnit.DAY, 7));
   }

   public List getReportOrder() {
      ArrayList var3 = new ArrayList();
      String var4 = this.mPrefs.getString("behavior_report_order", (String)null);
      if (var4 == null) {
         var3.add(FuelConsumptionReport.class);
         var3.add(FuelPriceReport.class);
         var3.add(MileageReport.class);
         var3.add(CostsReport.class);
         return var3;
      } else {
         String[] var8 = var4.split(",");
         int var2 = var8.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            String var5 = var8[var1];

            try {
               Class var6 = Class.forName(var5);
               if (AbstractReport.class.isAssignableFrom(var6)) {
                  var3.add(var6);
               }
            } catch (Exception var7) {
               Log.w("Preferences", String.format("Error loading report order: %s.", var5), var7);
            }
         }

         return var3;
      }
   }

   public String getSyncLocalFileRev() {
      return this.mPrefs.getString("sync_local_file_rev", (String)null);
   }

   public String getUnitCurrency() {
      return this.mPrefs.getString("unit_currency", "EUR");
   }

   public String getUnitDistance() {
      return this.mPrefs.getString("unit_distance", "km");
   }

   public int getUnitFuelConsumption() {
      return Integer.parseInt(this.mPrefs.getString("unit_fuel_consumption", "0"));
   }

   public String getUnitVolume() {
      return this.mPrefs.getString("unit_volume", "l");
   }

   public boolean isAutoGuessMissingDataEnabled() {
      return this.mPrefs.getBoolean("behavior_auto_guess_missing_data", false);
   }

   public boolean isShowCarMenu() {
      return this.mPrefs.getBoolean("behavior_show_car_menu", true);
   }

   @Deprecated
   public void removeDeprecatedSyncSettings() {
      Editor var1 = this.mPrefs.edit();
      var1.remove("sync_dropbox_account");
      var1.remove("sync_dropbox_token");
      var1.remove("sync_dropbox_rev");
      var1.remove("sync_drive_modified_date");
      var1.remove("sync_drive_account");
      var1.remove("sync_current_provider");
      var1.remove("sync_on_change");
      var1.remove("sync_on_start");
      var1.apply();
   }

   public void setReportOrder(List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var2.add(((Class)var3.next()).getName());
      }

      Editor var4 = this.mPrefs.edit();
      var4.putString("behavior_report_order", TextUtils.join(",", var2));
      var4.apply();
   }

   public void setSyncLocalFileRev(String var1) {
      Editor var2 = this.mPrefs.edit();
      var2.putString("sync_local_file_rev", var1);
      var2.apply();
   }
}
