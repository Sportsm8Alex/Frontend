package com.example.alex.helloworld.CreateNewMeeting;

/**
 * Created by alex on 10/30/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.alex.helloworld.Z_Unused_Inactive.AddSport;
import com.example.alex.helloworld.Home;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.Z_Unused_Inactive.Invites;
import com.example.alex.helloworld.UserClasses.LoginScreen;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.Z_Unused_Inactive.SportAttributes;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;
import com.example.alex.helloworld.activities.AccountPage;

import org.json.simple.parser.ParseException;
import org.json.JSONException;

import java.util.ArrayList;

public class Sport extends AppCompatActivity implements UIthread {


    ArrayList<Information> sportIDs;
    ArrayList<SportAttributes> attributes;
    Spinner mySpinner;
    int sportID;
    String[] sportArten;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Sport.this, AddSport.class);
                startActivity(intent);
            }
        });
        navigationView();
        updateUI();


    }

    public void onClick(View view) {
        createList();
    }


    public void funGame(View v) {
        Bundle b = new Bundle();
        b.putInt("sportID", sportID);
        b.putBoolean("liga", false);
        Intent intent = new Intent(this, CreateNewMeeting.class);
        intent.putExtras(b);
        startActivity(intent);
    }


    private void initSpinner() {
        if (sportArten != null) {
            mySpinner = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<String> myAdapater = new ArrayAdapter<>(Sport.this, R.layout.item_custom_spinner, sportArten);
            myAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(myAdapater);

            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //update(mySpinner.getSelectedItem().toString());
                    update(position);
                    sportID = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void update(int sID) {

        Button training = (Button) findViewById(R.id.button_training);
        Button fSpiel = (Button) findViewById(R.id.button_funGame);
        ImageView iV = (ImageView) findViewById(R.id.imageView_sportart);

        Information temp = sportIDs.get(sID);
        Resources res = getResources();
        TypedArray draws = res.obtainTypedArray(R.array.sportDrawables);
        iV.setImageDrawable(draws.getDrawable(Integer.parseInt(temp.sportID)));

        fSpiel.setVisibility(View.GONE);
        training.setVisibility(View.GONE);

        if (Integer.valueOf(sportIDs.get(sID).team) == 1) {
            fSpiel.setVisibility(View.VISIBLE);
            training.setVisibility(View.GONE);
        } else {
            fSpiel.setVisibility(View.GONE);
            training.setVisibility(View.VISIBLE);
        }

    }

    private void createList() {
        String[] params = {"IndexSports.php", "function", "getData"};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
        updateUI();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void updateUI() {
        SharedPreferences sharedPrefs = getSharedPreferences("IndexSports", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexSportsgetDataJSON", "");
        try {
            sportIDs = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        if (sportIDs != null) {
            sportArten = new String[sportIDs.size()];
            for (int j = 0; j < sportIDs.size(); j++) {
                sportArten[j] = sportIDs.get(j).sportname;
            }
        }
        initSpinner();
    }

    //Implements Navigation View
    private void navigationView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView.getMenu().getItem(3).setChecked(true);

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
                        intent = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_invite:
                        intent = new Intent(getApplicationContext(), Invites.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_sportart:
                        intent = new Intent(getApplicationContext(), Sport.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;
                    case R.id.nav_home:
                        intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        closeNavDrawer();
                        return true;

                }

                return Sport.super.onOptionsItemSelected(item);
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
    public void updateUI(String answer) {

    }

}
