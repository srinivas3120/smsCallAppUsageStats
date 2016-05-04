package com.srinivas.ele.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;

import com.srinivas.ele.config.Config;
import com.srinivas.ele.database.DatabaseMgr;
import com.srinivas.ele.database.EleDBHelper;
import com.srinivas.ele.database.Env;
import com.srinivas.ele.database.NotificationTable;

/**
 * Created by Mudavath Srinivas on 19-11-2015.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance=null;
    public static final String TAG = MyApplication.class
            .getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        Config.init(getAppContext());
        EleDBHelper dbHelper = new EleDBHelper();
        Env.init(getAppContext(), dbHelper, null, true);

        LocalBroadcastManager.getInstance(MyApplication.getAppContext()).registerReceiver(onNotice, new IntentFilter("Msg"));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    public static MyApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            ContentValues contentValues=new ContentValues();
            contentValues.put(NotificationTable.APP_NAME,intent.getStringExtra(NotificationTable.APP_NAME));
            contentValues.put(NotificationTable.PACKAGE,intent.getStringExtra(NotificationTable.PACKAGE));
            contentValues.put(NotificationTable.TITLE,intent.getStringExtra(NotificationTable.TITLE));
            contentValues.put(NotificationTable.BODY,intent.getStringExtra(NotificationTable.BODY));
            contentValues.put(NotificationTable.DATE,intent.getLongExtra(NotificationTable.DATE, System.currentTimeMillis()));
            contentValues.put(NotificationTable.TICKER,intent.getStringExtra(NotificationTable.TICKER));
            // store in database...
            DatabaseMgr.insertRow(NotificationTable.TABLE_NAME, contentValues);
        }
    };
}
