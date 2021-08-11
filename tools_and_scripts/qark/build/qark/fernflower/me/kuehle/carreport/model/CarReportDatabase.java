package me.kuehle.carreport.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import me.kuehle.carreport.model.CarReportDatabase.1;
import me.kuehle.carreport.model.CarReportDatabase.AssetFileBasedMigration;
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

@Database(
   entities = {Car.class, FuelType.class, Reminder.class, Refueling.class, OtherCost.class},
   version = 11
)
@TypeConverters({SQLTypeConverters.class})
public abstract class CarReportDatabase extends RoomDatabase {
   private static final String DB_NAME = "data.db";
   private static final String LOG_TAG = "CarReportDatabase";
   private static CarReportDatabase sInstance;

   public static CarReportDatabase getInstance(Context var0) {
      synchronized(CarReportDatabase.class){}

      CarReportDatabase var8;
      label90: {
         Throwable var10000;
         label94: {
            boolean var10001;
            try {
               if (sInstance == null) {
                  sInstance = (CarReportDatabase)Room.databaseBuilder(var0, CarReportDatabase.class, "data.db").allowMainThreadQueries().addMigrations(new Migration[]{new AssetFileBasedMigration(var0, 2), new AssetFileBasedMigration(var0, 3), new AssetFileBasedMigration(var0, 4), new AssetFileBasedMigration(var0, 5), new AssetFileBasedMigration(var0, 6), new AssetFileBasedMigration(var0, 7), new AssetFileBasedMigration(var0, 8), new AssetFileBasedMigration(var0, 9), new AssetFileBasedMigration(var0, 10), new 1(10, 11)}).build();
               }
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break label94;
            }

            label79:
            try {
               var8 = sInstance;
               break label90;
            } catch (Throwable var5) {
               var10000 = var5;
               var10001 = false;
               break label79;
            }
         }

         Throwable var7 = var10000;
         throw var7;
      }

      return var8;
   }

   public static void resetInstance() {
      DataSQLiteOpenHelper.resetInstance();
      CarReportDatabase var0 = sInstance;
      if (var0 != null) {
         var0.close();
         sInstance = null;
      }
   }

   public abstract CarDAO getCarDao();

   public abstract FuelTypeDAO getFuelTypeDao();

   public abstract OtherCostDAO getOtherCostDao();

   public abstract RefuelingDAO getRefuelingDao();

   public abstract ReminderDAO getReminderDao();
}
