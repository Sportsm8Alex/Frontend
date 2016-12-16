package com.example.alex.helloworld.CalendarViews;

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
import android.widget.TextView;

import com.example.alex.helloworld.databaseConnection.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by Korbi on 10/30/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MeetingsViewHolder> implements UIthread {

    Context context;
    ArrayList<Information> meetingsOnDay;
    DateTimeFormatter formatter;

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

        DateTime time = formatter.parseDateTime(meetingsOnDay.get(position).startTime);
        meetingsViewHolder.start.setText(time.toString("HH:mm"));
        time = formatter.parseDateTime(meetingsOnDay.get(position).endTime);
        meetingsViewHolder.stopp.setText(time.toString("HH:mm"));
        meetingsViewHolder.textview.setText(time.toString("dd.MM.YYYY"));
        Resources res = context.getResources();
        String[] array = res.getStringArray(R.array.sportarten);
        int x = Integer.valueOf(meetingsOnDay.get(position).sportID);
        meetingsViewHolder.meetingName.setText(array[x]);
        final Information infoData = meetingsOnDay.get(position);
        if (Integer.valueOf(meetingsOnDay.get(position).confirmed) == 0) {
            meetingsViewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.red));

        } else if (Integer.valueOf(meetingsOnDay.get(position).status) == 0) {
            meetingsViewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            meetingsViewHolder.decline.setVisibility(View.GONE);
            meetingsViewHolder.accept.setVisibility(View.GONE);

        } else {
            meetingsViewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            meetingsViewHolder.decline.setVisibility(View.GONE);
            meetingsViewHolder.accept.setVisibility(View.GONE);
        }

        onClickEvents(meetingsViewHolder, position, infoData);
    }

    private void onClickEvents(final MeetingsViewHolder meetingsViewHolder, final int position, final Information infoData) {
        meetingsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MeetingDetailView.class);
                Bundle b = new Bundle();
                b.putString("MeetingID",meetingsOnDay.get(position).MeetingID);
                b.putString("startTime",meetingsOnDay.get(position).startTime);
                b.putString("endTime",meetingsOnDay.get(position).endTime);
                b.putString("sportID",meetingsOnDay.get(position).sportID);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        meetingsViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(position, infoData, v, meetingsViewHolder);
                meetingsViewHolder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
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
                if (Integer.valueOf(meetingsOnDay.get(position).confirmed) == 1) {
                    meetingsViewHolder.decline_2.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    private void removeItem(int pos, Information infoData, View view) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexMeetings.php", "function", "declineAtt", "meetingID", meetingsOnDay.get(pos).MeetingID, "email", email};
        Database db = new Database(this, context);
        db.execute(params);
        int position = meetingsOnDay.indexOf(infoData);
        meetingsOnDay.remove(position);
        notifyItemRemoved(position);
    }

    public void confirm(int pos, Information infoData, View view, MeetingsViewHolder meetingsViewHolder) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexMeetings.php", "function", "confirmAtt", "meetingID", meetingsOnDay.get(pos).MeetingID, "email", email};
        Database db = new Database(this, context);
        db.execute(params);
        view.setVisibility(View.GONE);
        meetingsViewHolder.decline.setVisibility(View.GONE);
        meetingsOnDay.get(pos).confirmed = "1";
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

    class MeetingsViewHolder extends RecyclerView.ViewHolder {

        View indicator;
        TextView textview, start, stopp, meetingName;
        Button decline, decline_2, accept;
        CardView cardView;

        public MeetingsViewHolder(View itemView) {
            super(itemView);
            meetingName = (TextView) itemView.findViewById(R.id.meeting_name);
            indicator = itemView.findViewById(R.id.indicator_view);
            decline_2 = (Button) itemView.findViewById(R.id.decline_meeting_button_2);
            cardView = (CardView) itemView.findViewById(R.id.meeting_card);
            textview = (TextView) itemView.findViewById(R.id.date_textview);
            start = (TextView) itemView.findViewById(R.id.startzeit);
            stopp = (TextView) itemView.findViewById(R.id.stoppzeit);
            accept = (Button) itemView.findViewById(R.id.accept_meeting_button);
            decline = (Button) itemView.findViewById(R.id.decline_meeting_button);
        }
    }
}
