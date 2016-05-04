package com.srinivas.ele.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.srinivas.ele.R;
import com.srinivas.ele.database.CallTable;
import com.srinivas.ele.database.DatabaseMgr;
import com.srinivas.ele.database.NumbersTable;
import com.srinivas.ele.database.SmsTable;
import com.srinivas.ele.pojo.Call;
import com.srinivas.ele.pojo.CallLogItem;
import com.srinivas.ele.pojo.CallStatBasic;
import com.srinivas.ele.pojo.SMS;
import com.srinivas.ele.pojo.SMSLogItem;
import com.srinivas.ele.pojo.SmsStatBasic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mudavath Srinivas on 26-04-2016.
 */
public class Common {


    private static ProgressDialog progressDialog;
    private static Toast toast;
    private static Snackbar snackbar;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isConnected();
    }

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            progressDialog = null;
        }
    }

    public static void showAlert(Context context, String title, String msg, boolean cancelable) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(cancelable)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    public static void showCenteredToast(Context ctx, String msg) {
        if(toast==null){
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        }else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void showBottomToast(Context ctx, String msg) {
        if(toast==null){
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }


    public static void showSnackBarToast(@Nullable View view,String message){
        if(snackbar==null){
            snackbar = Snackbar
                    .make(view, message, Snackbar.LENGTH_LONG);
        }else{
            snackbar.setText(message);
        }
        snackbar.show();
    }

    public static void showSnackBarToast(@Nullable View view,String message, String action, View.OnClickListener clickListener){
        if(snackbar==null){
            snackbar = Snackbar
                    .make(view,message, Snackbar.LENGTH_LONG)
                    .setAction(action, clickListener);
        }else{
            snackbar.setText(message);
        }
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity activity, EditText editField) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        editField.requestFocus();
        inputManager.showSoftInput(editField, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            return true;
        }
        // Media storage not present
        return false;
    }

    public static boolean isSimSupport(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }

    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year","month","day","hr","min","sec");

    public static String getFormattedTime(long duration) {
        StringBuffer res = new StringBuffer();
        for(int i=0;i< times.size(); i++) {
            Long current = times.get(i);
            long temp = duration/current;
            if(temp>0) {
                res.append(temp).append(" ").append( timesString.get(i) ).append(temp > 1 ? "s" : "");
                break;
            }
        }
        if("".equals(res.toString()))
            return "Just now";
        else
            return res.toString();
    }

    public static String getAppName(String packageName){
        PackageManager packageManager= MyApplication.getAppContext().getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    public static Drawable getAppIcon(String packageName){
        try {
           return MyApplication.getAppContext().getPackageManager()
                    .getApplicationIcon(packageName);

        } catch (PackageManager.NameNotFoundException e) {
            return MyApplication.getAppContext()
                    .getDrawable(R.mipmap.ic_launcher);
        }
    }

    public static String formatSecToDuration(String durationInSecs){
        if(TextUtils.isEmpty(durationInSecs)){
            return "";
        }
        int totalSecs;
        try{
            totalSecs=Integer.valueOf(durationInSecs);
        }catch (NullPointerException e){
            return "";
        }

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Cursor readCalls(long from,long to)
    {
        return DatabaseMgr.selectRowsRawQuery("SELECT a._id,a.number,MAX( b.date ) AS date,SUM( CASE WHEN(b.TYPE=1) " +
                "THEN duration END ) AS i_d,SUM( CASE WHEN(b.TYPE=2) THEN duration END ) AS o_d," +
                "COUNT( CASE WHEN(b.TYPE=1) THEN 1 END ) AS i_o,COUNT( CASE WHEN(b.TYPE=2) THEN 1 END ) AS o_o," +
                "SUM( CASE WHEN(b.TYPE=3) THEN 1 END ) AS m_o,SUM( CASE WHEN(b.TYPE=6) THEN 1 END ) AS b_o " +
                "FROM  numbers AS a  INNER JOIN  calls AS b  ON a._id = b.number_id WHERE b.date " +
                "BETWEEN " + from + " and " + to + " GROUP BY a._id ORDER BY date DESC ");
    }

    public static Cursor readBasicCallsStats(long from,long to)
    {
        return DatabaseMgr.selectRowsRawQuery("SELECT  SUM( CASE WHEN(TYPE=1) THEN duration END ) AS i_d ," +
                " SUM( CASE WHEN(TYPE=2) THEN duration END ) AS o_d, " +
                "COUNT( CASE WHEN(TYPE=1) THEN 1 END ) AS i_o,COUNT( CASE WHEN(TYPE=2) THEN 1 END ) AS o_o " +
                "FROM  calls WHERE date  BETWEEN " + from + " and " + to);
    }


    public static ArrayList<CallLogItem> readCallsQuery(Cursor cursor) {
        ArrayList<CallLogItem> callLogItemArrayList=new ArrayList<>();
        try {
            while (cursor != null && cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex("number"));
                Long date = cursor.getLong(cursor.getColumnIndex("date"));
                String inCallDuration= cursor.getString(cursor.getColumnIndex("i_d"));
                String outGoingDuration= cursor.getString(cursor.getColumnIndex("o_d"));
                String noOfInCalls= cursor.getString(cursor.getColumnIndex("i_o"));
                String noOfOutGoingCalls= cursor.getString(cursor.getColumnIndex("o_o"));
                String noOfMissCalls= cursor.getString(cursor.getColumnIndex("m_o"));
                String noOfBlockedCalls= cursor.getString(cursor.getColumnIndex("b_o"));

                callLogItemArrayList.add(new CallLogItem(number,date,inCallDuration,outGoingDuration,noOfInCalls,
                        noOfOutGoingCalls,noOfMissCalls,noOfBlockedCalls));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return callLogItemArrayList;
        }
    }


    public static CallStatBasic readBasicCallsStatsQuery(Cursor cursor) {
        CallStatBasic callStatBasic=null;
        try {
            while (cursor != null && cursor.moveToNext()) {
                String inCallDuration= cursor.getString(cursor.getColumnIndex("i_d"));
                String outGoingDuration= cursor.getString(cursor.getColumnIndex("o_d"));
                String noOfInCalls= cursor.getString(cursor.getColumnIndex("i_o"));
                String noOfOutGoingCalls= cursor.getString(cursor.getColumnIndex("o_o"));

                callStatBasic= new CallStatBasic(inCallDuration,outGoingDuration,noOfInCalls,
                        noOfOutGoingCalls);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return callStatBasic;
    }

    public static void insertIntoCallTable(Call call) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(CallTable.NUMBER_ID, call.getNumber().getNumberId());
        contentvalues.put(CallTable.TYPE, call.getType());
        contentvalues.put(CallTable.DATE, call.getDate());
        contentvalues.put(CallTable.DURATION, call.getDuration());
        DatabaseMgr.insertRow(CallTable.TABLE_NAME, contentvalues);
    }

    public static Long insertIntoNumbersTable(Call call) {
        Cursor numberCursor;
        numberCursor=DatabaseMgr.selectRowsRawQuery("select _id from " + NumbersTable.TABLE_NAME + " where number='" + call.getNumber().getNumber()+"'");
        if(numberCursor!=null && numberCursor.getCount()>0){
            numberCursor.moveToFirst();
            return numberCursor.getLong(numberCursor.getColumnIndex(NumbersTable._ID));
        }else {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(NumbersTable.NUMBER, call.getNumber().getNumber());

            return Long.valueOf(DatabaseMgr.insertRow(NumbersTable.TABLE_NAME, contentvalues));
        }
    }

    public static Long[] getFromToDate(String frequency) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        Date fromYear;

        switch (frequency){

            case "Weekly":
                cal.add(Calendar.DAY_OF_MONTH, -7);
                fromYear = cal.getTime();
                break;
            case "Monthly":
                cal.add(Calendar.MONTH, -1);
                fromYear = cal.getTime();
                break;
            case "Yearly":
                cal.add(Calendar.YEAR, -1);
                fromYear = cal.getTime();
                break;
            case "Daily":
            default:
                cal.add(Calendar.DAY_OF_MONTH, -1);
                fromYear = cal.getTime();
                break;
        }
        return new Long[]{fromYear.getTime(),System.currentTimeMillis()};
    }

    public static Cursor readSMSs(long from,long to)
    {
        return DatabaseMgr.selectRowsRawQuery("SELECT a._id,a.number,MAX( b.date ) AS date," +
                "COUNT( CASE WHEN(b.TYPE=1) THEN 1 END ) AS i_o," +
                "COUNT( CASE WHEN(b.TYPE=2) THEN 1 END ) AS o_o" +
                "  FROM  numbers AS a  INNER JOIN  sms AS b  ON a._id = b.number_id " +
                "WHERE b.date BETWEEN "+from+" and "+to+" GROUP BY a._id ORDER BY date DESC ");
    }

    public static Cursor readBasicSMSsStats(long from,long to)
    {
        return DatabaseMgr.selectRowsRawQuery("SELECT  SUM( CASE WHEN(type=1) THEN 1 END ) AS i_o ," +
                " SUM( CASE WHEN(type=2) THEN 1 END ) AS o_o "+
                "FROM  sms WHERE date  BETWEEN "+from+" and "+to);
    }


    public static ArrayList<SMSLogItem> readSMSsQuery(Cursor cursor) {
        ArrayList<SMSLogItem> smsLogItemArrayList=new ArrayList<>();
        try {
            while (cursor != null && cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex("number"));
                Long date = cursor.getLong(cursor.getColumnIndex("date"));
                String noOfInSMSs= cursor.getString(cursor.getColumnIndex("i_o"));
                String noOfOutSMSs= cursor.getString(cursor.getColumnIndex("o_o"));

                smsLogItemArrayList.add(new SMSLogItem(number,date,noOfInSMSs, noOfOutSMSs));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return smsLogItemArrayList;
        }
    }


    public static SmsStatBasic readBasicSMSStatsQuery(Cursor cursor) {
        SmsStatBasic smsStatBasic=null;
        try {
            while (cursor != null && cursor.moveToNext()) {
                String noOfInSMSs= cursor.getString(cursor.getColumnIndex("i_o"));
                String noOfOutSMSs= cursor.getString(cursor.getColumnIndex("o_o"));

                smsStatBasic= new SmsStatBasic(noOfInSMSs,noOfOutSMSs);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return smsStatBasic;
    }

    public static void insertIntoCallTable(SMS sms) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(SmsTable.NUMBER_ID, sms.getNumber().getNumberId());
        contentvalues.put(SmsTable.TYPE, sms.getType());
        contentvalues.put(SmsTable.DATE, sms.getDate());
        DatabaseMgr.insertRow(SmsTable.TABLE_NAME, contentvalues);
    }

    public static Long insertIntoNumbersTable(SMS sms) {
        Cursor numberCursor;
        numberCursor=DatabaseMgr.selectRowsRawQuery("select _id from " + NumbersTable.TABLE_NAME + " where number='" + sms.getNumber().getNumber()+"'");
        if(numberCursor!=null && numberCursor.getCount()>0){
            numberCursor.moveToFirst();
            return numberCursor.getLong(numberCursor.getColumnIndex(NumbersTable._ID));
        }else {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(NumbersTable.NUMBER, sms.getNumber().getNumber());

            return Long.valueOf(DatabaseMgr.insertRow(NumbersTable.TABLE_NAME, contentvalues));
        }
    }

}
