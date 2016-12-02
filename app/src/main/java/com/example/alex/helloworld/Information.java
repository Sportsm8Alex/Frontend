package com.example.alex.helloworld;

import java.io.Serializable;

/**
 * Created by alex on 10/30/2016.
 */


public class Information implements Serializable{

    public String start;
    public String stop;
    public String title;
    public int success;
    public String MeetingID,startTime,endTime;
    public String username, password;
    public String names;
    public String friend;
    public String PPpath;
    public String email,GroupID,GroupName;
    public Boolean selected=false;
    public String team,sportname,sportID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
