package com.android.brogrammers.sportsm8.DataBaseConnection.DatabaseClasses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;



import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by Korbi on 03.06.2017.
 */

public class Meeting extends BaseObservable implements Parcelable {

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
    public float longitude, latitude;
    private String mytime;

    public Meeting(int meetingID, int minParticipants) {
        super();
        MeetingID = meetingID;
        this.minParticipants = minParticipants;
    }


    protected Meeting(Parcel in) {
        MeetingID = in.readInt();
        confirmed = in.readInt();
        minParticipants = in.readInt();
        begin = in.readInt();
        duration = in.readInt();
        dynamic = in.readInt();
        sportID = in.readInt();
        status = in.readInt();
        meetingActivity = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        mystartTime = in.readString();
        myendTime = in.readString();
        day = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        mytime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(MeetingID);
        dest.writeInt(confirmed);
        dest.writeInt(minParticipants);
        dest.writeInt(begin);
        dest.writeInt(duration);
        dest.writeInt(dynamic);
        dest.writeInt(sportID);
        dest.writeInt(status);
        dest.writeString(meetingActivity);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(mystartTime);
        dest.writeString(myendTime);
        dest.writeString(day);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        dest.writeString(mytime);
        dest.writeLong(dtStartTime.getMillis());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

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

