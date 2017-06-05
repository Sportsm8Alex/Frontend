package com.android.brogrammers.sportsm8.CalendarViews;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.CalendarViews.Adapter.MeetingCardAdapter;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * CCreated by Korbi on 10/30/2016.
 */

public class DayFragment extends Fragment implements Updateable {
    RecyclerView recyclerView;
    ArrayList<Meeting> meetingsOnDay;
    private FloatingActionButton floatingActionButton;

    public static DayFragment newInstance(int position, ArrayList<Meeting> meetingsOnDay) {

        // how exactly to hand down meetingsOnDay to MeetingCardAdapter?
        //#######################??
        DayFragment dayFragment = new DayFragment();
        Bundle args = new Bundle();
        Collections.sort(meetingsOnDay,new CustomComperator());
        args.putSerializable("meetingsOnDay", meetingsOnDay);
        //args.putParcelableArrayList("meetingsOnDay", meetingsOnDay);
        dayFragment.setArguments(args);
        return dayFragment;
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
        RecyclerView.Adapter rvAdapter = new MeetingCardAdapter(getContext(), meetingsOnDay);
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

    public ArrayList<Meeting> getMeetingsOnDay(){
        return (ArrayList<Meeting>) this.getArguments().getSerializable("meetingsOnDay");
    }

    @Override
    public void update() {
        meetingsOnDay =  (ArrayList<Meeting>) this.getArguments().getSerializable("meetingsOnDay");
       // RecyclerView.Adapter rvAdapter = new MeetingCardAdapter(getContext(),  (ArrayList<Information>) this.getArguments().getSerializable("meetingsOnDay"));
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public static class CustomComperator implements Comparator<Meeting>{

        @Override
        public int compare(Meeting o1, Meeting o2) {
            return o1.getStartDateTime().compareTo(o2.getStartDateTime());
        }
    }
}
