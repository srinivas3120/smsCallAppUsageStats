// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.srinivas.ele.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Convert
{

    public static final long DAY = 0x15180L;
    public static final long HOUR = 3600L;
    public static final long MINUTE = 60L;

    public Convert()
    {
    }

    public static String dateDMYToString(long l)
    {
        Date date = new Date(l);
        return (new SimpleDateFormat("dd MMMMMMMMMMMM yyyy", Locale.getDefault())).format(date);
    }

    public static String dateToString(long l)
    {
        Date date = new Date(l);
        return (new SimpleDateFormat("dd MMMMMMMMMMMM yy, HH:mm", Locale.getDefault())).format(date);
    }

    public static String dayToString(double d)
    {
        Date date = new Date((long)d);
        return (new SimpleDateFormat("dd MMMMMMMMMMMM yyyy", Locale.getDefault())).format(date);
    }

    public static String durationToString(long l)
    {
        NumberFormat numberformat = NumberFormat.getInstance(Locale.getDefault());
        long l1 = l / 3600L;
        long l2 = l - l1 * 3600L;
        l = l2 / 60L;
        l2 -= l * 60L;
        StringBuilder stringbuilder = (new StringBuilder()).append("");
        String s;
        if (l1 > 9L)
        {
            s = numberformat.format(l1);
        } else
        {
            s = (new StringBuilder()).append("0").append(numberformat.format(l1)).toString();
        }
        s = stringbuilder.append(s).toString();
        stringbuilder = (new StringBuilder()).append(s);
        if (l > 9L)
        {
            s = (new StringBuilder()).append(":").append(numberformat.format(l)).toString();
        } else
        {
            s = (new StringBuilder()).append(":0").append(numberformat.format(l)).toString();
        }
        s = stringbuilder.append(s).toString();
        stringbuilder = (new StringBuilder()).append(s);
        if (l2 > 9L)
        {
            s = (new StringBuilder()).append(":").append(numberformat.format(l2)).toString();
        } else
        {
            s = (new StringBuilder()).append(":0").append(numberformat.format(l2)).toString();
        }
        return stringbuilder.append(s).toString();
    }

    public static String durationToStringHMS(double d, Context context)
    {
        return durationToStringHMS((long)d, context);
    }


    public static String getCSVDateString(long l)
    {
        Date date = new Date(l);
        return (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(date);
    }

    public static String getCSVHourString(long l)
    {
        Date date = new Date(l);
        return (new SimpleDateFormat("HH:mm:ss", Locale.getDefault())).format(date);
    }
}
