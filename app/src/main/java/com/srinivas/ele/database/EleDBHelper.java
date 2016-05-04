package com.srinivas.ele.database;

import android.database.sqlite.SQLiteDatabase;


public class EleDBHelper implements DBHelper {
    @Override
    public int getDBVersion() {
        return 1;
    }

    @Override
    public String getDBName() {
        return "ele";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+NumbersTable.TABLE_NAME+" ("+
                NumbersTable._ID+" INTEGER PRIMARY KEY, "+
                NumbersTable.NUMBER+" TEXT);");

        db.execSQL("CREATE TABLE "+NotificationTable.TABLE_NAME+" ("+
                NotificationTable._ID+" INTEGER PRIMARY KEY, "+
                NotificationTable.APP_NAME+" TEXT, "+
                NotificationTable.PACKAGE+" TEXT, "+
                NotificationTable.TITLE+" TEXT, "+
                NotificationTable.BODY+" TEXT, "+
                NotificationTable.TICKER+" TEXT, "+
                NotificationTable.DATE+" INTEGER);");

        db.execSQL("CREATE TABLE "+CallTable.TABLE_NAME+" ("+
                CallTable._ID+" INTEGER PRIMARY KEY, "+
                CallTable.TYPE+" TEXT, "+
                CallTable.DATE+" INTEGER, "+
                CallTable.DURATION+" INTEGER, "+
                CallTable.NUMBER_ID+" INTEGER,FOREIGN KEY( "+CallTable.NUMBER_ID+") REFERENCES "+NumbersTable.TABLE_NAME+"("+NumbersTable._ID+") );");

        db.execSQL("CREATE TABLE "+SmsTable.TABLE_NAME+" ("+
                SmsTable._ID+" INTEGER PRIMARY KEY,"+
                SmsTable.DATE+" INTEGER,"+
                SmsTable.TYPE+" INTEGER,"+
                SmsTable.NUMBER_ID+" INTEGER,FOREIGN KEY( "+SmsTable.NUMBER_ID+") REFERENCES "+NumbersTable.TABLE_NAME+"("+NumbersTable._ID+") );");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
