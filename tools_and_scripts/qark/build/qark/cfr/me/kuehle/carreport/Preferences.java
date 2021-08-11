/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Environment
 *  android.preference.PreferenceManager
 *  android.text.TextUtils
 *  android.util.Log
 *  me.kuehle.carreport.DistanceEntryMode
 *  me.kuehle.carreport.PriceEntryMode
 *  me.kuehle.carreport.data.report.AbstractReport
 *  me.kuehle.carreport.data.report.CostsReport
 *  me.kuehle.carreport.data.report.FuelConsumptionReport
 *  me.kuehle.carreport.data.report.FuelPriceReport
 *  me.kuehle.carreport.data.report.MileageReport
 *  me.kuehle.carreport.provider.car.CarCursor
 *  me.kuehle.carreport.provider.car.CarSelection
 *  me.kuehle.carreport.provider.reminder.TimeSpanUnit
 *  me.kuehle.carreport.util.TimeSpan
 */
package me.kuehle.carreport;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import me.kuehle.carreport.DistanceEntryMode;
import me.kuehle.carreport.PriceEntryMode;
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

    public Preferences(Context context) {
        this.mContext = context;
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences((Context)context);
    }

    public boolean getAutoBackupEnabled() {
        return this.mPrefs.getBoolean("behavior_auto_backup", false);
    }

    public int getAutoBackupRetention() {
        return this.mPrefs.getInt("behaviour_keep_backups", 12);
    }

    public String getBackupPath() {
        String string = this.mPrefs.getString("backup_folder", "");
        if (string.isEmpty()) {
            return new File(Environment.getExternalStorageDirectory(), "CarReportBackups").getAbsolutePath();
        }
        return string;
    }

    public long getDefaultCar() {
        int n = Integer.parseInt(this.mPrefs.getString("behavior_default_car", "1"));
        CarCursor carCursor = new CarSelection().query(this.mContext.getContentResolver(), new String[]{"_id"});
        if (carCursor.getCount() == 0) {
            return 0L;
        }
        while (carCursor.moveToNext()) {
            if (carCursor.getId() != (long)n) continue;
            return n;
        }
        carCursor.moveToFirst();
        return carCursor.getId();
    }

    @Deprecated
    public String getDeprecatedDropboxAccessToken() {
        return this.mPrefs.getString("sync_dropbox_token", null);
    }

    @Deprecated
    public String getDeprecatedDropboxAccount() {
        return this.mPrefs.getString("sync_dropbox_account", null);
    }

    @Deprecated
    public String getDeprecatedDropboxLocalRev() {
        return this.mPrefs.getString("sync_dropbox_rev", null);
    }

    @Deprecated
    public String getDeprecatedGoogleDriveAccount() {
        return this.mPrefs.getString("sync_drive_account", null);
    }

    @Deprecated
    public Date getDeprecatedGoogleDriveLocalModifiedDate() {
        long l;
        block3 : {
            try {
                l = this.mPrefs.getLong("sync_drive_modified_date", -1L);
                if (l != -1L) break block3;
                return null;
            }
            catch (ClassCastException classCastException) {
                return null;
            }
        }
        Date date = new Date(l);
        return date;
    }

    @Deprecated
    public String getDeprecatedSynchronizationProvider() {
        return this.mPrefs.getString("sync_current_provider", null);
    }

    public DistanceEntryMode getDistanceEntryMode() {
        String string = this.mPrefs.getString("behavior_distance_entry_mode", "TOTAL");
        try {
            string = DistanceEntryMode.valueOf((String)string);
            return string;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return DistanceEntryMode.TOTAL;
        }
    }

    public PriceEntryMode getPriceEntryMode() {
        String string = this.mPrefs.getString("behavior_price_entry_mode", "TOTAL_AND_VOLUME");
        try {
            string = PriceEntryMode.valueOf((String)string);
            return string;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return PriceEntryMode.TOTAL_AND_VOLUME;
        }
    }

    public TimeSpan getReminderSnoozeDuration() {
        return TimeSpan.fromString((String)this.mPrefs.getString("behavior_reminder_snooze_duration", "7 DAY"), (TimeSpan)new TimeSpan(TimeSpanUnit.DAY, 7));
    }

    public List<Class<? extends AbstractReport>> getReportOrder() {
        ArrayList<Class<? extends AbstractReport>> arrayList = new ArrayList<Class<? extends AbstractReport>>();
        String[] arrstring = this.mPrefs.getString("behavior_report_order", null);
        if (arrstring == null) {
            arrayList.add(FuelConsumptionReport.class);
            arrayList.add(FuelPriceReport.class);
            arrayList.add(MileageReport.class);
            arrayList.add(CostsReport.class);
            return arrayList;
        }
        for (String string : arrstring.split(",")) {
            try {
                Class class_ = Class.forName(string);
                if (!AbstractReport.class.isAssignableFrom(class_)) continue;
                arrayList.add(class_);
            }
            catch (Exception exception) {
                Log.w((String)"Preferences", (String)String.format("Error loading report order: %s.", string), (Throwable)exception);
            }
        }
        return arrayList;
    }

    public String getSyncLocalFileRev() {
        return this.mPrefs.getString("sync_local_file_rev", null);
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
        SharedPreferences.Editor editor = this.mPrefs.edit();
        editor.remove("sync_dropbox_account");
        editor.remove("sync_dropbox_token");
        editor.remove("sync_dropbox_rev");
        editor.remove("sync_drive_modified_date");
        editor.remove("sync_drive_account");
        editor.remove("sync_current_provider");
        editor.remove("sync_on_change");
        editor.remove("sync_on_start");
        editor.apply();
    }

    public void setReportOrder(List<Class<? extends AbstractReport>> object) {
        ArrayList<String> arrayList = new ArrayList<String>();
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add(((Class)object.next()).getName());
        }
        object = this.mPrefs.edit();
        object.putString("behavior_report_order", TextUtils.join((CharSequence)",", arrayList));
        object.apply();
    }

    public void setSyncLocalFileRev(String string) {
        SharedPreferences.Editor editor = this.mPrefs.edit();
        editor.putString("sync_local_file_rev", string);
        editor.apply();
    }
}

