// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport;

import java.util.Iterator;
import android.text.TextUtils;
import android.content.SharedPreferences$Editor;
import android.util.Log;
import me.kuehle.carreport.data.report.CostsReport;
import me.kuehle.carreport.data.report.MileageReport;
import me.kuehle.carreport.data.report.FuelPriceReport;
import me.kuehle.carreport.data.report.FuelConsumptionReport;
import java.util.ArrayList;
import me.kuehle.carreport.data.report.AbstractReport;
import java.util.List;
import me.kuehle.carreport.provider.reminder.TimeSpanUnit;
import me.kuehle.carreport.util.TimeSpan;
import java.util.Date;
import me.kuehle.carreport.provider.car.CarCursor;
import me.kuehle.carreport.provider.car.CarSelection;
import java.io.File;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.Context;

public class Preferences
{
    private static final String TAG = "Preferences";
    private Context mContext;
    private SharedPreferences mPrefs;
    
    public Preferences(final Context mContext) {
        this.mContext = mContext;
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    
    public boolean getAutoBackupEnabled() {
        return this.mPrefs.getBoolean("behavior_auto_backup", false);
    }
    
    public int getAutoBackupRetention() {
        return this.mPrefs.getInt("behaviour_keep_backups", 12);
    }
    
    public String getBackupPath() {
        final String string = this.mPrefs.getString("backup_folder", "");
        if (string.isEmpty()) {
            return new File(Environment.getExternalStorageDirectory(), "CarReportBackups").getAbsolutePath();
        }
        return string;
    }
    
    public long getDefaultCar() {
        final int int1 = Integer.parseInt(this.mPrefs.getString("behavior_default_car", "1"));
        final CarCursor query = new CarSelection().query(this.mContext.getContentResolver(), new String[] { "_id" });
        if (query.getCount() == 0) {
            return 0L;
        }
        while (query.moveToNext()) {
            if (query.getId() == int1) {
                return int1;
            }
        }
        query.moveToFirst();
        return query.getId();
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
        try {
            final long long1 = this.mPrefs.getLong("sync_drive_modified_date", -1L);
            if (long1 == -1L) {
                return null;
            }
            return new Date(long1);
        }
        catch (ClassCastException ex) {
            return null;
        }
    }
    
    @Deprecated
    public String getDeprecatedSynchronizationProvider() {
        return this.mPrefs.getString("sync_current_provider", (String)null);
    }
    
    public DistanceEntryMode getDistanceEntryMode() {
        final String string = this.mPrefs.getString("behavior_distance_entry_mode", "TOTAL");
        try {
            return DistanceEntryMode.valueOf(string);
        }
        catch (IllegalArgumentException ex) {
            return DistanceEntryMode.TOTAL;
        }
    }
    
    public PriceEntryMode getPriceEntryMode() {
        final String string = this.mPrefs.getString("behavior_price_entry_mode", "TOTAL_AND_VOLUME");
        try {
            return PriceEntryMode.valueOf(string);
        }
        catch (IllegalArgumentException ex) {
            return PriceEntryMode.TOTAL_AND_VOLUME;
        }
    }
    
    public TimeSpan getReminderSnoozeDuration() {
        return TimeSpan.fromString(this.mPrefs.getString("behavior_reminder_snooze_duration", "7 DAY"), new TimeSpan(TimeSpanUnit.DAY, 7));
    }
    
    public List<Class<? extends AbstractReport>> getReportOrder() {
        final ArrayList<Class<? extends AbstractReport>> list = new ArrayList<Class<? extends AbstractReport>>();
        final String string = this.mPrefs.getString("behavior_report_order", (String)null);
        if (string == null) {
            list.add((Class<? extends AbstractReport>)FuelConsumptionReport.class);
            list.add((Class<? extends AbstractReport>)FuelPriceReport.class);
            list.add((Class<? extends AbstractReport>)MileageReport.class);
            list.add((Class<? extends AbstractReport>)CostsReport.class);
            return list;
        }
        final String[] split = string.split(",");
        for (int length = split.length, i = 0; i < length; ++i) {
            final String s = split[i];
            try {
                final Class<?> forName = Class.forName(s);
                if (AbstractReport.class.isAssignableFrom(forName)) {
                    list.add((Class<? extends AbstractReport>)forName);
                }
            }
            catch (Exception ex) {
                Log.w("Preferences", String.format("Error loading report order: %s.", s), (Throwable)ex);
            }
        }
        return list;
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
        final SharedPreferences$Editor edit = this.mPrefs.edit();
        edit.remove("sync_dropbox_account");
        edit.remove("sync_dropbox_token");
        edit.remove("sync_dropbox_rev");
        edit.remove("sync_drive_modified_date");
        edit.remove("sync_drive_account");
        edit.remove("sync_current_provider");
        edit.remove("sync_on_change");
        edit.remove("sync_on_start");
        edit.apply();
    }
    
    public void setReportOrder(final List<Class<? extends AbstractReport>> list) {
        final ArrayList<String> list2 = new ArrayList<String>();
        final Iterator<Class<? extends AbstractReport>> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add(iterator.next().getName());
        }
        final SharedPreferences$Editor edit = this.mPrefs.edit();
        edit.putString("behavior_report_order", TextUtils.join((CharSequence)",", (Iterable)list2));
        edit.apply();
    }
    
    public void setSyncLocalFileRev(final String s) {
        final SharedPreferences$Editor edit = this.mPrefs.edit();
        edit.putString("sync_local_file_rev", s);
        edit.apply();
    }
}
