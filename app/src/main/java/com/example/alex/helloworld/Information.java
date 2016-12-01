package com.example.alex.helloworld;

import java.io.Serializable;

/**
 * Created by alex on 10/30/2016.
 */

public class Information implements Serializable{

    public String start;
    public String stop;
    public String datum;
    public String title;
    public String names;
    public String friend;
    public String username,PPpath;
    public String email,GroupID,GroupName;
    public String MeetingID,endTime,startTime;
    public Boolean selected=false;
    public String team,sportname,sportID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
