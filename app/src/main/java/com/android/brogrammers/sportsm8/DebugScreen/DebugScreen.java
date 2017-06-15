package com.android.brogrammers.sportsm8.DebugScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class DebugScreen extends AppCompatActivity implements UIthread {

    @BindView(R.id.email_switcher)
    Button emailSwitch;
    int i;
    int newmeetings = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        emailSwitch.setText(sharedPrefs.getString("email", ""));
    }


    @OnClick(R.id.email_switcher)
    public void onClick(View view) {
        String[] emails = getResources().getStringArray(R.array.emails);
        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email", emails[i]);
        editor.apply();
        Button btn = (Button) findViewById(R.id.email_switcher);
        btn.setText(emails[i]);
        i++;
        i = i % emails.length;
    }

    @OnClick(R.id.random_meetings)
    public void createRandomMeetings() {
        Database db = new Database(this, getBaseContext());
        SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        MutableDateTime randomStartTime = new MutableDateTime();
        MutableDateTime randomEndTime;
        Random random = new Random();
        int randInt = random.nextInt(7);
        randomStartTime.addDays(randInt);
        randomStartTime.addHours(randInt);
        randomEndTime = randomStartTime;
        randomEndTime.addHours(2);
        String[] params = {"IndexMeetings.php", "function", "newMeeting", "startTime", formatter.print(randomStartTime), "endTime", formatter.print(randomEndTime), "minPar", 2 + "", "member", email, "activity", "TEST_MEETING", "sportID", "" + randInt, "dynamic", 0 + ""};
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        while (newmeetings < 10){
            createRandomMeetings();
            newmeetings++;
        }
    }
}
