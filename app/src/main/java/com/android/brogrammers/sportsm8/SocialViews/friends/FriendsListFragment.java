package com.android.brogrammers.sportsm8.SocialViews.friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;

public class FriendsListFragment extends Fragment implements UIthread {

    private ArrayList<Information> friends;
    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private FragmentSocial fragmentSocial;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        //Declaration Variables
        friends = new ArrayList<>();
        //
        fragmentSocial = (FragmentSocial) getParentFragment();
     // Fragment fragment = getFragmentManager().findFragmentById(R.id.friends_tab_fragment);
        //Declaration Views
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        updateUI("");
        recyclerView.addOnScrollListener(new onScrollListener());
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        updateUI("");
        return view;
    }

    public void updateFriendsList() {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexFriendship.php", "function", "getFriends", "email", email};
        Database db = new Database(this, getContext());
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("IndexFriendship", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexFriendshipgetFriendsJSON", "");
        try {
            friends = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        adapter = new FriendsListAdapter(getContext(), null, friends,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //Method to set all connections between Friends,Friendslistfragment, Friendslistadapter and Clicklistener
        fragmentSocial.setReferencesFriends(friends,this,adapter);
        adapter.notifyDataSetChanged();
        //stops loading animation
        fragmentSocial.setSwipeRefreshLayout(false);

    }
    private class onScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setDuration(100);
            } else if (dy < 0) {
                bottomNavigationView.animate().translationY(0).setDuration(100);
            }
            super.onScrolled(recyclerView, dx, dy);
        }
    }




}