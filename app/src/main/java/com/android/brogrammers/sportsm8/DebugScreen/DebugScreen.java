package com.android.brogrammers.sportsm8.DebugScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.android.brogrammers.sportsm8.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DebugScreen extends AppCompatActivity {

    @BindView(R.id.email_switcher)
            Button emailSwitch;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        emailSwitch.setText(sharedPrefs.getString("email",""));
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

}
