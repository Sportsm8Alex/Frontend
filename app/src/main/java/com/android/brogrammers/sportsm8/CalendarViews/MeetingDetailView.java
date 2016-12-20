package com.android.brogrammers.sportsm8.CalendarViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

public class MeetingDetailView extends AppCompatActivity implements UIthread, SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private int meetingID;
    private String startTime, endTime, sportID,activity;
    private ArrayList<Information> members, Selection;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Views
        listView = (ListView) findViewById(R.id.listview_meeting_detail);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.meeting_detail_swipeRefresh);
        TextView textView_time = (TextView) findViewById(R.id.time_meeting_detail);
        TextView textView_date = (TextView) findViewById(R.id.time_meetingdetail);
        TextView textView_sportID = (TextView) findViewById(R.id.activity_name_detailview);
        //Variables
        Bundle b = getIntent().getExtras();
        meetingID = b.getInt("MeetingID");
        startTime = b.getString("startTime");
        endTime = b.getString("endTime");
        sportID = b.getString("sportID");
        activity = b.getString("activity");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(startTime);
        DateTime dt2 = formatter.parseDateTime(endTime);
        Resources res = getResources();
        String[] array = res.getStringArray(R.array.sportarten);
        //Set Views
        textView_time.setText(dt.toString("HH:mm") + "-" + dt2.toString("HH:mm"));
        textView_date.setText(dt.toString("dd.MM.YYYY"));
        if(Integer.valueOf(sportID)==8008) {
           textView_sportID.setText(activity);
        }else{
            textView_sportID.setText(array[Integer.valueOf(sportID)]);
        }
        getMemberList();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SelectionMode", true);
        Intent intent = new Intent(this, OnlyFriendsView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<Information>) bundle.getSerializable("partyList");

                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("IndexMeetings.php", "function", "addMembersToMeeting", "MeetingID", meetingID + ""));

                for (int i = 0; i < Selection.size(); i++) {
                    paramsArrayList.add("member" + i);
                    paramsArrayList.add(Selection.get(i).email);
                }
                String[] params = new String[paramsArrayList.size()];
                params = paramsArrayList.toArray(params);
                Database db = new Database(this, getBaseContext());
                db.execute(params);

            }
        }
        getMemberList();
    }

    private void getMemberList() {
        String[] params = {"IndexMeetings.php", "function", "getMeetingMembers", "MeetingID", meetingID + ""};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        SharedPreferences sharedPrefs = getSharedPreferences("IndexMeetings", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexMeetingsgetMeetingMembersJSON", "");
        try {
            members = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> emails = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            emails.add(members.get(i).email + members.get(i).begin);
        }
        ListViewAdapter arrayAdapter = new ListViewAdapter(this, members);
        listView.setAdapter(arrayAdapter);
        swipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onRefresh() {
        getMemberList();
    }


    class ListViewAdapter extends BaseAdapter {

        ArrayList<Information> list;
        Context context;

        ListViewAdapter(Context context, ArrayList<Information> listItem) {
            list = listItem;
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
                time.setText(list.get(i).begin + ":00 - " + end + ":00");
            } else {
                username.setText(list.get(i).username);
            }
            if (list.get(i).confirmed == 1) {
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            }
            return row;
        }
    }
}
