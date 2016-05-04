package com.srinivas.ele.database;

import android.content.Context;

public class Env {
    public static Context appContext;
    public static DBHelper dbHelper;
    public static String logFilePath;
    public static boolean isDebugMode;

    public static void init(Context appContext, DBHelper dbHelper,String logFilePath, boolean isDebugMode) {
        Env.appContext = appContext;
        Env.dbHelper = dbHelper;
        Env.logFilePath = logFilePath;
        Env.isDebugMode = isDebugMode;
    }
}
