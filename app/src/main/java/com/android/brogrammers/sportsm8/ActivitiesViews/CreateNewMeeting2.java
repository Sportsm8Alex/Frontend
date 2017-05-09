package com.android.brogrammers.sportsm8.ActivitiesViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.SelectorContainer;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by Korbi on 22.10.2016.
 */

public class CreateNewMeeting2 extends Activity implements TextWatcher, android.support.v7.widget.SearchView.OnQueryTextListener, UIthread {

    int minMemberCount = 4, minHours = 2;
    Button selectedButton;


    int sportart_ID = -1;
    ArrayList<Information> Selection = new ArrayList<>();
    ArrayList<Information> SelectionGroup = new ArrayList<>();
    private Boolean start;
    private MutableDateTime startTime, endTime;
    private DateTime datetime, backUpStartTime, backUpEndTime;
    private DateTimeFormatter formatter;

    @BindView(R.id.edittext_choose_activity)
    SearchView editTextChooseActivity;
    @BindView(R.id.textview_min_meeting_time)
    TextView minTimeTextView;
    @BindView(R.id.textview_min_party_size)
    TextView minPartySizeTextView;
    @BindView(R.id.button_begin_time)
    Button startTimeButton;
    @BindView(R.id.button_end_time)
    Button endTimeButton;
    @BindView(R.id.button_begin_date)
    Button startDateButton;
    @BindView(R.id.button_end_date)
    Button endDateButton;
    @BindView(R.id.button_add_friends)
    Button addFriendsButton;
    @BindView(R.id.check_dynamic)
    Switch checkSwitch;

    private String extraInfoString;
    private NumberPicker numHours, minMember;
    private int step = 0;
    private int dynamic = 0;
    Boolean bBegin = false, bEnd = false, bDate = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting2);
        ButterKnife.bind(this);
        start = true;
        extraInfoString = "";

        //sportartID
        Bundle b = getIntent().getExtras();
        if (b != null) {
            sportart_ID = b.getInt("sportID");
        }

        formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        datetime = new DateTime();
        startTime = new MutableDateTime();
        endTime = new MutableDateTime();

        //SearchView
        if (sportart_ID == 8008) {
            editTextChooseActivity.setOnQueryTextListener(this);
            editTextChooseActivity.setIconifiedByDefault(false);
            editTextChooseActivity.setIconified(false);
            editTextChooseActivity.setQueryHint("Was willst du tun?");
            editTextChooseActivity.setVisibility(View.VISIBLE);
            editTextChooseActivity.clearFocus();
            ImageView searchViewIcon = (ImageView) editTextChooseActivity.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
            searchViewIcon.setImageDrawable(null);
        } else {
            TextView activityName = (TextView) findViewById(R.id.activity_name);
            Resources res = getResources();
            String[] array = res.getStringArray(R.array.sportarten);
            activityName.setText(array[sportart_ID]);
        }

        startDateButton.setText(datetime.toString("EE., dd. MMM. yyyy"));
        endDateButton.setText(datetime.toString("EE., dd. MMM. yyyy"));
        startTimeButton.setText(datetime.toString("HH:mm"));
        endTimeButton.setText(datetime.toString("HH:mm"));
        minTimeTextView.setText(minHours + "");
        minPartySizeTextView.setText(minMemberCount + "");

        //startCycle
        timeButtons(startTimeButton);
    }

    @OnCheckedChanged(R.id.check_dynamic)
    public void onCheckedChanged(boolean checked) {
        if (checked) {
            startTimeButton.setText(startTime.toString("HH:" + (startTime.getMinuteOfHour() - startTime.getMinuteOfHour() % 15)));
            endTimeButton.setText(endTime.toString("HH:" + (endTime.getMinuteOfHour() - endTime.getMinuteOfHour() % 15)));
        } else {
            startTimeButton.setText(startTime.toString("HH:mm"));
            endTimeButton.setText(endTime.toString("HH:mm"));
        }
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.button_add_friends)
    void addFriends() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SelectionMode", true);
        bundle.putSerializable("Selection", Selection);
        Intent intent = new Intent(this, SelectorContainer.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.button_min_meeting_time)
    void setMinTime() {
        createNumberPickerDialog("Wie viele Stunden?", minHours, 24, false);
    }

    @OnClick(R.id.button_min_party_size)
    void setMinPartySize() {
        createNumberPickerDialog("Ab wie vielen Leuten?", minMemberCount, Selection.size(), true);
    }

    @OnClick({R.id.button_begin_time, R.id.button_end_time})
    public void timeButtons(View view) {
        switch (view.getId()) {
            case R.id.button_begin_time:
                TimePickerDialog tdp = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        startTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
                        startTime.set(DateTimeFieldType.minuteOfHour(), minute);
                        startTimeButton.setText(startTime.toString("HH:mm"));
                        bBegin = true;
                        if (step == 0) {
                            step++;
                            timeButtons(endTimeButton);
                        }
                    }
                }, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true);
                if (checkSwitch.isChecked()) tdp.setTimeInterval(1, 15);
                tdp.show(getFragmentManager(), "TimePickerDialog");
                break;
            case R.id.button_end_time:
                TimePickerDialog tdp2 = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        endTime.set(DateTimeFieldType.hourOfDay(), hourOfDay);
                        endTime.set(DateTimeFieldType.minuteOfHour(), minute);
                        endTimeButton.setText(endTime.toString("HH:mm"));
                        bEnd = true;
                        if (step == 1) {
                            step++;
                            dateButton(startDateButton);
                        }
                    }
                }, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true);
                if (checkSwitch.isChecked()) tdp2.setTimeInterval(1, 15);
                tdp2.show(getFragmentManager(), "TimePickerDialog");
                break;
        }


    }


    @OnClick({R.id.button_begin_date, R.id.button_end_date})
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
                        bDate = true;
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

    @OnClick(R.id.save_meeting)
    public void createMeeting(View view) {

        if (bBegin && bEnd && bDate && minMemberCount != 0 && minHours != 0) {
            if (startTime.isBefore(endTime)) {
                if (checkSwitch.isChecked()) {
                    dynamic = 1;
                    startTime.setMinuteOfHour((startTime.getMinuteOfHour() - startTime.getMinuteOfHour() % 15));
                    endTime.setMinuteOfHour((endTime.getMinuteOfHour() - endTime.getMinuteOfHour() % 15));
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
                // Toast.makeText(getBaseContext(), "Neues Meeting erstellt", Toast.LENGTH_SHORT).show();
                Toasty.success(getBaseContext(), "Neues Meeting erstellt", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                // Toast.makeText(this, R.string.setTimeWrong, Toast.LENGTH_SHORT).show();
                Toasty.error(this, "Falsche Zeit eingestellt", Toast.LENGTH_SHORT).show();

            }

        } else {
            // Toast.makeText(this, R.string.text_empty_fields, Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Nicht alle Felder ausgefüllt", Toast.LENGTH_SHORT).show();
        }

    }

    private void createNumberPickerDialog(String message, int current, int max, final Boolean partySize) {
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
                    minPartySizeTextView.setText(minMemberCount + "");
                    minPartySizeTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));
                } else {
                    minHours = numberPicker.getValue();
                    minTimeTextView.setText(minHours + "");
                    minTimeTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setView(numberPicker).show();
    }


    public void cancelButton(View v) {
        finish();
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

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        extraInfoString = newText;
        return false;
    }
}