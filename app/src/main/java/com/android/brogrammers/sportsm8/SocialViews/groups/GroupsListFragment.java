package com.android.brogrammers.sportsm8.SocialViews.groups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Group;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.RetroFitClient;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsListFragment extends Fragment {
    private ArrayList<Group> groups;
    private RecyclerView recyclerView;
    private GroupListAdapter adapter;
    private FragmentSocial fragmentSocial;
    private APIService apiService = APIUtils.getAPIService();
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        //Views
        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        //variables
        groups = new ArrayList<>();
        fragmentSocial = (FragmentSocial) getParentFragment();
        adapter = new GroupListAdapter(getContext(), null, groups);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        return view;
    }

    public void updateGroupList() {
        final SharedPreferences sharedPrefs = getContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        apiService.getGroups("getGroups", email).enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                RetroFitClient.storeObjectList(response.body(), "groups", getContext());
                updateUI();
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) { }
        });
    }
    public void updateUI() {
        groups = (ArrayList<Group>) RetroFitClient.retrieveObjectList("groups", getContext(), new TypeToken<ArrayList<Group>>() {
        }.getType());
        adapter = new GroupListAdapter(getContext(), null, groups);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
        fragmentSocial.setReferencesGroups(groups, this, adapter);
        fragmentSocial.setSwipeRefreshLayout(false);
    }


}