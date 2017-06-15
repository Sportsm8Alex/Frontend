package com.android.brogrammers.sportsm8.CalendarViews;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.CalendarViews.Adapter.MeetingCardAdapter;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayFragment extends Fragment {
    RecyclerView recyclerView;
    List<Meeting> meetingsOnDay;
    RecyclerView.Adapter rvAdapter;
    private FloatingActionButton floatingActionButton;

    public static DayFragment newInstance(List<Meeting> meetingsOnDay) {
        DayFragment dayFragment = new DayFragment();
        Bundle args = new Bundle();
        Collections.sort(meetingsOnDay,new CustomComperator());
        args.putSerializable("meetingsOnDay", new ArrayList<>(meetingsOnDay));
        dayFragment.setArguments(args);
        return dayFragment;
    }

    public void updateInstance(List<Meeting> meetings,DateTime today, double latitude,double longitude,boolean locationMode){
        meetingsOnDay.clear();

        for (int i = 0; i < meetings.size(); i++) {
            DateTime dt1 = meetings.get(i).getStartDateTime();
            if (dt1.withTimeAtStartOfDay().equals(today.withTimeAtStartOfDay())) {
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
        rvAdapter = new MeetingCardAdapter(getContext(), meetingsOnDay);
        recyclerView.setAdapter(rvAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day, container, false);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_calendar);
        // needs to use the containers function since its not an activity

        //#############
        recyclerView = (RecyclerView) view.findViewById(R.id.meetings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        meetingsOnDay = (ArrayList<Meeting>) this.getArguments().getSerializable("meetingsOnDay");
        rvAdapter = new MeetingCardAdapter(getContext(), meetingsOnDay);
        recyclerView.setAdapter(rvAdapter);

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    floatingActionButton.animate().translationX(0).setDuration(100);
                } else {
                    floatingActionButton.animate().translationX(500);
                }
            }
        });
        return view;
    }

    public List<Meeting> getMeetingsOnDay(){
        return (ArrayList<Meeting>) this.getArguments().getSerializable("meetingsOnDay");
    }


    public static class CustomComperator implements Comparator<Meeting>{

        @Override
        public int compare(Meeting o1, Meeting o2) {
            return o1.getStartDateTime().compareTo(o2.getStartDateTime());
        }
    }
}
