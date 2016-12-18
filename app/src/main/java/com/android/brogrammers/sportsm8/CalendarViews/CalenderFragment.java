package com.android.brogrammers.sportsm8.CalendarViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalenderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment implements UIthread,SwipeRefreshLayout.OnRefreshListener {

    TabLayout slidingTabLayout;
    Activity parentActivity;
    SwipeRefreshLayout swipeRefreshLayout;
    private CharSequence Titles[] = {"Mo", "Di", "We", "Do", "Fr", "Sa", "So"};
    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public CalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalenderFragment.
     */
    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        slidingTabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);


        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        //changed this method
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), parentActivity.getApplicationContext(), 7));

        slidingTabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.calender_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        getMeetings();
        return rootView;
    }

    public void getMeetings() {
        SharedPreferences sharedPrefs = parentActivity.getApplicationContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexMeetings.php", "function", "getMeeting", "email", email};
        Database db = new Database(this, parentActivity.getApplicationContext());
        db.execute(params);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        ViewPager viewPager = (ViewPager) parentActivity.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), parentActivity.getApplicationContext(), 7));
        slidingTabLayout.setupWithViewPager(viewPager);

        // needs to be adapted to keep current db info if refreshing not possible (no overwriting of sharedPreferences if no connection)

    }

    @Override
    public void updateUI(String answer) {
        ViewPager viewPager = (ViewPager) parentActivity.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), parentActivity.getApplicationContext(), 7));
        slidingTabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getMeetings();
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

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private int numberOfTabs;

        public ViewPagerAdapter(FragmentManager fragmentManager, Context ApplicationContext, int numberOfTabs) {
            super(fragmentManager);
            this.numberOfTabs = numberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            //get local Information
            SharedPreferences sharedPrefs = parentActivity.getSharedPreferences("IndexMeetings", Context.MODE_PRIVATE);
            String meetingJson = sharedPrefs.getString("IndexMeetingsgetMeetingJSON", "");
            //
            System.out.println("THESE ARE ALL MEETINGZ " + meetingJson); // Gives me nothing but success = 0 :O
            //
            ArrayList<Information> meetingsOnDay = new ArrayList<Information>();

            try {
                ArrayList<Information> meetings = Database.jsonToArrayList(meetingJson);
                int today = LocalDate.now().getDayOfYear();
                System.out.println("THIS IS TODAY " + today);
                for (int i = 0; i < meetings.size(); i++) {
                    String date = meetings.get(i).startTime.substring(0, 10); //problem if no meetingsOnDay yet!?
                    int dateOfMeeting = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(date).getDayOfYear();
                    if (dateOfMeeting == today + position) {
                        meetingsOnDay.add(meetings.get(i));
                    }
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            return DayFragment.newInstance(position, meetingsOnDay); //position+1 ???
        }

        @Override
        public int getCount() {
            return numberOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String[] btnText = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};

            int todayInWeek = LocalDate.now().getDayOfWeek() - 1;
            /*for(int i=0; i<7; i++){
                tabLayout.addTab(tabLayout.newTab().setText(btnText[(todayInWeek+i)%7]));
            }*/
            return btnText[(todayInWeek + position) % 7];

            //return super.getPageTitle(position);
        }

    }
}
