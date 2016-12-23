package com.android.brogrammers.sportsm8.CalendarViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TimePicker;

import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Korbi on 10/30/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MeetingsViewHolder> implements UIthread {

    private Context context;
    private ArrayList<Information> meetingsOnDay;
    private DateTimeFormatter formatter;
    private int begin,beginMinute;

    public RecyclerViewAdapter(Context context, ArrayList<Information> meetingsOnDay) {
        this.context = context;
        this.meetingsOnDay = meetingsOnDay;
    }

    //doesnt need an int position because the card looks the same for all; still this is iteratad through before and just like onBindViewHolder!
    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //this is what the recyclerViewAdapter returns to the recyclerView. Equivalent to FragmentPagerAdapter's getItem which returns the Fragment
        formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        View view = LayoutInflater.from(context).inflate(R.layout.item_meetings, parent, false);
        return new MeetingsViewHolder(view);
    }

    //onBindViewholder seems to be iterated through, probably getItemCount() times!!
    @Override
    public void onBindViewHolder(MeetingsViewHolder meetingsViewHolder, int position) {
        getOptimalTime(position);
        DateTime timeS = formatter.parseDateTime(meetingsOnDay.get(position).startTime);
        DateTime timeE = formatter.parseDateTime(meetingsOnDay.get(position).endTime);
        meetingsViewHolder.time.setText(timeS.toString("HH:mm") + " - " + timeE.toString("HH:mm"));
        meetingsViewHolder.textview.setText(timeE.toString("dd.MM.YYYY"));
        Resources res = context.getResources();
        String[] array = res.getStringArray(R.array.sportarten);
        //TODO: Change php to Json_Check_numeric and Attribute in Information to int
        int x = Integer.valueOf(meetingsOnDay.get(position).sportID);
        if (x == 8008) {
            meetingsViewHolder.meetingName.setText(meetingsOnDay.get(position).meetingActivity);
        } else {
            meetingsViewHolder.meetingName.setText(array[x]);
        }
        final Information infoData = meetingsOnDay.get(position);
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
        onClickEvents(meetingsViewHolder, position, infoData);
    }

    private void getOptimalTime(int position) {
        int[] timeArray = meetingsOnDay.get(position).timeArray;
        MutableDateTime startTime = formatter.parseMutableDateTime(meetingsOnDay.get(position).startTime);
        MutableDateTime endTime = formatter.parseMutableDateTime(meetingsOnDay.get(position).endTime);
        Boolean temp = false;
        int begin = 0;
        int dur = 0;
        for (int i = 0; i < 96; i++) {
            if (!temp && timeArray[i] >= meetingsOnDay.get(position).minParticipants) {
                begin = i+1;
                temp = true;
            } else if (temp && timeArray[i] < meetingsOnDay.get(position).minParticipants) {
                if (i - begin - 1 > dur) {
                    startTime.setHourOfDay((begin/4));
                    startTime.setMinuteOfHour(begin%4*15);
                    endTime.setHourOfDay(i/4);
                    endTime.setMinuteOfHour(i%4*15);
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

    private void onClickEvents(final MeetingsViewHolder meetingsViewHolder, final int position, final Information infoData) {
        meetingsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MeetingDetailView.class);
                Bundle b = new Bundle();
                b.putInt("MeetingID", meetingsOnDay.get(position).MeetingID);
                b.putString("startTime", meetingsOnDay.get(position).startTime);
                b.putString("endTime", meetingsOnDay.get(position).endTime);
                b.putString("sportID", meetingsOnDay.get(position).sportID);
                b.putString("activity", meetingsOnDay.get(position).meetingActivity);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        meetingsViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(position);
                //TODO: Check if minParticipants are reached after accepting
                setCardWaiting(meetingsViewHolder, position);
            }
        });
        meetingsViewHolder.decline_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position, infoData, v);
            }
        });

        meetingsViewHolder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position, infoData, v);
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
                                setOtherTime(begin,beginMinute, endHourOfDay,endMinute, position);
                                setCardWaiting(meetingsViewHolder, position);
                            }
                        }, 0, 0, true);
                        tdp2.setTimeInterval(1, 15);
                        tdp2.show(((Activity)context).getFragmentManager(),"tag");
                    }
                }, 0, 0, true);
                tdp.setTimeInterval(1, 15);
                tdp.show(((Activity)context).getFragmentManager(),"tag");


               /* new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        begin = i;
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                setOtherTime(begin, i, position);
                                setCardWaiting(meetingsViewHolder, position);
                            }
                        }, 0, 0, true).show();
                    }
                }, 0, 0, true).show();*/

            }
        });
    }

    public void setOtherTime(int begin,int beginMinute, int end,int endMinute, int position) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        Database db = new Database(this, context);
        int beginDatabase=begin*4+(beginMinute/15);
        int durationDatabase=((end*4)+endMinute/15)-((begin*4)+beginMinute/15);
        String[] params = {"IndexMeetings.php", "function", "setOtherTime", "start", beginDatabase+"", "duration", durationDatabase+"", "meetingID", meetingsOnDay.get(position).MeetingID + "", "email", email};
        db.execute(params);
    }

    private void removeItem(int pos, Information infoData, View view) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexMeetings.php", "function", "declineAtt", "meetingID", meetingsOnDay.get(pos).MeetingID + "", "email", email};
        Database db = new Database(this, context);
        db.execute(params);
        int position = meetingsOnDay.indexOf(infoData);
        meetingsOnDay.remove(position);
        notifyItemRemoved(position);
    }

    public void confirm(int pos) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        if (meetingsOnDay.get(pos).dynamic == 0) {
            String[] params = {"IndexMeetings.php", "function", "confirmAtt", "meetingID", meetingsOnDay.get(pos).MeetingID + "", "email", email};
            Database db = new Database(this, context);
            db.execute(params);
            meetingsOnDay.get(pos).confirmed = 1;
        } else {
            DateTime timeS = formatter.parseDateTime(meetingsOnDay.get(pos).startTime);
            DateTime timeE = formatter.parseDateTime(meetingsOnDay.get(pos).endTime);
            setOtherTime(timeS.getHourOfDay(),timeS.getMinuteOfHour(), timeE.getHourOfDay(),timeE.getMinuteOfHour(), pos);
        }
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
        start.setHourOfDay(meetingsOnDay.get(position).begin/4);
        start.setMinuteOfHour(meetingsOnDay.get(position).begin%4*15);
        MutableDateTime endTime = new MutableDateTime();
        endTime.setHourOfDay(end/4);
        endTime.setMinuteOfHour(end%4*15);
        viewHolder.myTime.setText(start.toString("HH:mm - ")+endTime.toString("HH:mm"));
    }


    class MeetingsViewHolder extends RecyclerView.ViewHolder {

        View indicator;
        TextView textview, time, meetingName, myTime;
        Button decline, decline_2, accept, otherTime;
        CardView cardView;
        ImageView badge;

        public MeetingsViewHolder(View itemView) {
            super(itemView);
            meetingName = (TextView) itemView.findViewById(R.id.meeting_name);
            time = (TextView) itemView.findViewById(R.id.ll_time);
            textview = (TextView) itemView.findViewById(R.id.date_textview);
            myTime = (TextView) itemView.findViewById(R.id.myTime_textView);
            decline_2 = (Button) itemView.findViewById(R.id.decline_meeting_button_2);
            accept = (Button) itemView.findViewById(R.id.accept_meeting_button);
            decline = (Button) itemView.findViewById(R.id.decline_meeting_button);
            otherTime = (Button) itemView.findViewById(R.id.other_time);
            cardView = (CardView) itemView.findViewById(R.id.meeting_card);
            indicator = itemView.findViewById(R.id.indicator_view);
            badge = (ImageView) itemView.findViewById(R.id.status_badge);
        }
    }
}
