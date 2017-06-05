package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by Korbi on 03.06.2017.
 */

public class Meeting implements Serializable {

    public int MeetingID, confirmed = 0, minParticipants, begin, duration, dynamic,sportID;
    public String startTime, endTime, status, meetingActivity;
    public int[] timeArray = new int[24];
    public Boolean meetingIsGood = false;

    public DateTime getStartDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(startTime);
    }

    public DateTime getEndDateTime() {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(endTime);
    }
}
