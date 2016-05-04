package com.srinivas.ele.pojo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.srinivas.ele.utils.Convert;
import com.srinivas.ele.utils.MyApplication;


public class Call {

    private long date;
    private long duration;
    private long id;
    private NumberC number;
    private int type;


    public Call(long date, long duration, long id, NumberC number, int type) {
        this.date = date;
        this.duration = duration;
        this.id = id;
        this.number = number;
        this.type = type;
    }

    public Call(long date, long duration, NumberC number, int type) {
        this.date = date;
        this.duration = duration;
        this.number = number;
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NumberC getNumber() {
        return number;
    }

    public void setNumber(NumberC number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public static Call readProviderCursor(Context context, Cursor cursor)
    {
        String s = cursor.getString(cursor.getColumnIndex("number"));
        int i = cursor.getInt(cursor.getColumnIndex("type"));
        return new Call(cursor.getLong(cursor.getColumnIndex("duration")), cursor.getLong(cursor.getColumnIndex("date")), new NumberC(s),i);
    }

}
