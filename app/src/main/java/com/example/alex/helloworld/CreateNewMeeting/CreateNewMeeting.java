package com.example.alex.helloworld.CreateNewMeeting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.alex.helloworld.Friends.OnlyFriendsView;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Korbi on 22.10.2016.
 */

public class CreateNewMeeting extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TextWatcher, UIthread {

    int minMemberCount = 4, minHours = 2;
    Button SelectedButton;

    int sportart_ID = -1;
    ArrayList<Information> Selection = new ArrayList<>();
    ArrayList<Information> SelectionGroup = new ArrayList<>();
    private EditText additionalInfos;
    private Boolean endOrStart;
    private MutableDateTime startTime, endTime;
    private DateTime datetime;
    private DateTimeFormatter formatter;
    private Button startTime_b, endTime_b, date_b;
    private String extraInfoString;
    private NumberPicker numHours, minMember;
    private int step = 0;
    Boolean bBegin = false, bEnd = false, bDate = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_meeting);
        endOrStart = false;
        extraInfoString = "";

        //Popup größe
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * .9), (int) (width * .9));

        //sportartID
        Bundle b = getIntent().getExtras();
        if (b != null) {
            sportart_ID = b.getInt("sportID");
        }
        formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        datetime = new DateTime();
        startTime = new MutableDateTime();
        endTime = new MutableDateTime();
        additionalInfos = (EditText) findViewById(R.id.editText_additional);
        additionalInfos.setSingleLine();
        additionalInfos.addTextChangedListener(this);
        startTime_b = (Button) findViewById(R.id.button_beginn);
        endTime_b = (Button) findViewById(R.id.button_ende);
        date_b = (Button) findViewById(R.id.button_datum);

        numHours = (NumberPicker) findViewById(R.id.numberPicker_hours);
        minMember = (NumberPicker) findViewById(R.id.numberPicker_minMember);
        numHours.setMinValue(0);
        numHours.setMaxValue(24);
        numHours.setValue(minHours);
        numHours.setWrapSelectorWheel(false);
        minMember.setMinValue(0);
        minMember.setMaxValue(100);
        minMember.setValue(minMemberCount);
        minMember.setWrapSelectorWheel(false);
        SelectedButton = (Button) findViewById(R.id.button_beginn);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, datetime.getHourOfDay(), datetime.getMinuteOfHour(), true);
        timePickerDialog.show();

    }

    public void timeButtons(View view) {
        TimePickerDialog timePickerDialog;
        SelectedButton = (Button) view;
        switch (view.getId()) {
            case R.id.button_ende:
                step = 1;
                timePickerDialog = new TimePickerDialog(this, this, endTime.getHourOfDay(), endTime.getMinuteOfHour(), true);
                timePickerDialog.show();
                endOrStart = false;
                break;
            case R.id.button_beginn:
                step = 0;
                timePickerDialog = new TimePickerDialog(this, this, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true);
                timePickerDialog.show();
                endOrStart = true;
                break;
        }


    }

    public void dateButton(View view) {
        int x = datetime.getMonthOfYear();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, datetime.getYear(), datetime.getMonthOfYear() - 1, datetime.getDayOfMonth());
        datePickerDialog.show();
        SelectedButton = (Button) view;

    }


    public void cancelButton(View v) {
        finish();
    }

    public void okButton(View v) {
        minMemberCount = minMember.getValue();
        minHours = numHours.getValue();

        if (bBegin && bEnd && bDate && startTime.get(DateTimeFieldType.year()) != 0 && minMemberCount != 0) {
            if (startTime.isBefore(endTime)) {

                SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                String email = sharedPrefs.getString("email", "");
                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("IndexMeetings.php", "function", "newMeeting", "startTime", formatter.print(startTime), "endTime", formatter.print(endTime), "minPar", minMemberCount + "", "member", email, "activity", extraInfoString, "sportID", "" + sportart_ID)
                );
                for (int i = 0; i < Selection.size(); i++) {
                    paramsArrayList.add("member" + i);
                    paramsArrayList.add(Selection.get(i).email);
                }
                String[] params = new String[paramsArrayList.size()];
                params = paramsArrayList.toArray(params);

                Database db = new Database(this, getBaseContext());
                db.execute(params);
                Toast.makeText(getBaseContext(), "Neues Meeting erstellt", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, R.string.setTimeWrong, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, R.string.text_empty_fields, Toast.LENGTH_SHORT).show();
        }

    }

    public void mergeGroupsAndFriends() {
        ArrayList<Information> temp = new ArrayList<>();
        for (int i = 0; i < SelectionGroup.size(); i++) {
            String[] params = {"IndexGroups.php", "function", "getGroupMembers", "GroupID", SelectionGroup.get(0).GroupID};
            Database db = new Database(this, getBaseContext());
            db.execute(params);

            SharedPreferences sharedPrefs = getSharedPreferences("IndexGroups", Context.MODE_PRIVATE);
            String meetingJson = sharedPrefs.getString("IndexGroupsgetGroupMembersJSON", "");
            try {
                temp = Database.jsonToArrayList(meetingJson);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < temp.size(); j++) {
                Boolean tempBool = false;
                int tempInt = 0;
                for (int h = 0; h < Selection.size(); h++) {
                    if (temp.get(j).email.equals(Selection.get(h).email)) {
                        tempBool = true;
                    }
                }
                if (!tempBool) {
                    Selection.add(temp.get(j));
                }
            }

        }
        TextView textView = (TextView) findViewById(R.id.number_added);
        textView.setText(Selection.size() + " Teilnehmer");

    }

    public void addPartiMembersButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SelectionMode", true);
        Intent intent = new Intent(this, OnlyFriendsView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        bDate = true;
        startTime.setDate(year, month + 1, dayOfMonth);
        endTime.setDate(year, month + 1, dayOfMonth);
        SelectedButton.setText(endTime.toString("dd.MM.YYYY"));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (endOrStart) {
            bBegin = true;
            startTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
            startTime.set(DateTimeFieldType.minuteOfHour(), minute);
            SelectedButton.setText(startTime.toString("HH:mm"));

        } else {
            bEnd = true;
            endTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
            endTime.set(DateTimeFieldType.minuteOfHour(), minute);
            SelectedButton.setText(endTime.toString("HH:mm"));
        }
        if (step == 0) {
            step++;
            endOrStart = false;
            SelectedButton = (Button) findViewById(R.id.button_ende);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, datetime.getHourOfDay(), datetime.getMinuteOfHour(), true);
            timePickerDialog.show();
        } else if (step == 1) {
            step++;
            dateButton(findViewById(R.id.button_datum));
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<Information>) bundle.getSerializable("partyList");
                SelectionGroup = (ArrayList<Information>) bundle.getSerializable("groupList");

            }
        }
        TextView textView = (TextView) findViewById(R.id.number_added);
        textView.setText(Selection.size() + " Teilnehmer");
        //mergeGroupsAndFriends();
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
        Toast.makeText(CreateNewMeeting.this, extraInfoString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {

    }
}