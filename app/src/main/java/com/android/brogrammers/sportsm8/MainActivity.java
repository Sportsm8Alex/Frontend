package com.android.brogrammers.sportsm8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.ActivitiesViews.ActivitiesFragment;
import com.android.brogrammers.sportsm8.ActivitiesViews.CreateNewMeeting2;
import com.android.brogrammers.sportsm8.CalendarViews.CalenderFragment2;
import com.android.brogrammers.sportsm8.DebugScreen.DebugScreen;
import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.UserClasses.AccountPage;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alex on 10/30/2016.
 */

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, CalenderFragment2.OnFragmentInteractionListener, AccountPage.OnFragmentInteractionListener, ActivitiesFragment.OnFragmentInteractionListener, FragmentSocial.OnFragmentInteractionListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private BottomNavigationView bottomNavigationView;
    //to prevent crashes

    ArrayList<Information> arrayListMeetings;
    DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");

    @BindView(R.id.fab_calendar)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;
    @BindView(R.id.change_start_date)
    ImageButton startDate;
    @BindView(R.id.image_button_toolbar)
    ImageButton imageButtonToolbar;
    @BindView(R.id.toolbar_title)
    TextView textView;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        navigationView();

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new AccountPage();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        getNumberOfUnanswered();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                switch (tabId) {
                    //not correct way to do
                    // don't start new activities
                    case R.id.tab_account:
                        floatingActionButton.setVisibility(View.GONE);
                        fragment = new AccountPage();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        appBarLayout.setExpanded(true);
                        startDate.setVisibility(View.GONE);
                        textView.setText("My Account");
                        break;
                    case R.id.tab_calendar:
                        floatingActionButton.setVisibility(View.VISIBLE);
                        fragment = new CalenderFragment2();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        startDate.setVisibility(View.VISIBLE);
                        textView.setText("Kalender");
                        break;
                    case R.id.tab_friends:
                        floatingActionButton.setVisibility(View.GONE);
                        fragment = new FragmentSocial();
                        imageButtonToolbar.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .alpha(1.0f);
                        imageButtonToolbar.setVisibility(View.VISIBLE);
                        startDate.setVisibility(View.GONE);
                        textView.setText("Freunde");
                        break;
                }
                getNumberOfUnanswered();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).commit();
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_calendar:
                        ((CalenderFragment2) fragment).scrollTo(0);
                        break;
                    default:
                        break;
                }


            }
        });

    }

    @OnClick(R.id.image_button_toolbar)
    public void searchNewFriends() {
        Bundle b = new Bundle();
        b.putBoolean("search", true);
        Intent intent = new Intent(this, OnlyFriendsView.class);
        intent.putExtras(b);
        startActivity(intent);

    }

    @OnClick(R.id.change_start_date)
    public void changeStartDate() {
        DateTime today = new DateTime();
        DatePickerDialog dPD = new DatePickerDialog().newInstance(this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
        ArrayList<Calendar> highlights = new ArrayList<>();
        for (int i = 0; i < arrayListMeetings.size(); i++) {
            String x = arrayListMeetings.get(i).startTime;
            DateTime dateTime = formatter.parseDateTime(arrayListMeetings.get(i).startTime);
            Calendar date1 = dateTime.toCalendar(Locale.getDefault());
            highlights.add(date1);
        }
        Calendar[] days = highlights.toArray(new Calendar[highlights.size()]);
        dPD.setHighlightedDays(days);
        dPD.show(getFragmentManager(), "DatePicker");
    }

    @OnClick(R.id.fab_calendar)
    public void createNewMeeting() {
        Bundle b = new Bundle();
        //   b.putInt("sportID", Integer.valueOf(sportIDs.get(sportID).sportID));
        Intent intent = new Intent(this, CreateNewMeeting2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MainActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        switch (id) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Implements Navigation View
    private void navigationView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationView.getMenu().getItem(1).setChecked(true);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {

                    case R.id.nav_account:
                        intent = new Intent(getApplicationContext(), AccountPage.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_logout:
                        //delete locally saved userinformation
                        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.clear();
                        editor.apply();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();

                        intent = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_home:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_debug:
                        intent = new Intent(getApplicationContext(), DebugScreen.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_settings:
                        return true;
                }
                return MainActivity.super.onOptionsItemSelected(item);
            }
        });
    }

    public void getNumberOfUnanswered() {
        arrayListMeetings = new ArrayList<>();
        SharedPreferences sharedPrefs = getSharedPreferences("IndexMeetings", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexMeetingsgetMeetingJSON", "");
        try {
            arrayListMeetings = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        int x = 0;
        for (int i = 0; i < arrayListMeetings.size(); i++) {
            if (arrayListMeetings.get(i).confirmed != 1) {
                x++;
            }
        }
        BottomBarTab calendar = bottomBar.getTabAtPosition(1);
        calendar.setBadgeCount(x);
    }


    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        CalenderFragment2 calenderFragment2 = (CalenderFragment2) fragment;
        calenderFragment2.setStartDate(year, monthOfYear, dayOfMonth);
    }
}

