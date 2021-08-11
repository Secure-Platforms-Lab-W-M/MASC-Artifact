package me.kuehle.carreport.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Build.VERSION;
import android.util.Log;
import me.kuehle.carreport.BuildConfig;
import me.kuehle.carreport.model.CarReportDatabase;

@Deprecated
public class DataSQLiteOpenHelper extends SQLiteOpenHelper {
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

   private DataSQLiteOpenHelper(Context var1, String var2, DatabaseErrorHandler var3) {
      super(var1, var2, (CursorFactory)null, 11, var3);
      this.mContext = var1;
      this.mOpenHelperCallbacks = new DataSQLiteOpenHelperCallbacks();
   }

   public static DataSQLiteOpenHelper getInstance(Context var0) {
      if (sInstance == null) {
         sInstance = newInstance(var0.getApplicationContext());
      }

      return sInstance;
   }

   private static DataSQLiteOpenHelper newInstance(Context var0) {
      CarReportDatabase var1 = CarReportDatabase.getInstance(var0);
      var1.getCarDao().getAll();
      var1.getFuelTypeDao().getAll();
      var1.getOtherCostDao().getAll();
      var1.getRefuelingDao().getAll();
      var1.getReminderDao().getAll();
      String var2 = var1.getOpenHelper().getDatabaseName();
      Log.d(TAG, String.format("Using database at %s via classic content provider.", var0.getDatabasePath(var2).getAbsolutePath()));
      return newInstancePostHoneycomb(var0, var2);
   }

   private static DataSQLiteOpenHelper newInstancePostHoneycomb(Context var0, String var1) {
      return new DataSQLiteOpenHelper(var0, var1, new DefaultDatabaseErrorHandler());
   }

   public static void resetInstance() {
      if (sInstance != null) {
         Log.d(TAG, "resetting instance");
         sInstance.close();
         sInstance = null;
      }
   }

   private void setForeignKeyConstraintsEnabled(SQLiteDatabase var1) {
      if (VERSION.SDK_INT < 16) {
         this.setForeignKeyConstraintsEnabledPreJellyBean(var1);
      } else {
         this.setForeignKeyConstraintsEnabledPostJellyBean(var1);
      }
   }

   @TargetApi(16)
   private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase var1) {
      var1.setForeignKeyConstraintsEnabled(true);
   }

   private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase var1) {
      var1.execSQL("PRAGMA foreign_keys=ON;");
   }

   public void onCreate(SQLiteDatabase var1) {
      if (BuildConfig.DEBUG) {
         Log.d(TAG, "onCreate");
      }

      this.mOpenHelperCallbacks.onPreCreate(this.mContext, var1);
      this.mOpenHelperCallbacks.onPostCreate(this.mContext, var1);
   }

   public void onOpen(SQLiteDatabase var1) {
      super.onOpen(var1);
      if (!var1.isReadOnly()) {
         this.setForeignKeyConstraintsEnabled(var1);
      }

      this.mOpenHelperCallbacks.onOpen(this.mContext, var1);
   }

   public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
   }
}
