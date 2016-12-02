package com.example.alex.helloworld;

/**
 * Created by alex on 10/30/2016.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.alex.helloworld.activities.AccountPage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Sport extends AppCompatActivity {

    ArrayList<SportAttributes> attributes;
    String JSON_STRING;
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
git 
        try {
            createList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        navigationView();


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


    private void initSpinner() {
        mySpinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapater = new ArrayAdapter<String>(Sport.this, R.layout.custom_spinner_item, sportArten);
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
        Resources res = getResources();
        TypedArray draws = res.obtainTypedArray(R.array.sportDrawables);
        iV.setImageDrawable(draws.getDrawable(temp.ID));

        fSpiel.setVisibility(View.GONE);
        lSpiel.setVisibility(View.GONE);
        training.setVisibility(View.GONE);

        if (temp.team == 1) {
            fSpiel.setVisibility(View.VISIBLE);
            lSpiel.setVisibility(View.VISIBLE);
            training.setVisibility(View.GONE);
        } else {
            fSpiel.setVisibility(View.GONE);
            lSpiel.setVisibility(View.GONE);
            training.setVisibility(View.VISIBLE);
        }

    }

    private void createList() throws ExecutionException, InterruptedException {

        SportID backgroundWorker = new SportID();
        backgroundWorker.execute("number");
        attributes = new ArrayList<>();
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

    private class SportID extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        URL url2 = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on the UI thread

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://10.0.2.2:8080/android_user_api/Backend/include/getSport.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("URL not found");
                return "exception";
            }

            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout((15000));
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                StringBuilder successString = new StringBuilder();
                String line;
                String success;

                while ((JSON_STRING = reader.readLine()) != null) {
                    successString.append(JSON_STRING);
                    System.out.println(successString.toString());
                }
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(successString.toString());
                JSONArray jArr = (JSONArray) json.get("sports");
                sportArten = new String[jArr.size()];
                for (int i = 0; i < jArr.size(); i++) {
                    JSONObject jSonO = (JSONObject) jArr.get(i);
                    Object name = jSonO.get("sportname");
                    Object team = jSonO.get("team");
                    Object sID = jSonO.get("sportID");
                    SportAttributes temp = new SportAttributes();
                    temp.name = name.toString();
                    temp.team = Integer.valueOf(team.toString());
                    temp.ID = Integer.valueOf(sID.toString());
                    attributes.add(temp);
                    sportArten[i] = name.toString();
                }

                initSpinner();
                return successString.toString().trim();


            } catch (IOException e2) {
                e2.printStackTrace();
                return "exception";
            } catch (ParseException e) {
                e.printStackTrace();
                return "expection";
            } finally {
                conn.disconnect();

            }
        }

        @Override
        protected void onPostExecute(String success) {

            Toast.makeText(Sport.this, "add", Toast.LENGTH_LONG).show();


        }
    }

}
