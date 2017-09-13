package com.android.brogrammers.sportsm8.DataBaseConnection.DatabaseClasses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.TextView;



import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by Korbi on 03.06.2017.
 */

public class Meeting extends BaseObservable implements Serializable {

    public int MeetingID;


    private int confirmed = 0;
    public int minParticipants;
    public int begin;
    public int duration;
    public int dynamic;
    public int sportID;
    public int status;
    public String meetingActivity;
    private String startTime;
    private String endTime;
    private String mystartTime;
    private String myendTime;
    private String day;
    public int[] timeArray = new int[24];
    public boolean meetingIsGood;
    public float longitude, latitude;
    private String mytime;

    public Meeting(int meetingID, int minParticipants) {
        MeetingID = meetingID;
        this.minParticipants = minParticipants;
    }

    public Meeting(int meetingID) {
        MeetingID = meetingID;
    }

    public DateTime getStartDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(startTime);
    }

    public DateTime getEndDateTime() {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(endTime);
    }

    public String getDay() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(startTime).toString("yyyy-MM-dd");
    }


    public String getStartTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(startTime).toString("HH:mm");
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;

    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(endTime).toString("HH:mm");
    }


    public void setStartDateTime(DateTime dateTime) {
        startTime = dateTime.toString("YYYY-MM-dd HH:mm:ss");
    }

    public void setEndStartDateTime(DateTime dateTime) {
        endTime = dateTime.toString("YYYY-MM-dd HH:mm:ss");
    }

    @BindingAdapter({"date", "format"})
    public static void setDate(TextView view, String time, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(time);
        view.setText(dateTime.toString(format));
    }

    public String getMytime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        if(myendTime!=null) {
            if (!myendTime.toString().equals("0000-00-00 00:00:00"))
                return formatter.parseDateTime(mystartTime).toString("HH:mm - ") + formatter.parseDateTime(myendTime).toString("HH:mm");
            else return "";
        }else return "";
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
       // notifyPropertyChanged(BR.meeting);
    }

    @Bindable
    public int getConfirmed() {
        return confirmed;
    }
}

