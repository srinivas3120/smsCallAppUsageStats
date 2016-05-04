package com.srinivas.ele.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.srinivas.ele.R;
import com.srinivas.ele.adapter.SmsStatsAdapter;
import com.srinivas.ele.database.DatabaseMgr;
import com.srinivas.ele.database.NumbersTable;
import com.srinivas.ele.database.SmsTable;
import com.srinivas.ele.pojo.Born;
import com.srinivas.ele.pojo.NumberC;
import com.srinivas.ele.pojo.SMS;
import com.srinivas.ele.pojo.SMSLogItem;
import com.srinivas.ele.pojo.SmsStatBasic;
import com.srinivas.ele.utils.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class SMSStatisticsFragment  extends Fragment {

    private Spinner mSpinner;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private SmsStatsAdapter smsStatsAdapter;

    TextView incomingSMSs;
    TextView outgoingSMSs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sms_log_statistics, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        smsStatsAdapter = new SmsStatsAdapter();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_sms_log);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(smsStatsAdapter);

        incomingSMSs = (TextView) rootView.findViewById(R.id.incomingSMS);
        outgoingSMSs = (TextView) rootView.findViewById(R.id.outgoingSMS);

        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_time_span);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Common.showProgressDialog(getContext(), getString(R.string.string_loading));
                new SMSLogTask(getContext(),strings[position]).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class SMSLogTask extends AsyncTask<String, String, String> {

        private Context context;
        long to;
        long from;
        public SMSLogTask(Context context){
            this.context=context;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date fromYear = cal.getTime();
            this.to=System.currentTimeMillis();
            this.from=fromYear.getTime();
        }
        public SMSLogTask(Context context,String frequency){
            this.context=context;
            Long[] frequencyDate=getFromToDate(frequency);

            this.from=frequencyDate[0];
            this.to=frequencyDate[1];
        }

        @Override
        protected String doInBackground(String... params) {

            Born born=getMaxDateInserted();
            if(born==null){
                getSmsLogAndInsert(context, from, to);
            }else {
                if(from<born.min){
                    getSmsLogAndInsert(context, from, born.min);
                    // insert from to born.min
                }
                if(to > born.max) {
                    getSmsLogAndInsert(context, born.max, to);
                    //insert born.max to to
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new ReadSMSs(from,to).execute();
            super.onPostExecute(s);
        }

        private void getSmsLogAndInsert(Context context, long from, long to) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            Cursor cursor=context.getContentResolver().query(Telephony.Sms.CONTENT_URI, new String[]{
                    "_id", "address", "date", "type"
            }, "date BETWEEN  ? AND ?  and (type = 1 or type = 2 )", new String[]{
                    String.valueOf(from), String.valueOf(to)
            }, "address");

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        SMS sms=readProviderCursor(cursor);
                        long numberId=insertIntoNumbersTable(sms);
                        sms.getNumber().setNumberId(numberId);
                        insertIntoCallTable(sms);
                    } while (cursor.moveToNext());
                }
            }
        }

    }

    private class ReadSMSs extends AsyncTask<String, String, ArrayList<SMSLogItem>>{
        long from; long to;

        public ReadSMSs(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected ArrayList<SMSLogItem> doInBackground(String... params) {
            Cursor cursor = readSMSs(from, to);
            return readSMSsQuery(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<SMSLogItem> smsLogItems) {
            // update recyclerview and adapter
            smsStatsAdapter.setCustomSMSLogItems(smsLogItems);
            smsStatsAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
            new ReadBasicCallsStats(from,to).execute();
            super.onPostExecute(smsLogItems);
        }
    }

    private class ReadBasicCallsStats extends AsyncTask<String, String, SmsStatBasic>{
        long from; long to;

        public ReadBasicCallsStats(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected SmsStatBasic doInBackground(String... params) {
            Cursor cursor = readBasicSMSsStats(from, to);
            return readBasicSMSStatsQuery(cursor);
        }

        @Override
        protected void onPostExecute(SmsStatBasic smsStatBasic) {
            incomingSMSs.setText(smsStatBasic.getNoOfInSMSs());
            outgoingSMSs.setText(smsStatBasic.getNoOfOutSMSs());
            Common.dismissProgressDialog();
            super.onPostExecute(smsStatBasic);
        }
    }




    private static Long[] getFromToDate(String frequency) {
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

    private static Born getMaxDateInserted() {
        Cursor cursor = DatabaseMgr.selectRowsRawQuery("SELECT MAX( date ) AS MAX,MIN( date ) AS MIN FROM  " + SmsTable.TABLE_NAME);
        Born born=null;
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                born = new Born();
                born.max = cursor.getLong(cursor.getColumnIndex("MAX"));
                born.min = cursor.getLong(cursor.getColumnIndex("MIN"));
            }
        }
        cursor.close();
        return born;
    }

    private static void insertIntoCallTable(SMS sms) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(SmsTable.NUMBER_ID, sms.getNumber().getNumberId());
        contentvalues.put(SmsTable.TYPE, sms.getType());
        contentvalues.put(SmsTable.DATE, sms.getDate());
        DatabaseMgr.insertRow(SmsTable.TABLE_NAME, contentvalues);
    }

    private static Long insertIntoNumbersTable(SMS sms) {
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

    public static SMS readProviderCursor(Cursor cursor)
    {
        String address = cursor.getString(cursor.getColumnIndex("address"));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        return new SMS( cursor.getLong(cursor.getColumnIndex("date")),-1l,
                new NumberC(address, -1l),type);
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


}
