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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class AccountPage extends AppCompatActivity implements UIthread{

    @BindView(R.id.email_accountpage)
    TextView emailTV;
    @BindView(R.id.username_accountpage)
    TextView usernameTV;
    @BindView(R.id.accountpage_pp)
    CircleImageView circleImageView;
    @BindView(R.id.friend_number)
    TextView friendcount;
    @BindView(R.id.group_memberships_number)
    TextView groupcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        SharedPreferences sharedPrefs = getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email","");
        emailTV.setText(email);


        String params[]= {"IndexAccounts.php","function","getAccountInfo","email",email};
        Database db = new Database(this,getApplicationContext());
        db.execute(params);



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
        usernameTV.setText(accountData.get(0).username);
        groupcount.setText(accountData.get(0).groupcount+"");
        friendcount.setText(accountData.get(0).friendcount+"");
        Picasso.with(this)
                .load("http://sportsm8.bplaced.net" + accountData.get(0).PPpath)
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                // .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(circleImageView);
    }
}
