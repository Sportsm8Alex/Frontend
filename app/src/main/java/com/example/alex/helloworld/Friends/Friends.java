package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.LoginScreen;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.SlidingTabLayout.SlidingTabLayout;
import com.example.alex.helloworld.databaseConnection.DBconnection;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Friends extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private DBconnection dBconnection;
    private EditText editText;
    private ArrayList<String> members;
    private ArrayList<Information> friends;
    private ArrayList<Information> selected;
    RecyclerView recyclerView;
    FriendsListAdapter adapter;

    ViewPager pager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Friends", "Groups"};
    int NumOfTabs = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSelection();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editText = (EditText) findViewById(R.id.search_friends);
        members = new ArrayList<>();
        selected = new ArrayList<>();
        friends = new ArrayList<>();
        //updateFriendsList("Korbi@Korbi.de");

        SearchView searchView = (SearchView) findViewById(R.id.search_view_friends);
        searchView.setOnQueryTextListener(this);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NumOfTabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(viewPagerAdapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getBaseContext(), R.color.colorAccent);
            }
        });
        tabs.setViewPager(pager);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void finishSelection() {
        for (Iterator<Information> iterator = friends.iterator(); iterator.hasNext();) {
            Information info = iterator.next();
            if (!info.selected) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("partyList", friends);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setData(ArrayList<Information> data) {
        friends = data;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null) {
            int pos = adapter.search(newText);
            recyclerView.scrollToPosition(pos);
            return true;
        }
        return false;

    }

}
