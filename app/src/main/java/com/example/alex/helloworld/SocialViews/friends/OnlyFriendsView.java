package com.example.alex.helloworld.SocialViews.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.alex.helloworld.databaseConnection.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.SocialViews.ClickListener;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

public class OnlyFriendsView extends AppCompatActivity implements UIthread, SearchView.OnQueryTextListener, ClickListener {

    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private ArrayList<Information> friends;
    private Boolean search = false;
    private Toolbar toolbar;
    private ActionMode actionMode;
    private OnlyFriendsView.ActionModeCallBack actionModeCallBack = new OnlyFriendsView.ActionModeCallBack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        search = b.getBoolean("search");
        ImageButton addMembers = (ImageButton) findViewById(R.id.image_button_add_groupmembers);
        SearchView searchView = (SearchView) findViewById(R.id.searchview_new_friends);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        recyclerView = (RecyclerView) findViewById(R.id.add_new_friend_recyclerview);
        friends = new ArrayList<>();
        adapter = new FriendsListAdapter(getBaseContext(), null, friends, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        if (!search) {
            searchView.setVisibility(View.GONE);
            addMembers.setVisibility(View.VISIBLE);
            actionMode = startSupportActionMode(actionModeCallBack);
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

    private void getFriends() {
        SharedPreferences sharedPrefs = getSharedPreferences("IndexFriendship", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexFriendshipgetFriendsJSON", "");
        try {
            friends = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        Iterator<Information> itr = friends.iterator();
        while (itr.hasNext()) {
            Information info = itr.next();
            if (Integer.valueOf(info.getConfirmed()) == 0) {
                itr.remove();
            }
        }
        adapter = new FriendsListAdapter(getBaseContext(), this, friends, false);
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

        adapter = new FriendsListAdapter(getBaseContext(), this, friends, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
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


    @Override
    public void onItemClicked(int position, Boolean fromGroup) {
        friends.get(position).selected ^= true;
        toggleSelection(position, false);
    }

    private void toggleSelection(int position, Boolean fromGroup) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        actionMode.setTitle(String.valueOf(count));
        actionMode.invalidate();
    }


    private class ActionModeCallBack implements android.support.v7.view.ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_add:
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
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            finish();
        }


    }


    @Override
    public boolean onItemLongClicked(int position, Boolean fromGroup) {
        return false;
    }

}
