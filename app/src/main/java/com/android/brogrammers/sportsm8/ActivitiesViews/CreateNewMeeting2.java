package com.android.brogrammers.sportsm8.ActivitiesViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.brogrammers.sportsm8.SocialViews.SelectorContainer;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Korbi on 22.10.2016.
 */

public class CreateNewMeeting2 extends Activity implements TextWatcher, UIthread, View.OnClickListener {

    int minMemberCount = 4, minHours = 2;
    Button selectedButton;

    int sportart_ID = -1;
    ArrayList<Information> Selection = new ArrayList<>();
    ArrayList<Information> SelectionGroup = new ArrayList<>();
    private EditText additionalInfos, editTextChooseActivity;
    private Boolean start;
    private MutableDateTime startTime, endTime;
    private DateTime datetime,backUpStartTime,backUpEndTime;
    private DateTimeFormatter formatter;
    private Button startTime_b, endTime_b, date_b;
    private TextView minTimeTextView, minPartySizeTextView;
    private Button startTimeButton, endTimeButton, startDateButton, endDateButton, addFriendsButton;
    private String extraInfoString;
    private NumberPicker numHours, minMember;
    private Switch checkSwitch;
    private int step = 0;
    private int dynamic = 0;
    Boolean bBegin = false, bEnd = false, bDate = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting2);
        start = true;
        extraInfoString = "";

        //sportartID
        Bundle b = getIntent().getExtras();
        if (b != null) {
            sportart_ID = b.getInt("sportID");
        }
        if (sportart_ID == 8008) {
            findViewById(R.id.edittext_custom_meeting).setVisibility(View.VISIBLE);
        }
        formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        datetime = new DateTime();
        startTime = new MutableDateTime();
        endTime = new MutableDateTime();

        editTextChooseActivity = (EditText) findViewById(R.id.edittext_choose_activity);
        editTextChooseActivity.addTextChangedListener(this);

        startTimeButton = (Button) findViewById(R.id.button_begin_time);
        endTimeButton = (Button) findViewById(R.id.button_end_time);
        startDateButton = (Button) findViewById(R.id.button_begin_date);
        endDateButton = (Button) findViewById(R.id.button_end_date);
        addFriendsButton = (Button) findViewById(R.id.button_add_friends);
        minPartySizeTextView = (TextView) findViewById(R.id.textview_min_party_size);
        minTimeTextView = (TextView) findViewById(R.id.textview_min_meeting_time);

        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.save_meeting).setOnClickListener(this);
        findViewById(R.id.button_min_party_size).setOnClickListener(this);
        findViewById(R.id.button_min_meeting_time).setOnClickListener(this);
        startTimeButton.setOnClickListener(this);
        endTimeButton.setOnClickListener(this);
        startDateButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        addFriendsButton.setOnClickListener(this);
        checkSwitch = (Switch) findViewById(R.id.check_dynamic);
        checkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    startTimeButton.setText(startTime.toString("HH:00"));
                    endTimeButton.setText(endTime.toString("HH:00"));
                }else{
                    startTimeButton.setText(startTime.toString("HH:mm"));
                    endTimeButton.setText(endTime.toString("HH:mm"));
                }
            }
        });


        startDateButton.setText(datetime.toString("EE., dd. MMM. yyyy"));
        endDateButton.setText(datetime.toString("EE., dd. MMM. yyyy"));
        startTimeButton.setText(datetime.toString("HH:mm"));
        endTimeButton.setText(datetime.toString("HH:mm"));
        minTimeTextView.setText(minHours+"");
        minPartySizeTextView.setText(minMemberCount+"");

        //startCycle
       onClick(startTimeButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_begin_time:
                timeButtons(view);
                break;
            case R.id.button_end_time:
                timeButtons(view);
                break;
            case R.id.button_begin_date:
                dateButton(view);
                break;
            case R.id.button_end_date:
                dateButton(view);
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.save_meeting:
                createMeeting();
                break;
            case R.id.button_add_friends:
                Bundle bundle = new Bundle();
                bundle.putBoolean("SelectionMode", true);
                Intent intent = new Intent(this, SelectorContainer.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.button_min_meeting_time:
                createNumberPickerDialog("Wie viele Stunden?",minHours, 24, false);
                break;
            case R.id.button_min_party_size:
                createNumberPickerDialog("Ab wie vielen Leuten?",minMemberCount ,Selection.size(), true);
                break;
        }
    }

    private void createNumberPickerDialog(String message,int current ,int max, final Boolean partySize) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(0);
        numberPicker.setValue(current);
        numberPicker.setWrapSelectorWheel(false);
        builder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (partySize) {
                    minMemberCount = numberPicker.getValue();
                    minPartySizeTextView.setText(minMemberCount+"");
                    minPartySizeTextView.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.green));
                }else{
                    minHours = numberPicker.getValue();
                    minTimeTextView.setText(minHours+"");
                    minTimeTextView.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.green));
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setView(numberPicker).show();
    }

    public void timeButtons(View view) {
        switch (view.getId()) {
            case R.id.button_begin_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        startTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
                        startTime.set(DateTimeFieldType.minuteOfHour(), minute);
                        startTimeButton.setText(startTime.toString("HH:mm"));
                        if(checkSwitch.isChecked())startTimeButton.setText(startTime.toString("HH:00"));
                        bBegin=true;
                        if(step ==0){
                            step++;
                            onClick(endTimeButton);
                        }
                    }
                }, endTime.getHourOfDay(), endTime.getMinuteOfHour(), true).show();
                break;
            case R.id.button_end_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        endTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
                        endTime.set(DateTimeFieldType.minuteOfHour(), minute);
                        if(checkSwitch.isChecked()){}
                        endTimeButton.setText(endTime.toString("HH:mm"));
                        if(checkSwitch.isChecked())endTimeButton.setText(endTime.toString("HH:00"));
                        bEnd=true;
                        if(step ==1){
                            step++;
                            onClick(startDateButton);
                        }
                    }
                }, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true).show();
                break;
        }


    }

    public void dateButton(View view) {
        switch (view.getId()) {
            case R.id.button_begin_date:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        startTime.setDate(year, month + 1, dayOfMonth);
                        endTime.setDate(year, month + 1, dayOfMonth);
                        startDateButton.setText(endTime.toString("EE., dd. MMM. yyyy"));
                        endDateButton.setText(endTime.toString("EE., dd. MMM. yyyy"));
                        bDate=true;
                    }
                }, datetime.getYear(), datetime.getMonthOfYear() - 1, datetime.getDayOfMonth()).show();
                break;
            case R.id.button_end_date:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        startTime.setDate(year, month + 1, dayOfMonth);
                        endTime.setDate(year, month + 1, dayOfMonth);
                        endDateButton.setText(endTime.toString("EE., dd. MMM. yyyy"));
                    }
                }, datetime.getYear(), datetime.getMonthOfYear() - 1, datetime.getDayOfMonth()).show();
                break;
        }

    }


    public void cancelButton(View v) {
        finish();
    }

    public void createMeeting() {

        if (bBegin && bEnd && bDate && minMemberCount != 0&&minHours!=0) {
            if (startTime.isBefore(endTime)) {
                if (checkSwitch.isChecked()) {
                    dynamic = 1;
                    startTime.setMinuteOfHour(0);
                    endTime.setMinuteOfHour(0);
                }
                SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                String email = sharedPrefs.getString("email", "");
                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("IndexMeetings.php", "function", "newMeeting", "startTime", formatter.print(startTime), "endTime", formatter.print(endTime), "minPar", minMemberCount + "", "member", email, "activity", extraInfoString, "sportID", "" + sportart_ID, "dynamic", dynamic + "")
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
        addFriendsButton.setText(Selection.size() + "  Teilnehmer hinzugefügt");
        addFriendsButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));

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
        mergeGroupsAndFriends();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        extraInfoString = editTextChooseActivity.getText().toString();
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {

    }


}