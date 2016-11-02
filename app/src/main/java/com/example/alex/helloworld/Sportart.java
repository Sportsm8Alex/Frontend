package com.example.alex.helloworld;

/**
 * Created by alex on 10/30/2016.
 */
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Sportart extends AppCompatActivity {

    ArrayList<SportAttributes> attributes;

    Spinner mySpinner;
    String[] beach = {"1", "8", "145", "4", "5"};
    String[] fuss = {"0", "7", "125", "8", "2"};
    Button training;
    Button fSpiel;
    Button lSpiel;
    Button nochEinButton;
    RelativeLayout rangLayout;
    RelativeLayout gelaufen;
    int sportID;

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
                Snackbar.make(view, "Ich bin ein Nutzloser Button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mySpinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapater = new ArrayAdapter<String>(Sportart.this, R.layout.custom_spinner_item, getResources().getStringArray(R.array.names));
        myAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapater);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Sportart.this, mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //update(mySpinner.getSelectedItem().toString());
                update(position);
                sportID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        training = (Button) findViewById(R.id.button_training);
        fSpiel = (Button) findViewById(R.id.button_funGame);
        lSpiel = (Button) findViewById(R.id.button_lSpiel);
        nochEinButton = (Button) findViewById(R.id.nochEinButton);
        rangLayout = (RelativeLayout) findViewById(R.id.layout_top);
        gelaufen = (RelativeLayout) findViewById(R.id.layout_laufen);

        createList();
    }

    public void funGame(View v) {
        Bundle b = new Bundle();
        b.putInt("sportID",sportID);
        b.putBoolean("liga",false);
        Intent intent = new Intent(this, Gamepicker.class);
        startActivity(intent);
    }

    public void ligaGame(View v){
        Bundle b = new Bundle();
        b.putInt("sportID",sportID);
        b.putBoolean("liga",true);
        Intent intent = new Intent(this, GamePickerLiga.class);
        startActivity(intent);
    }
    public void training(View v){
        Bundle b = new Bundle();
        b.putInt("sportID",sportID);
    }

    private void update(int sID) {
        TextView siege = (TextView) findViewById(R.id.textview_siege);
        TextView niederlage = (TextView) findViewById(R.id.textiview_niederlage);
        TextView rang = (TextView) findViewById(R.id.textview_rang2);
        TextView weltweit = (TextView) findViewById(R.id.textview_weltweit);
        ImageView iV = (ImageView)findViewById(R.id.imageView_sportart);

        SportAttributes temp = attributes.get(sID);
        iV.setImageResource(temp.draw);

        fSpiel.setVisibility(View.GONE);
        lSpiel.setVisibility(View.GONE);
        training.setVisibility(View.GONE);

        if(temp.funGame)
            fSpiel.setVisibility(View.VISIBLE);
        else
            fSpiel.setVisibility(View.GONE);

        if(temp.ligaGame)
            lSpiel.setVisibility(View.VISIBLE);
        else
            lSpiel.setVisibility(View.GONE);

        if(temp.training)
            training.setVisibility(View.VISIBLE);
        else
            training.setVisibility(View.GONE);

    }

    private void createList(){
        attributes=new ArrayList<SportAttributes>();
        SportAttributes beachen=new SportAttributes();
        beachen.funGame=true;
        beachen.ligaGame=true;
        beachen.training=false;
        beachen.nochEinButton=true;
        beachen.draw=R.drawable.beachen;
        attributes.add(beachen);

        SportAttributes soccer=new SportAttributes();
        soccer.funGame=true;
        soccer.ligaGame=true;
        soccer.training=false;
        soccer.nochEinButton=true;
        soccer.draw=R.drawable.soccer;
        attributes.add(soccer);

        SportAttributes run=new SportAttributes();
        run.funGame=false;
        run.ligaGame=false;
        run.training=true;
        run.nochEinButton=true;
        run.draw=R.drawable.run;
        attributes.add(run);

    }


}
