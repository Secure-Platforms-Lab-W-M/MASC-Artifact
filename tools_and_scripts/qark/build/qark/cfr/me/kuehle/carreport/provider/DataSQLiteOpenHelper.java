/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.database.DatabaseErrorHandler
 *  android.database.DefaultDatabaseErrorHandler
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  androidx.sqlite.db.SupportSQLiteOpenHelper
 *  me.kuehle.carreport.model.dao.CarDAO
 *  me.kuehle.carreport.model.dao.FuelTypeDAO
 *  me.kuehle.carreport.model.dao.OtherCostDAO
 *  me.kuehle.carreport.model.dao.RefuelingDAO
 *  me.kuehle.carreport.model.dao.ReminderDAO
 *  me.kuehle.carreport.provider.DataSQLiteOpenHelperCallbacks
 */
package me.kuehle.carreport.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.io.File;
import java.util.List;
import me.kuehle.carreport.BuildConfig;
import me.kuehle.carreport.model.CarReportDatabase;
import me.kuehle.carreport.model.dao.CarDAO;
import me.kuehle.carreport.model.dao.FuelTypeDAO;
import me.kuehle.carreport.model.dao.OtherCostDAO;
import me.kuehle.carreport.model.dao.RefuelingDAO;
import me.kuehle.carreport.model.dao.ReminderDAO;
import me.kuehle.carreport.provider.DataSQLiteOpenHelperCallbacks;

@Deprecated
public class DataSQLiteOpenHelper
extends SQLiteOpenHelper {
    public static final String DATABASE_FILE_NAME = "data.db";
    private static final int DATABASE_VERSION = 11;
    public static final String SQL_CREATE_TABLE_CAR = "CREATE TABLE IF NOT EXISTS car ( _id INTEGER PRIMARY KEY AUTOINCREMENT, car__name TEXT NOT NULL, color INTEGER NOT NULL, initial_mileage INTEGER NOT NULL DEFAULT 0, suspended_since INTEGER  );";
    public static final String SQL_CREATE_TABLE_FUEL_TYPE = "CREATE TABLE IF NOT EXISTS fuel_type ( _id INTEGER PRIMARY KEY AUTOINCREMENT, fuel_type__name TEXT NOT NULL, category TEXT , CONSTRAINT unique_name UNIQUE (fuel_type__name) ON CONFLICT REPLACE );";
    public static final String SQL_CREATE_TABLE_OTHER_COST = "CREATE TABLE IF NOT EXISTS other_cost ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, date INTEGER NOT NULL, mileage INTEGER, price REAL NOT NULL, recurrence_interval INTEGER NOT NULL, recurrence_multiplier INTEGER NOT NULL, end_date INTEGER, note TEXT NOT NULL, car_id INTEGER NOT NULL , CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    public static final String SQL_CREATE_TABLE_REFUELING = "CREATE TABLE IF NOT EXISTS refueling ( _id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER NOT NULL, mileage INTEGER NOT NULL, volume REAL NOT NULL, price REAL NOT NULL, partial INTEGER NOT NULL, note TEXT NOT NULL, fuel_type_id INTEGER NOT NULL, car_id INTEGER NOT NULL , CONSTRAINT fk_fuel_type_id FOREIGN KEY (fuel_type_id) REFERENCES fuel_type (_id) ON DELETE CASCADE, CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    public static final String SQL_CREATE_TABLE_REMINDER = "CREATE TABLE IF NOT EXISTS reminder ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, after_time_span_unit INTEGER, after_time_span_count INTEGER, after_distance INTEGER, start_date INTEGER NOT NULL, start_mileage INTEGER NOT NULL, notification_dismissed INTEGER NOT NULL, snoozed_until INTEGER, car_id INTEGER NOT NULL , CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    private static final String TAG = DataSQLiteOpenHelper.class.getSimpleName();
    private static DataSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final DataSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    private DataSQLiteOpenHelper(Context context, String string, DatabaseErrorHandler databaseErrorHandler) {
        super(context, string, null, 11, databaseErrorHandler);
        this.mContext = context;
        this.mOpenHelperCallbacks = new DataSQLiteOpenHelperCallbacks();
    }

    public static DataSQLiteOpenHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = DataSQLiteOpenHelper.newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static DataSQLiteOpenHelper newInstance(Context context) {
        Object object = CarReportDatabase.getInstance(context);
        object.getCarDao().getAll();
        object.getFuelTypeDao().getAll();
        object.getOtherCostDao().getAll();
        object.getRefuelingDao().getAll();
        object.getReminderDao().getAll();
        object = object.getOpenHelper().getDatabaseName();
        Log.d((String)TAG, (String)String.format("Using database at %s via classic content provider.", context.getDatabasePath((String)object).getAbsolutePath()));
        return DataSQLiteOpenHelper.newInstancePostHoneycomb(context, (String)object);
    }

    private static DataSQLiteOpenHelper newInstancePostHoneycomb(Context context, String string) {
        return new DataSQLiteOpenHelper(context, string, (DatabaseErrorHandler)new DefaultDatabaseErrorHandler());
    }

    public static void resetInstance() {
        if (sInstance != null) {
            Log.d((String)TAG, (String)"resetting instance");
            sInstance.close();
            sInstance = null;
            return;
        }
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase sQLiteDatabase) {
        if (Build.VERSION.SDK_INT < 16) {
            this.setForeignKeyConstraintsEnabledPreJellyBean(sQLiteDatabase);
            return;
        }
        this.setForeignKeyConstraintsEnabledPostJellyBean(sQLiteDatabase);
    }

    @TargetApi(value=16)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        if (BuildConfig.DEBUG) {
            Log.d((String)TAG, (String)"onCreate");
        }
        this.mOpenHelperCallbacks.onPreCreate(this.mContext, sQLiteDatabase);
        this.mOpenHelperCallbacks.onPostCreate(this.mContext, sQLiteDatabase);
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        super.onOpen(sQLiteDatabase);
        if (!sQLiteDatabase.isReadOnly()) {
            this.setForeignKeyConstraintsEnabled(sQLiteDatabase);
        }
        this.mOpenHelperCallbacks.onOpen(this.mContext, sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
    }
}

