package com.srinivas.ele.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinivas.ele.R;
import com.srinivas.ele.pojo.CustomUsageStats;
import com.srinivas.ele.utils.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {

    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mAppName;
        private final TextView mTotalTimeUsed;
        private final ImageView mAppIcon;
        private final TextView mLastTimeUsed;

        public ViewHolder(View v) {
            super(v);
            mAppName = (TextView) v.findViewById(R.id.tv_app_name);
            mTotalTimeUsed = (TextView) v.findViewById(R.id.tv_total_time_used);
            mLastTimeUsed = (TextView) v.findViewById(R.id.tv_last_time_used);
            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
        }

        public TextView getLastTimeUsed() {
            return mLastTimeUsed;
        }

        public TextView getTotalTimeUsed() {
            return mTotalTimeUsed;
        }

        public TextView getAppName() {
            return mAppName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }
    }

    public UsageListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_usage_stats_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getAppName().setText(Common.getAppName(
                mCustomUsageStatsList.get(position).usageStats.getPackageName()));
        long totalTimeUsed = mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();
        long lastTimeUsed = mCustomUsageStatsList.get(position).usageStats.getLastTimeUsed();
        viewHolder.getLastTimeUsed().setText(mDateFormat.format(new Date(lastTimeUsed)));
        viewHolder.getTotalTimeUsed().setText(Common.getFormattedTime(totalTimeUsed));
        viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);
    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }


}