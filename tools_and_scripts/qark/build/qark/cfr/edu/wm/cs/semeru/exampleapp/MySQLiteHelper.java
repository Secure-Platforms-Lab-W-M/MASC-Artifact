/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 */
package edu.wm.cs.semeru.exampleapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Random;

public class MySQLiteHelper
extends SQLiteOpenHelper {
    public MySQLiteHelper(Context context) {
        super(context, "example", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        new Random().nextInt();
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
    }
}

