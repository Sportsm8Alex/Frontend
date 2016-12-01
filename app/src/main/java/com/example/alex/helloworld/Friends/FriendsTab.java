package com.example.alex.helloworld.Friends;

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
import com.example.alex.helloworld.databaseConnection.AsyncResponse;
import com.example.alex.helloworld.databaseConnection.DBconnection;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class FriendsTab extends Fragment {
    private ArrayList<Information> friends;
    RecyclerView recyclerView;
    FriendsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.friends_tab,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.friends_recycler_view);
        friends = new ArrayList<>();
        updateFriendsList("Korbi@Korbi.de"); //Email from Shared Prefernces?

        //Sets empty adapter to prevent Errors
        adapter = new FriendsListAdapter(getContext(), friends,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    public void toggle(int pos){
        friends.get(pos).selected ^= true;
        Friends activity = (Friends) getActivity();
        activity.setData(friends);
    }

    //Needs to be adapted to Alex DatabaseConnection
    private void updateFriendsList(String email) {
        String[] params = {"/IndexFriendship.php", "function", "getFriends", "email", email};
       new DBconnection(new AsyncResponse() {
            @Override
            public void processFinish(String output) throws ParseException, JSONException {
                parseToArrayList(output);
            }
        }).execute(params);


    }

    //Not need for Alex Database Connection
    private ArrayList<Information> parseToArrayList(String jsonObjectSring) throws JSONException {
        if(jsonObjectSring!=null) {
            ArrayList<Information> data = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonObjectSring);

            int i = 0;
            while (jsonObject.has("" + i)) {
                String meetingString = jsonObject.get("" + i).toString();
                Gson gson = new Gson();
                Information current = gson.fromJson(meetingString, Information.class);
                data.add(current);
                i++;
            }
            adapter = new FriendsListAdapter(getContext(), data, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Friends activity = (Friends) getActivity();
            activity.setData(data);
            friends = data;
            return data;
        }else
            return null;

    }


}