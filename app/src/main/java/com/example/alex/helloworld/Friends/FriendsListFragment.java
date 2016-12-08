package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment implements UIthread {

    private ArrayList<Information> friends;
    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private Boolean selectionMode;
    private Friends activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        //Declaration Variables
        friends = new ArrayList<>();
        activity = (Friends) getActivity();
        selectionMode = activity.getSelectionMode();
        //Declaration Views
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);

        activity.setReferenceFriendsList(this);
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

    public void declineSelection() {
        adapter = new FriendsListAdapter(getContext(), friends, this, null, selectionMode, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }

    public void toggle(int pos) {
        friends.get(pos).selected ^= true;
        activity.setDataFriends(friends);
    }

    public void activateSelectionMode(Boolean bool, int count) {
        activity.activateSelectionMode(bool, count);
    }

    public void updateCount(int count) {
        activity.updateCount(count);
    }

    public void removeSelection() {
        updateUI("");
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
        adapter = new FriendsListAdapter(getContext(), friends, this, null, selectionMode, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Friends activity = (Friends) getActivity();
        activity.setDataFriends(friends);
        adapter.notifyDataSetChanged();
        activity.setSwipeRefreshLayout(false);
    }



}