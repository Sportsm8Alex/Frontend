package com.example.alex.helloworld;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Korbi on 22.10.2016.
 */

public class Pop extends Activity {
    Button button_stdp;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;
    int hour_a = 0;
    int minute_a = 0;
    int year_a;
    int month_a;
    int day_a;
    Button b;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup1);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.3));

    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_a = hourOfDay;
            minute_a=minute;
            b.setText(hour_a+":"+minute_a);
            if(minute_a<10){
                b.setText(hour_a+":0"+minute_a);
            }
        }
    };

    protected DatePickerDialog.OnDateSetListener kDateTimeListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_a = year;
            month_a = month+1;
            day_a = dayOfMonth;
            b.setText(day_a+"."+month_a);
        }
    };

    public Dialog onCreateDialog(int id) {
        switch(id){
            case DIALOG_ID:
                TimePickerDialog tp = new TimePickerDialog(this, kTimePickerListner, hour_a, minute_a, true);
                return tp;
            case DIALOG_ID2:
                return new DatePickerDialog(this, kDateTimeListner, year_a, month_a, day_a);
        }
        return null;
    }

    public void beginn(View v){
        showDialog(DIALOG_ID);
        b = (Button) v;
    }

    public void datum(View v){
        showDialog(DIALOG_ID2);
        b= (Button) v;
    }

    public void ok(View v){
        finish();
    }

}
