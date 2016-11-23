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
    public String username;
    public String email;
    public String MeetingID,endTime,startTime;



    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getMeetingID() {
        return MeetingID;
    }

    public void setMeetingID(String meetingID) {
        MeetingID = meetingID;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
