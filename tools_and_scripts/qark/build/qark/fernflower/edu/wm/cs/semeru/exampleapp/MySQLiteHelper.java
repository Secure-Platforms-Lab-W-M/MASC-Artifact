package edu.wm.cs.semeru.exampleapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import java.util.Random;

public class MySQLiteHelper extends SQLiteOpenHelper {
   public MySQLiteHelper(Context var1) {
      super(var1, "example", (CursorFactory)null, 1);
   }

   public void onCreate(SQLiteDatabase var1) {
      (new Random()).nextInt();
   }

   public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
   }
}
