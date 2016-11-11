package com.example.alex.helloworld;

/**
 * Created by alex on 10/30/2016.
 */

import android.content.Intent;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Sport extends AppCompatActivity {

    ArrayList<SportAttributes> attributes;

    Spinner mySpinner;
    int sportID;

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

        createList();
        navigationView();
        initSpinner();
    }


    public void funGame(View v) {
        Bundle b = new Bundle();
        b.putInt("sportID", sportID);
        b.putBoolean("liga", false);
        Intent intent = new Intent(this, Gamepicker.class);
        startActivity(intent);
    }

    public void ligaGame(View v) {
        Bundle b = new Bundle();
        b.putInt("sportID", sportID);
        b.putBoolean("liga", true);
        Intent intent = new Intent(this, GamePickerLiga.class);
        startActivity(intent);
    }

    public void training(View v) {
        Bundle b = new Bundle();
        b.putInt("sportID", sportID);
    }
    private void initSpinner(){
        mySpinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapater = new ArrayAdapter<String>(Sport.this, R.layout.custom_spinner_item, getResources().getStringArray(R.array.names));
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

    private void update(int sID) {

        Button training = (Button) findViewById(R.id.button_training);
        Button fSpiel = (Button) findViewById(R.id.button_funGame);
        Button lSpiel = (Button) findViewById(R.id.button_lSpiel);
        Button nochEinButton = (Button) findViewById(R.id.nochEinButton);
        ImageView iV = (ImageView) findViewById(R.id.imageView_sportart);

        SportAttributes temp = attributes.get(sID);
        iV.setImageResource(temp.draw);

        fSpiel.setVisibility(View.GONE);
        lSpiel.setVisibility(View.GONE);
        training.setVisibility(View.GONE);

        if (temp.funGame)
            fSpiel.setVisibility(View.VISIBLE);
        else
            fSpiel.setVisibility(View.GONE);

        if (temp.ligaGame)
            lSpiel.setVisibility(View.VISIBLE);
        else
            lSpiel.setVisibility(View.GONE);

        if (temp.training)
            training.setVisibility(View.VISIBLE);
        else
            training.setVisibility(View.GONE);

    }

    private void createList() {
        attributes = new ArrayList<SportAttributes>();
        SportAttributes beachen = new SportAttributes();
        beachen.name = "Beachvolleyball";
        beachen.funGame = true;
        beachen.ligaGame = true;
        beachen.training = false;
        beachen.nochEinButton = true;
        beachen.draw = R.drawable.beachen;
        attributes.add(beachen);

        SportAttributes soccer = new SportAttributes();
        beachen.name = "Fußball";
        soccer.funGame = true;
        soccer.ligaGame = true;
        soccer.training = false;
        soccer.nochEinButton = true;
        soccer.draw = R.drawable.soccer;
        attributes.add(soccer);

        SportAttributes run = new SportAttributes();
        beachen.name = "Laufen";
        run.funGame = false;
        run.ligaGame = false;
        run.training = true;
        run.nochEinButton = true;
        run.draw = R.drawable.run;
        attributes.add(run);

        String file_name = "save_local";

        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            fileOutputStream.write(beachen.name.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

}
