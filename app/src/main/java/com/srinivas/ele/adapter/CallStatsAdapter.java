package com.srinivas.ele.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinivas.ele.R;
import com.srinivas.ele.pojo.CallLogItem;
import com.srinivas.ele.pojo.NotificationItem;
import com.srinivas.ele.utils.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class CallStatsAdapter extends RecyclerView.Adapter<CallStatsAdapter.ViewHolder> {

    private ArrayList<CallLogItem> callLogItems = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();

    public CallStatsAdapter(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView number;
        private TextView outgoingDuration;
        private TextView incomingDuration;
        private TextView missed;
        private TextView blocked;

        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            number = (TextView) v.findViewById(R.id.number);
            outgoingDuration = (TextView) v.findViewById(R.id.outgoingDuration);
            incomingDuration = (TextView) v.findViewById(R.id.incomingDuration);
            missed = (TextView) v.findViewById(R.id.missed);
            blocked = (TextView) v.findViewById(R.id.blocked);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_log_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CallLogItem callLogItem=callLogItems.get(position);
        viewHolder.date.setText(mDateFormat.format(new Date(callLogItem.getDate())));
        viewHolder.number.setText(callLogItem.getNumber());
        viewHolder.incomingDuration.setText(Common.formatSecToDuration(callLogItem.getInCallDuration()) + "(" + callLogItem.getNoOfInCalls() + ")");
        viewHolder.outgoingDuration.setText(Common.formatSecToDuration(callLogItem.getOutGoingDuration())+"("+callLogItem.getNoOfOutGoingCalls()+")");
        viewHolder.missed.setText(callLogItem.getNoOfMissCalls());
        viewHolder.blocked.setText(callLogItem.getNoOfBlockedCalls());
    }

    @Override
    public int getItemCount() {
        return callLogItems.size();
    }

    public void setCustomCallLogItems(ArrayList<CallLogItem> callLogItems) {
        this.callLogItems = callLogItems;
    }

}
