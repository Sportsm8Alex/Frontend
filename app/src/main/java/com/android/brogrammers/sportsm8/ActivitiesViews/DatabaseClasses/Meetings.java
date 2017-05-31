package com.android.brogrammers.sportsm8.ActivitiesViews.DatabaseClasses;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Korbi on 23.05.2017.
 */

public class Meetings {
    public int confirmed, minParticipants, duration, dynamic, begin, status, meetingActivity;
    public Date startTime, endTime;
    public ArrayList<Integer> timeArray;
    public Boolean meetingIsGood;

    public Meetings(int minParticipants, int dynamic, int meetingActivity,Date startTime,Date endTime) {
        this.minParticipants = minParticipants;
        this.dynamic = dynamic;
        this.meetingActivity = meetingActivity;
        this.startTime = startTime;
        this.endTime = endTime;
        confirmed = 0;
        duration = 0;
        status = 0;
       timeArray= new ArrayList<>();
        for (int i=0;i<24;i++){
            timeArray.add(0);
        }
        meetingIsGood = false;
    }
}
