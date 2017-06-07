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
import android.support.v4.content.ContextCompat;
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
import com.android.brogrammers.sportsm8.ActivitiesViews.CreateNewMeeting;
import com.android.brogrammers.sportsm8.CalendarViews.CalenderFragment;
import com.android.brogrammers.sportsm8.DebugScreen.DebugScreen;
import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.UserClasses.AccountPage;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.converter.gson.GsonConverterFactory;

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
    //to prevent crashes
    String JsonString = "[{\"MeetingID\":261,\"status\":0,\"dynamic\":0,\"minParticipants\":4,\"startTime\":\"2017-01-28 20:18:30\",\"endTime\":\"2017-01-28 21:18:30\",\"sportID\":0,\"meetingActivity\":\"\",\"hour1\":0,\"hour2\":0,\"hour3\":0,\"hour4\":0,\"hour5\":0,\"hour6\":0,\"hour7\":0,\"hour8\":0,\"hour9\":0,\"hour10\":0,\"hour11\":0,\"hour12\":0,\"hour13\":0,\"hour14\":0,\"hour15\":0,\"hour16\":0,\"hour17\":0,\"hour18\":0,\"hour19\":0,\"hour20\":0,\"hour21\":0,\"hour22\":0,\"hour23\":0,\"hour24\":0,\"hour25\":0,\"hour26\":0,\"hour27\":0,\"hour28\":0,\"hour29\":0,\"hour30\":0,\"hour31\":0,\"hour32\":0,\"hour33\":0,\"hour34\":0,\"hour35\":0,\"hour36\":0,\"hour37\":0,\"hour38\":0,\"hour39\":0,\"hour40\":0,\"hour41\":0,\"hour42\":0,\"hour43\":0,\"hour44\":0,\"hour45\":0,\"hour46\":0,\"hour47\":0,\"hour48\":0,\"hour49\":0,\"hour50\":0,\"hour51\":0,\"hour52\":0,\"hour53\":0,\"hour54\":0,\"hour55\":0,\"hour56\":0,\"hour57\":0,\"hour58\":0,\"hour59\":0,\"hour60\":0,\"hour61\":0,\"hour62\":0,\"hour63\":0,\"hour64\":0,\"hour65\":0,\"hour66\":0,\"hour67\":0,\"hour68\":0,\"hour69\":0,\"hour70\":0,\"hour71\":0,\"hour72\":0,\"hour73\":0,\"hour74\":0,\"hour75\":0,\"hour76\":0,\"hour77\":0,\"hour78\":0,\"hour79\":0,\"hour80\":0,\"hour81\":0,\"hour82\":0,\"hour83\":0,\"hour84\":0,\"hour85\":0,\"hour86\":0,\"hour87\":0,\"hour88\":0,\"hour89\":0,\"hour90\":0,\"hour91\":0,\"hour92\":0,\"hour93\":0,\"hour94\":0,\"hour95\":0,\"hour96\":0,\"begin\":0,\"duration\":0,\"confirmed\":1,\"timeArray\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},{\"MeetingID\":262,\"status\":0,\"dynamic\":0,\"minParticipants\":4,\"startTime\":\"2017-02-02 16:51:34\",\"endTime\":\"2017-02-02 17:51:34\",\"sportID\":0,\"meetingActivity\":\"\",\"hour1\":0,\"hour2\":0,\"hour3\":0,\"hour4\":0,\"hour5\":0,\"hour6\":0,\"hour7\":0,\"hour8\":0,\"hour9\":0,\"hour10\":0,\"hour11\":0,\"hour12\":0,\"hour13\":0,\"hour14\":0,\"hour15\":0,\"hour16\":0,\"hour17\":0,\"hour18\":0,\"hour19\":0,\"hour20\":0,\"hour21\":0,\"hour22\":0,\"hour23\":0,\"hour24\":0,\"hour25\":0,\"hour26\":0,\"hour27\":0,\"hour28\":0,\"hour29\":0,\"hour30\":0,\"hour31\":0,\"hour32\":0,\"hour33\":0,\"hour34\":0,\"hour35\":0,\"hour36\":0,\"hour37\":0,\"hour38\":0,\"hour39\":0,\"hour40\":0,\"hour41\":0,\"hour42\":0,\"hour43\":0,\"hour44\":0,\"hour45\":0,\"hour46\":0,\"hour47\":0,\"hour48\":0,\"hour49\":0,\"hour50\":0,\"hour51\":0,\"hour52\":0,\"hour53\":0,\"hour54\":0,\"hour55\":0,\"hour56\":0,\"hour57\":0,\"hour58\":0,\"hour59\":0,\"hour60\":0,\"hour61\":0,\"hour62\":0,\"hour63\":0,\"hour64\":0,\"hour65\":0,\"hour66\":0,\"hour67\":0,\"hour68\":0,\"hour69\":0,\"hour70\":0,\"hour71\":0,\"hour72\":0,\"hour73\":0,\"hour74\":0,\"hour75\":0,\"hour76\":0,\"hour77\":0,\"hour78\":0,\"hour79\":0,\"hour80\":0,\"hour81\":0,\"hour82\":0,\"hour83\":0,\"hour84\":0,\"hour85\":0,\"hour86\":0,\"hour87\":0,\"hour88\":0,\"hour89\":0,\"hour90\":0,\"hour91\":0,\"hour92\":0,\"hour93\":0,\"hour94\":0,\"hour95\":0,\"hour96\":0,\"begin\":0,\"duration\":0,\"confirmed\":0,\"timeArray\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]";


    ArrayList<Information> arrayListMeetings;

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
                        fragment = new AccountPage();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        appBarLayout.setExpanded(true);
                        startDate.setVisibility(View.GONE);
                        setLocation.setVisibility(View.GONE);
                        textView.setText("My Account");
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
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                CalenderFragment calenderFragment2 = (CalenderFragment) fragment;
//                calenderFragment2.setStartDate(date.getYear(), date.getMonth(), date.getDay());
//                calendarView.setVisibility(View.GONE);
//                expanded = false;
//            }
//        });
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
        calenderFragment.toggleCalendar();

