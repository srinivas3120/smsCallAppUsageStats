package com.srinivas.ele.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
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
import com.srinivas.ele.adapter.CallStatsAdapter;
import com.srinivas.ele.adapter.UsageListAdapter;
import com.srinivas.ele.database.CallTable;
import com.srinivas.ele.database.DatabaseMgr;
import com.srinivas.ele.database.NumbersTable;
import com.srinivas.ele.pojo.Born;
import com.srinivas.ele.pojo.Call;
import com.srinivas.ele.pojo.CallLogItem;
import com.srinivas.ele.pojo.CallStatBasic;
import com.srinivas.ele.pojo.NumberC;
import com.srinivas.ele.utils.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 01-05-2016.
 */
public class CallStatisticsFragment extends Fragment {
    private Spinner mSpinner;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private CallStatsAdapter callStatsAdapter;

    TextView incomingDuration;
    TextView outgoingDuration;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call_log_statistics, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        callStatsAdapter = new CallStatsAdapter();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_call_log);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(callStatsAdapter);

        incomingDuration= (TextView) rootView.findViewById(R.id.incomingDuration);
        outgoingDuration= (TextView) rootView.findViewById(R.id.outgoingDuration);

        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_time_span);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Common.showProgressDialog(getContext(),getString(R.string.string_loading));
                new CallLogTask(getContext(),strings[position]).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class CallLogTask extends AsyncTask<String, String, String> {

        private Context context;
        long to;
        long from;
        public CallLogTask(Context context){
            this.context=context;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date fromYear = cal.getTime();
            this.to=System.currentTimeMillis();
            this.from=fromYear.getTime();
        }
        public CallLogTask(Context context,String frequency){
            this.context=context;
            Long[] frequencyDate=Common.getFromToDate(frequency);

            this.from=frequencyDate[0];
            this.to=frequencyDate[1];
        }

        @Override
        protected String doInBackground(String... params) {

            Born born=getMaxDateInserted();
            if(born==null){
                getCallLogAndInsert(context,from,to);
            }else {
                if(from<born.min){
                    getCallLogAndInsert(context,from,born.min);
                    // insert from to born.min
                }
                if(to > born.max) {
                    getCallLogAndInsert(context,born.max,to);
                    //insert born.max to to
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new ReadCalls(from,to).execute();
            super.onPostExecute(s);

        }

        private void getCallLogAndInsert(Context context, long from, long to) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            Cursor cursor=context.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{
                    "number", "duration", "type", "date"
            }, "date BETWEEN  ? AND ? ", new String[]{
                    String.valueOf(from), String.valueOf(to)
            }, "number");

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Call call=readProviderCursor(cursor);
                        long numberId=Common.insertIntoNumbersTable(call);
                        call.getNumber().setNumberId(numberId);
                        Common.insertIntoCallTable(call);
                    } while (cursor.moveToNext());
                }
            }
        }

    }

    private class ReadCalls extends AsyncTask<String, String, ArrayList<CallLogItem>>{
        long from; long to;

        public ReadCalls(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected ArrayList<CallLogItem> doInBackground(String... params) {
            Cursor cursor = Common.readCalls(from, to);
            return Common.readCallsQuery(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<CallLogItem> callLogItems) {
            // update recyclerview and adapter
            callStatsAdapter.setCustomCallLogItems(callLogItems);
            callStatsAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
            new ReadBasicCallsStats(from,to).execute();
            super.onPostExecute(callLogItems);
        }
    }

    private class ReadBasicCallsStats extends AsyncTask<String, String, CallStatBasic>{
        long from; long to;

        public ReadBasicCallsStats(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected CallStatBasic doInBackground(String... params) {
            Cursor cursor = Common.readBasicCallsStats(from, to);
            return Common.readBasicCallsStatsQuery(cursor);
        }

        @Override
        protected void onPostExecute(CallStatBasic callStatBasic) {
            incomingDuration.setText(Common.formatSecToDuration(callStatBasic.getInCallDuration())+"("+callStatBasic.getNoOfInCalls()+")");
            outgoingDuration.setText(Common.formatSecToDuration(callStatBasic.getOutGoingDuration())+"("+callStatBasic.getNoOfOutGoingCalls()+")");
            Common.dismissProgressDialog();
            super.onPostExecute(callStatBasic);
        }
    }


    private static Born getMaxDateInserted() {
        Cursor cursor = DatabaseMgr.selectRowsRawQuery("SELECT MAX( date ) AS MAX,MIN( date ) AS MIN FROM  " + CallTable.TABLE_NAME);
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


    public static Call readProviderCursor(Cursor cursor)
    {
        String number = cursor.getString(cursor.getColumnIndex("number"));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        return new Call( cursor.getLong(cursor.getColumnIndex("date")),cursor.getLong(cursor.getColumnIndex("duration")), new NumberC(number, -1l),type);
    }



}
