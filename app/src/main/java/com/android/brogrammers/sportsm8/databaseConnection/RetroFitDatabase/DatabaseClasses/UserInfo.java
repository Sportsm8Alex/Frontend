package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses;

import java.io.Serializable;

/**
 * Created by Korbi on 05.06.2017.
 */

public class UserInfo implements Serializable {
    public String email, PPpath, username, friend;
    public int friendcount, groupcount, confirmed,begin,duration;
    public Boolean selected = false;

    public UserInfo() {
    }

    public UserInfo(String email) {
        this.email = email;
    }
}
