// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport.model;

import me.kuehle.carreport.model.dao.ReminderDAO;
import me.kuehle.carreport.model.dao.RefuelingDAO;
import me.kuehle.carreport.model.dao.OtherCostDAO;
import me.kuehle.carreport.model.dao.FuelTypeDAO;
import me.kuehle.carreport.model.dao.CarDAO;
import me.kuehle.carreport.provider.DataSQLiteOpenHelper;
import androidx.room.migration.Migration;
import androidx.room.Room;
import android.content.Context;
import me.kuehle.carreport.model.entity.helper.SQLTypeConverters;
import androidx.room.TypeConverters;
import me.kuehle.carreport.model.entity.OtherCost;
import me.kuehle.carreport.model.entity.Refueling;
import me.kuehle.carreport.model.entity.Reminder;
import me.kuehle.carreport.model.entity.FuelType;
import me.kuehle.carreport.model.entity.Car;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Car.class, FuelType.class, Reminder.class, Refueling.class, OtherCost.class }, version = 11)
@TypeConverters({ SQLTypeConverters.class })
public abstract class CarReportDatabase extends RoomDatabase
{
    private static final String DB_NAME = "data.db";
    private static final String LOG_TAG = "CarReportDatabase";
    private static CarReportDatabase sInstance;
    
    public static CarReportDatabase getInstance(final Context context) {
        while (true) {
            while (true) {
                Label_0186: {
                    synchronized (CarReportDatabase.class) {
                        if (CarReportDatabase.sInstance == null) {
                            CarReportDatabase.sInstance = (CarReportDatabase)Room.databaseBuilder(context, (Class)CarReportDatabase.class, "data.db").allowMainThreadQueries().addMigrations(new Migration[] { (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 2), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 3), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 4), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 5), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 6), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 7), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 8), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 9), (Migration)new CarReportDatabase.CarReportDatabase$AssetFileBasedMigration(context, 10), (Migration)new CarReportDatabase$1(10, 11) }).build();
                            return CarReportDatabase.sInstance;
                        }
                        break Label_0186;
                    }
                }
                continue;
            }
        }
    }
    
    public static void resetInstance() {
        DataSQLiteOpenHelper.resetInstance();
        final CarReportDatabase sInstance = CarReportDatabase.sInstance;
        if (sInstance != null) {
            sInstance.close();
            CarReportDatabase.sInstance = null;
        }
    }
    
    public abstract CarDAO getCarDao();
    
    public abstract FuelTypeDAO getFuelTypeDao();
    
    public abstract OtherCostDAO getOtherCostDao();
    
    public abstract RefuelingDAO getRefuelingDao();
    
    public abstract ReminderDAO getReminderDao();
}
