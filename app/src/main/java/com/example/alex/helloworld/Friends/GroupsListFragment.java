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

public class GroupsListFragment extends Fragment implements UIthread {
    private ArrayList<Information> groups;
    Friends activity;
    RecyclerView recyclerView;
    GroupListAdapter adapter;
    Boolean selectionMode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.groups_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.group_recycler_view);
        groups = new ArrayList<>();

        activity = (Friends) getActivity();
        selectionMode=activity.getSelectionMode();
        activity.setReferenceGroupList(this);

        adapter = new GroupListAdapter(getContext(), groups,this,selectionMode);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI("");
        return v;
    }
    public void toggle(int pos) {
        groups.get(pos).selected ^= true;
        activity.setDataGroups(groups);
    }

    public void updateGroupList() {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexGroups.php", "function", "getGroups"};
        Database db = new Database(this, getContext());
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    public void activateSelectionMode(Boolean bool,int count){
        activity.activateSelectionMode(bool,count);
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
        adapter = new GroupListAdapter(getContext(), groups,this,selectionMode);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
        activity.setSwipeRefreshLayout(false);
    }
}