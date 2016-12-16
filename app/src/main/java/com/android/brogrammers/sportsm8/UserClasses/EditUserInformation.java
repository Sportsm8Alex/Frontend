package com.android.brogrammers.sportsm8.UserClasses;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.android.brogrammers.sportsm8.R;

/**
 * Created by agemcipe on 30.11.16.
 */

public class EditUserInformation extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_edit_user_information, null));
        return builder.create();
    }

}