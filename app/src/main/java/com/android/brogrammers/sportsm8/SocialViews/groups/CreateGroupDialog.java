package com.android.brogrammers.sportsm8.SocialViews.groups;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.Information;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Korbi on 19.12.2016.
 */

public class CreateGroupDialog extends android.support.v4.app.DialogFragment implements TextWatcher, UIthread, View.OnClickListener {
    private ArrayList<Information> groupmembers;
    private String groupname;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_group, null);
        builder.setView(view);
        EditText groupname = (EditText) view.findViewById(R.id.editText_new_group);
        view.findViewById(R.id.button_create_group).setOnClickListener(this);
        //Declaration Variables
        Bundle bundle = getArguments();
        groupmembers = (ArrayList<Information>) bundle.getSerializable("GroupList");
        //Init Listeners
        groupname.addTextChangedListener(this);
        groupname.setSingleLine(true);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        String email = sharedPrefs.getString("email", "");
        ArrayList<String> paramsArrayList = new ArrayList<>(
                Arrays.asList("IndexGroups.php", "function", "newGroup", "groupName", groupname, "member", email)
        );
        for (int i = 0; i < groupmembers.size(); i++) {
            paramsArrayList.add("member" + i);
            paramsArrayList.add(groupmembers.get(i).email);
        }
        String[] params = new String[paramsArrayList.size()];
        params = paramsArrayList.toArray(params);

        Database db = new Database(this, getContext());
        db.execute(params);
        dismiss();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        groupname = charSequence.toString();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