//        DateTime today = new DateTime();
//        DatePickerDialog dPD = new DatePickerDialog().newInstance(this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
//        ArrayList<Calendar> highlights = new ArrayList<>();
//        for (int i = 0; i < arrayListMeetings.size(); i++) {
//            String x = arrayListMeetings.get(i).startTime;
//            DateTime dateTime = arrayListMeetings.get(i).getStartDateTime();
//            Calendar date1 = dateTime.toCalendar(Locale.getDefault());
//            highlights.add(date1);
//        }
//        Calendar[] days = highlights.toArray(new Calendar[highlights.size()]);
//        dPD.setHighlightedDays(days);
//        dPD.show(getFragmentManager(), "DatePicker");
    }

    @OnClick(R.id.fab_calendar)
    public void createNewMeeting() {
        CalenderFragment calenderFragment2 = (CalenderFragment) fragment;
        DateTime dateTime = calenderFragment2.getSelectedDate().toDateTime();
        Intent intent = new Intent(this, CreateNewMeeting.class);
        intent.putExtra("date", dateTime.toString("dd/MM/yyyy"));
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
            locationON=false;
            setLocation.setImageResource(R.drawable.ic_location_off_white_24dp);
            CalenderFragment c1 = (CalenderFragment) fragment;
            c1.setLocation(0,0,false);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng coord = place.getLatLng();
                longitude = coord.longitude;
                latitude = coord.latitude;
                CalenderFragment c1 = (CalenderFragment) fragment;
                c1.setLocation(longitude, latitude,true);
                locationON=true;
                setLocation.setImageResource(R.drawable.ic_location_on_white_24dp);
            }
        }
    }
}

