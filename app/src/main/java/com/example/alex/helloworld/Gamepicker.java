package com.example.alex.helloworld;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Korbi on 22.10.2016.
 */

public class Gamepicker extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    int hour_a, minute_a, day_a, month_a, year_a, numP;
    int sportart_ID = -1;
    Button b;
    Boolean start, end, date, num, ea;
    Project project;
    String extraInfo;
    NumberPicker noPicker = null;
    EditText extraInfos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_picker);
        project = new Project();
        start = false;
        end = false;
        date = false;
        ea = false;
        num = false;

        //NumPicker
        noPicker = (NumberPicker)findViewById(R.id.numberpicker_fun);
        noPicker.setMaxValue(100);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(false);

        //Popup größe
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (width * .8));
        //sportartID
        Bundle b = getIntent().getExtras();
        if (b != null) {
            sportart_ID = b.getInt("sportart");
        }
        project.id = sportart_ID;
        additionalInfos();

    }

    public void beginn(View v) {

        Calendar c = Calendar.getInstance();
        hour_a = c.get(Calendar.HOUR_OF_DAY);
        minute_a = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour_a, minute_a, true);
        timePickerDialog.show();
        b = (Button) v;
        ea = true;
        start = true;
    }

    public void ende(View v) {

        Calendar c = Calendar.getInstance();
        hour_a = c.get(Calendar.HOUR_OF_DAY);
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

    private void additionalInfos(){
        extraInfos = (EditText) findViewById(R.id.editText_additional);
        extraInfos.setSingleLine();
        extraInfos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String extra = extraInfos.getText().toString();
               // /project.extraInfo = extraInfos.getText().toString();

            }
        });
    }

    @Override
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

    public void cancel(View v) {
        finish();
    }

    public void plus(View v) {
        numP++;
        String s = String.valueOf(numP);
        TextView textView = (TextView) findViewById(R.id.textview_num);
        textView.setText(s);
        Button temp = (Button) findViewById(R.id.button_minus);
        temp.setEnabled(true);
        project.numParti = numP;
    }

    public void minus(View v) {
        if (numP > 0) {
            numP--;
            if(numP==0){
                Button temp = (Button) findViewById(R.id.button_minus);
                temp.setEnabled(false);
            }
        }
        String s = String.valueOf(numP);
        project.numParti = numP;
        TextView textView = (TextView) findViewById(R.id.textview_num);
        textView.setText(s);

    }

    public void ok(View v) {
        int tempBe = project.begin_h * 60 + project.begin_m;
        int tempEn = project.end_h * 60 + project.end_m;
        Toast.makeText(this, project.extraInfo, Toast.LENGTH_SHORT).show();

        if (date && start && end && project.numParti > 0) {
            if (tempBe - tempEn < 0) {

                //fertiges Projekt in Datenbank übertragen
                //pushProject(project);
                finish();

            } else {
                Toast.makeText(this, "Zeit falsch eingestellt", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Nicht alle Felder ausgefüllt", Toast.LENGTH_SHORT).show();
        }

    }

}
