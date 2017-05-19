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
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alex on 10/30/2016.
 */

public class MainActivity extends AppCompatActivity implements CalenderFragment2.OnFragmentInteractionListener, AccountPage.OnFragmentInteractionListener,ActivitiesFragment.OnFragmentInteractionListener, FragmentSocial.OnFragmentInteractionListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private BottomNavigationView bottomNavigationView;

    private boolean tempCalendar= false;  //to prevent crashes

    private ImageButton imageButtonToolbar;
    private TextView textView;

    @BindView(R.id.fab_calendar)
    FloatingActionButton floatingActionButton;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        imageButtonToolbar = (ImageButton) findViewById(R.id.image_button_toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        navigationView();
        //buttons();
        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new AccountPage();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                switch (tabId) {
                    //not correct way to do
                    // don't start new activities
                    case R.id.tab_account:
                        tempCalendar=false;
                        floatingActionButton.setVisibility(View.GONE);
                        fragment = new ActivitiesFragment();
                        imageButtonToolbar.animate()
                                .scaleX(0)
                                .scaleY(0)
                                .alpha(0.0f);
                        imageButtonToolbar.setVisibility(View.GONE);
                        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                        appBarLayout.setExpanded(true);
                        findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                        break;
                    case R.id.tab_calendar:
                        if(!tempCalendar) {
                            tempCalendar=true;
                            floatingActionButton.setVisibility(View.VISIBLE);
                            fragment = new CalenderFragment2();
                            imageButtonToolbar.animate()
                                    .scaleX(0)
                                    .scaleY(0)
                                    .alpha(0.0f);
                            imageButtonToolbar.setVisibility(View.GONE);
                            findViewById(R.id.spinner2).setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Kalender");
                        }
                        break;
                    case R.id.tab_friends:
                        tempCalendar=false;
                        floatingActionButton.setVisibility(View.GONE);
                        fragment = new FragmentSocial();
                        imageButtonToolbar.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .alpha(1.0f);
                        imageButtonToolbar.setVisibility(View.VISIBLE);
                        findViewById(R.id.spinner2).setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Freunde");
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).commit();
            }
        });


        //bottom navigation
        /*bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            //not correct way to do
                            // don't start new activities
                            case R.id.bottom_navigation_sports:
                                tempCalendar=false;
                                floatingActionButton.setVisibility(View.GONE);
                                fragment = new AccountPage();
                                imageButtonToolbar.animate()
                                        .scaleX(0)
                                        .scaleY(0)
                                        .alpha(0.0f);
                                imageButtonToolbar.setVisibility(View.GONE);
                                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                                appBarLayout.setExpanded(true);
                                findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
                                textView.setVisibility(View.GONE);
                                break;
                            case R.id.bottom_navigation_calender:
                                if(!tempCalendar) {
                                    tempCalendar=true;
                                    floatingActionButton.setVisibility(View.VISIBLE);
                                    fragment = new CalenderFragment2();
                                    imageButtonToolbar.animate()
                                            .scaleX(0)
                                            .scaleY(0)
                                            .alpha(0.0f);
                                    imageButtonToolbar.setVisibility(View.GONE);
                                    findViewById(R.id.spinner2).setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText("Kalender");
                                }
                                break;
                            case R.id.bottom_navigation_friends:
                                tempCalendar=false;
                                floatingActionButton.setVisibility(View.GONE);
                                fragment = new FragmentSocial();
                                imageButtonToolbar.animate()
                                        .scaleX(1)
                                        .scaleY(1)
                                        .alpha(1.0f);
                                imageButtonToolbar.setVisibility(View.VISIBLE);
                                findViewById(R.id.spinner2).setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Freunde");
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment).commit();
                        return true;
                    }
                }
        );*/
    }




    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.image_button_toolbar:
                Bundle b = new Bundle();
                b.putBoolean("search", true);
                intent = new Intent(this, OnlyFriendsView.class);
                intent.putExtras(b);
                startActivity(intent);
                break;
            default:
                finish();
        }
    }

    @OnClick(R.id.fab_calendar)
    public void createNewMeeting(){
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


}

