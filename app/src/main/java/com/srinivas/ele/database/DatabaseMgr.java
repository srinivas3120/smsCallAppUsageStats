package com.srinivas.ele.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseMgr {
    private static String TAG = "DatabaseMgr";

    public static SQLiteDatabase sqLiteDb = null;
    // public SQLiteDatabase sqLiteDbRO = null;
    private static DatabaseMgr instance = null;
    private static SQLiteHelper sqLiteHelper=null;
    public synchronized static DatabaseMgr getInstance() throws InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DatabaseMgr();
            instance.init();
        }
        return instance;
    }

    /**
     * This method is called to initialize database module.
     */
    private synchronized static boolean init() throws IllegalAccessException, InstantiationException {
        sqLiteHelper = new SQLiteHelper();
        sqLiteDb = sqLiteHelper.getWritableDatabase();
        // sqLiteDbRO = dbHelper.getReadableDatabase();
        sqLiteDb.setPageSize(4 * 1024);// default is 1 K
        return true;
    }

    public synchronized static String getDatabasePath(){
        return sqLiteHelper.getWritableDatabase().getPath();
    }

    public synchronized static void deleteAllTables(String[] tables){
        for(String table:tables){
            sqLiteDb.delete(table,null,null);
        }

    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValue
     * @return
     */
    public synchronized static int insertRow(String tableName, ContentValues contentValue) {

        //if (Logger.IS_DEBUG)
            Log.i(TAG, "insertRow(): + tableName [" + tableName + "] values [" + ((contentValue != null) ? contentValue.toString() : "") + "]");
        int retCode = -1;
        try {
            getInstance().sqLiteDb.beginTransaction();

            if (contentValue == null)
                return 0;

            retCode = (int) sqLiteDb.insertWithOnConflict(
                    tableName, null, contentValue,
                    SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            //if (Logger.IS_DEBUG) {
            Log.e(TAG, "insertRow(): Exception [" + e + "] tableName [" + tableName + "] values [" + ((contentValue != null) ? contentValue.toString() : "") + "]");
                e.printStackTrace();
            //}
        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValues
     * @return number of insert value
     */
    public synchronized static int insertRows(String tableName,
                                              ArrayList<ContentValues> contentValues) {
//        if (Logger.IS_DEBUG)
        Log.e(TAG, "insertRows(): tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));

        int numberOfRowInsert = 0;
        try {
            getInstance().sqLiteDb.beginTransaction();
            for (ContentValues contactValue : contentValues) {
                try {
                    if (contactValue == null)
                        return 0;
                    sqLiteDb.insertWithOnConflict(tableName, null,
                            contactValue, SQLiteDatabase.CONFLICT_REPLACE);
                    numberOfRowInsert++;
                } catch (Exception e) {
//                    if (Logger.IS_DEBUG) {
                    Log.e(TAG, "insertRows(): exception ["
                            + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));
                    e.printStackTrace();
//                    }
                }
            }
        } catch (Exception e) {
//            if (Logger.IS_DEBUG) {
            Log.e(TAG, "insertRows(): exception ["
                    + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));
            e.printStackTrace();
//            }
        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return numberOfRowInsert;
    }


    public synchronized  static Cursor selectRows(String tableName,String[] columns, String selection,  String[] selectionArgs){
        Cursor cur = null;
        try {
            getInstance().sqLiteDb.beginTransaction();
            cur=sqLiteDb.query(tableName, columns, selection, selectionArgs, null, null, null);
        }catch (Exception ex){
            Log.e("ELE", "In Exception on DBMgr  : " + ex.toString());
        }finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return cur;
    }


    public synchronized  static Cursor selectRowsRawQuery(String rawQuery){
        Cursor cur = null;
        try {
            getInstance().sqLiteDb.beginTransaction();
            cur=sqLiteDb.rawQuery(rawQuery, null);

        }catch (Exception ex){
            Log.e("ELE", "In Exception on DBMgr  : " + ex.toString());
        }finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return cur;
    }


}
