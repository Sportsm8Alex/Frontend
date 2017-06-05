package com.android.brogrammers.sportsm8.CalendarViews.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.CalendarViews.MeetingDetailView;
import com.android.brogrammers.sportsm8.databaseConnection.DatabaseHelperMeetings;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korbi on 10/30/2016.
 */

public class MeetingCardAdapter extends RecyclerView.Adapter<MeetingCardAdapter.MeetingsViewHolder> implements UIthread {

    private Context context;
    private ArrayList<Meeting> meetingsOnDay;
    private int begin, beginMinute;
    private DatabaseHelperMeetings databaseHelperMeetings;

    public MeetingCardAdapter(Context context, ArrayList<Meeting> meetingsOnDay) {
        this.context = context;
        this.meetingsOnDay = meetingsOnDay;
    }

    //doesnt need an int position because the card looks the same for all; still this is iteratad through before and just like onBindViewHolder!
    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //this is what the recyclerViewAdapter returns to the recyclerView. Equivalent to FragmentPagerAdapter's getItem which returns the Fragment
        View view = LayoutInflater.from(context).inflate(R.layout.item_meetings, parent, false);
        ButterKnife.bind(this, view);
        databaseHelperMeetings = new DatabaseHelperMeetings(context);
        return new MeetingsViewHolder(view);
    }

    //onBindViewholder seems to be iterated through, probably getItemCount() times!!
    @Override
    public void onBindViewHolder(MeetingsViewHolder meetingsViewHolder, int position) {
        getOptimalTime(position);
        DateTime timeS = meetingsOnDay.get(position).getStartDateTime();
        DateTime timeE = meetingsOnDay.get(position).getEndDateTime();
        meetingsViewHolder.time.setText(timeS.toString("HH:mm") + " - " + timeE.toString("HH:mm"));
        meetingsViewHolder.textview.setText(timeE.toString("dd.MM.YYYY"));
        Resources res = context.getResources();
        String[] array = res.getStringArray(R.array.sportarten);
        //TODO: Change php to Json_Check_numeric and Attribute in Information to int
        meetingsViewHolder.meetingName.setText(meetingsOnDay.get(position).meetingActivity);
        final Meeting infoData = meetingsOnDay.get(position);
        if (meetingsOnDay.get(position).dynamic == 1) {
            meetingsViewHolder.otherTime.setVisibility(View.VISIBLE);
            if (meetingsOnDay.get(position).meetingIsGood && meetingsOnDay.get(position).duration != 0) {
                setCardReady(meetingsViewHolder, position);
            } else if (meetingsOnDay.get(position).duration == 0) {
                setCardUndecided(meetingsViewHolder, position);
            } else if (meetingsOnDay.get(position).duration != 0) {
                setCardWaiting(meetingsViewHolder, position);
            }
        } else {
            if (Integer.valueOf(meetingsOnDay.get(position).status) == 0 && meetingsOnDay.get(position).confirmed == 1) {
                setCardWaiting(meetingsViewHolder, position);
            } else if (Integer.valueOf(meetingsOnDay.get(position).status) == 1 && meetingsOnDay.get(position).confirmed == 1) {
                setCardReady(meetingsViewHolder, position);
            }
        }
        meetingsViewHolder.decline.setTag(position);
        onClickEvents(meetingsViewHolder, position, infoData);
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
                    meetingsOnDay.get(position).startTime = startTime.toString("YYYY-MM-dd HH:mm:ss");
                    meetingsOnDay.get(position).endTime = endTime.toString("YYYY-MM-dd HH:mm:ss");
                    dur = begin - i - 1;
                    meetingsOnDay.get(position).meetingIsGood = true;
                }
                begin = 0;
                temp = false;
            }
        }
    }

    private void onClickEvents(final MeetingsViewHolder meetingsViewHolder, final int position, final Meeting infoData) {
        meetingsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MeetingDetailView.class);
                Bundle b = new Bundle();
                b.putSerializable("MeetingOnDay",meetingsOnDay.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        meetingsViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoData.confirmed=1;
                databaseHelperMeetings.confirm(infoData);
                //TODO: Check if minParticipants are reached after accepting
                setCardWaiting(meetingsViewHolder, position);
            }
        });
        meetingsViewHolder.decline_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(infoData);
            }
        });

        meetingsViewHolder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(infoData);
            }
        });
        meetingsViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (meetingsOnDay.get(position).duration != 0 || meetingsOnDay.get(position).confirmed == 1) {
                    meetingsViewHolder.decline_2.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        meetingsViewHolder.otherTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tdp = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        begin = hourOfDay;
                        beginMinute = minute;
                        TimePickerDialog tdp2 = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int endHourOfDay, int endMinute, int second) {
                                databaseHelperMeetings.setOtherTime(begin, beginMinute, endHourOfDay, endMinute, meetingsOnDay.get(position));
                                setCardWaiting(meetingsViewHolder, position);
                            }
                        }, 0, 0, true);
                        tdp2.setTimeInterval(1, 15);
                        tdp2.show(((Activity) context).getFragmentManager(), "tag");
                    }
                }, 0, 0, true);
                tdp.setTimeInterval(1, 15);
                tdp.show(((Activity) context).getFragmentManager(), "tag");
            }
        });
    }


    public void removeItem(Meeting infoData) {
        databaseHelperMeetings.declineMeeting(infoData);
        int position = meetingsOnDay.indexOf(infoData);
        meetingsOnDay.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return meetingsOnDay.size();
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {

    }

    //Help Methods for setting view attributes to declutter Code
    private void setCardWaiting(MeetingsViewHolder viewHolder, int position) {
        viewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        viewHolder.badge.setImageResource(R.drawable.waiting);
        viewHolder.accept.setVisibility(View.GONE);
        viewHolder.decline.setVisibility(View.GONE);
        viewHolder.otherTime.setVisibility(View.GONE);
        if (meetingsOnDay.get(position).dynamic == 1) setMyTime(viewHolder, position);
    }

    private void setCardUndecided(MeetingsViewHolder viewHolder, int position) {
        viewHolder.badge.setImageResource(R.drawable.unanswered);
        viewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        viewHolder.accept.setVisibility(View.VISIBLE);
        viewHolder.decline.setVisibility(View.VISIBLE);
    }

    private void setCardReady(MeetingsViewHolder viewHolder, int position) {
        viewHolder.badge.setImageResource(R.drawable.confirmed_white);
        viewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        viewHolder.accept.setVisibility(View.GONE);
        viewHolder.decline.setVisibility(View.GONE);
        viewHolder.otherTime.setVisibility(View.GONE);
        if (meetingsOnDay.get(position).dynamic == 1) setMyTime(viewHolder, position);
    }

    private void setMyTime(MeetingsViewHolder viewHolder, int position) {
        viewHolder.myTime.setVisibility(View.VISIBLE);
        int end = meetingsOnDay.get(position).begin + meetingsOnDay.get(position).duration;
        MutableDateTime start = new MutableDateTime();
        start.setHourOfDay(meetingsOnDay.get(position).begin / 4);
        start.setMinuteOfHour(meetingsOnDay.get(position).begin % 4 * 15);
        MutableDateTime endTime = new MutableDateTime();
        endTime.setHourOfDay(end / 4);
        endTime.setMinuteOfHour(end % 4 * 15);
        viewHolder.myTime.setText(start.toString("HH:mm - ") + endTime.toString("HH:mm"));
    }


    class MeetingsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.indicator_view)
        View indicator;
        @BindView(R.id.meeting_name)
        TextView meetingName;
        @BindView(R.id.ll_time)
        TextView time;
        @BindView(R.id.date_textview)
        TextView textview;
        @BindView(R.id.myTime_textView)
        TextView myTime;
        @BindView(R.id.decline_meeting_button)
        Button decline;
        @BindView(R.id.decline_meeting_button_2)
        Button decline_2;
        @BindView(R.id.accept_meeting_button)
        Button accept;
        @BindView(R.id.other_time)
        Button otherTime;
        @BindView(R.id.meeting_card)
        CardView cardView;
        @BindView(R.id.status_badge)
        ImageView badge;

        public MeetingsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
