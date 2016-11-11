package com.example.alex.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;

/**
 * Created by alex on 10/30/2016.
 */

public class Home extends AppCompatActivity {

    int anzahl = 2;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        buttons();

       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
       mDrawerLayout.addDrawerListener(mToggle);
       mToggle.syncState();

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void buttons() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double width = size.x;
        width = width * 0.93;
        int widthh = (int) width;
        int height = size.y;
        Button sport = (Button) findViewById(R.id.sportart_button);
        Button einlad = (Button) findViewById(R.id.einladungen_button);
        Button msg = (Button) findViewById(R.id.msg_button);
        Button calendar = (Button) findViewById(R.id.calendar_button);
        sport.setWidth(widthh / 2);
        sport.setHeight(widthh / 2);

        einlad.setWidth(widthh / 2);
        einlad.setHeight(widthh / 2);

        msg.setWidth(widthh / 2);
        msg.setHeight(widthh / 2);

        calendar.setWidth(widthh / 2);
        calendar.setHeight(widthh / 2);
    }

    public void sportart(View view) {
        Intent intent = new Intent(this, Sportart.class);
        startActivity(intent);
    }

    public void einladungen(View view) {
        Intent intent = new Intent(this, Einladungen.class);
        startActivity(intent);
    }
    public void calendar(View view){
        Intent intent = new Intent(this, DisplayWeekActivity.class);
        startActivity(intent);
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                Home.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}