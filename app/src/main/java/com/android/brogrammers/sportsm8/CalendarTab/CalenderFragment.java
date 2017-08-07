package com.android.brogrammers.sportsm8.CalendarTab;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brogrammers.sportsm8.CalendarTab.Adapter.CalendarViewPagerAdapter;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.ViewHelperClass;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalenderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment implements ViewPager.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener {

    CalendarViewPagerAdapter viewPagerAdapter;
    List<Meeting> meetings;
    TabLayout tabLayout;
    Activity parentActivity;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialCalendarView calendarView;
    TextView filterTV;
    private OnFragmentInteractionListener mListener;
    private final List<DayFragment> mFragmentList = new ArrayList<>();
    Boolean onStartUp = true;
    double longitude, latitude;
    private boolean locationMode;
    APIService apiService = APIUtils.getAPIService();
    DateTime today = new DateTime();

    public CalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalenderFragmentOld.
     */
    public static CalenderFragment newInstance(String param1, String param2) {
        return new CalenderFragment();
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
        calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendar_calendar);
        filterTV = (TextView) rootView.findViewById(R.id.filter_tv);
        //changed this method
        viewPagerAdapter = new CalendarViewPagerAdapter(this.getChildFragmentManager(), parentActivity.getApplicationContext(), mFragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSmoothScrollingEnabled(true);
        customTabs();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                setStartDate(date.getYear(), date.getMonth(), date.getDay());
                ViewHelperClass.expand(calendarView, 250);
            }
        });


        //createScrollView();
        return rootView;
    }


    public void getMeetings() {
        SharedPreferences sharedPrefs = parentActivity.getApplicationContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        apiService.getMeetings(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Meeting>>() {
                    @Override
                    public void onSuccess(List<Meeting> meetingsList) {
                        if (meetingsList != null) meetings = meetingsList;
                        createHighlightList();
                        if (onStartUp) {
                            createFragmentList(14);
                            onStartUp = false;
                            viewPagerAdapter.notifyDataSetChanged();
                        } else {
                            int position = tabLayout.getSelectedTabPosition();
                            DayFragment dayFragment = (DayFragment) viewPagerAdapter.getItem(position);
                            dayFragment.updateInstance(meetings, viewPagerAdapter.getToday().plusDays(position), latitude, longitude, locationMode);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        customTabs();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getContext(), "Connection Failed").show();
                    }
                });
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
    public void onRefresh() {

        //viewPagerAdapter.setNeedsUpdate(true);
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


    private List<DayFragment> addTab(int count) {
        List<DayFragment> temp = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<Meeting> meetingsOnDay = new ArrayList<>();
            for (int j = 0; j < meetings.size(); j++) {
                String date = meetings.get(j).getDay(); //problem if no meetingsOnDay yet!?
                int dateOfMeeting = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(date).getDayOfYear();
                if (dateOfMeeting == viewPagerAdapter.getToday().getDayOfYear() + (count + i)) {
                    meetingsOnDay.add(meetings.get(j));
                }
            }
            temp.add(DayFragment.newInstance(meetingsOnDay));
        }
        return temp;
    }

    public MutableDateTime getSelectedDate() {
        MutableDateTime dt = new MutableDateTime();
        dt.addDays(tabLayout.getSelectedTabPosition());
        return dt;
    }

    private void createFragmentList(int count) {
        mFragmentList.clear();
        for (int j = 0; j < count; j++) {
            List<Meeting> meetingsOnDay = new ArrayList<>();
            //int today = LocalDate.now().getDayOfYear();
            System.out.println("THIS IS TODAY " + viewPagerAdapter.getToday());
            for (int i = 0; i < meetings.size(); i++) {
                DateTime dt1 = meetings.get(i).getStartDateTime();
                DateTime dt2 = viewPagerAdapter.getToday();
                if (dt1.withTimeAtStartOfDay().equals(dt2.plusDays(j).withTimeAtStartOfDay())) {
                    if (locationMode) {
                        Location start = new Location("locationA");
                        start.setLatitude(latitude);
                        start.setLongitude(longitude);
                        Location end = new Location("locationB");
                        end.setLatitude(meetings.get(i).latitude);
                        end.setLongitude(meetings.get(i).longitude);
                        double distance = start.distanceTo(end);
                        if (distance < 5000) {
                            meetingsOnDay.add(meetings.get(i));
                        }
                    } else {
                        meetingsOnDay.add(meetings.get(i));
                    }
                }
            }
            DayFragment temp = DayFragment.newInstance(meetingsOnDay);
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

    public void toggleView(View view) {
        switch (view.getId()) {
            case R.id.setLocation:
                ViewHelperClass.expand(filterTV, 250);
                break;
            case R.id.change_start_date:
                ViewHelperClass.expand(calendarView, 250);
                break;
        }
    }

    public void createHighlightList() {
        List<CalendarDay> highlights = new ArrayList<>();
        for (int i = 0; i < meetings.size(); i++) {
            DateTime dateTime = meetings.get(i).getStartDateTime();
            CalendarDay date1 = CalendarDay.from(dateTime.toDate());
            highlights.add(date1);
        }
        EventDecorator eventDecorator = new EventDecorator(ContextCompat.getColor(getContext(), R.color.red), highlights);
        calendarView.addDecorator(eventDecorator);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        calendarView.setTileHeightDp(35);
        calendarView.setTileWidth(displayMetrics.widthPixels / 8);
        // CalendarDay[] days = highlights.toArray(new CalendarDay[highlights.size()]);
    }

    public void setLocation(double longitude, double latitude, boolean locationMode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationMode = locationMode;
        viewPagerAdapter.setNeedsUpdate(true);
        createFragmentList(tabLayout.getTabCount());
        viewPagerAdapter.notifyDataSetChanged();
        customTabs();
        viewPagerAdapter.setNeedsUpdate(false);
    }

    public void setFilterText(CharSequence text) {
        filterTV.setText("Meetings in:  " + text);
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

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final List<CalendarDay> dates = new ArrayList<>();

        public EventDecorator(int color, List<CalendarDay> dates) {
            this.color = color;
            this.dates.addAll(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            //  view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_access_time_black_48dp));
            view.addSpan(new DotSpan(5, color));
        }
    }

}
