package com.android.brogrammers.sportsm8.CalendarViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.brogrammers.sportsm8.CalendarViews.Adapter.CalendarViewPagerAdapter;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalenderFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalenderFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment2 extends Fragment implements ViewPager.OnPageChangeListener, UIthread, SwipeRefreshLayout.OnRefreshListener {

    CalendarViewPagerAdapter viewPagerAdapter;
    ArrayList<Information> meetings;
    TabLayout tabLayout;
    Activity parentActivity;
    SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;
    private final ArrayList<DayFragment> mFragmentList = new ArrayList<>();
    Boolean onStartUp = true;

    public CalenderFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalenderFragment.
     */
    public static CalenderFragment2 newInstance(String param1, String param2) {
        return new CalenderFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = getActivity();
        meetings = new ArrayList<>();
        getMeetings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.calender_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        //changed this method
        viewPagerAdapter = new CalendarViewPagerAdapter(this.getChildFragmentManager(), parentActivity.getApplicationContext(), mFragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSmoothScrollingEnabled(true);
        customTabs();
        //createScrollView();
        return rootView;
    }


    public void getMeetings() {
        SharedPreferences sharedPrefs = parentActivity.getApplicationContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexMeetings.php", "function", "getMeeting", "email", email};
        Database db = new Database(this, parentActivity.getApplicationContext());
        db.execute(params);
    }

    private void parseMeetings() {
        SharedPreferences sharedPrefs = parentActivity.getSharedPreferences("IndexMeetings", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexMeetingsgetMeetingJSON", "");
        try {
            meetings = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }


    private void customTabs() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(null);
            tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
        int x = 0;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateUI() {
        // needs to be adapted to keep current db info if refreshing not possible (no overwriting of sharedPreferences if no connection)

    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateUI(String answer) {
        parseMeetings();
        if (onStartUp) {
            createFragmentList(7);
            onStartUp = false;
        } else {
            createFragmentList(mFragmentList.size());
        }
        swipeRefreshLayout.setRefreshing(false);
        viewPagerAdapter.notifyDataSetChanged();
        viewPagerAdapter.setNeedsUpdate(false);
        customTabs();
    }

    @Override
    public void onRefresh() {
        viewPagerAdapter.setNeedsUpdate(true);
        getMeetings();
    }

    public void setStartDate(int year, int month, int dayOfMonth) {
        viewPagerAdapter.setNeedsUpdate(true);
        viewPagerAdapter.setToday(new DateTime(year, month + 1, dayOfMonth, 0, 0));
        mFragmentList.clear();
        createFragmentList(7);
        viewPagerAdapter.notifyDataSetChanged();
        customTabs();
        scrollTo(0);
        viewPagerAdapter.setNeedsUpdate(false);
    }


    private ArrayList<DayFragment> addTab(int count) {
        ArrayList<DayFragment> temp = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ArrayList<Information> meetingsOnDay = new ArrayList<Information>();
            for (int j = 0; j < meetings.size(); j++) {
                String date = meetings.get(j).startTime.substring(0, 10); //problem if no meetingsOnDay yet!?
                int dateOfMeeting = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(date).getDayOfYear();
                if (dateOfMeeting == viewPagerAdapter.getToday().getDayOfYear() + (count + i)) {
                    meetingsOnDay.add(meetings.get(j));
                }
            }
            temp.add(DayFragment.newInstance((count + i), meetingsOnDay));
        }
        return temp;
    }

    public MutableDateTime getSelectedDate(){
        MutableDateTime dt = new MutableDateTime();
        dt.addDays(tabLayout.getSelectedTabPosition());
        return dt;
    }

    private void createFragmentList(int count) {
        mFragmentList.clear();
        for (int j = 0; j < count; j++) {
            ArrayList<Information> meetingsOnDay = new ArrayList<Information>();
            //int today = LocalDate.now().getDayOfYear();
            System.out.println("THIS IS TODAY " + viewPagerAdapter.getToday());
            for (int i = 0; i < meetings.size(); i++) {
                String date = meetings.get(i).startTime.substring(0, 10); //problem if no meetingsOnDay yet!?
                int dateOfMeeting = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(date).getDayOfYear();
                if (dateOfMeeting == viewPagerAdapter.getToday().getDayOfYear() + j) {
                    meetingsOnDay.add(meetings.get(i));
                }
            }
            DayFragment temp = DayFragment.newInstance(j, meetingsOnDay);
            mFragmentList.add(temp);
        }
    }

    public void scrollTo(int i) {
        if (tabLayout.getTabAt(i) != null) {
            tabLayout.getTabAt(i).select();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = tabLayout.getTabCount();
        if (count < 60) {
            if (count - position == 1) {
                Toasty.info(parentActivity, "Neue Tage geladen", Toast.LENGTH_SHORT).show();
                mFragmentList.addAll(addTab(count));
                viewPagerAdapter.notifyDataSetChanged();
                customTabs();
                scrollTo(position);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
