package com.example.alex.helloworld;

/**
 * Created by agemcipe on 31.10.16.
 */

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

/**
 * Created by agemcipe on 21.10.16.
 */
public class DisplayWeekActivity extends AppCompatActivity implements View.OnClickListener {
    private int daySelected;
    private int colorPrimary ;
    private int colorAccent ;

    Button[] weekDayButtons = new Button[7];
    final static String DEBUG_TAG = "Yolo: ";
    private float x1, x2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_week);
        colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        colorAccent =  ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
        weekDayButtons[0] = (Button) findViewById(R.id.mondayButton);
        weekDayButtons[1] = (Button) findViewById(R.id.tuesdayButton);
        weekDayButtons[2] = (Button) findViewById(R.id.wednesdayButton);
        weekDayButtons[3] = (Button) findViewById(R.id.thursdayButton);
        weekDayButtons[4] = (Button) findViewById(R.id.fridayButton);
        weekDayButtons[5] = (Button) findViewById(R.id.saturdayButton);
        weekDayButtons[6] = (Button) findViewById(R.id.sundayButton);

        initButtons(weekDayButtons);
    }


    /**
     * function that changes the BackgroundResource of an array of Buttons expect the
     * the Button at index j
     * @param Buttons the array of Buttons
     * @param j the index {@code int} of the button that does not change the background color
     * @param k the int value of the color
     */
    private void setBackground(Button[] Buttons, int j, int k ){
        for(int i = 0; i < Buttons.length; i++){
            if(i != j){
                Buttons[i].setBackgroundColor(k);
            }
        }
    }

    /**
     * function to initialize Buttons with onClickListener
     * @param Buttons the array of Buttons
     */
    private void initButtons(Button[] Buttons){
        Calendar c = Calendar.getInstance();
        daySelected = ((c.get(Calendar.DAY_OF_WEEK)-2)%7+7)%7;
        for(int i = 0; i < Buttons.length; i++) {
            Buttons[i].setOnClickListener(this);
            Buttons[i].setBackgroundColor(colorPrimary);
        }
        Buttons[daySelected].setBackgroundColor(colorAccent);
    }


    /**
     * overriden onClick Method for weekDayButtons
     * @param v
     */
    @Override
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.mondayButton:
                daySelected = 0;
                break;

            case R.id.tuesdayButton:
                daySelected = 1;
                break;

            case R.id.wednesdayButton:
                daySelected = 2;
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
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");
                x1 = event.getX();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                x2 = event.getX();


                if(x1 < x2){
                    //left to right swipe
                    daySelected--;
                    if(daySelected < 0) daySelected = 0;
                    Log.d(DEBUG_TAG, "Left to Right Swipe detected");

                } else if( x1 > x2){
                    //right to left swipe
                    daySelected++;
                    if(daySelected > 6) daySelected = 6;
                    Log.d(DEBUG_TAG, "Right to Left Swipe detected");
                }
                weekDayButtons[daySelected].setBackgroundColor(colorAccent);
                setBackground(weekDayButtons, daySelected, colorPrimary);

                Log.d(DEBUG_TAG,"Action was UP");

                return true;
            default :
                return super.onTouchEvent(event);
        }
    }



}
