package com.android.brogrammers.sportsm8.CalendarViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingDetailView extends AppCompatActivity implements UIthread, SwipeRefreshLayout.OnRefreshListener {

    private int meetingID;
    private String startTime, endTime, sportID, activity;
    private ArrayList<Information> members, Selection;
    ListViewAdapter arrayAdapter;

    @BindView(R.id.listview_meeting_detail)
    ListView listView;
    //    @BindView(R.id.meeting_detail_swipeRefresh)
//    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.time_meeting_detail)
    TextView textView_time;
    @BindView(R.id.time_meetingdetail)
    TextView textView_date;
    @BindView(R.id.activity_name_detailview)
    TextView textView_sportID;
    @BindView(R.id.meeting_detail_view_imageview)
    ImageView bannerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
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
        TypedArray bannerArray = res.obtainTypedArray(R.array.sportDrawables);
        //Set Views
        textView_time.setText(dt.toString("HH:mm") + "-" + dt2.toString("HH:mm"));
        textView_date.setText(dt.toString("dd.MM.YYYY"));
        if (Integer.valueOf(sportID) < bannerArray.length()) {
            bannerImage.setImageResource(bannerArray.getResourceId(Integer.valueOf(sportID), R.drawable.custommeeting));
        }
        textView_sportID.setText(activity);
        getMemberList();
        //    swipeRefreshLayout.setOnRefreshListener(this);
    }


    @OnClick(R.id.add_people_to_meeting)
    public void addPeopleToMeeting(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SelectionMode", true);
        Intent intent = new Intent(this, OnlyFriendsView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.back_arrow_meeting_detail)
    public void back() {
        finish();
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
        if (members != null) {
            members.clear();
        }
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
        arrayAdapter = new ListViewAdapter(this, members);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getMemberList();
    }


    class ListViewAdapter extends BaseAdapter {

        Context context;
        ArrayList<Information> list;

        ListViewAdapter(Context context, ArrayList<Information> memberList) {
            list = memberList;
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
                //time.setText(list.get(i).begin + ":00 - " + end + ":00");
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
    }
}
