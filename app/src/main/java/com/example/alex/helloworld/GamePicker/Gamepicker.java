package com.example.alex.helloworld.GamePicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import com.example.alex.helloworld.Friends.Friends;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.AsyncResponse;
import com.example.alex.helloworld.databaseConnection.DBconnection;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Korbi on 22.10.2016.
 */

public class Gamepicker extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TextWatcher {
    int numP = 4, minHours = 2;
    Button SelectedButton;

    int sportart_ID = -1;
    ArrayList<Information> Selection = new ArrayList<>();
    private EditText additionalInfos;
    private Boolean endOrStart;
    private MutableDateTime startTime, endTime;
    private DateTime datetime;
    private DateTimeFormatter formatter;
    private String extraInfoString;
    private TextView text_minHour;
    private TextView text_minParti;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_picker);
        endOrStart = false;

        //Popup größe
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * .8), (int) (width * .8));

        //sportartID
        Bundle b = getIntent().getExtras();
        if (b != null) {
            sportart_ID = b.getInt("sportID");
        }
        formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        datetime = new DateTime();
        startTime = new MutableDateTime();
        startTime.set(DateTimeFieldType.secondOfMinute(), 0);
        startTime.set(DateTimeFieldType.year(), 0);
        endTime = new MutableDateTime();
        endTime.set(DateTimeFieldType.secondOfMinute(), 0);
        endTime.set(DateTimeFieldType.year(), 0);
        additionalInfos = (EditText) findViewById(R.id.editText_additional);
        additionalInfos.setSingleLine();
        additionalInfos.addTextChangedListener(this);
        text_minHour = (TextView) findViewById(R.id.textview_num_hour);
        text_minHour.setText(minHours + "");
        text_minParti = (TextView) findViewById(R.id.textview_num);
        text_minParti.setText(numP + "");


    }

    public void timeButtons(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, datetime.getHourOfDay(), datetime.getMinuteOfHour(), true);
        timePickerDialog.show();
        SelectedButton = (Button) view;
        switch (view.getId()) {
            case R.id.button_ende:
                endOrStart = false;
                break;
            case R.id.button_beginn:
                endOrStart = true;
                break;
        }

    }

    public void dateButton(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
        datePickerDialog.show();
        SelectedButton = (Button) view;

    }

    public void memberCountButton(View view) {
        Button minus = (Button) findViewById(R.id.button_minus);
        switch (view.getId()) {
            case R.id.button_plus:
                numP++;
                text_minParti.setText("" + numP);
                minus.setEnabled(true);
                break;
            case R.id.button_minus:
                if (numP > 0) {
                    numP--;
                    if (numP == 0) {
                        minus.setEnabled(false);
                    }
                }
                text_minParti.setText("" + numP);
                break;
        }
    }

    public void cancelButton(View v) {
        finish();
    }

    public void okButton(View v) {

        if (startTime.get(DateTimeFieldType.millisOfSecond()) == 0 && endTime.get(DateTimeFieldType.millisOfSecond()) == 0 && startTime.get(DateTimeFieldType.year()) != 0 && numP != 0) {
            if (startTime.isBefore(endTime)) {
                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("/IndexMeetings.php", "function", "newMeeting", "startTime", formatter.print(startTime), "endTime", formatter.print(endTime))
                );
                for (int i = 0; i < Selection.size(); i++) {
                    paramsArrayList.add("member" + i);
                    paramsArrayList.add(Selection.get(i).email);
                }
                String[] params = new String[paramsArrayList.size()];
                params = paramsArrayList.toArray(params);

                System.out.print("ha");

                new DBconnection(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) throws ParseException, JSONException {

                    }
                }).execute(params);
                finish();

            } else {
                Toast.makeText(this, R.string.setTimeWrong, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, R.string.text_empty_fields, Toast.LENGTH_SHORT).show();
        }

    }

    public void addPartiMembersButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SelectionMode", true);
        Intent intent = new Intent(this, Friends.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void minTimeButton(View view) {
        Button minus = (Button) findViewById(R.id.button_minus_hour);
        switch (view.getId()) {
            case R.id.button_plus_hour:
                minHours++;
                text_minHour.setText("" + minHours);
                minus.setEnabled(true);
                break;
            case R.id.button_minus_hour:
                if (minHours > 0) {
                    minHours--;
                    if (minHours == 0) {
                        minus.setEnabled(false);
                    }
                }
                text_minHour.setText("" + minHours);
                break;
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        startTime.setDate(year, month, dayOfMonth);
        endTime.setDate(year, month, dayOfMonth);
        SelectedButton.setText(dayOfMonth + "." + month + "." + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        NumberFormat f = new DecimalFormat("00");
        if (endOrStart) {
            startTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
            startTime.set(DateTimeFieldType.minuteOfHour(), minute);
            startTime.set(DateTimeFieldType.millisOfSecond(), 0);

        } else {
            endTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
            endTime.set(DateTimeFieldType.minuteOfHour(), minute);
            endTime.set(DateTimeFieldType.millisOfSecond(), 0);
        }
        SelectedButton.setText(f.format(hourOfDay) + ":" + f.format(minute));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String[] params = {"/IndexMeetings.php", "function", "newMeeting", "startTime", formatter.print(startTime), "endTime", formatter.print(endTime)};
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<Information>) bundle.getSerializable("partyList");
                TextView textView = (TextView) findViewById(R.id.number_added);
                textView.setText(Selection.size() + " Teilnehmer");
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        extraInfoString = additionalInfos.getText().toString();
        Toast.makeText(Gamepicker.this, extraInfoString, Toast.LENGTH_SHORT).show();
    }

}