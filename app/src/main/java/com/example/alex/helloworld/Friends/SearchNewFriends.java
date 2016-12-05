package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.alex.helloworld.Friends.FriendsListAdapter;
import com.example.alex.helloworld.Friends.FriendsListFragment;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class SearchNewFriends extends AppCompatActivity implements UIthread, SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private ArrayList<Information> friends;
    private FriendsListFragment friendsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchView searchView = (SearchView) findViewById(R.id.searchview_new_friends);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        friendsListFragment = new FriendsListFragment();
        recyclerView = (RecyclerView) findViewById(R.id.add_new_friend_recyclerview);
        friends = new ArrayList<>();
        adapter = new FriendsListAdapter(getBaseContext(), friends, friendsListFragment, this, false, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

    }

    private void getSearchResults(String search) {
        SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        String[] params = {"IndexFriendship.php", "function", "searchNewFriend", "email", email, "friendname", search};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
        updateUI();


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

        adapter = new FriendsListAdapter(getBaseContext(), friends, friendsListFragment, this, false, true);
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
}
