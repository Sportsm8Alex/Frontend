package com.android.brogrammers.sportsm8.databaseConnection;

import java.io.Serializable;

/**
 * Created by alex on 10/30/2016.
 */


public class Information implements Serializable {

    //userInformation
    public String email, PPpath,username,friend;
    public int friendcount,groupcount;

    //meetingInformation
    public int MeetingID,confirmed=0,minParticipants,begin,duration,dynamic;
    public String startTime, endTime,status, meetingActivity;
    public int[] timeArray=new int[24];
    public Boolean meetingIsGood=false;

    //GroupInformation
    public String GroupID, GroupName;

    //SportInformation
    public String team, sportname, sportID;

    //Help Variables
    public Boolean selected = false;
    public int success;


    public String getUsername() {
        return username;
    }

    public int getConfirmed(){return confirmed;}

    public void setUsername(String username) {
        this.username = username;
    }
}
