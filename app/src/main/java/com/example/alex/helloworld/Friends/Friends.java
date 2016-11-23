package com.example.alex.helloworld.Friends;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.AsyncResponse;
import com.example.alex.helloworld.databaseConnection.DBconnection;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Friends extends AppCompatActivity {
    private DBconnection dBconnection;
    private EditText editText;
    private FriendsListAdapter friendsListAdapter;
    private ArrayList<String> members;
    private ArrayList<Information> friends;
    private ArrayList<Information> selected;
    RecyclerView recyclerView;
    FriendsListAdapter adapter;

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
                finishSelection();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView = (RecyclerView) findViewById(R.id.friendsRview);

        editText = (EditText) findViewById(R.id.search_friends);
        members = new ArrayList<>();
        selected = new ArrayList<>();
        friends = new ArrayList<>();
        updateFriendsList("Korbi@Korbi.de");

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

        int i = 0;
        while (jsonObject.has("" + i)) {
            String meetingString = jsonObject.get("" + i).toString();
            Gson gson = new Gson();
            Information current = gson.fromJson(meetingString, Information.class);
            data.add(current);
            i++;
        }
        adapter = new FriendsListAdapter(Friends.this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Friends.this));
        friends = data;
        return data;

    }

    private void finishSelection() {

        for(int i = 0;i<4;i++){
            if(recyclerView.getChildAt(i).isSelected()){
               selected.add(friends.get(i));
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("partyList",selected);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }


}
