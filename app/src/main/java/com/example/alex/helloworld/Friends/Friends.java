package com.example.alex.helloworld.Friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.helloworld.DisplayWeekActivity.DisplayWeekActivity;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.AsyncResponse;
import com.example.alex.helloworld.databaseConnection.DBconnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {
    DBconnection dBconnection;
    EditText editText;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        editText = (EditText) findViewById(R.id.search_friends);
        listView = (ListView) findViewById(R.id.friends_listview);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateFriendsList("Korbi@Korbi.de");
        onClickListener();

    }
    private void onClickListener(){
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     String string =(String) listView.getItemAtPosition(i);
               editText.setText(string);

           }
       });

    }

    private void updateFriendsList(String email) {
        String[] params = {"/IndexFriendship.php", "callID", "3", "email", email};
        dBconnection = (DBconnection) new DBconnection(new AsyncResponse() {
            @Override
            public void processFinish(String output) throws ParseException, JSONException {
                parseToArrayList(output);
            }
        }).execute(params);


    }

    private ArrayList<Information> parseToArrayList(String jsonObjectSring) throws JSONException {
        ArrayList<Information> data = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonObjectSring);

        listView.setAdapter(arrayAdapter);
        int i = 0;
        while (jsonObject.has("" + i)) {
            String meetingString = jsonObject.get("" + i).toString();
            Gson gson = new Gson();
            Information current = gson.fromJson(meetingString, Information.class);
            data.add(current);
            arrayAdapter.add(current.friend);
            i++;
        }


        return data;

    }
    private void updateListView(ArrayList<Information> arrayList){
        ArrayAdapter<Information> arrayAdapter = new ArrayAdapter<Information>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

}
