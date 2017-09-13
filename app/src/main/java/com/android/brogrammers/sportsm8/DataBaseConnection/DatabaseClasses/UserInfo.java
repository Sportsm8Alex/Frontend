package com.android.brogrammers.sportsm8.DataBaseConnection.DatabaseClasses;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by Korbi on 05.06.2017.
 */

public class UserInfo implements Serializable {
    public String email, PPpath, username, friend, mystartTime, myendTime;
    public int friendcount, groupcount, confirmed,begin,duration;
    public Boolean selected = false;

    public UserInfo() {
    }

    public UserInfo(String email) {
        this.email = email;
    }

    public DateTime getStartDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(mystartTime);
    }

    public DateTime getEndDateTime() {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(myendTime);
    }

    public void setMystartTime(String mystartTime) {
        this.mystartTime = mystartTime;
    }

    public void setMyendTime(String myendTime) {
        this.myendTime = myendTime;
    }
}
