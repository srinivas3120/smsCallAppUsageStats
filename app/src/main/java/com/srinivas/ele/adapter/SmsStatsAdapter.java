package com.srinivas.ele.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srinivas.ele.R;
import com.srinivas.ele.pojo.CallLogItem;
import com.srinivas.ele.pojo.SMSLogItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class SmsStatsAdapter extends RecyclerView.Adapter<SmsStatsAdapter.ViewHolder> {

    private ArrayList<SMSLogItem> smsLogItems = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();

    public SmsStatsAdapter(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView number;
        private TextView sms_stats_r;
        private TextView sms_stats_s;

        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            number = (TextView) v.findViewById(R.id.number);
            sms_stats_r = (TextView) v.findViewById(R.id.sms_stats_r);
            sms_stats_s = (TextView) v.findViewById(R.id.sms_stats_s);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_log_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SMSLogItem smsLogItem= smsLogItems.get(position);
        viewHolder.date.setText(mDateFormat.format(new Date(smsLogItem.getDate())));
        viewHolder.number.setText(smsLogItem.getNumber());
        viewHolder.sms_stats_s.setText(smsLogItem.getNoOfOutSMSs());
        viewHolder.sms_stats_r.setText(smsLogItem.getNoOfInSMSs());
    }

    @Override
    public int getItemCount() {
        return smsLogItems.size();
    }

    public void setCustomSMSLogItems(ArrayList<SMSLogItem> smsLogItems) {
        this.smsLogItems = smsLogItems;
    }

}
