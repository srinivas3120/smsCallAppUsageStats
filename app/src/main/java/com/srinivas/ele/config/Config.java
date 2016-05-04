package com.srinivas.ele.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Mudavath Srinivas on 26-04-2016.
 */
public class Config {

    private static Context context;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences settings = null;
    private static String CONFIG_FILE = "ele.db";

    public static void init(Context paramContext) {
        settings = paramContext.getSharedPreferences(CONFIG_FILE, 0);
        editor = settings.edit();
        context = paramContext;
    }


}
