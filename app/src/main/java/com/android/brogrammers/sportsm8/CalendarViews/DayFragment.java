package com.android.brogrammers.sportsm8.CalendarViews;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Information;

import java.util.ArrayList;

/**
 * CCreated by Korbi on 10/30/2016.
 */

public class DayFragment extends Fragment {
    RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;

    public static DayFragment newInstance(int position, ArrayList<Information> meetingsOnDay) {

        // how exactly to hand down meetingsOnDay to RecyclerViewAdapter?
        //#######################??
        DayFragment dayFragment = new DayFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.frragment_day, container, false);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        // needs to use the containers function since its not an activity

        //#############
        //It doesnt seem to find the recyclerView!?
        recyclerView = (RecyclerView) view.findViewById(R.id.meetings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter rvAdapter = new RecyclerViewAdapter(getContext(), (ArrayList<Information>) this.getArguments().getSerializable("meetingsOnDay"));
        recyclerView.setAdapter(rvAdapter);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    bottomNavigationView.animate().translationY(0).setDuration(100);
                } else {
                    bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setDuration(100);
                }
            }
        });
        return view;
    }


}
