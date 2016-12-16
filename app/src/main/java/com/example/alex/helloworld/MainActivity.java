package com.example.alex.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.alex.helloworld.ActivitiesViews.ActivitiesFragment;
import com.example.alex.helloworld.CalendarViews.CalenderFragment;
import com.example.alex.helloworld.SocialViews.FragmentSocial;
import com.example.alex.helloworld.SocialViews.friends.OnlyFriendsView;
import com.example.alex.helloworld.UserClasses.LoginScreen;
import com.example.alex.helloworld.UserClasses.AccountPage;

/**
 * Created by alex on 10/30/2016.
 */

public class MainActivity extends AppCompatActivity implements CalenderFragment.OnFragmentInteractionListener, ActivitiesFragment.OnFragmentInteractionListener, FragmentSocial.OnFragmentInteractionListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private ImageButton imageButtonToolbar;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        imageButtonToolbar = (ImageButton) findViewById(R.id.image_button_toolbar);
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
        fragment = new ActivitiesFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        //bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
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
                                fragment = new ActivitiesFragment();
                                imageButtonToolbar.animate()
                                        .scaleX(0)
                                        .scaleY(0)
                                        .alpha(0.0f);
                                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                                appBarLayout.setExpanded(true);
                                findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
                                break;
                            case R.id.bottom_navigation_calender:
                                fragment = new CalenderFragment();
                                imageButtonToolbar.animate()
                                        .scaleX(0)
                                        .scaleY(0)
                                        .alpha(0.0f);
                                findViewById(R.id.spinner2).setVisibility(View.GONE);
                                break;
                            case R.id.bottom_navigation_friends:
                                fragment = new FragmentSocial();
                                imageButtonToolbar.animate()
                                        .scaleX(1)
                                        .scaleY(1)
                                        .alpha(1.0f);
                                findViewById(R.id.spinner2).setVisibility(View.GONE);
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment).commit();

                        return true;
                    }
                }
        );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                return true;
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

                        intent = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_sportart:
                        return true;
                    case R.id.nav_home:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        closeNavDrawer();
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

