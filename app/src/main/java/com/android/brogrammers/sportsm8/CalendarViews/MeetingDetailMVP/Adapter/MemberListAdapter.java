package com.android.brogrammers.sportsm8.CalendarViews.MeetingDetailMVP.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import org.joda.time.MutableDateTime;

import java.util.ArrayList;

/**
 * Created by Korbi on 08.06.2017.
 */

public class MemberListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserInfo> list;

    public MemberListAdapter(Context context, ArrayList<UserInfo> memberList) {
        this.list = memberList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_listitem, viewGroup, false);
        TextView username = (TextView) row.findViewById(R.id.meeting_username);
        TextView time = (TextView) row.findViewById(R.id.meeting_user_time);
        int end = list.get(i).begin + list.get(i).duration;
        if (list.get(i).duration != 0) {
            row.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            username.setText(list.get(i).username);
            //time.setFilterText(list.get(i).begin + ":00 - " + end + ":00");
            MutableDateTime start = new MutableDateTime();
            start.setHourOfDay(list.get(i).begin / 4);
            start.setMinuteOfHour(list.get(i).begin % 4 * 15);
            MutableDateTime endTime = new MutableDateTime();
            endTime.setHourOfDay(end / 4);
            endTime.setMinuteOfHour(end % 4 * 15);
            time.setText(start.toString("HH:mm - ") + endTime.toString("HH:mm"));
        } else {
            username.setText(list.get(i).username);
        }
        if (list.get(i).confirmed == 1) {
            row.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }
        return row;
    }

    public void setList(ArrayList<UserInfo> list) {
        this.list = list;
    }
}
