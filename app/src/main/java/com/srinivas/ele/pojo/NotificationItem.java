package com.srinivas.ele.pojo;

import android.graphics.drawable.Drawable;

/**
 * Created by Mudavath Srinivas on 01-05-2016.
 */
public class NotificationItem {

    private String appName;
    private String packageName;
    private String title;
    private String body;
    private long date;
    private Drawable appIcon;

    public NotificationItem(long date, String appName, String packageName, String title, String body,Drawable appIcon) {
        this.date = date;
        this.appName = appName;
        this.packageName=packageName;
        this.title = title;
        this.body = body;
        this.appIcon=appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
