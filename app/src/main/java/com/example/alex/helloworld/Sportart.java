package com.example.alex.helloworld;

/**
 * Created by alex on 10/30/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sportart extends AppCompatActivity {

    Toolbar myToolbar;
    Spinner mySpinner;
    String[] beach = {"1", "8", "145", "4", "5"};
    String[] fuss = {"0", "7", "125", "8", "2"};
    Button training;
    Button fSpiel;
    Button lSpiel;
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
        rangLayout = (RelativeLayout) findViewById(R.id.layout_top);
        gelaufen = (RelativeLayout) findViewById(R.id.layout_laufen);
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
        if (sID==0) {
            training.setVisibility(View.GONE);
            fSpiel.setVisibility(View.VISIBLE);
            lSpiel.setVisibility(View.VISIBLE);
            siege.setText(beach[1]);
            niederlage.setText(beach[2]);
            rang.setText(beach[3]);
            weltweit.setText(beach[4]);
            rangLayout.setVisibility(RelativeLayout.VISIBLE);

            gelaufen.setVisibility(RelativeLayout.GONE);
        } else if (sID==1) {
            training.setVisibility(View.GONE);
            fSpiel.setVisibility(View.VISIBLE);
            lSpiel.setVisibility(View.VISIBLE);
            siege.setText(fuss[1]);
            niederlage.setText(fuss[2]);
            rang.setText(fuss[3]);
            weltweit.setText(fuss[4]);
            gelaufen.setVisibility(RelativeLayout.GONE);
            rangLayout.setVisibility(RelativeLayout.VISIBLE);

        } else if (sID==2) {
            training.setVisibility(View.VISIBLE);
            fSpiel.setVisibility(View.GONE);
            lSpiel.setVisibility(View.GONE);
            rangLayout.setVisibility(RelativeLayout.GONE);
            gelaufen.setVisibility(RelativeLayout.VISIBLE);
        }
    }


}
