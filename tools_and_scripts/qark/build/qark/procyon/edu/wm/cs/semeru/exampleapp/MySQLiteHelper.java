// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package edu.wm.cs.semeru.exampleapp;

import java.util.Random;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper
{
    public MySQLiteHelper(final Context context) {
        super(context, "example", (SQLiteDatabase$CursorFactory)null, 1);
    }
    
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        new Random().nextInt();
    }
    
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
    }
}
