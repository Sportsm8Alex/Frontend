package com.example.alex.helloworld.Friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alex.helloworld.GroupDetailView;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.SlidingTabLayout.SlidingTabLayout;

import java.util.ArrayList;

public class Friends extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ViewPager.OnPageChangeListener, ClickListener {
    private ArrayList<Information> friends, groups;
    private FriendsListFragment friendsListFragment;
    private GroupsListFragment groupsListFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_selected_count;
    private ImageButton decline_selection, page_button;
    private Boolean addToMeetingMode, newGroupMode = false;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Friends", "Groups"};
    private int NumOfTabs = 2;

    Toolbar toolbar;
    AppBarLayout.LayoutParams params;
    AppBarLayout.LayoutParams params2;

    private ActionMode actionMode;
    private Friends.ActionModeCallBack actionModeCallBack = new Friends.ActionModeCallBack();
    private FriendsListAdapter adapterReference;
    private GroupListAdapter adapterReferenceGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Declarations Variables
        Bundle bundle = getIntent().getExtras();
        addToMeetingMode = bundle.getBoolean("SelectionMode");
        if (addToMeetingMode) {
            actionMode = startSupportActionMode(actionModeCallBack);
        }

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


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params2 = (AppBarLayout.LayoutParams) tabs.getLayoutParams();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discard_selection_button:
                textView_selected_count.setVisibility(View.GONE);
                decline_selection.setVisibility(View.GONE);
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                page_button.setImageResource(R.drawable.ic_person_add_white_24dp);
                break;
            case R.id.add_new_friend:
                if (newGroupMode) {
                    createGroup();
                } else if (!addToMeetingMode) {
                    Bundle b = new Bundle();
                    b.putBoolean("search", true);
                    Intent intent = new Intent(this, OnlyFriendsView.class);
                    intent.putExtras(b);
                    startActivity(intent);
                } else if (addToMeetingMode) {
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
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            textView_selected_count.setVisibility(View.GONE);
            decline_selection.setVisibility(View.GONE);
            newGroupMode = false;
        }
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
    public void onPageSelected(int position) {
        if (position == 1 && actionMode != null && !addToMeetingMode) {
            adapterReference.clearSelection();
            actionMode.finish();
            actionMode = null;
        }

    }

    public void setSwipeRefreshLayout(boolean bool) {
        swipeRefreshLayout.setRefreshing(bool);
    }

    @Override
    public void onItemClicked(int position, Boolean fromGroup) {
        if (!fromGroup) {
            if (actionMode != null && Integer.valueOf(friends.get(position).confirmed) == 1) {
                friends.get(position).selected ^= true;
                toggleSelection(position, fromGroup);
            }
        } else if (actionMode != null && fromGroup) {
            groups.get(position).selected ^= true;
            toggleSelection(position, fromGroup);
        }
    }

    @Override
    public boolean onItemLongClicked(int position, Boolean fromGroup) {
        if (actionMode == null && Integer.valueOf(friends.get(position).confirmed) == 1) {
            int i=0;
            while(Integer.valueOf(friends.get(i).confirmed)==0){
                i++;
            }
            adapterReference.removeRange(0,i);
            actionMode = startSupportActionMode(actionModeCallBack);
        }
        return false;
    }

    private void toggleSelection(int position, Boolean fromGroup) {
        if (!fromGroup) adapterReference.toggleSelection(position);
        else adapterReferenceGroup.toggleSelection(position);
        int count = adapterReference.getSelectedItemCount() + adapterReferenceGroup.getSelectedItemCount();
        if (count == 0 && !addToMeetingMode) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    public void setReferencesFriends(ArrayList<Information> friends, FriendsListFragment friendsListFragment, FriendsListAdapter adapter) {
        this.adapterReference = adapter;
        adapterReference.setClicklistener(this);
        this.friendsListFragment = friendsListFragment;
        this.friends = friends;
        if(addToMeetingMode){
            int i=0;
            while(Integer.valueOf(friends.get(i).confirmed)==0){
                i++;
            }
            adapter.removeRange(0,i);
        }
    }

    public void setReferencesGroups(ArrayList<Information> groups, GroupsListFragment groupsListFragment, GroupListAdapter adapter) {
        this.adapterReferenceGroup = adapter;
        this.adapterReferenceGroup.setSelectionMode(addToMeetingMode);
        this.adapterReferenceGroup.setClickListener(this);
        this.groups = groups;
        this.groupsListFragment = groupsListFragment;
    }


    private class ActionModeCallBack implements android.support.v7.view.ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
                    if (addToMeetingMode) {
                        finishSelection();
                    } else {
                        createGroup();
                    }
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (addToMeetingMode) {
                finish();
            }
            adapterReference.clearSelection();
            actionMode = null;
        }
    }

//Unused Override Methods
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
