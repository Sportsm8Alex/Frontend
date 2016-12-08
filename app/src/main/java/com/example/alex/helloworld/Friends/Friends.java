package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.SlidingTabLayout.SlidingTabLayout;

import java.util.ArrayList;

public class Friends extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ViewPager.OnPageChangeListener {
    private ArrayList<Information> friends, groups;
    private FriendsListFragment friendsListFragment;
    private GroupsListFragment groupsListFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_selected_count;
    private ImageButton decline_selection,page_button;
    private Boolean selectionMode, newGroupMode = false;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Friends", "Groups"};
    private int NumOfTabs = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Declarations Variables
        Bundle bundle = getIntent().getExtras();
        selectionMode = bundle.getBoolean("SelectionMode");
        friends = new ArrayList<>();
        groups = new ArrayList<>();
        //Declarations Views
        page_button = (ImageButton) findViewById(R.id.add_new_friend);
        textView_selected_count = (TextView) findViewById(R.id.selected_friends_number);
        decline_selection = (ImageButton) findViewById(R.id.discard_selection_button);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.friends_refresh);
        //Hiding Keyboard on Startup
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Setting OnRefreshListener
        swipeRefreshLayout.setOnRefreshListener(this);
        //Setting up ViewPager
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NumOfTabs);
        pager.addOnPageChangeListener(this);
        pager.setAdapter(viewPagerAdapter);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getBaseContext(), R.color.colorAccent);
            }
        });
        tabs.setViewPager(pager);
        page_button.setImageResource(R.drawable.ic_person_add_white_24dp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discard_selection_button:
                textView_selected_count.setVisibility(View.GONE);
                decline_selection.setVisibility(View.GONE);
                newGroupMode = false;
                page_button.setImageResource(R.drawable.ic_person_add_white_24dp);
                friendsListFragment.declineSelection();
                break;
            case R.id.add_new_friend:
                if (newGroupMode) {
                    createGroup();
                } else if (!selectionMode) {
                    Intent intent = new Intent(this, SearchNewFriends.class);
                    startActivity(intent);
                } else if (selectionMode) {
                    finishSelection();
                }
                break;
        }
    }

    private void finishSelection() {

        ArrayList<Information> selectionfriends = new ArrayList<>();
        ArrayList<Information> selectiongroups = new ArrayList<>();
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).selected) {
                selectionfriends.add(friends.get(i));
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).selected) {
                selectiongroups.add(groups.get(i));
            }
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("partyList", selectionfriends);
        bundle.putSerializable("groupList", selectiongroups);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void createGroup() {

        ArrayList<Information> selection = new ArrayList<>();
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).selected) {
                selection.add(friends.get(i));
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("GroupList", selection);
        Intent intent = new Intent(this, CreateGroup.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void activateSelectionMode(Boolean bool, int count) {

        textView_selected_count.setVisibility(View.VISIBLE);
        textView_selected_count.setText(getString(R.string.text_selected, count));
        decline_selection.setVisibility(View.VISIBLE);
        page_button.setImageResource(R.drawable.ic_group_add_white_24dp);
        newGroupMode = bool;
    }

    public void updateCount(int count) {
        textView_selected_count.setText(getString(R.string.text_selected, count));
    }

    public void setSwipeRefreshLayout(Boolean bool) {
        swipeRefreshLayout.setRefreshing(bool);
    }

    public void setReferenceFriendsList(FriendsListFragment friendsListFragment) {
        this.friendsListFragment = friendsListFragment;
    }

    public void setReferenceGroupList(GroupsListFragment groupsListFragment) {
        this.groupsListFragment = groupsListFragment;
    }

    public boolean getSelectionMode() {
        return selectionMode;
    }

    public void setDataFriends(ArrayList<Information> data) {
        friends = data;
    }

    public void setDataGroups(ArrayList<Information> data) {
        groups = data;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        friendsListFragment.updateFriendsList();
        groupsListFragment.updateGroupList();
        textView_selected_count.setVisibility(View.GONE);
        decline_selection.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position==1) {
            page_button.setImageResource(R.drawable.ic_person_add_white_24dp);
            if(newGroupMode) {
                friendsListFragment.removeSelection();
                textView_selected_count.setVisibility(View.GONE);
                decline_selection.setVisibility(View.GONE);
                newGroupMode = false;
            }

        }else if(position ==2){
            page_button.setImageResource(R.drawable.ic_group_add_white_24dp);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
