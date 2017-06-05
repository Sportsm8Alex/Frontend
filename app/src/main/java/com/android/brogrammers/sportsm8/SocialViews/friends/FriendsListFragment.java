package com.android.brogrammers.sportsm8.SocialViews.friends;

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

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.RetroFitClient;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListFragment extends Fragment {

    private ArrayList<UserInfo> friends;
    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private FragmentSocial fragmentSocial;
    private APIService apiService = APIUtils.getAPIService();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        //Declaration Variables
        friends = new ArrayList<>();
        fragmentSocial = (FragmentSocial) getParentFragment();
        //Declaration Views
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        updateUI();
        return view;
    }

    public void updateFriendsList() {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        apiService.getFriends("getFriends", email).enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                RetroFitClient.storeObjectList(response.body(), "friends", getContext());
                updateUI();
            }
            @Override
            public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {}
        });
    }

    public void updateUI() {
        friends = (ArrayList<UserInfo>) RetroFitClient.retrieveObjectList("friends", getContext(), new TypeToken<ArrayList<UserInfo>>() {
        }.getType());
        adapter = new FriendsListAdapter(getContext(), null, friends, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Method to set all connections between Friends,Friendslistfragment, Friendslistadapter and Clicklistener
        fragmentSocial.setReferencesFriends(friends, this, adapter);
        adapter.notifyDataSetChanged();
        //stops loading animation
        fragmentSocial.setSwipeRefreshLayout(false);
    }
}