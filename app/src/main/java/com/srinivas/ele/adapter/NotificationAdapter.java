package com.srinivas.ele.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinivas.ele.R;
import com.srinivas.ele.pojo.CustomUsageStats;
import com.srinivas.ele.pojo.NotificationItem;
import com.srinivas.ele.utils.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mudavath Srinivas on 01-05-2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {

    private ArrayList<NotificationItem> notificationItems = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();

    public NotificationAdapter(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mBody;
        private TextView mDate;
        private TextView mTitle;
        private TextView mAppName;
        private ImageView mAppIcon;

        public ViewHolder(View v) {
            super(v);
            mAppName = (TextView) v.findViewById(R.id.tv_app_name);
            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
            mTitle = (TextView) v.findViewById(R.id.tv_title);
            mBody = (TextView) v.findViewById(R.id.tv_body);
            mDate = (TextView) v.findViewById(R.id.tv_date);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        NotificationItem notificationItem=notificationItems.get(position);
        viewHolder.mAppName.setText(Common.getAppName(
                notificationItem.getPackageName()));
        viewHolder.mDate.setText(mDateFormat.format(new Date(notificationItem.getDate())));
        viewHolder.mTitle.setText(notificationItem.getTitle());
        viewHolder.mBody.setText(notificationItem.getBody());
        viewHolder.mAppIcon.setImageDrawable(notificationItem.getAppIcon());
    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }

    public void setCustomNotificationItems(ArrayList<NotificationItem> notificationItems) {
        this.notificationItems = notificationItems;
    }
}
