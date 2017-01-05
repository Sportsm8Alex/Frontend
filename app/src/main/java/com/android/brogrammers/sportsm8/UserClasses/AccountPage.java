package com.android.brogrammers.sportsm8.UserClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class AccountPage extends AppCompatActivity implements View.OnClickListener,UIthread{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initiating the buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);


        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email","");
        TextView emailTV = (TextView)findViewById(R.id.email_accountpage);
        emailTV.setText(email);


        String params[]= {"IndexAccounts.php","function","getAccountInfo","email",email};
        Database db = new Database(this,getApplicationContext());
        db.execute(params);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
           /* case R.id.edit_userinformation:
                //start new alert dialog
                System.out.println("edit user information");
                FragmentManager fm = getSupportFragmentManager();
                EditUserInformation editUserInformationDialog = new EditUserInformation();
                editUserInformationDialog.show(fm, "editUserInformation");
                break;*/
        }
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        ArrayList<Information> accountData = new ArrayList<>();
        SharedPreferences sharedPrefs = getSharedPreferences("IndexAccounts", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexAccountsgetAccountInfoJSON", "");
        try {
            accountData = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        TextView usernameTV = (TextView) findViewById(R.id.username_accountpage);
        usernameTV.setText(accountData.get(0).username);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.accountpage_pp);
        Picasso.with(this)
                .load("http://sportsm8.bplaced.net" + accountData.get(0).PPpath)
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                // .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(circleImageView);
    }
}
