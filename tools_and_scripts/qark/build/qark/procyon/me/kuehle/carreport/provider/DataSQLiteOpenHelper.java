// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package me.kuehle.carreport.provider;

import me.kuehle.carreport.BuildConfig;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.database.sqlite.SQLiteDatabase;
import android.database.DefaultDatabaseErrorHandler;
import android.util.Log;
import me.kuehle.carreport.model.CarReportDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.DatabaseErrorHandler;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class DataSQLiteOpenHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_FILE_NAME = "data.db";
    private static final int DATABASE_VERSION = 11;
    public static final String SQL_CREATE_TABLE_CAR = "CREATE TABLE IF NOT EXISTS car ( _id INTEGER PRIMARY KEY AUTOINCREMENT, car__name TEXT NOT NULL, color INTEGER NOT NULL, initial_mileage INTEGER NOT NULL DEFAULT 0, suspended_since INTEGER  );";
    public static final String SQL_CREATE_TABLE_FUEL_TYPE = "CREATE TABLE IF NOT EXISTS fuel_type ( _id INTEGER PRIMARY KEY AUTOINCREMENT, fuel_type__name TEXT NOT NULL, category TEXT , CONSTRAINT unique_name UNIQUE (fuel_type__name) ON CONFLICT REPLACE );";
    public static final String SQL_CREATE_TABLE_OTHER_COST = "CREATE TABLE IF NOT EXISTS other_cost ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, date INTEGER NOT NULL, mileage INTEGER, price REAL NOT NULL, recurrence_interval INTEGER NOT NULL, recurrence_multiplier INTEGER NOT NULL, end_date INTEGER, note TEXT NOT NULL, car_id INTEGER NOT NULL , CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    public static final String SQL_CREATE_TABLE_REFUELING = "CREATE TABLE IF NOT EXISTS refueling ( _id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER NOT NULL, mileage INTEGER NOT NULL, volume REAL NOT NULL, price REAL NOT NULL, partial INTEGER NOT NULL, note TEXT NOT NULL, fuel_type_id INTEGER NOT NULL, car_id INTEGER NOT NULL , CONSTRAINT fk_fuel_type_id FOREIGN KEY (fuel_type_id) REFERENCES fuel_type (_id) ON DELETE CASCADE, CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    public static final String SQL_CREATE_TABLE_REMINDER = "CREATE TABLE IF NOT EXISTS reminder ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, after_time_span_unit INTEGER, after_time_span_count INTEGER, after_distance INTEGER, start_date INTEGER NOT NULL, start_mileage INTEGER NOT NULL, notification_dismissed INTEGER NOT NULL, snoozed_until INTEGER, car_id INTEGER NOT NULL , CONSTRAINT fk_car_id FOREIGN KEY (car_id) REFERENCES car (_id) ON DELETE CASCADE );";
    private static final String TAG;
    private static DataSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final DataSQLiteOpenHelperCallbacks mOpenHelperCallbacks;
    
    static {
        TAG = DataSQLiteOpenHelper.class.getSimpleName();
    }
    
    private DataSQLiteOpenHelper(final Context mContext, final String s, final DatabaseErrorHandler databaseErrorHandler) {
        super(mContext, s, (SQLiteDatabase$CursorFactory)null, 11, databaseErrorHandler);
        this.mContext = mContext;
        this.mOpenHelperCallbacks = new DataSQLiteOpenHelperCallbacks();
    }
    
    public static DataSQLiteOpenHelper getInstance(final Context context) {
        if (DataSQLiteOpenHelper.sInstance == null) {
            DataSQLiteOpenHelper.sInstance = newInstance(context.getApplicationContext());
        }
        return DataSQLiteOpenHelper.sInstance;
    }
    
    private static DataSQLiteOpenHelper newInstance(final Context context) {
        final CarReportDatabase instance = CarReportDatabase.getInstance(context);
        instance.getCarDao().getAll();
        instance.getFuelTypeDao().getAll();
        instance.getOtherCostDao().getAll();
        instance.getRefuelingDao().getAll();
        instance.getReminderDao().getAll();
        final String databaseName = instance.getOpenHelper().getDatabaseName();
        Log.d(DataSQLiteOpenHelper.TAG, String.format("Using database at %s via classic content provider.", context.getDatabasePath(databaseName).getAbsolutePath()));
        return newInstancePostHoneycomb(context, databaseName);
    }
    
    private static DataSQLiteOpenHelper newInstancePostHoneycomb(final Context context, final String s) {
        return new DataSQLiteOpenHelper(context, s, (DatabaseErrorHandler)new DefaultDatabaseErrorHandler());
    }
    
    public static void resetInstance() {
        if (DataSQLiteOpenHelper.sInstance != null) {
            Log.d(DataSQLiteOpenHelper.TAG, "resetting instance");
            DataSQLiteOpenHelper.sInstance.close();
            DataSQLiteOpenHelper.sInstance = null;
        }
    }
    
    private void setForeignKeyConstraintsEnabled(final SQLiteDatabase sqLiteDatabase) {
        if (Build$VERSION.SDK_INT < 16) {
            this.setForeignKeyConstraintsEnabledPreJellyBean(sqLiteDatabase);
            return;
        }
        this.setForeignKeyConstraintsEnabledPostJellyBean(sqLiteDatabase);
    }
    
    @TargetApi(16)
    private void setForeignKeyConstraintsEnabledPostJellyBean(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }
    
    private void setForeignKeyConstraintsEnabledPreJellyBean(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }
    
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        if (BuildConfig.DEBUG) {
            Log.d(DataSQLiteOpenHelper.TAG, "onCreate");
        }
        this.mOpenHelperCallbacks.onPreCreate(this.mContext, sqLiteDatabase);
        this.mOpenHelperCallbacks.onPostCreate(this.mContext, sqLiteDatabase);
    }
    
    public void onOpen(final SQLiteDatabase foreignKeyConstraintsEnabled) {
        super.onOpen(foreignKeyConstraintsEnabled);
        if (!foreignKeyConstraintsEnabled.isReadOnly()) {
            this.setForeignKeyConstraintsEnabled(foreignKeyConstraintsEnabled);
        }
        this.mOpenHelperCallbacks.onOpen(this.mContext, foreignKeyConstraintsEnabled);
    }
    
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
    }
}
