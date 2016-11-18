package com.example.alex.helloworld.DisplayWeekActivity;

/**
 * Created by agemcipe on 31.10.16.
 */

import android.icu.text.IDNA;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.helloworld.Data;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.Meeting;
import com.example.alex.helloworld.MyCustomAdapter;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.Sport;
import com.example.alex.helloworld.databaseConnection.AsyncResponse;
import com.example.alex.helloworld.databaseConnection.DBconnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by agemcipe on 21.10.16.
 */

public class DisplayWeekActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    private int daySelected;
    private int colorPrimary;
    private int colorAccent;

    Button[] weekDayButtons = new Button[7];
    final static String DEBUG_TAG = "Yolo: ";
    private float x1, x2;
    RecyclerView recyclerView;
    MyCustomAdapter adapter;
    DBconnection dBconnection;

    ArrayList<Information> data = new ArrayList<>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_week);
        colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        colorAccent = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
        weekDayButtons[0] = (Button) findViewById(R.id.mondayButton);
        weekDayButtons[1] = (Button) findViewById(R.id.tuesdayButton);
        weekDayButtons[2] = (Button) findViewById(R.id.wednesdayButton);
        weekDayButtons[3] = (Button) findViewById(R.id.thursdayButton);
        weekDayButtons[4] = (Button) findViewById(R.id.fridayButton);
        weekDayButtons[5] = (Button) findViewById(R.id.saturdayButton);
        weekDayButtons[6] = (Button) findViewById(R.id.sundayButton);

        initButtons(weekDayButtons);
    }

    private void update() throws ExecutionException, InterruptedException, MalformedURLException {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        String meetingID = "1";   //Value from somewhere
        String[] params = {"/IndexMeetings.php", "function", "getMeeting", "meetingID", meetingID};
        dBconnection = (DBconnection) new DBconnection(new AsyncResponse() {
            @Override
            public void processFinish(String output) throws ParseException, JSONException {
                //data = Data.getCalendar(output);
                data = jsonToArrayList(output);
                adapter = new MyCustomAdapter(DisplayWeekActivity.this, data);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DisplayWeekActivity.this));

            }
        }).execute(params);


    }

    private ArrayList<Information> jsonToArrayList(String jsonObjectSring) throws JSONException, ParseException {

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


        return data;

    }


    /**
     * function that changes the BackgroundResource of an array of Buttons expect the
     * the Button at index j
     *
     * @param Buttons the array of Buttons
     * @param j       the index {@code int} of the button that does not change the background color
     * @param k       the int value of the color
     */
    private void setBackground(Button[] Buttons, int j, int k) {
        for (int i = 0; i < Buttons.length; i++) {
            if (i != j) {
                Buttons[i].setBackgroundColor(k);
            }
        }
    }

    /**
     * function to initialize Buttons with onClickListener
     *
     * @param Buttons the array of Buttons
     */
    private void initButtons(Button[] Buttons) {
        Calendar c = Calendar.getInstance();
        daySelected = ((c.get(Calendar.DAY_OF_WEEK) - 2) % 7 + 7) % 7;
        for (int i = 0; i < Buttons.length; i++) {
            Buttons[i].setOnClickListener(this);
            Buttons[i].setBackgroundColor(colorPrimary);
        }
        Buttons[daySelected].setBackgroundColor(colorAccent);
    }


    /**
     * overriden onClick Method for weekDayButtons
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mondayButton:
                daySelected = 0;
                break;

            case R.id.tuesdayButton:
                daySelected = 1;
                break;

            case R.id.wednesdayButton:
                daySelected = 2;
                try {
                    update();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.thursdayButton:
                daySelected = 3;
                break;

            case R.id.fridayButton:
                daySelected = 4;
                break;

            case R.id.saturdayButton:
                daySelected = 5;
                break;

            case R.id.sundayButton:
                daySelected = 6;
                break;

            default:
                break;
        }
        weekDayButtons[daySelected].setBackgroundColor(colorAccent);
        setBackground(weekDayButtons, daySelected, colorPrimary);
    }

    /**
     * overriden onTouchEvent function for swipe detection
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                x1 = event.getX();
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                x2 = event.getX();


                if (x1 < x2) {
                    //left to right swipe
                    daySelected--;
                    if (daySelected < 0) daySelected = 0;
                    Log.d(DEBUG_TAG, "Left to Right Swipe detected");

                } else if (x1 > x2) {
                    //right to left swipe
                    daySelected++;
                    if (daySelected > 6) daySelected = 6;
                    Log.d(DEBUG_TAG, "Right to Left Swipe detected");
                }
                weekDayButtons[daySelected].setBackgroundColor(colorAccent);
                setBackground(weekDayButtons, daySelected, colorPrimary);

                Log.d(DEBUG_TAG, "Action was UP");

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * AsyncResponse needs this, but doesnt use it here...
     *
     * @param output
     */
    @Override
    public void processFinish(String output) {

    }
}
