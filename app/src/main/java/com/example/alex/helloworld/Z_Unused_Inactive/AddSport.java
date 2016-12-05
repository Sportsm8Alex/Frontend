package com.example.alex.helloworld.Z_Unused_Inactive;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.helloworld.R;

import java.util.Arrays;

/**
 * Created by Korbi on 02.11.2016.
 */

public class AddSport extends Activity{

    String[] sportarten;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sportart);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (width * .8));

        sportarten=getResources().getStringArray(R.array.sportarten);
        Arrays.sort(sportarten);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,sportarten );

        final ListView listView = (ListView)findViewById(R.id.listview_addspo);
        listView.setAdapter(adapter);

        EditText search = (EditText) findViewById(R.id.editText_new_group);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) listView.getItemAtPosition(i);


                Toast.makeText(AddSport.this, selected, Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }



}
