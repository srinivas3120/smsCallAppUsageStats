package com.srinivas.ele.fragment;

import android.app.usage.UsageStats;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.srinivas.ele.R;
import com.srinivas.ele.adapter.NotificationAdapter;
import com.srinivas.ele.database.DatabaseMgr;
import com.srinivas.ele.database.NotificationTable;
import com.srinivas.ele.pojo.NotificationItem;
import com.srinivas.ele.utils.Common;
import com.srinivas.ele.utils.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Mudavath Srinivas on 01-05-2016.
 */
public class NotificationStatsFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button mOpenUsageSettingButton;
    Spinner mSpinner;

    NotificationAdapter mNotificationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_stats, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mNotificationAdapter=new NotificationAdapter();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_notification);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mNotificationAdapter);
        mOpenUsageSettingButton = (Button) rootView.findViewById(R.id.button_open_usage_setting);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_time_span);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetNotifications(strings[position]).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(!checkNotificationEnabled()){
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            getString(R.string.explanation_access_to_notification_is_not_enabled),
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            });
        }

        //new GetNotifications().execute();

    }

    public boolean checkNotificationEnabled() {
        try{
            if(Settings.Secure.getString(MyApplication.getAppContext().getContentResolver(),
                    "enabled_notification_listeners").contains(MyApplication.getAppContext().getPackageName()))
            {
                return true;
            } else {
                return false;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class GetNotifications extends AsyncTask<String, String, ArrayList<NotificationItem>> {


        long to;
        long from;
        public GetNotifications(){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date fromYear = cal.getTime();
            this.to=System.currentTimeMillis();
            this.from=fromYear.getTime();
        }
        public GetNotifications(String frequency){
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
            this.from=fromYear.getTime();
            this.to=System.currentTimeMillis();
        }


        @Override
        protected void onPreExecute() {
            Common.showProgressDialog(getContext(),getString(R.string.string_loading));
            super.onPreExecute();
        }

        @Override
        protected ArrayList<NotificationItem> doInBackground(String... params) {
            String blogQuery = "SELECT " + "v." + NotificationTable.APP_NAME
                    + ",v." + NotificationTable.DATE
                    + ",v." + NotificationTable.TITLE
                    + ",v." + NotificationTable.BODY
                    + ",v." + NotificationTable.PACKAGE
                    + " FROM " + NotificationTable.TABLE_NAME + " v  where "
                    +"v."+ NotificationTable.DATE +" between "+from+ " and "+to
                    +" ORDER BY 2 DESC ";

            Cursor cursor = DatabaseMgr.selectRowsRawQuery(blogQuery);
            ArrayList<NotificationItem> notificationItems=new ArrayList<>();

            try {
                while (cursor != null && cursor.moveToNext()) {
                    String packageName = cursor.getString(cursor.getColumnIndex(NotificationTable.PACKAGE));
                    String appName = cursor.getString(cursor.getColumnIndex(NotificationTable.APP_NAME));
                    Long date = cursor.getLong(cursor.getColumnIndex(NotificationTable.DATE));
                    String title = cursor.getString(cursor.getColumnIndex(NotificationTable.TITLE));
                    String body = cursor.getString(cursor.getColumnIndex(NotificationTable.BODY));
                    notificationItems.add(new NotificationItem(date,appName,packageName,title,body, Common.getAppIcon(packageName)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                return notificationItems;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationItem> notificationItems) {
            Common.dismissProgressDialog();
            mNotificationAdapter.setCustomNotificationItems(notificationItems);
            mNotificationAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
            super.onPostExecute(notificationItems);
        }
    }

}
