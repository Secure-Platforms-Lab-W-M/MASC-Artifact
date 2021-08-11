/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.room.Room
 *  androidx.room.RoomDatabase$Builder
 *  androidx.room.migration.Migration
 *  me.kuehle.carreport.model.CarReportDatabase$1
 *  me.kuehle.carreport.model.CarReportDatabase$AssetFileBasedMigration
 *  me.kuehle.carreport.model.dao.CarDAO
 *  me.kuehle.carreport.model.dao.FuelTypeDAO
 *  me.kuehle.carreport.model.dao.OtherCostDAO
 *  me.kuehle.carreport.model.dao.RefuelingDAO
 *  me.kuehle.carreport.model.dao.ReminderDAO
 *  me.kuehle.carreport.model.entity.Car
 *  me.kuehle.carreport.model.entity.FuelType
 *  me.kuehle.carreport.model.entity.OtherCost
 *  me.kuehle.carreport.model.entity.Refueling
 *  me.kuehle.carreport.model.entity.Reminder
 *  me.kuehle.carreport.model.entity.helper.SQLTypeConverters
 */
package me.kuehle.carreport.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import me.kuehle.carreport.model.CarReportDatabase;
import me.kuehle.carreport.model.dao.CarDAO;
import me.kuehle.carreport.model.dao.FuelTypeDAO;
import me.kuehle.carreport.model.dao.OtherCostDAO;
import me.kuehle.carreport.model.dao.RefuelingDAO;
import me.kuehle.carreport.model.dao.ReminderDAO;
import me.kuehle.carreport.model.entity.Car;
import me.kuehle.carreport.model.entity.FuelType;
import me.kuehle.carreport.model.entity.OtherCost;
import me.kuehle.carreport.model.entity.Refueling;
import me.kuehle.carreport.model.entity.Reminder;
import me.kuehle.carreport.model.entity.helper.SQLTypeConverters;
import me.kuehle.carreport.provider.DataSQLiteOpenHelper;

@Database(entities={Car.class, FuelType.class, Reminder.class, Refueling.class, OtherCost.class}, version=11)
@TypeConverters(value={SQLTypeConverters.class})
public abstract class CarReportDatabase
extends androidx.room.RoomDatabase {
    private static final String DB_NAME = "data.db";
    private static final String LOG_TAG = "CarReportDatabase";
    private static CarReportDatabase sInstance;

    public static CarReportDatabase getInstance(Context object) {
        synchronized (CarReportDatabase.class) {
            if (sInstance == null) {
                sInstance = (CarReportDatabase)Room.databaseBuilder((Context)object, CarReportDatabase.class, (String)"data.db").allowMainThreadQueries().addMigrations(new Migration[]{new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */, new 1(10, 11)}).build();
            }
            object = sInstance;
            return object;
        }
    }

    public static void resetInstance() {
        DataSQLiteOpenHelper.resetInstance();
        CarReportDatabase carReportDatabase = sInstance;
        if (carReportDatabase != null) {
            carReportDatabase.close();
            sInstance = null;
            return;
        }
    }

    public abstract CarDAO getCarDao();

    public abstract FuelTypeDAO getFuelTypeDao();

    public abstract OtherCostDAO getOtherCostDao();

    public abstract RefuelingDAO getRefuelingDao();

    public abstract ReminderDAO getReminderDao();
}

