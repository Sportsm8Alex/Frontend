package com.android.brogrammers.sportsm8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.ActivitiesViews.ActivitiesFragment;
import com.android.brogrammers.sportsm8.ActivitiesViews.CreateNewMeeting;
import com.android.brogrammers.sportsm8.CalendarViews.CalenderFragment;
import com.android.brogrammers.sportsm8.CalendarViews.MeetingDetailMVP.MeetingDetailActivity;
import com.android.brogrammers.sportsm8.DebugScreen.DebugScreen;
import com.android.brogrammers.sportsm8.SocialFeed.SocialFeedFragment.SocialFeedActivity;
import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.UserClasses.AccountPage;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by alex on 10/30/2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CalenderFragment.OnFragmentInteractionListener, AccountPage.OnFragmentInteractionListener, ActivitiesFragment.OnFragmentInteractionListener, FragmentSocial.OnFragmentInteractionListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private BottomNavigationView bottomNavigationView;
    private double longitude, latitude;
    private boolean locationON = false;

    List<Information> arrayListMeetings;

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
    @BindView(R.id.setLocation)
    ImageButton setLocation;

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
        fragment = new FragmentSocial();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        setLocation.setOnClickListener(this);
        getNumberOfUnanswered();
        bottomBar.selectTabAtPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                switch (tabId) {
                    //not correct way to do
                    // don't start new activities
                    case R.id.tab_account:
                        floatingActionButton.setVisibility(View.GONE);
                       // fragment = new AccountPage();
                        fragment = new SocialFeedActivity();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        appBarLayout.setExpanded(true);
                        startDate.setVisibility(View.GONE);
                        setLocation.setVisibility(View.GONE);
                        textView.setText("Match Feed");
                        break;
                    case R.id.tab_calendar:
                        floatingActionButton.setVisibility(View.VISIBLE);
                        fragment = new CalenderFragment();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        setLocation.setVisibility(View.VISIBLE);
                        startDate.setVisibility(View.VISIBLE);
                        textView.setText("Kalender");
                        break;
                    case R.id.tab_friends:
                        floatingActionButton.setVisibility(View.GONE);
                        fragment = new FragmentSocial();

                        setLocation.setVisibility(View.GONE);
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
                        ((CalenderFragment) fragment).scrollTo(0);
                        break;
                    default:
                        break;
                }


            }
        });
        AppUpdater appUpdater = new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("http://sportsm8.bplaced.net/Update/update.json")
                .setDisplay(Display.DIALOG)
                .showAppUpdated(true);
        appUpdater.start();
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
        CalenderFragment calenderFragment = (CalenderFragment) fragment;
        calenderFragment.toggleView(startDate);
    }

    @OnClick(R.id.fab_calendar)
    public void createNewMeeting() {
        CalenderFragment calenderFragment2 = (CalenderFragment) fragment;
        DateTime dateTime = calenderFragment2.getSelectedDate().toDateTime();
        Intent intent = new Intent(this, CreateNewMeeting.class);
        intent.putExtra("date", dateTime.toString("dd/MM/yyyy"));
        startActivityForResult(intent, 3);
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
        CalenderFragment calenderFragment2 = (CalenderFragment) fragment;
        calenderFragment2.setStartDate(year, monthOfYear, dayOfMonth);
    }


    @Override
    public void onClick(View v) {
        if (!locationON) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), 1);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
            locationON = false;
            setLocation.setImageResource(R.drawable.ic_location_off_white_24dp);
            CalenderFragment c1 = (CalenderFragment) fragment;
            c1.setLocation(0, 0, false);
            c1.toggleView(setLocation);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode < 5) {
            CalenderFragment c1 = (CalenderFragment) fragment;
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    LatLng coord = place.getLatLng();
                    longitude = coord.longitude;
                    latitude = coord.latitude;
                    c1.setLocation(longitude, latitude, true);
                    locationON = true;
                    setLocation.setImageResource(R.drawable.ic_location_on_white_24dp);
                    c1.toggleView(setLocation);
                    c1.setFilterText(place.getAddress());
                }
            } else if (requestCode == 2 || resultCode == 3) {
                if (resultCode == RESULT_OK) {
                    c1.onRefresh();
                }
            }
        }

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File update = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "update.apk");
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(update);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}

