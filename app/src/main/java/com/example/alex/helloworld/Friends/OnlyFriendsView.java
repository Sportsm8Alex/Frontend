package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.alex.helloworld.Friends.FriendsListAdapter;
import com.example.alex.helloworld.Friends.FriendsListFragment;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

public class OnlyFriendsView extends AppCompatActivity implements UIthread, SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private ArrayList<Information> friends;
    private Boolean search = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        search = b.getBoolean("search");
        ImageButton addMembers = (ImageButton) findViewById(R.id.image_button_add_groupmembers);
        SearchView searchView = (SearchView) findViewById(R.id.searchview_new_friends);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        recyclerView = (RecyclerView) findViewById(R.id.add_new_friend_recyclerview);
        friends = new ArrayList<>();
        adapter = new FriendsListAdapter(getBaseContext(), friends, null, this, false, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        if (!search) {
            searchView.setVisibility(View.GONE);
            addMembers.setVisibility(View.VISIBLE);
            getFriends();
        }
    }

    public void onClick(View view) {

        ArrayList<Information> selectionfriends = new ArrayList<>();
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).selected) {
                selectionfriends.add(friends.get(i));
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("partyList", selectionfriends);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getSearchResults(String search) {
        SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexFriendship.php", "function", "searchNewFriend", "email", email, "friendname", search};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
        updateUI();


    }

    private void getFriends(){
        SharedPreferences sharedPrefs = getSharedPreferences("IndexFriendship", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexFriendshipgetFriendsJSON", "");
        try {
            friends = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        Iterator<Information> itr = friends.iterator();
        while(itr.hasNext()){
            Information info = itr.next();
            if(Integer.valueOf(info.getConfirmed())==0){
                itr.remove();
            }
        }
        adapter = new FriendsListAdapter(getBaseContext(), friends, null, this, true, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        SharedPreferences sharedPrefs = getSharedPreferences("IndexFriendship", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexFriendshipsearchNewFriendJSON", "");
        try {
            friends = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        adapter = new FriendsListAdapter(getBaseContext(), friends, null, this, false, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    public String getEmail(int adapterPosition) {
        return friends.get(adapterPosition).email;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getSearchResults(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void toggle(int adapterPosition) {
        friends.get(adapterPosition).selected^=true;
    }


}
