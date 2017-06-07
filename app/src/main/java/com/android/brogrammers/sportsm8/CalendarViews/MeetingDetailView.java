package com.android.brogrammers.sportsm8.CalendarViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.ViewHelperClass;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.DatabaseHelperMeetings;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.apptik.widget.MultiSlider;

public class MeetingDetailView extends AppCompatActivity implements UIthread, SwipeRefreshLayout.OnRefreshListener {

    private int count = 0;
    private ArrayList<Information> members;
    private ArrayList<UserInfo> Selection;
    Meeting thisMeeting;
    ListViewAdapter arrayAdapter;
    DatabaseHelperMeetings databaseHelperMeetings;
    DateTime startDateTime, endDateTime;

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
    @BindView(R.id.meeting_detail_collABL)
    AppBarLayout appBarLayout;
    @BindView(R.id.accept_meeting)
    ImageButton acceptMeeting;
    @BindView(R.id.decline_meeting)
    ImageButton declineMeeting;
    @BindView(R.id.rangebar)
    MultiSlider rangeBar;
    @BindView(R.id.progress_bar)
    LinearLayout progressBar;
    @BindView(R.id.new_endTime_detailV)
    TextView newEndTimeView;
    @BindView(R.id.new_startTime_detailV)
    TextView newStartTimeView;
    @BindView(R.id.dash_detailView)
    TextView dashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //Variables
        Bundle b = getIntent().getExtras();
        thisMeeting = (Meeting) b.getSerializable("MeetingOnDay");
        startDateTime = thisMeeting.getStartDateTime();
        endDateTime = thisMeeting.getEndDateTime();
        Resources res = getResources();
        TypedArray bannerArray = res.obtainTypedArray(R.array.sportDrawables);
        //Set Views
        textView_time.setText(startDateTime.toString("HH:mm") + "-" + endDateTime.toString("HH:mm"));
        textView_date.setText(startDateTime.toString("dd.MM.YYYY"));
        if (Integer.valueOf(thisMeeting.sportID) < bannerArray.length()) {
            bannerImage.setImageResource(bannerArray.getResourceId(Integer.valueOf(thisMeeting.sportID), R.drawable.custommeeting));
        }
        textView_sportID.setText(thisMeeting.meetingActivity);
        getMemberList();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < -100) verticalOffset = -100;
                float offset = 1 + (verticalOffset / 100);
                scaleView(new View[]{acceptMeeting, declineMeeting, rangeBar, newEndTimeView, newStartTimeView, dashView}, offset);
            }
        });
        databaseHelperMeetings = new DatabaseHelperMeetings(this);
        if (thisMeeting.dynamic == 0) {
            rangeBar.setVisibility(View.GONE);
        } else {
            rangeBarSetup();
        }
        if (thisMeeting.confirmed == 1 || thisMeeting.duration != 0) {
           ViewHelperClass.setInvisible(new View[]{acceptMeeting, declineMeeting, rangeBar, newEndTimeView, newStartTimeView, dashView});
        }
        //    swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void rangeBarSetup() {
        rangeBar.setMax(96);
        newStartTimeView.setText(startDateTime.toString("HH:mm"));
        newEndTimeView.setText(endDateTime.toString("HH:mm"));
        rangeBar.getThumb(0).setValue(startDateTime.getMinuteOfDay() / 15);
        rangeBar.getThumb(1).setValue(endDateTime.getMinuteOfDay() / 15);
        rangeBar.setStepsThumbsApart(4);
        rangeBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    startDateTime = startDateTime.withTimeAtStartOfDay().plusMinutes(value * 15);
                    newStartTimeView.setText(startDateTime.toString("HH:mm"));
                    thisMeeting.setStartDateTime(startDateTime);
                } else {
                    endDateTime = endDateTime.withTimeAtStartOfDay().plusMinutes(value * 15);
                    newEndTimeView.setText(endDateTime.toString("HH:mm"));
                    thisMeeting.setEndStartDateTime(endDateTime);
                }
            }
        });
    }

    private void setUpprogressBar() {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).confirmed == 1) {
                count++;
            }
        }
        for (int i = 0; i < thisMeeting.minParticipants; i++) {
            ImageView imageView = new ImageView(getBaseContext());
            imageView.setImageResource(R.drawable.ic_person_black_36dp);
            imageView.setId(i);
            if (i >= count) {
                imageView.setScaleX(0.5f);
                imageView.setScaleY(0.5f);
                imageView.setAlpha(0.5f);
                if (i >= members.size()) {
                    imageView.setAlpha(0f);
                }
            }
            progressBar.addView(imageView);
        }
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

    @OnClick(R.id.accept_meeting)
    public void acceptMeeting() {
        databaseHelperMeetings.confirm(thisMeeting);
        ViewHelperClass.setInvisible(new View[]{acceptMeeting, declineMeeting, rangeBar, newEndTimeView, newStartTimeView, dashView});
        progressBar.findViewById(count).animate().scaleX(1).scaleY(1).alpha(1);
        for (int i = 0; i < members.size(); i++) {
            String email = LoginScreen.getEmailAdress(this);
            if (members.get(i).email.equals(email)) {
                members.get(i).confirmed = 1;
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.decline_meeting)
    public void declineMeeting() {
        databaseHelperMeetings.declineMeeting(thisMeeting);
        finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<UserInfo>) bundle.getSerializable("partyList");

                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("IndexMeetings.php", "function", "addMembersToMeeting", "MeetingID", thisMeeting.MeetingID + ""));

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
        String[] params = {"IndexMeetings.php", "function", "getMeetingMembers", "MeetingID", thisMeeting.MeetingID + ""};
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
        setUpprogressBar();
        // swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getMemberList();
    }

    public void scaleView(View[] view, float offset) {
        for (int i = 0; i < view.length; i++) {
            view[i].animate().scaleX(offset).setDuration(100);
            view[i].animate().scaleY(offset).setDuration(100);
        }
    }
    private class ListViewAdapter extends BaseAdapter {

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