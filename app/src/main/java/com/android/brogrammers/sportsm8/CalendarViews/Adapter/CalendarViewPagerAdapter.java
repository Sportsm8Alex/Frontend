package com.android.brogrammers.sportsm8.CalendarViews.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.CalendarViews.DayFragment;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korbi on 23.05.2017.
 */


public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<DayFragment> fragmentList = new ArrayList<>();
    private Context context;
    private boolean needsUpdate=false;
    private DateTime today;

    public CalendarViewPagerAdapter(FragmentManager fragmentManager, Context ApplicationContext, List<DayFragment> meetings) {
        super(fragmentManager);
        fragmentList.clear();
        this.context = ApplicationContext;
        today=new DateTime();
        fragmentList = meetings; //Sets a reference doesnt copy
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        if (needsUpdate) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DateTime todayPosition = today.plusDays(position);
        return todayPosition.getDayOfMonth() + "." + todayPosition.getMonthOfYear();
    }

    public View getTabView(int position) {

        DateTime todayPosition = today.plusDays(position);
        View v = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) v.findViewById(R.id.textView_date);
        TextView tv2 = (TextView) v.findViewById(R.id.textView_day);
        tv.setText(todayPosition.getDayOfMonth() + "." + todayPosition.getMonthOfYear());
        tv2.setText(todayPosition.toString("E"));

        DayFragment dayFragment = (DayFragment) this.getItem(position);
        List<Meeting> onDay = dayFragment.getMeetingsOnDay();
        if (onDay != null) {
            if (onDay.size() > 0) {
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }
            for (int i = 0; i < onDay.size(); i++) {
                if (onDay.get(i).confirmed == 0) {
                    v.findViewById(R.id.imgView).setVisibility(View.VISIBLE);
                }
            }

        }
        return v;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public DateTime getToday() {
        return today;
    }

    public void setToday(DateTime today) {
        this.today = today;
    }
}

