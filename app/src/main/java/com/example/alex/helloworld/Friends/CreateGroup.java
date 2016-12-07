package com.example.alex.helloworld.Friends;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Korbi on 02.11.2016.
 */

public class CreateGroup extends Activity implements TextWatcher, UIthread {

    private ArrayList<Information> groupmembers;
    private String groupname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * .8), (int) (width * .8));
        //Declaration Views
        EditText groupname = (EditText) findViewById(R.id.editText_new_group);
        //Declaration Variables
        Bundle bundle = getIntent().getExtras();
        groupmembers = (ArrayList<Information>) bundle.getSerializable("GroupList");
        //Init Listeners
        groupname.addTextChangedListener(this);
        groupname.setSingleLine(true);
    }


    public void onClick(View view) {
        ArrayList<String> paramsArrayList = new ArrayList<>(
                Arrays.asList("IndexGroups.php", "function", "newGroup", "groupName", groupname)
        );
        for (int i = 0; i < groupmembers.size(); i++) {
            if (groupmembers.get(i).selected) {
                paramsArrayList.add("member" + i);
                paramsArrayList.add(groupmembers.get(i).email);
            }
        }
        String[] params = new String[paramsArrayList.size()];
        params = paramsArrayList.toArray(params);

        Database db = new Database(this, getBaseContext());
        db.execute(params);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        groupname = charSequence.toString();
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
}
