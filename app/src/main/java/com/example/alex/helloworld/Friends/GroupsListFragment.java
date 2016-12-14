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

import com.example.alex.helloworld.FriendFragment;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class GroupsListFragment extends Fragment implements UIthread {
    private ArrayList<Information> groups;
    private Friends activity;
    private RecyclerView recyclerView;
    private GroupListAdapter adapter;
    private Boolean selectionMode;
    private FriendFragment friendFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groups_fragment, container, false);
        //Views
        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        //variables
        groups = new ArrayList<>();
        friendFragment =(FriendFragment) getParentFragment();
        adapter = new GroupListAdapter(getContext(),null,groups);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI("");
        return view;
    }

    public void updateGroupList() {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexGroups.php", "function", "getGroups","email",email};
        Database db = new Database(this, getContext());
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("IndexGroups", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexGroupsgetGroupsJSON", "");
        try {
            groups = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        adapter = new GroupListAdapter(getContext(),null,groups);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
        friendFragment.setReferencesGroups(groups,this,adapter);
        friendFragment.setSwipeRefreshLayout(false);
    }
}