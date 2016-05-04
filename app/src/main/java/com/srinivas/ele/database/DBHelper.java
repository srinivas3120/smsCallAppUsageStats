package com.srinivas.ele.database;

import android.database.sqlite.SQLiteDatabase;


public interface DBHelper {
    public int getDBVersion();
    public String getDBName();
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
