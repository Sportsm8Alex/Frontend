package com.example.alex.helloworld;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class GamePickerLiga extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int hour_a, minute_a, day_a, month_a, year_a, numP;
    int sportart_ID = -1;
    Button b;
    Boolean start, end, date, num, ea;
    Project project=new Project();
    String extraInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_picker_liga);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (width * .8));
    }


    public void cancel(View view) {
        finish();
    }

    public void ok(View view) {
        finish();
    }

    public void beginn(View v) {

        Calendar c = Calendar.getInstance();
        hour_a = c.get(Calendar.HOUR);
        minute_a = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour_a, minute_a, true);
        timePickerDialog.show();
        b = (Button) v;
        ea = true;
        start = true;
    }

    public void ende(View v) {

        Calendar c = Calendar.getInstance();
        hour_a = c.get(Calendar.HOUR);
        minute_a = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour_a, minute_a, true);
        timePickerDialog.show();
        ea = false;
        end = true;
        b = (Button) v;
    }

    public void datum(View v) {

        Calendar c = Calendar.getInstance();
        year_a = c.get(Calendar.YEAR);
        month_a = c.get(Calendar.MONTH);
        day_a = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year_a, month_a, day_a);
        datePickerDialog.show();
        date = true;
        b = (Button) v;

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        year_a = year;
        month_a = month;
        day_a = dayOfMonth;
        project.day = day_a;
        project.month = month_a;
        project.year = year_a;
        b.setText(day_a + "." + month_a + "." + year_a);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        NumberFormat f = new DecimalFormat("00");
        hour_a = hourOfDay;
        minute_a = minute;
        if (ea) {
            project.begin_m = minute_a;
            project.begin_h = hour_a;
        } else {
            project.end_m = minute_a;
            project.end_h = hour_a;
        }
        b.setText(f.format(hour_a) + ":" + f.format(minute_a));
    }
}