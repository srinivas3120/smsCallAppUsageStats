package com.srinivas.ele.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.srinivas.ele.database.NotificationTable;
import com.srinivas.ele.utils.Common;

/**
 * Created by Mudavath Srinivas on 29-04-2016.
 */
public class NotificationService extends NotificationListenerService {

    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {

        String pack = sbn.getPackageName();
        String appName = Common.getAppName(sbn.getPackageName());
        String ticker = sbn.getNotification().tickerText!=null?sbn.getNotification().tickerText.toString():null;
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text")!=null?extras.getCharSequence("android.text").toString():null;


        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra(NotificationTable.PACKAGE, pack);
        msgrcv.putExtra(NotificationTable.APP_NAME, appName);
        msgrcv.putExtra(NotificationTable.TICKER, ticker);
        msgrcv.putExtra(NotificationTable.TITLE, title);
        msgrcv.putExtra(NotificationTable.BODY, text);
        msgrcv.putExtra(NotificationTable.DATE, System.currentTimeMillis());

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
