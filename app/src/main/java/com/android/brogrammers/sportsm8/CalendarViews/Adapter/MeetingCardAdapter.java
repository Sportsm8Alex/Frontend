package com.android.brogrammers.sportsm8.CalendarViews.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.BR;
import com.android.brogrammers.sportsm8.CalendarViews.DayFragment;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.DatabaseHelperMeetings;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databinding.ItemMeetingsBinding;

import org.joda.time.MutableDateTime;

import java.util.List;

/**
 * Created by Korbi on 10/30/2016.
 */

public class MeetingCardAdapter extends RecyclerView.Adapter<MeetingCardAdapter.MeetingsViewHolder>{

    private Context context;
    private List<Meeting> meetingsOnDay;
    private DatabaseHelperMeetings databaseHelperMeetings;
    private MeetingsViewHolder meetingsViewHolder;
    private DayFragment dayFragment;

    public MeetingCardAdapter(Context context, List<Meeting> meetingsOnDay,DayFragment dayFragment) {
        this.context = context;
        this.meetingsOnDay = meetingsOnDay;
        this.dayFragment =dayFragment;
    }

    //doesnt need an int position because the card looks the same for all; still this is iterated through before and just like onBindViewHolder!
    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMeetingsBinding viewDataBinding = DataBindingUtil.inflate( LayoutInflater.from(context),R.layout.item_meetings,parent,false);
        viewDataBinding.setPresenter(dayFragment);
        //this is what the recyclerViewAdapter returns to the recyclerView. Equivalent to FragmentPagerAdapter's getItem which returns the Fragment
        databaseHelperMeetings = new DatabaseHelperMeetings(context);
        return new MeetingsViewHolder(viewDataBinding);
    }

    //onBindViewholder seems to be iterated through, probably getItemCount() times!!
    @Override
    public void onBindViewHolder(MeetingsViewHolder meetingsViewHolder, int position) {
        this.meetingsViewHolder = meetingsViewHolder;
        ViewDataBinding viewDataBinding =  this.meetingsViewHolder.getViewDataBinding();
        if (meetingsOnDay.get(position).dynamic==1) getOptimalTime(position);
        viewDataBinding.setVariable(BR.meeting,meetingsOnDay.get(position));
        viewDataBinding.executePendingBindings();
    }


    private void getOptimalTime(int position) {
        int[] timeArray = meetingsOnDay.get(position).timeArray;
        MutableDateTime startTime = meetingsOnDay.get(position).getStartDateTime().toMutableDateTime();
        MutableDateTime endTime = meetingsOnDay.get(position).getEndDateTime().toMutableDateTime();
        Boolean temp = false;
        int begin = 0;
        int dur = 0;
        for (int i = 0; i < 96; i++) {
            if (!temp && timeArray[i] >= meetingsOnDay.get(position).minParticipants) {
                begin = i + 1;
                temp = true;
            } else if (temp && timeArray[i] < meetingsOnDay.get(position).minParticipants) {
                if (i - begin - 1 > dur) {
                    startTime.setHourOfDay((begin / 4));
                    startTime.setMinuteOfHour(begin % 4 * 15);
                    endTime.setHourOfDay(i / 4);
                    endTime.setMinuteOfHour(i % 4 * 15);
                    meetingsOnDay.get(position).setStartTime(startTime.toString("YYYY-MM-dd HH:mm:ss"));
                    meetingsOnDay.get(position).setEndTime(endTime.toString("YYYY-MM-dd HH:mm:ss"));
                    dur = begin - i - 1;
                    meetingsOnDay.get(position).meetingIsGood = true;
                } else {
                    meetingsOnDay.get(position).meetingIsGood = false;
                }
                begin = 0;
                temp = false;
            }
        }

    }

    public void setMeetingsOnDay(List<Meeting> meetingsOnDay) {
        this.meetingsOnDay = meetingsOnDay;
    }

    public void removeItem(Meeting infoData) {
        int position = meetingsOnDay.indexOf(infoData);
        meetingsOnDay.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return meetingsOnDay.size();
    }


    class MeetingsViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mViewDataBinding;

        public MeetingsViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
//            ButterKnife.bind(this, itemView);
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

}
